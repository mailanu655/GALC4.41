package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * DeviceFormatId is ID for device format tag
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
 * @see DeviceFormat
 */
@Embeddable
public class DeviceFormatId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "CLIENT_ID")
    private String clientId;

    @Column(name = "TAG")
    private String tag;

    public DeviceFormatId() {
        super();
    }
    
    public DeviceFormatId(String clientId, String tag) {
    	super();
    	this.clientId = clientId;
    	this.tag = tag;
    }

    public String getClientId() {
        return StringUtils.trim(clientId);
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTag() {
        return StringUtils.trim(this.tag);
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeviceFormatId)) {
            return false;
        }
        DeviceFormatId other = (DeviceFormatId) o;
        return this.clientId.equals(other.clientId)
                && this.tag.equals(other.tag);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.clientId.hashCode();
        hash = hash * prime + this.tag.hashCode();
        return hash;
    }

}
