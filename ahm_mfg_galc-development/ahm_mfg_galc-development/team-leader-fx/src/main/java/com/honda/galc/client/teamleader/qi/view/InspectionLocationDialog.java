package com.honda.galc.client.teamleader.qi.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.controller.InspectionLocationDialogController;
import com.honda.galc.client.teamleader.qi.model.ItemMaintenanceModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.qi.QiInspectionLocation;
/**
 * 
 * <h3>InspectionLocationDialog Class description</h3>
 * <p>
 * InspectionLocationDialog is used to create the components on the PopUp screen etc
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
public class InspectionLocationDialog extends QiFxDialog<ItemMaintenanceModel> {
	
	 private String title;
	 private InspectionLocationDialogController controller;
	 private ItemMaintenanceModel model;
	 private QiInspectionLocation previousInspectionLocation;
	 private LoggedLabel asterisk;
     private LoggedButton createBtn;
     private LoggedButton cancelBtn;
     private UpperCaseFieldBean locationNameTextField;
     private UpperCaseFieldBean locationAbbrTextField;
     private UpperCaseFieldBean locationDescTextField;
     private UpperCaseFieldBean valueTextField;
     private LoggedTextArea reasonForChangeTextArea;
 	 private volatile boolean isCancel = false;
	
	public InspectionLocationDialog(String title, QiInspectionLocation previousInspectionLocation, ItemMaintenanceModel model,String applicationId) {
		super(title, applicationId,model);//Fix : Passing applicationId as parameter to fetch correct owner stage.
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.model=model;
		model.setQiLocation(previousInspectionLocation);
		this.title = title;
		this.previousInspectionLocation = previousInspectionLocation;
		this.controller = new InspectionLocationDialogController(model, this, previousInspectionLocation);
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
          HBox locationNameAbbrContainer = new HBox();
          HBox locationDescContainer = new HBox();
          HBox changeHierarchyContainer = new HBox();
          HBox setHierarchyContainer = new HBox();
          HBox buttonContainer = new HBox();
          HBox radioBtnContainer =createStatusRadioButtons(getController());
          HBox reasonForChangeContainer1= new HBox();
          createLocationNameAbbrContainer(locationNameAbbrContainer);
         
          LoggedLabel locationDescLabel = UiFactory.createLabel("locationDescLabel", "Description");
          applyCssOnLabel(locationDescLabel);
          locationDescTextField =  UiFactory.createUpperCaseFieldBean("locationDescTextField", 35, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
          locationDescContainer.getChildren().addAll(locationDescLabel,locationDescTextField);
          locationDescContainer.setSpacing(10);
          locationDescContainer.setPadding(new Insets(0,10,0,35));
          locationDescContainer.setAlignment(Pos.CENTER_LEFT);
          createReasonForChangeContainer(reasonForChangeContainer1);
          setHierarchyContainer.getChildren().addAll(createTitiledPane("Set Hierarchy", createHierarchyPanel()));
          changeHierarchyContainer.getChildren().addAll(reasonForChangeContainer1,setHierarchyContainer);
          changeHierarchyContainer.setSpacing(140);
          changeHierarchyContainer.setPadding(new Insets(0,10,0,15));
          createButtonContainer(buttonContainer);
          outerPane.setSpacing(20);
          outerPane.getChildren().addAll(radioBtnContainer,locationNameAbbrContainer,locationDescContainer ,changeHierarchyContainer,buttonContainer);
          ((BorderPane) this.getScene().getRoot()).setCenter(outerPane);
	  }
	  /**
	   * This method is used to create LocationName ,Location Abbr Label and Textfields controls
	   * @param locationNameAbbrContainer
	   */
	  private void createLocationNameAbbrContainer(HBox locationNameAbbrContainer){
		  HBox locationNameContainer = new HBox();
		  LoggedLabel locationNameLabel = UiFactory.createLabel("locationNameLabel", "Location Name");
          applyCssOnLabel(locationNameLabel);
          asterisk = UiFactory.createLabel("label", "*");
          asterisk.setStyle("-fx-text-fill: red");
          locationNameContainer.getChildren().addAll(locationNameLabel,asterisk);
          locationNameContainer.setAlignment(Pos.CENTER_LEFT);
          
          locationNameTextField =  UiFactory.createUpperCaseFieldBean("locationNameTextField", 35, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
          LoggedLabel locationAbbrLabel = UiFactory.createLabel("locationAbbrLabel", "Location Abbr");
          applyCssOnLabel(locationAbbrLabel);
          asterisk = UiFactory.createLabel("label", "*");
          asterisk.setStyle("-fx-text-fill: red");
          locationAbbrTextField =  UiFactory.createUpperCaseFieldBean("locationAbbrTextField", 12, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
         
          locationNameAbbrContainer.getChildren().addAll(locationNameContainer,locationNameTextField,locationAbbrLabel,locationAbbrTextField);
          locationNameAbbrContainer.setSpacing(10);
          locationNameAbbrContainer.setPadding(new Insets(20,10,0,10));
          locationNameAbbrContainer.setAlignment(Pos.CENTER_LEFT);
	  }
	  
	  /**
	   * This method is used to create Reason for Change Label and TextArea controls
	   * @param reasonForChangeContainer
	   */
	  private void createReasonForChangeContainer(HBox reasonForChangeContainer){
		  HBox reasonForChangeContainerWithAsterisk = new HBox();
		  LoggedLabel reasonForChangeLabel = UiFactory.createLabel("iqsReasonLabel", "Reason for\nChange");
          applyCssOnLabel(reasonForChangeLabel);
          LoggedLabel asterisk = UiFactory.createLabel("label", "*");
          asterisk.setStyle("-fx-text-fill: red");
          reasonForChangeTextArea = UiFactory.createTextArea();
          reasonForChangeTextArea.setPrefRowCount(2);
  		  reasonForChangeTextArea.setPrefColumnCount(25);
  		  reasonForChangeTextArea.setWrapText(true);
          reasonForChangeContainerWithAsterisk.getChildren().addAll(reasonForChangeLabel,asterisk);
          reasonForChangeContainerWithAsterisk.setPadding(new Insets(0,0,20,16));
          reasonForChangeContainerWithAsterisk.setAlignment(Pos.CENTER_LEFT);
          reasonForChangeContainer.getChildren().addAll(reasonForChangeContainerWithAsterisk,reasonForChangeTextArea);
          reasonForChangeContainer.setSpacing(10);
          reasonForChangeContainer.setPadding(new Insets(20,0,0,0));
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
	   * This method is used to load the data of Location on the Pop screen
	   */
	  private void loadUpdateData(){
		  	createBtn.setDisable(true);
			previousInspectionLocation= model.getQiLocation();
			locationNameTextField.setText(previousInspectionLocation.getInspectionPartLocationName());
			locationAbbrTextField.setText(previousInspectionLocation.getInspectionPartLocDescShort());
			locationDescTextField.setText(previousInspectionLocation.getInspectionPartLocDescLong());
			valueTextField.setText(String.valueOf(previousInspectionLocation.getHierarchy()));
			boolean isActive=previousInspectionLocation.isActive();
			getActiveRadioBtn().setSelected(isActive);
			getInactiveRadioBtn().setSelected(!isActive);
			boolean isPrimary=previousInspectionLocation.isPrimaryPosition();
			getPrimaryRadioBtn().setSelected(isPrimary);
			getSecondaryRadioBtn().setSelected(!isPrimary);
	  }
	 
	  /**
	   * This method is used to create the hierarchy value TextField 
	   * @return
	   */
	  private MigPane createHierarchyPanel(){
          MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");
          HBox hierarchyValueContainer = new HBox();
          HBox desiredPositionContainer = new HBox();
          LoggedLabel valueLabel = UiFactory.createLabel("valueLabel", "Value");
          applyCssOnLabel(valueLabel);
          valueTextField =  UiFactory.createUpperCaseFieldBean("valueTextField", 5, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
          hierarchyValueContainer.getChildren().addAll(valueLabel,valueTextField);
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
                       if("Set Hierarchy".equals(title))
                       titledPane.setPrefSize(300, 150);
                       else
                                       titledPane.setPrefSize(700, 500);
                       return titledPane;
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
	/**
	 * This method is used to apply CSS on the Label
	 * @param label
	 */
	private void applyCssOnLabel(LoggedLabel label){
		label.getStyleClass().add("display-label");
	}
	public ItemMaintenanceModel getModel() {
		return model;
	}

	public void setModel(ItemMaintenanceModel model) {
		this.model = model;
	}

	public QiInspectionLocation getPreviousInspectionLocation() {
		return previousInspectionLocation;
	}

	public void setPreviousInspectionLocation(
			QiInspectionLocation previousInspectionLocation) {
		this.previousInspectionLocation = previousInspectionLocation;
	}

	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}

	public boolean isCancel() {
		return isCancel;
	}
	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}
	public UpperCaseFieldBean getLocationNameTextField() {
		return locationNameTextField;
	}

	public void setLocationNameTextField(UpperCaseFieldBean locationNameTextField) {
		this.locationNameTextField = locationNameTextField;
	}

	public UpperCaseFieldBean getLocationAbbrTextField() {
		return locationAbbrTextField;
	}

	public void setLocationAbbrTextField(UpperCaseFieldBean locationAbbrTextField) {
		this.locationAbbrTextField = locationAbbrTextField;
	}

	public UpperCaseFieldBean getLocationDescTextField() {
		return locationDescTextField;
	}

	public void setLocationDescTextField(UpperCaseFieldBean locationDescTextField) {
		this.locationDescTextField = locationDescTextField;
	}

	public UpperCaseFieldBean getValueTextField() {
		return valueTextField;
	}

	public void setValueTextField(UpperCaseFieldBean valueTextField) {
		this.valueTextField = valueTextField;
	}

	public LoggedTextArea getReasonForChangeTextArea() {
		return reasonForChangeTextArea;
	}

	public void setReasonForChangeTextArea(LoggedTextArea reasonForChangeTextArea) {
		this.reasonForChangeTextArea = reasonForChangeTextArea;
	}

	public InspectionLocationDialogController getController() {
		return controller;
	}

	public void setController(InspectionLocationDialogController controller) {
		this.controller = controller;
	}

	public LoggedButton getCreateBtn() {
		return createBtn;
	}
	
}
