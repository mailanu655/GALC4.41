package com.honda.galc.oif.dto;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.qi.QiBomPart;
import com.honda.galc.entity.qi.QiBomPartId;
import com.honda.galc.util.GPCSData;

/**
 * 
 * <h3>BomPartDTO.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> BomPartDTO.java description </p>
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
 * <TD>Justin Jiang</TD>
 * <TD>March 16, 2017</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 * </TABLE>
 */
public class QiBomPartDto {
	
	@GPCSData("PLANT_LOC_CODE")
	private String plantLocCode;

	@GPCSData("MTC_MODEL")
	private String mtcModel;

	@GPCSData("DC_PART_NO")
	private String dcPartNo;

	@GPCSData("TGT_DC_PART_NAME")
	private String tgtDcPartName;

	public QiBomPartDto() {
	}

	public QiBomPart deriveQiBomPart(){
		QiBomPart qiBomPart = new QiBomPart();
		qiBomPart.setId(deriveID());
		qiBomPart.setMainPartNo(dcPartNo.substring(0, 5));
		qiBomPart.setDcPartName(StringUtils.trimToEmpty(tgtDcPartName));
		return qiBomPart;
	}
	
	private QiBomPartId deriveID(){
		QiBomPartId qiBomPartId = new QiBomPartId();
		qiBomPartId.setDcPartNo(dcPartNo);
		qiBomPartId.setModelCode(mtcModel);
		return qiBomPartId;
	}

	public String getPlantLocCode() {
		return plantLocCode;
	}

	public void setPlantLocCode(String plantLocCode) {
		this.plantLocCode = plantLocCode;
	}

	public String getMtcModel() {
		return mtcModel;
	}

	public void setMtcModel(String mtcModel) {
		this.mtcModel = mtcModel;
	}

	public String getDcPartNo() {
		return dcPartNo;
	}

	public void setDcPartNo(String dcPartNo) {
		this.dcPartNo = dcPartNo;
	}

	public String getTgtDcPartName() {
		return StringUtils.trimToEmpty(tgtDcPartName);
	}

	public void setTgtDcPartName(String tgtDcPartName) {
		this.tgtDcPartName = tgtDcPartName;
	}	
}
