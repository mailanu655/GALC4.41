/**
 * 
 */
package com.honda.galc.entity.product;

import com.honda.galc.entity.AuditEntry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import java.util.Date;


/**
 * @author vf031824
 *
 */
@Entity
@Table(name = "PARTS_LOADING_HISTORY_TBX")
public class PartsLoadingHistory extends AuditEntry{
    private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="PROCESS_POINT_ID")
	private String processPointId;
	
	@Column(name="PART_NAME")
	private String partName;
	
	@Column(name="PART_NUMBER")
	private String partNumber;
	
	@Column(name="PARTS_CONTAINER_SERIAL_NUMBER")
	private String partsContainerSerialNumber;
	
	@Column(name="ASSOCIATE_ID")
	private String associateId;
	
	@Column(name="ACTUAL_TIMESTAMP")
	private Date actualTimestamp;
	
	@Column(name="PARTS_LOADING_STATUS")
	private int partsLoadingStatus;
	
		
	public PartsLoadingHistory() {
		super();
	}
	
	public Object getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getProcessPointId() {
		return StringUtils.trim(processPointId);
	}
	
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getPartName() {
		return StringUtils.trim(this.partName);
	}
	
	public void setPartName(String partName) {
		this.partName = partName;
	}
	
	public String getPartNumber() {
		return StringUtils.trim(this.partNumber);
	}
	
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	
	public String getPartsContainerSerialNumber() {
		return StringUtils.trim(this.partsContainerSerialNumber);
	}
	
	public void setPartsContainerSerialNumber(String PartsContainerSerialNumber) {
		this.partsContainerSerialNumber = PartsContainerSerialNumber;
	}
	
	public String getAssociateId() {
		return StringUtils.trim(this.associateId);
	}
	
	public void setAssociateId(String associateId) {
		this.associateId = associateId;
	}
	
    public Date getActualTimestamp() {
        return actualTimestamp;
    }

    public void setActualTimestamp(Date actualTimestamp) {
        this.actualTimestamp = actualTimestamp;
    }
    
	public int getPartsLoadingStatus() {
		return this.partsLoadingStatus;
	}
	
	public void setPartsLoadingStatus(int partsLoadingStatus) {
		this.partsLoadingStatus = partsLoadingStatus;
	}
}