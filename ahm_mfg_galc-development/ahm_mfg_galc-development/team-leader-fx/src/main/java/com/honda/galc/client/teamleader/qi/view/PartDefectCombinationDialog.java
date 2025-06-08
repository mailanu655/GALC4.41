package com.honda.galc.client.teamleader.qi.view;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.controller.PartDefectCombinationController;
import com.honda.galc.client.teamleader.qi.controller.PartDefectCombinationController.ListType;
import com.honda.galc.client.teamleader.qi.controller.PartDefectCombinationDialogController;
import com.honda.galc.client.teamleader.qi.model.PartDefectCombMaintenanceModel;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.CreateUserAuditEntry;
import com.honda.galc.entity.qi.QiDefect;
import com.honda.galc.entity.qi.QiPartDefectCombination;
import com.honda.galc.entity.qi.QiPartLocationCombination;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PartDefectCombinationDialog</code> is the class for Part Defect Combination Dialog.
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
 * <TD>26/08/2016</TD>
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
public class PartDefectCombinationDialog extends QiFxDialog<PartDefectCombMaintenanceModel>{

	private LoggedButton createBtn;
	private LoggedButton cancelBtn;

	private LoggedTextArea reasonForChangeTextArea;
	
	private UpperCaseFieldBean fullPartFilterTextField;
	private UpperCaseFieldBean primaryDefectFilterTextField;
	private UpperCaseFieldBean secondaryDefectFilterTextField;

	private ObjectTablePane<QiPartLocationCombination> fullPartTablePane;
	private ObjectTablePane<QiDefect> primaryDefectTablePane;
	private ObjectTablePane<QiDefect> secondaryDefectTablePane;

	private String title;
	PartDefectCombinationDialogController controller;
	QiPartDefectCombination selectedComb;
	QiPartLocationCombination partLocComb;
	
	private ListType listType = ListType.BOTH;
	private volatile boolean isCancel = false;

	public PartDefectCombinationDialog(String title, QiPartDefectCombination selectedComb, PartDefectCombMaintenanceModel model,String applicationId, PartDefectCombinationController parentController) {

		super(title,applicationId,model);//Fix : Passing applicationId as parameter to fetch correct owner stage.
		this.title = title;
		this.selectedComb = selectedComb;
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.controller = new PartDefectCombinationDialogController(model, this, selectedComb, parentController);
		initComponents();
		if(this.title.equalsIgnoreCase(QiConstant.CREATE))
			loadCreateData();
		else if(this.title.equalsIgnoreCase(QiConstant.UPDATE))
			loadUpdateData();
		controller.initListeners();
		isCancel = false;
	}

	public String getReasonForChangeText()
	{
		return StringUtils.trim(reasonForChangeTextArea.getText());
	}

	public UpperCaseFieldBean getPrimaryDefectFilterTextField() {
		return primaryDefectFilterTextField;
	}

	public UpperCaseFieldBean getFullPartFilterTextField() {
		return fullPartFilterTextField;
	}

	public UpperCaseFieldBean getSecondaryDefectFilterTextField() {
		return secondaryDefectFilterTextField;
	}

	public ObjectTablePane<QiDefect> getPrimaryDefectTablePane() {
		return primaryDefectTablePane;
	}
	
	public ObjectTablePane<QiDefect> getSecondaryDefectTablePane() {
		return secondaryDefectTablePane;
	}

	public ObjectTablePane<QiPartLocationCombination> getFullPartTablePane() {
		return fullPartTablePane;
	}

	public LoggedButton getCreateBtn() {
		return createBtn;
	}

	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}

	public ListType getListType() {
		return listType;
	}

	public void setListType(ListType newType) {
		this.listType = newType;
	}

	/**
	 * @return the isCancel
	 */
	public boolean isCancel() {
		return isCancel;
	}

	/**
	 * @param isCancel the isCancel to set
	 */
	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}
	
	/**
	 * This method is used to initialize the components of Dialog screen.
	 */
	private void initComponents() {
		VBox outerPane = new VBox();
		outerPane.setPrefHeight(670);
		HBox mainPane = new HBox();
		VBox defectPane = new VBox();
		HBox buttonContainer = new HBox();
		HBox radioBtnContainer = createStatusRadioButtons(controller);
		radioBtnContainer.setPadding(new Insets(0, 10, 10, 0));
		HBox reasonForChangeContainer = new HBox();

		reasonForChangeTextArea = UiFactory.createTextArea();
		reasonForChangeTextArea.setWrapText(true);
		reasonForChangeTextArea.setPrefRowCount(2);
		reasonForChangeTextArea.setPrefColumnCount(40);
		reasonForChangeTextArea.setPrefWidth(575);
		reasonForChangeTextArea.addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(256));
		
		defectPane.getChildren().add(createTitiledPane("Primary Defect", createPrimaryDefectPanel(), 300, 250));
		defectPane.getChildren().add(createTitiledPane("Secondary Defect", createSecondaryDefectPanel(), 300, 250));
		defectPane.setSpacing(10);
		
		mainPane.setSpacing(10);
		mainPane.getChildren().add(createTitiledPane("QICS Full Part Name", createPartPanel(), 700, 510));
		mainPane.getChildren().add(defectPane);
		
		HBox labelBox = new HBox();
		LoggedLabel reasonForChangeLabel = UiFactory.createLabel("reasonForChangeLabel", "Reason for Change");
		reasonForChangeLabel.getStyleClass().add("display-label");
		LoggedLabel asterisk = UiFactory.createLabel("asteriskReasonForChange", "*");
		asterisk.getStyleClass().add("display-label");
		asterisk.setStyle("-fx-text-fill: red");
		labelBox.getChildren().addAll(reasonForChangeLabel,asterisk);

		reasonForChangeContainer.setPadding(new Insets(10, 10, 10, 0));
		reasonForChangeContainer.setSpacing(10);
		createBtn = createBtn(QiConstant.CREATE, controller);
		updateButton = createBtn(QiConstant.UPDATE, controller);
		cancelBtn = createBtn(QiConstant.CANCEL, controller);
		reasonForChangeContainer.getChildren().addAll(labelBox, reasonForChangeTextArea,createBtn, updateButton, cancelBtn);
		reasonForChangeContainer.setAlignment(Pos.CENTER);
		outerPane.getChildren().addAll(radioBtnContainer, mainPane, reasonForChangeContainer, buttonContainer);
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);
	}
	
	/**
	 * This method is used to create Part panel.
	 * @return
	 */
	private VBox createPartPanel() {
		
		VBox pane = new VBox();
		HBox filterBox = new HBox();
		Label fullPartLabel = UiFactory.createLabel("fullPartLabel", "Filter");
		fullPartLabel.getStyleClass().add("display-label");
		fullPartFilterTextField = createFilterTextField("fullPartFilterTextField", 18, controller);
		fullPartTablePane = createPartPane();
		fullPartTablePane.setPrefHeight(500);
		filterBox.getChildren().addAll(fullPartLabel,fullPartFilterTextField);
		filterBox.setSpacing(10);
		filterBox.setAlignment(Pos.CENTER_RIGHT);
		filterBox.setPadding(new Insets(0, 0, 5, 0));
		
		pane.getChildren().addAll(filterBox, fullPartTablePane);
		return pane;
	}
	
	/**
	 * This method is used to create Primary Defect panel.
	 * @return
	 */
	private VBox createPrimaryDefectPanel() {
		
		VBox pane = new VBox();
		HBox filterBox = new HBox();
		Label defectLabel = UiFactory.createLabel("fullPartLabel", "Filter");
		defectLabel.getStyleClass().add("display-label");
		primaryDefectFilterTextField = createFilterTextField("primaryDefectFilterTextField", 18, controller);
		primaryDefectTablePane = createDefectPane();
		
		filterBox.getChildren().addAll(defectLabel, primaryDefectFilterTextField);
		filterBox.setSpacing(10);
		filterBox.setAlignment(Pos.CENTER_RIGHT);
		filterBox.setPadding(new Insets(0, 0, 5, 0));
		
		pane.getChildren().addAll(filterBox, primaryDefectTablePane);
		return pane;
	}
	
	/**
	 * This method is used to create Secondary Defect panel.
	 * @return
	 */
	private VBox createSecondaryDefectPanel() {
		
		VBox pane = new VBox();
		HBox filterBox = new HBox();
		Label defectLabel = UiFactory.createLabel("fullPartLabel", "Filter");
		defectLabel.getStyleClass().add("display-label");
		secondaryDefectFilterTextField = createFilterTextField("secondaryDefectFilterTextField", 18, controller);
		secondaryDefectTablePane = createDefectPane();
		
		filterBox.getChildren().addAll(defectLabel, secondaryDefectFilterTextField);
		filterBox.setSpacing(10);
		filterBox.setAlignment(Pos.CENTER_RIGHT);
		filterBox.setPadding(new Insets(0, 0, 5, 0));
		
		pane.getChildren().addAll(filterBox, secondaryDefectTablePane);
		return pane;
	}
	
	/**
	 * This method is used to create TitledPane.
	 * @param title
	 * @param content
	 * @return
	 */
	private TitledPane createTitiledPane(String title,Node content, double width, double height) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(content);
		titledPane.setPrefSize(width, height);
		return titledPane;
	}
	
	/**
	 * This method is used to return ObjectTablePane for Part.
	 * @return
	 */
	private ObjectTablePane<QiPartLocationCombination> createPartPane() {
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("QICS Part Name", "inspectionPartName")
				.put("QICS Full Part Name", "fullPartDesc");
		
		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		Double[] columnWidth = new Double[] {
				0.15, 0.343
			}; 
		ObjectTablePane<QiPartLocationCombination> panel = new ObjectTablePane<QiPartLocationCombination>(columnMappingList, columnWidth);
		panel.setConstrainedResize(false);
		if(title.equalsIgnoreCase(QiConstant.CREATE))
			panel.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		else if(title.equalsIgnoreCase(QiConstant.UPDATE))
			panel.getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		return panel;
	}
	
	/**
	 * This method is used to return ObjectTablePane for Defect.
	 * @return
	 */
	private ObjectTablePane<QiDefect> createDefectPane() {
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Defect", "defectTypeName")
				.put("Abbr", "defectTypeDescriptionShort");
		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		ObjectTablePane<QiDefect> panel = new ObjectTablePane<QiDefect>(columnMappingList);
		panel.setConstrainedResize(false);
		panel.getTable().getColumns().get(0).setPrefWidth(136);
		panel.getTable().getColumns().get(1).setPrefWidth(136);
		if(title.equalsIgnoreCase(QiConstant.CREATE))
			panel.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		else if(title.equalsIgnoreCase(QiConstant.UPDATE))
			panel.getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		return panel;
	}

	public void reload() throws Exception {
		List<Short> statusList = new ArrayList<Short>();
		statusList.add((short)1);
		statusList.add((short)2);
		if(title.equalsIgnoreCase(QiConstant.CREATE)) {
			fullPartTablePane.setData(getModel().getPartLocCombByFilter(StringUtils.EMPTY, statusList));
			primaryDefectTablePane.setData(getModel().getActivePrimaryDefectByFilter(StringUtils.EMPTY));
			secondaryDefectTablePane.setData(getModel().getActiveSecondaryDefectByFilter(StringUtils.EMPTY));
		} else if(title.equals(QiConstant.UPDATE)) {
			fullPartTablePane.setData(getModel().getPartLocCombByFilter(StringUtils.trimToEmpty(partLocComb.getFullPartDesc()), statusList));
			primaryDefectTablePane.setData(getModel().getActivePrimaryDefectByFilter(StringUtils.trimToEmpty(selectedComb.getDefectTypeName())));
			secondaryDefectTablePane.setData(getModel().getActiveSecondaryDefectByFilter(StringUtils.trimToEmpty(selectedComb.getDefectTypeName2())));
		}
	}
	/**
	 * This method is used to load Create data.
	 */
	private void loadCreateData()
	{
		try {
			selectedComb = new QiPartDefectCombination();
			updateButton.setDisable(true);
			reasonForChangeTextArea.setDisable(true);
			reload();
		} catch (Exception e) {
			controller.handleException("An error occured in loading pop up screen ", "Failed to Open Create Part Defect Combination popup screen", e);
		}
	}
	/**
	 * This method is used to load Update data.
	 */
	private void loadUpdateData()
	{
		try {
			partLocComb = getModel().findPartLocCombByKey(selectedComb.getPartLocationId());
			createBtn.setDisable(true);
			reload();

			getActiveRadioBtn().setSelected(selectedComb.isActive());
			getInactiveRadioBtn().setSelected(!selectedComb.isActive());

			setSelectionInPartTable(partLocComb, fullPartTablePane, fullPartFilterTextField);
			setSelectionInDefectTable(getModel().getActivePrimaryDefectByFilter(selectedComb.getDefectTypeName()), 
					primaryDefectTablePane, primaryDefectFilterTextField, selectedComb.getDefectTypeName());
			setSelectionInDefectTable(getModel().getActiveSecondaryDefectByFilter(selectedComb.getDefectTypeName2()), 
					secondaryDefectTablePane, secondaryDefectFilterTextField, selectedComb.getDefectTypeName2());

		} catch (Exception e) {
			controller.handleException("An error occured in loading pop up screen ", "Failed to Open Update Part Defect Combination popup screen", e);
		}
	}
	/**
	 * This method is used to set selection in Part table.
	 */
	private void setSelectionInPartTable(QiPartLocationCombination comb, ObjectTablePane<QiPartLocationCombination> partTablePane, UpperCaseFieldBean filterTxt) {
		filterTxt.setText(StringUtils.trimToEmpty(comb.getFullPartDesc()));
		if(comb != null && comb.isActive())
			partTablePane.getTable().getSelectionModel().select(comb);
	}
	/**
	 * This method is used to set selection in Defect tables.
	 */
	private void setSelectionInDefectTable(List<QiDefect> defectList, ObjectTablePane<QiDefect> defectTablePane, UpperCaseFieldBean filterTxt, String key) {
		QiDefect defect = getObjectfromList(defectList, key);
		filterTxt.setText(StringUtils.trimToEmpty(key));
		if(defect != null && defect.isActive())
			defectTablePane.getTable().getSelectionModel().select(defect);
	}
	/**
	 * This method is used to get specific object from list.
	 * @param list
	 * @param key
	 * @return
	 */
	public <T extends CreateUserAuditEntry>T getObjectfromList(List<T> list, String key)
	{
		for (T item : list) {
			if(item.getId().equals(key))
				return item;
		}
		return null;
	}

}
