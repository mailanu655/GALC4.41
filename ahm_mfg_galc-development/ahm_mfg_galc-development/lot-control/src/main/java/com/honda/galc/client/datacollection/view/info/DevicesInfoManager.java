package com.honda.galc.client.datacollection.view.info;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.property.PrinterDevicePropertyBean;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.ListModel;
import com.honda.galc.device.IDevice;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.KeyValue;
import com.honda.galc.util.SortedArrayList;

/**
 * 
 * <h3>DevicesInfoManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DevicesInfoManager description </p>
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
 * <TD>Meghana G</TD>
 * <TD>Mar 22, 2011</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD>Modified the code to remove unwanted comments.
 * </TD>
 * </TR>  
 *
 * </TABLE>
 *   
 * @author Meghana G
 * Feb 3, 2011
 *
 */
public class DevicesInfoManager implements ListSelectionListener, ActionListener{

	private DeviceInfoPanel managerPanel;
	private ClientContext context;
	private List<String> list = new ArrayList<String>();
	ListModel<KeyValue<String, IDevice>> listModel;
	String terminal = ApplicationContext.getInstance().getHostName();
	public DevicesInfoManager(ClientContext context) {
		super();
		this.context = context;
		
		initialize();
	}
	
	public DevicesInfoManager(ClientContext context, DeviceInfoPanel managerPanel) {
		super();
		this.context = context;
		this.managerPanel = managerPanel;
		
		initialize();
	}


	private void initialize() {
		
		initComponents();
		
		initConnections();
		initScreen();
	}

    private void initComponents() {
    	getManagerPanel();
	}

	private void initConnections() {
	//	getManagerPanel().getSelectionList().getComponent().addListSelectionListener(this);
		getManagerPanel().getSelectionCombo().getComponent().addActionListener(this);
	}



	public DeviceInfoPanel getManagerPanel() {
		if(managerPanel == null){
			managerPanel = new DeviceInfoPanel("Devices",context);
			managerPanel.initialize();
		}
		return managerPanel;
	}
	
	public void setManagerPanel(DeviceInfoPanel managerPanel) {
		this.managerPanel = managerPanel;
	}
	
	public void refresh(){
		initScreen();
	}
	
	public void initScreen(){
		
		listModel = getDeviceListModel();
		for(int i=0; i<listModel.getSize(); i++)
			list.add(listModel.getElementAt(i).getKey());
		
		ComboBoxModel<String> model = new ComboBoxModel<String>(list);
		//managerPanel.getSelectionList().getLabel().setText("Devices Connected");
		managerPanel.getSelectionCombo().getComponent().setModel(model);
		managerPanel.getSelectionList().getComponent().setCellRenderer(listModel);
	}
	private ListModel<KeyValue<String,IDevice>> getDeviceListModel() {
		SortedArrayList<KeyValue<String,IDevice>> list = getDeviceList();
		return new ListModel<KeyValue<String,IDevice>>(list, "getKey");
	}

	private SortedArrayList<KeyValue<String, IDevice>> getDeviceList() {
		SortedArrayList<KeyValue<String, IDevice>> list = new SortedArrayList<KeyValue<String, IDevice>>("getKey");
		PrinterDevicePropertyBean bean = PropertyService.getPropertyBean(PrinterDevicePropertyBean.class,	terminal);
		
		Hashtable<String,IDevice> devList = DeviceManager.getInstance().getDevices();
			for (String deviceKey: devList.keySet()) {
				IDevice device = devList.get(deviceKey);

				list.add(new KeyValue<String,IDevice>(deviceKey,device));
			}
		return list;
	}

	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource().equals(getManagerPanel().getSelectionList().getComponent())) showDeviceInfo();
		
	}

	private void showDeviceInfo() {
		KeyValue<String,IDevice> selected = null;
	//	KeyValue<String,IDevice> selected = ((KeyValue<String,IDevice>)managerPanel.getSelectionList().getComponent().getSelectedValue());
		for(int i=0; i<listModel.getSize(); i++) {
			if((managerPanel.getSelectionCombo().getComponent().getSelectedItem()).equals(listModel.getElementAt(i).getKey()))
		 selected = listModel.getElementAt(i);
		}
		if(selected != null){
			IDevice device = selected.getValue();
			JPanel panel = managerPanel.refreshPanel(device.getType().value(), device);
			setDetailPanel(panel);
		}
	}

	private void setDetailPanel(JPanel panel) {
    	Component[] components = getManagerPanel().getDetailPanel().getComponents();
    	List<Component> list = Arrays.asList(components);
    	
    	if(!list.contains(panel)){
    		getManagerPanel().getDetailPanel().removeAll();
    		getManagerPanel().getDetailPanel().add(panel, BorderLayout.NORTH);
    	}

    	getManagerPanel().getDetailPanel().validate();
    	getManagerPanel().getDetailPanel().repaint();
		
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(getManagerPanel().getSelectionCombo().getComponent())) showDeviceInfo();
		
	}
}
