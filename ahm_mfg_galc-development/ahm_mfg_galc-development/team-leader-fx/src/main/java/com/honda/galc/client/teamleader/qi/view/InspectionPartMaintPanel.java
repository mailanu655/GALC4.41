package com.honda.galc.client.teamleader.qi.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.InspectionPartController;
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
import com.honda.galc.entity.qi.QiInspectionPart;

/**
 * 
 * <h3>InspectionPartMaintPanel Class description</h3>
 * <p>
 * InspectionPartMaintPanel is used to create the the RaioButtons (All, Activate,Reactivate) ,Filter ,TableView and populate data in TableView
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
public class InspectionPartMaintPanel extends QiAbstractTabbedView<ItemMaintenanceModel, InspectionPartController>{
	
	private UpperCaseFieldBean partFilterTextField;
	private ObjectTablePane<QiInspectionPart> partScreenTablePane;
	
	public InspectionPartMaintPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
	}
	
	/**
	 * This method is used to create layout of the Inspection Part Maintenance Parent screen
	 */
	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		Label inspectionPartFilterLabel = UiFactory.createLabel("label", "Filter");
		HBox outerContainer = new HBox();
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		double width = screenBounds.getWidth()/2;
		HBox radioButtonContainer = createFilterRadioButtons(getController(), width);
		HBox filterContainer = new HBox();
		partFilterTextField=createFilterTextField("filter-textfield", 25, getController());
		filterContainer.getChildren().addAll(inspectionPartFilterLabel,partFilterTextField);
		filterContainer.setSpacing(10);
		filterContainer.setPadding(new Insets(10));
		filterContainer.setAlignment(Pos.CENTER_RIGHT);
		filterContainer.setPrefWidth(width);
		outerContainer.getChildren().addAll(radioButtonContainer,filterContainer);
		partScreenTablePane = createPartTablePane();
		this.setTop(outerContainer);
		this.setCenter(partScreenTablePane);
	}

	
	/**
	 * This method is used to create a Table Pane.
	 * @return
	 */
	private ObjectTablePane<QiInspectionPart> createPartTablePane(){ 
		ColumnMappingList columnMappingList = ColumnMappingList.with("Name", "inspectionPartName")
		.put("Abbr", "inspectionPartDescShort")
		.put("Description","inspectionPartDescLong")
		.put("Part Class", "partClass")
		.put("Status\nActive=1\nInactive=0", "status")
		.put("Hierarchy\nValue", "hierarchy")
		.put("Allow\nMultiple\nYes=1\nNo=0", "allowMltpl")
		.put("Position\nPrimary=1\nSecondary=0","position")
		.put("Created\nBy", "createUser")
		.put("Created\nTimestamp", "createDate")
		.put("Updated\nBy", "updateUser")
		.put("Updated\nTimestamp","updateDate");
		
		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		Double[] columnWidth = new Double[] {
				0.08,0.05,0.1,0.05,0.05,0.1,0.05,0.1,0.05,0.1,0.1,0.13
			};
		ObjectTablePane<QiInspectionPart> panel = new ObjectTablePane<QiInspectionPart>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		LoggedTableColumn<QiInspectionPart, Integer> column = new LoggedTableColumn<QiInspectionPart, Integer>();
		createSerialNumber(column);
		panel.getTable().getColumns().add(0, column);
		panel.getTable().getColumns().get(0).setText("#");
		panel.getTable().getColumns().get(0).setResizable(true);
		panel.getTable().getColumns().get(0).setMaxWidth(100);
		panel.getTable().getColumns().get(0).setMinWidth(1);
		return panel;
	}
	/**
	 * This method is used to call when user switched to others tabs
	 */
	public void onTabSelected() {
		if(getAllRadioBtn().isSelected())
			reload(getPartFilterTextField().getText());
		else if(getActiveRadioBtn().isSelected())
			reload(getPartFilterTextField().getText(),QiActiveStatus.ACTIVE.getName());
		else 
			reload(getPartFilterTextField().getText(),QiActiveStatus.INACTIVE.getName());
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
		partScreenTablePane.setDataAndLazyList(getModel().findPartsByFilter(""));
	}
	/**
	 * This method is used to refresh the screen based on filter
	 * @param filter
	 */
	public void reload(String filter) {
		partScreenTablePane.setDataAndLazyList(getModel().findPartsByFilter(filter));
	}

	/**
	 * This method is used to refresh the screen based on filter
	 * @param filter
	 */
	public void reload(String filter, boolean isPreserveListPosition, boolean isPreserveSelect) {
		partScreenTablePane.setData(getModel().findPartsByFilter(filter), getModel().getLazyLoadDisplayRows(), isPreserveListPosition, isPreserveSelect);
	}
	/**
	 * This method is used to refresh the screen based on the filter value and status
	 * @param filter
	 * @param status
	 */
	public void reload(String filter,String status) {
		partScreenTablePane.setData(getModel().findPartsByFilter(filter,status));
	}
	/**
	 * This method is used to refresh the screen based on the filter value and status
	 * @param filter
	 * @param status
	 */
	public void reload(String filter,String status, boolean isPreserveListPosition, boolean isPreserveSelect) {
		partScreenTablePane.setData(getModel().findPartsByFilter(filter,status), getModel().getLazyLoadDisplayRows(), isPreserveListPosition, isPreserveSelect);
	}
	
	@Override
	public void start() {
	}

	public String getScreenName() {
		return QiRegionalScreenName.INSPECTION_PART.getScreenName();
	}
	public ObjectTablePane<QiInspectionPart> getEntryScreenTablePane() {
		return partScreenTablePane;
	}
	public String getFilterTextData()
	{
		return StringUtils.trimToEmpty(partFilterTextField.getText());
	}

	public UpperCaseFieldBean getPartFilterTextField() {
		return partFilterTextField;
	}

	public ObjectTablePane<QiInspectionPart> getPartScreenTablePane() {
		return partScreenTablePane;
	}
}
