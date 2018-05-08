package org.insight.cd.modules;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.insight.cd.db.DBConnection;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class InsertAdhocData {
	public static void main(String[] args) throws IOException {
		String edgesFileName = "src/main/resources/edges.txt";
		String communitiesFileName = "src/main/resources/communities.txt";
		
		//Init Connection
		DBConnection con = DBConnection.checkAndCreate();
		Session session  = con.getSession();
		
		// DROP KEYSPACE
		String dropKeyspace = "DROP KEYSPACE GraphSpace";
		session.execute(dropKeyspace);
		
		System.out.println("Dropped existing keyspace");
		
		//Create KEYSPACE
		String createKeySpace = "CREATE KEYSPACE GraphSpace "
				+ "WITH replication = {'class': 'NetworkTopologyStrategy','us-west-2':2}";
		
		session.execute(createKeySpace);
		
		System.out.println("Created new keyspace");
		
		String useKeyspace = "USE GraphSpace";
		session.execute(useKeyspace);
		System.out.println("Using keyspace");
		
		//Create edges table;
		String createEdgesTable = "CREATE TABLE Edges (source INT, destination INT, PRIMARY KEY (source, destination))";
		session.execute(createEdgesTable);
		System.out.println("Created Edges table");
		
		
		//Create communities table
		String createCommunitiesTable = "CREATE TABLE Communities (node INT, community INT, PRIMARY KEY (node))";
		session.execute(createCommunitiesTable);
		
		System.out.println("Created communities table");
		
		// Inserting edges
		BufferedReader reader = new BufferedReader(new FileReader(edgesFileName));
		
		PreparedStatement insertStatement = session.prepare("INSERT INTO Edges (source, destination) VALUES (?,?)");

		BoundStatement bound = new BoundStatement(insertStatement);
		
		String line = "";
		while((line = reader.readLine() ) != null) {
			String[] nodes = line.split(" ");
			int source = Integer.parseInt(nodes[0]);
			int destination = Integer.parseInt(nodes[1]);
			session.execute(bound.bind(source, destination));
		}
		
		reader.close();
		
		System.out.println("Inserting edges");
		
		//Inserting nodes
		reader = new BufferedReader(new FileReader(communitiesFileName));
		insertStatement = session.prepare("INSERT INTO Communities (node, community) VALUES (?,?)");
		bound = new BoundStatement(insertStatement); 
		line = "";
		while((line = reader.readLine() ) != null) {
			String[] nodes = line.split(" ");
			int node = Integer.parseInt(nodes[0]);
			int community = Integer.parseInt(nodes[1]);
			session.execute(bound.bind(node, community));
		}
		reader.close();
		
		System.out.println("Inserting communities");
		
		//select edges
		
		String select  = "SELECT * FROM Edges";
		ResultSet rs = session.execute(select);
		ArrayList<Integer> ints = new ArrayList<Integer>();

		rs.forEach(r -> {
			ints.add(r.getInt("source"));
			});
		
		System.out.println(ints);
		session.close();
		
		con.close();
	}
}
