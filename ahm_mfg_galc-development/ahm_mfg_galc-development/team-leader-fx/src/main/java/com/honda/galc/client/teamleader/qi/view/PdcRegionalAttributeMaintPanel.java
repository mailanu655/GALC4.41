package com.honda.galc.client.teamleader.qi.view;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.PdcRegionalAttributeMaintPanelController;
import com.honda.galc.client.teamleader.qi.model.PdcRegionalAttributeMaintModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.QiPartLocationCombinationDto;
import com.honda.galc.dto.qi.QiRegionalAttributeDto;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
/**
 * 
 * <h3>PdcRegionalAttributeMaintPanel Class description</h3>
 * <p>
 * PdcRegionalAttributeMaintPanel is used to assign ,Filter ,TableView and populate data in TableView
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
public class PdcRegionalAttributeMaintPanel extends QiAbstractTabbedView<PdcRegionalAttributeMaintModel, PdcRegionalAttributeMaintPanelController>{

	private UpperCaseFieldBean defectFilterTextField;
	private ObjectTablePane<QiRegionalAttributeDto> regionalAttributeMaintTablePane;
	private ObjectTablePane<QiPartLocationCombinationDto> partTablePane;
	ToggleGroup assignGroup;
	ToggleGroup reportableGroup;
	ToggleGroup searchTextGroup;

	private LoggedRadioButton allRadioButton;
	private LoggedRadioButton assignedRadioButton;
	private LoggedRadioButton notAssignedRadioButton;
	private LoggedRadioButton reportableAll;
	private LoggedRadioButton reportable;
	private LoggedRadioButton nonReportable;
	private ProgressBar progressBar;
	private VBox progressBox;
	private List<QiRegionalAttributeDto> regionalAttrList;
	private double width;
		
	public PdcRegionalAttributeMaintPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
	}

	/**
	 * This method is used to create layout of the Inspection Part Maintenance Parent screen
	 */
	public void initView() {
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		width = primaryScreenBounds.getWidth()/4;
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		
		HBox outerContainer = new HBox();
		StackPane mainPane = new StackPane();
		
		outerContainer.getChildren().addAll(createSearchFilterPanel(), createPartNameContainer(), createInstructionContainer());
		regionalAttributeMaintTablePane = createDefectTablePane();
		setSerialNumberColumnProperty();
		progressBar = new ProgressBar();
        progressBar.setPrefWidth(width/2);
        
        progressBox = new VBox();
        progressBox.getChildren().addAll(progressBar);
        progressBox.setVisible(false);
        progressBox.setAlignment(Pos.CENTER);
        mainPane.getChildren().addAll(regionalAttributeMaintTablePane,progressBox);
		this.setTop(outerContainer);
		this.setCenter(mainPane);
	}
	
	
	/**
	 * This method is used to create a Table Pane.
	 * @return
	 */
	private ObjectTablePane<QiRegionalAttributeDto> createDefectTablePane(){ 
		ColumnMappingList columnMappingList = ColumnMappingList.with("QICS Full Part Name", "fullPartName")
		.put("Primary Defect","defectTypeName")
		.put("Secondary Defect", "defectTypeName2")
		.put("Rpt/Non-Rpt", "reportableDefect")
		.put("Theme", "themeName")
		.put("IQS Version", "iqsVersion")
		.put("IQS Category","iqsCategory")
		.put("IQS Question", "iqsQuestion");
		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		Double[] columnWidth = new Double[] {
				0.24, 0.10,0.09,0.06,0.07,0.07,0.13,0.175
			};
		ObjectTablePane<QiRegionalAttributeDto> panel = new ObjectTablePane<QiRegionalAttributeDto>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		return panel;
	}
	
	private ObjectTablePane<QiPartLocationCombinationDto> createPartTablePane(){ 
		ColumnMappingList columnMappingList = ColumnMappingList.with("QICS Part Name", "inspectionPartName")
				.put("QICS Full Part Name", "fullPartDesc");

		Double[] columnWidth = new Double[] { 0.1, 0.34 };
		ObjectTablePane<QiPartLocationCombinationDto> panel = new ObjectTablePane<QiPartLocationCombinationDto>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		panel.setPrefSize(2*width, 220);
		
		LoggedTableColumn<QiPartLocationCombinationDto, Integer> column = new LoggedTableColumn<QiPartLocationCombinationDto, Integer>();
		createSerialNumber(column);
		column.setText("#");
		column.setResizable(true);
		column.setMaxWidth(100);
		column.setMinWidth(1);
		panel.getTable().getColumns().add(0, column);
		
		panel.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		return panel;
	}
	
	/**
	 * Sets the serial number column property.
	 */
	private void setSerialNumberColumnProperty() {
		LoggedTableColumn<QiRegionalAttributeDto, Integer> column = new LoggedTableColumn<QiRegionalAttributeDto, Integer>();
		createSerialNumber(column);
		column.setText("#");
		column.setResizable(true);
		column.setMaxWidth(100);
		column.setMinWidth(1);
		regionalAttributeMaintTablePane.getTable().getColumns().add(0, column);
	}
	
	@Override
	public void onTabSelected() {
	}

	@Override
	public String getScreenName() {
		return "Regional Attribute";
	}

	@Override
	public void reload() {
	}

	public void reload(List<QiPartLocationCombinationDto> selectedParts)  {
		reload(selectedParts, false, false);
	}
	
	/**
	 * Reload.
	 *
	 * @param filterValue the filter value
	 */
	public void reload(List<QiPartLocationCombinationDto> selectedParts, boolean isRestoreListPosition, boolean isRestoreSelect) {
		progressBox.setVisible(true);
		Task<Void> mainTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				String searchText = getDefectFilterTextValue();
				int whichFilter = getSearchGroupRadioButtonValue();
				regionalAttrList = getModel().findAllPdcRegionalAttributes(selectedParts, getAssignedGroupRadioButtonValue(), searchText, whichFilter);
				updateProgress(100, 100);
				return null;
			}
		};

		mainTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			public void handle(WorkerStateEvent t) {
				progressBox.setVisible(false);
				regionalAttributeMaintTablePane.setData(regionalAttrList, getModel().getLazyLoadDisplayRows(), isRestoreListPosition, isRestoreSelect);
			}
		});
		progressBar.progressProperty().bind(mainTask.progressProperty());

		new Thread(mainTask).start();
	}
	
	@Override
	public void start() {
	}
	
	private HBox createSearchFilterText()  {
		Label inspectionPartFilterLabel = UiFactory.createLabel("label", "Filter", 12);
		defectFilterTextField = UiFactory.createUpperCaseFieldBean("filter-textfield", 20, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		defectFilterTextField.setOnAction(getController());
		LoggedButton clearFilterTextBtn;
		clearFilterTextBtn = createPlainBtn(QiConstant.CLEAR_TEXT_SYMBOL, getController());
		clearFilterTextBtn.getStyleClass().add("small-btn");

		HBox filterContainer = new HBox();
		filterContainer.getChildren().addAll(inspectionPartFilterLabel,defectFilterTextField, clearFilterTextBtn);
		filterContainer.setSpacing(10);
		filterContainer.setPadding(new Insets(2, 10, 5, 0));
		filterContainer.setAlignment(Pos.TOP_LEFT);
		
		return filterContainer;
	}
	
	
	private VBox createPrimarySearchFilterSubPanel()  {
		Label searchByLbl = new Label("Search By:");
		searchByLbl.setStyle(Fonts.SS_DIALOG_PLAIN(16));
		
		searchTextGroup = new ToggleGroup();
		LoggedRadioButton partNameRadioBtn = createRadioButton(QiConstant.PART_NAME, searchTextGroup, true, getController());
		partNameRadioBtn.setId(QiConstant.PART_NAME);
		LoggedRadioButton defectTypeRadioBtn = createRadioButton(QiConstant.DEFECT_TYPE, searchTextGroup, false, getController());
		defectTypeRadioBtn.setId(QiConstant.DEFECT_TYPE);
		LoggedRadioButton iqsRadioBtn = createRadioButton(QiConstant.IQS, searchTextGroup, false, getController());
		iqsRadioBtn.setId(QiConstant.IQS);
		LoggedRadioButton themeRadioBtn = createRadioButton(QiConstant.THEME, searchTextGroup, false, getController());
		themeRadioBtn.setId(QiConstant.THEME);
		VBox searchFilterCol1 = new VBox();
		searchFilterCol1.getChildren().addAll(partNameRadioBtn, defectTypeRadioBtn);
		searchFilterCol1.setSpacing(5.0);
		VBox searchFilterCol2 = new VBox();
		searchFilterCol2.getChildren().addAll(iqsRadioBtn, themeRadioBtn);
		searchFilterCol2.setSpacing(5.0);
		HBox primaryFilterRow = new HBox();
		primaryFilterRow.getChildren().addAll(searchFilterCol1, searchFilterCol2);
		primaryFilterRow.setSpacing(10.0);
		
		VBox primaryFilterVBox = new VBox();
		primaryFilterVBox.getChildren().addAll(searchByLbl, primaryFilterRow);
		return primaryFilterVBox;
	}
	
	private VBox createReportableSubPanel()  {
		Label reportableLbl = new Label("Reportable:");		
		reportableLbl.setStyle(Fonts.SS_DIALOG_PLAIN(16));
		reportableGroup = new ToggleGroup();
		reportable = createRadioButton("Reportable", searchTextGroup, false, getController());
		reportable.setId(QiConstant.REPORTABLE);
		nonReportable = createRadioButton("Non-Reportable", searchTextGroup, false, getController());
		nonReportable.setId(QiConstant.NON_REPORTABLE);
		
		LoggedButton searchBtn = createPlainBtn("Search", getController());
		searchBtn.getStyleClass().add("small-btn");
		searchBtn.setPadding(new Insets(0,0,0,20));
		
		HBox reportableRow = new HBox();
		reportableRow.getChildren().addAll(reportable, nonReportable, searchBtn);
		reportableRow.setSpacing(10.0);
		VBox reportableVBox = new VBox();
		reportableVBox.getChildren().addAll(reportableLbl, reportableRow);
		return reportableVBox;
	}
	
	
	private HBox createSecondaryFilterSubPanel()  {
		Label assignedLbl = new Label("Assigned:");		
		assignedLbl.setStyle(Fonts.SS_DIALOG_PLAIN(16));
		assignGroup = new ToggleGroup();
		HBox assignRadioBtnRow = new HBox();
		this.allRadioButton = createRadioButton(QiConstant.ALL, assignGroup, true, getController());
		this.allRadioButton.setId(QiConstant.ASSIGNED_ALL);
		this.assignedRadioButton = createRadioButton(QiConstant.ASSIGNED, assignGroup, false, getController());
		this.assignedRadioButton.setId(QiConstant.ASSIGNED);
		this.notAssignedRadioButton = createRadioButton(QiConstant.NOT_ASSIGNED, assignGroup, false, getController());
		this.notAssignedRadioButton.setId(QiConstant.NOT_ASSIGNED);
		assignRadioBtnRow.getChildren().addAll(allRadioButton, assignedRadioButton, notAssignedRadioButton);
		assignRadioBtnRow.setSpacing(10.0);
		VBox secFilter = new VBox();
		secFilter.getChildren().addAll(assignedLbl, assignRadioBtnRow);
		HBox secGroup = new HBox();
		secGroup.getChildren().addAll(secFilter);
		return secGroup;
	}
	
	private VBox createSearchFilterPanel()  {
		
		HBox filterContainer = createSearchFilterText();
		VBox primaryFilterVBox = createPrimarySearchFilterSubPanel();
		VBox groupSearchV = new VBox();
		groupSearchV.getChildren().addAll(filterContainer, primaryFilterVBox);
		groupSearchV.setBorder(new Border(new BorderStroke(Color.BLACK, 
	            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
		VBox reportableVBox = createReportableSubPanel();
		Separator separator = new Separator();
		separator.setPadding(new Insets(10,0,0,0));
		separator.getStyleClass().add("bold-separator");
		VBox primaryFilter = new VBox();
		primaryFilter.getChildren().addAll(groupSearchV, reportableVBox, separator);
		
		HBox primaryGroup = new HBox();
		primaryGroup.getChildren().addAll(primaryFilter);
		
		HBox secGroup = createSecondaryFilterSubPanel();
		
		
		VBox searchFilterPanel = new VBox();
		searchFilterPanel.getChildren().addAll( primaryGroup, secGroup);
		searchFilterPanel.setSpacing(5.0);
		searchFilterPanel.setPadding(new Insets(10.0, 0.0, 0.0, 20.0));
		searchFilterPanel.setPrefWidth(width);
		searchFilterPanel.setAlignment(Pos.CENTER_LEFT);
		return searchFilterPanel;
	}
	
	public LoggedButton createPlainBtn(String text,EventHandler<ActionEvent> handler)
	{
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.setStyle(String.format("-fx-font-size: %dpx;-fx-font-weight: bold; -height: 10", 10));
		btn.setOnAction(handler);
		return btn;
	}
	
	/**
	 * This method return a list of values based on selected radio buttons (e.g. Not_Assigned=0, Assigned=1, All=2)
	 * @return
	 */
	public short getAssignedGroupRadioButtonValue() {
		if(getNotAssignedRadioButton().isSelected()) {
			return (short)0;
		} else if(getAssignedRadioButton().isSelected()){
			return (short)1;
		} else {
			return (short)2;
		}		
	}

	/**
	 * This method return a list of values based on selected radio buttons (e.g. Not_Reportable=0, Reportable=1, All=2)
	 * @return
	 */
	public short getReportableGroupRadioButtonValue() {
		if(getNonReportable().isSelected()) {
			return (short)0;
		} else if(getReportable().isSelected()){
			return (short)1;
		} else {
			return (short)2;
		}		
	}

	/**
	 * This method return a list of values based on selected radio buttons (e.g. Not_Reportable=0, Reportable=1, All=2)
	 * @return
	 */
	public short getSearchGroupRadioButtonValue() {
		LoggedRadioButton tog = (LoggedRadioButton)getSearchTextGroup().getSelectedToggle();
		short val = 1; //PART_NAME
		if(!QiConstant.PART_NAME.equals(tog.getId()))  {
			if(QiConstant.DEFECT_TYPE.equals(tog.getId()))  {
				return 2;
			}
			else if(QiConstant.IQS.equals(tog.getId()))  {
				return 3;
			}
			else if(QiConstant.THEME.equals(tog.getId()))  {
				return 4;
			}
			else if(QiConstant.REPORTABLE.equals(tog.getId()))  {
				return 5;
			}
			else if(QiConstant.NON_REPORTABLE.equals(tog.getId()))  {
				return 6;
			}
			
		}
		return val;
	}

	/**
	 * Creates the filter container.
	 *
	 * @param width the width
	 * @return the h box
	 */
	private VBox createPartNameContainer() {
		VBox partTableContainer = new VBox();
		partTablePane = createPartTablePane();
		partTableContainer.getChildren().addAll(partTablePane);
		partTableContainer.setPadding(new Insets(5));
		partTableContainer.setAlignment(Pos.CENTER_LEFT);
		return partTableContainer;
	}

	private HBox createInstructionContainer() {
		HBox instructionContainer = new HBox();
		Label inspectionPartFilterLabel = UiFactory.createLabel("label", "Enter minimal 3 char in Filter and hit Enter key to filter Part Loc.\nSelect one or more items from top table to filter Part Defect.", 14);
		instructionContainer.getChildren().addAll(inspectionPartFilterLabel);
		instructionContainer.setAlignment(Pos.CENTER_LEFT);
		instructionContainer.setPadding(new Insets(10, 10, 10, 10));
		instructionContainer.setPrefWidth(width);
		return instructionContainer;
	}
	
	public ObjectTablePane<QiRegionalAttributeDto> getRegionalAttributeMaintTablePane() {
		return regionalAttributeMaintTablePane;
	}

	public void setRegionalAttributeMaintTablePane(
			ObjectTablePane<QiRegionalAttributeDto> regionalAttributeMaintTablePane) {
		this.regionalAttributeMaintTablePane = regionalAttributeMaintTablePane;
	}
	
	public ObjectTablePane<QiPartLocationCombinationDto> getPartTablePane() {
		return partTablePane;
	}

	public void setPartTablePane(ObjectTablePane<QiPartLocationCombinationDto> partTablePane) {
		this.partTablePane = partTablePane;
	}

	public LoggedRadioButton getAllRadioButton() {
		return allRadioButton;
	}

	public void setAllRadioButton(LoggedRadioButton allRadioButton) {
		this.allRadioButton = allRadioButton;
	}

	public LoggedRadioButton getAssignedRadioButton() {
		return assignedRadioButton;
	}

	public void setAssignedRadioButton(LoggedRadioButton assignedRadioButton) {
		this.assignedRadioButton = assignedRadioButton;
	}

	public LoggedRadioButton getNotAssignedRadioButton() {
		return notAssignedRadioButton;
	}

	public void setNotAssignedRadioButton(LoggedRadioButton notAssignedRadioButton) {
		this.notAssignedRadioButton = notAssignedRadioButton;
	}

	public LoggedRadioButton getReportable() {
		return reportable;
	}

	public void setReportable(LoggedRadioButton reportable) {
		this.reportable = reportable;
	}

	public LoggedRadioButton getNonReportable() {
		return nonReportable;
	}

	public void setNonReportable(LoggedRadioButton nonReportable) {
		this.nonReportable = nonReportable;
	}

	public LoggedRadioButton getReportableAll() {
		return reportableAll;
	}

	public void setReportableAll(LoggedRadioButton reportableAll) {
		this.reportableAll = reportableAll;
	}

	public ToggleGroup getSearchTextGroup() {
		return searchTextGroup;
	}

	public void setSearchTextGroup(ToggleGroup searchTextGroup) {
		this.searchTextGroup = searchTextGroup;
	}

	public UpperCaseFieldBean getDefectFilterTextField() {
		return defectFilterTextField;
	}

	public void setDefectFilterTextField(UpperCaseFieldBean defectFilterTextField) {
		this.defectFilterTextField = defectFilterTextField;
	}
	
	/**
	 * Gets the defect filter text value.
	 *
	 * @return the defect filter text value
	 */
	public String getDefectFilterTextValue(){
		return StringUtils.trimToEmpty(defectFilterTextField.getText());
	}

}
