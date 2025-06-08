package com.honda.galc.client.teamleader.qi.view;

import java.util.List;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.WindowEvent;

import com.honda.galc.client.teamleader.qi.controller.ExportMtcModelController;
import com.honda.galc.client.teamleader.qi.model.ExportMtcModelModel;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.entity.qi.QiEntryScreen;
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */

public class ExportMtcModelDialog extends QiFxDialog<ExportMtcModelModel>{
	private LoggedButton createBtn;
	private LoggedButton cancelBtn;	
	private LoggedButton importBtn;
	private LoggedButton exportBtn;
	private ExportMtcModelController controller;
	private LoggedComboBox<String> productTypeCombobox;
	private LoggedComboBox<String> plantCombobox;		
	private LoggedLabel siteNameLabel;		
	private LoggedLabel entryModelLabel;		
	private ListView<String> entryDepartmentListViewMultiSelection;	
	private UpperCaseFieldBean entryScreenNameTextField;
	private UpperCaseFieldBean entryScreenDescriptionTextField;	
	private LoggedRadioButton imageRadioButton;
	private LoggedRadioButton textRadioButton;	
	private QiEntryScreen entryScreen;
	private LoggedLabel entryModelVersionLabelText;
	private LoggedTextArea statusTextArea;
	private double screenHeight = 0.0D;
	private double screenWidth = 0.0D;

	public ExportMtcModelDialog(String title, QiEntryScreenDto entryScreen,ExportMtcModelModel model,String applicationId) {
		super(title, applicationId,model);//Fix : Passing applicationId as parameter to fetch correct owner stage.		
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.controller = new ExportMtcModelController(model, this);
		this.setOnCloseRequest(new EventHandler<WindowEvent>() {
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
			statusTextArea.setDisable(true);	
		} catch (Exception e) {
			controller.handleException("An error occured in loading Import pop up  ", "Failed to Open Import popup screen", e);
		}
	}



	private void initComponents(String title) {			
		VBox outerPane = new VBox();
		screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
		screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
		outerPane.setPrefHeight(screenHeight/1.333);
		outerPane.setPrefWidth(screenWidth/2.0);
		HBox buttonContainer = new HBox();

		setButtonContainer(buttonContainer,title);		

		HBox allContent = new HBox(createLabels(), createControls());
		allContent.setSpacing(25.0);
		outerPane.getChildren().addAll(allContent, buttonContainer);
		outerPane.setSpacing(25.0);
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);

	}

	
	private VBox createLabels()  {
		LoggedLabel siteLabel=UiFactory.createLabel("label","Site: ");
		siteLabel.getStyleClass().add("display-label");
		HBox siteLabelCell = new HBox(siteLabel);
		siteLabelCell.setAlignment(Pos.CENTER_RIGHT);
		
		LoggedLabel plantLabel=UiFactory.createLabel("label","Entry Plant");
		plantLabel.getStyleClass().add("display-label");
		LoggedLabel asteriskLabel=getAsteriskLabel(UiFactory.createLabel("label","*"));
		HBox plantLabelCell = new HBox();
		plantLabelCell.getChildren().addAll(plantLabel,asteriskLabel);
		plantLabelCell.setAlignment(Pos.CENTER_RIGHT);
		
		LoggedLabel entryModelLabel=UiFactory.createLabel("label","Entry Model: ");
		entryModelLabel.getStyleClass().add("display-label");
		HBox entryModelLabelCell = new HBox(entryModelLabel);
		entryModelLabelCell.setAlignment(Pos.CENTER_RIGHT);
		entryModelLabelCell.setPadding(new Insets(10,0,0,10));

		LoggedLabel statusLabel = UiFactory.createLabel("status","Status");
		statusLabel.getStyleClass().add("display-label");
		HBox statusLabelCell = new HBox(statusLabel);
		statusLabelCell.setAlignment(Pos.CENTER_RIGHT);
		statusLabelCell.setPadding(new Insets(10,0,0,0));
		
		VBox labelVBox = new VBox(siteLabelCell, plantLabelCell, entryModelLabelCell, statusLabelCell);
		labelVBox.setSpacing(25.0);
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
		
		entryModelLabel = UiFactory.createLabel("label",getModel().getEntryModel());
		HBox entryModelCell = new HBox(entryModelLabel);
		entryModelCell.setAlignment(Pos.TOP_LEFT);
		
		statusTextArea = UiFactory.createTextArea();
		statusTextArea.setId("statusTxtArea");
		statusTextArea.setStyle("text-area-background: white;");
		statusTextArea.setPrefRowCount(20);
		statusTextArea.setWrapText(true);		
		statusTextArea.setDisable(false);
		statusTextArea.getStyleClass().add("display-label-blue");
		statusTextArea.getStyleClass().add("font-weight-normal");
		
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(statusTextArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(screenHeight/4.0);
        scrollPane.setPrefWidth(screenWidth/4.0);		

		HBox statusContentCell = new HBox(statusTextArea);
		statusContentCell.setAlignment(Pos.TOP_LEFT);
	

		VBox controlVBox = new VBox(siteNameCell, plantComboCell, entryModelCell, statusContentCell);
		controlVBox.setSpacing(25.0);
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
	
	private String getDefaultSiteName() {
		return getModel().getSiteName();
	}

	public LoggedRadioButton getImageRadioButton() {
		return imageRadioButton;
	}

	public void setImageRadioButton(LoggedRadioButton imageRadioButton) {
		this.imageRadioButton = imageRadioButton;
	}

	public LoggedRadioButton getTextRadioButton() {
		return textRadioButton;
	}

	public void setTextRadioButton(LoggedRadioButton textRadioButton) {
		this.textRadioButton = textRadioButton;
	}

	public ListView<String> getEntryDepartmentListViewMultiSelection() {
		return entryDepartmentListViewMultiSelection;
	}

	public void setEntryDepartmentListViewMultiSelection(ListView<String> entryDepartmentListViewMultiSelection) {
		this.entryDepartmentListViewMultiSelection = entryDepartmentListViewMultiSelection;
	}

	public QiEntryScreen getEntryScreen() {
		return entryScreen;
	}

	public void setEntryScreen(QiEntryScreen entryScreen) {
		this.entryScreen = entryScreen;
	}

	public UpperCaseFieldBean getEntryScreenNameTextField() {
		return entryScreenNameTextField;
	}

	public void setEntryScreenNameTextField(UpperCaseFieldBean entryScreenNameTextField) {
		this.entryScreenNameTextField = entryScreenNameTextField;
	}

	public UpperCaseFieldBean getEntryScreenDescriptionTextField() {
		return entryScreenDescriptionTextField;
	}

	public void setEntryScreenDescriptionTextField(UpperCaseFieldBean entryScreenDescriptionTextField) {
		this.entryScreenDescriptionTextField = entryScreenDescriptionTextField;
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
	
	public ExportMtcModelController getController() {
		return controller;
	}

	public void setController(ExportMtcModelController controller) {
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
	
	/**
	 * @return the statusTextArea
	 */
	public LoggedTextArea getStatusTextArea() {
		return statusTextArea;
	}

	/**
	 * @param statusTextArea the statusTextArea to set
	 */
	public void setStatusTextArea(LoggedTextArea statusTextArea) {
		this.statusTextArea = statusTextArea;
	}

	public LoggedLabel getEntryModelVersionLabelText() {
		return entryModelVersionLabelText;
	}

 	public void close(WindowEvent event) {
  	   if(getController().onClose())  {
  		   super.close();
  	   }
  	   else   {
  		   event.consume();
  	   }
 	}


}
