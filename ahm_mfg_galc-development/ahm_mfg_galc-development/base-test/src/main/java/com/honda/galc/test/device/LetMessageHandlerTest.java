package com.honda.galc.test.device;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.honda.galc.net.TCPSocketFactory;
import com.honda.galc.test.dao.AbstractBaseTest;
import com.honda.galc.util.StringUtil;

/**
 * @author Abhishek Garg
 * @date Mar 2, 2016
 */
public class LetMessageHandlerTest extends AbstractBaseTest {
	
	private Socket _socket = null;
	private volatile boolean _isDone = false;
	private BufferedReader _rd = null;
	private OutputStream _wr = null;
	private final int EOM = 0xFF;
	private int _iterationNumber = -1;
	public enum TestEnv {sandbox, dev, qa};
	protected String _deviceName = "";
	protected TestEnv _env = TestEnv.sandbox;	// default environment
	protected int _port = 9000;					// default port
	protected int _maxIterations = 1000;
	public static final int HEADER_LENGTH = 16;
	public static int FOOTER_LENGTH = 2;
	
	@Test
	public void validLetXMLFileTest() {
		// TODO Auto-generated method stub
		long i = 0;
		
		long startTime = System.currentTimeMillis();
			String _testFilePath = "datafiles" + File.separator + "ValidLetXmlFileTest.xml";
			try {
				getSocket().setKeepAlive(true);
				sendXml(_testFilePath);
				_isDone = receiveGalcResponse();
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try{
					getSocket().close();
				}catch(Exception ex){
					ex.printStackTrace();
					System.out.println("Unable to close client socket: " + StringUtils.trimToEmpty(ex.getMessage()));
				}
			}
		long timeTaken = System.currentTimeMillis() - startTime;
		System.out.println("Time taken to save " + i + " LET xmls " + timeTaken + " ms");
		assertEquals(true, _isDone);
	}
	
	@Test
	public void invalidLetXMLFileTest() {
		// TODO Auto-generated method stub
		long i = 0;
		
		long startTime = System.currentTimeMillis();
			String _testFilePath = "datafiles" + File.separator + "InvalidLetXmlFileTest.xml";
			try {
				getSocket().setKeepAlive(true);
				sendXml(_testFilePath);
				_isDone = receiveGalcResponse();
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try{
					getSocket().close();
				}catch(Exception ex){
					ex.printStackTrace();
					System.out.println("Unable to close client socket: " + StringUtils.trimToEmpty(ex.getMessage()));
				}
			}
		long timeTaken = System.currentTimeMillis() - startTime;
		System.out.println("Time taken to save " + i + " LET xmls " + timeTaken + " ms");
		assertEquals(false, _isDone);
	}
	
	private String getXmlContent(String _testFilePath, int iterationNumber) {
		// TODO Auto-generated method stub
		StringBuffer strBuf = new StringBuffer();
		FileInputStream fIn = null;
		try {
			fIn = new FileInputStream(_testFilePath);
			int aChar = -1;
			while ((aChar = fIn.read()) != -1) {
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
				ex.printStackTrace();
				System.out.println("Unable to close file input stream: " + StringUtils.trimToEmpty(ex.getMessage()));
			}
		}
		
		if (iterationNumber != -1)
			return strBuf.toString().replace("@TEST_ID@", "TEST0000" + StringUtil.padLeft(new Integer(iterationNumber).toString(), 5, '0', false));
		else
			return strBuf.toString();
	}
	
	private void sendXml(String _testFilePath) throws InterruptedException, IOException {
		
		if  (getSocket() != null && getSocket().isConnected() 
				&& getSocket().isBound() && !getSocket().isClosed()) {

			StringBuffer strBufHeader = new StringBuffer("A61E02T01TRES");
			String xmlContent = (getXmlContent(_testFilePath, _iterationNumber));
			byte[] bytes = new byte[HEADER_LENGTH + xmlContent.length() + FOOTER_LENGTH];
			
			// add header
			System.arraycopy(strBufHeader.toString().getBytes(), 0, bytes, 0, strBufHeader.length());
	        
			// add message body
			System.arraycopy(xmlContent.toString().getBytes(), 0, bytes, HEADER_LENGTH, xmlContent.length());
			
			// add footer
			bytes[HEADER_LENGTH + xmlContent.length() + 1] = (byte) EOM;

			getWriter().write(bytes);
			getWriter().flush();
		}
	}
	
	private boolean receiveGalcResponse () {
		// TODO Auto-generated method stub
		StringBuilder strBldr = new StringBuilder();
		try {
			if (getSocket() != null && getSocket().isConnected() && !getSocket().isClosed()) {
				int aChar = -1;
				long startTime = System.currentTimeMillis();
				long endTime = startTime + 30000;
				while ((System.currentTimeMillis() < endTime) && (aChar = getReader().read()) != EOM) {
					if(aChar!=-1){
						strBldr.append((char) aChar);
					}
				}
				if(!StringUtils.isEmpty(strBldr.toString())){
					System.out.println("Received GALC response: "+StringUtil.toHexString(strBldr));
					return true;
				}else{
					System.out.println("Response Timed out");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Unable to process received message: "+ strBldr);
		}
		return false;
	}

	public boolean isDone() {
		// TODO Auto-generated method stub
		return _isDone;
	}

	public Socket getSocket() {
		// TODO Auto-generated method stub
		return getClientSocket();
	}
	
	public BufferedReader getReader() throws IOException {
		// TODO Auto-generated method stub
		if (_rd == null) {
			_rd = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
		}
		return _rd;
	}
	
	public OutputStream getWriter() throws IOException {
		// TODO Auto-generated method stub
		if (_wr == null) {
			_wr = getSocket().getOutputStream();
		}
		return _wr;
	}
	
	private Socket getClientSocket() {
		// TODO Auto-generated method stub
		if (_socket == null || _socket.isClosed() || !_socket.isConnected()) {
			try {
				switch(_env) {
				case sandbox:
					_port = 9000;
					_socket = TCPSocketFactory.getSocket("VFVDB16", _port, 9000);	//HMIN SBX
					break;
					
				case dev:
					_socket = TCPSocketFactory.getSocket("10.142.253.30", _port, 9000);
					break;
					
				case qa:
					_port = 41100;
					_socket = TCPSocketFactory.getSocket("aixhminjava01t", _port, 2000);
					break;
					
				default:
					break;
				}
				
				System.out.println("Virtual " + _deviceName + " device started on port " + _port);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return _socket;
	}
}