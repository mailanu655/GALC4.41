package com.honda.galc.client.teamleader.qi.view;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.ImageToEntryScreenController;
import com.honda.galc.client.teamleader.qi.model.ImageToEntryScreenMaintenanceModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.dto.qi.QiImageSectionDto;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>	
 * <code>ImageToEntryScreenPanel</code> is the Panel class for Image to Entry
 * Screen assignment.
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
 * <TD>28/07/2016</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
/**   
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
public class ImageToEntryScreenPanel extends QiAbstractTabbedView<ImageToEntryScreenMaintenanceModel, ImageToEntryScreenController> {
	private LoggedComboBox<String> productTypeCombobox;
	private LoggedComboBox<String> entryModelCombobox;
	private ObjectTablePane<QiImageSectionDto> imageTablePane;
	private ImageView selectedImageView;
	private UpperCaseFieldBean imageFilterTextField;
	private LoggedLabel selectedImageNameLabel;
	private LoggedButton assignBtn;
	private LoggedButton updateBtn;
	private LoggedButton deassignBtn;
	private double width;
	private double height;
	private ObjectTablePane<QiEntryScreenDto> entryScreenDetailsTable;
	
	LoggedButton refreshBtn;

	public ImageToEntryScreenPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);

	}

	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		width = screenBounds.getWidth();
		height = screenBounds.getHeight();		
		HBox outerPane = new HBox();

		outerPane.getChildren().addAll(createImageToEntryScreenImageContainer(), createImageToEntryScreenTableContainer());
		outerPane.setAlignment(Pos.CENTER);
		this.setCenter(outerPane);
	}

	/**
	 * This method is used to create left side content of the screen
	 * 
	 * @return VBox
	 */
	private Node createImageToEntryScreenImageContainer() {
		MigPane leftPane = new MigPane("insets 0 0 0 0", "[left span shrink 0]", "[]10[center shrink 0]");

		LoggedLabel selectedImageLabel = createLoggedLabel("selectedImageLabel", "Selected Image Name : ", "display-label-14");
		selectedImageNameLabel = UiFactory.createLabel("selectedImageNameLabel", "", 14);
		HBox lblBox = new HBox();
		lblBox.getChildren().add(selectedImageLabel);
		lblBox.getChildren().add(selectedImageNameLabel);
		lblBox.setPadding(new Insets(20,0,30,0));
		leftPane.add(lblBox, "span,wrap");
		leftPane.add(createImageContainer(), "left,span,wrap");
		ScrollPane scroll = new ScrollPane();
		scroll.setContent(leftPane);
		scroll.setFitToWidth(true);
		scroll.setFitToHeight(true);
		scroll.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		return leftPane;
	}

	/**
	 * This method is used to create right side content of the screen
	 * 
	 * @return VBox
	 */
	private Node createImageToEntryScreenTableContainer() {
		VBox imageToEntryScreenTableContainer = new VBox();
		TitledPane imageToEntryScreenTitledPane = new TitledPane();
		imageToEntryScreenTitledPane.setText("Active Loaded Images");
		imageToEntryScreenTitledPane.setContent(populateTableContent());
		imageToEntryScreenTitledPane.setPrefSize(width / 2, height / 2.5);
		imageToEntryScreenTitledPane.setPadding(new Insets(0, 10, 0, 10));
		imageToEntryScreenTableContainer.getChildren().addAll(createImageToEntryScreenGridpane(), getEntryScreenTable(),imageToEntryScreenTitledPane, createButtonContainer());
		imageToEntryScreenTableContainer.setAlignment(Pos.TOP_RIGHT);
		ScrollPane scroll = new ScrollPane();
		scroll.setContent(imageToEntryScreenTableContainer);
		scroll.setFitToWidth(true);
		scroll.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		MigPane rightPane = new MigPane("insets 0 0 0 0", "[left center]", "");

		rightPane.add(scroll, "span,wrap");
		return rightPane;

	}

	/**
	 * This method is used to create HBox container
	 * 
	 * @param label1
	 * @param label2
	 * @param position
	 * @return HBox
	 */
	private HBox createContainer(LoggedLabel label1, LoggedLabel label2, Pos position) {
		HBox container = new HBox();
		container.getChildren().addAll(label1, label2);
		container.setAlignment(position);
		return container;
	}

	/**
	 * This method is used to create Button container
	 *  
	 * @return HBox containing Assign, Update and Deassign buttons
	 */
	private HBox createButtonContainer() {
		assignBtn = createBtn(QiConstant.ASSIGN, getController());
		setButtonInit(assignBtn);
		updateBtn =createBtn(QiConstant.UPDATE, getController());
		setButtonInit(updateBtn);
		deassignBtn = createBtn(QiConstant.DEASSIGN, getController());
		setButtonInit(deassignBtn);

		HBox btnContainer = new HBox();
		btnContainer.setSpacing(6);
		btnContainer.setAlignment(Pos.CENTER);
		btnContainer.getChildren().addAll(assignBtn, updateBtn, deassignBtn);
		return btnContainer;
	}

	/**
	 * This method is used to create LoggedLabel
	 * 
	 * @param id
	 * @param text
	 * @param cssClass
	 * @return LoggedLabel
	 */
	private LoggedLabel createLoggedLabel(String id, String text,String cssClass) {
		LoggedLabel label=UiFactory.createLabel(id, text);
		label.getStyleClass().add(cssClass);
		return label;
	}

	/**
	 * This method is used to create LoggedLabel
	 * 
	 * @param id
	 * @param text
	 * @param cssClass
	 * @param cssStyle
	 * @return LoggedLabel
	 */
	private LoggedLabel createLoggedLabel(String id, String text,String cssClass,String cssStyle) {
		LoggedLabel label=UiFactory.createLabel(id, text);
		label.getStyleClass().add(cssClass);
		label.setStyle(cssStyle);
		return label;
	}

	/**
	 * This method is used to create ColumnConstraints
	 * 
	 * @param hpos
	 * @return columnConstraint
	 */
	private ColumnConstraints setColumnConstraints(HPos hpos) {
		ColumnConstraints column = new ColumnConstraints();
		column.setHalignment(hpos);
		column.setPrefWidth(250);
		return column;
	}

	/**
	 * This method is used to create ComboBox
	 * @param <T>
	 *  
	 * @param className
	 * @param id
	 * @param width
	 * @param cssStyle
	 * @return comboBox
	 */
	private  LoggedComboBox<String> createComboBox(String id, int width,String cssStyle){
		LoggedComboBox<String> comboBox=new LoggedComboBox<String>(id);
		comboBox.setMinWidth(USE_COMPUTED_SIZE);
		comboBox.getStyleClass().add(cssStyle);
		return comboBox;
	}

	/**
	 * This method is used to create GridPane with required fields
	 *  
	 * @return imageToEntryScreenGridpane
	 */
	private GridPane createImageToEntryScreenGridpane() {
		GridPane imageToEntryScreenGridpane = createGrid();
		LoggedLabel siteNameLabel = createLoggedLabel("siteNameLabel", "Site ", "display-label-14");
		imageToEntryScreenGridpane.add(siteNameLabel, 0, 1);
		LoggedLabel siteNameContextLabel = UiFactory.createLabel("siteNameContextLabel", getSiteName());
		imageToEntryScreenGridpane.add(siteNameContextLabel, 1, 1);
		refreshBtn =UiFactory.createButton(QiConstant.REFRESH, "refreshBtn");
		refreshBtn.setStyle("-fx-font-size: 14px;");
		refreshBtn.setOnAction(getController());
		imageToEntryScreenGridpane.add(refreshBtn, 3, 1);

		imageToEntryScreenGridpane.add(createProductTypeLabelContainer(), 0, 2);
		productTypeCombobox = createComboBox("productType", 200, "combo-box-base");
		imageToEntryScreenGridpane.add(productTypeCombobox, 1, 2);

		imageToEntryScreenGridpane.add(createEntryModelLabelContainer(), 2, 2);
		entryModelCombobox = createComboBox("entryModel", 200, "combo-box-base");
		imageToEntryScreenGridpane.add(entryModelCombobox, 3, 2);
		return imageToEntryScreenGridpane;
	}

	/** This method is used to return gridPane template
	 * 
	 * @return
	 */
	private GridPane createGrid() {
		GridPane imageToEntryScreenGridpane = new GridPane();
		imageToEntryScreenGridpane.setHgap(10);
		imageToEntryScreenGridpane.setVgap(5);
		imageToEntryScreenGridpane.setPadding(new Insets(0, 10, 0, 0));
		imageToEntryScreenGridpane.setAlignment(Pos.CENTER);
		imageToEntryScreenGridpane.getColumnConstraints().addAll(setColumnConstraints(HPos.RIGHT),setColumnConstraints(HPos.LEFT),setColumnConstraints(HPos.CENTER),setColumnConstraints(HPos.RIGHT));
		return imageToEntryScreenGridpane;
	}
	/**  This method is used to return HBox productTypeLabelContainer
	 * 
	 * @return
	 */
	private HBox createProductTypeLabelContainer() {
		LoggedLabel productTypeLabel = createLoggedLabel("productTypeLabel", "Product Type", "display-label-14");
		LoggedLabel asteriskProductType = createLoggedLabel("asteriskProductType", "*", "display-label-14", "-fx-text-fill: red");
		HBox productTypeLabelContainer = createContainer(productTypeLabel, asteriskProductType, Pos.CENTER_RIGHT);
		return productTypeLabelContainer;
	}

	/**  This method is used to return HBox entryModelLabelContainer
	 * 
	 * @return
	 */
	private HBox createEntryModelLabelContainer() {
		LoggedLabel entryModelLabel = createLoggedLabel("entryModelLabel", "Entry Model", "display-label-14");
		LoggedLabel asteriskEntryModel = createLoggedLabel("asteriskEntryModel", "*", "display-label-14", "-fx-text-fill: red");
		HBox entryModelLabelContainer = createContainer(entryModelLabel, asteriskEntryModel, Pos.CENTER_RIGHT);
		return entryModelLabelContainer;
	}	


	/** Entry Screen Table panel
	 *
	 * @return
	 */
	private Node getEntryScreenTable() {
		TitledPane entryScreenImagePane = new TitledPane();
		entryScreenImagePane.setPrefWidth(width / 2);
		entryScreenImagePane.setPadding(new Insets(10));
		entryScreenImagePane.setText("Entry Screen List");
		entryScreenImagePane.setCollapsible(false);
		entryScreenImagePane.setContent(getEntryScreenTablePane());
		return entryScreenImagePane;
	}

	/** Entry Screen Table 
	 *
	 * @return
	 */
	private Node getEntryScreenTablePane() {
		VBox entryScreenTableContainer = new VBox();

		ColumnMappingList columnMappingList = ColumnMappingList.with("Entry Screen", "entryScreen").put("Is Used", "IsUsedVersionData").put("Entry Department", "divisionId").put("Image", "imageName");
		Double[] columnWidth = new Double[] {0.30,0.05,0.30,0.20}; 

		entryScreenDetailsTable = new ObjectTablePane<QiEntryScreenDto>(columnMappingList,columnWidth);
		entryScreenDetailsTable.setConstrainedResize(true);
		entryScreenDetailsTable.setPrefHeight(150);

		LoggedTableColumn<QiEntryScreenDto, Integer> column = new LoggedTableColumn<QiEntryScreenDto, Integer>();
		createSerialNumber(column);
		entryScreenDetailsTable.getTable().getColumns().add(0, column);
		entryScreenDetailsTable.getTable().getColumns().get(0).setText("#");
		entryScreenDetailsTable.getTable().getColumns().get(0).setResizable(true);
		entryScreenDetailsTable.getTable().getColumns().get(0).setMaxWidth(100);
		entryScreenDetailsTable.getTable().getColumns().get(0).setMinWidth(5);
		entryScreenDetailsTable.setId("entryScreenDetailsTable");
		entryScreenTableContainer.getChildren().addAll(entryScreenDetailsTable);
		return entryScreenTableContainer;
	}

	/**  This method is used to populate image names in table
	 * 
	 * @return
	 */
	private MigPane populateTableContent() {
		MigPane pane = new MigPane("insets 0 0 0 0", "[center,grow,fill]", "");
		Label filterTextFieldLabel = createLoggedLabel("filterTextFieldLabel", "Filter","display-label-14");
		imageFilterTextField = createFilterTextField("imageFilterTextField", 15, getController());
		imageTablePane = createTable();
		imageTablePane.setPadding(new Insets(10, 10, 5, 10));
		VBox outerPane = new VBox();
		HBox filterBox = new HBox();

		filterBox.getChildren().addAll(filterTextFieldLabel, imageFilterTextField);
		filterBox.setSpacing(10);
		filterBox.setAlignment(Pos.CENTER_RIGHT);
		filterBox.setPadding(new Insets(5,10,0,0));
		outerPane.getChildren().addAll(filterBox, imageTablePane);
		pane.add(outerPane, "span,wrap");

		LoggedTableColumn<QiImageSectionDto, Integer> column = new LoggedTableColumn<QiImageSectionDto, Integer>();
		createSerialNumber(column);
		imageTablePane.getTable().getColumns().add(0, column);
		imageTablePane.getTable().getColumns().get(0).setText("#");
		imageTablePane.getTable().getColumns().get(0).setResizable(true);
		imageTablePane.getTable().getColumns().get(0).setMaxWidth(100);
		imageTablePane.getTable().getColumns().get(0).setMinWidth(50);
		return pane;
	}

	/**
	 * This method is used to return ObjectTablePane for Image.
	 * 
	 * @return
	 */
	private ObjectTablePane<QiImageSectionDto> createTable() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Image Name", "imageName");
		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		Double[] columnWidth = new Double[] { 0.375 };
		ObjectTablePane<QiImageSectionDto> panel = new ObjectTablePane<QiImageSectionDto>(columnMappingList, columnWidth);
		panel.setConstrainedResize(false);
		panel.setId("imageToEntryScreenTablePane");
		return panel;
	}

	/**  This method is used to return AnchorPane used to display Image to the Entry Screen
	 * 
	 * @return
	 */
	private BorderPane createImageContainer() {
		BorderPane imageContainer=new BorderPane();
		selectedImageView = new ImageView();
		selectedImageView.setPickOnBounds(true);
		selectedImageView.setPreserveRatio(true);
		selectedImageView.setFitHeight(500.0);
		selectedImageView.setFitWidth(500.0);
		imageContainer.setCenter(selectedImageView);
		imageContainer.setPrefSize(500, 500.0);
		imageContainer.setStyle("-fx-border-style: solid inside;" + "-fx-border-width: 0.8;" + "-fx-border-insets: 2;" + "-fx-border-color: grey;");
		return imageContainer;
	}

	/**  This method is used to set initial state for the button
	 * 
	 */
	private void setButtonInit(LoggedButton btn) {
		btn.setDisable(true);
		btn.setStyle("-fx-font-size: 12px;");
	}

	/** This method is used to get Site Name
	 * 
	 * @return
	 */
	private String getSiteName() {
		return getModel().findSiteName();
	}

	/**
	 * This method is used to perform operation on tab selection
	 * 
	 */
	public void onTabSelected() {
		QiImageSectionDto selectedImageIndex=imageTablePane.getSelectedItem();
		reload(StringUtils.trim(imageFilterTextField.getText()));
		getController().clearDisplayMessage();
		getController().refreshBtnAction(selectedImageIndex);
	}

	@Override
	public void reload() {

	}

	public void reload(String filter) {
		imageTablePane.setData(getModel().findImageByFilter(StringUtils.trim(filter)));
	}

	@Override
	public void start() {

	}

	/**
	 * This method is used to set the Screen Name
	 * 
	 * @return screenName
	 */
	public String getScreenName() {
		return "Image to Entry Screen";
	}

	public ImageView getSelectedImageView() {
		return selectedImageView;
	}

	public void setSelectedImageView(ImageView selectedImageView) {
		this.selectedImageView = selectedImageView;
	}

	public LoggedComboBox<String> getProductTypeCombobox() {
		return productTypeCombobox;
	}

	public void setProductTypeCombobox(LoggedComboBox<String> productTypeCombobox) {
		this.productTypeCombobox = productTypeCombobox;
	}

	public LoggedComboBox<String> getEntryModelCombobox() {
		return entryModelCombobox;
	}

	public void setEntryModelCombobox(LoggedComboBox<String> entryModelCombobox) {
		this.entryModelCombobox = entryModelCombobox;
	}

	public LoggedLabel getselectedImageNameLabel() {
		return selectedImageNameLabel;
	}

	public void setselectedImageNameLabel(String selectedImageNameLabel) {
		this.selectedImageNameLabel.setText(selectedImageNameLabel);
	}

	public ObjectTablePane<QiImageSectionDto> getImageTablePane() {
		return imageTablePane;
	}

	public void setImageTablePane(ObjectTablePane<QiImageSectionDto> imageTablePane) {
		this.imageTablePane = imageTablePane;
	}

	public UpperCaseFieldBean getImageFilterTextField() {
		return imageFilterTextField;
	}

	public void setImageFilterTextField(UpperCaseFieldBean imageFilterTextField) {
		this.imageFilterTextField = imageFilterTextField;
	}

	public LoggedButton getAssignBtn() {
		return assignBtn;
	}

	public LoggedButton getUpdateBtn() {
		return updateBtn;
	}

	public LoggedButton getDeassignBtn() {
		return deassignBtn;
	}

	public ObjectTablePane<QiEntryScreenDto> getEntryScreenDetailsTable() {
		return entryScreenDetailsTable;
	}

	public void setEntryScreenDetailsTable(ObjectTablePane<QiEntryScreenDto> entryScreenDetailsTable) {
		this.entryScreenDetailsTable = entryScreenDetailsTable;
	}

	public LoggedButton getRefreshBtn() {
		return refreshBtn;
	}

}
