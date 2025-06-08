package com.honda.galc.device.mitsubishi;

import java.io.IOException;
import java.io.InterruptedIOException;

import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.net.SocketClient;

/**
 * 
 * <h3>MitsubishiPlcSocket</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> MitsubishiPlcSocket description </p>
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
 * @author Paul Chou
 * Nov 5, 2010
 *
 */
public class MitsubishiPlcSocket extends SocketClient
{
	protected int packetSize = 4096;
	private byte[] dataBuffer = new byte[packetSize];
	private Logger logger;
	
	
	public MitsubishiPlcSocket(String hostname, int port, int connectTimeout, Logger logger) {
		super(hostname, port, connectTimeout);
		this.logger = logger;
	}

	public MitsubishiPlcSocket(String hostname, int port) {
		super(hostname, port);
	}

	public MitsubishiPlcSocket(String hostname, int port,
			int connectionTimeout, int sotimeout, Logger logger) {
		super(hostname, port, connectionTimeout, sotimeout);
		this.logger = logger;
	}

	public byte[] receiveDataFromPlc() throws IOException, Throwable {
		try {
			
			getSocketClient();

			int count = 0;
			byte[] receivedArray = null;
			while(count <= 0){
				count = getInputStream().read(dataBuffer);

				if(count > 0){
					receivedArray = new byte[count];
					System.arraycopy(dataBuffer, 0, receivedArray, 0, count);
					return receivedArray;
				} 
			}
			return receivedArray;
			
		}
		catch (InterruptedIOException ioe) {
			ioe.printStackTrace();
			logger.error(ioe, "Error (inter)in recieveDataFromPlc(): " + ioe);
			throw (ioe);

		}
		catch (IOException ioe) {
			ioe.printStackTrace();
			closeConnections();
			logger.error(ioe, "Error (io) in recieveDataFromPlc(): " + ioe);
			throw (ioe);

		}
		catch (Throwable t) {
			t.printStackTrace();
			closeConnections();
			logger.error(t, "Error (t)in recieveDataFromPlc(): " + t.getMessage());
			throw (t);
		}
	}
	
	public boolean sendDataToPlc(byte[] data){
		
		getSocketClient();
		int retry = 0;
		try{
			
			while(retry < 3){
				try {
					getOutputStream().write(data);
					getOutputStream().flush();
					return true;
				} catch (Exception e) {
					Logger.getLogger().warn("Unable to write to PLC:", getHostname(), ":" + getPort(), " on try:" + retry);
					retry++;
					getSocketClient();
				}
			}
			Logger.getLogger().error("Failed to sendDataToPlc() after retry:" + retry);
			return false;
		} catch(Throwable t){
			Logger.getLogger().error(t, "Error in sendDataToPlc()");
			return false;
		}
	}

	public boolean isUnsolicitedPacket(){
		int command;
		try {
			command = getInputStream().read();
			getInputStream().reset();
			return command == 96;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	@Override
	protected void createSocket() throws ServiceTimeoutException {
		super.getSocketClient();
	}

}
