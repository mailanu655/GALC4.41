package com.honda.galc.dto;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.honda.galc.data.ProductNumberDef;

/**
 * 
 * 
 * <h3>EngineNumberingDto Class description</h3>
 * <p> EngineNumberingDto description </p>
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
 * @author Jeffray Huang<br>
 * Feb 23, 2015
 *
 *
 */
public class EngineNumberingDto implements IDto{
       
	private static final long serialVersionUID = 1L;

	@DtoTag()
	private String productId;
	
	@DtoTag(outputName = "SPEC")
	private String productSpecCode;
	
	@DtoTag(name ="PART_SERIAL_NUMBER")
	private String mcNumber;
	
	@DtoTag()
	private int lotSize;
	
	@DtoTag()
	private String productionLot;
	
	@DtoTag()
	private String startProductId;
	
	@DtoTag(name="LAST_PASSING_PROCESS_POINT_ID")
	private String processPointId;
	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductSpecCode() {
		return StringUtils.trim(productSpecCode);
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public String getMcNumber() {
		return StringUtils.trim(mcNumber);
	}

	public void setMcNumber(String mcNumber) {
		this.mcNumber = mcNumber;
	}

	public int getLotSize() {
		return lotSize;
	}

	public void setLotSize(int lotSize) {
		this.lotSize = lotSize;
	}

	public String getProductionLot() {
		return StringUtils.trim(productionLot);
	}

	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}

	public String getStartProductId() {
		return StringUtils.trim(startProductId);
	}

	public void setStartProductId(String startProductId) {
		this.startProductId = startProductId;
	}

	public String getProcessPointId() {
		return StringUtils.trim(processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	
	private int getSerialNumber(String engineNumber) {
		return NumberUtils.toInt(ProductNumberDef.EIN.getSequence(engineNumber), -1);
	}
	
	public String getLotSeq() {
		return "" + (getSerialNumber(getProductId()) - getSerialNumber(getStartProductId()) + 1) + "/"+getLotSize();
	}
	
}
