package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dao.product.EquipUnitGroupDao;
import com.honda.galc.dao.product.EquipmentGroupDao;
import com.honda.galc.entity.product.EquipUnitGroup;
import com.honda.galc.entity.product.EquipmentGroup;


/**
 * 
 * 
 * <h3> EquipAlarmPriorityPanel Class description</h3>
 * <h4> EquipAlarmPriorityPanel Description </h4>
 * <p>
 * <code>EquipAlarmPriorityPanel</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * 
 * @see
 * @ver 0.1
 * @author is08925
 * Nov 2, 2016
 */
public class EquipAlarmPriorityPanel extends TabbedPanel implements ListSelectionListener, TableModelListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ObjectTablePane<EquipmentGroup> equipGroupList;
	ObjectTablePane<EquipUnitGroup> equipGroupUnitList;
	
	
	public EquipAlarmPriorityPanel(TabbedMainWindow mainWindow) {
		super("Equipment Alarm Priority Panel", KeyEvent.VK_P,mainWindow);
		initComponents();
		addListeners();
		loadData();
	}
	
	public void addListeners() {
		equipGroupList.addListSelectionListener(this);
		equipGroupUnitList.addTableModelListener(this);
	}


	public void initComponents() {
		setLayout(new BorderLayout());
		equipGroupList = createEquipmentGroupListPane();
		equipGroupUnitList = createEquipmentUnitGroupListPane();
		JSplitPane splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
				equipGroupList, equipGroupUnitList);
		splitPanel.setDividerLocation(380);
		splitPanel.setContinuousLayout(true);
		add(splitPanel,BorderLayout.CENTER);

	}
	
	private void loadData(){
		equipGroupList.reloadData(getDao(EquipmentGroupDao.class).findAll());
	}
	
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected() {
		// TODO Auto-generated method stub
		
	}
	
	private ObjectTablePane<EquipmentGroup> createEquipmentGroupListPane() {
		ColumnMappings clumnMappings = ColumnMappings.with("Group Name", "groupName");
		
		ObjectTablePane<EquipmentGroup> pane = new ObjectTablePane<EquipmentGroup>(clumnMappings.get(),false);
		
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pane.getTable().setFont(new Font("sansserif", 1, 18));
		pane.getTable().setRowHeight(28);
		pane.setAlignment(SwingConstants.CENTER);
		return pane;
	}

	private ObjectTablePane<EquipUnitGroup> createEquipmentUnitGroupListPane() {
		ColumnMappings clumnMappings = ColumnMappings.with("Unit ID", "unitId")
				.put("Unit Name", "unitName").put("Priority",Integer.class,"priority",true);
		
		ObjectTablePane<EquipUnitGroup> pane = new ObjectTablePane<EquipUnitGroup>(clumnMappings.get(),true);
		
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pane.getTable().setFont(new Font("sansserif", 1, 18));
		pane.getTable().setRowHeight(28);
		pane.setAlignment(SwingConstants.CENTER);
		return pane;
	}
	
	public void groupNameSelected() {
		EquipmentGroup equipGroup = equipGroupList.getSelectedItem();
		equipGroupUnitList.reloadData(equipGroup.getUnitGroups());
	}

	public void valueChanged(ListSelectionEvent event) {
		if (event.getValueIsAdjusting()) return;
		if(event.getSource().equals(equipGroupList.getTable().getSelectionModel()))
			groupNameSelected();
	}

	public void tableChanged(TableModelEvent tableModelEvent) {
		if(equipGroupUnitList.isEvent(tableModelEvent)) {
			EquipUnitGroup unitGroup = equipGroupUnitList.getItems().get(tableModelEvent.getFirstRow());
			getDao(EquipUnitGroupDao.class).update(unitGroup);
			logUserAction(UPDATED, unitGroup);
		}
		
	}
}
