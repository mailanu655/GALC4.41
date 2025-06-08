package com.honda.galc.client.qi.repairentry;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.qi.base.QiFxDialog;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.entity.enumtype.DefectStatus;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.util.Callback;

/**
 * <h3>RepairAreaMgmtDialog Class description</h3>
 * <p>
 * RepairAreaMgmtDialog description
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
 * @author L&T Infotech<br>
 *         Feb 17, 2017
 *
 *
 */

public class RepairAreaMgmtDialog extends QiFxDialog<RepairEntryModel> {

	private RepairAreaMgmtDialogController controller;
	private TreeTableView<QiRepairResultDto> productTable;

	private LoggedButton assignUnitButton;
	private LoggedButton sendVinInTransitButton;
	private LoggedButton returnFromLotButton;
	private LoggedButton printTicketButton;
	private LoggedButton closeButton;

	private LabeledComboBox<String> plantCombobox;
	private LabeledComboBox<String> divisionCombobox;
	private LabeledComboBox<String> targetDeptCombobox;
	private LabeledComboBox<String> targetRepairAreaCombobox;
	private LabeledComboBox<String> parkingRepairAreaCombobox;
	private LabeledComboBox<String> rowCombobox;
	private LabeledComboBox<String> spaceCombobox;

	private LoggedRadioButton insideRadioButton;
	private LoggedRadioButton outsideRadioButton;

	private LoggedLabel plantLabel;
	private LoggedLabel parkingRepairAreaLabel;
	private LoggedLabel rowLabel;
	private LoggedLabel spaceLabel;	
	
	private GridPane repairAreaFilterGrid;
	
	public RepairAreaMgmtDialog(String title, RepairEntryModel model, String applicationId, TreeItem<QiRepairResultDto> qiRepairResultDto) {
		super("Repair Area Management", applicationId, model);

		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);

		this.setController(new RepairAreaMgmtDialogController(model, this, qiRepairResultDto));
		initComponents(); //not include repair area filter, which is dynamically generated
		getController().initData();
		getController().initListeners();
	}

	private void initComponents() {		
		getRootBorderPane().setCenter(createMaintContainer());
	}

	@Override
	public void close() {
		super.close();
		getController().close();
	}

	/**
	 * This method is used to create right side content of the screen
	 * 
	 * @return MigPane
	 */
	private MigPane createMaintContainer(){
		MigPane pane;
		if(getScreenWidth() > 1024){
			pane = new MigPane("insets 05 35 20 20 ", "[center,grow,fill]", "");
		}
		else{
			pane = new MigPane("insets 05 10 20 0 ", "[center,grow,fill]", "");
		}
		
		pane.add(getMainTablePane(),"span,wrap");
		pane.add(createRepairAreaFilterGrid(),"span,wrap");
		pane.add(getExistingVinAssignment(),"span,wrap");
		pane.add(createButtonContainer(),"span,wrap");
		pane.setPrefSize(getScreenWidth()-getScreenWidth()/10, 370 );
		
		return pane;
	}

	private HBox getInOutRadioButtons() {

		final String INSIDE = "Inside";
		final String OUTSIDE = "Outside";

		HBox radioButtonContainer = new HBox();	
		radioButtonContainer.setSpacing(5);
		radioButtonContainer.setPadding(new Insets(10));
		final ToggleGroup radioGroup = new ToggleGroup();

		insideRadioButton = createRadioButton(INSIDE, radioGroup, true,getController());
		insideRadioButton.setSelected(true);
		insideRadioButton.setToggleGroup(radioGroup);

		outsideRadioButton = createRadioButton(OUTSIDE, radioGroup, false,getController());
		outsideRadioButton.setToggleGroup(radioGroup);

		radioButtonContainer.getChildren().addAll(insideRadioButton,outsideRadioButton);
		radioButtonContainer.setAlignment(Pos.CENTER_LEFT);
		
		setFontSize(insideRadioButton);
		setFontSize(outsideRadioButton);
		
		return radioButtonContainer;
	}

	/**
	 * This method is used to create Button container
	 *  
	 * @return HBox containing Assign, Update and Deassign buttons
	 */
	private HBox createButtonContainer() {

		final String ASSIGN_UNIT = "Assign Unit";
		final String SEND_TO_IN_TRANSIT = "Send To In Transit";
		final String RETURN_FROM_LOT = "Return From Repair Area";
		final String PRINT_TICKET = "Print Ticket";
		final String CLOSE = "Close";

		assignUnitButton = createBtn(ASSIGN_UNIT, getController());
		sendVinInTransitButton =createBtn(SEND_TO_IN_TRANSIT, getController());
		returnFromLotButton = createBtn(RETURN_FROM_LOT, getController());
		printTicketButton =createBtn(PRINT_TICKET, getController());
		printTicketButton.setDisable(true);
		returnFromLotButton.setDisable(true);
		sendVinInTransitButton.setDisable(true);
		closeButton = createBtn(CLOSE, getController());
		
		if (getScreenWidth() < 1024) {
			setFontSize(assignUnitButton);
			setFontSize(sendVinInTransitButton);
			setFontSize(returnFromLotButton);
			setFontSize(printTicketButton);
			setFontSize(closeButton);
		}

		HBox btnContainer = new HBox();
		btnContainer.setSpacing(6);
		btnContainer.setAlignment(Pos.CENTER);
		btnContainer.getChildren().addAll(assignUnitButton, sendVinInTransitButton, returnFromLotButton, printTicketButton, closeButton);
		return btnContainer;
	}


	private Node getMainTablePane() {

		HBox mainTablePane = new HBox();
		mainTablePane.setPrefHeight(80);
		mainTablePane.setPadding(new Insets(10));
		mainTablePane.getChildren().addAll(createTreeTablePane());
		mainTablePane.fillHeightProperty();
		mainTablePane.setAlignment(Pos.CENTER);
		return mainTablePane;
	}


	@SuppressWarnings("unchecked")
	private TreeTableView<QiRepairResultDto> createTreeTablePane() {
		double width = getScreenWidth()-getScreenWidth()/20;

		TreeTableColumn<QiRepairResultDto, String> defDescColumn = new TreeTableColumn<QiRepairResultDto, String>("Defect Description");
		defDescColumn.setPrefWidth(width * 0.30);
		defDescColumn.setCellValueFactory(
				new Callback<CellDataFeatures<QiRepairResultDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<QiRepairResultDto, String> param) {
						return new ReadOnlyStringWrapper(param.getValue().getValue().getDefectDesc());
					}
				});

		TreeTableColumn<QiRepairResultDto, String> fixedColumn = new TreeTableColumn<QiRepairResultDto, String>("Fixed");
		fixedColumn.setPrefWidth(width * 0.05);
		fixedColumn.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<QiRepairResultDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<QiRepairResultDto, String> param) {
						String fixedStatus = param.getValue().getValue().getCurrentDefectStatus() == DefectStatus.FIXED.getId() ? "Y" : "N";
						return new ReadOnlyStringWrapper(fixedStatus);
					}

				});

		TreeTableColumn<QiRepairResultDto, String> entryDeptColumn = new TreeTableColumn<QiRepairResultDto, String>("Entry Dept");
		entryDeptColumn.setPrefWidth(width * 0.08);
		entryDeptColumn.setCellValueFactory(
				new Callback<CellDataFeatures<QiRepairResultDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<QiRepairResultDto, String> param) {
						return new ReadOnlyStringWrapper(param.getValue().getValue().getEntryDept());
					}
				});

		TreeTableColumn<QiRepairResultDto, String> processPointColumn = new TreeTableColumn<QiRepairResultDto, String>("Process Point");
		processPointColumn.setPrefWidth(width * 0.10);
		processPointColumn.setCellValueFactory(
				new Callback<CellDataFeatures<QiRepairResultDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<QiRepairResultDto, String> param) {
						return new ReadOnlyStringWrapper(param.getValue().getValue().getApplicationId());
					}
				});

		TreeTableColumn<QiRepairResultDto, String> repairAreaColumn = new TreeTableColumn<QiRepairResultDto, String>("Repair Area");
		repairAreaColumn.setPrefWidth(width * 0.20);
		repairAreaColumn
		.setCellValueFactory(new Callback<CellDataFeatures<QiRepairResultDto, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<QiRepairResultDto, String> param) {
				return new ReadOnlyStringWrapper(param.getValue().getValue().getRepairArea());
			}
		});

		TreeTableColumn<QiRepairResultDto, String> imageNameColumn = new TreeTableColumn<QiRepairResultDto, String>("Image Name");
		imageNameColumn.setPrefWidth(width * 0.10);
		imageNameColumn.setCellValueFactory(
				new Callback<CellDataFeatures<QiRepairResultDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<QiRepairResultDto, String> param) {
						return new ReadOnlyStringWrapper(param.getValue().getValue().getImageName());
					}
				});

		final TreeItem<QiRepairResultDto> root = new TreeItem<QiRepairResultDto>(new QiRepairResultDto());
		productTable = new TreeTableView<QiRepairResultDto>(root);
		productTable.getColumns().setAll(defDescColumn, fixedColumn, entryDeptColumn,
				processPointColumn, repairAreaColumn, imageNameColumn);
		productTable.setShowRoot(false);

		return productTable;
	}
	
	
	private GridPane createRepairAreaFilterGrid() {
		repairAreaFilterGrid = createGrid();
		repairAreaFilterGrid.add(getInOutRadioButtons(), 0, 0);
		if(getScreenWidth()>1024)
			repairAreaFilterGrid.setPadding(new Insets( 0, 10, 10, 40));
		
		repairAreaFilterGrid.setPrefHeight(155);
		repairAreaFilterGrid.setAlignment(Pos.TOP_LEFT);
		setFontSize(repairAreaFilterGrid);
		return repairAreaFilterGrid;
	}
	
	public void addInsideFilterComponents() {

		plantCombobox = createLabeledComboBox("plantCombobox", "Plant  ", true, false);
		divisionCombobox = createLabeledComboBox("divisionCombobox", "Division  ", true, false);
		parkingRepairAreaCombobox = createLabeledComboBox("parkingRepairAreaCombobox", "Repair Area  ", true, false);

		repairAreaFilterGrid.add(plantCombobox, 0, 1);
		repairAreaFilterGrid.add(divisionCombobox, 1, 1);
		repairAreaFilterGrid.add(parkingRepairAreaCombobox, 2, 1);
		
		if (getScreenWidth() < 1024) {
			setFontSize(plantCombobox.getControl());
			setFontSize(divisionCombobox.getControl());
			setFontSize(parkingRepairAreaCombobox.getControl());
		}
	}
	
	public void addOutsideFilterComponents() {
		targetDeptCombobox = createLabeledComboBox("targetDeptCombobox", "Target Dept  ", true, false);
		targetRepairAreaCombobox = createLabeledComboBox("targetRepairAreaCombobox", "Target Repair Area  ", true, false);
		plantCombobox = createLabeledComboBox("plantCombobox", "Plant  ", true, false);
		divisionCombobox = createLabeledComboBox("divisionCombobox", "Division  ", true, false);
		parkingRepairAreaCombobox = createLabeledComboBox("parkingRepairAreaCombobox", "Repair Area  ", true, false);
		rowCombobox = createLabeledComboBox("rowCombobox", "Row ", true, false);
		spaceCombobox = createLabeledComboBox("spaceCombobox", "Space ", true, false);
		
		repairAreaFilterGrid.add(targetDeptCombobox, 3, 1);
		repairAreaFilterGrid.add(targetRepairAreaCombobox, 4, 1);
		
		repairAreaFilterGrid.add(plantCombobox, 0, 2);
		repairAreaFilterGrid.add(divisionCombobox, 1, 2);
		repairAreaFilterGrid.add(parkingRepairAreaCombobox, 2, 2);
		repairAreaFilterGrid.add(rowCombobox, 3, 2);
		repairAreaFilterGrid.add(spaceCombobox, 4, 2);
		
		if (getScreenWidth() < 1024) {
			setFontSize(plantCombobox.getControl());
			setFontSize(divisionCombobox.getControl());
			setFontSize(targetDeptCombobox.getControl());
			setFontSize(targetRepairAreaCombobox.getControl());
			setFontSize(parkingRepairAreaCombobox.getControl());
			setFontSize(rowCombobox.getControl());
			setFontSize(spaceCombobox.getControl());
		}
	}
	

	private GridPane getExistingVinAssignment() {
		GridPane existingVinAssignmentGrid = createGrid();
		
		LoggedLabel vinIsParkedLabel = new LoggedLabel("parkedLabel","[------------------------------------------------------------------------------------------"
				+ "- Product is currently parked at ----------------------------------------------------------------------------------]");
		
		existingVinAssignmentGrid.add(vinIsParkedLabel, 0, 0 , 4, 1);
		
		plantLabel = new LoggedLabel("plantLabel");
		parkingRepairAreaLabel = new LoggedLabel("parkingRepairAreaLabel");
		rowLabel = new LoggedLabel("rowLabel");
		spaceLabel = new LoggedLabel("spaceLabel");
		
		plantLabel.setPadding(new Insets(0, 0, 0, 30));
		if(getScreenWidth()< 1024){
			vinIsParkedLabel.setPadding(new Insets(0, 0, 0, 50));
			plantLabel.setPadding(new Insets(0, 0, 0, 70));
		}
			
		existingVinAssignmentGrid.setPadding(new Insets(0));
		
		existingVinAssignmentGrid.add(plantLabel, 0, 1);
		existingVinAssignmentGrid.add(parkingRepairAreaLabel, 1, 1);
		existingVinAssignmentGrid.add(rowLabel, 2, 1);
		existingVinAssignmentGrid.add(spaceLabel, 3, 1);
		
		existingVinAssignmentGrid.getColumnConstraints().addAll(setColumnConstraints(HPos.LEFT),setColumnConstraints(HPos.LEFT),setColumnConstraints(HPos.LEFT),setColumnConstraints(HPos.LEFT));
		setFontSize(existingVinAssignmentGrid);
		return existingVinAssignmentGrid;
	}

	/** This method is used to return gridPane template
	 * 
	 * @return
	 */
	private GridPane createGrid() {
		GridPane gridpane = new GridPane();
		gridpane.setHgap(10);
		gridpane.setVgap(5);
		gridpane.setPadding(new Insets( 0, 10, 10, 0));
		gridpane.setAlignment(Pos.CENTER);
		gridpane.setGridLinesVisible(false);
		return gridpane;
	}
	
	/**
	 * This method is used to create ColumnConstraints
	 * 
	 * @param hpos
	 * @return columnConstraint
	 */
	private ColumnConstraints setColumnConstraints(HPos hpos) {
		ColumnConstraints column = new ColumnConstraints();
		column.setHalignment(hpos);
		column.setPrefWidth(250);
		return column;
	}
	
	/** This method will set font size of the components/labels 
	 * in the node according to screen size.
	 * 
	 * @param node
	 */
	public void setFontSize(Node node){
		node.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.01 * getScreenWidth())));
	}
	

	/** This method will return the screen width.
	 * 
	 * @return screen width
	 */
	private double getScreenWidth() {
		return  Screen.getPrimary().getVisualBounds().getWidth();
	}

	
	public TreeTableView<QiRepairResultDto> getProductTable() {
		return productTable;
	}

	public void setProductTable(TreeTableView<QiRepairResultDto> productTable) {
		this.productTable = productTable;
	}

	public RepairAreaMgmtDialogController getController() {
		return controller;
	}

	public void setController(RepairAreaMgmtDialogController controller) {
		this.controller = controller;
	}

	private BorderPane getRootBorderPane() {
		return (BorderPane) getScene().getRoot();
	}

	/**
	 * @return the assignUnitButton
	 */
	public LoggedButton getAssignUnitButton() {
		return assignUnitButton;
	}

	/**
	 * @return the sendVinInTransitButton
	 */
	public LoggedButton getSendVinInTransitButton() {
		return sendVinInTransitButton;
	}

	/**
	 * @return the returnFromLotButton
	 */
	public LoggedButton getReturnFromRepairAreaButton() {
		return returnFromLotButton;
	}

	/**
	 * @return the printTicketButton
	 */
	public LoggedButton getPrintTicketButton() {
		return printTicketButton;
	}

	/**
	 * @return the closeButton
	 */
	public LoggedButton getCloseButton() {
		return closeButton;
	}



	public LabeledComboBox<String> getPlantCombobox() {
		return plantCombobox;
	}
	
	public LabeledComboBox<String> getDivisionCombobox() {
		return divisionCombobox;
	}

	public LabeledComboBox<String> getTargetDeptCombobox() {
		return targetDeptCombobox;
	}

	public LabeledComboBox<String> getTargetRepairAreaCombobox() {
		return targetRepairAreaCombobox;
	}

	public LabeledComboBox<String> getParkingRepairAreaCombobox() {
		return parkingRepairAreaCombobox;
	}

	public LabeledComboBox<String> getRowCombobox() {
		return rowCombobox;
	}

	public LabeledComboBox<String> getSpaceCombobox() {
		return spaceCombobox;
	}

	public LoggedLabel getPlantLabel() {
		return plantLabel;
	}


	public LoggedLabel getParkingRepairAreaLabel() {
		return parkingRepairAreaLabel;
	}

	public LoggedLabel getRowLabel() {
		return rowLabel;
	}

	public LoggedLabel getSpaceLabel() {
		return spaceLabel;
	}

	public void setPlantLabel(LoggedLabel plantLabel) {
		this.plantLabel = plantLabel;
	}


	public void setParkingRepairAreaLabel(LoggedLabel parkingRepairAreaLabel) {
		this.parkingRepairAreaLabel = parkingRepairAreaLabel;
	}

	public void setRowLabel(LoggedLabel rowLabel) {
		this.rowLabel = rowLabel;
	}

	public void setSpaceLabel(LoggedLabel spaceLabel) {
		this.spaceLabel = spaceLabel;
	}

	public LoggedRadioButton getInsideRadioBtn() {
		return insideRadioButton;
	}

	public LoggedRadioButton getOutsideRadioBtn() {
		return outsideRadioButton;
	}

	public GridPane getRepairAreaFilterGrid() {
		return repairAreaFilterGrid;
	}

	public void setRepairAreaFilterGrid(GridPane repairAreaFilterGrid) {
		this.repairAreaFilterGrid = repairAreaFilterGrid;
	}
}
