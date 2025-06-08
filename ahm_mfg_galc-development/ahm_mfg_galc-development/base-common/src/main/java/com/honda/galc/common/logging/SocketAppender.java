package com.honda.galc.common.logging;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name = "SocketAppender", category = "Core", elementType = "appender", printObject = true)
public class SocketAppender extends SocketAppenderBase {

	
	/**
     The default port number of remote logging server (55555).
	 */
	static final int DEFAULT_PORT                 = 55555;

	private ObjectOutputStream oos;
	private int counter = 0;
	
	private SocketLogProcessor socketLogProcessor;

	// reset the ObjectOutputStream every 70 calls
	//private static final int RESET_FREQUENCY = 70;
	private static final int RESET_FREQUENCY = 100;

	public SocketAppender(String name, Filter filter, Layout<? extends Serializable> layout, 
								String primaryHost, int primaryPort, String secondaryHost, int secondaryPort, int queueSize) {
		super(name, filter, layout, primaryHost, primaryPort, secondaryHost, secondaryPort, queueSize);
	}


	/**
     Connect to the specified <b>RemoteHost</b> and <b>Port</b>.
	 */
	@Override
	public void activateOptions() {
		connect();
		socketLogProcessor = new SocketLogProcessor(getQueueSize(), this);
		socketLogProcessor.start();
	}

	/**
	 * Close this appender.  
	 *
	 * <p>This will mark the appender as closed and call then {@link
	 * #cleanUp} method.
	 * */
	@Override
	public synchronized void close() {
		super.close();
		if(socketLogProcessor != null) socketLogProcessor.stopRunning();
		cleanUp();
	}
	
	public String toString() {
		return "SocketAppender - " + getPrimaryHost() + ":" + getPrimaryPort();
	}

	/**
	 * Drop the connection to the remote host and release the underlying
	 * connector thread if it has been created 
	 * */
	public void cleanUp() {
		if(oos != null) {
			try {
				oos.close();
			} catch(IOException e) {
				StatusLogger.getLogger().error("Could not close oos." + e.getMessage());
			}
			oos = null;
		}
	}

	private boolean connect() {
		if(!connect(APPENDER_SOCKET_PRIMARY) && !switchConnection()) {
			setCurrentAppender(APPENDER_FILE);
			return false;
		}
		return true;
	}

	@Override
	protected boolean connect(int appendFlag){
		super.connect(appendFlag);
		boolean flag = super.isConnected();
		if(flag) {
			try {
				// First, close the previous connection if any.
				cleanUp();
				oos = new ObjectOutputStream(getSocket().getOutputStream());
			} catch(IOException e) {
				StatusLogger.getLogger().error("Unable to create output stream.");
				cleanUp();
				finalizeSocket();
				flag = false;
			}
		}
		return flag;
	}

	public void append(LogEvent event) {
		if(socketLogProcessor != null) {
			if(!socketLogProcessor.enqueue(event.toImmutable()))
				StatusLogger.getLogger().error("SocketAppender failed to enqueue LoggingEvent " + getLogRecord(event).getSingleLineMessage());
		}
		
	}

	@Override
	protected boolean socketWrite(LogEvent event) {
		return socketWrite(getLogRecord(event));
	}
	
	protected boolean socketWrite(LogRecord logRecord) {
		boolean flag = true;
		try {
			oos.writeObject(logRecord);
			oos.flush();
			if(++counter >= RESET_FREQUENCY) {
				counter = 0;
				// Failing to reset the object output stream every now and
				// then creates a serious memory leak.
				//System.err.println("Doing oos.reset()");
				oos.reset();
			}
		} catch(IOException e) {
			StatusLogger.getLogger().error("Could not write log message: " + e.getMessage());
			cleanUp();
			finalizeSocket();
			flag = false;
		}
		return flag;
	}
	
	public boolean isConnected() {
		return super.isConnected() && oos != null;
	}


    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }

    /**
     * Builds SocketAppender instances.
     * @param <B> The type to build
     */
    public static class Builder<B extends Builder<B>> extends AbstractOutputStreamAppender.Builder<B>
            implements org.apache.logging.log4j.core.util.Builder<SocketAppender> {

        @PluginBuilderAttribute
        @Required(message = "No primary host provided for SocketAppender")
        private String primaryHost;
        
        @PluginBuilderAttribute
        @Required(message = "No primary port provided for SocketAppender")
        private int primaryPort;
        
        @PluginBuilderAttribute
        private String secondaryHost;
        
        @PluginBuilderAttribute
        private int secondaryPort;	
    	
        @PluginBuilderAttribute
        private int queueSize;	
    	
        public Builder<B> setPrimaryHost(final String primaryHost) {
        	this.primaryHost = primaryHost;
        	return this;
        }
        
        public Builder<B> setPrimaryPort(int primaryPort) {
			this.primaryPort = primaryPort;
			return this;
		}

		public Builder<B> setSecondaryHost(String secondaryHost) {
			this.secondaryHost = secondaryHost;
			return this;
		}

		public Builder<B> setSecondaryPort(int secondaryPort) {
			this.secondaryPort = secondaryPort;
			return this;
		}
		
		public Builder<B> setQueueSize(int queueSize) {
			this.queueSize = queueSize;
			return this;
		}

		@Override
        public SocketAppender build() {
            SocketAppender appender = new SocketAppender(getName(), getFilter(), getLayout(), primaryHost, primaryPort, secondaryHost, secondaryPort, queueSize);
            appender.activateOptions();
            return appender;
        }
    }



}

