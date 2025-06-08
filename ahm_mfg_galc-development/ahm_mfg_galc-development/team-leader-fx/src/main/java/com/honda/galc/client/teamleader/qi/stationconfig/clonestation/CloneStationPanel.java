package com.honda.galc.client.teamleader.qi.stationconfig.clonestation;

import java.util.List;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigModel;
import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigPanel;
import com.honda.galc.client.teamleader.qi.stationconfig.dto.ComboBoxDisplayDto;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.StyleUtil;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.enumtype.StationConfigurationOperations;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 * 
 * <h3>CloneStationPanel Class description</h3>
 * <p>
 * CloneStationPanel creates the view for cloning a QICS station
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
 * @author vcc44349<br>
 * 
 */
public class CloneStationPanel {
	private CloneStationController controller;
	private EntryStationConfigModel model;
	private LoggedComboBox<String> plantComboBox;
	private LoggedComboBox<ComboBoxDisplayDto> divisionComboBox;
	private LoggedComboBox<ComboBoxDisplayDto> qicsStationComboBox;
	private CheckBox entryDeptChkBox;
	private CheckBox writeUpDeptChkBox;
	private CheckBox entryModelScreenChkBox;
	private CheckBox upcChkBox;
	private CheckBox settingsChkBox;
	private CheckBox prevDefectVisibleChkBox;
	private CheckBox limitedRespChkBox;
	private LoggedButton importBtn;
	private LoggedButton exportBtn;
	private LoggedButton copyBtn;
	private LoggedButton selectAllBtn;
	private LoggedButton resetBtn;
	protected VBox statusVBox;
	protected VBox statusLines;
	private double width;
	private double height;
	private EntryStationConfigPanel parentView;

	public CloneStationPanel(EntryStationConfigModel model,EntryStationConfigPanel view) {
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		width = screenBounds.getWidth();
		height = screenBounds.getHeight();
		controller=new CloneStationController(model,view);
		controller.setMyPanel(this);
		this.model=model;
		parentView = view;
	}
	/**
	 * Method to be called from Parent Panel to initialize Controller
	 */
	public void activateController() {
		controller.setMyPanel(this);
		controller.initListeners();
	}

	/**
	 * This method used to load data on panel
	 */
	public void reload() {
		controller.loadInitialData();
	}

	/**
	 * This method is used to create check boxes panel
	 * @return
	 */
	private Node getCheckBoxPanel() {
		VBox cloneStationContainer = new VBox();
		HBox checkBoxContainer = new HBox();
		VBox checkBoxContainerLeft = new VBox();
		VBox checkBoxContainerRight = new VBox();

		entryDeptChkBox = createCheckBox(QiConstant.CLONESTN_ENTRY_DEPT, getController(),StyleUtil.getBtnStyle(15, "1 5 1 5", "2 2 2 2", "5 5 5 5"));
		entryDeptChkBox.setId(QiConstant.CLONESTN_ENTRY_DEPT);
		entryDeptChkBox.setPadding(new Insets(5, 50, 5, 5));
		entryDeptChkBox.getStyleClass().add("display-label");
		writeUpDeptChkBox = createCheckBox(QiConstant.CLONESTN_WRITEUP_DEPT, getController(),StyleUtil.getBtnStyle(15, "1 5 1 5", "2 2 2 2", "5 5 5 5"));
		writeUpDeptChkBox.setId(QiConstant.CLONESTN_WRITEUP_DEPT);
		writeUpDeptChkBox.setPadding(new Insets(5, 50, 5, 5));
		writeUpDeptChkBox.getStyleClass().add("display-label");
		entryModelScreenChkBox = createCheckBox(QiConstant.CLONESTN_ENTRY_MODEL_SCREEN, getController(),StyleUtil.getBtnStyle(15, "1 5 1 5", "2 2 2 2", "5 5 5 5"));
		entryModelScreenChkBox.setId(QiConstant.CLONESTN_ENTRY_MODEL_SCREEN);
		entryModelScreenChkBox.setPadding(new Insets(5, 50, 5, 5));
		entryModelScreenChkBox.getStyleClass().add("display-label");
		upcChkBox = createCheckBox(QiConstant.CLONESTN_UPC, getController(),StyleUtil.getBtnStyle(15, "1 5 1 5", "2 2 2 2", "5 5 5 5"));
		upcChkBox.setId(QiConstant.CLONESTN_UPC);
		upcChkBox.setPadding(new Insets(5, 50, 5, 5));
		upcChkBox.getStyleClass().add("display-label");
		settingsChkBox = createCheckBox(QiConstant.CLONESTN_SETTINGS, getController(),StyleUtil.getBtnStyle(15, "1 5 1 5", "2 2 2 2", "5 5 5 5"));
		settingsChkBox.setId(QiConstant.CLONESTN_SETTINGS);
		settingsChkBox.setPadding(new Insets(5, 0, 5, 5));
		settingsChkBox.getStyleClass().add("display-label");
		prevDefectVisibleChkBox = createCheckBox(QiConstant.CLONESTN_PREV_DEFECT_VISIBLE, getController(),StyleUtil.getBtnStyle(15, "1 5 1 5", "2 2 2 2", "5 5 5 5"));
		prevDefectVisibleChkBox.setId(QiConstant.CLONESTN_PREV_DEFECT_VISIBLE);
		prevDefectVisibleChkBox.setPadding(new Insets(5, 0, 5, 5));
		prevDefectVisibleChkBox.getStyleClass().add("display-label");
		limitedRespChkBox = createCheckBox(QiConstant.CLONESTN_LIMITED_RESP, getController(),StyleUtil.getBtnStyle(15, "1 5 1 5", "2 2 2 2", "5 5 5 5"));
		limitedRespChkBox.setId(QiConstant.CLONESTN_LIMITED_RESP);
		limitedRespChkBox.setPadding(new Insets(5, 0, 5, 5));
		limitedRespChkBox.getStyleClass().add("display-label");
		checkBoxContainerLeft.setPrefWidth(width*0.25);
		checkBoxContainerRight.setPrefWidth(width*0.25);
		checkBoxContainerLeft.getChildren().addAll(entryDeptChkBox, writeUpDeptChkBox, entryModelScreenChkBox, upcChkBox);
		checkBoxContainerRight.getChildren().addAll(settingsChkBox, prevDefectVisibleChkBox, limitedRespChkBox);
		checkBoxContainer.getChildren().addAll(checkBoxContainerLeft, checkBoxContainerRight);
		cloneStationContainer.getChildren().addAll(checkBoxContainer);
		cloneStationContainer.getChildren().addAll(createButtons());
		cloneStationContainer.setPadding(new Insets(10, 0, 0, 182));
		return cloneStationContainer;
	}

	/**
	 * This method is used to create QicsRelatedModelAndScreen Panel
	 * @return
	 */
	public MigPane getCloneStationScreenPanel(){
		MigPane outerPane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");
		HBox destinationHBox = new HBox();
		VBox contentVBox = new VBox();
		VBox allVBox = new VBox();
		
		plantComboBox=new LoggedComboBox<String>();
		String thisSite = getModel().findSiteName();
		
		List<Plant> plants = getModel().findAllBySiteName(thisSite);
		for(Plant thisPlant : plants)  {
			plantComboBox.getItems().add(thisPlant.getPlantName());
		}
		divisionComboBox=new LoggedComboBox<ComboBoxDisplayDto>(); 
		qicsStationComboBox=new LoggedComboBox<ComboBoxDisplayDto>(); 
		
		LoggedLabel copyToLabel = UiFactory.createLabel("filterLabel", "Copy Destination", (int)(0.01*width));
		copyToLabel.getStyleClass().add("display-label-blue");
		Insets insets = new Insets(5, 5, 0, 20);
		HBox siteContainer = parentView.getEntryStationPanel().createSiteContainer();
		destinationHBox.getChildren().add(siteContainer);
		destinationHBox.getChildren().add(getGenericComboBox("Plant",15,insets,plantComboBox));
		destinationHBox.getChildren().add(getGenericComboBox("Division",15,insets,divisionComboBox));
		destinationHBox.getChildren().add(getGenericComboBox("QICS Station",15,insets,qicsStationComboBox));
		destinationHBox.setPadding(new Insets(0, 0, 0, 30));
		destinationHBox.setSpacing(40);
		contentVBox.getChildren().add(copyToLabel);
		contentVBox.getChildren().add(destinationHBox);
		contentVBox.getChildren().add(getCheckBoxPanel());
		
		//create status panel
		statusVBox = new VBox();
		statusLines = new VBox();
		LoggedLabel statusLabel = UiFactory.createLabel("status label", "Copy status", (int)(0.01*width));
		statusLabel.getStyleClass().add("display-label");
		statusVBox.getChildren().addAll(statusLabel, statusLines);
		statusVBox.setPadding(new Insets(30,0,0,0));
		allVBox.getChildren().addAll(contentVBox,statusVBox);
		outerPane.add(EntryStationConfigPanel.createTitledPane("Clone Station",allVBox,width,height),"span,wrap");
		disableInButtons(true);
		
		return outerPane;
	}

	public void addStatusLine(String val, boolean isError)  {
		
		int size = statusLines.getChildren().size();
		LoggedLabel statusContent = UiFactory.createLabel("status_content" + size);
		if(isError)  {
			statusContent.getStyleClass().add("display-label-warn");
		}
		else  {
			statusContent.getStyleClass().add("display-label");			
		}
		statusContent.setText(val);
		statusLines.getChildren().add(statusContent);
	}

	public void clearStatus()  {
		if(statusLines != null)  {
			statusLines.getChildren().clear();
		}
	}
		
	/**
	 * This method is used to create 'Copy' and 'Reset' , 'Import' and 'Export' buttons
	 * @return
	 */
	@SuppressWarnings("static-access")
	private HBox createButtons() {
		HBox allButtons = new HBox();
		HBox copyRstHBox = new HBox();
		HBox impExpHBox = new HBox();
		selectAllBtn = createBtn("Check All", getController());
		selectAllBtn.setId(StationConfigurationOperations.COPYSTN_SELECT_ALL.getName());
		selectAllBtn.setDisable(false);
		selectAllBtn.setPadding(new Insets(0, 10, 0, 0));
		resetBtn = createBtn("Clear",getController());
		resetBtn.setId(StationConfigurationOperations.COPYSTN_RESET.getName());
		resetBtn.setDisable(false);
		copyRstHBox.getChildren().addAll(selectAllBtn,resetBtn);
		copyRstHBox.setPadding(new Insets(0, 50, 0, 0));
		copyBtn = createBtn("Copy", getController());
		copyBtn.setId(StationConfigurationOperations.COPYSTN.getName());
		copyBtn.setDisable(true);
		copyBtn.setPadding(new Insets(0, 25, 0, 0));
		importBtn = createBtn("Import", getController());
		importBtn.setId(StationConfigurationOperations.COPYSTN_IMPORT.getName());
		importBtn.setDisable(true);
		importBtn.setPadding(new Insets(0, 10, 0, 0));
		exportBtn = createBtn("Export",getController());
		exportBtn.setId(StationConfigurationOperations.COPYSTN_EXPORT.getName());
		exportBtn.setDisable(true);
		impExpHBox.getChildren().addAll(copyBtn,importBtn, exportBtn);
		impExpHBox.setPadding(new Insets(0, 0, 0, 50));  //top, right, bottom, left...i.e., clockwise from top
		allButtons.getChildren().addAll(copyRstHBox, impExpHBox);
		allButtons.setPadding(new Insets(35, 0, 0, 5));
		return allButtons;
	}
	/**
	 * This method is used to create Button.
	 * @param text
	 * @param handler
	 * @return
	 */
	public static LoggedButton createBtn(String text,EventHandler<ActionEvent> handler)
	{
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.defaultButtonProperty().bind(btn.focusedProperty());
		btn.setOnAction(handler);
		btn.getStyleClass().add("main-screen-btn");
		btn.setStyle("-fx-font-size: 14px;");
		return btn;
	}

	/**
	 * This method is used to create Drop down for Plant , Division and QICS station
	 * @param name
	 * @param css
	 * @param spacing
	 * @param padding
	 * @param alignment
	 * @param LoggedComboBox
	 * @return
	 */
	private <T> HBox getGenericComboBox(String name,int spacing,Insets padding,LoggedComboBox<T> loggedComboBox) {
		HBox container = new HBox();
		LoggedLabel productTypeLabel = UiFactory.createLabel(name, name, (int)(0.01 * width));
		productTypeLabel.getStyleClass().add("display-label-14");
		container.getChildren().addAll(productTypeLabel, loggedComboBox);
		container.setSpacing(spacing);
		container.setPadding(padding);
		container.setAlignment(Pos.CENTER_RIGHT);
		return container;
	}

	public CloneStationController getController() {
		return controller;
	}
	/**
	 * This method is used to create checkbox
	 * @param text
	 * @param handler
	 * @param style
	 * @return
	 */
	public CheckBox createCheckBox(String text,EventHandler<ActionEvent> handler,String style)
	{
		CheckBox checkBox = new CheckBox(text);
		checkBox.setOnAction(handler);
		checkBox.setWrapText(true);
		checkBox.setPadding(new Insets(10));
		checkBox.getStyleClass().add(style);
		return checkBox;
	}
	

	public void reset()  {
		toggleAllCheckBoxes(false);
		statusLines.getChildren().clear();
	}

	public void selectAll()  {
		toggleAllCheckBoxes(true);
	}


	/**
	 * This method is used to set buttons .
	 */	
	public void toggleAllCheckBoxes(boolean bCheck) {
		getUpcChkBox().setSelected(bCheck); //disable
		getEntryDeptChkBox().setSelected(bCheck); //disable
		getEntryModelScreenChkBox().setSelected(bCheck); //disable
		getLimitedRespChkBox().setSelected(bCheck); //disable
		getPrevDefectVisibleChkBox().setSelected(bCheck); //disable
		getWriteUpDeptChkBox().setSelected(bCheck); //disable
		getSettingsChkBox().setSelected(bCheck); //disable
	}


	public void disableExportButton(boolean disabled){
		getExportBtn().setDisable(disabled);
	}

	public void disableImportButton(boolean disabled){
		getImportBtn().setDisable(disabled);
	}

	public void disableCopyButton(boolean disabled){
		getCopyBtn().setDisable(disabled);
	}

	public void disableResetButton(boolean disabled){
		getCopyBtn().setDisable(disabled);
	}

	public void disableInButtons(boolean disabled){
		getCopyBtn().setDisable(disabled);
		getImportBtn().setDisable(disabled);
	}

	public EntryStationConfigModel getModel() {
		return model;
	}
	public LoggedComboBox<String> getPlantComboBox() {
		return plantComboBox;
	}
	public LoggedComboBox<ComboBoxDisplayDto> getDivisionComboBox() {
		return divisionComboBox;
	}
	public String getDivisionComboBoxSelectedId() {
		return ComboBoxDisplayDto.getComboBoxSelectedId(getDivisionComboBox());
	}

	public void clearDivisionComboBox() {
		getDivisionComboBox().getItems().clear();
	}

	public LoggedComboBox<ComboBoxDisplayDto> getQicsStationComboBox() {
		return qicsStationComboBox;
	}
	public String getQicsStationComboBoxSelectedId() {
		return ComboBoxDisplayDto.getComboBoxSelectedId(getQicsStationComboBox());
	}

	public void clearQicsStationComboBox() {
		getQicsStationComboBox().getItems().clear();
	}

	public LoggedButton getImportBtn() {
		return importBtn;
	}
	public LoggedButton getExportBtn() {
		return exportBtn;
	}
	public LoggedButton getCopyBtn() {
		return copyBtn;
	}
	public LoggedButton getResetBtn() {
		return resetBtn;
	}
	public CheckBox getEntryDeptChkBox() {
		return entryDeptChkBox;
	}
	public CheckBox getWriteUpDeptChkBox() {
		return writeUpDeptChkBox;
	}
	public CheckBox getEntryModelScreenChkBox() {
		return entryModelScreenChkBox;
	}
	public CheckBox getPrevDefectVisibleChkBox() {
		return prevDefectVisibleChkBox;
	}
	public CheckBox getLimitedRespChkBox() {
		return limitedRespChkBox;
	}
	public CheckBox getSettingsChkBox() {
		return settingsChkBox;
	}
	public CheckBox getUpcChkBox() {
		return upcChkBox;
	}
}
