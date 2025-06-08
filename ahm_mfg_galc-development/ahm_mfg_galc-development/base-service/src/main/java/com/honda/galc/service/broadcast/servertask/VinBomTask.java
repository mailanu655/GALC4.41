package com.honda.galc.service.broadcast.servertask;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.lcvinbom.VinBomService;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>VinBomTask</code> is ... .
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
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 */
public class VinBomTask implements IServerTask {

	public DataContainer execute(DataContainer dc) {
		String productId = dc.getString(DataContainerTag.PRODUCT_ID);
		String processPointId = dc.getString(DataContainerTag.PROCESS_POINT_ID);
		String productSpecCode = StringUtils.substring(dc.getString(DataContainerTag.PRODUCT_SPEC_CODE), 0, 7);
		
		BaseProduct product = (BaseProduct) dc.get(DataContainerTag.PRODUCT);
		String productionLot = product.getProductionLot();

		if (StringUtils.isBlank(productId)) {
			throw new TaskException("ProductId can not be blank.");
		}
	
		ServiceFactory.getService(VinBomService.class).vinPartAssignment(productSpecCode, productId, productionLot, processPointId);
		Logger.getLogger().info("VIN BOM part Assignment completed for productId: " + productId + " at processPoint: " + processPointId);
		return dc;
	}
	
	
}
