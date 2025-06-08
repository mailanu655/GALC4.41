package com.honda.galc.system.config.web.forms;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.dao.conf.TerminalTypeDao;
import com.honda.galc.entity.conf.ApplicationByTerminal;
import com.honda.galc.entity.conf.TerminalType;
import com.honda.galc.service.ServiceFactory;

/**
 * Form bean for a Struts application.
 * @version 	1.0
 * @author
 */
public class TerminalForm extends ActionForm {
	
	private static final long serialVersionUID = 1L;

	/**
	 * The ID of division.
	 */
	private String divisionID ;
	
	/**
	  * NO_DEFINITION(Key Field)
	  */
	private String hostName;

	/**
	 * TCP/IP IP address .
	 */
	private String ipAddress;

	/**
	 * TCP/IP socket port number.
	 */
	private int port;

	/**
	 * The description of terminal.
	 */
	private String terminalDescription ;

	/**
	 *  Specify the terminal type LEGACY, NALC, NAQ
	 */
	private int afTerminalFlag ;

	/**
	  * NO_DEFINITION
	  */
	private String locatedProcessPointID ;
	
	/**
	 * @JM040
	 * The port the request router should use.
	 */
	private int routerPort = 0;  

	/**
	 * Apply button field
	 */
	private String apply = null;
	
	/**
	 * Flag that indicates a new device is being created.
	 */
	private String createFlag = null;
	
	private List applicationList = null;
	
	private List<ApplicationByTerminal> appList = new ArrayList<ApplicationByTerminal>();
	
	private TreeMap<Integer, String> terminalTypeMap = new TreeMap<Integer, String>();
    
	private String[] applicationID = null;
	
	private String operation = null;
	
	private String defaultApplication = null;
	
	private boolean editor = false;
	
	private boolean deleteConfirmed = false;
	
	/**
	 * A flag indicate this terminal auto update ip address or not.
	 */
	private boolean autoUpdateIpFlag;
	
	public TerminalForm() {
        for(TerminalType tType : ServiceFactory.getDao(TerminalTypeDao.class).findAll()) {
        	terminalTypeMap.put(tType.getTerminalFlag(), tType.getName());
        }
	}
	
    public void reset(ActionMapping mapping, HttpServletRequest request) {

        divisionID = null;
        hostName = null;
        ipAddress = null;
        port = 0;
        terminalDescription = null;
        afTerminalFlag = 0;
        locatedProcessPointID = null;
        routerPort = 0;
        createFlag = null;
        applicationList = null;
        appList = null;
        applicationID = null;
        operation = null;
        apply = null;
        defaultApplication = null;
        editor = false;
        deleteConfirmed = false;
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        return errors;
    }
    
    /**
     * @return Returns the deleteConfirmed.
     */
    public boolean isDeleteConfirmed() {
        return deleteConfirmed;
    }
    /**
     * @param deleteConfirmed The deleteConfirmed to set.
     */
    public void setDeleteConfirmed(boolean deleteConfirmed) {
        this.deleteConfirmed = deleteConfirmed;
    }
    /**
     * @return Returns the editor.
     */
    public boolean isEditor() {
        return editor;
    }
    /**
     * @param editor The editor to set.
     */
    public void setEditor(boolean editor) {
        this.editor = editor;
    }
    /**
     * @return Returns the defaultApplication.
     */
    public String getDefaultApplication() {
        return defaultApplication;
    }
    /**
     * @param defaultApplication The defaultApplication to set.
     */
    public void setDefaultApplication(String defaultApplication) {
        this.defaultApplication = defaultApplication;
    }
    /**
     * @return Returns the operation.
     */
    public String getOperation() {
        return operation;
    }
    /**
     * @param operation The operation to set.
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }
    /**
     * @return Returns the applicationID.
     */
    public String[] getApplicationID() {
        return applicationID;
    }
    /**
     * @param applicationID The applicationID to set.
     */
    public void setApplicationID(String[] applicationID) {
        this.applicationID = applicationID;
    }
    /**
     * @return Returns the applicationMap.
     */
    public List<ApplicationByTerminal> getApplicationByTerminalList() {
        return appList == null ? new ArrayList<ApplicationByTerminal>() : appList;
    }
    /**
     * @param applicationMap The applicationMap to set.
     */
    public void setApplicationByTerminalList(List<ApplicationByTerminal> appList) {
        this.appList = appList;
    }
    /**
     * @return Returns the applicationList.
     */
    public List getApplicationList() {
        return applicationList;
    }
    /**
     * @param applicationList The applicationList to set.
     */
    public void setApplicationList(List applicationList) {
        this.applicationList = applicationList;
    }
    /**
     * @return Returns the afTerminalFlag.
     */
    public int getAfTerminalFlag() {
		return afTerminalFlag;
    }
    /**
     * @param afTerminalFlag The afTerminalFlag to set.
     */
    public void setAfTerminalFlag(int afTerminalFlag) {
        this.afTerminalFlag =  afTerminalFlag;
    }
    /**
     * @return Returns the divisionID.
     */
    public String getDivisionID() {
        return divisionID;
    }
    /**
     * @param divisionID The divisionID to set.
     */
    public void setDivisionID(String divisionID) {
        this.divisionID = divisionID;
    }
    /**
     * @return Returns the hostName.
     */
    public String getHostName() {
        return hostName;
    }
    /**
     * @param hostName The hostName to set.
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
    /**
     * @return Returns the ipAddress.
     */
    public String getIpAddress() {
        return ipAddress;
    }
    /**
     * @param ipAddress The ipAddress to set.
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    /**
     * @return Returns the locatedProcessPointID.
     */
    public String getLocatedProcessPointID() {
        return locatedProcessPointID;
    }
    /**
     * @param locatedProcessPointID The locatedProcessPointID to set.
     */
    public void setLocatedProcessPointID(String locatedProcessPointID) {
        this.locatedProcessPointID = locatedProcessPointID;
    }
    /**
     * @return Returns the port.
     */
    public int getPort() {
        return port;
    }
    /**
     * @param port The port to set.
     */
    public void setPort(int port) {
        this.port = port;
    }
    /**
     * @return Returns the routerPort.
     */
    public int getRouterPort() {
        return routerPort;
    }
    /**
     * @param routerPort The routerPort to set.
     */
    public void setRouterPort(int routerPort) {
        this.routerPort = routerPort;
    }
    /**
     * @return Returns the terminalDescription.
     */
    public String getTerminalDescription() {
        return terminalDescription;
    }
    /**
     * @param terminalDescription The terminalDescription to set.
     */
    public void setTerminalDescription(String terminalDescription) {
        this.terminalDescription = terminalDescription;
    }
    /**
     * @return Returns the apply.
     */
    public String getApply() {
        return apply;
    }
    /**
     * @param apply The apply to set.
     */
    public void setApply(String apply) {
        this.apply = apply;
    }
    
    /**
     * @return Returns the createFlag.
     */
    public String getCreateFlag() {
        return createFlag;
    }
    
    /**
     * @param createFlag The createFlag to set.
     */
    public void setCreateFlag(String createFlag) {
        this.createFlag = createFlag;
    }
    
    public boolean isAutoUpdateIpFlag() {
		return autoUpdateIpFlag;
	}

	public void setAutoUpdateIpFlag(boolean autoUpdateIpFlag) {
		this.autoUpdateIpFlag = autoUpdateIpFlag;
	}
    
    public TreeMap<Integer, String> getTerminalTypeMap() {
        return terminalTypeMap;
    }
    
    public void setTerminalTypeMap(TreeMap<Integer, String> terminalTypeMap) {
        this.terminalTypeMap = terminalTypeMap;
    }
	
}
