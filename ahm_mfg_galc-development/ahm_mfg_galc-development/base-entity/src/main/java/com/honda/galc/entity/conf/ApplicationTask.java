package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * ApplicationTask represents application task bean descriptor identifying<br>
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
@Table(name = "GAL243TBX")
public class ApplicationTask extends AuditEntry {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ApplicationTaskId id;

    @Column(name = "JNDI_NAME")
    private String jndiName;

    @Column(name = "SEQUENCE_NO")
    private int sequenceNo;

    @Column(name = "INPUT_EVENT_EXIST_FLAG")
    private short inputEventExistFlag;

    @Column(name = "BEGIN_FLAG")
    private short beginFlag;

    @Column(name = "COMMIT_FLAG")
    private short commitFlag;

    @Column(name = "STATEFULL_SESSION_BEAN_FLAG")
    private short statefullSessionBeanFlag;

    private String argument;
    
//    @OneToOne(mappedBy="applicationTask") 
    @OneToOne(fetch = FetchType.EAGER, cascade = {})
    @JoinColumn(name = "TASK_NAME", referencedColumnName = "TASK_NAME",
              unique = true, nullable = false, insertable = false, updatable = false)
    private TaskSpec taskSpec;

    public ApplicationTask() {
        super();
    }
    
    public ApplicationTask(String applicationId,String taskName) {
    	this.id = new ApplicationTaskId();
    	id.setApplicationId(applicationId);
    	id.setTaskName(taskName);
    }

    public ApplicationTaskId getId() {
        return this.id;
    }

    public void setId(ApplicationTaskId id) {
        this.id = id;
    }

    public String getJndiName() {
        return StringUtils.trim(jndiName);
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public int getSequenceNo() {
        return this.sequenceNo;
    }

    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public short getInputEventExistFlag() {
        return this.inputEventExistFlag;
    }

    public void setInputEventExistFlag(short inputEventExistFlag) {
        this.inputEventExistFlag = inputEventExistFlag;
    }

    public short getBeginFlag() {
        return this.beginFlag;
    }

    public void setBeginFlag(short beginFlag) {
        this.beginFlag = beginFlag;
    }

    public short getCommitFlag() {
        return this.commitFlag;
    }

    public void setCommitFlag(short commitFlag) {
        this.commitFlag = commitFlag;
    }

    public short getStatefullSessionBeanFlag() {
        return this.statefullSessionBeanFlag;
    }

    public void setStatefullSessionBeanFlag(short statefullSessionBeanFlag) {
        this.statefullSessionBeanFlag = statefullSessionBeanFlag;
    }

    public String getArgument() {
        return StringUtils.trim(argument);
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public TaskSpec getTaskSpec() {
        return taskSpec;
    }

    public void setTaskSpec(TaskSpec taskSpec) {
        this.taskSpec = taskSpec;
    }

//    public Application getApplication() {
//        return application;
//    }
//
//    public void setApplication(Application application) {
//        this.application = application;
//    }
	@Override
	public String toString() {
		return toString(id.getApplicationId(),id.getTaskName());
	}
    

}
