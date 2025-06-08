package com.honda.galc.client.teamleader.qi.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.honda.galc.client.teamleader.qi.controller.PdcLocalAttributeMaintDialogController;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.util.KeyValue;
/**   
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
public class PdcLocalResponsibilityAssignmentDialog extends VBox {

	private static PdcLocalResponsibilityAssignmentDialog instance;
	private static PdcLocalAttributeMaintDialogController attributeMaintDialogController;
	private LoggedComboBox<String> siteComboBox;
	private LoggedComboBox<String> plantComboBox;
	private LoggedComboBox<String> departmentComboBox;
	private LoggedComboBox<KeyValue<String,Integer>> responsibleLevel1ComboBox;
	private LoggedComboBox<KeyValue<String,Integer>> responsibleLevel2ComboBox;
	private LoggedComboBox<KeyValue<String,Integer>> responsibleLevel3ComboBox;
	private CheckBox pddaRelatedInfoCheckBox;
	private LoggedButton clearResp;
	private LoggedButton resetResp;
	
	private PdcLocalResponsibilityAssignmentDialog() {}
	
	public static PdcLocalResponsibilityAssignmentDialog getInstance(PdcLocalAttributeMaintDialogController dialogController) {
		if(instance == null) {
			instance = new PdcLocalResponsibilityAssignmentDialog();
		}
		attributeMaintDialogController = dialogController;
		return instance;
	}
	
	/**
	 * This method is used to create Button.
	 * @param text
	 * @param handler
	 * @return
	 */
	public LoggedButton createPlainBtn(String text,EventHandler<ActionEvent> handler)
	{
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.30 * btn.getWidth())));
		btn.setOnAction(handler);
		return btn;
	}
	
	public HBox createResponsibilityAssignmentDetails() {
		HBox responsibilityPanel = new HBox();
		VBox vBoxResponsibilityAssignmentDetails = new VBox();
		VBox resetButtons = new VBox();
		clearResp = createPlainBtn("Clear", attributeMaintDialogController);
		resetResp = createPlainBtn("Reset", attributeMaintDialogController);
		clearResp.setOnAction(attributeMaintDialogController);
		resetResp.setOnAction(attributeMaintDialogController);
		resetButtons.getChildren().addAll(clearResp, resetResp);
		resetButtons.setPadding(new Insets(10, 20, 0, 0));
		resetButtons.setSpacing(10);
		vBoxResponsibilityAssignmentDetails.maxWidth(260);
		vBoxResponsibilityAssignmentDetails.minWidth(260);
		pddaRelatedInfoCheckBox = new CheckBox("PDDA Related Info");
		pddaRelatedInfoCheckBox.setDisable(true);
		vBoxResponsibilityAssignmentDetails.getChildren().addAll(createSiteComboBox(), createPlantComboBox(), createDepartmentComboBox(), createResponsibleLevel1ComboBox(),  createResponsibleLevel2ComboBox(),createResponsibleLevel3ComboBox(), pddaRelatedInfoCheckBox);
		responsibilityPanel.getChildren().addAll(vBoxResponsibilityAssignmentDetails,resetButtons);
		responsibilityPanel.setSpacing(10);
		return responsibilityPanel;
	}
	
	/**
	 * Creates the site combo box.
	 *
	 * @return the h box
	 */
	private HBox createSiteComboBox() {
		HBox siteContainer = new HBox();
		siteComboBox = new LoggedComboBox<String>("siteComboBox"); 
		siteComboBox.getStyleClass().add("combo-box-base");
		siteComboBox.setMinWidth(130.0);
		siteComboBox.setMaxWidth(130.0);
		
		HBox labelBox = new HBox();
		LoggedLabel siteLabel = UiFactory.createLabel("siteLabel", "Site ");
		LoggedLabel asterisk = UiFactory.createLabel("label", "*");
		asterisk.setStyle("-fx-text-fill: red");
		labelBox.getChildren().addAll(siteLabel, asterisk);
		labelBox.setMaxWidth(120.0);
		labelBox.setPrefWidth(120.0);
				
		siteContainer.setAlignment(Pos.CENTER_LEFT);
		siteContainer.setSpacing(10);
		siteContainer.setPadding(new Insets(10, 0, 0, 10));
		siteContainer.getChildren().addAll(labelBox, siteComboBox);
		return siteContainer;
	}
	
	/**
	 * Creates the plant combo box.
	 *
	 * @return the h box
	 */
	private HBox createPlantComboBox() {
		HBox plantContainer = new HBox();
		plantComboBox = new LoggedComboBox<String>("plantComboBox1"); 
		plantComboBox.getStyleClass().add("combo-box-base");
		plantComboBox.setMinWidth(130.0);
		plantComboBox.setMaxWidth(130.0);
		
		HBox labelBox = new HBox();
		LoggedLabel plantLabel = UiFactory.createLabel("plantLabel", "Plant ");
		LoggedLabel asterisk = UiFactory.createLabel("label", "*");
		asterisk.setStyle("-fx-text-fill: red");
		labelBox.getChildren().addAll(plantLabel, asterisk);
		labelBox.setMaxWidth(120.0);
		labelBox.setPrefWidth(120.0);
		
		plantContainer.setAlignment(Pos.CENTER_LEFT);
		plantContainer.setSpacing(10);
		plantContainer.setPadding(new Insets(10, 0, 0, 10));
		plantContainer.getChildren().addAll(labelBox, plantComboBox);
		return plantContainer;
	}
	
	/**
	 * Creates the department combo box.
	 *
	 * @return the h box
	 */
	@SuppressWarnings("unchecked")
	private HBox createDepartmentComboBox() {
		HBox departmentContainer = new HBox();
		departmentComboBox = new LoggedComboBox<String>("departmentComboBox"); 
		departmentComboBox.getStyleClass().add("combo-box-base");
		departmentComboBox.setMinWidth(130.0);
		departmentComboBox.setMaxWidth(130.0);
		
		HBox labelBox = new HBox();
		LoggedLabel departmentLabel = UiFactory.createLabel("departmentLabel", "Department ");
		LoggedLabel asterisk = UiFactory.createLabel("label", "*");
		asterisk.setStyle("-fx-text-fill: red");
		labelBox.getChildren().addAll(departmentLabel, asterisk);
		labelBox.setMaxWidth(120.0);
		labelBox.setPrefWidth(120.0);
		
		departmentContainer.setAlignment(Pos.CENTER_LEFT);
		departmentContainer.setSpacing(10);
		departmentContainer.setPadding(new Insets(10, 0, 0, 10));
		departmentContainer.getChildren().addAll(labelBox, departmentComboBox);
		return departmentContainer;
	}
	
	/**
	 * Creates the responsible level3 combo box.
	 *
	 * @return the h box
	 */
	private HBox createResponsibleLevel3ComboBox() {
		HBox responsibleLevel3Container = new HBox();
		responsibleLevel3ComboBox = new LoggedComboBox<KeyValue<String,Integer>>("responsibleLevel3ComboBox");
		responsibleLevel3ComboBox.getStyleClass().add("combo-box-base");
		responsibleLevel3ComboBox.setMinWidth(130.0);
		responsibleLevel3ComboBox.setMaxWidth(130.0);
		//responsibleLevel3ComboBox.setDisable(true);
		responsibleLevel3ComboBox.setStyle("-fx-opacity: 1.0;");
		LoggedLabel responsibleLevel3Label = UiFactory.createLabel("responsibleLevel3Label", "Resp L3 ");
		responsibleLevel3Label.setMaxWidth(120.0);
		responsibleLevel3Label.setPrefWidth(120.0);
		
		responsibleLevel3Container.setAlignment(Pos.CENTER_LEFT);
		responsibleLevel3Container.setSpacing(10);
		responsibleLevel3Container.setPadding(new Insets(10, 0, 0, 10));
		responsibleLevel3Container.getChildren().addAll(responsibleLevel3Label, responsibleLevel3ComboBox);
		return responsibleLevel3Container;
	}
	
	/**
	 * Creates the responsible level2 combo box.
	 *
	 * @return the h box
	 */
	private HBox createResponsibleLevel2ComboBox() {
		HBox responsibleLevel2Container = new HBox();
		responsibleLevel2ComboBox = new LoggedComboBox<KeyValue<String,Integer>>("responsibleLevel2ComboBox");
		responsibleLevel2ComboBox.getStyleClass().add("combo-box-base");
		responsibleLevel2ComboBox.setMinWidth(130.0);
		responsibleLevel2ComboBox.setMaxWidth(130.0);
		//responsibleLevel2ComboBox.setDisable(true);
		responsibleLevel2ComboBox.setStyle("-fx-opacity: 1.0;");
		LoggedLabel responsibleLevel2Label = UiFactory.createLabel("responsibleLevel2Label", "Resp L2 ");
		responsibleLevel2Label.setMaxWidth(120.0);
		responsibleLevel2Label.setPrefWidth(120.0);
		
		responsibleLevel2Container.setAlignment(Pos.CENTER_LEFT);
		responsibleLevel2Container.setSpacing(10);
		responsibleLevel2Container.setPadding(new Insets(10, 0, 0, 10));
		responsibleLevel2Container.getChildren().addAll(responsibleLevel2Label, responsibleLevel2ComboBox);
		return responsibleLevel2Container;
	}
	
	/**
	 * Creates the responsible level1 combo box.
	 *
	 * @return the h box
	 */
	private HBox createResponsibleLevel1ComboBox() {
		HBox responsibleLevel1Container = new HBox();
		responsibleLevel1ComboBox = new LoggedComboBox<KeyValue<String,Integer>>("responsibleLevel1ComboBox");
		responsibleLevel1ComboBox.getStyleClass().add("combo-box-base");
		responsibleLevel1ComboBox.setMinWidth(130.0);
		responsibleLevel1ComboBox.setMaxWidth(130.0);
		
		HBox labelBox = new HBox();
		LoggedLabel responsibleLevel1Label = UiFactory.createLabel("responsibleLevel1Label", "Resp L1 ");
		LoggedLabel asterisk = UiFactory.createLabel("label", "*");
		asterisk.setStyle("-fx-text-fill: red");
		labelBox.getChildren().addAll(responsibleLevel1Label, asterisk);
		labelBox.setMaxWidth(120.0);
		labelBox.setPrefWidth(120.0);
		
		responsibleLevel1Container.setAlignment(Pos.CENTER_LEFT);
		responsibleLevel1Container.setSpacing(10);
		responsibleLevel1Container.setPadding(new Insets(10, 0, 0, 10));
		responsibleLevel1Container.getChildren().addAll(labelBox, responsibleLevel1ComboBox);
		return responsibleLevel1Container;
	}

	public PdcLocalAttributeMaintDialogController getAttributeMaintDialogController() {
		return attributeMaintDialogController;
	}

	public LoggedComboBox<String> getSiteComboBox() {
		return siteComboBox;
	}

	public void setSiteComboBox(LoggedComboBox<String> siteComboBox) {
		this.siteComboBox = siteComboBox;
	}

	public LoggedComboBox<String> getPlantComboBox() {
		return plantComboBox;
	}

	public void setPlantComboBox(LoggedComboBox<String> plantComboBox) {
		this.plantComboBox = plantComboBox;
	}

	public LoggedComboBox<String> getDepartmentComboBox() {
		return departmentComboBox;
	}

	public void setDepartmentComboBox(LoggedComboBox<String> departmentComboBox) {
		this.departmentComboBox = departmentComboBox;
	}

	public LoggedComboBox<KeyValue<String,Integer>> getResponsibleLevel1ComboBox() {
		return responsibleLevel1ComboBox;
	}

	public LoggedComboBox<KeyValue<String,Integer>> getResponsibleLevel2ComboBox() {
		return responsibleLevel2ComboBox;
	}

	public LoggedComboBox<KeyValue<String,Integer>> getResponsibleLevel3ComboBox() {
		return responsibleLevel3ComboBox;
	}

	public CheckBox getPddaRelatedInfoCheckBox() {
		return pddaRelatedInfoCheckBox;
	}

	public void setPddaRelatedInfoCheckBox(CheckBox pddaRelatedInfoCheckBox) {
		this.pddaRelatedInfoCheckBox = pddaRelatedInfoCheckBox;
	}

	/**
	 * @return the clearResp
	 */
	public LoggedButton getClearResp() {
		return clearResp;
	}

	/**
	 * @return the resetResp
	 */
	public LoggedButton getResetResp() {
		return resetResp;
	}
}
