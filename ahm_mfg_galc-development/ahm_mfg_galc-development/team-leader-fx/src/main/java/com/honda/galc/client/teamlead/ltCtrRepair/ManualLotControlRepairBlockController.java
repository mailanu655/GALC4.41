package com.honda.galc.client.teamlead.ltCtrRepair;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.teamleader.fx.ManualLotControlRepairPanel;
import com.honda.galc.client.teamleader.model.PartResult;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.BlockBuildResultDao;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.BlockBuildResult;
import com.honda.galc.service.ServiceFactory;

public class ManualLotControlRepairBlockController extends
		ManualLotControlRepairController {

	public ManualLotControlRepairBlockController(MainWindow mainWin,
			ManualLotControlRepairPanel repairPanel) {
		super(mainWin, repairPanel);
	}
	
	@Override
	public Block checkProductOnServer(String productId) {
		try {

			return ServiceFactory.getDao(BlockDao.class).findByMCDCNumber(productId);
			
		} catch (Exception e) {
			String msg = "failed to load " + getProductType().getProductName() +	": " + productId;
			Logger.getLogger().warn(e, msg);
			throw new TaskException(msg);
		}
	}

	


	protected void removeInstalledPart(PartResult result) {
		if(result.getBuildResult() == null)
			return;
		
		ServiceFactory.getDao(BlockBuildResultDao.class).remove((BlockBuildResult)result.getBuildResult());
		result.setBuildResult(null);

		Logger.getLogger().info("Installed Part Result was removed by user:" + ClientMainFx.getInstance().getApplicationContext().getUserId() +
				System.getProperty("line.separator") + result.getBuildResult());

	}
	

}
