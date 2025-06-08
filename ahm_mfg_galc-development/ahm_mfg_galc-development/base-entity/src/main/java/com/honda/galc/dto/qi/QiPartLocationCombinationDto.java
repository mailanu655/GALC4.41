package com.honda.galc.dto.qi;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;

/**
 * 
 * <h3>QiPartLocationCombinationDto Class description</h3>
 * <p>
 * QiPartLocationCombinationDto description
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
 * @author Justin Jiang<br>
 *         March 9, 2020
 *
 */

public class QiPartLocationCombinationDto implements IDto {

	private static final long serialVersionUID = 1L;

	@DtoTag(outputName = "PART_LOCATION_ID")
	private Integer partLocationId;
	@DtoTag(outputName = "FULL_PART_DESC")
	private String fullPartDesc;
	@DtoTag(outputName = "INSPECTION_PART_NAME")
	private String inspectionPartName;

	public QiPartLocationCombinationDto() {
		super();
	}

	public Integer getPartLocationId() {
		return partLocationId;
	}

	public void setPartLocationId(Integer partLocationId) {
		this.partLocationId = partLocationId;
	}

	public String getInspectionPartName() {
		return StringUtils.trimToEmpty(inspectionPartName);
	}

	public void setInspectionPartName(String inspectionPartName) {
		this.inspectionPartName = inspectionPartName;
	}

	public String getFullPartDesc() {
		return StringUtils.trimToEmpty(fullPartDesc);
	}

	public void setFullPartDesc(String fullPartDesc) {
		this.fullPartDesc = fullPartDesc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fullPartDesc == null) ? 0 : fullPartDesc.hashCode());
		result = prime * result + ((inspectionPartName == null) ? 0 : inspectionPartName.hashCode());
		result = prime * result + ((partLocationId == null) ? 0 : partLocationId.hashCode());
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
		QiPartLocationCombinationDto other = (QiPartLocationCombinationDto) obj;
		if (fullPartDesc == null) {
			if (other.fullPartDesc != null)
				return false;
		} else if (!fullPartDesc.equals(other.fullPartDesc))
			return false;
		if (inspectionPartName == null) {
			if (other.inspectionPartName != null)
				return false;
		} else if (!inspectionPartName.equals(other.inspectionPartName))
			return false;
		if (partLocationId == null) {
			if (other.partLocationId != null)
				return false;
		} else if (!partLocationId.equals(other.partLocationId))
			return false;
		return true;
	}
}
