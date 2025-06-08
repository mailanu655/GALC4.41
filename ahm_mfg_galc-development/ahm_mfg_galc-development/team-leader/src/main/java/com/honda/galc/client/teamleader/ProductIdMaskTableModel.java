package com.honda.galc.client.teamleader;

import java.util.List;

import javax.swing.JTable;
import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.entity.product.ProductIdMask;

/**
 * 
 * <h3>ProductIdMaskTableModel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> table model for product Id Mask Map list</p>
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
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Kamlesh Maharjan
 * March 05, 2016
 * 
 */

public class ProductIdMaskTableModel extends BaseTableModel<ProductIdMask> {
	private static final long serialVersionUID = 1L;

	public ProductIdMaskTableModel(JTable table,List<ProductIdMask> items) {
		super(items, new String[] {"#", "Process Point Id", "Product Spec Code","PRODUCT_MASK"}, table);
	}
	
	
	public boolean isCellEditable (int row, int column){
		return column >= 3;
    }
	
	public Object getValueAt(int rowIndex, int columnIndex) {
        
		ProductIdMask productIdMask = getItem(rowIndex);
        
        switch(columnIndex) {
            case 0: return rowIndex + 1;
            case 1: return productIdMask.getId().getProcessPointId();
            case 2: return productIdMask.getProductSpecCode();
            case 3: return productIdMask.getId().getProductIdMask();
        }
        return null;
    }
	
	public void setValueAt(Object value, int row, int column) {
		if(row >= getRowCount() || column <3 ) return;
		super.setValueAt(value, row, column);
	
		this.fireTableCellUpdated(row, column);
	}
	
	public ProductIdMask findProductIdMask(ProductIdMask productIdMask) {
		for (ProductIdMask item : getItems()) {
			if(item.getId().equals(productIdMask.getId())) return item;
		}
		return null;
	}
	
}
