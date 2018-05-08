package org.insight.cd.utils

import org.apache.spark.{SparkConf, SparkContext}

import org.neo4j.spark._
import scala.collection.JavaConverters._


object PersistGraph {
    def main(args: Array[String]): Unit = {
        val graphFile = args{0}
        val moviesInfo = args{1}

        val conf : SparkConf = new SparkConf().setAppName("All pair shortest path")
        conf.set("spark.neo4j.bolt.url","bolt://34.211.3.246:7687")
        conf.set("spark.neo4j.bolt.user","neo4j")
        conf.set("spark.neo4j.bolt.password","I@mR00t")

        val sc : SparkContext = new SparkContext(conf)

        val reader = new GraphXReader()
        val graph = reader.getGraph(graphFile, sc, 30)


        val movies = sc.textFile(moviesInfo)
            .map(line => line.split(","))
            .map(array => (array{0}.toLong, List(Map("vertex_id"-> array{0}.toLong, "title"->array.slice(1, array.length - 1).mkString(","), "genre"->array{array.length-1}).asJava).asJava)).collect().toMap

        val propertiesIngraph = graph.mapVertices((vertexId, _) => movies(vertexId))

        Neo4jGraph.saveGraph(sc, propertiesIngraph, nodeProp = "movieInfo")

    }
}
