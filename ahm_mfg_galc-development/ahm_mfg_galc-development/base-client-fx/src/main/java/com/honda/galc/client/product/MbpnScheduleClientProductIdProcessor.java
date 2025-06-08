package com.honda.galc.client.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.schedule.SchedulingEvent;
import com.honda.galc.client.schedule.SchedulingEventType;
import com.honda.galc.client.schedule.mbpn.ScheduleClientPropertyBean;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.conf.MCProductPddaPlatformDao;
import com.honda.galc.dao.conf.MCProductStructureDao;
import com.honda.galc.dao.conf.MCViosMasterPlatformDao;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.conf.MCProductPddaPlatform;
import com.honda.galc.entity.conf.MCProductStructure;
import com.honda.galc.entity.enumtype.DeviceDataType;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.PddaPlatformUtil;
import com.honda.galc.vios.dto.PddaPlatformDto;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class MbpnScheduleClientProductIdProcessor extends VIOSProductIdProcessor {
	
	private PddaPlatformDto pddaPlatformDto;
	
	public MbpnScheduleClientProductIdProcessor(ProductController productIdController) {
		super(productIdController);
	}

	public void processInputNumber(ProductEvent event) {

		Logger.getLogger().debug("Schedule Client VIOS Product ID Processor: "+event);
		if(event!=null) {
			TextField inputTextField = getProductController().getView().getInputPane().getProductIdField();
			String inputNumber = inputTextField.getText();
			//Get PDDA Platform from user
			try {
				pddaPlatformDto = getPlatformDetails(inputNumber);
			} catch (Exception e) {
				//Exception caught to stop the process
				return;
			}
			Map<String, Object> dataContainer = new HashMap<String, Object>();
			dataContainer.put(TagNames.PRODUCT_ID.name(), inputNumber);
			if(!PddaPlatformUtil.isBlankPddaPlatform(pddaPlatformDto)) {
				dataContainer.put(TagNames.PDDA_PLATFORM.name(), pddaPlatformDto);
			}
			EventBusUtil.publishAndWait(new SchedulingEvent(dataContainer, SchedulingEventType.PROCESS_PRODUCT_WITH_PLATFORM));
		}
		else {
			processViosProductId();
		}
	}

	private PddaPlatformDto getPlatformDetails(String productId) throws Exception {
		boolean isDialogEnabled = PropertyService.getPropertyBean(ScheduleClientPropertyBean.class, getProductController().getProcessPointId()).isPddaPlatformDialogEnabled();
		if(isDialogEnabled) {
			//Check whether PDDA platform details already exist
			MCProductPddaPlatform platform = ServiceFactory.getDao(MCProductPddaPlatformDao.class).findByKey(productId);
			if(platform != null) {
				//PDDA platform found! Now, check if structure revision is created or not
				List<MCProductStructure> prodStructureList = ServiceFactory.getDao(MCProductStructureDao.class).findAllByProductIdAndDivisionId(
						productId, getProductController().getModel().getDivisionId());
				if(!prodStructureList.isEmpty()) {
					//Valid structure revision found! Return PDDA Platform
					return platform.convertToPddaPlatformDto();
				}
			}
			//Get PDDA Platform details from user
			return getPlatformDetailsFromDialog(productId);
		}
		return null;
	}

	private PddaPlatformDto getPlatformDetailsFromDialog(String productId) throws Exception {
		boolean isProductIdSet = false;
		boolean isPlatformSet = false;
		Device device = ServiceFactory.getDao(DeviceDao.class).findByKey(getProductController().getProcessPointId());
		List<DeviceFormat> deviceFormatList = device.getDeviceDataFormats();
		
		for(DeviceFormat format : deviceFormatList) {
			if(format.getTag().equals(TagNames.PRODUCT_ID.name())
					&& format.getTagType() == DeviceTagType.TAG.getId()
					&& format.getDataType() == DeviceDataType.STRING.getId()) {
				isProductIdSet = true;
			} else if(format.getTag().equals(TagNames.PDDA_PLATFORM.name())
					&& format.getTagType() == DeviceTagType.TAG.getId()
					&& format.getDataType() == DeviceDataType.STRING.getId()) {
				isPlatformSet = true;
			}
		}
		if(!(isProductIdSet && isPlatformSet)) {
			MessageDialog.showError(ClientMainFx.getInstance().getStage(), "Device Format Configuartion is incomplete. Please contact IS.");
			//Throw an Exception to stop process
			throw new Exception();
		}
		
		SelectModelYearDialog selectModelYearDialog = new SelectModelYearDialog();
		boolean isConfirmed = selectModelYearDialog.confirm();
		if(isConfirmed) {
			PddaPlatformDto selectedItem = selectModelYearDialog.getSelectedItem();
			MCProductPddaPlatform platform = ServiceFactory.getDao(MCProductPddaPlatformDao.class).findByKey(productId);
			if(platform != null) {
				//In case if wrong PDDA platform is selected and structure hasn't got created, 
				//save updated PDDA platform details provided by user
				List<MCProductStructure> prodStructureList = ServiceFactory.getDao(MCProductStructureDao.class).findAllByProductIdAndDivisionId(
						productId, getProductController().getModel().getDivisionId());
				if(prodStructureList.isEmpty()) {
					MCProductPddaPlatform productPddaPlatform = new MCProductPddaPlatform(productId, selectedItem);
					ServiceFactory.getDao(MCProductPddaPlatformDao.class).save(productPddaPlatform);
				}
			}
			return selectedItem;
		}
		return null;
	}

	private class SelectModelYearDialog extends FxDialog{

		private static final String ERROR_MSG = "Please Select Platform";
		private static final String OK = "Ok";

		private LoggedButton okBtn;
		private ObjectTablePane<PddaPlatformDto> modelYearTablePane;
		private LoggedLabel errorMessageLabel;
		private String buttonClickedName;
		private PddaPlatformDto selectedItem;

		public SelectModelYearDialog() {
			super("Select Platform", ClientMainFx.getInstance().getStage(getProductController().getView().getApplicationId()));
			this.initStyle(StageStyle.DECORATED);
			initComponents();
		}

		public boolean confirm()
		{
			loadData();
			showDialog();
			modelYearTablePane.getTable().getSelectionModel().selectFirst();
			okBtn.requestFocus();
			return (buttonClickedName.equalsIgnoreCase(OK) && selectedItem != null);
		}

		private void initComponents(){

			VBox mainContainer = new VBox();

			modelYearTablePane = createModelYearTablePane();
			okBtn = createBtn(OK);
			errorMessageLabel = UiFactory.createLabel("errorMessageLabel", ERROR_MSG);
			errorMessageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px; -fx-font-weight: bold;");
			errorMessageLabel.setVisible(false);

			HBox buttonContainer = new HBox();
			buttonContainer.getChildren().addAll(okBtn);
			buttonContainer.setAlignment(Pos.CENTER);
			buttonContainer.setPadding(new Insets(5));
			buttonContainer.setSpacing(10);

			HBox errorMessageContainer = new HBox();
			errorMessageContainer.getChildren().add(errorMessageLabel);
			errorMessageContainer.setAlignment(Pos.CENTER);
			errorMessageContainer.setPadding(new Insets(5));

			mainContainer.getChildren().addAll(modelYearTablePane, buttonContainer, errorMessageContainer);
			mainContainer.setAlignment(Pos.CENTER);
			((BorderPane) this.getScene().getRoot()).setCenter(mainContainer);
		}

		private void loadData() {
			String plantLocCode = StringUtils.trimToEmpty(PropertyService.getPropertyBean(SystemPropertyBean.class).getPlantLocCode());
			String deptCode = StringUtils.trimToEmpty(PropertyService.getPropertyBean(ScheduleClientPropertyBean.class, getProductController().getProcessPointId()).getPddaDeptCode());
			List<PddaPlatformDto> platformList = ServiceFactory.getDao(MCViosMasterPlatformDao.class).findAllPlatformsByPlantLocCodeAndDeptCode(plantLocCode, deptCode);
			modelYearTablePane.setData(platformList);
			if(pddaPlatformDto != null && modelYearTablePane.getTable().getItems().contains(pddaPlatformDto)) {
				modelYearTablePane.getTable().getSelectionModel().select(pddaPlatformDto);
			}
		}

		private LoggedButton createBtn(String text) {
			LoggedButton btn = UiFactory.createButton(text, text);
			btn.defaultButtonProperty().bind(btn.focusedProperty());
			btn.setOnAction(BUTTON_CLICK_EVENT_HANDLER);
			btn.getStyleClass().add("popup-btn");
			return btn;
		}

		private final EventHandler<ActionEvent> BUTTON_CLICK_EVENT_HANDLER = new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				Object source = event.getSource();
				if (source instanceof LoggedButton) {
					LoggedButton btn = (LoggedButton) source;
					buttonClickedName = btn.getText();
					if(modelYearTablePane.getSelectedItem() == null && buttonClickedName.equals(OK)) {
						errorMessageLabel.setVisible(true);
					} else {
						selectedItem = modelYearTablePane.getSelectedItem();
						errorMessageLabel.setVisible(false);
						Stage stage = (Stage) btn.getScene().getWindow();
						stage.close();
					}
				}
			}
		};

		private ObjectTablePane<PddaPlatformDto> createModelYearTablePane(){
			ColumnMappingList columnMappingList = 
					ColumnMappingList.with("Model Year Date", "modelYearDate")
					.put("PROD SCH QTY", "prodSchQty")
					.put("PROD ASM LINE NO", "prodAsmLineNo")
					.put("Vehicle Model Code","vehicleModelCode");

			Double[] columnWidth = new Double[] {0.10,0.10,0.10,0.10,0.10,0.10};
			final ObjectTablePane<PddaPlatformDto> panel = new ObjectTablePane<PddaPlatformDto>(columnMappingList,columnWidth);
			return panel;
		}

		public PddaPlatformDto getSelectedItem() {
			return selectedItem;
		}
	}
}
