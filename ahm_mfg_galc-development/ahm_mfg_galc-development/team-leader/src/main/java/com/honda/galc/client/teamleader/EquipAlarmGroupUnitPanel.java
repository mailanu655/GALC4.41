package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dao.product.EquipUnitFaultDao;
import com.honda.galc.dao.product.EquipUnitGroupDao;
import com.honda.galc.dao.product.EquipmentGroupDao;
import com.honda.galc.entity.product.EquipUnitFault;
import com.honda.galc.entity.product.EquipUnitGroup;
import com.honda.galc.entity.product.EquipmentGroup;

/**
 * 
 * 
 * <h3> EquipAlarmGroupUnitPanel Class description</h3>
 * <h4> EquipAlarmGroupUnitPanel Description </h4>
 * <p>
 * <code>EquipAlarmGroupUnitPanel</code> is ...
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
public class EquipAlarmGroupUnitPanel extends TabbedPanel implements ListSelectionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JButton addButton= new JButton("ADD GROUP");
	private JButton removeButton = new JButton("REMOVE GROUP");
	private JButton moveLeftButton= new JButton("<<");
	private JButton moveRightButton = new JButton(">>");
	
	ObjectTablePane<EquipmentGroup> equipGroupListPane;
	ObjectTablePane<EquipUnitGroup> equipGroupUnitListPane;
	ObjectTablePane<EquipUnitFault> equipUnitFaultListPane;
	
	List<EquipUnitFault> equipUnitFaultList;
	
	public EquipAlarmGroupUnitPanel(TabbedMainWindow mainWindow) {
		super("Equipment Alarm Group Unit Panel", KeyEvent.VK_G,mainWindow);
		initComponents();
		addListeners();
		loadData();
	}

	
	public void initComponents() {
		setLayout(new BorderLayout());
		JSplitPane splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
				createTopPanel(), createBottomPanel());
		splitPanel.setDividerLocation(180);
		splitPanel.setContinuousLayout(true);
		add(splitPanel,BorderLayout.CENTER);
	}
	
	private void addListeners() {
		equipGroupListPane.addListSelectionListener(this);
		moveLeftButton.addActionListener(this);
		moveRightButton.addActionListener(this);
		addButton.addActionListener(this);
		removeButton.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent event) {
		if(event.getSource().equals(moveLeftButton)) moveLeftButtonClicked();
		else if(event.getSource().equals(moveRightButton)) moveRightButtonClicked();
		else if(event.getSource().equals(addButton)) addGroupButtonClicked();
		else if(event.getSource().equals(removeButton)) removeGroupButtonClicked();
	}
	
	private void addGroupButtonClicked() {
		String groupName = MessageDialog.showInputDialog("Input New Group Name", "Group Name",50 , true);
		if(StringUtils.isEmpty(groupName)) return;
		groupName = groupName.trim();
		if(groupNameExist(groupName)) {
			MessageDialog.showError("Group Name " + groupName + " already exists" );
		}else {
			EquipmentGroup eg = new EquipmentGroup();
            eg.setGroupId((short)(getMaxGroupId() + 1));
			eg.setGroupName(groupName);
			eg.setGroupDescription("");
			eg.setFaultCount((short)0);
			getDao(EquipmentGroupDao.class).insert(eg);
			logUserAction(INSERTED, eg);
			equipGroupListPane.reloadData(getDao(EquipmentGroupDao.class).findAll());
		}
	}
	
	private void removeGroupButtonClicked() {
		EquipmentGroup equipGroup = equipGroupListPane.getSelectedItem();
		if(equipGroup == null) return;
		if(equipGroup.getUnitGroups().size() > 0) {
			boolean isOk = MessageDialog.confirm(this, "The selected group contains group units, if you delete the selected group, these units will be deleted, are you sure to proceed?");
			if(isOk) {
				getDao(EquipUnitGroupDao.class).deleteAllForGroup(equipGroup.getId());
				logUserAction("deleted EquipUnitGroup by " + equipGroup.getId());
				equipGroup.setUnitGroups(null);
				getDao(EquipmentGroupDao.class).remove(equipGroup);
				logUserAction(REMOVED, equipGroup);
				equipGroupListPane.clearSelection();
				loadData();
				equipGroupListPane.getTable().getSelectionModel().setSelectionInterval(0, 0);
			}
		}
	}
	
	private boolean groupNameExist(String groupName){
		for(EquipmentGroup eg : equipGroupListPane.getItems()){
			if (eg.getGroupName().equalsIgnoreCase(groupName.trim()))
				return true;
		}
		return false;
	}
	
	private short getMaxGroupId() {
        short n = -1;
        for(EquipmentGroup eg : equipGroupListPane.getItems()) {
            if (eg.getId() > n) n = eg.getId();
        }
        return n;
    }
	
	private void moveLeftButtonClicked() {
		EquipmentGroup equipGroup = equipGroupListPane.getSelectedItem();
		EquipUnitFault unitFault = equipUnitFaultListPane.getSelectedItem();
		if(unitFault == null) return;
		EquipUnitGroup unitGroup = new EquipUnitGroup(equipGroup.getId(),unitFault.getId());
		getDao(EquipUnitGroupDao.class).insert(unitGroup);
		logUserAction(INSERTED, unitGroup);
		loadData();
		groupNameSelected();
	}
	
	private void moveRightButtonClicked() {
		EquipUnitGroup unitGroup = equipGroupUnitListPane.getSelectedItem();
		if(unitGroup == null) return;
		getDao(EquipUnitGroupDao.class).remove(unitGroup);
		logUserAction(REMOVED, unitGroup);
		loadData();
		groupNameSelected();
		
	}
	
	
	private JPanel createTopPanel() {
		equipGroupListPane = createEquipmentGroupListPane();
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(equipGroupListPane,BorderLayout.CENTER);
		panel.add(createGroupButtonPanel(),BorderLayout.EAST);
		return panel;
	}
	
	private JPanel createBottomPanel() {
		equipGroupUnitListPane = createEquipmentUnitGroupListPane();
		equipUnitFaultListPane = createEquipmentUnitFaultListPane();
		JPanel panel = new JPanel(new MigLayout("insets 1", "[grow,fill]"));
		panel.add(equipGroupUnitListPane);
		panel.add(createUnitButtonPanel());
		panel.add(equipUnitFaultListPane);
		
		return panel;
	}
	
	
	
	private JPanel createGroupButtonPanel() {
		JPanel buttonPanel = new JPanel(new MigLayout("insets 50 20 50 20", "[grow,fill]"));
		
			
		buttonPanel.add(addButton,"cell 0 1");
		buttonPanel.add(removeButton,"cell 0 2,gaptop 20");
		return buttonPanel;
	}

	private JPanel createUnitButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.add(moveLeftButton);
		buttonPanel.add(Box.createVerticalStrut(30));
		buttonPanel.add(moveRightButton);
		return buttonPanel;
	}
	
	@Override
	public void onTabSelected() {
		// TODO Auto-generated method stub
		
	}
	
	private void loadData() {
		equipGroupListPane.reloadData(getDao(EquipmentGroupDao.class).findAll());
		equipUnitFaultList = getDao(EquipUnitFaultDao.class).findAll();
	}
	
	private ObjectTablePane<EquipmentGroup> createEquipmentGroupListPane() {
		ColumnMappings clumnMappings = ColumnMappings.with("Group ID", "groupId").put("Group Name", "groupName")
				.put("Group Description","groupDescription").put("Fault Count","faultCount");
		
		ObjectTablePane<EquipmentGroup> pane = new ObjectTablePane<EquipmentGroup>(clumnMappings.get(),false);
		
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pane.getTable().setFont(new Font("sansserif", 1, 18));
		pane.getTable().setRowHeight(28);
		pane.setAlignment(SwingConstants.CENTER);
		return pane;
	}
	
	private ObjectTablePane<EquipUnitGroup> createEquipmentUnitGroupListPane() {
		ColumnMappings clumnMappings = ColumnMappings.with("Unit ID", "unitId")
				.put("Unit Name", "unitName");
		
		ObjectTablePane<EquipUnitGroup> pane = new ObjectTablePane<EquipUnitGroup>(clumnMappings.get(),true);
		
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pane.getTable().setFont(new Font("sansserif", 1, 18));
		pane.getTable().setRowHeight(28);
		pane.setAlignment(SwingConstants.CENTER);
		return pane;
	}
	
	private ObjectTablePane<EquipUnitFault> createEquipmentUnitFaultListPane() {
		ColumnMappings clumnMappings = ColumnMappings.with("Unit ID", "id")
				.put("Unit Name", "unitName");
		
		ObjectTablePane<EquipUnitFault> pane = new ObjectTablePane<EquipUnitFault>(clumnMappings.get(),true);
		
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pane.getTable().setFont(new Font("sansserif", 1, 18));
		pane.getTable().setRowHeight(28);
		pane.setAlignment(SwingConstants.CENTER);
		return pane;
	}


	public void valueChanged(ListSelectionEvent event) {
		if (event.getValueIsAdjusting()) return;
		if(event.getSource().equals(equipGroupListPane.getTable().getSelectionModel()))
			groupNameSelected();
	}
	
	public void groupNameSelected() {
		EquipmentGroup equipGroup = equipGroupListPane.getSelectedItem();
		if(equipGroup == null) return;
		equipGroupUnitListPane.reloadData(equipGroup.getUnitGroups());
		equipUnitFaultListPane.reloadData(getAvailableUnits(equipGroup.getUnitGroups()));
	}
	
	private List<EquipUnitFault> getAvailableUnits(List<EquipUnitGroup> unitGroups) {
		List<EquipUnitFault> availableUnitFaultList = new ArrayList<EquipUnitFault>(equipUnitFaultList);
		for(EquipUnitGroup unitGroup : unitGroups) {
			EquipUnitFault unitFault = getUnitFault(unitGroup);
			if(unitFault != null) availableUnitFaultList.remove(unitFault);
		}
		return availableUnitFaultList;
	}
	
	private EquipUnitFault getUnitFault(EquipUnitGroup unitGroup) {
		for(EquipUnitFault unitFault : equipUnitFaultList) {
			if(unitFault.getId().equals(unitGroup.getUnitId())) return unitFault;
		}
		return null;
	}

}
