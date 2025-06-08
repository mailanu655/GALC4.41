package com.honda.galc.client.teamleader.qi.view;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.controller.PartLocationCombinationController;
import com.honda.galc.client.teamleader.qi.controller.PartLocationCombinationDialogController;
import com.honda.galc.client.teamleader.qi.controller.PartDefectCombinationController.ListType;
import com.honda.galc.client.teamleader.qi.model.ItemMaintenanceModel;
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
import com.honda.galc.entity.qi.QiInspectionLocation;
import com.honda.galc.entity.qi.QiInspectionPart;
import com.honda.galc.entity.qi.QiPartLocationCombination;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PartLocationCombinationDialogController</code> is the class for Part Location Combination Dialog.
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
public class PartLocationCombinationDialog extends QiFxDialog<ItemMaintenanceModel>{

	private LoggedButton createBtn;
	private LoggedButton cancelBtn;

	private LoggedLabel fullPartNameValueLabel;
	private LoggedTextArea reasonForChangeTextArea;

	private UpperCaseFieldBean part1FilterTextField;
	private UpperCaseFieldBean part2FilterTextField;
	private UpperCaseFieldBean part3FilterTextField;
	private UpperCaseFieldBean part1Location1FilterTextField;
	private UpperCaseFieldBean part1Location2FilterTextField;
	private UpperCaseFieldBean part2Location1FilterTextField;
	private UpperCaseFieldBean part2Location2FilterTextField;

	private ObjectTablePane<QiInspectionPart> part1TablePane;
	private ObjectTablePane<QiInspectionPart> part2TablePane;
	private ObjectTablePane<QiInspectionPart> part3TablePane;
	private ObjectTablePane<QiInspectionLocation> part1Location1TablePane;
	private ObjectTablePane<QiInspectionLocation> part1Location2TablePane;
	private ObjectTablePane<QiInspectionLocation> part2Location1TablePane;
	private ObjectTablePane<QiInspectionLocation> part2Location2TablePane;

	private String title;
	PartLocationCombinationDialogController controller;
	QiPartLocationCombination selectedComb;
	private String screenName;
	private ListType listType = ListType.BOTH;
	private volatile boolean isCancel = false;

	public PartLocationCombinationDialog(String title, QiPartLocationCombination selectedComb, ItemMaintenanceModel model, String screenName,String applicationId, PartLocationCombinationController parentController) {

		super(title, applicationId,model);//Fix : Passing applicationId as parameter to fetch correct owner stage.
		this.title = title;
		this.screenName = screenName;
		this.selectedComb = selectedComb;
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.controller = new PartLocationCombinationDialogController(model, this, selectedComb, parentController);
		initComponents();
		if(this.title.equalsIgnoreCase(QiConstant.CREATE))
			loadCreateData();
		else if(this.title.equalsIgnoreCase(QiConstant.UPDATE))
			loadUpdateData();
		controller.initListeners();
		isCancel = false;
	}

	@Override
 	public void close() {
 	   super.close();
 	   if (controller!=null) {
 	     controller.close();
 	   }
	}
	
	public String getReasonForChangeText()
	{
		return StringUtils.trim(reasonForChangeTextArea.getText());
	}

	public LoggedLabel getFullPartNameValueLabel() {
		return fullPartNameValueLabel;
	}

	public UpperCaseFieldBean getPart1FilterTextField() {
		return part1FilterTextField;
	}

	public UpperCaseFieldBean getPart2FilterTextField() {
		return part2FilterTextField;
	}

	public UpperCaseFieldBean getPart3FilterTextField() {
		return part3FilterTextField;
	}

	public UpperCaseFieldBean getPart1Location1FilterTextField() {
		return part1Location1FilterTextField;
	}

	public UpperCaseFieldBean getPart1Location2FilterTextField() {
		return part1Location2FilterTextField;
	}

	public UpperCaseFieldBean getPart2Location1FilterTextField() {
		return part2Location1FilterTextField;
	}

	public UpperCaseFieldBean getPart2Location2FilterTextField() {
		return part2Location2FilterTextField;
	}

	public ObjectTablePane<QiInspectionPart> getPart1TablePane() {
		return part1TablePane;
	}

	public ObjectTablePane<QiInspectionPart> getPart2TablePane() {
		return part2TablePane;
	}

	public ObjectTablePane<QiInspectionPart> getPart3TablePane() {
		return part3TablePane;
	}

	public ObjectTablePane<QiInspectionLocation> getPart1Location1TablePane() {
		return part1Location1TablePane;
	}

	public ObjectTablePane<QiInspectionLocation> getPart1Location2TablePane() {
		return part1Location2TablePane;
	}

	public ObjectTablePane<QiInspectionLocation> getPart2Location1TablePane() {
		return part2Location1TablePane;
	}

	public ObjectTablePane<QiInspectionLocation> getPart2Location2TablePane() {
		return part2Location2TablePane;
	}

	public LoggedButton getCreateBtn() {
		return createBtn;
	}

	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}
	
	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	/**
	 * @return the listType
	 */
	public ListType getListType() {
		return listType;
	}

	/**
	 * @param listType the listType to set
	 */
	public void setListType(ListType listType) {
		this.listType = listType;
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
		HBox labelContainer = new HBox();
		HBox radioBtnContainer = createStatusRadioButtons(controller);
		radioBtnContainer.setPadding(new Insets(0, 10, 10, 10));
		HBox reasonForChangeContainer = new HBox();

		reasonForChangeTextArea = UiFactory.createTextArea();
		reasonForChangeTextArea.setWrapText(true);
		reasonForChangeTextArea.setPrefRowCount(2);
		reasonForChangeTextArea.setPrefColumnCount(40);
		reasonForChangeTextArea.addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(256));

		labelContainer.setAlignment(Pos.CENTER_LEFT);
		labelContainer.setPadding(new Insets(10, 10, 10, 10));
		labelContainer.setSpacing(10);

		LoggedLabel fullPartNameLabel = UiFactory.createLabel("fullPartNameLabel", "Concatenate Part Name");
		fullPartNameLabel.getStyleClass().add("display-label");
		fullPartNameValueLabel = UiFactory.createLabel("fullPartNameValueLabel", "");
		labelContainer.getChildren().addAll(fullPartNameLabel, fullPartNameValueLabel);

		mainPane.setSpacing(10);
		mainPane.getChildren().add(createTitiledPane("Part 1", createPart1Panel()));
		mainPane.getChildren().add(createTitiledPane("Part 2", createPart2Panel()));
		mainPane.getChildren().add(createTitiledPane("Part 3", createPart3Panel()));

		HBox labelBox = new HBox();
		LoggedLabel reasonForChangeLabel = UiFactory.createLabel("reasonForChangeLabel", "Reason for Change");
		reasonForChangeLabel.getStyleClass().add("display-label");
		LoggedLabel asterisk = UiFactory.createLabel("asteriskReasonForChange", "*");
		asterisk.getStyleClass().add("display-label");
		asterisk.setStyle("-fx-text-fill: red");
		labelBox.getChildren().addAll(reasonForChangeLabel,asterisk);

		reasonForChangeContainer.setPadding(new Insets(10, 10, 10, 10));
		reasonForChangeContainer.setSpacing(10);

		createBtn = createBtn(QiConstant.CREATE, controller);
		updateButton = createBtn(QiConstant.UPDATE, controller);
		cancelBtn = createBtn(QiConstant.CANCEL, controller);
		reasonForChangeContainer.getChildren().addAll(labelBox, reasonForChangeTextArea,createBtn, updateButton, cancelBtn);
		reasonForChangeContainer.setAlignment(Pos.CENTER);
		outerPane.getChildren().addAll(labelContainer, radioBtnContainer, mainPane, reasonForChangeContainer);
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);
	}
	/**
	 * This method is used to create Part 1 panel.
	 * @return
	 */
	private MigPane createPart1Panel() {
		MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");

		VBox mainBoxPart1 = new VBox();
		HBox labelBox = new HBox();
		HBox filterBox = new HBox();

		LoggedLabel asterisk = UiFactory.createLabel("asteriskPart1", "*");
		asterisk.getStyleClass().add("display-label");
		asterisk.setStyle("-fx-text-fill: red");
		Label part1Label = UiFactory.createLabel("part1Label", "Part");
		part1Label.getStyleClass().add("display-label");
		labelBox.getChildren().addAll(part1Label, asterisk);
		part1FilterTextField = createFilterTextField("part1FilterTextField", 18, controller);
		part1TablePane = createPartPane();
		filterBox.getChildren().addAll(labelBox,part1FilterTextField);
		filterBox.setSpacing(10);
		filterBox.setAlignment(Pos.CENTER_RIGHT);
		filterBox.setPadding(new Insets(0, 0, 5, 0));
		mainBoxPart1.getChildren().addAll(filterBox,part1TablePane);

		pane.add(mainBoxPart1,"span,wrap");

		Label location1Label = UiFactory.createLabel("part1Location1Label", "Location 1");
		location1Label.getStyleClass().add("display-label");
		part1Location1FilterTextField = createFilterTextField("part1Location1FilterTextField", 18, controller);
		part1Location1TablePane = createPartLocationPane();

		pane.add(location1Label);
		pane.add(part1Location1FilterTextField,"wrap");
		pane.add(part1Location1TablePane,"span,wrap");

		Label location2Label = UiFactory.createLabel("part1Location2Label", "Location 2");
		location2Label.getStyleClass().add("display-label");
		part1Location2FilterTextField = createFilterTextField("part1Location2FilterTextField", 18, controller);
		part1Location2TablePane = createPartLocationPane();

		pane.add(location2Label);
		pane.add(part1Location2FilterTextField,"wrap");
		pane.add(part1Location2TablePane,"span,wrap");

		return pane;

	}
	/**
	 * This method is used to create Part 2 panel.
	 * @return
	 */
	private MigPane createPart2Panel() {
		MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");
		Label part2Label = UiFactory.createLabel("part2Label", "Part");
		part2Label.getStyleClass().add("display-label");
		part2FilterTextField = createFilterTextField("part2FilterTextField", 18, controller);
		part2TablePane = createPartPane();

		VBox mainBox = new VBox();
		HBox filterBox = new HBox();

		filterBox.getChildren().addAll(part2Label,part2FilterTextField);
		filterBox.setSpacing(10);
		filterBox.setAlignment(Pos.CENTER_RIGHT);
		filterBox.setPadding(new Insets(0, 0, 5, 0));
		mainBox.getChildren().addAll(filterBox,part2TablePane);

		pane.add(mainBox,"span,wrap");

		Label location1Label = UiFactory.createLabel("part2Location1Label", "Location 1");
		location1Label.getStyleClass().add("display-label");
		part2Location1FilterTextField = createFilterTextField("part2Location1FilterTextField", 18, controller);
		part2Location1TablePane = createPartLocationPane();

		pane.add(location1Label);
		pane.add(part2Location1FilterTextField,"wrap");
		pane.add(part2Location1TablePane,"span,wrap");

		Label location2Label = UiFactory.createLabel("part2Location2Label", "Location 2");
		location2Label.getStyleClass().add("display-label");
		part2Location2FilterTextField = createFilterTextField("part2Location2FilterTextField", 18, controller);
		part2Location2TablePane = createPartLocationPane();

		pane.add(location2Label);
		pane.add(part2Location2FilterTextField,"wrap");
		pane.add(part2Location2TablePane,"span,wrap");

		return pane;
	}
	/**
	 * This method is used to create Part 3 panel.
	 * @return
	 */
	private MigPane createPart3Panel() {
		MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");
		Label part3Label = UiFactory.createLabel("part3Label", "Part");
		part3Label.getStyleClass().add("display-label");
		part3FilterTextField = createFilterTextField("part3FilterTextField", 18, controller);
		part3TablePane = createPartPane();

		VBox mainBox = new VBox();
		HBox filterBox = new HBox();

		filterBox.getChildren().addAll(part3Label,part3FilterTextField);
		filterBox.setSpacing(10);
		filterBox.setAlignment(Pos.CENTER_RIGHT);
		filterBox.setPadding(new Insets(0, 0, 5, 0));
		mainBox.getChildren().addAll(filterBox,part3TablePane);

		pane.add(mainBox,"span,wrap");

		return pane;
	}
	/**
	 * This method is used to create TitledPane for Part panel.
	 * @param title
	 * @param content
	 * @return
	 */
	private TitledPane createTitiledPane(String title,Node content) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(content);
		titledPane.setPrefSize(300, 500);
		return titledPane;
	}
	/**
	 * This method is used to return ObjectTablePane for Part.
	 * @return
	 */
	private ObjectTablePane<QiInspectionPart> createPartPane() {
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Part Name", "inspectionPartName")
				.put("Abbr", "inspectionPartDescShort");
		ObjectTablePane<QiInspectionPart> panel = new ObjectTablePane<QiInspectionPart>(columnMappingList);
		panel.setStyle("-fx-padding: 5px 0px 5px 0px; -fx-min-height: 125px");
		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		panel.setConstrainedResize(false);
		panel.getTable().getColumns().get(0).setPrefWidth(135);
		panel.getTable().getColumns().get(1).setPrefWidth(135);
		return panel;
	}
	/**
	 * This method is used to return ObjectTablePane for Location.
	 * @return
	 */
	private ObjectTablePane<QiInspectionLocation> createPartLocationPane() {
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Location", "inspectionPartLocationName")
				.put("Abbr", "inspectionPartLocDescShort");
		ObjectTablePane<QiInspectionLocation> panel = new ObjectTablePane<QiInspectionLocation>(columnMappingList);
		panel.setStyle("-fx-padding: 5px 0px 5px 0px;  -fx-min-height: 125px");
		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		panel.setConstrainedResize(false);
		panel.getTable().getColumns().get(0).setPrefWidth(135);
		panel.getTable().getColumns().get(1).setPrefWidth(135);
		return panel;
	}

	public void reload() throws Exception {
		if(title.equalsIgnoreCase(QiConstant.CREATE)) {
			part1TablePane.setData(getModel().getActivePrimaryPartNames());
			part2TablePane.setData(getModel().getActivePartNames());
			part3TablePane.setData(getModel().getActiveSecondaryPartNames());
			part1Location1TablePane.setData(getModel().getActivePrimaryLocationNames());
			part1Location2TablePane.setData(getModel().getActiveLocationNames());
			part2Location1TablePane.setData(getModel().getActivePrimaryLocationNames());
			part2Location2TablePane.setData(getModel().getActiveLocationNames());
		} else if(title.equals(QiConstant.UPDATE)) {
			part1TablePane.setData(getModel().getFilteredActivePrimaryPartNames(StringUtils.trimToEmpty(selectedComb.getInspectionPartName())));
			part2TablePane.setData(getModel().getFilteredActivePartNames(StringUtils.trimToEmpty(selectedComb.getInspectionPart2Name())));
			part3TablePane.setData(getModel().getFilteredActiveSecondaryPartNames(StringUtils.trimToEmpty(selectedComb.getInspectionPart3Name())));

			part1Location1TablePane.setData(getModel().getFilteredActivePrimaryLocationNames(StringUtils.trimToEmpty(selectedComb.getInspectionPartLocationName())));
			part1Location2TablePane.setData(getModel().getFilteredActiveLocationNames(StringUtils.trimToEmpty(selectedComb.getInspectionPartLocation2Name())));
			part2Location1TablePane.setData(getModel().getFilteredActivePrimaryLocationNames(StringUtils.trimToEmpty(selectedComb.getInspectionPart2LocationName())));
			part2Location2TablePane.setData(getModel().getFilteredActiveLocationNames(StringUtils.trimToEmpty(selectedComb.getInspectionPart2Location2Name())));
		}
	}
	/**
	 * This method is used to load Create data.
	 */
	private void loadCreateData()
	{
		try {
			selectedComb = new QiPartLocationCombination();
			updateButton.setDisable(true);
			reasonForChangeTextArea.setDisable(true);
			reload();
		} catch (Exception e) {
			controller.handleException("An error occured in loading pop up screen ", "Failed to Open Create Part Location Combination popup screen", e);
		}
	}
	/**
	 * This method is used to load Update data.
	 */
	private void loadUpdateData()
	{
		try {
			createBtn.setDisable(true);
			fullPartNameValueLabel.setText(selectedComb.getFullPartDesc());

			reload();

			getActiveRadioBtn().setSelected(selectedComb.isActive());
			getInactiveRadioBtn().setSelected(!selectedComb.isActive());

			List<QiInspectionPart> part1List = getModel().getFilteredActivePrimaryPartNames(selectedComb.getInspectionPartName());
			List<QiInspectionPart> part2List = getModel().getFilteredActivePartNames(selectedComb.getInspectionPart2Name());
			List<QiInspectionPart> part3List = getModel().getFilteredActiveSecondaryPartNames(selectedComb.getInspectionPart3Name());

			List<QiInspectionLocation> part1Loc1List = getModel().getFilteredActivePrimaryLocationNames(selectedComb.getInspectionPartLocationName());
			List<QiInspectionLocation> part1Loc2List = getModel().getFilteredActiveLocationNames(selectedComb.getInspectionPartLocation2Name());
			List<QiInspectionLocation> part2Loc1List = getModel().getFilteredActivePrimaryLocationNames(selectedComb.getInspectionPart2LocationName());
			List<QiInspectionLocation> part2Loc2List = getModel().getFilteredActiveLocationNames(selectedComb.getInspectionPart2Location2Name());

			setSelectionInPartTables(part1List, part1TablePane, part1FilterTextField, selectedComb.getInspectionPartName());
			setSelectionInPartTables(part2List, part2TablePane, part2FilterTextField, selectedComb.getInspectionPart2Name());
			setSelectionInPartTables(part3List, part3TablePane, part3FilterTextField, selectedComb.getInspectionPart3Name());

			setSelectionInLocationTables(part1Loc1List, part1Location1TablePane, part1Location1FilterTextField, selectedComb.getInspectionPartLocationName());
			setSelectionInLocationTables(part1Loc2List, part1Location2TablePane, part1Location2FilterTextField, selectedComb.getInspectionPartLocation2Name());
			setSelectionInLocationTables(part2Loc1List, part2Location1TablePane, part2Location1FilterTextField, selectedComb.getInspectionPart2LocationName());
			setSelectionInLocationTables(part2Loc2List, part2Location2TablePane, part2Location2FilterTextField, selectedComb.getInspectionPart2Location2Name());

		} catch (Exception e) {
			controller.handleException("An error occured in loading pop up screen ", "Failed to Open Update Part Location Combination popup screen", e);
		}
	}
	/**
	 * This method is used to set selection in Part tables.
	 * @param partList
	 * @param partTablePane
	 * @param filterTxt
	 * @param partName
	 */
	private void setSelectionInPartTables(List<QiInspectionPart> partList, ObjectTablePane<QiInspectionPart> partTablePane, UpperCaseFieldBean filterTxt, final String partName) {
		filterTxt.setText(partName);
		QiInspectionPart part = getObjectfromList(partList, partName);
		if(!partList.isEmpty() && part!=null && !partName.equals(StringUtils.EMPTY))
			partTablePane.getTable().getSelectionModel().select(part);
	}
	/**
	 * This method is used to set selection in Location tables.
	 * @param locationList
	 * @param locationTablePane
	 * @param filterTxt
	 * @param locationName
	 */
	private void setSelectionInLocationTables(List<QiInspectionLocation> locationList, ObjectTablePane<QiInspectionLocation> locationTablePane, UpperCaseFieldBean filterTxt, String locationName) {
		filterTxt.setText(locationName);
		QiInspectionLocation location = getObjectfromList(locationList, locationName);
		if(!locationList.isEmpty() && location!=null && !locationName.equals(StringUtils.EMPTY))
			locationTablePane.getTable().getSelectionModel().select(location);
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
