package com.honda.galc.client.teamleader;

import java.util.List;

import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * <h3>Class description</h3>
 * Controller class for Manual Lot Control Repair.
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Jan 19, 2015</TD>
 * <TD>1.0</TD>
 * <TD>GY 20150119</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */

public class ManualLotControlRepairBaseController extends ManualLotControlRepairController<BaseProduct, ProductBuildResult> {

	public ManualLotControlRepairBaseController(MainWindow mainWin, ManualLotControlRepairPanel repairPanel,
			IManualLtCtrResultEnterViewManager resultEnterViewManager) {
		super(mainWin, repairPanel, resultEnterViewManager);
	}
	
	@Override
	protected BaseProduct checkProductOnServer(String productId) {
		try {
			return ProductTypeUtil.getTypeUtil(getProductType()).findProduct(productId);
		} catch (Exception e) {
			String msg = "failed to load " + getProductType().name() +	": " + productId;
			Logger.getLogger().warn(e, msg);
			throw new TaskException(msg);
		}
	}

	@Override
	protected boolean isMeasurementStatusOk() {
		return partResult.isHeadLess() || super.isMeasurementStatusOk();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void loadProductBuildResults() {
		productBuildResulits = (List<ProductBuildResult>) ProductTypeUtil.getTypeUtil(getProductType()).getProductBuildResultDao().findAllByProductId(product.getProductId());
		loadProductBuildResults(productBuildResulits);
	}

	@Override
	protected void assembleLotControl() {
		super.assembleLotControl();
		if(product instanceof DieCast) {
			super.assembleLotControlDiecast();
		}
	}
}
