package com.honda.galc.service.datacollection;

import java.util.List;

import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.ProductBuildResult;

/**
 * 
 * <h3>IDeviceHelper</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> IDeviceHelper description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Mar 12, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Mar 12, 2012
 * @param <T>
 */

public interface IDeviceHelper{
	
	/**
	 * Get product build results for parts from input device data by lot control rules. 
	 * 
	 * @param rules
	 * @return
	 */
	public List<ProductBuildResult> getBuildResults(List<LotControlRule> rules);
	
	/**
	 * Get Product Id from device input
	 * @return
	 */
	public String getProductId();

	/**
	 * Set current product name for multi-product support
	 * @param productName 
	 */
	public void setProductName(String productName);

	/**
	 * Set current valid product Id 
	 * @param productId
	 */
	public void setCurrentProductId(String productId);
	
	/**
	 * Get input device Entity
	 * @return
	 */
	public Device getDevice();
	
	/**
	 * Input device formats for the current product
	 * @return
	 */
	public List<DeviceFormat> getCurrentProductDeviceDataFormats();
	
	/**
	 * Reply device formats for the current product
	 * @return
	 */
	public List<DeviceFormat> getCurrentProductReplyDeviceDataFormats();
	
	/**
	 * Get product build results for parts from input device data by property of MbpnInstalledParts. 
	 * 
	 * @param installPartNames
	 * @return
	 */
	public List<ProductBuildResult> getBuildResultsMbpnProduct(List<String> installPartNames);

	/**
	 * Indicate there is context update
	 * @return
	 */
	public boolean hasUpdate();
}
