package com.honda.galc.client.teamleader.qi.productRecovery;

import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.service.ServiceFactory;
/**
 * 
 * <h3>ManualLotControlRepairKnuckleController</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ManualLotControlRepairKnuckleController description </p>
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
 * <TD>Mar 28, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author L&T Infotech
 * @since Aug 28, 2017
 */
public class ManualLotControlRepairKnuckleController extends
		ManualLotControlRepairController<SubProduct, InstalledPart> {

	public ManualLotControlRepairKnuckleController(MainWindow mainWin,
			ManualLotControlRepairPanel repairPanel,
			IManualLtCtrResultEnterViewManager resultEnterViewManager) {
		super(mainWin, repairPanel, resultEnterViewManager);
	}
	
	protected SubProduct checkProductOnServer(String productId) {
		try {
			SubProductDao subProductDao = ServiceFactory.getDao(SubProductDao.class);
			return subProductDao.findByKey(productId);
		} catch (Exception e) {
			String msg = "failed to load " + property.getProductType() +	": " + productId;
			Logger.getLogger().warn(e, msg);
			throw new TaskException(msg);
		}
	}
	

	@Override
	protected void loadProductBuildResults() {

		InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		productBuildResulits = installedPartDao.findAllByProductId(product.getProductId());
		
		loadInstalledParts(productBuildResulits);
		
	}

}
