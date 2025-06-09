package com.honda.mfg.connection;

import java.util.ArrayList;
import java.util.List;

import org.bushe.swing.event.EventBus;

import com.honda.mfg.connection.messages.MessageRequest;
import com.honda.mfg.connection.messages.MessageResponse;
import com.honda.mfg.connection.processor.messages.ConnectionMessage;

/**
 * User: Jeffrey M Lutz Date: 4/12/11
 */
public class MockMessageRequestProcessor implements MessageRequestProcessor {
	private List<Object> sendRequestBehaviors;
	private List<MessageRequest> sentRequests;

	public MockMessageRequestProcessor() {
		sentRequests = new ArrayList<MessageRequest>();
		sendRequestBehaviors = new ArrayList<Object>();
	}

	public void sendRequest(MessageRequest messageRequest) {
		Object o = null;
		if (sendRequestBehaviors.size() > 0) {
			o = sendRequestBehaviors.get(0);
			sendRequestBehaviors.remove(0);
		}
		if (o != null && o instanceof RuntimeException) {
			throw (RuntimeException) o;
		}
		addRequestSent(messageRequest);
		if (o != null && o instanceof MessageResponse) {
			MessageResponse resp = (MessageResponse) o;
			EventBus.publish(resp);
		} else if (o != null && o instanceof ConnectionMessage) {
			ConnectionMessage resp = (ConnectionMessage) o;
			EventBus.publish(resp);
		}
	}

	public void addSendRequestBehavior(Object o) {
		sendRequestBehaviors.add(o);
	}

	public List<MessageRequest> getRequestsSent() {
		List<MessageRequest> cloneList = new ArrayList<MessageRequest>();
		cloneList.addAll(sentRequests);
		return cloneList;
	}

	private void addRequestSent(MessageRequest request) {
		sentRequests.add(request);
	}
}
