package com.honda.galc.client.teamleader.qi.view;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.PdcMaintController;
import com.honda.galc.client.teamleader.qi.model.PdcLocalAttributeMaintModel;
import com.honda.galc.client.teamleader.qi.stationconfig.dto.ComboBoxDisplayDto;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.LoggedText;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.PdcRegionalAttributeMaintDto;
import com.honda.galc.entity.qi.QiStationEntryScreen;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ObjectComparator;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
/**
 * 
 * <h3>PdcRegionalAttributeMaintPanel Class description</h3>
 * <p>
 * PdcRegionalAttributeMaintPanel is used to assign ,Filter ,TableView and populate data in TableView
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
/**   
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
public class PdcLocalAttributeMaintPanel extends QiAbstractTabbedView<PdcLocalAttributeMaintModel, PdcMaintController>{


	private UpperCaseFieldBean defectFilterTextField;
	private ObjectTablePane<PdcRegionalAttributeMaintDto> localAttributeMaintTablePane;
	private LoggedRadioButton allRadioButton;
	private LoggedRadioButton assignedRadioButton;
	private LoggedRadioButton notAssignedRadioButton;
	private LoggedText site;
	private LabeledComboBox<String> plantComboBox;
	private LabeledComboBox<String> productTypesComboBox;
	private LabeledComboBox<String> entryModelComboBox;
	private LabeledComboBox<String> entryScreenComboBox;
	private LoggedButton refreshBtn;
	private double width;
	private LabeledComboBox<ComboBoxDisplayDto> entryDeptComboBox;
	
	private LoggedButton displayQrCodeButton;
	private ProgressBar progressBar;
	private VBox progressBox;
	List<PdcRegionalAttributeMaintDto> pdcRegionalAttributeList;
	
	private LoggedButton updateModelYearButton;

	public PdcLocalAttributeMaintPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);
	}

	/**
	 * This method is used to create layout of the Inspection Part Maintenance Parent screen
	 */
	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		HBox outerContainer = new HBox();
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		this.width = screenBounds.getWidth()/2;
		progressBar = new ProgressBar();
        progressBar.setPrefWidth(width/4);
        StackPane mainPane = new StackPane();
        
        progressBox = new VBox();
        progressBox.getChildren().addAll(progressBar);
        progressBox.setVisible(false);
        progressBox.setAlignment(Pos.CENTER);

		outerContainer.getChildren().addAll(createComboBoxes());
		localAttributeMaintTablePane = createDefectTablePane();
		LoggedTableColumn<PdcRegionalAttributeMaintDto, Integer> column = new LoggedTableColumn<PdcRegionalAttributeMaintDto, Integer>();

		createSerialNumber(column);
		localAttributeMaintTablePane.getTable().getColumns().add(0, column);
		localAttributeMaintTablePane.getTable().getColumns().get(0).setText("#");
		localAttributeMaintTablePane.getTable().getColumns().get(0).setResizable(true);
		localAttributeMaintTablePane.getTable().getColumns().get(0).setMaxWidth(40);
		localAttributeMaintTablePane.getTable().getColumns().get(0).setMinWidth(1);
		this.setTop(createFilterComboBoxes());
		loadPlantComboBoxData();
		 mainPane.getChildren().addAll(localAttributeMaintTablePane,progressBox);
		this.setCenter(mainPane);
	}

	/**
	 * Load plant combo box data.
	 */
	public void loadPlantComboBoxData() {
		plantComboBox.getControl().setPromptText("Select");
		plantComboBox.getControl().getItems().addAll(getModel().findAllBySite(site.getText()));
	}

	/**
	 * Creates the filter combo boxes.
	 *
	 * @return the v box
	 */
	private VBox createFilterComboBoxes() {
		VBox mainContainer = new VBox();
		HBox filterContainer = new HBox();
		filterContainer.getChildren().addAll(createFilterRadioButtons());
		mainContainer.getChildren().addAll(createSiteValue(), createComboBoxes(), filterContainer);
		return mainContainer;
	}

	/**
	 * Creates the combo boxes.
	 *
	 * @return the h box
	 */
	private HBox createComboBoxes() {
		createPlantComboBox();
		createProductTypesComboBox();
		createEntryModelComboBox();
		createEntryScreenComboBox();
		createEntryDeptComboBox();
		HBox comboboxCntainer = new HBox();

		comboboxCntainer.getChildren().addAll(plantComboBox, productTypesComboBox,entryModelComboBox, entryDeptComboBox,entryScreenComboBox);
		comboboxCntainer.setSpacing(10);
		comboboxCntainer.setAlignment(Pos.CENTER_RIGHT);
		HBox.setHgrow(comboboxCntainer,Priority.ALWAYS);
		return comboboxCntainer;
	}

	/**
	 * This method is used to create a Table Pane.
	 * @return
	 */
	private ObjectTablePane<PdcRegionalAttributeMaintDto> createDefectTablePane(){ 
		ColumnMappingList columnMappingList = ColumnMappingList.with("QICS Full Part Name", "fullPartName")
				.put("Primary Defect","defectTypeName")
				.put("Secondary Defect", "defectTypeName2")
				.put("Rpt/Non-Rpt", "reportableDefect")
				.put("Responsibility","responsibility")
				.put("Local Theme","localTheme")
				.put("Theme", "themeName")
				.put("Fire Engine", "engineFiringFlag")
				.put("Menu", "textEntryMenu")
				.put("Defect Category", "defectCategory")
				.put("Init Rep Area", "repairArea")
				.put("Init Rep Meth", "repairMethod")
				.put("Init Rep Time", "repairTime", Integer.class)
				.put("Total time", "totalTime", Integer.class)
				.put("IQS Version", "iqsVersion")
				.put("IQS Category","iqsCategory")
				.put("IQS Question", "iqsQuestion")
				.put("PDDA Info", "pddaInfo")
				.put("Is Used", "IsUsedVersionData")
				;

		Double[] columnWidth = new Double[] {
				0.12,
				0.08,
				0.02,
				0.02,
				0.06,
				0.06,
				0.09,
				0.02,
				0.08,
				0.08,
				0.02,
				0.05,
				0.02,
				0.02,
				0.04,
				0.05,
				0.07,
				0.08,
				0.02
		};
		ObjectTablePane<PdcRegionalAttributeMaintDto> panel = new ObjectTablePane<PdcRegionalAttributeMaintDto>(columnMappingList,columnWidth);
		return panel;
	}

	public void onTabSelected() {
		getController().refreshBtnAction();
	}

	public String getScreenName() {
		return "Local Attribute";
	}

	@Override
	public void reload() {
	}
	
	//clear selected item, can be called after Assign
	public void clearSelectedAttribute()  {
		if(localAttributeMaintTablePane.getTable().getSelectionModel().getSelectedItems().size() > 0)  {
			localAttributeMaintTablePane.getTable().getSelectionModel().clearSelection();
		}
	}

	/**
	 * Reload.
	 *
	 * @param filterValue the filter value
	 */
	public void reload(final String entryScreen, final String filterValue) {
		reload(entryScreen,filterValue, false, false);
	}
	/**
	 * Reload.
	 *
	 * @param filterValue the filter value
	 */
	public void reload(final String entryScreen, final String filterValue, boolean isRestoreListPosition, boolean isRestoreSelect) {
		final String entryModel = getEntryModelComboBox().getValue() == null ? "" : getEntryModelComboBox().getValue().toString();
		String entryDept = StringUtils.trimToEmpty(getEntryDeptComboBoxSelectedId());
		
		progressBox.setVisible(true);
		Task<Void> mainTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				pdcRegionalAttributeList =  getModel().findAllPdcLocalAttributes(entryScreen, entryModel, getSelectedRadioButtonValue(), filterValue);
				Collections.sort(pdcRegionalAttributeList, new ObjectComparator<PdcRegionalAttributeMaintDto>("fullPartName", "defectTypeName", "defectTypeName2"));
				updateProgress(100, 100);
				return null;
			}
		};

		mainTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			public void handle(WorkerStateEvent t) {
				progressBox.setVisible(false);
				localAttributeMaintTablePane.setData(pdcRegionalAttributeList, getModel().getLazyLoadDisplayRows(), isRestoreListPosition, isRestoreSelect);
			}
		});
		progressBar.progressProperty().bind(mainTask.progressProperty());

		new Thread(mainTask).start();
		
		// Enable/disable Update Pdda Model Year button
		if(localAttributeMaintTablePane.getTable().getSelectionModel().getSelectedItems().size()>0 && assignedRadioButton.isSelected()){
			getUpdateModelYearButton().setDisable(false);
		}
		
		// check if scan is set true for Entry Screen used to hide/display QR Code button.
		List<QiStationEntryScreen> stationEntryList=getModel().findAllByEntryScreenModelAndDept(entryScreen,entryModel,entryDept);
		if(localAttributeMaintTablePane.getTable().getItems().size()>0 && assignedRadioButton.isSelected() 
				&& stationEntryList!=null && stationEntryList.size()>0){
			displayQrCodeButton.setDisable(false);
		}
		else
		{
			displayQrCodeButton.setDisable(true);
		}
	}


	@Override
	public void start() {
	}

	/**
	 * Creates the filter radio buttons.
	 *
	 * @return the h box
	 */
	private HBox createFilterRadioButtons() {
		HBox container = new HBox();

		HBox radioBtnContainer = new HBox();
		ToggleGroup group = new ToggleGroup();
		this.allRadioButton = createRadioButton(QiConstant.ALL, group, true, getController());
		this.allRadioButton.setId("ASSIGNED_ALL");
		this.assignedRadioButton = createRadioButton(QiConstant.ASSIGNED, group, false, getController());
		this.assignedRadioButton.setId("ASSIGNED");
		this.notAssignedRadioButton = createRadioButton(QiConstant.NOT_ASSIGNED, group, false, getController());
		this.notAssignedRadioButton.setId("NOT_ASSIGNED");

		radioBtnContainer.getChildren().addAll(allRadioButton, assignedRadioButton, notAssignedRadioButton);
		radioBtnContainer.setAlignment(Pos.CENTER_LEFT);
		radioBtnContainer.setSpacing(10);
		radioBtnContainer.setPadding(new Insets(0, 0, 0, 10));
		radioBtnContainer.setPrefWidth(width);


		container.getChildren().addAll(radioBtnContainer, createFilterContainer(width));
		return container;
	}

	/**
	 * This method return a list of values based on selected radio buttons (e.g. Active - 1, Inactive - 0, All - 0 & 1)
	 * @return
	 */
	public short getSelectedRadioButtonValue() {
		if(getNotAssignedRadioButton().isSelected()) {
			return (short)0;
		} else if(getAssignedRadioButton().isSelected()){
			return (short)1;
		} else {
			return (short)2;
		}

	}

	/**
	 * Creates the filter container.
	 *
	 * @param width the width
	 * @return the h box
	 */
	private HBox createFilterContainer(double width) {
		HBox filterContainer = new HBox();
		Label partFilterLabel = UiFactory.createLabel("label", "Filter", Fonts.SS_DIALOG_BOLD(14));
		defectFilterTextField = UiFactory.createUpperCaseFieldBean("filter-textfield", 18, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		defectFilterTextField.setOnAction(getController());

		filterContainer.setSpacing(10);
		filterContainer.setPadding(new Insets(10));
		filterContainer.setAlignment(Pos.CENTER_RIGHT);
		filterContainer.setPrefWidth(width);
		filterContainer.getChildren().addAll(partFilterLabel,defectFilterTextField);
		return filterContainer;
	}


	/**
	 * Creates the site value.(
	 *
	 * @return the h box
	 */
	private HBox createSiteValue() {
		HBox themeContainer = new HBox();
		HBox siteContainer =  new HBox();
		LoggedLabel siteLabel = UiFactory.createLabel("siteLabel", "Site", Fonts.SS_DIALOG_BOLD(14));
		siteLabel.setMaxWidth(40.0);
		siteLabel.setPrefWidth(40.0);
		refreshBtn = createBtn("Refresh",getController());
		displayQrCodeButton = createBtn("displayQrCodeButton",getController());
		displayQrCodeButton.setText("Display QR Code");
		displayQrCodeButton.setDisable(true);
		updateModelYearButton = createBtn("updatePddaModelYearButton",getController());
		updateModelYearButton.setText("Update Pdda Model Year");
		updateModelYearButton.setDisable(true);
		this.site = UiFactory.createText(getModel().getSiteName());
		themeContainer.setSpacing(10);
		themeContainer.setPadding(new Insets(10, 10, 0, 10));
		siteContainer.getChildren().addAll(siteLabel,site);
		HBox.setHgrow(siteContainer, Priority.ALWAYS);
		siteContainer.setAlignment(Pos.CENTER_LEFT);
		themeContainer.getChildren().addAll(siteContainer,updateModelYearButton,displayQrCodeButton,refreshBtn);
		return themeContainer;
	}

	/**
	 * Creates the plant combo box.
	 *user
	 * @return the h box
	 */

	private void createPlantComboBox(){
		plantComboBox =  createLabeledComboBox("Plant", true, new Insets(5), true, true);
		plantComboBox.getControl().setMinHeight(35.0);
		plantComboBox.getControl().setMinWidth(100.0);
		plantComboBox.getLabel().setPadding(new Insets(0,10,0,0));
		plantComboBox.getLabel().getStyleClass().add("Fonts.SS_DIALOG_BOLD(14)");
		plantComboBox.getControl().setId("plantComboBox");
		HBox.setHgrow(plantComboBox,Priority.ALWAYS);

	}
	private void createProductTypesComboBox(){
		productTypesComboBox = createLabeledComboBox("Product Type", true, new Insets(5),true, true);
		productTypesComboBox.getControl().setMinHeight(35.0);
		productTypesComboBox.getLabel().getStyleClass().add("Fonts.SS_DIALOG_BOLD(14)");
		productTypesComboBox.getControl().setId("productTypesComboBox");
		productTypesComboBox.getLabel().setPadding(new Insets(0,10,0,0));
		HBox.setHgrow(productTypesComboBox, Priority.ALWAYS);
	}
	/**
	 * Creates the entry model combo box.
	 *
	 * @return the h box
	 */
	private void createEntryModelComboBox() {
		entryModelComboBox = createLabeledComboBox("Entry Model", true, new Insets(5), true, true);
		entryModelComboBox.getControl().setMinHeight(35.0);
		entryModelComboBox.getLabel().getStyleClass().add("Fonts.SS_DIALOG_BOLD(14)");
		entryModelComboBox.getControl().setId("entryModelComboBox");
		entryModelComboBox.getLabel().setPadding(new Insets(0,10,0,0));
		HBox.setHgrow(entryModelComboBox,Priority.ALWAYS);
	}
	/**
	 * Creates the entry screen combo box.
	 *
	 * @return the h box
	 */
	private void createEntryScreenComboBox() {
		entryScreenComboBox = createLabeledComboBox("Entry Screen", true, new  Insets(5), true, true);
		entryScreenComboBox.getControl().setMinHeight(35.0);
		entryScreenComboBox.getControl().setMinHeight(35.0);
		entryScreenComboBox.getControl().setId("entryScreenComboBox");
		entryScreenComboBox.getLabel().getStyleClass().add("Fonts.SS_DIALOG_BOLD(14)");
		entryScreenComboBox.getLabel().setPadding(new Insets(0,10,0,0));
		HBox.setHgrow(entryScreenComboBox, Priority.ALWAYS);

	}

	/**
	 * Creates the entry screen combo box.
	 *
	 * @return the h box
	 */
	private void createEntryDeptComboBox() {
		entryDeptComboBox = createCustomLabeledComboBox("Entry Dept", true, new  Insets(5), true, true);
		entryDeptComboBox.getControl().setMinHeight(35.0);
		entryDeptComboBox.getControl().setMinHeight(35.0);
		entryDeptComboBox.getLabel().getStyleClass().add("Fonts.SS_DIALOG_BOLD(14)");
		entryDeptComboBox.getLabel().setPadding(new Insets(0,10,0,0));
		entryDeptComboBox.getControl().setId("entryDeptComboBox");
		HBox.setHgrow(entryDeptComboBox, Priority.ALWAYS);

	}

	/**
	 * This method is used to create LabeledComboBox
	 * @param id
	 * @return
	 */
	private LabeledComboBox<String> createLabeledComboBox(String label,boolean isHorizontal, Insets insets, boolean isLabelBold, boolean isMandaotry) {
		LabeledComboBox<String> comboBox = new LabeledComboBox<String>(label,isHorizontal,insets,isLabelBold,isMandaotry);
		return comboBox;
	}

	/**
	 * This method is used to create LabeledComboBox
	 * @param id
	 * @return
	 */
	private LabeledComboBox<ComboBoxDisplayDto> createCustomLabeledComboBox(String label,boolean isHorizontal, Insets insets, boolean isLabelBold, boolean isMandaotry) {
		LabeledComboBox<ComboBoxDisplayDto> comboBox = new LabeledComboBox<ComboBoxDisplayDto>(label,isHorizontal,insets,isLabelBold,isMandaotry);
		return comboBox;
	}

	public ObjectTablePane<PdcRegionalAttributeMaintDto> getLocalAttributeMaintTablePane() {
		return localAttributeMaintTablePane;
	}

	public void setLocalAttributeMaintTablePane(
			ObjectTablePane<PdcRegionalAttributeMaintDto> localAttributeMaintTablePane) {
		this.localAttributeMaintTablePane = localAttributeMaintTablePane;
	}

	public LoggedRadioButton getAllRadioButton() {
		return allRadioButton;
	}

	public void setAllRadioButton(LoggedRadioButton allRadioButton) {
		this.allRadioButton = allRadioButton;
	}

	public LoggedRadioButton getAssignedRadioButton() {
		return assignedRadioButton;
	}

	public void setAssignedRadioButton(LoggedRadioButton assignedRadioButton) {
		this.assignedRadioButton = assignedRadioButton;
	}

	public LoggedRadioButton getNotAssignedRadioButton() {
		return notAssignedRadioButton;
	}

	public void setNotAssignedRadioButton(LoggedRadioButton notAssignedRadioButton) {
		this.notAssignedRadioButton = notAssignedRadioButton;
	}

	public UpperCaseFieldBean getDefectFilterTextField() {
		return defectFilterTextField;
	}

	public void setDefectFilterTextField(UpperCaseFieldBean defectFilterTextField) {
		this.defectFilterTextField = defectFilterTextField;
	}

	public ComboBox<String> getPlantComboBox() {
		return plantComboBox.getControl();
	}

	public ComboBox<String> getEntryModelComboBox() {
		return entryModelComboBox.getControl();
	}

	public ComboBox<String> getEntryScreenComboBox() {
		return entryScreenComboBox.getControl();
	}

	public LoggedText getSite() {
		return site;
	}

	public void setSite(LoggedText site) {
		this.site = site;
	}

	public ComboBox<String> getProductTypesComboBox() {
		return productTypesComboBox.getControl();
	}

	/**
	 * Gets the defect filter text value.
	 *
	 * @return the defect filter text value
	 */
	public String getDefectFilterTextValue(){
		return StringUtils.trimToEmpty(defectFilterTextField.getText());
	}

	public ComboBox<ComboBoxDisplayDto> getEntryDeptComboBox() {
		return entryDeptComboBox.getControl();
	}

	public String getEntryDeptComboBoxSelectedId() {
		ComboBoxDisplayDto dto = (ComboBoxDisplayDto) getEntryDeptComboBox()
				.getSelectionModel().getSelectedItem();
		String deptId = "";
		if(dto != null)  {
			deptId = dto.getId();
		}
		return deptId;
	}
	
	public ComboBoxDisplayDto getEntryDeptComboBoxSelectedItem() {
		ComboBoxDisplayDto dto = (ComboBoxDisplayDto) getEntryDeptComboBox()
				.getSelectionModel().getSelectedItem();
		return dto;
	}
	
	public void clearEntryDeptComboBox() {
		getEntryDeptComboBox().getItems().clear();
	}


	public LoggedButton getDisplayQrCodeButton() {
		return displayQrCodeButton;
	}
	
	public LoggedButton getRefreshBtn() {
		return refreshBtn;
	}

	public LoggedButton getUpdateModelYearButton() {
		return updateModelYearButton;
	}
}
