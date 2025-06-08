package com.honda.galc.webstart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.honda.galc.entity.conf.WebStartBuild;
import com.honda.galc.entity.conf.WebStartClient;

/**
 * <h3>Class description</h3>
 * Value object to hold information about web start configuration  
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>May 30, 2008</TD>
 * <TD>&nbsp;</TD>
 * <TD>@RL023</TD>
 * <TD>Client configuraiton cache</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 2.0
 * @author Roman Lasenko
 */
public class WsClientConfig {
	
	private String hostId;
	
	private WebStartClient wsClient;
	
	private String terminalTypeName;
	
	private Date modified;
	
	private Date lastModified;
	
	private WebStartBuild build;
	
	private boolean defaultBuild;
	
	private String mainClass;
	
	private String dispacherURL;
	
	private boolean useCache = false;
	
	private boolean isNewApp = false;
	
	private List<String> jars = new ArrayList<String>();

	private List<String> nativeJars = new ArrayList<String>();
	
	private String lookAndFeel;
	
	private List<String> args = new ArrayList<String>();
	
	private String initialHeapSize = "256m";
	
	private String maxHeapSize="512m";

	/**
	 * @param hostId
	 * @param wsClient
	 * @param terminalTypeName
	 * @param modified
	 */
	public WsClientConfig(String hostId, WebStartClient wsClient, String terminalTypeName, Date modified) {
		super();
		this.hostId = hostId;
		this.wsClient = wsClient;
		this.terminalTypeName = terminalTypeName;
		this.modified = modified;
	}
	
	/**
	 * @return the hostId
	 */
	public String getHostId() {
		return hostId;
	}
	
	/**
	 * @return the terminalTypeName
	 */
	public String getTerminalTypeName() {
		return terminalTypeName;
	}

	/**
	 * @return the lastModifed
	 */
	public Date getLastModified() {
		return lastModified;
	}
	
	/**
	 * @return the modified
	 */
	public Date getModified() {
		return modified;
	}
	
	/**
	 * @return the wsClient
	 */
	public WebStartClient getWsClient() {
		return wsClient;
	}

	/**
	 * @return the build
	 */
	public WebStartBuild getBuild() {
		return build;
	}

	/**
	 * @param build the build to set
	 */
	public void setBuild(WebStartBuild build) {
		this.build = build;
	}

	/**
	 * @return the defaultBuid
	 */
	public boolean isDefaultBuild() {
		return defaultBuild;
	}

	/**
	 * @param defaultBuid the defaultBuid to set
	 */
	public void setDefaultBuid(boolean defaultBuid) {
		this.defaultBuild = defaultBuid;
	}

	/**
	 * @param lastModified the lastModifed to set
	 */
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getMainClass() {
		return mainClass;
	}

	public void setMainClass(String mainClass) {
		this.mainClass = mainClass;
	}

	public String getDispacherURL() {
		return dispacherURL;
	}

	public void setDispacherURL(String dispacherURL) {
		this.dispacherURL = dispacherURL;
	}

	public boolean isNewApp() {
		return isNewApp;
	}

	public void setNewApp(boolean isNewApp) {
		this.isNewApp = isNewApp;
	}

	public List<String> getJars() {
		return jars;
	}

	public void setJars(List<String> jars) {
		this.jars = jars;
	}

	public List<String> getNativeJars() {
		return nativeJars;
	}

	public void setNativeJars(List<String> nativeJars) {
		this.nativeJars = nativeJars;
	}

	public boolean isUseCache() {
		return useCache;
	}

	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}

	public String getLookAndFeel() {
		return lookAndFeel;
	}

	public void setLookAndFeel(String lookAndFeel) {
		this.lookAndFeel = lookAndFeel;
	}

	public List<String> getArgs() {
		return args;
	}

	public void setArgs(List<String> args) {
		this.args = args;
	}

	public String getInitialHeapSize() {
		return initialHeapSize;
	}

	public void setInitialHeapSize(String initialHeapSize) {
		this.initialHeapSize = initialHeapSize;
	}

	public String getMaxHeapSize() {
		return maxHeapSize;
	}

	public void setMaxHeapSize(String maxHeapSize) {
		this.maxHeapSize = maxHeapSize;
	}
	
}
