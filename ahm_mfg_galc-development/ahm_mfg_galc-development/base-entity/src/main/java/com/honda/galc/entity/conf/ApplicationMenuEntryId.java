package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * ApplicationMenuEntryId is an ID of ApplicationMenuEntry
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
 * @see ApplicationMenuEntry
 */
@Embeddable
public class ApplicationMenuEntryId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "CLIENT_ID")
    private String clientId;

    private int nodeNumber;

    public ApplicationMenuEntryId() {
        super();
    }

    public ApplicationMenuEntryId(String clientId, int nodeNumber) {
        this.clientId = clientId;
        this.nodeNumber = nodeNumber;
    }

    public String getClientId() {
        return StringUtils.trim(clientId);
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getNodeNumber() {
        return this.nodeNumber;
    }

    public void setNodeNumber(int nodenumber) {
        this.nodeNumber = nodenumber;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ApplicationMenuEntryId)) {
            return false;
        }
        ApplicationMenuEntryId other = (ApplicationMenuEntryId) o;
        return this.getClientId().equals(other.getClientId())
                && (this.nodeNumber == other.nodeNumber);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.clientId.hashCode();
        hash = hash * prime + this.nodeNumber;
        return hash;
    }

}
