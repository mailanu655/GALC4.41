/**
 * 
 */
package com.honda.galc.device.let;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * @date Feb 3, 2012
 */
public class HandHeldDeviceXmlSendThread extends Thread {
	
	private final int EOM = 0xFF;
	private String _testFilePath = "";
	private Socket _socket = null;
	private BufferedReader _rd = null;
	private OutputStream _wr = null;
	private volatile boolean _isDone = false;
	private String header = null;
	private volatile boolean processFromScreen = false;
	public static final int HEADER_LENGTH = 16;
	public static int FOOTER_LENGTH = 2;
	
	public HandHeldDeviceXmlSendThread(Socket socket, String header, String filePath) {
		super(HandHeldDeviceXmlSendThread.class.getSimpleName());
		_socket = socket;
		this.header = header;
		_testFilePath = filePath;
	}
	
	public HandHeldDeviceXmlSendThread(Socket socket, String header, String filePath, boolean processFromScreen) {
		super(HandHeldDeviceXmlSendThread.class.getSimpleName());
		_socket = socket;
		this.header = header;
		_testFilePath = filePath;
		this.processFromScreen = processFromScreen;
	}
	
	

	/**
	 * send xml content to the socket client
	 */
	public void run() {
		try {
			getSocket().setKeepAlive(true);
			sendXml();
			receiveGalcResponse();
			_isDone = true;
		}
		catch(Exception ex)	{
			ex.printStackTrace();
		} finally {
			try {
				getSocket().close();
			} catch (Exception ex) {
				Logger.getLogger().error(ex,"Unable to close client socket: " + StringUtils.trimToEmpty(ex.getMessage()));
			}
		}
	}

	private void sendXml() throws InterruptedException, IOException {
		if  (getSocket() != null && getSocket().isConnected() 
				&& getSocket().isBound() && !getSocket().isClosed()) {

			String xmlContent = "";
			if(processFromScreen){
				xmlContent = _testFilePath;
			}else{
				xmlContent = getXmlContent();
			}
			StringBuffer strBufHeader = new StringBuffer(header);
			byte[] bytes = new byte[HEADER_LENGTH + xmlContent.length() + FOOTER_LENGTH];
			
			System.arraycopy(strBufHeader.toString().getBytes(), 0, bytes, 0, strBufHeader.length());
	        
			System.arraycopy(xmlContent.toString().getBytes(), 0, bytes, HEADER_LENGTH, xmlContent.length());
			
			// add footer
			bytes[HEADER_LENGTH + xmlContent.length() + 1] = (byte) EOM;

			getWriter().write(bytes);
			getWriter().flush();
		}
	}
	
	private void receiveGalcResponse () {
		StringBuilder strBldr = new StringBuilder();
		try {
			if (getSocket() != null && getSocket().isConnected() && !getSocket().isClosed()) {
				int aChar = -1;
				long startTime = System.currentTimeMillis();
				long endTime = startTime + 30000;
				while ((System.currentTimeMillis() < endTime) && (aChar = getReader().read()) != EOM) { 
					if (aChar != -1) {
						strBldr.append((char) aChar);
					}
				}
				if (strBldr.length() > 0) {
					Logger.getLogger().error("Received GALC response: " + StringUtil.toHexString(strBldr));
				} else {
					Logger.getLogger().error("GALC response time out. ");
				}
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Logger.getLogger().error(ex,"Unable to process received message: " + strBldr);
		}
	}
	
	private String getXmlContent() {
		StringBuffer strBuf = new StringBuffer();
		FileInputStream fIn = null;
		try {
			fIn = new FileInputStream(_testFilePath);
			int aChar = -1;
			while ((aChar = fIn.read())!= -1) {
				strBuf.append((char) aChar);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fIn.close();
			} catch (Exception ex) {
				Logger.getLogger().error(ex,"Unable to close file input stream: " + StringUtils.trimToEmpty(ex.getMessage()));
			}
		}

		return strBuf.toString().trim();
	}
	
	public boolean isDone() {
		return _isDone;
	}

	public Socket getSocket() {
		return _socket;
	}
	
	public BufferedReader getReader() throws IOException {
		if (_rd == null) {
			_rd = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
		}
		return _rd;
	}
	
	public OutputStream getWriter() throws IOException {
		if (_wr == null) {
			_wr = getSocket().getOutputStream();
		}
		return _wr;
	}
}