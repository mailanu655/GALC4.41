package com.honda.galc.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

import com.honda.galc.common.logging.Logger;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * 
 * <h3>FtpClientHelper</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> FtpClientHelper description </p>
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
 * @author Paul Chou
 * Jun. 10, 2014
 *
 */

public class FtpClientHelper {
	String server;
	int port;
	
	String userName;
	String password;
	FTPClient client;
	FTPSClient ftps;
	Logger logger;
	
	Channel channel;
	Session session;
	ChannelSftp sftp;
	JSch jsch;

	
	public FtpClientHelper(String server, int port, String userName, String password, Logger logger) {
		super();
		this.server = server;
		this.port = port;
		this.userName = userName;
		this.password = password;
		this.logger = logger;
	}
	
	public FTPClient getClient(){
		
		try {
			client = new FTPClient();
			if (port > 0)
				client.connect(server, port);
			else 
				client.connect(server);
			
			logger.info("Connected to " + server + " on " + (port>0 ? port : client.getDefaultPort()));
			
			//check connection status
			int reply = client.getReplyCode();

			if (!FTPReply.isPositiveCompletion(reply))
			{
				client.disconnect();
				logger.error("FTP server refused connection.");
				return null;
			}
			
			if(!client.login(userName, password)){
				client.logout();
				logger.warn("Failed to log into FTP Server:" + server + " user:" + userName);
				return null;
			}
			
			logger.info("connected to Ftp Server - remote system is :" + client.getSystemType());
			
			client.setFileType(FTP.ASCII_FILE_TYPE);//set to ascii by default
			
		} catch (Exception e) {
			logger.error(e, " Exception to connect to server.");
		} 
		
		return client;
	}
	
	public ChannelSftp getSftpClient() {
		try {
			logger.info("Connected to " + server + " on " + (port>0 ? port : client.getDefaultPort()));
			
			jsch = new JSch();
			session = jsch.getSession(userName, server, port);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no"); 
			session.connect();
			
			channel = session.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
		} catch (Exception e) {
			logger.error(e, " Exception to connect to server.");
			return null;
		}
		return sftp;
	}
		
	public void closeSftp(){
		try {
			channel.disconnect();
			session.disconnect();
		}catch (Exception e2) {// TODO: handle exception
		}
		
	}
	
	public BufferedReader getBufferedReader(String remoteFile){
		try {
			client = getClient();
			InputStream ins = client.retrieveFileStream(remoteFile);
			
			InputStreamReader r = new InputStreamReader(ins);
			return new BufferedReader(r);
			
		} catch (Exception e) {
			logger.error(e, " failed to read remote file:" + remoteFile);
		}
		
		return null;
	}
	
	public void disconnect(){
		if(client!= null && client.isConnected()) {
			try {
				client.disconnect();
			} catch (Exception e2) {// TODO: handle exception
			}
		}
	}
	
}
