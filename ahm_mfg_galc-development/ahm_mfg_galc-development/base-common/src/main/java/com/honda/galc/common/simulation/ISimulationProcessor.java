package com.honda.galc.common.simulation;

import java.awt.Point;
/**
 * 
 *    
 * @version 1
 * @author Gangadhararao Gadde : Nov 9, 2017
 * @author Fredrick Yessaian - Aug 22, 2018 : RGALCDEV-8181 .
 */
public interface ISimulationProcessor {

	public void buttonClick(String componentName, Point point);
	
	public void mouseClick(String componentName, String mouseButton, Point point, int mouseClickDelay);
		
	public void enterText(String componentName, String text,boolean hasEnterKey);
	
	public void enterTextInComboBox(String componentName, String text);
		
	public void enterTableCellValue(String componentName, String rowNum, String columnNum, String value); 
	
	public void enterKey(int keyCode);
	
	public void selectCheckBox(String componentName,Boolean check);
	
	public void selectComboBox(String componentName, String text);
	
	public void selectListBox(String componentName, String text);
	
	public void selectMenuItem(String componentName);
	
	public void selectRadioButton(String componentName);
	
	public void selectTabbedPane(String componentName);
	
	public void findTextAndClick(String componentName);

	public void clearTextBox(String componentName, Integer size);

	public void selectTableRow(String componentName, String mouseButton,String columnName, String value,String menuItem);

}
