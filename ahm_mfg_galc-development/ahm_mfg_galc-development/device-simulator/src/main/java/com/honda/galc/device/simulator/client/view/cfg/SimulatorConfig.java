package com.honda.galc.device.simulator.client.view.cfg;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.constant.Delimiter;
import com.honda.galc.device.simulator.data.SimulatorConstants.ServerType;
import com.honda.galc.util.AbstractPropertyConfig;

/**
 * 
 * <h3>SimulatorConfig Class description</h3>
 * <p> SimulatorConfig description </p>
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
 * Sep 9, 2013
 *
 *
 */
public class SimulatorConfig extends AbstractPropertyConfig{

	private static final String FILE_PATH ="simulator.properties";
	private static final String HOST_NAME ="HOST_NAME";
	private static final String SERVICE_HANDLER="BaseWeb/HttpServiceHandler";
	private static final String DEVICE_HANDLER="BaseWeb/HttpDeviceHandler";
	private static final String REQUEST_DISPATCHER="servlet/HTTPRequestDispatcher";
	private static final String RESET_WEB="RestWeb/";
	
	public static String env_arg;
	public static boolean isLegacy = false;
	public static String hostName;
	
	private static String serverName;
	
	private static SimulatorConfig instance;
	
	
	public SimulatorConfig() {
		super(FILE_PATH);
	}
	
	protected String getPreferedSuffix() {
		return serverName;
	}
	
	public static void setEnvArgument(String env){
		SimulatorConfig.env_arg = env;
		SimulatorConfig.serverName = env;
	}
	
	public static String getFilePath() {
	    return FILE_PATH;	
	}
	
	public static String getServerName() {
		return SimulatorConfig.serverName;
	}	
	
	public static void setServerName(String serverName) {
		SimulatorConfig.serverName = serverName;
		getInstance().prefferedSuffix = serverName;
	}
	
	public String getHostName(){
		return StringUtils.isEmpty(hostName) ? getProperty(HOST_NAME) : hostName;
	}
	
	public String getServiceURL(){
		return "http://" + getHostName() + "/" + SERVICE_HANDLER;
	}
	
	public String getRequestDispatcherURL() {
		return "http://" + getHostName() + "/" + REQUEST_DISPATCHER;
	}
	
	public String getDeviceURL() {
		return isLegacy ? getRequestDispatcherURL() : getDeviceHandlerURL();
	}
	
	public String getDeviceHandlerURL(){
		return "http://" + getHostName() + "/" + DEVICE_HANDLER;
	}
	
	@SuppressWarnings("unchecked")
	public String[] getServerList() {
		Set<String> servers = new HashSet<String>();
		if(StringUtils.isEmpty(env_arg) || "DEFAULT".equalsIgnoreCase(env_arg)) return new String[0];
		
		String[] names = env_arg.split(Delimiter.UNDERSCORE);
		
		for(Map.Entry entry : properties.entrySet()){
			String key = (String) entry.getKey();
			String[] items = key.split("\\" + Delimiter.DOT);
			if(items.length == 2 && HOST_NAME.equalsIgnoreCase(items[0]) && items[1].startsWith(names[0])) servers.add(items[1]); 
		}
		
		return servers.toArray(new String[servers.size()]);
	}
	
	public static SimulatorConfig getInstance(){
		if(instance==null) instance = new SimulatorConfig();
		return instance;
	}

	public String getRestServiceURL() {
		
		return "http://" + getHostName() + "/" + RESET_WEB;
	}

	public void updateServerType(ServerType serverType) {
		if(serverType == ServerType.Galc_Server)
			isLegacy = false;
		else if(serverType == ServerType.Legacy_Galc_Server)
			isLegacy = true;
		
	}
	

	
}
