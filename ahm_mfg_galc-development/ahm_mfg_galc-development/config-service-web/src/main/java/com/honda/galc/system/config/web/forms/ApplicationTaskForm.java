package com.honda.galc.system.config.web.forms;

import java.util.ArrayList;
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
public class ApplicationTaskForm extends ActionForm

{
	private static final long serialVersionUID = 1L;
	private static final String ADD_TASK = "add";
    
    private String taskName;
    
    private boolean commitFlag=false;
    private boolean beginFlag = false;
    
    private String taskArg = null;
    
    private boolean inputEventFlag = false;
    
    
    private List<TaskSpec> taskSpecList = null;
    
    private String applicationID = null;
    
    /**
     * Sequence in the task list.
     */
    private int sequenceNo = 0;
    
    /**
     * Operation is either add or modify.
     */
    private String operation = ADD_TASK;

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
     * @return Returns the beginFlag.
     */
    public boolean isBeginFlag() {
        return beginFlag;
    }
    /**
     * @param beginFlag The beginFlag to set.
     */
    public void setBeginFlag(boolean beginFlag) {
        this.beginFlag = beginFlag;
    }
    /**
     * @return Returns the commitFlag.
     */
    public boolean isCommitFlag() {
        return commitFlag;
    }
    /**
     * @param commitFlag The commitFlag to set.
     */
    public void setCommitFlag(boolean commitFlag) {
        this.commitFlag = commitFlag;
    }
    /**
     * @return Returns the inputEventFlag.
     */
    public boolean isInputEventFlag() {
        return inputEventFlag;
    }
    /**
     * @param inputEventFlag The inputEventFlag to set.
     */
    public void setInputEventFlag(boolean inputEventFlag) {
        this.inputEventFlag = inputEventFlag;
    }
    /**
     * @return Returns the taskArg.
     */
    public String getTaskArg() {
        if (taskArg == null)
        {
            taskArg = "";
        }
        return taskArg;
    }
    /**
     * @param taskArg The taskArg to set.
     */
    public void setTaskArg(String taskArg) {
        this.taskArg = taskArg;
    }
    /**
     * @return Returns the taskName.
     */
    public String getTaskName() {
        if (taskName == null)
        {
            taskName = "";
        }
        return taskName;
    }
    /**
     * @param taskName The taskName to set.
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    
    
    /**
     * @return Returns the taskNameList, which is a list of TaskSpecData objects
     */
    public List<TaskSpec> getTaskSpecList() {
        if (taskSpecList == null)
        {
            taskSpecList = new ArrayList<TaskSpec>(0);
        }
        return taskSpecList;
    }
    
    /**
     * @param taskNameList The taskNameList to set. A list of TaskSpecData objects
     */
    public void setTaskSpecList(List<TaskSpec> taskSpecList) {
        this.taskSpecList = taskSpecList;
    }
    /**
     * @return Returns the applicationID.
     */
    public String getApplicationID() {
        if (applicationID == null)
        {
            applicationID = "";
        }
        return applicationID;
    }
    /**
     * @param applicationID The applicationID to set.
     */
    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
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
}
