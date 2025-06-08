package com.honda.galc.client.teamleader.qi.stationconfig.department;


import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.util.Callback;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigModel;
import com.honda.galc.client.teamleader.qi.view.QiFxDialog;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiEntryStationSettingsDto;
import com.honda.galc.dto.qi.QiWriteUpDepartmentDto;
import com.honda.galc.entity.enumtype.QiWriteUpDepartmentColor;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>WriteUpDeptDialog</code> is the Dialog Panel class for displaying dialog with WriteupDept values
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
public class WriteUpDeptDialog extends QiFxDialog<EntryStationConfigModel>{

	private MigPane pane;
	private boolean isFullAccess,flag;
	private WriteUpDeptController controller;
	protected ObjectTablePane<QiEntryStationSettingsDto> entryStationConfigSettingsTablePane;
	private LoggedComboBox<String> updateSettingValueComboBox;
	private LoggedButton updateSettingsPropertyBtn;
	private LoggedButton doneBtn;
	private LoggedButton cancelBtn;
	private ObjectTablePane<QiWriteUpDepartmentDto> availableWriteUpDepartObjectTablePane;
	private List<QiWriteUpDepartmentDto> writeUpDepartments;
	private List<QiWriteUpDepartmentDto> assignedWriteUpDepartments;
	private ToggleGroup group;
	private List<LoggedComboBox<String>> colorComboBoxes; 
	private List<LoggedRadioButton> radioButtons;
	
	public WriteUpDeptDialog(String title,boolean isFullAccess,EntryStationConfigModel model, List<QiWriteUpDepartmentDto> writeUpDepartments,String applicationId) {
		super(title, applicationId, model);
		this.isFullAccess=isFullAccess;
		this.writeUpDepartments=writeUpDepartments;
	    flag=false;
		this.controller = new WriteUpDeptController(model, this);
		initComponents();
	    this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
	}

	public boolean isFullAccess() {
		return isFullAccess;
	}

	public MigPane getPane() {
		return pane;
	}

	public List<LoggedRadioButton> getRadioButtons() {
		return radioButtons;
	}

	public void setRadioButtons(List<LoggedRadioButton> radioButtons) {
		this.radioButtons = radioButtons;
	}

	public List<LoggedComboBox<String>> getColorComboBoxes() {
		return colorComboBoxes;
	}


	public LoggedButton getSaveAllSettingBtn() {
		return doneBtn;
	}

	public WriteUpDeptController getController() {
		return controller;
	}


	public ObjectTablePane<QiEntryStationSettingsDto> getEntryStationConfigSettingsTablePane() {
		return entryStationConfigSettingsTablePane;
	}

	public void setEntryStationConfigSettingsTablePane(
			ObjectTablePane<QiEntryStationSettingsDto> entryStationConfigSettingsTablePane) {
		this.entryStationConfigSettingsTablePane = entryStationConfigSettingsTablePane;
	}

	public LoggedComboBox<String> getUpdateSettingValueComboBox() {
		return updateSettingValueComboBox;
	}

	public void setUpdateSettingValueComboBox(LoggedComboBox<String> updateSettingValueComboBox) {
		this.updateSettingValueComboBox = updateSettingValueComboBox;
	}

	public LoggedButton getUpdateSettingsPropertyBtn() {
		return updateSettingsPropertyBtn;
	}

	public void setUpdateSettingsPropertyBtn(LoggedButton updateSettingsPropertyBtn) {
		this.updateSettingsPropertyBtn = updateSettingsPropertyBtn;
	}
	public ObjectTablePane<QiWriteUpDepartmentDto> getAvailableWriteUpDepartObjectTablePane() {
		return availableWriteUpDepartObjectTablePane;
	}

	public List<QiWriteUpDepartmentDto> getAssignedWriteUpDepartments() {
		return assignedWriteUpDepartments;
	}

	public void setAssignedWriteUpDepartments(List<QiWriteUpDepartmentDto> assignedWriteUpDepartments) {
		this.assignedWriteUpDepartments = assignedWriteUpDepartments;
	}

	
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	private void initComponents() {
		HBox outerPane = new HBox();
		outerPane.setPrefSize(Screen.getPrimary().getVisualBounds().getWidth()*0.4, Screen.getPrimary().getVisualBounds().getHeight()*0.3);
		outerPane.getChildren().add(getWriteUpDepartmentsPanel());
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);
	}
	/**
	 * This method is used to create writeup department table .
	 */	
	private Node getWriteUpDepartmentsPanel() {
		VBox vBox = new VBox();
		availableWriteUpDepartObjectTablePane= createWriteUpDepartmentTablePane();
		availableWriteUpDepartObjectTablePane.setData(writeUpDepartments);
		availableWriteUpDepartObjectTablePane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth()*0.38);
		vBox.getChildren().addAll(availableWriteUpDepartObjectTablePane,getButtons());
		return vBox;
    }
	/**
	 * This method is used to create buttons .
	 */	
	private Node getButtons() {
		HBox button = new HBox();
		doneBtn = createBtn("Done",getController());
		doneBtn.setAlignment(Pos.CENTER);
		cancelBtn = createBtn(QiConstant.CANCEL,getController());
		cancelBtn.setAlignment(Pos.CENTER);
		button.getChildren().addAll(doneBtn,cancelBtn);
		button.setAlignment(Pos.CENTER);
		return button;
	}

	public String getScreenName() {
		return "EntryStationConfigManagement";
	}
	/**
	 * This method is used to create writeup department tablepane .
	 */	
    private ObjectTablePane<QiWriteUpDepartmentDto> createWriteUpDepartmentTablePane(){ 
		ColumnMappingList columnMappingList=null;
		Double[] columnWidth=null;
		
			columnMappingList = ColumnMappingList.with("WriteUp Dept", "divisionId");
			columnWidth = new Double[] {0.252};
	
		ObjectTablePane<QiWriteUpDepartmentDto> panel = new ObjectTablePane<QiWriteUpDepartmentDto>(columnMappingList,columnWidth);
		LoggedTableColumn<QiWriteUpDepartmentDto,Boolean> defaultImageCol = new LoggedTableColumn<QiWriteUpDepartmentDto,Boolean>();
		
		createDefaultImage(defaultImageCol);
		panel.getTable().getColumns().add(0, defaultImageCol);
		panel.getTable().getColumns().get(0).setText("Default");
		panel.getTable().getColumns().get(0).setResizable(true);
		panel.getTable().getColumns().get(0).setMaxWidth(70);
		panel.getTable().getColumns().get(0).setMinWidth(70);
		LoggedTableColumn<QiWriteUpDepartmentDto, String> defaultColorCol = new LoggedTableColumn<QiWriteUpDepartmentDto, String>();
		createDefaultColor(defaultColorCol);
	
        panel.getTable().getColumns().add(2, defaultColorCol);
		panel.getTable().getColumns().get(2).setText("Ink");
		panel.getTable().getColumns().get(2).setResizable(true);
		panel.getTable().getColumns().get(2).setMaxWidth(100);
		panel.getTable().getColumns().get(2).setMinWidth(100);

		return panel;
	}
    private void createDefaultColor(LoggedTableColumn defaultColorCol) {
    	colorComboBoxes= new ArrayList<LoggedComboBox<String>>();
    	defaultColorCol.setCellFactory( new Callback<LoggedTableColumn<QiWriteUpDepartmentDto, String>, LoggedTableCell<QiWriteUpDepartmentDto,String>>()
		{ public LoggedTableCell<QiWriteUpDepartmentDto,String> call(LoggedTableColumn<QiWriteUpDepartmentDto,String> p)
			{	
				return new LoggedTableCell<QiWriteUpDepartmentDto,String>()
				{
					@Override
					public void updateItem( String item, boolean empty )
					{
						super.updateItem( item, empty ); 
						if(empty){
							 setText(null);
	                         setGraphic(null);	
						}else
						
						{
						  HBox hb = new HBox();
				          hb.setAlignment(Pos.CENTER);
				          LoggedComboBox<String> comboBox = new LoggedComboBox<String>();
				          comboBox.setId(getIndex()+StringUtils.EMPTY);
				          comboBox.setOnAction(getController());
				          comboBox.getItems().addAll(getColorValues());
				          comboBox.setValue(getColorValues().first());
				          colorComboBoxes.add(comboBox);
				          hb.getChildren().add(comboBox);
				          setGraphic(hb);
				        }
					}
				  };
			     }
				});
		
	}
	/**
	 * This method is used to create default image column.
	 */	
	private void createDefaultImage(LoggedTableColumn rowIndex){
		    group= new ToggleGroup();
            radioButtons= new ArrayList<LoggedRadioButton>();
    		rowIndex.setCellFactory( new Callback<LoggedTableColumn<QiWriteUpDepartmentDto, Boolean>, LoggedTableCell<QiWriteUpDepartmentDto,Boolean>>()
    		{ public LoggedTableCell<QiWriteUpDepartmentDto,Boolean> call(LoggedTableColumn<QiWriteUpDepartmentDto,Boolean> p)
    			{	
    				return new LoggedTableCell<QiWriteUpDepartmentDto,Boolean>()
    				{
    					@Override
    					public void updateItem( Boolean item, boolean empty )
    					{
    						super.updateItem( item, empty ); 
    						if(empty){
    							 setText(null);
    	                         setGraphic(null);	
    						}else
    						
    						{
    						  HBox hb = new HBox();
    				          hb.setAlignment(Pos.CENTER);
    				        
    				          LoggedRadioButton button = createRadioButton(StringUtils.EMPTY, group, false, getController());
    				          button.setToggleGroup(group);
    				          button.setId(getIndex()+StringUtils.EMPTY);
    				          radioButtons.add(button);
    				          hb.getChildren().add(button);
    				          setGraphic(hb);  
    						}
    					}
    				   };
    			     }
    				});
    	}
	/**
	 * This method is used to get the colr values for combobox .
	 */	
	private TreeSet<String> getColorValues() {
		TreeSet<String> colors= new TreeSet<String>();
		for(QiWriteUpDepartmentColor color : QiWriteUpDepartmentColor.values()){
			colors.add(color.getName());	
		}
		return colors;
	}
}
