package com.honda.galc.logserver;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.lang.time.DateFormatUtils;

import com.honda.galc.common.logging.AbstractFileLogWriter;



/**
 * 
 * <h3>LogServer Class description</h3>
 * <p>This is the main class of GALC log server. It handles initial
 * connection requests from clients and forks individual LogRequestHandler 
 * thread for each socket connection to process log messages. All LogRequestHandler
 * enqueues log messages to a center LogRequestProcessor to dispatch the log messages 
 * to different log files
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
 * Feb 4, 2010
 *
 */

public class LogServer extends Thread {
    
    private static LogServer logServer;
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	private boolean running = false;
	private LogServerConfig logServerConfig;
	private LogRequestProcessor logRequestProcessor;
	private MessageFileLogWriter logServerFile;
	
  
	public LogServer() throws FileNotFoundException {
		super();
        logServerConfig = new LogServerConfig();
        
		String dateFormat = String.format("%tF",System.currentTimeMillis());
        PrintStream out = new PrintStream(new FileOutputStream(new File(logServerConfig.getLogFilePath(),String.format("LogServer_stdout_%s.txt",dateFormat))));
        PrintStream err = new PrintStream(new FileOutputStream(new File(logServerConfig.getLogFilePath(),String.format("LogServer_stderr_%s.txt",dateFormat))));
	    System.setOut(out);
	    System.setErr(err);
    
        AbstractFileLogWriter.setShiftDirectory(logServerConfig.isShiftDir(),logServerConfig.getShiftStartTimes());		
	}
	
	
	private void init() {
        initServerLogWriter();
        createServerSocket();
        startLogRequestProcessor();
	}
	
	private void startLogRequestProcessor(){
	    logRequestProcessor = new LogRequestProcessor(logServerConfig);
	    logRequestProcessor.start();
	}

/**
 * See class comment for launch options.
 * @param arguments
 * @throws FileNotFoundException 
 */	
	public static void main(String[] arguments) throws FileNotFoundException {
			logServer = new LogServer();
			logServer.init();
			logServer.start();
	}

	private void createServerSocket() {
		try {
			serverSocket = new ServerSocket(logServerConfig.getServerPort());
		} catch (IOException e) {
			log("GalcLogServer Exception: " + e.getMessage());
		}
	}
	
	private void initServerLogWriter() {
	    logServerFile = new MessageFileLogWriter(logServerConfig.getLogFilePath(),"LogServer");
	}

/**
 * Tell the serverSocket to start accepting incoming requests.
 *
 */	
	public void acceptMessage() {
		if(serverSocket == null) {
			log("ServerSocket is null. Please try to launch the server again.");
			setRunning(false);
		} else {
			log("Log server is waiting for connections.");
		}

		try {
			while(running) {
				socket = serverSocket.accept();
				if(socket != null) {
					handleRequest(socket);
				}
			}

		} catch (IOException e) {
				if(running) {
					log("GalcLogServer Exception: " + e.getMessage());
				}
		}
	}
  
	public void run() {
		setRunning(true);
		log("Starting Log Server at port " + logServerConfig.getServerPort() + " ...");
		setPriority(Thread.MAX_PRIORITY);
		acceptMessage();
	}
	
	public void handleRequest(Socket socket) {
	       log("log client connected from " + socket.getRemoteSocketAddress());
	       LogRequestHandler handler = new LogRequestHandler(socket, logRequestProcessor);
           handler.start();

	}
	
	public void logMessage(String message){
		try {
			message = DateFormatUtils.format(System.currentTimeMillis(),"yy-MM-dd HH:mm:ss.SSS") + " " + message;
			if(logServerFile != null) logServerFile.log(message);
		} catch (OutOfMemoryError e) {
			System.err.println("Out of memory. Please check disk space usage or other system errors.");
			System.exit(0);
		}
	}

/**
 * Stop the server.
 *
 */
	public void stopRunning() {
		setRunning(false);
		try {
			if(serverSocket != null) serverSocket.close();
		} catch (IOException e) {
		}
		log("Log server stopped.");
	}

/**
 * Log a message.
 * @param aMessage the message to be logged.
 */	
	public static void log(String aMessage) {
		logServer.logMessage(aMessage);
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	
}