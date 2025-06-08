package com.honda.galc.client.teamleader;

import com.honda.galc.client.teamleader.property.ManualLotControlRepairPropertyBean;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.data.ProductType;

/**
 * 
 * <h3>FrontIpuCaseManualLtCtrResultEnterViewManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> FrontIpuCaseManualLtCtrResultEnterViewManager description </p>
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
 * <TD>K Maharjan</TD>
 * <TD>Dec 12, 2024</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 */

public class FrontIpuCaseManualLtCtrResultEnterViewManager extends ManualLtCtrResultEnterViewManagerBase{
	public FrontIpuCaseManualLtCtrResultEnterViewManager(MainWindow mainWin,
			ManualLotControlRepairPropertyBean property) {
		super(mainWin.getApplicationContext(), property, mainWin.getApplication());
	}
	
	public FrontIpuCaseManualLtCtrResultEnterViewManager(MainWindow mainWin,
			ManualLotControlRepairPropertyBean property, ProductType currentProductType) {
		super(mainWin.getApplicationContext(), property, mainWin.getApplication(), currentProductType);
	}
}
