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
import com.honda.galc.rest.json.JsonContentHandler;

/**
 * 
 * @author Keifer Xing <br>
 *         Oct. 22, 2013
 */
public class PartServiceTest {
	Gson gson;

	@Before
	public void setUp() throws Exception {
		gson = JsonContentHandler.getGson();
	}

	@Test
	public void testSaveSingleProduct() {
		try {
			URL url = new URL("http://localhost:8080/RestWeb/DataCollectionService/execute");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			String jsonstrings = "{" +
					"'com.honda.galc.data.DefaultDataContainer':" +
						"{" +
							"'Ride Height Left Front.MEASUREMENT_VALUE':'1'," +
							"'Ride Height Right Front.PART_SERIAL_NUMBER':'1'," +
							"'Ride Height Right Rear.INSTALLED_PART_STATUS':'1'," +
							"'APPLICATION_ID':'KF0001'," +
							"'Ride Height Left Rear.PART_SERIAL_NUMBER':'1'," +
							"'PRODUCT_ID':'5FNRL18512B000002'," +
							"'Ride Height Left Front.INSTALLED_PART_STATUS':'1'," +
							"'Ride Height Left Rear.MEASUREMENT_VALUE':'1'," +
							"'CLIENT_ID':'RestService_299_Test_Req'," +
							"'Ride Height Right Front.MEASUREMENT_VALUE':'1'," +
							"'Ride Height Left Front.PART_SERIAL_NUMBER':'1'," +
							"'Ride Height Left Rear.INSTALLED_PART_STATUS':'1'," +
							"'Ride Height Right Rear.MEASUREMENT_VALUE':'1'," +
							"'PROCESS_POINT_ID':'KF0001'," +
							"'SENDER__HOST':'xpofi2:10.142.254.217'," +
							"'Ride Height Right Rear.PART_SERIAL_NUMBER':'1'," +
							"'Ride Height Right Front.INSTALLED_PART_STATUS':'1'" +
						"}" +
					"}";
			OutputStream os = conn.getOutputStream();
			os.write(jsonstrings.getBytes());
			os.flush();
			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				fail("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			System.err.println(br.readLine());
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Failed with exception " + e);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed with exception " + e);
		}
	}
}
