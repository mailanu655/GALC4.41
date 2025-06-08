package com.honda.galc.client.dc.view;

import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.dc.enumtype.DataCollectionEventType;
import com.honda.galc.client.dc.enumtype.DataCollectionResultEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.event.DataCollectionResultEvent;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.dc.mvc.ISkipRejectInputView;
import com.honda.galc.client.dc.processor.IDataCollectionTaskProcessor;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.FxTransitionsUtil;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.DataCollectionIndexData;
import com.honda.galc.device.dataformat.MeasurementInputData;

/**
 * @author Subu Kathiresan
 * @date Sep 26, 2014
 */
public class LotControlViewEventHandler {

	private Logger logger;
	private volatile IDataCollectionWidget<IDataCollectionTaskProcessor<? extends IDeviceData>> view = null;
	private static LotControlViewEventHandler instance = null;

	public LotControlViewEventHandler(IDataCollectionWidget<IDataCollectionTaskProcessor<? extends IDeviceData>> view) {
		this.view = view;
		register();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public LotControlViewEventHandler(OperationView view) {
		this.view = (IDataCollectionWidget) view;
		register();
	}

	public void register() {
		if (EventBusUtil.isRegistered(instance)) {
			EventBusUtil.unregister(instance);
		}
		EventBusUtil.register(this);
		instance = this;
	}

	@Subscribe
	public void received(DataCollectionResultEvent event) {
		try {
			if (view.getOperation().equals(event.getOperation())) {
				getLogger().info(event.getType() + " received");
				switch(event.getType()) {
				case DC_COMPLETED_FOR_PART:
					break;
				case INVALID_PART_SCAN_RECEIVED:
				case INVALID_MEASUREMENT_RECEIVED:
					handleError(event);
					break;
				case VALID_PART_SCAN_RECEIVED:
					handleValidPartScan(event);
					break;
				case VALID_MEASUREMENT_RECEIVED:
					handleValidMeasurement(event);
					break;
				case SKIP_PART_SCAN_RECEIVED:
				case SKIP_MEASUREMENT_RECEIVED:
					skipInput(event);
					break;
				case REJECT_PART_SCAN_RECEIVED:
				case REJECT_MEASUREMENT_RECEIVED:
					rejectInput(event);
					break;
				default:
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	private void handleError(DataCollectionResultEvent event) {
		TextField textField = view.getInputFields().get(view.getCurrentInputFieldIndex());
		textField.setEffect(FxTransitionsUtil.outerGlow(Color.RED));
		textField.selectAll();
		//BAK - 2015-12-21 - Only run max Attempt logic on an invalid measurement
				if (event.getType()==DataCollectionResultEventType.INVALID_MEASUREMENT_RECEIVED) {
					MeasurementInputData measurementInputData = (MeasurementInputData)event.getInputData();
					if(measurementInputData.getAttemptNumber() > 3) {
						maxAttemptMeasurement(measurementInputData.getMeasurementIndex());
					}
		}
		EventBusUtil.publish(new StatusMessageEvent(event.getType().getMessage(), StatusMessageEventType.ERROR));
	}

	private void handleValidPartScan(DataCollectionResultEvent event) {
		handleValidInput(0);
	}

	private void handleValidMeasurement(DataCollectionResultEvent event) {
		MeasurementInputData mInputData = (MeasurementInputData) event.getInputData();
		int inputIndex = -1;
		if (hasScanPart()) {
			inputIndex = mInputData.getMeasurementIndex();
		} else {
			inputIndex = mInputData.getMeasurementIndex() - 1;
		}
		handleValidInput(inputIndex);
	}

	public void handleValidInput(int inputIndex) {
		ChangeBtnImage(inputIndex);
		view.getInputFields().get(inputIndex).setEffect(FxTransitionsUtil.innerGlow(Color.LIGHTGREEN));
		view.getInputFields().get(inputIndex).setEditable(false);
		setFocusToNextExpectedInputField();
		EventBusUtil.publish(new StatusMessageEvent("", StatusMessageEventType.CLEAR));
	}

	public void ChangeBtnImage(int inputIndex) {
		if (view instanceof ISkipRejectInputView) {
			Button btn = ((ISkipRejectInputView) view).getSkipRejectButtons().get(inputIndex);
			FxTransitionsUtil.rotateTransition(1, 1000, btn.getGraphic());
			btn.setGraphic(DataCollectionViewUtil.setBtnImageView(btn, DataCollectionViewUtil.REJECT_IMAGE));
		}
	}

	private void maxAttemptMeasurement(int index) {
		int inputIndex = -1;
		if (hasScanPart()) {
			inputIndex = index;
		} else {
			inputIndex = index - 1;
		}
		maxAttemptInput(inputIndex);
	}

	public void maxAttemptInput(int inputIndex) {
		ChangeBtnImage(inputIndex);
		view.getInputFields().get(inputIndex).setEffect(FxTransitionsUtil.innerGlow(Color.RED));
		view.getInputFields().get(inputIndex).setEditable(false);
		setFocusToNextExpectedInputField();
		EventBusUtil.publish(new StatusMessageEvent("", StatusMessageEventType.CLEAR));
	}

	public void skipInput(DataCollectionResultEvent event) {
		if (view instanceof ISkipRejectInputView) {
			setFocusToNextExpectedInputField();
			EventBusUtil.publish(new StatusMessageEvent("", StatusMessageEventType.CLEAR));
		}
	}

	public void setFocusToNextExpectedInputField() {
		TextField expectedInputField = getNextFocusableTextField(view.getInputFields(), 0);
		if (expectedInputField != null) {
			view.setCurrentInputFieldIndex(view.getInputFields().indexOf(expectedInputField));
			view.prepareExpectedInputField(view.getInputFields().indexOf(expectedInputField));
		}
	}

	public void rejectInput(DataCollectionResultEvent event) {}

	public TextField getNextFocusableTextField(List<TextField> list, int startIdx) {
		if (null == list || list.isEmpty()) {
			return null;
		}
		if (startIdx < 0) {
			startIdx = 0;
		}
		int len = list.size();
		TextField tf = null;
		for (int i = startIdx; i < len; i++) {
			tf = list.get(i);
			if (tf.getText().isEmpty()) {
				if (wasInputSkipped(i)) {
					continue;
				}
				return tf;
			}
		}
		return null;
	}

	public boolean wasInputSkipped(int index) {
		Button btn = getSkipRejectButton(index);
		return (btn != null && (btn.getId().equals(DataCollectionViewUtil.COLLECT_SCAN_IMAGE) ||
				btn.getId().equals(DataCollectionViewUtil.COLLECT_TORQUE_IMAGE)));
	}

	public Button getSkipRejectButton(int index) {
		if (view instanceof ISkipRejectInputView) {
			return ((ISkipRejectInputView)view).getSkipRejectButtons().get(index);
		} else {
			return null;
		}
	}

	public void dispatchWaitEvents(int inputFieldIndex) {
		if (inputFieldIndex == -1) {
			getLogger().error("ERROR: Not a valid input field index");
			return;
		}
		DataCollectionIndexData dcIndexData = new DataCollectionIndexData(inputFieldIndex);
		dcIndexData.setHasScanPart(hasScanPart());
		if (hasScanPart() && inputFieldIndex == 0) {
			EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.WAITING_FOR_PART_SN, dcIndexData));
		} else {
			EventBusUtil.publish(new DataCollectionEvent(DataCollectionEventType.WAITING_FOR_MEASUREMENT, dcIndexData));
		}
	}

	private boolean hasScanPart() {
		return DataCollectionModel.hasScanPart(view.getOperation());
	}


	public Logger getLogger() {
		if (logger == null) {
			logger = Logger.getLogger();
		}
		return logger;
	}
}

