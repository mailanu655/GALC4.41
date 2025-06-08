package com.honda.galc.system.config.web.forms;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.entity.conf.TaskSpec;

/**
 * Form bean for a Struts application.
 * @version 	1.0
 * @author
 */
public class TaskSpecificationForm extends ActionForm

{

    private static final long serialVersionUID = 1L;

	private String taskName = null;
    
    private String jndiName = null;
    
    private String taskDescription = null;
    
    private boolean statefulSessionBean = false;
    
    private String apply = null;
    
    private String delete = null;
    
    private String cancel = null;
    
    private String findTask = null;
    

    // Task specification List
    private List<TaskSpec> taskSpecList = null;
    
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
     * @return Returns the findTask.
     */
    public String getFindTask() {
        return findTask;
    }
    /**
     * @param findTask The findTask to set.
     */
    public void setFindTask(String findTask) {
        this.findTask = findTask;
    }
    /**
     * @return Returns the jndiName.
     */
    public String getJndiName() {
        return jndiName;
    }
    /**
     * @param jndiName The jndiName to set.
     */
    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }
    /**
     * @return Returns the statefulSessionBean.
     */
    public boolean isStatefulSessionBean() {
        return statefulSessionBean;
    }
    /**
     * @param statefulSessionBean The statefulSessionBean to set.
     */
    public void setStatefulSessionBean(boolean statefulSessionBean) {
        this.statefulSessionBean = statefulSessionBean;
    }
    /**
     * @return Returns the taskDescription.
     */
    public String getTaskDescription() {
        return taskDescription;
    }
    /**
     * @param taskDescription The taskDescription to set.
     */
    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }
    /**
     * @return Returns the taskName.
     */
    public String getTaskName() {
        return taskName;
    }
    /**
     * @param taskName The taskName to set.
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    
    /**
     * @return Returns the taskSpecList.
     */
    public List<TaskSpec> getTaskSpecList() {
        if (taskSpecList == null)
        {
            taskSpecList = new java.util.ArrayList<TaskSpec>(0);
           
        }
        return taskSpecList;
    }
    
    
    /**
     * @param taskSpecList The taskSpecList to set.
     */
    public void setTaskSpecList(List<TaskSpec> taskSpecList) {
        this.taskSpecList = taskSpecList;
    }
    
    /**
     * Utility method for obtaining TaskSpecData from form.
     * @return
     */
    public TaskSpec getTaskSpecData()
    {
        TaskSpec data = new TaskSpec();
        
        data.setJndiName(getJndiName());
        data.setStatefullSessionBeanFlag((short)(isStatefulSessionBean()? 1: 0));
        data.setTaskName(getTaskName());
        data.setTaskSpecDescription(getTaskDescription());
        
        return data;
    }
}
