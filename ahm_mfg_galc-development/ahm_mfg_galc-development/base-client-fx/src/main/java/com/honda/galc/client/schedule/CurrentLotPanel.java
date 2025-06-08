package com.honda.galc.client.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.PreProductionLot;

/**
 * 
 * <h3>CurrentLotPanel Class description</h3>
 * <p> Current Lot Panel description </p>
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
 * <TD>Janak Bhalla & Alok Ghode</TD>
 * <TD>March 05, 2015</TD>
 * <TD>1.0</TD>
 * <TD>GY 20150305</TD>
 * <TD>Initial Release</TD>
 * </TABLE>
 *  
 */
public class CurrentLotPanel extends BorderPane {

	private Label productionLotLabel = UiFactory.createLabel("productionLotLabel", "Production Lot");

	private TextField positionTxtField = UiFactory.createTextField("positionTxtField");

	private Label specLabel = UiFactory.createLabel("specLabel", "Spec");

	private TextField productionLotTxtField = UiFactory.createTextField("productionLotTxtField");

	private TextField specTxtField = UiFactory.createTextField("specTxtField");

	private Label positionLabel = UiFactory.createLabel("positionLabel", "Position");

	private ObjectTablePane<MultiValueObject<PreProductionLot>> currentLotTblPane;
	private boolean isMoveByKdLot;
	private boolean isProcessProduct;
	private boolean isShowProductLot;
	private boolean isChangeLotSize;
	private MultiValueObject<PreProductionLot> currentLot;
	private ScheduleClientController controller;
	private ExpectedProductPanel productPane;

	public CurrentLotPanel(ScheduleClientController controller) {
		Map<String, Object> properties = controller.getCurrentPanelProperties();
		this.controller = controller;
		this.isMoveByKdLot = (Boolean) properties
				.get(DefaultScheduleClientProperty.IS_MOVE_BY_KD_LOT);
		isChangeLotSize = (Boolean) properties
				.get(DefaultScheduleClientProperty.CHANGE_LOT_SIZE);
		EventBusUtil.register(this);
		initializePanels(properties);
	}

	public MultiValueObject<PreProductionLot> getCurrentLot() {
		return currentLot;
	}

	public void setCurrentLot(MultiValueObject<PreProductionLot> currentLot) {
		this.currentLot = currentLot;
	}

	public List<PreProductionLot> getCurrentLots() {
		List<PreProductionLot> lots = new ArrayList<PreProductionLot>();

		if (this.isMoveByKdLot) {
			for (MultiValueObject<PreProductionLot> lot : currentLotTblPane
					.getTable().getItems()) {
				lots.add(lot.getKeyObject());
			}
		} else {
			if (currentLot != null)
				lots.add(currentLot.getKeyObject());
		}
		return lots;
	}

	public void lotInfoChanged(List<MultiValueObject<PreProductionLot>> lots) {
		populateLotInformation(lots.size() == 0 ? null : lots.get(0));

		if (currentLotTblPane != null)
			currentLotTblPane.setData(lots);
	}

	public void populateLotInformation(MultiValueObject<PreProductionLot> lot) {
		currentLot = lot;
		if (lot != null) {
			if (!isShowProductLot) {
				// show "Starting VIN" in the current lot panel
				productionLotTxtField.setText(lot.getKeyObject()
						.getStartProductId());
			} else {
				productionLotTxtField.setText(lot.getKeyObject()
						.getProductionLot());
			}
			specTxtField.setText(lot.getKeyObject().getProductSpecCode());
			if (isChangeLotSize()) {
				positionTxtField.setText((lot.getKeyObject().getLotPosition()));
			} else {
				// position for 2SD product is shown as "stampedCount/lotSize"
				String lotPosition = "" + lot.getKeyObject().getStampedCount()
						+ "/" + lot.getKeyObject().getLotSize();
				positionTxtField.setText(lotPosition);
			}
		}
	}
	@Subscribe
	@SuppressWarnings("unchecked")
	public void onEvent(SchedulingEvent event) {

		if (event.getEventType() == SchedulingEventType.CURRENT_ORDER_CHANGED) {
			if (!(event.getTargetObject() instanceof List))
				return;
			final List<MultiValueObject<PreProductionLot>> preProductionLots = (List<MultiValueObject<PreProductionLot>>) event
					.getTargetObject();
			Platform.runLater(new Runnable() {
				public void run() {
					lotInfoChanged(preProductionLots);
				}
			});
		} else if (event.getEventType() == SchedulingEventType.EXPECTED_PRODUCT_CHANGED) {
			if (isProcessProduct && productPane != null) {
				BaseProduct product = (BaseProduct) event.getTargetObject();
				if (product != null) {
					productPane.setExpectedProduct(product);
					productPane.populateData();
				}
			}
		}
	}

	public ObjectTablePane<MultiValueObject<PreProductionLot>> getLotPanel() {
		return currentLotTblPane;
	}

	public void setLotPanel(
			ObjectTablePane<MultiValueObject<PreProductionLot>> lotPanel) {
		this.currentLotTblPane = lotPanel;
	}

	public boolean isShowProductLot() {
		return isShowProductLot;
	}

	public boolean isChangeLotSize() {
		return isChangeLotSize;
	}

	private void initializePanels(Map<String, Object> properties) {
		setTop(createCurrentProductPanel());
		currentLotTblPane = createTableView(properties);
		setCenter(currentLotTblPane);

		isProcessProduct = (Boolean) properties
				.get(DefaultScheduleClientProperty.PROCESS_PRODUCT);
		productPane = createProductPane(properties);
		if (isProcessProduct)
			setBottom(productPane);
		isShowProductLot = (Boolean) properties
				.get(DefaultScheduleClientProperty.SHOW_CURRENT_PRODUCT_LOT);
		if (!isShowProductLot)
			productionLotLabel.setText("Starting VIN");
	}

	private HBox createCurrentProductPanel() {
		HBox hBox = new HBox();
		hBox.setPadding(new Insets(15, 12, 15, 12));
		hBox.setSpacing(10);
		HBox.setHgrow(productionLotTxtField, Priority.ALWAYS);
		HBox.setHgrow(specTxtField, Priority.ALWAYS);
		HBox.setHgrow(positionTxtField, Priority.SOMETIMES);
		initTextField(productionLotTxtField);
		initTextField(specTxtField);
		initTextField(positionTxtField);
		initLabel(productionLotLabel);
		initLabel(specLabel);
		initLabel(positionLabel);
		hBox.setAlignment(Pos.CENTER);
		hBox.getChildren().addAll(productionLotLabel, productionLotTxtField,
				specLabel, specTxtField, positionLabel, positionTxtField);
		return hBox;
	}

	private void initTextField(TextField comp) {
		comp.setFont(Fonts.DIALOG_BOLD_20);
		comp.setEditable(false);
		comp.setStyle("-fx-background-color: #00ff00;");
	}

	private void initLabel(Label comp) {
		comp.setFont(Fonts.DIALOG_BOLD_16);
	}

	private ScheduleClientTable<PreProductionLot> createTableView(
			Map<String, Object> properties) {
		ScheduleClientTable<PreProductionLot> pane = new ScheduleClientTable<PreProductionLot>(
				controller, properties);
		pane.getTable().getSelectionModel()
				.setSelectionMode(SelectionMode.MULTIPLE);
		return pane;
	}

	private ExpectedProductPanel createProductPane(
			Map<String, Object> properties) {
		ExpectedProductPanel productPane = new ExpectedProductPanel(
				(Integer) properties.get(TagNames.RESET.name()));
		Boolean checkDuplicateProductId = (Boolean)properties.get(DefaultScheduleClientProperty.CHECK_DUPLICATE_EXPECTED_PRODUCT_ID);
		checkDuplicateProductId = checkDuplicateProductId == null ? Boolean.FALSE: checkDuplicateProductId;
		productPane.setCheckDuplicateProductId(checkDuplicateProductId);
		return productPane;
	}
	
	public ExpectedProductPanel getProductPane() {
		return productPane;
	}

}
