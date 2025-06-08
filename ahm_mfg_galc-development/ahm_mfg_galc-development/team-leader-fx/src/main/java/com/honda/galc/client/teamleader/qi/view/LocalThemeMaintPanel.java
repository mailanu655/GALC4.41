package com.honda.galc.client.teamleader.qi.view;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.LocalThemeMaintController;
import com.honda.galc.client.teamleader.qi.model.LocalThemeModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.qi.QiLocalTheme;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;

/**
 * 
 * <h3>LocalThemeMaintPanel Class description</h3>
 * <p>
 * LocalThemeMaintPanel is used to create the the Radio Buttons (All, Activate,Reactivate) ,Filter ,Table View and populate data in TableView
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
 *    May 11, 2017
 */
public class LocalThemeMaintPanel extends QiAbstractTabbedView<LocalThemeModel, LocalThemeMaintController>{
	
	private UpperCaseFieldBean LocalThemeFilterTextField;
	private ObjectTablePane<QiLocalTheme> LocalThemeTablePane;
	
	public LocalThemeMaintPanel(TabbedMainWindow mainWindow) {
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
		LocalThemeFilterTextField=createFilterTextField("filter-textfield", 25, getController());
		filterContainer.getChildren().addAll(inspectionPartFilterLabel,LocalThemeFilterTextField);
		filterContainer.setSpacing(10);
		filterContainer.setPadding(new Insets(10));
		filterContainer.setAlignment(Pos.CENTER_RIGHT);
		filterContainer.setPrefWidth(width);
		outerContainer.getChildren().addAll(radioButtonContainer,filterContainer);
		LocalThemeTablePane = createLocalThemeTablePane();
		this.setTop(outerContainer);
		this.setCenter(LocalThemeTablePane);
	}

	
	/**
	 * This method is used to create a Table Pane.
	 * @return
	 */
	private ObjectTablePane<QiLocalTheme> createLocalThemeTablePane(){ 
		ColumnMappingList columnMappingList = ColumnMappingList.with("Local Theme", "localTheme")
		.put("Description","localThemeDescription")
		.put("Status", "status");
		
		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		Double[] columnWidth = new Double[] { 0.3, 0.5, 0.2 };
		ObjectTablePane<QiLocalTheme> panel = new ObjectTablePane<QiLocalTheme>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		LoggedTableColumn<QiLocalTheme, Integer> column = new LoggedTableColumn<QiLocalTheme, Integer>();
		createSerialNumber(column);
		panel.getTable().getColumns().add(0, column);
		panel.getTable().getColumns().get(0).setText("#");
		panel.getTable().getColumns().get(0).setResizable(true);
		panel.getTable().getColumns().get(0).setMinWidth(0.1);
		return panel;
	}
	
	
	/**
	 * This method is used to call when user switched to others tabs
	 */
	public void onTabSelected() {
		if(getAllRadioBtn().isSelected())
			reload(getPartFilterTextField().getText());
		else
			reload(getPartFilterTextField().getText(), getActiveRadioBtn().isSelected() ? (short) 1 : (short) 0);
		
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
		LocalThemeTablePane.setData(getModel().findAllLocalThemesByFilter(""));
	}
	/**
	 * This method is used to refresh the screen based on filter
	 * @param filter
	 */
	public void reload(String filter) {
		LocalThemeTablePane.setData(getModel().findAllLocalThemesByFilter(filter));
	}

	/**
	 * This method is used to refresh the screen based on the filter value and status
	 * @param filter
	 * @param status
	 */
	public void reload(String filter,short status) {
		LocalThemeTablePane.setData(getModel().findAllLocalThemesByFilterAndStatus(filter,status));
	}
	
	@Override
	public void start() {
	}

	public String getScreenName() {
		return "Local Theme";
	}

	public String getFilterTextData()
	{
		return StringUtils.trimToEmpty(LocalThemeFilterTextField.getText());
	}

	public UpperCaseFieldBean getPartFilterTextField() {
		return LocalThemeFilterTextField;
	}

	public ObjectTablePane<QiLocalTheme> getLocalThemeTablePane() {
		return LocalThemeTablePane;
	}
}
