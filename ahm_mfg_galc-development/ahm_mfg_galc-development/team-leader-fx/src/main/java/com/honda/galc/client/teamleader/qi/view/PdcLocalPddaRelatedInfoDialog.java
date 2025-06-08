package com.honda.galc.client.teamleader.qi.view;

import javafx.geometry.Insets;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.honda.galc.client.teamleader.qi.controller.PdcLocalAttributeMaintDialogController;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedText;
import com.honda.galc.client.utils.UiFactory;
/**   
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
public class PdcLocalPddaRelatedInfoDialog extends TitledPane {

	private static PdcLocalPddaRelatedInfoDialog instance;
	private static PdcLocalAttributeMaintDialogController attributeMaintDialogController;
	
	private LoggedComboBox<String> modelYearComboBox;
	private LoggedComboBox<String> vehicleModelCodeComboBox;
	private LoggedComboBox<String> processNumberComboBox;
	private LoggedComboBox<String> unitNumberComboBox;
	private LoggedText unitDescriptionText;
	private TitledPane titledPane;
	
	private PdcLocalPddaRelatedInfoDialog() {}
	
	public static PdcLocalPddaRelatedInfoDialog getInstance(PdcLocalAttributeMaintDialogController dialogController) {
		if(instance == null) {
			instance = new PdcLocalPddaRelatedInfoDialog();
		}
		attributeMaintDialogController = dialogController;
		return instance;
	}

	/**
	 * Creates the pdda related info.
	 *
	 * @return the titled pane
	 */
	public TitledPane createPddaRelatedInfo() {
		titledPane = new TitledPane();
        titledPane.setText("PDDA Related Info");
		VBox vBoxPddaRelatedInfo = new VBox();
		vBoxPddaRelatedInfo.maxWidth(450);
		vBoxPddaRelatedInfo.minWidth(450);
		vBoxPddaRelatedInfo.getChildren().addAll(createModelYearComboBox(), createVehicleModelCodeComboBox(), createProcessNumberComboBox(), createUnitNumberComboBox(), createUnitDescriptionTextArea());
		titledPane.setContent(vBoxPddaRelatedInfo);
		return titledPane; 
	}
	
	/**
	 * Creates the model year combo box.
	 *
	 * @return the h box
	 */
	private HBox createModelYearComboBox() {
		HBox modelYearContainer = new HBox();
		modelYearComboBox = new LoggedComboBox<String>("modelYearComboBox"); 
		modelYearComboBox.getStyleClass().add("combo-box-base");
		modelYearComboBox.setMinWidth(350.0);
		modelYearComboBox.setMaxWidth(350.0);
		modelYearComboBox.setMinHeight(30.0);
		modelYearComboBox.setMaxHeight(30.0);
	
		HBox labelBox = new HBox();
		LoggedLabel modelyearLabel = UiFactory.createLabel("modelyearLabel", "Model Year");
		LoggedLabel asterisk = UiFactory.createLabel("label", "*");
		asterisk.setStyle("-fx-text-fill: red");
		labelBox.getChildren().addAll(modelyearLabel, asterisk);
		labelBox.setMaxWidth(120.0);
		labelBox.setPrefWidth(120.0);

		modelYearContainer.setPadding(new Insets(10, 0, 0, 10));
		modelYearContainer.setSpacing(10);
		modelYearContainer.setMaxWidth(500);
		modelYearContainer.setMinWidth(500);
		modelYearContainer.getChildren().addAll(labelBox, modelYearComboBox);
		return modelYearContainer;
	}
	
	/**
	 * Creates the vehicle model code combo box.
	 *
	 * @return the h box
	 */
	private HBox createVehicleModelCodeComboBox() {
		HBox vehicleModelCodeContainer = new HBox();
		vehicleModelCodeComboBox = new LoggedComboBox<String>("vehicleModelCodeComboBox"); 
		vehicleModelCodeComboBox.getStyleClass().add("combo-box-base");
		vehicleModelCodeComboBox.setMinWidth(350.0);
		vehicleModelCodeComboBox.setMaxWidth(350.0);
		vehicleModelCodeComboBox.setMinHeight(30.0);
		vehicleModelCodeComboBox.setMaxHeight(30.0);
	
		HBox labelBox = new HBox();
		LoggedLabel vehicleModelCodeLabel = UiFactory.createLabel("modelyearLabel", "Vehicle Model Code");
		LoggedLabel asterisk = UiFactory.createLabel("label", "*");
		asterisk.setStyle("-fx-text-fill: red");
		labelBox.getChildren().addAll(vehicleModelCodeLabel, asterisk);
		labelBox.setMaxWidth(120.0);
		labelBox.setPrefWidth(120.0);

		vehicleModelCodeContainer.setPadding(new Insets(10, 0, 0, 10));
		vehicleModelCodeContainer.setSpacing(10);
		vehicleModelCodeContainer.getChildren().addAll(labelBox, vehicleModelCodeComboBox);
		return vehicleModelCodeContainer;
	}
	
	/**
	 * Creates the process number combo box.
	 *
	 * @return the h box
	 */
	private HBox createProcessNumberComboBox() {
		HBox processNumberContainer = new HBox();
		processNumberComboBox = new LoggedComboBox<String>("processNumberComboBox"); 
		processNumberComboBox.getStyleClass().add("combo-box-base");
		processNumberComboBox.setMinWidth(350.0);
		processNumberComboBox.setMaxWidth(350.0);
		processNumberComboBox.setMinHeight(30.0);
		processNumberComboBox.setMaxHeight(30.0);
	
		HBox labelBox = new HBox();
		LoggedLabel processNumberLabel = UiFactory.createLabel("processNumberLabel", "Process Number");
		LoggedLabel asterisk = UiFactory.createLabel("label", "*");
		asterisk.setStyle("-fx-text-fill: red");
		labelBox.getChildren().addAll(processNumberLabel, asterisk);
		labelBox.setMaxWidth(120.0);
		labelBox.setPrefWidth(120.0);

		processNumberContainer.setPadding(new Insets(10, 0, 0, 10));
		processNumberContainer.setSpacing(10);
		processNumberContainer.getChildren().addAll(labelBox, processNumberComboBox);
		return processNumberContainer;
	}
	
	/**
	 * Creates the unit number combo box.
	 *
	 * @return the hbox
	 */
	private HBox createUnitNumberComboBox() {
		HBox unitNumberContainer = new HBox();
		unitNumberComboBox = new LoggedComboBox<String>("unitNumberComboBox"); 
		unitNumberComboBox.getStyleClass().add("combo-box-base");
		unitNumberComboBox.setMinWidth(350.0);
		unitNumberComboBox.setMaxWidth(350.0);
		unitNumberComboBox.setMinHeight(30.0);
		unitNumberComboBox.setMaxHeight(30.0);
	
		HBox labelBox = new HBox();
		LoggedLabel unitNumberLabel = UiFactory.createLabel("unitNumberLabel", "Unit Number");
		LoggedLabel asterisk = UiFactory.createLabel("label", "*");
		asterisk.setStyle("-fx-text-fill: red");
		labelBox.getChildren().addAll(unitNumberLabel, asterisk);
		labelBox.setMaxWidth(120.0);
		labelBox.setPrefWidth(120.0);

		unitNumberContainer.setPadding(new Insets(10, 0, 0, 10));
		unitNumberContainer.setSpacing(10);
		unitNumberContainer.getChildren().addAll(labelBox, unitNumberComboBox);
		return unitNumberContainer;
	}
	
	/**
	 * Creates the unit description text area.
	 *
	 * @return the h box
	 */
	private HBox createUnitDescriptionTextArea() {
		HBox unitContainer = new HBox();
		unitDescriptionText  = UiFactory.createText();
		unitDescriptionText.setWrappingWidth(300);
	
		LoggedLabel unitDescriptionLabel = UiFactory.createLabel("label", "Unit Description");
		unitDescriptionLabel.setMaxWidth(120.0);
		unitDescriptionLabel.setPrefWidth(120.0);

		unitContainer.setPadding(new Insets(10, 0, 0, 10));
		unitContainer.setSpacing(10);
		unitContainer.getChildren().addAll(unitDescriptionLabel, unitDescriptionText);
		return unitContainer;
	}
	public LoggedComboBox<String> getModelYearComboBox() {
		return modelYearComboBox;
	}

	public void setModelYearComboBox(LoggedComboBox<String> modelYearComboBox) {
		this.modelYearComboBox = modelYearComboBox;
	}

	public LoggedComboBox<String> getVehicleModelCodeComboBox() {
		return vehicleModelCodeComboBox;
	}

	public void setVehicleModelCodeComboBox(
			LoggedComboBox<String> vehicleModelCodeComboBox) {
		this.vehicleModelCodeComboBox = vehicleModelCodeComboBox;
	}

	public LoggedComboBox<String> getProcessNumberComboBox() {
		return processNumberComboBox;
	}

	public void setProcessNumberComboBox(
			LoggedComboBox<String> processNumberComboBox) {
		this.processNumberComboBox = processNumberComboBox;
	}

	public LoggedComboBox<String> getUnitNumberComboBox() {
		return unitNumberComboBox;
	}

	public void setUnitNumberComboBox(LoggedComboBox<String> unitNumberComboBox) {
		this.unitNumberComboBox = unitNumberComboBox;
	}

	public LoggedText getUnitDescriptionText() {
		return unitDescriptionText;
	}

	public void setUnitDescriptionText(LoggedText unitDescriptionText) {
		this.unitDescriptionText = unitDescriptionText;
	}

	public static PdcLocalAttributeMaintDialogController getAttributeMaintDialogController() {
		return attributeMaintDialogController;
	}

	public TitledPane getTitledPane() {
		return titledPane;
	}

	public void setTitledPane(TitledPane titledPane) {
		this.titledPane = titledPane;
	}
	
	
}
