package com.honda.galc.client.teamleader.qi.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.honda.galc.client.teamleader.qi.controller.EntryModelDialogController;
import com.honda.galc.client.teamleader.qi.model.EntryModelMaintenanceModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.qi.QiEntryModel;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>EntryModelDialog</code> is the Dialog Panel class for Mtc to Entry Model Assignment Screen.
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>L&T Infotech</TD>
 * <TD>29/08/2016</TD>

 * </TABLE>
*/
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */

 public class EntryModelDialog extends QiFxDialog<EntryModelMaintenanceModel> {
	private LoggedLabel productTypeContextLabel;
	private LoggedLabel productType;
	private LoggedLabel modelName;
	private LoggedLabel modelDesc;
	private LoggedLabel reasonForChangeLabel;
	private UpperCaseFieldBean entryModelName;
	private UpperCaseFieldBean entryModelDesc;
	private LoggedTextArea reasonForChangeText;
	private LoggedButton createBtn;
	private LoggedButton cancelBtn;
	private String title;
	private EntryModelDialogController controller;
	private QiEntryModel selectedModel;

	public EntryModelDialog(String title, QiEntryModel selectedModel, EntryModelMaintenanceModel model,String applicationId) {

		super(title, applicationId, model);//Fix : Passing applicationId as parameter to fetch correct owner stage.
		this.title = title;
		this.selectedModel = selectedModel;
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.controller = new EntryModelDialogController(model, this, selectedModel);
		initComponents();
		if (this.title.equals(QiConstant.CREATE))
			loadCreateData();
		else if (this.title.equals(QiConstant.UPDATE))
			loadUpdateData();
		controller.initListeners();
	}
	/**
	 * This method is used to initialize the components of panel
	 */
	private void initComponents() {
		VBox outerPane = new VBox();
		outerPane.getChildren().add(createStatusRadioButtons(getController()));
		outerPane.getChildren().add(getProductTypeContainer());
		outerPane.getChildren().add(getEntryModelNameContainer());
		outerPane.getChildren().add(getDescriptionContainer());
		outerPane.getChildren().add(getReasonContainer());
		outerPane.getChildren().add(getButtonContainer());
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);
	}
	public EntryModelDialogController getController() {
		return controller;
	}
	public void setController(EntryModelDialogController controller) {
		this.controller = controller;
	}
	
	/**
	 * this method is used to Get Product Type
	 */
	private HBox getProductTypeContainer() {
		HBox productContainer = new HBox();
		productType = UiFactory.createLabel("productType", "Product Type");
		productType.setPadding(new Insets(0, 0, 0, 60));
		productType.getStyleClass().add("display-label");
		productTypeContextLabel = UiFactory.createLabel("siteNameContextLabel");
		productTypeContextLabel.setPadding(new Insets(0, 0, 0,15));
		productContainer.getChildren().addAll(productType, productTypeContextLabel);
		productContainer.setAlignment(Pos.CENTER_LEFT);
		productContainer.setPadding(new Insets(10, 10, 10, 10));
		productContainer.setSpacing(10);
		return productContainer;
	}
	/**
	 *this method is used to Get Entry Model Name
	 */
	private HBox getEntryModelNameContainer() {
		HBox nameContainer = new HBox();
		modelName = UiFactory.createLabel("modelName", "Entry Model");
		modelName.getStyleClass().add("display-label");
		modelName.setPadding(new Insets(0, 0, 0, 60));
		LoggedLabel asteriskLbl = getAsteriskLabel(UiFactory.createLabel("label", "*"));
		nameContainer.getChildren().addAll(modelName, asteriskLbl);
		nameContainer.getChildren().add(getNameFieldContainer());
		nameContainer.setAlignment(Pos.CENTER_LEFT);
		nameContainer.setPadding(new Insets(10, 10, 10, 10));
		nameContainer.setSpacing(10);
		return nameContainer;
	}
	/**
	 *this method is used to Get Entry Model Name
	 */
	private HBox getNameFieldContainer() {
		HBox nameFieldContainer = new HBox();
		entryModelName = (UpperCaseFieldBean) UiFactory.createTextField("entryModelNameTxt",32,Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT, true);
		entryModelName.setId("entryModelName");
		nameFieldContainer.getChildren().add(entryModelName);
		nameFieldContainer.setPadding(new Insets(0, 0, 0,5));
		return nameFieldContainer;
	}
	/**
	 *this method is used to Get Entry model description
	 */
	private HBox getDescriptionContainer() {
		HBox descContainer = new HBox();
		modelDesc = UiFactory.createLabel("modelDesc", "Entry Model Description");
		modelDesc.getStyleClass().add("display-label");
		entryModelDesc =(UpperCaseFieldBean) UiFactory.createTextField("entryModelDesc",23,Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT, true);
		entryModelDesc.setId("entryModelDesc");
		entryModelDesc.setPrefSize(350, 20);
		descContainer.getChildren().add(modelDesc);
		descContainer.getChildren().add(entryModelDesc);
		descContainer.setAlignment(Pos.CENTER_LEFT);
		descContainer.setPadding(new Insets(10, 10, 10, 20));
		descContainer.setSpacing(10);
		return descContainer;
	}
	/**
	 *this method is used to Get reason for change
	 */
	private HBox getReasonContainer() {
		HBox reasonContainer = new HBox();
		reasonForChangeLabel = UiFactory.createLabel("reasonForChangeLabel", "Reason For Change");
		reasonForChangeLabel.getStyleClass().add("display-label");
		LoggedLabel asteriskLbl2 = getAsteriskLabel(UiFactory.createLabel("label", "*"));
		reasonContainer.getChildren().add(reasonForChangeLabel);
		reasonContainer.getChildren().add(asteriskLbl2);
		reasonContainer.getChildren().add(getReasonTextContainer());
		reasonContainer.setAlignment(Pos.CENTER_LEFT);
		reasonContainer.setPadding(new Insets(10, 10, 10, 30));
		reasonContainer.setSpacing(10);
		return reasonContainer;
	}
	/**
	 *this method is used Get reason for change text
	 */
	private HBox getReasonTextContainer() {
		HBox reasonTextContainer = new HBox();
		reasonForChangeText = UiFactory.createTextArea();
		reasonForChangeText.setId("reasonForChangeText");
		reasonForChangeText.setPrefSize(350,50);
		reasonForChangeText.setWrapText(true);
		reasonTextContainer.getChildren().add(reasonForChangeText);
		reasonTextContainer.setPadding(new Insets(0, 0, 0,5));
		return reasonTextContainer;
	}
	/**
	 * Get button container
	 */
	private HBox getButtonContainer() {
		HBox buttonContainer = new HBox();
		createBtn = createBtn(QiConstant.CREATE, getController());
		 createBtn.setPadding(new Insets(5,5,5,5));
		 updateButton = createBtn(QiConstant.UPDATE, getController());
		 updateButton.setPadding(new Insets(5,5,5,5));
		 cancelBtn = createBtn(QiConstant.CANCEL, getController());
		 cancelBtn.setPadding(new Insets(5,5,5,5));
		buttonContainer.getChildren().addAll(createBtn, updateButton, cancelBtn);
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setPadding(new Insets(5, 5, 5, 5));
		buttonContainer.setSpacing(10);
		return buttonContainer;
	}


	private LoggedLabel getAsteriskLabel(LoggedLabel loggedLabel) {
		loggedLabel = UiFactory.createLabel("label", "*");
		loggedLabel.setStyle("-fx-text-fill: red");
		return loggedLabel;
	}
	/**
	 * This method is used to load Update data.
	 */
	private void loadUpdateData() {
			createBtn.setDisable(true);
			getActiveRadioBtn().setSelected(selectedModel.isActive());
			getInactiveRadioBtn().setSelected(!selectedModel.isActive());
			productTypeContextLabel.setText(selectedModel.getProductType());
			entryModelName.setText(selectedModel.getId().getEntryModel());
			entryModelDesc.setText(selectedModel.getEntryModelDescription());
		}

	/**
	 * This method is used to load Create data.
	 */
	private void loadCreateData() {
			productTypeContextLabel.setText(selectedModel.getProductType());
			selectedModel = new QiEntryModel();
			updateButton.setDisable(true);
			reasonForChangeText.setDisable(true);
	}

	public LoggedTextArea getReasonForChangeText() {
		return reasonForChangeText;
	}

	public LoggedLabel getProductTypeContextLabel() {
		return productTypeContextLabel;
	}

	public UpperCaseFieldBean getEntryModelName() {
		return entryModelName;
	}

	public UpperCaseFieldBean getEntryModelDesc() {
		return entryModelDesc;
	}

	public LoggedButton getCreateBtn() {
		return createBtn;
	}

	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}


}
