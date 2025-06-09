// StampServiceServerSocket.java

package com.honda.mfg.stamp.storage.service.clientmgr;


import com.honda.mfg.stamp.conveyor.service.StorageStateUpdateService;
import com.honda.mfg.stamp.storage.service.utils.MessageSinkInterface;
import com.honda.mfg.stamp.storage.service.utils.ServiceRoleObserver;
import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapper;
import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapperImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * This class provides a server socket to accept connections on the port that is specified
 * in stamp-service-socket.properties.  Sockets are managed by a {@link StampServiceSocketConnection} in its own thread
 * @author VCC44349
 * @see com.honda.mfg.stamp.storage.service.utils.MessageTransport
 * @see CommandProcessor
*/
public class StampServiceServerSocket implements Runnable, StampServiceServerSocketInterface, ServiceRoleObserver
{
    private static final Logger LOG = LoggerFactory.getLogger(StampServiceServerSocket.class);


	private ServerSocket serverSocket = null;
	//private Socket clientSocket_ 		= null;
	private Properties props = null;
	private int maxNumConnections_ = DEFAULT_MAX_CONNECTIONS;
	private int maxNumTimeouts = DEFAULT_MAX_NUM_TIMEOUTS;
	private int serverPort = 44449;
	private int preWriteDelay = 1;//1 millisecond
	private int preReadDelay = 1;//1 millisecond
	private int serverSocketTimeOut = 0;//60 seconds
	private int numConnections = 0;
	private int numTimeOuts = 0;
	private boolean bInitialized = false;
	/**
	 * Constructor takes a config file name and loads the server socket properties
	 * @param configFileName
	 * @throws IOException
	 * @throws Exception
	 * @see #StampServiceServerSocket(Properties)
	 * @see #loadConfig(String)
	 */
	public StampServiceServerSocket(String configFileName)
		throws  Exception
	{
		try
		{
			loadConfig(configFileName);
	        serviceRoleWrapper = ServiceRoleWrapperImpl.getInstance();
	        serviceRoleWrapper.addObserver(this);
		}
		catch (IOException ioEx)
		{
			LOG.error("StampServiceServerSocket.StampServiceServerSocket():" + ioEx);
			throw ioEx;
		}	
		catch(Exception e)
		{
			LOG.error("StampServiceServerSocket.StampServiceServerSocket():" + e);
			throw e;
		}
	}

	/**
	 * Constructor takes a {@link java.util.Properties} parameter for socket
	 * server properties
	 * @param properties
	 */
	public StampServiceServerSocket(Properties properties)
	{
		props = properties;
        serviceRoleWrapper = ServiceRoleWrapperImpl.getInstance();
        serviceRoleWrapper.addObserver(this);
	}

	private void loadConfig(String configFileName)
		throws Exception
	{
		try
		{
			FileInputStream fis = new FileInputStream(configFileName);
			props = new Properties();
			props.load(fis);
		}
		catch (IOException ioEx)
		{
			LOG.error("StampServiceServerSocket.loadConfig():" + ioEx);
			throw ioEx;
		}	
		catch (Exception e)
		{
			LOG.error("StampServiceServerSocket.loadConfig():" + e);
			throw e;
		}	
	}

	/*
	 * This method reads configuration properties for the server socket
	 * @param defaultPort
	 * @throws BindException
	 * @throws SocketException
	 * @throws IOException
	 * @throws Exception
	 */
	@Override
	public void init() throws Exception
	{
		//Create server socket
		try
		{
			serverPort = Integer.valueOf(props.getProperty(SERVER_PORT_PROPERTY_KEY,
																		String.valueOf(DEFAULT_PORT))).intValue();

			maxNumConnections_ = Integer.valueOf(props.getProperty(MAX_NUM_CONNECTIONS,
																		String.valueOf(maxNumConnections_))).intValue();

			LOG.info("StampServiceServerSocket.init():ServerSocket running on port: " + serverPort);
			LOG.info("StampServiceServerSocket.init():ServerSocket maxNumConnections_ set to: "+ maxNumConnections_ );

			serverSocketTimeOut = Integer.valueOf(props.getProperty(SERVER_SOCKET_TIMEOUT_PROPERTY_KEY,
																		String.valueOf(serverSocketTimeOut))).intValue();

			maxNumTimeouts = Integer.valueOf(props.getProperty(MAX_NUM_TIMEOUTS,
																		String.valueOf(maxNumTimeouts))).intValue();
			LOG.info("StampServiceServerSocket.init():ServerSocket maxNumTimeouts_ set to: "+ maxNumTimeouts);

			preWriteDelay = Integer.valueOf(props.getProperty(PRE_WRITE_DELAY, String.valueOf(preWriteDelay))).intValue();
			LOG.info("StampServiceServerSocket.init():ServerSocket preWriteDelay_ set to: "+ preWriteDelay );

			preReadDelay = Integer.valueOf(props.getProperty(PRE_READ_DELAY, String.valueOf(preReadDelay))).intValue();
			LOG.info("StampServiceServerSocket.init():ServerSocket preReadDelay_ set to: "+ preReadDelay );
			bInitialized = true;
		}
		catch(Exception e)
		{
			LOG.error("StampServiceServerSocket.init():" +e);
			throw e;
		}
	}

	@Override
	public void start() throws IOException
	{
		run();
	}

	/* This is the run method for the socket server.  Creates the server socket
	 * and waits for client connections.  Client sockets are handed off to
	 * {@link StampServiceSocketConnection} for further processing on a separate thread,
	 * and continues to wait for client connections
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		try
		{
			init();
			while(!done)
			{
				//	Split into a thread when a connection is established.
				try
				{
					//Wait for a connection

					if (serverSocket == null)
					{
						LOG.info("StampServiceServerSocket.run():Creating ServerSocket, PORT: " + serverPort);
						serverSocket = new ServerSocket(serverPort,maxNumConnections_);
						LOG.info("StampServiceServerSocket.run():Created ServerSocket...");

						serverSocket.setSoTimeout(serverSocketTimeOut);
						LOG.info("StampServiceServerSocket.run():ServerSocket timeout set to: "+ serverSocketTimeOut + " milliseconds");
					}

					LOG.info("StampServiceServerSocket.run():ServerSocket waiting for client connection...");
					LOG.info("StampServiceServerSocket.run():ServerSocket arrived at latch gate, waiting...");
					getServiceRoleWrapper().getLatch().await();
					LOG.info("StampServiceServerSocket.run():ServerSocket latch released, accepting client connections...");
					Socket clientSocket = null;
					if(serverSocket == null)  {
						continue;
					}
					clientSocket = serverSocket.accept();
					numConnections++;
					
					LOG.info("StampServiceServerSocket.run():Client connected....");

					StampServiceSocketConnection stampServiceSocketConnection = new StampServiceSocketConnection(clientSocket, props);
					MessageSinkInterface cmdProc = CommandProcessor.getInstance();
					cmdProc.setCarrierManager(carrierManager);
					stampServiceSocketConnection.setObjectSink(cmdProc);
					socketMap.put(String.valueOf(clientSocket.hashCode()), stampServiceSocketConnection);

					stampServiceSocketConnection.start();
				}
				catch (SocketException sockEx)  {
					LOG.error("StampServiceServerSocket.run():" + sockEx);
					sockEx.printStackTrace();
					if(!switchToPassive)  {setDone(true);}
				}
				catch (InterruptedIOException ioEx)  //also SocketTimeoutException
				{
					numTimeOuts++;
					LOG.error("StampServiceServerSocket.run():" +ioEx);

					if(enableMaxTimeOuts_)
					{
						if(numTimeOuts >= maxNumTimeouts)
						{
							setDone(true);
							//break;  NO NEED to break, instead setDone(true) above
						}
					}
				}
				catch (IOException ioEx)
				{
					setDone(true);
					LOG.error("StampServiceServerSocket.run():" +ioEx);
					ioEx.printStackTrace();
					//break;  NO NEED to break, instead setDone(true) above
				}
				catch (Exception e)
				{
					setDone(true);
					LOG.error("StampServiceServerSocket.run():" +e);
					e.printStackTrace();

					LOG.error("StampServiceServerSocket.run(): Closing the ServerSocket ...");

					try
					{
						if(serverSocket != null) {
                            serverSocket.close();}
					}
					catch (Exception e2)
					{
						LOG.error("StampServiceServerSocket.run(): Exception closing server socket. " + e2);
					}

					serverSocket = null;

					//break;  NO NEED to break, instead setDone(true) above
				}
			} // end while
		} //end try enclosing while
		catch(Exception e)
		{
			LOG.error("StampServiceServerSocket.run(): " + e);
			e.printStackTrace();
		}
		finally  {
			closeSocket();
		}
	}//end of run()


	@Override
	public void closeSocket()  {
		setDone(true);
		if(socketMap != null && !socketMap.isEmpty())  {
			for(StampServiceSocketConnectionInterface soClient : socketMap.values())  {
				if(soClient != null)  {
					soClient.closeSocket();					
				}
			}
			socketMap.clear();
		}
		try
		{
			if(serverSocket != null) {
                serverSocket.close();}
		}
		catch (Exception e2)
		{
			LOG.error("StampServiceServerSocket.run(): Exception closing server socket. " + e2);
		}
		finally  {
			serverSocket = null;
		}
	}
	
	/**
	 * close socket, but re-enter loop, i.e., don't set done.
	 * typically used for switching to passive mode
	 */
	public void closeAndWait()  {
		//setDone(true);  //in this case, don't set done
		setSwitchToPassive(serviceRoleWrapper.isPassive());
		if(socketMap != null && !socketMap.isEmpty())  {
			for(StampServiceSocketConnectionInterface soClient : socketMap.values())  {
				if(soClient != null)  {
					soClient.closeSocket();
				}
			}
		}
		try
		{
			if(serverSocket != null) {
                serverSocket.close();}
		}
		catch (Exception e2)
		{
			LOG.error("StampServiceServerSocket.run(): Exception closing server socket. " + e2);
		}
		finally  {
			serverSocket = null;
		}
	}
	
	public static void main(String args[])
	{
		try
		{
			  String configFileName = new String(args[0]);
				StampServiceServerSocket serverSo = new StampServiceServerSocket(configFileName);

				serverSo.init();
				serverSo.enableMaxTimeOuts(true);

				ExecutorService threadService = Executors.newSingleThreadExecutor();
				//t.setDaemon(true);
				threadService.execute(serverSo);

				Thread.sleep(Integer.valueOf(args[1]).intValue());
				serverSo.setDone(true);
				Thread.sleep(2 * serverSo.getServerSocketTimeout());
				LOG.info("StampServiceServerSocket.main():MainThread finished executing");

		}
		catch(Exception e)
		{
			LOG.error("Exception in main()", e);
			e.printStackTrace();
		}
	}

	@Override
	public int getNumConnections()
	{
		return numConnections;
	}
	public int getServerSocketTimeout()
	{
		return serverSocketTimeOut;
	}
	public int getNumTimeOuts()
	{
		return numTimeOuts;
	}
	public boolean isEnableMaxTimeOuts()
	{
		return enableMaxTimeOuts_;
	}
	public void enableMaxTimeOuts(boolean to)
	{
		enableMaxTimeOuts_ = to;
	}

	private volatile boolean switchToPassive = false;

	private boolean isSwitchToPassive() {
		return switchToPassive;
	}

	private void setSwitchToPassive(boolean switchToPassive) {
		this.switchToPassive = switchToPassive;
	}

	private volatile boolean done = false;
	@Override
	public boolean isDone() {
		return done;
	}

	@Override
	public void setDone(boolean done) {
		this.done = done;
	}
	
	private StorageStateUpdateService carrierManager;
	public StorageStateUpdateService getCarrierManager() {
		return carrierManager;
	}

	public void setCarrierManager(StorageStateUpdateService newManager) {
		this.carrierManager = newManager;
	}

	private Map< String, StampServiceSocketConnectionInterface> socketMap = new HashMap<String, StampServiceSocketConnectionInterface>();
	public Map<String, StampServiceSocketConnectionInterface> getSocketMap() {
		return socketMap;
	}

	public void setSocketMap(
			Map<String, StampServiceSocketConnectionInterface> socketMap) {
		this.socketMap = socketMap;
	}

    private ServiceRoleWrapper serviceRoleWrapper = null;
	public ServiceRoleWrapper getServiceRoleWrapper() {
		return serviceRoleWrapper;
	}

	@Override
	public void setServiceRoleWrapper(ServiceRoleWrapper serviceRoleWrap) {
		this.serviceRoleWrapper = serviceRoleWrap;
	}

	
	@Override
	public void roleChange(boolean isPassive) {
		if(isPassive)  {closeAndWait();}
	}

	public boolean isbInitialized() {
		return bInitialized;
	}

	protected void setbInitialized(boolean bInitialized) {
		this.bInitialized = bInitialized;
	}

	private boolean enableMaxTimeOuts_ = false;
	
}//end of class StampServiceServerSocket
