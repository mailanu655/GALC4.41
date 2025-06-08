package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * GpcsPlant represents plant from the viewpoint of GPCS
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
@Table(name = "GAL237TBX")
public class GpcsPlant extends AuditEntry  {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private GpcsPlantId id;

    @Column(name = "GPCS_PLANT_CODE")
    private String gpcsPlantCode;

    public GpcsPlant() {
        super();
    }

    public GpcsPlantId getId() {
        return this.id;
    }

    public void setId(GpcsPlantId id) {
        this.id = id;
    }

    public String getGpcsPlantCode() {
        return StringUtils.trim(this.gpcsPlantCode);
    }

    public void setGpcsPlantCode(String gpcsPlantCode) {
        this.gpcsPlantCode = gpcsPlantCode;
    }
	@Override
	public String toString() {
		return toString(id.getPlantName());
	}
    

}
