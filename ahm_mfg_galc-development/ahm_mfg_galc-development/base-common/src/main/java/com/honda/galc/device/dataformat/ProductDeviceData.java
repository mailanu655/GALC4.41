package com.honda.galc.device.dataformat;

import java.io.Serializable;

import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.Tag;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProductDeviceData</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Sep 18, 2013
 */
public class ProductDeviceData implements IDeviceData, Serializable {

	private static final long serialVersionUID = 1L;

	@Tag(name = "DATA_COLLECTION_COMPLETE", optional = false)
	private String dataCollectionComplete;

	@Tag(name = "PRODUCT_ID", optional = true)
	private String productId;

	@Tag(name = "REPAIRED_FLAG", optional = true)
	private Boolean repairedFlag;

	@Tag(name = "DEFECT_STATUS", optional = true)
	private String defectStatus = "";

	@Tag(name = "HOLD_STATUS", optional = true)
	private int holdStatus;

	@Tag(name = "PRODUCT_SPEC_CODE", optional = true)
	private String productSpecCode = "";

	@Tag(name = "MODEL_YEAR_CODE", optional = true)
	private String modelYearCode = "";

	@Tag(name = "MODEL_CODE", optional = true)
	private String modelCode = "";

	@Tag(name = "MODEL_TYPE_CODE", optional = true)
	private String modelTypeCode = "";

	@Tag(name = "MODEL_OPTION_CODE", optional = true)
	private String modelOptionCode = "";

	@Tag(name = "MODEL_YEAR_MODEL_CODE", optional = true)
	private String modelYearModelCode = "";

	// === get/set === //
	public String getDataCollectionComplete() {
		return dataCollectionComplete;
	}

	public void setDataCollectionComplete(String dataCollectionComplete) {
		this.dataCollectionComplete = dataCollectionComplete;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getDefectStatus() {
		return defectStatus;
	}

	public void setDefectStatus(String defectStatus) {
		this.defectStatus = defectStatus;
	}

	public Boolean getRepairedFlag() {
		return repairedFlag;
	}

	public void setRepairedFlag(Boolean repairedFlag) {
		this.repairedFlag = repairedFlag;
	}

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
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

	public String getModelYearModelCode() {
		return modelYearModelCode;
	}

	public void setModelYearModelCode(String modelYearModelCode) {
		this.modelYearModelCode = modelYearModelCode;
	}

	public int getHoldStatus() {
		return holdStatus;
	}

	public void setHoldStatus(int holdStatus) {
		this.holdStatus = holdStatus;
	}
}
