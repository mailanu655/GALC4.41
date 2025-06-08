package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.ProductNumberDef.NumberType;


/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>DieCast</code> is used as super class for
 * <code>Engine<code> subcomponents that are die casted.
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>Karol Wozniak</TD>
 * <TD>Feb 6, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Jeffray Huang
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@MappedSuperclass()
public abstract class DieCast extends BaseProduct {

	private static final long serialVersionUID = 1L;

	@Column(name = "DC_SERIAL_NUMBER")
    private String dcSerialNumber;
	
	@Column(name = "MC_SERIAL_NUMBER")
    private String mcSerialNumber;
	
	@Column(name = "ENGINE_SERIAL_NUMBER")
    private String engineSerialNumber;
	
	@Column(name = "DUNNAGE")
    private String dunnage;
	
	@Column(name = "HOLD_STATUS")
    private int holdStatus;

	@Column(name = "DEFECT_STATUS")
    private Integer defectStatus;
	
	@Column(name = "MODEL")
    private String modelCode;
	
	@Transient
	private boolean isProductScrappable = true;

	public Integer getDefectStatusValue() {
		return defectStatus;
	}

	public void setDefectStatusValue(Integer defectStatus) {
		this.defectStatus = defectStatus;
	}

	public String getModelCode() {
		if(StringUtils.trimToEmpty(modelCode).length() == 3) return StringUtils.trim(modelCode);
		else return super.getModelCode();
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	
	@Override
	public String getProductSpecCode() {
		return StringUtils.trim(modelCode);
	}
	
	// === get/set === //
	public String getDcSerialNumber() {
		return StringUtils.trim(dcSerialNumber);
	}

	public void setDcSerialNumber(String dcSerialNumber) {
		this.dcSerialNumber = dcSerialNumber;
	}

	@Override
	public String getDunnage() {
		return StringUtils.trim(dunnage);
	}

	public void setDunnage(String dunnage) {
		this.dunnage = dunnage;
	}

	public String getEngineSerialNumber() {
		return StringUtils.trim(engineSerialNumber);
	}

	public void setEngineSerialNumber(String engineSerialNumber) {
		this.engineSerialNumber = engineSerialNumber;
	}

	public int getHoldStatus() {
		return holdStatus;
	}

	public void setHoldStatus(int holdStatus) {
		this.holdStatus = holdStatus;
	}

	public String getMcSerialNumber() {
		return StringUtils.trim(mcSerialNumber);
	}

	public void setMcSerialNumber(String mcSerialNumber) {
		this.mcSerialNumber = mcSerialNumber;
	}
	
	public String toString() {
		return toString(getProductId());
	}
	
	@Override
	public String getOwnerProductId() {
		return getEngineSerialNumber();
	}

	@Override
	public String getProductionLot() {
		return "";
	}

	@Override
	public boolean isProductScrappable() {
		return this.isProductScrappable;
	}
	
	public String getSerialNumber(NumberType numberType) {
		if (NumberType.IN.equals(numberType)) {
			return getProductId();
		}
		if (NumberType.MC.equals(numberType)) {
			return getMcSerialNumber();
		} 
		if (NumberType.DC.equals(numberType)) {
			return getDcSerialNumber();
		}
		return null;
	}
}
