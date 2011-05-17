package com.voxeo.ozone.web.client;

import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.voxeo.ozone.client.XmppConnectionAdapter;
import com.voxeo.servlet.xmpp.ozone.stanza.XmppObject;

public class ClientConnectionListener extends XmppConnectionAdapter {

	public ClientConnectionListener() {}

	@Override
	public void messageSent(XmppObject message) {

		MessagesQueue.publish(new Message(generateMessage(message.toString()), Message.Type.OUT));
	}
		
	private String generateMessage(String message) {
		
		return DateFormatUtils.format(new Date(), "dd/MM/yyyy hh:mm:ss") + ": " + StringEscapeUtils.escapeXml(message);		
	}
}
