package com.honda.galc.dto.qi;

import org.apache.commons.lang.StringUtils;
import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;
import com.honda.galc.entity.enumtype.QiActiveStatus;

public class QiImageSectionDto implements IDto{
	
	private static final long serialVersionUID = 1L;
	
	@DtoTag(outputName ="SEQUENCE_NO")
	private Integer sequenceNo;
	@DtoTag(outputName ="PART_LOCATION_ID")
	private Integer partLocationId;
	@DtoTag(outputName ="IMAGE_SECTION_ID")
	private Integer imageSectionId;
	@DtoTag(outputName ="INSPECTION_PART_NAME")
	private String inspectionPartName;
	@DtoTag(outputName ="INSPECTION_PART_LOCATION_NAME")
	private String inspectionPartLocationName;
	@DtoTag(outputName ="INSPECTION_PART_LOCATION2_NAME")
	private String inspectionPartLocation2Name;
	@DtoTag(outputName ="INSPECTION_PART2_NAME")
	private String inspectionPart2Name;
	@DtoTag(outputName ="INSPECTION_PART2_LOCATION_NAME")
	private String inspectionPart2LocationName;
	@DtoTag(outputName ="INSPECTION_PART2_LOCATION2_NAME")
	private String inspectionPart2Location2Name;
	@DtoTag(outputName ="INSPECTION_PART3_NAME")
	private String inspectionPart3Name;
	@DtoTag(outputName ="IMAGE_NAME")
	private String imageName;
	@DtoTag(outputName ="ACTIVE")
	private short active;
	
	public String getImageName() {
		return StringUtils.trimToEmpty(imageName);
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public short getActive() {
		return active;
	}
	public void setActive(short active) {
		this.active = active;
	}
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public Integer getPartLocationId() {
		return partLocationId;
	}
	public Integer getImageSectionId() {
		return imageSectionId;
	}
	public String getInspectionPartName() {
		return inspectionPartName;
	}
	public String getInspectionPartLocationName() {
		return inspectionPartLocationName;
	}
	public String getInspectionPartLocation2Name() {
		return inspectionPartLocation2Name;
	}
	public String getInspectionPart2Name() {
		return inspectionPart2Name;
	}
	public String getInspectionPart2LocationName() {
		return inspectionPart2LocationName;
	}
	public String getInspectionPart2Location2Name() {
		return inspectionPart2Location2Name;
	}
	public String getInspectionPart3Name() {
		return inspectionPart3Name;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public void setPartLocationId(Integer partLocationId) {
		this.partLocationId = partLocationId;
	}
	public void setImageSectionId(Integer imageSectionId) {
		this.imageSectionId = imageSectionId;
	}
	public void setInspectionPartName(String inspectionPartName) {
		this.inspectionPartName = inspectionPartName;
	}
	public void setInspectionPartLocationName(String inspectionPartLocationName) {
		this.inspectionPartLocationName = inspectionPartLocationName;
	}
	public void setInspectionPartLocation2Name(String inspectionPartLocation2Name) {
		this.inspectionPartLocation2Name = inspectionPartLocation2Name;
	}
	public void setInspectionPart2Name(String inspectionPart2Name) {
		this.inspectionPart2Name = inspectionPart2Name;
	}
	public void setInspectionPart2LocationName(String inspectionPart2LocationName) {
		this.inspectionPart2LocationName = inspectionPart2LocationName;
	}
	public void setInspectionPart2Location2Name(String inspectionPart2Location2Name) {
		this.inspectionPart2Location2Name = inspectionPart2Location2Name;
	}
	public void setInspectionPart3Name(String inspectionPart3Name) {
		this.inspectionPart3Name = inspectionPart3Name;
	}
	
	public String getStatus() {
		return StringUtils.trimToEmpty(QiActiveStatus.getType(active).getName());
	}
	
	public String getFullPartDesc() {
		return StringUtils.trimToEmpty(inspectionPartName+" " +
				   inspectionPartLocationName+" " +
				   inspectionPartLocation2Name+" " +
				   inspectionPart2Name+" " +
				   inspectionPart2LocationName+" " +
				   inspectionPart2Location2Name+" " +
				   inspectionPart3Name).replaceAll("null", "").replaceAll("\\s+"," ");
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result	+ ((imageSectionId == null) ? 0 : imageSectionId.hashCode());
		result = prime * result	+ ((inspectionPart2Location2Name == null) ? 0 : inspectionPart2Location2Name.hashCode());
		result = prime * result	+ ((inspectionPart2LocationName == null) ? 0 : inspectionPart2LocationName.hashCode());
		result = prime * result	+ ((inspectionPart2Name == null) ? 0 : inspectionPart2Name.hashCode());
		result = prime * result	+ ((inspectionPart3Name == null) ? 0 : inspectionPart3Name.hashCode());
		result = prime * result	+ ((inspectionPartLocation2Name == null) ? 0 : inspectionPartLocation2Name.hashCode());
		result = prime * result	+ ((inspectionPartLocationName == null) ? 0 : inspectionPartLocationName.hashCode());
		result = prime * result	+ ((inspectionPartName == null) ? 0 : inspectionPartName.hashCode());
		result = prime * result	+ ((partLocationId == null) ? 0 : partLocationId.hashCode());
		result = prime * result	+ ((sequenceNo == null) ? 0 : sequenceNo.hashCode());
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
		QiImageSectionDto other = (QiImageSectionDto) obj;
		if (imageSectionId == null) {
			if (other.imageSectionId != null)
				return false;
		} else if (!imageSectionId.equals(other.imageSectionId))
			return false;
		if (inspectionPart2Location2Name == null) {
			if (other.inspectionPart2Location2Name != null)
				return false;
		} else if (!inspectionPart2Location2Name.equals(other.inspectionPart2Location2Name))
			return false;
		if (inspectionPart2LocationName == null) {
			if (other.inspectionPart2LocationName != null)
				return false;
		} else if (!inspectionPart2LocationName.equals(other.inspectionPart2LocationName))
			return false;
		if (inspectionPart2Name == null) {
			if (other.inspectionPart2Name != null)
				return false;
		} else if (!inspectionPart2Name.equals(other.inspectionPart2Name))
			return false;
		if (inspectionPart3Name == null) {
			if (other.inspectionPart3Name != null)
				return false;
		} else if (!inspectionPart3Name.equals(other.inspectionPart3Name))
			return false;
		if (inspectionPartLocation2Name == null) {
			if (other.inspectionPartLocation2Name != null)
				return false;
		} else if (!inspectionPartLocation2Name.equals(other.inspectionPartLocation2Name))
			return false;
		if (inspectionPartLocationName == null) {
			if (other.inspectionPartLocationName != null)
				return false;
		} else if (!inspectionPartLocationName.equals(other.inspectionPartLocationName))
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
		if (sequenceNo == null) {
			if (other.sequenceNo != null)
				return false;
		} else if (!sequenceNo.equals(other.sequenceNo))
			return false;
		if (active != other.active)
			return false;
		if (imageName == null) {
			if (other.imageName != null)
				return false;
		} else if (!imageName.equals(other.imageName))
			return false;
		return true;
	}
}
