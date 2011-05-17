package com.voxeo.ozone.web.client;

import java.util.List;
import java.util.UUID;

import com.voxeo.ozone.client.OzoneClient;
import com.voxeo.ozone.client.XmppConnection;
import com.voxeo.ozone.client.XmppException;

public class CustomOzoneClient extends OzoneClient {

	private ClientStanzaListener stanzaListener;
	private ClientConnectionListener connectionListener;
	
	private String id;

	public CustomOzoneClient(String serviceName) {
		
		super(serviceName);
		this.id = UUID.randomUUID().toString();
	}
	
	@Override
	public void connect(String username, String password, String resource) throws XmppException {

		super.connect(username,password,resource);

		if (stanzaListener == null) {
			stanzaListener = new ClientStanzaListener();
			connectionListener = new ClientConnectionListener();
		}

		addStanzaListener(stanzaListener);
		XmppConnection connection = getXmppConnection();
		connection.addXmppConnectionListener(connectionListener);
	}
		
	@Override
	public void disconnect() throws XmppException {

		super.disconnect();
		removeStanzaListener(stanzaListener);
	}
		
	public List<String> getAvailableCalls() {
		
		return stanzaListener.getAvailableCalls();
	}
	
	public String getId() {
		return id;
	}
}
