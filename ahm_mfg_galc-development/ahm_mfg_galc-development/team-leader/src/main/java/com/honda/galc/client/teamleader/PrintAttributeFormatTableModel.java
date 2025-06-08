package com.honda.galc.client.teamleader;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import com.honda.galc.client.ui.component.ComboBoxCellEditor;
import com.honda.galc.client.ui.component.ComboBoxCellRender;
import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.common.exception.DataConversionException;
import com.honda.galc.entity.conf.PrintAttributeFormat;
import com.honda.galc.entity.enumtype.PrintAttributeFormatRequiredType;
import com.honda.galc.entity.enumtype.PrintAttributeType;

/**
 * 
 * <h3>PrintAttributeFormatTableModel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PrintAttributeFormatTableModel description </p>
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
 * <TD>Mar 10, 2017</TD>
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
 * @since Mar 10, 2017
 */
public class PrintAttributeFormatTableModel extends SortableTableModel<PrintAttributeFormat> {
	private static final long serialVersionUID = 1L;
	
	public PrintAttributeFormatTableModel(JTable table,List<PrintAttributeFormat> items) {
		super(items, new String[] {"Seq","Attribute", "AttributeTyp", "Attribute Value",	"Offset", "Length","Required"},table);
		//setColumnWidths(new int[]{30, 200, 220, 280, 40, 40, 100});
		setAttributeTypeComboBoxCell();
		setRequiredComboBoxCell();
		
	}
	
	public boolean isCellEditable (int row, int column){
		return column >= 1;
    }

	public Object getValueAt(int rowIndex, int columnIndex) {
        
		PrintAttributeFormat paFormat = getItem(rowIndex);
		
        switch(columnIndex) {
            case 0: return paFormat.getSequenceNumber();
            case 1: return paFormat.getAttribute();
            case 2: return paFormat.getAttributeType();
            case 3: return paFormat.getAttributeValue();
            case 4: return paFormat.getOffset();
            case 5: return paFormat.getLength();
            case 6: return paFormat.getRequiredType();
        }
        return null;
    }
	
	public void setValueAt(Object value, int row, int column) {
		if(row >= getRowCount() || column < 1) return;
		super.setValueAt(value, row, column);
		PrintAttributeFormat paFormat = getItem(row);
		try{
			switch(column){
			case 0: paFormat.setSequenceNumber(parseInt(value.toString())); break;
			case 1: paFormat.getId().setAttribute((String)value); break;
			case 2: paFormat.setAttributeType((PrintAttributeType)value); break;
			case 3: paFormat.setAttributeValue(value == null ? null: value.toString()); break;
			case 4: paFormat.setOffset(parseInt(value.toString())); break;
			case 5: paFormat.setLength(parseInt(value.toString())); break;
			case 6: paFormat.setRequiredTypeId(value == null ? 0 : ((PrintAttributeFormatRequiredType)value).getId()); break;
			}
			
		}catch(NumberFormatException e) {
			return;
		}catch(DataConversionException e) {
			return;
		}
		this.fireTableCellUpdated(row, column);
	}
	
	
	
	private void setRequiredComboBoxCell() {
		PrintAttributeFormatRequiredType[] values = PrintAttributeFormatRequiredType.values();
		TableColumn col = table.getColumnModel().getColumn(6);
		col.setCellEditor(new ComboBoxCellEditor(values,false));
		col.setCellRenderer(new ComboBoxCellRender(values));
		
	}

	private void setAttributeTypeComboBoxCell() {
		PrintAttributeType[] values = PrintAttributeType.values();
		TableColumn col = table.getColumnModel().getColumn(2);
		col.setCellEditor(new ComboBoxCellEditor(values,false));
		col.setCellRenderer(new ComboBoxCellRender(values));
		
	}
	
	 public Class<?> getColumnClass(int columnIndex){
		 if(columnIndex == 0)
			 return Integer.class;
		 
		 return super.getColumnClass(columnIndex);
	 }

}
