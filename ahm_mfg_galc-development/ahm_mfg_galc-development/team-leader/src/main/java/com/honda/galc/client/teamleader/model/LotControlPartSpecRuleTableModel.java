package com.honda.galc.client.teamleader.model;

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
import com.honda.galc.entity.product.Product;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonPartUtility;

public class LotControlPartSpecRuleTableModel extends SortableTableModel<LotControlPartSpecRule> {
	
	private static final long serialVersionUID = 1L;
	private boolean editable = true;
	private boolean isHeadless = false;

	public LotControlPartSpecRuleTableModel(Boolean isHeadless, JTable table,List<LotControlPartSpecRule> items) {
		super(items, new String[] {"#","Select","Part Id","Part Mask","Part Mark", "Part #","Part Color","Comment",
				"Seq#", "Sub Id", "Part Confirm","Verify","Scan","Unique",
				"Expected Install Time","Instruction Code","Device Id","Strategy"},table);
		setColumnWidths(new int[]{30,50,50, 120, 100, 100, 100, 100, 30, 50, 80, 60, 100, 60, 100,100, 100, 350});
		this.isHeadless = isHeadless;
		setSubIdComboBoxCell();
		setPsnScanComboBoxCell();
		setStrategyIdComboBoxCell();
	}
	
	public LotControlPartSpecRuleTableModel(JTable table,List<LotControlPartSpecRule> items) {
		this(false, table, items);
	}

	public LotControlPartSpecRuleTableModel(boolean isHeadless, JTable table, List<LotControlPartSpecRule> selectedRules, boolean editable) {
		this(isHeadless, table, selectedRules);
		this.editable = editable;
	}

	public LotControlPartSpecRuleTableModel(JTable table, List<LotControlPartSpecRule> selectedRules, boolean editable) {
		this(false, table, selectedRules);
		this.editable = editable;
	}

	public boolean isCellEditable (int row, int column){
        // label column is editable
		if(column == 0 || column == 2 || column ==3||column == 4|| column == 5 || column == 6 || column == 7) {return false;}
		
		return editable ;
    }
	
	 /**
     *  Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
     *
     *  @param columnIndex  the column being queried
     *  @return the Object.class
     */
    public Class<?> getColumnClass(int columnIndex) {
    	if(columnIndex ==1 || columnIndex ==10 || columnIndex == 11 || columnIndex == 13 ) return Boolean.class;
    	return Object.class;
    }

	public Object getValueAt(int rowIndex, int columnIndex) {
        
		LotControlPartSpecRule rule = getItem(rowIndex);
        
        switch(columnIndex) {
            case 0: return rowIndex + 1;
            case 1: return rule.isSelect();
            case 2: return rule.getPartSpec().getId().getPartId();
            case 3: return getPartSerialNumberMaskDisplayName(rule.getPartSpec().getPartSerialNumberMask());
            case 4: return rule.getPartSpec().getPartMark();
            case 5: return rule.getPartSpec().getPartNumber();
            case 6: return StringUtils.isBlank(rule.getPartSpec().getPartColorCode()) ? "*" : rule.getPartSpec().getPartColorCode();
            case 7: return rule.getPartSpec().getComment();
            case 8: return rule.getSequenceNumber();
            case 9: return rule.getSubId();
            case 10: return rule.isPartConfirmFlag();
            case 11: return rule.getVerificationFlag();
            case 12: return rule.getSerialNumberScanType();
            case 13: return rule.getSerialNumberUniqueFlag();
            case 14: return rule.getExpectedInstallTime();
            case 15: return rule.getInstructionCode();
            case 16: return rule.getDeviceId();
            case 17: return getStrategyDisplayName(rule);
           
        }
        return null;
    }

	private Object getStrategyDisplayName(LotControlPartSpecRule rule) {
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
		if(row >= getRowCount()) return;
		super.setValueAt(value, row, column);
		LotControlPartSpecRule rule = getItem(row);
		try{
			if(column == 1) rule.setSelect((Boolean)value);
			else if(column == 8) rule.setSequenceNumber(parseInt(value.toString()));
			else if(column == 9) rule.setSubId((String)value);
			else if(column == 10) rule.setPartConfirmFlag((Boolean)value);
			else if(column == 11) rule.setVerificationFlag((Boolean)value);
			else if(column == 12) rule.setSerialNumberScanFlag(((PartSerialNumberScanType)value).getId());
			else if(column == 13) rule.setSerialNumberUniqueFlag((Boolean)value);
			else if(column == 14) rule.setExpectedInstallTime(parseInt((String)value));
			else if(column == 15){
				rule.setInstructionCode(parseString((String)value,3));
			}
			else if(column == 16)rule.setDeviceId((String)value);
			else if(column == 17)rule.setStrategy(value == null ? null : getStrategyType(value.toString()));
			
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
		TableColumn col = table.getColumnModel().getColumn(9);
		col.setCellEditor(new ComboBoxCellEditor(subIds,true));
		col.setCellRenderer(new ComboBoxCellRender(subIds));
	}

	private void setPsnScanComboBoxCell() {
		Object[] scanTypes = PartSerialNumberScanType.values(isHeadless);
		TableColumn col = table.getColumnModel().getColumn(12);
		col.setCellEditor(new ComboBoxCellEditor(scanTypes,true));
		col.setCellRenderer(new ComboBoxCellRender(scanTypes));
		
	}
	
	private void setStrategyIdComboBoxCell() {
		Object[] strategyIds = getStrategyTypes().toArray();
		TableColumn col = table.getColumnModel().getColumn(17);
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

	private String getPartSerialNumberMaskDisplayName(String partSerialNumberMask){
		
		String amask = StringUtils.replace(partSerialNumberMask,"<<"+CommonPartUtility.WILD_CARD_MULTI_CHARS+">>",String.valueOf(CommonPartUtility.WILD_CARD_MULTI_CHARS));
		 amask = StringUtils.replace(amask,"<<"+CommonPartUtility.WILD_CARD_ONE_ALPHANUMERIC+">>",String.valueOf(CommonPartUtility.WILD_CARD_ONE_ALPHANUMERIC));
		 amask = StringUtils.replace(amask,"<<"+CommonPartUtility.WILD_CARD_ONE_CHAR+">>",String.valueOf(CommonPartUtility.WILD_CARD_ONE_CHAR));
		 amask = StringUtils.replace(amask,"<<"+CommonPartUtility.WILD_CARD_ONE_NUMBER+">>",String.valueOf(CommonPartUtility.WILD_CARD_ONE_NUMBER));
		 amask = StringUtils.replace(amask,"<<"+CommonPartUtility.WILD_CARD_ONE_ANYTHING+">>",String.valueOf(CommonPartUtility.WILD_CARD_ONE_ANYTHING));
		return amask;
	}
}
