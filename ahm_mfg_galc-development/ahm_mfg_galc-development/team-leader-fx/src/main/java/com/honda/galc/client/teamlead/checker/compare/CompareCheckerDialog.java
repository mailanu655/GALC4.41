package com.honda.galc.client.teamlead.checker.compare;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.teamlead.checker.CheckerConfigModel;
import com.honda.galc.client.teamlead.checker.CheckerConstants;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedText;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.MeasurementCheckerDto;
import com.honda.galc.dto.OperationCheckerDto;
import com.honda.galc.dto.PartCheckerDto;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
/**
 * 
 * <h3>CompareCheckerDialog</h3>
 * <p> CompareCheckerDialog  Class description</p>
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
 * </TABLE>
 *   
 * @author Hemant Kumar<br>
 * Apr 30, 2018
 *
 */
public class CompareCheckerDialog extends FxDialog {

	private LabeledComboBox<String> fromPlantComboBox;
	private LabeledComboBox<String> fromDepartmentComboBox;
	private LabeledComboBox<String> fromModelYearComboBox;
	private LabeledComboBox<String> fromProductionRateComboBox;
	private LabeledComboBox<String> fromLineNumberComboBox;
	private LabeledComboBox<String> fromVMCComboBox;

	private LabeledComboBox<String> toPlantComboBox;
	private LabeledComboBox<String> toDepartmentComboBox;
	private LabeledComboBox<String> toModelYearComboBox;
	private LabeledComboBox<String> toProductionRateComboBox;
	private LabeledComboBox<String> toLineNumberComboBox;
	private LabeledComboBox<String> toVMCComboBox;

	private LoggedText errMsgText;
	private LoggedButton compareBtn;
	private LoggedButton cancelBtn;
	private CheckerConfigModel model;
	private StackPane stackPane;
	private VBox pddaPlatformContainer;
	private VBox comparisonContainer;
	private LoggedButton okBtn;

	private CompareOperationCheckers opCheckerCmp;
	private ComparePartCheckers partCheckerCmp;
	private CompareMeasurementCheckers measCheckerCmp;

	public CompareCheckerDialog(CheckerConfigModel model) {
		super("Compare Checker From PDDA Platform", ClientMainFx.getInstance().getStage());
		this.getScene().getStylesheets().add(CheckerConstants.CSS_PATH);
		this.model = model;
		initComponents();
	}

	private void initComponents() {
		stackPane = new StackPane();
		stackPane.setAlignment(Pos.CENTER);
		stackPane.setMinHeight(getScreenHeight() * 0.80);
		stackPane.setMaxHeight(getScreenHeight() * 0.80);
		stackPane.setMinWidth(getScreenWidth() * 0.91);
		stackPane.setMaxWidth(getScreenWidth() * 0.91);
		stackPane.setPadding(new Insets(10));

		pddaPlatformContainer = createPddaPlatformContainer();
		pddaPlatformContainer.setVisible(true);

		comparisonContainer = createComparisonContainer();
		comparisonContainer.setVisible(false);

		stackPane.getChildren().addAll(pddaPlatformContainer, comparisonContainer);
		((BorderPane) this.getScene().getRoot()).setCenter(stackPane);
		loadComboBox();
		handleButtonAction();
	}

	private VBox createPddaPlatformContainer(){
		VBox pane = new VBox();
		pane.setAlignment(Pos.CENTER);
		pane.setSpacing(10);
		pane.getChildren().addAll(createTitiledPane("From PDDA Platform", createFromPDDAContainer()), 
				createTitiledPane("To PDDA Platform", createToPDDAContainer()), createButton(), createErrorMsgText());
		return pane;
	}

	private VBox createComparisonContainer() {
		TabPane tabPane = new TabPane();
		opCheckerCmp = new CompareOperationCheckers();
		partCheckerCmp = new ComparePartCheckers();
		measCheckerCmp = new CompareMeasurementCheckers();

		Tab operationCheckerTab = new Tab("Operation Checker");
		operationCheckerTab.setClosable(false);
		operationCheckerTab.setContent(opCheckerCmp.getCheckerTable());

		Tab partCheckerTab = new Tab("Part Checker");
		partCheckerTab.setClosable(false);
		partCheckerTab.setContent(partCheckerCmp.getCheckerTable());

		Tab measCheckerTab = new Tab("Measurement Checker");
		measCheckerTab.setClosable(false);
		measCheckerTab.setContent(measCheckerCmp.getCheckerTable());

		tabPane.getTabs().addAll(operationCheckerTab, partCheckerTab, measCheckerTab);
		tabPane.minWidthProperty().bind(this.widthProperty());
		tabPane.maxWidthProperty().bind(this.widthProperty());
		tabPane.minHeightProperty().bind(this.heightProperty().multiply(0.88));
		tabPane.maxHeightProperty().bind(this.heightProperty().multiply(0.88));
		tabPane.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.0085 * getScreenWidth())));
		okBtn = createBtn("Ok");
		HBox buttonBox = new HBox(okBtn);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setPadding(new Insets(10));

		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(tabPane, buttonBox);
		vbox.setPadding(new Insets(20));
		return vbox;
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
		addFromVMCComboBoxListener();

		addToPlantComboBoxListener();
		addToDepartmentComboBoxListener();
		addToModelYearComboBoxListener();
		addToProductionRateComboBoxListener();
		addToLineNoComboBoxListener();
		addToVMCComboBoxListener();
	}

	private void loadFromPlantComboBox() {
		getFromPlantComboBox().getItems().clear();
		getFromPlantComboBox().setPromptText("Select");
		getFromPlantComboBox().getItems().addAll(model.loadFromPlant());
	}

	private void addFromPlantComboBoxListener() {
		getFromPlantComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				loadFromDepartmentComboBox(newValue);
				errMsgText.setText(StringUtils.EMPTY);
			} 
		});
	}

	private void loadFromDepartmentComboBox(String plant) {
		getFromDepartmentComboBox().getItems().clear();
		getFromModelYearComboBox().getItems().clear();
		getFromModelYearComboBox().setValue(null);
		getFromProductionRateComboBox().getItems().clear();
		getFromProductionRateComboBox().setValue(null);
		getFromLineNumberComboBox().getItems().clear();
		getFromVMCComboBox().getItems().clear();
		getFromLineNumberComboBox().setValue(null);
		getFromVMCComboBox().setValue(null);
		getFromDepartmentComboBox().setPromptText("Select");
		getFromDepartmentComboBox().getItems().addAll(model.loadFromDepartment(plant));
	}

	private void addFromDepartmentComboBoxListener() {
		getFromDepartmentComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				loadFromModelYearComboBox(newValue);
				errMsgText.setText(StringUtils.EMPTY);
			} 
		});
	}

	private void loadFromModelYearComboBox(String dept) {
		getFromModelYearComboBox().getItems().clear();
		getFromModelYearComboBox().setValue(null);
		getFromProductionRateComboBox().getItems().clear();
		getFromProductionRateComboBox().setValue(null);
		getFromLineNumberComboBox().getItems().clear();
		getFromVMCComboBox().getItems().clear();
		getFromLineNumberComboBox().setValue(null);
		getFromVMCComboBox().setValue(null);
		getFromModelYearComboBox().setPromptText("Select");
		String plant = getFromPlantComboBox() != null ? getFromPlantComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
		for (Long modelYear : model.loadFromModelYear(plant, dept)) {
			getFromModelYearComboBox().getItems().add(modelYear.toString());
		}
	}

	private void addFromModelYearComboBoxListener() {
		getFromModelYearComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				loadFromProductionRateComboBox(newValue);
				errMsgText.setText(StringUtils.EMPTY);
			} 
		});
	}

	private void loadFromProductionRateComboBox(String modelYear) {
		getFromProductionRateComboBox().getItems().clear();
		getFromProductionRateComboBox().setValue(null);
		getFromLineNumberComboBox().getItems().clear();
		getFromVMCComboBox().getItems().clear();
		getFromLineNumberComboBox().setValue(null);
		getFromVMCComboBox().setValue(null);
		getFromProductionRateComboBox().setPromptText("Select");
		String plant = getFromPlantComboBox() != null ? getFromPlantComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
		String dept = getFromDepartmentComboBox() != null ? getFromDepartmentComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
		for (Long l : model.loadFromProductionRate(plant, dept, 
				new BigDecimal(modelYear.replaceAll(",", StringUtils.EMPTY)))) {
			getFromProductionRateComboBox().getItems().add(l.toString());
		}
	}

	private void addFromProductionRateComboBoxListener() {
		getFromProductionRateComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				loadFromLineNoComboBox(newValue);
				errMsgText.setText(StringUtils.EMPTY);
			} 
		});
	}

	private void loadFromLineNoComboBox(String productionRate) {
		getFromLineNumberComboBox().getItems().clear();
		getFromVMCComboBox().getItems().clear();
		getFromLineNumberComboBox().setValue(null);
		getFromVMCComboBox().setValue(null);
		getFromLineNumberComboBox().setPromptText("Select");
		getFromVMCComboBox().getItems().clear();
		String plant = getFromPlantComboBox() != null  && getFromDepartmentComboBox().getSelectionModel().getSelectedItem() != null ? 
				getFromPlantComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
				String dept = getFromDepartmentComboBox() != null && getFromDepartmentComboBox().getSelectionModel().getSelectedItem() != null ? 
						getFromDepartmentComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
						String modelYear = getFromModelYearComboBox() != null && getFromModelYearComboBox().getSelectionModel().getSelectedItem() != null ? 
								getFromModelYearComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
								if(!StringUtils.isBlank(modelYear))
									getFromLineNumberComboBox().getItems().addAll(model.loadFromLineNo(plant, dept, 
											new BigDecimal(modelYear.replaceAll(",", StringUtils.EMPTY)), new BigDecimal(productionRate.replaceAll(",", StringUtils.EMPTY))));
	}

	private void addFromLineNoComboBoxListener() {
		getFromLineNumberComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				loadFromVMCComboBox(newValue);
				errMsgText.setText(StringUtils.EMPTY);
			} 
		});
	}

	private void loadFromVMCComboBox(String lineNo) {
		getFromVMCComboBox().getItems().clear();
		getFromVMCComboBox().setValue(null);
		getFromVMCComboBox().setPromptText("Select");
		String plant = getFromPlantComboBox() != null  && getFromPlantComboBox().getSelectionModel().getSelectedItem() != null ? 
				getFromPlantComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
				String dept = getFromDepartmentComboBox() != null  && getFromDepartmentComboBox().getSelectionModel().getSelectedItem() != null ? 
						getFromDepartmentComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
						String modelYear = getFromModelYearComboBox() != null  && getFromModelYearComboBox().getSelectionModel().getSelectedItem() != null ? 
								getFromModelYearComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
								String productionRate = getFromProductionRateComboBox() != null  && getFromProductionRateComboBox().getSelectionModel().getSelectedItem() != null ? 
										getFromProductionRateComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
										if(!StringUtils.isBlank(modelYear) && !StringUtils.isBlank(productionRate))
											getFromVMCComboBox().getItems().addAll(model.loadFromVMC(plant, dept, 
													new BigDecimal(modelYear.replaceAll(",", StringUtils.EMPTY)), new BigDecimal(productionRate.replaceAll(",", StringUtils.EMPTY)), lineNo));
	}

	private void addFromVMCComboBoxListener() {
		getFromVMCComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				errMsgText.setText(StringUtils.EMPTY);
			} 
		});
	}

	private void loadToPlantComboBox() {
		getToPlantComboBox().getItems().clear();
		getToPlantComboBox().setPromptText("Select");
		getToPlantComboBox().getItems().addAll(model.loadFromPlant());
	}

	private void addToPlantComboBoxListener() {
		getToPlantComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				loadToDepartmentComboBox(newValue);
				errMsgText.setText(StringUtils.EMPTY);
			} 
		});
	}

	private void loadToDepartmentComboBox(String plant) {
		getToDepartmentComboBox().getItems().clear();
		getToModelYearComboBox().getItems().clear();
		getToModelYearComboBox().setValue(null);
		getToProductionRateComboBox().getItems().clear();
		getToProductionRateComboBox().setValue(null);
		getToLineNumberComboBox().getItems().clear();
		getToVMCComboBox().getItems().clear();
		getToLineNumberComboBox().setValue(null);
		getToVMCComboBox().setValue(null);
		getToDepartmentComboBox().setPromptText("Select");
		getToDepartmentComboBox().getItems().addAll(model.loadFromDepartment(plant));

	}

	private void addToDepartmentComboBoxListener() {
		getToDepartmentComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				loadToModelYearComboBox(newValue);
				errMsgText.setText(StringUtils.EMPTY);
			} 
		});
	}

	private void loadToModelYearComboBox(String dept) {
		getToModelYearComboBox().getItems().clear();
		getToModelYearComboBox().setValue(null);
		getToProductionRateComboBox().getItems().clear();
		getToProductionRateComboBox().setValue(null);
		getToLineNumberComboBox().getItems().clear();
		getToVMCComboBox().getItems().clear();
		getToLineNumberComboBox().setValue(null);
		getToVMCComboBox().setValue(null);
		getToModelYearComboBox().setPromptText("Select");
		String plant = getToPlantComboBox() != null ? getToPlantComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
		for (Long l : model.loadFromModelYear(plant, dept)) {
			getToModelYearComboBox().getItems().add(l.toString());
		}
	}

	private void addToModelYearComboBoxListener() {
		getToModelYearComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				loadToProductionRateComboBox(newValue);
				errMsgText.setText(StringUtils.EMPTY);
			} 
		});
	}

	private void loadToProductionRateComboBox(String modelYear) {
		getToProductionRateComboBox().getItems().clear();
		getToProductionRateComboBox().setValue(null);
		getToLineNumberComboBox().getItems().clear();
		getToVMCComboBox().getItems().clear();
		getToLineNumberComboBox().setValue(null);
		getToVMCComboBox().setValue(null);
		getToProductionRateComboBox().setPromptText("Select");
		String plant = getToPlantComboBox() != null ? getToPlantComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
		String dept = getToDepartmentComboBox() != null ? getToDepartmentComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
		if(modelYear != null) {
			for (Long l : model.loadFromProductionRate(plant, dept, 
					new BigDecimal(modelYear.replaceAll(",", StringUtils.EMPTY)))) {
				getToProductionRateComboBox().getItems().add(l.toString());
			}
		}
	}

	private void addToProductionRateComboBoxListener() {
		getToProductionRateComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				loadToLineNoComboBox(newValue);
				errMsgText.setText(StringUtils.EMPTY);
			} 
		});
	}

	private void loadToLineNoComboBox(String productionRate) {
		getToLineNumberComboBox().getItems().clear();
		getToVMCComboBox().getItems().clear();
		getToLineNumberComboBox().setValue(null);
		getToVMCComboBox().setValue(null);
		getToLineNumberComboBox().setPromptText("Select");
		String plant = getToPlantComboBox() != null  && getToDepartmentComboBox().getSelectionModel().getSelectedItem() != null ? 
				getToPlantComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
				String dept = getToDepartmentComboBox() != null && getToDepartmentComboBox().getSelectionModel().getSelectedItem() != null ? 
						getToDepartmentComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
						String modelYear = getToModelYearComboBox() != null && getToModelYearComboBox().getSelectionModel().getSelectedItem() != null ? 
								getToModelYearComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
								if(!StringUtils.isBlank(modelYear))
									getToLineNumberComboBox().getItems().addAll(model.loadFromLineNo(plant, dept, 
											new BigDecimal(modelYear.replaceAll(",", StringUtils.EMPTY)), new BigDecimal(productionRate.replaceAll(",", StringUtils.EMPTY))));
	}

	private void addToLineNoComboBoxListener() {
		getToLineNumberComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				loadToVMCComboBox(newValue);
				errMsgText.setText(StringUtils.EMPTY);
			} 
		});
	}

	private void loadToVMCComboBox(String lineNo) {
		getToVMCComboBox().getItems().clear();
		getToVMCComboBox().setValue(null);
		getToVMCComboBox().setPromptText("Select");
		String plant = getToPlantComboBox() != null  && getToPlantComboBox().getSelectionModel().getSelectedItem() != null ? 
				getToPlantComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
				String dept = getToDepartmentComboBox() != null  && getToDepartmentComboBox().getSelectionModel().getSelectedItem() != null ? 
						getToDepartmentComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
						String modelYear = getToModelYearComboBox() != null  && getToModelYearComboBox().getSelectionModel().getSelectedItem() != null ? 
								getToModelYearComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
								String productionRate = getToProductionRateComboBox() != null  && getToProductionRateComboBox().getSelectionModel().getSelectedItem() != null ? 
										getToProductionRateComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
										if(!StringUtils.isBlank(modelYear) && !StringUtils.isBlank(productionRate))
											getToVMCComboBox().getItems().addAll(model.loadFromVMC(plant, dept, 
													new BigDecimal(modelYear.replaceAll(",", StringUtils.EMPTY)), new BigDecimal(productionRate.replaceAll(",", StringUtils.EMPTY)), lineNo));
	}

	private void addToVMCComboBoxListener() {
		getToVMCComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				errMsgText.setText(StringUtils.EMPTY);
			} 
		});
	}

	private GridPane createFromPDDAContainer(){
		GridPane mainPane = new GridPane();

		fromPlantComboBox = createLabeledComboBox("fromPlantComboBox", "Plant", true, true, false);
		fromDepartmentComboBox = createLabeledComboBox("fromDepartmentComboBox", "Department", true, true, false);

		fromModelYearComboBox = createLabeledComboBox("fromModelYearComboBox", "Model Year", true, true, false);
		fromProductionRateComboBox = createLabeledComboBox("fromProductionRateComboBox", "Production Rate", true, true, false);

		fromLineNumberComboBox = createLabeledComboBox("fromLineNumberComboBox", "Line Number", true, true, false);
		fromVMCComboBox = createLabeledComboBox("fromVMCComboBox", "VMC", true, true, false);

		mainPane.add(fromPlantComboBox, 0, 0);
		mainPane.add(fromDepartmentComboBox, 1, 0);

		mainPane.add(fromModelYearComboBox, 0, 1);
		mainPane.add(fromProductionRateComboBox, 1, 1);

		mainPane.add(fromLineNumberComboBox, 0, 2);
		mainPane.add(fromVMCComboBox, 1, 2);

		mainPane.setHgap(10);
		mainPane.setVgap(10);
		return mainPane;
	}

	private GridPane createToPDDAContainer(){
		GridPane mainPane = new GridPane();

		toPlantComboBox = createLabeledComboBox("toPlantComboBox", "Plant", true, true, false);
		toDepartmentComboBox = createLabeledComboBox("toDepartmentComboBox", "Department", true, true, false);

		toModelYearComboBox = createLabeledComboBox("toModelYearComboBox", "Model Year", true, true, false);
		toProductionRateComboBox = createLabeledComboBox("toProductionRateComboBox", "Production Rate", true, true, false);

		toLineNumberComboBox = createLabeledComboBox("toLineNumberComboBox", "Line Number", true, true, false);
		toVMCComboBox = createLabeledComboBox("toVMCComboBox", "VMC", true, true, false);

		mainPane.add(toPlantComboBox, 0, 0);
		mainPane.add(toDepartmentComboBox, 1, 0);

		mainPane.add(toModelYearComboBox, 0, 1);
		mainPane.add(toProductionRateComboBox, 1, 1);

		mainPane.add(toLineNumberComboBox, 0, 2);
		mainPane.add(toVMCComboBox, 1, 2);

		mainPane.setHgap(10);
		mainPane.setVgap(10);
		return mainPane;
	}

	private void handleButtonAction(){
		compareBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				compareBtnAction(actionEvent);
			}
		});
		cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				cancelBtnAction(actionEvent);
			}
		});
		okBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				okBtnAction();
			}
		});
	}

	private void okBtnAction() {
		opCheckerCmp.getCheckerTable().getItems().clear();
		partCheckerCmp.getCheckerTable().getItems().clear();
		measCheckerCmp.getCheckerTable().getItems().clear();
		pddaPlatformContainer.setVisible(true);
		pddaPlatformContainer.toFront();
		comparisonContainer.setVisible(false);
		comparisonContainer.toBack();
	}

	private void compareBtnAction(ActionEvent event){
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
			loadComparisionData();
			pddaPlatformContainer.setVisible(false);
			pddaPlatformContainer.toBack();
			comparisonContainer.setVisible(true);
			comparisonContainer.toFront();

		} catch (Exception e) {
			Logger.getLogger().error(e, new LogRecord("An exception occured in compareBtnAction() method"));
		}
	}

	private void loadComparisionData() {
		List<OperationCheckerDto> fromOpCheckerList = new ArrayList<OperationCheckerDto>(CompareCheckerUtil.getOpCheckerDtoListFromEntityList(
				model.loadOperationCheckerByPddaPlatform(
						getFromPlantValue(), getFromDepartmentValue(), new BigDecimal(getFromModelYearValue().replaceAll(",", StringUtils.EMPTY)), 
						new BigDecimal(getFromProductionRateValue().replaceAll(",", StringUtils.EMPTY)), getFromLineNoValue(), getFromVmcValue())));
		List<OperationCheckerDto> toOpCheckerList = new ArrayList<OperationCheckerDto>(CompareCheckerUtil.getOpCheckerDtoListFromEntityList(
				model.loadOperationCheckerByPddaPlatform(
						getToPlantValue(), getToDepartmentValue(), new BigDecimal(getToModelYearValue().replaceAll(",", StringUtils.EMPTY)), 
						new BigDecimal(getToProductionRateValue().replaceAll(",", StringUtils.EMPTY)), getToLineNoValue(), getToVmcValue())));

		List<PartCheckerDto> fromPartCheckerList = new ArrayList<PartCheckerDto>(
				model.loadPartCheckerByPddaPlatform(
						getFromPlantValue(), getFromDepartmentValue(), new BigDecimal(getFromModelYearValue().replaceAll(",", StringUtils.EMPTY)), 
						new BigDecimal(getFromProductionRateValue().replaceAll(",", StringUtils.EMPTY)), getFromLineNoValue(), getFromVmcValue()));
		List<PartCheckerDto> toPartCheckerList = new ArrayList<PartCheckerDto>(
				model.loadPartCheckerByPddaPlatform(
						getToPlantValue(), getToDepartmentValue(), new BigDecimal(getToModelYearValue().replaceAll(",", StringUtils.EMPTY)), 
						new BigDecimal(getToProductionRateValue().replaceAll(",", StringUtils.EMPTY)), getToLineNoValue(), getToVmcValue()));

		List<MeasurementCheckerDto> fromMeasCheckerDtoList = new ArrayList<MeasurementCheckerDto>(
				model.loadMeasurementCheckerByPddaPlatform(
						getFromPlantValue(), getFromDepartmentValue(), new BigDecimal(getFromModelYearValue().replaceAll(",", StringUtils.EMPTY)), 
						new BigDecimal(getFromProductionRateValue().replaceAll(",", StringUtils.EMPTY)), getFromLineNoValue(), getFromVmcValue()));
		List<MeasurementCheckerDto> toMeasCheckerDtoList = new ArrayList<MeasurementCheckerDto>(
				model.loadMeasurementCheckerByPddaPlatform(
						getToPlantValue(), getToDepartmentValue(), new BigDecimal(getToModelYearValue().replaceAll(",", StringUtils.EMPTY)), 
						new BigDecimal(getToProductionRateValue().replaceAll(",", StringUtils.EMPTY)), getToLineNoValue(), getToVmcValue()));

		opCheckerCmp.populateTable(new OperationMapperConverter(fromOpCheckerList, toOpCheckerList).getMapperList());
		partCheckerCmp.populateTable(new PartMapperConverter(fromPartCheckerList, toPartCheckerList).getMapperList());
		measCheckerCmp.populateTable(new MeasurementMapperConverter(fromMeasCheckerDtoList, toMeasCheckerDtoList).getMapperList());
		changeDiffColText();
	}
	
	private void changeDiffColText() {
		String colText = "Plant: "+getFromPlantValue()+", Dept: "+getFromDepartmentValue()+", Year: "+getFromModelYearValue()+", Qty: "+getFromProductionRateValue()+", Line: "+getFromLineNoValue()+", VMC: "+getFromVmcValue()
				+ "     v/s     "
				+ "Plant: "+getToPlantValue()+", Dept: "+getToDepartmentValue()+", Year: "+getToModelYearValue()+", Qty: "+getToProductionRateValue()+", Line: "+getToLineNoValue()+", VMC: "+getToVmcValue();
		opCheckerCmp.getDiffCol().setText(colText);
		partCheckerCmp.getDiffCol().setText(colText);
		measCheckerCmp.getDiffCol().setText(colText);
	}

	private boolean validateFromPlantComboBox() {
		return getFromPlantComboBox() == null || getFromPlantComboBox().getSelectionModel().getSelectedItem() == null;
	}

	private boolean validateFromDepartmentComboBox() {
		return getFromDepartmentComboBox() == null  || getFromDepartmentComboBox().getSelectionModel().getSelectedItem() == null;
	}

	private boolean validateFromModelYearComboBox() {
		return getFromModelYearComboBox() == null  || getFromModelYearComboBox().getSelectionModel().getSelectedItem() == null;
	}

	private boolean validateFromProductionRateComboBox() {
		return getFromProductionRateComboBox() == null  || getFromProductionRateComboBox().getSelectionModel().getSelectedItem() == null;
	}

	private boolean validateFromLineNoComboBox() {
		return getFromLineNumberComboBox() == null  || getFromLineNumberComboBox().getSelectionModel().getSelectedItem() == null;
	}

	private boolean validateFromVMCComboBox() {
		return getFromVMCComboBox() == null  || getFromVMCComboBox().getSelectionModel().getSelectedItem() == null;
	}

	private boolean validateToPlantComboBox() {
		return getToPlantComboBox() == null  || getToPlantComboBox().getSelectionModel().getSelectedItem() == null;
	}

	private boolean validateToDepartmentComboBox() {
		return getToDepartmentComboBox() == null  || getToDepartmentComboBox().getSelectionModel().getSelectedItem() == null;
	}

	private boolean validateToModelYearComboBox() {
		return getToModelYearComboBox() == null  || getToModelYearComboBox().getSelectionModel().getSelectedItem() == null;
	}

	private boolean validateToProductionRateComboBox() {
		return getToProductionRateComboBox() == null  || getToProductionRateComboBox().getSelectionModel().getSelectedItem() == null;
	}

	private boolean validateToLineNoComboBox() {
		return getToLineNumberComboBox() == null  || getToLineNumberComboBox().getSelectionModel().getSelectedItem() == null;
	}

	private boolean validateToVMCComboBox() {
		return getToVMCComboBox() == null  || getToVMCComboBox().getSelectionModel().getSelectedItem() == null;
	}

	private void cancelBtnAction(ActionEvent event){
		Stage stage = (Stage) cancelBtn.getScene().getWindow();
		stage.close();
	}

	private String getFromPlantValue() {
		return getFromPlantComboBox().getSelectionModel().getSelectedItem() != null ? 
				getFromPlantComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
	}

	private String getFromDepartmentValue() {
		return getFromDepartmentComboBox().getSelectionModel().getSelectedItem() != null ? 
				getFromDepartmentComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
	}

	private String getFromModelYearValue() {
		return getFromModelYearComboBox().getSelectionModel().getSelectedItem() != null ? 
				getFromModelYearComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
	}

	private String getFromProductionRateValue() {
		return getFromProductionRateComboBox().getSelectionModel().getSelectedItem() != null ? 
				getFromProductionRateComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
	}

	private String getFromVmcValue() {
		return getFromVMCComboBox().getSelectionModel().getSelectedItem() != null ? 
				getFromVMCComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
	}

	private String getFromLineNoValue() {
		return getFromLineNumberComboBox().getSelectionModel().getSelectedItem() != null ? 
				getFromLineNumberComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
	}

	private String getToPlantValue() {
		return getToPlantComboBox().getSelectionModel().getSelectedItem() != null ? 
				getToPlantComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
	}

	private String getToDepartmentValue() {
		return getToDepartmentComboBox().getSelectionModel().getSelectedItem() != null ? 
				getToDepartmentComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
	}

	private String getToModelYearValue() {
		return getToModelYearComboBox().getSelectionModel().getSelectedItem() != null ? 
				getToModelYearComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
	}

	private String getToProductionRateValue() {
		return getToProductionRateComboBox().getSelectionModel().getSelectedItem() != null ? 
				getToProductionRateComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
	}

	private String getToVmcValue() {
		return getToVMCComboBox().getSelectionModel().getSelectedItem() != null ? 
				getToVMCComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
	}

	private String getToLineNoValue() {
		return getToLineNumberComboBox().getSelectionModel().getSelectedItem() != null ? 
				getToLineNumberComboBox().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
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
		compareBtn = createBtn("Compare");
		cancelBtn = createBtn("Cancel");
		viewBtnHBox.setPadding(new Insets(15));
		viewBtnHBox.getChildren().addAll(compareBtn, cancelBtn);
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
		btn.setMinWidth(100);
		btn.setStyle("-fx-pref-height: 5px; -fx-pref-width: 100px; -fx-font-size : 11pt;");
		btn.getStyleClass().add("table-button");
		return btn;
	}

	private TitledPane createTitiledPane(String title, Node content) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setFont(Font.font(StringUtils.EMPTY, FontWeight.BOLD, 12));
		titledPane.setContent(content);
		titledPane.setCollapsible(false);
		titledPane.setContentDisplay(ContentDisplay.CENTER);
		titledPane.setMinWidth(getScreenWidth() * 0.45);
		titledPane.setMaxWidth(getScreenWidth() * 0.45);
		return titledPane;
	}

	/**
	 * This method is used to create Labeled Combobox
	 */
	private LabeledComboBox<String> createLabeledComboBox(String id, String labelName, boolean isHorizontal, boolean isMandatory, boolean isDisabled) {
		LabeledComboBox<String> comboBox = new LabeledComboBox<String>(labelName,isHorizontal,new Insets(0),true,isMandatory);
		comboBox.setId(id);
		comboBox.getControl().setMinHeight(30);
		comboBox.getControl().setMinWidth(200);
		comboBox.getControl().getStyleClass().add("combo-box-base");
		comboBox.getControl().setDisable(isDisabled);
		comboBox.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.009 * getScreenWidth())));
		return comboBox;
	}

	public double getScreenWidth() {
		return Screen.getPrimary().getVisualBounds().getWidth();
	}
	public double getScreenHeight() {
		return Screen.getPrimary().getVisualBounds().getHeight();
	}

	public void disablePanel(boolean isDisabled) {
		((BorderPane) this.getScene().getRoot()).getCenter().setDisable(isDisabled);
	}

	public ComboBox<String> getFromPlantComboBox() {
		return fromPlantComboBox.getControl();
	}

	public ComboBox<String> getFromDepartmentComboBox() {
		return fromDepartmentComboBox.getControl();
	}

	public ComboBox<String> getFromModelYearComboBox() {
		return fromModelYearComboBox.getControl();
	}

	public ComboBox<String> getFromProductionRateComboBox() {
		return fromProductionRateComboBox.getControl();
	}

	public ComboBox<String> getFromLineNumberComboBox() {
		return fromLineNumberComboBox.getControl();
	}

	public ComboBox<String> getFromVMCComboBox() {
		return fromVMCComboBox.getControl();
	}

	public ComboBox<String> getToPlantComboBox() {
		return toPlantComboBox.getControl();
	}

	public ComboBox<String> getToDepartmentComboBox() {
		return toDepartmentComboBox.getControl();
	}

	public ComboBox<String> getToModelYearComboBox() {
		return toModelYearComboBox.getControl();
	}

	public ComboBox<String> getToProductionRateComboBox() {
		return toProductionRateComboBox.getControl();
	}

	public ComboBox<String> getToLineNumberComboBox() {
		return toLineNumberComboBox.getControl();
	}

	public ComboBox<String> getToVMCComboBox() {
		return toVMCComboBox.getControl();
	}
}
