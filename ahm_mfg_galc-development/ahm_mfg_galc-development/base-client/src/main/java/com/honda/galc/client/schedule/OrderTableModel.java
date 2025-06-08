/**
 * 
 */
package com.honda.galc.client.schedule;

import java.util.List;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.entity.product.Order;

/**
 * @author Subu Kathiresan
 * @date Jan 15, 2013
 */
public class OrderTableModel extends BaseTableModel<Order> implements ListSelectionListener   {

	private static final long serialVersionUID = -8080353332283127051L;
	
	final static String[] columns =  {"Order Number", "Product Spec Code", "Seq", "Qty"};
	int[] columWidths = {130, 120, 110, 100};

	public OrderTableModel(List<Order> items, JTable table) {
		super(items, columns, table);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= getRowCount()) return null;
		Order order = getItems().get(rowIndex);

		switch(columnIndex){
		case 0: return order.getId().getOrderNo();
		case 1: return order.getProductSpecCode();
		case 2: return order.getPrioritySeq();
		case 3: return order.getProdOrderQty();
		default:
			return super.getValueAt(rowIndex, columnIndex);
		}
	}
	
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
}

