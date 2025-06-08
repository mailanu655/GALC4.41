package com.honda.galc.device.simulator.torque;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

import org.bushe.swing.event.EventBus;

import com.honda.galc.client.ui.event.Event;
import com.honda.galc.client.ui.event.EventType;
import com.honda.galc.device.simulator.utils.Logger;
import com.honda.galc.device.simulator.utils.Utils;
import com.honda.galc.util.StringUtil;

/**
 * This thread will service a single client of the virtual device
 * after a connection is established between the two.  
 * A state machine will be maintained for every client connection.  OPC messages
 * received on this connection will alter the state of the virtual device.
 * When the state machine reaches the "READY" state to send torque readings,
 * this thread will attempt to send torque readings retrieved from the 
 * test case files _in the "datafiles" directory.
 *  
 * @author Kathiresan Subu
 */
public class KepServerConnectionThread extends Thread 
{
	private Socket _socket = null;
	private InputStreamReader _rd = null;
	private PrintWriter _pOut = null;
	private FileWriter _fOut = null;
	
	private StateMachine _deviceStateMachine = null;
	private static boolean _sendingTorque = false;
	private String _deviceId;
	private String vin;
	
	/**
	 * Creates a new KepServerConnectionThread to service a client
	 * 
	 * @param name
	 * @param socket
	 * @param fOut
	 */
    public KepServerConnectionThread(String deviceId, Socket socket, FileWriter fOut) 
    {
    	super(KepServerConnectionThread.class.getSimpleName());
    	
    	_socket = socket;
    	_fOut = fOut;
    	_deviceId = deviceId;
    	_deviceStateMachine = new StateMachine(fOut); 
    }

	/**
	 * Starts the thread when a connection is established with the client.  Messages
	 * received _in this connection will be parsed (just the header). 
	 */
	public void run()
	{
		Logger.log(_fOut, "New client connected to " + _socket.getLocalPort() + " from IP " + _socket.getRemoteSocketAddress()); 			
		char nullValue = 0;
	
	    try 
	    {
	        _rd = new InputStreamReader(_socket.getInputStream());
	        _pOut = new PrintWriter(_socket.getOutputStream(), true);	        	        
	        StringBuffer strBuf = new StringBuffer();
	        int intchar;
	        
	        while ((intchar = _rd.read()) != -1)
	        {
	        	if (intchar == nullValue)			// Indicates end of a message _in the socket inputstream from KepServer
	        	{
	        		// Messages received from client while handling 
	        		// this current message will have to wait for processing
	        		handleMessage(strBuf);			
	        		strBuf = new StringBuffer();
	        	}
	        	else
	        		strBuf.append((char) intchar);
	        }	        
	    } 
	    catch (IOException ex) 
	    {
	    	Logger.log(_fOut, ex.getMessage());
			ex.printStackTrace();
	    }
	    finally
	    {
	    	try
	    	{
	    		if (_pOut.checkError())
	    			Logger.log(_fOut, "Server " + _socket.getInetAddress() + " encountered socket output writer error");
	    	}
	    	catch (Exception ex)
	    	{
	    		Logger.log(_fOut, ex.getMessage());
				ex.printStackTrace();
	    	}	    		
	    }
	}
	
	/**
	 * Based on the message type, the state machine will be altered 
	 * and replies will be sent as defined _in the "OPCReplies.xml" file _in
	 * the "control" files directory
	 * 
	 * @param strBuf
	 */
	private void handleMessage(StringBuffer strBuf)
	{
		try
		{
			String str = strBuf.toString();
			OPMessageType msgType = OPMessageType.getMessageType(strBuf);
			String messageId = StringUtil.padLeft(new Integer(msgType.value()).toString(), 4, '0', true);
				
			Logger.log(_fOut, "<<   " + str + "<<");
			Logger.log(_fOut, "Message Received     : " 
					+ messageId
					+ " - " 
					+ msgType.name());
						
			switch (msgType)
			{
				case keepAlive:								// 9999
					break;
					
				case communicationStart:					// 0001
					_deviceStateMachine.setCurrentState(_deviceStateMachine.getCurrentState() | StateMachine.COMMUNICATION_START);
					break;
				
				case communicationStop:						// 0003
					_deviceStateMachine.setCurrentState(_deviceStateMachine.getCurrentState() & ~StateMachine.COMMUNICATION_START);
					break;
					
				case paramSetSelectedSubscribe:				// 0014
					_deviceStateMachine.setCurrentState(_deviceStateMachine.getCurrentState() | StateMachine.SUBSCRIBED_TO_PSET);
					break;

				case paramSetSelectedAck:					// 0016
					_deviceStateMachine.setCurrentState(_deviceStateMachine.getCurrentState() | StateMachine.PARAMETER_SET_SELECTED_ACK);
					break;

				case selectParamSet:						// 0018
					_deviceStateMachine.setCurrentState(_deviceStateMachine.getCurrentState() | StateMachine.SELECTED_PARAM_SET);
					break;
					
				case selectJob:						        // 0038		
					_deviceStateMachine.setCurrentState(_deviceStateMachine.getCurrentState() | StateMachine.SELECT_JOB);
					break;
					
				case jobInfoAcknowledge:				    // 0036		
					_deviceStateMachine.setCurrentState(_deviceStateMachine.getCurrentState() | StateMachine.JOB_INFO_ACKNOWLEDGE);
					break;	
					
				case toolDisable:							// 0042		
					_deviceStateMachine.setCurrentState(_deviceStateMachine.getCurrentState() & ~StateMachine.DEVICE_ENABLED);
					break;

				case toolEnable:							// 0043		
					_deviceStateMachine.setCurrentState(_deviceStateMachine.getCurrentState() | StateMachine.DEVICE_ENABLED);
					break;
						
				case lastTighteningResultDataSubscribe:		// 0060
					_deviceStateMachine.setCurrentState(_deviceStateMachine.getCurrentState() | StateMachine.SUBSCRIBED_TO_DEVICE);
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

			EventBus.publish(new Event(msgType, strBuf.substring(20).trim(), EventType.CHANGED));
			sendReplies(msgType);
		}
		catch(Exception ex)
		{
			Logger.log(_fOut, ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	/**
	 * Send replies to the client based on the replies defined
	 * for this message _in the OPCReplies.xml file.  If the reply 
	 * is of type OPCMessageType.lastTighteningResultUploadReply, then a 
	 * torque tightening reading is sent
	 * 
	 * @param msgType
	 */
	protected void sendReplies(OPMessageType msgType)
	{
		ArrayList<String> replies = OPMessageType.getReplyMessages(msgType);
		if (replies == null || replies.isEmpty())
			return;

		for (String messageId: replies)
		{
			Hashtable<String, String> msgFields = new Hashtable<String, String>();
			msgFields.put("CONTROLLER_NAME", _deviceId);
			msgFields.put("MESSAGE_ID", OPMessageType.getMessageId(msgType));
			
				Utils.writeMessageToOutStream(_fOut, _pOut, OPMessage.createMessage(messageId, msgFields), ">>");
		}
	}

	/**
	 * 
	 * @return
	 */
	public static boolean getSendingTorque()
	{
		return _sendingTorque;
	}
	
	/**
	 * 
	 * @param val
	 */
	public static void setSendingTorque(boolean val)
	{
		_sendingTorque = val;
	}
	
	/**
	 * Have the device simulator send a torque if it is enabled
	 * @param torque
	 */
	public void sendTorque(Torque torque){
		while((_deviceStateMachine.getCurrentState() & StateMachine.READY_FOR_TORQUE_READING) != StateMachine.READY_FOR_TORQUE_READING ){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Hashtable<String, String> fieldsData = new Hashtable<String, String>();
		for(OPMessageField tag: torque.getFields())
		{
			if (!tag.getValue().trim().equalsIgnoreCase(""))
				fieldsData.put(tag.getName().toUpperCase(), tag.getValue().trim());
		}
		
		if(vin != null && !fieldsData.keySet().contains("VIN_NUMBER"))
			fieldsData.put("VIN_NUMBER", vin);
		
		Utils.writeMessageToOutStream(_fOut, _pOut, OPMessage.createMessage(OPMessageType.lastTighteningResultUploadReply, fieldsData), ">>");

	}
	/**
	 * Force the Device Simulator to send a torque regardless of its
	 * disabled status
	 * @param torque
	 */
	public void forceTorque(Torque torque){
		Hashtable<String, String> fieldsData = new Hashtable<String, String>();
		for(OPMessageField tag: torque.getFields())
		{
			if (!tag.getValue().trim().equalsIgnoreCase(""))
				fieldsData.put(tag.getName().toUpperCase(), tag.getValue().trim());
		}
		Utils.writeMessageToOutStream(_fOut, _pOut, OPMessage.createMessage(OPMessageType.lastTighteningResultUploadReply, fieldsData), ">>");

	}
	
	
}
