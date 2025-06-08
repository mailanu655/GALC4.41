package com.honda.galc.client;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.GalcStation;

public class Arguments {

	private static final String SEPARATOR = File.separator;

	// required params
	private String rootDir;
	private String serverURL;
	
	// optional params
	private String hostName;
	private String skin = "default";
	private boolean useCache = false;

	// additional params
	private Map<String, String> additionalParams = new HashMap<String, String>();

	public static Arguments create(List<String> params) {
		Arguments arguments = new Arguments();
		ArrayList<String> requiredAndOptional = arguments.processAdditionalParams(params);
		
		if (requiredAndOptional.size() < 2) {
			return null;
		} else {
			arguments.parse(requiredAndOptional);
			return arguments;
		}
	}

	private Arguments() {
		hostName = fetchHostName();
	}

	private void parse(ArrayList<String> params) {
		int i = 0;
		for (String param: params) {
			switch (i++) {
			case 0: 			// required
				setRootDir(param);
				break;
			case 1: 			// required
				serverURL = param;
				break;
			case 2: 			// optional
				hostName = param;
				break;
			case 3: 			// optional
				skin = param;
				break;
			case 4:				// optional
				useCache = Boolean.parseBoolean(param);
				break;
			default:
				parseAdditionalParam(param);
				break;
			}
		}
	}

	private ArrayList<String> processAdditionalParams(List<String> params) {
		ArrayList<String> requiredAndOptional = new ArrayList<String>();
		for (String param: params) {
			if (param.startsWith("-")) {
				parseAdditionalParam(param);
			} else {
				requiredAndOptional.add(param);
			}
		}
		return requiredAndOptional;
	}

	private void parseAdditionalParam(String param) {
		int idx = param.indexOf('=');
		if (idx > 0) {
			String paramkey = param.substring(0, idx);
			String paramval = param.substring(idx + 1);
			additionalParams.put(paramkey, paramval);
		} else {
			additionalParams.put(param, param);
		}
	}

	private String fetchHostName() {
		String name = null;
		try {
			name = GalcStation.getHostName();
		} catch (Exception e) {
		}
		return name;
	}
	
	public String getRootDir() {
		return rootDir;
	}

	public void setRootDir(String rootDir) {
		if (rootDir == null || rootDir.trim().equals("") || rootDir.trim().equals("user.home")) {
			rootDir = System.getProperty("user.home");
		}
		this.rootDir = rootDir;
	}

	public String getServerURL() {
		return serverURL;
	}

	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
		
	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	public boolean isUseCache() {
		return useCache;
	}

	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}

	public String getCachePath() {
		String[] pathElements = { rootDir, SEPARATOR, "cache", SEPARATOR, hostName.trim() };
		return StringUtils.join(pathElements, null);
	}
	
	public String getUserName() {
		return additionalParams.get("-user");
	}

	public String getPassword() {
		return additionalParams.get("-pwd");
	}

	public boolean isHeadless() {
		return Boolean.parseBoolean(additionalParams.get("-headless"));
	}
	
	public Map<String, String> getAdditionalParams() {
		return additionalParams;
	}
	
	public Arguments copy(){
		Arguments copy = new Arguments();
		copy.setHostName(this.getHostName());
		copy.setRootDir(this.getRootDir());
		copy.setServerURL(this.getServerURL());
		copy.setSkin(this.getSkin());
		copy.setUseCache(this.isUseCache());
		copy.getAdditionalParams().putAll(this.getAdditionalParams());
		
		return copy;
	}
}
