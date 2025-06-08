package com.honda.galc.client.teamleader.common;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.honda.galc.client.teamleader.qi.view.QiFxDialog;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.conf.RegionalCode;

 public class RegionalDataDialog extends QiFxDialog<RegionalDataMaintenanceModel> {

	private LoggedLabel regionalCodeNameLabel;
	private LoggedLabel valueLabel;
	private LoggedLabel nameLabel;
	private LoggedLabel abbreviationLabel;
	private LoggedLabel descriptionLabel;
	private LoggedLabel reasonForChangeLabel;
	
	private UpperCaseFieldBean regionalCodeNameField;
	private LoggedTextField valueField;
	private LoggedTextField nameField;
	private LoggedTextField abbreviationField;
	private LoggedTextField descriptionField;	
	private LoggedTextArea reasonForChangeTextArea;
	
	private LoggedButton createBtn;
	private LoggedButton updateBtn;
	private LoggedButton cancelBtn;
	
	private String title;
	private RegionalDataDialogController controller;
	private RegionalCode selectedRegionalCode;
	private RegionalCode selectedRegionalValue;

	public RegionalDataDialog(String title, RegionalDataMaintenanceModel model, String applicationId) {

		super(title, applicationId, model);
		this.title = title;
		this.selectedRegionalCode = model.getSelectedRegionalCode();
		this.selectedRegionalValue = model.getSelectedRegionalValue();
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.controller = new RegionalDataDialogController(model, this);

		initComponents();
		
		if (this.title.equals(RegionalDataConstant.CREATE_CODE)) {
			reasonForChangeTextArea.setDisable(true);
			updateBtn.setDisable(true);
		} else if (this.title.equals(RegionalDataConstant.CREATE_VALUE)) {
			regionalCodeNameField.setText(selectedRegionalCode.getId().getRegionalCodeName());
			regionalCodeNameField.setDisable(true);
			reasonForChangeTextArea.setDisable(true);
			updateBtn.setDisable(true);
		} else if (this.title.equals(RegionalDataConstant.UPDATE_CODE)) {
			regionalCodeNameField.setText(selectedRegionalCode.getId().getRegionalCodeName());
			valueField.setText("");
			nameField.setText("");
			abbreviationField.setText("");
			descriptionField.setText("");
			valueField.setDisable(true);
			nameField.setDisable(true);
			abbreviationField.setDisable(true);
			descriptionField.setDisable(true);
			createBtn.setDisable(true);
			updateBtn.setDisable(true);
		} else if (this.title.equals(RegionalDataConstant.UPDATE_VALUE)) {
			regionalCodeNameField.setText(selectedRegionalValue.getId().getRegionalCodeName());
			valueField.setText(selectedRegionalValue.getId().getRegionalValue());
			nameField.setText(selectedRegionalValue.getRegionalValueName());
			abbreviationField.setText(selectedRegionalValue.getRegionalValueAbbr());
			descriptionField.setText(selectedRegionalValue.getRegionalValueDesc());
			regionalCodeNameField.setDisable(true);
			createBtn.setDisable(true);
			updateBtn.setDisable(true);
		}
		controller.initListeners();
	}
	/**
	 * This method is used to initialize the components of panel
	 */
	private void initComponents() {
		VBox vBox = new VBox();
		vBox.getChildren().add(createRegionalCodeNameHBox());
		vBox.getChildren().add(createValueHBox());
		vBox.getChildren().add(createNameHBox());
		vBox.getChildren().add(createAbbreviationHBox());
		vBox.getChildren().add(createDescriptionHBox());
		vBox.getChildren().add(createReasonHBox());
		vBox.getChildren().add(createButtonHBox());
		((BorderPane) this.getScene().getRoot()).setCenter(vBox);
	}
	public RegionalDataDialogController getController() {
		return controller;
	}

	public void setController(RegionalDataDialogController controller) {
		this.controller = controller;
	}
	
	private HBox createRegionalCodeNameHBox() {
		HBox hBox = new HBox();
		regionalCodeNameLabel = UiFactory.createLabel("regionalCodeNameLabel", "Regional Code Name");
		regionalCodeNameLabel.getStyleClass().add("display-label");
		regionalCodeNameField = (UpperCaseFieldBean) UiFactory.createTextField("regionalCodeNameField", 32, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT, true);
		hBox.getChildren().addAll(regionalCodeNameLabel, getAsteriskLabel(), regionalCodeNameField);
		hBox.setAlignment(Pos.CENTER_LEFT);
		hBox.setPadding(new Insets(40, 10, 10, 10));
		hBox.setSpacing(10);
		return hBox;
	}
	
	private HBox createValueHBox() {
		HBox hBox = new HBox();
		valueLabel = UiFactory.createLabel("value", "Value");
		valueLabel.getStyleClass().add("display-label");
		valueField = (LoggedTextField) UiFactory.createTextField("valueField", 32, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT, false);
		hBox.getChildren().addAll(valueLabel, getAsteriskLabel(), valueField);
		hBox.setAlignment(Pos.CENTER_LEFT);
		hBox.setPadding(new Insets(10, 10, 10, 102));
		hBox.setSpacing(10);
		return hBox;
	}
	
	private HBox createNameHBox() {
		HBox hBox = new HBox();
		nameLabel = UiFactory.createLabel("name", "Name");
		nameLabel.getStyleClass().add("display-label");
		nameField = (LoggedTextField) UiFactory.createTextField("nameField", 32, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT, false);
		hBox.getChildren().addAll(nameLabel, nameField);
		hBox.setAlignment(Pos.CENTER_LEFT);
		hBox.setPadding(new Insets(10, 10, 10, 118));
		hBox.setSpacing(10);
		return hBox;
	}

	private HBox createAbbreviationHBox() {
		HBox hBox = new HBox();
		abbreviationLabel = UiFactory.createLabel("abbreviation", "Abbreviation");
		abbreviationLabel.getStyleClass().add("display-label");
		abbreviationField =(LoggedTextField) UiFactory.createTextField("abbreviationField", 32, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT, false);

		hBox.getChildren().addAll(abbreviationLabel, abbreviationField);
		hBox.setAlignment(Pos.CENTER_LEFT);
		hBox.setPadding(new Insets(10, 10, 10, 77));
		hBox.setSpacing(10);
		return hBox;
	}
	/**
	 *this method is used to Get Entry model description
	 */
	private HBox createDescriptionHBox() {
		HBox hBox = new HBox();
		descriptionLabel = UiFactory.createLabel("description", "Description");
		descriptionLabel.getStyleClass().add("display-label");
		descriptionField =(LoggedTextField) UiFactory.createTextField("descriptionField", 32, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT, false);
		hBox.getChildren().addAll(descriptionLabel, descriptionField);
		hBox.setAlignment(Pos.CENTER_LEFT);
		hBox.setPadding(new Insets(10, 10, 10, 85));
		hBox.setSpacing(10);
		return hBox;
	}
	/**
	 *this method is used to Get reason for change
	 */
	private HBox createReasonHBox() {
		HBox hBox = new HBox();
		reasonForChangeLabel = UiFactory.createLabel("reasonForChangeLabel", "Reason For Change");
		reasonForChangeLabel.getStyleClass().add("display-label");
		reasonForChangeTextArea = UiFactory.createTextArea();
		reasonForChangeTextArea.setId("reasonForChangeText");
		reasonForChangeTextArea.setPrefSize(380, 60);
		reasonForChangeTextArea.setWrapText(true);
		
		hBox.getChildren().addAll(reasonForChangeLabel, getAsteriskLabel(), reasonForChangeTextArea);

		hBox.setAlignment(Pos.CENTER_LEFT);
		hBox.setPadding(new Insets(10, 10, 10, 20));
		hBox.setSpacing(10);
		return hBox;
	}

	/**
	 * Get button container
	 */
	private HBox createButtonHBox() {
		HBox hBox = new HBox();
		createBtn = createBtn("Create", getController());
		createBtn.setPadding(new Insets(5,5,5,5));
		updateBtn = createBtn("Update", getController());
		updateBtn.setPadding(new Insets(5,5,5,5));
		cancelBtn = createBtn("Cancel", getController());
		cancelBtn.setPadding(new Insets(5,5,5,5));
		hBox.getChildren().addAll(createBtn, updateBtn, cancelBtn);
		hBox.setAlignment(Pos.CENTER);
		hBox.setPadding(new Insets(5, 5, 5, 5));
		hBox.setSpacing(10);
		return hBox;
	}

	private LoggedLabel getAsteriskLabel() {
		LoggedLabel loggedLabel = UiFactory.createLabel("label", "*");
		loggedLabel.setStyle("-fx-text-fill: red");
		return loggedLabel;
	}

	public UpperCaseFieldBean getRegionalCodeNameField() {
		return regionalCodeNameField;
	}

	public LoggedTextField getValueField() {
		return valueField;
	}

	public LoggedTextField getNameField() {
		return nameField;
	}
	
	public LoggedButton getCreateBtn() {
		return createBtn;
	}

	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}

	public LoggedButton getUpdateBtn() {
		return updateBtn;
	}
	
	public LoggedTextField getAbbreviationField() {
		return abbreviationField;
	}
	
	public LoggedTextField getDescriptionField() {
		return descriptionField;
	}
	
	public LoggedTextArea getReasonForChangeTextArea() {
		return reasonForChangeTextArea;
	}
	
	public String getScreenName() {
		return "Regional Data Dialog";
	}
}
