package com.honda.galc.client.teamleader.fx.dataRecovery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.ui.ApplicationMainPane;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProduct;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProductRecoveryPanel</code> is ...
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
 * <TD> L&T Infotech</TD>
 * <TD>Jul 14, 2017</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author  L&T Infotech
 */
public class ProductRecoveryPanel extends ApplicationMainPane implements EventHandler<ActionEvent> {

	private LoggedLabel headerLabel;
	private ProductPanel productPanel;
	private PartDataPanel dataPanel;

	private boolean idle = true;
	private ProductType productType;
	private ProductRecoveryConfig config;
	private Map<String, ProductRecoveryConfig> recoveryConfigs;
	private Map<String, PartDataPanel> dataPanels;

	private MigPane dataPanelContainer;
	private DataRecoveryController controller;

	public ProductRecoveryPanel(MainWindow frame, ProductType productType, ProductRecoveryConfig config) {
		super(frame);
		this.productType = productType;
		initialize(frame, config);
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
	}



	public ProductRecoveryPanel(MainWindow frame, ProductType productType, List<ProductRecoveryConfig> configs) {
		super(frame);
		this.productType = productType;
		this.dataPanels = new HashMap<String, PartDataPanel>();
		this.recoveryConfigs = new HashMap<String, ProductRecoveryConfig>();
		initialize(frame, configs);
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
	}

	protected void initialize(MainWindow frame, ProductRecoveryConfig config) {
		this.config = config;
		if (config == null) {
			return;
		}
		setController(new DataRecoveryController(frame, getProductType()));
		initializeElements(frame, config);
		setIdleMode();
	}


	protected void initialize(MainWindow frame, List<ProductRecoveryConfig> configs) {
		for (ProductRecoveryConfig prc : configs) {
			if (prc != null && StringUtils.isNotBlank(prc.getConfigId())) {
				getRecoveryConfigs().put(prc.getConfigId(), prc);
			}
		}
		ProductRecoveryConfig config = getRecoveryConfigs().get("*");
		if (config == null) {
			config = new ProductRecoveryConfig("*", getProductType().getProductName(), getProductType(), 0);
			getRecoveryConfigs().put("*", config);
		}
		initialize(frame, config);
	}

	protected void initializeElements(MainWindow frame, ProductRecoveryConfig config) {

		dataPanelContainer =  new MigPane("", "[grow]", "");
		
		headerLabel = createHeaderLabel();
		productPanel = createProductInfoPanel();
		dataPanel = createDataPanel(config);
		
		ScrollPane scrollPaneContainer = new ScrollPane();

		scrollPaneContainer.setContent(dataPanel);
		scrollPaneContainer.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		scrollPaneContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPaneContainer.setStyle(" -fx-background-color: transparent;");

		dataPanelContainer.add(headerLabel, "span,wrap");
		dataPanelContainer.add(productPanel, "span,wrap");
		dataPanelContainer.add(scrollPaneContainer, "span,wrap");

		this.setCenter(dataPanelContainer);
	}

	// === ui elements factory methods === //
	protected LoggedLabel createHeaderLabel() {
		LoggedLabel headerLabel = new LoggedLabel(getHeaderLabelTxt());
		headerLabel.setText(getHeaderLabelTxt());
		headerLabel.setFont(Utils.getInputFont());
		headerLabel.setAlignment(Pos.CENTER_LEFT);
		return headerLabel;
	}

	protected ProductPanel createProductInfoPanel() {
		ProductPanel panel = new ProductPanel(this);

		return panel;

	}

	protected PartDataPanel createDataPanel(ProductRecoveryConfig config) {
		PartDataPanel panel = ObjectFactory.createProductDataPanel(config, getController());
		panel.setDisable(false);
		if (getRecoveryConfigs() != null) {
			getDataPanels().put(config.getConfigId(), panel);
		}
		return panel;
	}

	// === get/set === //
	public ProductRecoveryConfig getConfig() {
		return config;
	}

	protected String getHeaderLabelTxt() {
		return getConfig().getTitle();
	}

	public ProductPanel getProductPanel() {
		return productPanel;
	}

	public PartDataPanel getDataPanel() {
		return dataPanel;
	}

	protected LoggedLabel getHeaderLabel() {
		return headerLabel;
	}

	public DataRecoveryController getController() {
		return controller;
	}

	public void setController(DataRecoveryController controller) {
		this.controller = controller;
	}

	protected BaseProduct getProduct() {
		return	getController().getProduct();
	}

	public boolean isIdle() {
		return idle;
	}

	public void setIdle(boolean idle) {
		this.idle = idle;
	}


	protected Map<String, ProductRecoveryConfig> getRecoveryConfigs() {
		return recoveryConfigs;
	}

	protected Map<String, PartDataPanel> getDataPanels() {
		return dataPanels;
	}

	public String getScreenName() {
		if (getRecoveryConfigs() != null && !getRecoveryConfigs().isEmpty()) {
			return getProductType().name();
		} else {
			return getConfig().getScreenName();
		}
	}

	protected void setDataPanel(PartDataPanel dataPanel) {
		this.dataPanel = dataPanel;
	}

	protected void setConfig(ProductRecoveryConfig config) {
		this.config = config;
	}

	protected ProductType getProductType() {
		return productType;
	}

	protected PartDataPanel getDataPanel(ProductRecoveryConfig config) {
		if (config == null) {
			return null;
		}
		PartDataPanel panel = getDataPanels().get(config.getConfigId());
		if (panel == null) {
			panel = createDataPanel(config);
		}
		return panel;
	}

	// === state control === //
	public void setIdleMode() {
		getController().getMainWindow().clearMessage();
		getController().getMainWindow().clearStatusMessage();
		getDataPanel().setIdleMode();
		getProductPanel().setIdleMode();
		setIdle(true);
	}

	public void setInputMode(ActionEvent actionEvent) {
		setIdle(false);
		getProductPanel().setInputMode(actionEvent);
		getDataPanel().setInputMode();
		//		repaint();
	}

	public void resetDataPanelElements() {
		if (getRecoveryConfigs() != null && !getRecoveryConfigs().isEmpty()) {
			PartDataPanel currentPanel = getDataPanel();
			ProductRecoveryConfig config = getRecoveryConfigs().get(getProduct().getModelCode());
			if (config == null) {
				config = getRecoveryConfigs().get("*");
			}
			PartDataPanel pdp = getDataPanel(config);
			if (currentPanel != pdp) {
				setConfig(config);
				setDataPanel(pdp);
				currentPanel.setIdleMode();
				getDataPanelContainer().add(getDataPanel());
			}
		}
	}

	protected MigPane getDataPanelContainer() {
		return dataPanelContainer;
	}

	@Override
	public void handle(ActionEvent event) {

	}


}
