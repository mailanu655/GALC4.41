/**
 * 
 */
package com.honda.galc.client.schedule;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.entity.product.ProductPriorityPlan;

/**
 * @author Subu Kathiresan
 * @date Jan 15, 2013
 */
public class ProductTableModel extends BaseTableModel<ProductPriorityPlan> {

	private static final long serialVersionUID = 7316204068613773739L;
	
	private final static String[] columns =  {"Seq", "Order Number", "Product", "Container", "Position"};

	public ProductTableModel(List<ProductPriorityPlan> items, JTable table) {
		super(items, columns, table);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= getRowCount()) return null;
		ProductPriorityPlan product = getItems().get(rowIndex);

		switch(columnIndex){
		case 0: return product.getId().getTrackingSequenceNo();
		case 1: return product.getOrderNo();
		case 2: return product.getProductId();
		case 3: return product.getContainerId();
		case 4: return product.getContainerPos();
		default:
			return super.getValueAt(rowIndex, columnIndex);
		}
	}
}