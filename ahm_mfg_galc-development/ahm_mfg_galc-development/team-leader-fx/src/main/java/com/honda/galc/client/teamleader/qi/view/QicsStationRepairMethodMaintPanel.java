package com.honda.galc.client.teamleader.qi.view;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.QicsStationRepairMethodMaintController;
import com.honda.galc.client.teamleader.qi.model.QicsStationRepairMethodMaintenanceModel;
import com.honda.galc.client.teamleader.qi.stationconfig.dto.ComboBoxDisplayDto;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.qi.QiRepairMethod;
import com.honda.galc.client.utils.QiConstant;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QicsStationRepairMethodMaintPanel</code> is the Panel class for Qics Station RepairMethod Maintenance
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
/**   
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
public class QicsStationRepairMethodMaintPanel extends QiAbstractTabbedView<QicsStationRepairMethodMaintenanceModel, QicsStationRepairMethodMaintController>{
	private LoggedButton saveBtn;
	private LoggedButton cancelBtn;
	private LoggedButton leftShiftBtn;
	private LoggedButton rightShiftBtn;
	private LoggedButton refreshBtn;
	private LoggedLabel siteValueLabel;
	private UpperCaseFieldBean methodFilterTextField;
	private ObjectTablePane<QiRepairMethod> availableRepairMethodTablePane;
	private ObjectTablePane<QiRepairMethod> selectedRepairMethodTablePane;
	private LoggedComboBox<ComboBoxDisplayDto> divisionComboBox;
	private LoggedComboBox<String> plantComboBox;
	private ListView<String> qicsStationListView; 
	private CheckBox showRepairMethodForDivisionChkBox;

	public QicsStationRepairMethodMaintPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
		leftShiftBtn.setDisable(true);
		rightShiftBtn.setDisable(true);
		saveBtn.setDisable(true);
	}

	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		VBox outerPane = new VBox();
		HBox outerContainer = new HBox();
		outerContainer.setAlignment(Pos.CENTER_LEFT);
		outerContainer.setPadding(new Insets(10, 10, 10, 10));
		HBox outerLabelContainer = new HBox();
		outerLabelContainer.setAlignment(Pos.CENTER_LEFT);
		outerLabelContainer.setPadding(new Insets(10, 10, 10, 10));
		outerLabelContainer.setSpacing(100);
		outerPane.setSpacing(10);
		HBox siteOuterContainer = createSiteOuterContainer();
		HBox plantOuterContainer = createPlantOuterContainer();
		HBox divisionOuterContainer = creatDivisionOuterContainer();
		HBox qicsStationOuterContainer = createQicsStationOuterContainer();
		outerLabelContainer.getChildren().addAll(siteOuterContainer,plantOuterContainer,divisionOuterContainer);
		outerLabelContainer.setPadding(new Insets(15,0,0,30));
		outerLabelContainer.setSpacing(125);
		HBox.setHgrow(outerLabelContainer, Priority.ALWAYS);
		refreshBtn = createBtn("Refresh",getController());
		outerContainer.getChildren().addAll(outerLabelContainer,refreshBtn);
		HBox availableRepairMethod = createAvailableRepairMethod();
		HBox buttonContainer= createBtnContainer();
		outerPane.getChildren().addAll(outerContainer,qicsStationOuterContainer,availableRepairMethod,buttonContainer);
		this.setTop(outerPane);
	}


	/**
	 * This method is used create site outer container which contains site label and site value.
	 */
	private HBox createSiteOuterContainer() {
		HBox siteOuterContainer = new HBox();
		HBox siteLabelContainer = new HBox();
		LoggedLabel siteLabel = UiFactory.createLabel("label", "Site");
		siteLabelContainer.getChildren().addAll(siteLabel);
		siteValueLabel =new LoggedLabel();
		siteValueLabel.setText(getDefaultSiteName());
		siteOuterContainer.setSpacing(10);
		siteOuterContainer.setPrefWidth(200);
		siteOuterContainer.getChildren().addAll(siteLabelContainer,siteValueLabel);

		return siteOuterContainer;
	}

	/**
	 * This method is used create plant outer container which contains plant label and plant combo box.
	 */
	@SuppressWarnings("unchecked")
	private HBox createPlantOuterContainer() {
		HBox plantOuterContainer = new HBox();
		HBox plantLabelContainer = new HBox();
		LoggedLabel plantLabel = UiFactory.createLabel("label", "Plant");
		plantLabelContainer.getChildren().addAll(plantLabel);
		plantComboBox = new LoggedComboBox<String>("plantComboBox");
		plantComboBox.getStyleClass().add("combo-box-base");
		plantComboBox.setMinWidth(200);
		String siteName = siteValueLabel.getText();
		plantComboBox.setPromptText("Select");
		for (Plant plantObj : getModel().findAllBySite(siteName)) {
			plantComboBox.getItems().add(plantObj.getPlantName());
		}
		plantOuterContainer.setSpacing(10);
		plantOuterContainer.setPadding(new Insets(0,0,0,-200));
		plantOuterContainer.getChildren().addAll(plantLabelContainer,plantComboBox);
		return plantOuterContainer;
	}

	/**
	 * This method is used create division outer container which contains division label and division combo box.
	 */
	private HBox creatDivisionOuterContainer() {
		HBox divisionOuterContainer = new HBox();
		HBox divisionLabelContainer = new HBox();
		LoggedLabel divisionLabel = UiFactory.createLabel("label", "Division");
		divisionLabelContainer.getChildren().addAll(divisionLabel);
		divisionComboBox = new LoggedComboBox<ComboBoxDisplayDto>("divisionComboBox");
		divisionComboBox.getStyleClass().add("combo-box-base");
		divisionComboBox.setMinWidth(200);
		divisionComboBox.setPromptText("Select");

		divisionOuterContainer.setSpacing(10);
		divisionOuterContainer.setPadding(new Insets(0,0,0,-75));
		divisionOuterContainer.getChildren().addAll(divisionLabelContainer,divisionComboBox);
		return divisionOuterContainer;
	}

	/**
	 * This method is used create Qics station outer container which contains Qics station label and Qics station list view.
	 */
	private HBox createQicsStationOuterContainer() {
		HBox qicsStationOuterContainer = new HBox();
		HBox qicsStationLabelContainer = new HBox();
		LoggedLabel qicsStationLabel = UiFactory.createLabel("label", "QICS Station");
		LoggedLabel qicsStationAsterisk = UiFactory.createLabel("label", "*");
		qicsStationAsterisk.setStyle("-fx-text-fill: red");
		qicsStationLabelContainer.getChildren().addAll(qicsStationLabel,qicsStationAsterisk);
		qicsStationListView=new ListView<String>();
		qicsStationListView.setPrefWidth(530);
		qicsStationListView.setPrefHeight(90);
		qicsStationOuterContainer.getChildren().addAll(qicsStationLabelContainer,qicsStationListView);
		qicsStationOuterContainer.setSpacing(10);
		qicsStationOuterContainer.setPadding(new Insets(0,0,0,30));
		return qicsStationOuterContainer;
	}

	/**This method is used to create Available Repair Method and Selected QICS Station Repair Method tables
	 * @return
	 */
	private HBox createAvailableRepairMethod() {
		HBox availableRepairMethod = new HBox();
		availableRepairMethod.setSpacing(10);
		VBox leftRightShiftContainer = new VBox();
		leftRightShiftContainer.setSpacing(10);
		leftShiftBtn = createBtn("<",getController());
		leftShiftBtn.setId("leftShiftBtn");
		rightShiftBtn = createBtn(">",getController());
		leftRightShiftContainer.setAlignment(Pos.CENTER);
		rightShiftBtn.setId("rightShiftBtn");
		availableRepairMethod.setPadding(new Insets(0, 0, 0, 30));
		leftRightShiftContainer.getChildren().addAll(leftShiftBtn,rightShiftBtn);
		availableRepairMethod.getChildren().add(createTitiledPane("Available Repair Methods", createAvailableRepairMethodPanel()));
		availableRepairMethod.getChildren().add(leftRightShiftContainer);
		availableRepairMethod.getChildren().add(createTitiledPane("Selected QICS Station Repair Methods", createSelectedRepairMethodPanel()));
		return availableRepairMethod;
	}

	/**
	 * This method is used create button.
	 */
	private HBox createBtnContainer() {
		HBox buttonContainer= new HBox();
		buttonContainer.setSpacing(10);
		saveBtn = createBtn("Save",getController());
		buttonContainer.setAlignment(Pos.BOTTOM_RIGHT);
		buttonContainer.setPadding(new Insets(0, 40, 0, 0));
		buttonContainer.getChildren().addAll(saveBtn);
		if(!isFullAccess()){
			saveBtn.setDisable(true);
		}
		return buttonContainer;
	}


	/**
	 * This method is used to create TitledPane for QICS station repair methods panel.
	 * @param title
	 * @param content
	 * @return
	 */
	private TitledPane createTitiledPane(String title,Node content) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(content);
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		double titlePaneWidth = 0.45*(primaryScreenBounds.getWidth());
		titledPane.setPrefSize(titlePaneWidth, 350);
		return titledPane;
	}

	/**
	 * This method is used to create Available repair methods panel.
	 * @return
	 */
	private MigPane createAvailableRepairMethodPanel() {
		MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");

		HBox checkBoxLabelBox = new HBox();
		showRepairMethodForDivisionChkBox =createCheckBox("Show repair methods used in this division", getController(),"checkBox-ImageSection");
		showRepairMethodForDivisionChkBox.setId("showRepairMethodForDivisionChkBox");
		showRepairMethodForDivisionChkBox.setStyle("-fx-font-size: 8pt; -fx-font-weight: bold;");
		checkBoxLabelBox.getChildren().addAll(showRepairMethodForDivisionChkBox);
		checkBoxLabelBox.setPadding(new Insets(0, 0, 0, 0));

		HBox filterLabelBox = new HBox();
		Label methodFilterLabel = UiFactory.createLabel("label", "Filter");
		methodFilterLabel.getStyleClass().add("display-label");
		
		methodFilterTextField =  createFilterTextField("filter-textfield", 12, getController());
		filterLabelBox.setSpacing(05);
		filterLabelBox.setAlignment(Pos.BASELINE_RIGHT);
		filterLabelBox.getChildren().addAll(methodFilterLabel,methodFilterTextField);
		availableRepairMethodTablePane = createAvailableRepairMethodPane();
		availableRepairMethodTablePane.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		LoggedTableColumn<QiRepairMethod, Integer> column = new LoggedTableColumn<QiRepairMethod, Integer>();

		createSerialNumber(column);
		availableRepairMethodTablePane.getTable().getColumns().add(0, column);
		availableRepairMethodTablePane.getTable().getColumns().get(0).setText("#");
		availableRepairMethodTablePane.getTable().getColumns().get(0).setResizable(true);
		availableRepairMethodTablePane.getTable().getColumns().get(0).setMaxWidth(100);
		availableRepairMethodTablePane.getTable().getColumns().get(0).setMinWidth(50);

		pane.add(checkBoxLabelBox);
		pane.add(filterLabelBox,"span,wrap");
		pane.add(availableRepairMethodTablePane,"span,wrap");
		pane.setId("mig-pane");
		return pane;
	}
	
	/**
	 * This method is used to create Selected repair methods panel.
	 * @return
	 */
	private MigPane createSelectedRepairMethodPanel() {
		MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");
		selectedRepairMethodTablePane = createSelectedRepairMethodPane();
		selectedRepairMethodTablePane.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		LoggedTableColumn<QiRepairMethod, Integer> column = new LoggedTableColumn<QiRepairMethod, Integer>();

		createSerialNumber(column);
		selectedRepairMethodTablePane.getTable().getColumns().add(0, column);
		selectedRepairMethodTablePane.getTable().getColumns().get(0).setText("#");
		selectedRepairMethodTablePane.getTable().getColumns().get(0).setResizable(true);
		selectedRepairMethodTablePane.getTable().getColumns().get(0).setMaxWidth(100);
		selectedRepairMethodTablePane.getTable().getColumns().get(0).setMinWidth(50);

		pane.add(selectedRepairMethodTablePane,"span,wrap");
		pane.setId("mig-pane");
		return pane;
	}

	/**
	 * This method is used create check box.
	 */
	
	/**
	 * This method is used to return ObjectTablePane for Available repair methods.
	 * @return
	 */
	private ObjectTablePane<QiRepairMethod> createAvailableRepairMethodPane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Name", "repairMethod").put("Description", "repairMethodDescription");
		ObjectTablePane<QiRepairMethod> panel = new ObjectTablePane<QiRepairMethod>(columnMappingList);
		panel.setStyle("-fx-padding: 5px 0px 5px 0px; -fx-min-height: 125px");
		panel.setPrefWidth(0.45*Screen.getPrimary().getVisualBounds().getWidth());
		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		panel.setConstrainedResize(false);
		panel.getTable().getColumns().get(0).setPrefWidth(panel.getPrefWidth()*0.4);
		panel.getTable().getColumns().get(1).setPrefWidth(panel.getPrefWidth()*0.43);
		return panel;
	}
	
	/**userid
	 * This method is used to return ObjectTablePane for Available repair methods.
	 * @return
	 */
	private ObjectTablePane<QiRepairMethod> createSelectedRepairMethodPane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Name", "repairMethod").put("Description", "repairMethodDescription");
		ObjectTablePane<QiRepairMethod> panel = new ObjectTablePane<QiRepairMethod>(columnMappingList);
		panel.setStyle("-fx-padding: 5px 0px 5px 0px; -fx-min-height: 125px");
		panel.setPrefWidth(0.45*Screen.getPrimary().getVisualBounds().getWidth());
		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		panel.setConstrainedResize(false);
		panel.getTable().getColumns().get(0).setPrefWidth(panel.getPrefWidth()*0.4);
		panel.getTable().getColumns().get(1).setPrefWidth(panel.getPrefWidth()*0.43);
		return panel;
	}

	public void onTabSelected() {
		List<QiRepairMethod> selectedRepairMethodList =new ArrayList<QiRepairMethod>();
		selectedRepairMethodList.addAll(getAvailableRepairMethodTablePane().getTable().getSelectionModel().getSelectedItems());
		reload(StringUtils.trim(methodFilterTextField.getText()));
		getController().refreshBtnAction(selectedRepairMethodList);
		getController().clearDisplayMessage();
	}

	public String getScreenName() {
		return "Station Repair Method";
	}

	@Override
	public void reload() {
		availableRepairMethodTablePane.setData(getModel().findReairMethodByFilter(""));
	}

	@SuppressWarnings("unchecked")
	public void reload(String filter) {
		ComboBoxDisplayDto dto = (ComboBoxDisplayDto) divisionComboBox.getSelectionModel().getSelectedItem();
		if(dto != null) {
			if(showRepairMethodForDivisionChkBox.isSelected()){
				String divisionName = dto.getId();
				List<QiRepairMethod> filteredRepairMethodsForSelectedDivisionList = getModel().findFilteredRepairMethodsForSelectedDivision(filter, divisionName);
				List<QiRepairMethod> selectedRepairMethodList= getSelectedRepairMethodTablePane().getTable().getItems();
				List<QiRepairMethod> availableRepairMethodFilteredList = new ArrayList<QiRepairMethod>() ;
				for(QiRepairMethod qiRepairMethodObj : filteredRepairMethodsForSelectedDivisionList){
					if(!selectedRepairMethodList.contains(qiRepairMethodObj))
						availableRepairMethodFilteredList.add(qiRepairMethodObj);
				}
	
				availableRepairMethodTablePane.setData(availableRepairMethodFilteredList);
			}
			else if(!showRepairMethodForDivisionChkBox.isSelected()){
				List<QiRepairMethod> selectedRepairMethodList;
				List<QiRepairMethod> repairMethosByFilterList;
					selectedRepairMethodList = getSelectedRepairMethodTablePane().getTable().getItems();
					repairMethosByFilterList = getModel().findReairMethodByFilter(filter);
					if(selectedRepairMethodList.size() !=0){
						List<QiRepairMethod> subtractedAllRepairMethodsList = ListUtils.subtract(repairMethosByFilterList,selectedRepairMethodList);
						availableRepairMethodTablePane.setData(subtractedAllRepairMethodsList);
						}
					else if(selectedRepairMethodList.size() ==0){
						availableRepairMethodTablePane.setData(repairMethosByFilterList);
					}
			}
		}
	}
	@Override
	public void start() {
		
	}

	private String getDefaultSiteName() {
		return getModel().findSiteName();
	}

	public CheckBox getShowRepairMehodForDivisionChkBox() {
		return showRepairMethodForDivisionChkBox;
	}

	public LoggedComboBox<String> getPlantComboBox() {
		return plantComboBox;
	}

	public LoggedButton getSaveBtn() {
		return saveBtn;
	}

	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}

	public LoggedButton getLeftShiftBtn() {
		return leftShiftBtn;
	}

	public LoggedButton getRightShiftBtn() {
		return rightShiftBtn;
	}

	public UpperCaseFieldBean getMethodFilterTextField() {
		return methodFilterTextField;
	}

	public ObjectTablePane<QiRepairMethod> getAvailableRepairMethodTablePane() {
		return availableRepairMethodTablePane;
	}

	public ObjectTablePane<QiRepairMethod> getSelectedRepairMethodTablePane() {
		return selectedRepairMethodTablePane;
	}

	public LoggedComboBox<ComboBoxDisplayDto> getDivisionComboBox() {
		return divisionComboBox;
	}

	public String getDivisionComboBoxSelectedId() {
		ComboBoxDisplayDto dto = (ComboBoxDisplayDto) getDivisionComboBox().getValue();
		String divisionId = "";
		if(dto != null)  {
			divisionId = dto.getId();
		}
		return divisionId;
	}

	public void clearDivisionComboBox() {
		getDivisionComboBox().getItems().clear();
	}

	public ComboBoxDisplayDto getDivisionComboBoxSelectedItem() {
		ComboBoxDisplayDto dto = (ComboBoxDisplayDto) getDivisionComboBox()
				.getSelectionModel().getSelectedItem();
		return dto;
	}
	
	public ListView<String> getQicsStationListView() {
		return qicsStationListView;
	}

	public LoggedLabel getSiteValueLabel() {
		return siteValueLabel;
	}

	public LoggedButton getRefreshBtn() {
		return refreshBtn;
	}


}
