package com.honda.galc.client.teamleader.qi.view;

import java.util.List;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.WindowEvent;

import com.honda.galc.client.teamleader.qi.controller.ImportMtcModelController;
import com.honda.galc.client.teamleader.qi.model.ImportMtcModelModel;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.qi.QiEntryScreenDto;
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */

public class ImportMtcModelDialog extends QiFxDialog<ImportMtcModelModel>{
	private LoggedButton createBtn;
	private LoggedButton cancelBtn;	
	private LoggedButton importBtn;
	private LoggedButton exportBtn;
	private ImportMtcModelController controller;
	private LoggedComboBox<String> productTypeCombobox;
	private LoggedComboBox<String> plantCombobox;		
	private LoggedLabel siteNameLabel;		
	private LoggedLabel productTypeNameLabel;		
	private UpperCaseFieldBean entryModelText;
	private UpperCaseFieldBean entryModelDescText;
	private TextArea statusTextArea;
	private LoggedLabel entryModelVersionLabelText;
	private double screenHeight = 0.0D;
	private double screenWidth = 0.0D;
	

	public ImportMtcModelDialog(String title, QiEntryScreenDto entryScreen,ImportMtcModelModel model,String applicationId) {
		super(title, applicationId,model);//Fix : Passing applicationId as parameter to fetch correct owner stage.		
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.controller = new ImportMtcModelController(model, this);
		this.setOnCloseRequest( new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
			    close(event);	
			}
		});
		initComponents(title);
		if(title.equals(QiConstant.IMPORT))  {
			loadImportData();
		}
		if(title.equals(QiConstant.EXPORT))  {
			loadExportData();
		}
		controller.initListeners();
		Logger.getLogger().check("Entry Screen Dialog populated");
	}
	
	@SuppressWarnings("unchecked")
	private void loadExportData() {
		try {
	
			List<String> entryPlantList=getModel().findPlantBySite(getDefaultSiteName());
			plantCombobox.getItems().addAll(entryPlantList);
		} catch (Exception e) {
			controller.handleException("An error occured in loading Export pop up  ", "Failed to Open Export popup screen", e);
		}
	}

	@SuppressWarnings("unchecked")
	private void loadImportData()
	{
		try {
			List<String> entryPlantList=getModel().findPlantBySite(getDefaultSiteName());
			plantCombobox.getItems().addAll(entryPlantList);
		} catch (Exception e) {
			controller.handleException("An error occured in loading Import pop up  ", "Failed to Open Import popup screen", e);
		}
	}



	private void initComponents(String title) {	
		screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
		screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
		VBox outerPane = new VBox();
		outerPane.setPrefHeight(screenHeight/1.333);
		outerPane.setPrefWidth(screenWidth/2.0);
		HBox buttonContainer = new HBox();

		setButtonContainer(buttonContainer,title);		

		HBox allContent = new HBox(createLabels(), createControls());
		allContent.setSpacing(25.0);
		allContent.setPadding(new Insets(20,0,0,0));
		outerPane.getChildren().addAll(allContent, buttonContainer);
		outerPane.setSpacing(25.0);
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);

	}

	
	private VBox createLabels()  {
		LoggedLabel siteLabel=UiFactory.createLabel("lblSite","Site: ");
		siteLabel.getStyleClass().add("display-label");
		HBox siteLabelCell = new HBox(siteLabel);
		siteLabelCell.setAlignment(Pos.CENTER_RIGHT);
		
		LoggedLabel plantLabel=UiFactory.createLabel("lblEntryPlant","Entry Plant");
		plantLabel.getStyleClass().add("display-label");
		HBox plantLabelCell = new HBox();
		plantLabelCell.getChildren().addAll(plantLabel,getAsteriskLabel(UiFactory.createLabel("lblAst1","*")));
		plantLabelCell.setAlignment(Pos.CENTER_RIGHT);
		plantLabelCell.setPadding(new Insets(3,0,0,10));
		
		LoggedLabel productTypeLabel=UiFactory.createLabel("lblProdType","ProductType: ");
		productTypeLabel.getStyleClass().add("display-label");
		HBox productTypeLabelCell = new HBox(productTypeLabel);
		productTypeLabelCell.setAlignment(Pos.CENTER_RIGHT);
		productTypeLabelCell.setPadding(new Insets(6,0,0,10));
		
		LoggedLabel entryModelLabel=UiFactory.createLabel("lblEntryModel","NEW Entry Model Name: ");
		entryModelLabel.getStyleClass().add("display-label");
		HBox entryModelLabelCell = new HBox(entryModelLabel, getAsteriskLabel(UiFactory.createLabel("lblAst2","*")));
		entryModelLabelCell.setAlignment(Pos.CENTER_RIGHT);
		entryModelLabelCell.setPadding(new Insets(5,0,0,10));

		LoggedLabel entryModelDescLabel=UiFactory.createLabel("lblDesc","Entry Model Desc ");
		entryModelDescLabel.getStyleClass().add("display-label");
		HBox entryModelDescLabelCell = new HBox(entryModelDescLabel);
		entryModelDescLabelCell.setAlignment(Pos.CENTER_RIGHT);
		entryModelDescLabelCell.setPadding(new Insets(5,0,0,10));

		LoggedLabel statusLabel = UiFactory.createLabel("lblStatus","Status");
		statusLabel.getStyleClass().add("display-label");
		HBox statusLabelCell = new HBox(statusLabel);
		statusLabelCell.setAlignment(Pos.CENTER_RIGHT);
		statusLabelCell.setPadding(new Insets(8,0,0,0));
		
		VBox labelVBox = new VBox(
				siteLabelCell,
				plantLabelCell,
				productTypeLabelCell,
				entryModelLabelCell,
				entryModelDescLabelCell,
				statusLabelCell);
		labelVBox.setSpacing(20.0);
		return labelVBox;
	}
	
	private VBox createControls()  {
		siteNameLabel = UiFactory.createLabel("label",getModel().getSiteName());
		HBox siteNameCell = new HBox(siteNameLabel);
		siteNameCell.setAlignment(Pos.TOP_LEFT);
		
		plantCombobox = new LoggedComboBox<String>();
		plantCombobox.setId("plantCmbBox");
		plantCombobox.getStyleClass().add("combo-box-base");
		HBox plantComboCell = new HBox(plantCombobox);
		plantComboCell.setAlignment(Pos.TOP_LEFT);
		
		entryModelText = new UpperCaseFieldBean("entryModelId");
		HBox entryModelCell = new HBox(entryModelText);
		entryModelCell.setAlignment(Pos.TOP_LEFT);
		
		productTypeNameLabel = UiFactory.createLabel("label",getModel().getProductType());
		HBox productTypeNameCell = new HBox(productTypeNameLabel);
		productTypeNameCell.setAlignment(Pos.TOP_LEFT);
		
		entryModelDescText = new UpperCaseFieldBean("entryModelDescId");
		HBox entryModelDescCell = new HBox(entryModelDescText);
		entryModelDescCell.setAlignment(Pos.TOP_LEFT);
		
		statusTextArea = new TextArea();
		statusTextArea.setStyle("text-area-background: white;");
		statusTextArea.setId("statusTxtArea");
		statusTextArea.setPrefRowCount(20);
		statusTextArea.setWrapText(true);
		statusTextArea.setDisable(false);
		statusTextArea.setPrefHeight(screenHeight/4.0);
		statusTextArea.setPrefWidth(screenWidth/4.0);
		statusTextArea.getStyleClass().add("display-label-blue");
		statusTextArea.getStyleClass().add("font-weight-normal");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(statusTextArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(screenHeight/4.0);
        scrollPane.setPrefWidth(screenWidth/4.0);		

		HBox statusContentCell = new HBox(statusTextArea);
		statusContentCell.setAlignment(Pos.TOP_LEFT);
		
		VBox controlVBox = new VBox(
				siteNameCell,
				plantComboCell,
				productTypeNameCell,
				entryModelCell,
				entryModelDescCell,
				statusContentCell);
		controlVBox.setSpacing(20.0);
		return controlVBox;
	}
	

	private void setButtonContainer(HBox buttonContainer,String title) {
		if(title.equals(QiConstant.IMPORT))  {
			importBtn = createBtn(QiConstant.IMPORT, getController());
		}
		if(title.equals(QiConstant.EXPORT))  {
			exportBtn = createBtn(QiConstant.EXPORT, getController());

		}
	    
		cancelBtn = createBtn(QiConstant.CANCEL,getController());		
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setPadding(new Insets(5,5,5,5));
		buttonContainer.setSpacing(10);
		if(title.equals(QiConstant.IMPORT))  {
			buttonContainer.getChildren().addAll(importBtn, cancelBtn);
		}
		if(title.equals(QiConstant.EXPORT))  {
			buttonContainer.getChildren().addAll(exportBtn, cancelBtn);
		}

	}

 	public void close(WindowEvent event) {
 	   if(getController().onClose())  {
 		   super.close();
 	   }
 	   else   {
 		   event.consume();
 	   }
	}


	private String getDefaultSiteName() {
		return getModel().getSiteName();
	}

	public TextArea getStatusTextArea() {
		return statusTextArea;
	}

	public void setStatusTextArea(TextArea statusTextArea) {
		this.statusTextArea = statusTextArea;
	}

	public LoggedComboBox<String> getProductTypeCombobox() {
		return productTypeCombobox;
	}

	public void setProductTypeCombobox(LoggedComboBox<String> productTypeCombobox) {
		this.productTypeCombobox = productTypeCombobox;
	}

	private LoggedLabel getAsteriskLabel(LoggedLabel loggedLabel){
		loggedLabel=UiFactory.createLabel("label","*");
		loggedLabel.setStyle("-fx-text-fill: red");
		return loggedLabel;
	}
	
	public ImportMtcModelController getController() {
		return controller;
	}

	public void setController(ImportMtcModelController controller) {
		this.controller = controller;
	}	

	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}
	
	public LoggedButton getExportBtn() {
		return exportBtn;
	}
	
	public LoggedButton getImportBtn() {
		return importBtn;
	}

	public LoggedButton getCreateBtn() {
		return createBtn;
	}

	public LoggedComboBox<String> getPlantCombobox() {
		return plantCombobox;
	}

	public void setPlantCombobox(LoggedComboBox<String> plantCombobox) {
		this.plantCombobox = plantCombobox;
	}
	
	public LoggedLabel getEntryModelVersionLabelText() {
		return entryModelVersionLabelText;
	}

	/**
	 * @return the entryModelText
	 */
	public UpperCaseFieldBean getEntryModelText() {
		return entryModelText;
	}

	/**
	 * @param entryModelText the entryModelText to set
	 */
	public void setEntryModelText(UpperCaseFieldBean entryModelText) {
		this.entryModelText = entryModelText;
	}

	/**
	 * @return the entryModelDescText
	 */
	public UpperCaseFieldBean getEntryModelDescText() {
		return entryModelDescText;
	}

	/**
	 * @param entryModelDescText the entryModelDescText to set
	 */
	public void setEntryModelDescText(UpperCaseFieldBean entryModelDescText) {
		this.entryModelDescText = entryModelDescText;
	}

}
