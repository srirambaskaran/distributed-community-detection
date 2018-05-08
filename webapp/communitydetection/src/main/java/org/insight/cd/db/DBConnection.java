package org.insight.cd.db;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Session;

public class DBConnection {
	
	private static DBConnection CONNECTION;
	private static final String USERNAME = "db_select";
	private static final String PASSWORD = "IamN0tRoot";
	private static final Integer PORT = 9042;

	private Cluster cluster;
    private Session session;
 
    
	
	private DBConnection() {
		connect(PORT, 
				"ec2-35-162-93-253.us-west-2.compute.amazonaws.com", 
				"ec2-34-217-253-198.us-west-2.compute.amazonaws.com", 
				"ec2-34-210-119-36.us-west-2.compute.amazonaws.com", 
				"ec2-50-112-46-206.us-west-2.compute.amazonaws.com");
	}
	
	public static DBConnection checkAndCreate() {
		if(CONNECTION == null) 
			CONNECTION = new DBConnection();
		return CONNECTION;
	}
	
	public void connect(Integer port, String... node) {
        Builder b = Cluster.builder().addContactPoints(node).withCredentials(USERNAME, PASSWORD);
        
        if (port != null) {
            b.withPort(port);
        }
        cluster = b.build();
 
        session = cluster.connect();
    }
 
    public Session getSession() {
        return this.session;
    }
 
    public void close() {
        session.close();
        cluster.close();
    }
}
