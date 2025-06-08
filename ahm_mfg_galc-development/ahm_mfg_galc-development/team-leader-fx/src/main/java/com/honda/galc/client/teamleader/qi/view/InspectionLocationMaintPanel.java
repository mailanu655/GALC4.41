package com.honda.galc.client.teamleader.qi.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.InspectionLocationController;
import com.honda.galc.client.teamleader.qi.enumtype.QiRegionalScreenName;
import com.honda.galc.client.teamleader.qi.model.ItemMaintenanceModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.enumtype.QiActiveStatus;
import com.honda.galc.entity.qi.QiInspectionLocation;

/**
 * 
 * <h3>InspectionLocationMaintPanel Class description</h3>
 * <p>
 * InspectionLocationMaintPanel is used to create the the RaioButtons (All, Activate,Reactivate) ,Filter ,TableView and populate data in TableView
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
public class InspectionLocationMaintPanel extends QiAbstractTabbedView<ItemMaintenanceModel, InspectionLocationController>{
	
	private UpperCaseFieldBean locationFilterTextField;
	
	private ObjectTablePane<QiInspectionLocation> locationScreenTablePane;
	public ObjectTablePane<QiInspectionLocation> getLocationScreenTablePane() {
		return locationScreenTablePane;
	}
	public InspectionLocationMaintPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
	}
	/**
	 * This method is used to create layout of the Inspection Location Parent screen
	 */
	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		Label inspectionLocationFilterLabel = UiFactory.createLabel("label", "Filter");
		HBox outerContainer = new HBox();
		HBox filterContainer = new HBox();
		locationFilterTextField= createFilterTextField("filter-textfield", 25, getController());
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		double width = screenBounds.getWidth()/2;
		HBox radioButtonContainer = createFilterRadioButtons(getController(), width);
		 
		filterContainer.getChildren().addAll(inspectionLocationFilterLabel,locationFilterTextField);
		filterContainer.setSpacing(10);
		filterContainer.setPadding(new Insets(10));
		filterContainer.setAlignment(Pos.CENTER_RIGHT);
		filterContainer.setPrefWidth(width);
		
		outerContainer.getChildren().addAll(radioButtonContainer,filterContainer);
		locationScreenTablePane = createPartTablePane();
		this.setTop(outerContainer);
		this.setCenter(locationScreenTablePane);
	}
	/**
	 * This method is used to create a Table Pane.
	 * @return
	 */
	private ObjectTablePane<QiInspectionLocation> createPartTablePane(){ 
		ColumnMappingList columnMappingList = ColumnMappingList.with("Name", "inspectionPartLocationName")
		.put("Abbr", "inspectionPartLocDescShort").put("Description","inspectionPartLocDescLong")
		.put("Status\nActive=1\nInactive=0", "status").put("Hierarchy\nValue", "hierarchy")
		.put("Position\nPrimary=1\nSecondary=0","position").put("Created\nBy", "createUser").put("Created\nTimestamp", "createDate")
		.put("Updated\nBy", "updateUser").put("Updated\nTimestamp","updateDate");
		
		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		Double[] columnWidth = new Double[] {
				0.15,0.1,0.15,0.05,0.05,0.1,0.05,0.12,0.05,0.1
			};
		ObjectTablePane<QiInspectionLocation> panel = new ObjectTablePane<QiInspectionLocation>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		LoggedTableColumn<QiInspectionLocation, Integer> column = new LoggedTableColumn<QiInspectionLocation, Integer>();
		panel.getTable().getColumns().add(0, column);
		panel.getTable().getColumns().get(0).setText("#");
		panel.getTable().getColumns().get(0).setResizable(true);
		panel.getTable().getColumns().get(0).setMaxWidth(100);
		panel.getTable().getColumns().get(0).setMinWidth(1);
		createSerialNumber(column);
		return panel;
	}
	/**
	 * This method is used to call when user switched to others tabs
	 */
	public void onTabSelected() {
		if(getAllRadioBtn().isSelected())
			reload(getLocationFilterTextField().getText(), false);
		else if(getActiveRadioBtn().isSelected())
			reload(getLocationFilterTextField().getText(),QiActiveStatus.ACTIVE.getName());
		else 
			reload(getLocationFilterTextField().getText(),QiActiveStatus.INACTIVE.getName());
	}

	@Override
	public ViewId getViewId() {
		return null;
	}
	/**
	 * This method is used to refresh the screen 
	 */
	@Override
	public void reload() {
		locationScreenTablePane.setData(getModel().getLocationByFilter(""));
	}
	/**
	 * This method is used to refresh the screen based on filter
	 * @param filter
	 */
	public void reload(String filter, boolean isRestoreSelect) {
		locationScreenTablePane.setData(getModel().getLocationByFilter(filter), isRestoreSelect);
	}
	/**
	 * This method is used to refresh the screen based on the filter value and status
	 * @param filter
	 * @param status
	 */
	public void reload(String filter,String status) {
		locationScreenTablePane.setData(getModel().getLocationByFilter(filter,status));
	}
	
	@Override
	public void start() {
	}

	public String getScreenName() {
		return QiRegionalScreenName.INSPECTION_LOCATION.getScreenName();
	}
	public ObjectTablePane<QiInspectionLocation> getEntryScreenTablePane() {
		return locationScreenTablePane;
	}
	public String getFilterTextData()
	{
		return StringUtils.trimToEmpty(locationFilterTextField.getText());
	}

	public UpperCaseFieldBean getLocationFilterTextField() {
		return locationFilterTextField;
	}

	public void setLocationFilterTextField(
			UpperCaseFieldBean locationFilterTextField) {
		this.locationFilterTextField = locationFilterTextField;
	}

	public void setLocationScreenTablePane(
			ObjectTablePane<QiInspectionLocation> locationScreenTablePane) {
		this.locationScreenTablePane = locationScreenTablePane;
	}

}
