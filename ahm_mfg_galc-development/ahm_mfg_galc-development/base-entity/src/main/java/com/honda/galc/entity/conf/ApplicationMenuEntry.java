package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * ApplicationMenuEntry represents a menu item for a terminal
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
@Table(name = "GAL287TBX")
public class ApplicationMenuEntry extends AuditEntry {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ApplicationMenuEntryId id;

    @Column(name="NODENAME")
	private String nodeName;

    @Column(name="PARENTNODENUMBER")
	private int parentNodeNumber;

    @Transient
    private boolean isLeafNode;

    @OneToOne(targetEntity = Application.class,fetch = FetchType.EAGER)
    @JoinColumn(name="NODENAME",referencedColumnName="APPLICATION_ID",updatable=false,insertable=false)
    private Application application;
    
    public ApplicationMenuEntry() {
        super();
    }
    
    public ApplicationMenuEntry(String clientId, int nodeNumber) {
    	this.id = new ApplicationMenuEntryId(clientId, nodeNumber);
    }
    
    public ApplicationMenuEntryId getId() {
        return this.id;
    }

    public void setId(ApplicationMenuEntryId id) {
        this.id = id;
    }

    public String getNodeName() {
        return StringUtils.trim(nodeName);
    }

    public void setNodeName(String nodename) {
        this.nodeName = nodename;
    }

    public int getParentNodeNumber() {
        return this.parentNodeNumber;
    }

    public void setParentNodeNumber(int parentnodenumber) {
        this.parentNodeNumber = parentnodenumber;
    }

    public boolean isLeafNode() {

        return isLeafNode;

    }

    public void setLeafNode(boolean isLeafNode) {

        this.isLeafNode = isLeafNode;

    }
    
    
    public boolean isApplication() {
    	return application != null;
    }
    
    public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}
	
	public String getApplicationName() {
		return application == null ? nodeName : application.getApplicationName();
	}

    public ApplicationMenuEntry clone() {
    	ApplicationMenuEntry menu = new ApplicationMenuEntry();
    	ApplicationMenuEntryId menuId = new ApplicationMenuEntryId();
    	menuId.setClientId(id.getClientId());
    	menuId.setNodeNumber(id.getNodeNumber());
    	menu.setId(id);
    	menu.setParentNodeNumber(this.parentNodeNumber);
    	menu.setNodeName(this.nodeName);
    	return menu;
    }

	@Override
	public String toString() {
		return toString(id.getClientId(),id.getNodeNumber(),getParentNodeNumber());
	}


}
