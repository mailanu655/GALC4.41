package com.honda.galc.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.honda.galc.common.exception.ServiceInvocationException;
import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.common.logging.Logger;

/**
 * 
 * <h3>AbstractRequestInvoker Class description</h3>
 * <p> AbstractRequestInvoker is an abstract class to handle the life cycle of communicating 
 * to a server endpoint.  </p>
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
 * @author Jeffray Huang<br>
 * Mar 29, 2010
 *
 */
public abstract class AbstractRequestInvoker<T> {
    protected ObjectOutputStream oos = null;
    protected ObjectInputStream ois = null;
    
    
    public Object invoke( T request) {
            
    	long start = System.currentTimeMillis();
		
        initConnection();
        
        Object obj = null;
        
        try {
            send(request);
            obj = receive();
        }finally{
            close();
        }

        long end = System.currentTimeMillis();
       
        Logger.getLogger().debug((end-start), "Invoked " + request);
 	   	    
        return obj;
            
    }
    
    protected void send(T request) {
        
        try {
            if(oos == null)oos = getOutputStream();
            oos.writeObject(request);
            oos.flush();

        } catch (IOException e) {
            throw new ServiceInvocationException("Failed to write request " + request == null ? "null" : request.toString() +
                            " to " + getServerAddress() + " due to " + e.toString()) ;
        }
    }
    
    
    protected Object receive() {
        Object obj = null;
        try {
            if(ois == null) ois = getInputStream();
            obj = ois.readObject();
        } catch (IOException e) {
            throw  new ServiceInvocationException("Failed to receive reply from " +
                            " to " + getServerAddress() + " due to " + e.toString(), e) ;
        } catch (ClassNotFoundException e) {
            throw  new ServiceInvocationException("Failed to receive reply from " +
                            " to " + getServerAddress() + " due to " + e.toString(), e) ;
        } 
        return obj;
    }
    
    protected void close() {
        try{
            if(oos != null) oos.close();
            if(ois != null) ois.close();
        }catch(IOException e) {
            throw new ServiceInvocationException("Error to close connection at " + getServerAddress() + "due to "  + e.getMessage());
        }finally{
            oos = null;
            ois = null;
        }
    }
    
    
    protected ServiceTimeoutException createServiceTimeoutException(Exception e) {
        return new ServiceTimeoutException("server at " + getServerAddress() + " is not available due to " + e.toString());
    }
    
    abstract protected void initConnection();
    abstract protected ObjectOutputStream getOutputStream()throws IOException;
    
    abstract protected ObjectInputStream getInputStream()throws IOException;
    
    abstract protected String getServerAddress();
}
