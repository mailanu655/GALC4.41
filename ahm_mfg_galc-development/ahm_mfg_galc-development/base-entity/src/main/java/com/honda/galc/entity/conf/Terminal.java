package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Terminal is line control terminal
 * <p/>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>Aug 31, 2009</TD>
 * <TD>&nbsp;</TD>
 * <TD>Created</TD>
 * </TR>
 * </TABLE>
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name = "GAL234TBX")
public class Terminal extends AuditEntry {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "HOST_NAME")
    private String hostName;

    @Column(name = "IP_ADDRESS")
    private String ipAddress;

    private int port;

    @Column(name = "ROUTER_PORT")
    private int routerPort;

    @Column(name = "TERMINAL_DESCRIPTION")
    private String terminalDescription;

    @Column(name = "LOCATED_PROCESS_POINT_ID")
    private String locatedProcessPointId;

    @Column(name = "DIVISION_ID")
    private String divisionId;

    @Column(name = "AF_TERMINAL_FLAG")
    private int afTerminalFlag;
   
    @OneToOne(targetEntity = ProcessPoint.class,fetch = FetchType.EAGER)
    @JoinColumn(name="LOCATED_PROCESS_POINT_ID",referencedColumnName="PROCESS_POINT_ID",updatable=false,insertable=false)
    private ProcessPoint processPoint;
 
    @Column(name = "AUTO_UPDATE_IP_ADDRESS")
    private short autoUpdateIpAddress;
    
    public Terminal() {
        super();
    }

    public String getHostName() {
        return StringUtils.trim(hostName);
    }
    
    public String getId() {
    	return getHostName();
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIpAddress() {
        return StringUtils.trim(ipAddress);
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getRouterPort() {
        return this.routerPort;
    }

    public void setRouterPort(int routerPort) {
        this.routerPort = routerPort;
    }

    public String getTerminalDescription() {
        return StringUtils.trim(this.terminalDescription);
    }

    public void setTerminalDescription(String terminalDescription) {
        this.terminalDescription = StringUtils.trim(terminalDescription);
    }

    public String getLocatedProcessPointId() {
        return StringUtils.trim(locatedProcessPointId);
    }

    public void setLocatedProcessPointId(String locatedProcessPointId) {
        this.locatedProcessPointId = locatedProcessPointId;
    }

    public String getDivisionId() {
        return StringUtils.trim(this.divisionId);
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = StringUtils.trim(divisionId);
    }

    public int getAfTerminalFlag() {
        return this.afTerminalFlag;
    }

    public void setAfTerminalFlag(int afTerminalFlag) {
        this.afTerminalFlag = afTerminalFlag;
    }

	public ProcessPoint getProcessPoint() {
		return processPoint;
	}

	public void setProcessPoint(ProcessPoint processPoint) {
		this.processPoint = processPoint;
	}
	@Override
	public String toString() {
		return toString(getHostName(),getLocatedProcessPointId());
	}

	public void setAutoUpdateIpAddress(short autoUpdateIpFlag) {
		this.autoUpdateIpAddress = autoUpdateIpFlag;	
	}
	
	public short getAutoUpdateIpAddress() {
		return this.autoUpdateIpAddress ;	
	}

	public boolean isAutoUpdateIpAddress() {
		return this.autoUpdateIpAddress == 0?false:true;
	}
    
    
}