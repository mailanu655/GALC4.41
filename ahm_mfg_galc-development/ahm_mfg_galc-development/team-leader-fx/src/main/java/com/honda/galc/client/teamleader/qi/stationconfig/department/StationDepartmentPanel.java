package com.honda.galc.client.teamleader.qi.stationconfig.department;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigModel;
import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigPanel;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.IDto;
import com.honda.galc.dto.qi.QiEntryDepartmentDto;
import com.honda.galc.dto.qi.QiWriteUpDepartmentDto;
import com.honda.galc.entity.enumtype.StationConfigurationOperations;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.util.Callback;

/**
 * 
 * <h3>StationDepartmentPanel Class description</h3>
 * <p>
 * StationDepartmentPanel is used to load data in TableViews and perform the action on Update , Reset and Shift buttonsetc.
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
public class StationDepartmentPanel {
	private StationDepartmentController controller;
	private ObjectTablePane<QiEntryDepartmentDto> currentlyAssignedEntryDeptObjectTablePane;
	private LoggedButton rightShiftDepartmentBtn;
	private LoggedButton leftShiftDepartmentBtn;
	private LoggedButton rightShiftWriteUpDepartmentBtn;
	private LoggedButton leftShiftWriteUpDepartmentBtn;
	private LoggedButton updateWriteUpDepartmentBtn;
	private LoggedButton updateEntryDepartmentBtn;
	private LoggedButton resetEntryDepartmentBtn;
	private ObjectTablePane<QiEntryDepartmentDto> availableEntryDepartObjectTablePane;
	private ObjectTablePane<QiWriteUpDepartmentDto> currentlyAssignedWriteUpDepartObjectTablePane;
	private ObjectTablePane<QiWriteUpDepartmentDto> availableWriteUpDepartObjectTablePane;
	private LoggedButton resetWriteUpDepartmentBtn;
	private static WriteUpDeptController writeUpDeptController;
	private double width;
	private SimpleBooleanProperty defaultValue;
	private SimpleBooleanProperty falseValue;

	public StationDepartmentPanel (EntryStationConfigModel model,WriteUpDeptController writeUpDeptController,EntryStationConfigPanel view) {
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		this.width = screenBounds.getWidth();
		this.defaultValue = new SimpleBooleanProperty();
		this.falseValue = new SimpleBooleanProperty();
		this.controller=new StationDepartmentController(model, view);
		this.writeUpDeptController=writeUpDeptController;
	}

	/**
	 * Method to be called from Parent Panel to initialize Controller
	 */
	public void activateController() {
		controller.initListeners();
	}

	/**
	 * This method used to load data on panel
	 */
	public void reload() {
		controller.loadInitialData();
	}

	/**
	 * This method is used to create Entry Dept Panel
	 * @return
	 */
	public MigPane getEntryDepartmentPanel(){
		MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");
		VBox entryDeptContainer = new VBox();
		HBox assignmentContainer = new HBox();
		VBox currentlyAssigned= new VBox();
		LoggedLabel currentlyAssignedLabel= UiFactory.createLabel("currentlyAssigned", "Currently Assigned",13);
		currentlyAssignedEntryDeptObjectTablePane= creatEntryDepartmentTablePane("currentlyAssigned");
		currentlyAssignedEntryDeptObjectTablePane.setId("currentlyAssignedEntryDeptObjectTablePane");
		currentlyAssignedEntryDeptObjectTablePane.setSelectionMode(SelectionMode.MULTIPLE);
		
		currentlyAssignedEntryDeptObjectTablePane.addListener(new ChangeListener<QiEntryDepartmentDto>() {

			public void changed(
					ObservableValue<? extends QiEntryDepartmentDto> arg0,
					QiEntryDepartmentDto arg1, QiEntryDepartmentDto arg2) {
				
				if(arg2 != null && arg2.getDivisionId() != null)
					Logger.getLogger().check("Currently Assigned Entry Dept : " + arg2.getDivisionId() + " selected");
			}
		});
		
		HBox lblRow = new HBox();
		lblRow.getChildren().addAll(currentlyAssignedLabel);
		currentlyAssigned.getChildren().addAll(lblRow,currentlyAssignedEntryDeptObjectTablePane);
		VBox shiftButton = new VBox();
		rightShiftDepartmentBtn= createBtn(">",getController());
		rightShiftDepartmentBtn.setId(StationConfigurationOperations.RIGHT_SHIFT_DEPARTMENT.getName());
		leftShiftDepartmentBtn= createBtn("<",getController());
		leftShiftDepartmentBtn.setId(StationConfigurationOperations.LEFT_SHIFT_DEPARTMENT.getName());
		shiftButton.getChildren().addAll(rightShiftDepartmentBtn,leftShiftDepartmentBtn);
		shiftButton.setPadding(new Insets(45, 0, 0, 0));
		assignmentContainer.getChildren().addAll(createAvailableEntryDept(),shiftButton,currentlyAssigned);
		assignmentContainer.setPadding(new Insets(0, 0, 5, 15));
		assignmentContainer.setSpacing(10);

		updateEntryDepartmentBtn = createBtn("Update", getController());
		updateEntryDepartmentBtn.setId(StationConfigurationOperations.UPDATE_ENTRY_DEPARTMENT.getName());
		updateEntryDepartmentBtn.setDisable(true);

		HBox btnRow = new HBox();
		resetEntryDepartmentBtn = createBtn("Reset",getController());
		resetEntryDepartmentBtn.setId(StationConfigurationOperations.RESET_ENTRY_DEPARTMENT.getName());
		resetEntryDepartmentBtn.setDisable(true);
		btnRow.getChildren().addAll(updateEntryDepartmentBtn,resetEntryDepartmentBtn);
		btnRow.setAlignment(Pos.CENTER_RIGHT);
		entryDeptContainer.getChildren().addAll(assignmentContainer,btnRow);

		pane.add(EntryStationConfigPanel.createTitledPane("Entry Dept",entryDeptContainer,(width*0.50),200),"span,wrap");
		
		return pane;
	}

	/**
	 * This method is used to create available EntryDept and currently assigned Entry Dept table view 
	 * @param department
	 * @return
	 */
	private ObjectTablePane<QiEntryDepartmentDto> creatEntryDepartmentTablePane(String department){ 
		ColumnMappingList columnMappingList=null;
		Double[] columnWidth=null;
		if(department.equalsIgnoreCase("available")){
			columnMappingList = ColumnMappingList.with("Entry Dept", "divisionId").put("Name", "divisionName");
			columnWidth = new Double[] {0.04,0.1};
			ObjectTablePane<QiEntryDepartmentDto> panel = new ObjectTablePane<QiEntryDepartmentDto>(columnMappingList,columnWidth);
			return panel;	
		}
		else{
			columnMappingList = ColumnMappingList.with("Entry Dept", "divisionId").put("Name", "divisionName");
			columnWidth = new Double[] {0.04,0.1};
			ObjectTablePane<QiEntryDepartmentDto> panel = new ObjectTablePane<QiEntryDepartmentDto>(columnMappingList,columnWidth);
			LoggedTableColumn<QiEntryDepartmentDto,Boolean> defaultImageCol = new LoggedTableColumn<QiEntryDepartmentDto,Boolean>();
			createDefaultEntryDepartment(defaultImageCol);
			panel.getTable().getColumns().add(0, defaultImageCol);
			panel.getTable().getColumns().get(0).setText("Default");
			panel.getTable().getColumns().get(0).setResizable(true);
			panel.getTable().getColumns().get(0).setPrefWidth(60);
			return panel;
		}
	}

	/**
	 * This method is used to create Available EntryDept table view
	 * @return
	 */
	private VBox createAvailableEntryDept(){
		VBox availableDepartment= new VBox();
		LoggedLabel available= UiFactory.createLabel("available", "Available",13);
		availableEntryDepartObjectTablePane = creatEntryDepartmentTablePane("available");
		availableEntryDepartObjectTablePane.setSelectionMode(SelectionMode.MULTIPLE);
		availableEntryDepartObjectTablePane.setPrefWidth(availableDepartment.getPrefWidth());
		availableEntryDepartObjectTablePane.setId("availableEntryDepartObjectTablePane");
		
		availableEntryDepartObjectTablePane.addListener(new ChangeListener<QiEntryDepartmentDto>() {
			
			public void changed(ObservableValue<? extends QiEntryDepartmentDto> arg0,
					QiEntryDepartmentDto arg1, QiEntryDepartmentDto arg2) {
				
				if(arg2 != null && arg2.getDivisionId() != null)
					Logger.getLogger().check("Available Entry Dept : " + arg2.getDivisionId() + " selected");
			}
		});
		
		availableDepartment.getChildren().addAll(available,availableEntryDepartObjectTablePane);
		return availableDepartment;
	}
	/**
	 * This method is used to create boolean binding on the default column
	 * @param rowIndex
	 */
	public void createDefaultEntryDepartment(LoggedTableColumn<QiEntryDepartmentDto, Boolean> rowIndex){
		defaultValue.set(true);
		falseValue.set(false);
		rowIndex.setCellValueFactory(new Callback<CellDataFeatures<QiEntryDepartmentDto, Boolean>, ObservableValue<Boolean>>() {
			public ObservableValue<Boolean> call(CellDataFeatures<QiEntryDepartmentDto, Boolean> p) {
				return Bindings.equal((p.getValue().getIsDefault()==(short)1?defaultValue:falseValue),defaultValue);
			}
		});
		createDefaultImage(rowIndex);
	}
	/**
	 * This method is used to create Button.
	 * @param text
	 * @param handler
	 * @return
	 */
	public static LoggedButton createBtn(String text,EventHandler<ActionEvent> handler)
	{
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.defaultButtonProperty().bind(btn.focusedProperty());
		btn.setOnAction(handler);
		btn.getStyleClass().add("main-screen-btn");
		btn.setStyle("-fx-font-size: 10px;");
		return btn;
	}

	/**
	 * This method is used to create Writeup Dept table view
	 * @return
	 */
	public MigPane getWriteUpDepartmentPanel(){
		VBox shiftButton = new VBox();
		rightShiftWriteUpDepartmentBtn= createBtn(">",getController());
		rightShiftWriteUpDepartmentBtn.setId(StationConfigurationOperations.RIGHT_SHIFT_WRITEUP_DEPARTMENT.getName());
		leftShiftWriteUpDepartmentBtn= createBtn("<",getController());
		leftShiftWriteUpDepartmentBtn.setId(StationConfigurationOperations.LEFT_SHIFT_WRITEUP_DEPARTMENT.getName());
		shiftButton.getChildren().addAll(rightShiftWriteUpDepartmentBtn,leftShiftWriteUpDepartmentBtn);
		shiftButton.setPadding(new Insets(35, 0, 0, 0));
		VBox availableWriteUpDepartment= new VBox();
		LoggedLabel available= UiFactory.createLabel("available", "Available",13);
		availableWriteUpDepartObjectTablePane = createWriteUpDepartmentTablePane("available");
		availableWriteUpDepartObjectTablePane.setSelectionMode(SelectionMode.MULTIPLE);
		availableWriteUpDepartment.getChildren().addAll(available,availableWriteUpDepartObjectTablePane);
		return createWriteUpDeptPanel(availableWriteUpDepartment,shiftButton);
	}
	/**
	 * This method is used to create WriteUp Dept table view
	 * @param availableWriteUpDepartment
	 * @param shiftButton
	 * @return
	 */
	private MigPane createWriteUpDeptPanel(VBox availableWriteUpDepartment,VBox shiftButton){
		MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");
		VBox currentlyAssigned= new VBox();
		HBox tableContainer= new HBox();
		VBox writeupDeptContainer = new VBox();
		HBox topRow = new HBox();
		HBox btnRow = new HBox();
		LoggedLabel currentlyAssignedLabel= UiFactory.createLabel("currentlyAssigned", "Currently Assigned", 13);
		HBox updateResetButton = new HBox();
		updateWriteUpDepartmentBtn= createBtn("Update", getController());
		updateWriteUpDepartmentBtn.setId(StationConfigurationOperations.UPDATE_WRITEUP_DEPARTMENT.getName());
		updateWriteUpDepartmentBtn.setDisable(true);
		resetWriteUpDepartmentBtn =createBtn("Reset",getController());
		resetWriteUpDepartmentBtn.setId(StationConfigurationOperations.RESET_WRITEUP_DEPARTMENT.getName());
		resetWriteUpDepartmentBtn.setDisable(true);
		updateResetButton.getChildren().addAll(updateWriteUpDepartmentBtn,resetWriteUpDepartmentBtn);		
		topRow.getChildren().addAll(currentlyAssignedLabel);
		btnRow.getChildren().add(updateResetButton);
		btnRow.setAlignment(Pos.CENTER_RIGHT);
		
		currentlyAssignedWriteUpDepartObjectTablePane= createWriteUpDepartmentTablePane("currentlyAssigned");
		currentlyAssignedWriteUpDepartObjectTablePane.setSelectionMode(SelectionMode.MULTIPLE);
		
		currentlyAssigned.getChildren().addAll(currentlyAssignedLabel,currentlyAssignedWriteUpDepartObjectTablePane);
		tableContainer.getChildren().addAll(availableWriteUpDepartment,shiftButton,currentlyAssigned);
		tableContainer.setPadding(new Insets(0, 0, 5, 12));
		tableContainer.setSpacing(10);
		writeupDeptContainer.getChildren().addAll(tableContainer,btnRow);
		pane.add(EntryStationConfigPanel.createTitledPane("WriteUp Dept",writeupDeptContainer,(width*0.50),200),"span,wrap");
		return pane;
	}
	/**
	 * This method is used to create a Table Pane.
	 * @return
	 */
	private ObjectTablePane<QiWriteUpDepartmentDto> createWriteUpDepartmentTablePane(String department){ 
		ColumnMappingList columnMappingList=null;;
		Double[] columnWidth=null;
		if(department.equalsIgnoreCase("available")){
			columnMappingList = ColumnMappingList.with("WriteUp Dept", "divisionId").put("Name", "divisionName");
			columnWidth = new Double[] {0.04,0.1};
			ObjectTablePane<QiWriteUpDepartmentDto> panel = new ObjectTablePane<QiWriteUpDepartmentDto>(columnMappingList,columnWidth);
			return panel;
		}
		else 
		{
			columnMappingList = ColumnMappingList.with("WriteUp Dept", "divisionId").put("Name", "divisionName").put("Ink", "colorCode");
			columnWidth = new Double[] {0.04,0.1,0.1};
			ObjectTablePane<QiWriteUpDepartmentDto> panel = new ObjectTablePane<QiWriteUpDepartmentDto>(columnMappingList,columnWidth);
			LoggedTableColumn<QiWriteUpDepartmentDto,Boolean> defaultImageCol = new LoggedTableColumn<QiWriteUpDepartmentDto,Boolean>();
			createDefaultWriteupDepartment(defaultImageCol);
			panel.getTable().getColumns().add(0, defaultImageCol);
			panel.getTable().getColumns().get(0).setText("Default");
			panel.getTable().getColumns().get(0).setResizable(true);
			panel.getTable().getColumns().get(0).setMaxWidth(60);
			panel.getTable().getColumns().get(0).setMinWidth(60);
			return panel;
		}
	}
	/**
	 * This method is used to create boolean binding on default column
	 */
	public void createDefaultWriteupDepartment(LoggedTableColumn<QiWriteUpDepartmentDto, Boolean> rowIndex) {
		defaultValue.set(true);
		falseValue.set(false);
		rowIndex.setCellValueFactory(new Callback<CellDataFeatures<QiWriteUpDepartmentDto, Boolean>, ObservableValue<Boolean>>() {
			public ObservableValue<Boolean> call(CellDataFeatures<QiWriteUpDepartmentDto, Boolean> p) {
				return Bindings.equal((p.getValue().getIsDefault()==(short)1?defaultValue:falseValue),defaultValue);
			}
		});
		createDefaultImage(rowIndex);	
	}
	/**
	 * This method is used to set 'tick image' on the default column
	 * @param rowIndex
	 */
	static void createDefaultImage(LoggedTableColumn rowIndex) {
		rowIndex.setCellFactory( new Callback<LoggedTableColumn<IDto, Boolean>, LoggedTableCell<IDto,Boolean>>()
		{
			public LoggedTableCell<IDto,Boolean> call(LoggedTableColumn<IDto,Boolean> p)
			{	
				return new LoggedTableCell<IDto,Boolean>()
				{
					@Override
					public void updateItem( Boolean item, boolean empty )
					{
						super.updateItem( item, empty ); 
						if(empty){
							setText(null);
							setGraphic(null);	
						}else{
							if(item){					
								ImageView imageView = new ImageView();
								Image image = new Image(getClass().getResource("/resource/com/honda/galc/client/images/qi/tick.jpg").toString());
								imageView.setImage(image);
								imageView.setFitWidth(25);
								imageView.setFitHeight(25);
								setGraphic(imageView);
							}else
							{ setText(null);
							setGraphic(null);	
							}
						}
					}
				};
			}
		});
	}
	
	public ObjectTablePane<QiEntryDepartmentDto> getCurrentlyAssignedEntryDeptObjectTablePane() {
		return currentlyAssignedEntryDeptObjectTablePane;
	}

	public void setCurrentlyAssignedEntryDeptObjectTablePane(
			ObjectTablePane<QiEntryDepartmentDto> currentlyAssignedEntryDeptObjectTablePane) {
		this.currentlyAssignedEntryDeptObjectTablePane = currentlyAssignedEntryDeptObjectTablePane;
	}

	public LoggedButton getRightShiftDepartmentBtn() {
		return rightShiftDepartmentBtn;
	}

	public void setRightShiftDepartmentBtn(LoggedButton rightShiftDepartmentBtn) {
		this.rightShiftDepartmentBtn = rightShiftDepartmentBtn;
	}

	public StationDepartmentController getController() {
		return controller;
	}

	public void setController(
			StationDepartmentController controller) {
		this.controller = controller;
	}

	public LoggedButton getLeftShiftDepartmentBtn() {
		return leftShiftDepartmentBtn;
	}

	public void setLeftShiftDepartmentBtn(LoggedButton leftShiftDepartmentBtn) {
		this.leftShiftDepartmentBtn = leftShiftDepartmentBtn;
	}

	public LoggedButton getRightShiftWriteUpDepartmentBtn() {
		return rightShiftWriteUpDepartmentBtn;
	}

	public void setRightShiftWriteUpDepartmentBtn(
			LoggedButton rightShiftWriteUpDepartmentBtn) {
		this.rightShiftWriteUpDepartmentBtn = rightShiftWriteUpDepartmentBtn;
	}

	public LoggedButton getLeftShiftWriteUpDepartmentBtn() {
		return leftShiftWriteUpDepartmentBtn;
	}

	public void setLeftShiftWriteUpDepartmentBtn(
			LoggedButton leftShiftWriteUpDepartmentBtn) {
		this.leftShiftWriteUpDepartmentBtn = leftShiftWriteUpDepartmentBtn;
	}

	public LoggedButton getUpdateWriteUpDepartmentBtn() {
		return updateWriteUpDepartmentBtn;
	}

	public void setUpdateWriteUpDepartmentBtn(
			LoggedButton updateWriteUpDepartmentBtn) {
		this.updateWriteUpDepartmentBtn = updateWriteUpDepartmentBtn;
	}

	public LoggedButton getUpdateEntryDepartmentBtn() {
		return updateEntryDepartmentBtn;
	}

	public void setUpdateEntryDepartmentBtn(LoggedButton updateEntryDepartmentBtn) {
		this.updateEntryDepartmentBtn = updateEntryDepartmentBtn;
	}

	public LoggedButton getResetEntryDepartmentBtn() {
		return resetEntryDepartmentBtn;
	}

	public void setResetEntryDepartmentBtn(LoggedButton resetEntryDepartmentBtn) {
		this.resetEntryDepartmentBtn = resetEntryDepartmentBtn;
	}

	public ObjectTablePane<QiEntryDepartmentDto> getAvailableEntryDepartObjectTablePane() {
		return availableEntryDepartObjectTablePane;
	}

	public void setAvailableEntryDepartObjectTablePane(
			ObjectTablePane<QiEntryDepartmentDto> availableEntryDepartObjectTablePane) {
		this.availableEntryDepartObjectTablePane = availableEntryDepartObjectTablePane;
	}

	public LoggedButton getResetWriteUpDepartmentBtn() {
		return resetWriteUpDepartmentBtn;
	}

	public ObjectTablePane<QiWriteUpDepartmentDto> getCurrentlyAssignedWriteUpDepartObjectTablePane() {
		return currentlyAssignedWriteUpDepartObjectTablePane;
	}

	public ObjectTablePane<QiWriteUpDepartmentDto> getAvailableWriteUpDepartObjectTablePane() {
		return availableWriteUpDepartObjectTablePane;
	}
}
