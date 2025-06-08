package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * GpcsPlantId is ID of GpcsPlant object
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
 * @see GpcsPlant
 */
@Embeddable
public class GpcsPlantId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "SITE_NAME")
    private String siteName;

    @Column(name = "PLANT_NAME")
    private String plantName;

    public GpcsPlantId() {
        super();
    }

    public String getSiteName() {
        return StringUtils.trim(this.siteName);
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getPlantName() {
        return StringUtils.trim(this.plantName);
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GpcsPlantId)) {
            return false;
        }
        GpcsPlantId other = (GpcsPlantId) o;
        return this.siteName.equals(other.siteName)
                && this.plantName.equals(other.plantName);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.siteName.hashCode();
        hash = hash * prime + this.plantName.hashCode();
        return hash;
    }

}
