/**
 * 
 */
package com.honda.galc.client.teamleader.qi.productRecovery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.utils.ManualLotControlRepairConstants;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.ConrodBuildResultDao;
import com.honda.galc.dao.product.ConrodDao;
import com.honda.galc.entity.product.Conrod;
import com.honda.galc.entity.product.ConrodBuildResult;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.service.ServiceFactory;

/**
 * <h3>ManualLotControlRepairConrodController.java Class description</h3>
 * <p> ManualLotControlRepairConrodController.java description </p>
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
 * @author L&T infotech
 * Aug 18, 2017
 */
public class ManualLotControlRepairConrodController extends
ManualLotControlRepairController<Conrod,ConrodBuildResult>{
	public ManualLotControlRepairConrodController(MainWindow mainWin,
			ManualLotControlRepairPanel repairPanel,
			IManualLtCtrResultEnterViewManager resultEnterViewManager) {
		super(mainWin, repairPanel, resultEnterViewManager);
	}

	@Override
	protected void loadProductBuildResults() {
		
		productBuildResulits = ServiceFactory.getDao(ConrodBuildResultDao.class).findAllByProductId(product.getProductId());
		
		loadProductBuildResults(productBuildResulits);
	}
	
	@Override
	protected Conrod checkProductOnServer(String productId) {
		try {

			return ServiceFactory.getDao(ConrodDao.class).findByMCDCNumber(productId);
			
		} catch (Exception e) {
			String msg = "failed to load " + property.getProductType() +	": " + productId;
			Logger.getLogger().warn(e, msg);
			throw new TaskException(msg);
		}
	}

	@Override
	protected void assembleLotControl() {
		super.assembleLotControl();
		super.assembleLotControlDiecast();
	}

	@Override
	protected void removeInstalledPart(PartResult result) {
		if(result.getBuildResult() == null)
			return;
		
		ServiceFactory.getDao(ConrodBuildResultDao.class).remove((ConrodBuildResult)result.getBuildResult());
		result.setBuildResult(null);

		Logger.getLogger().info("Installed Part Result was removed by user:" + appContext.getUserId() +
				ManualLotControlRepairConstants.NEW_LINE + partResult.getBuildResult());

	}
	
}
