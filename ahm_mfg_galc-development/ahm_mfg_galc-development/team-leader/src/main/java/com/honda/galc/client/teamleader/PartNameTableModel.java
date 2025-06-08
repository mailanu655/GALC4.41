package com.honda.galc.client.teamleader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import com.honda.galc.client.ui.component.ComboBoxCellEditor;
import com.honda.galc.client.ui.component.ComboBoxCellRender;
import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.enumtype.PartNameVisibleType;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.service.ServiceFactory;
/**
 * 
 * <h3>PartNameTableModel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> table model for part name </p>
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
 * <TD>YX</TD>
 * <TD>05.06.2014</TD>
 * <TD>0.1</TD>
 * <TD>SR30946</TD>
 * <TD>Add timestamp for any changes of the parts'lot control rules in part Panel</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author ?
 * @since ?
 */
/**
 * 
 * @author Gangadhararao Gadde
 * @date Oct 04, 2016
 * 
 */
public class PartNameTableModel extends SortableTableModel<PartName> {

	
	private static final long serialVersionUID = 1L;
	private static String parentProductType = "";
	//Data format for update column
	private static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	//BCC Added Part Confirm at AF Off column
/*	public PartNameTableModel(JTable table,List<PartName> items) {
		super(items, new String[] {"#","Part Name","Label"},table);
	}*/

	public PartNameTableModel(JTable table,boolean isPartConfirm,List<PartName> items,String productType) {
		super(items,isPartConfirm ? 
				new String[] {"#","Part Name","Label","Update","Part Visible","Repair Check","Part Confirm","Subproduct Type"}: 
				new String[] {"#","Part Name","Label","Update","Part Visible","Repair Check"},	
				table);
		parentProductType = productType;
        if (isPartConfirm) { setSubProductTypeComboCell();}		
        setPartVisibleComboCell();
	}
	
	private void setSubProductTypeComboCell() {
		Object[] subProducts = getSubProducts().toArray();
		TableColumn col = table.getColumnModel().getColumn(7);
		col.setCellEditor(new ComboBoxCellEditor(subProducts,false));
		col.setCellRenderer(new ComboBoxCellRender(subProducts));
	}
	
	private void setPartVisibleComboCell() {
		Object[] partVisibleTypes = PartNameVisibleType.values();
		TableColumn col = table.getColumnModel().getColumn(4);
		col.setCellEditor(new ComboBoxCellEditor(partVisibleTypes,false));
		col.setCellRenderer(new ComboBoxCellRender(partVisibleTypes));
	}
	

	private TreeSet<String> getSubProducts() {
		ProductTypeDao productType = ServiceFactory.getDao(ProductTypeDao.class);
		List<ProductTypeData> productTypeList = productType.findAll();
		
		TreeSet<String> productTypeNameList = new TreeSet<String>();
		productTypeNameList.add("");
		for(ProductTypeData productTypeData : productTypeList) {
			if ((productTypeData.getProductTypeName() != null && !productTypeData.getProductTypeName().equalsIgnoreCase("")) && 
					!productTypeData.getProductTypeName().equalsIgnoreCase(ProductType.FRAME.name()) && //Frame can never be a subproduct.
					parentProductType.equalsIgnoreCase(productTypeData.getOwnerProductTypeName()) || productTypeData.getProductTypeName().equalsIgnoreCase(ProductType.MBPN.name())){   
				productTypeNameList.add(productTypeData.getProductTypeName());
			}
		}
		
		return productTypeNameList;
	}

	public PartNameTableModel(JTable table,List<PartName> items,boolean partNameOnly) {
		super(items, new String[] {"#","Part Name"},table);
	}
	
	public boolean isCellEditable (int row, int column){
        // label column is editable
		return column > 1 && (column!=3 || column!=7);
    }
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		PartName partName = getItem(rowIndex);
		
        //BCC 9/27 Add case 3
		
        switch(columnIndex) {
            case 0: return rowIndex + 1;
            case 1: return partName.getPartName();
            case 2: return partName.getWindowLabel();
            case 3: 
            	//SR30946 add timestamp
            	Date latest = partName.getLotControlUpdateTimestamp();
            	return latest==null?"":FORMAT.format(latest);
            case 4: return partName.getPartVisibleType();
            case 5: 
            	return Boolean.valueOf(partName.getRepairCheck() == 1? true : false);
            case 6: 
            	return Boolean.valueOf(partName.getPartConfirmCheck() == 1? true : false);
            case 7: return partName.getSubProductType();
        }
        return null;
    }

	/*
	 * BCC 9/28/11: Added check for column changed. Was just calling setWindowLabel
	 */
	public void setValueAt(Object value, int row, int column) {
		if(row >= getRowCount() || column <2 ||column==3) return;
		super.setValueAt(value, row, column);
		PartName partName = getItem(row);
		if(column == 2)
			partName.setWindowLabel(value == null ? null : value.toString());
	    else if (column == 4){
			partName.setPartVisible(((PartNameVisibleType)value).getId());
		}
	    else if (column == 5) {
		    int val = ((Boolean) value == true ? 1 : 0);
			partName.setRepairCheck(val);
		}
		else if (column == 6) {
		    int val = ((Boolean) value == true ? 1 : 0);
			partName.setPartConfirmCheck(val);
		} else if (column == 7) {
			if (value == null || value.toString().equalsIgnoreCase("")) {
				partName.setSubProductType(null);
			} else {
				partName.setSubProductType(value.toString());
			}
		}
		this.fireTableCellUpdated(row, column);
	}

	public boolean hasPartName(String name) {
		for (PartName partName : getItems()) {
			if(partName.getPartName().equals(name)) return true;
		}
		return false;
	}
	
	
	public Class<?> getColumnClass(int columnIndex) {
		return ((columnIndex == 5 || columnIndex ==6) ? Boolean.class : super.getColumnClass(columnIndex));
	}

}
