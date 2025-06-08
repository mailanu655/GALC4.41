package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * TaskSpec is a task specification descriptor
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
@Table(name = "GAL244TBX")
public class TaskSpec extends AuditEntry {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "TASK_NAME")
    private String taskName;

    @Column(name = "TASKSPEC_DESCRIPTION")
    private String taskSpecDescription;

    @Column(name = "STATEFULL_SESSION_BEAN_FLAG")
    private short statefullSessionBeanFlag;

    @Column(name = "JNDI_NAME")
    private String jndiName;
    
//    @OneToOne(fetch = FetchType.EAGER, cascade = {})
//    @JoinColumn(name = "TASK_NAME", referencedColumnName = "TASK_NAME",
//              unique = true, nullable = false, insertable = false, updatable = false)
//
//    //   @OneToOne(mappedBy="taskSpec")
    @Transient
    private ApplicationTask applicationTask;
    
    public TaskSpec() {
        super();
    }

    public String getTaskName() {
        return StringUtils.trim(this.taskName);
    }
    
    public String getId() {
    	return getTaskName();
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskSpecDescription() {
        return StringUtils.trim(this.taskSpecDescription);
    }

    public void setTaskSpecDescription(String taskspecDescription) {
        this.taskSpecDescription = taskspecDescription;
    }

    public short getStatefullSessionBeanFlag() {
        return this.statefullSessionBeanFlag;
    }

    public void setStatefullSessionBeanFlag(short statefullSessionBeanFlag) {
        this.statefullSessionBeanFlag = statefullSessionBeanFlag;
    }

    public String getJndiName() {
        return StringUtils.trim(this.jndiName);
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public ApplicationTask getApplicationTask() {
        return applicationTask;
    }

    public void setApplicationTask(ApplicationTask applicationTask) {
        this.applicationTask = applicationTask;
    }
	@Override
	public String toString() {
		return toString(getTaskName(),getJndiName());
	}
    
    

}
