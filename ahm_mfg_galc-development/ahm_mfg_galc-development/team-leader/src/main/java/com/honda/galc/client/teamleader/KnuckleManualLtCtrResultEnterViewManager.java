package com.honda.galc.client.teamleader;

import com.honda.galc.client.teamleader.property.ManualLotControlRepairPropertyBean;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.data.ProductType;
/**
 * 
 * <h3>KnuckleManualLtCtrResultEnterViewManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> KnuckleManualLtCtrResultEnterViewManager description </p>
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
 * <TD>Mar 29, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Mar 29, 2012
 */
public class KnuckleManualLtCtrResultEnterViewManager extends ManualLtCtrResultEnterViewManagerBase{
	public KnuckleManualLtCtrResultEnterViewManager(MainWindow mainWin,
			ManualLotControlRepairPropertyBean property) {
		super(mainWin.getApplicationContext(), property, mainWin.getApplication());
	}
	
	public KnuckleManualLtCtrResultEnterViewManager(MainWindow mainWin,
			ManualLotControlRepairPropertyBean property, ProductType productType) {
		super(mainWin.getApplicationContext(), property, mainWin.getApplication(), productType);
	}
}
