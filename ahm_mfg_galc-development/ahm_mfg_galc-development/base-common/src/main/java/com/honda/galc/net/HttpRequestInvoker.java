package com.honda.galc.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.common.logging.Logger;


/**
 * 
 * <h3>HttpRequestInvoker Class description</h3>
 * <p> HttpRequestInvoker is for communication to http endpoint </p>
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

public class HttpRequestInvoker extends AbstractRequestInvoker<Request>{
	
	private static final String SESSION_COOKIE_NAME = "JSESSIONID";  
    
	private static int connectionTimeout = 0;
    

	private String url ;
	private HttpURLConnection urlConnection = null;
    private String sessionCookieValue;
    
    private HttpRequestInvoker() {
        
    }
    
    public HttpRequestInvoker (String urlString) {
        this();
        this.url = urlString;
    }
    
    public HttpRequestInvoker (String urlString,String sessionCookieValue) {
        this();
        this.url = urlString;
        this.sessionCookieValue = sessionCookieValue;
    }
	
	public void initConnection() throws ServiceTimeoutException{
        try {
            URL hostUrl = new URL(url);
            urlConnection = (HttpURLConnection)hostUrl.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            if(connectionTimeout > 0)
            	urlConnection.setConnectTimeout(connectionTimeout);
            urlConnection.setReadTimeout(0);
            urlConnection.setRequestMethod("GET");
            if(!StringUtils.isEmpty(sessionCookieValue))
            	urlConnection.setRequestProperty("cookie", sessionCookieValue);
            urlConnection.connect();
        } catch (Exception e) {
            throw createServiceTimeoutException(e);
        }    
	}
	
	public void initConnection(int timeout) throws ServiceTimeoutException{
        try {
            URL hostUrl = new URL(url);
            urlConnection = (HttpURLConnection)hostUrl.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(timeout);
            urlConnection.setRequestMethod("GET");
            if(!StringUtils.isEmpty(sessionCookieValue))
            	urlConnection.setRequestProperty("cookie", sessionCookieValue);
            urlConnection.connect();
        } catch (Exception e) {
            throw createServiceTimeoutException(e);
        }    
	}
	
	public static int getConnectionTimeout() {
		return connectionTimeout;
	}

	public static void setConnectionTimeout(int connectionTimeout) {
		HttpRequestInvoker.connectionTimeout = connectionTimeout;
	}
	
	public String createSession() {
		
		invoke(new Request(Request.CREATE_SESSION, new Object[]{}));
		return getSessionCookieValue();
		
	}
	
	public void destroySession(String sessionCookieValue) {
		
		invoke(new Request(Request.DESTROY_SESSION, new Object[]{}));
		
	}
	
	private String getSessionCookieValue() {
		// Loop through the header fields and pull out the Session cookie
		// value		   
	   	int hdrloop = 1;
	   	do
	   	{
			String key = urlConnection.getHeaderFieldKey(hdrloop);
			if (key == null) {
				Logger.getLogger().warn("unable to create http session");
				return null;
			}

			String value = urlConnection.getHeaderField(hdrloop);

			Logger.getLogger().info("hdrloop = " + hdrloop + " key : " + key + " value :" + value );

			if (value != null && value.indexOf(SESSION_COOKIE_NAME) >= 0)
			{
				
				return value;
				
			}

			hdrloop++;
		}
		while (true);
	   	
	}
	
	public Object invoke( Request request) {
		try{
			return super.invoke(request);
		}catch(ServiceTimeoutException e) {
			ServiceMonitor.getInstance().startMonitorHttpService();
			throw e;
		}
	}    

    @Override
    public ObjectInputStream getInputStream() throws IOException {
        return new ObjectInputStream(urlConnection.getInputStream());
    }

    @Override
    public ObjectOutputStream getOutputStream() throws IOException {
        return new ObjectOutputStream(urlConnection.getOutputStream());
    }

    @Override
    public String getServerAddress() {
        return url;
    }
}
