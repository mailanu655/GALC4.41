package com.honda.galc.device.simulator.client.view.cfg;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.device.simulator.client.view.SimulatorMainPanel;
import com.honda.galc.device.simulator.client.view.data.ConfigValueObject;
import com.honda.galc.device.simulator.data.SimulatorConstants;
import com.honda.galc.device.simulator.data.SimulatorConstants.EiType;
import com.honda.galc.device.simulator.data.SimulatorConstants.ServerType;
import com.honda.galc.net.HttpServiceProvider;



/**
 * <h3>GeneralVarPanel</h3>
 * <h4> </h4>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Sep. 23, 2008</TD>
 * <TD>Version 0.2</TD>
 * <TD></TD>
 * <TD>Initial version.</TD>
 * </TR>
 * </TABLE>
 */

public class GeneralVarPanel extends SimulatorVarPanel {
	private static final long serialVersionUID = 4736166756704006361L;
	private JPanel generalInfoPanel = null;
	private JComboBox serverComboBox;
	private JComboBox EiTypeComboBox = null;
	private JComboBox serverTypeComboBox = null;
	ArrayList<String> items = new ArrayList<String>();


	public GeneralVarPanel() {
		super();

		setLayout(new GridBagLayout());
		setName("General");
		initialize();
	}

	private void initialize() {
		JLabel serverLabel = new JLabel("Server: ");
		serverLabel.setFont(getTextFont());
		JLabel dispatcherHostLabel = new JLabel("EI Server Type: ");
		dispatcherHostLabel.setFont(getFont());
		JLabel eiTypeLabel = new JLabel("EI Type: ");
		eiTypeLabel.setFont(getTextFont());
		
		GridBagConstraints c1 = getConstraint(0, 0, 1);		
		add(serverLabel, c1);
		c1.gridx = 1;
		c1.gridwidth = 2;
		add(getServerComboBox(), c1);
		
		GridBagConstraints c2 = getConstraint(0, 1, 1);		
		add(dispatcherHostLabel, c2);
		c2.gridx = 1;
		c2.gridwidth = 2;
		add(getServerTypeComboBox(), c2);
		
		GridBagConstraints c3 = getConstraint(0, 2, 1);		
		add(eiTypeLabel, c3);
		c3.gridx = 1;
		c3.gridwidth = 2;
		add(getEiTypeComboBox(),c3);
		
		//add a info panel
		//GridBagConstraints c3 = getConstraint(0, 2, 3);
		//add(getGeneralInfoPanel(), c3); TODO

	}
	
	private Component getGeneralInfoPanel() {
		if(generalInfoPanel == null)
		{
			generalInfoPanel = new JPanel();
			JTextArea servNoteTxtArea = new JTextArea();
			servNoteTxtArea.setText(" Database :\n" +
				"");//TODO Database name
			servNoteTxtArea.setPreferredSize(new Dimension(264, 65));
			servNoteTxtArea.setBackground(new Color(238, 238, 238));
			servNoteTxtArea.setForeground(Color.black);
			servNoteTxtArea.setEnabled(true);
			servNoteTxtArea.setFont(new Font("Dialog", Font.PLAIN, 12));
			servNoteTxtArea.setEditable(false);
			generalInfoPanel.add(servNoteTxtArea);
			
		}
		return generalInfoPanel;
	}

	private JComboBox getServerComboBox(){
		if(serverComboBox == null) {
			serverComboBox = new JComboBox();
			serverComboBox.setFont(getFont());
			serverComboBox.setEditable(true);
			serverComboBox.setModel(
					new DefaultComboBoxModel(SimulatorConfig.getInstance().getServerList()));
			serverComboBox.getModel().setSelectedItem(SimulatorConfig.getServerName());
			
		}
		return serverComboBox;
	}
	
	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getEiTypeComboBox() {
		if (EiTypeComboBox == null) {
			EiTypeComboBox = new JComboBox();
			EiTypeComboBox.setFont(getFont());
			Object[] EiTypes = SimulatorConstants.EiType.values();
			DefaultComboBoxModel aModel= new DefaultComboBoxModel(EiTypes);
			EiTypeComboBox.setModel(aModel);
		}
		return EiTypeComboBox;
	}
	
	protected void setUrl(String  url)
	{
		if(items.contains(url))
			items.remove(url);
		
		items.add(0, url);
	}

	public void setEiType(EiType typeString)
	{
	    getEiTypeComboBox().setSelectedItem(typeString);
	}
	
	public String getEiType()
	{
		return getEiTypeComboBox().getSelectedItem().toString();
	}

	@Override
	protected void fromValueObj(ConfigValueObject vo) {
		setEiType(vo.getEiType().equals("OPC") ? SimulatorConstants.EiType.OPC :SimulatorConstants.EiType.EI);
	    setServerType(vo.getServerType());
	}

	private void setServerType(ServerType serverType) {
		getServerTypeComboBox().setSelectedItem(serverType);
	}

	@Override
	protected void saveToValueObj(ConfigValueObject vo) {
		updateServerName();
		vo.setEiType(getEiType());
		vo.setServerType(getServerType());
	}
	
	private ServerType getServerType() {
		return (ServerType)getServerTypeComboBox().getSelectedItem();
	}

	private void updateServerName(){
		String currentServerName = SimulatorConfig.getServerName();
		String currentHostName = SimulatorConfig.hostName;
		
		String serverName = (String)serverComboBox.getSelectedItem();
		if(Arrays.asList(SimulatorConfig.getInstance().getServerList()).contains(serverName)){
			SimulatorConfig.setServerName(serverName);
			SimulatorConfig.hostName = null;
		}else SimulatorConfig.hostName = serverName;
		
		HttpServiceProvider.setUrl(SimulatorConfig.getInstance().getServiceURL());
		try{
			((SimulatorMainPanel)DeviceSimulatorConfigManager.getInstance().getWindow().getCenterPanel()).getDeviceListTable().updateSiteList(null);
		}catch(Exception ex){
			SimulatorConfig.setServerName(currentServerName);
			SimulatorConfig.hostName = currentHostName;
			HttpServiceProvider.setUrl(SimulatorConfig.getInstance().getServiceURL());
			throw new TaskException("Invalid Server URL " + SimulatorConfig.getInstance().getServiceURL());
		}
	}

	public JComboBox getServerTypeComboBox() {
		if(serverTypeComboBox == null){
			serverTypeComboBox = new JComboBox();
			serverTypeComboBox.setFont(getFont());
			serverTypeComboBox.setEditable(true);
			serverTypeComboBox.setModel(
					new DefaultComboBoxModel(SimulatorConstants.ServerType.values()));
			serverTypeComboBox.getModel().setSelectedItem(SimulatorConstants.ServerType.Galc_Server.name());
		}		
		return serverTypeComboBox;
	}

	
}
