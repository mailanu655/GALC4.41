package com.honda.galc.client.teamleader.qi.reportingtarget;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.view.QiAbstractTabbedView;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedMenuItem;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.qi.QiReportingMetric;
import com.honda.galc.entity.qi.QiReportingTarget;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * <h3>Class ReportingTargetMaintenancePanel</h3>
 * <p>
 * <code>ReportingMetricDialog</code>
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>15/11/2016</TD>
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
public class ReportingTargetMaintenancePanel extends QiAbstractTabbedView<ReportingTargetMaintenanceModel, ReportingTargetMaintenanceController> {

	private final String DATE_FORMAT = "yyyy-MM-dd";
	
	private LabeledTextField siteTextField;
	
	private LabeledComboBox<String> plantComboBox;
	private LabeledComboBox<String> productTypeComboBox;
	private LabeledComboBox<String> modelGroupComboBox;
	private LabeledComboBox<String> modelYearComboBox;
	private LabeledComboBox<String> demandTypeComboBox;
	private LabeledComboBox<String> departmentComboBox;
	private LabeledComboBox<QiReportingMetric> metricComboBox;
	
	private ToggleGroup radioToggleGroup;
	private ToggleGroup respLevelRadioToggleGroup;
	private LoggedRadioButton plantRadioButton;
	private LoggedRadioButton departmentRadioButton;
	private LoggedRadioButton themeRadioButton;
	private LoggedRadioButton tempTrackingRadioButton;
	private LoggedRadioButton deptRespLevelRadioButton;
	private LoggedRadioButton responsibleLevel1RadioButton;
	private LoggedRadioButton responsibleLevel2RadioButton;
	private LoggedRadioButton responsibleLevel3RadioButton;
	
	private LoggedButton addButton;
	private LoggedTextField totalMetricValue;
	private GridPane dateRangePane;
	private HBox dynamicContainer;
	private ObjectTablePane<QiReportingTarget> reportingTargetTable;
	
	private DatePicker startDatePicker;
	private DatePicker endDatePicker;
	private LoggedMenuItem createMenuItem;
	private LoggedMenuItem updateMenuItem;
	private LoggedMenuItem deleteMenuItem;
	private LoggedButton queryButton;
	private LoggedButton deleteButton;

	public ReportingTargetMaintenancePanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);
	}

	@Override
	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		this.setTop(createTopPane());
		this.setBottom(createBottomPane());
		handleDeptRespRadioButtonSelection(false);
	}

	@Override
	public void start() {

	}
	
	/**
	 * This method will be used to render top filter components.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private MigPane createTopPane() {
		MigPane topFilterContainer = new MigPane("insets 0", "", "[]0[]");
		queryButton = createBtn("Query", getController());
		deleteButton = createBtn("Delete", getController());
		deleteButton.setDisable(true);
		metricComboBox = createLabeledComboBox("metricComboBox", "Metric", true, true, false);
		metricComboBox.getControl().setMinWidth(200);
		addCellFactoryInMetricCombobox();
		initContextMenu();
		addContextMenuOnMetricComboBox();
		
		topFilterContainer.add(createTopComboboxSection(), "wrap");
		topFilterContainer.add(createTopRadioButtonsSection(), "wrap");
		topFilterContainer.add(createResponsibleLevelFilterSection(),"split 3");
		topFilterContainer.add(createDateRangeLayout(),"center");
		topFilterContainer.add(queryButton,"wrap ,gapleft 40 ,right");
		topFilterContainer.add(metricComboBox,"split 2 , gapleft 8, left");
		topFilterContainer.add(deleteButton,"wrap , gapleft 1000 ,right");
		topFilterContainer.add(createDynamicPane(),"wrap");
		return topFilterContainer;
	}

	/**
	 * This method will be used to create responsible level filter section.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private MigPane createResponsibleLevelFilterSection() {
		MigPane responsibleLevelContainer = new MigPane("","[][]");
		departmentComboBox = createLabeledComboBox("departmentComboBox", "Department", true, true, false);
		respLevelRadioToggleGroup = new ToggleGroup();
		responsibleLevel1RadioButton = createRadioButton(QiConstant.RESPONSIBLE_LEVEL_1, respLevelRadioToggleGroup, false, getController());
		responsibleLevel2RadioButton = createRadioButton(QiConstant.RESPONSIBLE_LEVEL_2, respLevelRadioToggleGroup, false, getController());
		responsibleLevel3RadioButton = createRadioButton(QiConstant.RESPONSIBLE_LEVEL_3, respLevelRadioToggleGroup, false, getController());
		enableDisableResponsibleRadioButtons(false);
		
		responsibleLevelContainer.add(departmentComboBox);
		responsibleLevelContainer.add(responsibleLevel1RadioButton);
		responsibleLevelContainer.add(responsibleLevel2RadioButton);
		responsibleLevelContainer.add(responsibleLevel3RadioButton);
		return responsibleLevelContainer;
	}

	/**
	 * This method will be used to create radio button filter layout.
	 * 
	 * @return
	 */
	private MigPane createTopRadioButtonsSection() {
		MigPane radioButtonContainer = new MigPane("","[]20[]");
		radioToggleGroup = new ToggleGroup();
		plantRadioButton = createRadioButton(QiConstant.TARGET_PLANT, radioToggleGroup, false, getController());
		departmentRadioButton = createRadioButton(QiConstant.TARGET_DEPARTMENT, radioToggleGroup, false, getController());
		themeRadioButton = createRadioButton(QiConstant.TARGET_THEME, radioToggleGroup, false, getController());
		tempTrackingRadioButton = createRadioButton(QiConstant.TARGET_LOCAL_THEME, radioToggleGroup, false, getController());
		deptRespLevelRadioButton = createRadioButton(QiConstant.TARGET_DEPT_RESP_LEVEL, radioToggleGroup, false, getController());
		
		radioButtonContainer.add(plantRadioButton);
		radioButtonContainer.add(departmentRadioButton);
		radioButtonContainer.add(themeRadioButton);
		radioButtonContainer.add(tempTrackingRadioButton);
		radioButtonContainer.add(deptRespLevelRadioButton);
		return radioButtonContainer;
	}

	/**
	 * This method will be used to create all the top level combo boxes.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private MigPane createTopComboboxSection() {
		MigPane topFilterContainer = new MigPane();
		siteTextField = new LabeledTextField("Site", false, TextFieldState.READ_ONLY, new Insets(10), 50, Pos.CENTER, false);
		BorderPane.setAlignment(siteTextField.getLabel(), Pos.CENTER);
		siteTextField.getControl().setMinWidth(100);
		siteTextField.setText(getModel().getSiteName());
		// creating combo box
		plantComboBox = createLabeledComboBox("plantComboBox", "Plant", false, true, false);
		productTypeComboBox = createLabeledComboBox("productTypeComboBox", "Product Type", false, true, false);
		modelGroupComboBox = createLabeledComboBox("modelComboBox", "Model Group", false, true, false);
		modelYearComboBox = createLabeledComboBox("modelYearComboBox", "Model Yr", false, true, false);
		demandTypeComboBox = createLabeledComboBox("demandTypeComboBox", "Demand Type", false, true, false);
		demandTypeComboBox.getControl().getItems().addAll(getModel().findAllDemandType());
		demandTypeComboBox.getControl().getSelectionModel().select(0);

		topFilterContainer.add(siteTextField);
		topFilterContainer.add(plantComboBox);
		topFilterContainer.add(productTypeComboBox);
		topFilterContainer.add(modelGroupComboBox);
		topFilterContainer.add(modelYearComboBox);
		topFilterContainer.add(demandTypeComboBox);
		return topFilterContainer;
	}
	
	
	/**
	 * Method will add cell factory in Metric combo box which will display
	 * 'Metric Name' only.
	 */
	private void addCellFactoryInMetricCombobox() {
		metricComboBox.getControl()
				.setCellFactory(new Callback<ListView<QiReportingMetric>, ListCell<QiReportingMetric>>() {
					@Override
					public ListCell<QiReportingMetric> call(ListView<QiReportingMetric> param) {

						ListCell<QiReportingMetric> cell = new ListCell<QiReportingMetric>() {
							@Override
							protected void updateItem(QiReportingMetric item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setText("");
								} else {
									setText(item.getId().getMetricName());
								}
							}
						};
						return cell;
					}
				});

		metricComboBox.getControl().setButtonCell(new ListCell<QiReportingMetric>() {
			@Override
			protected void updateItem(QiReportingMetric item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText("");
				} else {
					setText(item.getId().getMetricName());
				}
			}
		});
	}
	
	/**
	 * This method will be used to embed context menu on metric combo box.
	 * 
	 */
	private void addContextMenuOnMetricComboBox() {
		final ContextMenu contextMenu = new ContextMenu();
		metricComboBox.getControl().setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
			@Override
			public void handle(ContextMenuEvent menuEvent) {
			if(getController().isFullAccess()){
				contextMenu.getItems().clear();
				if (metricComboBox.getControl().getSelectionModel().isEmpty()) {
					contextMenu.getItems().add(createMenuItem);
				} else {
					contextMenu.getItems().addAll(createMenuItem, updateMenuItem, deleteMenuItem);
				}
				contextMenu.show(metricComboBox.getControl(), menuEvent.getScreenX(), menuEvent.getScreenY());
			}
			}
		});
		
	}
	
	/**
	 * This method will be used to initialize context menu items.
	 */
	private void initContextMenu() {
		createMenuItem = UiFactory.createMenuItem(QiConstant.CREATE);
		updateMenuItem = UiFactory.createMenuItem(QiConstant.UPDATE);
		deleteMenuItem = UiFactory.createMenuItem(QiConstant.DELETE);

		createMenuItem.setOnAction(getController());
		updateMenuItem.setOnAction(getController());
		deleteMenuItem.setOnAction(getController());
	}

	/**
	 * This method will be used to render date range component.
	 * 
	 * @return
	 */
	private Node createDateRangeLayout() {
		// first row components
		LoggedLabel blankLabel = createLoggedLabel("blankLabel", "", Fonts.SS_DIALOG_PLAIN(14), TextAlignment.CENTER);
		LoggedLabel startLabel = createLoggedLabel("startLabel", "Start", Fonts.SS_DIALOG_PLAIN(14), TextAlignment.CENTER);
		LoggedLabel endLabel = createLoggedLabel("endLabel", "End", Fonts.SS_DIALOG_PLAIN(14), TextAlignment.CENTER);
		// second row components
		LoggedLabel dateRangeLable = createLoggedLabel("dateRangeLable", "Date Range", Fonts.SS_DIALOG_PLAIN(14), TextAlignment.CENTER);
		dateRangeLable.getStyleClass().add("-fx-background-color: grey;");
		startDatePicker = getCalendarDatePicker();
		endDatePicker = getCalendarDatePicker();

		dateRangePane = new GridPane();
		dateRangePane.setAlignment(Pos.TOP_RIGHT);
		dateRangePane.setGridLinesVisible(true);
		dateRangePane.add(blankLabel, 1, 1);
		dateRangePane.add(startLabel, 2, 1);
		dateRangePane.add(endLabel, 3, 1);
		dateRangePane.add(dateRangeLable, 1, 2);
		dateRangePane.add(startDatePicker, 2, 2);
		dateRangePane.add(endDatePicker, 3, 2);
		GridPane.setMargin(startLabel, new Insets(5, 70, 5, 75));
		GridPane.setMargin(endLabel, new Insets(5, 70, 5, 77));
		GridPane.setMargin(dateRangeLable, new Insets(5));
		GridPane.setMargin(startDatePicker, new Insets(5));
		GridPane.setMargin(endDatePicker, new Insets(5));
		
		return dateRangePane;
	}
	
	/**
	 * This method will be used to date picker component.
	 * 
	 * @return
	 */
	private DatePicker getCalendarDatePicker() {
		DatePicker datePicker = new DatePicker();
		StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

			@Override
			public String toString(LocalDate date) {
				if (date != null) {
					return dateFormatter.format(date);
				} else {
					return "";
				}
			}

			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateFormatter);
				} else {
					return null;
				}
			}
		};
		datePicker.setConverter(converter);
		datePicker.setPromptText(DATE_FORMAT.toLowerCase());
		datePicker.setStyle("-fx-background-color: #ffc0cb;");
		return datePicker;
	}
	
	/**
	 * This HBox pane will act as a dynamic pane, children will be added
	 * dynamically based on some radio button selections.
	 * 
	 * @return
	 */
	private Node createDynamicPane() {
		dynamicContainer = new HBox(2);
		ScrollPane scroller = new ScrollPane(dynamicContainer);
		scroller.setVbarPolicy(ScrollBarPolicy.NEVER);
		scroller.setFitToWidth(true);
		scroller.setBorder(Border.EMPTY);
		scroller.setPrefHeight(100);
		scroller.setStyle("-fx-background-color:transparent;");

		return scroller;
	}

	
	/**
	 * Method will be used to create bottom panel
	 * 
	 * @return
	 */
	private MigPane createBottomPane() {
		totalMetricValue = UiFactory.createTextField("totalMetricValue", null, TextFieldState.READ_ONLY);
		addButton = createBtn(QiConstant.ADD, getController());
		addButton.setPrefHeight(35);
		addButton.setPrefWidth(120);
		addButton.setStyle("-fx-font-size: 12pt; -fx-font-weight: bold;");
		
		if (isFullAccess()) {
			addButton.setDisable(false);
		} else {
			addButton.setDisable(true);
		}
		
		MigPane bottomContainer = new MigPane();
		bottomContainer.add(totalMetricValue, "split 2, align right");
		bottomContainer.add(addButton, "wrap");
		bottomContainer.add(createTargetTablePane());
		return bottomContainer;
	}

	/**
	 * Method will be used to render target table
	 * 
	 * @return
	 */
	private ObjectTablePane<QiReportingTarget> createTargetTablePane() {

		ColumnMappingList columnMappingList = ColumnMappingList.with("#", "$rowid")
				.put("Effective Date", "effectiveDate").put("Site", "site").put("Plant", "plant")
				.put("Department", "department")
				.put("Product Type", "productType").put("Model Group", "modelGroup")
				.put("Model Yr Desc", "modelYearDescription").put("Demand Type", "demandType")
				.put("Metric", "metricName").put("Target", "target").put("Target Item", "targetItem")
				.put("Value", "metricValue").put("Calcuated Value", "calculatedMetricValue");

		Double[] columnWidth = new Double[] { 0.04, 0.06, 0.05, 0.08, 0.08, 0.06, 0.05, 0.07, 0.07, 0.10, 0.10, 0.08, 0.06, 0.10 };
		reportingTargetTable = new ObjectTablePane<QiReportingTarget>(columnMappingList, columnWidth);

		reportingTargetTable.setMaxHeight(getScreenHeight() * .32);
		reportingTargetTable.setPadding(new Insets(0, 2, 0, 10));
		addEffectiveDateCellValueFactory();
		
		return reportingTargetTable;
	}
	
	/**
	 * This method will apply formatting on effective date column.
	 */
	@SuppressWarnings("unchecked")
	private void addEffectiveDateCellValueFactory() {
		TableColumn<QiReportingTarget, String> dateColumn = (TableColumn<QiReportingTarget, String>) reportingTargetTable.getTable().getColumns().get(1);
		final SimpleDateFormat myDateFormatter = new SimpleDateFormat(DATE_FORMAT);
		if (dateColumn.getText().equalsIgnoreCase("Effective Date")) {
			dateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<QiReportingTarget, String>, ObservableValue<String>>() {
						@Override
						public ObservableValue<String> call(CellDataFeatures<QiReportingTarget, String> param) {
							SimpleStringProperty property = new SimpleStringProperty();
							property.setValue(myDateFormatter.format(param.getValue().getEffectiveDate()));
							return property;
						}
					});
		}
	}
	
	/**
	 * Method will be used to create label object.
	 * 
	 * @param id
	 * @param text
	 * @param fontStyle
	 * @param alignment
	 * @return
	 */
	private LoggedLabel createLoggedLabel(String id, String text, String fontStyle, TextAlignment alignment) {
		LoggedLabel loggedLabel = UiFactory.createLabel(id, text, fontStyle, alignment);
		loggedLabel.setFont(new Font(2));
		return loggedLabel;
	}

	/**
	 * Method will be used to enable/disable responsible levels radio buttons.
	 * 
	 * @param disableFlag
	 */
	public void enableDisableResponsibleRadioButtons(boolean disableFlag) {
		responsibleLevel1RadioButton.setDisable(disableFlag);
		responsibleLevel2RadioButton.setDisable(disableFlag);
		responsibleLevel3RadioButton.setDisable(disableFlag);
	}
	
	/**
	 * This method will be used to create Labeled Combo Box
	 * 
	 * @param id
	 * @param labelName
	 * @param isHorizontal
	 * @param isMandatory
	 * @param isDisabled
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public LabeledComboBox createLabeledComboBox(String id, String labelName, boolean isHorizontal, boolean isMandatory, boolean isDisabled) {
		LabeledComboBox comboBox = new LabeledComboBox(labelName,isHorizontal,new Insets(10),true,isMandatory);
		comboBox.setId(id);
		comboBox.getControl().setMinHeight(35);
		comboBox.getControl().getStyleClass().add("combo-box-base");
		comboBox.getControl().setDisable(isDisabled);
		return comboBox;
	}
	
	@Override
	public void onTabSelected() {
		getController().clearMessage();
		getController().refreshBtnAction(); 
	}

	public void handleDeptRespRadioButtonSelection(boolean isVisible) {
		getDepartmentComboBox().setVisible(isVisible);
		getResponsibleLevel1RadioButton().setVisible(isVisible);
		getResponsibleLevel2RadioButton().setVisible(isVisible);
		getResponsibleLevel3RadioButton().setVisible(isVisible);
		if(isVisible)
			dynamicContainer.getChildren().clear();
		
	}

	@Override
	public String getScreenName() {
		return "Target";
	}

	@Override
	public void reload() {

	}

	public LabeledTextField getSiteTextField() {
		return siteTextField;
	}

	public void setSiteTextField(LabeledTextField siteTextField) {
		this.siteTextField = siteTextField;
	}

	public LabeledComboBox<String> getPlantComboBox() {
		return plantComboBox;
	}

	public void setPlantComboBox(LabeledComboBox<String> plantComboBox) {
		this.plantComboBox = plantComboBox;
	}

	public LabeledComboBox<String> getProductTypeComboBox() {
		return productTypeComboBox;
	}

	public void setProductTypeComboBox(LabeledComboBox<String> productTypeComboBox) {
		this.productTypeComboBox = productTypeComboBox;
	}

	public LabeledComboBox<String> getModelComboBox() {
		return modelGroupComboBox;
	}

	public void setModelComboBox(LabeledComboBox<String> modelComboBox) {
		this.modelGroupComboBox = modelComboBox;
	}

	public LabeledComboBox<String> getModelYearComboBox() {
		return modelYearComboBox;
	}

	public void setModelYearComboBox(LabeledComboBox<String> modelYearComboBox) {
		this.modelYearComboBox = modelYearComboBox;
	}

	public LabeledComboBox<String> getDemandTypeComboBox() {
		return demandTypeComboBox;
	}

	public void setDemandTypeComboBox(LabeledComboBox<String> demandTypeComboBox) {
		this.demandTypeComboBox = demandTypeComboBox;
	}

	public LabeledComboBox<String> getDepartmentComboBox() {
		return departmentComboBox;
	}

	public void setDepartmentComboBox(LabeledComboBox<String> departmentComboBox) {
		this.departmentComboBox = departmentComboBox;
	}

	public LabeledComboBox<QiReportingMetric> getMetricComboBox() {
		return metricComboBox;
	}

	public ToggleGroup getRadioToggleGroup() {
		return radioToggleGroup;
	}
	
	public ToggleGroup getRespLevelRadioToggleGroup() {
		return respLevelRadioToggleGroup;
	}

	public LoggedRadioButton getPlantRadioButton() {
		return plantRadioButton;
	}

	public LoggedRadioButton getDepartmentRadioButton() {
		return departmentRadioButton;
	}

	public LoggedRadioButton getThemeRadioButton() {
		return themeRadioButton;
	}

	public LoggedRadioButton getTempTrackingRadioButton() {
		return tempTrackingRadioButton;
	}

	public LoggedRadioButton getResponsibleLevel1RadioButton() {
		return responsibleLevel1RadioButton;
	}

	public LoggedRadioButton getResponsibleLevel2RadioButton() {
		return responsibleLevel2RadioButton;
	}

	public LoggedRadioButton getResponsibleLevel3RadioButton() {
		return responsibleLevel3RadioButton;
	}

	public LoggedButton getAddButton() {
		return addButton;
	}

	public LoggedTextField getTotalMetricValue() {
		return totalMetricValue;
	}

	public GridPane getDateRangePane() {
		return dateRangePane;
	}

	public HBox getDynamicContainer() {
		return dynamicContainer;
	}

	public ObjectTablePane<QiReportingTarget> getReportingTargetTable() {
		return reportingTargetTable;
	}

	public DatePicker getStartDatePicker() {
		return startDatePicker;
	}

	public DatePicker getEndDatePicker() {
		return endDatePicker;
	}

	private double getScreenHeight() {
		return Screen.getPrimary().getVisualBounds().getHeight();
	}

	public LoggedMenuItem getCreateMenuItem() {
		return createMenuItem;
	}

	public LoggedMenuItem getUpdateMenuItem() {
		return updateMenuItem;
	}

	public LoggedMenuItem getDeleteMenuItem() {
		return deleteMenuItem;
	}

	public LoggedButton getDeleteButton() {
		return deleteButton;
	}

	public LoggedButton getQueryButton() {
		return queryButton;
	}

	public LoggedRadioButton getDeptRespLevelRadioButton() {
		return deptRespLevelRadioButton;
	}
	
}
