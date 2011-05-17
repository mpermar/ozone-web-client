package com.voxeo.ozone.web.client;

import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.voxeo.ozone.client.OzoneClient;
import com.voxeo.ozone.client.ref.SayRef;

/**
 * Servlet implementation class OzoneClientServlet
 */
public class OzoneClientServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String SERVER_ENDPOINT = "127.0.0.1";
	
	private ScriptEngine engine;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OzoneClientServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {

    	super.init();
		
		ScriptEngineManager factory = new ScriptEngineManager();
		engine = factory.getEngineByName("groovy");
    }
    
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

		OzoneClient client = SessionUtils.checkAndGetOzoneClient(request, response);
		if (client ==  null) {
			response.sendError(500);
			response.flushBuffer();
			return;
		} else {
			if (engine.get("client") == null) {
				engine.put("client", client);
			}
		}
		
		if (client.getLastCallId() == null) {
			response.sendError(400, "No Active Calls.");
			response.getOutputStream().write("No active calls.".getBytes());
			response.flushBuffer();
			return;
			
		}
		
		String script = request.getParameter("script");
		if (script == null) {
			response.sendError(400);
			response.getOutputStream().write("You need to provide some script".getBytes());
			response.flushBuffer();
			return;
		}
		
		StringBuilder buffer = new StringBuilder();
		Scanner scanner = new Scanner(script);
		String line = null;
		
		while(scanner.hasNextLine()) {
			line = scanner.nextLine().trim();
			if (line.startsWith("answer") ||
				line.startsWith("say") ||
				line.startsWith("transfer") ||
				line.startsWith("ask") ||
				line.startsWith("conference") ||
				line.startsWith("hangup") ||
				line.startsWith("dial")) {
				line = "def result = client." + line.trim();
			} else {
				if (line.startsWith("stop") ||
					line.startsWith("resume") ||
					line.startsWith("pause")) {
					line = "if (result) { result." + line.trim() + "}";
				}
			}
			buffer.append(line);
		}
		
		try {
			Object result = engine.eval(new StringReader(buffer.toString()));
			if (result != null && result instanceof SayRef) {
				engine.put("result",result);
			}
		} catch (Exception e) {
			response.sendError(500, e.getMessage());
			response.getWriter().println(e);
			response.flushBuffer();
			return;
		}
		
		response.getWriter().println("All ok");
		response.flushBuffer();
		return;		
	}
}
