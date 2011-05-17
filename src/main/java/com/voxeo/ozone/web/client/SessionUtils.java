package com.voxeo.ozone.web.client;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.voxeo.ozone.client.OzoneClient;

public class SessionUtils {

	public static final String SERVER_ENDPOINT = "127.0.0.1";
		
	private static Lock lock = new ReentrantLock();
	private static OzoneClient persistentClient;
	
	public static OzoneClient checkAndGetOzoneClient(HttpServletRequest request, HttpServletResponse response) {
		
		lock.lock();
		
		try {
			HttpSession session = request.getSession(true);
			OzoneClient client = (OzoneClient)session.getAttribute("client");
			if (client == null) {
				if (persistentClient == null) {					
					persistentClient = new CustomOzoneClient(SERVER_ENDPOINT);
					try {
						persistentClient.connect("userc", "1");
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}
				session.setAttribute("client", persistentClient);
				client = persistentClient;
			}		
			return client;
		} finally {
			lock.unlock();
		}
	}
}
