package org.insight.cd.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/status")
/**
 * Servlet implementation class CurrentStatusServlet
 */
public class CurrentStatusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CurrentStatusServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String json = "{\"nodes\":[{\"id\":1,\"group\":1},{\"id\":2,\"group\":2},{\"id\":3,\"group\":3},{\"id\":4,\"group\":4},{\"id\":5,\"group\":5},{\"id\":6,\"group\":6},{\"id\":7,\"group\":7},{\"id\":8,\"group\":8},{\"id\":9,\"group\":9},{\"id\":10,\"group\":10}],\"links\":[{\"source\":1,\"target\":2,\"value\":5},{\"source\":2,\"target\":7,\"value\":10},{\"source\":3,\"target\":4,\"value\":4},{\"source\":1,\"target\":2,\"value\":7},{\"source\":1,\"target\":4,\"value\":5},{\"source\":4,\"target\":6,\"value\":4},{\"source\":8,\"target\":3,\"value\":6},{\"source\":9,\"target\":1,\"value\":1},{\"source\":2,\"target\":4,\"value\":3}]}";
		response.getWriter().write(json);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
