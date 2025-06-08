package com.honda.galc.client.teamleader.qi.stationconfig.limitresponsibility;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigModel;
import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigPanel;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.QiStationResponsibilityDto;
import com.honda.galc.entity.enumtype.StationConfigurationOperations;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
/**
 * 
 * <h3>LimitResponsibilityPanel Class description</h3>
 * <p>
 * LimitResponsibilityPanel: Panel file for Limit Responsibility
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
 *        June 13, 2017
 * 
 */
public class LimitResponsibilityPanel extends BorderPane {

	private enum TablePaneType { SITE, PLANT, DEPT, LEVEL1};

	private LimitResponsibilityController controller;
	private ObjectTablePane<QiStationResponsibilityDto> availableResponsibilitySiteTablePane;
	private ObjectTablePane<QiStationResponsibilityDto> availableResponsibilityPlantTablePane;
	private ObjectTablePane<QiStationResponsibilityDto> availableResponsibilityDepartmentTablePane;
	private ObjectTablePane<QiStationResponsibilityDto> availableResponsibilityLevel1TablePane;
	private ObjectTablePane<QiStationResponsibilityDto> currentlyAssignedResponsibilitySiteTablePane;
	private ObjectTablePane<QiStationResponsibilityDto> currentlyAssignedResponsibilityPlantTablePane;
	private ObjectTablePane<QiStationResponsibilityDto> currentlyAssignedResponsibilityDepartmentTablePane;
	private ObjectTablePane<QiStationResponsibilityDto> currentlyAssignedResponsibilityLevel1TablePane;

	private LoggedButton assignSiteButton;
	private LoggedButton deassignSiteButton;
	private LoggedButton assignPlantButton;
	private LoggedButton deassignPlantButton;
	private LoggedButton assignDepartmentButton;
	private LoggedButton deassignDepartmentButton;
	private LoggedButton assignRespLevelButton;
	private LoggedButton deassignRespLevelButton;

	private LoggedButton updateBtn;
	private LoggedButton resetBtn;

	private TitledPane siteTitledPane;
	private TitledPane plantTitledPane;
	private TitledPane deptTitledPane;
	private TitledPane respTitledPane;

	public LimitResponsibilityPanel(EntryStationConfigModel model,
			EntryStationConfigPanel view) {
		controller = new LimitResponsibilityController(model, view);
		this.setCenter(initView());
	}

	/**
	 * This method used to load data on panel
	 */
	public void reload() {
		controller.loadInitialData();
	}

	/**
	 * This method is used to create Titled Pane
	 * @param title
	 * @param content
	 * @param width
	 * @param height
	 * @return
	 */
	private static TitledPane createTitledPane(String title,Node content,double width,double height) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(content);
		titledPane.setPrefSize(width, height);
		return titledPane;
	}

	/**
	 * This method is used to initialize panel view
	 * @return
	 */
	private Node initView() {
		MigPane finalpane = new MigPane("insets 5 5 0 5", "[left]5[center]5[left]", "");

		updateBtn = createBtn("Update", getController());
		updateBtn.setId(StationConfigurationOperations.UPDATE_RESPONSIBILITY.getName());
		updateBtn.setDisable(true);
		resetBtn = createBtn("Reset",getController());
		resetBtn.setId(StationConfigurationOperations.RESET_RESPONSIBILITY.getName());
		resetBtn.setDisable(true);

		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		Double screenResolutionWidth = screenBounds.getWidth();
		Double screenResolutionHeight = screenBounds.getHeight();

		siteTitledPane = createTitledPane("Site",createSitePane(),screenResolutionWidth*0.45,screenResolutionHeight*0.40);
		plantTitledPane = createTitledPane("Plant",createPlantPane(),screenResolutionWidth*0.45,screenResolutionHeight*0.40);
		deptTitledPane = createTitledPane("Dept",createDeptPane(),screenResolutionWidth*0.92,screenResolutionHeight*0.40);
		respTitledPane = createTitledPane("Responsible Level 1",createRespLevel1Pane(),screenResolutionWidth*0.92,screenResolutionHeight*0.40);

		finalpane.add(siteTitledPane,"split 2");
		finalpane.add(deptTitledPane,"wrap");
		finalpane.add(plantTitledPane,"split 2");
		finalpane.add(respTitledPane,"wrap");

		finalpane.add(updateBtn,"split 2 , center");
		finalpane.add(resetBtn,"center");
		return finalpane;
	}

	/**
	 * This method is used to create Site Pane
	 * @return
	 */
	private MigPane createSitePane(){
		MigPane pane = new MigPane("insets 5 5 0 5", "[left]5[center]5[left]", "[]5[]5[]");

		LoggedLabel availableSiteLabel= createLabelForPanel("available", "Available");
		LoggedLabel currentlyAssignedSiteLabel= createLabelForPanel("currentlyAssigned", "Currently Assigned");
		availableResponsibilitySiteTablePane=createTablePane(TablePaneType.SITE);
		availableResponsibilitySiteTablePane.setSelectionMode(SelectionMode.MULTIPLE);
		currentlyAssignedResponsibilitySiteTablePane=createTablePane(TablePaneType.SITE);
		currentlyAssignedResponsibilitySiteTablePane.setSelectionMode(SelectionMode.MULTIPLE);
		assignSiteButton= createBtn(">",getController());
		assignSiteButton.setId(StationConfigurationOperations.RIGHT_SHIFT_RESPONSIBILITY_SITE.getName());
		assignSiteButton.setDisable(true);
		deassignSiteButton= createBtn("<",getController());
		deassignSiteButton.setDisable(true);
		deassignSiteButton.setId(StationConfigurationOperations.LEFT_SHIFT_RESPONSIBILITY_SITE.getName());

		pane.add(availableSiteLabel,"cell 0 0");
		pane.add(currentlyAssignedSiteLabel,"cell 2 0");
		pane.add(availableResponsibilitySiteTablePane,"cell 0 1");
		pane.add(assignSiteButton,"cell 1 1,center,split 2,flowy");
		pane.add(deassignSiteButton);
		pane.add(currentlyAssignedResponsibilitySiteTablePane,"cell 2 1");

		return pane;
	}

	/**
	 * This method is used to create Plant Pane
	 * @return
	 */
	private MigPane createPlantPane(){
		MigPane pane = new MigPane("insets 5 5 0 5", "[left]5[center]5[left]", "[]5[]5[]5[]5[]");

		LoggedLabel availablePlantLabel= createLabelForPanel("available", "Available");
		LoggedLabel currentlyAssignedPlantLabel= createLabelForPanel("currentlyAssigned", "Currently Assigned");
		currentlyAssignedPlantLabel.getStyleClass().add("display-label-14");
		availableResponsibilityPlantTablePane=createTablePane(TablePaneType.PLANT);
		availableResponsibilityPlantTablePane.setSelectionMode(SelectionMode.MULTIPLE);
		currentlyAssignedResponsibilityPlantTablePane=createTablePane(TablePaneType.PLANT);
		currentlyAssignedResponsibilityPlantTablePane.setSelectionMode(SelectionMode.MULTIPLE);
		assignPlantButton= createBtn(">",getController());
		assignPlantButton.setDisable(true);
		assignPlantButton.setId(StationConfigurationOperations.RIGHT_SHIFT_RESPONSIBILITY_PLANT.getName());
		deassignPlantButton= createBtn("<",getController());
		deassignPlantButton.setDisable(true);

		deassignPlantButton.setId(StationConfigurationOperations.LEFT_SHIFT_RESPONSIBILITY_PLANT.getName());

		pane.add(availablePlantLabel,"cell 0 2");
		pane.add(currentlyAssignedPlantLabel,"cell 2 2");
		pane.add(availableResponsibilityPlantTablePane,"cell 0 3");
		pane.add(assignPlantButton,"cell 1 3,center,split 2,flowy");
		pane.add(deassignPlantButton);
		pane.add(currentlyAssignedResponsibilityPlantTablePane,"cell 2 3");

		return pane;
	}

	/**
	 * This method is used to Dept Pane
	 * @return
	 */
	private MigPane createDeptPane(){
		MigPane pane = new MigPane("insets 5 5 0 5", "[left]5[center]5[left]", "[]5[]5[]");

		LoggedLabel availableDeptLabel= createLabelForPanel("available", "Available");
		LoggedLabel currentlyAssignedDeptLabel= createLabelForPanel("currentlyAssigned", "Currently Assigned");
		availableResponsibilityDepartmentTablePane=createTablePane(TablePaneType.DEPT);
		availableResponsibilityDepartmentTablePane.setSelectionMode(SelectionMode.MULTIPLE);
		currentlyAssignedResponsibilityDepartmentTablePane=createTablePane(TablePaneType.DEPT);
		currentlyAssignedResponsibilityDepartmentTablePane.setSelectionMode(SelectionMode.MULTIPLE);
		assignDepartmentButton= createBtn(">",getController());
		assignDepartmentButton.setId(StationConfigurationOperations.RIGHT_SHIFT_RESPONSIBILITY_DEPT.getName());
		assignDepartmentButton.setDisable(true);
		deassignDepartmentButton= createBtn("<",getController());
		deassignDepartmentButton.setDisable(true);
		deassignDepartmentButton.setId(StationConfigurationOperations.LEFT_SHIFT_RESPONSIBILITY_DEPT.getName());

		pane.add(availableDeptLabel,"cell 3 0");
		pane.add(currentlyAssignedDeptLabel,"cell 5 0");
		pane.add(availableResponsibilityDepartmentTablePane,"cell 3 1");
		pane.add(assignDepartmentButton,"cell 4 1,center,split 2,flowy");
		pane.add(deassignDepartmentButton);
		pane.add(currentlyAssignedResponsibilityDepartmentTablePane,"cell 5 1");

		return pane;
	}

	/**
	 * This method is used to create Resp Level 1 Pane
	 * @return
	 */
	private MigPane createRespLevel1Pane(){
		MigPane pane = new MigPane("insets 5 5 0 5", "[left]5[center]5[left]", "[]5[]5[]5[]5[]");

		LoggedLabel availableLevel1Label= createLabelForPanel("available", "Available");
		LoggedLabel currentlyAssignedLevel1Label= createLabelForPanel("currentlyAssigned", "Currently Assigned");
		availableResponsibilityLevel1TablePane=createTablePane(TablePaneType.LEVEL1);
		availableResponsibilityLevel1TablePane.setSelectionMode(SelectionMode.MULTIPLE);
		currentlyAssignedResponsibilityLevel1TablePane=createTablePane(TablePaneType.LEVEL1);
		currentlyAssignedResponsibilityLevel1TablePane.setSelectionMode(SelectionMode.MULTIPLE);
		assignRespLevelButton= createBtn(">",getController());
		assignRespLevelButton.setId(StationConfigurationOperations.RIGHT_SHIFT_RESPONSIBILITY_LEVEL1.getName());
		assignRespLevelButton.setDisable(true);
		deassignRespLevelButton= createBtn("<",getController());
		deassignRespLevelButton.setDisable(true);
		deassignRespLevelButton.setId(StationConfigurationOperations.LEFT_SHIFT_RESPONSIBILITY_LEVEL1.getName());

		pane.add(availableLevel1Label,"cell 3 2");
		pane.add(currentlyAssignedLevel1Label,"cell 5 2");
		pane.add(availableResponsibilityLevel1TablePane,"cell 3 3");
		pane.add(assignRespLevelButton,"cell 4 3,center,split 2,flowy");
		pane.add(deassignRespLevelButton);
		pane.add(currentlyAssignedResponsibilityLevel1TablePane,"cell 5 3");

		return pane;
	}

	/**
	 * This method is used to create button
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
	 * This method is used to create table pane
	 * @return
	 */
	private ObjectTablePane<QiStationResponsibilityDto> createTablePane(TablePaneType component){ 
		ColumnMappingList columnMappingList=null;
		Double[] columnWidth=null;
		ObjectTablePane<QiStationResponsibilityDto> panel = null;
		double widthConstraint = 0;

		switch(component) {
		case SITE:
			columnMappingList = ColumnMappingList.with("Site", "site");
			columnWidth = new Double[] {0.11};
			widthConstraint = 0.3;
			break;
		case PLANT:
			columnMappingList = ColumnMappingList.with("Site", "site").put("Plant","plant");
			columnWidth = new Double[] {0.05,0.055};
			widthConstraint = 0.3;
			break;
		case DEPT:
			columnMappingList = ColumnMappingList.with("Site", "site").put("Plant","plant").put("Dept","dept").put("Dept Name", "departmentName");
			columnWidth = new Double[] {0.1,0.1,0.04, 0.10};
			widthConstraint = 0.4;
			break;
		case LEVEL1:
			columnMappingList = ColumnMappingList.with("Site", "site").put("Plant","plant").put("Dept","dept").put("Dept Name", "departmentName").put("Level 1","responsibleLevelName");
			columnWidth = new Double[] {0.08,0.08,0.04,0.04,0.1};
			widthConstraint = 0.4;
			break;
		}
		panel = new ObjectTablePane<QiStationResponsibilityDto>(columnMappingList,columnWidth);
		panel.setPrefSize(Screen.getPrimary().getVisualBounds().getWidth()*widthConstraint, Screen.getPrimary().getVisualBounds().getHeight()*0.3);
		return panel;
	}
	/**
	 * This Method is used to enable the buttons.
	 */
	public void enableButtons(){
		assignSiteButton.setDisable(false);
		deassignSiteButton.setDisable(false);
		assignPlantButton.setDisable(false);
		deassignPlantButton.setDisable(false);
		assignDepartmentButton.setDisable(false);
		deassignDepartmentButton.setDisable(false);
		assignRespLevelButton.setDisable(false);
		deassignRespLevelButton.setDisable(false);
	}
	/**
	 * Create Label for Panel
	 */
	private LoggedLabel createLabelForPanel(String id, String labelText) {
		LoggedLabel label= UiFactory.createLabel(id, labelText);
		label.setStyle(String.format("-fx-font-weight: bold; -fx-font-size: %dpx;", (int)(0.01 * getScreenWidth())));
		return label;
	}
	/**
	 * Method to be called from Parent Panel to initialize Controller
	 */
	public void activateController() {
		controller.initListeners();
	}

	public LimitResponsibilityController getController() {
		return controller;
	}

	public TitledPane getSiteTitledPane() {
		return siteTitledPane;
	}

	public TitledPane getPlantTitledPane() {
		return plantTitledPane;
	}

	public TitledPane getDeptTitledPane() {
		return deptTitledPane;
	}

	public TitledPane getRespTitledPane() {
		return respTitledPane;
	}

	public ObjectTablePane<QiStationResponsibilityDto> getAvailableResponsibilitySiteTablePane() {
		return availableResponsibilitySiteTablePane;
	}

	public ObjectTablePane<QiStationResponsibilityDto> getAvailableResponsibilityPlantTablePane() {
		return availableResponsibilityPlantTablePane;
	}

	public ObjectTablePane<QiStationResponsibilityDto> getAvailableResponsibilityDepartmentTablePane() {
		return availableResponsibilityDepartmentTablePane;
	}

	public ObjectTablePane<QiStationResponsibilityDto> getAvailableResponsibilityLevel1TablePane() {
		return availableResponsibilityLevel1TablePane;
	}

	public ObjectTablePane<QiStationResponsibilityDto> getCurrentlyAssignedResponsibilitySiteTablePane() {
		return currentlyAssignedResponsibilitySiteTablePane;
	}

	public ObjectTablePane<QiStationResponsibilityDto> getCurrentlyAssignedResponsibilityPlantTablePane() {
		return currentlyAssignedResponsibilityPlantTablePane;
	}

	public ObjectTablePane<QiStationResponsibilityDto> getCurrentlyAssignedResponsibilityDepartmentTablePane() {
		return currentlyAssignedResponsibilityDepartmentTablePane;
	}

	public ObjectTablePane<QiStationResponsibilityDto> getCurrentlyAssignedResponsibilityLevel1TablePane() {
		return currentlyAssignedResponsibilityLevel1TablePane;
	}

	public LoggedButton getUpdateBtn() {
		return updateBtn;
	}

	public LoggedButton getResetBtn() {
		return resetBtn;
	}
	
	public double getScreenWidth() {
		return Screen.getPrimary().getVisualBounds().getWidth();
	}
	
	public double getScreenHeight() {
		return Screen.getPrimary().getVisualBounds().getHeight();
	}
}