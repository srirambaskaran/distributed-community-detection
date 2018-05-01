from pyspark import SparkContext,SparkConf
import os

def all_pairs(users):
	return [(x,y) for x in users for y in users if x != y]

conf = SparkConf().setAppName("Create graph")
sc   = SparkContext(conf=conf)

inputFile = "s3a://graph-cd-bucket/ratings.csv"
outputFile = "s3a://graph-cd-bucket/edges"
THRESHOLD = 4.0

# Creating a RDD of (movie_id, user_id) for all ratings > THRESHOLD
sc.broadcast(THRESHOLD)
ratingRecord = sc.textFile(inputFile) \
    .map(lambda line: line.split("\t")) \
    .map(lambda array: (int(array[0]), int(array[1]), float(array[2]))) \
    .filter(lambda (movie, user, rating): rating > THRESHOLD) \
    .map(lambda (movie, user, rating): (movie, user))

# Grouping by movies and picking users
coratedUserList = ratingRecord.groupByKey().values()

# Creating an edge between all pairs of users.
graphEdges = coratedUserList.flatMap(all_pairs)

# Write into file
graphEdges.saveAsTextFile(outputFile)