
package com.honda.galc.rest.test;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.honda.galc.dto.rest.WeldProductTrackDTO;
import com.honda.galc.rest.json.JsonContentHandler;
import com.honda.galc.service.weld.WeldOrderService;

/**
 *
 * @author Wade Pei <br>
 * @date   Oct 24, 2013
 */
public class WeldOrderServiceTest {

	Gson gson;
	WeldOrderService service;
	
	@Before
	public void setUp() throws Exception {
		gson = JsonContentHandler.getGson();
		service = null;//new WeldOrderServiceImpl();
	}


	@Test
	public void testTrackProduct() {
		Object response = testRestServicePost(generateJsonRequestForTrack(), "http://localhost:8080/RestWeb/WeldOrderService/trackProduct");
		String retString = response.toString();
		System.out.println(retString);
	}

	private Object testRestServicePost(String jsonStr, String urlStr) {
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			System.out.println(jsonStr);
			OutputStream os = conn.getOutputStream();
			os.write(jsonStr.getBytes());
			os.flush();
			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				fail("Failed : HTTP error code : " + conn.getResponseCode());
				return null;
			}
			//Object response = null;
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String responseStr = br.readLine();
			conn.disconnect();
			
			return responseStr;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Failed with exception " + e);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed with exception " + e);
		}
		return null;
	}
	
	private String generateJsonRequestForTrack() {
		WeldProductTrackDTO trackDTO = new WeldProductTrackDTO();
		trackDTO.setOrderNo("HWE 024BP308455801");
		trackDTO.setProcessPointId("WeldPP");
		trackDTO.setProductId("2111303071145166W");
		trackDTO.setProductSpecCode("77102TR6 C000     NH686L");
		StringBuilder result = new StringBuilder("{\"com.honda.galc.dto.rest.ProductTrackDTO\":");
		result.append(gson.toJson(trackDTO));
		result.append("}");
		return result.toString();
	}
	
}
