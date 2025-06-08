package com.honda.galc.client.teamleader.qi.view;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.honda.galc.client.teamleader.qi.controller.RepairAreaDialogController;
import com.honda.galc.client.teamleader.qi.model.ParkingLocationMaintenanceModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.qi.QiRepairArea;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * 
 * <h3>RepairAreaDialog Class description</h3>
 * <p>
 * RepairAreaDialog is used to create and update the Repair Area 
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
 * 
 * </TABLE>
 * 
 * @author LnTInfotech<br>
 * 
 */
public class RepairAreaDialog extends QiFxDialog<ParkingLocationMaintenanceModel>{
	private String title;
	private LoggedLabel siteValueLabel;
	private LoggedLabel plantValueLabel;
	private RepairAreaDialogController controller;
	private LoggedTextArea reasonForChangeTextArea;
	private LoggedButton createBtn;
	private LoggedButton cancelBtn;		
	private LabeledComboBox<String> locationCombobox;	
	private LabeledComboBox<String> rowFillSequenceCombobox;		
	private LabeledUpperCaseTextField repairAreaNameTextField;	
	private LabeledUpperCaseTextField repairAreaDescTextField;
	private LabeledUpperCaseTextField priorityTextField;
	private LabeledComboBox<String> divCombobox;

	private QiRepairArea repairArea;
	private String plantName;

	public RepairAreaDialog(String title, QiRepairArea owner, ParkingLocationMaintenanceModel model,String applicationId, String plantName) {
		super(title, applicationId,model);//Fix : Passing applicationId as parameter to fetch correct owner stage.
		this.title = title;
		this.repairArea = owner;
		this.plantName = plantName;
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.controller = new RepairAreaDialogController(model, this,owner,plantName);		
		initComponents();
		if(this.title.equals(QiConstant.CREATE))
			loadCreateData();
		else if(this.title.equals(QiConstant.UPDATE))
			loadUpdateData();
		controller.initListeners();
	}
	
	private void loadCreateData(){	
		repairArea = new QiRepairArea();
		updateButton.setDisable(true);
		reasonForChangeTextArea.setDisable(true);
		plantValueLabel.setText(plantName);
		locationCombobox.getControl().getItems().addAll("I - Inside","O - Outside","T - InTransit");	
		List<String> divList = QiCommonUtil.getUniqueArrayList(Lists.transform(getModel().findAllDivisionBySiteAndPlant(getModel().getSiteName(),plantName), new Function<Division, String>() {
			@Override
			public String apply(final Division entity) {
				return StringUtils.trimToEmpty(entity.getDivisionName());
			}
		}));
		divCombobox.getControl().getItems().addAll(FXCollections.observableArrayList(divList));
		rowFillSequenceCombobox.getControl().getItems().addAll("A - Ascending","D - Descending");	
		
	}

	private void loadUpdateData() {
		createBtn.setDisable(true);
		plantValueLabel.setText(plantName);
		repairAreaNameTextField.setText(repairArea.getRepairAreaName());
		repairAreaDescTextField.setText(repairArea.getRepairAreaDescription());
		priorityTextField.setText(String.valueOf(repairArea.getPriority()));
		locationCombobox.getControl().getItems().addAll("I - Inside","O - Outside","T - InTransit");	
		List<String> divList = QiCommonUtil.getUniqueArrayList(Lists.transform(getModel().findAllDivisionBySiteAndPlant(getModel().getSiteName(),plantName), new Function<Division, String>() {
			@Override
			public String apply(final Division entity) {
				return StringUtils.trimToEmpty(entity.getDivisionName());
			}
		}));
		divCombobox.getControl().getItems().addAll(FXCollections.observableArrayList(divList));
		divCombobox.getControl().setValue(repairArea.getDivName());
		if(repairArea.getLocation() == 'I')
			locationCombobox.getControl().setValue(repairArea.getLocation()+" - Inside");
		else if(repairArea.getLocation() == 'O')
			locationCombobox.getControl().setValue(repairArea.getLocation()+" - Outside");
		else if(repairArea.getLocation() == 'T')
			locationCombobox.getControl().setValue(repairArea.getLocation()+" - InTransit");
		rowFillSequenceCombobox.getControl().getItems().addAll("A - Ascending","D - Descending");	
		if(repairArea.getRowFillSeq() == 'A')
			rowFillSequenceCombobox.getControl().setValue(repairArea.getRowFillSeq()+" - Ascending");
		else if(repairArea.getRowFillSeq() == 'D')
			rowFillSequenceCombobox.getControl().setValue(repairArea.getRowFillSeq()+" - Descending");
	}

	private void initComponents() {
		VBox outerPane = new VBox();				
		HBox currentCombinationContainer = new HBox();
		HBox repairAreaDataContainer = new HBox();		
		currentCombinationContainer.getChildren().addAll(createTitiledPane("Current Combination", createCurrentCombinationPanel()));
		currentCombinationContainer.setPadding(new Insets(10));
		repairAreaDataContainer.getChildren().addAll(createTitiledPane("Repair Area Data", repairAreaDataPanel()));
		repairAreaDataContainer.setPadding(new Insets(10));
		outerPane.getChildren().addAll(currentCombinationContainer,repairAreaDataContainer);		
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);		
	}

	private MigPane createCurrentCombinationPanel(){
		MigPane pane = new MigPane("", // Layout Constraints
				 "50[]120[]", // Column constraints
				 "10[]20[]");
		LoggedLabel siteNameLabel = UiFactory.createLabel("site", "Site");
		siteNameLabel.getStyleClass().add("display-label");
		siteValueLabel =new LoggedLabel();
		siteValueLabel.setText(getModel().getSiteName());
		
		LoggedLabel plantNameLabel = UiFactory.createLabel("plant", "Plant");
		plantNameLabel.getStyleClass().add("display-label");
		plantValueLabel =new LoggedLabel();
		plantValueLabel.setText(repairArea.getPlantName());
		
		LoggedLabel DivNameLabel = UiFactory.createLabel("divisionlbl", "Division");
		DivNameLabel.getStyleClass().add("display-label");
		
		divCombobox = new LabeledComboBox<String>("", true, new Insets(0,0,0,0), true, false);
		divCombobox.setId("division");
		divCombobox.getControl().getStyleClass().add("combo-box-base");
		divCombobox.getControl().setMinHeight(35.0);
		
		pane.add(siteNameLabel,"align left");
		pane.add(siteValueLabel,"wrap");
		pane.add(plantNameLabel,"align left");
		pane.add(plantValueLabel,"wrap");
		pane.add(DivNameLabel,"align left");
		pane.add(divCombobox,"wrap");
		return pane;
	}

	private MigPane repairAreaDataPanel(){
		MigPane pane = new MigPane("insets 10 10 10 10", "", "");      
		HBox reasonForChangeContainer = new HBox();
		HBox buttonContainer = new HBox();

		reasonForChangeTextArea = UiFactory.createTextArea();
		reasonForChangeTextArea.setPrefWidth(300);
		reasonForChangeTextArea.setPrefRowCount(2);
		reasonForChangeTextArea.setWrapText(true);
		reasonForChangeTextArea.setId("Reason for Change");

		repairAreaNameTextField = new LabeledUpperCaseTextField("Repair Area Name", "Repair Area Name", 10, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT, true,new Insets(10,10,10,10));
		repairAreaDescTextField = new LabeledUpperCaseTextField("Repair Area Desc", "Repair Area Desc", 30, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT, true,new Insets(10,10,10,10));
		repairAreaNameTextField.getLabel().setPadding(new Insets(10,30,10,40));
		repairAreaDescTextField.getLabel().setPadding(new Insets(10,35,10,40));
		locationCombobox = new LabeledComboBox<String>("Location", true, new Insets(10,10,10,10), true, true);
		locationCombobox.getLabel().setPadding(new Insets(10,90,10,30));
		locationCombobox.setId("Location");
		locationCombobox.getControl().getStyleClass().add("combo-box-base");
		locationCombobox.getControl().setMinHeight(35.0);
		rowFillSequenceCombobox = new LabeledComboBox<String>("Row Fill Sequence", true, new Insets(10,10,10,10), true, true);
		rowFillSequenceCombobox.setId("Fill Sequence");
		rowFillSequenceCombobox.getLabel().setPadding(new Insets(10,35,10,30));
		rowFillSequenceCombobox.getControl().getStyleClass().add("combo-box-base");
		rowFillSequenceCombobox.getControl().setMinHeight(35.0);
		priorityTextField = new LabeledUpperCaseTextField("Priority", "Priority", 6, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT, true,new Insets(0));
		priorityTextField.getLabel().setPadding(new Insets(10,103,10,40));
		setReasonForChangeContainer(reasonForChangeContainer);
		setButtonContainer(buttonContainer);

		pane.add(repairAreaNameTextField,"wrap");
		pane.add(repairAreaDescTextField,"wrap");
		pane.add(locationCombobox,"wrap");
		pane.add(priorityTextField,"wrap");
		pane.add(rowFillSequenceCombobox,"wrap");
		pane.add(reasonForChangeContainer,"wrap");
		pane.add(buttonContainer,"span,wrap");
		return pane;
	}


	private void setReasonForChangeContainer(HBox reasonForChangeContainer) {
		HBox reasonForChange = new HBox();
		LoggedLabel reasonForChangeLabel = UiFactory.createLabel("reasonForChange","Reason for Change");
		reasonForChangeLabel.getStyleClass().add("display-label");
		LoggedLabel asterisk1 = UiFactory.createLabel("asteriskReasonForChange", "*");
		asterisk1.getStyleClass().add("display-label");
		asterisk1.setStyle("-fx-text-fill: red");
		reasonForChange.getChildren().addAll(reasonForChangeLabel,asterisk1);
		reasonForChangeContainer.setPadding(new Insets(5, 10, 10, 40));
		reasonForChangeContainer.setSpacing(37);
		reasonForChangeContainer.getChildren().addAll(reasonForChange, reasonForChangeTextArea);
	}

	private void setButtonContainer(HBox buttonContainer) {
		createBtn = createBtn(QiConstant.CREATE, getController());
		createBtn.setPadding(new Insets(5,5,5,5));
		updateButton = createBtn(QiConstant.UPDATE,getController());
		cancelBtn = createBtn(QiConstant.CANCEL,getController());		
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setPadding(new Insets(10, 10, 10, 140));
		buttonContainer.setSpacing(20);
		buttonContainer.getChildren().addAll(createBtn, updateButton, cancelBtn);
	}

	private TitledPane createTitiledPane(String title,Node content) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(content);
		if("Current Combination".equals(title)){
			titledPane.setPrefSize(600, 180);
		}
		else{
			titledPane.setPrefSize(600, 440);
		}
		return titledPane;
	}

	public RepairAreaDialogController getController() {
		return controller;
	}

	public LoggedButton getCreateBtn() {
		return createBtn;
	}

	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}

	public QiRepairArea getRepairMethod() {
		return repairArea;
	}

	public LoggedTextArea getReasonForChangeTextArea() {
		return reasonForChangeTextArea;
	}

	public LabeledUpperCaseTextField getRepairAreaNameTextField() {
		return repairAreaNameTextField;
	}

	public ComboBox<String> getLocationCombobox() {
		return locationCombobox.getControl();
	}
	
	public ComboBox<String> getDivCombobox() {
		return divCombobox.getControl();
	}

	public ComboBox<String> getRowFillSequenceCombobox() {
		return rowFillSequenceCombobox.getControl();
	}
	
	public LabeledUpperCaseTextField getRepairAreaDescTextField() {
		return repairAreaDescTextField;
	}

	public LabeledUpperCaseTextField getPriorityTextField() {
		return priorityTextField;
	}

}
