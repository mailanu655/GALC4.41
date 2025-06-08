package com.honda.galc.client.teamleader.model;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.entity.conf.User;

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
public class UserTableModel extends BaseTableModel<User>{
	private static final long serialVersionUID = 1L;
	final static String[] columns =  new String[] {"User", "Name"};

	
	public UserTableModel(List<User> items, JTable table) {

		super(items, columns,table);
		
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= getRowCount()) return null;
		User user = items.get(rowIndex);

		switch(columnIndex){
			case 0: return user.getUserId();
			case 1: return user.getUserName();
			default:
				return super.getValueAt(rowIndex, columnIndex);
		}
	}

}
