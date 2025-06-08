package com.honda.galc.device.simulator.torque;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.device.simulator.utils.Logger;
import com.honda.galc.device.simulator.utils.Utils;
import com.honda.galc.util.StringUtil;

/**
 * This thread will handle communication with a single Torque Client
 * 
 * - A state machine will be maintained for every client connection.  
 * - OPC messages received on this connection will alter the state of the virtual device.
 * - Will send Torque to the client when the state machine reaches "READY" state
 * - Replies to messages will be based on controlfiles/OPReplies.xml
 *  
 * @author Subu Kathiresan
 * @date Jan 06, 2015
 */
public class TorqueClientServiceThread extends Thread {
	
	private Socket socket = null;
	private InputStreamReader inReader = null;
	private PrintWriter pOut = null;
	private FileWriter fOut = null;
	
	private TorqueSimulatorStateMachine deviceStateMachine = null;
	private static boolean sendingTorque = false;
	private String deviceId;
	private String vin;
	
	/**
	 * Creates a new TorqueClientServiceThread to service a client
	 * 
	 * @param name
	 * @param socket
	 * @param fOut
	 */
    public TorqueClientServiceThread(String deviceId, Socket socket, FileWriter fOut) {
    	super(TorqueClientServiceThread.class.getSimpleName());
    	
    	this.socket = socket;
    	this.fOut = fOut;
    	this.deviceId = deviceId;
    	this.deviceStateMachine = new TorqueSimulatorStateMachine(fOut); 
    }

	/**
	 * Starts the thread when a connection is established with the client.  Messages
	 * received in this connection will be parsed (just the header). 
	 */
	public void run() 	{
	
		Logger.log(fOut, "New client connected to " + socket.getLocalPort() + " from IP " + socket.getRemoteSocketAddress()); 			
		char nullValue = 0;
	
	    try {
	        inReader = new InputStreamReader(socket.getInputStream());
	        pOut = new PrintWriter(socket.getOutputStream(), true);	        	        
	        StringBuffer strBuf = new StringBuffer();
	        int intchar;
	        
	        while ((intchar = inReader.read()) != -1) {
	        	if (intchar == nullValue) {			// Indicates end of a message in the socket inputstream from Torque Client
	        		// Messages received from client while handling 
	        		// this current message will have to wait for processing
	        		handleMessage(strBuf);			
	        		strBuf = new StringBuffer();
	        	} else {
	        		strBuf.append((char) intchar);
	        	}
	        }	        
	    } catch (IOException ex) {
	    	Logger.log(fOut, ex.getMessage());
			ex.printStackTrace();
	    } finally {
	    	try	{
	    		if (pOut.checkError())
	    			Logger.log(fOut, "Server " + socket.getInetAddress() + " encountered socket output writer error");
	    	} catch (Exception ex) {
	    		Logger.log(fOut, ex.getMessage());
				ex.printStackTrace();
	    	}	    		
	    }
	}
	
	/**
	 * Based on the message type, the state machine will be altered 
	 * and replies will be sent as defined in the "OPCReplies.xml" file in
	 * the "control" files directory
	 * 
	 * @param strBuf
	 */
	private void handleMessage(StringBuffer strBuf) {
		try {
			String str = strBuf.toString();
			OPMessageType msgType = OPMessageType.getMessageType(strBuf);
			String messageId = StringUtil.padLeft(new Integer(msgType.value()).toString(), 4, '0', true);
				
			Logger.log(fOut, "<<   " + str + "<<");
			Logger.log(fOut, "Message Received     : " 
					+ messageId
					+ " - " 
					+ msgType.name());
						
			EventBusUtil.publish(new OPMessageEvent(deviceId, msgType, strBuf.toString()));
			
			switch (msgType) {
				case keepAlive:								// 9999
					break;
					
				case communicationStart:					// 0001
					deviceStateMachine.setCurrentState(deviceStateMachine.getCurrentState() | TorqueSimulatorStateMachine.COMMUNICATION_START);
					break;
				
				case communicationStop:						// 0003
					deviceStateMachine.setCurrentState(deviceStateMachine.getCurrentState() & ~TorqueSimulatorStateMachine.COMMUNICATION_START);
					break;
					
				case paramSetSelectedSubscribe:				// 0014
					deviceStateMachine.setCurrentState(deviceStateMachine.getCurrentState() | TorqueSimulatorStateMachine.SUBSCRIBED_TO_PSET);
					break;

				case paramSetSelectedAck:					// 0016
					deviceStateMachine.setCurrentState(deviceStateMachine.getCurrentState() | TorqueSimulatorStateMachine.PARAMETER_SET_SELECTED_ACK);
					break;

				case selectParamSet:						// 0018
					deviceStateMachine.setCurrentState(deviceStateMachine.getCurrentState() | TorqueSimulatorStateMachine.SELECTED_PARAM_SET);
					break;
					
				case jobInfoAcknowledge:				    // 0036		
					deviceStateMachine.setCurrentState(deviceStateMachine.getCurrentState() | TorqueSimulatorStateMachine.JOB_INFO_ACKNOWLEDGE);
					break;	
					
				case selectJob:						        // 0038		
					deviceStateMachine.setCurrentState(deviceStateMachine.getCurrentState() | TorqueSimulatorStateMachine.SELECT_JOB);
					break;
					
				case toolDisable:							// 0042		
					deviceStateMachine.setCurrentState(deviceStateMachine.getCurrentState() & ~TorqueSimulatorStateMachine.DEVICE_ENABLED);
					break;

				case toolEnable:							// 0043		
					deviceStateMachine.setCurrentState(deviceStateMachine.getCurrentState() | TorqueSimulatorStateMachine.DEVICE_ENABLED);
					break;
						
				case lastTighteningResultDataSubscribe:		// 0060
					deviceStateMachine.setCurrentState(deviceStateMachine.getCurrentState() | TorqueSimulatorStateMachine.SUBSCRIBED_TO_DEVICE);
					break;
				
				case vinDownloadRequest:
					vin = strBuf.substring(20).trim();
					break;
					
				case abortJob:
					vin = null;
					break;
					
				default:
					break;				
			}

			sendReplies(msgType);
		}
		catch(Exception ex) {
			Logger.log(fOut, ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	/**
	 * Send replies to the client based on the replies defined
	 * for this message in the OPCReplies.xml file.  If the reply 
	 * is of type OPCMessageType.lastTighteningResultUploadReply, then a 
	 * torque tightening reading is sent
	 * 
	 * @param msgType
	 */
	protected void sendReplies(OPMessageType msgType) {
		ArrayList<String> replies = OPMessageType.getReplyMessages(msgType);
		if (replies == null || replies.isEmpty())
			return;

		for (String messageId: replies) {
			Hashtable<String, String> msgFields = new Hashtable<String, String>();
			msgFields.put("CONTROLLER_NAME", deviceId);
			msgFields.put("MESSAGE_ID", OPMessageType.getMessageId(msgType));
			
			Utils.writeMessageToOutStream(fOut, pOut, OPMessage.createMessage(messageId, msgFields), ">>");
		}
	}

	/**
	 * 
	 * @return
	 */
	public static boolean getSendingTorque() {
		return sendingTorque;
	}
	
	/**
	 * 
	 * @param val
	 */
	public static void setSendingTorque(boolean val) {
		sendingTorque = val;
	}
	
	/**
	 * Have the device simulator send a torque if it is enabled
	 * @param torque
	 */
	public void sendTorque(Torque torque) {
		while((deviceStateMachine.getCurrentState() & TorqueSimulatorStateMachine.READY_FOR_TORQUE_READING) 
				!= TorqueSimulatorStateMachine.READY_FOR_TORQUE_READING ) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Hashtable<String, String> fieldsData = new Hashtable<String, String>();
		for(OPMessageField tag: torque.getFields()) {
			if (!tag.getValue().trim().equalsIgnoreCase(""))
				fieldsData.put(tag.getName().toUpperCase(), tag.getValue().trim());
		}
		
		if(vin != null && !fieldsData.keySet().contains("VIN_NUMBER"))
			fieldsData.put("VIN_NUMBER", vin);
		
		Utils.writeMessageToOutStream(fOut, pOut, OPMessage.createMessage(OPMessageType.lastTighteningResultUploadReply, fieldsData), ">>");
	}

	/**
	 * Force the Device Simulator to send a torque regardless of its
	 * disabled status
	 * @param torque
	 */
	public void forceTorque(Torque torque){
		Hashtable<String, String> fieldsData = new Hashtable<String, String>();
		for(OPMessageField tag: torque.getFields()) {
			if (!tag.getValue().trim().equalsIgnoreCase(""))
				fieldsData.put(tag.getName().toUpperCase(), tag.getValue().trim());
		}
		Utils.writeMessageToOutStream(fOut, pOut, OPMessage.createMessage(OPMessageType.lastTighteningResultUploadReply, fieldsData), ">>");
	}
}
