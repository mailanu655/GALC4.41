package com.honda.galc.entity.conf;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name = "GAL177TBX")
public class InRepairArea extends AuditEntry{
	
	private static final long serialVersionUID = 1L;
	
    @Id
    @Column(name = "PRODUCT_ID")
    private String productId;
    
    @Column(name = "REPAIR_AREA_NAME")
    private String repairAreaName;
    
    @Column(name = "PARKING_LOCATION")
    private String parkingLocation;
    
    @Column(name = "REJECTION")
    private String rejection;
      
    @Column(name = "RESPONSIBLE_DEPT")
    private String responsibleDept;

    @Column(name = "ACTUAL_TIMESTAMP")
    private Date actualTimestamp;

	public Object getId() {		
		return getProductId();
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getRepairAreaName() {
		return repairAreaName;
	}

	public void setRepairAreaName(String repairAreaName) {
		this.repairAreaName = repairAreaName;
	}

	public String getParkingLocation() {
		return parkingLocation;
	}

	public void setParkingLocation(String parkingLocation) {
		this.parkingLocation = parkingLocation;
	}

	public String getRejection() {
		return rejection;
	}

	public void setRejection(String rejection) {
		this.rejection = rejection;
	}

	public String getResponsibleDept() {
		return responsibleDept;
	}

	public void setResponsibleDept(String responsibleDept) {
		this.responsibleDept = responsibleDept;
	}

	public Date getActualTimestamp() {
		return actualTimestamp;
	}

	public void setActualTimestamp(Date actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}
	

}
