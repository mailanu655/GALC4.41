package com.honda.galc.client.teamleader.qi.view;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.PartDefectCombinationController;
import com.honda.galc.client.teamleader.qi.enumtype.QiRegionalScreenName;
import com.honda.galc.client.teamleader.qi.model.PartDefectCombMaintenanceModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.QiPartDefectCombinationDto;
import com.honda.galc.dto.qi.QiPartLocationCombinationDto;
import com.honda.galc.entity.qi.QiPartDefectCombination;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PartDefectCombinationMaintPanel</code> is the Panel class for Part Defect Combination.
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>L&T Infotech</TD>
 * <TD>26/08/2016</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
public class PartDefectCombinationMaintPanel extends QiAbstractTabbedView<PartDefectCombMaintenanceModel, PartDefectCombinationController> {
	
	private UpperCaseFieldBean filterTextField;
	
	private ObjectTablePane<QiPartDefectCombinationDto> partDefectCombinationTablePane;
	private ObjectTablePane<QiPartLocationCombinationDto> partTablePane;
	private ProgressBar progressBar;
	private VBox progressBox;
	List<QiPartDefectCombinationDto> loadedList;
	private double containerWidth;
	private ToggleGroup searchTextGroup;

	public PartDefectCombinationMaintPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
	}
	
	public void initView() {
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		containerWidth = screenBounds.getWidth()/4;
		StackPane outerPane = new StackPane();
		progressBar = new ProgressBar();
        progressBar.setPrefWidth(containerWidth/2);
        
        progressBox = new VBox();
        progressBox.getChildren().addAll(progressBar);
        progressBox.setVisible(false);
        progressBox.setAlignment(Pos.CENTER);
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		LoggedLabel filterLabel = UiFactory.createLabel("filterLabel", "Filter");
		filterLabel.getStyleClass().add("display-label");
		HBox mainTopContainer = new HBox();
		
		partDefectCombinationTablePane = createPartLocationCombinationTablePane();
		mainTopContainer.getChildren().addAll(createSearchFilterPanel(), createFilterContainer(), createInstructionContainer());
		outerPane.getChildren().addAll(partDefectCombinationTablePane,progressBox);
		this.setTop(mainTopContainer);
		this.setCenter(outerPane);
	}

	private VBox createFilterContainer() {
		VBox filterVBox = new VBox();
		partTablePane = createPartTablePane();
		filterVBox.getChildren().addAll(partTablePane);
		filterVBox.setPadding(new Insets(5));
		return filterVBox;
	}

	public LoggedButton createPlainBtn(String text,EventHandler<ActionEvent> handler)
	{
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.setStyle(String.format("-fx-font-size: %dpx;-fx-font-weight: bold; -height: 10", 10));
		btn.setOnAction(handler);
		return btn;
	}
	
	private HBox createSearchFilterText()  {
		Label inspectionPartFilterLabel = UiFactory.createLabel("label", "Filter", 12);
		filterTextField = UiFactory.createUpperCaseFieldBean("filter-textfield", 20, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		filterTextField.setOnAction(getController());
		LoggedButton clearFilterTextBtn;
		clearFilterTextBtn = createPlainBtn(QiConstant.CLEAR_TEXT_SYMBOL, getController());
		clearFilterTextBtn.getStyleClass().add("small-btn");

		HBox filterContainer = new HBox();
		filterContainer.getChildren().addAll(inspectionPartFilterLabel,filterTextField, clearFilterTextBtn);
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
		HBox primaryFilterRow = new HBox();
		primaryFilterRow.getChildren().addAll(partNameRadioBtn, defectTypeRadioBtn);
		primaryFilterRow.setSpacing(10.0);
		
		VBox primaryFilterVBox = new VBox();
		primaryFilterVBox.getChildren().addAll(searchByLbl, primaryFilterRow);
		return primaryFilterVBox;
	}
	
	
	private VBox createSearchFilterPanel()  {
		
		HBox filterContainer = createSearchFilterText();
		VBox primaryFilterVBox = createPrimarySearchFilterSubPanel();
		VBox groupSearchV = new VBox();
		groupSearchV.getChildren().addAll(filterContainer, primaryFilterVBox);
		
		Separator separator = new Separator();
		separator.setPadding(new Insets(10,0,0,0));
		separator.getStyleClass().add("bold-separator");
		VBox primaryFilter = new VBox();
		primaryFilter.getChildren().addAll(groupSearchV, separator);
		
		HBox primaryGroup = new HBox();
		primaryGroup.getChildren().addAll(primaryFilter);
		
		HBox secGroup = createFilterRadioButtons(getController(), containerWidth);
		
		
		VBox searchFilterPanel = new VBox();
		searchFilterPanel.getChildren().addAll( primaryGroup, secGroup);
		searchFilterPanel.setSpacing(5.0);
		searchFilterPanel.setPadding(new Insets(10.0, 0.0, 0.0, 20.0));
		searchFilterPanel.setPrefWidth(containerWidth);
		searchFilterPanel.setAlignment(Pos.CENTER_LEFT);
		return searchFilterPanel;
	}
	
	/**
	 * This method is used to create a Table Pane.
	 * @return
	 */
	private ObjectTablePane<QiPartDefectCombinationDto> createPartLocationCombinationTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("QICS Full Part Name", "fullPartDesc")
						  .put("Primary Defect", "primaryDefect")
						  .put("Secondary Defect", "secondaryDefect")
						  .put("Status\nActive=1\nInactive=0","status");
		
		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		Double[] columnWidth = new Double[] {
				0.49, 0.20, 0.20, 0.103
			}; 
		ObjectTablePane<QiPartDefectCombinationDto> panel = new ObjectTablePane<QiPartDefectCombinationDto>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		panel.setId("partDefectCombinationTablePane");
		return panel;
	}
	
	private ObjectTablePane<QiPartLocationCombinationDto> createPartTablePane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("QICS Part Name", "inspectionPartName")
				.put("QICS Full Part Name", "fullPartDesc");

		Double[] columnWidth = new Double[] { 0.1, 0.34 };
		ObjectTablePane<QiPartLocationCombinationDto> panel = new ObjectTablePane<QiPartLocationCombinationDto>(
				columnMappingList, columnWidth);
		panel.setConstrainedResize(false);
		panel.setPrefSize(containerWidth * 2, 220);

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
	
	@Override
	public void onTabSelected() {
	}

	@Override
	public ViewId getViewId() {
		return null;
	}

	@Override
	public void reload() {
	}
	
	public void reload(List<QiPartLocationCombinationDto> selectedParts)  {
		reload(selectedParts, false, false);
	}
	
	public void reload(List<QiPartLocationCombinationDto> selectedParts, boolean isRestoreListPosition, boolean isRestoreSelect) {
		progressBox.setVisible(true);
		Task<Void> mainTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				String searchText = getFilterTextData();
				int whichFilter = getSearchGroupRadioButtonValue();
				loadedList = getModel().findAllPartDefectCombByFilter(selectedParts, getSelectedRadioButtonValue(), searchText, whichFilter);
				updateProgress(100, 100);
				return null;
			}
		};

		mainTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			public void handle(WorkerStateEvent t) {
				progressBox.setVisible(false);
				partDefectCombinationTablePane.setData(loadedList, getModel().getLazyLoadDisplayRows(), isRestoreListPosition, isRestoreSelect);
			}
		});
		progressBar.progressProperty().bind(mainTask.progressProperty());

		new Thread(mainTask).start();
	}

	public short getSelectedRadioButtonValue() {
		if (getAllRadioBtn().isSelected()) {
			return (short) 2;
		} else if (getActiveRadioBtn().isSelected()) {
			return (short) 1;
		} else {
			return (short) 0;
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
		}
		return val;
	}

	@Override
	public void start() {
	}

	@Override
	public String getScreenName() {
		return QiRegionalScreenName.PART_DEFECT_COMBINATION.getScreenName();
	}
	
	public ObjectTablePane<QiPartDefectCombinationDto> getPartDefectCombinationTablePane() {
		return partDefectCombinationTablePane;
	}
	
	public ObjectTablePane<QiPartLocationCombinationDto> getPartTablePane() {
		return partTablePane;
	}

	public void setPartTablePane(ObjectTablePane<QiPartLocationCombinationDto> partTablePane) {
		this.partTablePane = partTablePane;
	}
	
	public UpperCaseFieldBean getFilterTextField() {
		return filterTextField;
	}
	
	/**
	 * This method is used to return text in filter to the controller.
	 * @return
	 */
	public String getFilterTextData()
	{
		return StringUtils.trimToEmpty(filterTextField.getText());
	}
	
	public ToggleGroup getSearchTextGroup() {
		return searchTextGroup;
	}

	public void setSearchTextGroup(ToggleGroup searchTextGroup) {
		this.searchTextGroup = searchTextGroup;
	}

	/**
	 * This method is used to get Entity object from DTO object.
	 * @param dto
	 * @return
	 */
	public QiPartDefectCombination getEntityFromDto(QiPartDefectCombinationDto dto) {
		return getModel().findPartDefectCombByKey(dto.getRegionalDefectCombinationId());
	}
	
	private HBox createInstructionContainer() {
		HBox instructionContainer = new HBox();
		Label inspectionPartFilterLabel = UiFactory.createLabel("label",
				"Enter minimal 3 char in Filter and hit Enter key to filter Part Loc.\nSelect one or more items from top table to filter Part Defect.",
				14);
		instructionContainer.getChildren().addAll(inspectionPartFilterLabel);
		instructionContainer.setAlignment(Pos.CENTER_LEFT);
		instructionContainer.setPadding(new Insets(10, 10, 10, 10));
		instructionContainer.setPrefWidth(containerWidth);
		return instructionContainer;
	}
}
