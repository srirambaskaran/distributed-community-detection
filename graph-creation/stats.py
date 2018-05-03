from pyspark import SparkContext,SparkConf
import sys


conf = SparkConf().setAppName("Get stats about graph")
sc   = SparkContext(conf=conf)

if len(sys.argv) < 2:
	print "Usage: <input-file:string>"
	raise SystemExit

inputFolder = sys.argv[1]

lines = sc.textFile(inputFolder)

print str(lines.count())

