package com.honda.galc.client.qi.lotControl;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.qi.base.AbstractQiProcessView;
import com.honda.galc.client.qi.base.QiProcessModel;
import com.honda.galc.client.teamleader.qi.productRecovery.ManualLotControlRepairPanel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.utils.QiConstant;

/**
 * 
 * <h3>LotControlManualRepairView</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlManualRepairView description </p>
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
 * <TD>Apr 15, 2011</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Pooja Patidar
 * @since Aug 31, 2017
 */

public class LotControlManualRepairView extends AbstractQiProcessView<QiProcessModel, LotControlManualRepairController> {
	
	private ManualLotControlRepairPanel dataPanel;
	
	public LotControlManualRepairView(MainWindow mainWindow) {
		super(ViewId.PRODUCT_RECOVERY,mainWindow);
		
		
	}
	
	@Override
	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		renderProductPanel();
	}
	
	protected void renderProductPanel() {
		this.dataPanel = createProductDataPanel();
		this.getChildren().clear();
		this.setCenter(dataPanel);
	}
	
	protected ManualLotControlRepairPanel createProductDataPanel() {
		return new ManualLotControlRepairPanel(window, getModel().getApplicationContext().getProductTypeDataList(),
				getModel().getApplicationContext().getProductTypeData().getProductType());
	}

	@Override
	public void reload() {
		dataPanel.setProduct(getProductModel().getProduct());
		dataPanel.reload(getModel().getProductId());		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	
	
}
