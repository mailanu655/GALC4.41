package com.honda.galc.client.teamleader.model;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.entity.product.ProductCarrier;

	/**
	 * 
	 * <h3>ProductCarrierResultModel.java</h3>
	 * <h3> Class description</h3>
	 * <h4> Description </h4>
	 * <p> ProductCarrierResultModel.java description </p>
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
	 * @author KMaharjan - vfc01499
	 * Nov 10, 2014
	 *
	 */

	public class ProductCarrierResultModel extends SortableTableModel<ProductCarrier> {
		private static final long serialVersionUID = 1L;

		public ProductCarrierResultModel(List<ProductCarrier> items, JTable table) {
			super(items, new String[] {"#","Product ID","ON_TIMESTAMP","PROCESS_POINT_ID","CREATE_TIMESTAMP"}, table);
		}
		
		public Object getValueAt(int rowIndex, int columnIndex) {
	        
			ProductCarrier item = getItem(rowIndex);
	        
	        switch(columnIndex) {
	            case 0: return rowIndex + 1;
	            case 1: return item.getId().getProductId();
	            case 2: return item.getId().getOnTimestamp();
	            case 3: return item.getProcessPointId();
	            case 4: return item.getCreateTimestamp();
	        }
	        return null;
	    }
}
