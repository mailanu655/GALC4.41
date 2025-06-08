package com.honda.galc.client.teamleader.qi.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.RepairMethodController;
import com.honda.galc.client.teamleader.qi.enumtype.QiRegionalScreenName;
import com.honda.galc.client.teamleader.qi.model.RepairMethodMaintenanceModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.qi.QiRepairMethod;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;

/**
 * 
 * <h3>RepairMethod Maintenance Panel Class description</h3>
 * <p> RepairMethod Maintenance Panel description </p>
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * </TABLE>
 * @author L&T Infotech<br>
 * Aug 1, 2016
 */
public class RepairMethodMaintenancePanel extends QiAbstractTabbedView<RepairMethodMaintenanceModel,RepairMethodController>{

	private UpperCaseFieldBean repairMethodNameFilterTextField;
	private ObjectTablePane<QiRepairMethod> repairMethodTablePane;
	
	public RepairMethodMaintenancePanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
	}
	
	/**
	 * This method creates layout of the Repair Method Maintenance Parent screen
	 */

	@Override
	public void initView() {
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		double width = primaryScreenBounds.getWidth()/2;
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		LoggedLabel repairMethodNameFilterLabel = UiFactory.createLabel("repairMethodNameFilterLabel", "Filter");
		repairMethodNameFilterLabel.getStyleClass().add("display-label");
		HBox filterContainer = new HBox();		
		HBox mainFilterContainer = new HBox();
		repairMethodNameFilterTextField= createFilterTextField("filterTextField", 25, getController());
		HBox radioButtonContainer =createFilterRadioButtons(getController(), width);
		filterContainer.getChildren().addAll(repairMethodNameFilterLabel, repairMethodNameFilterTextField);
		filterContainer.setSpacing(10);
		filterContainer.setPadding(new Insets(10));
		filterContainer.setAlignment(Pos.CENTER_RIGHT);
		filterContainer.setPrefWidth(width);
		
		mainFilterContainer.getChildren().addAll(radioButtonContainer,filterContainer);
        mainFilterContainer.setAlignment(Pos.CENTER_LEFT);
		repairMethodTablePane = createRepairMethodTablePane();		
		LoggedTableColumn<QiRepairMethod, Integer> column = new LoggedTableColumn<QiRepairMethod, Integer>();		
		createSerialNumber(column);
		setRepairMethodTablePane(column);
		this.setTop(mainFilterContainer);
		this.setCenter(repairMethodTablePane);
	}

	private void setRepairMethodTablePane(LoggedTableColumn<QiRepairMethod, Integer> column) {
		repairMethodTablePane.getTable().getColumns().add(0, column);
		repairMethodTablePane.getTable().getColumns().get(0).setText("#");
		repairMethodTablePane.getTable().getColumns().get(0).setResizable(true);
		repairMethodTablePane.getTable().getColumns().get(0).setMaxWidth(45);
		repairMethodTablePane.getTable().getColumns().get(0).setMinWidth(1);
	}

	

	/**
	 * This method is used to create a Table Pane.
	 * @return
	 */
	
	private ObjectTablePane<QiRepairMethod> createRepairMethodTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Repair Method Name", "repairMethod")
				.put("Repair Method Description","repairMethodDescription")
				.put("Status"+"\n"+"ACTIVE=1"+"\n"+"INACTIVE=0"+"\n", "status")
				.put("Created By", "createUser")
				.put("Created TimeStamp", "createDate")
				.put("Updated By", "updateUser")
				.put("Updated TimeStamp", "updateDate");

		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		Double[] columnWidth = new Double[] {
				0.25,0.27, 0.08, 0.06, 0.12, 0.06, 0.11
		}; 
		ObjectTablePane<QiRepairMethod> panel = new ObjectTablePane<QiRepairMethod>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		return panel;
	}

	/**
	 * This method is used to refresh the screen on Tab selection
	 */
	
	public void onTabSelected() {		
		reload(StringUtils.trim(repairMethodNameFilterTextField.getText()));
	}
	
	/**
	 * This method is used to refresh the screen 
	 */

	@Override
	public void reload() {
		repairMethodTablePane.setData(getModel().findAllRepairMethods("", getSelectedRadioButtonValue()));
	}
	
	/**
	 * This method is used to refresh the screen 
	 * @param filter
	 */

	public void reload(String filter) {
		repairMethodTablePane.setData(getModel().findAllRepairMethods(filter, getSelectedRadioButtonValue()));
	}
	
	public List<Short> getSelectedRadioButtonValue() {
		List<Short> statusList = new ArrayList<Short>();
		if(getAllRadioBtn().isSelected()) {
			statusList.add((short)1);
			statusList.add((short)0);
		} else {
			if(getActiveRadioBtn().isSelected())
			{
				statusList.add((short)1);
			}
			else
			{
				statusList.add((short)0);
			}
		}
		return statusList;
	}
	
	@Override
	public void start() {
		
	}


	public ObjectTablePane<QiRepairMethod> getRepairMethodTablePane() {
		return repairMethodTablePane;
	}
	
	public String getFilterTextData()
	{
		return StringUtils.trimToEmpty(repairMethodNameFilterTextField.getText());
	}
	
	public String getScreenName() {
		return QiRegionalScreenName.REPAIR_METHOD.getScreenName();
	}

	public UpperCaseFieldBean getRepairMethodNameFilterTextField() {
		return repairMethodNameFilterTextField;
	}


	public void setRepairMethodNameFilterTextField(UpperCaseFieldBean repairMethodNameFilterTextField) {
		this.repairMethodNameFilterTextField = repairMethodNameFilterTextField;
	}
		
	


	public void setRepairMethodTablePane(ObjectTablePane<QiRepairMethod> repairMethodTablePane) {
		this.repairMethodTablePane = repairMethodTablePane;
	}
	

}
