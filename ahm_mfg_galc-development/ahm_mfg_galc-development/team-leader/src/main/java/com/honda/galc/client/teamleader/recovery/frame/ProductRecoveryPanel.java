package com.honda.galc.client.teamleader.recovery.frame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProduct;

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
 * <TD>Karol Wozniak</TD>
 * <TD>Jul 11, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class ProductRecoveryPanel extends ApplicationMainPanel {

	private static final long serialVersionUID = 1L;
	private JLabel headerLabel;
	private ProductPanel productPanel;
	private PartDataPanel dataPanel;

	private boolean idle = true;
	private ProductType productType;
	private ProductRecoveryConfig config;
	private Map<String, ProductRecoveryConfig> recoveryConfigs;
	private Map<String, PartDataPanel> dataPanels;

	private JPanel dataPanelContainer;
	private DataRecoveryController controller;

	public ProductRecoveryPanel(MainWindow frame, ProductType productType, ProductRecoveryConfig config) {
		super(frame);
		this.productType = productType;
		initialize(frame, config);
	}

	public ProductRecoveryPanel(MainWindow frame, ProductType productType, List<ProductRecoveryConfig> configs) {
		super(frame);
		this.productType = productType;
		this.dataPanels = new HashMap<String, PartDataPanel>();
		this.recoveryConfigs = new HashMap<String, ProductRecoveryConfig>();
		initialize(frame, configs);
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
		headerLabel = createHeaderLabel();
		productPanel = createProductInfoPanel();
		dataPanel = createDataPanel(config);

		setLayout(new MigLayout("insets 0 5 0 5", "[max, fill]"));
		setSize(1025, 625);
		add(headerLabel, "wrap");
		add(productPanel, "height 180!, wrap");

		this.dataPanelContainer = new JPanel(new MigLayout("insets 0 0 0 0", "[990!]"));
		JScrollPane panel = new JScrollPane();
		panel.setViewportView(getDataPanelContainer());
		panel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		add(panel, "height max");
		getDataPanelContainer().add(getDataPanel());
	}

	// === ui elements factory methods === //
	protected JLabel createHeaderLabel() {
		JLabel headerLabel = new JLabel(getHeaderLabelTxt(), JLabel.CENTER);
		headerLabel.setFont(Utils.getInputFont());
		return headerLabel;
	}

	protected ProductPanel createProductInfoPanel() {
		ProductPanel panel = new ProductPanel(this);
		panel.setBorder(BorderFactory.createEtchedBorder());
		return panel;
	}

	protected PartDataPanel createDataPanel(ProductRecoveryConfig config) {
		PartDataPanel panel = ObjectFactory.createProductDataPanel(config, getController());
		panel.setEnabled(false);
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

	protected JLabel getHeaderLabel() {
		return headerLabel;
	}

	public DataRecoveryController getController() {
		return controller;
	}

	public void setController(DataRecoveryController controller) {
		this.controller = controller;
	}

	protected BaseProduct getProduct() {
		return getController().getProduct();
	}

	public boolean isIdle() {
		return idle;
	}

	public void setIdle(boolean idle) {
		this.idle = idle;
	}

	protected JPanel getDataPanelContainer() {
		return dataPanelContainer;
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
		repaint();
	}

	public void setInputMode() {
		setIdle(false);
		getProductPanel().setInputMode();
		getDataPanel().setInputMode();
		repaint();
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
				getDataPanelContainer().remove(currentPanel);
				getDataPanelContainer().add(getDataPanel());
			}
		}
	}
}
