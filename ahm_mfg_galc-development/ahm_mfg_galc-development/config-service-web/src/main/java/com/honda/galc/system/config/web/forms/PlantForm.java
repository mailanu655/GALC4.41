package com.honda.galc.system.config.web.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.conf.PlantId;


/**
 * Form bean for a Struts application.
 * Users may access 2 fields on this form:
 * <ul>
 * <li>plantDescription - [your comment here]
 * <li>plantName - [your comment here]
 * </ul>
 * @version 	1.0
 * @author
 */
public class PlantForm extends ActionForm

{

	private static final long serialVersionUID = 1L;
	
	private String plantDescription = null;

    private String plantName = null;
    
    private String createFlag = "false";
    
    private String siteName = null;
    
    private String apply = null;
    private String delete = null;
    
    private boolean deleteConfirmed = false;
    private boolean editor = false;
    private boolean divisionEditor = false;
    
    private boolean refreshTree = false;

    /**
     * Get plantDescription
     * @return String
     */
    public String getPlantDescription() {
        return plantDescription;
    }

    /**
     * Set plantDescription
     * @param <code>String</code>
     */
    public void setPlantDescription(String p) {
        this.plantDescription = p;
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
     * Constructor
     */
    public PlantForm() {
        super();
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {

        // Reset values are provided as samples only. Change as appropriate.

        plantDescription = null;
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
	 * @return Returns the createFlag.
	 */
	public String getCreateFlag() {
		if (createFlag == null)
		{
			createFlag = "false";
		}
		return createFlag;
	}
	/**
	 * @param createFlag The createFlag to set.
	 */
	public void setCreateFlag(String createFlag) {
		this.createFlag = createFlag;
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
	 * Utility method to build a PlantData object from the form.
	 * @return
	 */
	public Plant getPlant()
	{
		Plant plant = new Plant();
		plant.setPlantLongDescription(getPlantDescription());
		PlantId plantId = new PlantId();
		plantId.setPlantName(getPlantName());
		plantId.setSiteName(getSiteName());
		plant.setId(plantId);
		
		return plant;
	}
	
	/**
	 * Utility method to store settings from  a PlantData object
	 * into the form.
	 * @return
	 */
	public void setPlant(Plant plant)
	{
		setPlantName(plant.getId().getPlantName());
		setPlantDescription(plant.getPlantLongDescription());
		setSiteName(plant.getId().getSiteName());
		return;
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
     * @return Returns the divisionEditor.
     */
    public boolean isDivisionEditor() {
        return divisionEditor;
    }
    /**
     * @param divisionEditor The divisionEditor to set.
     */
    public void setDivisionEditor(boolean divisionEditor) {
        this.divisionEditor = divisionEditor;
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
