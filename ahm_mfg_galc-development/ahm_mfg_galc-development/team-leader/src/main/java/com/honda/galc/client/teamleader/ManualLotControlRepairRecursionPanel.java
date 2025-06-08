package com.honda.galc.client.teamleader;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JTabbedPane;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.teamleader.property.ManualLotControlRepairPropertyBean;
import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.ProductPanel;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.service.property.PropertyService;

public class ManualLotControlRepairRecursionPanel extends ApplicationMainPanel
		implements ActionListener {
	private static final long serialVersionUID = 1L;
	private ProductPanel productIdPanel;
	private ManualLotControlRepairPropertyBean property;
	private ProductTypeData productTypeData;
	private JTabbedPane pane;
	private List<String> panelIds;
	String engineSerialNumber;

	public ManualLotControlRepairRecursionPanel(MainWindow mainWin) {
		super(mainWin);
		initialize();
		initComponents();
		initConnections();
		AnnotationProcessor.process(this);
	}

	public ProductPanel getProductIdPanel() {
		if (productIdPanel == null) {
			productIdPanel = new ProductPanel(window, getProductTypeData());
			productIdPanel.getProductSpecField().setVisible(false);
			productIdPanel.getProductSpecLabel().setVisible(false);
			boolean isRemoveIEnabled = PropertyService
					.getPropertyBean(ProductPropertyBean.class, window.getApplication().getApplicationId())
					.isRemoveIEnabled();
			int numExtraChar = (isRemoveIEnabled && productIdPanel.getProductType().equals(ProductType.FRAME)) ? 1 : 0;
			productIdPanel.getProductIdField()
					.setMaximumLength((property.isProductIdCheckDisabled() ? property.getMaxProductSnLength()
							: productIdPanel.getMaxProductIdLength()) + numExtraChar);

		}
		return productIdPanel;

	}

	public UpperCaseFieldBean getProductIdField() {
		return getProductIdPanel().getProductIdField();

	}

	public ProductTypeData getProductTypeData() {
		if (productTypeData == null) {
			for (ProductTypeData type : window.getApplicationContext()
					.getProductTypeDataList()) {
				if (type.getProductTypeName().equals(property.getProductType())) {
					productTypeData = type;
					break;
				}
			}
		}

		return productTypeData;
	}

	private void initialize() {

		try {
			property = PropertyService.getPropertyBean(
					ManualLotControlRepairPropertyBean.class, window
							.getApplication().getApplicationId());

			createTabbedPane();

		} catch (Exception e) {
			e.printStackTrace();
			Logger.getLogger().error(e,
					"Exception to start ManualLotControlRepairView");
		}
	}

	public void initConnections() {
		getProductIdField().addActionListener(this);
	}

	public void initComponents() {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(getProductIdPanel());
		add(getPane());

	}

	private void createTabbedPane() {
			pane = new JTabbedPane();
			ManualLotControlRepairPanel manualLotControlRepairPanel = new ManualLotControlRepairPanel(
					window, ProductType.getType(property.getProductType()));
			pane.addTab(property.getProductType(), manualLotControlRepairPanel);
			panelIds = getSubProductTypes();
				for (int i = 0; i < panelIds.size(); i++) {
					manualLotControlRepairPanel = new ManualLotControlRepairPanel(
							window, ProductType.getType(panelIds.get(i)));
					pane.addTab(panelIds.get(i), manualLotControlRepairPanel);
				}
				
		}
		
		
	public JTabbedPane getPane() {
		return pane;
	}

	public List<String> getSubProductTypes() {
		String subProductTypes = PropertyService.getProperty(ApplicationContext
				.getInstance().getApplicationId(), "SUB_PRODUCT_TYPES", "");
		List<String> panelIds = new ArrayList<String>();
		if(!StringUtils.isEmpty(subProductTypes))
			for (String name : subProductTypes.split(Delimiter.COMMA)) {
				panelIds.add(name.trim());
			}
		return panelIds;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getProductIdPanel().getProductIdField()) {

			getProductIdPanel().getProductLookupButton().setEnabled(false);
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			ProductProcessEvent productProcessEvent = new ProductProcessEvent(
					getProductIdField().getText(), ProductTypeCatalog.getProductType(property
							.getProductType()), ProductProcessEvent.State.LOAD);
			EventBus.publish(productProcessEvent);
		}

	}
	
	@EventSubscriber()
	public void onProductProcessEvent(ProductProcessEvent event) {
		if (event.getState().equals(ProductProcessEvent.State.COMPLETE)) {
			getProductIdPanel().getProductLookupButton().setEnabled(true);
			getProductIdPanel().refresh();
			getProductIdField().requestFocus();
		} else if (event.getState().equals(ProductProcessEvent.State.ERROR)) {
			getProductIdField().setStatus(false);
		} else if (event.getState().equals(ProductProcessEvent.State.VALID_PRODUCT)) {
			getProductIdField().setStatus(true);
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}

	}

}
