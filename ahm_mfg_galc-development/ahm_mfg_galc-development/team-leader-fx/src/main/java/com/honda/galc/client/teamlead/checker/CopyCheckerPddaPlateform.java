package com.honda.galc.client.teamlead.checker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedText;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.constant.PartType;
import com.honda.galc.dto.CopyCheckerDetailsDto;
import com.honda.galc.dto.MeasurementCheckerDto;
import com.honda.galc.dto.PartCheckerDto;
import com.honda.galc.entity.conf.MCMeasurementChecker;
import com.honda.galc.entity.conf.MCOperationChecker;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.MCPartChecker;
import com.honda.galc.entity.conf.MCPddaUnitRevision;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class CopyCheckerPddaPlateform extends FxDialog {

	private LoggedComboBox<String> fromPlantComboBox;
	private LoggedComboBox<String> fromDepartmentComboBox;
	private LoggedComboBox<String> fromModelYearComboBox;
	private LoggedComboBox<String> fromProductionRateComboBox;
	private LoggedComboBox<String> fromLineNumberComboBox;
	private LoggedComboBox<String> fromVMCComboBox;

	private LoggedComboBox<String> toPlantComboBox;
	private LoggedComboBox<String> toDepartmentComboBox;
	private LoggedComboBox<String> toModelYearComboBox;
	private LoggedComboBox<String> toProductionRateComboBox;
	private LoggedComboBox<String> toLineNumberComboBox;
	private LoggedComboBox<String> toVMCComboBox;
	private LoggedText errMsgText;
	private LoggedButton copyBtn;
	private LoggedButton cancelBtn;
	private CheckerConfigModel model;
	private Label updateLabel;
	private Label errorLabel;
	private ProgressBar progress;
	private static double progressCounter = 0.0;
	@SuppressWarnings("rawtypes")
	private Task mainTask;
	private Label operationCheckerLabel;
	private Label partCheckerLabel;
	private Label measurementCheckerLabel;

	public CopyCheckerPddaPlateform(String title, CheckerConfigModel model) {
		super("Copy Checker From PDDA Platform", ClientMainFx.getInstance().getStage());
		this.model = model;
		initComponents();
	}

	private void initComponents() {
		((BorderPane) this.getScene().getRoot()).setTop(createProgressContainer());
		((BorderPane) this.getScene().getRoot()).setCenter(createContainer());
		loadComboBox();
		handleButtonAction();
	}

	private VBox createProgressContainer() {
		VBox container = new VBox();
		updateLabel = new Label("Waiting to Copy...");
		updateLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; ");

		errorLabel = new Label();
		errorLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: red;");

		progress = new ProgressBar();
		progress.setMinWidth(400);
		progress.setMaxWidth(400);
		progress.setVisible(false);
		
		HBox labelContainer = new HBox();
		operationCheckerLabel = new Label("Operation Checker");
		operationCheckerLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; ");
		
		partCheckerLabel = new Label("Part Checker");
		partCheckerLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; ");
		
		measurementCheckerLabel = new Label("Measurement Checker");
		measurementCheckerLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; ");
		
		labelContainer.getChildren().addAll(operationCheckerLabel, partCheckerLabel, measurementCheckerLabel);
		labelContainer.setAlignment(Pos.CENTER);
		labelContainer.setSpacing(20);
		
		container.getChildren().addAll(progress, updateLabel, errorLabel, labelContainer);
		container.setAlignment(Pos.CENTER);
		container.setPadding(new Insets(10));
		return container;
	}

	private VBox createContainer(){
		VBox pane = new VBox();
		pane.getChildren().addAll(createTitiledPane("Copy All Active Checkers From PDDA Platform", createFromPDDAContainer()), 
				createTitiledPane("To PDDA Platform", createToPDDAContainer()), createButton(), createErrorMsgText());
		return pane;
	}

	private void loadComboBox() {
		loadFromPlantComboBox();
		loadToPlantComboBox();
		addComboBoxListener();
	}

	private void addComboBoxListener() {
		addFromPlantComboBoxListener();
		addFromDepartmentComboBoxListener();
		addFromModelYearComboBoxListener();
		addFromProductionRateComboBoxListener();
		addFromLineNoComboBoxListener();

		addToPlantComboBoxListener();
		addToDepartmentComboBoxListener();
		addToModelYearComboBoxListener();
		addToProductionRateComboBoxListener();
		addToLineNoComboBoxListener();
	}

	@SuppressWarnings("unchecked")
	private void loadFromPlantComboBox() {
		fromPlantComboBox.getItems().clear();
		fromPlantComboBox.setPromptText("Select");
		fromPlantComboBox.getItems().addAll(model.loadFromPlant());
	}

	@SuppressWarnings("unchecked")
	private void addFromPlantComboBoxListener() {
		fromPlantComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				loadFromDepartmentComboBox(newValue);
			} 
		});
	}

	@SuppressWarnings("unchecked")
	private void loadFromDepartmentComboBox(String plant) {
		fromDepartmentComboBox.getItems().clear();
		fromModelYearComboBox.getItems().clear();
		fromModelYearComboBox.setValue(null);
		fromProductionRateComboBox.getItems().clear();
		fromProductionRateComboBox.setValue(null);
		fromLineNumberComboBox.getItems().clear();
		fromVMCComboBox.getItems().clear();
		fromLineNumberComboBox.setValue(null);
		fromVMCComboBox.setValue(null);
		fromDepartmentComboBox.setPromptText("Select");
		fromDepartmentComboBox.getItems().addAll(model.loadFromDepartment(plant));
	}

	@SuppressWarnings("unchecked")
	private void addFromDepartmentComboBoxListener() {
		fromDepartmentComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				loadFromModelYearComboBox(newValue);
			} 
		});
	}

	@SuppressWarnings("unchecked")
	private void loadFromModelYearComboBox(String dept) {
		fromModelYearComboBox.getItems().clear();
		fromModelYearComboBox.setValue(null);
		fromProductionRateComboBox.getItems().clear();
		fromProductionRateComboBox.setValue(null);
		fromLineNumberComboBox.getItems().clear();
		fromVMCComboBox.getItems().clear();
		fromLineNumberComboBox.setValue(null);
		fromVMCComboBox.setValue(null);
		fromModelYearComboBox.setPromptText("Select");
		String plant = fromPlantComboBox != null ? fromPlantComboBox.getSelectionModel().getSelectedItem().toString() : "";
		for (Long l : model.loadFromModelYear(plant, dept)) {
			fromModelYearComboBox.getItems().add(l.toString());
		}
	}

	@SuppressWarnings("unchecked")
	private void addFromModelYearComboBoxListener() {
		fromModelYearComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				loadFromProductionRateComboBox(newValue);
			} 
		});
	}

	@SuppressWarnings("unchecked")
	private void loadFromProductionRateComboBox(String modelYear) {
		fromProductionRateComboBox.getItems().clear();
		fromProductionRateComboBox.setValue(null);
		fromLineNumberComboBox.getItems().clear();
		fromVMCComboBox.getItems().clear();
		fromLineNumberComboBox.setValue(null);
		fromVMCComboBox.setValue(null);
		fromProductionRateComboBox.setPromptText("Select");
		String plant = fromPlantComboBox != null ? fromPlantComboBox.getSelectionModel().getSelectedItem().toString() : "";
		String dept = fromDepartmentComboBox != null ? fromDepartmentComboBox.getSelectionModel().getSelectedItem().toString() : "";
		for (Long l : model.loadFromProductionRate(plant, dept, 
				new BigDecimal(modelYear.replaceAll(",", "")))) {
			fromProductionRateComboBox.getItems().add(l.toString());
		}
	}

	@SuppressWarnings("unchecked")
	private void addFromProductionRateComboBoxListener() {
		fromProductionRateComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				loadFromLineNoComboBox(newValue);
			} 
		});
	}

	@SuppressWarnings("unchecked")
	private void loadFromLineNoComboBox(String productionRate) {
		fromLineNumberComboBox.getItems().clear();
		fromVMCComboBox.getItems().clear();
		fromLineNumberComboBox.setValue(null);
		fromVMCComboBox.setValue(null);
		fromLineNumberComboBox.setPromptText("Select");
		fromVMCComboBox.getItems().clear();
		String plant = fromPlantComboBox != null  && fromDepartmentComboBox.getSelectionModel().getSelectedItem() != null ? 
				fromPlantComboBox.getSelectionModel().getSelectedItem().toString() : "";
				String dept = fromDepartmentComboBox != null && fromDepartmentComboBox.getSelectionModel().getSelectedItem() != null ? 
						fromDepartmentComboBox.getSelectionModel().getSelectedItem().toString() : "";
						String modelYear = fromModelYearComboBox != null && fromModelYearComboBox.getSelectionModel().getSelectedItem() != null ? 
								fromModelYearComboBox.getSelectionModel().getSelectedItem().toString() : "";
								if(!StringUtils.isBlank(modelYear))
									fromLineNumberComboBox.getItems().addAll(model.loadFromLineNo(plant, dept, 
											new BigDecimal(modelYear.replaceAll(",", "")), new BigDecimal(productionRate.replaceAll(",", ""))));
	}

	@SuppressWarnings("unchecked")
	private void addFromLineNoComboBoxListener() {
		fromLineNumberComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				loadFromVMCComboBox(newValue);
			} 
		});
	}

	@SuppressWarnings("unchecked")
	private void loadFromVMCComboBox(String lineNo) {
		fromVMCComboBox.getItems().clear();
		fromVMCComboBox.setValue(null);
		fromVMCComboBox.setPromptText("Select");
		String plant = fromPlantComboBox != null  && fromPlantComboBox.getSelectionModel().getSelectedItem() != null ? 
				fromPlantComboBox.getSelectionModel().getSelectedItem().toString() : "";
				String dept = fromDepartmentComboBox != null  && fromDepartmentComboBox.getSelectionModel().getSelectedItem() != null ? 
						fromDepartmentComboBox.getSelectionModel().getSelectedItem().toString() : "";
						String modelYear = fromModelYearComboBox != null  && fromModelYearComboBox.getSelectionModel().getSelectedItem() != null ? 
								fromModelYearComboBox.getSelectionModel().getSelectedItem().toString() : "";
								String productionRate = fromProductionRateComboBox != null  && fromProductionRateComboBox.getSelectionModel().getSelectedItem() != null ? 
										fromProductionRateComboBox.getSelectionModel().getSelectedItem().toString() : "";
										if(!StringUtils.isBlank(modelYear) && !StringUtils.isBlank(productionRate))
											fromVMCComboBox.getItems().addAll(model.loadFromVMC(plant, dept, 
													new BigDecimal(modelYear.replaceAll(",", "")), new BigDecimal(productionRate.replaceAll(",", "")), lineNo));
	}

	@SuppressWarnings("unchecked")
	private void loadToPlantComboBox() {
		toPlantComboBox.getItems().clear();
		toPlantComboBox.setPromptText("Select");
		toPlantComboBox.getItems().addAll(model.loadFromPlant());
	}

	@SuppressWarnings("unchecked")
	private void addToPlantComboBoxListener() {
		toPlantComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				loadToDepartmentComboBox(newValue);
			} 
		});
	}

	@SuppressWarnings("unchecked")
	private void loadToDepartmentComboBox(String plant) {
		toDepartmentComboBox.getItems().clear();
		toModelYearComboBox.getItems().clear();
		toModelYearComboBox.setValue(null);
		toProductionRateComboBox.getItems().clear();
		toProductionRateComboBox.setValue(null);
		toLineNumberComboBox.getItems().clear();
		toVMCComboBox.getItems().clear();
		toLineNumberComboBox.setValue(null);
		toVMCComboBox.setValue(null);
		toDepartmentComboBox.setPromptText("Select");
		toDepartmentComboBox.getItems().addAll(model.loadFromDepartment(plant));

	}

	@SuppressWarnings("unchecked")
	private void addToDepartmentComboBoxListener() {
		toDepartmentComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				loadToModelYearComboBox(newValue);
			} 
		});
	}

	@SuppressWarnings("unchecked")
	private void loadToModelYearComboBox(String dept) {
		toModelYearComboBox.getItems().clear();
		toModelYearComboBox.setValue(null);
		toProductionRateComboBox.getItems().clear();
		toProductionRateComboBox.setValue(null);
		toLineNumberComboBox.getItems().clear();
		toVMCComboBox.getItems().clear();
		toLineNumberComboBox.setValue(null);
		toVMCComboBox.setValue(null);
		toModelYearComboBox.setPromptText("Select");
		String plant = toPlantComboBox != null ? toPlantComboBox.getSelectionModel().getSelectedItem().toString() : "";
		for (Long l : model.loadFromModelYear(plant, dept)) {
			toModelYearComboBox.getItems().add(l.toString());
		}
	}

	@SuppressWarnings("unchecked")
	private void addToModelYearComboBoxListener() {
		toModelYearComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				loadToProductionRateComboBox(newValue);
			} 
		});
	}

	@SuppressWarnings("unchecked")
	private void loadToProductionRateComboBox(String modelYear) {
		toProductionRateComboBox.getItems().clear();
		toProductionRateComboBox.setValue(null);
		toLineNumberComboBox.getItems().clear();
		toVMCComboBox.getItems().clear();
		toLineNumberComboBox.setValue(null);
		toVMCComboBox.setValue(null);
		toProductionRateComboBox.setPromptText("Select");
		String plant = toPlantComboBox != null ? toPlantComboBox.getSelectionModel().getSelectedItem().toString() : "";
		String dept = toDepartmentComboBox != null ? toDepartmentComboBox.getSelectionModel().getSelectedItem().toString() : "";
		if(modelYear != null) {
			for (Long l : model.loadFromProductionRate(plant, dept, 
					new BigDecimal(modelYear.replaceAll(",", "")))) {
				toProductionRateComboBox.getItems().add(l.toString());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void addToProductionRateComboBoxListener() {
		toProductionRateComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				loadToLineNoComboBox(newValue);
			} 
		});
	}

	@SuppressWarnings("unchecked")
	private void loadToLineNoComboBox(String productionRate) {
		toLineNumberComboBox.getItems().clear();
		toVMCComboBox.getItems().clear();
		toLineNumberComboBox.setValue(null);
		toVMCComboBox.setValue(null);
		toLineNumberComboBox.setPromptText("Select");
		String plant = toPlantComboBox != null  && toDepartmentComboBox.getSelectionModel().getSelectedItem() != null ? 
				toPlantComboBox.getSelectionModel().getSelectedItem().toString() : "";
				String dept = toDepartmentComboBox != null && toDepartmentComboBox.getSelectionModel().getSelectedItem() != null ? 
						toDepartmentComboBox.getSelectionModel().getSelectedItem().toString() : "";
						String modelYear = toModelYearComboBox != null && toModelYearComboBox.getSelectionModel().getSelectedItem() != null ? 
								toModelYearComboBox.getSelectionModel().getSelectedItem().toString() : "";
								if(!StringUtils.isBlank(modelYear))
									toLineNumberComboBox.getItems().addAll(model.loadFromLineNo(plant, dept, 
											new BigDecimal(modelYear.replaceAll(",", "")), new BigDecimal(productionRate.replaceAll(",", ""))));
	}

	@SuppressWarnings("unchecked")
	private void addToLineNoComboBoxListener() {
		toLineNumberComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				loadToVMCComboBox(newValue);
			} 
		});
	}

	@SuppressWarnings("unchecked")
	private void loadToVMCComboBox(String lineNo) {
		toVMCComboBox.getItems().clear();
		toVMCComboBox.setValue(null);
		toVMCComboBox.setPromptText("Select");
		String plant = toPlantComboBox != null  && toPlantComboBox.getSelectionModel().getSelectedItem() != null ? 
				toPlantComboBox.getSelectionModel().getSelectedItem().toString() : "";
				String dept = toDepartmentComboBox != null  && toDepartmentComboBox.getSelectionModel().getSelectedItem() != null ? 
						toDepartmentComboBox.getSelectionModel().getSelectedItem().toString() : "";
						String modelYear = toModelYearComboBox != null  && toModelYearComboBox.getSelectionModel().getSelectedItem() != null ? 
								toModelYearComboBox.getSelectionModel().getSelectedItem().toString() : "";
								String productionRate = toProductionRateComboBox != null  && toProductionRateComboBox.getSelectionModel().getSelectedItem() != null ? 
										toProductionRateComboBox.getSelectionModel().getSelectedItem().toString() : "";
										if(!StringUtils.isBlank(modelYear) && !StringUtils.isBlank(productionRate))
											toVMCComboBox.getItems().addAll(model.loadFromVMC(plant, dept, 
													new BigDecimal(modelYear.replaceAll(",", "")), new BigDecimal(productionRate.replaceAll(",", "")), lineNo));
	}

	private VBox createFromPDDAContainer(){
		VBox fromPane = new VBox();
		fromPane.setSpacing(0);
		fromPane.setPrefWidth(580);
		fromPane.setPrefHeight(100);

		HBox firstContainer = new HBox();
		Label plantLbl = new Label("Plant ");
		plantLbl.setStyle("-fx-font-weight: bold ;");
		plantLbl.setPadding(new Insets(0,47,0,0));
		fromPlantComboBox = createComboBox(fromPlantComboBox, "fromPlantComboBox");
		Label deptLbl = new Label("Department ");
		deptLbl.setStyle("-fx-font-weight: bold ;");
		fromDepartmentComboBox = createComboBox(fromDepartmentComboBox, "fromDepartmentComboBox");
		deptLbl.setPadding(new Insets(0,24,0,30));
		firstContainer.getChildren().addAll(plantLbl, fromPlantComboBox, deptLbl, fromDepartmentComboBox);
		firstContainer.setSpacing(10);
		firstContainer.setPadding(new Insets(5));

		HBox secondContainer = new HBox();
		Label modelYearLbl = new Label("Model Year ");
		modelYearLbl.setStyle("-fx-font-weight: bold ;");
		modelYearLbl.setPadding(new Insets(0,10,0,0));
		fromModelYearComboBox = createComboBox(fromModelYearComboBox, "fromModelYearComboBox");
		Label prodRateLbl = new Label("Production Rate ");
		prodRateLbl.setStyle("-fx-font-weight: bold ;");
		prodRateLbl.setPadding(new Insets(0,0,0,30));
		fromProductionRateComboBox = createComboBox(fromProductionRateComboBox, "fromProductionRateComboBox");
		secondContainer.getChildren().addAll(modelYearLbl, fromModelYearComboBox, prodRateLbl, fromProductionRateComboBox);
		secondContainer.setSpacing(10);
		secondContainer.setPadding(new Insets(5));

		HBox thirdContainer = new HBox();
		Label lineNoLbl = new Label("Line Number ");
		lineNoLbl.setStyle("-fx-font-weight: bold ;");
		fromLineNumberComboBox = createComboBox(fromLineNumberComboBox, "fromLineNumberComboBox");

		Label vmcLbl = new Label("VMC ");
		vmcLbl.setStyle("-fx-font-weight: bold ;");
		fromVMCComboBox = createComboBox(fromVMCComboBox, "fromVMCComboBox");
		vmcLbl.setPadding(new Insets(0,72,0,30));
		thirdContainer.getChildren().addAll(lineNoLbl, fromLineNumberComboBox,vmcLbl, fromVMCComboBox);
		thirdContainer.setSpacing(10);
		thirdContainer.setPadding(new Insets(5));

		fromPane.getChildren().addAll(firstContainer, secondContainer, thirdContainer );
		return fromPane;
	}

	private VBox createToPDDAContainer(){
		VBox toPane = new VBox();
		toPane.setSpacing(0);
		toPane.setPrefWidth(580);
		toPane.setPrefHeight(100);

		HBox firstContainer = new HBox();
		Label plantLbl = new Label("Plant ");
		plantLbl.setStyle("-fx-font-weight: bold ;");
		plantLbl.setPadding(new Insets(0,47,0,0));
		toPlantComboBox = createComboBox(toPlantComboBox, "toPlanComboBox");
		Label deptLbl = new Label("Department ");
		deptLbl.setStyle("-fx-font-weight: bold ;");
		toDepartmentComboBox = createComboBox(toDepartmentComboBox, "toDepartmentComboBox");
		deptLbl.setPadding(new Insets(0,24,0,30));
		firstContainer.getChildren().addAll(plantLbl, toPlantComboBox, deptLbl, toDepartmentComboBox);
		firstContainer.setSpacing(10);
		firstContainer.setPadding(new Insets(5));

		HBox secondContainer = new HBox();
		Label modelYearLbl = new Label("Model Year ");
		modelYearLbl.setStyle("-fx-font-weight: bold ;");
		modelYearLbl.setPadding(new Insets(0,10,0,0));
		toModelYearComboBox = createComboBox(toModelYearComboBox, "toModelYearComboBox");
		Label prodRateLbl = new Label("Production Rate ");
		prodRateLbl.setStyle("-fx-font-weight: bold ;");
		prodRateLbl.setPadding(new Insets(0,0,0,30));
		toProductionRateComboBox = createComboBox(toProductionRateComboBox, "toProductionRateComboBox");
		secondContainer.getChildren().addAll(modelYearLbl, toModelYearComboBox, prodRateLbl, toProductionRateComboBox);
		secondContainer.setSpacing(10);
		secondContainer.setPadding(new Insets(5));

		HBox thirdContainer = new HBox();
		Label lineNoLbl = new Label("Line Number ");
		lineNoLbl.setStyle("-fx-font-weight: bold ;");
		toLineNumberComboBox = createComboBox(toLineNumberComboBox, "toLineNumberComboBox");
		Label vmcLbl = new Label("VMC ");
		vmcLbl.setStyle("-fx-font-weight: bold ;");
		toVMCComboBox = createComboBox(toVMCComboBox, "toVMCComboBox");
		vmcLbl.setPadding(new Insets(0,72,0,30));
		thirdContainer.getChildren().addAll(lineNoLbl, toLineNumberComboBox,vmcLbl, toVMCComboBox);
		thirdContainer.setSpacing(10);
		thirdContainer.setPadding(new Insets(5));

		toPane.getChildren().addAll(firstContainer, secondContainer, thirdContainer );
		return toPane;
	}

	private void handleButtonAction(){
		copyBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				copyBtnAction(actionEvent);
			}
		});
		cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				cancelBtnAction(actionEvent);
			}
		});
	}

	private void copyBtnAction(ActionEvent event){
		try {
			if(validateFromPlantComboBox()) {
				this.errMsgText.setText("Please Select From Plant ");
				return;
			} else if(validateFromDepartmentComboBox()) {
				this.errMsgText.setText("Please Select From Department ");
				return;
			} else if(validateFromModelYearComboBox()) {
				this.errMsgText.setText("Please Select From Model Year ");
				return;
			} else if(validateFromProductionRateComboBox()) {
				this.errMsgText.setText("Please Select From Production Rate ");
				return;
			} else if(validateFromLineNoComboBox()) {
				this.errMsgText.setText("Please Select From Line Number ");
				return;
			} else if(validateFromVMCComboBox()) {
				this.errMsgText.setText("Please Select From VMC ");
				return;
			} else if(validateToPlantComboBox()) {
				this.errMsgText.setText("Please Select To Plant ");
				return;
			} else if(validateToDepartmentComboBox()) {
				this.errMsgText.setText("Please Select To Department ");
				return;
			} else if(validateToModelYearComboBox()) {
				this.errMsgText.setText("Please Select To Model Year ");
				return;
			} else if(validateToProductionRateComboBox()) {
				this.errMsgText.setText("Please Select To Production Rate ");
				return;
			} else if(validateToLineNoComboBox()) {
				this.errMsgText.setText("Please Select To Line Number ");
				return;
			} else if(validateToVMCComboBox()) {
				this.errMsgText.setText("Please Select To VMC ");
				return;
			}
			Map<String, String> unitNoMap = new HashMap<String, String>();
			progress.setVisible(true);
			resetLabelColor();
			disablePanel(true);
			copyOperationChecker(unitNoMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void resetLabelColor() {
		operationCheckerLabel.setTextFill(Paint.valueOf("black"));
		partCheckerLabel.setTextFill(Paint.valueOf("black"));
		measurementCheckerLabel.setTextFill(Paint.valueOf("black"));
	}

	@SuppressWarnings("unchecked")
	private void copyOperationChecker(final Map<String, String> unitMap) {
		
		mainTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				
				updateMessage("Waiting to copy Operation Checker...");
				
				List<MCOperationChecker> checkerList = new ArrayList<MCOperationChecker>();
				
				List<MCOperationChecker> fromOpChecker = model.loadOperationCheckerByPddaPlatform(getFromPlantValue(), getFromDepartmentValue(), new BigDecimal(getFromModelYearValue().replaceAll(",", "")), 
						new BigDecimal(getFromProductionRateValue().replaceAll(",", "")), getFromLineNoValue(), getFromVmcValue());
				int size = fromOpChecker.size();
				for(MCOperationChecker operationChecker : fromOpChecker) {
					MCPddaUnitRevision pddaUnitRevision = model.findByOpNameAndOpRev(operationChecker.getId().getOperationName(), operationChecker.getId().getOperationRevision());
					if(pddaUnitRevision != null) {
						unitMap.put(operationChecker.getId().getOperationName().trim(), pddaUnitRevision.getId().getUnitNo().trim());
						List<CopyCheckerDetailsDto> newPlatformOperationNameList = model.findAllByUnitNoAndPddaDetails(pddaUnitRevision.getId().getUnitNo(), getToPlantValue(), getToDepartmentValue(), 
								new BigDecimal(getToModelYearValue().replaceAll(",", "")),new BigDecimal(getToProductionRateValue().replaceAll(",", "")), getToLineNoValue(), getToVmcValue(), "OC");

						for (CopyCheckerDetailsDto copyDto : newPlatformOperationNameList) {
							List<MCOperationRevision> revisions = model.findAllByOperationAndRevisions(copyDto.getOperationName().trim(), null, true);
							if(revisions != null && revisions.size() > 0) {
								operationChecker.getId().setOperationName(copyDto.getOperationName().trim());
								operationChecker.getId().setOperationRevision(revisions.get(0).getId().getOperationRevision());
								if(model.findOperationCheckerById(operationChecker.getId()) == null) {
									checkerList.add(operationChecker);
								}
							}
						}
					}
				}
				boolean isCopied = false;
				if(checkerList.isEmpty()) {
					operationCheckerLabel.setTextFill(Paint.valueOf("darkorange"));
				}
				for(MCOperationChecker checker : checkerList) {	
					model.addOperationChecker(checker);
					progressCounter = progressCounter + 1;
					updateProgress(progressCounter, size);
					updateMessage("Copying Operation Checker...");
					isCopied = true;
				}
				if(isCopied) {
					operationCheckerLabel.setTextFill(Paint.valueOf("green"));
				}
				progressCounter = 0.0;
				updateProgress(0, size);
				return null;
			}
		};

		mainTask.setOnSucceeded(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				errorLabel.setText(StringUtils.EMPTY);
				copyPartChecker(unitMap);
			}
		});
		mainTask.setOnFailed(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				errorLabel.setText(CheckerConstants.COPY_ERROR_MSG+" Operation Checker");
				mainTask.getException().printStackTrace();
			}
		});
		progress.progressProperty().bind(mainTask.progressProperty());
		updateLabel.textProperty().bind(mainTask.messageProperty());
		new Thread(mainTask).start();
	}

	@SuppressWarnings("unchecked")
	private void copyPartChecker(final Map<String, String> unitMap) {
		
		mainTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				
				updateMessage("Waiting to copy Part Checker...");
				
				List<MCPartChecker> checkerList = new ArrayList<MCPartChecker>();
				
				List<PartCheckerDto> fromPartChecker = model.loadPartCheckerByPddaPlatform(getFromPlantValue(), getFromDepartmentValue(), new BigDecimal(getFromModelYearValue().replaceAll(",", "")), 
						new BigDecimal(getFromProductionRateValue().replaceAll(",", "")), getFromLineNoValue(), getFromVmcValue());
				int size = fromPartChecker.size();

				for (PartCheckerDto partCheckerDto : fromPartChecker) {
					String unitNumber = "";
					MCPartChecker partChecker = ConvertToEntity.convertToEntity(partCheckerDto);
					if(unitMap.containsKey(partCheckerDto.getOperationName().trim())) {
						unitNumber = unitMap.get(partCheckerDto.getOperationName()).toString();
					} else {
						MCPddaUnitRevision pddaUnitRevision = model.findByOpNameAndOpRev(partCheckerDto.getOperationName(), partCheckerDto.getOperationRevision());
						if(pddaUnitRevision != null) {
							unitNumber = pddaUnitRevision.getId().getUnitNo().trim();
							unitMap.put(partCheckerDto.getOperationName().trim(), unitNumber);
						}
					}
					List<CopyCheckerDetailsDto> newPlatformOperationNameList = model.findAllByUnitNoAndPddaDetails(unitNumber, getToPlantValue(), getToDepartmentValue(), 
							new BigDecimal(getToModelYearValue().replaceAll(",", "")),new BigDecimal(getToProductionRateValue().replaceAll(",", "")), getToLineNoValue(), getToVmcValue(), "PC");

					for (CopyCheckerDetailsDto copyDto : newPlatformOperationNameList) {
						if(partCheckerDto.getPartNo().equals(copyDto.getPartNo()) && 
								partCheckerDto.getPartItemNo().equals(copyDto.getPartItemNo()) && 
								partCheckerDto.getPartSectionCode().equals(copyDto.getPartSectionCode()) && 
								partCheckerDto.getPartType().equals(copyDto.getPartType())) {

							MCOperationPartRevision partRevision =  model.findAllByPartNoSecCodeItemNoAndType(copyDto.getOperationName(), copyDto.getPartNo(), 
									copyDto.getPartItemNo(), copyDto.getPartSectionCode(), PartType.get(copyDto.getPartType()));
							if(partRevision != null) {
								partChecker.getId().setOperationName(copyDto.getOperationName().trim());
								partChecker.getId().setOperationRevision(copyDto.getOperationRevision());
								partChecker.getId().setPartId(partRevision.getId().getPartId());
								if(model.findPartCheckerById(partChecker.getId()) == null) {
									checkerList.add(partChecker);
								}
							}
						}
					}
				}
				boolean isCopied = false;
				if(checkerList.isEmpty()) {
					partCheckerLabel.setTextFill(Paint.valueOf("darkorange"));
				}
				for(MCPartChecker checker : checkerList) {
					model.addPartChecker(checker);
					progressCounter = progressCounter + 1;
					updateProgress(progressCounter, size);
					updateMessage("Copying Part Checker...");
					isCopied = true;
				}
				if(isCopied) {
					partCheckerLabel.setTextFill(Paint.valueOf("green"));
				}
				progressCounter = 0.0;
				updateProgress(0, 0);
				return null;
			}
		};

		mainTask.setOnSucceeded(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				errorLabel.setText(StringUtils.EMPTY);
				copyMeasurementChecker(unitMap);
			}
		});
		mainTask.setOnFailed(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				errorLabel.setText(CheckerConstants.COPY_ERROR_MSG+" Part Checker");
				mainTask.getException().printStackTrace();
			}
		});
		progress.progressProperty().bind(mainTask.progressProperty());
		updateLabel.textProperty().bind(mainTask.messageProperty());
		new Thread(mainTask).start();
	}

	@SuppressWarnings("unchecked")
	private void copyMeasurementChecker(final Map<String, String> unitMap) {
		
		mainTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				
				updateMessage("Waiting to copy Measurement Checker...");
				
				List<MCMeasurementChecker> checkerList = new ArrayList<MCMeasurementChecker>();
				
				List<MeasurementCheckerDto> fromMeasurementChecker = model.loadMeasurementCheckerByPddaPlatform(getFromPlantValue(), getFromDepartmentValue(), new BigDecimal(getFromModelYearValue().replaceAll(",", "")), 
						new BigDecimal(getFromProductionRateValue().replaceAll(",", "")), getFromLineNoValue(), getFromVmcValue());
				int size = fromMeasurementChecker.size();

				for (MeasurementCheckerDto measCheckerDto : fromMeasurementChecker) {
					MCMeasurementChecker measurementChecker = ConvertToEntity.convertToEntity(measCheckerDto);
					String unitNumber = "";
					if(unitMap.containsKey(measCheckerDto.getOperationName().trim())) {
						unitNumber = unitMap.get(measCheckerDto.getOperationName()).toString();
					} else {
						MCPddaUnitRevision pddaUnitRevision = model.findByOpNameAndOpRev(measCheckerDto.getOperationName(), measCheckerDto.getOperationRevision());
						if(pddaUnitRevision != null) {
							unitNumber = pddaUnitRevision.getId().getUnitNo().trim();
							unitMap.put(measCheckerDto.getOperationName().trim(), unitNumber);
						}
					}
					List<CopyCheckerDetailsDto> newPlatformOperationNameList = model.findAllByUnitNoAndPddaDetails(unitNumber, getToPlantValue(), getToDepartmentValue(), 
							new BigDecimal(getToModelYearValue().replaceAll(",", "")),new BigDecimal(getToProductionRateValue().replaceAll(",", "")), getToLineNoValue(), getToVmcValue(), "MC");

					for (CopyCheckerDetailsDto copyDto : newPlatformOperationNameList) {
						if(measCheckerDto.getPartNo().equals(copyDto.getPartNo()) && 
								measCheckerDto.getPartItemNo().equals(copyDto.getPartItemNo()) && 
								measCheckerDto.getPartSectionCode().equals(copyDto.getPartSectionCode()) && 
								measCheckerDto.getPartType().equals(copyDto.getPartType()) &&
								measCheckerDto.getMinLimit() == copyDto.getMinLimit() && 
								measCheckerDto.getMaxLimit() == copyDto.getMaxLimit() &&
								measCheckerDto.getMeasurementSeqNum() == copyDto.getMeasurementSeqNum()) {
							measurementChecker.getId().setOperationName(copyDto.getOperationName().trim());
							measurementChecker.getId().setOperationRevision(copyDto.getOperationRevision());
							measurementChecker.getId().setPartId(copyDto.getPartId());
							if(model.findMeasurementCheckerById(measurementChecker.getId()) == null) {
								checkerList.add(measurementChecker);
							}
						}
					}
				}
				boolean isCopied = false;
				if(checkerList.isEmpty()) {
					measurementCheckerLabel.setTextFill(Paint.valueOf("darkorange"));
				}
				for(MCMeasurementChecker checker : checkerList) {
					model.addMeasurementChecker(checker);
					progressCounter = progressCounter + 1;
					updateProgress(progressCounter, size);
					updateMessage("Copying Measurement Checker...");
					isCopied = true;
				}
				if(isCopied) {
					measurementCheckerLabel.setTextFill(Paint.valueOf("green"));
				}
				progressCounter = 0.0;
				updateMessage("Copy Complete!");
				progress.setVisible(false);
				disablePanel(false);
				return null;
			}
		};

		mainTask.setOnSucceeded(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				errorLabel.setText(StringUtils.EMPTY);
			}
		});
		mainTask.setOnFailed(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				errorLabel.setText(CheckerConstants.COPY_ERROR_MSG+" Measurement Checker");
				mainTask.getException().printStackTrace();
			}
		});
		progress.progressProperty().bind(mainTask.progressProperty());
		updateLabel.textProperty().bind(mainTask.messageProperty());
		new Thread(mainTask).start();
	}

	private boolean validateFromPlantComboBox() {
		return fromPlantComboBox == null || fromPlantComboBox.getSelectionModel().getSelectedItem() == null;
	}

	private boolean validateFromDepartmentComboBox() {
		return fromDepartmentComboBox == null  || fromDepartmentComboBox.getSelectionModel().getSelectedItem() == null;
	}

	private boolean validateFromModelYearComboBox() {
		return fromModelYearComboBox == null  || fromModelYearComboBox.getSelectionModel().getSelectedItem() == null;
	}

	private boolean validateFromProductionRateComboBox() {
		return fromProductionRateComboBox == null  || fromProductionRateComboBox.getSelectionModel().getSelectedItem() == null;
	}

	private boolean validateFromLineNoComboBox() {
		return fromLineNumberComboBox == null  || fromLineNumberComboBox.getSelectionModel().getSelectedItem() == null;
	}

	private boolean validateFromVMCComboBox() {
		return fromVMCComboBox == null  || fromVMCComboBox.getSelectionModel().getSelectedItem() == null;
	}

	private boolean validateToPlantComboBox() {
		return toPlantComboBox == null  || toPlantComboBox.getSelectionModel().getSelectedItem() == null;
	}

	private boolean validateToDepartmentComboBox() {
		return toDepartmentComboBox == null  || toDepartmentComboBox.getSelectionModel().getSelectedItem() == null;
	}

	private boolean validateToModelYearComboBox() {
		return toModelYearComboBox == null  || toModelYearComboBox.getSelectionModel().getSelectedItem() == null;
	}

	private boolean validateToProductionRateComboBox() {
		return toProductionRateComboBox == null  || toProductionRateComboBox.getSelectionModel().getSelectedItem() == null;
	}

	private boolean validateToLineNoComboBox() {
		return toLineNumberComboBox == null  || toLineNumberComboBox.getSelectionModel().getSelectedItem() == null;
	}

	private boolean validateToVMCComboBox() {
		return toVMCComboBox == null  || toVMCComboBox.getSelectionModel().getSelectedItem() == null;
	}

	private void cancelBtnAction(ActionEvent event){
		LoggedButton cancelBtn = getCancelBtn();
		Stage stage = (Stage) cancelBtn.getScene().getWindow();
		stage.close();
	}

	private String getFromPlantValue() {
		return fromPlantComboBox.getSelectionModel().getSelectedItem() != null ? 
				fromPlantComboBox.getSelectionModel().getSelectedItem().toString() : "";
	}

	private String getFromDepartmentValue() {
		return fromDepartmentComboBox.getSelectionModel().getSelectedItem() != null ? 
				fromDepartmentComboBox.getSelectionModel().getSelectedItem().toString() : "";
	}

	private String getFromModelYearValue() {
		return fromModelYearComboBox.getSelectionModel().getSelectedItem() != null ? 
				fromModelYearComboBox.getSelectionModel().getSelectedItem().toString() : "";
	}

	private String getFromProductionRateValue() {
		return fromProductionRateComboBox.getSelectionModel().getSelectedItem() != null ? 
				fromProductionRateComboBox.getSelectionModel().getSelectedItem().toString() : "";
	}

	private String getFromVmcValue() {
		return fromVMCComboBox.getSelectionModel().getSelectedItem() != null ? 
				fromVMCComboBox.getSelectionModel().getSelectedItem().toString() : "";
	}

	private String getFromLineNoValue() {
		return fromLineNumberComboBox.getSelectionModel().getSelectedItem() != null ? 
				fromLineNumberComboBox.getSelectionModel().getSelectedItem().toString() : "";
	}

	private String getToPlantValue() {
		return toPlantComboBox.getSelectionModel().getSelectedItem() != null ? 
				toPlantComboBox.getSelectionModel().getSelectedItem().toString() : "";
	}

	private String getToDepartmentValue() {
		return toDepartmentComboBox.getSelectionModel().getSelectedItem() != null ? 
				toDepartmentComboBox.getSelectionModel().getSelectedItem().toString() : "";
	}

	private String getToModelYearValue() {
		return toModelYearComboBox.getSelectionModel().getSelectedItem() != null ? 
				toModelYearComboBox.getSelectionModel().getSelectedItem().toString() : "";
	}

	private String getToProductionRateValue() {
		return toProductionRateComboBox.getSelectionModel().getSelectedItem() != null ? 
				toProductionRateComboBox.getSelectionModel().getSelectedItem().toString() : "";
	}

	private String getToVmcValue() {
		return toVMCComboBox.getSelectionModel().getSelectedItem() != null ? 
				toVMCComboBox.getSelectionModel().getSelectedItem().toString() : "";
	}

	private String getToLineNoValue() {
		return toLineNumberComboBox.getSelectionModel().getSelectedItem() != null ? 
				toLineNumberComboBox.getSelectionModel().getSelectedItem().toString() : "";
	}

	public LoggedComboBox<String> createComboBox(LoggedComboBox<String> comboBox, String id) {
		if(comboBox == null){
			comboBox = new LoggedComboBox<String>(id);
			comboBox.setMaxWidth(150);
			comboBox.setMinWidth(150);
			comboBox.setMinHeight(25.0);
			comboBox.setMaxHeight(25.0);
			comboBox.setPrefWidth(100);
			comboBox.setStyle("-fx-font-size: 10pt; -fx-font-family: arial;");
		}
		return comboBox;
	}

	public LoggedTextField createTextField(LoggedTextField textField, float width) {
		if(textField == null){
			textField = new LoggedTextField();
			textField.setMaxWidth(width);
			textField.setMinWidth(width);
		}
		return textField;
	}

	private HBox createButton() {
		HBox viewBtnHBox = new HBox();
		copyBtn = createBtn("Copy");
		cancelBtn = createBtn("Cancel");
		viewBtnHBox.setPadding(new Insets(15));
		viewBtnHBox.getChildren().addAll(copyBtn, cancelBtn);
		viewBtnHBox.setPrefSize(250,20);
		viewBtnHBox.setSpacing(5);
		viewBtnHBox.setPadding(new Insets(10,0,0,20));
		HBox.setHgrow(viewBtnHBox, Priority.ALWAYS);
		viewBtnHBox.setAlignment(Pos.BOTTOM_CENTER);
		return viewBtnHBox;
	}

	private LoggedText createErrorMsgText() {
		if(errMsgText == null) {
			errMsgText = new LoggedText();
			errMsgText.setFont(Font.font ("Verdana", 16));
			errMsgText.setFill(Color.RED);
		}
		return errMsgText;
	}

	public LoggedButton createBtn(String text) {
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.defaultButtonProperty().bind(btn.focusedProperty());
		btn.setStyle("-fx-pref-height: 5px; -fx-pref-width: 100px; -fx-font-size : 11pt;");
		btn.getStyleClass().add("table-button");
		return btn;
	}

	private TitledPane createTitiledPane(String title, Node content) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setFont(Font.font("", FontWeight.BOLD, 14));
		titledPane.setContent(content);
		titledPane.setCollapsible(false);
		return titledPane;
	}

	public LoggedButton getCopyBtn() {
		return copyBtn;
	}

	public void setCopyBtn(LoggedButton copyBtn) {
		this.copyBtn = copyBtn;
	}

	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}

	public void setCancelBtn(LoggedButton cancelBtn) {
		this.cancelBtn = cancelBtn;
	}
	
	public void disablePanel(boolean isDisabled) {
		((BorderPane) this.getScene().getRoot()).getCenter().setDisable(isDisabled);
	}
}
