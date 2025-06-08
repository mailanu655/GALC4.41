package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * PreviouLineId is ID of PreviousLine
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
 * <TD>Jeffray Huang</TD>
 * <TD>Aug 31, 2010</TD>
 * <TD>&nbsp;</TD>
 * <TD>Created</TD>
 * </TR>
 * </TABLE>
 *
 * @see Plant
 */
@Embeddable
public class PreviousLineId implements Serializable {
    private static final long serialVersionUID = 1L;

    
    @Column(name = "LINE_ID")
    private String lineId;

    @Column(name = "PREVIOUS_LINE_ID")
    private String previousLineId;


    public PreviousLineId() {
        super();
    }

 
    public String getLineId() {
		return StringUtils.trim(lineId);
	}


	public void setLineId(String lineId) {
		this.lineId = lineId;
	}


	public String getPreviousLineId() {
		return StringUtils.trim(previousLineId);
	}


	public void setPreviousLineId(String previousLineId) {
		this.previousLineId = previousLineId;
	}


	@Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PreviousLineId)) {
            return false;
        }
        PreviousLineId other = (PreviousLineId) o;
        return getLineId().equals(other.getLineId()) && getPreviousLineId().equals(other.getPreviousLineId());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.lineId.hashCode();
        hash = hash * prime + this.previousLineId.hashCode();
        return hash;
    }

}
