package com.honda.galc.system.config.web.forms;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.entity.conf.Line;


/**
 * Form bean for a Struts application.
 * Users may access 10 fields on this form:
 * <ul>
 * <li>divisionName - [your comment here]
 * <li>maxInventory - [your comment here]
 * <li>divisionID - [your comment here]
 * <li>lineType - [your comment here]
 * <li>lineDescription - [your comment here]
 * <li>entryProcessPointID - [your comment here]
 * <li>plantName - [your comment here]
 * <li>lineName - [your comment here]
 * <li>minInventory - [your comment here]
 * <li>lineID - [your comment here]
 * </ul>
 * @version 	1.0
 * @author
 */
public class LineForm extends ActionForm

{

	private static final long serialVersionUID = 1L;

	private String divisionName = null;

    private int maxInventory = 0;

    private String divisionID = null;

    private int lineType = 0;

    private String entryProcessPointID = null;

    private String lineDescription = null;

    private String lineName = null;

    private String plantName = null;

    private String lineID = null;

    private int minInventory = 0;
    
    private int sequenceNo;
    private String siteName;
    private int stdInventory;
    
    
    private String apply = null;
    private String delete = null;
    private String cancel = null;
    
    private boolean createFlag = false;
    
    private String previousLinesText = null;
    
    private boolean editor = false;
    private boolean deleteConfirmed = false;
    
    private boolean refreshTree = false;
    
    private int processPointCount = 0;

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
        this.divisionName = d;
    }

    /**
     * Get maxInventory
     * @return int
     */
    public int getMaxInventory() {
        return maxInventory;
    }

    /**
     * Set maxInventory
     * @param <code>int</code>
     */
    public void setMaxInventory(int m) {
        this.maxInventory = m;
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
     * Get lineType
     * @return int
     */
    public int getLineType() {
        return lineType;
    }

    /**
     * Set lineType
     * @param <code>int</code>
     */
    public void setLineType(int l) {
        this.lineType = l;
    }

    /**
     * Get entryProcessPointID
     * @return String
     */
    public String getEntryProcessPointID() {
        return entryProcessPointID;
    }

    /**
     * Set entryProcessPointID
     * @param <code>String</code>
     */
    public void setEntryProcessPointID(String e) {
        this.entryProcessPointID = e;
    }

    /**
     * Get lineDescription
     * @return String
     */
    public String getLineDescription() {
        return lineDescription;
    }

    /**
     * Set lineDescription
     * @param <code>String</code>
     */
    public void setLineDescription(String l) {
        this.lineDescription = l;
    }

    /**
     * Get lineName
     * @return String
     */
    public String getLineName() {
        return lineName;
    }

    /**
     * Set lineName
     * @param <code>String</code>
     */
    public void setLineName(String l) {
        this.lineName = l;
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

    /**
     * Get lineID
     * @return String
     */
    public String getLineID() {
        return lineID;
    }

    /**
     * Set lineID
     * @param <code>String</code>
     */
    public void setLineID(String l) {
        this.lineID = l;
    }

    /**
     * Get minInventory
     * @return int
     */
    public int getMinInventory() {
        return minInventory;
    }

    /**
     * Set minInventory
     * @param <code>int</code>
     */
    public void setMinInventory(int m) {
        this.minInventory = m;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {

        // Reset values are provided as samples only. Change as appropriate.

        divisionName = null;
        maxInventory = 0;
        divisionID = null;
        lineType = 0;
        entryProcessPointID = null;
        lineDescription = null;
        lineName = null;
        plantName = null;
        lineID = null;
        minInventory = 0;

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
     * @return Returns the sequenceNo.
     */
    public int getSequenceNo() {
        return sequenceNo;
    }
    /**
     * @param sequenceNo The sequenceNo to set.
     */
    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
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
     * @return Returns the stdInventory.
     */
    public int getStdInventory() {
        return stdInventory;
    }
    /**
     * @param stdInventory The stdInventory to set.
     */
    public void setStdInventory(int stdInventory) {
        this.stdInventory = stdInventory;
    }
    
    /**
     * Utility method for setting values from line data.
     * @param data
     */
    public void setLineData(Line data)
    {
        setDivisionID(data.getDivisionId());
        setDivisionName(data.getDivisionName());
        setEntryProcessPointID(data.getEntryProcessPointId());
        setLineDescription(data.getLineDescription());
        setLineID(data.getLineId());
        setLineName(data.getLineName());
        setLineType(data.getLineTypeId());
        setMaxInventory(data.getMaximumInventory());
        setMinInventory(data.getMinimumInventory());
        setPlantName(data.getPlantName());
        setSequenceNo(data.getLineSequenceNumber());
        setSiteName(data.getSiteName());
        setStdInventory(data.getStandardInventory());
        setPreviousLinesText(data.getPreviousLinesIdsAsString());
        setProcessPointCount(data.getProcessPointListCount());
        
    }
    
    /**
     * Utility method for getting a line data object from the form.
     * @return
     */
    public Line getLineData()
    {
        Line line = new Line();
    	
        line.setDivisionId(getDivisionID());
        line.setDivisionName(getDivisionName());
        line.setEntryProcessPointId(getEntryProcessPointID());
        line.setLineDescription(getLineDescription());
        line.setLineId(getLineID());
        line.setLineName(getLineName());
        line.setLineTypeId(getLineType());
        line.setMaximumInventory(getMaxInventory());
        line.setMinimumInventory(getMinInventory());
        line.setPlantName(getPlantName());
        line.setLineSequenceNumber(getSequenceNo());
        line.setSiteName(getSiteName());
        line.setStandardInventory(getStdInventory());
        line.setPreviousLines(getPreviousLinesText());
        return line;
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
    public boolean isCreateFlag() {
        return createFlag;
    }
    
    /**
     * @param createFlag The createFlag to set.
     */
    public void setCreateFlag(boolean createFlag) {
        this.createFlag = createFlag;
    }
    
    /**
     * @return Returns the previousLinesText.
     */
    public String getPreviousLinesText() {
        if (previousLinesText == null)
        {
            previousLinesText = "";
        }
        return previousLinesText;
    }
    
    /**
     * @param previousLinesText The previousLinesText to set.
     */
    public void setPreviousLinesText(String previousLinesText) {
        this.previousLinesText = previousLinesText;
    }
    
    /**
     * 
     * @param previousLinesList
     */
    public void setPreviousLinesList(List previousLinesList)
    {
        StringBuffer buf = new StringBuffer();
        
        Iterator iterator = previousLinesList.iterator();
        while (iterator.hasNext())
        {
            buf.append(iterator.next());
            if (iterator.hasNext())
            {
                buf.append(",");
            }
        }
        
        setPreviousLinesText(buf.toString());
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
     * @return Returns the cancel.
     */
    public String getCancel() {
        return cancel;
    }
    /**
     * @param cancel The cancel to set.
     */
    public void setCancel(String cancel) {
        this.cancel = cancel;
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
    /**
     * @return Returns the processPointCount.
     */
    public int getProcessPointCount() {
        return processPointCount;
    }
    /**
     * @param processPointCount The processPointCount to set.
     */
    public void setProcessPointCount(int processPointCount) {
        this.processPointCount = processPointCount;
    }
    
	public String getMissingGpcsDataMessage() {
		return missingGpcsDataMessage;
	}

	public void setMissingGpcsDataMessage(String missingGpcsDataMessage) {
		this.missingGpcsDataMessage = missingGpcsDataMessage;
	}
}
