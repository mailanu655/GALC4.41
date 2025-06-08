package com.honda.galc.client.schedule;

import static com.honda.galc.service.ServiceFactory.getService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.ui.LoginDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.product.ProductHistoryDao;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.enumtype.ProductStampingSendStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.on.WeldOnService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.service.vinstamp.VinStampingService;
import com.honda.galc.util.SortedArrayList;

/**
 * 
 * <h3>Last LotSelection Screen </h3> <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * LastLotSelectionScreen description
 * </p>
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
 * <TD>Janak Bhalla & Alok Ghode</TD>
 * <TD>March 05, 2015</TD>
 * <TD>1.0</TD>
 * <TD>GY 20150305</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 */
public class LastProductSelectionDialog extends FxDialog implements
EventHandler<javafx.event.ActionEvent> {
	private ScheduleClientTable<Object> productTablePanel;
	@FXML
	private Button cancelButton;

	@FXML
	private HBox upperButtonPanel;

	@FXML
	private HBox lowerButtonPanel;

	@FXML
	private GridPane mainGridPane;

	@FXML
	private Button sendPlcButton;

	@FXML
	private Button sendOnButton;

	@FXML
	private Button changeSentButton;

	private ScheduleMainPanel schedulePanel;

	@SuppressWarnings("rawtypes")
	private ProductHistoryDao productHistoryDao;
	private ProductStampingSequenceDao productStampingSequenceDao;
	private Map<String, Object> lastProductSelectionProperties;

	private final ScheduleClientProperty property;
	private final ProductType productType;
	private final List<String> exceptionOnProcessPointModels;

	private static final String ON = "ON";

	public LastProductSelectionDialog(ScheduleMainPanel schedulePanel) {
		super("Last Lot Selection", ClientMainFx.getInstance().getStage(), true);
		this.schedulePanel = schedulePanel;
		this.property = schedulePanel.getController().getProperties();
		this.productType = ProductType.valueOf(property.getProductType());
		this.lastProductSelectionProperties = schedulePanel.getController().getLastproductSelectionPanelProperties();
		this.exceptionOnProcessPointModels = initExceptionOnProcessPointModels();
		initialize();
	}

	@SuppressWarnings("rawtypes")
	protected ProductHistoryDao getProductHistoryDao() {
		if (this.productHistoryDao == null) {
			this.productHistoryDao = ProductTypeUtil.getProductHistoryDao(getProductType());
		}
		return this.productHistoryDao;
	}

	protected ProductStampingSequenceDao getProductStampingSequenceDao() {
		if (this.productStampingSequenceDao == null) {
			this.productStampingSequenceDao = ServiceFactory.getDao(ProductStampingSequenceDao.class);
		}
		return this.productStampingSequenceDao;
	}

	private void initialize() {
		initcomponents();
		sendPlcButton.setOnAction(this);
		sendOnButton.setOnAction(this);
		changeSentButton.setOnAction(this);
		cancelButton.setOnAction(this);
	}

	private void initcomponents() {
		if (!ProductType.FRAME.equals(getProductType())) {
			mainGridPane.getChildren().remove(upperButtonPanel);
			GridPane.setRowIndex(lowerButtonPanel, 8);
			mainGridPane.setPrefHeight(570.0);
		} else {
			GridPane.setMargin(upperButtonPanel, new Insets(0,55,0,55));
		}
		if (!property.isAllowSendToPlc()) {
			lowerButtonPanel.getChildren().remove(sendPlcButton);
			GridPane.setMargin(lowerButtonPanel, new Insets(0,155,0,155));
		} else {
			GridPane.setMargin(lowerButtonPanel, new Insets(0,55,0,55));
		}
		mainGridPane.add(getProductTablePanel(), 0, 0, 3, 8);
		GridPane.setMargin(productTablePanel, new Insets(35));
	}

	private List<String> initExceptionOnProcessPointModels() {
		if (StringUtils.isEmpty(getProperty().getExceptionOnProcessPoint())) {
			return Collections.emptyList();
		} else {
			//return Arrays.asList(getProperty().getExceptionOnProcessPointModels());
			List<String> result = new ArrayList<String>();
			List<com.honda.galc.entity.conf.ComponentProperty> eoppms = PropertyService.getProperties(schedulePanel.getProcessPointId(), "EXCEPTION_ON_PROCESS_POINT_MODEL\\([0-9]+\\)");
			for (com.honda.galc.entity.conf.ComponentProperty eoppm : eoppms) {
				result.add(eoppm.getPropertyValue());
			}
			return result;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadProductData(List<MultiValueObject<PreProductionLot>> selectedItems) {
		List<MultiValueObject<?>> items = prepareProductData(selectedItems);
		getProductTablePanel().getTable().getItems().clear();
		if (items != null && !items.isEmpty()) {
			for(MultiValueObject item: items) {
				getProductTablePanel().getTable().getItems().add(item);
			}
		}
		getProductTablePanel().clearSelection();
	}

	@SuppressWarnings("unchecked")
	private List<MultiValueObject<?>> prepareProductData(List<MultiValueObject<PreProductionLot>> selectedItems) {
		List<MultiValueObject<?>> productIdData = new ArrayList<MultiValueObject<?>>();
		for (MultiValueObject<PreProductionLot> lot : selectedItems) {
			List<? extends BaseProduct> allInLot = ProductTypeUtil.getProductDao(productType).findAllByProductionLot(lot.getKeyObject().getProductionLot());
			productIdData.addAll((productType == ProductType.KNUCKLE) ? sortKnuckles((List<SubProduct>) allInLot) : sortProducts((List<BaseProduct>) allInLot));
		}
		return productIdData;
	}

	private List<MultiValueObject<?>> sortKnuckles(List<SubProduct> allInLot) {
		List<String> leftKnuckles = new ArrayList<String>();
		List<String> rightKnuckles = new ArrayList<String>();
		List<MultiValueObject<?>> sortedList = new ArrayList<MultiValueObject<?>>();

		for (SubProduct subProd : allInLot) {
			if (subProd.getSubId().equals(SubProduct.SUB_ID_LEFT))
				leftKnuckles.add(subProd.getProductId());
			else
				rightKnuckles.add(subProd.getProductId());
		}

		Collections.sort(leftKnuckles);
		Collections.sort(rightKnuckles);

		int count = Math.max(leftKnuckles.size(), rightKnuckles.size());
		for (int i = 0; i < count; i++) {
			try {
				String left = leftKnuckles.get(i);
				String right = rightKnuckles.get(i);
				sortedList.add(new MultiValueObject<BaseProduct>(StringUtils.isEmpty(left) ? "" : left, StringUtils.isEmpty(right) ? "" : right));
			} catch (Exception e) {
				getLogger().error("Exception to find Knuckle on index:" + i);
			}
		}
		return sortedList;
	}

	private List<MultiValueObject<?>> sortProducts(List<BaseProduct> allInLot) {
		if (allInLot != null && !allInLot.isEmpty()) {
			List<ProductStampingSequence> productStampingSequences = getProductStampingSequencesForLot(allInLot);
			if (productStampingSequences != null) {
				return sortProductsByStampingSequenceNumber(productStampingSequences);
			} else {
				String model = getModelForBaseProduct(allInLot.get(0));
				return sortProductsByProductId(allInLot, model);
			}
		}
		return new ArrayList<MultiValueObject<?>>();
	}

	private List<MultiValueObject<?>> sortProductsByProductId(List<BaseProduct> allInLot, String model) {
		List<MultiValueObject<?>> sortedList = new ArrayList<MultiValueObject<?>>();
		SortedArrayList<BaseProduct> prodList = new SortedArrayList<BaseProduct>("getProductId");
		prodList.addAll(allInLot);
		String onProcessPointId = exceptionOnProcessPointModels.contains(model) ? getProperty().getExceptionOnProcessPoint() : getProperty().getOnProcessPoint();
		for (BaseProduct prod : prodList)
			sortedList.add(new MultiValueObject<String>(prod.getProductId(), getProductHistoryDao().hasProductHistory(prod.getProductId(), onProcessPointId) ? ON : ""));
		return sortedList;
	}

	private List<MultiValueObject<?>> sortProductsByStampingSequenceNumber(List<ProductStampingSequence> productStampingSequences) {
		List<MultiValueObject<?>> sortedList = new ArrayList<MultiValueObject<?>>();
		Collections.sort(productStampingSequences, new Comparator<ProductStampingSequence>() {
			@Override
			public int compare(ProductStampingSequence productStampingSequence1, ProductStampingSequence productStampingSequence2) {
				return (productStampingSequence1.getStampingSequenceNumber() - productStampingSequence2.getStampingSequenceNumber());
			}
		});
		for (ProductStampingSequence productStampingSequence : productStampingSequences) {
			if (productStampingSequence.getSendStatus() == ProductStampingSendStatus.WAITING.getId()) {
				sortedList.add(new MultiValueObject<String>(productStampingSequence.getId().getProductID(), ProductStampingSendStatus.WAITING.name()));
			} else if (productStampingSequence.getSendStatus() == ProductStampingSendStatus.SENT.getId()) {
				sortedList.add(new MultiValueObject<String>(productStampingSequence.getId().getProductID(), ProductStampingSendStatus.SENT.name()));
			} else if (productStampingSequence.getSendStatus() == ProductStampingSendStatus.STAMPED.getId()) {
				sortedList.add(new MultiValueObject<String>(productStampingSequence.getId().getProductID(), ProductStampingSendStatus.STAMPED.name()));
			} else if (productStampingSequence.getSendStatus() == ProductStampingSendStatus.SHIPPED.getId()) {
				sortedList.add(new MultiValueObject<String>(productStampingSequence.getId().getProductID(), ProductStampingSendStatus.SHIPPED.name()));
			} else {
				sortedList.add(new MultiValueObject<String>(productStampingSequence.getId().getProductID(), "ERROR"));
			}
		}
		return sortedList;
	}

	private List<ProductStampingSequence> getProductStampingSequencesForLot(List<BaseProduct> allInLot) {
		List<ProductStampingSequence> productStampingSequences = new ArrayList<ProductStampingSequence>();
		for (BaseProduct product : allInLot) {
			ProductStampingSequence productStampingSequence = getProductStampingSequenceDao().findById(product.getProductionLot(),product.getProductId());
			if (productStampingSequence == null || productStampingSequence.getStampingSequenceNumber() == null) return null; // if a product does not have a stamping sequence number, return null (note that STAMPING_SEQUENCE_NO in GAL216TBX is nullable)
			productStampingSequences.add(productStampingSequence);
		}
		return productStampingSequences;
	}

	public ScheduleClientTable<Object> getProductTablePanel() {
		if (productTablePanel == null) {
			String[] heading = productType == ProductType.KNUCKLE ? new String[] { "Left Knuckle", "Right Knuckle" } : new String[] { "Product Id", "On Status" };
			lastProductSelectionProperties.put(DefaultScheduleClientProperty.COLUMN_HEADINGS, heading);
			productTablePanel = new ScheduleClientTableLastProductSelection<Object>(schedulePanel.getController(), lastProductSelectionProperties);
		}
		return productTablePanel;
	}

	private ObservableList<MultiValueObject<Object>> getProductTablePanelItems() {
		List<MultiValueObject<Object>> items = new ArrayList<MultiValueObject<Object>>();
		items.addAll(getProductTablePanel().getTable().getItems());
		return FXCollections.observableArrayList(items);
	}

	public Button getSendToOnButton(){
		return sendOnButton;
	}

	public Button getChangeToSentButton(){
		return changeSentButton;
	}

	private boolean login() {
		if (LoginDialog.login() != LoginStatus.OK)
			return false;
		if (!ClientMainFx.getInstance().getAccessControlManager().isAuthorized(property.getAuthorizationGroup())) {
			JOptionPane.showMessageDialog(null, "You have no access permission to execute this action.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	public static void sendToPlc(Stage mainWindow, String deviceId, List<MultiValueObject<BaseProduct>> products) {
		if (products == null || products.isEmpty())
			return;

		BaseProduct product = products.get(0).getKeyObject();
		if (product.getProductType().equals(ProductType.KNUCKLE)) {
			if (products.size() < 2) {
				MessageDialog.showError(mainWindow, "Please select both left and right knuckles");
				return;
			} else {
				SubProduct product1 = (SubProduct) products.get(0).getKeyObject();
				SubProduct product2 = (SubProduct) products.get(1).getKeyObject();
				String productIdLeft = null;
				String productIdRight = null;
				if (product1.getSubId().equals(SubProduct.SUB_ID_LEFT))
					productIdLeft = product1.getProductId();
				else
					productIdRight = product1.getProductId();
				if (product2.getSubId().equals(SubProduct.SUB_ID_RIGHT))
					productIdRight = product2.getProductId();
				else
					productIdLeft = product2.getProductId();
				if (productIdLeft == null || productIdRight == null) {
					MessageDialog.showError(mainWindow, "Please select both left and right knuckles");
					return;
				}
				sendToPlc(mainWindow, deviceId, ProductType.KNUCKLE.getProductName(), Arrays.asList(new Object[] { productIdLeft, productIdRight }));
			}
		} else {
			sendToPlc(mainWindow, deviceId, product.getProductType().getProductName(), Arrays.asList(new Object[] { product.getProductId() }));
		}
	}

	public static void sendToPlc(Stage mainWindow, String deviceId, String productTypeName, List<Object> productIds) {
		EiDevice eiDevice = DeviceManager.getInstance().getEiDevice();
		if (eiDevice == null) {
			Logger.getLogger().info("Failed to write Last Product to PLC because EI Device is not configured.");
			MessageDialog.showError(mainWindow, "Failed to write Last Product to PLC because EI Device is not configured.");
			return;
		}
		if (StringUtils.isEmpty(deviceId)) {
			MessageDialog.showError(mainWindow, "Error: configuration error - LAST_LOT_DEVICE_ID is not defined.");
			return;
		}
		if (!MessageDialog.confirm(mainWindow, getLotWriteInfo(productTypeName, productIds)))
			return;
		try {
			eiDevice.syncSend(deviceId, createData(deviceId, productIds));
			Logger.getLogger().info("Sent product ids " + productIds + " to PLC successfully");
			MessageDialog.showInfo(mainWindow, "Sent product ids " + productIds + " to PLC successfully");
		} catch (Exception e) {
			Logger.getLogger().error(e, "Failed to write to PLC:" + getLotWriteInfo(productTypeName, productIds));
			MessageDialog.showError(mainWindow, "Failed to write Last Product to PLC :" + e.toString());
		}
	}

	private static String getLotWriteInfo(String productTypeName, List<Object> productIds) {
		boolean isPair = productIds.size() >= 2;
		StringBuilder sb = new StringBuilder();
		sb.append("The ").append(productTypeName);
		sb.append(isPair ? " Pair " : " ");
		sb.append(productIds.get(0));
		if (isPair)
			sb.append(" & ").append(productIds.get(1));
		sb.append(isPair ? " will be sent to the PLC as the last pair processed." : " will be sent to the PLC as the last product processed.");
		return sb.toString();
	}

	protected void sendToOn() {
		MultiValueObject<?> item = getProductTablePanel().getTable().getSelectionModel().getSelectedItem();
		if (item == null || item.getValues() == null || item.getValues().isEmpty()) {
			MessageDialog.showError(this, "Please make a selection", "Send to ON");
			return;
		}
		String productId = (String) item.getValues().get(0);
		BaseProduct product = ProductTypeUtil.getProductDao(getProductType()).findByKey(productId);
		if (product == null) {
			MessageDialog.showError(this, "Product " + getProductType() + " does not exist for " + productId, "Send to ON");
			return;
		}
		String model = getModelForBaseProduct(product);
		String onProcessPointId, componentId;
		if (exceptionOnProcessPointModels.contains(model)) {
			onProcessPointId = getProperty().getExceptionOnProcessPoint();
			componentId = onProcessPointId;
		} else {
			onProcessPointId = getProperty().getOnProcessPoint();
			componentId = null;
		}
		if (StringUtils.isBlank(onProcessPointId)) {
			MessageDialog.showError(this, "ON Process Point is not defined.", "Send to ON");
			return;
		}

		List<?> historyList = ProductTypeUtil.getProductHistoryDao(getProductType()).findAllByProductAndProcessPoint(productId, onProcessPointId);
		final String msg;
		if (historyList != null && historyList.size() > 0) {
			msg = "Product already has an ON record. Are you sure you want to reprocess it ?";
		} else {
			msg = "Are you sure you want to send product to ON ?";
		}
		if (!MessageDialog.confirm(this, msg)) {
			return;
		}
		try {
			WeldOnService onService = getService(WeldOnService.class);
			onService.processProduct(product, onProcessPointId, componentId);
			item.setValue(1, ON);
		} catch (Exception e) {
			Logger.getLogger().error(e, "Failed to send product", productId, " to ON ", onProcessPointId);
			MessageDialog.showError(this, "Failed to send product to ON, " + e.toString());
		}
		refresh();
	}

	protected void changeToSent() {
		MultiValueObject<?> item = getProductTablePanel().getTable().getSelectionModel().getSelectedItem();
		if (item == null || item.getValues() == null || item.getValues().isEmpty()) {
			MessageDialog.showError(this, "Please make a selection", "Change to Sent");
			return;
		}
		String productId = (String) item.getValues().get(0);
		BaseProduct product = ProductTypeUtil.getProductDao(getProductType()).findByKey(productId);
		if (product == null) {
			MessageDialog.showError(this, "Product " + getProductType() + " does not exist for " + productId, "Change to Sent");
			return;
		}
		ProductStampingSequence productStampingSequence = getProductStampingSequenceDao().findById(product.getProductionLot(), product.getProductId());
		if (productStampingSequence == null) {
			MessageDialog.showError(this, "Product " + getProductType() + " does not have a stamping sequence for %s" + productId, "Change to Sent");
			return;
		}
		if (productStampingSequence.getSendStatus() != ProductStampingSendStatus.WAITING.getId()) {
			MessageDialog.showError(this, "Product is in " + ProductStampingSendStatus.getType(productStampingSequence.getSendStatus()).name() + " status. It cannot be changed to SENT status.", "Change to Sent");
			return;
		}

		String model = getModelForBaseProduct(product);
		String onProcessPointId, componentId;
		if (exceptionOnProcessPointModels.contains(model)) {
			onProcessPointId = getProperty().getExceptionOnProcessPoint();
			componentId = onProcessPointId;
		} else {
			onProcessPointId = getProperty().getOnProcessPoint();
			componentId = null;
		}
		if (StringUtils.isBlank(onProcessPointId)) {
			MessageDialog.showError(this, "ON Process Point is not defined.", "Change to Sent");
			return;
		}

		if (!MessageDialog.confirm(this, "Are you sure you want to change product to SENT status?")) {
			return;
		}
		try {
			VinStampingService vinStampingService = getService(VinStampingService.class);
			if (vinStampingService.updateStatusToSent(product.getProductionLot(), productId, componentId)) {
				item.setValue(1, ProductStampingSendStatus.SENT.name());
			} else {
				throw new RuntimeException("Failed to change product " + productId + " to SENT");
			}
		} catch (Exception e) {
			Logger.getLogger().error(e, "Failed to change product", productId, " to SENT ", onProcessPointId);
			MessageDialog.showError(this, "Failed to change product to SENT, " + e.toString());
		}
		refresh();
	}

	private static DataContainer createData(String deviceId, List<Object> productIds) {
		DataContainer data = new DefaultDataContainer();
		if (productIds.size() == 1) {
			data.put(TagNames.PRODUCT_ID.name(), productIds.get(0));
		} else {
			data.put(SubProduct.SUB_ID_LEFT + Delimiter.DOT + TagNames.PRODUCT_ID.name(), productIds.get(0));
			data.put(SubProduct.SUB_ID_RIGHT + Delimiter.DOT + TagNames.PRODUCT_ID.name(), productIds.get(1));
		}
		data.setClientID(deviceId);
		Logger.getLogger().info("write last product to ", deviceId + " : " + data.toString());
		return data;
	}

	private static String getModelForBaseProduct(BaseProduct product) {
		try {
			if (product != null) return product.getModelCode();
			return null;
		} catch (Exception e) {
			Logger.getLogger().error(e, "Unable to get model for product " + product.toString());
			return null;
		}
	}

	public SubProductDao getSubProductDao() {
		return ServiceFactory.getDao(SubProductDao.class);
	}

	public void open(List<MultiValueObject<PreProductionLot>> selectedItems) {
		loadProductData(selectedItems);
		this.showDialog();
	}

	protected ProductType getProductType() {
		return productType;
	}

	protected ScheduleClientProperty getProperty() {
		return property;
	}

	protected void refresh() {
		schedulePanel.getController().retrievePreProductionLots();
		ObservableList<MultiValueObject<Object>> items = getProductTablePanelItems();
		getProductTablePanel().getTable().getItems().clear();
		getProductTablePanel().setData(items);
	}

	@Override
	public void handle(javafx.event.ActionEvent e) {
		if (e.getSource() == sendPlcButton) {
			if (!login()) {
				return;
			}
			Logger.getLogger().info("User:" + ClientMainFx.getInstance().getAccessControlManager().getUserName() + " logged in to write:", getProductTablePanel().getTable().getSelectionModel().getSelectedItem().getValues().toString(), " to PLC.");
			if (getProductTablePanel().getTable().getSelectionModel().getSelectedItem() == null) {
				getLogger().warn("No Product seleted, can not sent to PLC.");
				return;
			}
			LastProductSelectionDialog.sendToPlc((Stage) this.getOwner(), property.getLastLotDeviceId(), property.getProductType(), getProductTablePanel().getTable().getSelectionModel().getSelectedItem().getValues());
		} else if (e.getSource() == sendOnButton) {
			if (property.isRequireLoginForSendToOn() && !login()) {
				return;
			}
			Logger.getLogger().info("User:" + ClientMainFx.getInstance().getAccessControlManager().getUserName() + " logged in to send ", getProductTablePanel().getTable().getSelectionModel().getSelectedItem().getValues().toString(), " to ON process point.");
			sendToOn();
		} else if (e.getSource() == changeSentButton) {
			if (property.isRequireLoginForChangeToSent() && !login()) {
				return;
			}
			Logger.getLogger().info("User:" + ClientMainFx.getInstance().getAccessControlManager().getUserName() + " logged in to change ", getProductTablePanel().getTable().getSelectionModel().getSelectedItem().getValues().toString(), " to SENT status.");
			changeToSent();
		} else if (e.getSource() == cancelButton) {
			this.close();
		}
	}

	public Logger getLogger() {
		return schedulePanel.getMainWindow().getLogger();
	}
}
