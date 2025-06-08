package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * ApplicationTaskId is ID of ApplicationTaskId
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
 *
 * @see ApplicationTask
 */
@Embeddable
public class ApplicationTaskId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "APPLICATION_ID")
    private String applicationId;

    @Column(name = "TASK_NAME")
    private String taskName;

    public ApplicationTaskId() {
        super();
    }

    public String getApplicationId() {
        return StringUtils.trim(applicationId);
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getTaskName() {
        return StringUtils.trim(taskName);
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ApplicationTaskId)) {
            return false;
        }
        ApplicationTaskId other = (ApplicationTaskId) o;
        return this.applicationId.equals(other.applicationId)
                && this.taskName.equals(other.taskName);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.applicationId.hashCode();
        hash = hash * prime + this.taskName.hashCode();
        return hash;
    }
}
