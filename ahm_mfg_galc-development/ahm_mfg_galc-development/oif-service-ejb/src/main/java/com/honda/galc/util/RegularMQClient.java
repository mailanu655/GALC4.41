package com.honda.galc.util;

import java.io.EOFException;
import java.io.IOException;

import com.honda.galc.common.logging.Logger;
import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

/**
 * 
 * <h3>RegularMQClient Class description</h3>
 * <p> This is a utility class which provides methods for writing to and reading from a message queue. </p>
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
 * @author Ratul Chakravarty<br>
 * February 24, 2014
 *
 *
 */
/** * * 
* @version 1.0
* @author Ratul Chakravarty 
* @since February 24, 2014
*/
public class RegularMQClient {

	private String queueManagerNameStr;
	private String user;
	private String password;
	private String queueName;
	private String hostName;
	private int portNumber;
	private String channel;
	// Create a default local queue.
	private MQQueue defaultLocalQueue;
	private MQQueueManager queueManager;
	private boolean isUTF = false;
	private boolean isMQSTR = false;

	private static Logger logger = Logger.getLogger("RegularMQClient");

	public RegularMQClient(String hostName, int portNumber,
			String queueManagerNameStr, String channel, String queueName,
			String user, String password) throws MQException {
		this.hostName = hostName;
		this.portNumber = portNumber;
		this.queueManagerNameStr = queueManagerNameStr;
		this.channel = channel;
		this.queueName = queueName;
		this.user = user;
		this.password = password;
		init();
	}

	private void init() throws MQException {
		// Set up MQ Environment.
		MQEnvironment.hostname = hostName;
		MQEnvironment.channel = channel;
		MQEnvironment.port = portNumber;
		MQEnvironment.userID = user;
		MQEnvironment.password = password;
		// set transport properties.
		MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY, MQC.TRANSPORT_MQSERIES_CLIENT);
		
		queueManager = new MQQueueManager(queueManagerNameStr);
	}
	
	
	/**
	 * This method initializes the client to deal with UTF String messages.
	 * @throws MQException
	 */
	public void initUTF() throws MQException {
		init();
		int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT ;
		// initialize MQ manager.
		queueManager = new MQQueueManager(queueManagerNameStr);
		defaultLocalQueue = queueManager.accessQueue(queueName, openOptions);
		isUTF = true;
		isMQSTR = false;
	}
	
	/**
	 * This method initializes the client to deal with MQSTR String messages.
	 * @throws MQException
	 */
	public void initMQSTR() throws MQException {
		init();
		int openOptions = MQC.MQOO_FAIL_IF_QUIESCING | MQC.MQOO_INPUT_SHARED | MQC.MQOO_BROWSE;
		// initialize MQ manager.
		queueManager = new MQQueueManager(queueManagerNameStr);
		defaultLocalQueue = queueManager.accessQueue(queueName, openOptions);
		isUTF = false;
		isMQSTR = true;
	}
	
	public void putMsgStringToMQ(String msg) throws MQException, IOException {
		if(isUTF) {
			putUTFMsgStringToMQ(msg);
		} else if(isMQSTR) {
			putMQSTRMsgStringToMQ(msg);
		}
	}

	private void putMQSTRMsgStringToMQ(String msg) throws IOException, MQException {
		try
		{
			MQMessage putMessage = new MQMessage();
			putMessage.format = MQC.MQFMT_STRING;
			putMessage.feedback = MQC.MQFB_NONE;
			putMessage.messageType = MQC.MQMT_DATAGRAM;
			putMessage.characterSet = 1200;
			putMessage.encoding = 546;
			char[] chars = msg.trim().toCharArray();
			for (char c : chars) {
				putMessage.writeChar(c);
			}
	
			// specify the message options...
			MQPutMessageOptions pmo = new MQPutMessageOptions();
			int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT;
			defaultLocalQueue = queueManager.accessQueue(queueName, openOptions);
	
			// put the message on the queue
			defaultLocalQueue.put(putMessage, pmo);
			//complete the transaction
			queueManager.commit();
		}
		finally
		{
			//close the queue and close the queue connections 
			if ( defaultLocalQueue != null && defaultLocalQueue.isOpen() )
			{
				defaultLocalQueue.close();
			}
		}
	}	

	private void putUTFMsgStringToMQ(String msg) throws IOException, MQException {

		MQMessage putMessage = new MQMessage();
		putMessage.writeUTF(msg);

		// specify the message options...
		MQPutMessageOptions pmo = new MQPutMessageOptions();
		// put the message on the queue
		defaultLocalQueue.put(putMessage, pmo);
	}

	public String getMsgStringFromMQ() throws MQException, IOException {
		String msg = null;
		if(isUTF) {
			msg = getUTFMsgStringFromMQ();
		} else if(isMQSTR) {
			msg = getMQSTRMsgStringFromMQ();
		}
		return msg;
	}
	
	private String getUTFMsgStringFromMQ() throws MQException, IOException {
		MQMessage getMessages = new MQMessage();

		// get message options.
		MQGetMessageOptions gmo = new MQGetMessageOptions();
		defaultLocalQueue.get(getMessages, gmo);
		return getMessages.readUTF();
	}

	private String getMQSTRMsgStringFromMQ() throws MQException, IOException {

		MQMessage getMessages = new MQMessage();
		StringBuilder retStr = new StringBuilder();

		// get message options.
		MQGetMessageOptions gmo = new MQGetMessageOptions();
		gmo.options = MQC.MQGMO_WAIT | MQC.MQGMO_BROWSE_FIRST;
		gmo.matchOptions = MQC.MQMO_NONE;
		gmo.waitInterval = 10000;
		defaultLocalQueue.get(getMessages, gmo);
		int msglen = getMessages.getMessageLength();
		
		try {
			for(int i = 0; i < msglen; i++) {
				retStr.append(getMessages.readChar());
			}
		} catch (EOFException e) {
			if(e.getMessage().contains("MQJE086")) {
				// Do Nothing. End Of File character of the message reached.
			} else {
				logger.error("EOFException occured: " + e);
			}
		}
		// removing the message
		gmo.options = MQC.MQGMO_MSG_UNDER_CURSOR;
		defaultLocalQueue.get(getMessages, gmo);
		return retStr.toString();
	}
	
	/**
	 * This method removes the message currently under the cursor if 'toDelete' flag is true and fetch the next msg and return
	 * 
	 * @param options
	 * @param toDelete
	 * @return
	 * @throws MQException
	 * @throws IOException
	 */
	public String removeCurrentMsgAndFetchNextMsg(boolean toDelete) throws MQException, IOException {
		
		if(!isMQSTR) {
			logger.error("Not initialized to run with MQSTR messages with 546 encoding.");
			return null;
		}
		StringBuilder retStr = new StringBuilder();
		
		MQMessage getMessages = new MQMessage();
		MQGetMessageOptions gmo = new MQGetMessageOptions();
		gmo.matchOptions = MQC.MQMO_NONE;
		gmo.waitInterval = 10000;
		if(toDelete) {
			// removing the message
			gmo.options = MQC.MQGMO_MSG_UNDER_CURSOR;
			defaultLocalQueue.get(getMessages, gmo);
		}
		
		// fetch next msg
		gmo.options = MQC.MQGMO_WAIT | MQC.MQGMO_BROWSE_NEXT;
		defaultLocalQueue.get(getMessages, gmo);
		int msglen = getMessages.getMessageLength();
		
		try {
			for(int i = 0; i < msglen; i++) {
				retStr.append(getMessages.readChar());
			}
		} catch (EOFException e) {
			if(e.getMessage().contains("MQJE086")) {
				// Do Nothing. End Of File character of the message reached.
			} else {
				logger.error("EOFException occured: " + e);
			}
		}
		return retStr.toString();
	}

	public void finalize() throws MQException {
		if(defaultLocalQueue != null && defaultLocalQueue.isOpen()) {
			try {
				defaultLocalQueue.close();
			} catch (MQException e) {
				logger.error("Exception in closing message queue. " + e.getMessage());
				e.printStackTrace();
				throw e;
			}
			defaultLocalQueue = null;
		}
		if(queueManager != null && queueManager.isConnected()) {
			try {
				queueManager.disconnect();
			} catch (MQException e) {
				logger.error("Exception in closing queue manager. " + e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}
		if(queueManager != null && queueManager.isOpen()) {
			try {
				queueManager.close();
			} catch (MQException e) {
				logger.error("Exception in closing queue manager. " + e.getMessage());
				e.printStackTrace();
				throw e;
			}
			queueManager = null;
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Host Name:");
		sb.append(hostName);
		sb.append(" Port:");
		sb.append(portNumber);
		sb.append(" Queue Manager:");
		sb.append(queueManagerNameStr);
		sb.append(" Channel:");
		sb.append(channel);
		sb.append(" Queue:");
		sb.append(queueName);
		sb.append(" User:");
		sb.append(user);
		sb.append(" MQQueue object:");
		sb.append(defaultLocalQueue);
		sb.append(" MQQueueManager object:");
		sb.append(queueManager);
		return sb.toString();
	}
	
	/**
	 * This method is used for put messages in remote Queue and local queue. 
	 * This method only send message using MQFMT. This method can be translate to different encodings. 
	 * @param message		-	This is the message content
	 * @param characterSet	-	This represent the characterSet (e.x. 819)
	 * @param encoding		-	This represent the encoding(279)
	 * @throws IOException
	 * @throws MQException
	 */
	public void putMessage(final String message, final Integer characterSet, final Integer encoding) throws IOException, MQException {
		try
		{
			MQMessage putMessage = new MQMessage();
			putMessage.format = MQC.MQFMT_STRING;
			putMessage.feedback = MQC.MQFB_NONE;
			putMessage.messageType = MQC.MQMT_DATAGRAM;
			putMessage.characterSet = characterSet;
			putMessage.encoding = encoding;
			
			putMessage.writeString(message);
				
			// specify the message options...
			MQPutMessageOptions pmo = new MQPutMessageOptions();
			int openOptions =  MQC.MQOO_OUTPUT | MQC.MQOO_INQUIRE;
			defaultLocalQueue = queueManager.accessQueue(queueName, openOptions);
	
			// put the message on the queue
			defaultLocalQueue.put(putMessage, pmo);
			//complete the transaction
			queueManager.commit();
		}
		finally
		{
			//close the queue and close the queue connections 
			if ( defaultLocalQueue != null && defaultLocalQueue.isOpen() )
			{
				defaultLocalQueue.close();
			}
		}
	}
	
	/**
	 * Method to get the message from the Queue.
	 * @param characterSet 		--	indicate the characterSet to convert the message
	 * @param encoding			--	indicate the encoding to convert the message
	 * @param currentDepth	--	it is fill for the method to indicate the remaining quantity in the queue
	 * @return					--	the message content
	 * @throws MQException
	 * @throws IOException
	 */
	public String getMessage( final String queueTarget, Integer characterSet, Integer encoding ) throws MQException, IOException
	{
		String message = null;
		try {
			//create the message and set the options
			MQMessage mqMessage			=	new MQMessage ();
			mqMessage.messageId			=	MQC.MQCI_NONE;
			mqMessage.correlationId		=	MQC.MQCI_NONE;
			mqMessage.characterSet		=	characterSet;
			mqMessage.encoding			=	encoding;
			//Set the message options
			MQGetMessageOptions mqGetMessageOptions	=	new MQGetMessageOptions ();
			mqGetMessageOptions.matchOptions		=	MQC.MQMO_NONE;
			mqGetMessageOptions.options				=	MQC.MQGMO_BROWSE_FIRST | MQC.MQGMO_WAIT | MQC.MQGMO_CONVERT ;
			//set the MQ Queue Open Options, options to get the message
			int openQueueOptions					=	MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_BROWSE;
			defaultLocalQueue			=	queueManager.accessQueue( queueTarget, openQueueOptions );
			//get the message
			defaultLocalQueue.get( mqMessage, mqGetMessageOptions );
			final byte[] messageData	=	new byte [ mqMessage.getMessageLength() ];
			mqMessage.readFully	( messageData );
			message				=	new String( messageData );
			//delete the message from the queue
			mqGetMessageOptions.options	=	MQC.MQGMO_MSG_UNDER_CURSOR;
			defaultLocalQueue.get( mqMessage, mqGetMessageOptions );
		}		
		finally
		{
			if ( defaultLocalQueue != null && defaultLocalQueue.isOpen() )
			{
				defaultLocalQueue.close();
			}
		}
		return message;
	}
	
	/**
	 * This method is used for put messages in remote Queue and local queue to the given queue
	 * This method only send message using MQFMT. This method can be translate to different encodings. 
	 * @param queueTarget	-	This is the queue to send the message
	 * @param message		-	This is the message content
	 * @param characterSet	-	This represent the characterSet (e.x. 819)
	 * @param encoding		-	This represent the encoding(279)
	 * @throws IOException
	 * @throws MQException
	 */
	public void putMessage(final String queueTarget, final String message, final Integer characterSet, final Integer encoding) throws IOException, MQException {
		try
		{
			MQMessage putMessage = new MQMessage();
			putMessage.format = MQC.MQFMT_STRING;
			putMessage.feedback = MQC.MQFB_NONE;
			putMessage.messageType = MQC.MQMT_DATAGRAM;
			putMessage.characterSet = characterSet;
			putMessage.encoding = encoding;
			
			putMessage.writeString(message);
				
			// specify the message options...
			MQPutMessageOptions pmo = new MQPutMessageOptions();
			int openOptions =  MQC.MQOO_OUTPUT | MQC.MQOO_INQUIRE;
			defaultLocalQueue = queueManager.accessQueue( queueTarget, openOptions);
	
			// put the message on the queue
			defaultLocalQueue.put(putMessage, pmo);
			//complete the transaction
			queueManager.commit();
		}
		finally
		{
			//close the queue and close the queue connections 
			if ( defaultLocalQueue != null && defaultLocalQueue.isOpen() )
			{
				defaultLocalQueue.close();
			}
		}
	}

}
