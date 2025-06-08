package com.honda.galc.dto.qi;

import org.apache.commons.lang.StringUtils;
import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;

/**
 * 
 * <h3>QiRepairAreaSapceAssignmentDto Class description</h3>
 * <p>
 * QiRepairAreaSapceAssignmentDto contains the getter and setter of the
 * properties and maps this class with database table and properties with the
 * database its columns .
 * </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * 
 * </TABLE>
 * 
 * @author LnTInfotech<br>
 */

public class QiRepairAreaSapceAssignmentDto implements IDto {

	private static final long serialVersionUID = 1L;
	
	@DtoTag(outputName = "REPAIR_AREA_NAME")
	private String repairAreaName;

	@DtoTag(outputName = "REPAIR_AREA_ROW")
	private Integer repairAreaRow;

	@DtoTag(outputName = "REPAIR_AREA_SPACE")
	private Integer repairAreaSpace;
	
	@DtoTag(outputName = "PRODUCT_ID")
	private String productId;
	
	@DtoTag(outputName = "PRODUCT_TYPE")
	private String productType;
	
	@DtoTag(outputName = "DEFECT_TYPE_NAME")
	private String defectTypeName;
	
	@DtoTag(outputName = "RESPONSIBLE_PLANT")
	private String responsiblePlant;

	public String getRepairAreaName() {
		return StringUtils.trimToEmpty(repairAreaName);
	}

	public void setRepairAreaName(String repairAreaName) {
		this.repairAreaName = repairAreaName;
	}

	public String getProductId() {
		return StringUtils.trimToEmpty(productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductType() {
		return StringUtils.trimToEmpty(productType);
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getDefectTypeName() {
		return StringUtils.trimToEmpty(defectTypeName);
	}

	public void setDefectTypeName(String defectTypeName) {
		this.defectTypeName = defectTypeName;
	}

	public String getResponsiblePlant() {
		return StringUtils.trimToEmpty(responsiblePlant);
	}

	public void setResponsiblePlant(String responsiblePlant) {
		this.responsiblePlant = responsiblePlant;
	}

	public Integer getRepairAreaRow() {
		return repairAreaRow;
	}

	public void setRepairAreaRow(Integer repairAreaRow) {
		this.repairAreaRow = repairAreaRow;
	}

	public Integer getRepairAreaSpace() {
		return repairAreaSpace;
	}

	public void setRepairAreaSpace(Integer repairAreaSpace) {
		this.repairAreaSpace = repairAreaSpace;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((defectTypeName == null) ? 0 : defectTypeName.hashCode());
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
		result = prime * result
				+ ((productType == null) ? 0 : productType.hashCode());
		result = prime * result
				+ ((repairAreaName == null) ? 0 : repairAreaName.hashCode());
		result = prime * result
				+ ((repairAreaRow == null) ? 0 : repairAreaRow.hashCode());
		result = prime * result
				+ ((repairAreaSpace == null) ? 0 : repairAreaSpace.hashCode());
		result = prime
				* result
				+ ((responsiblePlant == null) ? 0 : responsiblePlant.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QiRepairAreaSapceAssignmentDto other = (QiRepairAreaSapceAssignmentDto) obj;
		if (defectTypeName == null) {
			if (other.defectTypeName != null)
				return false;
		} else if (!defectTypeName.equals(other.defectTypeName))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (productType == null) {
			if (other.productType != null)
				return false;
		} else if (!productType.equals(other.productType))
			return false;
		if (repairAreaName == null) {
			if (other.repairAreaName != null)
				return false;
		} else if (!repairAreaName.equals(other.repairAreaName))
			return false;
		if (repairAreaRow == null) {
			if (other.repairAreaRow != null)
				return false;
		} else if (!repairAreaRow.equals(other.repairAreaRow))
			return false;
		if (repairAreaSpace == null) {
			if (other.repairAreaSpace != null)
				return false;
		} else if (!repairAreaSpace.equals(other.repairAreaSpace))
			return false;
		if (responsiblePlant == null) {
			if (other.responsiblePlant != null)
				return false;
		} else if (!responsiblePlant.equals(other.responsiblePlant))
			return false;
		return true;
	}

}
