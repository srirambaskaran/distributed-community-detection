from pyspark import SparkContext,SparkConf
import os
import sys
import csv
from collections import Counter

conf = SparkConf().setAppName("Create graph")
sc   = SparkContext(conf=conf)

if len(sys.argv) < 4:
    print "Usage: <input-file:string> <output-folder:string> <movies-info-file:string> <user-info-output:string> <threshold:double>"
    raise SystemExit

moviesFile = sys.argv[1]
userInfoFolder = sys.argv[2]
inputFile = sys.argv[3]
THRESHOLD = float(sys.argv[4])

movies = sc.parallelize(lines) \
    .map(lambda array: (int(array[0]), (array[1], array[2].strip())))

ratingRecord = sc.textFile(inputFile , minPartitions=128) \
    .map(lambda line: line.split(",")) \
    .map(lambda array: (int(array[0]), int(array[1]), float(array[2]))) \
    .filter(lambda (user, movie, rating): rating >= THRESHOLD) \

moviesJoined = ratingRecord \
    .map(lambda (user, movie, rating): (movie, (user, rating))) \
    .join(movies) \
    .map(lambda (movie, ((user, rating), (name, genre))): (user, []) if genre is None else (user, genre.split("|"))) \
    .groupByKey() \
    .map(lambda (user, genreList): (user, list(genreList))) \
    .map(lambda (user, genreList): (user, len(genreList), Counter(reduce(custom_reduce, genreList)).items())) \
    .map(lambda (user, numMovies, genreCounts): str(user)+","+str(numMovies)+","+("|".join(map(lambda x: x[0]+":"+str(x[1]), genreCounts))))

sc.textFile(movieFile) \
    .map(lambda line: line.split(","))
    .map(lambda array: int(array[0].strip()), (array[1:len(array)-1].strip(), array[len(array)-1].strip()))
