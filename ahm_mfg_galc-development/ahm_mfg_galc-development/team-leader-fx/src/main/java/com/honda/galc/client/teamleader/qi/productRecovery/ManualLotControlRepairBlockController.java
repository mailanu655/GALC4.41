package com.honda.galc.client.teamleader.qi.productRecovery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.utils.ManualLotControlRepairConstants;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.BlockBuildResultDao;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.BlockBuildResult;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.service.ServiceFactory;
/**
 * 
 * <h3>ManualLotControlRepairBlockController</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ManualLotControlRepairBlockController description </p>
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
 * <TD>L&T Infotech</TD>
 * <TD>Aug 28,2017</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author L&T infotech
 * Aug 18, 2017
 */
public class ManualLotControlRepairBlockController extends
		ManualLotControlRepairController<Block, BlockBuildResult> {

	public ManualLotControlRepairBlockController(MainWindow mainWin,
			ManualLotControlRepairPanel repairPanel,
			IManualLtCtrResultEnterViewManager resultEnterViewManager) {
		super(mainWin, repairPanel, resultEnterViewManager);
	}
	
	@Override
	protected Block checkProductOnServer(String productId) {
		try {

			return ServiceFactory.getDao(BlockDao.class).findByMCDCNumber(productId);
			
		} catch (Exception e) {
			String msg = "failed to load " + property.getProductType() +	": " + productId;
			Logger.getLogger().warn(e, msg);
			throw new TaskException(msg);
		}
	}

	@Override
	protected void loadProductBuildResults() {
		productBuildResulits = ServiceFactory.getDao(BlockBuildResultDao.class).findAllByProductId(product.getProductId());

		loadProductBuildResults(productBuildResulits);

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
		
		ServiceFactory.getDao(BlockBuildResultDao.class).remove((BlockBuildResult)result.getBuildResult());
		result.setBuildResult(null);

		Logger.getLogger().info("Installed Part Result was removed by user:" + appContext.getUserId() +
				ManualLotControlRepairConstants.NEW_LINE + partResult.getBuildResult());

	}
	
}
