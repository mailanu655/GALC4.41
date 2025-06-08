package com.honda.galc.device.simulator.client.view.data;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.simulator.client.view.cfg.SimulatorConfig;
import com.honda.galc.device.simulator.data.SimulatorConstants;
import com.honda.galc.device.simulator.data.SimulatorConstants.ServerType;

/**
 * 
 * <h3>ConfigValueObject</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ConfigValueObject description </p>
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
 * <TD>Sep 12, 2013</TD>
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
 * @since Sep 12, 2013
 */
public class ConfigValueObject {
	/* Simulator Server config variables */
	private String eiType;
	
	/* GALC server type */
	ServerType serverType = ServerType.Galc_Server;//Default to GALC Server
	
	/* Simulator UI config variables */
	private boolean senderShowTag; //or tagValue
	private boolean replyShowTag;
	
	/* Application Server Client config variables */
	boolean useOPCServerClientCfg;
	
	/* default serverClientType and serverClientMode for all device */ 
	private int serverClientType;
	private int serverClientMode;
	private String opcInstanceName = null;
	
	/* response data container*/
	private DataContainer responseDc;
	
	/* exclude host list */
	String[] excludeHosts;
	String[] exclIpMasks;
	
	public ConfigValueObject() {
		super();
		this.eiType = SimulatorConstants.EiType.OPC.toString();
		this.senderShowTag = false;
		this.useOPCServerClientCfg = false;
		this.serverClientType = 0;
		this.serverClientMode = 0;
		this.opcInstanceName = null;
		this.responseDc = new DefaultDataContainer();
		//Default value for reply
		responseDc.put(DataContainerTag.CLIENT_ID, "OPCACK");
		responseDc.put("DATA", "1");
	}

	/**
	 * @return the replyShowTag
	 */
	public boolean isReplyShowTag() {
		return replyShowTag;
	}

	/**
	 * @param replyShowTag the replyShowTag to set
	 */
	public void setReplyShowTag(boolean replyShowTag) {
		this.replyShowTag = replyShowTag;
	}

	/**
	 * @return the senderShowTag
	 */
	public boolean isSenderShowTag() {
		return senderShowTag;
	}

	/**
	 * @param senderShowTag the senderShowTag to set
	 */
	public void setSenderShowTag(boolean senderShowTag) {
		this.senderShowTag = senderShowTag;
	}

	/**
	 * @return the serverClientMode
	 */
	public int getServerClientMode() {
		return serverClientMode;
	}

	/**
	 * @param serverClientMode the serverClientMode to set
	 */
	public void setServerClientMode(int serverClientMode) {
		this.serverClientMode = serverClientMode;
	}

	/**
	 * @return the serverClientType
	 */
	public int getServerClientType() {
		return serverClientType;
	}

	/**
	 * @param serverClientType the serverClientType to set
	 */
	public void setServerClientType(int serverClientType) {
		this.serverClientType = serverClientType;
	}

	
	/**
	 * @return the useOPCServerClientCfg
	 */
	public boolean isUseOPCServerClientCfg() {
		return useOPCServerClientCfg;
	}

	/**
	 * @param useOPCServerClientCfg the useOPCServerClientCfg to set
	 */
	public void setUseOPCServerClientCfg(boolean useOPCServerClientCfg) {
		this.useOPCServerClientCfg = useOPCServerClientCfg;
	}
	
	public String toString()
	{
		String str = "eiType:" + eiType + "\n";
		str += "senderShowTag: " + senderShowTag + "\n";
		str += "replyShowTag: " + replyShowTag + "\n";
		str += "useOPCServerClientCfg :" + useOPCServerClientCfg + "\n";
		str += "serverClientType: " + serverClientType + "\n";
		str += "serverClientMode: " + serverClientMode + "\n";
		str += "opcInstanceName: " + opcInstanceName + "\n";
		str += "responseDc: " + responseDc;
 		return str;
	}

	/**
	 * @return the eiType
	 */
	public String getEiType() {
		return eiType;
	}

	/**
	 * @param eiType the eiType to set
	 */
	public void setEiType(String eiType) {
		this.eiType = eiType;
	}

	/**
	 * @return the opcInstanceName
	 */
	public String getOpcInstanceName() {
		return opcInstanceName;
	}

	/**
	 * @param opcInstanceName the opcInstanceName to set
	 */
	public void setOpcInstanceName(String opcInstanceName) {
		this.opcInstanceName = opcInstanceName;
	}

	public DataContainer getResponseDc() {
		return responseDc;
	}

	public void setResponseDc(DataContainer responseDc) {
		this.responseDc = responseDc;
	}

	public void setExcludeHosts(String[] excludehosts) {
		this.excludeHosts = excludehosts;
	}
	
	public String[] getExcludeHosts(){
		return excludeHosts;
	}

	public String[] getExclIpMasks() {
		return exclIpMasks;
	}

	public void setExclIpMasks(String[] exclIpMasks) {
		this.exclIpMasks = exclIpMasks;
	}

	public ServerType getServerType() {
		return serverType;
	}
	
	public void setServerType(ServerType serverType) {
		this.serverType = serverType;
		SimulatorConfig.getInstance().updateServerType(serverType);
	}
	
	
}
