package com.honda.galc.system.config.web.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.entity.conf.Division;


/**
 * Form bean for a Struts application.
 * Users may access 4 fields on this form:
 * <ul>
 * <li>divisionName - [your comment here]
 * <li>divisionID - [your comment here]
 * <li>divisionDescription - [your comment here]
 * <li>plantName - [your comment here]
 * </ul>
 * @version 	1.0
 * @author
 */
public class DivisionForm extends ActionForm

{

    private static final long serialVersionUID = 1L;

	private String divisionName = null;

    private String divisionID = null;

    private String divisionDescription = null;

    private String plantName = null;
    
    private String siteName = null;
    
    private String apply = null;
    
    private String delete = null;
    
    private boolean deleteConfirmed = false;
    
    private String createFlag = null;
    
    private int sequenceNumber = 0;
    
    private boolean editor = false;
    
    private boolean terminalEditor = false;
    
    private boolean deviceEditor = false;
    
    private boolean refreshTree = false;
    
    private String processLocation;   
    
    private String lineNo;
    
    private String missingGpcsDataMessage="Missing Division to GPCS relationship - needed to determine line, shift and Production Date.";
    

    /**
     * Get divisionName
     * @return String
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * Set divisionName
     * @param <code>String</code>
     */
    public void setDivisionName(String d) {
    	if (d != null)
    	{
    		d = d.trim();
    	}
        this.divisionName = d;
    }

    /**
     * Get divisionID
     * @return String
     */
    public String getDivisionID() {
        return divisionID;
    }

    /**
     * Set divisionID
     * @param <code>String</code>
     */
    public void setDivisionID(String d) {
        this.divisionID = d;
    }

    /**
     * Get divisionDescription
     * @return String
     */
    public String getDivisionDescription() {
        return divisionDescription;
    }

    /**
     * Set divisionDescription
     * @param <code>String</code>
     */
    public void setDivisionDescription(String d) {
    	
    	if (d != null)
    	{
    		d=d.trim();
    	}
        this.divisionDescription = d;
    }

    /**
     * Get plantName
     * @return String
     */
    public String getPlantName() {
        return plantName;
    }

    /**
     * Set plantName
     * @param <code>String</code>
     */
    public void setPlantName(String p) {
        this.plantName = p;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {

        // Reset values are provided as samples only. Change as appropriate.

        divisionName = null;
        divisionID = null;
        divisionDescription = null;
        plantName = null;

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
	 * @return Returns the siteName.
	 */
	public String getSiteName() {
		return siteName;
	}
	
	/**
	 * @param siteName The siteName to set.
	 */
	public void setSiteName(String siteName) {
		
		
		this.siteName = siteName;
	}

	/**
	 * Utility method for setting form values from
	 * DivisionData object.
	 * @param data
	 */
	public void setDivisionData(Division data)
	{
	    setDivisionID(data.getDivisionId());
	    setDivisionDescription(data.getDivisionDescription());
	    setDivisionName(data.getDivisionName());
	    setPlantName(data.getPlantName());
	    setSiteName(data.getSiteName());
	    setSequenceNumber(data.getSequenceNumber());
	}
	
	public Division getDivisionData()
	{
		Division data = new Division();
		data.setDivisionId(getDivisionID());
		data.setPlantName(getPlantName());
		data.setSiteName(getSiteName());
		data.setDivisionName(getDivisionName());
		data.setDivisionDescription(getDivisionDescription());
		data.setSequenceNumber(getSequenceNumber());
		
		
		return data;
	}
	
	/**
	 * @return Returns the accept.
	 */
	public String getApply() {
		return apply;
	}
	/**
	 * @param accept The accept to set.
	 */
	public void setApply(String a) {
		this.apply = a;
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
	/**
	 * @return Returns the sequenceNumber.
	 */
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	
	/**
	 * @param sequenceNumber The sequenceNumber to set.
	 */
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
    /**
     * @return Returns the delete.
     */
    public String getDelete() {
        return delete;
    }
    /**
     * @param delete The delete to set.
     */
    public void setDelete(String delete) {
        this.delete = delete;
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
     * @return Returns the deviceEditor.
     */
    public boolean isDeviceEditor() {
        return deviceEditor;
    }
    /**
     * @param deviceEditor The deviceEditor to set.
     */
    public void setDeviceEditor(boolean deviceEditor) {
        this.deviceEditor = deviceEditor;
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
     * @return Returns the terminalEditor.
     */
    public boolean isTerminalEditor() {
        return terminalEditor;
    }
    /**
     * @param terminalEditor The terminalEditor to set.
     */
    public void setTerminalEditor(boolean terminalEditor) {
        this.terminalEditor = terminalEditor;
    }
    /**
     * @return Returns the refreshTree.
     */
    public boolean isRefreshTree() {
        return refreshTree;
    }
    /**
     * @param refreshTree The refreshTree to set.
     */
    public void setRefreshTree(boolean refreshTree) {
        this.refreshTree = refreshTree;
    }
    
	public String getProcessLocation() {
		return processLocation;
	}

	public void setProcessLocation(String processLocation) {
		this.processLocation = processLocation;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getMissingGpcsDataMessage() {
		return missingGpcsDataMessage;
	}

	public void setMissingGpcsDataMessage(String missingGpcsDataMessage) {
		this.missingGpcsDataMessage = missingGpcsDataMessage;
	}
}
