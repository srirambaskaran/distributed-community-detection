package org.insight.cd.servlets;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Servlet implementation class DegreeDistribution
 */
public class DegreeDistribution extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DegreeDistribution() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] categories = {"1","3","5","7","9","More"};
		Integer[] data = {1020,4402,2143,420,121};
		
		HashMap<String, Object> returned = new HashMap<>();
		returned.put("categories", categories);
		
		HashMap<String, List<Integer>> series = new  HashMap<>();
		series.put("data", Arrays.asList(data));
		returned.put("series", series);
		
		String returnString = new ObjectMapper().writeValueAsString(returned);
		
		response.getWriter().write(returnString);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
