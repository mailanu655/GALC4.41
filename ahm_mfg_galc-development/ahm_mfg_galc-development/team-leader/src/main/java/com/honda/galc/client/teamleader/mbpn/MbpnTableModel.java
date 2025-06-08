package com.honda.galc.client.teamleader.mbpn;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.client.ui.component.ComboBoxCellEditor;
import com.honda.galc.client.ui.component.ComboBoxCellRender;
import com.honda.galc.entity.product.MbpnProductType;

/**
 * 
 * <h3>MbpnTableModel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> MbpnTableModel description </p>
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
 * <TD>May 26, 2017</TD>
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
 * @since May 26, 2017
 */
public class MbpnTableModel extends BaseTableModel<MbpnProductType> {
	private static final long serialVersionUID = 1L;
	private String[] mainNos;
	private String[] ownerMainNos;
	private String[] types;
	private boolean editable = false;
	final static String[] columns =  new String[] {"Product_Type", "Owner_Main_No", "Main_No" };
	public MbpnTableModel(List<MbpnProductType> items,JTable table, String[] mbpnTypes, String[]mainNos, String[] ownerMainNos, boolean editable) {
		super(items, columns, table);
		
		this.mainNos = mainNos;
		this.ownerMainNos = ownerMainNos;
		this.editable = editable;
		types = mbpnTypes;
		
		setProductTypeComboBoxCell();
		setMainNoComboBoxCell();
		setOwnerMainNoComboBoxCell();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= getRowCount()) return null;
		MbpnProductType mbpnType = items.get(rowIndex);

		switch(columnIndex){
			case 0: return mbpnType.getProductType();
			case 1: return mbpnType.getId().getOwnerMainNo();
			case 2: return mbpnType.getId().getMainNo();
		}
		return null;
	}

	public void setValueAt(Object value, int row, int column) {
		super.setValueAt(value, row, column);
		MbpnProductType mbpnType = items.get(row);
		String valueString = value == null ? null : value.toString().trim();
		try{
			switch(column) {
			    case 0: mbpnType.setProductType(valueString);break;
				case 1: mbpnType.getId().setOwnerMainNo(valueString);break;
				case 2: mbpnType.getId().setMainNo(valueString);break;
			}
		}catch(Exception e) {
			return;
		}
		this.fireTableCellUpdated(row, column);
	}   
	
	public boolean isCellEditable (int row, int column){
		return column != 1 && editable;
	}
	
	private void setProductTypeComboBoxCell() {
		if(types == null || types.length == 0) return;
		TableColumn col = table.getColumnModel().getColumn(0);
		col.setCellEditor(new ComboBoxCellEditor(types,editable));
		col.setCellRenderer(new ComboBoxCellRender(types));
	}
	
	private void setOwnerMainNoComboBoxCell() {
		if(ownerMainNos == null) return;
		TableColumn col = table.getColumnModel().getColumn(1);
		col.setCellEditor(new ComboBoxCellEditor(ownerMainNos,editable));
		col.setCellRenderer(new ComboBoxCellRender(ownerMainNos));
		
	}

	private void setMainNoComboBoxCell() {
		if(mainNos == null) return;
		TableColumn col = table.getColumnModel().getColumn(2);
		col.setCellEditor(new ComboBoxCellEditor(mainNos,editable));
		col.setCellRenderer(new ComboBoxCellRender(mainNos));
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	

}
