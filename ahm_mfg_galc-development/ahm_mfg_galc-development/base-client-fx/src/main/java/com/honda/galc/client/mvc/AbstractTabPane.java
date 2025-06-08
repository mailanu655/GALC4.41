package com.honda.galc.client.mvc;

import java.util.Arrays;

import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappingList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class AbstractTabPane extends TabbedPanel {
	private double screenWidth;
	private double screenHeight;

	protected StringProperty tabStyle = new SimpleStringProperty();
	
	public AbstractTabPane(String screenName, int keyEvent, MainWindow mainWindow) {
		super(screenName, keyEvent, mainWindow);
	}
	
	protected Double[] getcolumnWidth(ColumnMappingList columnMappingList) {
		int columnCount = columnMappingList.get().size();
		Double[] columnWidth = new Double[columnCount];
		double totalWidth =  1;
		double eachColumnWidth = totalWidth / columnCount;
		Arrays.fill(columnWidth, eachColumnWidth);
		return columnWidth;
	}
	
	public String getTableFontStyle() {
		return String.format("-fx-font-size: %dpx;", (int)(0.009 * getScreenWidth()));
	}
	
	public String getContextMenuFontStyle() {
		return String.format("-fx-font-family : Dialog; -fx-font-weight: bold; -fx-background-color: #e8ecf1; -fx-font-size: %dpx;", (int)(0.007 * screenWidth));
	}
	
	public String getBorderStyle() {
		return "-fx-border-color: white, grey; -fx-border-width: 2, 1; -fx-border-insets: 0, 0 1 1 0";
	}
	
	public String getContextMenuItemPadding() {
		return "-fx-padding: 10 0 5 0";
	}
	
	public String getLabelStyle() {
		return String.format("-fx-font-family : Dialog; fx-padding: 0 2px 0 0; -fx-font-size: %dpx;", (int)(0.013 * (getScreenWidth())));
	}
	
	public double getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(double screenWidth) {
		this.screenWidth = screenWidth;
	}

	public double getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(double screenHeight) {
		this.screenHeight = screenHeight;
	}
}
