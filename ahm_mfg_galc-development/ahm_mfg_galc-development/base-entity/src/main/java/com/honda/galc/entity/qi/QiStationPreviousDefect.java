package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.util.ToStringUtil;
/**
 * 
 * <h3>QiStationPreviousDefect Class description</h3>
 * <p>
 * QiStationPreviousDefect contains the getter and setter of the Entry department defect
 *  properties and maps this class with database table and properties with the
 * database its columns .
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
 * @author LnTInfotech<br>
 *         10th April,2017
 * 
 */
@Entity
@Table(name = "QI_STATION_PREVIOUS_DEFECT_TBX")
public class QiStationPreviousDefect extends CreateUserAuditEntry {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QiStationPreviousDefect() {
	}
	
	@EmbeddedId
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private QiStationPreviousDefectId id;

	@Column(name = "ORIGINAL_DEFECT_STATUS")
	@Auditable
	private short originalDefectStatus;
	
	@Column(name = "CURRENT_DEFECT_STATUS")
	@Auditable
	private short currentDefectStatus;

	public QiStationPreviousDefectId getId() {
		return id;
	}

	public void setId(QiStationPreviousDefectId id) {
		this.id = id;
	}

	public short getOriginalDefectStatus() {
		return originalDefectStatus;
	}
	
	public void setOriginalDefectStatus(short originalDefectStatus) {
		this.originalDefectStatus = originalDefectStatus;
	}
	
	public String getOriginalDefectstatusName(){
		return DefectStatus.getType(this.originalDefectStatus).getName();
	}
	
	public short getCurrentDefectStatus() {
		return currentDefectStatus;
	}

	public void setCurrentDefectStatus(short currentDefectStatus) {
		this.currentDefectStatus = currentDefectStatus;
	}
	
	public String getCurrentDefectStatusName(){
		return DefectStatus.getType(this.currentDefectStatus).getName();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + originalDefectStatus;
		result = prime * result + currentDefectStatus;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		QiStationPreviousDefect other = (QiStationPreviousDefect) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (originalDefectStatus != other.originalDefectStatus)
			return false;
		if (currentDefectStatus != other.currentDefectStatus)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}
}
