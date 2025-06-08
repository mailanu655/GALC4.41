package com.honda.galc.client.teamleader.qi.view;

import java.net.URL;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.MtcToEntryModelController;
import com.honda.galc.client.teamleader.qi.model.EntryModelMaintenanceModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.QiMtcToEntryModelDto;
import com.honda.galc.entity.qi.QiEntryModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>MtcToEntryModelPanel</code> is the Panel class for Mtc to Entry Model
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
 * <TD>25/08/2016</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * </TR>
 * </TABLE>
 */
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */

public class MtcToEntryModelPanel extends QiAbstractTabbedView<EntryModelMaintenanceModel, MtcToEntryModelController> {

	private UpperCaseFieldBean mtcModelNameFilterTextField;
	private ComboBox<String> productTypeComboBox;
	private LoggedLabel siteNameContextLabel;
	private LoggedButton btnSave;
	private LoggedButton btnRefresh;
	private LoggedButton assignArrowBtn;
	private LoggedButton deassignArrowBtn;
	protected ObjectTablePane<QiEntryModel> qiEntryModelTablePane;
	protected ObjectTablePane<QiMtcToEntryModelDto> qiAvailableMtcModelgroupingTablePane;
	protected ObjectTablePane<QiMtcToEntryModelDto> qiAssgnedMtcModelgroupingTablePane;

	public UpperCaseFieldBean getMtcModelNameFilterTextField() {
		return mtcModelNameFilterTextField;
	}

	public ComboBox<String> getProductType() {
		return productTypeComboBox;
	}

	public ObjectTablePane<QiEntryModel> getQiEntryModelTablePane() {
		return qiEntryModelTablePane;
	}

	public void setQiEntryModelTablePane(ObjectTablePane<QiEntryModel> qiEntryModelTablePane) {
		this.qiEntryModelTablePane = qiEntryModelTablePane;
	}

	public ObjectTablePane<QiMtcToEntryModelDto> getQiAvailableMtcModelgroupingTablePane() {
		return qiAvailableMtcModelgroupingTablePane;
	}

	public void setQiAssgnModelgroupingTablePane(ObjectTablePane<QiMtcToEntryModelDto> qiAssgnModelgroupingTablePane) {
		this.qiAssgnedMtcModelgroupingTablePane = qiAssgnModelgroupingTablePane;
	}

	public ObjectTablePane<QiMtcToEntryModelDto> getAssignModelGroupingTablePane() {
		return qiAssgnedMtcModelgroupingTablePane;
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

	public MtcToEntryModelPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);

	}

	/**
	 * This method is used to initialize the components of panel
	 */
	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		HBox mainPane = new HBox();
		mainPane.getChildren().addAll(getLeftPanel(), getRightPanel());
		this.setCenter(mainPane);
	}

	/**
	 * Get Left panel
	 */
	private VBox getLeftPanel() {
		VBox leftPane = new VBox();
		leftPane.getChildren().add(getSiteNameContextLabel());
		leftPane.getChildren().add(getProductTypeComboBox());
		leftPane.getChildren().add(getRadioButtonTableContainer());
		leftPane.setAlignment(Pos.CENTER_LEFT);
		return leftPane;
	}

	/**
	 * Get Entry model Table with Radio Button
	 */

	private VBox getRadioButtonTableContainer() {
		VBox radioBtnTblContainer = new VBox();
		HBox filterRadioBtnContainer = new HBox();
		filterRadioBtnContainer.getChildren().add(createFilterRadioButtons(getController(), findResolutionWidth()));
		filterRadioBtnContainer.setPadding(new Insets(0, 0, 0,20));
		radioBtnTblContainer.getChildren().add(filterRadioBtnContainer);
		radioBtnTblContainer.getChildren().add(getEntryModelTablePanel());
		radioBtnTblContainer.setPadding(new Insets(50, 0, 0, 0));
		return radioBtnTblContainer;
	}

	/**
	 * Get Entry model table
	 */
	private ObjectTablePane<QiEntryModel> getEntryModelTablePanel() {
		qiEntryModelTablePane = createQiEntryModelTablePane();
		qiEntryModelTablePane.setId("qiEntryModelTablePane");
		qiEntryModelTablePane.setPadding(new Insets(5, 0, 50, 30));
		qiEntryModelTablePane.setPrefWidth(findResolutionWidth());
		qiEntryModelTablePane.setPrefHeight(390);
		return qiEntryModelTablePane;
	}

	/**
	 * Get Product Type ComboBox
	 */
	private HBox getProductTypeComboBox() {
		HBox product = new HBox();
		LoggedLabel productTypeLabel = UiFactory.createLabel("productType", "Product Type");
		productTypeLabel.getStyleClass().add("display-label-14");
		List<String> productTypeList = getModel().getProductTypes();
		productTypeComboBox = new ComboBox<String>();
		productTypeComboBox.setId("productTypeComboBox");
		productTypeComboBox.getItems().addAll(productTypeList);
		product.setSpacing(20);
		product.setPadding(new Insets(20, 30, 0, 20));
		product.setAlignment(Pos.CENTER_LEFT);
		product.getChildren().addAll(productTypeLabel, productTypeComboBox);
		return product;
	}

	/**
	 * Get Site Name
	 */
	private HBox getSiteNameContextLabel() {
		HBox site = new HBox();
		LoggedLabel siteNameLabel = UiFactory.createLabel("siteNameLabel", "Site");
		siteNameLabel.getStyleClass().add("display-label-14");
		siteNameLabel.setPadding(new Insets(0,0,0,60));
		siteNameContextLabel = UiFactory.createLabel("siteNameContextLabel", getModel().getSiteName());
		siteNameContextLabel.setPadding(new Insets(0,0,0,10));
		site.setSpacing(20);
		site.setPadding(new Insets(20, 0, 0, 20));
		site.setAlignment(Pos.CENTER_LEFT);
		site.getChildren().addAll(siteNameLabel, siteNameContextLabel);
		return site;
	}

	/**
	 * Get Right Panel
	 */

	private VBox getRightPanel() {
		VBox rightPane = new VBox();
		btnRefresh = createBtn(QiConstant.REFRESH,getController());	
		rightPane.getChildren().add(btnRefresh);
		rightPane.getChildren().add(getFilterAvailableMtcModelPanel());
		rightPane.getChildren().add(getAssignDeassignButtonContainer());
		rightPane.getChildren().add(getAssignedMtcModelPanel());
		rightPane.getChildren().add(getSaveButton());
		rightPane.setAlignment(Pos.CENTER_RIGHT);
		rightPane.setPadding(new Insets(0,50,0,20));
		rightPane.setSpacing(10);;
		return rightPane;
	}

	/**
	 * Get Save Button
	 */
	private HBox getSaveButton() {
		HBox save = new HBox();
		btnSave = createBtn(QiConstant.SAVE,getController());	
		save.getChildren().add(btnSave);
		save.setAlignment(Pos.BOTTOM_RIGHT);
		if (!isFullAccess()) {
			btnSave.setDisable(true);
		}
		return save;
	}

	/**
	 * Get Assigned Mtc Model table
	 */
	private VBox getAssignedMtcModelPanel() {

		VBox assignedMtcModelPanel = new VBox();
		assignedMtcModelPanel.getChildren().add(createTitiledPane("Assigned MTC Models", createAssgnModelPanel()));
		return assignedMtcModelPanel;
	}

	/**
	 * Get Assign Deassign Buttons
	 */
	private HBox getAssignDeassignButtonContainer() {
		HBox buttonContainer = new HBox();
		buttonContainer.getChildren().add(getDeassignButton());
		buttonContainer.getChildren().add(getAssignButton());
		buttonContainer.setAlignment(Pos.CENTER);
		return buttonContainer;
	}
	/**
	 * Get Assign Button
	 */
	private HBox getAssignButton() {
		HBox assignArrow = new HBox();
		URL assignArrowUrl = getClass().getResource("/resource/com/honda/galc/client/images/qi/down.png");
		assignArrowBtn = createBtn(assignArrowUrl, "Clik to push selected rows down", QiConstant.ASSIGN);
		assignArrowBtn.setPadding(new Insets(0, 0, 0, 15));
		assignArrow.getChildren().add(assignArrowBtn);
		assignArrow.setPadding(new Insets(0, 0, 0, 15));
		return assignArrow;
	}
	/**
	 * Get Deassign Buttons
	 */
	private HBox getDeassignButton() {
		HBox deassignArrow = new HBox();
		URL deassignArrowUrl = getClass().getResource("/resource/com/honda/galc/client/images/qi/up.png");
		deassignArrowBtn = createBtn(deassignArrowUrl, "Clik to push selected rows up", QiConstant.DEASSIGN);
		deassignArrowBtn.setPadding(new Insets(0, 15, 0, 0));
		deassignArrow.getChildren().add(deassignArrowBtn);
		deassignArrow.setPadding(new Insets(0, 15, 0, 0));
		return deassignArrow;
	}

	/**
	 * Get Table for Available Mtc Model
	 */

	private VBox getFilterAvailableMtcModelPanel() {
		VBox availableMtcModelPanel = new VBox();
		availableMtcModelPanel.getChildren().add(createTitiledPane("Available MTC Models", createAvailableModelPanel()));		
		return availableMtcModelPanel;
	}

	/**
	 * Get MigPane for Available Mtc Model Table
	 */
	private MigPane createAvailableModelPanel() {
		MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");

		HBox filterContainer = new HBox();

		LoggedLabel mtcModelNameFilterLabel = UiFactory.createLabel("mtcModelNameFilterLabel", "Filter");
		mtcModelNameFilterLabel.getStyleClass().add("display-label");
		mtcModelNameFilterTextField = createFilterTextField("mtcModelNameFilterTextField", 25, getController());
		filterContainer.getChildren().addAll(mtcModelNameFilterLabel, mtcModelNameFilterTextField);
		filterContainer.setSpacing(10);
		filterContainer.setAlignment(Pos.BASELINE_RIGHT);
		pane.add(filterContainer, "span,wrap");

		qiAvailableMtcModelgroupingTablePane = createAvailableMtcModelTablePane();
		qiAvailableMtcModelgroupingTablePane.setId("qiAvailableMtcModelgroupingTablePane");


		pane.add(qiAvailableMtcModelgroupingTablePane, "span,wrap");
		pane.setId("avlModelgroupingTablePane");

		pane.getStyleClass().add("mig-pane");

		return pane;
	}

	/**
	 * Get MigPane for Assigned Mtc Model Table
	 */
	private MigPane createAssgnModelPanel() {
		MigPane pane = new MigPane("insets 1 5 10 5", "[center,grow,fill]", "");
		qiAssgnedMtcModelgroupingTablePane = createAssgnModelTablePane();
		qiAssgnedMtcModelgroupingTablePane.setPrefWidth(findResolutionWidth());
		pane.add(qiAssgnedMtcModelgroupingTablePane, "span,wrap");
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
		titledPane.setPrefSize(830, 350);
		return titledPane;
	}

	/**
	 * Get Available Mtc Model Table
	 */
	private ObjectTablePane<QiMtcToEntryModelDto> createAvailableMtcModelTablePane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("MTC Model", "AvailablelMtcModels").put("Model Year", "ModelYearDescription").put("Description", "modelDescription");
		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		Double[] columnWidth = new Double[] { 0.095, 0.095, 0.19 };
		ObjectTablePane<QiMtcToEntryModelDto> panel = new ObjectTablePane<QiMtcToEntryModelDto>(columnMappingList,
				columnWidth);
		panel.setConstrainedResize(false);
		panel.setPrefHeight(300);
		panel.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		LoggedTableColumn<QiMtcToEntryModelDto, Integer> serialNoColAvailableMtcModelTable = new LoggedTableColumn<QiMtcToEntryModelDto, Integer>();
		createSerialNumber(serialNoColAvailableMtcModelTable);
		panel.getTable().getColumns().add(0, serialNoColAvailableMtcModelTable);
		panel.getTable().getColumns().get(0).setText("#");
		panel.getTable().getColumns().get(0).setResizable(true);
		panel.getTable().getColumns().get(0).setMaxWidth(100);
		panel.getTable().getColumns().get(0).setMinWidth(25);
		return panel;
	}

	/**
	 * Get Entry Model Table
	 */
	private ObjectTablePane<QiEntryModel> createQiEntryModelTablePane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Entry Model ", "id.entryModel").put("Is Used","IsUsedVersion")
				.put("Description", "EntryModelDescription").put("Status", "Status");

		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		Double[] columnWidth = new Double[] { 0.10,0.05, 0.200, 0.105 };
		ObjectTablePane<QiEntryModel> panel = new ObjectTablePane<QiEntryModel>(columnMappingList, columnWidth);
		LoggedTableColumn<QiEntryModel, Integer> serialNoColEntryModel = new LoggedTableColumn<QiEntryModel, Integer>();
		createSerialNumber(serialNoColEntryModel);
		panel.getTable().getColumns().add(0, serialNoColEntryModel);
		panel.getTable().getColumns().get(0).setText("#");
		panel.getTable().getColumns().get(0).setResizable(true);
		panel.getTable().getColumns().get(0).setMaxWidth(50);
		panel.getTable().getColumns().get(0).setMinWidth(1);
		panel.setConstrainedResize(false);

		return panel;
	}

	/**
	 * Get Assigned Mtc Model Table
	 */
	private ObjectTablePane<QiMtcToEntryModelDto> createAssgnModelTablePane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("MTC Model ", "MtcModel").put("Model Year", "ModelYearDescription").put("Description", "modelDescription");

		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		Double[] columnWidth = new Double[] {0.095, 0.095, 0.19 };
		ObjectTablePane<QiMtcToEntryModelDto> panel = new ObjectTablePane<QiMtcToEntryModelDto>(columnMappingList,
				columnWidth);
		panel.getTable().getColumns().get(0).setPrefWidth(100);
		panel.setConstrainedResize(false);
		panel.setPrefHeight(300);
		panel.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		LoggedTableColumn<QiMtcToEntryModelDto, Integer> serialNoColAssignedMtcModelTable = new LoggedTableColumn<QiMtcToEntryModelDto, Integer>();
		createSerialNumber(serialNoColAssignedMtcModelTable);
		panel.getTable().getColumns().add(0, serialNoColAssignedMtcModelTable);
		panel.getTable().getColumns().get(0).setText("#");
		panel.getTable().getColumns().get(0).setResizable(true);
		panel.getTable().getColumns().get(0).setMaxWidth(100);
		panel.getTable().getColumns().get(0).setMinWidth(25);
		return panel;
	}

	/**
	 * This method refresh the data on tab selection.
	 */
	public void onTabSelected() {
		getController().refreshBtnAction();
	}

	/**
	 * Get Screen Name
	 */
	public String getScreenName() {
		return "MTC To Entry Model";
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
		if(null!=getProductType().getSelectionModel().getSelectedItem()){
			getQiAvailableMtcModelgroupingTablePane().setData(getController().getAvailableMtcModelData(StringUtils.trimToEmpty(getMtcModelNameFilterTextField().getText()),
					StringUtils.trimToEmpty(productTypeComboBox.getSelectionModel().getSelectedItem())));
		}
	}
	/**
	 * This method refresh the data in Entry Model Table on product type
	 * selection.
	 */
	public void reloadEntrymodel(String productType) {
		if(null!=getProductType().getSelectionModel().getSelectedItem()){
			if(getAllRadioBtn().isSelected()){
				qiEntryModelTablePane.setData(getModel().getEntryModelByProductType(productType));
			}
			else {
				qiEntryModelTablePane.setData(getModel().getEntryModelByStatus(getActiveRadioBtn().isSelected() ? QiConstant.ACTIVE : QiConstant.INACTIVE,productType));
			}
		}
	}
	/**
	 * This method is used find the width
	 */
	private double findResolutionWidth(){
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		double width = screenBounds.getWidth() / 1.6;
		return width;
	}

	public LoggedButton getBtnRefresh() {
		return btnRefresh;
	}

	public void setBtnRefresh(LoggedButton btnRefresh) {
		this.btnRefresh = btnRefresh;
	}
}
