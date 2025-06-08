package com.honda.galc.tools.client;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.admin.ISVNChangeEntryHandler;
import org.tmatesoft.svn.core.wc.admin.SVNChangeEntry;
import org.tmatesoft.svn.core.wc.admin.SVNLookClient;


/**
 * This class will be used as a hook for subversion to verify that a Jira issue is valid and
 * in the correct status
 * @author Joseph Allen
 *
 */
public class VerifyJiraIssueStatus implements  ISVNChangeEntryHandler{
	public static String getIssueStatus = "http://jira.hma.am.honda.com/rest/api/2/issue/@ISSUE@?fields=status";
	public static String confirmStatus = "Ready for QA";
	public static String testCaseConfirmStatus = "Test Development Completed";
	public static String buildUser = "autobld";
	public static ArrayList <String> pathsToIgnore = new ArrayList<String>();
	
	/**
	 * Main class to start verification
	 * @param args
	 */
	public static void main(String args[]){
		String repoPath = args[0];
		String txn = args[1];
		pathsToIgnore.add("tag");
		
		checkToIgnore(repoPath, txn);
		String commiter = getCommiter(repoPath, txn);
		if(commiter != null && commiter.equals(buildUser)){
			System.err.println("Author = " + commiter);
			System.exit(0);
		}
		
		String issue = getIssue(repoPath, txn);
		String result = sendRequest(issue);
		if(!result.toUpperCase().contains(confirmStatus.toUpperCase()) && !result.toUpperCase().contains(testCaseConfirmStatus.toUpperCase())){
			System.err.println("Jira Issue (" + issue + ") not in status '" + confirmStatus + "' or '" + testCaseConfirmStatus + "', or issue not found.");
			System.exit(1);
		}
		System.exit(0);
	}
	
	/**
	 * Sends a JSON request to the server and returns the results
	 * @param paramsString The JSON request to send to the server
	 * @return The returned results from the server without the JSON syntax
	 */
	public static String sendRequest(String issue){
		HttpClient httpClient = null;
		try {
			httpClient = HttpClientBuilder.create().build();
 	        HttpGet request = new HttpGet(getIssueStatus.replace("@ISSUE@", issue));
 	        request.addHeader("content-type", "application/json");
 	        HttpResponse response = httpClient.execute(request);
 	        String result = EntityUtils.toString(response.getEntity());
		 	return result;
		 	}catch (Exception ex) {
		 		ex.printStackTrace();
		 	} finally {
		 	        httpClient.getConnectionManager().shutdown();
		 	}
		return null;
	 }
	
	/**
	 * Gets the repository change user
	 * @param repoPath
	 * @param txn
	 * @return Returns a string with the commiter user ID
	 */
	public static String getCommiter(String repoPath, String txn){
		File fileRepository = new File(repoPath);
		SVNLookClient svnLookClient = new SVNLookClient((ISVNAuthenticationManager)null, new DefaultSVNOptions());
		try {
			return svnLookClient.doGetAuthor(fileRepository, txn);
		} catch (SVNException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param repoPath
	 * @param txn
	 * @return The Jira issue ID from the commit comments
	 */
	public static String getIssue(String repoPath, String txn){
		File fileRepository = new File(repoPath);
		SVNLookClient svnLookClient = new SVNLookClient((ISVNAuthenticationManager)null, new DefaultSVNOptions());
		try {
			String log = svnLookClient.doGetLog(fileRepository, txn);
			System.err.println(repoPath);
			System.err.println(txn);
			System.err.println(log);
			if(log.length() <= 0){
				System.err.println("Commit comment is not in format of: R#121, RGALC-1231 :: Initial Commit");
				System.exit(1);
			}
			String retVal = log.substring(log.indexOf(",") + 1, log.indexOf(":")).trim();
			if(!retVal.isEmpty()){
				return retVal;
			}
		} catch (SVNException e1) {
			e1.printStackTrace();
		}
		return null;		
	}

	/**
	 * Checks to see if the change is a part of a branch or tag.
	 * @param repoPath
	 * @param txn
	 */
	public static void checkToIgnore(String repoPath, String txn){
		File fileRepository = new File(repoPath);
		SVNLookClient svnLookClient = new SVNLookClient((ISVNAuthenticationManager)null, new DefaultSVNOptions());
		VerifyJiraIssueStatus vjis = new VerifyJiraIssueStatus();
		try {
			svnLookClient.doGetChanged(fileRepository, txn, vjis, false);
		} catch (SVNException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * The handle method to check if the change is a part of the ingore
	 * paths. If so the app will exit
	 */
	@Override
	public void handleEntry(SVNChangeEntry change) throws SVNException {
		if(pathsToIgnore != null){
			for(String path : pathsToIgnore){
					if(change.getPath().contains(path)){
						System.err.println("The change path is: " + change.getPath());
						System.err.println("The ignore path is: " + path);
						System.err.println("Commit is for a branch or tag.");
						System.exit(0);
					}
			}
		}
	}
}
