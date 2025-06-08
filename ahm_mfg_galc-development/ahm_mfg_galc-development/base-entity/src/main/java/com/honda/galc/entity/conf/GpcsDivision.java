package com.honda.galc.entity.conf;

import com.honda.galc.entity.AuditEntry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * GpcsDivision represents GPCS division
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
@Table(name = "GAL238TBX")
public class GpcsDivision extends AuditEntry  {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "DIVISION_ID")
    private String divisionId;

    @Column(name = "GPCS_PLANT_CODE")
    private String gpcsPlantCode;

    @Column(name = "GPCS_LINE_NO")
    private String gpcsLineNo;

    @Column(name = "GPCS_PROCESS_LOCATION")
    private String gpcsProcessLocation;

    public GpcsDivision() {
        super();
    }

    public String getDivisionId() {
        return StringUtils.trim(this.divisionId);
    }
    
    public String getId() {
    	return getDivisionId();
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = StringUtils.trim(divisionId);
    }

    public String getGpcsPlantCode() {
        return StringUtils.trim(this.gpcsPlantCode);
    }

    public void setGpcsPlantCode(String gpcsPlantCode) {
        this.gpcsPlantCode = gpcsPlantCode;
    }

    public String getGpcsLineNo() {
        return StringUtils.trim(this.gpcsLineNo);
    }
    
	public void setGpcsLineNo(String gpcsLineNo) {
		this.gpcsLineNo = gpcsLineNo;
	}

    public String getGpcsProcessLocation() {
        return StringUtils.trim(this.gpcsProcessLocation);
    }

    public void setGpcsProcessLocation(String gpcsProcessLocation) {
        this.gpcsProcessLocation = gpcsProcessLocation;
    }
	@Override
	public String toString() {
		return toString(getDivisionId(),getGpcsLineNo(),getGpcsProcessLocation());
	}
    
}
