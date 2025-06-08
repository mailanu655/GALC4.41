
package com.honda.galc.dto.qi;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.honda.galc.dto.DefectMapDto;
import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;
import com.honda.galc.entity.enumtype.QiExternalSystem;
import com.honda.galc.entity.qi.QiExternalSystemData;
import com.honda.galc.entity.qi.QiExternalSystemDataId;

/**
 * 
 * <h3>QiCreateDefectDto Class description</h3>
 * <p>
 * QiCreateDefectDto
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
 * Feb 13, 2017
 * 
 */

public class QiCreateDefectDto implements IDto {

private static final long serialVersionUID = 1L;
	
	@DtoTag(outputName = "ENTRY_SITE")
	private String entrySite;
	
	@DtoTag(outputName = "ENTRY_DEPARTMENT")
	private String entryDepartment;
	
	@DtoTag(name = "PLANT")
	private String plantCode;
	
	@DtoTag(outputName = "PROCESS_POINT_ID")
	private String processPointId;
	
	@DtoTag(name = "PRODUCT_TYPE")
	private String productType;
	
	@DtoTag(outputName = "EXTERNAL_PART_CODE")
	private String externalPartCode;
	
	@DtoTag(outputName = "EXTERNAL_DEFECT_CODE")
	private String externalDefectCode;
	
	@DtoTag(outputName = "PRODUCT_ID")
	private String productId;
	
	@DtoTag(outputName = "ORIGINAL_DEFECT_STATUS")
	private Short originalDefectStatus;
	
	@DtoTag(outputName = "CURRENT_DEFECT_STATUS")
	private Short currentDefectStatus;
	
	@DtoTag(outputName = "WRITE_UP_DEPARTMENT")
	private String writeupDepartment;
	
	@DtoTag(outputName = "ASSOCIATE_ID")
	private String associateId;
	
	@DtoTag(outputName = "IMAGE_NAME")
	private String imageName;
	
	@DtoTag(outputName = "POINT_X")
	private Integer xAxis;
	
	@DtoTag(outputName = "POINT_Y")
	private Integer yAxis;
	
	@DtoTag(outputName = "EXTERNAL_SYSTEM_NAME")
	private String externalSystemName;
	
	@DtoTag(outputName = "EXTERNAL_SYSTEM_KEY")
	private Long externalSystemKey;

	public QiCreateDefectDto() {
		super();
		this.entrySite = "";
		this.entryDepartment = "";
		this.plantCode = "";
		this.processPointId = "";
		this.productType = "";
		this.externalPartCode = "";
		this.externalDefectCode = "";
		this.productId = "";
		this.originalDefectStatus = 0;
		this.currentDefectStatus = 0;
		this.writeupDepartment = "";
		this.associateId = "";
		this.imageName = "";
		this.xAxis = 0;
		this.yAxis = 0;
		this.externalSystemName = "";
		this.externalSystemKey = 0L;
	}

	public String getEntrySite() {
		return entrySite;
	}

	public void setEntrySite(String entrySite) {
		this.entrySite = entrySite;
	}

	public String getEntryDepartment() {
		return entryDepartment;
	}

	public void setEntryDepartment(String entryDepartment) {
		this.entryDepartment = entryDepartment;
	}

	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public String getProcessPointId() {
		return processPointId;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getExternalPartCode() {
		return externalPartCode;
	}

	public void setExternalPartCode(String externalPartCode) {
		this.externalPartCode = externalPartCode;
	}

	public String getExternalDefectCode() {
		return externalDefectCode;
	}

	public void setExternalDefectCode(String externalDefectCode) {
		this.externalDefectCode = externalDefectCode;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Short getOriginalDefectStatus() {
		return originalDefectStatus;
	}

	public void setOriginalDefectStatus(Short originalDefectStatus) {
		this.originalDefectStatus = originalDefectStatus;
	}

	public Short getCurrentDefectStatus() {
		return currentDefectStatus;
	}

	public void setCurrentDefectStatus(Short currentDefectStatus) {
		this.currentDefectStatus = currentDefectStatus;
	}

	public String getWriteupDepartment() {
		return writeupDepartment;
	}

	public void setWriteupDepartment(String writeupDepartment) {
		this.writeupDepartment = writeupDepartment;
	}

	public String getAssociateId() {
		return associateId;
	}

	public void setAssociateId(String associateId) {
		this.associateId = associateId;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getExternalSystemName() {
		return externalSystemName;
	}

	public void setExternalSystemName(String externalSystemName) {
		this.externalSystemName = externalSystemName;
	}

	public Long getExternalSystemKey() {
		return externalSystemKey;
	}

	public void setExternalSystemKey(Long externalSystemKey) {
		this.externalSystemKey = externalSystemKey;
	}

	public Integer getxAxis() {
		return xAxis;
	}

	public void setxAxis(Integer xAxis) {
		this.xAxis = xAxis;
	}

	public Integer getyAxis() {
		return yAxis;
	}

	public void setyAxis(Integer yAxis) {
		this.yAxis = yAxis;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@JsonIgnore
	public QiExternalSystemData getExternalDataEntity()  {
		
		QiExternalSystemDataId id =
				new QiExternalSystemDataId
					(getExternalSystemName(),
					 getExternalPartCode(),
					 getExternalDefectCode(),
					 getProductId(),
					 getProcessPointId(),
					 new Timestamp(System.currentTimeMillis()));
				
		QiExternalSystemData qiExtSystemData = new QiExternalSystemData();
		qiExtSystemData.setId(id);
		qiExtSystemData.setEntrySite(getEntrySite());
		qiExtSystemData.setEntryDept(getEntryDepartment());
		qiExtSystemData.setProductType(getProductType());
		qiExtSystemData.setAssociateId(getAssociateId());
		qiExtSystemData.setWriteUpDept(getWriteupDepartment());
		qiExtSystemData.setImageName(getImageName());
		qiExtSystemData.setOriginalDefectStatus(getOriginalDefectStatus());
		qiExtSystemData.setCurrentDefectStatus(getCurrentDefectStatus());
		qiExtSystemData.setExternalSystemDefectKey(getExternalSystemKey());
		qiExtSystemData.setPointX(getxAxis());
		qiExtSystemData.setPointY(getyAxis());
		
		return qiExtSystemData;
	}
	
	@JsonIgnore
	public DefectMapDto getDefectMapDto()  {
		DefectMapDto defectMapDto = new DefectMapDto();
		defectMapDto.setAssociateId(getAssociateId());
		defectMapDto.setCurrentDefectStatus(String.valueOf(getCurrentDefectStatus()));
		defectMapDto.setEntryDepartment(getEntryDepartment());
		defectMapDto.setEntrySite(getEntrySite());
		defectMapDto.setExternalDefectCode(getExternalDefectCode());
		defectMapDto.setExternalPartCode(getExternalPartCode());
		defectMapDto.setExternalSystemName(getExternalSystemName());
		defectMapDto.setExternalSystemKey(getExternalSystemKey());
		defectMapDto.setImageName(getImageName());
		defectMapDto.setOriginalDefectStatus(String.valueOf(getOriginalDefectStatus()));
		defectMapDto.setEntryPlantName(getPlantCode());
		defectMapDto.setProcessPointId(getProcessPointId());
		defectMapDto.setProductId(getProductId());
		defectMapDto.setProductType(getProductType());
		defectMapDto.setWriteupDepartment(getWriteupDepartment());
		defectMapDto.setxAxis(String.valueOf(getxAxis()));
		defectMapDto.setyAxis(String.valueOf(getyAxis()));
		if(getExternalSystemName().equalsIgnoreCase(QiExternalSystem.LOT_CONTROL.name()))  {
			defectMapDto.setLotControl(true);
		}
		return defectMapDto;
	}
}
