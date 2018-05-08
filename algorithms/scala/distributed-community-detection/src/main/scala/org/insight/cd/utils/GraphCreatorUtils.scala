package org.insight.cd.utils

import org.apache.spark.{SparkConf, SparkContext}


object GraphCreatorUtils {

    // Ingestion for data
    def main(args: Array[String]): Unit = {
        val inputFile: String =  args{0}
        val outputFile: String = args{1}
        val THRESHOLD: Double = args{2}.toDouble

        val conf : SparkConf = new SparkConf().setAppName("Create Graph")
        val sc : SparkContext = new SparkContext(conf)

        //Creating a RDD of (movie_id, user_id) for all ratings > 4
        val ratingRecord = sc.textFile(inputFile)
            .map(line => line.split(","))
            .map(array => (array{0}.toInt, array{1}.toInt, array{2}.toDouble))
            .filter{
                case (movie, user, rating) =>
                    rating >= THRESHOLD
            }
            .map{
                case (movie, user, rating) => (movie, user)
            }

        //Grouping by movies
        val coratedUserList = ratingRecord.groupByKey().values.map(_.toList)

        val graphEdges = coratedUserList.flatMap{
            users =>
                for(x <- users; y <- users) yield (x, y)
        }.filter(x => x._1 < x._2)
            .map(edge => (edge, 1))
            .reduceByKey(_+_)
            .map{
                case ( (user1, user2), corated) =>
                    user1.toString+","+user2.toString+","+corated.toString
            }


        // Write into file
        graphEdges.saveAsTextFile(outputFile)
    }


}
