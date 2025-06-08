package com.honda.galc.client.teamleader.qi.view;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.ResponsibleLoadTriggeredController;
import com.honda.galc.client.teamleader.qi.model.ResponsibleLoadTriggeredModel;
import com.honda.galc.client.teamleader.qi.stationconfig.dto.ComboBoxDisplayDto;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiPddaResponsibleLoadTriggerDto;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Screen;

/**
 * 
 * <h3>ResponsibleLoadTriggeredPanel Class description</h3>
 * <p>
 * ResponsibleLoadTriggeredPanel is used to load data in TableViews and perform the actions
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
public class ResponsibleLoadTriggeredPanel extends QiAbstractTabbedView<ResponsibleLoadTriggeredModel,ResponsibleLoadTriggeredController> {

	private LabeledComboBox<String> companyComboBox;
	private LabeledComboBox<String> plantComboBox;
	private LabeledComboBox<String> productComboBox;
	private LabeledComboBox<ComboBoxDisplayDto> departmentComboBox;
	private LoggedRadioButton fixedRadioBtn;
	private LoggedRadioButton confirmedRadioBtn;
	private LoggedRadioButton allRadioBtn;
	private double screenWidth;
	private double screenHeight;
	private ObjectTablePane<QiPddaResponsibleLoadTriggerDto> pddaResponsibilityTablePane;
	
	public ResponsibleLoadTriggeredPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);
	}

	public void onTabSelected() {
		getController().activate();
	}

	public String getScreenName() {
		return "Responsible Load Triggered";
	}

	@Override
	public void reload() {
		getController().loadPddaResponsibleTriggerData(getDepartmentComboBoxSelectedId());
	}

	@Override
	public void start() {
		
	}
	/*
	 * this method is used to initialize main MigPane
	 */
	@Override
	public void initView() {
		initComponent();
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		screenWidth = screenBounds.getWidth();
		screenHeight = screenBounds.getHeight();
		MigPane mainPane = new MigPane("insets 5 5 5 5", "[]", "[][]");
		mainPane.add(createTitledPane("Qualify Data",createDataQualifierContainer(),screenWidth*0.30,screenHeight*0.10));
		mainPane.add(companyComboBox);
		mainPane.add(plantComboBox);
		mainPane.add(productComboBox);
		mainPane.add(departmentComboBox,"wrap");
		mainPane.add(createPddaResponsibleTablePane(),"span");
		this.setCenter(mainPane);
		
		
	}
	/*
	 * this method is used to initialize different components
	 */
	private void initComponent() {
		companyComboBox = createLabeledComboBox("companyComboBox", "Company", true, true);
		companyComboBox.getControl().setPrefWidth(screenWidth*0.15);
		companyComboBox.getControl().setItems(FXCollections.observableArrayList(getModel().findAllRespCompanyByAdminConfirmedFix((short)0)));
		plantComboBox = createLabeledComboBox("plantComboBox", "Plant", true, true);
		plantComboBox.getControl().setPrefWidth(screenWidth*0.15);
		productComboBox = createLabeledComboBox("productComboBox", "Product", true, true);
		productComboBox.getControl().setPrefWidth(screenWidth*0.15);
		departmentComboBox = createCustomComboBox("departmentComboBox", "Dept", true, true);
		departmentComboBox.getControl().setPrefWidth(screenWidth*0.15);
		
		ToggleGroup group = new ToggleGroup();
		fixedRadioBtn = createRadioButton("Needs Confirmed/Fixed", group, true, getController());
		fixedRadioBtn.setId(QiConstant.FIXED);
		fixedRadioBtn.getStyleClass().add("display-label");
		fixedRadioBtn.setId("Fixed");
		
		confirmedRadioBtn = createRadioButton("Confirmed", group, false, getController());
		confirmedRadioBtn.setId(QiConstant.CONFIRMED);
		confirmedRadioBtn.getStyleClass().add("display-label");
		confirmedRadioBtn.setId("Confirmed");
		
		allRadioBtn = createRadioButton("All", group, false, getController());
		allRadioBtn.setId(QiConstant.ALL);
		allRadioBtn.getStyleClass().add("display-label");
		allRadioBtn.setId("All");
	}
	/*
	 * this method is used to create radio button pane
	 */
	private MigPane createDataQualifierContainer() {
		MigPane dataQualifierPane = new MigPane("insets 5", "", "");
		dataQualifierPane.add(fixedRadioBtn);
		dataQualifierPane.add(confirmedRadioBtn);
		dataQualifierPane.add(allRadioBtn);
		return dataQualifierPane;
	}
	/**
	 * This method is used to create the TitlePane 
	 * @param title  
	 * @param content 
	 * @param width
	 * @param height
	 * @return
	 */
	private static TitledPane createTitledPane(String title,Node content,double width,double height) {
        TitledPane titledPane = new TitledPane();
        titledPane.setText(title);
        titledPane.setContent(content);
        titledPane.setPrefSize(width, height);
        return titledPane;
	}
	/**
	 * This method is used to create LabeledComboBox
	 * @param id
	 * @return
	 */
	public LabeledComboBox<ComboBoxDisplayDto> createCustomComboBox(String id, String labelName, boolean isHorizontal, boolean isMandatory) {
		LabeledComboBox<ComboBoxDisplayDto> comboBox = new LabeledComboBox<ComboBoxDisplayDto>(labelName,isHorizontal,new Insets(0),true,isMandatory);
		comboBox.setId(id);
		comboBox.getControl().setMinHeight(25);
		comboBox.setPadding(new Insets(0, 10, 0, 10));
		comboBox.getControl().getStyleClass().add("combo-box-base");
		return comboBox;
	}
	/**
	 * This method is used to create LabeledComboBox
	 * @param id
	 * @return
	 */
	public LabeledComboBox<String> createLabeledComboBox(String id, String labelName, boolean isHorizontal, boolean isMandatory) {
		LabeledComboBox<String> comboBox = new LabeledComboBox<String>(labelName,isHorizontal,new Insets(0),true,isMandatory);
		comboBox.setId(id);
		comboBox.getControl().setMinHeight(25);
		comboBox.setPadding(new Insets(0, 10, 0, 10));
		comboBox.getControl().getStyleClass().add("combo-box-base");
		return comboBox;
	}
	/**
	 * This method is used to create the TableView
	 * @return
	 */
	private ObjectTablePane<QiPddaResponsibleLoadTriggerDto> createPddaResponsibleTablePane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Responsibility Id","pddaResponsibilityId").put("Data Location", "dataLocation")
				.put("Confirmed", "adminConfirmedFix").put("Timestamp", "dateTimestamp")
				.put("Line", "line").put("Base Part no", "basePartNo").put("Team Group", "teamGroupNo").put("Team", "teamNo").put("Process No", "processNo")
				.put("Process Name", "processName").put("Unit No", "unitNo").put("Unit Desc", "unitDesc");
		Double[] columnWidth = new Double[] { 0.15, 0.08, 0.04, 0.09, 0.03, 0.10, 0.05, 0.05, 0.05, 0.09, 0.12, 0.03, 0.07 };
	    pddaResponsibilityTablePane = new ObjectTablePane<QiPddaResponsibleLoadTriggerDto>(columnMappingList,
				columnWidth);
		pddaResponsibilityTablePane.setConstrainedResize(true);
		LoggedTableColumn<QiPddaResponsibleLoadTriggerDto, Integer> rowIndex = new LoggedTableColumn<QiPddaResponsibleLoadTriggerDto, Integer>();
		createSerialNumber(rowIndex);
		pddaResponsibilityTablePane.getTable().getColumns().add(0, rowIndex);
		pddaResponsibilityTablePane.getTable().getColumns().get(0).setText("#");
		pddaResponsibilityTablePane.getTable().getColumns().get(0).setResizable(true);
		pddaResponsibilityTablePane.getTable().getColumns().get(0).setMaxWidth(100);
		pddaResponsibilityTablePane.getTable().getColumns().get(0).setMinWidth(25);
		pddaResponsibilityTablePane.setId("pddaResponsibleTablePane");
		pddaResponsibilityTablePane.setPadding(new Insets(0, 0, 0, 0));
		pddaResponsibilityTablePane.setPrefSize(screenWidth, screenHeight*0.7);
		return pddaResponsibilityTablePane;
	}
	public LabeledComboBox<String> getCompanyComboBox() {
		return companyComboBox;
	}

	public LabeledComboBox<String> getPlantComboBox() {
		return plantComboBox;
	}

	public LabeledComboBox<String> getProductComboBox() {
		return productComboBox;
	}

	public LabeledComboBox<ComboBoxDisplayDto> getDepartmentComboBox() {
		return departmentComboBox;
	}

	public String getDepartmentComboBoxSelectedId() {
		ComboBoxDisplayDto dto = (ComboBoxDisplayDto) getDepartmentComboBox().getControl()
				.getSelectionModel().getSelectedItem();
		String deptId = "";
		if(dto != null)  {
			deptId = dto.getId();
		}
		return deptId;
	}

	public String getPlantComboBoxSelectedId() {
		String selectedItem = getPlantComboBox().getControl()
		.getSelectionModel().getSelectedItem().toString();
		return selectedItem;
	}

	public String getCompanyComboBoxSelectedId() {
		String selectedDept = getCompanyComboBox().getControl()
		.getSelectionModel().getSelectedItem().toString();
		return selectedDept;
	}

	public LoggedRadioButton getFixedRadioBtn() {
		return fixedRadioBtn;
	}

	public LoggedRadioButton getConfirmedRadioBtn() {
		return confirmedRadioBtn;
	}

	public LoggedRadioButton getAllRadioBtn() {
		return allRadioBtn;
	}

	public ObjectTablePane<QiPddaResponsibleLoadTriggerDto> getPddaResponsibilityTablePane() {
		return pddaResponsibilityTablePane;
	}
	
}
