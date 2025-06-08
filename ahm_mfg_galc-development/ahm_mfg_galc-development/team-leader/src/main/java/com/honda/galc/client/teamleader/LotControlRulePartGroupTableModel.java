package com.honda.galc.client.teamleader;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.entity.product.LotControlRule;
/**
 * 
 * <h3>LotControlRuleGroupMaintPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlRuleGroupMaintPanel description </p>
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
 * <TD>Nov 14, 2016</TD>
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
 * @since Nov 14, 2016
 */

public class LotControlRulePartGroupTableModel extends SortableTableModel<LotControlRule>{

	private static final long serialVersionUID = 1L;
	public LotControlRulePartGroupTableModel(Boolean isHeadless, JTable table,List<LotControlRule> items) {
		super(items, new String[] {"#","Part","Product Spec","Process Point Id"} ,table);
		setColumnWidths(new int[]{30, 140, 140, 120});
	}
	
	public LotControlRulePartGroupTableModel(JTable table,List<LotControlRule> items) {
		this(false, table, items);
	}
	
	public boolean isCellEditable (int row, int column){
		return false;
    }
	
	public Object getValueAt(int rowIndex, int columnIndex) {
        
		LotControlRule rule = getItem(rowIndex);
        
        switch(columnIndex) {
            case 0: return rowIndex + 1;
            case 1: return rule.getId().getPartName();
            case 2: return rule.getId().getProductSpecCode();
            case 3: return rule.getId().getProcessPointId();
           
        }
        return null;
    }
}
