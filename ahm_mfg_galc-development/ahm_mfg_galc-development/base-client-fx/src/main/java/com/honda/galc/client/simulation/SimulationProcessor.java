package com.honda.galc.client.simulation;


import static org.loadui.testfx.GuiTest.find;

import java.awt.Point;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;

import org.apache.commons.lang.StringUtils;
import org.loadui.testfx.framework.robot.FxRobot;
import org.loadui.testfx.framework.robot.impl.FxRobotImpl;

import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.simulation.AbstractSimulationProcessor;
import com.honda.galc.notification.service.INotificationService;

/**
 * 
 * 
 * <h3>SimulationProcessor Class description</h3>
 * <p> SimulationProcessor description </p>
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
 * @author Jeffray Huang<br>
 * Feb 26, 2015
 *
 *
 */
/**
 * 
 *    
 * @author Gangadhararao Gadde : Nov 9, 2017 : Simulation changes
 * @author Fredrick Yessaian - Aug 22, 2018 : RGALCDEV-8181 : Conditions moved to Simulation Processor.
 */
public class SimulationProcessor extends AbstractSimulationProcessor implements INotificationService {
	
	private FxRobot robot;
	private static SimulationProcessor instance = null;
	private Scene scene = null;

	private SimulationProcessor() {
		this.setRobot(new FxRobotImpl());
	}
	
	public static SimulationProcessor getInstance() {
		if (instance == null) {
			instance = new SimulationProcessor();
		}
		return instance;
	}
	
	public void registerForEvents() {
		EventBusUtil.register(this);
	}
	
	private FxRobot getRobot() {
		return this.robot;
	}

	private void setRobot(FxRobot robot) {
		this.robot = robot;
	}
	
	public void buttonClick(String componentName, Point point) {
		if(simEvent.getMouseClickDelay() >=0) getRobot().sleep(simEvent.getMouseClickDelay());
		
		if(StringUtils.isNotEmpty(componentName)) {
			getRobot().clickOn("#"+componentName);
		}
		
		Logger.getLogger().info("button (Component Name) " + componentName + " is clicked");
	}
	
	public void enterText(String componentName, String text,boolean hasEnterKey) {
		if(simEvent.getMouseClickDelay() >=0) getRobot().sleep(simEvent.getMouseClickDelay());
		
		if(hasEnterKey){
			getRobot().clickOn("#"+componentName).write(text + Character.toString ((char) 10));
		}else{
			getRobot().clickOn("#"+componentName).write(text);
		}

		Logger.getLogger().info("entered text " + text);
	}
	
	@Override
	public void findTextAndClick(String componentName) {
		if(simEvent.getMouseClickDelay() >=0) getRobot().sleep(simEvent.getMouseClickDelay());
		
		if(simEvent.getMouseButton()== null || simEvent.getMouseButton().equalsIgnoreCase("LEFT")){
			getRobot().clickOn(componentName, MouseButton.PRIMARY);
		}else if(simEvent.getMouseButton().equalsIgnoreCase("RIGHT")) {
			getRobot().clickOn(componentName, MouseButton.SECONDARY);
		}
	}
	
	@Override
	public void mouseClick(String componentName, String mouseButton, Point point, int mouseClickDelay) {
		if(simEvent.getMouseClickDelay() >=0) getRobot().sleep(simEvent.getMouseClickDelay());
		Point2D xy = new Point2D(point.getX(), point.getY());
		MouseButton mouseButtonClick =  (mouseButton == "LEFT")? MouseButton.PRIMARY : MouseButton.SECONDARY;
		getRobot().clickOn(xy, mouseButtonClick);
		if(mouseButton== null || mouseButton.equalsIgnoreCase("LEFT")){
			getRobot().clickOn(xy, MouseButton.PRIMARY);
		}else if(mouseButton.equalsIgnoreCase("RIGHT")) {
			getRobot().clickOn(xy, MouseButton.SECONDARY);
		}
	}
	
	@Override
	protected void printComponents() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void enterKey(int keyCode) {
		// TODO Auto-generated method stub
	}

	@Override
	public void enterTextInComboBox(final String componentName, final String text) {
		if(simEvent.getMouseClickDelay() >=0) getRobot().sleep(simEvent.getMouseClickDelay());
		
		Platform.runLater( new Runnable() {
		    @Override
		    public void run() {
				final Node node = find("#"+componentName);
				final ComboBox cmbBox=(javafx.scene.control.ComboBox) node;
				cmbBox.focusedProperty().get();
				cmbBox.setValue(text);
		    }
		});
	}

	@Override
	public void enterTableCellValue(String componentName, String rowNum,
			String columnNum, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectCheckBox(String componentName, Boolean check) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectComboBox(String componentName, final String text) {

		if (simEvent.getMouseClickDelay() >= 0)	getRobot().sleep(simEvent.getMouseClickDelay());
		
		if (!StringUtils.isEmpty(componentName) && !StringUtils.isEmpty(text)) {
			final Node node = find("#"+componentName);
			final ComboBox comboBox=(javafx.scene.control.ComboBox) node;
			ObservableList list = comboBox.getItems();
			int count = 0;
			for (Object item : list) {
				if (item.toString().trim().equals(text)) {
					final int index = count;
					Platform.runLater( new Runnable() {
					    @Override
					    public void run() {
					    	comboBox.getSelectionModel().select(index);
					    }
					});
					break;
				}
				count++;
			}
		}
	}

	@Override
	public void selectListBox(String componentName, String text) {
		if (simEvent.getMouseClickDelay() >= 0)	getRobot().sleep(simEvent.getMouseClickDelay());
		
		if (!StringUtils.isEmpty(componentName) && !StringUtils.isEmpty(text)) {
			final Node node = find("#"+componentName);
			final ListView listView=(javafx.scene.control.ListView) node;
			ObservableList list = listView.getItems();
			int count = 0;
			for (Object item : list) {
				if (item.toString().trim().equals(text)) {
					final int index = count;
					Platform.runLater( new Runnable() {
					    @Override
					    public void run() {
					    	listView.getSelectionModel().select(index);
					    }
					});
					break;
				}
				count++;
			}
		}
	}

	@Override
	public void selectMenuItem(String componentName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectRadioButton(String componentName) {
		if(simEvent.getMouseClickDelay() >=0) getRobot().sleep(simEvent.getMouseClickDelay());
		getRobot().clickOn("#"+componentName);
	}

	@Override
	public void selectTabbedPane(String componentName) {
		// TODO Auto-generated method stub
		
	}

	/* Most of the TableView in FX Client uses ObjectTablePane class to draw Table View component. 
	 * But ManualProductEntryDialog.java uses TableView class directly. 
	 * So a common method has been introduced to get TableView object.
	 * */
	private TableView getTableViewObj(String componentName){
		Object tblObject = find("#"+componentName);
		
		if(tblObject == null){
			Logger.getLogger().error("No component found for " + componentName + ", Check the component name in input xml");
			return null;
		}
		try{
			if (tblObject instanceof ObjectTablePane) {
				ObjectTablePane objectTablePane = (ObjectTablePane)tblObject;
				return (javafx.scene.control.TableView)objectTablePane.getTable();
			}else if(tblObject instanceof TableView) {
				return (javafx.scene.control.TableView)tblObject;
			}else{
				Logger.getLogger().error("Incompatible TableView object, Check the component name in input xml : " + tblObject.getClass());
				return null;
			}
		}catch(Exception exp){
			Logger.getLogger().error("Exception caught while retrieving TableView object in SimulationProcessor.getTableViewObj(). returning null object");
			exp.getStackTrace();
			return null;
		}

	}	

	public void selectTableRow(String componentName, String mouseButton,String columnName ,String  value,final String menuItem) {
		try {
			final TableView tableView = getTableViewObj(componentName);
			Platform.runLater( new Runnable() {
				@Override
				public void run() {
					tableView.requestFocus();
				}
			});
			int rowCount = tableView.getItems().size();
			ObservableList<TableColumn> list = tableView.getColumns();
			if(StringUtils.isNotEmpty(columnName) && StringUtils.isNotEmpty(value))
			{
				for(TableColumn column:list)
				{
					if(column.getText().trim().equals(columnName))
					{
						for(int i=0;i<rowCount;i++)
						{
							final int index=i;
							if(column.getCellData(i)!=null && value.trim().equals(column.getCellData(i).toString().trim()))
							{				
								Platform.runLater( new Runnable() {
									@Override
									public void run() {
										tableView.getSelectionModel().select(index);
									}
								});	
								break;
							}				
						}			
					}
				}
			}
			if(StringUtils.isNotEmpty(mouseButton) && mouseButton.equalsIgnoreCase("RIGHT") && StringUtils.isNotEmpty(menuItem))
			{
				Thread.sleep(2000);
				final ContextMenu contextMenu=tableView.contextMenuProperty().get();
				final Bounds bounds=tableView.localToScene(tableView.getBoundsInLocal());	
				Platform.runLater( new Runnable() {
					@Override
					public void run() {
						contextMenu.show(tableView,bounds.getMinX() + bounds.getWidth()/2,bounds.getMinY() + bounds.getHeight()/2);		        		    
					}});				
				Thread.sleep(2000);
				Platform.runLater( new Runnable() {
					@Override
					public void run() {
						for(MenuItem item:contextMenu.getItems())
						{
							if(item.getText().trim().equals(menuItem))
							{
								item.fire();
								break;
							}
						}				
					}		        
				});
			}
		} catch (Exception e) {
			Logger.getLogger().info("An exception occurred "+e.getMessage());
		}
	}
	
	public Scene getMainScene() {
		return scene;
	}
	
	public void setScene(Scene scene){
		this.scene = scene;
	}

	@Override
	public void clearTextBox(String componentName, Integer size) {
		if(simEvent.getMouseClickDelay() >=0) getRobot().sleep(simEvent.getMouseClickDelay());
		getRobot().clickOn("#"+componentName).eraseText(size);
		Logger.getLogger().check("Text erased in " + componentName);
	}

	@Override
	protected void createRobot() {
		// TODO Auto-generated method stub
		
	}
}
