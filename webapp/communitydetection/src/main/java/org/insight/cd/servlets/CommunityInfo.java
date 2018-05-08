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
 * Servlet implementation class CommunityInfo
 */
public class CommunityInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommunityInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String id = request.getParameter("id");
		String[] categories = {"Action", "Comedy", "Sci-Fi", "Crime", "Horror"};
		HashMap<String, String> communityInfo = new  HashMap<>();
		HashMap<String, Object> returned1 = new HashMap<>();
		HashMap<String, Object> returned2 = new HashMap<>();
		HashMap<String, Object> returned3 = new HashMap<>();
		HashMap<String, Object> returned4 = new HashMap<>();
		HashMap<String, Object> returned5 = new HashMap<>();
		HashMap<String, Object> returned6 = new HashMap<>();
		HashMap<String, Object> returned7 = new HashMap<>();
		HashMap<String, Object> returned8 = new HashMap<>();
		HashMap<String, Object> returned9 = new HashMap<>();
		HashMap<String, Object> returned10 = new HashMap<>();
		returned1.put("categories", categories);
		returned2.put("categories", categories);
		returned3.put("categories", categories);
		returned4.put("categories", categories);
		returned5.put("categories", categories);
		returned6.put("categories", categories);
		returned7.put("categories", categories);
		returned8.put("categories", categories);
		returned9.put("categories", categories);
		returned10.put("categories", categories);
		
		
		Integer[] data1 = {129, 28, 64, 14, 31};
		Integer[] data2 = {112, 34, 201, 31, 12};
		Integer[] data3 = {20, 121, 23, 13, 12};
		Integer[] data4 = {73, 83, 91, 15, 13};
		Integer[] data5 = {12, 4, 10, 21, 12};
		Integer[] data6 = {23, 35, 7, 12, 4};
		Integer[] data7 = {40, 34, 11, 0, 120};
		Integer[] data8 = {14, 53, 0, 2, 3};
		Integer[] data9 = {20, 12, 0, 2, 0};
		Integer[] data10 = {1, 0, 7, 5, 0};
		
		
		
		
		returned1.put("numNodes",266);
		returned2.put("numNodes",301);
		returned3.put("numNodes",220);
		returned4.put("numNodes",237);
		returned5.put("numNodes",189);
		returned6.put("numNodes",157);
		returned7.put("numNodes",191);
		returned8.put("numNodes",87);
		returned9.put("numNodes",34);
		returned10.put("numNodes",13);
		
		HashMap<String, List<Integer>> series1 = new  HashMap<>();
		series1.put("data", Arrays.asList(data1));
		returned1.put("series", series1);
		
		HashMap<String, List<Integer>> series2 = new  HashMap<>();
		series2.put("data", Arrays.asList(data2));
		returned2.put("series", series2);
		
		HashMap<String, List<Integer>> series3 = new  HashMap<>();
		series3.put("data", Arrays.asList(data3));
		returned3.put("series", series3);
		
		HashMap<String, List<Integer>> series4 = new  HashMap<>();
		series4.put("data", Arrays.asList(data4));
		returned4.put("series", series4);
		
		HashMap<String, List<Integer>> series5 = new  HashMap<>();
		series5.put("data", Arrays.asList(data5));
		returned5.put("series", series5);
		
		HashMap<String, List<Integer>> series6 = new  HashMap<>();
		series6.put("data", Arrays.asList(data6));
		returned6.put("series", series6);
		
		HashMap<String, List<Integer>> series7 = new  HashMap<>();
		series7.put("data", Arrays.asList(data7));
		returned7.put("series", series7);
		
		HashMap<String, List<Integer>> series8 = new  HashMap<>();
		series8.put("data", Arrays.asList(data8));
		returned8.put("series", series8);
		
		HashMap<String, List<Integer>> series9 = new  HashMap<>();
		series9.put("data", Arrays.asList(data9));
		returned9.put("series", series9);
		
		HashMap<String, List<Integer>> series10 = new  HashMap<>();
		series10.put("data", Arrays.asList(data10));
		returned10.put("series", series10);
		
		communityInfo.put("1", new ObjectMapper().writeValueAsString(returned1));
		communityInfo.put("2", new ObjectMapper().writeValueAsString(returned2));
		communityInfo.put("3", new ObjectMapper().writeValueAsString(returned3));
		communityInfo.put("4", new ObjectMapper().writeValueAsString(returned4));
		communityInfo.put("5", new ObjectMapper().writeValueAsString(returned5));
		communityInfo.put("6", new ObjectMapper().writeValueAsString(returned6));
		communityInfo.put("7", new ObjectMapper().writeValueAsString(returned7));
		communityInfo.put("8", new ObjectMapper().writeValueAsString(returned8));
		communityInfo.put("9", new ObjectMapper().writeValueAsString(returned9));
		communityInfo.put("10", new ObjectMapper().writeValueAsString(returned10));
		
		response.getWriter().write(communityInfo.get(id));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
