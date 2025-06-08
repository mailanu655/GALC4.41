package com.honda.galc.system.config.web.forms;

import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.OpcConfigEntry;

/**
 *<H3>DeviceForm</H3> 
* <h3>Class description</h3>
* <h4>Description</h4>
* <P>This is a Struts form bean for the Device settings page</P>
* <h4>Special Notes</h4>
* <h4>Change History</h4>
* <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
* <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
* <TH>Updated by</TH>
* <TH>Update date</TH>
* <TH>Version</TH>
* <TH>Mark of Update</TH>
* <TH>Reason</TH>
* </TR>
* <TR>
* <TD>martinek</TD>
* <TD>Oct 2, 2007</TD>
* <TD></TD>
* <TD>@JM200731</TD>
* <TD>Add support for OPC linkage</TD>
* </TR>
* </TABLE>
 */
public class DeviceForm extends ActionForm

{

	private static final long serialVersionUID = 1L;
	/**
	 * The alias name of device.
	 */
	private String aliasName ;
	
	/**
	 * NO_DEFINITION(Key Field)
	 */
	private String clientID;
	
	/**
	 * The description of device.
	 */
	private String deviceDescription ;
	
	/**
	 * This flag is indicated the type of device .
	 */
	private int deviceType;
	
	/**
	 * The ID of division.
	 */
	private String divisionID ;
	
	/**
	 * NO_DEFINITION
	 */
	private String eifIPAddress;
	
	/**
	 * TCP/IP Port number.
	 */
	private int eifPort ;
	
	
	/**
	 * NO_DEFINITION
	 */
	private String ioProcessPointID;


	private String replyClientID;
	
	/**
	 * @JM200731
	 * Optional list of OPCConfigurationData entries. This will be null
	 * if none defined.
	 */
	private List<OpcConfigEntry> opcConfigurationDataList = null;
	
	/**
	 * Apply button field
	 */
	private String apply = null;
	
	/**
	 * Flag that indicates a new device is being created.
	 */
	private boolean createFlag = false;
	
	private TreeMap deviceTypeMap = null;
	
	private boolean editor = false;

	private boolean deleteConfirmed = false;
	
    public void reset(ActionMapping mapping, HttpServletRequest request) {

        // Reset field values here.
        aliasName = null;
        clientID = null;
        deviceDescription = null;
        deviceType = 0;
        divisionID = null;
        eifIPAddress = null;
        eifPort = 0;
        ioProcessPointID = null;
        replyClientID = null;
        apply = null;
        createFlag = false;
        //deviceTypeMap is a constant, should not be reset
        //deviceTypeMap=null
        editor = false;
        deleteConfirmed = false;
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        // Validate the fields in your form, adding
        // adding each error to this.errors as found, e.g.

        // if ((field == null) || (field.length() == 0)) {
        //   errors.add("field", new org.apache.struts.action.ActionError("error.field.required"));
        // }
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
     * @return Returns the aliasName.
     */
    public String getAliasName() {
        return aliasName;
    }
    /**
     * @param aliasName The aliasName to set.
     */
    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
    /**
     * @return Returns the clientID.
     */
    public String getClientID() {
        return clientID;
    }
    /**
     * @param clientID The clientID to set.
     */
    public void setClientID(String clientID) {
        this.clientID = clientID;
    }
    /**
     * @return Returns the deviceDescription.
     */
    public String getDeviceDescription() {
        return deviceDescription;
    }
    /**
     * @param deviceDescription The deviceDescription to set.
     */
    public void setDeviceDescription(String deviceDescription) {
        this.deviceDescription = deviceDescription;
    }
    /**
     * @return Returns the deviceType.
     */
    public int getDeviceType() {
        return deviceType;
    }
    /**
     * @param deviceType The deviceType to set.
     */
    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
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
     * @return Returns the eifIPAddress.
     */
    public String getEifIPAddress() {
        return eifIPAddress;
    }
    /**
     * @param eifIPAddress The eifIPAddress to set.
     */
    public void setEifIPAddress(String eifIPAddress) {
        this.eifIPAddress = eifIPAddress;
    }
    /**
     * @return Returns the eifPort.
     */
    public int getEifPort() {
        return eifPort;
    }
    /**
     * @param eifPort The eifPort to set.
     */
    public void setEifPort(int eifPort) {
        this.eifPort = eifPort;
    }
    /**
     * @return Returns the ioProcessPointID.
     */
    public String getIoProcessPointID() {
        return ioProcessPointID;
    }
    /**
     * @param ioProcessPointID The ioProcessPointID to set.
     */
    public void setIoProcessPointID(String ioProcessPointID) {
        this.ioProcessPointID = ioProcessPointID;
    }
    /**
     * @return Returns the replyClientID.
     */
    public String getReplyClientID() {
        return replyClientID;
    }
    /**
     * @param replyClientID The replyClientID to set.
     */
    public void setReplyClientID(String replyClientID) {
        this.replyClientID = replyClientID;
    }
    
    public Device getDeviceData() {
        Device result = new Device();
        result.setAliasName(aliasName);
        result.setClientId(clientID);
        result.setDeviceDescription(deviceDescription);
        result.setDeviceTypeId(deviceType);
        result.setDivisionId(divisionID);
        result.setEifIpAddress(eifIPAddress);
        result.setEifPort(eifPort);
        result.setIoProcessPointId(ioProcessPointID);
        result.setReplyClientId(replyClientID);
        return result;
    }
    
    public void setDeviceData(Device data) {
        aliasName = data.getAliasName();
        clientID = data.getClientId();
        deviceDescription = data.getDeviceDescription();
        deviceType = data.getDeviceTypeId();
        divisionID = data.getDivisionId();
        eifIPAddress = data.getEifIpAddress();
        eifPort = data.getEifPort();
        ioProcessPointID = data.getIoProcessPointId();
        replyClientID = data.getReplyClientId();
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
     * @return Returns the deviceTypeMap.
     */
    public TreeMap getDeviceTypeMap() {
        return deviceTypeMap;
    }
    /**
     * @param deviceTypeMap The deviceTypeMap to set.
     */
    public void setDeviceTypeMap(TreeMap deviceTypeMap) {
        this.deviceTypeMap = deviceTypeMap;
    }

	public boolean isCreateFlag() {
		return createFlag;
	}

	public void setCreateFlag(boolean createFlag) {
		this.createFlag = createFlag;
	}

	public List<OpcConfigEntry> getOpcConfigurationDataList() {
		return opcConfigurationDataList;
	}

	public void setOpcConfigurationDataList(List<OpcConfigEntry> opcConfigurationDataList) {
		this.opcConfigurationDataList = opcConfigurationDataList;
	}
}
