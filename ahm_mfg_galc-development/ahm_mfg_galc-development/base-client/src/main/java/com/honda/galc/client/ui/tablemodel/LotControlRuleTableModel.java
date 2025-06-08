package com.honda.galc.client.ui.tablemodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.property.CommonTlPropertyBean;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ComboBoxCellEditor;
import com.honda.galc.client.ui.component.ComboBoxCellRender;
import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.common.exception.DataConversionException;
import com.honda.galc.entity.enumtype.PartSerialNumberScanType;
import com.honda.galc.entity.enumtype.StrategyType;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Product;
import com.honda.galc.service.property.PropertyService;
 
public class LotControlRuleTableModel extends SortableTableModel<LotControlRule> {
	
	private static final long serialVersionUID = 1L;
	private boolean editable = true;
	private boolean isHeadless = false;

	public LotControlRuleTableModel(Boolean isHeadless, JTable table,List<LotControlRule> items) {
		super(items, new String[] {"#","Product Spec", "Process Point Id",
				"Seq#", "Part","Sub Id","Part Confirm","Verify","Scan","Unique",
				"Expected Install Time","Instruction Code","Device Id","Strategy","Qics Defect Reqd"},table);
		setColumnWidths(new int[]{30, 120, 100, 30, 170, 50,100, 50, 110, 60, 150, 200, 100, 350, 50});
		this.isHeadless = isHeadless;
		setSubIdComboBoxCell();
		setPsnScanComboBoxCell();
		setStrategyIdComboBoxCell();
	}
	
	public LotControlRuleTableModel(JTable table,List<LotControlRule> items) {
		this(false, table, items);
	}

	public LotControlRuleTableModel(boolean isHeadless, JTable table, List<LotControlRule> selectedRules, boolean editable) {
		this(isHeadless, table, selectedRules);
		this.editable = editable;
	}

	public LotControlRuleTableModel(JTable table, List<LotControlRule> selectedRules, boolean editable) {
		this(false, table, selectedRules);
		this.editable = editable;
	}

	public boolean isCellEditable (int row, int column){
        // label column is editable
		return column == 13 ? editable && (getStrategyTypes().size() > 1) : editable && (column == 3 || column >= 5);
    }
	
	 /**
     *  Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
     *
     *  @param columnIndex  the column being queried
     *  @return the Object.class
     */
    public Class<?> getColumnClass(int columnIndex) {
    	if(columnIndex ==6 || columnIndex == 7|| columnIndex == 9 || columnIndex == 14) return Boolean.class;
    	return Object.class;
    }

	public Object getValueAt(int rowIndex, int columnIndex) {
        
		LotControlRule rule = getItem(rowIndex);
        
        switch(columnIndex) {
            case 0: return rowIndex + 1;
            case 1: return rule.getId().getYMTO();
            case 2: return rule.getId().getProcessPointId();
            case 3: return rule.getSequenceNumber();
            case 4: return rule.getId().getPartName();
            case 5: return rule.getSubId();
            case 6: return rule.isPartConfirm();
            case 7: return rule.isVerify();
            case 8: return rule.getSerialNumberScanType();
            case 9: return rule.isUnique();
            case 10: return rule.getExpectedInstallTime();
            case 11: return rule.getInstructionCode();
            case 12: return rule.getDeviceId();
            case 13: return getStrategyDisplayName(rule);
            case 14: return rule.isQicsDefect();

          
        }
        return null;
    }

	private Object getStrategyDisplayName(LotControlRule rule) {
		String strategyName = StringUtils.trimToEmpty(rule.getStrategy());
		if (strategyName.equals(""))
			return "";
		
		StrategyType type = null;
		try {
			type = StrategyType.valueOf(strategyName);
		} catch (Exception ex) {}
		
		if (type != null) {
			return type.getDisplayName();
		} else { 
			// check if its a legacy strategy. TODO this section needs to be removed after converting
			// all legacy stations to regional code base
			for(StrategyType sType :StrategyType.values()){
				if(strategyName.equalsIgnoreCase(sType.getCanonicalStrategyClassName())) return sType.getDisplayName();
			}
			return "";
		}
	}
	
	public void setValueAt(Object value, int row, int column) {
		if(row >= getRowCount() || column <3 || column == 4) return;
		super.setValueAt(value, row, column);
		LotControlRule rule = getItem(row);
		try{
			if(column == 3) rule.setSequenceNumber(parseInt(value.toString()));
			else if(column == 5) rule.setSubId((String)value);
			else if(column == 6) rule.setPartConfirm((Boolean)value);
			else if(column == 7) rule.setVerify((Boolean)value);
			else if(column == 8) rule.setSerialNumberScanFlag(((PartSerialNumberScanType)value).getId());
			else if(column == 9) rule.setUnique((Boolean)value);
			else if(column == 10) rule.setExpectedInstallTime(parseInt((String)value));
			else if(column == 11){
				rule.setInstructionCode(parseString((String)value,3));
			}
			else if(column == 12)rule.setDeviceId((String)value);
			else if(column == 13)rule.setStrategy(value == null ? null : getStrategyType(value.toString()));
			else if(column == 14)rule.setQicsDefect((Boolean)value);

		}catch(NumberFormatException e) {
			return;
		}catch(DataConversionException e) {
			return;
		}
		this.fireTableCellUpdated(row, column);
	}
	
	private String getStrategyType(String strategyDisplayName) {
		String strategyName = StrategyType.getName(strategyDisplayName);
		
		//TODO This is a stop-gap solution.  Remove the if condition after the legacy stations
		//     have been converted to the regional code-base
		if (strategyName.startsWith("LEGACY")) {
			return StrategyType.valueOf(strategyName).getCanonicalStrategyClassName();
		} else {
			return StrategyType.getName(strategyDisplayName);
		}
	}
	
	private void setSubIdComboBoxCell() {
		Object[] subIds = Product.getSubIds();
		TableColumn col = table.getColumnModel().getColumn(5);
		col.setCellEditor(new ComboBoxCellEditor(subIds,true));
		col.setCellRenderer(new ComboBoxCellRender(subIds));
	}

	private void setPsnScanComboBoxCell() {
		Object[] scanTypes = PartSerialNumberScanType.values(isHeadless);
		TableColumn col = table.getColumnModel().getColumn(8);
		col.setCellEditor(new ComboBoxCellEditor(scanTypes,true));
		col.setCellRenderer(new ComboBoxCellRender(scanTypes));
		
	}
	
	private void setStrategyIdComboBoxCell() {
		Object[] strategyIds = getStrategyTypes().toArray();
		TableColumn col = table.getColumnModel().getColumn(13);
		col.setCellEditor(new ComboBoxCellEditor(strategyIds,false));
		col.setCellRenderer(new ComboBoxCellRender(strategyIds));
	}
	
	private ArrayList<String> getStrategyTypes() {
		CommonTlPropertyBean bean = PropertyService.getPropertyBean(CommonTlPropertyBean.class);
		String types = bean.getStrategyTypes();
		List<String> asList = new ArrayList<String>();
		if (!StringUtils.trimToEmpty(types).equals("")) {
			asList = Arrays.asList(types.split(","));
		}
		return StrategyType.getComboDisplayNames(asList);
	}

	public LotControlRule findRule(LotControlRule rule) {
		for (LotControlRule item : getItems()) {
			if(item.getId().equals(rule.getId())) return item;
		}
		return null;
	}
	
	public boolean hasRule(LotControlRule rule) {
		 return findRule(rule) != null;
	}
	
	private String parseString(String valueString, int length) {
		int len = StringUtils.length(valueString);
		if(len == length || (len < length && len > 0) ) return valueString;
		else {
			if(len == 0){
				MessageDialog.showError(getParentComponent(),"Input cannot be empty");
				throw new DataConversionException("");
			}else{
				MessageDialog.showError(getParentComponent(),"Input must be equal to or less than " + length + " characters");
				throw new DataConversionException("");
			}
		}
	}
}
