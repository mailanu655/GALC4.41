package com.honda.galc.client.teamleader;

import com.honda.galc.client.teamleader.property.ManualLotControlRepairPropertyBean;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.data.ProductType;
/**
 * 
 * <h3>FrameManualLtCtrResultEnterViewManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> FrameManualLtCtrResultEnterViewManager description </p>
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
 *   
 * @author Paul Chou
 * Aug 23, 2010
 *
 */
public class FrameManualLtCtrResultEnterViewManager extends ManualLtCtrResultEnterViewManagerBase{

	public FrameManualLtCtrResultEnterViewManager(MainWindow mainWin,
			ManualLotControlRepairPropertyBean property) {
		super(mainWin.getApplicationContext(), property, mainWin.getApplication());
	}

	public FrameManualLtCtrResultEnterViewManager(MainWindow mainWin,
			ManualLotControlRepairPropertyBean property, ProductType currentProductType) {
		super(mainWin.getApplicationContext(), property, mainWin.getApplication(), currentProductType);
	}
	
}
