package org.insight.cd.algorithms

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.graphx._
import org.apache.spark.{SparkConf, SparkContext}
import org.insight.cd.utils.GraphXReader

object CommunityDetection {

    def getCommunities(graph: Graph[VertedData, Long], mBroadcast: Broadcast[Long]): Graph[VertedData, Long] = {
        val m = mBroadcast.value

        val modularityChanges = graph.mapTriplets{
            triplet =>
                val srcAttr = triplet.srcAttr
                val destAttr = triplet.dstAttr

                val edgeWeight = triplet.attr

                val mod1 = ( destAttr.internalTotalEdgeWeight.toDouble + edgeWeight.toDouble ) / (2 * m)
                val mod2 = Math.pow(( destAttr.degree.toDouble + srcAttr.degree.toDouble ) / (2 * m), 2)
                val mod31 = destAttr.internalTotalEdgeWeight.toDouble / (2*m)
                val mod32 =  Math.pow(destAttr.degree.toDouble / (2*m), 2)
                val mod33 =  Math.pow(srcAttr.degree.toDouble / (2*m), 2)

                val q = (mod1 - mod2) - (mod31 - mod32 - mod33)
                (q, edgeWeight, triplet.dstAttr.internalTotalEdgeWeight, triplet.dstAttr.degree)
        }

        val communityAssignments = modularityChanges.aggregateMessages[(VertexId, (Double, Long, Long, Long))](
            (context) =>
                context.sendToSrc((context.dstId, (context.attr._1, context.attr._2, context.attr._3, context.attr._4)))
            ,(msg1, msg2) =>
                if(msg1._2._1 > msg2._2._1) msg1 else msg2
        )

        val updatedGraph = graph.outerJoinVertices[(VertexId, (Double, Long, Long, Long)), VertedData](communityAssignments){
            case (vertexId, currentData, communityUpdate) =>
                if(communityUpdate.isEmpty){
                    currentData
                } else {
                    val otherCommunityId = communityUpdate.get._1
                    if(currentData.community == otherCommunityId){
                        currentData
                    } else{
                        val internalTotalWeight = (currentData.internalTotalEdgeWeight
                                                        + communityUpdate.get._2._3
                                                        + communityUpdate.get._2._2)


                        val degree = currentData.degree + communityUpdate.get._2._4 - 2 // removing degree contribution between these vertices
                        val newData = new VertedData(otherCommunityId, internalTotalWeight, degree, true)

                        newData
                    }
                }
        }


        updatedGraph
    }

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setAppName("Louvain")
        val sc = new SparkContext(conf)
        sc.setLogLevel("ERROR")

        val file = args {0}
        val maxIterations = args{1}.toInt

        val graph = GraphXReader.createLouvainGraph(file, sc)
        val m = graph.edges.count()
        val mBroadcast = sc.broadcast(m)

        var communities = graph
        var i = 0
        var changed = true
        while(changed && i < maxIterations){

            communities = communities.mapVertices((_, data) => new VertedData(data.community, data.internalTotalEdgeWeight, data.degree, false))

            var oldGraph = communities
            communities = getCommunities(oldGraph, mBroadcast)

            oldGraph.unpersist(blocking =  false)
            i+=1

            changed = communities.vertices.map(data => data._2.changed).reduce(_ || _)

            println("Iteration: "+i + " over. Running again.")
        }

        communities.vertices.map(x => (x._2.community, 1)).reduceByKey(_+_).sortBy(x => x._2, ascending = false).take(10).foreach(println)
    }
}
