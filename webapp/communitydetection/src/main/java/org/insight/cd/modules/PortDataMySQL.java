package org.insight.cd.modules;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PortDataMySQL {

	public static void main(String[] args) {

		String url = "ec2-54-214-137-8.us-west-2.compute.amazonaws.com";

		Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + url + ":3306/MovieLensCommunityInfo", "movie_db_update", "ModifyM3");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		BufferedReader bf = null;
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO USER_MOVIE_RATINGS VALUES(?,?,?)");
			con.setAutoCommit(false);

			bf = new BufferedReader(new FileReader("/Users/srirambaskaran/Documents/Insight/repo/data/ml-20m/ratings.csv"));
			String line = bf.readLine();
			int count = 0;
			while((line = bf.readLine()) != null) {
				String[] tokens = line.split(",");
				int userId = Integer.parseInt(tokens[0]);
				int movieId = Integer.parseInt(tokens[1]);
				double rating = Double.parseDouble(tokens[2]);
				
				ps.setInt(1, userId);
				ps.setInt(2, movieId);
				ps.setDouble(3, rating);
				ps.addBatch();
				
				count+=1;
				
				if (count % 1000 == 0) {
					ps.executeBatch();
					System.out.println("Compelted "+count);
				}
			}
			con.commit();
			bf.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}

	}

}
