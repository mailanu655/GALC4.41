package com.honda.galc.client.datacollection.view.info;
/**
 * 
 * <h3>DeviceInfoPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DeviceInfoPanel displays all Devices information configured for current process point. </p>
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
 * <TD>Mar 8, 2011</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD>Modified the code to add refresh method.</TD>
 * </TR>  
 * </TABLE>
 *   
 * @author Meghana Ghanekar
 * Feb 3, 2011
 *
 */
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.device.lotcontrol.TorqueSocketDevice;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledListBox;
import com.honda.galc.client.ui.component.SplitInfoPanel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.IDevice;
import com.honda.galc.entity.conf.ComponentProperty;


public class DeviceInfoPanel extends SplitInfoPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private ClientContext context;
	private List<IConfigurableDevice> currentPanels = new ArrayList<IConfigurableDevice>();
	private JPanel currentPanel = null;
	TorqueConfigPanel torquePanel = null;
	protected LabeledComboBox selectionCombo;
	

	public DeviceInfoPanel(String title,ClientContext context) {
		super();
		this.title = title;
		this.context = context;
		initialize();
	}

	public void initialize() {
		try {			
			this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			this.setDividerSize(1);
			setLeftComponent(getSelectionPanel());
			setRightComponent(getDetailPanel());
			setDividerLocation(200);
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception initialize DeviceInfoPanel");
		}

	}

	public JPanel getSelectionPanel(){
		JPanel comboPanel = new JPanel();
		comboPanel.setFont(Fonts.DIALOG_BOLD_14);
		comboPanel.setLayout(new FlowLayout());
		comboPanel.add(getSelectionCombo());
		return comboPanel;
	}
	public JPanel getDetailPanel() {
		if (detailsPanel == null) {
			detailsPanel = new JPanel();
			detailsPanel.setLayout(new BorderLayout());
		}

		return detailsPanel;
	}

	protected JPanel refreshPanel(int type,IDevice device) {
		JPanel panel = null;
		switch(type){
		case 1:
		case 2: 
			panel = getTorqueInfoPanel(device);
			break;
		case 3:
			panel = getLaserInfoPanel(device);
			break;
		case 6 :
			panel = getEIDevicePanel(device);
			break;
		case 7: 
		case 8:
			panel = getPrinterDevicePanel(device);
		default: 
		}
		currentPanel = panel;
		return panel;
	}

	private JPanel getPrinterDevicePanel(IDevice device){
		JPanel panel = new JPanel();
		JPanel comboPanel = new JPanel();
		if(device!= null){
			comboPanel = new PrinterConfigPanel(device, context);
			return comboPanel;
		}

		return panel;
	}
	
	private JPanel getEIDevicePanel(IDevice device) {
		JPanel panel = new JPanel();
		if(device != null){
			JLabel devNameLabel = new JLabel("Device Name");
			JLabel devName = new JLabel(device.getName());
			panel.add(devNameLabel);
			panel.add(devName);
		}
		return panel;
	}

	private JPanel getLaserInfoPanel(IDevice device) {
		JPanel panel = new JPanel();
		if(device != null){
			JLabel devNameLabel = new JLabel("Device Name");
			JLabel devName = new JLabel(device.getName());
			panel.add(devNameLabel);
			panel.add(devName);
		}
		return panel;
	}
	
	private JPanel getTorqueInfoPanel(IDevice device) {
		JPanel panel = new JPanel();
		TorqueSocketDevice socketDevice = null;

		if(device != null){
		socketDevice = (TorqueSocketDevice)device;
			if(socketDevice!=null){
				if(torquePanel == null){
					torquePanel= new TorqueConfigPanel(socketDevice,context);
					currentPanels.add(torquePanel);
				}
				return torquePanel;
			}
		}
		return panel;
	}

	public LabeledListBox getSelectionList() {
		if(selectionList == null){
			selectionList = new LabeledListBox(title);
			selectionList.getLabel().setAlignmentX(CENTER_ALIGNMENT);
			selectionList.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			selectionList.getComponent().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);	
		}
		return selectionList;
	}

	public LabeledComboBox getSelectionCombo() {
		if(selectionCombo == null){
			selectionCombo = new LabeledComboBox(title);
			selectionCombo.getLabel().setAlignmentX(CENTER_ALIGNMENT);
			selectionCombo.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			//selectionCombo.getComponent().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);	
		}
		return selectionCombo;
	}
	protected GridBagConstraints getConstraint(int gridx, int gridy, int gridwidth, double weightx) {
		GridBagConstraints c = new GridBagConstraints();		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 0, 10);
		c.weightx =weightx;

		c.gridx = gridx;
		c.gridy = gridy;
		c.gridwidth = gridwidth;		
		return c;
	}

	private List<ComponentProperty> getDeviceProperties() {
		List<ComponentProperty> properties = new ArrayList<ComponentProperty>();
		for (Iterator<IConfigurableDevice> panelIter = (Iterator<IConfigurableDevice>) currentPanels.iterator(); panelIter.hasNext();) {
			IConfigurableDevice panel = (IConfigurableDevice) panelIter.next();
			properties.addAll(panel.getChangedProperties());	
		}
		return properties;
	}

	public void saveDeviceConfig() {
		if(currentPanel instanceof PrinterConfigPanel)
		{PrinterConfigPanel panel = (PrinterConfigPanel)currentPanel;
			panel.saveDeviceConfig();
		}
		else{
			List<ComponentProperty> properties = getDeviceProperties();
			if (properties.size() > 0 ) {
			context.getDbManager().saveProperties(properties);
			}
		}
		//MessageDialog.showInfo(this, "Device configurations were saved. Please restart client to pick up configuration changes.", "Information");
	}

	public void releaseResources() {
		for (IConfigurableDevice panel: currentPanels){
			if(panel instanceof TorqueConfigPanel){
			   TorqueConfigPanel p = (TorqueConfigPanel)panel;
		       p.releaseResources();	   
			}
		}
	}

	public void refresh(){
		if (currentPanel instanceof TorqueConfigPanel){
			TorqueConfigPanel panel = (TorqueConfigPanel)currentPanel;
			panel.refresh();
		}
		
	}
}
