package com.honda.galc.client.ui.tablemodel;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.data.MbpnDef;
import com.honda.galc.entity.product.BuildAttribute;

public class BuildAttributeTableModel extends SortableTableModel<BuildAttribute> {

	private static final long serialVersionUID = 1L;
	private boolean isProductSpec;
	private static String[] specCodeHeader = {"#","Year","Model", "Model Type", "Option","Ext Color", "Int Color", "Attribute Name","Sub Id","Attribute Value","Description"};
	private static String[] mbpnHeader = {"#","Main No","Class No", "ProtoType Code", "Type No","Supplementary No", "Target No", "Hes Color", "Attribute Name", "Sub Id","Attribute Value","Description"};
	
	public BuildAttributeTableModel(JTable table,boolean isProductSpec, List<BuildAttribute> items) {
		super(items, isProductSpec ? specCodeHeader : mbpnHeader, table);
		this.isProductSpec = isProductSpec;
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
        
		BuildAttribute item = getItem(rowIndex);
        return isProductSpec ? getProductSpecValue(rowIndex, columnIndex, item) :
        	                   getMbpnValue(rowIndex, columnIndex, item);
    }
	
	private Object getMbpnValue(int rowIndex, int columnIndex, BuildAttribute item) {
		switch(columnIndex) {
		case 0: return rowIndex + 1;
		case 1: return MbpnDef.MAIN_NO.getValue(item.getProductSpecCode());
		case 2: return MbpnDef.CLASS_NO.getValue(item.getProductSpecCode());
		case 3: return MbpnDef.PROTOTYPE_CODE.getValue(item.getProductSpecCode());
		case 4: return MbpnDef.TYPE_NO.getValue(item.getProductSpecCode());
		case 5: return MbpnDef.SUPPLEMENTARY_NO.getValue(item.getProductSpecCode());
		case 6: return MbpnDef.TARGET_NO.getValue(item.getProductSpecCode());
		case 7: return MbpnDef.HES_COLOR.getValue(item.getProductSpecCode());
		case 8: return item.getId().getAttribute();
		case 9: return item.getId().getSubId();
		case 10: return item.getAttributeValue();
		case 11: return item.getAttributeDescription();
		}
		return null;
	}



	private Object getProductSpecValue(int rowIndex, int columnIndex, BuildAttribute item) {
		switch(columnIndex) {
            case 0: return rowIndex + 1;
            case 1: return item.getModelYearCode();
            case 2: return item.getModelCode();
            case 3: return item.getModelTypeCode();
            case 4: return item.getModelOptionCode();
            case 5: return item.getExtColorCode();
            case 6: return item.getIntColorCode();
            case 7: return item.getId().getAttribute();
            case 8: return item.getId().getSubId();
            case 9: return item.getAttributeValue();
            case 10: return item.getAttributeDescription();
        }
        return null;
	}
	
	public boolean isProductSpec() {
		return isProductSpec;
	}
}
