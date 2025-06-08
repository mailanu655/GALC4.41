package com.honda.galc.client.teamleader.datamigration;


import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.view.QiAbstractTabbedView;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.DataMigrationDto;
import com.honda.galc.entity.qi.QiMappingCombination;
import com.honda.galc.entity.qi.QiOldCombination;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;

/**
 * <h3> DataMigrationAssociationMaintenancePanel Class description</h3> <h4>Description</h4>
 * <p>
 * <code>DataMigrationAssociationMaintenancePanel</code> is the Panel class for to populate data and create or delete defect combination association 
 * between NAQ Defect description and NGLC/Old GALC defect combination
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

public class DataMigrationAssociationMaintenancePanel extends QiAbstractTabbedView<DataMigrationAssociationMaintenanceModel, DataMigrationAssociationController>
{
	private ObjectTablePane<DataMigrationDto> naqDefectCombTablePane;
	private ObjectTablePane<DataMigrationDto> legacyDefectCombTablePane;
	private ObjectTablePane<QiMappingCombination> associatedDefectCombTablePane;
	private UpperCaseFieldBean naqFilterTextField;
	private UpperCaseFieldBean legacyFilterTextField;
	private UpperCaseFieldBean associatedFilterTextField;
	private double  screenWidth;
	private double  screenHeight;
	
	public  DataMigrationAssociationMaintenancePanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
	}

	@Override
	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		this.screenWidth=Screen.getPrimary().getVisualBounds().getWidth();
		this.screenHeight=Screen.getPrimary().getVisualBounds().getHeight();
		MigPane mainPane = new MigPane("insets 0 0 0 0", "[left,grow][shrink 0]", "[]05[shrink 0]");
		mainPane.add(createTitledPane("Full QICS Part Defect Descriptions", createNaqDefectCombPanel(),screenWidth*0.50,screenHeight/2.08),"split 2, span 2,left,gaptop 5, gapleft 5");
		mainPane.add(createTitledPane("Old Part Defect Combination", createLegacyDefectCombPanel(),screenWidth*0.50,screenHeight/2.08),"span,wrap, gaptop 5, gapleft 5, gapright 5");
		mainPane.add(createTitledPane("Associated Defect Combinations", createAssociatedPartListingPanel(),screenWidth,screenHeight/2.43)," gapleft 5, gapright 5");
		this.setCenter(mainPane);
	}

	/**
	 * This method is used to create BOM Part panel.
	 * @return
	 */
	private MigPane createNaqDefectCombPanel() {
		MigPane pane = new MigPane("insets 10 0 0 0", "[center,grow,fill]", "");
		HBox naqFilterLabelBox = new HBox();
		LoggedLabel naqFilterLabel= UiFactory.createLabel("naqFilterLabel", "Filter");
		naqFilterLabel.getStyleClass().add("display-label");
		naqFilterTextField = createFilterTextField("filter-textfield", 18, getController());
		naqFilterLabelBox.getChildren().addAll(naqFilterLabel,naqFilterTextField);
		this.naqDefectCombTablePane = createNaqDefectCombPane();
		naqFilterLabelBox.setSpacing(10);
		naqFilterLabelBox.setAlignment(Pos.CENTER_RIGHT);
		naqFilterLabelBox.setPadding(new Insets(0, 5, 5, 0));
		pane.add(naqFilterLabelBox,"span,wrap");
		pane.add(naqDefectCombTablePane);
		return pane;
	}
	

	/**
	 * This method is used to create QICS Part panel.
	 * @return
	 */
	private MigPane  createLegacyDefectCombPanel() {
		MigPane pane = new MigPane("insets 10 0 0 0", "[center,grow,fill]", "");
		HBox legacyFilterLabelBox = new HBox();
		LoggedLabel legacyFilterLabel= UiFactory.createLabel("nglcFilterLabel", "Filter");
		legacyFilterLabel.getStyleClass().add("display-label");
		legacyFilterTextField = createFilterTextField("filter-textfield", 18, getController());
		legacyFilterLabelBox.getChildren().addAll(legacyFilterLabel,legacyFilterTextField);
		this.legacyDefectCombTablePane = createLegacyDefectCombPane();
		legacyFilterLabelBox.setSpacing(10);
		legacyFilterLabelBox.setAlignment(Pos.CENTER_RIGHT);
		legacyFilterLabelBox.setPadding(new Insets(0, 5, 5, 0));
		pane.add(legacyFilterLabelBox,"span,wrap");
		pane.add(legacyDefectCombTablePane);
		return pane;
	}

	/**
	 * This method is used to create Associated Part panel.
	 * @return
	 */
	private MigPane createAssociatedPartListingPanel() {
		MigPane pane = new MigPane("insets 10 0 0 0", "[center,grow,fill]", "");
		HBox associatedFilterLabelBox = new HBox();
		LoggedLabel naqFilterLabel= UiFactory.createLabel("associatedFilterLabel", "Filter");
		naqFilterLabel.getStyleClass().add("display-label");
		associatedFilterTextField = createFilterTextField("filter-textfield", 18, getController());
		associatedFilterLabelBox.getChildren().addAll(naqFilterLabel,associatedFilterTextField);
		this.associatedDefectCombTablePane=createAssociatedPartListingPane();
		associatedFilterLabelBox.setSpacing(10);
		associatedFilterLabelBox.setAlignment(Pos.CENTER_RIGHT);
		associatedFilterLabelBox.setPadding(new Insets(0, 5, 5, 0));
		pane.add(associatedFilterLabelBox,"span,wrap, right");
		pane.add(associatedDefectCombTablePane);
		return pane;
	}
	/**
	 * This method is used to create TitledPane for Bom Part and Qics panel.
	 * @param title
	 * @param content
	 * @return
	 */
	private TitledPane createTitledPane(String title,Node content, double width, double height) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(content);
		titledPane.setPrefSize(width,height);
		return titledPane;
	}

	
	/**
	 * This method is used to return ObjectTablePane for Part.
	 * @return
	 */
	private ObjectTablePane<DataMigrationDto> createNaqDefectCombPane() {
		ColumnMappingList columnMappingList =ColumnMappingList.with("Full QICS Part Defect Description", "partDefectDesc");
		Double[] columnWidth = new Double[] {0.500};
		ObjectTablePane<DataMigrationDto> panel = new ObjectTablePane<DataMigrationDto>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		panel.setSelectionMode(SelectionMode.SINGLE);
		return panel;
	}

	/**
	 * This method is used to return ObjectTablePane for Qics Part.
	 * @return
	 */
	private ObjectTablePane<DataMigrationDto> createLegacyDefectCombPane() {
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Old Part Defect Combination", "partDefectDesc");
		Double[] columnWidth = new Double[] {0.500};
		ObjectTablePane<DataMigrationDto> panel = new ObjectTablePane<DataMigrationDto>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		panel.setSelectionMode(SelectionMode.MULTIPLE);
		return panel;
	}


	/**
	 * This method is used to return ObjectTablePane for Associated Part.
	 * @return
	 */
	private ObjectTablePane<QiMappingCombination> createAssociatedPartListingPane() {
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Part Name", "naqNameOfPart")
				.put("Regional Defect Combination", "partDefectDesc") .put("Old Part Defect Combination", "id.oldCombination");
		Double[] columnWidth = new Double[] { 0.180, 0.420, 0.400};
		ObjectTablePane<QiMappingCombination> panel = new ObjectTablePane<QiMappingCombination>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		panel.setPrefWidth(screenWidth);
		panel.setSelectionMode(SelectionMode.MULTIPLE);
		return panel;
	}

	public void onTabSelected() {
		reload(StringUtils.trim(getLegacyPartFilterTextField().getText()),"NGLC Filter");
		reload(StringUtils.trim(getNaqPartFilterTextField().getText()),"NAQ Filter");
		reload(StringUtils.trim(getAssociatedFilterTextField().getText()),"Associated Filter");
	}

	public String getScreenName() {
		return "Data Migration";
	}

	/**
	 * This method reloads BOM Part Table and QICS Part table based on filter selection
	 * @return
	 */
	public void reload(String filterText, String filterName) {
		getController().filterAllDefectCombinations(filterText,filterName);
	}

	public void getTablePanelCleared(){
	   getAssociatedPartListingTablePane().getTable().getItems().clear();
	   getNaqDefectCombinationTablePane().getTable().getItems().clear();
	   getLegacyDefectCombTablePane().getTable().getItems().clear();
	}
	
	@Override
	public void start() {
	}

	public ObjectTablePane<DataMigrationDto> getLegacyDefectCombTablePane() {
		return legacyDefectCombTablePane;
	}

	public String getNaqFilterTextData()
	{
		return StringUtils.trimToEmpty(naqFilterTextField.getText());
	}

	public UpperCaseFieldBean getLegacyPartFilterTextField() {
		return legacyFilterTextField;
	}
	public void setLegacyPartFilterTextField(UpperCaseFieldBean legacyFilterTextField) {
		this.legacyFilterTextField = legacyFilterTextField;
	}
	public UpperCaseFieldBean getAssociatedFilterTextField() {
		return associatedFilterTextField;
	}

	public ObjectTablePane<DataMigrationDto> getNaqDefectCombinationTablePane() {
		return naqDefectCombTablePane;
	}
	public void setNaqDefectCombinationTablePane(ObjectTablePane<DataMigrationDto> naQicsDefectCombTablePane) {
		this.naqDefectCombTablePane = naQicsDefectCombTablePane;
	}
	public UpperCaseFieldBean getNaqPartFilterTextField() {
		return naqFilterTextField;
	}
	public void setNaqFilterTextField(UpperCaseFieldBean naqFilterTextField) {
		this.naqFilterTextField = naqFilterTextField;
	}
	public void setLegacyDefectCombTablePane(ObjectTablePane<DataMigrationDto> legacyDefectCombTablePane) {
		this.legacyDefectCombTablePane = legacyDefectCombTablePane;
	}

	public ObjectTablePane<QiMappingCombination> getAssociatedPartListingTablePane() {
		return associatedDefectCombTablePane;
	}
	public void setAssociatedPartListingTablePane(ObjectTablePane<QiMappingCombination> associatedDefectCombTablePane) {
		this.associatedDefectCombTablePane = associatedDefectCombTablePane;
	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub
		
	}
	
}
