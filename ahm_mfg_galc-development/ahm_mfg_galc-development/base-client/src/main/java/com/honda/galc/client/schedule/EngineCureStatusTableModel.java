package com.honda.galc.client.schedule;

import java.awt.Color;
import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.entity.product.Engine;

/**
 * @author Zack chai
 * @date Jan 14, 2014
 */
public class EngineCureStatusTableModel extends SortableTableModel<Engine> {

	private static final long serialVersionUID = 1L;
	public String message = null;
	public Engine _item = null;
	final static String[] columnNames = {"KD Lot", "Engine #", "Cure Time Start", "Wait (min)", "Cure End (calc)", "Status"};
	
	DateFormat simpleDateFormat = null;
	private final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	
	public EngineCureStatusTableModel(List<Engine> items, JTable table) {
		super(items, columnNames, table);
		
		for(int i= 0; i<getColumnCount();i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(new Renderer());
		}
		
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		
		setAlignment(JLabel.CENTER);
		
		pack();
	}
	

	public Object getValueAt(int rowIndex, int columnIndex) {
        
		Engine item = getItem(rowIndex);
        
        switch(columnIndex) {
        	case 0: return item.getKdLotNumber(); 
            case 1: return item.getProductId();
            case 2: 
            	if(item.getCureTimeBegin() != null){
            		DateFormat df = getSimpleDateFormat(DEFAULT_DATE_FORMAT);
            		return df.format(item.getCureTimeBegin());
            	}
            	return "";
            case 3: return item.getCureWaitTimer();
            case 4: return item.getCureTimeEnd();
            case 5: return item.getTrackingStatus();
        }
        return null;
    }
	
	
	private class Renderer extends DefaultTableCellRenderer{
		   
		private static final long serialVersionUID = 1L;
		Color backgroundToSet;
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
			
			Component comp = 
			   cellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			
			if(isSelected)
			{
				comp.setBackground(Color.YELLOW);
				backgroundToSet = table.getBackground();
			}
			else 
			{
				comp.setBackground(Color.LIGHT_GRAY);
			}
			
			return comp;
	   }
    }

	private DateFormat getSimpleDateFormat(String datePattern){
		if(simpleDateFormat != null){
			return simpleDateFormat;
		}
		if(StringUtils.isEmpty(datePattern)){
			datePattern = DEFAULT_DATE_FORMAT;
		}
		simpleDateFormat = new SimpleDateFormat(datePattern);
		return simpleDateFormat;
	}
}
