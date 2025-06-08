package com.honda.galc.client.teamleader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.teamleader.model.PartResult;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.HeadBuildResultDao;
import com.honda.galc.dao.product.HeadDao;
import com.honda.galc.entity.product.Head;
import com.honda.galc.entity.product.HeadBuildResult;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.service.ServiceFactory;
/**
 * 
 * <h3>ManualLotControlRepairHeadController</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ManualLotControlRepairHeadController description </p>
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
public class ManualLotControlRepairHeadController extends
		ManualLotControlRepairController<Head,HeadBuildResult> {

	public ManualLotControlRepairHeadController(MainWindow mainWin,
			ManualLotControlRepairPanel repairPanel,
			IManualLtCtrResultEnterViewManager resultEnterViewManager) {
		super(mainWin, repairPanel, resultEnterViewManager);
	}

	@Override
	protected void loadProductBuildResults() {
		
		productBuildResulits = ServiceFactory.getDao(HeadBuildResultDao.class).findAllByProductId(product.getProductId());
		
		loadProductBuildResults(productBuildResulits);
	}
	
	@Override
	protected Head checkProductOnServer(String productId) {
		try {

			return ServiceFactory.getDao(HeadDao.class).findByMCDCNumber(productId);
			
		} catch (Exception e) {
			String msg = "failed to load " + property.getProductType() +	": " + productId;
			Logger.getLogger().warn(e, msg);
			throw new TaskException(msg);
		}
	}

	protected void renderProductSpecField() {
		getView().getProductSpecField().setText(product.getMcSerialNumber());
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
		
		ServiceFactory.getDao(HeadBuildResultDao.class).remove((HeadBuildResult)result.getBuildResult());
		result.setBuildResult(null);

		Logger.getLogger().info("Installed Part Result was removed by user:" + appContext.getUserId() +
				System.getProperty("line.separator") + partResult.getBuildResult());

	}

}
