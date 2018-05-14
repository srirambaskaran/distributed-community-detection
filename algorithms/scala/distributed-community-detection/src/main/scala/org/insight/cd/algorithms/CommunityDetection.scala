package org.insight.cd.algorithms

import java.io.FileReader
import java.util.Properties

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.graphx._
import org.apache.spark.{SparkConf, SparkContext}
import org.insight.cd.utils.{GraphXReader, PersistGraph}

object CommunityDetection {

    /**
      * Runs one iteration of Louvain algorithm to identifiy communities.
      * Triplet:
      *     Defines a (src)-[attr]->(dst) for an edge in the graph.
      *     It takes the initial ordering of nodes when graph was created.
      *
      * Pesudocode:
      *     1. For each edge triplet, calculate the change in modularity of joining the "src" node with "dst",
      *         Store this information in the edge.
      *     2. Send the change in modularity and pick the best neighbour.
      *     3. Move the node to the community, create a new node with both nodes in them.
      *     4. Repeat 1-3 for all nodes in the graph.
      *
      * @param graph
      *                Graph[VertexData, Long]
      * @param mBroadcast
      *                    Total number of edges in the graph.
      * @return
      */

    def getCommunities(graph: Graph[VertexData, Long], mBroadcast: Broadcast[Long]): Graph[VertexData, Long] = {
        val m = mBroadcast.value

        val modularityChanges = graph.mapTriplets{
            triplet =>
                val srcAttr = triplet.srcAttr
                val destAttr = triplet.dstAttr

                val edgeWeight = triplet.attr

                //5 parts of the formula to calculate change in modularity, refer to the paper.
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

        val updatedGraph = graph.outerJoinVertices[(VertexId, (Double, Long, Long, Long)), VertexData](communityAssignments){
            case (_, currentData, communityUpdate) =>
                if(communityUpdate.isEmpty){
                    currentData
                } else {
                    val otherCommunityId = communityUpdate.get._1
                    if(currentData.community == otherCommunityId){
                        currentData
                    } else {
                        val internalTotalWeight = (currentData.internalTotalEdgeWeight
                                                        + communityUpdate.get._2._3
                                                        + communityUpdate.get._2._2)


                        val degree = currentData.degree + communityUpdate.get._2._4 - 2 // removing degree contribution for edges between these vertices
                        val newData = new VertexData(otherCommunityId, internalTotalWeight, degree, true) //setting changed=true, new node added.

                        newData
                    }
                }
        }


        updatedGraph
    }

    def loadProperites(file: String): Properties = {
        val properties = new Properties()
        properties.load(new FileReader(file))

        properties
    }

    /**
      * Main method. Inputs require a graph file, refer to README for more details on how the graph file should look like.
      * Usage:
      *     --class org.insight.cd.
      *
      *
      * @param args
      *             command line arguments.
      */

    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setAppName("Louvain")
        val sc = new SparkContext(conf)
        sc.setLogLevel("ERROR")

        val properties = loadProperites(args{0})

        val file = properties.getProperty("algorithm.file.inputFile")

        val maxIterations = properties.getProperty("algorithm.maxIterations").toInt

        val graph = GraphXReader.createLouvainGraph(file, sc)
        val m = graph.edges.count()
        val mBroadcast = sc.broadcast(m)

        var communities = graph
        var i = 0
        var changed = true
        while(changed && i < maxIterations){

            communities = communities.mapVertices((_, data) => new VertexData(data.community, data.internalTotalEdgeWeight, data.degree, false))

            val oldGraph = communities
            communities = getCommunities(oldGraph, mBroadcast)

            oldGraph.unpersist(blocking =  false)
            i+=1

            changed = communities.vertices.map(data => data._2.changed).reduce(_ || _)

            println("Iteration: "+i + " over. Running again.")
        }

        if(properties.getProperty("algorithm.persistGraph").equals("true"))
            PersistGraph.storeGraph(communities, properties)
    }
}
