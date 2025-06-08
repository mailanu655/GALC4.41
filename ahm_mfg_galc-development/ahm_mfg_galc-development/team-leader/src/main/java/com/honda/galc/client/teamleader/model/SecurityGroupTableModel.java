package com.honda.galc.client.teamleader.model;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.entity.conf.SecurityGroup;

/**
 * 
 * <h3>SecurityGroupTableModel Class description</h3>
 * <p> SecurityGroupTableModel description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Feb 17, 2011
 *
 *
 */
public class SecurityGroupTableModel extends BaseTableModel<SecurityGroup>{
	private static final long serialVersionUID = 1L;
	final static String[] columns =  new String[] {"Group ID", "Group Name"};

	
	public SecurityGroupTableModel(List<SecurityGroup> items, JTable table) {

		super(items, columns,table);
		
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= getRowCount()) return null;
		SecurityGroup securityGroup = items.get(rowIndex);

		switch(columnIndex){
			case 0: return securityGroup.getSecurityGroup();
			case 1: return securityGroup.getGroupName();
			default:
				return super.getValueAt(rowIndex, columnIndex);
		}
	}

}
