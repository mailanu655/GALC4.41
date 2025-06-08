package com.honda.galc.rest.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

/**
 * @author Subu Kathiresan
 * @date Jul 10, 2013
 */
public class JsonPostTest {

	private final String USER_AGENT = "Mozilla/5.0";
	
	@Test
	public void testJsonSendObjectAndPrimitive() {
		try {
			String url = "http://localhost:9080/RestWeb/MapFeatureLocationDao/getFeatureLocations";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
			//add request header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	 
			String postData = "{\"com.honda.galc.entity.product.MbpnProduct\":{\"productId\": \"19XFB5F58CE000005\",\"currentOrderNo\": \"A1234B\",\"currentProductSpecCode\": \"XSIMBA5\"}, \"com.honda.galc.rest.util.JsonInt\":{\"val\":1}}";
	 
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			con.getOutputStream().write(postData.getBytes());
			con.getOutputStream().flush();
			con.getOutputStream().close();
	 
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Post parameters : " + postData);
			System.out.println("Response Code : " + responseCode);
	 
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
	 
			//print result
			System.out.println(response.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}    
	}
	
	@Test
	public void testJsonSendEnum() {
		try {
			String url = "http://localhost:9080/RestWeb/MapFeatureLocationDao/getFeatureLocations";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
			//add request header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	 
			String postData = "{\"com.honda.galc.entity.enumtype.ApplicationType\":\"TEAM_LEAD\"}";
	 
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			con.getOutputStream().write(postData.getBytes());
			con.getOutputStream().flush();
			con.getOutputStream().close();
	 
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Post parameters : " + postData);
			System.out.println("Response Code : " + responseCode);
	 
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
	 
			//print result
			System.out.println(response.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}    
	}
}
