package com.honda.galc.client.teamleader.qi.defectTagging;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TitledPane;
import javafx.stage.Screen;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.defectResult.DefectResultMaintModel;
import com.honda.galc.client.teamleader.qi.defectResult.SearchMaintenancePanel;
import com.honda.galc.client.teamleader.qi.view.QiAbstractTabbedView;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.QiDefectResultDto;

/**
 * 
 * <h3>DefectTaggingMaintenancePanel Class description</h3>
 * <p>
 * DefectTaggingMaintenancePanel is used to load data in TableViews and perform the action on Update ,Create and delete incidents.
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
 * @author L&TInfotech<br>
 * 
 */
public class DefectTaggingMaintenancePanel extends QiAbstractTabbedView<DefectResultMaintModel, DefectTaggingMaintenanceController> {
	private  SearchMaintenancePanel searchMaintenancePanel;
	private LoggedButton incidentCreateBtn;
	private LoggedButton incidentUpdateBtn;
	private LoggedButton deleteTaggingBtn;
	private LoggedButton addTaggingBtn;
	private LoggedComboBox<String> availableIncidentComboBox;
	private ObjectTablePane<QiDefectResultDto> defectResultsTablePane;
	private LoggedButton advancedSearchBtn;
	private LoggedButton searchBtn;
	private LoggedButton resetButton;
	private UpperCaseFieldBean incidentFilterTextField;
	
	public DefectTaggingMaintenancePanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);
	}

	@Override
	public void initView() {
		searchMaintenancePanel = new SearchMaintenancePanel(getModel(),QiConstant.DEFECT_TAGGING, getController().isFullAccess());
		searchMaintenancePanel.setDefectTaggingMaintenancePanel(this);
		getController().setSearchMaintenancePanel(searchMaintenancePanel);
		MigPane mainPane = new MigPane("insets 5", "[left,grow]", "");
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		double screenResolutionWidth = screenBounds.getWidth();
		double screenResolutionHeight = screenBounds.getHeight();
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		searchBtn = createBtn("Search ", getController());
		searchBtn.setId("searchBtn");
		searchBtn.setDisable(true);
		advancedSearchBtn = createBtn("Advanced Search ", getController());
		advancedSearchBtn.setId("advancedSearchBtn");
		advancedSearchBtn.setDisable(true);
		resetButton = createBtn(QiConstant.RESET, getController());
		resetButton.setId("resetButton");
		resetButton.setDisable(true);
		mainPane.add(getSearchAndAdvanceAndResetButton(screenResolutionWidth),"wrap");
		mainPane.add(getDefectTaggingSearchResultContainer(screenResolutionWidth,screenResolutionHeight),"wrap");
		this.setTop(searchMaintenancePanel.getSearchPanel());
		this.setCenter(mainPane);
	}

	private MigPane getSearchAndAdvanceAndResetButton(double screenResolutionWidth) {
		MigPane searchAndAdvanceButtonPane = new MigPane("insets 5", "", "");
		searchAndAdvanceButtonPane.add(searchBtn,"split 2,gapleft "+screenResolutionWidth/1.6);
		searchAndAdvanceButtonPane.add(advancedSearchBtn);
		searchAndAdvanceButtonPane.add(resetButton,"wrap");
		Label maxLimitLabel=new Label("Max Search Limit :300");
		maxLimitLabel.setStyle("-fx-text-background-color: red;-fx-font-size: 12px;");
		searchAndAdvanceButtonPane.add(maxLimitLabel,"gapleft "+screenResolutionWidth/1.6);
		return searchAndAdvanceButtonPane;
	}
	/**
	 * This method is used to create Combobox related to Incident and Buttons related to Tagging
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private MigPane getDefectTaggingSearchResultContainer(double screenResolutionWidth,double screenResolutionHeight) {
		MigPane pane = new MigPane("insets 00 00 00 00", "[left,grow,shrink]", "[][]");
		Label inspectionPartFilterLabel = UiFactory.createLabel("label", "Filter");
		incidentFilterTextField=createFilterTextField("filter-textfield", 20, getController());
		this.defectResultsTablePane = createDefectTablePane();
		deleteTaggingBtn = createBtn("Delete Tagging ", getController());
		incidentCreateBtn = createBtn("Create Incident ", getController());
		incidentUpdateBtn = createBtn("Update Incident ", getController());
		incidentUpdateBtn.setDisable(true);
		addTaggingBtn = createBtn("Add Tagging", getController());
		availableIncidentComboBox = new LoggedComboBox<String>();
		availableIncidentComboBox.setId("availableIncidentComboBox");
		availableIncidentComboBox.getStyleClass().add("display-label-14");
		availableIncidentComboBox.getItems().addAll(getModel().findAllQiIncidentTitle());
		availableIncidentComboBox.setMinSize(400, 30);
		pane.add(inspectionPartFilterLabel,"split2 , right");
		pane.add(incidentFilterTextField,"wrap , right");
		pane.add(createTitledPane("Current Defect Selection",defectResultsTablePane,screenResolutionWidth*0.95,screenResolutionHeight*0.35),"span");
		pane.add(availableIncidentComboBox,"split 12, span 8,height ::30 ,width :: 400");
		pane.add(incidentUpdateBtn,"gapleft:200 ,right");
		pane.add(incidentCreateBtn,"right");
		pane.add(addTaggingBtn,"right");
		pane.add(deleteTaggingBtn,"right");
		return pane;
	}
	/**
	 * This method is used to create the TableView
	 * @return
	 */
	private ObjectTablePane<QiDefectResultDto> createDefectTablePane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Product Id", "productId")
				.put("Part Defect Combination", "partDefectDesc")
				.put("Incident Title", "incidentTitle")
				.put("Incident Type", "incidentType")
				.put("Incident Date", "incidentDate");
		Double[] columnWidth = new Double[] { 0.15, 0.20, 0.31, 0.18, 0.10 };
		ObjectTablePane<QiDefectResultDto> tablePane = new ObjectTablePane<QiDefectResultDto>(columnMappingList,
				columnWidth);
		tablePane.setConstrainedResize(false);
		tablePane.setSelectionMode(SelectionMode.MULTIPLE);
		tablePane.setId("createDefectTablePane");
		tablePane.setPadding(new Insets(0, 0, 0, 0));
		return tablePane;
	}
	
	/**
	 * This method is used to create the TitlePane 
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
	public void onTabSelected() {
		getController().activate();
	}

	@Override
	public void reload() {

	}

	@Override
	public void start() {

	}

	public String getScreenName() {
		return "Defect Tagging";
	}

	public LoggedButton getIncidentCreateBtn() {
		return incidentCreateBtn;
	}

	public void setIncidentCreateBtn(LoggedButton incidentCreateBtn) {
		this.incidentCreateBtn = incidentCreateBtn;
	}
	
	public LoggedButton getIncidentUpdateBtn() {
		return incidentUpdateBtn;
	}

	public LoggedButton getDeleteTaggingBtn() {
		return deleteTaggingBtn;
	}

	public void setDeleteTaggingBtn(LoggedButton deleteTaggingBtn) {
		this.deleteTaggingBtn = deleteTaggingBtn;
	}

	public LoggedButton getAddTaggingBtn() {
		return addTaggingBtn;
	}

	public void setAddTaggingBtn(LoggedButton addTaggingBtn) {
		this.addTaggingBtn = addTaggingBtn;
	}

	
	public LoggedComboBox<String> getAvailableIncidentType() {
		return availableIncidentComboBox;
	}

	public void setAvailableIncidentType(
			LoggedComboBox<String> availableIncidentComboBox) {
		this.availableIncidentComboBox = availableIncidentComboBox;
	}

	public ObjectTablePane<QiDefectResultDto> getDefectResultsTablePane() {
		return defectResultsTablePane;
	}

	public void setDefectResultsTablePane(
			ObjectTablePane<QiDefectResultDto> qiDefectResultsTablePane) {
		this.defectResultsTablePane = qiDefectResultsTablePane;
	}

	/**
	 * this method used to display user operation message
	 */
	public void setUserOperationMessage(final String message){
		EventBusUtil.publish(new StatusMessageEvent(message, StatusMessageEventType.INFO));
	}
	
	/**
	 * this method used to display user operation message
	 */
	public void setUserOperationErrorMessage(final String message){
		EventBusUtil.publish(new StatusMessageEvent(message, StatusMessageEventType.ERROR));
	}

	public LoggedButton getAdvancedSearchBtn() {
		return advancedSearchBtn;
	}

	public void setAdvancedSearchBtn(LoggedButton advancedSearchBtn) {
		this.advancedSearchBtn = advancedSearchBtn;
	}

	public LoggedButton getSearchBtn() {
		return searchBtn;
	}

	public void setSearchBtn(LoggedButton searchBtn) {
		this.searchBtn = searchBtn;
	}

	public LoggedButton getResetButton() {
		return resetButton;
	}

	public void setResetButton(LoggedButton resetButton) {
		this.resetButton = resetButton;
	}
	
	public String getFilterTextData()
	{
		return StringUtils.trimToEmpty(incidentFilterTextField.getText().trim());
	}

	public UpperCaseFieldBean getPartFilterTextField() {
		return incidentFilterTextField;
	}
	
	
}
