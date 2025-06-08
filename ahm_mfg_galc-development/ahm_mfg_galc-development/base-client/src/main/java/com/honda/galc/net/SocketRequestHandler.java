package com.honda.galc.net;

import java.net.Socket;
import java.net.SocketException;
import java.io.*;

import com.honda.galc.common.exception.DataConversionException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerXMLUtil;
import com.honda.galc.data.DefaultDataContainer;


public class SocketRequestHandler extends Thread {
	private SocketRequestDispatcher socketRequestDispatcher = null;
	private Socket socket = null;

	private static final DataContainer asyncResponseDC = new DefaultDataContainer();
	
	static {
        asyncResponseDC.put(DataContainerTag.DATA, "1");
        asyncResponseDC.put(DataContainerTag.CLIENT_ID, "ACK");
	}
	
    /**
     * Don't let anyone instantiate this class.
     */
    @SuppressWarnings("unused")
    private SocketRequestHandler() {
    }
    /**
     * Receive the processing socket,
     * and create the new SocketRequestHandler object. 
     * @param aGroup ThreadGroup
     * @param aClientSocket Socket
     * @param aDispatcher SocketRequestDispatcher Class
     */
    public SocketRequestHandler(
        Socket aClientSocket, 
        SocketRequestDispatcher aDispatcher) {
        	
      this.socket = aClientSocket;
      try {
		this.socket.setSoTimeout(30*1000); // 30 seconds
      } catch (SocketException e) {
		e.printStackTrace();
      }
      
      this.socketRequestDispatcher = aDispatcher;
    }
    /**
     * Process the request of Client.
     */
    public void run() {
    	BufferedInputStream bis = null;
    	InputStream is = null;
    	try {
    		is= socket.getInputStream();
    		if(this.socketRequestDispatcher.isSupportLegacy()){
    			parseLegacyObject(is);
    			return;
    		}
    		bis = new BufferedInputStream(is);
			 if(useXML(bis))parseDeviceData(bis);
			 else processObject(bis);
		} catch (Exception e) {
			Logger.getLogger().error(e, this.getClass().getSimpleName());
			e.printStackTrace();
		}finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        
    }     
      

    public boolean useXML(BufferedInputStream bis) throws IOException {
    	
    	String str = "<?xml";
    	boolean useXML = true;
    	for(int i = 0; i<str.length();i++) {
 			if((char)bis.read() != str.charAt(i)) {
 				useXML = false;
 				break;
 			}
    	}
    	bis.reset();
    	return useXML;
    }
    
    public void parseDeviceData(InputStream is)  {
    	
    	try{
	    	 DataContainer dc = DataContainerXMLUtil.readDeviceDataFromXML(is);
	    	
	    	 String synchMode = (String) dc.remove(DataContainerTag.EI_SYNC_MODE);
	         boolean isAsync = synchMode != null && synchMode.equals(DataContainerTag.EI_ASYNC);
	         Logger.getLogger().debug("input data container:" + dc.toString());
	         
	    	 if(isAsync) {
	    		 sendReturnDC(asyncResponseDC);
	    	 }
	    	 DataContainer returnDC = null;
	    	 try {
	    		 returnDC = this.socketRequestDispatcher.dispatchDeviceData(dc);
	    	 }catch(Exception e) {
	    		 returnDC = setProcessIncompleteContainer(dc.getClientID());
	    	 }
	    	 
	    	 if(returnDC == null)
	    		 returnDC = setProcessIncompleteContainer(dc.getClientID());
	    	 
	    	 Logger.getLogger().debug("return data container:" + returnDC.toString());
	    	 
	    	 if(!isAsync) sendReturnDC(returnDC);
    	}catch(DataConversionException e) {
    		Logger.getLogger().error(e, "Error to parse device data.");
    	}
	    	 
    }
    
    
	private DataContainer setProcessIncompleteContainer(String replyClientId) {
    	DataContainer result = new DefaultDataContainer();
    	result.put(DataContainerTag.CLIENT_ID, replyClientId);
		result.put(DataContainerTag.PROCESS_COMPLETE, "0");
    	return result;
    }
    
    private void sendReturnDC(DataContainer returnDC) {
    	OutputStream os = null;
    	try {
			os = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			Logger.getLogger().error(e, "Exception occured when sending return DC.");
		}
    	if(os != null) {
    		DataContainerXMLUtil.convertToXML(returnDC, os);
    		try {
				os.flush();
			} catch (IOException e) {
				e.printStackTrace();
				Logger.getLogger().error(e, "Exception occured at flush when sending return DC.");
			}
    	}
    }
    
    private Object readObject(BufferedInputStream bis) {
    	ObjectInputStream ois = null;
        
    	try {
	    	ois = new ObjectInputStream(bis);
			return ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
			Logger.getLogger().error(e, "Exception occured when reading object from socket");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			Logger.getLogger().error(e, "Exception occured when reading object from socket");
		}finally{
			try {
                if(ois != null) ois.close();
            } catch (IOException e) {
            	Logger.getLogger().error(e, "Exception occured when closing object input stream");
            }
		}
	
		return null;
    }
    
    private void processObject(BufferedInputStream bis){
    	Object obj = readObject(bis);
    	if(obj == null) return;
    	if(obj instanceof Request) parseRequest((Request)obj);
     }
    
    public void parseRequest(Request request) {
    	
    	Object obj = socketRequestDispatcher.dispatchRequest(request);
    	if (obj != null) {
    		ObjectOutputStream oos = null;
    		try {
    			oos = new ObjectOutputStream(socket.getOutputStream());
    		} catch (IOException e) {
    			e.printStackTrace();
    			Logger.getLogger().error(e, "Exception occured in parseRequest");
    		}
    		if(oos != null) {
    			try {
    				oos.writeObject(obj);
    				oos.flush();
    				oos.close();
    			} catch (IOException e) {
    				Logger.getLogger().error(e, "Exception occured in parseRequest when writing object");
    			}
    		}
    	}
    }
    
   public void parseLegacyObject(InputStream bis) {
    	
    	ObjectInputStream ois;
    	
    	com.honda.global.galc.common.data.DataContainer dc = null;
    	ObjectOutputStream oos = null;
        try {
        	ois = new ObjectInputStream(bis);
    		oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeUTF("READY");
            oos.flush();
            ois.readBoolean(); // sync flag;
            dc = ( com.honda.global.galc.common.data.DefaultDataContainer)ois.readObject();
            this.socketRequestDispatcher.dispatchToLegacyDataContainerListener(dc);
            
            oos.writeUTF("TERMINATE");
            oos.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
 
}
