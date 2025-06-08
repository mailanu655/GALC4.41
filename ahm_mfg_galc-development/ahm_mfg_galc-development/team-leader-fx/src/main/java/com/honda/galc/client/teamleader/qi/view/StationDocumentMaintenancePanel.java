package com.honda.galc.client.teamleader.qi.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.StationDocumentMaintenanceController;
import com.honda.galc.client.teamleader.qi.model.StationDocumentMaintenanceModel;
import com.honda.galc.client.teamleader.qi.stationconfig.dto.ComboBoxDisplayDto;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.qi.QiDocument;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 * 
 * <h3>StationDocumentMaintenancePanel Class description</h3>
 * <p>
 * StationDocumentMaintenancePanel description
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
 * @author Justin Jiang<br>
 *         February 20, 2020
 *
 */

public class StationDocumentMaintenancePanel
		extends QiAbstractTabbedView<StationDocumentMaintenanceModel, StationDocumentMaintenanceController> {

	private LoggedButton saveBtn;
	private LoggedButton leftShiftBtn;
	private LoggedButton rightShiftBtn;
	private LoggedButton refreshBtn;
	private LoggedLabel siteValueLabel;
	private LoggedTextField documentFilterTextField;
	private ObjectTablePane<QiDocument> availableDocumentTablePane;
	private ObjectTablePane<QiDocument> assignedDocumentTablePane;
	private LoggedComboBox<ComboBoxDisplayDto> divisionComboBox;
	private LoggedComboBox<String> plantComboBox;
	private ListView<String> qicsStationListView;

	public StationDocumentMaintenancePanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);
		leftShiftBtn.setDisable(true);
		rightShiftBtn.setDisable(true);
		saveBtn.setDisable(true);
	}

	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		VBox outerPane = new VBox();
		HBox outerContainer = new HBox();
		outerContainer.setAlignment(Pos.CENTER_LEFT);
		outerContainer.setPadding(new Insets(10, 10, 10, 10));
		HBox outerLabelContainer = new HBox();
		outerLabelContainer.setAlignment(Pos.CENTER_LEFT);
		outerLabelContainer.setPadding(new Insets(10, 10, 10, 10));
		outerLabelContainer.setSpacing(100);
		outerPane.setSpacing(10);
		HBox siteOuterContainer = createSiteOuterContainer();
		HBox plantOuterContainer = createPlantOuterContainer();
		HBox divisionOuterContainer = createDivisionOuterContainer();
		HBox qicsStationOuterContainer = createStationOuterContainer();
		outerLabelContainer.getChildren().addAll(siteOuterContainer, plantOuterContainer, divisionOuterContainer);
		outerLabelContainer.setPadding(new Insets(15, 0, 0, 30));
		outerLabelContainer.setSpacing(125);
		HBox.setHgrow(outerLabelContainer, Priority.ALWAYS);
		refreshBtn = createBtn("Refresh", getController());
		outerContainer.getChildren().addAll(outerLabelContainer, refreshBtn);
		HBox availableDocument = createAvailableDocument();
		HBox buttonContainer = saveBtnContainer();
		outerPane.getChildren().addAll(outerContainer, qicsStationOuterContainer, availableDocument, buttonContainer);
		this.setTop(outerPane);
	}

	private HBox createSiteOuterContainer() {
		HBox siteOuterContainer = new HBox();
		HBox siteLabelContainer = new HBox();
		LoggedLabel siteLabel = UiFactory.createLabel("label", "Site");
		siteLabelContainer.getChildren().addAll(siteLabel);
		siteValueLabel = new LoggedLabel();
		siteValueLabel.setText(getDefaultSiteName());
		siteOuterContainer.setSpacing(10);
		siteOuterContainer.setPrefWidth(200);
		siteOuterContainer.getChildren().addAll(siteLabelContainer, siteValueLabel);

		return siteOuterContainer;
	}

	/**
	 * This method is used create plant outer container which contains plant label
	 * and plant combo box.
	 */
	@SuppressWarnings("unchecked")
	private HBox createPlantOuterContainer() {
		HBox plantOuterContainer = new HBox();
		HBox plantLabelContainer = new HBox();
		LoggedLabel plantLabel = UiFactory.createLabel("label", "Plant");
		plantLabelContainer.getChildren().addAll(plantLabel);
		plantComboBox = new LoggedComboBox<String>("plantComboBox");
		plantComboBox.getStyleClass().add("combo-box-base");
		plantComboBox.setMinWidth(200);
		String siteName = siteValueLabel.getText();
		plantComboBox.setPromptText("Select");
		for (Plant plantObj : getModel().findAllBySite(siteName)) {
			plantComboBox.getItems().add(plantObj.getPlantName());
		}
		plantOuterContainer.setSpacing(10);
		plantOuterContainer.setPadding(new Insets(0, 0, 0, -200));
		plantOuterContainer.getChildren().addAll(plantLabelContainer, plantComboBox);
		return plantOuterContainer;
	}

	/**
	 * This method is used create division outer container which contains division
	 * label and division combo box.
	 */
	private HBox createDivisionOuterContainer() {
		HBox divisionOuterContainer = new HBox();
		HBox divisionLabelContainer = new HBox();
		LoggedLabel divisionLabel = UiFactory.createLabel("label", "Division");
		divisionLabelContainer.getChildren().addAll(divisionLabel);
		divisionComboBox = new LoggedComboBox<ComboBoxDisplayDto>("divisionComboBox");
		divisionComboBox.getStyleClass().add("combo-box-base");
		divisionComboBox.setMinWidth(200);
		divisionComboBox.setPromptText("Select");

		divisionOuterContainer.setSpacing(10);
		divisionOuterContainer.setPadding(new Insets(0, 0, 0, -75));
		divisionOuterContainer.getChildren().addAll(divisionLabelContainer, divisionComboBox);
		return divisionOuterContainer;
	}

	/**
	 * This method is used create Qics station outer container which contains Qics
	 * station label and Qics station list view.
	 */
	private HBox createStationOuterContainer() {
		HBox qicsStationOuterContainer = new HBox();
		HBox qicsStationLabelContainer = new HBox();
		LoggedLabel qicsStationLabel = UiFactory.createLabel("label", "QICS Station");
		LoggedLabel qicsStationAsterisk = UiFactory.createLabel("label", "*");
		qicsStationAsterisk.setStyle("-fx-text-fill: red");
		qicsStationLabelContainer.getChildren().addAll(qicsStationLabel, qicsStationAsterisk);
		qicsStationListView = new ListView<String>();
		qicsStationListView.setPrefWidth(530);
		qicsStationListView.setPrefHeight(90);
		qicsStationOuterContainer.getChildren().addAll(qicsStationLabelContainer, qicsStationListView);
		qicsStationOuterContainer.setSpacing(10);
		qicsStationOuterContainer.setPadding(new Insets(0, 0, 0, 30));
		return qicsStationOuterContainer;
	}

	private HBox createAvailableDocument() {
		HBox availableDocument = new HBox();
		availableDocument.setSpacing(10);
		VBox leftRightShiftContainer = new VBox();
		leftRightShiftContainer.setSpacing(10);
		leftShiftBtn = createBtn("<", getController());
		leftShiftBtn.setId("leftShiftBtn");
		rightShiftBtn = createBtn(">", getController());
		leftRightShiftContainer.setAlignment(Pos.CENTER);
		rightShiftBtn.setId("rightShiftBtn");
		availableDocument.setPadding(new Insets(0, 0, 0, 30));
		leftRightShiftContainer.getChildren().addAll(leftShiftBtn, rightShiftBtn);
		availableDocument.getChildren().add(createTitiledPane("Available Documents", createAvailableDocumentPanel()));
		availableDocument.getChildren().add(leftRightShiftContainer);
		availableDocument.getChildren()
				.add(createTitiledPane("Assigned Station Documents", createAssignedDocumentPanel()));
		return availableDocument;
	}

	/**
	 * This method is used create button.
	 */
	private HBox saveBtnContainer() {
		HBox buttonContainer = new HBox();
		buttonContainer.setSpacing(10);
		saveBtn = createBtn("Save", getController());
		buttonContainer.setAlignment(Pos.BOTTOM_RIGHT);
		buttonContainer.setPadding(new Insets(0, 40, 0, 0));
		buttonContainer.getChildren().addAll(saveBtn);
		if (!isFullAccess()) {
			saveBtn.setDisable(true);
		}
		return buttonContainer;
	}

	private TitledPane createTitiledPane(String title, Node content) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(content);
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		double titlePaneWidth = 0.45 * (primaryScreenBounds.getWidth());
		titledPane.setPrefSize(titlePaneWidth, 350);
		return titledPane;
	}

	private MigPane createAvailableDocumentPanel() {
		MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");
		HBox filterLabelBox = new HBox();
		Label methodFilterLabel = UiFactory.createLabel("label", "Filter");
		methodFilterLabel.getStyleClass().add("display-label");

		documentFilterTextField = createLoggedFilterTextField("filter-textfield", 12, getController());
		filterLabelBox.setSpacing(05);
		filterLabelBox.setAlignment(Pos.BASELINE_RIGHT);
		filterLabelBox.getChildren().addAll(methodFilterLabel, documentFilterTextField);
		availableDocumentTablePane = createAvailableDocumentPane();
		availableDocumentTablePane.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		LoggedTableColumn<QiDocument, Integer> column = new LoggedTableColumn<QiDocument, Integer>();

		createSerialNumber(column);
		availableDocumentTablePane.getTable().getColumns().add(0, column);
		availableDocumentTablePane.getTable().getColumns().get(0).setText("#");
		availableDocumentTablePane.getTable().getColumns().get(0).setResizable(true);
		availableDocumentTablePane.getTable().getColumns().get(0).setMaxWidth(100);
		availableDocumentTablePane.getTable().getColumns().get(0).setMinWidth(20);

		pane.add(filterLabelBox, "span,wrap");
		pane.add(availableDocumentTablePane, "span,wrap");
		pane.setId("mig-pane");
		return pane;
	}

	private MigPane createAssignedDocumentPanel() {
		MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");
		assignedDocumentTablePane = createAssignedDocumentPane();
		assignedDocumentTablePane.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		LoggedTableColumn<QiDocument, Integer> column = new LoggedTableColumn<QiDocument, Integer>();

		createSerialNumber(column);
		assignedDocumentTablePane.getTable().getColumns().add(0, column);
		assignedDocumentTablePane.getTable().getColumns().get(0).setText("#");
		assignedDocumentTablePane.getTable().getColumns().get(0).setResizable(true);
		assignedDocumentTablePane.getTable().getColumns().get(0).setMaxWidth(100);
		assignedDocumentTablePane.getTable().getColumns().get(0).setMinWidth(20);

		pane.add(assignedDocumentTablePane, "span,wrap");
		pane.setId("mig-pane");
		return pane;
	}

	private ObjectTablePane<QiDocument> createAvailableDocumentPane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Document Name", "documentName")
				.put("Document Link", "documentLink");
		ObjectTablePane<QiDocument> panel = new ObjectTablePane<QiDocument>(columnMappingList);
		panel.setStyle("-fx-padding: 5px 0px 5px 0px; -fx-min-height: 125px");
		panel.setPrefWidth(0.47 * Screen.getPrimary().getVisualBounds().getWidth());
		panel.setConstrainedResize(false);
		panel.getTable().getColumns().get(0).setPrefWidth(panel.getPrefWidth() * 0.3);
		panel.getTable().getColumns().get(1).setPrefWidth(panel.getPrefWidth() * 0.53);
		return panel;
	}

	private ObjectTablePane<QiDocument> createAssignedDocumentPane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Document Name", "documentName")
				.put("Document Link", "documentLink");
		ObjectTablePane<QiDocument> panel = new ObjectTablePane<QiDocument>(columnMappingList);
		panel.setStyle("-fx-padding: 5px 0px 5px 0px; -fx-min-height: 125px");
		panel.setPrefWidth(0.47 * Screen.getPrimary().getVisualBounds().getWidth());
		panel.setConstrainedResize(false);
		panel.getTable().getColumns().get(0).setPrefWidth(panel.getPrefWidth() * 0.3);
		panel.getTable().getColumns().get(1).setPrefWidth(panel.getPrefWidth() * 0.53);
		return panel;
	}

	public void onTabSelected() {
		List<QiDocument> selectedDocumentList = new ArrayList<QiDocument>();
		selectedDocumentList.addAll(getAvailableDocumentTablePane().getTable().getSelectionModel().getSelectedItems());
		reload(StringUtils.trim(documentFilterTextField.getText()));
		getController().refreshBtnAction(selectedDocumentList);
		getController().clearDisplayMessage();
	}

	public String getScreenName() {
		return "Station Document Maintenance";
	}

	@Override
	public void reload() {
		availableDocumentTablePane.setData(getModel().findDocumentByFilter(""));
	}

	@SuppressWarnings("unchecked")
	public void reload(String filter) {
		List<QiDocument> assignedDocumentList;
		List<QiDocument> documentByFilterList;
		assignedDocumentList = getAssignedDocumentTablePane().getTable().getItems();
		documentByFilterList = getModel().findDocumentByFilter(filter);
		if (assignedDocumentList.size() != 0) {
			List<QiDocument> subtractedAllDocumentsList = ListUtils.subtract(documentByFilterList,
					assignedDocumentList);
			availableDocumentTablePane.setData(subtractedAllDocumentsList);
		} else if (assignedDocumentList.size() == 0) {
			availableDocumentTablePane.setData(documentByFilterList);
		}
	}

	@Override
	public void start() {
	}

	private String getDefaultSiteName() {
		return getModel().findSiteName();
	}

	public LoggedComboBox<String> getPlantComboBox() {
		return plantComboBox;
	}

	public LoggedButton getSaveBtn() {
		return saveBtn;
	}

	public LoggedButton getLeftShiftBtn() {
		return leftShiftBtn;
	}

	public LoggedButton getRightShiftBtn() {
		return rightShiftBtn;
	}

	public LoggedTextField getDocumentFilterTextField() {
		return documentFilterTextField;
	}

	public ObjectTablePane<QiDocument> getAvailableDocumentTablePane() {
		return availableDocumentTablePane;
	}

	public ObjectTablePane<QiDocument> getAssignedDocumentTablePane() {
		return assignedDocumentTablePane;
	}

	public LoggedComboBox<ComboBoxDisplayDto> getDivisionComboBox() {
		return divisionComboBox;
	}

	public String getDivisionComboBoxSelectedId() {
		ComboBoxDisplayDto dto = (ComboBoxDisplayDto) getDivisionComboBox().getValue();
		String divisionId = "";
		if (dto != null) {
			divisionId = dto.getId();
		}
		return divisionId;
	}

	public void clearDivisionComboBox() {
		getDivisionComboBox().getItems().clear();
	}

	public ComboBoxDisplayDto getDivisionComboBoxSelectedItem() {
		ComboBoxDisplayDto dto = (ComboBoxDisplayDto) getDivisionComboBox().getSelectionModel().getSelectedItem();
		return dto;
	}

	public ListView<String> getStationListView() {
		return qicsStationListView;
	}

	public LoggedLabel getSiteValueLabel() {
		return siteValueLabel;
	}

	public LoggedButton getRefreshBtn() {
		return refreshBtn;
	}
}
