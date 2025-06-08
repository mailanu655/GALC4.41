package com.honda.galc.logserver;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.net.SocketException;

import com.honda.galc.common.logging.LogRecord;



/**
 * 
 * <h3>LogRequestHandler Class description</h3>
 * <p> LogRequestHandler takes the log messages from current socket connection
 * and enqueue the log messages to the center LogRequestProcessor 
 *  </p>
 * <p> This thread terminates when the client closes the socket connection
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Mar 4, 2010
 *
 */

public class LogRequestHandler extends Thread {
	private Socket socket;
	private LogRequestProcessor requestProcessor;
	private boolean running;

	public LogRequestHandler (Socket aSocket, LogRequestProcessor requestProcessor) {
		socket = aSocket;
		this.requestProcessor = requestProcessor;
	}
	
	
	public void run() {
	    running = true;
		try {
		    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
	        
		    while(running){
		    	LogRecord record = (LogRecord) input.readObject();
		        enqueue(record);
		    }
		} catch (SocketException e) {
		    LogServer.log("SocketException client " + socket.getRemoteSocketAddress() + " disconnected  - " + e.getMessage());
		    LogServer.log(captureException(e));
		} catch (EOFException e) {
		    LogServer.log("EOFException client " + socket.getRemoteSocketAddress() + " disconnected  - " + e.getMessage());
		    LogServer.log(captureException(e));
	    } catch (IOException e){
		    e.printStackTrace();
		    LogServer.log(captureException(e));
		} catch (ClassNotFoundException e){
		    LogServer.log("Class not found: " + e.getMessage());
		    LogServer.log(captureException(e));
		} catch (OutOfMemoryError oe) {
			System.err.println("Out of memory. Please check disk space usage or other system errors.");
		    LogServer.log(captureException(oe));
			System.exit(0);
		}

	}
	
	private String captureException(Throwable t) {
		StringWriter s = new StringWriter();
		PrintWriter p = new PrintWriter(s);
		t.printStackTrace(p);
		return s.getBuffer().toString();
	}
	
	private void enqueue(LogRecord logRecord) {
	    if(!requestProcessor.enqueue( logRecord))
	    	LogServer.log("Emergency : unable to enqueue message : " + logRecord.getSingleLineMessage());
	}
	
	public void stopRunning() {
		running = false;
	}
	
	public boolean isRunning() {
		return running;
	}
	
}


