package com.honda.galc.dto.qi;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
/**
 * 
 * <h3>QiBomPartDto Class description</h3>
 * <p>
 * QiBomPartDto is used to map the data in BomPart Table on screen by mapping with BomPart Table and Bompart mapping table
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
public class QiBomPartDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String mainPartNo;
	private String dieCastPartName;
	private String inspectionPart;
	private String dieCastPartNo;
	
	
	public String getMainPartNo() {
		return StringUtils.trimToEmpty(this.mainPartNo);
	}
	public void setMainPartNo(String mainPartNo) {
		this.mainPartNo = mainPartNo;
	}
	public String getDieCastPartName() {
		return StringUtils.trimToEmpty(this.dieCastPartName);
	}
	public void setDieCastPartName(String dcPartName) {
		this.dieCastPartName = dcPartName;
	}

	public String getInspectionPart() {
		return StringUtils.trimToEmpty(this.inspectionPart);
	}
	public void setInspectionPart(String inspectionPart) {
		this.inspectionPart = inspectionPart;
	}
	public String getDieCastPartNo() {
		return StringUtils.trimToEmpty(this.dieCastPartNo);
	}
	public void setDieCastPartNo(String dcPartNo) {
		this.dieCastPartNo = dcPartNo;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dieCastPartName == null) ? 0 : dieCastPartName.hashCode());
		result = prime * result + ((dieCastPartNo == null) ? 0 : dieCastPartNo.hashCode());
		result = prime * result + ((inspectionPart == null) ? 0 : inspectionPart.hashCode());
		result = prime * result + ((mainPartNo == null) ? 0 : mainPartNo.hashCode());
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
		QiBomPartDto other = (QiBomPartDto) obj;
		if (dieCastPartName == null) {
			if (other.dieCastPartName != null)
				return false;
		} else if (!dieCastPartName.equals(other.dieCastPartName))
			return false;
		if (dieCastPartNo == null) {
			if (other.dieCastPartNo != null)
				return false;
		} else if (!dieCastPartNo.equals(other.dieCastPartNo))
			return false;
		if (inspectionPart == null) {
			if (other.inspectionPart != null)
				return false;
		} else if (!inspectionPart.equals(other.inspectionPart))
			return false;
		if (mainPartNo == null) {
			if (other.mainPartNo != null)
				return false;
		} else if (!mainPartNo.equals(other.mainPartNo))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "QiBomPartDto [mainPartNo=" + mainPartNo + ", dcPartName=" + dieCastPartName + ", dcPartNo=" + dieCastPartNo +" , inspectionPart="
				+ inspectionPart+"]";
	}
}
 
