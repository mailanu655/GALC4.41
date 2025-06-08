package com.honda.galc.let.message;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.enumtype.LetReply;
import com.honda.galc.client.enumtype.LetTotalStatus;
import com.honda.galc.client.enumtype.MessageType;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LetMessageDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.LETDataTag;
import com.honda.galc.entity.product.LetMessage;
import com.honda.galc.entity.product.LetSpool;
import com.honda.galc.jca.SocketMessage;
import com.honda.galc.let.util.LetUtil;
import com.honda.galc.letxml.model.UnitInTest;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.util.StringUtil;

public class LetMessageHandler {

	private static final int EOM = 0xFF;
    private static final int EOD = 0x00;
    private static final String ISO_8859_1 	= "ISO-8859-1";
	private static final String LOGGER_ID 	= "JcaAdaptor";
	private static final String MAC_ADDRESS	= "MACAddress=\"";
	private static final String BUILD_CODE	= "BuildCode=\"";
	private static final String VIN			= "VIN=\"";
	
	private SocketMessage socketMessage;
	private String nodeName;
	
	protected LetMessageHandler()  {
	}
	
	public LetMessageHandler(SocketMessage socketMessage, String nodeName) {
		setSocketMessage(socketMessage);
		setSocketTimeout(socketMessage.getRawSocket());
		setNodeName(nodeName);
	}

	public void handleLetMessage() {
		getLogger().info("Entering LetMessageHandler.handleMessage()"); 

		long startTime = System.currentTimeMillis();
		StringBuilder recdStrB = new StringBuilder();
		LetProcessItem processItem = null;
		
		try {
			readMessage(recdStrB);
			if (recdStrB.length() > 1) {
			    processItem = LetUtil.createProcessRequest(recdStrB, getNodeName(), getLetSpool());
				LetUtil.saveLetXmlAsFile(processItem);					// save xml as file
				LetMessage letMsg = saveMessage(processItem);			// save xml to LET_MESSAGE_TBX
				processItem.setMsgId(letMsg.getMessageId());
				if (processItem.isValid()) {
					LetUtil.addMsgToProcessQueue(processItem); 			// add msg to process queue
				}
				sendReplyToLetDevice(processItem);						// send OK or NG
				sendBroadcast(processItem, letMsg);						// send a broadcast if configured
				logMsgHandlingDetails(startTime, processItem);
			}
		} catch (SocketTimeoutException ex) {
			handleSocketTimeoutException(ex);
		} catch (Exception ex) {
			handleException(processItem, ex);
		} finally{
			finalizeSocketMessage(recdStrB);
		}
		getLogger().info("Exiting LetMessageHandler.handleMessage()"); 
	}

	protected void sendBroadcast(LetProcessItem processItem, LetMessage letMsg) {
		if(LetUtil.getLetPropertyBean().getSendBroadcast() != null) {
			String sendBroadCast = LetUtil.getLetPropertyBean().getSendBroadcast().get(letMsg.getTerminalId() +","+String.valueOf(letMsg.getSpoolId()));
			if(sendBroadCast != null && Boolean.valueOf(sendBroadCast)){
				broadcastMessage(letMsg, processItem);
			}
		}
	}

	protected LetMessage saveMessage(LetProcessItem processItem) {
		processItem.setActualTimeStamp(new Timestamp(new Date().getTime()));
		return getDao(LetMessageDao.class).save(setLetMessages(new LetMessage(),processItem));
	}

	private LetMessage setLetMessages(LetMessage letMessage, LetProcessItem processItem) {
		String productId = "";
		// Example header 'A61E02T01TRES[0][0][0]'
		StringBuilder recdStrB = processItem.getReceivedMsg();
        String mfgId = processItem.getReceivedMsg().substring(0, 3);					// A61
        String target = processItem.getReceivedMsg().substring(3, 6);					// E02
        String terminalId = processItem.getReceivedMsg().substring(6, 9);				// T01
        String msg = processItem.getReceivedMsg().substring(9, 13);						// TRES
        String data = processItem.getReceivedMsg().substring(16).trim();				// UnitInTest xml
        ArrayList<String> missingAttribs = new ArrayList<String>();
        String macAddress = LetUtil.getAttribVal(recdStrB, MAC_ADDRESS, missingAttribs);
        String buildCode = LetUtil.getAttribVal(recdStrB, BUILD_CODE, missingAttribs);
		
        if (LetUtil.getLetPropertyBean().isSendVinInResponse() && data.length() >= 36) {
        	 productId = LetUtil.getAttribVal(recdStrB, VIN, missingAttribs);			// UNIT_IN_TEST VIN
        }
		
        if (processItem.isValid()) {
        	letMessage.setTotalStatus(LetTotalStatus.IN_PROCESS.name());        	
        } else {
        	letMessage.setTotalStatus(LetTotalStatus.NG_XML.name());
        }
        
		if (validateMessage(processItem)) {
			letMessage.setMessageReply(LetReply.OK.toString());
			letMessage.setXmlMessageBody(data);
		} else {
			letMessage.setMessageReply(LetReply.NOT_SENT.toString());
			letMessage.setExceptionMessageBody(data);
		}
		
		letMessage.setMacAddress(macAddress);
		letMessage.setBuildCode(buildCode);
        letMessage.setActualTimestamp(processItem.getActualTimeStamp());
        letMessage.setMessageHeader(mfgId + target+terminalId +  msg);
        letMessage.setProductId(productId);
        letMessage.setTerminalId(terminalId);
        letMessage.setMessageType(msg);
        letMessage.setSpoolId(getSpoolId());
        letMessage.setIpAddress(getSocketHostAddress());
		return letMessage;
	}

	public void logMsgHandlingDetails(long startTime, LetProcessItem processItem) {
		getLogger().check("LET request handled in " 
				+ (System.currentTimeMillis() - startTime) 
				+ " milliseconds" 
				+ "\n  Request id         : "
				+ StringUtils.trimToEmpty(processItem.getFileLocation()) 
				+ "\n  Processed at node  : " 
				+ getNodeName()
				+ "\n  Current Queue size : "
				+ LetDataQueueProcessor.getInstance().getCurrentSize()
				+ "\n  Reply sent to LET  : "
				+ processItem.getReply());
	}

	public void handleException(LetProcessItem processItem, Exception ex) {
		ex.printStackTrace();
		String fileLocation = StringUtils.trimToEmpty(processItem == null ? "" : processItem.getFileLocation());
		String msg = "Exception handling LET message "  
				+ fileLocation
				+ " from socket " 
				+ socketMessage.getRawSocket().getRemoteSocketAddress().toString() 
				+ ": "
				+ ex.getMessage();
		getLogger().error(ex, msg);
		LetUtil.sendAlertEmail(msg, ex);
	}

	public void handleSocketTimeoutException(SocketTimeoutException ex) {
		ex.printStackTrace();
		String msg = "Client socket from LET device " 
				+ socketMessage.getRawSocket().getRemoteSocketAddress().toString()
				+ " timed out after " 
				+ getClientSocketTimeout() 
				+ " milliseconds: "
				+ ex.getMessage();
		getLogger().error(ex, msg);
		LetUtil.sendAlertEmail(msg, ex);
	}

	public void finalizeSocketMessage(StringBuilder recdStrB) {
		try {
			getLogger().info("Message received from LET device "
					+ socketMessage.getRawSocket().getRemoteSocketAddress().toString()
					+ " is: " + StringUtil.replaceNonPrintableCharacters(recdStrB));
			socketMessage.getRawSocket().close();
			getLogger().info("Closed LET client socket from LET device " 
					+ socketMessage.getRawSocket().getRemoteSocketAddress().toString());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	protected void readMessage(StringBuilder recdStrB) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(socketMessage.getInputStream()));
		int aChar = -1;
		while ((aChar = br.read()) != EOM && aChar != -1) {
			recdStrB.append((char) aChar);
		}
	}

	/**
	 * send Reply back to LET device
	 * 
	 * @param item
	 */
	private void sendReplyToLetDevice(LetProcessItem item) {
		item.setReply(LetReply.NOT_SENT);
		try {
	          if (item.getUnitInTest() != null) {
				sendOk(item);
			} else {
				logNgMessage(item);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Could not send reply for request " + item.getMsgKey() 
					+ ". Received Message: " 
					+ StringUtil.replaceNonPrintableCharacters(item.getReceivedMsg()));
		}
	}
	
	public void sendOk(LetProcessItem item) {
		item.setReply(LetReply.OK);
		sendReplyMessage(item);
	}

	public void logNgMessage(LetProcessItem item) {
		getLogger().info("The LET Message received is NAK for request " + item.getMsgKey() + ".  Received Message: " + item.getReceivedMsg());
	}
	
	public void sendReplyMessage(LetProcessItem item) {
    	// Example header 'A61E02T01TRES[0][0][0]'
        String mfgId = item.getReceivedMsg().substring(0, 3);			// A61
        String target = item.getReceivedMsg().substring(3, 6);			// E02
        String terminalId = item.getReceivedMsg().substring(6, 9);		// T01
        String msg = item.getReceivedMsg().substring(9, 13);			// TRES
        String data = item.getReceivedMsg().substring(16);				// UnitInTest xml
        
        byte[] byteHeader = null;
        byte[] byteData = null;
        byte[] bytes = null;

        try {
            StringBuilder sbfReplyValue = new StringBuilder();
            if (LetUtil.getLetPropertyBean().isSendVinInResponse() && data.length() >= 36) {
            	String productId = data.substring(19, 36);				// UNIT_IN_TEST VIN
            	sbfReplyValue.append(productId);
            }
            
            while (sbfReplyValue.length() < 20) {
                sbfReplyValue.append('0');
            }
            
            if (msg.equals(MessageType.TRES.toString())) {			// Test RESult
                byteData = sbfReplyValue.toString().getBytes(ISO_8859_1);
            } else {						// DIAG, RINP
                byteData = data.getBytes(ISO_8859_1);
            }
            
            byteHeader = (mfgId + terminalId + target + msg).getBytes(ISO_8859_1);
            bytes = new byte[byteHeader.length + byteData.length + 5];

            System.arraycopy(byteHeader, 0, bytes, 0, byteHeader.length);
            bytes[byteHeader.length] = (byte) 0x00;
            bytes[byteHeader.length + 1] = (byte) 0x01;
            bytes[byteHeader.length + 2] = (byte) EOD;
            System.arraycopy(byteData, 0, bytes, byteHeader.length + 3, byteData.length);
            bytes[bytes.length - 2] = (byte) EOD;
            bytes[bytes.length - 1] = (byte) EOM;

        } catch (Exception ex) {
            ex.printStackTrace();
            getLogger().error(ex, "Unable to construct reply message for request " + item.getMsgKey() + ": " + ex.getMessage());
        }
        
		try {
			socketMessage.getOutputStream().write(bytes);
			socketMessage.getOutputStream().flush();
			logReply(bytes, item.getMsgKey());
		} catch (IOException ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Could not send reply to LET device for request " + item.getMsgKey() + ".  Received Message: " + item.getReceivedMsg());
		}
    }
    
	private void logReply(byte[] bytes, String msgKey) {
		try {
			StringBuilder reply = new StringBuilder();
			for (byte singleByte: bytes) {
				reply.append((char)singleByte);
			}
			getLogger().info("Sent reply to LET device for request " + msgKey + ": " + StringUtil.replaceNonPrintableCharacters(reply));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Validate the message to determine if the reply
	 * back to LET device needs to be OK or NG
	 * 
	 * @param item
	 * @return
	 */
	private boolean validateMessage(LetProcessItem item) {
		long startTime = System.currentTimeMillis();
		if ((item.getReceivedMsg().length() < 1) || (!item.isValid()))
			return false;
		
		try {
	        String header = item.getReceivedMsg().substring(0, 16);
	        String msg = header.substring(9, 13);
	        String footer = item.getReceivedMsg().substring(item.getReceivedMsg().length() - 1);
	        String data = item.getReceivedMsg().substring(16).trim();
	
	        if (!msg.equals(MessageType.TRES.toString()) && !msg.equals(MessageType.DIAG.toString()) && !msg.equals(MessageType.RINP.toString())) {
	        	getLogger().error("Invalid MessageType: " + msg);
	            return false;
	        }
	
	        if (!header.substring(13, 15).equals("\u0000\u0000")) {
	        	getLogger().error("Invalid Header format (status): " + header.substring(13, 15));
	            return false;
	        }
	
	        if (!header.substring(15, 16).equals("\u0000")) {
	        	getLogger().error("Invalid Header format (eod): " + header.substring(15, 16));
	            return false;
	        }
	
	        if (!footer.equals("\u0000")) {
	        	getLogger().error("Invalid End of message (eom): " + footer);
	            return false;
	        }
	        
	        	try {
	        		UnitInTest unitInTest = LetUtil.convertToUnitInTest(data, item.getMsgKey());
	        		if(unitInTest==null){
	        			throw new Exception();
        		} else {
        		    item.setUnitInTest(unitInTest);
	        		} 
	            } catch(Exception ex) {
	            	handleValidationFailure(item, ex);
	            	return false;
	            }

	        getLogger().debug("Validating xml took " 
	        		+ (System.currentTimeMillis() - startTime) 
	        		+ " milliseconds\n  "
	        		+ LogRecord.replaceNonPrintableCharacters(data));
	        return true;
		} catch (Exception ex) {
			handleValidationFailure(item, ex);
			return false;
		}
	}

	protected void handleValidationFailure(LetProcessItem item, Exception ex) {
		ex.printStackTrace();
		getLogger().error("Validation failed for request " + item.getMsgKey() + ".  Received Message: " 
						+ StringUtil.replaceNonPrintableCharacters(item.getReceivedMsg()));
	}

	public int getClientSocketTimeout() {
		int timeout = 3000;
		try {
			timeout = socketMessage.getRawSocket().getSoTimeout();
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
		return timeout;
	}
	
    public void setSocketTimeout(Socket socket) {
    	try {
    		socket.setSoTimeout(LetUtil.getLetPropertyBean().getConnectionTimeout());
    	} catch (SocketException ex) {
    		ex.printStackTrace();
    		getLogger().error(ex, "Unable to set socket timeout for connection from device " 
				+ socketMessage.getRawSocket().getRemoteSocketAddress().toString()
				+ ": "
				+ ex.getMessage());
    	}
    }
	
	public SocketMessage getSocketMessage() {
		return this.socketMessage;
	}
	
	public void setSocketMessage(SocketMessage socketMessage) {
		this.socketMessage = socketMessage;
	}
	    
    public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	public void setLetClientSocket(SocketMessage letClientSocket) {
		this.socketMessage = letClientSocket;
	}

	public SocketMessage getLetClientSocket() {
		return socketMessage;
	}
	
	public static Logger getLogger(){
		return Logger.getLogger(LOGGER_ID);
	}

	private void broadcastMessage(LetMessage letMsg, LetProcessItem processItem) {
		try {
			String testResult = "fail";
	        UnitInTest unitInTest = processItem.getUnitInTest();
			if (unitInTest != null) {
				testResult = unitInTest.getTotalStatus().toString();
			}

			String processPointId = LetUtil.getLetPropertyBean().getLetTerminalProcessPoint().get(letMsg.getTerminalId().trim());

			DataContainer dc = new DefaultDataContainer();
			dc.put(DataContainerTag.PRODUCT_ID, letMsg.getProductId().trim());
			dc.put(LETDataTag.TERMINAL_ID, letMsg.getTerminalId().trim());
			dc.put(LETDataTag.TEST_RESULT, testResult.trim());
			dc.put(LETDataTag.MESSAGE_ID, letMsg.getMessageId());
			dc.put(LETDataTag.SPOOL_ID, letMsg.getSpoolId());

			getService(BroadcastService.class).broadcast(processPointId, dc);
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to broadcast message: " + StringUtils.trimToEmpty(ex.getMessage()));
		}
	}
	
	protected LetSpool getLetSpool() {
        return getSocketMessage().getLetSpool() ;
	}
	
	protected Integer getSpoolId() {
	    return getLetSpool().getSpoolId();
	}
	
	protected String getSocketHostAddress() {
	    return getSocketMessage().getRawSocket().getInetAddress().getHostAddress();
	}
}
