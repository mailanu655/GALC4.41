package com.honda.galc.entity.conf;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.jdbc.ElementJoinColumn;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.ApplicationType;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Application is a configuration entry for application (team leader or process point)
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
@Table(name = "GAL241TBX")
public class Application extends AuditEntry {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "APPLICATION_ID")
    private String applicationId;

    @Column(name = "APPLICATION_NAME")
    private String applicationName;

    @Column(name = "APPLICATION_TYPE")
    private int applicationTypeId;

    @Column(name = "TERMINAL_APPLICATION_FLAG")
    private short terminalApplicationFlag;

    @Column(name = "SCREEN_ID")
    private String screenId;

    @Column(name = "WINDOW_TITLE")
    private String windowTitle;

    @Column(name = "SCREEN_CLASS")
    private String screenClass;

    @Column(name = "APPLICATION_DESCRIPTION")
    private String applicationDescription;

    private int preload;

    @Column(name = "SESSION_REQUIRED")
    private short sessionRequired;

    @Column(name = "PERSISTENT_SESSION")
    private short persistentSession;

 //   @OneToMany(targetEntity = ApplicationTask.class, mappedBy = "application", cascade = {}, fetch = FetchType.EAGER)
    @OneToMany(targetEntity = ApplicationTask.class,fetch = FetchType.EAGER,cascade={})
    @ElementJoinColumn(name="APPLICATION_ID", referencedColumnName="APPLICATION_ID",updatable = false,insertable=false)
    @OrderBy(" sequenceNo ASC")
    private List<ApplicationTask> applicationTasks;

    public Application() {
        super();
    }

    public String getApplicationId() {
        return StringUtils.trim(applicationId);
    }

    public String getId() {
    	return getApplicationId();
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return StringUtils.trim(applicationName);
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public int getApplicationTypeId() {
        return this.applicationTypeId;
    }

    public void setApplicationTypeId(int applicationType) {
        this.applicationTypeId = applicationType;
    }

    public void setApplicationType(ApplicationType type) {
        this.applicationTypeId = type.getId();
    }

    public ApplicationType getApplicationType() {
        return ApplicationType.getType(applicationTypeId);
    }

    public short getTerminalApplicationFlag() {
        return this.terminalApplicationFlag;
    }

    public void setTerminalApplicationFlag(short terminalApplicationFlag) {
        this.terminalApplicationFlag = terminalApplicationFlag;
    }

    public String getScreenId() {
        return StringUtils.trim(screenId);
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    public String getWindowTitle() {
        return StringUtils.trim(this.windowTitle);
    }

    public void setWindowTitle(String windowTitle) {
        this.windowTitle = StringUtils.trim(windowTitle);
    }

    public String getScreenClass() {
        return StringUtils.trim(screenClass);
    }

    public void setScreenClass(String screenClass) {
        this.screenClass = screenClass;
    }

    public String getApplicationDescription() {
        return StringUtils.trim(applicationDescription);
    }

    public void setApplicationDescription(String applicationDescription) {
        this.applicationDescription = applicationDescription;
    }

    public int getPreload() {
        return this.preload;
    }

    public void setPreload(int preload) {
        this.preload = preload;
    }

    public short getSessionRequired() {
        return this.sessionRequired;
    }

    public void setSessionRequired(short sessionRequired) {
        this.sessionRequired = sessionRequired;
    }

    public short getPersistentSession() {
        return this.persistentSession;
    }

    public void setPersistentSession(short persistentSession) {
        this.persistentSession = persistentSession;
    }

    public List<ApplicationTask> getApplicationTasks() {
        if(applicationTasks == null) applicationTasks = new ArrayList<ApplicationTask>();
        return applicationTasks;
    }

    public void setApplicationTasks(List<ApplicationTask> applicationTasks) {
        this.applicationTasks = applicationTasks;
    }

	@Override
	public String toString() {
		return toString(getApplicationId());
	}
}
