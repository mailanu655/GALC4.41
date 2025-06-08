package com.honda.galc.device.dataformat;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.Tag;

/**
 * 
 * <h3>Data Collection Complete ProductId</h3>
 * <p>DataCollectionCompleteProductId</p>
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
 * @author Paul Chou<br>
 * Jan. 26, 2016
 *
 *
 */
public class DataCollectionCompleteProductId implements IDeviceData, Serializable{
	private static final long serialVersionUID = 1L;
	
	@Tag(name="PRODUCT_ID")
	private String productId;
	
	@Tag(name="DATA_COLLECTION_COMPLETE")
	private String completeFlag;

	public String getProductId() {
		return StringUtils.trim(productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getCompleteFlag() {
		return completeFlag;
	}

	public void setCompleteFlag(String completeFlag) {
		this.completeFlag = completeFlag;
	}
	

}
