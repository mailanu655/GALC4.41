package com.honda.galc.system.config.web.forms;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.entity.conf.OpcConfigEntry;


/**
 * 
 * <h3>OpcListForm</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>This is the Struts form bean for OPC list page.</p>
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
 * <TD>Added process point and device options</TD>
 * </TR>
 * </TABLE>
 */
public class OpcListForm extends ActionForm
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -782381147164472731L;
	
	private String instanceName = null;
	
	private List<String> instanceNames = null;
	
	private List<OpcConfigEntry> opcConfigurationDataList = null;
	
    private String fetchList = null;
    private String fetchInstances = null;
    
    /**
     * @JM200731
     * This field is used when loading the list for a specific process point ID.
     */
    private String processPointID = null;

    /**
     * @JM200731
     * This field is used when loading the form for a specific process point ID. 
     */
    private String deviceID = null;
    
    private boolean editor = false;

	public void reset(ActionMapping mapping, HttpServletRequest request) {

	// Reset field values here.

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

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public List<String> getInstanceNames() {
		if (instanceNames == null) {
			instanceNames = new ArrayList<String>(0);
		}
		return instanceNames;
	}

	public void setInstanceNames(List<String> instanceNames) {
		
		this.instanceNames = instanceNames;
	}

	public List<OpcConfigEntry> getOpcConfigurationDataList() {
		if (opcConfigurationDataList == null)
		{
			opcConfigurationDataList = new ArrayList<OpcConfigEntry>(0);
		}
		return opcConfigurationDataList;
	}

	public void setOpcConfigurationDataList(List<OpcConfigEntry> opcConfigurationDataList) {
		this.opcConfigurationDataList = opcConfigurationDataList;
	}

	public boolean isEditor() {
		return editor;
	}

	public void setEditor(boolean editor) {
		this.editor = editor;
	}

	public String getFetchInstances() {
		return fetchInstances;
	}

	public void setFetchInstances(String fetchInstances) {
		this.fetchInstances = fetchInstances;
	}

	public String getFetchList() {
		return fetchList;
	}

	public void setFetchList(String fetchList) {
		this.fetchList = fetchList;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getProcessPointID() {
		return processPointID;
	}

	public void setProcessPointID(String processPointID) {
		this.processPointID = processPointID;
	}
}
