package com.honda.galc.client.teamleader;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.data.ProductSpecCodeDef;
import com.honda.galc.entity.product.ProductSpecCode;

/**
 * 
 * <h3>ProductSpecCodeTableModel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductSpecCodeTableModel description </p>
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
 * <TD>Mar 31, 2012</TD>
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
 * @since Mar 31, 2012
 */
public class ProductSpecCodeTableModel  extends SortableTableModel<ProductSpecCode>{
	private static final long serialVersionUID = 1L;
	final static String[] columns =  new String[] {
		"#","Spec Code", "Product Type", "Year", "Model", "Model Type", "Model Option", "Ext Color", "Int Color", "Description"
	};
	int[] columWidths = {45, 180, 100, 80, 80, 80, 80, 100, 80, 400};
	public ProductSpecCodeTableModel(JTable table,List<ProductSpecCode> items) {
		super(items, columns, table);
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		
		setAlignment(JLabel.CENTER);
		setColumnWidths(columWidths);
	}
	
	public boolean isCellEditable (int row, int column){
		return column > 0 && column != 2 && column != 1;
    }
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= getRowCount()) return null;
		ProductSpecCode specCode = items.get(rowIndex);

		switch(columnIndex) {
		case 0: return rowIndex + 1;
		case 1: return specCode.getId().getProductSpecCode();
		case 2: return specCode.getId().getProductType();
		case 3: return specCode.getModelYearCode();
		case 4: return specCode.getModelCode();
		case 5: return specCode.getModelTypeCode();
		case 6: return specCode.getModelOptionCode();
		case 7: return specCode.getExtColorCode();
		case 8: return specCode.getIntColorCode();
		case 9: return specCode.getModelYearDescription();

		}
		return null;

	}
	
	public void setValueAt(Object value, int rowIndex, int column) {
		if(!isCellEditable(rowIndex, column)) return;
		super.setValueAt(value, rowIndex, column);
		ProductSpecCode specCode = items.get(rowIndex);
		String valueString = value == null ? null :  value.toString().trim();
		try {
			switch (column) {
			case 1:	specCode.getId().setProductSpecCode(valueString); break;
			case 2:	specCode.getId().setProductType(valueString); break;
			case 3:	specCode.setModelYearCode(valueString); break;
			case 4:	specCode.setModelCode(valueString); break;
			case 5:	specCode.setModelTypeCode(valueString); break;
			case 6:	specCode.setModelOptionCode(valueString); break;
			case 7: specCode.setExtColorCode(valueString); break;
			case 8:	specCode.setIntColorCode(valueString); break;
			case 9:	specCode.setModelYearDescription(valueString); break;

			}
		} catch (Exception e) {
			return;
		}
		this.fireTableCellUpdated(rowIndex, column);
	}

	public ProductSpecCode findItem(ProductSpecCode selected) {
		for (ProductSpecCode item : getItems()) {
			if(item.getId().equals(selected.getId())) return item;
		}
		return null;
	}
}
