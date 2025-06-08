package com.honda.galc.client.teamlead.ltCtrRepair;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.teamleader.fx.ManualLotControlRepairPanel;
import com.honda.galc.client.teamleader.model.PartResult;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.HeadBuildResultDao;
import com.honda.galc.dao.product.HeadDao;
import com.honda.galc.entity.product.Head;
import com.honda.galc.entity.product.HeadBuildResult;
import com.honda.galc.service.ServiceFactory;

public class ManualLotControlRepairHeadController extends
		ManualLotControlRepairController {

	public ManualLotControlRepairHeadController(MainWindow mainWin,
			ManualLotControlRepairPanel repairPanel) {
		super(mainWin, repairPanel);
	}

	protected void removeInstalledPart(PartResult result) {
		if(result.getBuildResult() == null)
			return;
		
		ServiceFactory.getDao(HeadBuildResultDao.class).remove((HeadBuildResult)result.getBuildResult());
		result.setBuildResult(null);

		Logger.getLogger().info("Installed Part Result was removed by user:" + ClientMainFx.getInstance().getApplicationContext().getUserId() +
				System.getProperty("line.separator") + result.getBuildResult());

	}
	
	
}
