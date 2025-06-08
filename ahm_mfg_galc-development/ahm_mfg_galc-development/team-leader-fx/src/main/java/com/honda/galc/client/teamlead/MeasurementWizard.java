package com.honda.galc.client.teamlead;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.dto.MCMeasurementDTO;
import com.honda.galc.client.dto.MCOperationPartRevisionDTO;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dao.conf.MCOperationMeasurementDao;
import com.honda.galc.dao.conf.MCOperationPartRevisionDao;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationPartRevisionId;
import com.honda.galc.service.ServiceFactory;

public class MeasurementWizard {

	private Stage dialog = null;
	private Stage owner = null;
	Button okButton;
	Button cancelButton;
	TextField processorTF, viewTF, typeTF, seqNumberTF, deviceIdTF,
			deviceMsgTF, minLimitTF, maxLimitTF, maxAttemptsTF;

	private MfgMaintFXMLPane controller;

	Label processorLabel, viewLabel, typeLabel, seqNumberLabel, deviceIdLabel,
			deviceMsgLabel, minLimitLabel, maxLimitLabel, maxAttemptsLabel;

	MCOperationPartRevisionDTO part;
	MCMeasurementDTO measurement;
	String mode;
	private String eol = System.getProperty("line.separator");

	Label errorMsg = UiFactory.createLabel("errorMsg");

	public MeasurementWizard(MCOperationPartRevisionDTO part,
			MfgMaintFXMLPane controller, String mode) {
		this.part = part;
		this.controller = controller;
		this.mode = mode;
		constructStage();
	}

	public MeasurementWizard(MCMeasurementDTO measurement,
			MfgMaintFXMLPane controller, String mode) {
		this.measurement = measurement;
		this.controller = controller;
		this.mode = mode;
		constructStage();
	}

	public void closeMessage() {
		dialog.close();
	}

	public void constructStage() {
		dialog = new Stage();
		dialog.initStyle(StageStyle.DECORATED);
		if (owner != null)
			dialog.initOwner(owner);
		dialog.initModality(Modality.APPLICATION_MODAL);
		Scene scene = constructScene();

		dialog.setHeight(600);
		dialog.setWidth(500);

		dialog.setScene(scene);
		dialog.centerOnScreen();

		dialog.toFront();
		dialog.showAndWait();
	}

	private Scene constructScene() {
		String cssPath = "/resource/css/mfgmaintscreen.css";

		if (ClientMainFx.class.getResource(cssPath) == null) {
			getLogger().warn(
					String.format(
							"Unable to load stylesheet [%s]. Using default",
							cssPath));
		}

		seqNumberTF = UiFactory.createTextField("seqNumberTF");

		seqNumberLabel = UiFactory.createLabel("seqNumberLabel", "Sequence Number");
		seqNumberTF.setEditable(false);

		viewTF = UiFactory.createTextField("viewTF");
		viewLabel = UiFactory.createLabel("viewLabel", "Enter  View");

		processorTF = UiFactory.createTextField("processorTF");
		processorLabel = UiFactory.createLabel("processorLabel", "Enter Processor");

		deviceIdTF = UiFactory.createTextField("deviceIdTF");
		deviceIdLabel = UiFactory.createLabel("deviceIdLabel", "Enter Device Id");

		deviceMsgTF = UiFactory.createTextField("deviceMsgTF");
		deviceMsgLabel = UiFactory.createLabel("deviceMsgLabel", "Enter Device Message");

		minLimitTF = UiFactory.createTextField("minLimitTF");
		minLimitLabel = UiFactory.createLabel("minLimitLabel", "Enter Min Limit");

		maxLimitTF = UiFactory.createTextField("maxLimitTF");
		maxLimitLabel = UiFactory.createLabel("maxLimitLabel", "Enter Max Limit");

		maxAttemptsTF = UiFactory.createTextField("maxAttemptsTF");
		maxAttemptsLabel = UiFactory.createLabel("maxAttemptsLabel", "Enter Max Attempts");

		if (mode.equals("Add")) {
			maxAttemptsTF.setText("3");
			seqNumberTF.setText(String.valueOf(findMaxSequenceNumber()));
		}

		typeTF = UiFactory.createTextField("typeTF");
		typeLabel = UiFactory.createLabel("typeLabel", "Enter Type");
		String okButtonValue = "Save";
		if (mode.equals("Add")) {
			okButtonValue = "Add";
		}
		okButton = UiFactory.createButton(okButtonValue);

		cancelButton = UiFactory.createButton("Cancel");
		if (mode.equals("Edit"))
			setValues();

		okButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				String errorMsgStr = validate();
				if (errorMsgStr.length() > 0) {
					errorMsg.setText(errorMsgStr);
					errorMsg.setTextFill(Color.RED);
				}
				else {
					MCMeasurementDTO rec = measurement;
					if (mode.equals("Add")) {
						rec = new MCMeasurementDTO();
					}
					rec.setDeviceId(deviceIdTF.getText());
					rec.setDeviceMsg(deviceMsgTF.getText());
					rec.setMaxAttempts(maxAttemptsTF.getText());
					rec.setMaxLimit(maxLimitTF.getText());
					rec.setMeasurementType(typeTF.getText());
					rec.setMinLimit(minLimitTF.getText());
					rec.setProcessor(processorTF.getText());
					rec.setSeqNumber(seqNumberTF.getText());
					rec.setView(viewTF.getText());
					rec.setMeasurementType(typeTF.getText());
					if (mode.equals("Add")) {
						rec.setOperationName(part.getOperationName());
						rec.setPartId(part.getPartId());
						rec.setPartRev(part.getPartRev());
						
						MCOperationMeasurement entity = DTOConverter
								.convertMCMeasurementDTO(rec);
						ServiceFactory.getDao(MCOperationMeasurementDao.class).save(entity);
						
						controller.getMeasurementsTable().tblView.getItems().addAll(rec);
						
						//Setting measurement count
						MCOperationPartRevision partRevEntity = DTOConverter
								.convertMcoperationPartRevisionDTO(part);
						
						MCOperationPartRevisionId recEntityId = new MCOperationPartRevisionId(part.getOperationName(), part.getPartId(), Integer.parseInt(part.getPartRev()));
						MCOperationPartRevision recEntity = ServiceFactory.getDao(MCOperationPartRevisionDao.class).findByKey(recEntityId);
						
						if(recEntity != null && recEntity.getApproved() != null) {
							partRevEntity.setApproved(new Timestamp(recEntity.getApproved().getTime()));
						}
						if(recEntity != null && recEntity.getDeprecated() != null) {
							partRevEntity.setDeprecated(new Timestamp(recEntity.getDeprecated().getTime()));
						}
						
						ServiceFactory.getDao(MCOperationPartRevisionDao.class)
								.save(partRevEntity);
					}
					else if (mode.equals("Edit")) {
						MCOperationMeasurement entity = DTOConverter
								.convertMCMeasurementDTO(rec);
						ServiceFactory.getDao(MCOperationMeasurementDao.class).save(entity);
					}

					List<MCMeasurementDTO> list = new ArrayList<MCMeasurementDTO>(
							controller.getMeasurementsTable().tblView.getItems());
					controller.getMeasurementsTable().tblView.getItems().setAll(list);
					closeMessage();
				}
			}
		});

		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				closeMessage();
			}
		});

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(30, 30, 30, 30));
		grid.add(seqNumberLabel, 0, 2);
		grid.add(seqNumberTF, 1, 2);

		grid.add(processorLabel, 0, 3);
		grid.add(processorTF, 1, 3);

		grid.add(viewLabel, 0, 4);
		grid.add(viewTF, 1, 4);

		grid.add(minLimitLabel, 0, 5);
		grid.add(minLimitTF, 1, 5);

		grid.add(maxLimitLabel, 0, 6);
		grid.add(maxLimitTF, 1, 6);

		grid.add(maxAttemptsLabel, 0, 7);
		grid.add(maxAttemptsTF, 1, 7);

		grid.add(typeLabel, 0, 8);
		grid.add(typeTF, 1, 8);
		
		grid.add(deviceIdLabel, 0, 9);
		grid.add(deviceIdTF, 1, 9);
		
		grid.add(deviceMsgLabel, 0, 10);
		grid.add(deviceMsgTF, 1, 10);

		grid.add(okButton, 0, 12);
		grid.add(cancelButton, 1, 12);

		grid.add(errorMsg, 0, 13, 2, 1);

		Scene scene = new Scene(grid);
		scene.getStylesheets().add(cssPath);
		return scene;
	}

	private void setValues() {
		viewTF.setText(measurement.getView());
		processorTF.setText(measurement.getProcessor());
		deviceIdTF.setText(measurement.getDeviceId());
		deviceMsgTF.setText(measurement.getDeviceMsg());
		minLimitTF.setText(measurement.getMinLimit());
		maxLimitTF.setText(measurement.getMaxLimit());
		maxAttemptsTF.setText(measurement.getMaxAttempts());
		typeTF.setText(measurement.getMeasurementType());
		seqNumberTF.setText(measurement.getSeqNumber());
	}

	private String validate() {
		String errorMsgStr = "";
		double minValue = 0, maxValue = 0.;

		try {
			if (minLimitTF.getText().length() > 0)
				minValue = Double.valueOf(minLimitTF.getText());
		} catch (NumberFormatException e) {
			errorMsgStr = "Min Limit is not valid"+eol;
		}
		
		try {
			if (maxLimitTF.getText().length() > 0)
				maxValue = Double.valueOf(maxLimitTF.getText());
		} catch (NumberFormatException e) {
			errorMsgStr += "Max Limit is not valid"+eol;
		}

		try {
			if (maxAttemptsTF.getText().length() > 0) {
				Integer.valueOf(maxAttemptsTF.getText());
			} else {
				errorMsgStr += "A value must be provided for Max Attempts"+eol;
			}
		} catch (NumberFormatException e) {
			errorMsgStr += "Max Attempts is not valid"+eol;
		}

		if (minValue > maxValue)
			errorMsgStr += "Min Limit cannot be greater than Max Limit"+eol;
		
		
		
		return errorMsgStr;
	}

	

	private int findMaxSequenceNumber() {
		List<Integer> seqList = new ArrayList<Integer>();
		List<MCMeasurementDTO> list = controller.getMeasurementsTable().tblView.getItems();
		if (list.size() == 0)
			return 1;
		for (MCMeasurementDTO meas : list) {
			seqList.add(Integer.parseInt(meas.getSeqNumber()));
		}
		Object mx = Collections.max(seqList);
		return Integer.parseInt(mx.toString()) +1;
	}
}
