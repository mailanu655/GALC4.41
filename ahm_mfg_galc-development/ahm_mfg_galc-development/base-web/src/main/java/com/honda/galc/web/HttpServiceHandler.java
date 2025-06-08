package com.honda.galc.web;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.honda.galc.common.exception.BaseException;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.net.Request;

/**
 * 
 * <h3>HttpServiceHandler Class description</h3>
 * <p> HttpServiceHandler description </p>
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
 * Oct 24, 2010
 *
 *
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class HttpServiceHandler extends AbstractHttpServiceHandler{

	
	private static final long serialVersionUID = 1L;
	private static final String JPA_SERVER = "JPA Server";
	
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws javax.servlet.ServletException, java.io.IOException{
			process(request,response);
//			logCurrentCell();
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{
		process(request,response);
//		logCurrentCell();
	}
	
	public void process(HttpServletRequest request, HttpServletResponse response){
		
		ObjectInputStream input = null;
        Object retObj = null;
        Request serviceRequest = null;
        
        try {
            input = new ObjectInputStream(request.getInputStream());
        } catch (IOException e) {
            retObj = handleException(e,null);
        }
        
        // parse request from HttpRequest
        if(input != null) {
            try {
                serviceRequest = (Request) input.readObject();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                retObj = handleException(e,null);
            }
        }
        
        if(serviceRequest != null) {
            
        	Object serviceBean = null;
 
        	if(serviceRequest.getTargetClass() == null) {
            	
            	if(serviceRequest.isCreateSessionMethod()) {
            		// create new session
            		request.getSession(true);
            		
            		getLogger().info("created a new session");
            		
            	}else if(serviceRequest.isDestroySessionMethod()) {
            		
            		HttpSession session = request.getSession(false);
                    
        			if (session != null)
        				session.invalidate();   	
                 	
        			getLogger().info("destroyed current session");
            	}
            }else {
        	
	            // get service bean (Dao or service)
	            try {
	                serviceBean = ApplicationContextProvider.getBean(serviceRequest.getTargetClass());
	            }catch(Exception e) {
	                retObj = handleException(e,serviceRequest);
	            }
	            
	            // invoke service bean
	            if(serviceBean != null) {
	                try {
	            		
	                	retObj = invokeService(serviceRequest,serviceBean);
	            		
	                }catch (Exception e) {
	                    retObj = handleException(e,serviceRequest);
	                }
	            }
            }
        }
        
        // output return value
        JpaOutputStream output = null;
        try {
            output = new JpaOutputStream(response.getOutputStream());
            output.outputObject(retObj);
            output.flush();
            output.close();
        } catch (IOException e) {
            handleException(e,serviceRequest);
        }
		
	}
	
	private Object invokeService(Request serviceRequest, Object serviceBean) throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		long start = System.currentTimeMillis();
		
		Object retObj = serviceRequest.invoke(serviceBean);
		
		long end =  System.currentTimeMillis();
    	long timespan = end - start;
    	
    	String message = "Invoke " + serviceRequest + " completed";
    	int size = 0;
		if(retObj instanceof List) {
        	size = ((List)retObj).size();
        	message += " and Result list size : " + size;
		}	
        if(size > propertyBean.getResultListWarningThreshold() || timespan > propertyBean.getExecutionTimeWarningThreshold()) 
        		getLogger().warn(timespan, message);
        	else 
        		getLogger().debug(timespan, message);
        
        return retObj;
        
	}
	

	private Throwable handleException(Exception e, Request request) {
	 // translate system exception to GALC exceptions
        Throwable throwable = translateException(e);
        // log system exception
        if(!(e instanceof BaseException || (e.getCause() instanceof BaseException))){
            e.printStackTrace();
            if(request != null)
                getLogger().error(e,"Failed to process request " + request + " due to " + ((Throwable)throwable).getMessage());
            else
                getLogger().error(e,"Failed to handle http request due to ",((Throwable)throwable).getMessage());
        } 
        return throwable;
	}
	

	@Override
	protected String getHandlerId() {
		return JPA_SERVER;
		
	}
	


}
