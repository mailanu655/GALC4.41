package com.honda.galc.webstart;

import java.io.Serializable;

/**
 * <h3>Class description</h3>
 * This class represents a terminal in GALC. 
 * It gets all the information from GALC terminal table.
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
 * <TD>Guang Yang</TD>
 * <TD>Dec 21, 2007</TD>
 * <TD>2.0</TD>
 * <TD>@GY 20071221</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 * 
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>Sep 19, 2008</TD>
 * <TD>&nbsp;</TD>
 * <TD>@RL056</TD>
 * <TD>JTRAC:ENGINE-174 NotSerializableException when Wily collects probes:<br>
 * 		the class was made Serializable</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 2.0
 * @author Guang Yang
 */
public class WebStartTerminal implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String hostName;
	private String ipAddress;
	private String port;
	private String description;
	private String processPointId;
	
	public WebStartTerminal() {
		super();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getProcessPointId() {
		return processPointId;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	
}
