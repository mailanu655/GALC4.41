package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Zone represents plant floor zone
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
@Table(name = "GAL235TBX")
public class Zone extends AuditEntry {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ZONE_ID")
    private String zoneId;

    @Column(name = "DIVISION_ID")
    private String divisionId;

    @Column(name = "ZONE_NAME")
    private String zoneName;

    @Column(name = "ZONE_DESCRIPTION")
    private String zoneDescription;

    public Zone() {
        super();
    }

    public String getZoneId() {
        return StringUtils.trim(this.zoneId);
    }
    
    public String getId() {
    	return getZoneId();
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getDivisionId() {
        return StringUtils.trim(this.divisionId);
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public String getZoneName() {
        return StringUtils.trim(this.zoneName);
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getZoneDescription() {
        return StringUtils.trim(this.zoneDescription);
    }

    public void setZoneDescription(String zoneDescription) {
        this.zoneDescription = zoneDescription;
    }

	@Override
	public String toString() {
		return toString(getZoneId(),getDivisionId());
	}
    
}
