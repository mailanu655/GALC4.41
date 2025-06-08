package com.honda.galc.entity.qi;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>QiDefectDevice Class description</h3>
 * <p>
 * QiDefectDevice
 * </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * 
 * </TABLE>
 * 
 * 
 *    
 * @author Kamlesh Maharjan
 * @since July 18, 2019
 * 
 */

@Entity
@Table(name = "QI_DEFECT_DEVICE_TBX")
public class QiDefectDevice extends AuditEntry {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "DEFECTRESULTID", nullable=false)
	private long defectResultId;
	
	@Column(name="DEVICE_ID")
	private String deviceId; 
		
	@Column(name = "ACTUAL_TIMESTAMP")
	private Date actualTimestamp;
	
	public QiDefectDevice() {
		super();
	}
	
	public Object getId() {
		return getDefectResultId();
	}

	public long getDefectResultId() {
		return defectResultId;
	}

	public void setDefectResultId(long defectResultId) {
		this.defectResultId = defectResultId;
	}
	
	public String getDeviceId() {
		return StringUtils.trim(deviceId);
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Date getActualTimestamp() {
		return actualTimestamp;
	}

	public void setActualTimestamp(Date actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	public String toString() {
		return toString(getId(),getDeviceId(),getActualTimestamp());
	}

}
