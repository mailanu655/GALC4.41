package com.honda.galc.client.teamleader.qi.stationconfig.previousdefect;


import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigModel;
import com.honda.galc.client.teamleader.qi.view.QiFxDialog;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiEntryDepartmentDto;
import com.honda.galc.entity.enumtype.DefectStatus;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.util.Callback;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PreviousDefectVisibleDialog</code> is the Dialog Panel class for displaying dialog with EntryStationPreviousDefect values
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * </TR>
 * <TR>
 * <TD>L&T Infotech</TD>

 * </TABLE>
 */
public class PreviousDefectVisibleDialog extends QiFxDialog<EntryStationConfigModel>{

	private MigPane pane;
	private boolean isFullAccess;
	private PreviousDefectVisibleController controller;
	private LoggedButton updateSettingsPropertyBtn;
	private LoggedButton doneBtn;
	private LoggedButton cancelBtn;
	private ObjectTablePane<QiEntryDepartmentDto> availableEntryDepartmentDefectObjectTablePane;
	private List<QiEntryDepartmentDto> entryDepartments;
	private List<QiEntryDepartmentDto> assignedEntryDepartments;
	private List<LoggedComboBox<String>> currentDefectComboBoxes;
	private List<LoggedComboBox<String>> originalDefectComboBoxes; 

	public PreviousDefectVisibleDialog(String title,boolean isFullAccess,EntryStationConfigModel model, List<QiEntryDepartmentDto> entryDepartments,String applicationId) {
		super(title, applicationId, model);
		this.isFullAccess=isFullAccess;
		this.entryDepartments=entryDepartments;
		this.controller = new PreviousDefectVisibleController(model, this);
		initComponents();
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
	}

	public boolean isFullAccess() {
		return isFullAccess;
	}

	public MigPane getPane() {
		return pane;
	}

	public List<LoggedComboBox<String>> getCurrentDefectComboBoxes() {
		return currentDefectComboBoxes;
	}
	
	public List<LoggedComboBox<String>> getOriginalDefectComboBoxes() {
		return originalDefectComboBoxes;
	}

	public LoggedButton getSaveAllSettingBtn() {
		return doneBtn;
	}

	public PreviousDefectVisibleController getController() {
		return controller;
	}

	public LoggedButton getUpdateSettingsPropertyBtn() {
		return updateSettingsPropertyBtn;
	}

	public void setUpdateSettingsPropertyBtn(LoggedButton updateSettingsPropertyBtn) {
		this.updateSettingsPropertyBtn = updateSettingsPropertyBtn;
	}
	public ObjectTablePane<QiEntryDepartmentDto> getAvailableEntryDepartObjectTablePane() {
		return availableEntryDepartmentDefectObjectTablePane;
	}

	public List<QiEntryDepartmentDto> getAssignedEntryDepartments() {
		return assignedEntryDepartments;
	}

	public void setAssignedEntryDepartments(List<QiEntryDepartmentDto> assignedEntryDepartments) {
		this.assignedEntryDepartments = assignedEntryDepartments;
	}

	private void initComponents() {
		MigPane pane = new MigPane("insets 10 5 0 5", "[center]", "[]10[]");
		pane.add(getEntryDepartmentsPanel(),"cell 0 0");
		pane.add(getDoneButton(),"cell 0 1,split 2");
		pane.add(getCancelButton());
		pane.setPrefSize(Screen.getPrimary().getVisualBounds().getWidth()*0.37, Screen.getPrimary().getVisualBounds().getHeight()*0.3);
		((BorderPane) this.getScene().getRoot()).setCenter(pane);
	}
	/**
	 * This method is used to create entry department table .
	 */	
	private ObjectTablePane<QiEntryDepartmentDto> getEntryDepartmentsPanel() {
		availableEntryDepartmentDefectObjectTablePane= createEntryDepartmentDefectTablePane();
		availableEntryDepartmentDefectObjectTablePane.setData(entryDepartments);
		availableEntryDepartmentDefectObjectTablePane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth()*0.38);
		return availableEntryDepartmentDefectObjectTablePane;
	}
	/**
	 * This method is used to create done button .
	 */	
	private LoggedButton getDoneButton() {
		doneBtn = createBtn(QiConstant.DONE,getController());
		doneBtn.setAlignment(Pos.CENTER);
		return doneBtn;
	}
	/**
	 * This method is used to create  button.
	 */	
	private LoggedButton getCancelButton() {
		cancelBtn = createBtn(QiConstant.CANCEL,getController());
		cancelBtn.setAlignment(Pos.CENTER);
		return cancelBtn;
	}

	public String getScreenName() {
		return "EntryStationConfigManagement";
	}
	
	/**
	 * This method is used to create EntryStationPreviousDefect tablepane .
	 */	
	private ObjectTablePane<QiEntryDepartmentDto> createEntryDepartmentDefectTablePane(){ 
		ColumnMappingList columnMappingList=null;
		Double[] columnWidth=null;

		columnMappingList = ColumnMappingList.with("Entry Dept", "divisionId");
		columnWidth = new Double[] {0.17};

		ObjectTablePane<QiEntryDepartmentDto> panel = new ObjectTablePane<QiEntryDepartmentDto>(columnMappingList,columnWidth);
		LoggedTableColumn<QiEntryDepartmentDto, String> tableColumnOriginalDefectStatus = new LoggedTableColumn<QiEntryDepartmentDto, String>();
		LoggedTableColumn<QiEntryDepartmentDto, String> tableColumnCurrentDefectStatus = new LoggedTableColumn<QiEntryDepartmentDto, String>();
		createTableColumnOriginalDefectStatus(tableColumnOriginalDefectStatus);
		createTableColumnCurrentDefectStatus(tableColumnCurrentDefectStatus);

		panel.getTable().getColumns().add(1, tableColumnOriginalDefectStatus);
		panel.getTable().getColumns().get(1).setText("Original Defect Status");
		panel.getTable().getColumns().get(1).setResizable(true);
		panel.getTable().getColumns().get(1).setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth()*0.1);
		panel.getTable().getColumns().get(1).setMinWidth(Screen.getPrimary().getVisualBounds().getWidth()*0.1);
		
		panel.getTable().getColumns().add(2, tableColumnCurrentDefectStatus);
		panel.getTable().getColumns().get(2).setText("Current Defect Status");
		panel.getTable().getColumns().get(2).setResizable(true);
		panel.getTable().getColumns().get(2).setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth()*0.1);
		panel.getTable().getColumns().get(2).setMinWidth(Screen.getPrimary().getVisualBounds().getWidth()*0.1);

		return panel;
	}
	
	private void createTableColumnOriginalDefectStatus(LoggedTableColumn tableColumnDefectStatus) {
		originalDefectComboBoxes = new ArrayList<LoggedComboBox<String>>();
		tableColumnDefectStatus.setCellFactory(new Callback<LoggedTableColumn<QiEntryDepartmentDto, String>, LoggedTableCell<QiEntryDepartmentDto,String>>() { 
			public LoggedTableCell<QiEntryDepartmentDto,String> call(LoggedTableColumn<QiEntryDepartmentDto,String> p) {	
				return new LoggedTableCell<QiEntryDepartmentDto,String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty); 
						if (empty) {
							setText(null);
							setGraphic(null);	
						} else {
							HBox hb = new HBox();
							hb.setAlignment(Pos.CENTER);
							LoggedComboBox<String> comboBox = new LoggedComboBox<String>();
							comboBox.setId(getIndex()+StringUtils.EMPTY);
							comboBox.setOnAction(getController());
							comboBox.getItems().addAll(getOriginalDefectStatusValues());
							comboBox.setValue(getOriginalDefectStatusValues().first());
							originalDefectComboBoxes.add(comboBox);
							hb.getChildren().add(comboBox);
							setGraphic(hb);
						}
					}
				};
			}
		});
	}
	
	private void createTableColumnCurrentDefectStatus(LoggedTableColumn tableColumnDefectStatus) {
		currentDefectComboBoxes = new ArrayList<LoggedComboBox<String>>();
		tableColumnDefectStatus.setCellFactory(new Callback<LoggedTableColumn<QiEntryDepartmentDto, String>, LoggedTableCell<QiEntryDepartmentDto,String>>() { 
			public LoggedTableCell<QiEntryDepartmentDto,String> call(LoggedTableColumn<QiEntryDepartmentDto,String> p) {	
				return new LoggedTableCell<QiEntryDepartmentDto,String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty); 
						if (empty) {
							setText(null);
							setGraphic(null);	
						} else {
							HBox hb = new HBox();
							hb.setAlignment(Pos.CENTER);
							LoggedComboBox<String> comboBox = new LoggedComboBox<String>();
							comboBox.setId(getIndex()+StringUtils.EMPTY);
							comboBox.setOnAction(getController());
							comboBox.getItems().addAll(getCurrentDefectStatusValues());
							comboBox.setValue(getCurrentDefectStatusValues().first());
							currentDefectComboBoxes.add(comboBox);
							hb.getChildren().add(comboBox);
							setGraphic(hb);
						}
					}
				};
			}
		});
	}

	/**
	 * This method is used to get the defect values for combobox .
	 */	
	private TreeSet<String> getOriginalDefectStatusValues() {
		TreeSet<String> defects= new TreeSet<String>();
		defects.add(DefectStatus.ALL.getName());
		defects.add(DefectStatus.NOT_REPAIRED.getName());
		defects.add(DefectStatus.REPAIRED.getName());
		return defects;
	}
	
	private TreeSet<String> getCurrentDefectStatusValues() {
		TreeSet<String> defects= new TreeSet<String>();
		defects.add(DefectStatus.ALL.getName());
		defects.add(DefectStatus.NOT_FIXED.getName());
		defects.add(DefectStatus.FIXED.getName());
		return defects;
	}
}
