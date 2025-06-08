package com.honda.galc.client.teamleader.qi.view;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.DefectController;
import com.honda.galc.client.teamleader.qi.enumtype.QiRegionalScreenName;
import com.honda.galc.client.teamleader.qi.model.ItemMaintenanceModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.qi.QiDefect;

/**
 * 
 * <h3>DefectMaintPanel Class description</h3>
 * <p> DefectMaintPanel description </p>
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
 * @author L&T Infotech<br>
 * Aug 1, 2016
 *
 *
 */

public class DefectMaintPanel extends QiAbstractTabbedView<ItemMaintenanceModel, DefectController>{

	private UpperCaseFieldBean defectFilterTextField;
	private ObjectTablePane<QiDefect> defectTablePane;

	public DefectMaintPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
	}

	/**
	 * This method creates layout of the Defect Maintenance Parent screen
	 */
	public void initView() {
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		double width = primaryScreenBounds.getWidth()/2;
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		HBox outerContainer = new HBox();
		HBox filterContainer = new HBox();
		HBox radioButtonContainer =createFilterRadioButtons(getController(), width);
		LoggedLabel filterLabel = UiFactory.createLabel("filterLabel", "Filter");
		filterLabel.getStyleClass().add("display-label");
		defectFilterTextField = createFilterTextField("filter-textfield", 25, getController());
		filterContainer.getChildren().addAll(filterLabel,defectFilterTextField);
		filterContainer.setSpacing(10);
		filterContainer.setPadding(new Insets(10));
		filterContainer.setAlignment(Pos.CENTER_RIGHT);
		filterContainer.setPrefWidth(width);

		outerContainer.getChildren().addAll(radioButtonContainer,filterContainer);
		outerContainer.setAlignment(Pos.CENTER_LEFT);

		defectTablePane = createDefectTablePane();

		LoggedTableColumn<QiDefect, Integer> column = new LoggedTableColumn<QiDefect, Integer>();

		createSerialNumber(column);
		defectTablePane.getTable().getColumns().add(0, column);
		defectTablePane.getTable().getColumns().get(0).setText("#");
		defectTablePane.getTable().getColumns().get(0).setResizable(true);
		defectTablePane.getTable().getColumns().get(0).setMaxWidth(100);
		defectTablePane.getTable().getColumns().get(0).setMinWidth(1);
		this.setTop(outerContainer);
		this.setCenter(defectTablePane);
	}


	/**
	 * This method is used to create a Table Pane.
	 * @return
	 */
	private ObjectTablePane<QiDefect> createDefectTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Name", "defectTypeName")
				.put("Abbr", "defectTypeDescriptionShort")
				.put("Description","defectTypeDescriptionLong")
				.put("Defect Category", "defectCategoryName")
				.put("Status"+"\n"+"ACTIVE=1"+"\n"+"INACTIVE=0"+"\n","status")
				.put("Position"+"\n"+"PRIMARY=1"+"\n"+"SECONDARY=0"+"\n","position")
				.put("Created"+"\n"+"    By", "createUser")
				.put("Created Timestamp","createDate")
				.put("Updated"+"\n"+"    By", "updateUser")
				.put("Updated Timestamp","updateDate");

		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		Double[] columnWidth = new Double[] {
				0.10, 0.1, 0.1, 0.1,0.1,0.05,0.07,0.1,0.075,0.13
		}; 
		ObjectTablePane<QiDefect> panel = new ObjectTablePane<QiDefect>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		return panel;
	}

	/**
	 * This method is used to refresh the screen on Tab selection
	 */
	public void onTabSelected() {
		reload(StringUtils.trim(defectFilterTextField.getText()));
		getController().clearDisplayMessage();
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
		defectTablePane.setDataAndLazyList(getModel().getDefectByFilter("",getSelectedRadioButtonValue()));
	}

	/**
	 * This method is used to refresh the screen 
	 * @param filter
	 * @param status
	 */
	public void reload(String filter) {
		defectTablePane.setDataAndLazyList(getModel().getDefectByFilter(filter,getSelectedRadioButtonValue()));
	}
	/**
	 * This method is used to refresh the screen 
	 * @param filter
	 * @param status
	 */
	public void reload(String filter, boolean isPreserveListPosition, boolean isPreserveSelect) {
		defectTablePane.setData(getModel().getDefectByFilter(filter,getSelectedRadioButtonValue()), getModel().getLazyLoadDisplayRows(), isPreserveListPosition, isPreserveSelect);
	}

	/**
	 * This method return a list of values based on selected radio buttons (e.g. Active - 1, Inactive - 0, All - 0 & 1)
	 * @return
	 */
	public List<Short> getSelectedRadioButtonValue() {
		List<Short> statusList = new ArrayList<Short>();
		if(getAllRadioBtn().isSelected()) {
			statusList.add((short)1);
			statusList.add((short)0);
		} else {
			if(getActiveRadioBtn().isSelected())
				statusList.add((short)1);
			else
				statusList.add((short)0);
			statusList.add((short)2);
		}
		return statusList;
	}

	@Override
	public void start() {
	}

	public String getScreenName() {
		return QiRegionalScreenName.DEFECT_MAINTENANCE.getScreenName();
	}

	public ObjectTablePane<QiDefect> getDefectTablePane() {
		return defectTablePane;
	}

	public String getFilterTextData()
	{
		return StringUtils.trimToEmpty(defectFilterTextField.getText());
	}


	public UpperCaseFieldBean getDefectFilterTextField() {
		return defectFilterTextField;
	}

	public void setDefectFilterTextField(UpperCaseFieldBean defectFilterTextField) {
		this.defectFilterTextField = defectFilterTextField;
	}
}
