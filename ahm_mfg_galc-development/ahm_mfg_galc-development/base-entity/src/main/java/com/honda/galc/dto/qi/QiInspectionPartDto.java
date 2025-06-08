package com.honda.galc.dto.qi;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;
/**
 * 
 * <h3>QiDefectResultDto Class description</h3>
 * <p>
 * QiDefectResultDto
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
 *        Oct 03, 2016
 * 
 */
public class QiInspectionPartDto implements IDto,Comparator<QiInspectionPartDto> {
	
	private static final long serialVersionUID = 1L;
	
	@DtoTag(outputName = "INSPECTION_PART_NAME")
	private String inspectionPartName;
	
	@DtoTag(outputName ="MAIN_PART_NO")
	private String mainPartNo;
	
	

	public String getInspectionPartName() {
		return StringUtils.trimToEmpty(inspectionPartName);
	}



	public void setInspectionPartName(String inspectionPartName) {
		this.inspectionPartName = inspectionPartName;
	}



	public String getMainPartNo() {
		return StringUtils.trimToEmpty(mainPartNo);
	}



	public void setMainPartNo(String mainPartNo) {
		this.mainPartNo = mainPartNo;
	}



	public int compare(QiInspectionPartDto object1, QiInspectionPartDto object2) {
		return object1.getInspectionPartName().compareTo(object2.getInspectionPartName());
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((inspectionPartName == null) ? 0 : inspectionPartName
						.hashCode());
		result = prime * result
				+ ((mainPartNo == null) ? 0 : mainPartNo.hashCode());
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
		QiInspectionPartDto other = (QiInspectionPartDto) obj;
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
