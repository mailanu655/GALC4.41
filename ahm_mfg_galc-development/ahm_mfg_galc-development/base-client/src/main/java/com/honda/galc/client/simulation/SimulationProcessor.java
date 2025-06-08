package com.honda.galc.client.simulation;

import static org.fest.swing.data.TableCell.row;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.fest.swing.core.BasicRobot;
import org.fest.swing.core.KeyPressInfo;
import org.fest.swing.core.MouseButton;
import org.fest.swing.core.Robot;
import org.fest.swing.data.TableCell;
import org.fest.swing.finder.JOptionPaneFinder;
import org.fest.swing.finder.WindowFinder;
import org.fest.swing.fixture.ContainerFixture;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JButtonFixture;
import org.fest.swing.fixture.JCheckBoxFixture;
import org.fest.swing.fixture.JComboBoxFixture;
import org.fest.swing.fixture.JListFixture;
import org.fest.swing.fixture.JMenuItemFixture;
import org.fest.swing.fixture.JOptionPaneFixture;
import org.fest.swing.fixture.JRadioButtonFixture;
import org.fest.swing.fixture.JTabbedPaneFixture;
import org.fest.swing.fixture.JTableFixture;
import org.fest.swing.fixture.JTextComponentFixture;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.notification.IClientNotificationHandler;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.simulation.AbstractSimulationProcessor;
import com.honda.galc.notification.service.INotificationService;

/**
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
 * May 14, 2012
 *
 *
 */
/**
 * 
 *    
 * @author Gangadhararao Gadde : Nov 9, 2017 : Simulation changes
 * @author Fredrick Yessaian - Aug 22, 2018 : RGALCDEV-8181 : Conditions moved to Simulation Processor.
 */
public class SimulationProcessor extends AbstractSimulationProcessor implements INotificationService, IClientNotificationHandler{
	private Robot robot;
	
	public SimulationProcessor() {
	}
	
	public Robot getRobot() {
		return robot;
	}

	public void setRobot(Robot robot) {
		this.robot = robot;
	}
	

	public void buttonClick(String componentName, Point point) {
		if(!StringUtils.isEmpty(componentName)) {
			JButtonFixture button = findButton(componentName);
			button.click();
		}else {
			if(point == null) return;
			FrameFixture window= findMainWindow();
			point.x +=window.component().getX();
			point.y +=window.component().getY();
			robot.click(point, MouseButton.LEFT_BUTTON, 1);
		}
		
		getLogger().info("button " + componentName + " is clicked");
	}
	
	public void mouseClick(String componentName, String mouseButton, Point point, int mouseClickDelay) {
		ContainerFixture<?> container = findContainer();
		Component comp = StringUtils.isEmpty(componentName)? container.component() : 
				robot.finder().findByName(container.target, componentName);
		
		point.x +=comp.getX();
		point.y +=comp.getY();
		MouseButton button = StringUtils.isEmpty(mouseButton) || mouseButton.equalsIgnoreCase("LEFT")?
				MouseButton.LEFT_BUTTON : MouseButton.RIGHT_BUTTON;

		doRobotClick(point, button, mouseClickDelay);
	}

	private void doRobotClick(Point point, MouseButton button, int mouseClickDelay) {
		try {
			Thread.sleep(mouseClickDelay);		
			robot.click(point, button, 1);
			getLogger().info("Mouse is clicked at point " + point);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void enterText(String componentName, String text,boolean hasEnterKey) {
		JTextComponentFixture textBox = findTextBox(componentName);
		if(textBox !=null) {
			textBox.setText(text);
			if(hasEnterKey) textBox.pressAndReleaseKey(KeyPressInfo.keyCode(KeyEvent.VK_ENTER));
		}else robot.enterText(text + (hasEnterKey ? "\n" : ""));
		getLogger().info("entered text " + text);
	}
	
	
	protected void createRobot() {
		if(robot == null)
			robot = BasicRobot.robotWithCurrentAwtHierarchy();
	}
	
	@Override
	protected void printComponents() {
		robot.printer().printComponents(System.out);
	}
	
	private FrameFixture findMainWindow() {
		String frameName = null;
		if(!simEvent.isContainerComponentDefined()) frameName = ApplicationContext.getInstance().getDefaultApplication().getApplicationId();
		else if(simEvent.getWindow()!=null) frameName = simEvent.getWindow();
		else return null;
		return WindowFinder.findFrame(frameName).withTimeout(10000).using(robot);
	}
	
	private FrameFixture findDefaultWindow() {
		return WindowFinder.findFrame(ApplicationContext.getInstance().getDefaultApplication().getApplicationId()).withTimeout(10000).using(robot);
	}
	
	private Logger getLogger() {
		return ((MainWindow)findDefaultWindow().component()).getLogger();
	}
	
	private DialogFixture findDialog() {
		if(simEvent.getDialog() != null) return WindowFinder.findDialog(simEvent.getDialog()).withTimeout(5000).using(robot);
		else return null;
	}
	
	private JOptionPaneFixture findOptionPane() {
		if(simEvent.getOptionPane() != null) return JOptionPaneFinder.findOptionPane().withTimeout(5000).using(robot);
		else return null;
	}
	
	
	private JButtonFixture findButton(String name) {
		JOptionPaneFixture optionPane = findOptionPane();
		if(optionPane != null) {
			if("OK".equalsIgnoreCase(name)) return optionPane.okButton();
			else if("YES".equalsIgnoreCase(name)) return optionPane.yesButton();
			else if("NO".equalsIgnoreCase(name)) return optionPane.noButton();
			else if("CANCEL".equalsIgnoreCase(name)) return optionPane.cancelButton();
			else return optionPane.button(name);
		}else {
			DialogFixture dialog = findDialog();
			if(dialog != null) return dialog.button(name);
			else return findMainWindow().button(name);
		}
	}
	
	private JCheckBoxFixture findCheckBox(String name) {
		boolean isNameEmpty  = name == null || name.length() == 0;
		ContainerFixture<?> container = findContainer();
		return isNameEmpty? container.checkBox() : container.checkBox(name);
	}
	
	private JTextComponentFixture findTextBox(String name) {
		boolean isNameEmpty  = name == null || name.length() == 0;
		if(!simEvent.isContainerComponentDefined()&& isNameEmpty) return null;
		ContainerFixture<?> container = findContainer();
		return isNameEmpty? container.textBox() : container.textBox(name);
	}
	
	private ContainerFixture<?> findContainer() {
		JOptionPaneFixture optionPane = findOptionPane();
		if(optionPane != null) return optionPane;
		DialogFixture dialog = findDialog();
		if(dialog != null) return dialog;
		return findMainWindow();
	}

	public void selectTabbedPane(String componentName) {
		FrameFixture frameFixture= findMainWindow();
		JTabbedPaneFixture tabbedPaneFixture = frameFixture.tabbedPane();
		tabbedPaneFixture.selectTab(componentName);
	}

	private Integer toInteger(String param) {
		return NumberUtils.isNumber(param) ? NumberUtils.toInt(param) : null; 
	}
	
	public void selectTableRow(String componentName, String mouseButton, String paramValue1, String value, String menuItem) {
		Integer row = toInteger(paramValue1);
		if(row != null){
			selectTableRow(componentName, mouseButton, row);
		}else{
			selectTableRow(componentName, mouseButton, paramValue1);
		}
		
	}
	
	private void selectTableRow(String componentName, String mouseButton,Integer row) {
		FrameFixture frameFixture= findMainWindow();
		JTableFixture tableFixture = frameFixture.table(componentName);
		tableFixture.selectRows(row);
		if(StringUtils.isEmpty(mouseButton) || mouseButton.equalsIgnoreCase("LEFT")){
			getLogger().info("Table " + componentName + " row : "+ row + " is selected");
		}else if(mouseButton.equalsIgnoreCase("RIGHT")) {
			tableFixture.showPopupMenuAt(TableCell.row(row).column(0));
			getLogger().info("Table " + componentName + " row : "+ row + " is right clicked");
		}
	}
	
	private void selectTableRow(String componentName, String mouseButton,String regex) {
		FrameFixture frameFixture= findMainWindow();
		JTableFixture tableFixture = frameFixture.table(componentName);
		TableCell cell = tableFixture.cell(Pattern.compile(regex));
		tableFixture.selectCell(cell);
		if(StringUtils.isEmpty(mouseButton) || mouseButton.equalsIgnoreCase("LEFT")){
			getLogger().info("Table " + componentName + " row : "+ cell.row + " is selected");
		}else if(mouseButton.equalsIgnoreCase("RIGHT")) {
			tableFixture.showPopupMenuAt(cell);
			getLogger().info("Table " + componentName + " row : "+ cell.row+ " is right clicked");
		}
	}
	
	public void selectListBox(String componentName, String text){
		Integer row = toInteger(text);
		
		if(row != null){
			selectListBoxByRow(componentName, row);
		}else{
			selectListBoxByText(componentName, text);
		}
		
	}
	
	private void selectListBoxByText(String componentName, String text){
		FrameFixture frameFixture= findMainWindow();
		if(frameFixture != null){
			JListFixture listFixture = frameFixture.list(componentName);
			listFixture.selectItem(text);
			return;
		}
		
		DialogFixture dialog = findDialog();
		if(dialog != null){
			JListFixture listFixture = dialog.list(componentName);
			listFixture.selectItem(text);
		}		
	}
	
	private void selectListBoxByRow(String componentName, Integer row){
		FrameFixture frameFixture= findMainWindow();
		if(frameFixture != null){
			JListFixture listFixture = frameFixture.list(componentName);
			listFixture.selectItems(row);
			return;
		}
		
		DialogFixture dialog = findDialog();
		if(dialog != null){
			JListFixture listFixture = dialog.list(componentName);
			listFixture.selectItems(row);
		}
	}
	
	public void selectRadioButton(String componentName) {
		FrameFixture frameFixture= findMainWindow();
		JRadioButtonFixture radioButtonFixture = frameFixture.radioButton(componentName);
		radioButtonFixture.click();
		getLogger().info("Radio Button " + componentName + " is selected");
	}
	
	public void selectCheckBox(String componentName,Boolean check) {
		JCheckBoxFixture checkBoxFixture = findCheckBox(componentName);
		if(check == null || check)checkBoxFixture.check();
		else checkBoxFixture.uncheck();
	}
	
	public void enterTextInComboBox(String componentName, String text) {
		FrameFixture frameFixture= findMainWindow();
		JComboBoxFixture comboBoxFixture = frameFixture.comboBox(componentName);
		comboBoxFixture.target.setSelectedItem(text);
		comboBoxFixture.pressAndReleaseKey(KeyPressInfo.keyCode(KeyEvent.VK_ENTER));
		getLogger().info("ComboBox item "+componentName + " : " + text + " is entered");
	}
	
	public void selectComboBox(String componentName, String text) {
		FrameFixture frameFixture= findMainWindow();
		JComboBoxFixture comboBoxFixture = frameFixture.comboBox(componentName);
		comboBoxFixture.selectItem(text);
		getLogger().info("ComboBox item "+text + " is selected");
	}
	public void selectMenuItem(String componentName){
		FrameFixture frameFixture= findMainWindow();
		String[] items = componentName.split("\\.");
		JMenuItemFixture menuItemFixture = frameFixture.menuItemWithPath(items);
		menuItemFixture.click();
		getLogger().info("Menu item " + componentName + " is selected");
	}
	
	public void enterTableCellValue(String componentName, String rowNum, String columnNum, String value) {
		FrameFixture frameFixture= findMainWindow();
		JTableFixture tableFixture = frameFixture.table(componentName);
		Integer rowno= Integer.parseInt(rowNum);
		Integer colno= Integer.parseInt(columnNum);
		tableFixture.enterValue(row(rowno).column(colno), value); 
		getLogger().info(value+" entered at row:"+rowNum+" , column:"+columnNum+" for table:" + componentName);
	}

	public void enterKey(int keyCode) {
		getRobot().pressAndReleaseKey(keyCode, null);
	}

	@Override
	public void findTextAndClick(String componentName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAppContext(ApplicationContext appContext) {
		// TODO Auto-generated method stub
	}

	@Override
	public void clearTextBox(String componentName, Integer size) {
		// TODO Auto-generated method stub
		
	}

}
