package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * PrintFormId is ID of PrintForm
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
 * @see PrintForm
 */
@Embeddable
public class PrintFormId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "FORM_ID")
    private String formId;

    @Column(name = "DESTINATION_ID")
    private String destinationId;

    public PrintFormId() {
        super();
    }

    public PrintFormId(String formId, String destinationId) {
		this.formId = formId;
		this.destinationId = destinationId;
	}
    
    public String getFormId() {
        return StringUtils.trim(this.formId);
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getDestinationId() {
        return StringUtils.trim(this.destinationId);
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrintFormId other = (PrintFormId) obj;
		if (getDestinationId() == null) {
			if (other.getDestinationId() != null)
				return false;
		} else if (!getDestinationId().equals(other.getDestinationId()))
			return false;
		if (getFormId() == null) {
			if (other.getFormId() != null)
				return false;
		} else if (!getFormId().equals(other.getFormId()))
			return false;
		return true;
	}

     @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.formId.hashCode();
        hash = hash * prime + this.destinationId.hashCode();
        return hash;
    }

}
