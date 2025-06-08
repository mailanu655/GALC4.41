package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>QiBomPart Class description</h3>
 * <p>
 * QiBomPart contains the getter and setter of the Bom Part properties and maps this class with database table and properties with the database its columns .
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
 *        MAY 06, 2016
 * 
 */
@Entity
@Table(name = "QI_BOM_PART_TBX")
public class QiBomPart extends AuditEntry  {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "MAIN_PART_NO", nullable=false, length=5)
	private String mainPartNo;
	@Column(name = "DC_PART_NAME", nullable=false, length=30)
	private String dcPartName;
	@EmbeddedId
	private QiBomPartId id;
	
	public QiBomPart() {
		super();
	}
	
	public QiBomPartId getId() {
		return this.id;
	}

	public void setId(QiBomPartId id) {
		this.id = id;
	}

	public String getMainPartNo() {
		return StringUtils.trimToEmpty(this.mainPartNo);
	}

	public void setMainPartNo(String mainPartNo) {
		this.mainPartNo = mainPartNo;
	}

	public String getDcPartName() {
		return StringUtils.trimToEmpty(this.dcPartName);
	}
	
	public void setDcPartName(String dcPartName) {
		this.dcPartName = dcPartName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((id == null) ? 0
						: id.hashCode());
		result = prime
				* result
				+ ((mainPartNo == null) ? 0
						: mainPartNo.hashCode());
		result = prime
				* result
				+ ((dcPartName == null) ? 0
						: dcPartName.hashCode());
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
		QiBomPart other = (QiBomPart) obj;
		if (mainPartNo == null) {
			if (other.mainPartNo != null)
				return false;
		} else if (!mainPartNo.equals(other.mainPartNo))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (dcPartName == null) {
			if (other.dcPartName != null)
				return false;
		} else if (!dcPartName.equals(other.dcPartName))
			return false;
		return true;
	}

}
