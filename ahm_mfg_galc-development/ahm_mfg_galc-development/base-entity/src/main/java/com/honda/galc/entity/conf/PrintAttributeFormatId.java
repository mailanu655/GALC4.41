package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * PrintAttributeFormatId is ID for PrintAttributeFormat
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
 * @see PrintAttributeFormat
 */
@Embeddable
public class PrintAttributeFormatId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "FORMID")
    private String formId;

    @Column(name = "ATTRIBUTE")
    private String attribute;

    public PrintAttributeFormatId() {
        super();
    }

    public String getFormId() {
        return StringUtils.trim(this.formId);
    }

    public void setFormId(String formid) {
        this.formId = formid;
    }

    public String getAttribute() {
        return StringUtils.trim(this.attribute);
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PrintAttributeFormatId)) {
            return false;
        }
        PrintAttributeFormatId other = (PrintAttributeFormatId) o;
        return getFormId().equals(other.getFormId())
                && getAttribute().equals(other.getAttribute());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + getFormId().hashCode();
        hash = hash * prime + getAttribute().hashCode();
        return hash;
    }

}
