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
 * Servlet implementation class MovieGenreDistribution
 */
public class MovieGenreDistribution extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MovieGenreDistribution() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String[] categories = {"Action", "Comedy", "Sci-Fi", "Crime", "Horror"};
		Integer[] data = {3623, 2421, 2318, 1729, 1102};
		
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
