package com.honda.galc.system.config.web.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.entity.conf.Zone;


/**
 * Form bean for a Struts application.
 * @version 	1.0
 * @author
 */
public class ZoneForm extends ActionForm

{
   private static final long serialVersionUID = 1L;

	private String divisionID = null;
    
    private String zoneID = null;
    
    private String description = null;
    
    private String zoneName = null;
    
    private boolean existingZone = true;
    
    private boolean deleteConfirmed = false;
    
    
    private String apply = null;
    private String delete = null;
    private String cancel = null;
    
    private boolean editor = false;
    
    private boolean refreshTree = false;

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
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return Returns the existingZone.
     */
    public boolean isExistingZone() {
        return existingZone;
    }
    /**
     * @param existingZone The existingZone to set.
     */
    public void setExistingZone(boolean existingZone) {
        this.existingZone = existingZone;
    }
    /**
     * @return Returns the zoneID.
     */
    public String getZoneID() {
        return zoneID;
    }
    /**
     * @param zoneID The zoneID to set.
     */
    public void setZoneID(String zoneID) {
        this.zoneID = zoneID;
    }
    /**
     * @return Returns the zoneName.
     */
    public String getZoneName() {
        return zoneName;
    }
    /**
     * @param zoneName The zoneName to set.
     */
    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
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
    
    public void setZoneData(Zone zoneData)
    {
        this.setZoneID(zoneData.getZoneId());
        this.setDescription(zoneData.getZoneDescription());
        this.setZoneName(zoneData.getZoneName());
        this.setDivisionID(zoneData.getDivisionId());
    }
    
    public Zone getZoneData()
    {
        Zone data = new Zone();
        
        data.setZoneId(getZoneID());
        data.setDivisionId(getDivisionID());
        data.setZoneDescription(getDescription());
        data.setZoneName(getZoneName());
        
        return data;
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
}
