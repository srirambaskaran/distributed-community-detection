package org.insight.cd.utils

import java.sql.{Connection, DriverManager, PreparedStatement}

import java.util.Properties

import org.apache.spark.graphx.Graph
import org.insight.cd.algorithms.VertexData


object PersistGraph {



    def storeGraph(graph: Graph[VertexData, Long], properties: Properties): Unit = {

        val host = properties.getProperty("mysql.host")
        val port = properties.getProperty("mysql.port")
        val user = properties.getProperty("mysql.user")
        val pass = properties.getProperty("mysql.password")
        val db = properties.getProperty("mysql.defaultDB")


        Class.forName("com.mysql.cj.jdbc.Driver").newInstance
        val con: Connection = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+db, user, pass)
        con.setAutoCommit(false)

        val nodesRDD = graph.vertices.map {
            case (vertexId, data) =>
                (vertexId, data.community, data.internalTotalEdgeWeight, data.degree)
        }

        val communities = nodesRDD.map{
            case (_, communityId, internalWeight, degree) =>
                (communityId.toInt, internalWeight.toInt, degree.toInt)
        }.distinct().collect()

        val communityInsert: PreparedStatement = con.prepareStatement("INSERT IGNORE INTO Community VALUES (?,?,?)")

        communities.foreach{
            case (communityId, internalWeight, degree) =>
                communityInsert.setInt(1, communityId)
                communityInsert.setInt(2, internalWeight)
                communityInsert.setInt(3, degree)
                communityInsert.addBatch()
        }

        communityInsert.executeLargeBatch()
        con.commit()

        val nodes = nodesRDD.collect()

        val nodeInsert: PreparedStatement = con.prepareStatement("INSERT INTO Node VALUES (?,?) ON DUPLICATE KEY UPDATE community_id=?")

        nodes.foreach {
            case (vertexId, communityId, _, _) =>
                nodeInsert.setInt(1, vertexId.toInt)
                nodeInsert.setInt(2, communityId.toInt)
                nodeInsert.setInt(3, communityId.toInt)
                nodeInsert.addBatch()
        }

        nodeInsert.executeLargeBatch()
        con.commit()

        val edges = graph.triplets.map {
            triplet =>
                (triplet.srcId.toInt, triplet.dstId.toInt)
        }.collect()

        val edgeInsert: PreparedStatement = con.prepareStatement("INSERT IGNORE INTO Edge VALUES (?,?)")



        edges.foreach{
            case (srcId, dstId) =>
                edgeInsert.setInt(1, srcId)
                edgeInsert.setInt(2, dstId)
                edgeInsert.addBatch()
        }

        edgeInsert.executeLargeBatch()
        con.commit()
        con.close()
        println("Inserted everything")

    }
}
