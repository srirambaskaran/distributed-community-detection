package org.insight.cd.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.insight.cd.db.DBConnection;
import org.insight.cd.modules.Histogram;

/**
 * Servlet implementation class CommunityDistribution
 */
public class CommunityDistributionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CommunityDistributionServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Connection con = DBConnection.checkAndCreate();
			Statement statement = con.createStatement();
			ResultSet result = statement.executeQuery("select community_id, count(*) from Node group by community_id");
			Histogram histogram = new Histogram();
			while (result.next()) {
				int count = result.getInt(2);
				histogram.addDataPoint(count);
			}
			HashMap<String, Object> returned = new HashMap<>();
			returned.put("categories", histogram.getCategoryLabels());

			HashMap<String, List<Integer>> series = new HashMap<>();
			series.put("data", histogram.getBins());
			returned.put("series", series);
			String returnString = new ObjectMapper().writeValueAsString(returned);
			response.getWriter().write(returnString);

			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
