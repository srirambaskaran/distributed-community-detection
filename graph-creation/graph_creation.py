from pyspark import SparkContext,SparkConf
import os
import sys
import csv
from collections import Counter

def custom_reduce(x, y):
	if x is None and y is None:return []
	if x is None: return y
	if y is None: return x
	return x + y

def all_pairs(users):
	return [((str(x)+","+str(y)),1) for x in users for y in users if x < y]

conf = SparkConf().setAppName("Create graph")
sc   = SparkContext(conf=conf)

if len(sys.argv) < 5:
	print "Usage: <input-file:string> <output-folder:string> <movies-info-file:string> <user-info-output:string> <threshold:double>"
	raise SystemExit

inputFile = sys.argv[1]
outputFile = sys.argv[2]
moviesFile = sys.argv[3]
userInfoFolder = sys.argv[4]
THRESHOLD = float(sys.argv[5])

# Creating a RDD of (movie_id, user_id) for all ratings > THRESHOLD
sc.broadcast(THRESHOLD)

reader = csv.reader(open(moviesFile))
lines = [line for line in reader]

movies = sc.parallelize(lines) \
	.map(lambda array: (int(array[0]), (array[1], array[2].strip())))

ratingRecord = sc.textFile(inputFile) \
    .map(lambda line: line.split(",")) \
    .map(lambda array: (int(array[0]), int(array[1]), float(array[2]))) \
    .filter(lambda (user, movie, rating): rating >= THRESHOLD)

moviesJoined = ratingRecord \
	.map(lambda (user, movie, rating): (movie, (user, rating))) \
	.join(movies) \
	.map(lambda (movie, ((user, rating), (name, genre))): (user, []) if genre is None else (user, genre.split("|"))) \
	.groupByKey() \
	.map(lambda (user, genreList): (user, list(genreList))) \
	.map(lambda (user, genreList): (user, len(genreList), Counter(reduce(custom_reduce, genreList)).items())) \
	.map(lambda (user, numMovies, genreCounts): str(user)+","+str(numMovies)+","+("|".join(map(lambda x: x[0]+":"+str(x[1]), genreCounts))))

moviesJoined.saveAsTextFile(userInfoFolder)
del moviesJoined

ratingRecord = ratingRecord.map(lambda (user, movie, rating): (movie, user))



# Grouping by movies and picking users
coratedUserList = ratingRecord.groupByKey().values()



# Creating an edge between all pairs of users, set weight to number of corated movies.
graphEdges = coratedUserList.flatMap(all_pairs) \
			.reduceByKey(lambda x,y: x+y) \
			.map(lambda (users, weight): users+","+str(weight))


# Write into file
graphEdges.saveAsTextFile(outputFile)