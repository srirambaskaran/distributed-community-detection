package org.insight.cd.modules;

import java.util.ArrayList;

import org.insight.cd.db.DBConnection;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class SelectData {
	public static void main(String[] args) {
		DBConnection con = DBConnection.checkAndCreate();
		Session session = con.getSession();
		//select edges		
		String select  = "SELECT * FROM Edges WHERE source = 1";
		try {
		ResultSet rs = session.execute(select);
		ArrayList<Integer> ints = new ArrayList<Integer>();

		rs.forEach(r -> {
			ints.add(r.getInt("source"));
			});
		
		System.out.println(ints);
		} catch(Exception e) {
			System.out.println("Some exception");
			e.printStackTrace();
			
		} finally {
			session.close();
			con.close();
		}
		
	}
}
