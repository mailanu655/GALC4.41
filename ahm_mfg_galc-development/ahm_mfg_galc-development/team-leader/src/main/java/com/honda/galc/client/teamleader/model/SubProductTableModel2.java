package com.honda.galc.client.teamleader.model;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.teamleader.ProductPrintQueueItem;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.SubProduct;

/** * * 
* 
* @author Gangadhararao Gadde 
* @since May 16, 2016
*/

public class SubProductTableModel2 extends ProductTableModel {

	private static final long serialVersionUID = 1L;
	public String message = null;
	public ProductPrintQueueItem _printItem = null;
	public List <ProductPrintQueueItem> _selectedPrintList= null;
	
	public SubProductTableModel2(List<BaseProduct> items, JTable table) {
		super(items, table);
		

	}
	
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
        
		SubProduct item = (SubProduct) getItem(rowIndex);
        
        switch(columnIndex) {
        	case 0: return rowIndex + 1;
            case 1: return 0;
        	
            case 2: return item.getProductId();
            case 3: return item.getProductSpecCode();
            case 4: return item.getProductionDate() != null ? item.getProductionDate().toString() : "";
           
        }
        return null;
    }

}
