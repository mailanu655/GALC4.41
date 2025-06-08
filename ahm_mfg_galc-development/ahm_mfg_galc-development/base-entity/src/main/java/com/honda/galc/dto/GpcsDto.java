package com.honda.galc.dto;

import java.util.List;

import com.honda.galc.entity.product.Measurement;

public class GpcsDto implements IDto{
   
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@DtoTag(name = "SITE_NAME")
	private String siteName;
	
	@DtoTag(name = "PLANT_NAME")
	private String plantName;

	@DtoTag(name = "DIVISION_ID")
	private String divisionId;

	@DtoTag(outputName = "DIVISION_NAME")
	private String divisionName;
	
	@DtoTag(outputName = "PLANT_CODE")
	private String gPCSPlantCode;
	
	@DtoTag(outputName = "PROCESS_LOCATION")
	private String gPCSProcessLocation;
	
	@DtoTag(outputName = "LINE_NO")
	private String gPCSLineNo;

	@DtoTag(outputName = "PLANT_CODE")
	private String planCode;

	@DtoTag(outputName = "DIVISION_ID_AND_NAME")
	private String divisionIdAndName;

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getPlantName() {
		return plantName;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	public String getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getgPCSPlantCode() {
		return gPCSPlantCode;
	}

	public void setgPCSPlantCode(String gPCSPlantCode) {
		this.gPCSPlantCode = gPCSPlantCode;
	}

	public String getgPCSProcessLocation() {
		return gPCSProcessLocation;
	}

	public void setgPCSProcessLocation(String gPCSProcessLocation) {
		this.gPCSProcessLocation = gPCSProcessLocation;
	}

	public String getgPCSLineNo() {
		return gPCSLineNo;
	}

	public void setgPCSLineNo(String gPCSLineNo) {
		this.gPCSLineNo = gPCSLineNo;
	}

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public String getDivisionIdAndName() {
		return divisionIdAndName;
	}

	public void setDivisionIdAndName(String divisionIdAndName) {
		this.divisionIdAndName = divisionIdAndName;
	}
	
	public Object getId() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getProductId() {
		// TODO Auto-generated method stub
		return null;
	}
	public void setProductId(String productId) {
		// TODO Auto-generated method stub
		
	}
	public List<Measurement> getMeasurements() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getInstalledPartReason() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getPartSerialNumber() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getPartName() {
		// TODO Auto-generated method stub
		return null;
	}
	public void setPartName(String partName) {
		// TODO Auto-generated method stub
		
	}
	public String getResultValue() {
		// TODO Auto-generated method stub
		return null;
	}
	public void setResultValue(String resultValue) {
		// TODO Auto-generated method stub
		
	}
	
	
}
