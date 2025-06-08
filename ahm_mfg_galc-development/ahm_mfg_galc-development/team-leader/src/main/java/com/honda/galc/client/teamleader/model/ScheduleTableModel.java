package com.honda.galc.client.teamleader.model;

import java.awt.Color;
import java.awt.Component;
import java.sql.Time;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;

import com.honda.galc.client.teamleader.ScheduleMaintenancePanel;
import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.common.logging.Logger;

/**
 * 
 * <h3>ScheduleTableModel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ScheduleTableModel ported from GALC </p>
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
 * @author Paul Chou
 * Mar 7, 2011
 *
 */

public class ScheduleTableModel extends BaseTableModel<Object[]>{
	private static final long serialVersionUID = 1L;
	public static String[] columnNames = {"PROCESS_LOCATION","PERIOD","SHIFT","PERIOD_LABEL","TYPE","PLAN","START_TIME","END_TIME","NEXT_DAY","CAPACITY","CAPACITY_ON"};
	private boolean editEnabled = true;
	private boolean clearPending = false;
	private ScheduleMaintenancePanel panel;
	private int cellChanges[][] = null;
	private int erroredRow = -1;
	private boolean enableValidation = true;
	protected String[] left = {"AE","PA"};
	protected String[] right = {"AF", "WE"};
	
	public ScheduleTableModel(ScheduleMaintenancePanel panel, List<Object[]> items) {
		super(items, columnNames, panel.getSchedulePanel().getTable());
		this.panel = panel;
		initialize();
	}

	private void initialize() {
		resetCellChanges(items);
		
		for(int i = 0; i < columnNames.length; i++){
			table.getColumnModel().getColumn(i).setCellRenderer(new Renderer());
			if(i > 5)
				table.getColumnModel().getColumn(i).setCellEditor(new CellEditor(new JTextField()));
		}
		
	}

	public boolean isCellEditable (int row, int column){
		if(!editEnabled ) { 
			return false;
		}
		
		if(columnNames[column].equalsIgnoreCase("START_TIME") ||
				columnNames[column].equalsIgnoreCase("END_TIME") ||
				columnNames[column].equalsIgnoreCase("PLAN") || 
				columnNames[column].equalsIgnoreCase("CAPACITY") ||
				columnNames[column].equalsIgnoreCase("CAPACITY_ON")){
			return true;
		} else 
			return false;
    }
	
	public Object getValueAt(int rowIndex, int columnIndex) {
        
		Object[] item = getItem(rowIndex);
		
		return item[columnIndex];
    }
	
	public void setValueAt(Object value, int row, int column) {
		
		try{
			super.setValueAt(value, row, column);
			
			if(!value.equals(getItem(row)[column])){
				getItem(row)[column] = value;
				this.fireTableCellUpdated(row, column);
				
				cellChanges[row][column] = 1;
				setClearPending(false);
				
				if(isEnableValidation())
					validateTableData();
			}
			
		}catch(Exception ex){
			Logger.getLogger().warn(ex, "Exception at setValueAt row:" + row , " col:" + column + " value:" + value);
		}
		
	}

	public void resetCellChanges(List<Object[]> items){
		int rows = (items == null) ? 0 : items.size();
		cellChanges = new int[rows][getColumnCount()];
		for(int i=0; i<rows; i++){
			for (int j=0; j<getColumnCount(); j++) {
				cellChanges[i][j] = 0;
			}
		}
	}
	
	public boolean isTableChanged() {
		for (int i=0; i<getRowCount(); i++) {
			if (isRowChanged(i)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isRowChanged(int row) {
		for (int i=0; i<getColumnCount(); i++) {
			if (hasCellChanged(row, i)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isEnableValidation() {
		return enableValidation;
	}

	public void setEnableValidation(boolean enableValidation) {
		this.enableValidation = enableValidation;
	}

	@Override
	public void refresh(List<Object[]> items) {
		resetCellChanges(items);
		
		super.refresh(items);
		
		validateTableData();
	}

	private void validateTableData() {
		String processLoc = "";
		long dayStartTime = -1;
		long curTime = -1;
		boolean validEnd = true;

		erroredRow = -1;
		
		Integer firstPeriodNextDay = null;
		Integer lastNextDay = null;
		Time lastEndTime = null;
		
		int count = getRowCount();
		
		for (int i = 0; i < count; i++) {
			String thisProcessLoc = getValueAt(i, 0).toString();
			// Since our table has multiple "process locations" (e.g. AE, AF, PA, WE)
			// need to validation each section (per process location) of the table
			// seperately.  Check here for a "new" section.
			if (!thisProcessLoc.equals(processLoc)) {
				if (!validEnd) {
					erroredRow = i-1;
					break;
				}
				// New process location, reset everything
				dayStartTime = getSecondOfDay(0, getValueAt(i, 6).toString());
				curTime = dayStartTime;
				processLoc = thisProcessLoc;
				validEnd = false;
				lastEndTime = getEndTime(i);
				firstPeriodNextDay = (Integer) getValueAt(i, 8);
				
				if (firstPeriodNextDay == null) {
					firstPeriodNextDay = 0;
				}
			}
			else {
				long thisStartTime = getSecondOfDay(dayStartTime, getValueAt(i, 6).toString());
				if (thisStartTime != (curTime + 1)) {
					erroredRow = i;
					break;
				}
			}
			
			// Validate not before
			long thisEndTime = getSecondOfDay(dayStartTime, getValueAt(i, 7).toString());
			if (thisEndTime <= curTime) {
				erroredRow = i;
				break;
			}
			else {
				// Check if endTime valid End
				if (thisEndTime - dayStartTime == 86399L) {
					validEnd = true;
				}
			
				Time endTime = getEndTime(i);
				Integer nextDayInt = (Integer) getValueAt(i, 8);
				Integer newNextDayInt = nextDayInt;
				
				
				if (lastNextDay == null) {
					lastNextDay = nextDayInt;
				}
				
				if ( endTime.getTime() < lastEndTime.getTime()) {
					if (nextDayInt.intValue() != firstPeriodNextDay + 1) {
						newNextDayInt = firstPeriodNextDay + 1;
						boolean validation = enableValidation;
						setEnableValidation(false);
						setValueAt(newNextDayInt, i, 8);
						setEnableValidation(validation);
					}
				}
				else {
					if (nextDayInt.intValue() != lastNextDay) {
						newNextDayInt = (isRowChanged(i -1))? lastNextDay : firstPeriodNextDay;
						boolean validation = enableValidation;
						setEnableValidation(false);
						setValueAt(newNextDayInt, i, 8);
						setEnableValidation(validation);
					}
				}
				lastNextDay = newNextDayInt;
				lastEndTime = endTime;
				
				curTime = thisEndTime;
			}
		}
		// Check the final row for a valid end (unless some other error occurred)
		if (erroredRow < 0 && !validEnd) {
			erroredRow = getRowCount()-1;
		}
		// Display error if necessary
		if (erroredRow >= 0) {
			panel.setErrorMessage("Error in time sequence.  Erroneous times shown in red.");
		}
	}
	
	private Time getEndTime(int row) {
		Object endTimeObj = getValueAt(row, 7);
		
		if(endTimeObj instanceof Time)
			return (Time)endTimeObj;
		else 
			return Time.valueOf(endTimeObj.toString());
	}

	public int getErroredRow() {
		return erroredRow;
	}

	public long getSecondOfDay(long start, String tod) {
		// Always receive tod as hh:mm:ss
		long seconds = 3600 * Integer.parseInt(tod.substring(0,2));
		seconds += 60 * Integer.parseInt(tod.substring(3,5));
		seconds += Integer.parseInt(tod.substring(6));
		if (seconds < start) {
			seconds += (24*60*60);
		}
		return seconds;
	}


	public boolean hasCellChanged(int row, int column) {
		return cellChanges[row][column] == 1 ? true: false;
	}


	public boolean isClearPending() {
		return clearPending;
	}

	public void setClearPending(boolean clearPending) {
		this.clearPending = clearPending;
	}
	
	private class Renderer extends DefaultTableCellRenderer{
		   
		private static final long serialVersionUID = 1L;
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			setForeground(Color.black);
			setBackground(Color.white);

			if (hasCellChanged(row, column)) {
				setOpaque(true);
				setBackground(new Color(255,128,128));
				if (!table.isCellEditable(row, column)) {
					setBackground(new Color(175,85,85));
				}
				
				if(panel != null) panel.dataChanged(true);
			}
			else if (!table.isCellEditable(row, column)) {
				setOpaque(true);
				setForeground(Color.darkGray);
				setBackground(new Color(225,225,225));
				if ((row%2) == 1) {
					setBackground(new Color(200,200,200));
				}
			}

			if (isSide(value, left)) {
				setHorizontalAlignment(JLabel.LEFT);
			} else if(isSide(value,right)) {
				setHorizontalAlignment(JLabel.RIGHT);
			} else {
				setHorizontalAlignment(JLabel.CENTER);
			}

			if (getErroredRow() == row && value.toString().indexOf(':') >= 0) {
				setOpaque(true);
				setBackground(Color.red);
			}

			setText(value.toString());
			return this;
	   }

		private boolean isSide(Object value, String[] side) {
			for(int i = 0; i < side.length; i++)
				if(side[i].equals(value))
					return true;
			
			return false;
		}
    }
	
	public void displayMessage(String message) {
		if(panel != null) panel.setErrorMessage(message);
	}
	
	
	private class CellEditor extends DefaultCellEditor{

		private static final long serialVersionUID = 6254570916920558674L;
		private String _valueWhenEntered = "";
		private JTextField _field = null;

		public CellEditor(JTextField tf) {
			super(tf);
			_field = tf;
		}


		/**
		 * override DefaultCellEditor getCellEditorValue method
		 */
		public Object getCellEditorValue() {
			// If ':' occurs, this is a Time; otherwise an Integer
			if (_valueWhenEntered.indexOf(':') < 0) {
				return new Integer(_field.getText());
			}
			else {
				return _field.getText();
			}
		}

		/**
		 * override DefaultCellEditor getTableCellEditorComponent method
		 */
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			// Save value when entered to validate against when editing stops
			_valueWhenEntered = value.toString();
			return super.getTableCellEditorComponent(table, value, isSelected, row, column);
		}

		/**
		 * override DefaultCellEditor stopCellEditing method
		 */
		public boolean stopCellEditing() {
			displayMessage("");
			// If no changes in value, cancel cell editing to prevent
			// cell from "appearing" to have changed.
			if (_valueWhenEntered.equals(_field.getText())) {
				super.cancelCellEditing();
				return true;
			}
			else if (_valueWhenEntered.indexOf(':') < 0) {
				// validate the Integer entered
				try {
					if (_field.getText().length() > 3) {
						displayMessage("Capacity limited to 3 digits, please fix");
						return false;
					}
					else if (_field.getText().indexOf('-') >= 0) {
						displayMessage("Capacity must be non-negative, please fix");
						return false;
					}
					
					Integer.parseInt(_field.getText());
				} catch (NumberFormatException e) {
					displayMessage("Capacity value format error, please fix");
					return false;
				}
			}
			else {
				// validate the Time entered
				boolean error = false;
				try {
					// Properly formatted Time must be exactly 8 characters
					if (_field.getText().length() != 8) {
						error = true;
					}
					StringTokenizer st = new StringTokenizer(_field.getText(), ":");
					// Check hours
					String s = st.nextToken();
					int value = Integer.parseInt(s);
					if (s.length() != 2 || value < 0 || value > 23) {
						error = true;
					}
					// Check minutes
					s = st.nextToken();
					value = Integer.parseInt(s);
					if (s.length() != 2 || value < 0 || value > 59) {
						error = true;
					}
					// Check seconds
					s = st.nextToken();
					value = Integer.parseInt(s);
					if (s.length() != 2 || value < 0 || value > 59) {
						error = true;
					}
				} catch (Throwable e) {
					error = true;
				}
				if (error) {
					displayMessage("Time format error <hh:mm:dd>, please fix");
					return false;
				}
			}
			return super.stopCellEditing();
		}
	}
	
}
