package com.honda.galc.entity.qi;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;

/**
 * 
 * <h3>QiStationUpcPart Class description</h3>
 * <p>
 * QiStationUpcPart contains the getter and setter of station upc parts
 * properties and maps this class with database table and properties with the
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
 *         Jan 05, 2016
 * 
 */
@Entity
@Table(name = "QI_STATION_UPC_PART_TBX")
public class QiStationUpcPart extends CreateUserAuditEntry {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	QiStationUpcPartId id;
	
	public QiStationUpcPart() {

	}

	public void setId(QiStationUpcPartId id) {
		this.id = id;
	}

	public QiStationUpcPartId getId() {
		return this.id;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		QiStationUpcPart other = (QiStationUpcPart) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
}
