package com.honda.galc.entity.product;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@MappedSuperclass()
public abstract class ProductHistory extends AuditEntry {
    
    private static final long serialVersionUID = 1L;
    
    @Column(name="PROCESS_COUNT")
	private int processCount; 
	
    @Column(name="DEVICE_ID")
	private String deviceId; 
    
    @Column(name = "PRODUCTION_DATE")
    private Date productionDate;
    
    @Column(name = "ASSOCIATE_NO")
    private String associateNo;

    @Transient
    private String processPointName;
    
    @Transient
    private String carrierId;
    
    public abstract String getProductId();
    public abstract void setProductId(String productId);
    public abstract String getProcessPointId();
    public abstract void setProcessPointId(String processPointId);
    public abstract Timestamp getActualTimestamp();
    public abstract void setActualTimestamp(Timestamp timestamp);
    public abstract String getApproverNo();
    public abstract void setApproverNo(String approverNo);

	public String getProcessPointName() {
		return processPointName;
	}

	public void setProcessPointName(String processPointName) {
		this.processPointName = processPointName;
	}
	
	public String getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(String carrierId) {
		this.carrierId = carrierId;
	}
	
	public int getProcessCount() {
		return processCount;
	}

	public void setProcessCount(int processCount) {
		this.processCount = processCount;
	}
	public String getDeviceId() {
		return  StringUtils.trimToEmpty(deviceId);
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}
	public String getAssociateNo() {
		return associateNo;
	}
	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
	}
	

}
