package com.honda.galc.client.common.data;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.entity.product.PartLink;


	public class PartLinkTableModel extends SortableTableModel<PartLink> {
		
		private static final long serialVersionUID = 1L;
		private boolean editable = true;
		
		public PartLinkTableModel(JTable table,List<PartLink> items, boolean editable, boolean autoSize) {
			super(items, new String[] {"#","Part Name","Product Spec Code", "Child Part Name"},table);
			this.setAlignment(JLabel.CENTER);
			if(!autoSize)
				this.setColumnWidths(new int[] {30,500,500,100});
			
			this.editable = editable;
		}
			
		public PartLinkTableModel(JTable table,List<PartLink> items) {
			this(table,items,true);
		}
		
		public PartLinkTableModel(JTable table,List<PartLink> items,boolean editable) {
			this(table, items, editable, false);
		}	
		
		public PartLinkTableModel(JTable table,List<PartLink> items,boolean editable, boolean autosize, boolean partNameAndSpecCodeOnly) {
			super(items, new String[] {"#","Part Name","Product Spec Code"},table);

		}
		
		public boolean isEditable() {
			return editable;
		}

		public void setEditable(boolean editable) {
			this.editable = editable;
		}

		public boolean isCellEditable (int row, int column){
			return editable && column >= 3 && column!=6;
	    }
		
		public Object getValueAt(int rowIndex, int columnIndex) {
	        
	    	PartLink partLink = getItem(rowIndex);
	        
	        switch(columnIndex) {
	            case 0: return rowIndex + 1;
	            case 1: return partLink.getId().getChildPartName();
	            case 2: return partLink.getId().getProductSpecCode();
	            case 3: return partLink.getId().getParentPartName();
	        }
	        return null;
	    }	
}



