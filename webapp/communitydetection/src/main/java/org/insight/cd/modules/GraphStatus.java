package org.insight.cd.modules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.codehaus.jackson.map.ObjectMapper;
import org.insight.cd.db.DBConnection;
import org.insight.cd.models.D3Link;
import org.insight.cd.models.D3Node;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class GraphStatus {
	private DBConnection connection;
	
	public GraphStatus() {
		connection = DBConnection.checkAndCreate();
	}
	
	public String getCurrentGraph() {
		List<D3Node> nodes = getNodes();		
		List<D3Link> edges = getEdges();
		HashMap<String, Object> result = new HashMap<>();
		result.put("nodes",nodes);
		result.put("links", edges);
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(result);
			System.out.println("Done retrieving");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	private List<D3Link> getEdges() {
		String query = "SELECT * FROM GraphSpace.Edges WHERE source < 100 AND destination < 100 ALLOW FILTERING";
		Session session = connection.getSession();
		ResultSet rs = session.execute(query);
		List<D3Link> links = new ArrayList<D3Link>();
		Random rand = new Random();
		rs.forEach(result -> {
			links.add(new D3Link(result.getInt("source"), result.getInt("destination"), rand.nextInt(10)));
		});
		
		return links;
	}
	

	private List<D3Node> getNodes() {
		String query = "SELECT * FROM GraphSpace.Communities WHERE node < 100 ALLOW FILTERING";
		Session session = connection.getSession();
		ResultSet rs = session.execute(query);
		List<D3Node> nodes = new ArrayList<D3Node>();
		rs.forEach(result -> {
			nodes.add(new D3Node(result.getInt("node"), result.getInt("community")));
		});
		
		return nodes;
	}
}
