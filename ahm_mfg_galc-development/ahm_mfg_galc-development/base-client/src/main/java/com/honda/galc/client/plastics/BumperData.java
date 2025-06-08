package com.honda.galc.client.plastics;

import java.io.Serializable;

public class BumperData implements Serializable {
	
	final static long serialVersionUID = 3206093459760846163L;
	
	private String bumperId;
	private String bumperTypeId;
	private String manufacturerCode;
	private int cartId;
	private long productScheduleId; // max BIGINT Value = 9 223 372 036 854 775 807
	private int inventoryCategoryId;
	private String trackingStatus;
	private String actualModelYearCode;
	private String actualModelCode;
	private String actualModelTypeCode;
	private String actualExteriorColorCode;
	private String actualModelOptionCode;
	private String actualInteriorColorCode;
	private int paintCount;
	private String actualPaintPlant;
	private String actualFramePlant;
	private String createUserId;
	private String kdLot;
	private String productionLot;
	private Integer defectStatus;

	public BumperData() {
	}
	
	public String getKdLot() {
		return this.kdLot;
	}

	public void setKdLot(String kdLot) {
		this.kdLot = kdLot;
	}

	public String getProductionLot() {
		return this.productionLot;
	}

	public void setProductionLot(String prodLotNumber) {
		this.productionLot = prodLotNumber;
	}
	public String getActualExteriorColorCode() {
		return this.actualExteriorColorCode;
	}
	public void setActualExteriorColorCode(String actualExteriorColorCode) {
		this.actualExteriorColorCode = actualExteriorColorCode;
	}
	public String getActualFramePlant() {
		return this.actualFramePlant;
	}
	public void setActualFramePlant(String actualFramePlant) {
		this.actualFramePlant = actualFramePlant;
	}
	public String getActualInteriorColorCode() {
		return this.actualInteriorColorCode;
	}
	public void setActualInteriorColorCode(String actualInteriorColorCode) {
		this.actualInteriorColorCode = actualInteriorColorCode;
	}
	public String getActualModelCode() {
		return this.actualModelCode;
	}
	public void setActualModelCode(String actualModelCode) {
		this.actualModelCode = actualModelCode;
	}
	public String getActualModelOptionCode() {
		return this.actualModelOptionCode;
	}
	public void setActualModelOptionCode(String actualModelOptionCode) {
		this.actualModelOptionCode = actualModelOptionCode;
	}
	public String getActualModelTypeCode() {
		return this.actualModelTypeCode;
	}
	public void setActualModelTypeCode(String actualModelTypeCode) {
		this.actualModelTypeCode = actualModelTypeCode;
	}
	public String getActualModelYearCode() {
		return this.actualModelYearCode;
	}
	public void setActualModelYearCode(String actualModelYearCode) {
		this.actualModelYearCode = actualModelYearCode;
	}
	public String getActualPaintPlant() {
		return this.actualPaintPlant;
	}
	public void setActualPaintPlant(String actualPaintPlant) {
		this.actualPaintPlant = actualPaintPlant;
	}
	public String getBumperId() {
		return this.bumperId;
	}
	public void setBumperId(String bumperId) {
		this.bumperId = bumperId;
	}
	public String getBumperTypeId() {
		return this.bumperTypeId;
	}
	public void setBumperTypeId(String bumperTypeId) {
		this.bumperTypeId = bumperTypeId;
	}
	public int getCartId() {
		return this.cartId;
	}
	public Integer getDefectStatus() {
		return this.defectStatus;
	}
	public void setDefectStatus(Integer defect) {
		this.defectStatus = defect;
	}
	public void setCartId(Integer cartId) {
		this.cartId = cartId.intValue();
	}
	public String getCreateUserId() {
		return this.createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public int getInventoryCategoryId() {
		return this.inventoryCategoryId;
	}
	public void setInventoryCategoryId(Integer inventoryCategoryId) {
		this.inventoryCategoryId = inventoryCategoryId.intValue();
	}
	public String getManufacturerCode() {
		return this.manufacturerCode;
	}
	public void setManufacturerCode(String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
	}
	public int getPaintCount() {
		return this.paintCount;
	}
	public void setPaintCount(Integer paintCount) {
		//this.paintCount = paintCount;
		this.paintCount = paintCount.intValue();
	}
	public long getProductScheduleId() {
		return this.productScheduleId;
	}
	public void setProductScheduleId(Long productScheduleId) {
		this.productScheduleId = productScheduleId.longValue();
	}
	public String getTrackingStatus() {
		return this.trackingStatus;
	}
	public void setTrackingStatus(String trackingStatus) {
		this.trackingStatus = trackingStatus;
	}
	// Helper to get MTO for QICS etc.
	public String getYMTOC() {
		return this.actualModelYearCode + this.actualModelCode + this.actualModelTypeCode + this.actualModelOptionCode + this.actualExteriorColorCode;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(" bumperId: " + this.bumperId);
		sb.append(" bumperTypeId: " + this.bumperTypeId);
		sb.append(" manufacturerCode: " + this.manufacturerCode);
		sb.append(" cartId: " + this.cartId);
		sb.append(" productScheduleId: " + this.productScheduleId);
		sb.append(" inventoryCategoryId: " + this.inventoryCategoryId);
		sb.append(" trackingStatus: " + this.trackingStatus);
		sb.append(" actualModelYearCode: " + this.actualModelYearCode);
		sb.append(" actualModelCode: " + this.actualModelCode);
		sb.append(" actualModelTypeCode: " + this.actualModelTypeCode);
		sb.append(" actualExteriorColorCode: " + this.actualExteriorColorCode);
		sb.append(" actualModelOptionCode: " + this.actualModelOptionCode);
		sb.append(" actualInteriorColorCode: " + this.actualInteriorColorCode);
		sb.append(" paintCount: " + this.paintCount);
		sb.append(" actualPaintPlant: " + this.actualPaintPlant);
		sb.append(" actualFramePlant: " + this.actualFramePlant);
		sb.append(" createUserId: " + this.createUserId);
		sb.append(" kdLot: " + this.kdLot);
		sb.append(" productionLot: " + this.productionLot);
		sb.append(" defectStatus: " + this.defectStatus);
		
		return sb.toString();
	}

}
