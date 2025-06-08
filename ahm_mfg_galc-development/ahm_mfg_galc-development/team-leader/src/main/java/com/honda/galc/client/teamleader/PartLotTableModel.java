package com.honda.galc.client.teamleader;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import com.honda.galc.client.ui.component.ComboBoxCellEditor;
import com.honda.galc.client.ui.component.ComboBoxCellRender;
import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.enumtype.PartLotStatus;
import com.honda.galc.entity.product.PartLot;
import com.honda.galc.entity.product.PartLotId;

public class PartLotTableModel extends SortableTableModel<PartLot>{
	private static final long serialVersionUID = 1L;
	final static String[] columns =  new String[] {
		"#","PART_SERIAL_NUMBER", "PART_NAME", "PART_NUMBER", "STATUS", "TOTAL", "REMAINING", "COMMENT", "CREATE_TIMESTAMP", "UPDATE_TIMESTAMP"
	};
	
	Map<PartLotId, PartLot> changedItems = new HashMap<PartLotId, PartLot>();
	
	//int[] columWidths = {40, 300, 260, 260, 110, 130, 120, 280, 180, 180};
	int[] columWidths = {40, 190, 170, 170, 120, 80, 80, 240, 180, 180};
	
	public PartLotTableModel(JTable table,List<PartLot> items) {
		
		super(items, columns, table);
		for(int i= 0; i<getColumnCount();i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(new Renderer());
		}
		
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		
		setAlignment(JLabel.CENTER);
		setColumnWidths(columWidths);
		setStatusIdComboBoxCell();
	}
	

	public boolean isCellEditable (int row, int column){
		return column == 1 || (column >= 3 && column <= 7);
    }

	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= getRowCount()) return null;
		PartLot partLot = items.get(rowIndex);

		switch(columnIndex) {
		case 0: return rowIndex + 1;
		case 1: return partLot.getId().getPartSerialNumber();
		case 2: return partLot.getId().getPartName();
		case 3: return partLot.getId().getPartNumber();
		case 4: return partLot.getStatus();
		case 5: return partLot.getStartingQuantity();
		case 6: return partLot.getCurrentQuantity();
		default:
			return super.getValueAt(rowIndex, columnIndex);

		}

	}
	
	
	public void setValueAt(Object value, int row, int column) {
		if(!isCellEditable(row, column)) return;
		super.setValueAt(value, row, column);
		PartLot partLot = getItem(row);
		PartLot orgPartLot = null;
		try{
			if(column == 1) {
				
				if(!partLot.getId().getPartSerialNumber().equals(value)){
					orgPartLot = getOrgPartLot(partLot);
					partLot.getId().setPartSerialNumber(value.toString());
					changedItems.put(partLot.getId(), orgPartLot);
				}
			}
			else if(column == 3) {
				
				if(!partLot.getId().getPartNumber().equals(value)){
					orgPartLot = getOrgPartLot(partLot);
					partLot.getId().setPartNumber(value.toString());
					changedItems.put(partLot.getId(), orgPartLot);
				}
			}
			else if(column == 4) {
				
				if(partLot.getStatus() != PartLotStatus.valueOf(value.toString())){
					orgPartLot = getOrgPartLot(partLot);
					partLot.setStatus(PartLotStatus.valueOf(value.toString()));
					changedItems.put(partLot.getId(), orgPartLot);
				}
			}
			else if(column == 5){ 
				if(partLot.getStartingQuantity() != (Integer.valueOf(value.toString()))){
					orgPartLot = getOrgPartLot(partLot);
					partLot.setStartingQuantity(Integer.valueOf(value.toString()));
					changedItems.put(partLot.getId(), orgPartLot);
				}
			}
			else if(column == 6) {
				if(partLot.getCurrentQuantity() != (Integer.valueOf(value.toString()))){
					orgPartLot = getOrgPartLot(partLot);
					partLot.setCurrentQuantity(Integer.valueOf(value.toString()));
					changedItems.put(partLot.getId(), orgPartLot);
				}
			}
			else if(column == 7) {
				if(value != null && !value.toString().equals(partLot.getComment())){
					orgPartLot = getOrgPartLot(partLot);
					partLot.setComment((String)value);
					changedItems.put(partLot.getId(), orgPartLot);
				}
			}
			
			table.setSelectionBackground(Color.yellow);
			
		}catch(Exception e) {
		    Logger.getLogger().warn(e, "Exception to set value.");
			return;
		}
		
		this.fireTableCellUpdated(row, column);
	}


	private PartLot getOrgPartLot(PartLot partLot) {
		return  changedItems.containsKey(partLot.getId()) ? changedItems.remove(partLot.getId()) : copyPartLot(partLot);
	}

	private PartLot copyPartLot(PartLot partLot) {
		PartLot newLot = new PartLot();
		PartLotId id = new PartLotId(partLot.getId().getPartSerialNumber(), partLot.getId().getPartName());
		id.setPartNumber(partLot.getId().getPartNumber());
		newLot.setId(id);
		newLot.setComment(partLot.getComment());
		newLot.setCurrentQuantity(partLot.getCurrentQuantity());
		newLot.setStartingQuantity(partLot.getStartingQuantity());
		newLot.setStatus(partLot.getStatus());
		
		
		return newLot;
	}




	private void setStatusIdComboBoxCell() {
		Object [] status = PartLotStatus.values();
		TableColumn col = table.getColumnModel().getColumn(4);
		col.setCellEditor(new ComboBoxCellEditor(status,true));
		col.setCellRenderer(new ComboBoxCellRender(status));
		
	}
	
	public PartLot findPartLot(PartLot selected) {
		for (PartLot item : getItems()) {
			if(item.getId().equals(selected.getId())) return item;
		}
		return null;
	}
	
	public PartLot getOriginalPartLot(PartLotId id){
		return changedItems.get(id);
	}
	
	public void deleteOriginal(PartLotId id){
		if(changedItems.containsKey(id))
			changedItems.remove(id);
	}
	
	public void clearChanged(){
		changedItems.clear();
	}


	public boolean isUpdatingPrimaryKey(PartLotId id) {
		return (changedItems.containsKey(id) && !id.equals(changedItems.get(id).getId()));
		
	}
	
	public boolean isChangedRow(int row) {
		PartLot lot = items.get(row);
		return changedItems.containsKey(lot.getId());
	}

	
	private class Renderer extends DefaultTableCellRenderer{
		   
		private static final long serialVersionUID = 1L;
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
			cellRenderer.setHorizontalAlignment(this.getHorizontalAlignment());
			Component comp = 
			   cellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			
			if(isChangedRow(row)) {
				comp.setBackground(Color.yellow);
			}
			
			return comp;
	   }
    }


	public boolean isSelectedChangedItem() {
		return changedItems.containsKey(getSelectedItem().getId());
	}

	
}
