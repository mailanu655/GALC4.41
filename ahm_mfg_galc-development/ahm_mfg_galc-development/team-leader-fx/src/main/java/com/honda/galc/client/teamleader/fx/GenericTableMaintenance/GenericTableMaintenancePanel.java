package com.honda.galc.client.teamleader.fx.GenericTableMaintenance;

import static com.honda.galc.service.ServiceFactory.getService;


import java.awt.event.KeyEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicLong;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.persistence.Column;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.FXOptionPane;
import com.honda.galc.client.ui.component.LoggedTextFieldTableCell;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.property.GenericTableMaintenancePropertyBean;
import com.honda.galc.service.GenericDaoService;
import com.honda.galc.service.IDaoService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonUtil;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


/*   
 * @author Gangadhararao Gadde , Subu kathiresan, Raj Salethaiyan
 * 
 *
 *
 */


public  class GenericTableMaintenancePanel extends TabbedPanel implements Observer,EventHandler<javafx.event.ActionEvent>
{

	private static final long serialVersionUID = -1L;	

	protected List<AuditEntry> displayEntityList = new ArrayList<AuditEntry>();
	protected List<AuditEntry> originalEntityList= new ArrayList<AuditEntry>();
	protected HashMap<Integer,AuditEntry> oldValuesEntityMap= new HashMap<Integer,AuditEntry>();
	protected HashMap<Integer, AuditEntry> updatedEntityMap = new HashMap<Integer, AuditEntry>();
	protected List<AuditEntry> deleteEntityList =  new ArrayList<AuditEntry>();
	protected ObservableList<Integer> updatedTableRowList=  FXCollections.observableArrayList();
	protected CommonFilterDialog filter = null;
	protected ImageView filterImageView = null;	
	protected ImageView colorLegendImageView = null;	
	protected Button clearBtn = null;	
	protected Button hideBtn = null;	
	protected Button deleteBtn = null;	
	protected Button saveBtn = null;	
	protected Button cancelBtn = null;
	protected Button massUpdateBtn=null;
	protected GridPane buttonPanel = null;
	protected GridPane changeDescPanel = null;
	protected GridPane massUpdatePanel = null;
	protected GridPane filterLabelPanel = null;
	protected Label filterLabel=null;
	protected GridPane tablePanel = null;
	protected GridPane tabbedPanel = null;
	protected TabPane tabbedPane = null;
	protected Pane mainPane = null;
	protected EditTable parentDisplayTable = null;
	protected GenericTableMaintConfiguration cfg = new GenericTableMaintConfiguration();
	protected TextField changeTxtField=null;
	protected TextField massUpdateTxtField=null;
	protected Label massUpdateLabel=null;
	protected TextArea changeDescTxtArea=null;
	protected Label  changeNumberLabel;
	protected Label  changeDescLabel;
	protected GenericTableMaintenancePropertyBean genericTableMaintenancePropertyBean=null;
	protected final TableViewStyleChangeRowFactory rowFactory = new TableViewStyleChangeRowFactory("updatedGenericTableRow","deletedGenericTableRow");
	protected Map<String, Integer> entityColumnsLengthMap = new HashMap<String, Integer>();
	protected Map<String, Integer> historyEntityColumnsLengthMap = new HashMap<String, Integer>();
	protected final static String CHANGE_NUMBER="CHANGE_NUMBER";
	protected final static String CHANGE_DESCRIPTION="CHANGE_DESCRIPTION";
	protected final static String ORIGINAL_RECORD="(Original Record)";
	
	
	
	public GenericTableMaintenancePanel(TabbedMainWindow mainWindow) {
		super("Generic Table Maintenance Panel", KeyEvent.VK_G,mainWindow);	
		initView();	
	}
	
	public String getSelectPrefix(String entityAlias) {
		return "select " + entityAlias.toLowerCase() + " from " + entityAlias + " as " + entityAlias.toLowerCase() + " ";
	}
	

	public void initializeData() {		
		getTableData();
		refreshViewTable();
	}

	protected void getTableData() {
		try {
			ObservableList<AuditEntry> list=populateTable(displayEntityList);		
			getViewTable().setItems(list);
			refreshViewTable();
			
		}catch (Exception e) {
			e.printStackTrace();
			getLogger().error("An error occurred the Generic Table Maintenance Panel"+e.getMessage());
		}
	}

	public void refreshViewTable()
	{
		((TableColumn)getViewTable().getColumns().get(0)).setVisible(false);
		((TableColumn)getViewTable().getColumns().get(0)).setVisible(true);
	}

	public void initView(){
		try{
			configureColumns();
			setCenter(getMainPanel());		
			initConnections();
			setVisible(true);
		}catch(Exception e){
			e.printStackTrace();
			getLogger().error("An error occurred the Generic Table Maintenance Panel"+e.getMessage());
		}
	}	

	protected Pane getMainPanel() {
		if (mainPane == null) {
			try {
				mainPane = new VBox(40);
				mainPane.setId("mainPane");			
				mainPane.getChildren().add(getFilterPanel());
				mainPane.getChildren().add(getTablePanel());
				genericTableMaintenancePropertyBean = PropertyService.getPropertyBean(GenericTableMaintenancePropertyBean.class, getApplicationId());
				getEntityColumnLengthData();
				if(getGenericTableMaintenancePropertyBean().isMassUpdateEnabled())
				   mainPane.getChildren().add(getMassUpdatePanel());
				if(getGenericTableMaintenancePropertyBean().isHistoryEntitySaveEnabled())
			    	mainPane.getChildren().add(getChangeDescPanel());	
				mainPane.getChildren().add(getButtonPanel());
			} catch (Exception e) {
				e.printStackTrace();
				getLogger().error("An error occurred the Generic Table Maintenance Panel"+e.getMessage());
			}
		}
		return mainPane;
	}

	protected GridPane getButtonPanel(){
		if(buttonPanel == null){
			buttonPanel = new GridPane();
			buttonPanel.setHgap(100);			
			buttonPanel.add(getJButtonClear(),1,0);		
			buttonPanel.add(getJButtonHide(),2,0);
			buttonPanel.add(getJButtonDelete(),3,0);
			buttonPanel.add(getJButtonSave(),4,0);
			buttonPanel.add(getJButtonCancel(),5,0);
			if(getGenericTableMaintenancePropertyBean().isMassUpdateEnabled())
			   buttonPanel.add(getJButtonMassUpdate(),6,0);
			buttonPanel.setPadding(new Insets(20,0,0,20));
		}
		return buttonPanel;
	}

	protected GridPane getChangeDescPanel(){
		if(changeDescPanel == null){
			changeDescPanel = new GridPane();
			changeDescPanel.setHgap(10);	
			changeDescPanel.setVgap(10);
			changeDescPanel.add(getChangeNumberLabel(), 1, 0);
			changeDescPanel.add(getChangeTxtField(),2,0);	
			changeDescPanel.add(getChangeDescLabel(), 1, 1);
			changeDescPanel.add(getChangeDescTxtArea(),2,1);
		}
		return changeDescPanel;
	}
	
	protected GridPane getMassUpdatePanel(){
		if(massUpdatePanel == null){
			massUpdatePanel = new GridPane();
			massUpdatePanel.setHgap(20);	
			massUpdatePanel.setVgap(20);
			massUpdatePanel.add(getMassUpdateLabel(), 1, 0);
			massUpdatePanel.add(getMassUpdateTxtField(), 2, 0);
			
		}
		return massUpdatePanel;
	}
	
	private Label getMassUpdateLabel() {
		if(massUpdateLabel==null)
		{
			massUpdateLabel = UiFactory.getInfo().createLabel("massUpdateLabel", "Mass Update");
			massUpdateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 25));
		}
		return massUpdateLabel;
	}
	
	private TextField getMassUpdateTxtField() {
		if(massUpdateTxtField==null)		
		{
			 massUpdateTxtField=UiFactory.createTextField("massUpdateTxtField", 50, UiFactory.getIdle().getInputFont(), TextFieldState.EDIT); 
			 massUpdateTxtField.setMinWidth(50);
		}
		return massUpdateTxtField;
	}
	
	private TextArea getChangeDescTxtArea() {
		if(changeDescTxtArea==null)
		{
			changeDescTxtArea=UiFactory.createTextArea();
			changeDescTxtArea.setEditable(true);
			changeDescTxtArea.setStyle(UiFactory.getIdle().getInputFont());
		}
		return changeDescTxtArea;
	}

	private TextField getChangeTxtField() {
		if(changeTxtField==null)		
		{
			changeTxtField = UiFactory.createTextField("changeTxtField", 20, UiFactory.getIdle().getInputFont(), TextFieldState.EDIT); 
			changeTxtField.setMaxWidth(400);
		}
		return changeTxtField;
	}

	private Label getChangeDescLabel() {
		if(changeDescLabel==null)
		{
			changeDescLabel = UiFactory.getInfo().createLabel("changeDescLabel", "Change Description:");
			changeDescLabel.setFont(Font.font("Arial", FontWeight.BOLD, 25));
		}
		return changeDescLabel;
	}

	private Label getChangeNumberLabel() {
		if(changeNumberLabel==null)
		{
			changeNumberLabel = UiFactory.getInfo().createLabel("changeNumberLabel", "Change Request Number:");
			changeNumberLabel.setFont(Font.font("Arial", FontWeight.BOLD, 25));
		}
		return changeNumberLabel;
	}

	protected GridPane getFilterPanel(){
		if(filterLabelPanel == null){
			filterLabelPanel = new GridPane();
			filterLabelPanel.add(getFilterLabel(),0,0);
			filterLabelPanel.add(getFilterImageView(),1,0);
			filterLabelPanel.add(getColorLegendImageView(),40,0);
			filterLabelPanel.setHgap(10);
			filterLabelPanel.setPadding(new Insets(20,0,0,20));
		}
		return filterLabelPanel;
	}

	protected Label getFilterLabel()
	{
		if(filterLabel==null)
		{
			filterLabel =UiFactory.createLabel("filterLabel", "Filter");
			filterLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
			filterLabel.setPrefWidth(50);
		}
		return filterLabel;
	}

	protected GridPane getTablePanel(){
		if(tablePanel == null){
			tablePanel = new GridPane();
			tablePanel.add(getViewTable(),0,0);		
			ColumnConstraints column = new ColumnConstraints();
			column.setPercentWidth(95);
			tablePanel.getColumnConstraints().addAll(column);			   
			RowConstraints row = new RowConstraints();
			row.setPercentHeight(100);
			tablePanel.getRowConstraints().addAll(row);
		}
		return tablePanel;
	}

	public void autoColumnResize(TableView<?> tableView) {
		AtomicLong remainingColumnsWidth = new AtomicLong();
		AtomicLong imageColumnWidth=new AtomicLong();
		tableView.getColumns().forEach(column -> {
			if(!((MaintenanceTableColumn)column).getEntityColumnName().isEmpty())
				remainingColumnsWidth.addAndGet((long) column.getWidth());
			else
				imageColumnWidth.addAndGet((long) column.getWidth());
		});
		double tableViewWidth = tableView.getWidth()-imageColumnWidth.get();
		if (tableViewWidth > remainingColumnsWidth.get()) {
			tableView.getColumns().forEach(column -> {
				if(!((MaintenanceTableColumn)column).getEntityColumnName().isEmpty())
					column.setPrefWidth(column.getWidth()+((tableViewWidth-remainingColumnsWidth.get())/(tableView.getColumns().size()-1)));
			});
		}
	}

	private EditTable getViewTable() {
		if (parentDisplayTable == null) {
			try {
				parentDisplayTable = new EditTable();
				parentDisplayTable.setId("ParentTable");
				Iterator itr = cfg.getDisplayColumns().iterator();
				MaintenanceTableColumn mtc;
				while(itr.hasNext()){					
					mtc = (MaintenanceTableColumn)itr.next();
					parentDisplayTable.getColumns().add(mtc);	
				}
				parentDisplayTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				parentDisplayTable.setEditable(true);
				Platform.runLater(() -> autoColumnResize(parentDisplayTable));
				parentDisplayTable.getSelectionModel().setCellSelectionEnabled(true);
				getStylesheets().add(getClass().getResource("/resource/css/TableViewRowHighlighting.css").toExternalForm());				
				parentDisplayTable.setRowFactory(rowFactory);				
				parentDisplayTable.widthProperty().addListener(new ChangeListener<Number>(){
					@Override
					public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth)
					{
						final TableHeaderRow header = (TableHeaderRow) parentDisplayTable.lookup("TableHeaderRow");
						header.reorderingProperty().addListener(new ChangeListener<Boolean>() {
							@Override
							public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
								header.setReordering(false);
							}
						});
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
				getLogger().error("An error occurred the Generic Table Maintenance Panel"+e.getMessage());
			}
		}
		return parentDisplayTable;
	}

	protected MaintenanceTableColumn getTableColumn(String dispName) {
		MaintenanceTableColumn tableColumn = new MaintenanceTableColumn();			
		try {			
			tableColumn.setText(dispName);
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error("An error occurred the Generic Table Maintenance Panel"+e.getMessage());
		}

		return tableColumn;
	}

	protected ImageView getFilterImageView() {
		if (filterImageView == null) {
			String filterImagePath = "/resource/com/honda/galc/client/images/filter.png";
			Image filterImage = new Image(filterImagePath);
			filterImageView = new ImageView(filterImage);
			filterImageView.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent arg0) {
					invokeCommonFilter();
				}
			});
		}
		return filterImageView;
	}

	protected Button getJButtonClear() {
		if (clearBtn == null) {			
				clearBtn = UiFactory.getInfo().createButton("CLEAR", true);						
		}
		return clearBtn;
	}

	protected Button getJButtonHide() {
		if (hideBtn == null) {			
				hideBtn =UiFactory.getInfo().createButton("HIDE", true);								
		}
		return hideBtn;
	}

	protected Button getJButtonDelete() {
		if (deleteBtn == null) {
				deleteBtn =UiFactory.getInfo().createButton("DELETE", true);						
		}
		return deleteBtn;
	}

	protected Button getJButtonSave() {
		if (saveBtn == null) {
				saveBtn = UiFactory.getInfo().createButton("SAVE", false);
		}
		return saveBtn;
	}

	protected Button getJButtonCancel() {
		if (cancelBtn == null) {
				cancelBtn = UiFactory.getInfo().createButton("CANCEL", true);			
		}
		return cancelBtn;
	}
	
	protected Button getJButtonMassUpdate() {
		if (massUpdateBtn == null) {
			massUpdateBtn = UiFactory.getInfo().createButton("MASS UPDATE", true);			
		}
		return massUpdateBtn;
	}
	
	public void clearData()
	{
		getViewTable().getItems().clear();
		clearParentTrackers();				 
		displayEntityList.clear();
		originalEntityList.clear();
		enableDisableSave();
		refreshViewTable();
		resetUpdatedRowsColor();
	}

	public void handle(ActionEvent e) {
		if (e.getSource().equals(getJButtonClear()))
		{
			resetUpdatedRowsColor();
			clearData();			 
		}
		else if (e.getSource().equals(getJButtonHide()))
		{
			int numRows = getViewTable().getSelectionModel().getSelectedIndices().size();
			if(numRows == 0)
				FXOptionPane.showMessageDialog(null, "No rows selected", "");
			else{				
				ObservableList selectedRows =  getViewTable().getSelectionModel().getSelectedItems();						
				for(Object selectedRow:selectedRows)
				{
					if(selectedRow!=null)
					{
						updatedEntityMap.remove(selectedRow);							
						deleteEntityList.remove(selectedRow);
						displayEntityList.remove(selectedRow);
					}
				}
				ObservableList<Integer> noOfRows = getViewTable().getSelectionModel().getSelectedIndices(); 				
				for(int rowNum:noOfRows)
				{			    
					getViewTable().getItems().remove(rowNum);
					enableDisableSave();
				}
			}
		}
		else if (e.getSource().equals(getJButtonDelete()))
		{
			enableDisableColumnSorting(false);
			rowFactory.getStyledDeletedRowIndicesList().addAll(getViewTable().getSelectionModel().getSelectedIndices());
			int numRows = getViewTable().getSelectionModel().getSelectedItems().size();
			if(numRows == 0)
				FXOptionPane.showMessageDialog(null, "No rows selected", "");
			else{
				ObservableList<Integer> selectedRows =  getViewTable().getSelectionModel().getSelectedIndices();					
				for(int rowNum:selectedRows) {												
					deleteEntityList.add((AuditEntry)getViewTable().getItems().get(rowNum));						
				}
			}
			refreshViewTable();
			enableDisableSave();
		}
		else if (e.getSource().equals(getJButtonSave()))
		{	
			if(getGenericTableMaintenancePropertyBean().isHistoryEntitySaveEnabled())
			{
				if(getChangeTxtField().getText().trim().length()==0)
				{
					FXOptionPane.showMessageDialog(null, " Please enter Change Request Number", "");
					return;
				}else if(getChangeDescTxtArea().getText().trim().length()==0)
				{
					FXOptionPane.showMessageDialog(null, " Please enter Change Description Information", "");
					return;
				}else if(getChangeTxtField().getText().trim().length()>historyEntityColumnsLengthMap.get(CHANGE_NUMBER))
				{
					FXOptionPane.showMessageDialog(null, "Change Request Number cannot exceed database column size of "+historyEntityColumnsLengthMap.get(CHANGE_NUMBER), "");
					return;
				}else if(getChangeDescTxtArea().getText().trim().length()>(historyEntityColumnsLengthMap.get(CHANGE_DESCRIPTION)-ORIGINAL_RECORD.length()))
				{	
					FXOptionPane.showMessageDialog(null, "Change Description Information cannot exceed size of "+(historyEntityColumnsLengthMap.get(CHANGE_DESCRIPTION)-ORIGINAL_RECORD.length()), "");
					return;
				}
			}			
			
			saveAndDelete();
			reloadTableData();
			initializeData();
							
		}
		else if (e.getSource().equals(getJButtonCancel()))
		{
			reloadTableData();
			refreshViewTable();
		}
		else if (e.getSource().equals(getJButtonMassUpdate()))
		{
			massUpdate();			
		}
		
	}

	private void enableDisableColumnSorting(boolean isSortable) {
		ObservableList<TableColumn> columns=getViewTable().getColumns();
		for(TableColumn column:columns)
		{
			column.setSortable(isSortable);
		}
	}

	private void reloadTableData() {
		resetUpdatedRowsColor();
		displayEntityList.clear();									
		for( AuditEntry currentEntity:originalEntityList ){
			AuditEntry currentEntityClone = (AuditEntry)currentEntity.deepCopy();
			displayEntityList.add(currentEntityClone);					 
		}
		clearParentTrackers();	
		initializeData();
		enableDisableSave();
		enableDisableColumnSorting(true);
	}

	private void resetUpdatedRowsColor() {
		rowFactory.getStyledDeletedRowIndicesList().clear();
		rowFactory.getStyledUpdatedRowIndicesList().clear();
		updatedTableRowList.clear();
	}

	protected void saveAndDelete(){
		try{			
			for(Object key : deleteEntityList){
				if(updatedEntityMap.containsValue(key))
					updatedEntityMap.remove(key);			
				originalEntityList.remove(key);
	
			}
			if(!updatedEntityMap.isEmpty()){
				List<AuditEntry> finalUpdateEntityList=new ArrayList<AuditEntry>();
				for (Integer key: updatedEntityMap.keySet()) 
				{					
					int k=-1;
					boolean isOriginalEntity=false;
					for(int m=0;m<originalEntityList.size();m++)
					{
						Method method = originalEntityList.get(m).getId().getClass().getDeclaredMethod("equals", Object.class);
						Boolean primaryKeyMatches =(Boolean)method.invoke(originalEntityList.get(m).getId(), updatedEntityMap.get(key).getId());
						if(primaryKeyMatches)
						{
							 k=m;
							 isOriginalEntity =	compareEntireEntity(updatedEntityMap.get(key), cfg.getDisplayColumns(), originalEntityList.get(m));							 
							 break;
						}
					}
					if(k==-1)
					{
						
						originalEntityList.add(updatedEntityMap.get(key));
						finalUpdateEntityList.add(updatedEntityMap.get(key));
						
					}else
					{			
						originalEntityList.set(k,updatedEntityMap.get(key));
						if(!isOriginalEntity)
						{
						   finalUpdateEntityList.add(updatedEntityMap.get(key));
						}else
						{
							oldValuesEntityMap.remove(key);
						}
					}
					
				}
				executeSaveGenericEntities(finalUpdateEntityList);
			}
			if(!deleteEntityList.isEmpty()){
				executeDeleteGenericEntities(deleteEntityList);
			}				
		}catch(Exception e){
			e.printStackTrace();
			getLogger().error("An exception occured. Please contact the Admin.");
		}
	}

	private void clearParentTrackers() {		
		updatedEntityMap.clear();
		deleteEntityList.clear();
		oldValuesEntityMap.clear();
		getChangeTxtField().setText(""); 
		getChangeDescTxtArea().setText("");
		if(getGenericTableMaintenancePropertyBean().isMassUpdateEnabled())
		   getMassUpdateTxtField().setText("");
		setErrorMessage("");
	}

	protected void enableDisableSave(){	
		if(updatedEntityMap.size() > 0 || deleteEntityList.size()>0)
		{
			getJButtonSave().setDisable(false);
			getJButtonHide().setDisable(true);
		}else
		{
			getJButtonSave().setDisable(true);
			getJButtonHide().setDisable(false);
		}		
		if( deleteEntityList.size()>0)
		{
			getJButtonDelete().setDisable(true);
		}else
		{
			getJButtonDelete().setDisable(false);
		}
	}

	protected void initConnections() throws java.lang.Exception {
		getJButtonClear().setOnAction(this);
		getJButtonHide().setOnAction(this);
		getJButtonDelete().setOnAction(this);
		getJButtonSave().setOnAction(this);
		getJButtonCancel().setOnAction(this);
		getJButtonMassUpdate().setOnAction(this);

	}

	protected class EditTable extends TableView {    

		public boolean isCellEditable(int row, int column) { 
			String displayName =( (TableColumn)(this.getColumns().get(column))).getId().toString();
			boolean isEditable = true;
			Iterator itr = null;			

			try{
				if(this.getId().equalsIgnoreCase("ParentTable")){
					itr = cfg.getDisplayColumns().iterator();
				}       		
				MaintenanceTableColumn mtc;
				while(itr.hasNext()){
					mtc = (MaintenanceTableColumn)itr.next() ; 
					if(mtc.getText().equalsIgnoreCase(displayName)){
						if(!mtc.isEditable())
							return false;  
						else
							return true;
					}
				}
			} 
			catch(Exception e){
				e.printStackTrace();
			}        	
			return isEditable;
		}

	}	


	protected String getCorrectEntityColName(String entityColName){
		String correctedEntityColName = null;
		try{
			if(entityColName!= null)
				correctedEntityColName = entityColName.substring(0, 1).toUpperCase()+entityColName.substring(1);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(correctedEntityColName == null)
			correctedEntityColName = "";
		return correctedEntityColName;
	}
	
	public Class<?> getPrimitiveTypeClass(String name)
	{
		if (name.equals("byte")) return byte.class;
		else if (name.equals("short")) return short.class;
		else if (name.equals("int")) return int.class;
		else if (name.equals("long")) return long.class;
		else if (name.equals("char")) return char.class;
		else if (name.equals("float")) return float.class;
		else if (name.equals("double")) return double.class;
		else if (name.equals("boolean")) return boolean.class;
		else if (name.equals("void")) return void.class;
		else return null;
	}



	protected HashMap updateMapEntity(String currvalue, int colIndex,AuditEntry entity, ArrayList<MaintenanceTableColumn> mtcList){
		HashMap<Boolean,AuditEntry> returnHash = new HashMap<Boolean,AuditEntry>();
		try{
			int colCount = 0;
			int allowedLength   = 0;		
			for(MaintenanceTableColumn mtc :mtcList){
				colCount++;
				if(colCount == colIndex){
					Class argTypes =null;
					if(mtc.getColumnDataType().equalsIgnoreCase(Timestamp.class.getSimpleName()))
						argTypes=Timestamp.class;
					else
					    argTypes = getPrimitiveTypeClass(mtc.getColumnDataType())==null?Class.forName(mtc.getColumnDataType()):getPrimitiveTypeClass(mtc.getColumnDataType());
					Class<?> c = entity.getClass();
					String entityColName = getCorrectEntityColName(mtc.getEntityColumnName().toString());
					Method targetMethod = c.getDeclaredMethod("set"+entityColName, argTypes);	
					if(argTypes.equals(Timestamp.class))
					{
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
					    dateFormat.setLenient(false);
					    java.util.Date date=null;
					    try {
					    	date= dateFormat.parse(currvalue.trim());
					    } catch (ParseException pe) {
					    	FXOptionPane.showMessageDialog(null, mtc.getText().toString().replace("*", "")+"  should be of yyyy-MM-dd HH:mm:ss.SSS format", "");
					    	returnHash.put(false, null);
							return returnHash;
					    }
					    targetMethod.invoke(entity, new Timestamp(date.getTime()));
						returnHash.put(true, entity);
						break;
					}
					else if(argTypes.equals(String.class))
					{
						Field[] fields = entity.getId().getClass().getDeclaredFields();			 		
						for(Field field :fields){
							if(field.getName().equals(mtc.getEntityColumnName().toString()))
							{
								allowedLength = field.getAnnotation(Column.class).length();
								break;
							}
						}
						String columnHeaderName= mtc.getText();
						if(entityColumnsLengthMap.get(columnHeaderName)!=null && currvalue.length()> entityColumnsLengthMap.get(columnHeaderName))
						{
							FXOptionPane.showMessageDialog(null, mtc.getText().toString().replace("*", "")+" length should not be greater than database column length of "+entityColumnsLengthMap.get(columnHeaderName), "");
							returnHash.put(false, null);
							return returnHash;
						}
						if( allowedLength ==0)
							allowedLength = c.getDeclaredField(mtc.getEntityColumnName().toString()).getAnnotation(Column.class).length();

						if(currvalue.length()>allowedLength)
						{
							FXOptionPane.showMessageDialog(null, mtc.getText().toString().replace("*", "")+" length should not be greater than "+allowedLength, "");
							returnHash.put(false, null);
							return returnHash;
						}
					}
					
					if(getEnum(mtc.getColumnDataType()).getValidObj(currvalue)==null)
					{
					    returnHash.put(false, null);
						return returnHash;
					}
					
					targetMethod.invoke(entity, getEnum(mtc.getColumnDataType()).getValidObj(currvalue));
					returnHash.put(true, entity);
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			getLogger().error("An error occurred the Generic Table Maintenance Panel"+e.getMessage());
			returnHash.put(false, null);
			return returnHash;		
		}	
		return returnHash;		
	}
	
	protected boolean compareEntireEntity(AuditEntry entity, ArrayList<MaintenanceTableColumn> mtcList,AuditEntry originalEntity){
		try{
			for(MaintenanceTableColumn mtc :mtcList){
				Class<?> classz = entity.getClass();
				if(!StringUtils.isBlank(mtc.getEntityColumnName()))
				{
					String entityColName = getCorrectEntityColName(mtc.getEntityColumnName().toString());
					Method targetMethod = classz.getDeclaredMethod("get"+entityColName);	

					Object newValue=targetMethod.invoke(entity);
					Object oldValue=targetMethod.invoke(originalEntity);
					if(!newValue.equals(oldValue))
					{
						return false;
					}	
				}

			}
		}catch(Exception e){
			e.printStackTrace();
			getLogger().error("An error occurred the Generic Table Maintenance Panel"+e.getMessage());
			
			return true;		
		}	
		 return true;		
	}


	private ColumnDataType getEnum(String type) {
		if (type.equals("short")) return ColumnDataType.INTEGER;
		else if (type.equals("int")) return ColumnDataType.INTEGER;
		else if (type.equals("long")) return ColumnDataType.LONG;
		else if (type.equals("char")) return ColumnDataType.STRING;
		else if (type.equals("double")) return ColumnDataType.DOUBLE;
		else if (type.equals("boolean")) return ColumnDataType.BOOLEAN;
		else return ColumnDataType.getEnum(type);
	}

	public ObservableList<AuditEntry> populateTable(List<AuditEntry> displayEntityMap){

		ObservableList<AuditEntry> list=FXCollections.observableArrayList();
		try{
			for(AuditEntry entity : displayEntityMap){		
				list.add(entity);
			}
		}catch(Exception e){
			e.printStackTrace();
			getLogger().error("An error occurred the Generic Table Maintenance Panel"+e.getMessage());
		}
		return list;
	}

	protected void populateParentTable() throws Exception {
		try{
			clearData();
			String entityAlias=getGenericTableMaintenancePropertyBean().getEntityName().substring(getGenericTableMaintenancePropertyBean().getEntityName().lastIndexOf(".")+1, getGenericTableMaintenancePropertyBean().getEntityName().length());
			String selectQuery= getSelectPrefix(entityAlias) + filter.getWhereString();
			Long count=getService(GenericDaoService.class).getRowCountGenericSql(entityAlias,filter.getWhereString());
			if(count > getGenericTableMaintenancePropertyBean().getMaxRowCount())
			{
				FXOptionPane.showMessageDialog(null,count+" rows returned.Please select different criteria as more than "+getGenericTableMaintenancePropertyBean().getMaxRowCount()+" rows returned for the selected filter criteria.","");
				initializeData();
				return;
			}
			ObservableList<AuditEntry> freshEntityList = null;		
			freshEntityList = FXCollections.observableArrayList(getService(GenericDaoService.class).executeGenericSqlSelectQuery(selectQuery.toString()));
			if(freshEntityList.size()==0)
				FXOptionPane.showMessageDialog(null,"No records available for the filter criteria","");
			if(!originalEntityList.isEmpty()){				
				for(AuditEntry currentEntity : freshEntityList ){
					if(!originalEntityList.contains(currentEntity)){
						originalEntityList.add(currentEntity);						
						AuditEntry currentEntityClone = (AuditEntry)currentEntity.deepCopy();
						displayEntityList.add(currentEntityClone);
					}
				}
			}
			else{
				for(AuditEntry currentEntity : freshEntityList ){
					originalEntityList.add(currentEntity);						
					AuditEntry currentEntityClone = (AuditEntry)currentEntity.deepCopy();						
					displayEntityList.add(currentEntityClone);						
				}
			}
			initializeData();
		}
		catch(Exception e){
			e.printStackTrace();
			getLogger().error("An error occurred the Generic Table Maintenance Panel"+e.getMessage());
		}
	}

	public void invokeCommonFilter() {
		try {
			if(this.filter == null || !this.filter.isShowing())
				this.filter = new CommonFilterDialog(this, getApplicationId(),"ParentTable");

		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error("An error occurred the Generic Table Maintenance Panel"+e.getMessage());
		}
	}

	public void handleCellEdit(CellEditEvent<AuditEntry, Object> t) {

		try {
			String currValue = t.getNewValue().toString();	
			displayEntityList=getViewTable().getItems();		
			AuditEntry entity =  displayEntityList.get(t.getTablePosition().getRow());
			AuditEntry entityClone = (AuditEntry)entity.deepCopy();
			HashMap<Boolean,AuditEntry> returnHash = updateMapEntity(currValue,t.getTablePosition().getColumn()+1,entity ,cfg.getDisplayColumns());
			if(returnHash.containsKey(true))
			{
				AuditEntry newEntity = returnHash.get(true);
				getLogger().info("Entity key after update : " + newEntity.getId());
				updatedEntityMap.put(t.getTablePosition().getRow(), newEntity);
				if(oldValuesEntityMap.isEmpty()|| oldValuesEntityMap.get(t.getTablePosition().getRow())==null)
				{
					oldValuesEntityMap.put(t.getTablePosition().getRow(),entityClone);
				}
			}
			else
			{
				getTableData();
			}
			enableDisableSave();
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error("An error occurred the Generic Table Maintenance Panel"+e.getMessage());
		}
	}

	public void executeDeleteGenericEntities(List deleteKeySet) {
		saveHistoryEntities(deleteKeySet);
		getEntityDao(getGenericTableMaintenancePropertyBean().getDaoName()).removeAll(deleteKeySet);
	}
	
	public void saveHistoryEntities(List entitiesList)
	{
		if(getGenericTableMaintenancePropertyBean().isHistoryEntitySaveEnabled())
		{							
			List historyList = getHistoryEntityList(entitiesList,false);			
			if(historyList.size()>0)
				getEntityDao(getGenericTableMaintenancePropertyBean().getHistoryDaoName()).saveAll(historyList);
			List oldValuesHistoryEntityList=getHistoryEntityList(new  ArrayList<AuditEntry>(oldValuesEntityMap.values()),true);
			if(oldValuesHistoryEntityList.size()>0)
				getEntityDao(getGenericTableMaintenancePropertyBean().getHistoryDaoName()).saveAll(oldValuesHistoryEntityList);			
		}
	}
	
	private void getEntityColumnLengthData() {
		try {
			Class<?> entityClass=Class.forName(getGenericTableMaintenancePropertyBean().getEntityName());
			List<Object[]> columnsLengthArray =getEntityDao(getGenericTableMaintenancePropertyBean().getDaoName()).findAllColumnLengths(CommonUtil.getTableName((Class<? extends AuditEntry>) entityClass));			
			for(Object[] array:columnsLengthArray) 
			{
				entityColumnsLengthMap.put((String)array[0], (Integer)array[1]);
			}
			if(getGenericTableMaintenancePropertyBean().isHistoryEntitySaveEnabled())
			{
				Class<?> historyEntityClass=Class.forName(getGenericTableMaintenancePropertyBean().getHistoryEntityName());
				List<Object[]> historyColumnsLengthArray =getEntityDao(getGenericTableMaintenancePropertyBean().getHistoryDaoName()).findAllColumnLengths(CommonUtil.getTableName((Class<? extends AuditEntry>) historyEntityClass));			
				for(Object[] array:historyColumnsLengthArray) 
				{
					historyEntityColumnsLengthMap.put((String)array[0], (Integer)array[1]);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			getLogger().error("An error occurred the Generic Table Maintenance Panel"+e.getMessage());
		}
	}

	

	public void executeSaveGenericEntities(List updateKeySet) {
		try {
			saveHistoryEntities(updateKeySet);			
			getEntityDao(getGenericTableMaintenancePropertyBean().getDaoName()).saveAll(updateKeySet);	
			oldValuesEntityMap.clear();
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error("An error occurred the Generic Table Maintenance Panel"+e.getMessage());
		} 		
	}

	private List getHistoryEntityList(List updateKeySet,boolean isOriginal) {
		List historyList=new ArrayList();
		try{
			Class<?> historyClass=Class.forName(getGenericTableMaintenancePropertyBean().getHistoryEntityName());
			Class<?> entityClass=Class.forName(getGenericTableMaintenancePropertyBean().getEntityName());
			Class<?>[] argTypes={entityClass,String.class,String.class,String.class};
			Constructor constructor=historyClass.getConstructor(argTypes);
			for(Object obj:updateKeySet)
			{	Object historyEntity=null;
			if(isOriginal)
				historyEntity=constructor.newInstance(obj, getMainWindow().getUserId(), getChangeTxtField().getText(), getChangeDescTxtArea().getText()+ORIGINAL_RECORD);
			else
				historyEntity=constructor.newInstance(obj, getMainWindow().getUserId(), getChangeTxtField().getText(), getChangeDescTxtArea().getText());
				historyList.add(historyEntity);
			}
			return historyList;
		}catch(Exception e)
		{
			e.printStackTrace();
			getLogger().error("An error occurred the Generic Table Maintenance Panel"+e.getMessage());
		}
		return historyList;
	}

	private IDaoService getEntityDao(String daoName) {
		Class<?> daoClass=null;
		IDaoService dao=null;
		try {
			daoClass = Class.forName(daoName);
			dao = ServiceFactory.getDao((Class<? extends IDaoService>)daoClass);
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error("An error occurred the Generic Table Maintenance Panel"+e.getMessage());
		}		
		return dao;
	}
	
	@Override
	public void onTabSelected() {
		if(isInitialized) {
			return;
		} 
	}

	public void configureColumns(){
		try{
			MaintenanceTableColumn updateDeleteImageColumn=getTableColumn("");
			Callback<TableColumn<String,String>, TableCell<String,String>> updateDeleteImageColumnCellFactory =
					new Callback<TableColumn<String,String>, TableCell<String,String>>() {
				public TableCell<String,String> call(TableColumn<String,String> p) {
					return new UpdateDeleteImageColumnCellFactory<String>();
				}
			};
			updateDeleteImageColumn.setCellFactory(updateDeleteImageColumnCellFactory);
			cfg.getDisplayColumns().add(updateDeleteImageColumn);		
			List<GenericTableMaintFilter> filterList=TableMaintenanaceProperties.getFilters(getApplicationId());
			for(GenericTableMaintFilter filter:filterList)
			{
				MaintenanceTableColumn mtc=getTableColumn(filter.getLabel());
				mtc.setCellValueFactory(new PropertyValueFactory<AuditEntry,Object>(filter.getColumnName()));
				mtc.setEntityColumnName(filter.getColumnName());
				if(filter.isEditable())
				{
					mtc.setEditable(filter.isEditable());
					 Callback<TableColumn<Map, String>, TableCell<Map, String>> cellFactoryForMap = new Callback<TableColumn<Map, String>, TableCell<Map, String>>() {
				            public TableCell call(TableColumn p) {
				                return new LoggedTextFieldTableCell(new StringConverter() {
				                    @Override
				                    public String toString(Object t) {
				                        return String.valueOf(t);
				                    }
				
				                    @Override
				                    public Object fromString(String string) {
				                        return string;
				                    }
				                }
				               
				                )
				                {
				                	 @Override 
				                	 public void commitEdit(Object item) {
				                         super.commitEdit(item);				                         
				                         if (item != null && !(item.toString()).equals("")) {
				                        	 updatedTableRowList.addAll(getViewTable().getSelectionModel().getSelectedIndices());
				                        	 rowFactory.getStyledUpdatedRowIndicesList().addAll(getViewTable().getSelectionModel().getSelectedIndices());
				                        	 enableDisableColumnSorting(false);
				                        	 refreshViewTable();
				                         }
				                     }	
				                };
				       
				            }
				        };
				        mtc.setCellFactory(cellFactoryForMap);
					   mtc.setOnEditCommit(new EventHandler<CellEditEvent<AuditEntry, Object>>() {
						public void handle(CellEditEvent<AuditEntry, Object> t) {
							handleCellEdit(t);
						}
					});
				}				
				mtc.setColumnDataType(filter.getDataType());			
				cfg.getDisplayColumns().add(mtc);			
			}
		}catch(Exception e){
			e.printStackTrace();
			getLogger().error("An error occurred the Generic Table Maintenance Panel"+e.getMessage());
		}
	}

	public void update(Observable o, Object arg){
		try {
			 this.filter=(CommonFilterDialog)arg;
			 if(filter.getObserverAction().equalsIgnoreCase("CANCEL"))	{
				getLogger().info("Filter was cancelled out");
			}else if(filter.getObserverAction().equalsIgnoreCase("ParentTable"))	{
				getLogger().info("WhereString is " + filter.getWhereString());
				populateParentTable();				
			} 
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error("An error occurred the Generic Table Maintenance Panel"+e.getMessage());
		}
	}
	
	protected class UpdateDeleteImageColumnCellFactory<T> extends	TableCell<T, String> {

		public UpdateDeleteImageColumnCellFactory() {
		}

		@Override
		public void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);
			if((AuditEntry)getTableRow().getItem()!=null && !deleteEntityList.isEmpty() && deleteEntityList.contains((AuditEntry)getTableRow().getItem())){
				setGraphic(getDeleteImageView());
				return;
			}
			if((AuditEntry)getTableRow().getItem()!=null && !updatedEntityMap.isEmpty() && updatedEntityMap.containsValue((AuditEntry)getTableRow().getItem())){
				setGraphic(getUpdateImageView());
				return;
			}
			setGraphic(null);			
		}

		private ImageView getUpdateImageView() {
				String updateImagePath = "/resource/com/honda/galc/client/images/GenericTableMainteanceUpdate.jpg";
				Image updateImage = new Image(updateImagePath);
				ImageView updateImageView=new ImageView(updateImage);	
				return updateImageView;
		}

		private ImageView getDeleteImageView() {		
				String deleteImagePath = "/resource/com/honda/galc/client/images/GenericTableMainteancDelete.jpg";
				Image deleteImage = new Image(deleteImagePath);
				ImageView deleteImageView=new ImageView(deleteImage);
				return deleteImageView;
		}
	}

	protected ImageView getColorLegendImageView() {
		if (colorLegendImageView == null) {
			String colorLegendImagePath = "/resource/com/honda/galc/client/images/GenericMaintenanceColorLegend.jpg";
			Image colorLegendImage = new Image(colorLegendImagePath);
			colorLegendImageView = new ImageView(colorLegendImage);
		}
		return colorLegendImageView;
	}


	
	public void massUpdate() {
		try {
			String massUpdateValue = getMassUpdateTxtField().getText();
			if(massUpdateValue.isEmpty())
			{
				FXOptionPane.showMessageDialog(null, " Please enter the Mass Update Value", "");
				return;
			}
			final ObservableList<TablePosition> selectedCells = getViewTable().getSelectionModel().getSelectedCells();
			if(selectedCells.size()==0)
			{
				FXOptionPane.showMessageDialog(null, " Please select the cells for Mass Update", "");
				return;
			}
			for (TablePosition pos : selectedCells) 
			{
				if(pos.getColumn()==0)
				{
					FXOptionPane.showMessageDialog(null, " Please do not select first column(reserved for displaying Update/delete Images) for Mass Update.", "");
				    return;
				}
			}
			FXOptionPane.Response response = FXOptionPane.showConfirmDialog(null, " Do you wish to update the selected Cells with "+ "\"" + massUpdateValue + "\" value ?", "Confirm Mass Update?", FXOptionPane.Type.CONFIRM);
			if(response.equals(FXOptionPane.Response.YES))
			{
				displayEntityList=getViewTable().getItems();	
				
				for (TablePosition pos : selectedCells) 
				{
					AuditEntry entity =  displayEntityList.get(pos.getRow());
					AuditEntry entityClone = (AuditEntry)entity.deepCopy();
					HashMap<Boolean,AuditEntry> returnHash = updateMapEntity(massUpdateValue,pos.getColumn()+1,entity ,cfg.getDisplayColumns());
					if(returnHash.containsKey(true))
					{
						AuditEntry newEntity = returnHash.get(true);
						getLogger().info("Entity key after update : " + newEntity.getId());
						updatedEntityMap.put(pos.getRow(), newEntity);
						if(oldValuesEntityMap.isEmpty()|| oldValuesEntityMap.get(pos.getRow())==null)
						{
							oldValuesEntityMap.put(pos.getRow(),entityClone);
						}
					}		    	
				}
				updatedTableRowList.addAll(getViewTable().getSelectionModel().getSelectedIndices());
				rowFactory.getStyledUpdatedRowIndicesList().addAll(getViewTable().getSelectionModel().getSelectedIndices());
				enableDisableColumnSorting(false);
				getTableData();    
				enableDisableSave(); 
			}

		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error("An error occurred in the Generic Table Maintenance Panel"+e.getMessage());
		}
	}
	
	public GenericTableMaintenancePropertyBean getGenericTableMaintenancePropertyBean() {
		return genericTableMaintenancePropertyBean;
	}



} 
