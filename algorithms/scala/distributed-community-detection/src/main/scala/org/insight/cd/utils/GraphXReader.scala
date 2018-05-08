package org.insight.cd.utils

import org.apache.spark.SparkContext
import org.apache.spark.graphx.{Edge, Graph}

class GraphXReader {

    def getGraph(partitionedFile: String, sc: SparkContext, weightThreshold: Int): Graph[Int, Int] ={
        sc.broadcast(weightThreshold)
        val edges =  sc.textFile(partitionedFile)
            .map(line => line.split(","))
            .map(array => (array{0}.toInt, array{1}.toInt, array{2}.toInt))
            .filter{
                case(vertex1: Int, vertex2: Int, weight: Int) =>
                    weight > weightThreshold
            }
            .map{
                case(vertex1: Int, vertex2: Int, weight: Int) =>
                    Edge(vertex1, vertex2, weight)
            }

        Graph.fromEdges(edges, defaultValue = -1)
    }

}
