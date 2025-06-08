package com.honda.galc.dto.qi;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.IDto;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QiRegionalAttributeDto</code> is the Dto class for Regional Attributes in PDC.
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
public class QiRegionalAttributeDto implements IDto {
	
	private static final long serialVersionUID = 1L;
	
	private Integer regionalDefectCombinationId;
	
	private short active;
	
	private Integer partLocationId;
	
	private String defectTypeName;
	
	private String defectTypeName2;
	
	private short reportable;
	
	private String themeName;
	
	private String iqsVersion;
	
	private String iqsCategory;
	
	private String iqsQuestion;
	
	private int iqsQuestionNo;
	
	private Integer iqsId;
	
	private String reportableDefect;
	
	private String fullPartName;
	
	private String createUser;
	
	private Date updateTimestamp;

	public void setFullPartName(String fullPartName) {
		this.fullPartName = fullPartName;
	}
	
	public Integer getRegionalDefectCombinationId() {
		return regionalDefectCombinationId;
	}

	public void setRegionalDefectCombinationId(Integer regionalDefectCombinationId) {
		this.regionalDefectCombinationId = regionalDefectCombinationId;
	}


	public String getDefectTypeName() {
		return StringUtils.trimToEmpty(this.defectTypeName);
	}

	public String getFullPartName() {
		return fullPartName;
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
		return reportable ;
	}

	public void setReportable(short reportable) {
		this.reportable = reportable;
	}

	public String getThemeName() {
		return StringUtils.trimToEmpty(this.themeName);
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	public String getIqsVersion() {
		return StringUtils.trimToEmpty(this.iqsVersion);
	}
	
	public void setIqsVersion(String iqsVersion) {
		this.iqsVersion = iqsVersion;
	}
	
	public String getIqsCategory() {
		return StringUtils.trimToEmpty(this.iqsCategory);
	}
	
	public void setIqsCategory(String iqsCategory) {
		this.iqsCategory = iqsCategory;
	}
	
	public String getIqsQuestion() {
		return StringUtils.trimToEmpty(this.iqsQuestion);
	}
	
	public void setIqsQuestion(String iqsQuestion) {
		this.iqsQuestion = iqsQuestion;
	}

	public int getIqsQuestionNo() {
		return iqsQuestionNo;
	}

	public void setIqsQuestionNo(int iqsQuestionNo) {
		this.iqsQuestionNo = iqsQuestionNo;
	}

	public String getReportableDefect() {
		return reportable == 0  ? "Yes" : "No";
	}

	public void setReportableDefect(String reportableDefect) {
		this.reportableDefect = reportableDefect;
	}
	public short getActive() {
		return active;
	}
	public void setActive(short active) {
		this.active = active;
	}
	public Integer getPartLocationId() {
		return partLocationId;
	}
	public void setPartLocationId(Integer partLocationId) {
		this.partLocationId = partLocationId;
	}
	public Integer getIqsId() {
		return iqsId;
	}
	public void setIqsId(Integer iqsId) {
		this.iqsId = iqsId;
	}
	
	public String getCreateUser() {
		return StringUtils.trimToEmpty(createUser);
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime * result + ((createUser == null) ? 0 : createUser.hashCode());
		result = prime * result + ((defectTypeName == null) ? 0 : defectTypeName.hashCode());
		result = prime * result + ((defectTypeName2 == null) ? 0 : defectTypeName2.hashCode());
		result = prime * result + ((fullPartName == null) ? 0 : fullPartName.hashCode());
		result = prime * result + ((iqsCategory == null) ? 0 : iqsCategory.hashCode());
		result = prime * result + ((iqsId == null) ? 0 : iqsId.hashCode());
		result = prime * result + ((iqsQuestion == null) ? 0 : iqsQuestion.hashCode());
		result = prime * result + iqsQuestionNo;
		result = prime * result + ((iqsVersion == null) ? 0 : iqsVersion.hashCode());
		result = prime * result + ((partLocationId == null) ? 0 : partLocationId.hashCode());
		result = prime * result + ((regionalDefectCombinationId == null) ? 0 : regionalDefectCombinationId.hashCode());
		result = prime * result + reportable;
		result = prime * result + ((reportableDefect == null) ? 0 : reportableDefect.hashCode());
		result = prime * result + ((themeName == null) ? 0 : themeName.hashCode());
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
		QiRegionalAttributeDto other = (QiRegionalAttributeDto) obj;
		if (active != other.active)
			return false;
		if (createUser == null) {
			if (other.createUser != null)
				return false;
		} else if (!createUser.equals(other.createUser))
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
		if (fullPartName == null) {
			if (other.fullPartName != null)
				return false;
		} else if (!fullPartName.equals(other.fullPartName))
			return false;
		if (iqsCategory == null) {
			if (other.iqsCategory != null)
				return false;
		} else if (!iqsCategory.equals(other.iqsCategory))
			return false;
		if (iqsId == null) {
			if (other.iqsId != null)
				return false;
		} else if (!iqsId.equals(other.iqsId))
			return false;
		if (iqsQuestion == null) {
			if (other.iqsQuestion != null)
				return false;
		} else if (!iqsQuestion.equals(other.iqsQuestion))
			return false;
		if (iqsQuestionNo != other.iqsQuestionNo)
			return false;
		if (iqsVersion == null) {
			if (other.iqsVersion != null)
				return false;
		} else if (!iqsVersion.equals(other.iqsVersion))
			return false;
		if (partLocationId == null) {
			if (other.partLocationId != null)
				return false;
		} else if (!partLocationId.equals(other.partLocationId))
			return false;
		if (regionalDefectCombinationId == null) {
			if (other.regionalDefectCombinationId != null)
				return false;
		} else if (!regionalDefectCombinationId.equals(other.regionalDefectCombinationId))
			return false;
		if (reportable != other.reportable)
			return false;
		if (reportableDefect == null) {
			if (other.reportableDefect != null)
				return false;
		} else if (!reportableDefect.equals(other.reportableDefect))
			return false;
		if (themeName == null) {
			if (other.themeName != null)
				return false;
		} else if (!themeName.equals(other.themeName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QiRegionalAttributeDto [regionalDefectCombinationId=" + regionalDefectCombinationId + ", active="
				+ active + ", partLocationId=" + partLocationId + ", defectTypeName=" + defectTypeName
				+ ", defectTypeName2=" + defectTypeName2 + ", reportable=" + reportable + ", themeName=" + themeName
				+ ", iqsVersion=" + iqsVersion + ", iqsCategory=" + iqsCategory + ", iqsQuestion=" + iqsQuestion
				+ ", iqsQuestionNo=" + iqsQuestionNo + ", iqsId=" + iqsId + ", reportableDefect=" + reportableDefect
				+ ", fullPartName=" + fullPartName + ", createUser=" + createUser + "]";
	}
	
	/**
	 * To set value in DTO
	 * @param qiDtoList
	 * @param list
	 */
	public void setRegionalAttributeDtoList(List<QiRegionalAttributeDto> qiDtoList, List<Object[]> list) {
		QiRegionalAttributeDto dto;
		if(list!=null && !list.isEmpty()){
			for(Object[] obj : list){
				dto = new QiRegionalAttributeDto();
				dto.setRegionalDefectCombinationId(Integer.parseInt(obj[0].toString()));
				dto.setFullPartName(obj[1].toString());
				dto.setDefectTypeName(obj[2].toString());
				dto.setDefectTypeName2(obj[3]== null ? StringUtils.EMPTY :obj[3].toString());
				dto.setReportable(obj[4]== null ? 0 :(short)Integer.parseInt(obj[4].toString()));
				dto.setActive((short)Integer.parseInt(obj[5].toString()));
				dto.setThemeName(obj[6]== null ? StringUtils.EMPTY :obj[6].toString());
				dto.setCreateUser(obj[7].toString());
				dto.setPartLocationId(Integer.parseInt(obj[8].toString()));
				dto.setIqsId(obj[9]== null ? 0 :Integer.parseInt(obj[9].toString()));
				dto.setIqsVersion(obj[10]== null ? StringUtils.EMPTY :obj[10].toString());
				dto.setIqsCategory(obj[11]== null ? StringUtils.EMPTY :obj[11].toString());
				dto.setIqsQuestion(obj[12]== null ? StringUtils.EMPTY :obj[12].toString());
				dto.setIqsQuestionNo(obj[13]== null ? 0 :(short)Integer.parseInt(obj[13].toString()));
				dto.setUpdateTimestamp(obj[14] == null ? null : (Date) obj[14]);
				qiDtoList.add(dto);
			}
		}
	}

	
}
