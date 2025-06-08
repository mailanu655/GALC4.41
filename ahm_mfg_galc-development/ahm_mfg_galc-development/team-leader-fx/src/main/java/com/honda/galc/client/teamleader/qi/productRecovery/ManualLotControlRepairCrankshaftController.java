/**
 * 
 */
package com.honda.galc.client.teamleader.qi.productRecovery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.CrankshaftBuildResultDao;
import com.honda.galc.dao.product.CrankshaftDao;
import com.honda.galc.entity.product.Crankshaft;
import com.honda.galc.entity.product.CrankshaftBuildResult;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.service.ServiceFactory;

/**
 * <h3>ManualLotControlRepairCrankshaftController.java Class description</h3>
 * <p> ManualLotControlRepairCrankshaftController.java description </p>
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
public class ManualLotControlRepairCrankshaftController extends
ManualLotControlRepairController<Crankshaft,CrankshaftBuildResult>{
	public ManualLotControlRepairCrankshaftController(MainWindow mainWin,
			ManualLotControlRepairPanel repairPanel,
			IManualLtCtrResultEnterViewManager resultEnterViewManager) {
		super(mainWin, repairPanel, resultEnterViewManager);
	}

	@Override
	protected void loadProductBuildResults() {
		
		productBuildResulits = ServiceFactory.getDao(CrankshaftBuildResultDao.class).findAllByProductId(product.getProductId());
		
		loadProductBuildResults(productBuildResulits);
	}
	
	@Override
	protected Crankshaft checkProductOnServer(String productId) {
		try {

			return ServiceFactory.getDao(CrankshaftDao.class).findByMCDCNumber(productId);
			
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
		
		ServiceFactory.getDao(CrankshaftBuildResultDao.class).remove((CrankshaftBuildResult)result.getBuildResult());
		result.setBuildResult(null);

		Logger.getLogger().info("Installed Part Result was removed by user:" + appContext.getUserId() +
				System.getProperty("line.separator") + partResult.getBuildResult());

	}
	
}