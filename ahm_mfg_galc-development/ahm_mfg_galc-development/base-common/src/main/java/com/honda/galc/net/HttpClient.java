package com.honda.galc.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import com.honda.galc.common.logging.Logger;

/**
 * 
 * <h3>HttpClient</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> HttpClient description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Sep 16, 2013</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Sep 16, 2013
 */
public class HttpClient {
	protected HttpURLConnection conn = null;
	protected Logger log = Logger.getLogger();
	protected String url = null;
	private int httpConnTimeout;
	private int httpReadTimeout;
	
	public HttpClient(String url) {
		super();
		this.url = url;
	}
	
	public HttpClient(String url, int httpConnTimeout, int httpReadTimeout) {
		this(url);
		this.httpConnTimeout = httpConnTimeout;
		this.httpReadTimeout = httpReadTimeout;
	}

	public void initConnection(String method) throws Exception{

		URL hostUrl = new URL(url);
		conn =((HttpURLConnection)hostUrl.openConnection());
		conn.setDoOutput(true);
		conn.setRequestMethod(method);
		conn.setRequestProperty("Content-Type", "application/json");
		if (httpConnTimeout>0)
			conn.setConnectTimeout(httpConnTimeout);
		if (httpReadTimeout>0)
			conn.setReadTimeout(httpReadTimeout);
	}
	
	public void initConnection(String method, Map<String, String> requestPropertyMap) throws Exception{

		URL hostUrl = new URL(url);
		conn =((HttpURLConnection)hostUrl.openConnection());
		conn.setDoOutput(true);
		conn.setRequestMethod(method);
		for(Map.Entry<String, String> entry : requestPropertyMap.entrySet()){
			conn.setRequestProperty(entry.getKey().trim(), entry.getValue().trim());
		}
		if (httpConnTimeout>0)
			conn.setConnectTimeout(httpConnTimeout);
		if (httpReadTimeout>0)
			conn.setReadTimeout(httpReadTimeout);
		
	}	
	
	private String doPost(String data, int responseCode) throws Exception {
		
		log.info("request data:" + data + " - url:" + url);
		
		OutputStream os = conn.getOutputStream();
		os.write(data.getBytes());
		os.flush();

		if (conn.getResponseCode() != responseCode) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

		StringBuilder output = new StringBuilder();
		
		String buf;
		while ((buf = br.readLine()) != null) 
			output.append(buf);

		log.info("response data:" + output);
		return output.toString();
		
	}
	
	private String doGet(int responseCode) throws Exception {
		
		log.info("url:" + url);

		if (conn.getResponseCode() != responseCode) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

		StringBuilder output = new StringBuilder();
		
		String buf;
		while ((buf = br.readLine()) != null) 
			output.append(buf);

		log.info("response data:" + output);
		return output.toString();
		
	}
	
	private String post(String data, int responseCode, Map<String, String> requestPropMap) {
		try {
			if(requestPropMap == null)
				initConnection("POST");
			else
				initConnection("POST", requestPropMap);
			return doPost(data, responseCode);
		} catch(SocketTimeoutException ste){
			log.error("Connection timed out while performing HTTP POST action. " +
					"Current configuration: Connect [" + httpConnTimeout + "] Read [" + httpReadTimeout + "]. " +
					ExceptionUtils.getMessage(ste));
		} catch (Exception e) {
			log.error("Unexpected error while performing HTTP POST action: " + e.getMessage());
		}finally{
			try {
				if (conn.getInputStream() != null)
					conn.getInputStream().close();
			} catch (Exception e2) {}
			try {
				if (conn.getOutputStream() != null)
					conn.getOutputStream().close();
			} catch (Exception e2) {}
			conn.disconnect();
		}
		
		return null;
		
	}
	
	private String get(int responseCode, Map<String, String> requestPropMap) {
		try {
			if(requestPropMap == null)
				initConnection("GET");
			else
				initConnection("GET", requestPropMap);
			
			return doGet(responseCode);
		} catch(SocketTimeoutException ste){
			log.error("Connection timed out while performing HTTP GET action. " +
					"Current configuration: Connect [" + httpConnTimeout + "] Read [" + httpReadTimeout + "]. " +
					ExceptionUtils.getMessage(ste));
		} catch (Exception e) {
			log.error("Unexpected error while performing HTTP GET action: " + e.getMessage());
		}finally{
			try {
				if (conn.getInputStream() != null)
					conn.getInputStream().close();
			} catch (Exception e2) {}
			try {
				if (conn.getOutputStream() != null)
					conn.getOutputStream().close();
			} catch (Exception e2) {}
			conn.disconnect();
		}
		
		return null;
		
	}
	
	public static String post (String url, String data, int responseCode) {
		HttpClient client = new HttpClient(url);
		return client.post(data, responseCode, null);
	}
	
	public static String post (String url, String data, int responseCode, int httpConnTimeout, int httpReadTimeout) {
		HttpClient client = new HttpClient(url, httpConnTimeout, httpReadTimeout);
		return client.post(data, responseCode, null);
	}

	public static String post (String url, String data, int responseCode, Map<String, String> requestPropMap) {
		HttpClient client = new HttpClient(url); 
		return client.post(data, responseCode, requestPropMap);
	}
	
	public static String post (String url, String data, int responseCode, Map<String, String> requestPropMap, int httpConnTimeout, int httpReadTimeout) {
		HttpClient client = new HttpClient(url, httpConnTimeout, httpReadTimeout);
		return client.post(data, responseCode, requestPropMap);
	}
	
	public static String get (String url, int responseCode) {
		HttpClient client = new HttpClient(url);
		return client.get(responseCode, null);

	}
	
	public static String get (String url, int responseCode, int httpConnTimeout, int httpReadTimeout) {
		HttpClient client = new HttpClient(url, httpConnTimeout, httpReadTimeout);
		return client.get(responseCode, null);

	}
	
	public static String get (String url, int responseCode, Map<String, String> requestPropMap) {
		HttpClient client = new HttpClient(url);
		return client.get(responseCode, requestPropMap);

	}
	
	public static String get (String url, int responseCode, Map<String, String> requestPropMap, int httpConnTimeout, int httpReadTimeout) {
		HttpClient client = new HttpClient(url, httpConnTimeout, httpReadTimeout);
		return client.get(responseCode, requestPropMap);

	}

	public int getHttpConnTimeout() {
		return httpConnTimeout;
	}

	public void setHttpConnTimeout(int httpConnTimeout) {
		this.httpConnTimeout = httpConnTimeout;
	}

	public int getHttpReadTimeout() {
		return httpReadTimeout;
	}

	public void setHttpReadTimeout(int httpReadTimeout) {
		this.httpReadTimeout = httpReadTimeout;
	}
	
}
