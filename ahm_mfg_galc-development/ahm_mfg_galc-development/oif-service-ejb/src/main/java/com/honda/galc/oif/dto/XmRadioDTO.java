package com.honda.galc.oif.dto;

import com.honda.galc.util.OutputData;
/**
 * 
 * <h3>XmRadioDTO.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> class to hold the XM Radio to be activated data </p>
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
 * <TD>KG</TD>
 * <TD>Nov 18, 2014</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author KG
 * @created Nov 18, 2014
 */
public class XmRadioDTO implements IOutputFormat {
	@OutputData(value="VIN")
	private String vin;
	@OutputData(value="PRODUCT_DIVISION_CODE")
	private String productDivisionCode;
	@OutputData(value="MODEL_NAME")
	private String modelName;
	@OutputData(value="MODEL_YEAR")
	private String modelYear;
	@OutputData(value="PROD_DATE")
	private String prodDate;
	@OutputData(value="RADIO_INSTALLED_DATE")
	private String radioInstalledDate;
	@OutputData(value="SHIP_DATE")
	private String shipDate;
	@OutputData(value="XM_RADIO_ID_PREFIX")
	private String xmRadioIdPrefix;
	@OutputData(value="XM_RADIO_ID_MUSIC")
	private String xmRadioIdMusic;
	@OutputData(value="XM_RADIO_ID_DATA")
	private String xmRadioIdData;
	@OutputData(value="TCU_NUMBER")
	private String tcuNumber;
	@OutputData(value="ICC_ID")
	private String iccId;
	@OutputData(value="FILLER")
	private String filler;
	
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getProductDivisionCode() {
		return productDivisionCode;
	}
	public void setProductDivisionCode(String productDivisionCode) {
		this.productDivisionCode = productDivisionCode;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getModelYear() {
		return modelYear;
	}
	public void setModelYear(String modelYear) {
		this.modelYear = modelYear;
	}
	public String getProdDate() {
		return prodDate;
	}
	public void setProdDate(String prodDate) {
		this.prodDate = prodDate;
	}
	public String getRadioInstalledDate() {
		return radioInstalledDate;
	}
	public void setRadioInstalledDate(String radioInstalledDate) {
		this.radioInstalledDate = radioInstalledDate;
	}
	public String getShipDate() {
		return shipDate;
	}
	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}
	public String getXmRadioIdPrefix() {
		return xmRadioIdPrefix;
	}
	public void setXmRadioIdPrefix(String xmRadioIdPrefix) {
		this.xmRadioIdPrefix = xmRadioIdPrefix;
	}
	public String getXmRadioIdMusic() {
		return xmRadioIdMusic;
	}
	public void setXmRadioIdMusic(String xmRadioIdMusic) {
		this.xmRadioIdMusic = xmRadioIdMusic;
	}
	public String getXmRadioIdData() {
		return xmRadioIdData;
	}
	public void setXmRadioIdData(String xmRadioIdData) {
		this.xmRadioIdData = xmRadioIdData;
	}
	public String getTcuNumber() {
		return tcuNumber;
	}
	public void setTcuNumber(String tcuNumber) {
		this.tcuNumber = tcuNumber;
	}
	public String getIccId() {
		return iccId;
	}
	public void setIccId(String iccId) {
		this.iccId = iccId;
	}
	public String getFiller() {
		return filler;
	}
	public void setFiller(String filler) {
		this.filler = filler;
	}
}
