package com.honda.galc.client.common.component;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * This class serves as both a TableModel and a CellRenderer.  It allows the user to
 * simply manage the background color for table rows.  Note: the tableModel may either
 * editable or non-editable and ALL cells within the table will have the same
 * editability.
 * <h4>Usage and Example</h4>
 * Currently being used by AEOffL2View class.
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>bmarks</TD>
 * <TD>2004/06/10</TD>
 * <TD>1.1.2.1</TD>
 * <TD>OHOnPhase2</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author bmarks
 */
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class RowColorTableModelCellRenderer extends DefaultTableModel
		implements TableCellRenderer {
			
	
	private static final long serialVersionUID = 1L;
	private TableCellRenderer _cellRenderer = new DefaultTableCellRenderer();
	private Color _defaultRowBackgroundColor = Color.white;
	private boolean _isModelEditable = true;
	private ArrayList<Color> _backgroundColorList = null;
	private boolean _backgroundColorsModified = false;

	//------------------------------------------------------
	// Constructors matching DefaultTableModel constructors
	//------------------------------------------------------
	public RowColorTableModelCellRenderer() {
		super();
		initializeBackgroundColorList(0);
	}

	public RowColorTableModelCellRenderer(int numRows, int numColumns) {
		super(numRows, numColumns);
		initializeBackgroundColorList(numRows);
	}

	public RowColorTableModelCellRenderer(Object[][] data, String[] colNames) {
		super(data, colNames);
		initializeBackgroundColorList(data.length);
	}

	public RowColorTableModelCellRenderer(Object[] colNames, int numRows) {
		super(colNames, numRows);
		initializeBackgroundColorList(numRows);
	}

	public RowColorTableModelCellRenderer(Vector<?> colNames, int numRows) {
		super(colNames, numRows);
		initializeBackgroundColorList(numRows);
	}

	public RowColorTableModelCellRenderer(Vector<?> data, Vector<?> colNames) {
		super(data, colNames);
		initializeBackgroundColorList(data.size());
	}

	//----------------
	// Public methods
	//----------------
	public void resetRowColors() {
		initializeBackgroundColorList(_backgroundColorList.size());
//		initializeBackgroundColorList(this.getRowCount());
		_backgroundColorsModified = false;
	}
	
	public void setDefaultRowColor(Color color) {
		_defaultRowBackgroundColor = color;
		if (!_backgroundColorsModified) {
			resetRowColors();
		}
	}
	
	public void setRowColor(int row, Color color) {
		_backgroundColorList.set(row, color);
		_backgroundColorsModified = true;
	}

	public void setModelEditable(boolean flag) {
		_isModelEditable = flag;
	}

	//-------------------------------------------
	// Implemented methods for TableCellRenderer
	//-------------------------------------------
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		// Use helper renderer to do most of work
		Component comp = _cellRenderer.getTableCellRendererComponent(
			table, value, isSelected, hasFocus, row, column);

		// Now set cell background color
		if (table.getSelectedRow() != row){
			comp.setBackground((Color) _backgroundColorList.get(row));
		}
		return comp;
	}

	//-------------------------------------------
	// Overloaded methods from DefaultTableModel
	//-------------------------------------------
	// As noted previously, all cells will have the same editability
	// as the entire table
	public boolean isCellEditable(int row, int column) {
		return _isModelEditable;
	}

	//-----------------
	// Private methods
	//-----------------
	private void initializeBackgroundColorList(int size) {
		_backgroundColorList = new ArrayList<Color>();
		for (int i = 0; i < size; i++) {
			_backgroundColorList.add(_defaultRowBackgroundColor);
		}
	}
}