package com.honda.galc.client.teamleader.mbpn;

import java.util.HashMap;
import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.entity.product.ProductIdNumberDef;

/**
 * 
 * <h3>ProductNumberDefTableModel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductNumberDefTableModel description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>May 30, 2017</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since May 30, 2017
 */
public class ProductNumberDefTableModel extends BaseTableModel<ProductIdNumberDef> {
	private static final long serialVersionUID = 1L;
	final static String[] columns =  new String[] {"DEF_ID", "LENGTH", "MASK"};
	private HashMap<Integer,Boolean> colEditable = new HashMap<Integer,Boolean>();
	
	public ProductNumberDefTableModel(List<ProductIdNumberDef> items, JTable table, boolean editable) {
		super(items, columns, table);
		for (int i = 0; i < columns.length; i++) {
			colEditable.put(i, editable);
		}
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= getRowCount()) return null;
		ProductIdNumberDef prodNumberDef = items.get(rowIndex);

		switch(columnIndex){
			case 0: return prodNumberDef.getProductIdDef();
			case 1: return prodNumberDef.getLength();
			case 2: return prodNumberDef.getMask();
		}
		return null;
	}

	
	public void setValueAt(Object value, int row, int column) {
		super.setValueAt(value, row, column);
		ProductIdNumberDef prodNumberDef = items.get(row);
		String valueString = value == null ? null : value.toString().trim();
		try{
			switch(column) {
			    case 0: prodNumberDef.setProductIdDef(valueString);break;
				case 1: prodNumberDef.setLength(Short.parseShort(valueString));break;
				case 2: prodNumberDef.setMask(valueString);break;
			}
		}catch(Exception e) {
			return;
		}
		this.fireTableCellUpdated(row, column);
	}
	
	public void setColumnEditable(int column, Boolean editable) {
		if (this.colEditable.containsKey(column))
			this.colEditable.replace(column, editable);
	}
	
	public boolean isCellEditable (int row, int column){
		return this.colEditable.get(column);
	}

}
