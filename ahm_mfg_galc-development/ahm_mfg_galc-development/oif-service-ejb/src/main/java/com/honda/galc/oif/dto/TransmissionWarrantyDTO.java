package com.honda.galc.oif.dto;

import com.honda.galc.util.OutputData;

public class TransmissionWarrantyDTO implements IOutputFormat {

	@OutputData( "PRODUCT_ID" )
	private String productId;
	//the transmission process point off date.
	@OutputData( "TRANS_BUILD_DATE" )
	private String buildDate;
	//the transmission process point off time
	@OutputData( "TRANS_BUILD_TIME" )
	private String buildTime;
	//the line number where the transmission was building
	@OutputData( "TRANS_BLD_LINE_NO" )
	private String lineNumber;
	@OutputData( "TRANS_FACTORY_CD" )
	private String plantCode;
	@OutputData( "TRANS_SHIFT_CD" )
	private String shift;
	//the model code of transmission
	@OutputData( "TRANS_TEAM_CD" )
	private String teamCd;
	//the type code of transmission
	@OutputData( "TRANS_TYPE_CD" )
	private String typeCd;
	//die casting lot number for the case
	@OutputData( "TRANS_CAST_LOT_NO" )
	private String castLotNumber;
	//machining lot number for the case
	@OutputData( "TRANS_MATCH_LOT_NO" )
	private String machLotNumber;
	//die casting lot number for the torque case
	@OutputData( "TORQUE_CAST_LOT_NO" )
	private String torqueCastLotNumber;
	//machining lot number for the torque case
	@OutputData( "TORQUE_MACH_LOT_NO" )
	private String torqueMachLotNumber;
	//transmission lot number
	@OutputData( "TRANS_KD_LOT_NO" )
	private String kdLotNumber;
	//transmission prod lot kd
	@OutputData( "PROD_LOT_KD_NO" )
	private String prodLotKd;
	//layout filler
	@OutputData( "FILLER")
	private String filler;
	
	/**
	 * @return the priduct_id
	 */
	public String getProductId() {
		return productId;
	}
	/**
	 * @param priduct_id the priduct_id to set
	 */
	public void setProductId(String priductId) {
		this.productId = priductId;
	}
	/**
	 * @return the buildDate
	 */
	public String getBuildDate() {
		return buildDate;
	}
	/**
	 * @param buildDate the buildDate to set
	 */
	public void setBuildDate(String buildDate) {
		this.buildDate = buildDate;
	}
	/**
	 * @return the buildTime
	 */
	public String getBuildTime() {
		return buildTime;
	}
	/**
	 * @param buildTime the buildTime to set
	 */
	public void setBuildTime(String buildTime) {
		this.buildTime = buildTime;
	}
	/**
	 * @return the lineNumber
	 */
	public String getLineNumber() {
		return lineNumber;
	}
	/**
	 * @param lineNumber the lineNumber to set
	 */
	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}
	/**
	 * @return the plantCode
	 */
	public String getPlantCode() {
		return plantCode;
	}
	/**
	 * @param plantCode the plantCode to set
	 */
	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}
	/**
	 * @return the shift
	 */
	public String getShift() {
		return shift;
	}
	/**
	 * @param shift the shift to set
	 */
	public void setShift(String shift) {
		this.shift = shift;
	}
	/**
	 * @return the teamCd
	 */
	public String getTeamCd() {
		return teamCd;
	}
	/**
	 * @param teamCd the teamCd to set
	 */
	public void setTeamCd(String teamCd) {
		this.teamCd = teamCd;
	}
	/**
	 * @return the typeCd
	 */
	public String getTypeCd() {
		return typeCd;
	}
	/**
	 * @param typeCd the typeCd to set
	 */
	public void setTypeCd(String typeCd) {
		this.typeCd = typeCd;
	}
	/**
	 * @return the castLotNumber
	 */
	public String getCastLotNumber() {
		return castLotNumber;
	}
	/**
	 * @param castLotNumber the castLotNumber to set
	 */
	public void setCastLotNumber(String castLotNumber) {
		this.castLotNumber = castLotNumber;
	}
	/**
	 * @return the machLotNumber
	 */
	public String getMachLotNumber() {
		return machLotNumber;
	}
	/**
	 * @param machLotNumber the machLotNumber to set
	 */
	public void setMachLotNumber(String machLotNumber) {
		this.machLotNumber = machLotNumber;
	}
	/**
	 * @return the torqueCastLotNumber
	 */
	public String getTorqueCastLotNumber() {
		return torqueCastLotNumber;
	}
	/**
	 * @param torqueCastLotNumber the torqueCastLotNumber to set
	 */
	public void setTorqueCastLotNumber(String torqueCastLotNumber) {
		this.torqueCastLotNumber = torqueCastLotNumber;
	}
	/**
	 * @return the torqueMachLotNumber
	 */
	public String getTorqueMachLotNumber() {
		return torqueMachLotNumber;
	}
	/**
	 * @param torqueMachLotNumber the torqueMachLotNumber to set
	 */
	public void setTorqueMachLotNumber(String torqueMachLotNumber) {
		this.torqueMachLotNumber = torqueMachLotNumber;
	}
	/**
	 * @return the kdLotNumber
	 */
	public String getKdLotNumber() {
		return kdLotNumber;
	}
	/**
	 * @param kdLotNumber the kdLotNumber to set
	 */
	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}
	/**
	 * @return the prodLotKd
	 */
	public String getProdLotKd() {
		return prodLotKd;
	}
	/**
	 * @param prodLotKd the prodLotKd to set
	 */
	public void setProdLotKd(String prodLotKd) {
		this.prodLotKd = prodLotKd;
	}
	/**
	 * 
	 * @return the filler
	 */
	public String getFiller() {
		return filler;
	}
	/**
	 * 
	 * @param filler the prodLotKd to set
	 */
	public void setFiller(String filler) {
		this.filler = filler;
	}
}
