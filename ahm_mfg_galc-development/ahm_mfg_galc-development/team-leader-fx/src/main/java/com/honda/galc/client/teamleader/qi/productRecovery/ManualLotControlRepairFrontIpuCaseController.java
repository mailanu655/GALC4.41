package com.honda.galc.client.teamleader.qi.productRecovery;

import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.entity.product.FrontIpuCase;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.service.ServiceFactory;
/**
 * 
 * <h3>ManualLotControlRepairFrontIpuCaseController</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ManualLotControlRepairFrontIpuCaseController description </p>
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
public class ManualLotControlRepairFrontIpuCaseController extends ManualLotControlRepairController<FrontIpuCase,InstalledPart> {

	public ManualLotControlRepairFrontIpuCaseController(MainWindow mainWin,
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
