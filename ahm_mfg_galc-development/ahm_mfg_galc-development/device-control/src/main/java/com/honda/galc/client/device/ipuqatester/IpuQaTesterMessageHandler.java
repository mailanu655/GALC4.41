package com.honda.galc.client.device.ipuqatester;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.Socket;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.InputSource;

import com.honda.galc.common.logging.LogRecord;

/**
 * @author Subu Kathiresan
 * @date March 20, 2012
 */
public class IpuQaTesterMessageHandler extends Thread {

    private final int EOD = 0x00;
    private final int EOM = 0xFF;
    public static final String DIAG = "DIAG";
    public static final String TRES = "TRES";
    public static final String RINP = "RINP";
    public static final String ISO_8859_1 = "ISO-8859-1";
    
	private Socket _socket = null;
	private IpuQaTesterSocketDevice _device = null;
	private BufferedReader _rd = null;
	private OutputStream _wr = null;
	
	public IpuQaTesterMessageHandler(IpuQaTesterSocketDevice device, Socket socket) {
		_socket = socket;
		_device = device;
		getDevice().getLogger().info("New socket connection accepted from " + socket.getInetAddress().getHostAddress() + " on port: " + socket.getPort());
	}
	
	public void run() {
		StringBuffer strBuffer = new StringBuffer();
		try {
			if (getSocket() != null && getSocket().isConnected()) {
				int aChar = -1;
				while ((aChar = getBufferedReader().read()) != EOM 
						&& aChar != -1) { 
					strBuffer.append((char) aChar);
				}
				if (strBuffer.length() > 1) {
					getDevice().processMessage(strBuffer);
					sendReplyToQaTesterDevice(strBuffer);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			getDevice().getLogger().error("Unable to process received IPU QA Tester message: " + strBuffer);
		}
	}
	
	/**
	 * Send reply back to QA Tester device
	 * 
	 * @param strBuf
	 */
	private void sendReplyToQaTesterDevice(StringBuffer strBuf) {
		try {
			if (validateMessage(strBuf))
				sendOk(strBuf);
			else
				sendNg(strBuf);
		} catch (Exception ex) {
			ex.printStackTrace();
			getDevice().getLogger().error("Could not send reply to IPU QA Tester device.  Received Message: " + strBuf);
		}
	}

	private boolean validateMessage(StringBuffer strBuf) {
		boolean isValid = true;
		
		if (strBuf.length() < 1)
			return false;
		
		try {
	        String header = strBuf.substring(0, 16);
	        String msg = header.substring(9, 13);
	        String footer = strBuf.substring(strBuf.length() - 1);
	        String data = strBuf.substring(16).trim();
	        getDevice().getLogger().debug("Validating XML: " + LogRecord.replaceNonPrintableCharacters(data));
	
	        if (!msg.equals(TRES) && !msg.equals(DIAG) && !msg.equals(RINP)) {
	            isValid = false;
	            getDevice().getLogger().error("MessageType: " + msg);
	        }
	
	        if (!header.substring(13, 15).equals("\u0000\u0000")) {
	            isValid = false;
	            getDevice().getLogger().error("header format (status) error: " + header.substring(13, 15));
	        }
	
	        if (!header.substring(15, 16).equals("\u0000")) {
	            isValid = false;
	            getDevice().getLogger().error("header format (eod) error: " + header.substring(15, 16));
	        }
	
	        if (!footer.equals("\u0000")) {
	            isValid = false;
	            getDevice().getLogger().error("end of message format error: " + footer);
	        }
	        
	        if (msg.equals(TRES)) {
	        	try {
	        		Transformer transformer = TransformerFactory.newInstance().newTransformer();
	        		Source source = new SAXSource(new InputSource(new StringReader(data)));
	        		Result result = new SAXResult(new IpuQaTesterSAXContentHandler());
	            	transformer.transform(source, result);
	            } catch(Exception ex) {
	            	ex.printStackTrace();
	            	isValid = false;
	    			getDevice().getLogger().error("Validation failed.  Received Message: " + strBuf);
	            }
	        }
		} catch (Exception ex) {
			isValid = false;
			ex.printStackTrace();
			getDevice().getLogger().error("Validation failed.  Received Message: " + strBuf);
		}
		return isValid;
	}
	
	public void sendOk(StringBuffer strBuf) {
		sendReplyMessage(strBuf, true);
	}
	
	public void sendNg(StringBuffer strBuf) {
		sendReplyMessage(strBuf, false);
	}
	
    public void sendReplyMessage(StringBuffer strBuf, boolean ok) {
    	
    	// Example header 'A61E02T01TRES[0][0][0]'
        String mfgId = strBuf.substring(0, 3);			// A61
        String target = strBuf.substring(3, 6);			// E02
        String terminalId = strBuf.substring(6, 9);		// T01
        String msg = strBuf.substring(9, 13);			// TRES
        String data = strBuf.substring(16);				// XML message
        
        byte[] byteHeader = null;
        byte[] byteData = null;
        byte[] bytes = null;

        try {
            StringBuffer sbfReplyValue = new StringBuffer();
            while (sbfReplyValue.length() < 20) {
                sbfReplyValue.append('0');
            }
            
            if (msg.equals(TRES)) {			// Test RESult
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
            getDevice().getLogger().error(ex.getMessage());
        }

        try {
			getOutStream().write(bytes);
			getOutStream().flush();
			logReply(bytes);
		} catch (IOException ex) {
			ex.printStackTrace();
			getDevice().getLogger().error("Could not send reply to IPU QA Tester device.  Received Message: " + strBuf);
		}
    }

	private void logReply(byte[] bytes) {
		try {
			StringBuffer reply = new StringBuffer();
			for (byte singleByte: bytes) {
				reply.append(singleByte);
			}
			getDevice().getLogger().info("Sent reply to IPU QA Tester device: " + LogRecord.replaceNonPrintableCharacters(reply));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	   
	public Socket getSocket() {
		return _socket;
	}
	
	public IpuQaTesterSocketDevice getDevice() {
		return _device;
	}
	
	public BufferedReader getBufferedReader() throws IOException {
		if (_rd == null) {
			_rd = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
		}
		return _rd;
	}
	
	public OutputStream getOutStream() throws IOException {
		if (_wr == null) {
			_wr = getSocket().getOutputStream();
		}
		return _wr;
	}
}
