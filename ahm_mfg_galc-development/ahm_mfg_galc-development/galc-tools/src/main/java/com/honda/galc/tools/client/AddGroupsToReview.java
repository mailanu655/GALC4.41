package com.honda.galc.tools.client;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * Add Review Groups to all Code Review
 * @author Joseph Allen
 *
 */
public class AddGroupsToReview extends Thread {
	private static String url = "http://hmaapp58.hma.am.honda.com:8080/services/json/v1";
	private static String loginTokenRequest = "[{\"command\" : \"SessionService.getLoginTicket\",\"args\":{\"login\":\"@USERNAME@\",\"password\":\"@PASSWORD@\"}}]";
	private static String addReviewGroup = "[{\"command\":\"SessionService.authenticate\",\"args\":{\"login\":\"@USERNAME@\",\"ticket\":\"@LOGIN_TOKEN@\"}},{\"command\":\"ReviewService.updateAssignments\",\"args\":{\"reviewId\":\"@ID@\",\"assignments\":[{\"poolGuid\":\"" + "@POOL@" + "\",\"role\":\"REVIEWER\"}]}}]";
	private static String addDBAReviewGroup = "[{\"command\":\"SessionService.authenticate\",\"args\":{\"login\":\"@USERNAME@\",\"ticket\":\"@LOGIN_TOKEN@\"}},{\"command\":\"ReviewService.updateAssignments\",\"args\":{\"reviewId\":\"@ID@\",\"assignments\":[{\"poolGuid\":\"" + "@POOL@" + "\",\"role\":\"REVIEWER\"}]}}]";
	private static String getReviewFileVersion = "[{\"command\":\"SessionService.authenticate\",\"args\":{\"login\":\"@USERNAME@\",\"ticket\":\"@LOGIN_TOKEN@\"}},{\"command\":\"ReviewService.getVersions\",\"args\":{\"reviewId\":\"@ID@\"}}]";
	private static String getReviewParticipants = "[{\"command\":\"SessionService.authenticate\",\"args\":{\"login\":\"@USERNAME@\",\"ticket\":\"@LOGIN_TOKEN@\"}},{\"command\":\"ReviewService.getParticipants\",\"args\":{\"reviewId\":\"@ID@\"}}]";
	private static String regionalDBSvnPath = "/BaseLibrary/design/Database";
	
	/**
	 * 
	 * @param args 0=Review ID 1=username 2=password 3=Comma separated listed of review pools 4=Comma separated list of DBA pools
	 */
	public static void main(String[] args) {
		String reviewId = args[0];
		String username = args[1];
		String password = args[2];
		List <String> reviewPools = Arrays.asList(args[3].split(","));
		List <String> dbaPools = Arrays.asList(args[4].split(","));
		String loginToken = AddGroupsToReview.sendRequest(loginTokenRequest.replace("@USERNAME@", username).replace("@PASSWORD@", password));
		String participants = sendRequest(getReviewParticipants.replace("@ID@", reviewId).replace("@USERNAME@", username).replace("@LOGIN_TOKEN@", loginToken));
		
		for(String pool : reviewPools){
			if(!participants.contains(pool)){
				AddGroupsToReview.sendRequest(addReviewGroup.replace("@USERNAME@", username).replace("@LOGIN_TOKEN@", loginToken).replace("@ID@", reviewId).replace("@POOL@", pool));
			}
		}
		
		for(String pool : dbaPools){
			if(!participants.contains(pool)){
				if(hasRegionalDatabaseChanges(getReviewFileVersion.replace("@ID@", reviewId).replace("@USERNAME@", username).replace("@LOGIN_TOKEN@", loginToken))){
					AddGroupsToReview.sendRequest(addDBAReviewGroup.replace("@USERNAME@", username).replace("@LOGIN_TOKEN@", loginToken).replace("@ID@", reviewId).replace("@POOL@", pool));
				}
			}
		}
	}
	 
	/**
	 * Sends a JSON request to the server and returns the results
	 * @param paramsString The JSON request to send to the server
	 * @return The returned results from the server without the JSON syntax
	 */
	public static String sendRequest(String paramsString){
		System.out.println(paramsString);
		HttpClient httpClient = null;
		try {
			httpClient = HttpClientBuilder.create().build();
 	        HttpPost request = new HttpPost(url);
 	        StringEntity params =new StringEntity(paramsString);
 	        request.addHeader("content-type", "application/json");
 	        request.setEntity(params);
 	        HttpResponse response = httpClient.execute(request);
 	        String result = EntityUtils.toString(response.getEntity());
 	        result = result.replace("[", "").replace("\n", "").replace("{", "").replace(":", "").replace("\"", "").replace("}", "").replace("]", "").replace(" ", "").replace("result", "").replace("login", "").replace("Ticket", "");
 	        System.out.println(result);
		 	return result;
		 	}catch (Exception ex) {
		 		ex.printStackTrace();
		 	} finally {
		 	        httpClient.getConnectionManager().shutdown();
		 	}
		return null;
	 }
	
	/**
	 * Checks to see if the review has regional database changes
	 * @param paramsString
	 * @return
	 */
	public static boolean hasRegionalDatabaseChanges(String paramsString){
		String result = sendRequest(paramsString);
		if(result.contains(regionalDBSvnPath)){
			return true;
		}
		return false;
	}
}
