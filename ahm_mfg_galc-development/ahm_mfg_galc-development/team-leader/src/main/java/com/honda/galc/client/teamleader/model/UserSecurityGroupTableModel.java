package com.honda.galc.client.teamleader.model;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.entity.conf.UserSecurityGroup;

/**
 * 
 * <h3>UserTableModel Class description</h3>
 * <p> UserTableModel description </p>
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
public class UserSecurityGroupTableModel extends BaseTableModel<UserSecurityGroup>{
	private static final long serialVersionUID = 1L;
	final static String[] columns =  new String[] {"Group ID", "Group Name"};

	
	public UserSecurityGroupTableModel(List<UserSecurityGroup> items, JTable table) {

		super(items, columns,table);
		
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= getRowCount()) return null;
		UserSecurityGroup userSecurityGroup = items.get(rowIndex);

		switch(columnIndex){
			case 0: return userSecurityGroup.getSecurityGroupId();
			case 1: return userSecurityGroup.getGroupName();
			default:
				return super.getValueAt(rowIndex, columnIndex);
		}
	}

}
