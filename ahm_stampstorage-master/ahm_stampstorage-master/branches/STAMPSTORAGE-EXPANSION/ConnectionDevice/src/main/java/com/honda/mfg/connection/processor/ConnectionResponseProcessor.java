package com.honda.mfg.connection.processor;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.mfg.connection.exceptions.CommunicationsException;
import com.honda.mfg.connection.exceptions.NetworkCommunicationException;
import com.honda.mfg.connection.exceptions.ResponseTimeoutException;
import com.honda.mfg.connection.processor.messages.ConnectionExTrigger;
import com.honda.mfg.connection.processor.messages.ConnectionMessage;
import com.honda.mfg.connection.processor.messages.GeneralMessage;
import com.honda.mfg.connection.processor.messages.PingMessage;

/**
 * User: vcc30690 Date: 3/23/11
 */
public class ConnectionResponseProcessor implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(ConnectionResponseProcessor.class);
	private ConnectionResponseReader responseReader;
	private volatile boolean connected;

	private volatile boolean runOnce;
	private volatile boolean recordSequence;
	private static volatile int exCount = 0; // no. of consecutive communication exceptions
	private int maxExceptionCount = 10;
	private List<Object> runSequence = new ArrayList<Object>();

	public ConnectionResponsePublisher getMsgPublisher() {
		return msgPublisher;
	}

	public void setMsgPublisher(ConnectionResponsePublisher msgPublisher) {
		this.msgPublisher = msgPublisher;
	}

	public ConnectionResponsePublisher getPingPublisher() {
		return pingPublisher;
	}

	public void setPingPublisher(ConnectionResponsePublisher pingPublisher) {
		this.pingPublisher = pingPublisher;
	}

	private ConnectionResponsePublisher msgPublisher;
	private ConnectionResponsePublisher pingPublisher;

	public ConnectionResponseProcessor(ConnectionResponseReader responseReader) {
		this(responseReader, false);
	}

	ConnectionResponseProcessor(ConnectionResponseReader responseReader, boolean runOnce) {
		this(responseReader, runOnce, false);
	}

	ConnectionResponseProcessor(ConnectionResponseReader responseReader, boolean runOnce, boolean recordSequence) {
		this.responseReader = responseReader;
		this.recordSequence = recordSequence;
		this.runOnce = runOnce;
		this.connected = true;
	}

	public void run() {
		LOG.info("Starting response processor. Thread id: " + Thread.currentThread().getId());
		boolean successfulFirstPass = false;
		while (connected) {
			try {
				if (exCount > maxExceptionCount)
					sendExCount(exCount);
				processResponse();
				setExCount(0);
				if (!successfulFirstPass) {
					LOG.info("Complete first response processor cycle.");
					successfulFirstPass = true;
				}
			} catch (ResponseTimeoutException ex) {
				recordItem(ex);
				LOG.trace("Response timeout occurred.  MSG: " + ex.getMessage());
			} catch (CommunicationsException e) {
				recordItem(e);
				exCount++;
				LOG.error("Communications error. MSG: " + e.getMessage());
				connected = false;
			} catch (NetworkCommunicationException e) {
				recordItem(e);
				exCount++;
				LOG.error("Network error. MSG: " + e.getMessage());
				connected = false;
			} catch (Exception e) {
				LOG.error("Unknown exception. MSG: " + e.getMessage());
				e.printStackTrace();
			}

			if (runOnce) {
				break;
			}
		}
		LOG.error(
				"Stopped Response Processor since communications failed. Thread ID: " + Thread.currentThread().getId());
	}

	boolean isRunning() {
		return connected;
	}

	List<Object> getSequence() {
		return runSequence;
	}

	private void recordItem(Object item) {
		if (recordSequence) {
			runSequence.add(item);
		}
	}

	void processResponse() {
		String response = responseReader.getResponse();
		ConnectionMessage message;
		if (response.contains("PING")) {
			message = new PingMessage(response);
			pingPublisher.put(message);
		} else {
			message = new GeneralMessage(response);
			msgPublisher.put(message);
		}
		recordItem(message);
		LOG.debug("Putting Message in Queue:  " + message);
	}

	void setConnected(boolean val) {
		this.connected = val;
	}

	public int getExCount() {
		return exCount;
	}

	public void setExCount(int exCount) {
		this.exCount = exCount;
	}

	public int getMaxExceptionCount() {
		return maxExceptionCount;
	}

	public void setMaxExceptionCount(int maxExceptionCount) {
		this.maxExceptionCount = maxExceptionCount;
	}

	private void sendExCount(int exCount) {
		ConnectionExTrigger trigger = new ConnectionExTrigger();
		trigger.setExCount(exCount);
		msgPublisher.put(trigger);
		LOG.error("Published message:  ConnectionExTrigger, exCount=" + exCount);
		setExCount(0);
	}
}
