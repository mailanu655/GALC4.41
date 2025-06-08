package com.honda.galc.mdb;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.jca.SocketMessage;
import com.honda.galc.jca.SocketMessageEndpoint;
import com.honda.galc.let.message.LetMessageHandler;
import com.honda.galc.let.util.LetUtil;
import com.ibm.ejs.ras.RasHelper;

public class SocketMessageDrivenBean implements MessageDrivenBean, SocketMessageEndpoint {
	private static final long serialVersionUID = 1L;
	
	private static final String LOGGER_ID = "JcaAdaptor";
	
	public SocketMessageDrivenBean() {}

	public void ejbRemove() throws EJBException {}

	public void ejbCreate() throws EJBException {}

	public void setMessageDrivenContext(MessageDrivenContext ctx) throws EJBException {}

	public void onMessage(SocketMessage socketMessage) throws Exception {
		LetMessageHandler messageHandler;
		String remoteHost = getRemoteHostName(socketMessage);
		try {
			getLogger().info("Entering SocketMessageDrivenBean.onMessage for request " + remoteHost);
			
			messageHandler = new LetMessageHandler(socketMessage, getNodeName());  
			messageHandler.handleLetMessage();
			
			getLogger().info("Exiting SocketMessageDrivenBean.onMessage for request " + remoteHost);
		}catch(Exception ex){
			ex.printStackTrace();
			String msg = "SocketMessageDrivenBean.onMessage error handling request from "
					+ remoteHost 
					+ ": "
					+ ex.getMessage();
			getLogger().error(ex, msg);
			LetUtil.sendAlertEmail(msg, ex);
		} finally {
			try {
				if (!socketMessage.getRawSocket().isClosed()) {
					socketMessage.getRawSocket().close();
				} 
			} catch(Exception e) {}
			messageHandler = null;
		}
	}

	public String getRemoteHostName(SocketMessage socketMessage) {
		try {
			return socketMessage.getRawSocket().getRemoteSocketAddress().toString();
		} catch(Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}
	
	public static String getNodeName() {
		try {
			return RasHelper.getServerName().split("\\\\")[1];
		} catch (Exception ex) {
			return "server_node_name_unknown";
		}
	}
	
	public static Logger getLogger(){
		return Logger.getLogger(LOGGER_ID);
	}
}

