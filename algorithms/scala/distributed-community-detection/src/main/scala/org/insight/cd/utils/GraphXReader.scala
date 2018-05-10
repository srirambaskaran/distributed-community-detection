package org.insight.cd.utils

import org.apache.spark.SparkContext
import org.apache.spark.graphx._
import org.insight.cd.algorithms.VertedData

object GraphXReader {

    def createGraph(file: String, sc: SparkContext): Graph[Int, Long] = {

        val edges = sc.textFile(file)
            .filter(line => !line.startsWith("#"))
            .map(line => line.split("\\s+"))
            .map(array => new Edge(array{0}.toInt, array{1}.toInt, 1L))

        Graph.fromEdges(edges, defaultValue = -1)

    }

    def createLouvainGraph(file: String, sc: SparkContext):
    Graph[VertedData, Long] = {
        val graph = createGraph(file, sc)

        val nodeWeights = graph.aggregateMessages(
            (e:EdgeContext[Int,Long,Long]) => {
                e.sendToSrc(e.attr)
                e.sendToDst(e.attr)
            },
            (e1: Long, e2: Long) => e1 + e2
        )

        graph.outerJoinVertices(nodeWeights)((vid, data, degreeOption) => {
            val degree = degreeOption.getOrElse(0L)
            new VertedData(vid, 0L, degree, false)
        }).partitionBy(PartitionStrategy.EdgePartition2D).groupEdges(_ + _)
    }
}
