package com.honda.galc.client.teamleader.qi.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.controller.InspectionPartDialogController;
import com.honda.galc.client.teamleader.qi.model.ItemMaintenanceModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.qi.QiInspectionPart;
/**
 * 
 * <h3>InspectionPartDialog Class description</h3>
 * <p>
 * InspectionPartDialog is used to create the components on the PopUp screen etc
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
public class InspectionPartDialog extends QiFxDialog<ItemMaintenanceModel> {
	
	 private String title;
	 private InspectionPartDialogController controller;
	 private ItemMaintenanceModel model;
	 private QiInspectionPart previousInspectionPart;
     private LoggedButton createBtn;
     private LoggedButton cancelBtn;
     private UpperCaseFieldBean partNameTextField;
     private UpperCaseFieldBean partAbbrTextField;
     private UpperCaseFieldBean partClassTextField;
     private UpperCaseFieldBean partDescTextField;
     private UpperCaseFieldBean valueTextField;
     private CheckBox allowMultiple;
     private LoggedTextArea reasonForChangeTextArea;
 	 private volatile boolean isCancel = false;
	
	public InspectionPartDialog(String title, QiInspectionPart previousInspectionPart, ItemMaintenanceModel model,String applicationId) {
		super(title,applicationId,model);//Fix : Passing applicationId as parameter to fetch correct owner stage.
		this.title = title;
		this.controller = new InspectionPartDialogController(model, this, previousInspectionPart);
		this.previousInspectionPart = previousInspectionPart;
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		initComponents();
		if(this.title.equalsIgnoreCase(QiConstant.CREATE)){
			loadCreateData();
		}else if(this.title.equalsIgnoreCase(QiConstant.UPDATE)){
			loadUpdateData();
		}
		controller.initListeners();
	}
	/**
	 * This method is used to create the components like TextField,RadioButton etc. on the Popup screen
	 */
	  private void initComponents(){
	    	VBox outerPane = new VBox();
	    	outerPane.setPrefHeight(420);
	        HBox partNameAbbrContainer = new HBox();
	        HBox partDescContainer = new HBox();
	        HBox changeHierarchyContainer = new HBox();
	        HBox reasonForChangeContainer = new HBox();
	        HBox setHierarchyContainer = new HBox();
	        HBox buttonContainer = new HBox();
	        HBox radioBtnContainer =createStatusRadioButtons(getController());
	        createPartNameAbbrContainer(partNameAbbrContainer);
	        LoggedLabel partDescLabel = UiFactory.createLabel("partDescLabel", "Description");
	        applyCssOnLabel(partDescLabel);
	        partDescTextField =  UiFactory.createUpperCaseFieldBean("partDescTextField", 40, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
	        partDescContainer.getChildren().addAll(partDescLabel,partDescTextField);
	        partDescContainer.setSpacing(10);
	        partDescContainer.setPadding(new Insets(0,10,0,20));
	        partDescContainer.setAlignment(Pos.CENTER_LEFT);
	        setHierarchyContainer.getChildren().addAll(createTitiledPane("Set Hierarchy", createHierarchyPanel()));
	        createReasonForChangeContainer(reasonForChangeContainer);
	        changeHierarchyContainer.getChildren().addAll(reasonForChangeContainer,setHierarchyContainer);
	        changeHierarchyContainer.setSpacing(100);
	        changeHierarchyContainer.setPadding(new Insets(0,10,0,15));
	        createButtonContainer(buttonContainer);
	        outerPane.setSpacing(20);
	        outerPane.getChildren().addAll(radioBtnContainer,partNameAbbrContainer,partDescContainer ,changeHierarchyContainer,buttonContainer);
	        ((BorderPane) this.getScene().getRoot()).setCenter(outerPane);
	    
	  }
	 
	  /**
	   * This method is used to disable Update button , TextArea and set '0' value in hierarchy value field on the Popup screen 
	   */
	  private void loadCreateData(){
		  updateButton.setDisable(true);
		  reasonForChangeTextArea.setDisable(true);
		  valueTextField.settext("0");
	  }
	  /**
	   * This method is used to load the data of Part on the Pop screen
	   */
	  private void loadUpdateData(){
		  	createBtn.setDisable(true);
			partNameTextField.setText(previousInspectionPart.getInspectionPartName());
			partAbbrTextField.setText(previousInspectionPart.getInspectionPartDescShort());
			partDescTextField.setText(previousInspectionPart.getInspectionPartDescLong());
			partClassTextField.setText(previousInspectionPart.getPartClass());
			valueTextField.setText(String.valueOf(previousInspectionPart.getHierarchy()));
			boolean isActive = previousInspectionPart.isActive();
			getActiveRadioBtn().setSelected(isActive);
			getInactiveRadioBtn().setSelected(!isActive);
			boolean isPrimary = previousInspectionPart.isPrimaryPosition();
			getPrimaryRadioBtn().setSelected(isPrimary);
			getSecondaryRadioBtn().setSelected(!isPrimary);
			boolean isMultiple = previousInspectionPart.isAllowMultiple();
			allowMultiple.setSelected(isMultiple);
	  }
	  
	  /**
	   * This method is used to create the hierarchy value TextField and Allow Multiple checkbox
	   */
	 private MigPane createHierarchyPanel(){
          MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");
          HBox hierarchyValueContainer = new HBox();
          HBox desiredPositionContainer = new HBox();
          LoggedLabel valueLabel = UiFactory.createLabel("valueLabel", "Value");
          applyCssOnLabel(valueLabel);
          valueTextField =  UiFactory.createUpperCaseFieldBean("valueTextField", 4, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
          allowMultiple =createCheckBox("Allow Multiple", getController());
          allowMultiple.setId("radio-btn");
          hierarchyValueContainer.getChildren().addAll(valueLabel,valueTextField,allowMultiple);
          hierarchyValueContainer.setSpacing(10);
          hierarchyValueContainer.setPadding(new Insets(5));
          hierarchyValueContainer.setAlignment(Pos.CENTER_LEFT);
          desiredPositionContainer.getChildren().addAll(createTitiledPane("Set Desired Position", createPositionPanel()));
          pane.add(hierarchyValueContainer,"span,wrap");
          pane.add(desiredPositionContainer,"span,wrap");
          return pane;
	  }
	  /**
	   * This method is used to create the desired position
	   */
	  private MigPane createPositionPanel(){
          MigPane pane = new MigPane();
          HBox positionradioBtnContainer = createPositionRadioButtons(getController());
          pane.add(positionradioBtnContainer,"span,wrap");
          return pane;
	  }
	  
	  /**
       * This method is used to create TitledPane for Part panel.
       * @param title
       * @param content
       * @return
       */
    private TitledPane createTitiledPane(String title,Node content) {
                       TitledPane titledPane = new TitledPane();
                       titledPane.setText(title);
                       titledPane.setContent(content);
                       if("Set Hierarchy".equals(title)){
                    	   titledPane.setPrefSize(300, 150);
                       }
                       else{
                           titledPane.setPrefSize(700, 500);
                       }
                       return titledPane;
       }
    
	public InspectionPartDialogController getController() {
		return controller;
	}

	public ItemMaintenanceModel getModel() {
		return model;
	}

	public QiInspectionPart getPreviousInspectionPart() {
		return previousInspectionPart;
	}

	public LoggedButton getCreateBtn() {
		return createBtn;
	}


	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}

	public UpperCaseFieldBean getPartNameTextField() {
		return partNameTextField;
	}

	public UpperCaseFieldBean getPartAbbrTextField() {
		return partAbbrTextField;
	}

	public UpperCaseFieldBean getPartClassTextField() {
		return partClassTextField;
	}

	public UpperCaseFieldBean getPartDescTextField() {
		return partDescTextField;
	}

	public UpperCaseFieldBean getValueTextField() {
		return valueTextField;
	}

	public CheckBox getAllowMultiple() {
		return allowMultiple;
	}

	public LoggedTextArea getReasonForChangeTextArea() {
		return reasonForChangeTextArea;
	}
	/**
	 * This method is used to apply CSS on the Label
	 * @param label
	 */
	private void applyCssOnLabel(LoggedLabel label){
		label.getStyleClass().add("display-label");
	}
	/**
	 * This method is used to create PartName ,Part Abbr,Part Class Label and Textfields controls
	 * @param partNameAbbrContainer
	 * @return
	 */
	private void createPartNameAbbrContainer(HBox partNameAbbrContainer){
		HBox partNameContainer = new HBox();
        HBox partClassContainer = new HBox();
		LoggedLabel partNameLabel = UiFactory.createLabel("partNameLabel", "Part Name");
        applyCssOnLabel(partNameLabel);
        LoggedLabel partAsterisk = UiFactory.createLabel("label", "*");
        partAsterisk.setStyle("-fx-text-fill: red");
        partNameContainer.getChildren().addAll(partNameLabel,partAsterisk);
        partNameContainer.setAlignment(Pos.CENTER_LEFT);
        
        partNameTextField =  UiFactory.createUpperCaseFieldBean("partNameTextField", 24, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
        LoggedLabel partAbbrLabel = UiFactory.createLabel("partAbbrLabel", "Part Abbr");
        applyCssOnLabel(partAbbrLabel);
        partAsterisk = UiFactory.createLabel("label", "*");
        partAsterisk.setStyle("-fx-text-fill: red");
        partAbbrTextField =  UiFactory.createUpperCaseFieldBean("partAbbrTextField", 8, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
        
        LoggedLabel partClassLabel = UiFactory.createLabel("partClassLabel", "Part Class");
        applyCssOnLabel(partClassLabel);
        partClassContainer.getChildren().addAll(partClassLabel,partAsterisk);
        partClassContainer.setAlignment(Pos.CENTER_LEFT);
        partClassTextField =  UiFactory.createUpperCaseFieldBean("partClassTextField", 24, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
       
        partNameAbbrContainer.getChildren().addAll(partNameContainer,partNameTextField,partAbbrLabel,partAbbrTextField,partClassContainer,partClassTextField);
        partNameAbbrContainer.setSpacing(10);
        partNameAbbrContainer.setPadding(new Insets(20,10,0,20));
        partNameAbbrContainer.setAlignment(Pos.CENTER_LEFT);
	}
	
	/**
	 * This method is used to create Reason for Change Label and TextArea controls
	 * @param reasonForChangeContainer
	 * @return
	 */
	private void createReasonForChangeContainer(HBox reasonForChangeContainer){
		LoggedLabel reasonForChangeLabel = UiFactory.createLabel("iqsReasonLabel", "Reason for\nChange");
        applyCssOnLabel(reasonForChangeLabel);
        LoggedLabel asterisk = UiFactory.createLabel("label", "*");
        asterisk.setStyle("-fx-text-fill: red");
		reasonForChangeTextArea = UiFactory.createTextArea();
		reasonForChangeTextArea.setPrefRowCount(2);
		reasonForChangeTextArea.setPrefColumnCount(25);
		reasonForChangeTextArea.setWrapText(true);
		HBox reasonForChangeInnerContainer= new HBox();
		reasonForChangeInnerContainer.getChildren().addAll(reasonForChangeLabel,asterisk);
		reasonForChangeInnerContainer.setPadding(new Insets(0,0,20,0));
		reasonForChangeInnerContainer.setAlignment(Pos.CENTER_LEFT);
		reasonForChangeContainer.getChildren().addAll(reasonForChangeInnerContainer,reasonForChangeTextArea);
		reasonForChangeContainer.setSpacing(10);
		reasonForChangeContainer.setPadding(new Insets(20,0,0,0));
	}
	/**
	 * This method is used to create buttons like 'create','update' and 'cancel' and  register with action
	 * @param buttonContainer
	 * @return
	 */
	private HBox createButtonContainer(HBox buttonContainer){
		createBtn = createBtn(QiConstant.CREATE, getController());
        createBtn.setPadding(new Insets(5,5,5,5));
        createBtn.getStyleClass().add("popup-btn"); 
        updateButton = createBtn(QiConstant.UPDATE, getController());
        updateButton.getStyleClass().add("popup-btn");
        cancelBtn =  createBtn(QiConstant.CANCEL, getController());
        cancelBtn.getStyleClass().add("popup-btn");
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(25,0,15,0));
        buttonContainer.setSpacing(20);
        buttonContainer.getChildren().addAll(createBtn, updateButton, cancelBtn);
        return buttonContainer;
	}
	public boolean isCancel() {
		return isCancel;
	}
	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}
}
