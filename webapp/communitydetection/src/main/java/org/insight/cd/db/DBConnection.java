package org.insight.cd.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.insight.cd.exceptions.PersistentLayerException;

public class DBConnection {

	private static Connection CONNECTION;
	private static final String USERNAME = "graph_select";
	private static final String PASSWORD = "S3lectMe";
	private static final Integer PORT = 3306;
	private static final String HOST = "ec2-34-211-3-246.us-west-2.compute.amazonaws.com";
	private static final String DEFAULT_DB = "GraphDB";

	private DBConnection() {
		// to override creating objects
	}

	public static Connection checkAndCreate() throws PersistentLayerException {
		try {
			if (CONNECTION == null || CONNECTION.isClosed()) {

				CONNECTION = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DEFAULT_DB,
						USERNAME, PASSWORD);

			}
		} catch (SQLException e) {
			throw new PersistentLayerException("Unable to create connection.");
		}
		return CONNECTION;
	}
}
