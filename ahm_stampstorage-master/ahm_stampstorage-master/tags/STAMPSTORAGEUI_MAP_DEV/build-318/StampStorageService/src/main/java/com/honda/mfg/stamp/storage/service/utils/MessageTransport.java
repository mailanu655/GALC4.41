// MessageTransport.java

package com.honda.mfg.stamp.storage.service.utils;

import com.honda.io.socket.CharSets;
import com.honda.mfg.connection.messages.MessageRequest;
import com.honda.mfg.connection.processor.ConnectionMessageSeparators;
import com.honda.mfg.connection.processor.messages.ConnectionRequest;
import com.honda.mfg.connection.processor.messages.PingMessage;
import com.honda.mfg.stamp.conveyor.messages.JsonServiceWrapperMessage;
import com.honda.mfg.stamp.conveyor.messages.Message;
import com.honda.mfg.stamp.conveyor.processor.messagebuilders.json.JSonResponseParser;
import com.honda.mfg.stamp.storage.service.clientmgr.StampServiceSocketConnection;
import org.apache.derby.iapi.util.ByteArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Calendar;

/**
 * Utility class to send and receive messages over socket streams.
 * This class uses <END_OF_MESSAGE> sequence including <> to separate messages
 * @author VCC44349
 *
 */
public class MessageTransport implements Runnable
{
    private static final Logger LOG = LoggerFactory.getLogger(MessageTransport.class);
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	BufferedReader reader = null;
	BufferedWriter writer = null;
	private MessageSinkInterface messageSink = null;
	private StampServiceSocketConnection objectSource = null;
	private volatile boolean done = false;
	private static final int READ_BUF_SZ = 512;

	public boolean isDone() {
		return done;
	}
	public void setDone(boolean done) {
		this.done = done;
	}
	public MessageTransport(InputStream is, OutputStream os, StampServiceSocketConnection objectSource)
	{
		this.objectSource = objectSource;

		try
		{
			dis = new DataInputStream(is);
			dos = new DataOutputStream(os);
            reader = new BufferedReader(new InputStreamReader(dis, CharSets.ISO_8859_1));
            writer = new BufferedWriter(new OutputStreamWriter(dos, CharSets.ISO_8859_1));
		}
		catch(Exception e)
		{
			LOG.error("MessageTransport.constructor:" + e, 30);
			e.printStackTrace();
		}
	}
	public MessageSinkInterface getMessageSink()
	{
		return messageSink;
	}
	public void setMessageSink(MessageSinkInterface messageSink)
	{
		this.messageSink = messageSink;
	}

	public Object getObjectSource()
	{
		return objectSource;
	}

	public void start()
	{
		try
		{
			process();
		}
		catch(Exception e)
		{
			LOG.error("MessageTransport.process(): Exception, main process loop: "+ e.getMessage());
			e.printStackTrace();
		}
	}

	public void run()
	{
		try
		{
			process();
		}
		catch(Exception e)
		{
			LOG.error("MessageTransport.process(): Exception, main process loop: "+ e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * reads and processes messages
	 * PingMessage results in an immediate PingMessage response
	 * Other message types are processed by {CommandProcessor}
	 * @throws IOException
	 */
	public void process() throws IOException {
		char[] cBuf = null;
		int loopCount = 0;
		StringBuffer sBuf = new StringBuffer("");
		JSonResponseParser jSonParser = new JSonResponseParser();
		String messageSeparator = ConnectionMessageSeparators.DEFAULT.getSeparator();

		while (!done) {
			LOG.info("MessageTransport.process(): loop iteration: " + (++loopCount));

			try {
				cBuf = readObjectAsCharArray();

				String leftMessage = null, rightPartial = null;
				sBuf.append(cBuf);
				int indexSeparator = sBuf.indexOf(messageSeparator);
				if (indexSeparator >= 0) {
					leftMessage = sBuf.substring(0, indexSeparator);
					rightPartial = sBuf.substring(indexSeparator
							+ messageSeparator.length(), sBuf.length());
					try {
						if (leftMessage != null && leftMessage.contains("PING")) {
							objectSource.setPingReceivedFlag();
							PingMessage p = new PingMessage(Calendar.getInstance().getTime()
									.toString(), true);
							MessageRequest request = new ConnectionRequest(p, messageSeparator);
							writeObject(request);
							LOG.info("MessageTransport.process(): PING message received, sent: "
									+ p);
						}
						// The CarrierUpdateRequestMessage type is hard-coded
						// for now, will be made generic later
						else if(leftMessage != null){
							Message thisMessage = (Message) jSonParser.parse(
									leftMessage, JsonServiceWrapperMessage.class); //CarrierUpdateRequestMessage.class);
							if (messageSink != null) {
								messageSink.put(objectSource, thisMessage);
								LOG.info("MessageTransport.process(): message received) "
										+ thisMessage);
							}
						}
					} catch (Exception ex) {
						LOG.error("MessageTransport.process():continue loop with exception\n" + ex);
						ex.printStackTrace();
						continue;
					} finally {
						if (rightPartial != null && !rightPartial.isEmpty()) {
							sBuf = new StringBuffer(rightPartial.trim());
						}
						else  {
							sBuf = new StringBuffer();							
						}
					}
				}
			} catch (Exception e) {
				LOG.error("MessageTransport.process():exit loop with exception\n" + e);
				e.printStackTrace();
				break;
			}
		}
		LOG.info("MessageTransport.process(): Exit loop.");
	}
	
	public void writeObject(MessageRequest req) throws Exception
	{
		try
		{
			if(req != null && req.getMessageRequest() != null)  {
				byte[] ba = req.getMessageRequest().getBytes();
				dos.write(ba, 0, ba.length);
				dos.flush();
	            LOG.info("MessageTransport.writeObject: " + req.getMessageRequest());			
			}
		}
		catch(Exception e)
		{
			//NotSerializableException
			//InvalidClassException
			//IOException
			LOG.error("MessageTransport.writeObject():" + e, 30);
			e.printStackTrace();
			throw e;
		}
	}
	public ByteArray readObjectAsByteArray()
		throws InterruptedIOException, StreamCorruptedException, EOFException, IOException, Exception
	{
		ByteArray ret = null;
		try
		{
			byte[] buf = new byte[READ_BUF_SZ];
			int nRead = dis.read(buf);
			if(nRead > 0)  {
				ret = new ByteArray(buf, 0, nRead);
			}
		}
		catch(Exception ex)
		{
			//StreamCorruptedException
			//InterruptedIOException
			//EOFException
			//IOException
			LOG.error("MessageTransport.readObject():" + ex, 30);
			ex.printStackTrace();
			throw ex;
		}

		return ret;
	}

	/**
	 * reads data into a character buffer, size specified by {@link MessageTransport#READ_BUF_SZ}
	 * @return {@link char[]}
	 * @throws InterruptedIOException
	 * @throws StreamCorruptedException
	 * @throws EOFException
	 * @throws IOException
	 * @throws Exception
	 */
	public char[] readObjectAsCharArray()
			throws  Exception
	{
			char[] ret = null;
			try
			{
				char[] cBuf = new char[READ_BUF_SZ];
				int nRead = reader.read(cBuf);
				if(nRead > 0)  {
					ret = new char[nRead];
					for(int i = 0; i < nRead; i++)  {
						ret[i] = cBuf[i];
					}
				}
				return ret;
				
			}
			catch(Exception ex)
			{
				//StreamCorruptedException
				//InterruptedIOException
				//EOFException
				//IOException
				LOG.error("MessageTransport.readObject():" + ex, 30);
				ex.printStackTrace();
				throw ex;
			}
	}
	
//	public void main(String args[])
//	{
//		try
//		{
//		}
//		catch(Exception e)
//		{
//			LOG.error("MessageTransport.main():" + e, 30);
//			e.printStackTrace();
//		}
//	}
}
