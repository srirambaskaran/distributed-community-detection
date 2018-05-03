from pyspark import SparkContext,SparkConf
import os
import sys
from collections import Counter

def custom_reduce(x, y):
	if x is None and y is None:return []
	if x is None: return y
	if y is None: return x
	return x + y

def count(genre_list):
	counts = Countet(genre_list)
	return counts.items()


def all_pairs(users):
	return [((x,y),1) for x in users for y in users if x < y]

conf = SparkConf().setAppName("Create graph")
sc   = SparkContext(conf=conf)

if len(sys.argv) < 4:
	print "Usage: <input-file:string> <output-folder:string> <movies-info-file:string> <user-info-output:string> <threshold:double>"
	raise SystemExit

inputFile = sys.argv[1]
outputFile = sys.argv[2]
moviesFile = sys.argv[3]
THRESHOLD = float(sys.argv[4])

# Creating a RDD of (movie_id, user_id) for all ratings > THRESHOLD
sc.broadcast(THRESHOLD)
movies = sc.textFile(moviesFile) \
	.map(lambda line: line.split(",")) \
	.map(lambda array: (int(array[0]), (array[1], array[2])))

ratingRecord = sc.textFile(inputFile) \
    .map(lambda line: line.split(",")) \
    .map(lambda array: (int(array[0]), int(array[1]), float(array[2]))) \
    .filter(lambda (movie, user, rating): rating >= THRESHOLD)

moviesJoined = ratingRecord \
	.map(lambda (movie, user, rating): (movie, (user, rating))) \
	.leftOuterJoin(movies) \
	.map(lambda (movie, ((user, rating), (name, genre))): 
		(user, genre.split("|"))) \
	.groupByKey() \
	.map(lambda (user, genreList): (user, map( count, reduce(custom_reduce, genreList))))

ratingRecord = ratingRecord.map(lambda (movie, user, rating): (movie, user))


 

# Grouping by movies and picking users
coratedUserList = ratingRecord.groupByKey().values()

# Creating an edge between all pairs of users, set weight to number of corated movies.
graphEdges = coratedUserList.flatMap(all_pairs) \
			.reduceByKey(lambda x,y: x+y) \
			.map(lambda ((user1, user2), weight): str(user1)+","+str(user2)+","+str(weight))

# Write into file
graphEdges.saveAsTextFile(outputFile)