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
@Table(name = "GAL179TBX")
public class InspectionPart extends AuditEntry {
    @Id
    @Column(name = "INSPECTION_PART_NAME")
    private String inspectionPartName;

    @Column(name = "INSPECTION_PART_DESC_SHORT")
    private String inspectionPartDescShort;

    @Column(name = "INSPECTION_PART_DESC_LONG")
    private String inspectionPartDescLong;

    private static final long serialVersionUID = 1L;

    public InspectionPart() {
        super();
    }

    public String getInspectionPartName() {
        return StringUtils.trim(this.inspectionPartName);
    }
    
    public String getId() {
    	return getInspectionPartName();
    }

    public void setInspectionPartName(String inspectionPartName) {
        this.inspectionPartName = inspectionPartName;
    }

    public String getInspectionPartDescShort() {
        return  StringUtils.trim(this.inspectionPartDescShort);
    }

    public void setInspectionPartDescShort(String inspectionPartDescShort) {
        this.inspectionPartDescShort = inspectionPartDescShort;
    }

    public String getInspectionPartDescLong() {
    	return StringUtils.trim(this.inspectionPartDescLong);
    }

    public void setInspectionPartDescLong(String inspectionPartDescLong) {
        this.inspectionPartDescLong = inspectionPartDescLong;
    }

	@Override
	public String toString() {
		return toString(getInspectionPartName());
	}
 
}
