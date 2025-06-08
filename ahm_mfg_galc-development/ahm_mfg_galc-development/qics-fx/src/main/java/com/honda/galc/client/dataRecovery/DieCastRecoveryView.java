package com.honda.galc.client.dataRecovery;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.qi.base.AbstractQiProcessView;
import com.honda.galc.client.qi.base.QiProcessModel;
import com.honda.galc.client.teamleader.fx.dataRecovery.DataRecoveryController;
import com.honda.galc.client.teamleader.fx.dataRecovery.ObjectFactory;
import com.honda.galc.client.teamleader.fx.dataRecovery.PartDataPanel;
import com.honda.galc.client.teamleader.fx.dataRecovery.ProductRecoveryConfig;
import com.honda.galc.client.teamleader.fx.dataRecovery.ProductRecoveryConfigProvider;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.data.ProductType;


/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>RecoveryView</code> is ...
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>vcc72927</TD>
 * <TD>July 21,2017</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author L&T Infotech
 */
public class DieCastRecoveryView extends AbstractQiProcessView<QiProcessModel, DieCastRecoveryController> {

	private PartDataPanel dataPanel;

	public DieCastRecoveryView(MainWindow window) {
		super(ViewId.DIE_CAST_RECOVERY, window);
	}

	@Override
	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		
	}
	
	protected void renderProductPanel() {
		this.dataPanel = createProductDataPanel();
		this.getChildren().clear();
		this.setCenter(dataPanel);
	}

	protected PartDataPanel createProductDataPanel() {
		ProductType productType =getModel().getApplicationContext().getProductTypeData().getProductType();
		DataRecoveryController controller = new DataRecoveryController(getMainWindow(), productType);

		String modelCode = null;
		if (getProductModel() != null && getProductModel().getProduct() != null) {
			modelCode = getProductModel().getProduct().getModelCode();
		}
		ProductRecoveryConfig recoveryConfig = ProductRecoveryConfigProvider.createProductRecoveryConfig(getModel().getApplicationContext().getProcessPoint(), productType, modelCode);
		return ObjectFactory.createProductDataPanel(recoveryConfig, controller);
	}

	@Override
	public void reload() {
	}

	@Override
	public void start() {
		getDataPanel().getController().setProduct(getModel().getProductModel().getProduct());
		getDataPanel().getController().selectBuildResults(dataPanel.getPartNames());
	}
	
	public PartDataPanel getDataPanel() {
		return dataPanel;
	}

	public void setDataPanel(PartDataPanel dataPanel) {
		this.dataPanel = dataPanel;
	}
	
}