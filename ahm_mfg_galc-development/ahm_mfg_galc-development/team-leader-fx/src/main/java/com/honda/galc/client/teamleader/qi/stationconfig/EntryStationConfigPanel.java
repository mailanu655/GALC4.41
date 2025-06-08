package com.honda.galc.client.teamleader.qi.stationconfig;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.stationconfig.clonestation.CloneStationPanel;
import com.honda.galc.client.teamleader.qi.stationconfig.department.StationDepartmentPanel;
import com.honda.galc.client.teamleader.qi.stationconfig.department.WriteUpDeptController;
import com.honda.galc.client.teamleader.qi.stationconfig.entryscreen.StationEntryScreenPanel;
import com.honda.galc.client.teamleader.qi.stationconfig.limitresponsibility.LimitResponsibilityPanel;
import com.honda.galc.client.teamleader.qi.stationconfig.previousdefect.PreviousDefectVisibleMaintPanel;
import com.honda.galc.client.teamleader.qi.view.QiAbstractTabbedView;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.QiEntryStationSettingsDto;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.enumtype.StationConfigurationOperations;
import com.honda.galc.entity.qi.QiBomQicsPartMapping;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
/**
 * 
 * <h3>EntryStationConfigPanel Class description</h3>
 * <p>
 * EntryStationConfigPanel is used to load data in TableViews and perform the action on Update , Reset and Shift buttonsetc.
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
public class EntryStationConfigPanel extends QiAbstractTabbedView<EntryStationConfigModel,EntryStationConfigController>{
	
	private double width;
	private double height;
	private ObjectTablePane<QiEntryStationSettingsDto> entryStationConfigSettingsTablePane;
	private ObjectTablePane<QiBomQicsPartMapping> avilableUPCTablePane;
	private ObjectTablePane<QiBomQicsPartMapping> assignedUPCTablePane;
	private LoggedButton rightShiftUPCPartBtn;
	private LoggedButton leftShiftUPCPartBtn;
	private LoggedButton updateUPCPartBtn;
	private LoggedButton resetUPCPartBtn;
	private TabPane tabPane;
	private Tab entryScreenTab;
	private Tab settingTab;
	private Tab upcTab;
	private Tab previousDefectVisible;
	private Tab limitResponsibilityTab;
	private Tab cloneStationTab;
	private LoggedComboBox<String> updateSettingValueComboBox;
	private LoggedButton updateSettingsPropertyBtn;
	private LoggedButton saveAllSettingBtn;
	protected LabeledUpperCaseTextField upcPartFilterTextfield ;
	private EntryStationPanel entryStationPanel;
	private StationDepartmentPanel entryDeptAndWriteUpDeptPanel; 
	private StationEntryScreenPanel entryStationDefaultStatusPanel;
	private PreviousDefectVisibleMaintPanel entryDepartmentDefectPanel;
	private LimitResponsibilityPanel limitResponsibilityPanel;
	private CloneStationPanel cloneStationPanel;
	private LoggedTextField updateStettingValueTextField;
	private HBox entryStationConfigSettingsBox ;
	private LoggedComboBox<String> stationRepairAreaComboBox;
	private LoggedButton saveStationRepairAreaBtn;

	public EntryStationConfigPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
	}
	/**
	 * This method creates layout of the EntryStationConfigurationManagement screen
	 */
	@Override
	public void initView() {
		Rectangle2D screenBounds = getCurrentScreenBounds();
		width = screenBounds.getWidth();
		height = screenBounds.getHeight();
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		VBox container= new VBox();
		entryStationConfigSettingsBox = new HBox();
		entryStationPanel = EntryStationPanel.getInstance(getModel());
		entryDeptAndWriteUpDeptPanel = new StationDepartmentPanel(getModel(),new WriteUpDeptController(),this);
		entryDepartmentDefectPanel= new PreviousDefectVisibleMaintPanel(getModel(),this);
		entryStationDefaultStatusPanel =new StationEntryScreenPanel(getModel(),this);
		cloneStationPanel = new CloneStationPanel(getModel(), this);
		container.getChildren().addAll(entryStationPanel.getEntryStationPanel(),createTabbedPane());
		findPropertyValueText();
		this.setCenter(container);
		enabledAndDisabledButtons(true);
	}

	public void findPropertyValueText() {
		updateStettingValueTextField = new LoggedTextField();
		updateStettingValueTextField =  UiFactory.createTextField("maxQty", 12, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.TOP_LEFT, true);
		updateStettingValueTextField.getStyleClass().add("combo-box");
		updateStettingValueTextField.setVisible(false);
		entryStationConfigSettingsBox.getChildren().add(1, updateStettingValueTextField);
	}

	/**
	 * This method is used to create static Tabs
	 * @return
	 */
	private TabPane createTabbedPane(){
		HBox entryWriteUpDepartment = new HBox();
		VBox tabContainer= new VBox();
		ScrollPane entryScreenScroll = new ScrollPane();
		VBox settingsContainer= new VBox();
		VBox upcContainer= new VBox();
		tabPane = new TabPane();
		entryScreenTab = new Tab();
		settingTab = new Tab();
		upcTab = new Tab();
		previousDefectVisible= new Tab();
		limitResponsibilityTab = new Tab();
		cloneStationTab = new Tab();
		entryWriteUpDepartment.getChildren().addAll(entryDeptAndWriteUpDeptPanel.getEntryDepartmentPanel(),entryDeptAndWriteUpDeptPanel.getWriteUpDepartmentPanel());
		entryDeptAndWriteUpDeptPanel.activateController();
		tabContainer.getChildren().addAll(entryWriteUpDepartment,entryStationDefaultStatusPanel.getQicsRelatedModelAndScreenPanel(),entryStationDefaultStatusPanel.getEntryStationDefaultStatusPanel(), entryStationDefaultStatusPanel.getProcessPointDetailPanel());
		entryScreenScroll.setFitToWidth(true);
		entryScreenScroll.setContent(tabContainer);
		entryStationDefaultStatusPanel.activateController();
		settingsContainer.getChildren().add(getEntryStationSettingsPanel());
		upcContainer.getChildren().addAll(createUPCPanel());
		entryScreenTab.setText("Entry Screen");
		entryScreenTab.setId("entryScreenId");
		entryScreenTab.setContent(entryScreenScroll);
		entryScreenTab.setClosable(false);
		settingTab.setText("Setting");
		settingTab.setId("settingId");
		settingTab.setClosable(false);
		settingTab.setContent(settingsContainer);
		upcTab.setText("MBPN/UPC");
		upcTab.setId("UPC");
		upcTab.setContent(upcContainer);
		upcTab.setClosable(false);
		previousDefectVisible.setText("Previous Defect Visible");
		previousDefectVisible.setId("previousDefectVisible");
		previousDefectVisible.setContent(entryDepartmentDefectPanel.getEntryDepartmentDefectPanel());
		previousDefectVisible.setClosable(false);
		limitResponsibilityTab.setText("Limit Responsibility");
        limitResponsibilityTab.setId("limitResponsibility");
        limitResponsibilityPanel = new LimitResponsibilityPanel(getModel(), this);
        limitResponsibilityPanel.activateController();
        limitResponsibilityTab.setContent(limitResponsibilityPanel);
        limitResponsibilityTab.setClosable(false);
		cloneStationTab.setText("Clone Station");
		cloneStationTab.setId("cloneStationId");
		cloneStationTab.setContent(cloneStationPanel.getCloneStationScreenPanel());
		cloneStationTab.setClosable(false);
		cloneStationPanel.activateController();
        tabPane.getTabs().addAll(entryScreenTab, settingTab,upcTab,previousDefectVisible,limitResponsibilityTab,cloneStationTab);
        return tabPane;
	}

	public void onTabSelected() {
	}

	@Override
	public void reload(){
	}

	@Override
	public void start() {
	}

	public String getScreenName() {
		return "Station Config";
	}
	
	/**
	 * This method is used to create TitledPane for Part panel.
	 * @param title
	 * @param content
	 * @return
	 */
	public static TitledPane createTitledPane(String title,Node content,double width,double height) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(content);
		if("EntryStation".equals(title)){
			titledPane.setPrefSize(width, height);
		}
		else if(!title.contains("MBPN/UPC Part(s)")){
			titledPane.setPrefSize(width, height);
		}
		return titledPane;
	}

	/**
	 * This method is used to create UPC Part Panel 
	 * @return pane
	 */
	private MigPane createUPCPanel(){
		MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");
		HBox upcContainer=new HBox();
		VBox avilableUPCpanel=new VBox();
		avilableUPCTablePane = getUPCListTableView("AvailableUPCTable");
		assignedUPCTablePane = getUPCListTableView("AssigneUPCTable");
		TitledPane assignedTitledPane = createTitledPane("Assigned MBPN/UPC Part(s)",assignedUPCTablePane,0.0,0.0);
		assignedTitledPane.setPadding(new Insets(height/24.0,0,0,0));
		avilableUPCpanel.setPadding(new Insets(0));
		upcContainer.getChildren().addAll(createTitledPane("Available MBPN/UPC Part(s)",avilableUPCpanel,0.0,0.0),createUPCPartShiftButton(), assignedTitledPane);
		upcPartFilterTextfield = new LabeledUpperCaseTextField("Search MBPN/UPC Part", "Filter", 10, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT, false,new Insets(5));
		upcPartFilterTextfield.getLabel().setPadding(new Insets(5,0,0,0));
		avilableUPCpanel.getChildren().addAll(upcPartFilterTextfield,avilableUPCTablePane);
		upcContainer.setSpacing(50);
		pane.add(upcContainer, "left, span, wrap");
		pane.add(createUPCPartOperationButton());
		pane.setId("UPCPartPane");
		return pane;
	}

	/**
	 * This method is used to create shift button for UPC Part  
	 * @return shiftContainer
	 */
	private VBox createUPCPartShiftButton(){
		VBox shiftContainer=new VBox();
		rightShiftUPCPartBtn=entryDeptAndWriteUpDeptPanel.createBtn(">",getController());
		rightShiftUPCPartBtn.setId(StationConfigurationOperations.RIGHT_SHIFT_UPC_PART_BUTTON.getName());
		leftShiftUPCPartBtn=entryDeptAndWriteUpDeptPanel.createBtn("<",getController());
		leftShiftUPCPartBtn.setId(StationConfigurationOperations.LEFT_SHIFT_UPC_PART_BUTTON.getName());
		shiftContainer.getChildren().addAll(rightShiftUPCPartBtn,leftShiftUPCPartBtn);
		shiftContainer.setSpacing(20);
		shiftContainer.setAlignment(Pos.CENTER);
		return shiftContainer;
	}

	/**
	 * This method is used to create shift button for UPC Part  
	 * @return shiftContainer
	 */
	private HBox createUPCPartOperationButton(){
		HBox upcPartOperationButton = new HBox();
		updateUPCPartBtn=entryDeptAndWriteUpDeptPanel.createBtn("Update",getController());
		updateUPCPartBtn.setId(StationConfigurationOperations.UPDATE_UPC_PART_BUTTON.getName());
		resetUPCPartBtn=entryDeptAndWriteUpDeptPanel.createBtn("Reset",getController());
		resetUPCPartBtn.setId(StationConfigurationOperations.RESET_UPC_PART_BUTTON.getName());
		upcPartOperationButton.getChildren().addAll(resetUPCPartBtn,updateUPCPartBtn);
		upcPartOperationButton.setAlignment(Pos.BASELINE_LEFT);
		upcPartOperationButton.setSpacing(50);
		upcPartOperationButton.setPadding(new Insets(20, 0, 10, width/5.0));
		return upcPartOperationButton;
	}

	/**
	 * This method is used to create table for UPC part 
	 * @return panel
	 */
	private ObjectTablePane<QiBomQicsPartMapping> getUPCListTableView(String tableId){
		final ColumnMappingList columnMappingList = ColumnMappingList.with("MBPN Main No", "mainPartNo").put("MBPN/UPC Part Name", "inspectionPartName");
		final ObjectTablePane<QiBomQicsPartMapping> tablePane = new ObjectTablePane<QiBomQicsPartMapping>(columnMappingList);
		LoggedTableColumn<QiBomQicsPartMapping,Integer> serialNoColSettings = new LoggedTableColumn<QiBomQicsPartMapping, Integer>();
		createSerialNumber(serialNoColSettings);
		tablePane.getTable().getColumns().add(0, serialNoColSettings);
		tablePane.getTable().getColumns().get(0).setText("#");
		tablePane.getTable().getColumns().get(0).setResizable(true);
		tablePane.setPrefHeight(height/2.5);
		tablePane.setPrefWidth(width/4);
		tablePane.getTable().getColumns().get(0).setPrefWidth(tablePane.getPrefWidth()*0.2);
		tablePane.getTable().getColumns().get(1).setPrefWidth(tablePane.getPrefWidth()*0.40);
		tablePane.getTable().getColumns().get(2).setPrefWidth(tablePane.getPrefWidth()*0.40);
		tablePane.setSelectionMode(SelectionMode.MULTIPLE);
		tablePane.setConstrainedResize(false);
		tablePane.setId(tableId);
		return tablePane;
	}

	/**
	 * This method is used to enabled and disabled buttons like Update , Reset and Shift buttons
	 * @param enabledAndDisabled
	 */
	private void enabledAndDisabledButtons(boolean enabledAndDisabled){
		getEntryDeptAndWriteUpDeptPanel().getUpdateEntryDepartmentBtn().setDisable(enabledAndDisabled);
		getEntryDeptAndWriteUpDeptPanel().getUpdateWriteUpDepartmentBtn().setDisable(enabledAndDisabled);
		getEntryStationDefaultStatusPanel().getUpdateEntryScreenBtn().setDisable(enabledAndDisabled);
		getEntryDeptAndWriteUpDeptPanel().getResetEntryDepartmentBtn().setDisable(enabledAndDisabled);
		getEntryDeptAndWriteUpDeptPanel().getResetWriteUpDepartmentBtn().setDisable(enabledAndDisabled);
		getEntryStationDefaultStatusPanel().getResetEntryScreenBtn().setDisable(enabledAndDisabled);
		getEntryDeptAndWriteUpDeptPanel().getRightShiftDepartmentBtn().setDisable(enabledAndDisabled);
		getEntryDeptAndWriteUpDeptPanel().getLeftShiftDepartmentBtn().setDisable(enabledAndDisabled);
		getEntryDeptAndWriteUpDeptPanel().getRightShiftWriteUpDepartmentBtn().setDisable(enabledAndDisabled);
		getEntryDeptAndWriteUpDeptPanel().getLeftShiftWriteUpDepartmentBtn().setDisable(enabledAndDisabled);
		getEntryStationDefaultStatusPanel().getRightShiftEntryScreenBtn().setDisable(enabledAndDisabled);
		getEntryStationDefaultStatusPanel().getLeftShiftEntryScreenBtn().setDisable(enabledAndDisabled);
		getEntryStationDefaultStatusPanel().getUpdateDefaultStatusBtn().setDisable(enabledAndDisabled);
		getLeftShiftUPCPartBtn().setDisable(enabledAndDisabled);
		getRightShiftUPCPartBtn().setDisable(enabledAndDisabled);
		getUpdateUPCPartBtn().setDisable(enabledAndDisabled);
		getResetUPCPartBtn().setDisable(enabledAndDisabled);
	}
	
	/**
	 * This method is create Entry Station Settings panel
	 */
	private Node getEntryStationSettingsPanel() {
		VBox vBox = new VBox();
		entryStationConfigSettingsTablePane = createEntryStationConfigSettingsTablePane();
		entryStationConfigSettingsTablePane.setPadding(new Insets(5, 0, 0, 0));
		entryStationConfigSettingsTablePane.setId("entryStationConfigSettingsTablePane");
		vBox.getChildren().addAll(entryStationConfigSettingsTablePane, findPropertyValueCombobox(), getStationRepairAreaSettingsPanel());
		return vBox;
	}
	
	/**
	 * This method is used to get Station Repair Area Settings Panel
	 * @return
	 */
	private TitledPane getStationRepairAreaSettingsPanel() {
		MigPane panel = new MigPane("insets 5 5 0 5", "[left]5[center]", "");
		panel.setCenterShape(true);
		
		LoggedLabel nameLabel = UiFactory.createLabel("nameLabel", "Setting Name: ", (int)(width * 0.01));
		LoggedLabel nameValueLabel = UiFactory.createLabel("nameValueLabel", AdditionalStationConfigSettings.REPAIR_AREA.getSettingName(), (int)(width * 0.01));
		
		LoggedLabel descLabel = UiFactory.createLabel("descLabel", "Setting Description: ", (int)(width * 0.01));
		LoggedLabel descValueLabel = UiFactory.createLabel("descValueLabel", AdditionalStationConfigSettings.REPAIR_AREA.getSettingDesc(), (int)(width * 0.01));
		
		stationRepairAreaComboBox = new LoggedComboBox<String>();
		
		saveStationRepairAreaBtn = createBtn(QiConstant.SAVE, getController());
		saveStationRepairAreaBtn.setId(StationConfigurationOperations.SAVE_STATION_REPAIR_AREA.getName());
		saveStationRepairAreaBtn.setDisable(true);
		
		panel.add(nameLabel,"");
		panel.add(nameValueLabel,"wrap");
		panel.add(descLabel,"");
		panel.add(descValueLabel,"wrap");
		HBox valuePane = new HBox();
		valuePane.getChildren().addAll(stationRepairAreaComboBox, saveStationRepairAreaBtn);
		valuePane.setSpacing(10);
		valuePane.setPrefWidth(width * 0.5);
		valuePane.setAlignment(Pos.CENTER);
		
		panel.add(valuePane,"span");
		
		HBox mainPane = new HBox();
		mainPane.getChildren().add(panel);
		mainPane.setAlignment(Pos.CENTER);
		
		TitledPane pane = createTitledPane("Station Repair Area Setting", mainPane, width*0.7, height*0.3);
		return pane;
	}
	
	/**
	 * This method is used to create Entry Station Settings table
	 */
	private ObjectTablePane<QiEntryStationSettingsDto> createEntryStationConfigSettingsTablePane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Setting Name", "SettingName")
				.put("Setting Comment", "SettingDescription").put("Value", "SettingPropertyValue");
		Double[] columnWidth = new Double[] { 0.15, 0.60, 0.25 };
		ObjectTablePane<QiEntryStationSettingsDto> panel = new ObjectTablePane<QiEntryStationSettingsDto>(
				columnMappingList, columnWidth);
		panel.setConstrainedResize(true);
		LoggedTableColumn<QiEntryStationSettingsDto, Integer> serialNoColSettingsTable = new LoggedTableColumn<QiEntryStationSettingsDto, Integer>();
		createSerialNumber(serialNoColSettingsTable);
		panel.getTable().getColumns().add(0, serialNoColSettingsTable);
		panel.getTable().getColumns().get(0).setText("#");
		panel.getTable().getColumns().get(0).setResizable(true);
		panel.getTable().getColumns().get(0).setMaxWidth(25);
		panel.getTable().getColumns().get(0).setMinWidth(25);
		return panel;
	}

	/**
	 * This method is used to create LoggedComboBox for setting property values
	 */
	public MigPane findPropertyValueCombobox() {

		MigPane pane = new MigPane("insets 2 2 0 2", "[center,grow,fill]", "");
		updateSettingValueComboBox = new LoggedComboBox<String>();
		updateSettingValueComboBox.getStyleClass().add("combo-box");
		updateSettingValueComboBox.setId("updateSettingValueComboBox");
		entryStationConfigSettingsBox.getChildren().add(updateSettingValueComboBox);
		entryStationConfigSettingsBox.setAlignment(Pos.CENTER);
		entryStationConfigSettingsBox.getChildren().add(updatePropertyButton());
		pane.add(createTitledPane("Update Value for selected setting", entryStationConfigSettingsBox, width*0.7, height*0.1), "span,wrap");
		return pane;
	}

	/**
	 * This method is used to update property value for selected setting
	 */
	private HBox updatePropertyButton() {
		HBox save = new HBox();
		updateSettingsPropertyBtn = createBtn(QiConstant.SAVE, getController());
		updateSettingsPropertyBtn.setId(StationConfigurationOperations.UPDATE_SETTINGS_PROPERTY.getName());
		updateSettingsPropertyBtn.setDisable(true);
		save.getChildren().add(updateSettingsPropertyBtn);
		save.setPadding(new Insets(0, 0, 0, 30));
		return save;
	}
	/**
	 * This method is used to reload property value LoggedComboBox
	 */
	@SuppressWarnings("unchecked")
	public void reloadPropertyValue() {
		QiEntryStationSettingsDto myDto = getEntryStationConfigSettingsTablePane().getTable().getSelectionModel().getSelectedItem();
		if(null != myDto)  {
			getUpdateSettingValueComboBox().getItems().clear();
			if(myDto.getId() == 11)  {  //DEFAULT_UPC_QUANTITY
				getUpdateSettingValueComboBox().getItems().add("0");
			}
			getUpdateSettingValueComboBox().getItems().
			addAll(QiEntryStationConfigurationSettings.getType(myDto.getId()).isSettingTypeNumber() ?
					QiEntryStationConfigurationSettings.getType(myDto.getId()).getPropertyValueByNumber():
						QiEntryStationConfigurationSettings.getType(myDto.getId()).getPropertyValueByString());
			getUpdateSettingValueComboBox().setValue(myDto.getSettingPropertyValue());
		}
	}

	public TabPane getTabPane() {
		return tabPane;
	}
	public Tab getEntryScreenTab() {
		return entryScreenTab;
	}
	public Tab getSettingTab() {
		return settingTab;
	}
	public ObjectTablePane<QiEntryStationSettingsDto> getEntryStationConfigSettingsTablePane() {
		return entryStationConfigSettingsTablePane;
	}
	public LoggedComboBox<String> getUpdateSettingValueComboBox() {
		return updateSettingValueComboBox;
	}
	public LoggedButton getUpdateSettingsPropertyBtn() {
		return updateSettingsPropertyBtn;
	}
	public LoggedButton getSaveAllSettingBtn() {
		return saveAllSettingBtn;
	}
	public ObjectTablePane<QiBomQicsPartMapping> getAvilableUPCTablePane() {
		return avilableUPCTablePane;
	}
	public ObjectTablePane<QiBomQicsPartMapping> getAssignedUPCTablePane() {
		return assignedUPCTablePane;
	}
	public LoggedButton getRightShiftUPCPartBtn() {
		return rightShiftUPCPartBtn;
	}
	public LoggedButton getLeftShiftUPCPartBtn() {
		return leftShiftUPCPartBtn;
	}
	public LoggedButton getUpdateUPCPartBtn() {
		return updateUPCPartBtn;
	}
	public LoggedButton getResetUPCPartBtn() {
		return resetUPCPartBtn;
	}
	public UpperCaseFieldBean getUpcPartFilterTextfield() {
		return upcPartFilterTextfield.getControl();
	}
	public Tab getUpcTab() {
		return upcTab;
	}
	public StationDepartmentPanel getEntryDeptAndWriteUpDeptPanel() {
		return entryDeptAndWriteUpDeptPanel;
	}
	public StationEntryScreenPanel getEntryStationDefaultStatusPanel() {
		return entryStationDefaultStatusPanel;
	}
	public EntryStationPanel getEntryStationPanel() {
		return entryStationPanel;
	}
	public LoggedTextField getUpdateSettingValueTextField() {
		return updateStettingValueTextField;
	}
	public void setUpdateStettingValueTextField(
			LoggedTextField updateStettingValueTextField) {
		this.updateStettingValueTextField = updateStettingValueTextField;
	}
	public PreviousDefectVisibleMaintPanel getEntryDepartmentDefectPanel() {
		return entryDepartmentDefectPanel;
	}
	public void setEntryDepartmentDefectPanel(PreviousDefectVisibleMaintPanel entryDepartmentDefectPanel) {
		this.entryDepartmentDefectPanel = entryDepartmentDefectPanel;
	}
	public Tab getPreviousDefectVisible() {
		return previousDefectVisible;
	}
	public LimitResponsibilityPanel getLimitResponsibilityPanel() {
		return limitResponsibilityPanel;
	}
	public CloneStationPanel getCloneStationPanel() {
		return cloneStationPanel;
	}
	public Tab getCloneStationTab() {
		return cloneStationTab;
	}
	public Tab getLimitResponsibilityTab() {
		return limitResponsibilityTab;
	}
	public LoggedComboBox<String> getStationRepairAreaComboBox() {
		return stationRepairAreaComboBox;
	}
	public LoggedButton getSaveStationRepairAreaBtn() {
		return saveStationRepairAreaBtn;
	}
}
