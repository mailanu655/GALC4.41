package com.honda.galc.device.dataformat;

import java.io.Serializable;

import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.Tag;

/**
 * <h3>Class description</h3>
 * 
 * <h4>Description</h4>
 * <p>
 *   
 * </p>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>July 17, 2013</TD>
 * <TD>1.0</TD>
 * <TD>20130717</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 1.0
 * @author Dylan Yang
 */

public class RfidData implements IDeviceData, Serializable {
	private static final long serialVersionUID = -1255435459908858585L;

	@Tag(name="PRODUCT_ID")
	private String productId;
	
	@Tag(name="MODEL_YEAR_CODE")
	private String modelYearCode;
	
	@Tag(name="MODEL_CODE")
	private String modelCode;
	
	@Tag(name="MODEL_TYPE_CODE")
	private String modelTypeCode;
	
	@Tag(name="MODEL_OPTION_CODE")
	private String modelOptionCode;
	
	@Tag(name="PRODUCTION_LOT")
	private String productionLot;
	
	@Tag(name="LOT_SEQ_NO")
	private String lotSeqNo;

	@Tag(name="DATA_COLLECTION_COMPLETE")
	private String dcComplete;
	
	public RfidData() {
		
	}

	public void setDcComplete(String dcComplete) {
		this.dcComplete = dcComplete;
	}

	public String getDcComplete() {
		return dcComplete;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductId() {
		return productId;
	}

	public String getModelYearCode() {
		return modelYearCode;
	}

	public void setModelYearCode(String modelYearCode) {
		this.modelYearCode = modelYearCode;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getModelTypeCode() {
		return modelTypeCode;
	}

	public void setModelTypeCode(String modelTypeCode) {
		this.modelTypeCode = modelTypeCode;
	}

	public String getModelOptionCode() {
		return modelOptionCode;
	}

	public void setModelOptionCode(String modelOptionCode) {
		this.modelOptionCode = modelOptionCode;
	}

	public String getProductionLot() {
		return productionLot;
	}

	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}

	public String getLotSeqNo() {
		return lotSeqNo;
	}

	public void setLotSeqNo(String lotSeqNo) {
		this.lotSeqNo = lotSeqNo;
	}
}
