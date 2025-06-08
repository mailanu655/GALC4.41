package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;
import com.honda.galc.util.ToStringUtil;

/**
 * 
 * <h3>QiBomQicsPartMapping Class description</h3>
 * <p>
 * QiBomQicsPartMapping contains the getter and setter of the Associated Bom with Parts properties and maps this class with database table and properties with the database its columns .
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
@Table(name = "QI_BOM_QICS_PART_MAPPING_TBX")
public class QiBomQicsPartMapping extends CreateUserAuditEntry {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "MAIN_PART_NO", nullable=false, length=5)
	@Auditable(isPartOfPrimaryKey=true,sequence=1)
	private String mainPartNo;
	
	@Column(name = "INSPECTION_PART_NAME", nullable=false, length=32)
	@Auditable(isPartOfPrimaryKey=true,sequence=2)
	private String inspectionPartName;
	
	public QiBomQicsPartMapping() {
		super();
	}
	
	public QiBomQicsPartMapping(String mainPartNo,String inspectionPartName) {
	    new QiBomQicsPartMapping(mainPartNo, inspectionPartName);
	}

	public String getMainPartNo() {
		return StringUtils.trimToEmpty(this.mainPartNo);
	}

	public void setMainPartNo(String mainPartNo) {
		this.mainPartNo = mainPartNo;
	}

	public String getInspectionPartName() {
		return StringUtils.trimToEmpty(this.inspectionPartName);
	}

	public void setInspectionPartName(String inspectionPartName) {
		this.inspectionPartName = inspectionPartName;
	}


	public Object getId() {
		return StringUtils.trimToEmpty(getMainPartNo());
	}
	
	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((inspectionPartName == null) ? 0 : inspectionPartName.hashCode());
		result = prime * result + ((mainPartNo == null) ? 0 : mainPartNo.hashCode());
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
		QiBomQicsPartMapping other = (QiBomQicsPartMapping) obj;
		if (inspectionPartName == null) {
			if (other.inspectionPartName != null)
				return false;
		} else if (!inspectionPartName.equals(other.inspectionPartName))
			return false;
		if (mainPartNo == null) {
			if (other.mainPartNo != null)
				return false;
		} else if (!mainPartNo.equals(other.mainPartNo))
			return false;
		return true;
	}

}
