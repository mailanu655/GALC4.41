package com.honda.galc.client.teamlead.ltCtrRepair;


import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.teamleader.fx.ManualLotControlRepairPanel;
import com.honda.galc.client.teamleader.model.PartResult;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Head;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.service.ServiceFactory;

public class ManualLotControlRepairEngineController extends	ManualLotControlRepairController {
	
	public ManualLotControlRepairEngineController(MainWindow mainWin,
			ManualLotControlRepairPanel repairPanel) {
		super(mainWin, repairPanel);
	}



	@Override
	protected void removeInstalledPart(PartResult result) {
		if(result.getInstalledPart() == null || result.getInstalledPart().getMeasurements() == null)
			return;
		
		//make it exactly the same as current HCM production for Engine Plant, e.g. when repair 
		//head less measurement data is keep untouched. 
		if(!result.isHeadLess())
			super.removeMeasurementData(result);

		removeInstalledPartData(result);

		Logger.getLogger().info("Installed Part Result was removed by user:" + ClientMainFx.getInstance().getApplicationContext().getUserId() +
				System.getProperty("line.separator") + result.getInstalledPart());
	}
	
}
