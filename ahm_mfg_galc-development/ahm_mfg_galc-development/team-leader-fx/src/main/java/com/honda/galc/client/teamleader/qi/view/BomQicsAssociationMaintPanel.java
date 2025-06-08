package com.honda.galc.client.teamleader.qi.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.BomQicsAssociationController;
import com.honda.galc.client.teamleader.qi.model.BomQicsPartAssociationMaintenanceModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.QiBomPartDto;
import com.honda.galc.entity.qi.QiInspectionPart;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BomQicsAssociationMaintPanel</code> is the Panel class for Bom Qics Part Association Maintenance.
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

public class BomQicsAssociationMaintPanel extends QiAbstractTabbedView<BomQicsPartAssociationMaintenanceModel, BomQicsAssociationController>
{
	private ObjectTablePane<QiBomPartDto> bomPartNameTablePane;
	private ObjectTablePane<QiInspectionPart> qicsPartNameTablePane;
	private ObjectTablePane<QiBomPartDto> associatedPartListingTablePane;
	private LoggedRadioButton allRadioButton;
	private LoggedRadioButton associatedRadioButton;
	private LoggedRadioButton notAssociatedRadioButton;

	private UpperCaseFieldBean bomPartFilterTextField;
	private UpperCaseFieldBean qicsPartFilterTextField;
	
	boolean isRefresh=false;

	public  BomQicsAssociationMaintPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
	}

	@Override
	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		VBox outerPane = new VBox();
		HBox mainHBox=new HBox();

		HBox bomPartPane=new HBox();
		bomPartPane.getChildren().add(createTitledPane("BOM Part Name", createBomPartNamePanel()));

		HBox qicsPartPane=new HBox();
		qicsPartPane.getChildren().add(createTitledPane("	QICS Part Name", createQicsPartNamePanel()));

		mainHBox.getChildren().addAll(bomPartPane,qicsPartPane);

		HBox bottomPane=new HBox();
		bottomPane.getChildren().add(createAssociatedTitledPane("Associated Part Listing", createAssociatedPartListingPanel()));
		outerPane.getChildren().addAll(mainHBox,bottomPane);
		this.setTop(mainHBox);
		this.setCenter(bottomPane);
	}

	/**
	 * This method is used to create BOM Part panel.
	 * @return
	 */
	private MigPane createBomPartNamePanel() {
		MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");

		VBox mainBoxBomPart = new VBox();
		HBox bomPartLabelBox = new HBox();
		HBox bomPartRadioBox = new HBox();

		LoggedLabel bomPartFilterLblId= UiFactory.createLabel("bomPartFilterLblId", "Filter");
		bomPartFilterLblId.getStyleClass().add("display-label");
		bomPartFilterTextField = createFilterTextField("filter-textfield", 18, getController());

		bomPartLabelBox.getChildren().addAll(bomPartFilterLblId,bomPartFilterTextField);
		bomPartNameTablePane = createBomPartPane();
		bomPartLabelBox.setSpacing(10);
		bomPartLabelBox.setAlignment(Pos.CENTER_RIGHT);
		bomPartLabelBox.setPadding(new Insets(0, 0, 5, 0));
		mainBoxBomPart.getChildren().addAll(bomPartLabelBox,bomPartNameTablePane);
		pane.add(mainBoxBomPart,"span,wrap");

		ToggleGroup toggleGroup = new ToggleGroup();
		allRadioButton = createRadioButton(QiConstant.ALL, toggleGroup, true, getController());
		associatedRadioButton = createRadioButton("Associated", toggleGroup, false, getController());
		notAssociatedRadioButton = createRadioButton("Not Associated", toggleGroup, false, getController());
		bomPartRadioBox.getChildren().addAll(allRadioButton,associatedRadioButton,notAssociatedRadioButton);
		bomPartRadioBox.setSpacing(20);
		pane.add(bomPartRadioBox,"span,wrap");

		return pane;
	}

	/**
	 * This method is used to create QICS Part panel.
	 * @return
	 */
	private MigPane createQicsPartNamePanel() {
		MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");
		VBox mainBoxQicsPart = new VBox();
		HBox qicsPartLabelBox = new HBox();

		LoggedLabel qicsPartFilterLblId= UiFactory.createLabel("qicsPartFilterLblId", "Filter");
		qicsPartFilterLblId.getStyleClass().add("display-label");
		qicsPartFilterTextField = createFilterTextField("filter-textfieldqics", 18, getController());

		qicsPartLabelBox.getChildren().addAll(qicsPartFilterLblId,qicsPartFilterTextField);
		qicsPartNameTablePane = createQicsPartPane();
		qicsPartLabelBox.setSpacing(10);
		qicsPartLabelBox.setAlignment(Pos.CENTER_RIGHT);
		qicsPartLabelBox.setPadding(new Insets(0, 0, 5, 0));
		mainBoxQicsPart.getChildren().addAll(qicsPartLabelBox,qicsPartNameTablePane);

		pane.add(mainBoxQicsPart,"span,wrap");
		return pane;

	}

	/**
	 * This method is used to create Associated Part panel.
	 * @return
	 */
	private MigPane createAssociatedPartListingPanel() {
		MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");
		VBox associatedPartBox=new VBox();
		associatedPartListingTablePane=createAssociatedPartListingPane();
		associatedPartBox.getChildren().add(associatedPartListingTablePane);
		pane.add( associatedPartBox,"span,wrap");
		return pane;

	}
	/**
	 * This method is used to create TitledPane for Bom Part and Qics panel.
	 * @param title
	 * @param content
	 * @return
	 */
	private TitledPane createTitledPane(String title,Node content) {
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(content);
		titledPane.setPrefSize(primaryScreenBounds.getWidth()/2,350);
		return titledPane;
	}

	/**
	 * This method is used to create TitledPane for Associated part panel.
	 * @param title
	 * @param content
	 * @return
	 */
	private TitledPane createAssociatedTitledPane(String title,Node content) {
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(content);
		titledPane.setPrefSize(primaryScreenBounds.getWidth(),300);
		return titledPane;
	}

	/**
	 * This method is used to return ObjectTablePane for Part.
	 * @return
	 */
	private ObjectTablePane<QiBomPartDto> createBomPartPane() {
		ColumnMappingList columnMappingList =ColumnMappingList.with("BOM Part #", "mainPartNo")
				.put("BOM Part Name", "dieCastPartName").put("DC Part #" ,"dieCastPartNo");
		ObjectTablePane<QiBomPartDto> panel = new ObjectTablePane<QiBomPartDto>(columnMappingList);
		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		panel.getTable().getColumns().get(0).setPrefWidth(250);
		panel.getTable().getColumns().get(1).setPrefWidth(250);
		panel.getTable().getColumns().get(2).setPrefWidth(200);
		panel.setStyle("-fx-padding: 5px 0px 5px 0px; -fx-min-hei"
				+ "ght: 125px");
		panel.setConstrainedResize(false);
		return panel;
	}

	/**
	 * This method is used to return ObjectTablePane for Qics Part.
	 * @return
	 */
	private ObjectTablePane<QiInspectionPart> createQicsPartPane() {
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("QICS Part Name", "inspectionPartName");
		ObjectTablePane<QiInspectionPart> panel = new ObjectTablePane<QiInspectionPart>(columnMappingList);
		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		panel.getTable().getColumns().get(0).setPrefWidth(652);
		panel.setStyle("-fx-padding: 5px 0px 5px 0px; -fx-min-height: 125px");
		panel.setConstrainedResize(false);
		return panel;
	}


	/**
	 * This method is used to return ObjectTablePane for Associated Part.
	 * @return
	 */
	private ObjectTablePane<QiBomPartDto> createAssociatedPartListingPane() {
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("BOM Part #", "mainPartNo")
				.put("BOM Part Name", "dieCastPartName") .put("QICS Part Name", "inspectionPart");

		ObjectTablePane<QiBomPartDto> panel = new ObjectTablePane<QiBomPartDto>(columnMappingList);
		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		panel.getTable().getColumns().get(0).setPrefWidth(440);
		panel.getTable().getColumns().get(1).setPrefWidth(450);
		panel.getTable().getColumns().get(2).setPrefWidth(450);
		panel.setStyle("-fx-padding: 5px 0px 5px 0px; -fx-min-height: 125px");
		panel.setConstrainedResize(false);
		return panel;
	}

	public void onTabSelected() {
		isRefresh=true;
		QiInspectionPart qicsSelectedItem=getQicsPartNameTablePane().getSelectedItem();
		List<QiBomPartDto> bomSelectedItem = new ArrayList<QiBomPartDto>();
		bomSelectedItem.addAll(getBomPartNameTablePane().getSelectedItems());
		reload(StringUtils.trim(qicsPartFilterTextField.getText()),"QICS Filter");
		reload(StringUtils.trim(bomPartFilterTextField.getText()),"BOM Filter");
		getQicsPartNameTablePane().getTable().getSelectionModel().select(qicsSelectedItem);
		for(QiBomPartDto qiBomPartDto : bomSelectedItem){
			getBomPartNameTablePane().getTable().getSelectionModel().select(qiBomPartDto);
		}
		isRefresh=false;
	}

	public String getScreenName() {
		return "BOM QICS";
	}

	@Override
	public void reload() {
		bomPartNameTablePane.setData(getModel().getBomPartsByFilter(""));

	}

	/**
	 * This method reloads BOM Part Table and QICS Part table based on filter selection
	 * @return
	 */
	public void reload(String filter, String filterName) {
		if(filterName.equalsIgnoreCase("BOM Filter")){
			if(allRadioButton.isSelected()){
				bomPartNameTablePane.setData(getModel().getBomPartsByFilter(filter));
				associatedPartListingTablePane.getTable().getItems().clear();
				bomPartNameTablePane.getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			}else if(associatedRadioButton.isSelected()){
				associatedPartListingTablePane.getTable().getItems().clear();
				bomPartNameTablePane.setData(getModel().getAssociatedBomPartsByFilter(filter));
				bomPartNameTablePane.getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			}else{
				bomPartNameTablePane.setData(getModel().getNotAssociatedBomPartsByFilter(filter));
				associatedPartListingTablePane.getTable().getItems().clear();
				bomPartNameTablePane.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			}
		}
		else if(filterName.equalsIgnoreCase("QICS Filter")){
			qicsPartNameTablePane.setData(getModel().getActivePartNameByFilter(filter));
		}

	}


	@Override
	public void start() {
	}

	public ObjectTablePane<QiInspectionPart> getQicsPartNameTablePane() {
		return qicsPartNameTablePane;
	}

	public String getFilterTextData()
	{
		return StringUtils.trimToEmpty(bomPartFilterTextField.getText());
	}

	public LoggedRadioButton getAllRadioButton() {
		return allRadioButton;
	}
	public void setAllRadioButton(LoggedRadioButton allRadioButton) {
		this.allRadioButton = allRadioButton;
	}
	public LoggedRadioButton getAssociatedRadioButton() {
		return associatedRadioButton;
	}
	public void setAssociatedRadioButton(LoggedRadioButton associatedRadioButton) {
		this.associatedRadioButton = associatedRadioButton;
	}
	public UpperCaseFieldBean getQicsPartFilterTextField() {
		return qicsPartFilterTextField;
	}
	public void setQicsPartFilterTextField(UpperCaseFieldBean qicsPartFilterTextField) {
		this.qicsPartFilterTextField = qicsPartFilterTextField;
	}
	public ObjectTablePane<QiBomPartDto> getBomPartNameTablePane() {
		return bomPartNameTablePane;
	}
	public void setBomPartNameTablePane(
			ObjectTablePane<QiBomPartDto> bomPartNameTablePane) {
		this.bomPartNameTablePane = bomPartNameTablePane;
	}
	public UpperCaseFieldBean getBomPartFilterTextField() {
		return bomPartFilterTextField;
	}
	public void setBomPartFilterTextField(UpperCaseFieldBean bomPartFilterTextField) {
		this.bomPartFilterTextField = bomPartFilterTextField;
	}
	public void setQicsPartNameTablePane(
			ObjectTablePane<QiInspectionPart> qicsPartNameTablePane) {
		this.qicsPartNameTablePane = qicsPartNameTablePane;
	}

	public ObjectTablePane<QiBomPartDto> getAssociatedPartListingTablePane() {
		return associatedPartListingTablePane;
	}
	public void setAssociatedPartListingTablePane(
			ObjectTablePane<QiBomPartDto> associatedPartListingTablePane) {
		this.associatedPartListingTablePane = associatedPartListingTablePane;
	}

	public LoggedRadioButton getNotAssociatedRadioButton() {
		return notAssociatedRadioButton;
	}

	public void setNotAssociatedRadioButton(
			LoggedRadioButton notAssociatedRadioButton) {
		this.notAssociatedRadioButton = notAssociatedRadioButton;
	}
	
	public boolean isRefresh() {
		return isRefresh;
	}

}
