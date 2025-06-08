package com.honda.galc.service;

import java.util.List;

import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ProductBuildResult;
/**
 * 
 * <h3>QicsService</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QicsService description </p>
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
 * <TD>May 11, 2011</TD>
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
 * @since May 11, 2011
 */

public interface QicsService extends IService {
	
	/**
	 * Update Qics for the product build results
	 * Base on the configuration, this service may create Qics defect, scrap product, scrap_preheat product or
	 * repair existing product defects.
	 * @param processPointId - process point id
	 * @param product - product entity
	 * @param buildResult - product build result list
	 * @return
	 */
	boolean update(String processPointId, BaseProduct product, List<? extends ProductBuildResult> buildResults);
	
	/**
	 *Update Qics for the product build results
	 * Base on the configuration, this service may create Qics defect, scrap product, scrap_preheat product or
	 * repair existing product defects.
	 * 
	 * @param processPointId - process point id
	 * @param productType - product type
	 * @param buildResults - product build result list
	 * @return
	 */
	boolean update(String processPointId, ProductType productType, List<? extends ProductBuildResult> buildResults);
}
