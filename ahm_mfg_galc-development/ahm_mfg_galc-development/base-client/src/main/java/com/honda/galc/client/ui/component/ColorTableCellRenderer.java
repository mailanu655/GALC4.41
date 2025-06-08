package com.honda.galc.client.ui.component;

import java.awt.Color;
import java.awt.Component;
import java.io.Serializable;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.honda.galc.util.Primitive;
import com.honda.galc.util.ReflectionUtils;

/**
 * 
 * <h3>ColorTableCellRenderer Class description</h3>
 * <p> ColorTableCellRenderer description </p>
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
 * May 2, 2011
 *
 *
 */
public class ColorTableCellRenderer implements TableCellRenderer, Serializable{

	private static final long serialVersionUID = 1L;

	private TableCellRenderer targetCellRenderer;
	private int rowCount;
	private int columnCount;
	private Color[][] colors;
	private Color[][] foregroundColors;
	
	public ColorTableCellRenderer(TableCellRenderer targetCellRenderer, int rowCount, int columnCount) {
		
		this.targetCellRenderer = targetCellRenderer;
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		colors = new Color[rowCount][columnCount];
		foregroundColors = new Color[rowCount][columnCount];
		
	}
	
	public ColorTableCellRenderer(TableCellRenderer targetCellRenderer, Color[][] colors,int rowCount, int columnCount) {
		
		this.targetCellRenderer = targetCellRenderer;
		
		this.colors = colors;
		this.foregroundColors = new Color[rowCount][columnCount];
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		
	}
	
	public ColorTableCellRenderer(TableCellRenderer targetCellRenderer, Color[][] colors, Color[][] foregroundColors, int rowCount, int columnCount) {
		
		this.targetCellRenderer = targetCellRenderer;
		
		this.colors = colors;
		this.foregroundColors = foregroundColors;
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		
	}
	
	
	
	public TableCellRenderer getTargetCellRenderer() {
		return targetCellRenderer;
	}

	public void setTargetCellRenderer(TableCellRenderer targetCellRenderer) {
		this.targetCellRenderer = targetCellRenderer;
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component comp = 
			   targetCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if(!table.isCellSelected(row, column)&& row <rowCount && column<columnCount) {
		 comp.setBackground(colors[row][column]);
		 comp.setForeground(foregroundColors[row][column]);
		}
		return comp;
	}
	
	public void setColor(Color color, int row, int column) {
		setColor(color,null,row,column);
	}
	
	public void setColor(Color color, Color foregroundColor, int row, int column) {
		if(row < 0 || row >= rowCount || column < 0 || column >=columnCount) return;
		colors[row][column] = color;
		foregroundColors[row][column] = foregroundColor;
	}
	
	
	public void setColors(Color[][] colors, int rowCount, int columnCount) {
		setColors(colors,null,rowCount,columnCount);
	}
	
	public void setColors(Color[][] colors, Color[][] foregroundColors, int rowCount, int columnCount) {
		this.colors = colors;
		this.foregroundColors = foregroundColors;
		this.rowCount = rowCount;
		this.columnCount = columnCount;
	}
	
	public void setRowColor(Color color, int row) {
		setRowColor(color,null,row);
	}
	
	public void setRowColor(Color color, Color foregroundColor, int row) {
		
		if(row < 0 || row >= rowCount) return;
		
		for(int i = 0; i< columnCount ; i++) {
			
			colors[row][i] = color;
			
			foregroundColors[row][i] = foregroundColor;
			
		}
	}
	
	public void setColumnColor(Color color, int column) {
		setColumnColor(color,null,column);
	}
	
	public void setColumnColor(Color color, Color foregroundColor, int column) {
		
		if(column < 0 || column >= columnCount) return;
		
		for(int i = 0; i< rowCount ; i++) {
			
			colors[i][column] = color;
			
			foregroundColors[i][column] = foregroundColor;
			
		}
	}
	
	public void reset() {
		
		for(int i = 0; i<rowCount; i++) {
			for(int j = 0; j < columnCount; j++) {
				colors[i][j] = null;
				foregroundColors[i][j] = null;
			}
		}
	}
	public void setHorizontalAlignment(int alignment) {
		// invoke setHorizonal alignment when the class has the method
		ReflectionUtils.invoke(targetCellRenderer, "setHorizontalAlignment", new Primitive(alignment));

	}
}
