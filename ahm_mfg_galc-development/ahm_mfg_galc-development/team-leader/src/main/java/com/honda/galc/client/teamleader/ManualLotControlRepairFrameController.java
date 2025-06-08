package com.honda.galc.client.teamleader;

import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.service.ServiceFactory;
/**
 * 
 * <h3>ManualLotControlRepairFrameController</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ManualLotControlRepairFrameController description </p>
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
 * @author Paul Chou
 * @since Mar 28, 2012
 */
public class ManualLotControlRepairFrameController extends ManualLotControlRepairController<Frame,InstalledPart> {

	public ManualLotControlRepairFrameController(MainWindow mainWin,
			ManualLotControlRepairPanel repairPanel,
			IManualLtCtrResultEnterViewManager resultEnterViewManager) {
		super(mainWin, repairPanel, resultEnterViewManager);
	}
	

	@Override
	protected void loadProductBuildResults() {

		InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		productBuildResulits = installedPartDao.findAllByProductId(product.getProductId());
		
		loadInstalledParts(productBuildResulits);
		
	}
	
	
}
