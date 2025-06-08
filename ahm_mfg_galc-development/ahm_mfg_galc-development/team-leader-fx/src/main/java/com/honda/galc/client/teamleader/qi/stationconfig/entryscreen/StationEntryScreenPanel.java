package com.honda.galc.client.teamleader.qi.stationconfig.entryscreen;

import java.util.Comparator;
import java.util.List;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigModel;
import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigPanel;
import com.honda.galc.client.teamleader.qi.stationconfig.department.StationDepartmentPanel;
import com.honda.galc.client.teamleader.qi.stationconfig.dto.ApplicationDetailDto;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.StyleUtil;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.QiEntryDepartmentDto;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.dto.qi.QiTerminalDetailDto;
import com.honda.galc.entity.enumtype.StationConfigurationOperations;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * 
 * <h3>EntryStationDefaultStatusPanel Class description</h3>
 * <p>
 * EntryStationDefaultStatusPanel is used to create chechboxes , radio buttons and update button etc.
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
/**   
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
public class StationEntryScreenPanel {
	private StationEntryScreenController controller;
	private EntryStationConfigModel model;
	private LoggedComboBox<String> productTypeComboBox;
	private LoggedComboBox<String> modelComboBox;
	private LoggedComboBox<String> departmentComboBox;
	private ObjectTablePane<QiEntryScreenDto> entryScreenObjectTablePane;
	private ObjectTablePane<QiEntryScreenDto> entryScreenModelObjectTablePane;
	private ObjectTablePane<QiTerminalDetailDto> terminalDetailObjectTablePane;
	private CheckBox repairedChkBox;
	private CheckBox notRepairedChkBox;
	private CheckBox nonRepairableChkBox;
	private LoggedRadioButton noneRadioBtn;
	private LoggedRadioButton repairedRadioBtn;
	private LoggedRadioButton notRepairedRadioBtn;
	private LoggedButton updateDefaultStatusBtn;
	private LoggedButton updateEntryScreenBtn;
	private LoggedButton resetEntryScreenBtn;
	private LoggedButton rightShiftEntryScreenBtn;
	private LoggedButton leftShiftEntryScreenBtn;
	private LoggedRadioButton nonRepairableRadioBtn;
	private LoggedLabel ppId;
	private LoggedLabel ppName;
	private LoggedLabel ppDesc;
	private LoggedLabel windowTitle;

	private double width;
	private double height;

	public StationEntryScreenPanel(EntryStationConfigModel model,EntryStationConfigPanel view) {
		Rectangle2D screenBounds = view.getCurrentScreenBounds();
		width = screenBounds.getWidth();
		height = screenBounds.getHeight();
		controller=new StationEntryScreenController(model,view);
		this.model=model;
	}
	/**
	 * Method to be called from Parent Panel to initialize Controller
	 */
	public void activateController() {
		controller.initListeners();
	}

	/**
	 * This method used to load data on panel
	 */
	public void reload() {
		controller.loadInitialData();
	}
	/**
	 * This method is used to create Status panel for the station
	 * @return
	 */
	public MigPane getEntryStationDefaultStatusPanel() {
		VBox statusPanel = new VBox();
		MigPane pane = new MigPane("insets 2 2 0 2", "[center,grow,fill]", "");
		statusPanel.getChildren().add(getDefaultStatusCheckBoxPanel());
		statusPanel.getChildren().add(getDefaultStatusRadioButtonPanel());
		pane.add(EntryStationConfigPanel.createTitledPane("Default Status", statusPanel,width, height*0.10), "span,wrap");
		return pane;
	}

	/**
	 * This method is used to create Status panel for the station
	 * @return
	 */
	public VBox getProcessPointDetailPanel() {
		VBox ppDetailPanel = new VBox();
		VBox pane = new VBox();
		ppDetailPanel.getChildren().add(createProcessPointDetailsTable());
		pane.getChildren().add(EntryStationConfigPanel.createTitledPane("Process Point", ppDetailPanel,width, height*0.25));
		return pane;
	}
	/**
	 * This method is used to create check boxes panel
	 * @return
	 */
	private Node getDefaultStatusCheckBoxPanel() {
		HBox statusCheckBox = new HBox();
		HBox checkBoxContainer = new HBox();

		repairedChkBox = createCheckBox(QiConstant.REPAIRED, getController(),StyleUtil.getBtnStyle(8, "1 5 1 5", "2 2 2 2", "5 5 5 5"));
		repairedChkBox.setId(QiConstant.REPAIRED);
		repairedChkBox.setPadding(new Insets(0, 40, 0, 0));
		repairedChkBox.getStyleClass().add("display-label");
		notRepairedChkBox = createCheckBox(QiConstant.NOT_REPAIRED, getController(),StyleUtil.getBtnStyle(8, "1 5 1 5", "2 2 2 2", "5 5 5 5"));
		notRepairedChkBox.setId(QiConstant.NOT_REPAIRED);
		notRepairedChkBox.setPadding(new Insets(0, 40, 0, 0));
		notRepairedChkBox.getStyleClass().add("display-label");
		nonRepairableChkBox = createCheckBox(QiConstant.NON_REPAIRABLE, getController(),StyleUtil.getBtnStyle(8, "1 5 1 5", "2 2 2 2", "5 5 5 5"));
		nonRepairableChkBox.setId(QiConstant.NON_REPAIRABLE);
		nonRepairableChkBox.setPadding(new Insets(0, 0, 0, 0));
		nonRepairableChkBox.getStyleClass().add("display-label");
		checkBoxContainer.getChildren().addAll(repairedChkBox, notRepairedChkBox, nonRepairableChkBox);
		checkBoxContainer.setPadding(new Insets(0, 20, 0, 0));
		statusCheckBox.getChildren().addAll(checkBoxContainer,updateDefaultStatusButton());
		statusCheckBox.setPadding(new Insets(0, 0, 0, 160));
		return statusCheckBox;
	}
	/**
	 * This method is used to create radio button panel
	 * @return
	 */
	private Node getDefaultStatusRadioButtonPanel() {
		HBox radioBtnContainer = new HBox();
		ToggleGroup group = new ToggleGroup();
		LoggedLabel defaultStatusLabel = UiFactory.createLabel("default", "Default : ", 13);
		defaultStatusLabel.getStyleClass().add("display-label-14");
		noneRadioBtn = createRadioButton(QiConstant.NONE, group, true, getController());
		noneRadioBtn.getStyleClass().add("display-label");
		repairedRadioBtn = createRadioButton("", group, false, getController());
		repairedRadioBtn.setId(QiConstant.REPAIRED);
		repairedRadioBtn.setPadding(new Insets(0, 0, 0, 35));
		notRepairedRadioBtn = createRadioButton("", group, false, getController());
		notRepairedRadioBtn.setId(QiConstant.NOT_REPAIRED);
		notRepairedRadioBtn.setPadding(new Insets(0, 0, 0, 110));
		nonRepairableRadioBtn = createRadioButton("",group,false,getController());
		nonRepairableRadioBtn.setId(QiConstant.NON_REPAIRABLE);
		nonRepairableRadioBtn.setPadding(new Insets(0, 0, 0, 145));			
		radioBtnContainer.setPadding(new Insets(0, 0, 0, 10));
		radioBtnContainer.getChildren().addAll(defaultStatusLabel, noneRadioBtn, repairedRadioBtn, notRepairedRadioBtn, nonRepairableRadioBtn);
		return radioBtnContainer;
	}
	/**
	 * This method is used to update the status regarding QICS station
	 * @return
	 */
	private HBox updateDefaultStatusButton() {
		HBox button = new HBox();
		updateDefaultStatusBtn = StationDepartmentPanel.createBtn(QiConstant.UPDATE, getController());
		updateDefaultStatusBtn.setId(StationConfigurationOperations.UPDATE_ENTRY_STATION_STATUS.getName());
		updateDefaultStatusBtn.setDisable(true);
		button.getChildren().add(updateDefaultStatusBtn);
		button.setAlignment(Pos.CENTER_LEFT);
		return button;
	}

	/**
	 * This method is used to create QicsRelatedModelAndScreen Panel
	 * @return
	 */
	public MigPane getQicsRelatedModelAndScreenPanel(){
		MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");
		MigPane outerPane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");
		productTypeComboBox=new LoggedComboBox<String>();
		List<String> productList=getModel().findAllProductTypes();
		productTypeComboBox.getItems().addAll(productList);	
		productTypeComboBox.setId("qicsRelMdlScrProdTypeCmbBox");
		modelComboBox=new LoggedComboBox<String>();
		modelComboBox.setId("qicsRelMdlScrEntMdlCmbBox");
		departmentComboBox=new LoggedComboBox<String>();
		departmentComboBox.setId("qicsRelMdlScrEntDeptCmbBox");
		Insets insets = new Insets(5,0,0,0);
		pane.add(getGenericComboBox("Product Type",0,insets,productTypeComboBox),"split 4");
		pane.add(getGenericComboBox("Entry Model",0,insets,modelComboBox));
		pane.add(getGenericComboBox("Entry Dept",0,insets,departmentComboBox));
		pane.add(createUpdateAndResetButton(),"wrap");
		pane.add(createEntryScreenTable());
		outerPane.add(EntryStationConfigPanel.createTitledPane("QICS Related Model and Screen",pane,width,220),"span,wrap");
		return outerPane;
	}
	/**
	 * This method is used to create Entry Station and Entry Model table view
	 * @return
	 */
	private HBox createEntryScreenTable(){
		VBox shiftButton = new VBox();
		rightShiftEntryScreenBtn=createBtn(">",getController());
		rightShiftEntryScreenBtn.setId(StationConfigurationOperations.RIGHT_SHIFT_ENTRY_SCREEN.getName());
		leftShiftEntryScreenBtn=createBtn("<",getController());
		leftShiftEntryScreenBtn.setId(StationConfigurationOperations.LEFT_SHIFT_ENTRY_SCREEN.getName());
		shiftButton.getChildren().addAll(rightShiftEntryScreenBtn,leftShiftEntryScreenBtn);
		shiftButton.setPadding(new Insets(35, 0, 0, 0));
		HBox buttonTableContainer= new HBox();
		entryScreenObjectTablePane = creatScreenTablePane();
		entryScreenObjectTablePane.setPrefWidth(width*0.35);
		entryScreenObjectTablePane.setSelectionMode(SelectionMode.MULTIPLE);
		entryScreenObjectTablePane.setId("qicsRelMdlScrEntScnTblPane");
		entryScreenModelObjectTablePane=creatScreenModelTablePane();
		entryScreenModelObjectTablePane.setPrefWidth(width*0.40);
		entryScreenModelObjectTablePane.setSelectionMode(SelectionMode.MULTIPLE);
		entryScreenModelObjectTablePane.setId("qicsRelMdlScrEntMdlTblPane");
		buttonTableContainer.getChildren().addAll(entryScreenObjectTablePane,shiftButton,entryScreenModelObjectTablePane);
		buttonTableContainer.setSpacing(25);
		return buttonTableContainer;
	}

	/**
	 * This method is used to create Entry Station and Entry Model table view
	 * @return
	 */
	private HBox createProcessPointDetailsTable(){
		HBox vProcessPointContainer= new HBox();
		HBox vProcessPointDetails = new HBox();
		
		VBox ppLabels = new VBox();
		VBox ppValues = new VBox();
		LoggedLabel ppIdLbl = UiFactory.createLabel("ppIdLbl","Process Poind Id: ", 13);
		ppId = UiFactory.createLabel("ppId","", 13);
		ppId.getStyleClass().add("display-label-blue");
		LoggedLabel ppNameLbl = UiFactory.createLabel("ppNameLbl","Name: ", 13);
		ppName = UiFactory.createLabel("ppName","", 13);
		ppName.getStyleClass().add("display-label-blue");
		LoggedLabel ppDescLbl = UiFactory.createLabel("ppDescLbl","Description: ", 13);
		ppDesc = UiFactory.createLabel("ppDesc","", 13);
		ppDesc.getStyleClass().add("display-label-blue");
		LoggedLabel windowTitleLbl = UiFactory.createLabel("windowTitleLbl","Window Title: ", 13);
		windowTitle = UiFactory.createLabel("windowTitle","", 13);
		windowTitle.getStyleClass().add("display-label-blue");
		ppLabels.getChildren().addAll(ppIdLbl,ppNameLbl,ppDescLbl,windowTitleLbl);
		ppValues.getChildren().addAll(ppId,ppName,ppDesc,windowTitle);
		
		vProcessPointDetails.getChildren().addAll(ppLabels,ppValues);
		terminalDetailObjectTablePane=createTerminalDetailTablePane();
		terminalDetailObjectTablePane.setPrefWidth(width*0.40);
		terminalDetailObjectTablePane.setId("processPointDetailPane");
		vProcessPointContainer.getChildren().addAll(vProcessPointDetails,terminalDetailObjectTablePane);
		vProcessPointContainer.setSpacing(25);
		vProcessPointContainer.setPadding(new Insets(5, 0, 0, 0));
		return vProcessPointContainer;
	}
	
	public void clearProcessPointDetails()  {
		terminalDetailObjectTablePane.getTable().getItems().clear();
		ppId.setText("");
		ppName.setText("");
		ppDesc.setText("");
		windowTitle.setText("");
	}
	
	public void setProcessPointDetails(ApplicationDetailDto appDto)  {
		ppId.setText(appDto.getId());
		ppName.setText(appDto.getName());
		ppDesc.setText(appDto.getDescription());
		windowTitle.setText(appDto.getWindowTitle());
	}
	
	public void reloadTerminalDetails(List<QiTerminalDetailDto> terminalDtoList)  {
		terminalDetailObjectTablePane.getTable().getItems().clear();
		terminalDetailObjectTablePane.getTable().getItems().addAll(terminalDtoList);
		
	}
	
	/**
	 * This method is used to create a Table Pane.
	 * @return
	 */
	public ObjectTablePane<QiEntryScreenDto> creatScreenTablePane(){ 
		ColumnMappingList columnMappingList = ColumnMappingList.with("Screen", "entryScreen")
				.put("Image", "imageName");
		Double[] columnWidth = new Double[] {
				0.2,0.17
		};
		ObjectTablePane<QiEntryScreenDto> panel = new ObjectTablePane<QiEntryScreenDto>(columnMappingList,columnWidth);
		return panel;
	}
	/**
	 * This method is used to create Entry ScreenModel tableview
	 * @return
	 */
	private ObjectTablePane<QiEntryScreenDto> creatScreenModelTablePane(){ 
		ColumnMappingList columnMappingList = ColumnMappingList.with("Seq", "seq").put("Entry Model", "entryModel")
				.put("Screen", "entryScreen").put("Image", "imageName").put("Angle", "orientationAngleAsString").put("Scan", "allowScanAsString");
		Double[] columnWidth = new Double[] {
				0.02,0.08,0.11,0.1,0.04,0.04
		};
		ObjectTablePane<QiEntryScreenDto> panel = new ObjectTablePane<QiEntryScreenDto>(columnMappingList,columnWidth);

		sortColumn((TableColumn<QiEntryScreenDto, Object>) panel.getTable().getColumns().get(0));
		panel.setConstrainedResize(false);
		return panel;
	}

	/**
	 * This method is used to create Terminal information tableview
	 * @return
	 */
	private ObjectTablePane<QiTerminalDetailDto> createTerminalDetailTablePane(){ 
		ColumnMappingList columnMappingList = ColumnMappingList.with("Terminal", "hostName").put("Description", "terminalDescription")
				.put("Pole Location", "columnLocation").put("Extn", "phoneExtension");
		Double[] columnWidth = new Double[] {
				0.1,0.1,0.1,0.1
		};
		ObjectTablePane<QiTerminalDetailDto> panel = new ObjectTablePane<QiTerminalDetailDto>(columnMappingList,columnWidth);

		sortColumnByTerminal((TableColumn<QiTerminalDetailDto, Object>) panel.getTable().getColumns().get(0));
		panel.setConstrainedResize(false);
		return panel;
	}

	private void sortColumn(TableColumn<QiEntryScreenDto,Object> tableColumn){
		tableColumn.setComparator(new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				return	Integer.parseInt(o1.toString()) - Integer.parseInt(o2.toString());
			}
		});
	}

	private void sortColumnByTerminal(TableColumn<QiTerminalDetailDto,Object> tableColumn){
		tableColumn.setComparator(new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				return	o1.toString().compareToIgnoreCase(o2.toString());
			}
		});
	}

	/**
	 * This method is used to 'Update' and 'Reset' button and passing the controller to perform the action
	 * @return
	 */
	@SuppressWarnings("static-access")
	private HBox createUpdateAndResetButton(){
		HBox updateResetButton = new HBox();
		updateEntryScreenBtn = createBtn("Update", getController());
		updateEntryScreenBtn.setId(StationConfigurationOperations.UPDATE_ENTRY_SCREEN.getName());
		updateEntryScreenBtn.setDisable(true);
		updateEntryScreenBtn.setPadding(new Insets(5, 30, 0, 0));
		resetEntryScreenBtn = createBtn("Reset",getController());
		resetEntryScreenBtn.setId(StationConfigurationOperations.RESET_ENTRY_SCREEN.getName());
		resetEntryScreenBtn.setDisable(true);
		updateResetButton.getChildren().addAll(updateEntryScreenBtn,resetEntryScreenBtn);
		return updateResetButton;
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
	private HBox getGenericComboBox(String name,int spacing,Insets padding,LoggedComboBox<String> loggedComboBox) {
		HBox container = new HBox();
		LoggedLabel productTypeLabel = UiFactory.createLabel(name, name,13);
		productTypeLabel.getStyleClass().add("display-label");
		container.getChildren().addAll(productTypeLabel, loggedComboBox);
		container.setSpacing(spacing);
		container.setPadding(padding);
		container.setAlignment(Pos.CENTER_LEFT);
		return container;
	}

	public StationEntryScreenController getController() {
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
		checkBox.setDisable(true);
		return checkBox;
	}
	/**
	 * This method is used to create Radio Button.
	 * @param title
	 * @param group
	 * @param isSelected
	 * @return
	 */
	public LoggedRadioButton createRadioButton(String title, ToggleGroup group, boolean isSelected, EventHandler<ActionEvent> handler) {
		LoggedRadioButton radioButton = new LoggedRadioButton(title);
		radioButton.getStyleClass().add("radio-btn");
		radioButton.setToggleGroup(group);
		radioButton.setSelected(isSelected);
		radioButton.setOnAction(handler);
		radioButton.setDisable(true);
		return radioButton;
	}

	public LoggedButton getUpdateDefaultStatusBtn() {
		return updateDefaultStatusBtn;
	}

	public CheckBox getRepairedChkBox() {
		return repairedChkBox;
	}

	public void setRepairedChkBox(CheckBox repairedChkBox) {
		this.repairedChkBox = repairedChkBox;
	}

	public CheckBox getNotRepairedChkBox() {
		return notRepairedChkBox;
	}

	public void setNotRepairedChkBox(CheckBox notRepairedChkBox) {
		this.notRepairedChkBox = notRepairedChkBox;
	}

	public CheckBox getNonRepairableChkBox() {
		return nonRepairableChkBox;
	}

	public void setNonRepairableChkBox(CheckBox nonRepairableChkBox) {
		this.nonRepairableChkBox = nonRepairableChkBox;
	}

	public LoggedRadioButton getNoneRadioBtn() {
		return noneRadioBtn;
	}

	public void setNoneRadioBtn(LoggedRadioButton noneRadioBtn) {
		this.noneRadioBtn = noneRadioBtn;
	}

	public LoggedRadioButton getRepairedRadioBtn() {
		return repairedRadioBtn;
	}

	public void setRepairedRadioBtn(LoggedRadioButton repairedRadioBtn) {
		this.repairedRadioBtn = repairedRadioBtn;
	}

	public LoggedRadioButton getNotRepairedRadioBtn() {
		return notRepairedRadioBtn;
	}

	public void setNotRepairedRadioBtn(LoggedRadioButton notRepairedRadioBtn) {
		this.notRepairedRadioBtn = notRepairedRadioBtn;
	}
	public LoggedRadioButton getNonRepairableRadioBtn() {
		return nonRepairableRadioBtn;
	}
	public void setNonRepairableRadioBtn(LoggedRadioButton nonRepairableRadioBtn) {
		this.nonRepairableRadioBtn = nonRepairableRadioBtn;
	}
	public void setUpdateDefaultStatusBtn(LoggedButton updateDefaultStatusBtn) {
		this.updateDefaultStatusBtn = updateDefaultStatusBtn;
	}
	public EntryStationConfigModel getModel() {
		return model;
	}
	public ObjectTablePane<QiEntryScreenDto> getEntryScreenObjectTablePane() {
		return entryScreenObjectTablePane;
	}
	public ObjectTablePane<QiEntryScreenDto> getEntryScreenModelObjectTablePane() {
		return entryScreenModelObjectTablePane;
	}
	public LoggedButton getUpdateEntryScreenBtn() {
		return updateEntryScreenBtn;
	}
	public LoggedButton getResetEntryScreenBtn() {
		return resetEntryScreenBtn;
	}
	public LoggedButton getRightShiftEntryScreenBtn() {
		return rightShiftEntryScreenBtn;
	}
	public LoggedButton getLeftShiftEntryScreenBtn() {
		return leftShiftEntryScreenBtn;
	}
	public LoggedComboBox<String> getProductTypeComboBox() {
		return productTypeComboBox;
	}
	public LoggedComboBox<String> getModelComboBox() {
		return modelComboBox;
	}
	public LoggedComboBox<String> getDepartmentComboBox() {
		return departmentComboBox;
	}
	public String getDepartmentComboBoxSelectedId() {
		QiEntryDepartmentDto dto = (QiEntryDepartmentDto) getDepartmentComboBox().getValue();
		String deptId = "";
		if(dto != null)  {
			deptId = dto.getDivisionId();
		}
		return deptId;
	}

	public void clearDepartmentComboBox() {
		getDepartmentComboBox().getItems().clear();
	}

}
