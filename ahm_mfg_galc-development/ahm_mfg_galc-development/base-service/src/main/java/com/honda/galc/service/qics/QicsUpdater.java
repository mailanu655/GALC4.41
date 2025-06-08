package com.honda.galc.service.qics;

import java.util.List;

import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.service.IService;

/**
 * 
 * <h3>QicsUpdater</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QicsUpdater description </p>
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
 * @param <T>
 */

public interface QicsUpdater<T extends BaseProduct> extends IService{
	
	/**
	 * update Qics with product build results at specified process point
	 * 
	 * @param processPointId
	 * @param parts
	 * @return
	 */
	
	boolean update(String processPointId, List<? extends ProductBuildResult> parts);
}
