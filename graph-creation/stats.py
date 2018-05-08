from pyspark import SparkContext,SparkConf
import sys


conf = SparkConf().setAppName("Get stats about graph")
sc   = SparkContext(conf=conf)

if len(sys.argv) < 1:
	print "Usage: <input-file:string>"
	raise SystemExit

inputFolder = sys.argv[1]

lines = sc.textFile("s3a://graph-cd-bucket/edges-gt-4_weighted.csv/part-*")

print str(lines.count())

