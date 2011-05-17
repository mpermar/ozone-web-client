package com.voxeo.ozone.web.client;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class OzoneClientServlet
 */
public class StatsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		process(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		process(request,response);
	}

	protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		CustomOzoneClient client = (CustomOzoneClient)SessionUtils.checkAndGetOzoneClient(request, response);
		if (client ==  null) {
			response.sendError(500);
			response.flushBuffer();
			return;
		}
		
		String op = request.getParameter("op");
		if (op == null) {
			List<String> calls = client.getAvailableCalls();
			StringBuilder ajaxBuffer = new StringBuilder();
			if (calls.size() == 0) {
				response.getWriter().println("<p>No offers have been received yet.</p>");
			} else {
				for(String call: calls) {
					ajaxBuffer.append("<div class=\"item\"><a href=\"javascript:answerCall('"+call+"')\">" + call + "</a> <a href=\"javascript:answerCall('"+call+"')\"><img src=\"./images/call.png\"/></a></div>");
				}
				response.getWriter().println(ajaxBuffer);
				response.getWriter().flush();
			}
		}
	}
}
