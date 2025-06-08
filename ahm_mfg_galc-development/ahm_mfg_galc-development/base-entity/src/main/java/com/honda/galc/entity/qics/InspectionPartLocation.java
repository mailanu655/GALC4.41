package com.honda.galc.entity.qics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name = "GAL181TBX")
public class InspectionPartLocation extends AuditEntry {
    @Id
    @Column(name = "INSPECTION_PART_LOCATION_NAME")
    private String inspectionPartLocationName;

    @Column(name = "INSPECTION_PART_LOC_DESC_SHORT")
    private String inspectionPartLocDescShort;

    @Column(name = "INSPECTION_PART_LOC_DESC_LONG")
    private String inspectionPartLocDescLong;

    private static final long serialVersionUID = 1L;

    public InspectionPartLocation() {
        super();
    }

    public String getInspectionPartLocationName() {
        return StringUtils.trim(this.inspectionPartLocationName);
    }
    
    public String getId() {
    	return  getInspectionPartLocationName();
    }

    public void setInspectionPartLocationName(String inspectionPartLocationName) {
        this.inspectionPartLocationName = inspectionPartLocationName;
    }

    public String getInspectionPartLocDescShort() {
        return StringUtils.trim(this.inspectionPartLocDescShort);
    }

    public void setInspectionPartLocDescShort(String inspectionPartLocDescShort) {
        this.inspectionPartLocDescShort = inspectionPartLocDescShort;
    }

    public String getInspectionPartLocDescLong() {
        return  StringUtils.trim(this.inspectionPartLocDescLong);
    }

    public void setInspectionPartLocDescLong(String inspectionPartLocDescLong) {
        this.inspectionPartLocDescLong = inspectionPartLocDescLong;
    }

	@Override
	public String toString() {
		return toString(getInspectionPartLocationName());
	}
 
}
