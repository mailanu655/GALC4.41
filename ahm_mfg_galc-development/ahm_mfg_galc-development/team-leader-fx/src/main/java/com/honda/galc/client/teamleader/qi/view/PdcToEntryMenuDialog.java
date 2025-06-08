package com.honda.galc.client.teamleader.qi.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.honda.galc.client.teamleader.qi.controller.PdcToEntryMenuDialogController;
import com.honda.galc.client.teamleader.qi.model.PdcToEntryScreenAssignmentModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.entity.qi.QiTextEntryMenu;

public class PdcToEntryMenuDialog extends QiFxDialog<PdcToEntryScreenAssignmentModel>{

	private String title;
	private LoggedButton createBtn ;
	private LoggedButton cancelBtn;
	private LoggedTextArea menuDescTxtFld;
	private UpperCaseFieldBean menuNameTxtFld;
	private LoggedTextArea reasonForChangeTextArea;


	private PdcToEntryMenuDialogController pdcToEntryMenuDialogController ;
	private QiTextEntryMenu selectedComb;

	public PdcToEntryMenuDialog(String title, QiTextEntryMenu selectedComb,PdcToEntryScreenAssignmentModel model, QiEntryScreenDto qiEntryModelDto,String applicationId) {
		super(title, applicationId, model);//Fix : Passing applicationId as parameter to fetch correct owner stage.
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);

		this.pdcToEntryMenuDialogController = new PdcToEntryMenuDialogController(model,this,selectedComb,qiEntryModelDto);
		setModel(model);
		initComponents();
		pdcToEntryMenuDialogController.initListeners();
		this.title = title;
		this.selectedComb = selectedComb;
		if(this.title.equalsIgnoreCase(QiConstant.CREATE))
			loadCreateData();
		else if(this.title.equalsIgnoreCase(QiConstant.UPDATE))
			loadUpdateData();

	}


	private void initComponents() {

		VBox outerPane = new VBox();
		HBox menuNameContainer = new HBox();
		HBox menuDescTxtAreaContainer = new HBox();
		HBox reasonForChangeContainer = new HBox();
		HBox btnContainer = new HBox();

		LoggedLabel menuNameLbl = UiFactory.createLabel("label", "Menu Name");
		menuNameLbl.getStyleClass().add("display-label");
		LoggedLabel asteriskLbl = getAsteriskLabel(UiFactory.createLabel("label","*"));

		menuNameTxtFld = (UpperCaseFieldBean) UiFactory.createTextField("menuNameTxt", 23, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT, true);
		menuNameContainer.setAlignment(Pos.CENTER);
		menuNameContainer.setPadding(new Insets(5,5,5,5));
		menuNameContainer.setSpacing(10);
		menuNameContainer.getChildren().addAll(menuNameLbl,asteriskLbl,menuNameTxtFld);


		LoggedLabel menuDescLbl = UiFactory.createLabel("label", "Menu Description");
		menuDescLbl.getStyleClass().add("display-label");


		menuDescTxtFld =  UiFactory.createTextArea("");
		menuDescTxtFld.setPrefSize(275, 75);
		menuDescTxtFld.setWrapText(true);
		menuDescTxtAreaContainer.setPadding(new Insets(10, 10, 10, 40));
		menuDescTxtAreaContainer.setSpacing(10);
		menuDescTxtAreaContainer.getChildren().addAll(menuDescLbl,menuDescTxtFld);

		LoggedLabel reasonForChangeLabel = UiFactory.createLabel("label", "Reason For Change");
		reasonForChangeLabel.getStyleClass().add("display-label");
		LoggedLabel asterisk = getAsteriskLabel(UiFactory.createLabel("label","*"));

		reasonForChangeTextArea = UiFactory.createTextArea("");
		reasonForChangeTextArea.setPrefSize(275, 75);
		reasonForChangeTextArea.setWrapText(true);
		reasonForChangeContainer.setPadding(new Insets(10, 10, 10, 10));
		reasonForChangeContainer.setSpacing(10);
		reasonForChangeContainer.getChildren().addAll(reasonForChangeLabel,asterisk, reasonForChangeTextArea);

		createBtn = createBtn(QiConstant.CREATE,getPdcToEntryMenuDialogController());
		createBtn.setPadding(new Insets(50, 30, 10, 130));
		updateButton = createBtn(QiConstant.UPDATE, getPdcToEntryMenuDialogController());
		updateButton.setPadding(new Insets(50, 30, 10, 130));
		cancelBtn =  createBtn(QiConstant.CANCEL, getPdcToEntryMenuDialogController());
		cancelBtn.setPadding(new Insets(50, 30, 10, 130));
		btnContainer.setSpacing(30);
		btnContainer.setPadding(new Insets(30, 0, 0, 100));
		btnContainer.getChildren().addAll(createBtn,updateButton,cancelBtn);

		outerPane.getChildren().addAll( menuNameContainer,  menuDescTxtAreaContainer, reasonForChangeContainer,btnContainer);

		((BorderPane) this.getScene().getRoot()).setCenter(createTitiledPane("",outerPane));
	}

	private TitledPane createTitiledPane(String title,Node content) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(content);
		titledPane.setPrefSize(500, 300);
		return titledPane;
	}

	private LoggedLabel getAsteriskLabel(LoggedLabel loggedLabel){
		loggedLabel=UiFactory.createLabel("label","*");
		loggedLabel.setStyle("-fx-text-fill: red");
		return loggedLabel;
	}

	private void loadCreateData()
	{
		try {
			updateButton.setDisable(true);
			reasonForChangeTextArea.setDisable(true);
			getMenuNameTxtFld().settext("");
			getMenuDescTxtFld().setText("");
		} catch (Exception e) {
			pdcToEntryMenuDialogController.handleException("An error occured in loading pop up screen ", "Failed to Open Create Text Entry Menu popup screen", e);
		}
	}
	/**
	 * This method is used to load Update data.
	 */
	private void loadUpdateData()
	{
		try {
			getMenuNameTxtFld().settext(selectedComb.getId().getTextEntryMenu());
			getMenuDescTxtFld().setText(selectedComb.getTextEntryMenuDesc());
			createBtn.setDisable(true);
			updateButton.setDisable(true);
		} catch (Exception e) {
			pdcToEntryMenuDialogController.handleException("An error occured in loading pop up screen ", "Failed to Open Update Part Location Combination popup screen", e);
		}
	}


	public LoggedTextArea getMenuDescTxtFld() {
		return menuDescTxtFld;
	}


	public UpperCaseFieldBean getMenuNameTxtFld() {
		return menuNameTxtFld;
	}


	public LoggedButton getCreateBtn() {
		return createBtn;
	}


	public LoggedTextArea getReasonForChangeTextArea() {
		return reasonForChangeTextArea;
	}


	public QiTextEntryMenu getSelectedComb() {
		return selectedComb;
	}


	public void setSelectedComb(QiTextEntryMenu selectedComb) {
		this.selectedComb = selectedComb;
	}


	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}


	public PdcToEntryMenuDialogController getPdcToEntryMenuDialogController() {
		return pdcToEntryMenuDialogController;
	}


	public void setPdcToEntryMenuDialogController(PdcToEntryMenuDialogController pdcToEntryMenuDialogController) {
		this.pdcToEntryMenuDialogController = pdcToEntryMenuDialogController;
	}

}
