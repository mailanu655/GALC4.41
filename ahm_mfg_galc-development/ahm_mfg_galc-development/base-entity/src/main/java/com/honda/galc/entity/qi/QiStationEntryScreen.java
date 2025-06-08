package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;
/**
 * 
 * <h3>QiStationEntryScreen Class description</h3>
 * <p>
 * QiStationEntryScreen contains the getter and setter of the StationEntryScreen properties and maps this class with database table and properties with the database its columns .
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
 * 
 */
/**   
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
@Entity
@Table(name = "QI_STATION_ENTRY_SCREEN_TBX")
public class QiStationEntryScreen extends CreateUserAuditEntry {
	
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private QiStationEntryScreenId id;
	@Column(name = "ENTRY_SCREEN")
	@Auditable
	private String entryScreen;
	
	@Column(name = "ORIENTATION_ANGLE")
	@Auditable
	private short orientationAngle;

	@Column(name = "ALLOW_SCAN")
	@Auditable
	private short allowScan;
	
	public void setId(QiStationEntryScreenId id) {
		this.id = id;
	}

	public QiStationEntryScreenId getId() {
		return this.id;
	}
	public String getEntryScreen() {
		return StringUtils.trimToEmpty(entryScreen);
	}
	public void setEntryScreen(String entryScreen) {
		this.entryScreen = entryScreen;
	}

	public short getOrientationAngle() {
		return orientationAngle;
	}

	public void setOrientationAngle(short orientationAngle) {
		this.orientationAngle = orientationAngle;
	}
	
	public short getAllowScan() {
		return allowScan;
	}

	public void setAllowScan(short allowScan) {
		this.allowScan = allowScan;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entryScreen == null) ? 0 : entryScreen.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + orientationAngle;
		result = prime * result + allowScan;
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
		QiStationEntryScreen other = (QiStationEntryScreen) obj;
		if (entryScreen == null) {
			if (other.entryScreen != null)
				return false;
		} else if (!entryScreen.equals(other.entryScreen))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (orientationAngle != other.orientationAngle)
			return false;
		if (allowScan != other.allowScan)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return toString(getId().getProcessPointId(), getId().getDivisionId(), getId().getEntryModel(), getId().getSeq(), getEntryScreen(), getOrientationAngle(), getAllowScan());
	}
	
}
