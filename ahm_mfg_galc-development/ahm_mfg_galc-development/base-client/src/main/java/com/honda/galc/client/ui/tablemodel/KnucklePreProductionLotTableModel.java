package com.honda.galc.client.ui.tablemodel;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.entity.product.PreProductionLot;

public class KnucklePreProductionLotTableModel extends BaseTableModel<PreProductionLot> {

	private static final long serialVersionUID = 1L;
	
	private PreProductionLot currentProductLot;
	
	public KnucklePreProductionLotTableModel(JTable table,List<PreProductionLot> items) {
		super(items, new String[] {"Plant","Production Lot","KD Lot", "Lot Size", "Product Spec Code","Part Number","Part Mark"},table);

		
		for(int i= 0; i<getColumnCount();i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(new Renderer());
		}
		
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		
		setAlignment(JLabel.CENTER);
		
		pack();
		
	}
	
	public KnucklePreProductionLotTableModel(JTable table,boolean noHighlight, List<PreProductionLot> items) {
		super(items, new String[] {"Plant","Production Lot","KD Lot", "Lot Size", "Product Spec Code","Part Number","Part Mark"},table);

		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		
		setAlignment(JLabel.CENTER);
		
		pack();
		
	}

	public KnucklePreProductionLotTableModel(JTable table,List<PreProductionLot> items,boolean noPartMark) {
		
		super(items, new String[] {"Plant","Production Lot","KD Lot", "Lot Size", "Product Spec Code"},table);

		for(int i= 0; i<getColumnCount();i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(new Renderer());
		}
		
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		
		setAlignment(JLabel.CENTER);
		
		pack();
		
	}
	
	public KnucklePreProductionLotTableModel(JTable table,List<PreProductionLot> items,boolean noPartMark,boolean noPack) {
		
		super(items, new String[] {"Plant","Production Lot","KD Lot", "Lot Size", "Product Spec Code"},table);

		setColumnWidths(new int[]{30, 200, 200,60, 200});
		
		for(int i= 0; i<getColumnCount();i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(new Renderer());
		}
		
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		
		setAlignment(JLabel.CENTER);

	}
		
	public Object getValueAt(int rowIndex, int columnIndex) {
        
		PreProductionLot preProductionLot = getItem(rowIndex);
        
        switch(columnIndex) {
            case 0:return preProductionLot.getProductionLot().substring(5,6);
            case 1: return preProductionLot.getProductionLot();
            case 2: return preProductionLot.getKdLot();
            case 3: return preProductionLot.getLotSize();
            case 4: return preProductionLot.getProductSpecCode();
            case 5: return preProductionLot.getPartNumber();
            case 6: return preProductionLot.getPartMark();
        }
        
        return null;
    }
	
	public List<PreProductionLot> moveUp() {
		int selectedRows[] = table.getSelectedRows();
		if(selectedRows.length == 0) return new ArrayList<PreProductionLot>();
		return moveUp(selectedRows[0],selectedRows[selectedRows.length -1]);
			
	}
	
	public List<PreProductionLot> moveDown() {
		
		int selectedRows[] = table.getSelectedRows();
		if(selectedRows.length == 0 ) return new ArrayList<PreProductionLot>();
		return moveDown(selectedRows[0],selectedRows[selectedRows.length -1]);
			
	}

	
	public List<PreProductionLot> moveUp(int startRow,int endRow) {
		
		List<PreProductionLot> changedProdLots = new ArrayList<PreProductionLot>();
		
		int firstSelectedRow = getFirstRowWithSameKdLot(startRow);
		
		if(firstSelectedRow <= 0) return changedProdLots;
		
		int lastSelectedRow = getLastRowWithSameKdLot(endRow);
		
		int firstRow = getFirstRowWithSameKdLot(firstSelectedRow -1 );
		int lastRow = getLastRowWithSameKdLot(firstSelectedRow -1);
		
		return exchange(firstRow,lastRow,firstSelectedRow, lastSelectedRow);
		
	}
	
	public List<PreProductionLot> moveDown(int startRow,int endRow) {
		
		List<PreProductionLot> changedProdLots = new ArrayList<PreProductionLot>();
		
		int firstSelectedRow = getFirstRowWithSameKdLot(startRow);
		
		int lastSelectedRow = getLastRowWithSameKdLot(endRow);
		if(lastSelectedRow + 1 >=getRowCount()) return changedProdLots;
		
		
		int firstRow = getFirstRowWithSameKdLot(lastSelectedRow +1 );
		int lastRow = getLastRowWithSameKdLot(lastSelectedRow +1);
		
		return exchange(firstSelectedRow, lastSelectedRow, firstRow, lastRow);
		
	}
	
	private List<PreProductionLot> exchange(int first1,int last1,int first2,int last2) {
		
		List<PreProductionLot> changedLots = new ArrayList<PreProductionLot>();
		
		PreProductionLot prevLot = getItem(first1-1);

		PreProductionLot firstLot1 = getItem(first1 );
		PreProductionLot lastLot1 = getItem(last1 );
		PreProductionLot firstLot2 = getItem(first2 );
		PreProductionLot lastLot2 = getItem(last2 );
		
		PreProductionLot lastLot = getItem(last2 + 1 );
		
		if(prevLot == null) prevLot = currentProductLot;
		
		if(prevLot != null) {
			prevLot.setNextProductionLot(firstLot2.getProductionLot());
			changedLots.add(prevLot);
		}
		
		lastLot1.setNextProductionLot(lastLot == null ? null :lastLot.getProductionLot());
		changedLots.add(lastLot1);
		
		
		lastLot2.setNextProductionLot(firstLot1.getProductionLot());
		changedLots.add(lastLot2);
		
		List<PreProductionLot> lots = getSelectedProductionLots(first2, last2);
		items.removeAll(lots);
		items.addAll(first1,lots);
		
		return changedLots;
		
	}
	
	private List<PreProductionLot> getSelectedProductionLots(int start, int end) {
		
		List<PreProductionLot> preProdLots = new ArrayList<PreProductionLot>();
		
		for(int i = start; i<end + 1; i++) {
			preProdLots.add(getItem(i));
		}
		
		return preProdLots;
	}
	
	
	public List<PreProductionLot> getSelectedProductionLots() {
		
		int selectedRows[] = table.getSelectedRows();
		if(selectedRows.length == 0 ) return new ArrayList<PreProductionLot>();
		int startRow = selectedRows[0];
		int endRow = selectedRows[selectedRows.length -1];
		
		int firstSelectedRow = getFirstRowWithSameKdLot(startRow);
		int lastSelectedRow = getLastRowWithSameKdLot(endRow);
		
		return getSelectedProductionLots(firstSelectedRow,lastSelectedRow);
		
	}
	

	private boolean isRowSelected(int row) {
		int selectedRow = table.getSelectedRow();
		if(row == selectedRow) return true;
		PreProductionLot selectedProdLot = getItem(selectedRow);
		PreProductionLot currentProdLot = getItem(row);
		
		if(selectedProdLot == null || currentProdLot == null) return false;
		
		return currentProdLot.isSameKdLot(selectedProdLot);
    		
	}
	
	
	private int getFirstRowWithSameKdLot(int row) {
		
		int  firstRow = row;
		PreProductionLot selectedProdLot = getItem(row);
		
		for(int i = row -1 ; i >= 0; i--) {
			
			if(selectedProdLot.isSameKdLot(getItem(i))) firstRow = i;
			else break;
		}	
		return firstRow;
	}
	
	private int getLastRowWithSameKdLot(int row) {
		
		int  lastRow = row;
		PreProductionLot selectedProdLot = getItem(row);
		
		for(int i = row +1 ; i <= getRowCount(); i++) {
			
			if(selectedProdLot.isSameKdLot(getItem(i))) lastRow = i;
			else break;
		}	
		return lastRow;
	}
	
	private boolean isPlant1(int row) {
		PreProductionLot selectedProdLot = getItem(row);
		return (selectedProdLot != null && selectedProdLot.getProductionLot().substring(5,6).equals("1"));
		
	}
	
	private class Renderer extends DefaultTableCellRenderer{
	   
		private static final long serialVersionUID = 1L;
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
			cellRenderer.setHorizontalAlignment(this.getHorizontalAlignment());
			Component comp = 
			   cellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			
			
			// Now set cell background color
			if (column == 0) {
				if(!isPlant1(row) ) comp.setBackground(new Color(0, 255, 255));
			}
			else if(isRowSelected(row)) {
				comp.setBackground(Color.GREEN);
			}
			
			return comp;
	   }
    }

	public PreProductionLot getCurrentProductLot() {
		return currentProductLot;
	}

	public void setCurrentProductLot(PreProductionLot currentProductLot) {
		this.currentProductLot = currentProductLot;
	}
	
	public boolean isFirstKdLotSelected(){
		int[] selectedRows = table.getSelectedRows();
		if(selectedRows.length == 0) return false;
		
		return getFirstRowWithSameKdLot(selectedRows[0]) == 0;
	}
	
	public boolean isSecondKdLotSelected(){
		if(isFirstKdLotSelected()) return false;
		
		int[] selectedRows = table.getSelectedRows();
		if(selectedRows.length == 0) return false;
		
		return getFirstRowWithSameKdLot(getFirstRowWithSameKdLot(selectedRows[0]) -1) == 0;
	}
	
}
