package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;
/**
 * 
 * <h3>QIPartDefectCombination Class description</h3>
 * <p> QIPartDefectCombination description </p>
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
 * @author L&T Infotech<br>
 * Aug 26, 2016
 *
 *
 */

@Entity
@Table(name = "QI_REGIONAL_DEFECT_COMBINATION_TBX")
public class QiPartDefectCombination extends CreateUserAuditEntry {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "REGIONAL_DEFECT_COMBINATION_ID")	
	private Integer regionalDefectCombinationId;
	
	@Auditable
	@Column(name = "PART_LOCATION_ID")
	private Integer partLocationId;
	
	@Auditable
	@Column(name = "DEFECT_TYPE_NAME")
	private String defectTypeName;
	
	@Auditable
	@Column(name = "DEFECT_TYPE_NAME2")
	private String defectTypeName2;
	
	@Auditable
	@Column(name = "REPORTABLE")
	private short reportable;
	
	
	@Column(name = "PRODUCT_KIND")
	private String productKind;
	
	@Auditable
	@Column(name = "ACTIVE")
	private short active;

	@Auditable
	@Column(name = "IQS_ID")
	private Integer iqsId;
	
	@Auditable
	@Column(name = "THEME_NAME")
	private String themeName;
	
	public QiPartDefectCombination() {
		super();
	}
	
	public Integer getRegionalDefectCombinationId() {
		return regionalDefectCombinationId;
	}

	public void setRegionalDefectCombinationId(Integer regionalDefectCombinationId) {
		this.regionalDefectCombinationId = regionalDefectCombinationId;
	}

	public Integer getPartLocationId() {
		return partLocationId;
	}

	public void setPartLocationId(Integer partLocationId) {
		this.partLocationId = partLocationId;
	}

	public String getDefectTypeName() {
		return StringUtils.trimToEmpty(this.defectTypeName);
	}

	public void setDefectTypeName(String defectTypeName) {
		this.defectTypeName = defectTypeName;
	}

	public String getDefectTypeName2() {
		return StringUtils.trimToEmpty(this.defectTypeName2);
	}

	public void setDefectTypeName2(String defectTypeName2) {
		this.defectTypeName2 = defectTypeName2;
	}

	public short getReportable() {
		return reportable;
	}

	public void setReportable(short reportable) {
		this.reportable = reportable;
	}

	public String getProductKind() {
		return StringUtils.trimToEmpty(productKind);
	}

	public void setProductKind(String productKind) {
		this.productKind = productKind;
	}

	public short getActive() {
		return active;
	}

	public void setActive(short active) {
		this.active = active;
	}
	
	public void setActive(boolean isActive) {
		this.active = isActive ? (short) 1 : (short) 0;
	}
	
	public boolean isActive() {
		return (this.active) == (short)1 ? true : false;
	}
	
	public Integer getIqsId() {
		return iqsId;
	}

	public void setIqsId(Integer iqsId) {
		this.iqsId = iqsId;
	}

	public String getThemeName() {
		return StringUtils.trimToEmpty(themeName);
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime * result
				+ ((defectTypeName == null) ? 0 : defectTypeName.hashCode());
		result = prime * result
				+ ((defectTypeName2 == null) ? 0 : defectTypeName2.hashCode());
		result = prime * result + ((iqsId == null) ? 0 : iqsId.hashCode());
		result = prime * result
				+ ((partLocationId == null) ? 0 : partLocationId.hashCode());
		result = prime * result
				+ ((productKind == null) ? 0 : productKind.hashCode());
		result = prime
				* result
				+ ((regionalDefectCombinationId == null) ? 0
						: regionalDefectCombinationId.hashCode());
		result = prime * result + reportable;
		result = prime * result
				+ ((themeName == null) ? 0 : themeName.hashCode());
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
		QiPartDefectCombination other = (QiPartDefectCombination) obj;
		if (active != other.active)
			return false;
		if (defectTypeName == null) {
			if (other.defectTypeName != null)
				return false;
		} else if (!defectTypeName.equals(other.defectTypeName))
			return false;
		if (defectTypeName2 == null) {
			if (other.defectTypeName2 != null)
				return false;
		} else if (!defectTypeName2.equals(other.defectTypeName2))
			return false;
		if (iqsId == null) {
			if (other.iqsId != null)
				return false;
		} else if (!iqsId.equals(other.iqsId))
			return false;
		if (partLocationId == null) {
			if (other.partLocationId != null)
				return false;
		} else if (!partLocationId.equals(other.partLocationId))
			return false;
		if (productKind == null) {
			if (other.productKind != null)
				return false;
		} else if (!productKind.equals(other.productKind))
			return false;
		if (regionalDefectCombinationId == null) {
			if (other.regionalDefectCombinationId != null)
				return false;
		} else if (!regionalDefectCombinationId
				.equals(other.regionalDefectCombinationId))
			return false;
		if (reportable != other.reportable)
			return false;
		if (themeName == null) {
			if (other.themeName != null)
				return false;
		} else if (!themeName.equals(other.themeName))
			return false;
		return true;
	}

	public Object getId() {
		return regionalDefectCombinationId;
	}
}
