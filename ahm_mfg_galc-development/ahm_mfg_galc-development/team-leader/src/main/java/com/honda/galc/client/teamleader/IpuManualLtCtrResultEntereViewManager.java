package com.honda.galc.client.teamleader;

import com.honda.galc.client.teamleader.property.ManualLotControlRepairPropertyBean;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.data.ProductType;

public class IpuManualLtCtrResultEntereViewManager extends ManualLtCtrResultEnterViewManagerBase{
	public IpuManualLtCtrResultEntereViewManager(MainWindow mainWin,
			ManualLotControlRepairPropertyBean property) {
		super(mainWin.getApplicationContext(), property, mainWin.getApplication());
	}
	
	public IpuManualLtCtrResultEntereViewManager(MainWindow mainWin,
			ManualLotControlRepairPropertyBean property, ProductType currentProductType) {
		super(mainWin.getApplicationContext(), property, mainWin.getApplication(), currentProductType);
	}
}
