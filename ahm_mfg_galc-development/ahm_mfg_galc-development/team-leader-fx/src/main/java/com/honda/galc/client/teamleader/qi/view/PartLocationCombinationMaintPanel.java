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
import com.honda.galc.client.teamleader.qi.controller.PartLocationCombinationController;
import com.honda.galc.client.teamleader.qi.enumtype.QiRegionalScreenName;
import com.honda.galc.client.teamleader.qi.model.ItemMaintenanceModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.qi.QiPartLocationCombination;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PartLocationCombinationMaintPanel</code> is the Panel class for Part Location Combination.
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
 * <TD>14/07/2016</TD>
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
public class PartLocationCombinationMaintPanel extends QiAbstractTabbedView<ItemMaintenanceModel, PartLocationCombinationController> {
	
	private UpperCaseFieldBean fullPartNameFilterTextField;
	
	private ObjectTablePane<QiPartLocationCombination> partLocationCombinationTablePane;
	
	public PartLocationCombinationMaintPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
	}
	
	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		LoggedLabel fullPartNameFilterLabel = UiFactory.createLabel("fullPartNameFilterLabel", "Filter");
		fullPartNameFilterLabel.getStyleClass().add("display-label");
		HBox mainTopContainer = new HBox();
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		double width = screenBounds.getWidth()/2;
		HBox radioBtnContainer = createFilterRadioButtons(getController(), width);
		HBox filterContainer = new HBox();
		fullPartNameFilterTextField = createFilterTextField("fullPartNameFilterTextField", 25, getController());
		
		filterContainer.getChildren().addAll(fullPartNameFilterLabel, fullPartNameFilterTextField);
		filterContainer.setSpacing(10);
		filterContainer.setPadding(new Insets(10));
		filterContainer.setAlignment(Pos.CENTER_RIGHT);
		filterContainer.setPrefWidth(width);
		
		partLocationCombinationTablePane = createPartLocationCombinationTablePane();
		mainTopContainer.getChildren().addAll(radioBtnContainer, filterContainer);
		this.setTop(mainTopContainer);
		this.setCenter(partLocationCombinationTablePane);
	}

	/**
	 * This method is used to create a Table Pane.
	 * @return
	 */
	private ObjectTablePane<QiPartLocationCombination> createPartLocationCombinationTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("QICS Full Part Name", "fullPartDesc")
						  .put("QICS Part Name", "inspectionPartName")
						  .put("Status\nActive=1\nInactive=0","status");
		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		Double[] columnWidth = new Double[] {
				0.60, 0.30, 0.08
			}; 
		ObjectTablePane<QiPartLocationCombination> panel = new ObjectTablePane<QiPartLocationCombination>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		panel.setId("partLocationCombinationTablePane");
		return panel;
	}
	
	@Override
	public void onTabSelected() {
	}

	@Override
	public ViewId getViewId() {
		return null;
	}

	@Override
	public void reload() {
	}

	public void reload(String filter) {
		partLocationCombinationTablePane.setData(getModel().getPartLocCombByFilter(StringUtils.trim(filter), getSelectedRadioButtonValue()), getModel().getLazyLoadDisplayRows());
	}
	
	public void reload(String filter, boolean isPreserveListPosition, boolean isPreserveSelect) {
		partLocationCombinationTablePane.setData(getModel().getPartLocCombByFilter(StringUtils.trim(filter), getSelectedRadioButtonValue()), getModel().getLazyLoadDisplayRows(), isPreserveListPosition, isPreserveSelect);
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
		return QiRegionalScreenName.PART_LOCATION_COMBINATION.getScreenName();
	}
	
	public ObjectTablePane<QiPartLocationCombination> getPartLocationCombinationTablePane() {
		return partLocationCombinationTablePane;
	}
	
	public UpperCaseFieldBean getFullPartNameFilterTextField() {
		return fullPartNameFilterTextField;
	}
	
	/**
	 * This method is used to return text in filter to the controller.
	 * @return
	 */
	public String getFilterTextData()
	{
		return StringUtils.trimToEmpty(fullPartNameFilterTextField.getText());
	}

	public void setFullPartNameFilterTextField(
			UpperCaseFieldBean fullPartNameFilterTextField) {
		this.fullPartNameFilterTextField = fullPartNameFilterTextField;
	}
}
