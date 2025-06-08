package com.honda.galc.client.teamleader.mtctomodelgroup;

import java.net.URL;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.mvc.AbstractTabbedView;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.MtcToModelGroupDto;
import com.honda.galc.entity.product.ModelGroup;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>MtcToModelGroupPanel</code> is the Panel class for Mtc model to Model Group
 * Assignment Screen.
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
 * <TD>31/03/2017</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * </TR>
 * </TABLE>
 */
public class MtcToModelGroupPanel extends AbstractTabbedView<MtcToModelGroupMaintenanceModel, MtcToModelGroupController> {

	private LabeledUpperCaseTextField mtcModelNameFilterTextField;
	private LabeledComboBox<String> productTypeComboBox;
	private LabeledComboBox<String> systemComboBox;
	private LoggedLabel siteNameContextLabel;
	private LoggedButton btnSave;
	private LoggedButton btnRefresh;
	private LoggedButton assignArrowBtn;
	private LoggedButton deassignArrowBtn;
	protected ObjectTablePane<ModelGroup> modelGroupTablePane;
	protected ObjectTablePane<MtcToModelGroupDto> availableMtcModelgroupingTablePane;
	protected ObjectTablePane<MtcToModelGroupDto> assignedMtcModelgroupingTablePane;
	private double  screenWidth;
	private double  screenHeight;
	private LoggedRadioButton allRadioBtn;
	private LoggedRadioButton activeRadioBtn;
	private LoggedRadioButton inactiveRadioBtn;

	protected final static String CREATE = "Create";
	protected final static String UPDATE = "Update";
	protected final static String REACTIVATE = "Reactivate";
	protected final static String INACTIVATE = "Inactivate";
	protected final static String CANCEL = "Cancel";
	protected final static String ALL = "All";
	protected final static String ACTIVE = "Active";
	protected final static String INACTIVE = "Inactive";
	protected final static String SAVE = "Save";
	protected final static String DELETE = "Delete";
	protected final static String REFRESH = "Refresh";
	protected final static String ASSIGN = "Assign"; 
	protected final static String DEASSIGN = "Deassign"; 
	protected final static String ASSOCIATE = "Associate";
	protected final static String CSS_PATH = "/resource/css/QiMainCss.css";



	public MtcToModelGroupPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);

	}

	/**
	 * This method is used to initialize the components of panel
	 */
	public void initView() {
		getMainWindow().getStylesheets().add(CSS_PATH);
		this.screenWidth=Screen.getPrimary().getVisualBounds().getWidth();
		this.screenHeight=Screen.getPrimary().getVisualBounds().getHeight();
		HBox mainPane = new HBox();
		mainPane.getChildren().addAll(getLeftPanel(), getRightPanel());
		this.setCenter(mainPane);
	}

	/**
	 * Get Left panel
	 */
	private ScrollPane getLeftPanel() {
		MigPane leftPane = new MigPane("insets 0 0 0 0", "[left,grow][shrink 0]", "[]20[shrink 0]");
		leftPane.add(getReportingUsageLabel(),"left, span, wrap, gapleft 33, gaptop 30"); 
		leftPane.add(getSiteNameLoggedLabel(),"split 2, span 2,left,gapleft 33");
		leftPane.add(getSiteNameContextLabel(),"left, span, wrap,gapleft 45");
		leftPane.add(getProductTypeComboBox(),"left, span, wrap,gapleft 33");
		leftPane.add(getSystemComboBox(),"left, span, wrap, gapleft 33");
		leftPane.add(getRadioButtonTableContainer());
		ScrollPane scroll = new ScrollPane();
		scroll.setFitToWidth(true);
		scroll.setContent(leftPane);
		scroll.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		return scroll;
	}

	/**
	 * Get Model Group Table with Radio Button
	 */

	private VBox getRadioButtonTableContainer() {
		VBox radioBtnTblContainer = new VBox();
		HBox filterRadioBtnContainer = new HBox();
		filterRadioBtnContainer.getChildren().add(createFilterRadioButtons(getController(), screenWidth/1.6));
		filterRadioBtnContainer.setPadding(new Insets(0, 0, 0,20));
		radioBtnTblContainer.getChildren().add(filterRadioBtnContainer);
		radioBtnTblContainer.getChildren().add(getModelGroupTablePanel());
		radioBtnTblContainer.setPadding(new Insets(30, 0, 0, 0));
		return radioBtnTblContainer;
	}

	/**
	 * Get Model Group table
	 */
	private ObjectTablePane<ModelGroup> getModelGroupTablePanel() {
		modelGroupTablePane = createModelGroupTablePane();
		modelGroupTablePane.setPadding(new Insets(5, 0, 50, 30));
		modelGroupTablePane.setPrefWidth(screenWidth/1.6);
		modelGroupTablePane.setPrefHeight(screenHeight/1.87);
		return modelGroupTablePane;
	}

	/**
	 * Get Product Type ComboBox
	 */
	private LabeledComboBox<String>  getProductTypeComboBox() {
		List<String> productTypeList = getModel().getProductTypes();
		productTypeComboBox = createLabeledComboBox("productType", "Product Type", true, false);
		productTypeComboBox.getControl().getItems().addAll(productTypeList);
		if (productTypeList.size() == 1) {
			productTypeComboBox.getControl().setValue(productTypeList.get(0));
		}
		return productTypeComboBox;
	}

	/**
	 * Get System ComboBox
	 */
	private LabeledComboBox<String> getSystemComboBox() {
		List<String> systemList = getModel().getSystems();
		systemComboBox = createLabeledComboBox("system", "System", true, false);
		systemComboBox.getControl().getItems().addAll(systemList);
		if (systemList.size() == 1) {
			systemComboBox.getControl().setValue(systemList.get(0));
		}
		return systemComboBox;
	}

	/**
	 * This method is used to create LabeledComboBox
	 * @param id
	 * @return
	 */
	public LabeledComboBox<String> createLabeledComboBox(String id, String labelName, boolean isHorizontal, boolean isMandatory) {
		LabeledComboBox<String> comboBox = new LabeledComboBox<String>(labelName,isHorizontal,new Insets(0),true,isMandatory);
		comboBox.setId(id);
		comboBox.getControl().setMinHeight(25);
		comboBox.getControl().getStyleClass().add("combo-box-base");
		comboBox.getControl().setPrefSize(5,5);
		return comboBox;
	}

	/**
	 * Get Site Name
	 */
	private LoggedLabel getReportingUsageLabel() {
		LoggedLabel reportingUsageLabel = UiFactory.createLabel("reportingUsage", "*Reporting Usage");
		reportingUsageLabel.getStyleClass().add("display-label-14");
		return reportingUsageLabel;
	}	

	/**
	 * Get Site Name label 
	 */
	private LoggedLabel getSiteNameLoggedLabel() {
		LoggedLabel siteNameLabel = UiFactory.createLabel("siteNameLabel", "Site");
		siteNameLabel.getStyleClass().add("display-label-14");
		return siteNameLabel;
	}


	/**
	 * Get Site Name Context Label
	 */
	private LoggedLabel getSiteNameContextLabel() {
		siteNameContextLabel = UiFactory.createLabel("siteNameContextLabel", getModel().getSiteName());
		siteNameContextLabel.setPadding(new Insets(0,0,0,10));
		return siteNameContextLabel;
	}

	/**
	 * Get Right Panel
	 */

	private ScrollPane getRightPanel() {
		MigPane rightPane = new MigPane("insets 0 0 0 0", "[left,grow][shrink 0]", "[]10[shrink 0]");
		btnRefresh = createButton(REFRESH);	
		rightPane.add(btnRefresh,"right, span, wrap, gaptop 5, gapright 5");
		rightPane.add(createTitiledPane("Available MTC Models", createAvailableModelPanel()),"left, span, wrap, gapleft 15, gapright 5");
		rightPane.add(getAssignButton(),"split 2, span 2, center");
		rightPane.add(getDeassignButton(),"span, wrap, gapleft 30");
		rightPane.add(createTitiledPane("Assigned MTC Models", createAssignModelPanel()),"left, span, wrap, gapleft 15, gapright 5");
		rightPane.add(getSaveButton(),"right");
		
		ScrollPane scroll = new ScrollPane();
		scroll.setContent(rightPane);
		scroll.setFitToWidth(true);
		scroll.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		return scroll;
	}

	/**
	 * Get Save Button
	 */
	private LoggedButton  getSaveButton() {
		btnSave = createButton(SAVE);	
		if (!getController().isFullAccess()) {
			btnSave.setDisable(true);
		}
		return btnSave;
	}

	/**
	 * Get Assign Button
	 */
	private LoggedButton  getAssignButton() {
		URL assignArrowUrl = getClass().getResource("/resource/com/honda/galc/client/images/qi/down.png");
		assignArrowBtn = createBtn(assignArrowUrl, "Clik to push selected rows down", ASSIGN);
		return assignArrowBtn;
	}
	/**
	 * Get Deassign Buttons
	 */
	private LoggedButton  getDeassignButton() {
		URL deassignArrowUrl = getClass().getResource("/resource/com/honda/galc/client/images/qi/up.png");
		deassignArrowBtn = createBtn(deassignArrowUrl, "Clik to push selected rows up", DEASSIGN);
		return deassignArrowBtn;
	}

	/**
	 * Get MigPane for Available Mtc Model Table
	 */
	private MigPane createAvailableModelPanel() {
		MigPane pane = new MigPane("insets 0 0 0 0", "[left,grow][shrink 0]", "[top,grow][shrink 0]");
		LoggedLabel mtcModelNameFilterLabel = UiFactory.createLabel("mtcModelNameFilterLabel", "Filter");
		mtcModelNameFilterLabel.getStyleClass().add("display-label");
		mtcModelNameFilterTextField = new LabeledUpperCaseTextField("Filter", "Filter", 20, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT, false,new Insets(5,5,5,30));
		mtcModelNameFilterTextField.getLabel().setAlignment(Pos.BASELINE_LEFT);
		mtcModelNameFilterTextField.setPrefHeight(30);
		mtcModelNameFilterTextField.setMinHeight(30);
		mtcModelNameFilterTextField.getControl().setOnAction(getController());
		pane.add(mtcModelNameFilterTextField, "span,wrap,right");
		availableMtcModelgroupingTablePane = createAvailableMtcModelTablePane();
		pane.add(availableMtcModelgroupingTablePane, "span,wrap");
		pane.setId("avlModelgroupingTablePane");
		pane.getStyleClass().add("mig-pane");
		return pane;
	}

	/**
	 * Get MigPane for Assigned Mtc Model Table
	 */
	private MigPane createAssignModelPanel() {
		MigPane pane = new MigPane("insets 0 0 0 0", "[left,grow][shrink 0]", "[top,grow][shrink 0]");
		assignedMtcModelgroupingTablePane = createAssgnModelTablePane();
		assignedMtcModelgroupingTablePane.setPrefWidth(screenWidth/1.6);
		pane.add(assignedMtcModelgroupingTablePane, "span,wrap");
		pane.setId("assgnModelgroupingTablePane");
		pane.getStyleClass().add("mig-pane");
		return pane;
	}

	/**
	 * Get Titled pane for MigPane
	 */
	private TitledPane createTitiledPane(String title, Node migPane) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(migPane);
		titledPane.setPrefSize(screenWidth/1.66, screenHeight/3.31);
		titledPane.setPadding(new Insets(0));
		return titledPane;
	}

	/**
	 * Get Available Mtc Model Table
	 */
	private ObjectTablePane<MtcToModelGroupDto> createAvailableMtcModelTablePane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("MTC Model", "AvailablelMtcModels").put("Model Year", "ModelYearDescription").put("Description", "modelDescription");
		Double[] columnWidth = new Double[] { 0.095, 0.095, 0.19 };
		ObjectTablePane<MtcToModelGroupDto> panel = new ObjectTablePane<MtcToModelGroupDto>(columnMappingList, columnWidth);
		panel.getTable().setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		panel.setPrefHeight(screenHeight/4.55);
		panel.setPrefWidth(screenWidth/1.64);
		panel.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		LoggedTableColumn<MtcToModelGroupDto, Integer> serialNoColAvailableMtcModelTable = new LoggedTableColumn<MtcToModelGroupDto, Integer>();
		createSerialNumber(serialNoColAvailableMtcModelTable);
		panel.getTable().getColumns().add(0, serialNoColAvailableMtcModelTable);
		panel.getTable().getColumns().get(0).setText("#");
		panel.getTable().getColumns().get(0).setResizable(true);
		panel.getTable().getColumns().get(0).setPrefWidth(20);
		panel.setPadding(new Insets(0));
		panel.setConstrainedResize(false);
		return panel;
	}

	/**
	 * GetModel Group Table
	 */
	private ObjectTablePane<ModelGroup> createModelGroupTablePane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Model Group", "id.ModelGroup").put("Description", "ModelGroupDescription").put("Status", "Status");
		Double[] columnWidth = new Double[] { 0.12, 0.229, 0.100 };
		ObjectTablePane<ModelGroup> panel = new ObjectTablePane<ModelGroup>(columnMappingList, columnWidth);
		LoggedTableColumn<ModelGroup, Integer> serialNoColModelGroup = new LoggedTableColumn<ModelGroup, Integer>();
		createSerialNumber(serialNoColModelGroup);
		panel.getTable().getColumns().add(0, serialNoColModelGroup);
		panel.getTable().getColumns().get(0).setText("#");
		panel.getTable().getColumns().get(0).setResizable(true);
		panel.getTable().getColumns().get(0).setPrefWidth(20);
		panel.setConstrainedResize(false);
		return panel;
	}

	/**
	 * Get Assigned Mtc Model Table
	 */
	private ObjectTablePane<MtcToModelGroupDto> createAssgnModelTablePane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("MTC Model ", "MtcModel").put("Model Year", "ModelYearDescription").put("Description", "modelDescription");
		Double[] columnWidth = new Double[] { 0.1375, 0.1375, 0.275 };
		ObjectTablePane<MtcToModelGroupDto> panel = new ObjectTablePane<MtcToModelGroupDto>(columnMappingList, columnWidth);
		panel.getTable().getColumns().get(0).setPrefWidth(100);
		panel.getTable().setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		panel.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		LoggedTableColumn<MtcToModelGroupDto, Integer> serialNoColAssignedMtcModelTable = new LoggedTableColumn<MtcToModelGroupDto, Integer>();
		createSerialNumber(serialNoColAssignedMtcModelTable);
		panel.getTable().getColumns().add(0, serialNoColAssignedMtcModelTable);
		panel.getTable().getColumns().get(0).setText("#");
		panel.getTable().getColumns().get(0).setResizable(true);
		panel.getTable().getColumns().get(0).setPrefWidth(20);
		panel.setPadding(new Insets(0));
		panel.setMinHeight(screenHeight/3.73);
		panel.setConstrainedResize(false);
		return panel;
	}

	/**
	 * This method refresh the data on tab selection.
	 */
	public void onTabSelected() {
		getController().refreshBtnAction();
		getController().activate();
	}

	/**
	 * Get Screen Name
	 */
	public String getScreenName() {
		return "MTC To Model Group";
	}

	@Override
	public ViewId getViewId() {
		return null;
	}

	@Override
	public void start() {
	}

	/**
	 * This method creates assign deassign buttons
	 */
	private LoggedButton createBtn(URL url, String text, String id) {
		LoggedButton btn = UiFactory.createButton("");
		btn.setId(id);
		btn.setDisable(true);
		btn.setOnAction(getController());
		ImageView imageView = new ImageView();
		Image image = new Image(url.toString());
		imageView.setImage(image);
		imageView.setFitWidth(25);
		imageView.setFitHeight(25);
		btn.setGraphic(imageView);
		Tooltip tooltip = new Tooltip();
		tooltip.setText(text);
		btn.setTooltip(tooltip);
		btn.getStyleClass().add("drawing-tools");
		return btn;
	}


	/**
	 * This method refresh the data on Available Mtc Model table on product type
	 * selection.
	 */
	public void reload() {
		if (getSelectedProductType() != null && getSelectedSystem() != null) {
			getAvailableMtcModelgroupingTablePane().setData(getController().getAvailableMtcModelData(StringUtils.trimToEmpty(getMtcModelNameFilterTextField().getText()),
					StringUtils.trimToEmpty(getSelectedProductType())));
		}
	}
	/**
	 * This method refresh the data in Model Group Table on product type and system selection.
	 */
	public void reloadModelGroup(String productType, String system) {
		if (getSelectedProductType() != null && getSelectedSystem() != null) {
			if (getAllRadioBtn().isSelected()) {
				modelGroupTablePane.setData(getModel().findAllByProductTypeAndSystem(productType, system));
			} else {
				modelGroupTablePane.setData(getModel().findAllByStatusProductTypeAndSystem(getActiveRadioBtn().isSelected() ? ACTIVE : INACTIVE, productType, system));
			}
		}
	}

	/**
	 * this method is used to create button
	 */
	private LoggedButton createButton(String buttonName) {
		LoggedButton button = UiFactory.createButton(buttonName, buttonName);
		button.setOnAction(getController());
		button.getStyleClass().add("main-screen-btn");
		return button;
	}

	public LoggedRadioButton getAllRadioBtn() {
		return allRadioBtn;
	}

	public void setAllRadioBtn(LoggedRadioButton allRadioBtn) {
		this.allRadioBtn = allRadioBtn;
	}

	public LoggedRadioButton getActiveRadioBtn() {
		return activeRadioBtn;
	}

	public void setActiveRadioBtn(LoggedRadioButton activeRadioBtn) {
		this.activeRadioBtn = activeRadioBtn;
	}

	public LoggedRadioButton getInactiveRadioBtn() {
		return inactiveRadioBtn;
	}

	public void setInactiveRadioBtn(LoggedRadioButton inactiveRadioBtn) {
		this.inactiveRadioBtn = inactiveRadioBtn;
	}

	public HBox createFilterRadioButtons(EventHandler<ActionEvent> handler, double width) {
		HBox radioBtnContainer = new HBox();
		ToggleGroup group = new ToggleGroup();
		allRadioBtn = createRadioButton(ALL, group, true, handler);
		activeRadioBtn = createRadioButton(ACTIVE, group, false, handler);
		inactiveRadioBtn = createRadioButton(INACTIVE, group, false, handler);

		radioBtnContainer.getChildren().addAll(allRadioBtn, activeRadioBtn, inactiveRadioBtn);
		radioBtnContainer.setAlignment(Pos.CENTER_LEFT);
		radioBtnContainer.setSpacing(10);
		radioBtnContainer.setPadding(new Insets(0, 0, 0, 10));
		radioBtnContainer.setPrefWidth(width);
		return radioBtnContainer;
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
		return radioButton;
	}


	public LoggedButton getBtnRefresh() {
		return btnRefresh;
	}

	public void setBtnRefresh(LoggedButton btnRefresh) {
		this.btnRefresh = btnRefresh;
	}

	public LabeledUpperCaseTextField getMtcModelNameFilterTextField() {
		return mtcModelNameFilterTextField;
	}

	public LabeledComboBox<String> getProductType() {
		return productTypeComboBox;
	}

	public String getSelectedProductType() {
		return productTypeComboBox.getControl().getSelectionModel().getSelectedItem();
	}

	public LabeledComboBox<String> getSystem() {
		return systemComboBox;
	}

	public String getSelectedSystem() {
		return systemComboBox.getControl().getSelectionModel().getSelectedItem();
	}

	public ObjectTablePane<ModelGroup> getModelGroupTablePane() {
		return modelGroupTablePane;
	}

	public TableView<ModelGroup> getModelGroupTable() {
		return modelGroupTablePane.getTable();
	}

	public ModelGroup getSelectedModelGroup() {
		return modelGroupTablePane.getSelectedItem();
	}

	public void setModelGroupTablePane(ObjectTablePane<ModelGroup> modelGroupTablePane) {
		this.modelGroupTablePane = modelGroupTablePane;
	}

	public ObjectTablePane<MtcToModelGroupDto> getAvailableMtcModelgroupingTablePane() {
		return availableMtcModelgroupingTablePane;
	}

	public TableView<MtcToModelGroupDto> getAvailableMtcModelgroupingTable() {
		return availableMtcModelgroupingTablePane.getTable();
	}

	public MtcToModelGroupDto getSelectedAvailableMtcModelgrouping() {
		return availableMtcModelgroupingTablePane.getSelectedItem();
	}

	public void setassgnModelgroupingTablePane(ObjectTablePane<MtcToModelGroupDto> assignModelgroupingTablePane) {
		this.assignedMtcModelgroupingTablePane = assignModelgroupingTablePane;
	}

	public ObjectTablePane<MtcToModelGroupDto> getAssignedModelGroupingTablePane() {
		return assignedMtcModelgroupingTablePane;
	}

	public TableView<MtcToModelGroupDto> getAssignedModelGroupingTable() {
		return assignedMtcModelgroupingTablePane.getTable();
	}

	public MtcToModelGroupDto getSelectedAssignedModelGrouping() {
		return assignedMtcModelgroupingTablePane.getSelectedItem();
	}

	public LoggedButton getSaveBtn() {
		return btnSave;
	}

	public LoggedButton getAssignBtn() {
		return assignArrowBtn;
	}

	public LoggedButton getDeassignBtn() {
		return deassignArrowBtn;
	}

}
