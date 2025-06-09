package com.honda.io.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.channels.IllegalBlockingModeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.io.StreamPair;
import com.honda.mfg.connection.exceptions.NetworkCommunicationException;
import com.honda.mfg.connection.watchdog.WatchdogContext;

/**
 * User: Jeffrey M Lutz Date: Sep 9, 2010 Time: 9:27:48 AM
 */
public class SocketStreamPair implements StreamPair, WatchdogContext {
	private static final Logger LOG = LoggerFactory.getLogger(SocketStreamPair.class);

	private BufferedReader reader;
	private BufferedWriter writer;
	private SocketFactory socketFactory;
	private Socket socket;

	public SocketStreamPair(SocketFactory socketFactory) {
		this.socketFactory = socketFactory;
	}

	private void connectIfNotConnected() {
		if (socket == null) {
			try {
				socket = socketFactory.createSocket();
			} catch (UnknownHostException e) {
				throw new NetworkCommunicationException("Unknown host error:  " + socketFactory.getConfigMsg(), e);
			} catch (IllegalArgumentException e) {
				throw new NetworkCommunicationException("Illegal argument error:  " + socketFactory.getConfigMsg(), e);
			} catch (SocketTimeoutException e) {
				throw new NetworkCommunicationException("Socket timeout error:  " + socketFactory.getConfigMsg(), e);
			} catch (IllegalBlockingModeException e) {
				throw new NetworkCommunicationException("Illegal blocking mode error:  " + socketFactory.getConfigMsg(),
						e);
			} catch (SecurityException e) {
				throw new NetworkCommunicationException("Java security error:  " + socketFactory.getConfigMsg(), e);
			} catch (IOException e) {
				throw new NetworkCommunicationException("I/O error:  " + socketFactory.getConfigMsg(), e);
			} catch (Exception e) {
				LOG.debug(e.getMessage());
			}

			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), CharSets.ISO_8859_1));
				writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), CharSets.ISO_8859_1));
			} catch (IOException e) {
				reader = null;
				writer = null;
				throw new NetworkCommunicationException("Network error:  " + e.getMessage(), e);
			} catch (Exception e) {
				LOG.debug(e.getMessage());
			}
		}
	}

	@Override
	public BufferedReader in() {
		connectIfNotConnected();
		return reader;
	}

	@Override
	public BufferedWriter out() {
		connectIfNotConnected();
		return writer;
	}

	@Override
	public void close() {
		if (socket == null && writer == null && reader == null) {
			LOG.debug("Nothing to do.  Socket/Reader/Writer already closed.");
		}
		LOG.info("Attempting to close socket to:" + socketFactory.getHostName() + ":" + socketFactory.getPortNumber());
		try {
			reader.close();
		} catch (Exception e) {
			LOG.debug("Unable to close reader: " + e.getMessage());
		}
		try {
			writer.close();
		} catch (Exception e) {
			LOG.debug("Unable to close writer: " + e.getMessage());
		}
		try {
			socket.close();
		} catch (Exception e) {
			LOG.debug("Unable to close socket: " + e.getMessage());
		}
		socket = null;
		LOG.info("Closed socket.");
	}
}
