package com.honda.galc.dto.qi;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;
import com.honda.galc.entity.enumtype.QiActiveStatus;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QiPartDefectCombinationDto</code> is the Dto class for Part Defect Combination.
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>L&T Infotech</TD>
 * <TD>14/07/2016</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
public class QiPartDefectCombinationDto implements IDto{
	
	private static final long serialVersionUID = 1L;
	@DtoTag(outputName ="REGIONAL_DEFECT_COMBINATION_ID")
	private Integer regionalDefectCombinationId; 
	@DtoTag(outputName ="FULL_PART_DESC")
	private String fullPartDesc;
	@DtoTag(outputName ="DEFECT_TYPE_NAME")
	private String primaryDefect;
	@DtoTag(outputName ="DEFECT_TYPE_NAME2")
	private String secondaryDefect;
	@DtoTag(outputName ="ACTIVE")
	private short active;

	public QiPartDefectCombinationDto() {
		super();
	}
	
	public Integer getRegionalDefectCombinationId() {
		return regionalDefectCombinationId;
	}

	public void setRegionalDefectCombinationId(Integer regionalDefectCombinationId) {
		this.regionalDefectCombinationId = regionalDefectCombinationId;
	}

	public String getFullPartDesc() {
		return StringUtils.trimToEmpty(fullPartDesc);
	}
	public void setFullPartDesc(String fullPartDesc) {
		this.fullPartDesc = fullPartDesc;
	}
	public String getPrimaryDefect() {
		return StringUtils.trimToEmpty(primaryDefect);
	}
	public void setPrimaryDefect(String primaryDefect) {
		this.primaryDefect = primaryDefect;
	}
	public String getSecondaryDefect() {
		return StringUtils.trimToEmpty(secondaryDefect);
	}
	public void setSecondaryDefect(String secondaryDefect) {
		this.secondaryDefect = secondaryDefect;
	}
	public short getActive() {
		return active;
	}
	public void setActive(short active) {
		this.active = active;
	}
	public boolean isActive() {
		return active == 1;
	}
	public String getStatus() {
		return StringUtils.trimToEmpty(QiActiveStatus.getType(active).getName());
	}
	
	public void setDtoList(List<QiPartDefectCombinationDto> qiDtoList, List<Object[]> list) {
		QiPartDefectCombinationDto dto;
		if(list!=null && !list.isEmpty()){
			for(Object[] obj : list){
				dto = new QiPartDefectCombinationDto();
				dto.setRegionalDefectCombinationId(Integer.parseInt(obj[0].toString()));
				dto.setFullPartDesc(obj[1].toString());
				dto.setPrimaryDefect(obj[2].toString());
				dto.setSecondaryDefect(obj[3]== null ? StringUtils.EMPTY :obj[3].toString());
				dto.setActive(Short.parseShort(obj[4].toString()));
				qiDtoList.add(dto);
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime * result
				+ ((fullPartDesc == null) ? 0 : fullPartDesc.hashCode());
		result = prime * result
				+ ((primaryDefect == null) ? 0 : primaryDefect.hashCode());
		result = prime
				* result
				+ ((regionalDefectCombinationId == null) ? 0
						: regionalDefectCombinationId.hashCode());
		result = prime * result
				+ ((secondaryDefect == null) ? 0 : secondaryDefect.hashCode());
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
		QiPartDefectCombinationDto other = (QiPartDefectCombinationDto) obj;
		if (active != other.active)
			return false;
		if (fullPartDesc == null) {
			if (other.fullPartDesc != null)
				return false;
		} else if (!fullPartDesc.equals(other.fullPartDesc))
			return false;
		if (primaryDefect == null) {
			if (other.primaryDefect != null)
				return false;
		} else if (!primaryDefect.equals(other.primaryDefect))
			return false;
		if (regionalDefectCombinationId == null) {
			if (other.regionalDefectCombinationId != null)
				return false;
		} else if (!regionalDefectCombinationId
				.equals(other.regionalDefectCombinationId))
			return false;
		if (secondaryDefect == null) {
			if (other.secondaryDefect != null)
				return false;
		} else if (!secondaryDefect.equals(other.secondaryDefect))
			return false;
		return true;
	}
}
