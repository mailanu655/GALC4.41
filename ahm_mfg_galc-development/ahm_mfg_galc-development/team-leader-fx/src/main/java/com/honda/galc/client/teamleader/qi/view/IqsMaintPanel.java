package com.honda.galc.client.teamleader.qi.view;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.IqsMaintController;
import com.honda.galc.client.teamleader.qi.model.IqsMaintenanceModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.qi.QiIqs;
import com.honda.galc.entity.qi.QiIqsCategory;
import com.honda.galc.entity.qi.QiIqsQuestion;
import com.honda.galc.entity.qi.QiIqsVersion;

/**
 * 
 * <h3>IqsMaintPanel Class description</h3>
 * <p> IqsMaintPanel description </p>
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
 * Aug 29, 2016
 *
 *
 */
public class IqsMaintPanel extends QiAbstractTabbedView<IqsMaintenanceModel, IqsMaintController> {

	private ObjectTablePane<QiIqsCategory> iqsCategoryTablePane;
	private ObjectTablePane<QiIqsVersion> iqsVersionTablePane;
	private ObjectTablePane<QiIqsQuestion> iqsQuestionTablePane;
	private ObjectTablePane<QiIqs> iqsAssociationTablePane;
	private LoggedButton importButton;
	
	boolean isRefresh=false;


	public IqsMaintPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
	}

	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		VBox outerPane = new VBox();
		HBox importDataContainer = new HBox();
		HBox masterTableContainer = new HBox();
		HBox associationtableContainer = new HBox();
		importButton = UiFactory.createButton("Import IQS data","importbutton");
		importButton.setOnAction(getController());

		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		double width = primaryScreenBounds.getWidth()/2;
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		HBox radioButtonContainer =createFilterRadioButtons(getController(), width);

		importDataContainer.setSpacing(20);
		importDataContainer.setPadding(new Insets(10));
		importDataContainer.setAlignment(Pos.CENTER_RIGHT);
		if(!isFullAccess()){
			importButton.setDisable(true);
		}
		importDataContainer.getChildren().addAll(importButton);
		iqsVersionTablePane = createIqsVersionTablePane();
		iqsVersionTablePane.setPadding(new Insets(10));
		iqsCategoryTablePane = createIqsCategoryTablePane();
		iqsCategoryTablePane.setPadding(new Insets(10));
		iqsQuestionTablePane = createIqsQuestionTablePane();
		iqsQuestionTablePane.setPadding(new Insets(10));
		iqsAssociationTablePane = createIqsAssociationTablePane();
		iqsAssociationTablePane.setPadding(new Insets(10));
		masterTableContainer.getChildren().addAll(iqsVersionTablePane, iqsCategoryTablePane,iqsQuestionTablePane);
		associationtableContainer.getChildren().addAll(iqsAssociationTablePane);

		outerPane.getChildren().addAll(masterTableContainer,radioButtonContainer,associationtableContainer);
		this.setTop(importDataContainer);
		this.setCenter(outerPane);
	}

	/**
	 * This method is used to create IQS Version Table Pane.
	 */
	private ObjectTablePane<QiIqsVersion> createIqsVersionTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("IQS Version", "iqsVersion");
		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		ObjectTablePane<QiIqsVersion> panel = new ObjectTablePane<QiIqsVersion>(columnMappingList);
		panel.getTable().getColumns().get(0).setPrefWidth(240);
		panel.setConstrainedResize(false);
		return panel;
	}

	/**
	 * This method is used to create IQS Category Table Pane.
	 */
	private ObjectTablePane<QiIqsCategory> createIqsCategoryTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("IQS Category", "iqsCategory");
		ObjectTablePane<QiIqsCategory> panel = new ObjectTablePane<QiIqsCategory>(columnMappingList);
		panel.getTable().getColumns().get(0).setPrefWidth(240);
		panel.setConstrainedResize(false);
		return panel;
	}

	/**
	 * This method is used to create IQS Question Table Pane.
	 */
	private ObjectTablePane<QiIqsQuestion> createIqsQuestionTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("IQS Question No", "id.iqsQuestionNo")
				.put("IQS Question","id.iqsQuestion");

		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		Double[] columnWidth = new Double[]{0.09,0.50};
		ObjectTablePane<QiIqsQuestion> panel = new ObjectTablePane<QiIqsQuestion>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		return panel;
	}

	/**
	 * This method is used to create IQS Association Table Pane.
	 */
	private ObjectTablePane<QiIqs> createIqsAssociationTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("IQS Version", "iqsVersion")
				.put("IQS Category", "iqsCategory")
				.put("IQS Question No", "iqsQuestionNo")
				.put("IQS Question","iqsQuestion")
				.put("Status"+"\n"+"ACTIVE=1"+"\n"+"INACTIVE=0"+"\n","status");

		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		Double[] columnWidth = new Double[] {0.10,0.25,0.10,0.40,0.13};
		ObjectTablePane<QiIqs> panel = new ObjectTablePane<QiIqs>(columnMappingList,columnWidth);

		panel.setConstrainedResize(false);
		return panel;
	}
	/**
	 * This method refresh the data on tab selection.
	 */
	public void onTabSelected() {
		QiIqsCategory categorySelectedItem=getIqsCategoryTablePane().getTable().getSelectionModel().getSelectedItem();
		int questionSelectedItem=getIqsQuestionTablePane().getTable().getSelectionModel().getSelectedIndex();
		QiIqsVersion versionSelectedItem=getIqsVersionTablePane().getTable().getSelectionModel().getSelectedItem();
		reload();
		getIqsCategoryTablePane().getTable().getSelectionModel().select(categorySelectedItem);
		getIqsQuestionTablePane().getTable().getSelectionModel().select(questionSelectedItem);
		getIqsVersionTablePane().getTable().getSelectionModel().select(versionSelectedItem);
	}

	public String getScreenName() {
		return "IQS";
	}

	@Override
	public void reload() {
		isRefresh=true;
		iqsVersionTablePane.setData(getModel().findAllIqsVersion());
		iqsCategoryTablePane.setData(getModel().findAllIqsCategory());
		iqsQuestionTablePane.setData(getModel().findAllIqsQuestion());
		iqsAssociationTablePane.setData(getModel().findAllIqsAssociatedData(getSelectedRadioButtonValue()));
		isRefresh=false;
	}
	/**
	 * This method refresh the IQS Association Table
	 * @param qiIqs
	 * @return
	 */
	public void reload(QiIqs qiIqs){
		iqsAssociationTablePane.setData(getModel().findAssociationForSelectedValue(qiIqs,getSelectedRadioButtonValue()));
	}

	/**
	 * This method return a list of values based on selected radio buttons (e.g. Active - 1, Inactive - 0, All - 0 & 1)
	 * @return
	 */
	private List<Short> getSelectedRadioButtonValue() {
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

	public ObjectTablePane<QiIqsVersion> getIqsVersionTablePane() {
		return iqsVersionTablePane;
	}

	public ObjectTablePane<QiIqsCategory> getIqsCategoryTablePane() {
		return iqsCategoryTablePane;
	}

	public ObjectTablePane<QiIqsQuestion> getIqsQuestionTablePane() {
		return iqsQuestionTablePane;
	}

	public ObjectTablePane<QiIqs> getIqsAssociationTablePane() {
		return iqsAssociationTablePane;
	}

	public LoggedButton getImportButton() {
		return importButton;
	}

	public void setImportButton(LoggedButton importButton) {
		this.importButton = importButton;
	}

	public boolean isRefresh() {
		return isRefresh;
	}

}
