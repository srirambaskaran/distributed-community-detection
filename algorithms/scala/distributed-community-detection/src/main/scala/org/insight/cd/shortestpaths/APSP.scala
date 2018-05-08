package org.insight.cd.shortestpaths

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.graphx._
import org.insight.cd.utils.GraphXReader

object APSP {

    class ShortestPaths(val paths: Map[VertexId, (Set[VertexId], Int)]) extends Serializable{
        override def toString: String = paths.toString()
    }

    class Packet(val start: VertexId, val from: Set[VertexId], val cost: Int) extends Serializable{
        override def toString: String = "start="+start+", from="+from+", cost="+cost
    }

    class Message(val packets: List[Packet]) extends Serializable{
        override def toString: String = packets.mkString(", ")
    }

    /**
      * Main Method to calculate all-pair-shortest-path
      * @param graph
      * @return
      */

    def calculateAPSP(graph: Graph[Int, Int]): Graph[ShortestPaths, Int] ={

        val initialGraph = graph.mapVertices((x, _) => new ShortestPaths(Map(x  -> (Set[VertexId](), 0)) )).cache()

        println("Initial Graph created")

        def vertexUpdate(id: VertexId, vertex: ShortestPaths, message: Message): ShortestPaths = {
            val messageMap = message.packets.map(x => (x.start, (x.from, x.cost))).toMap
            val currentPaths = vertex.paths
            if (messageMap.nonEmpty){
                val updates = messageMap.map{
                    case (vertexId, (newFrom, newCost)) =>
                        if(currentPaths.contains(vertexId)){
                            val currentCost = currentPaths(vertexId)._2
                            val currentFrom = currentPaths(vertexId)._1
                            if(currentCost > newCost)
                                (vertexId, (newFrom, newCost))
                            else if(currentCost == newCost)
                                (vertexId, (currentFrom ++ newFrom, newCost))
                            else
                                (vertexId, (currentFrom, currentCost))
                        } else {
                            (vertexId, (newFrom, newCost))
                        }
                }
                new ShortestPaths(updates)
            } else {
                vertex
            }


        }

        /**
          * Return the actual stored cost in a destination vertex.
          * It is mainly called from sendMessage method.
          *
          * @param destPaths
          * @param vertexId
          * @return
          */

        def getCost(destPaths: Map[VertexId, (Set[VertexId], Int)], vertexId: VertexId): Int ={
            if(destPaths.contains(vertexId))
                destPaths(vertexId)._2
            else
                Integer.MAX_VALUE
        }

        def sendMessage(triplet: EdgeTriplet[ShortestPaths, Int]): Iterator[(VertexId, Message)] ={
            val message = triplet.srcAttr.paths
                .filter{
                    case (start, (from, cost)) =>
                        cost + triplet.attr < getCost(triplet.dstAttr.paths, start)
                }
                .map{
                    case (start, (from, cost)) =>
                        new Packet(start, Set(triplet.srcId), cost + triplet.attr)
                }.toList

            if(message.nonEmpty){
                Iterator((triplet.dstId, new Message(message)))
            }else{
                Iterator.empty
            }
        }

        def mergeMessageList(message1: (VertexId, (Set[VertexId], Int)), message2: (VertexId, (Set[VertexId], Int))): (VertexId, (Set[VertexId], Int)) ={
            if (message1._2._2 == message2._2._2)
                (message1._1, (message1._2._1 ++ message2._2._1, message1._2._2))
            else if (message1._2._2 < message2._2._2)
                message1
            else
                message2
        }

        def mergeMessages(forwardMessages1: Message, forwardMessages2: Message): Message ={

            val allMessages : List[(VertexId, (Set[VertexId], Int))] = (forwardMessages1.packets ++ forwardMessages2.packets).map(x => (x.start, (x.from, x.cost)))
            val messages = allMessages
                .groupBy(_._1).values
                .map(x => x.reduce(mergeMessageList))
                .map(x => new Packet(x._1, x._2._1, x._2._2)).toList
            new Message(messages)
        }

        val shortestPathsFilled = initialGraph.pregel(initialMsg = new Message(List()), activeDirection = EdgeDirection.Either)(
            vertexUpdate,
            sendMessage,
            mergeMessages
        )

        shortestPathsFilled
    }

    def main(args: Array[String]): Unit = {

        val partitionedFile = args{0}
        val weightThreshold = args{1}.toInt

        val conf : SparkConf = new SparkConf().setAppName("All pair shortest path")
        val sc : SparkContext = new SparkContext(conf)

        val graph = new GraphXReader().getGraph(partitionedFile, sc, weightThreshold).partitionBy(PartitionStrategy.EdgePartition2D)

        val shortestPaths = calculateAPSP(graph)
        val fewVertices = shortestPaths.vertices.filter{
            case (vertedId, vertexInfo) =>
                vertexInfo.paths.nonEmpty
        }.take(5)

        fewVertices.foreach(vertex => println(vertex._1, vertex._2.paths))


    }
}
