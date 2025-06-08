package com.honda.galc.client.dc.view;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.dc.enumtype.DataCollectionEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.dc.mvc.ISkipRejectInputView;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.ui.event.UnitNavigatorEvent;
import com.honda.galc.client.ui.event.UnitNavigatorEventType;
import com.honda.galc.client.utils.FxTransitionsUtil;
import com.honda.galc.client.utils.StyleUtil;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.client.utils.UiUtils;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.logging.TrainingDataCache;
import com.honda.galc.device.dataformat.DataCollectionIndexData;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.device.dataformat.Torque;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;

/**
 * @author Subu Kathiresan
 * @date Dec 19, 2014
 */
@SuppressWarnings("rawtypes")
public class DataCollectionViewUtil {
	
	public static String SKIP_IMAGE = "resource/com/honda/galc/client/dc/view/skip_next.png";
	public static String REJECT_IMAGE = "resource/com/honda/galc/client/dc/view/trash_can.png";
	public static String COLLECT_TORQUE_IMAGE = "resource/com/honda/galc/client/dc/view/torque.png";
	public static String COLLECT_SCAN_IMAGE = "resource/com/honda/galc/client/dc/view/scan.png";
	
	private Logger logger;
	private volatile MCOperationRevision operation = null;
	private volatile DataCollectionModel model = null;
	private volatile IDataCollectionWidget<?> view = null;
	private   ConcurrentHashMap<String, Boolean> processTrngFlagMap = new ConcurrentHashMap<String, Boolean>();
	public DataCollectionViewUtil(IDataCollectionWidget view, DataCollectionModel model) {
		this.view = view;
		this.operation = view.getOperation();
		this.model = model;
	}
	
	public void populateCollectedData(InstalledPart installedPart) {
		if (DataCollectionModel.hasScanPart(getOperation()) && !StringUtils.trimToEmpty(installedPart.getPartSerialNumber()).equals("")) {
			Color instPartColor = installedPart.getInstalledPartStatus() == InstalledPartStatus.OK ? Color.DEEPSKYBLUE : Color.RED;
			fillTextField(0, installedPart.getPartSerialNumber(), instPartColor);
		}
		for (Measurement measurement: installedPart.getMeasurements()) {
			int measurementSeq = measurement.getId().getMeasurementSequenceNumber();
			int index = DataCollectionModel.hasScanPart(getOperation()) ? measurementSeq : --measurementSeq;
			Color measColor = measurement.getMeasurementStatus() == MeasurementStatus.OK ? Color.DEEPSKYBLUE : Color.RED;
			fillTextField(index, Double.toString(measurement.getMeasurementValue()), measColor);
		}
		
		String operationName = operation.getId().getOperationName();
		ArrayList<Integer> skippedMeasurements = model.getSkippedMeasurementsMap().get(operationName);
		if (skippedMeasurements != null) {
			for (Integer skippedMeas: skippedMeasurements) {
				showSkippedIndput(skippedMeas, DataCollectionModel.hasScanPart(getOperation()));
			}
		}
		
	}
	

	private void fillTextField(int inputFieldIndex, String text, Color color) {
		if (inputFieldIndex < view.getInputFields().size()) {
			TextField txtField = (TextField) view.getInputFields().get(inputFieldIndex);
			txtField.setEditable(false);
			txtField.setFocusTraversable(false);
			txtField.setEffect(FxTransitionsUtil.innerGlow(color));
			txtField.setText(text);
			setRejectBtn(inputFieldIndex);
		}
	}
	
	private void setRejectBtn(int index) {
		if (view instanceof ISkipRejectInputView) {
			ISkipRejectInputView srView = (ISkipRejectInputView) view;
			Button btn = srView.getSkipRejectButtons().get(index);
			btn.setGraphic(setBtnImageView(btn, REJECT_IMAGE));
			FxTransitionsUtil.rotateTransition(1, 1000,  btn);
		}
	}
	
	public boolean alreadyCollected(TextField inputField) {
		if (inputField.getEffect() != null 
				&& inputField.getEffect().getClass().equals(InnerShadow.class)
				&& inputField.getText().trim().length() > 0) {
			return true;
		}
		return false;
	}
	
	public boolean isSkipped(TextField inputField) {
		if (inputField.getEffect() != null 
				&& inputField.getEffect().getClass().equals(InnerShadow.class)) {
			InnerShadow innerShadow = (InnerShadow) inputField.getEffect();
			if (innerShadow.getColor().equals(Color.ORANGE) 
					|| innerShadow.getColor().equals(Color.RED)) {
				return true;
			}
		}
		return false;
	}
	
	public void notifySkipReject(Button btn) {

		if (view instanceof ISkipRejectInputView) {
			int btnIndex = ((ISkipRejectInputView)view).getSkipRejectButtons().indexOf(btn);
			boolean hasScan = DataCollectionModel.hasScanPart(operation) && (btnIndex == 0);

			if (btn.getId().equals(SKIP_IMAGE)) {
				handleSkip(btn, btnIndex, hasScan);
			} else if (btn.getId().equals(REJECT_IMAGE)) {
				handleReject(btn, btnIndex, hasScan);
			} else if (btn.getId().equals(COLLECT_TORQUE_IMAGE) || btn.getId().equals(COLLECT_SCAN_IMAGE)) {
				handleCollect(btn, btnIndex);
			}
		
			UnitNavigatorEvent event  =  new UnitNavigatorEvent(UnitNavigatorEventType.MOVETO, model.getCurrentOperationIndex());
			EventBusUtil.publish(event);
		}
	}

	public void handleSkip(Button btn, int btnIndex, boolean hasScan) {
		getLogger().check("Skip button " + btnIndex + " clicked");
		FxTransitionsUtil.rotateTransition(1, 1000, btn.getGraphic());

		boolean isBadMeasurement = !view.getInputFields().get(btnIndex).getText().isEmpty();
		if (isBadMeasurement) {
			view.getInputFields().get(btnIndex).setEffect(FxTransitionsUtil.innerGlow(Color.RED));
		} else {
			view.getInputFields().get(btnIndex).setEffect(FxTransitionsUtil.innerGlow(Color.ORANGE));
		}
		
		view.getInputFields().get(btnIndex).setEditable(false);
		if (hasScan && (btnIndex == 0)) {
			btn.setGraphic(setBtnImageView(btn, COLLECT_SCAN_IMAGE));
			EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.PART_SN_SKIP, getDataCollectionIndex(btnIndex)));
		} else {
			btn.setGraphic(setBtnImageView(btn, COLLECT_TORQUE_IMAGE));
			EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.MEASUREMENT_SKIP, getDataCollectionIndex(btnIndex)));
		}
	}
	
	public void showSkippedIndput(int inputIndex, boolean hasScan) {
		if (!hasScan) {
			inputIndex--;
		}
		view.getInputFields().get(inputIndex).setEffect(FxTransitionsUtil.innerGlow(Color.ORANGE));
		view.getInputFields().get(inputIndex).setEditable(false);
		if (view instanceof ISkipRejectInputView) {
			Button btn = ((ISkipRejectInputView)view).getSkipRejectButtons().get(inputIndex);
			btn.setGraphic(setBtnImageView(btn, COLLECT_TORQUE_IMAGE));
		}
	}
	
	public void handleCollect(Button btn, int btnIndex) {
		getLogger().check("Collect button " + btnIndex + " clicked");
		FxTransitionsUtil.rotateTransition(1, 1000, btn.getGraphic());
		
		
		view.setCurrentInputFieldIndex(btnIndex);
		view.prepareExpectedInputField(btnIndex);
		
		if (btn.getId().equals(COLLECT_TORQUE_IMAGE)){
			TextField textInput = view.getInputFields().get(btnIndex);
			textInput.setEffect(null);
			textInput.setText("");
			textInput.setEditable(true);
			textInput.requestFocus();
		}
		btn.setGraphic(setBtnImageView(btn, SKIP_IMAGE));
		
	}

	public void handleReject(Button btn, int btnIndex, boolean hasScan) {
		getLogger().check("Reject button " + btnIndex + " clicked");
		
		if (hasScan && (btnIndex == 0)) {
			for (int i = 0; i < view.getInputFields().size();) {
				resetRejectInputField(i++);
			//	resetPartMeasInputField();
			}
			EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.PART_SN_REJECT, new PartSerialScanData()));
		} else  {
			resetRejectInputField(btnIndex);
			EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.MEASUREMENT_REJECT, getDataCollectionIndex(btnIndex)));
		}
		view.setCurrentInputFieldIndex(btnIndex);
		view.prepareExpectedInputField(btnIndex);
		EventBusUtil.publish(new StatusMessageEvent("", StatusMessageEventType.CLEAR));
	}
	public boolean isMeasOnlyOperation() {
		return DataCollectionModel.isMeasOnlyOperation(view.getOperation());
	}

	public void resetRejectInputField(int index) {
		Button button = ((ISkipRejectInputView)view).getSkipRejectButtons().get(index);
		TextField textInput = view.getInputFields().get(index);
		
		button.setGraphic(setBtnImageView(button, SKIP_IMAGE));
		FxTransitionsUtil.rotateTransition(1, 3000, button.getGraphic());
		
		textInput.setEffect(null);
		textInput.setText("");
		
		textInput.setEditable(true);
		textInput.requestFocus();

	}
	
	public void prepareExpectedInputField(MCOperationRevision operation, TextField textField, int inputFieldIndex) {
		for (Object inputField: view.getInputFields()) {
			TextField inputTxtField = (TextField) inputField;
			if (!textField.equals(inputTxtField)) {		// clear OuterGlow on fields other than the current input field
				if (inputTxtField.getEffect() != null && inputTxtField.getEffect().getClass().equals(DropShadow.class)) {
					inputTxtField.setEffect(null);
				}
			}
		}
		textField.setEffect(FxTransitionsUtil.outerGlow(Color.TEAL));
		if ((DataCollectionModel.hasScanPart(operation) || DataCollectionModel.isMeasOnlyOperation(getOperation())) && inputFieldIndex == 0) {
			textField.setEditable(true);
			textField.setFocusTraversable(true);
		}
		if (textField.isEditable()) {
			UiUtils.requestFocus(textField);
		}
	}
	
	public void showTorque(final Torque torque, final TextField textField) {
		if (getModel().getGoodMeasurementsCount(getOperationName()) == getModel().getMeasurementCount(getOperation())) {
			Logger.getLogger().warn("Unexpected Torque received: " + torque.toString());
			return;
		}
		Platform.runLater(new Runnable() {
			public void run() {
				textField.setText(Double.toString(torque.getTorqueValue()));
			}
		});
		EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.MEASUREMENT_RECEIVED, torque));
	}
	
	public DataCollectionIndexData getDataCollectionIndex(int btnIndex) {
		DataCollectionIndexData dcIndex = new DataCollectionIndexData();
		dcIndex.setInputIndex(btnIndex);
		dcIndex.setHasScanPart(DataCollectionModel.hasScanPart(operation));
		return dcIndex;
	}
	
	public Button createSkipRejectBtn() {
		return createSkipRejectBtn("15 5 15 5", "0,0,2,2", "2,2,1,1");
	}
	
	public Button createSkipRejectBtn(String padding, String insets,String radius) {
        Button skipRejectBtn = UiFactory.createButton("", StyleUtil.getBtnStyle(20, padding, insets, radius), true, "skipRejectBtn");
		skipRejectBtn.setMinWidth(Button.USE_PREF_SIZE);
		skipRejectBtn.setPrefWidth(StyleUtil.getCmdBtnWidth());
		skipRejectBtn.setMinHeight(Button.USE_PREF_SIZE);
		skipRejectBtn.setPrefHeight(StyleUtil.getCmdBtnHeight());
		FxTransitionsUtil.rotateTransition(1, 1000, skipRejectBtn.getGraphic());
		skipRejectBtn.setGraphic(setBtnImageView(skipRejectBtn, SKIP_IMAGE));
		return skipRejectBtn;
	}

	public static ImageView setBtnImageView(Button btn, String imgResource) {
		btn.setId(imgResource);
		Image image = new Image(imgResource);
        ImageView btnImgView = new ImageView(image);
        btnImgView.setImage(image);
        btnImgView.setFitWidth(StyleUtil.getCmdBtnImageWidth());
        btnImgView.setFitHeight(StyleUtil.getCmdBtnImageHeight());
        btnImgView.setPreserveRatio(true);
        btnImgView.setSmooth(true);
        btnImgView.setCache(true);
		return btnImgView;
	}
	
	public MCOperationPartRevision getPartSpec() {
		return getOperation().getSelectedPart();
	}
	
	public MCOperationRevision getOperation() {
		return operation;
	}
	
	public DataCollectionModel getModel() {
		return model;
	}
	
	public String getOperationName() {
		return getOperation().getId().getOperationName();
	}
	
	public Logger getLogger() {
		if (logger == null) {
			logger = Logger.getLogger();
		}
		return logger;
	}
}
