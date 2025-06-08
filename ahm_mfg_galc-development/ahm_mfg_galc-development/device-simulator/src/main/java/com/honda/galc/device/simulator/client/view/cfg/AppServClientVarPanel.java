package com.honda.galc.device.simulator.client.view.cfg;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.honda.galc.dao.conf.OpcConfigEntryDao;
import com.honda.galc.device.simulator.client.view.data.ConfigValueObject;
import com.honda.galc.device.simulator.data.SimulatorConstants;



/**
 * <h3>AppServClientVarPanel</h3>
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
public class AppServClientVarPanel extends SimulatorVarPanel {
	private static final long serialVersionUID = -4096002666144309699L;
	private JPanel clinetVarsPanel = null;
	private JLabel clientTypeLabel = null;
	private JLabel clientModeLabel = null;
	private JComboBox clientTypeComboBox = null;
	private JComboBox clientModeComboBox = null;
	private JPanel useOpcCfgPanel;
	private JComboBox opcInstanceNamesComboBox = null;
	private JCheckBox useOPCCfgForAppSevClientCheckBox = null;
	private List<String> opcInstanceList = new ArrayList<String>();

	public AppServClientVarPanel() {
		super();
		setName("Simulator Application Server Client");
		setLayout(new GridBagLayout());
		
		initialize();
	}

	private void initialize() {
		//get all opc instance names
		try{
			opcInstanceList = getDao(OpcConfigEntryDao.class).findAllOpcInstanceNames();
		}catch(Exception ex){
			
		};
		GridBagConstraints c1 = getConstraint(0, 0, 1);
		add(getUseOpcCfgPanel(), c1);
		
		GridBagConstraints c2 = getConstraint(0, 1, 1);
		c2.anchor = GridBagConstraints.EAST;
		add(getServerClinetVarPanel(), c2);
		
		getUseOPCCfgForAppSevClientCheckBox().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionOnOPCCfg();
			}
		});
	}

	
	private Component getUseOpcCfgPanel() {
		if (useOpcCfgPanel == null) {
			useOpcCfgPanel = new JPanel();
			useOpcCfgPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.EAST;

			useOpcCfgPanel.add(getUseOPCCfgForAppSevClientCheckBox(),c);
			GridBagConstraints c1 = new GridBagConstraints();
			c1.fill = GridBagConstraints.HORIZONTAL;
			useOpcCfgPanel.add(getOpcInstanceNamesComboBox(), c1);
			
		}
		return useOpcCfgPanel;
	}

	/**
	 * This method initializes serverClinetVarPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getServerClinetVarPanel() {
		if (clinetVarsPanel == null) {
			clinetVarsPanel = new JPanel();
			TitledBorder border = new TitledBorder("default");
			clinetVarsPanel.setBorder(border);	
			
			clientTypeLabel = new JLabel();
			clientTypeLabel.setText("Client Type: ");
			clientTypeLabel.setFont(getTextFont());
			
			clientModeLabel = new JLabel();
			clientModeLabel.setText("Client Mode: ");
			clientModeLabel.setFont(getTextFont());
			
			
			clinetVarsPanel.setLayout(new GridBagLayout());
			
			GridBagConstraints c1 = getConstraint(0, 1, 1);
			clinetVarsPanel.add(clientModeLabel, c1);
			
			GridBagConstraints c2 = getConstraint(0, 0, 1);
			clinetVarsPanel.add(clientTypeLabel, c2);
			
			GridBagConstraints c3 = getConstraint(1, 0, 1);
			c3.fill = GridBagConstraints.VERTICAL;
			c3.weightx = 1.0;
			clinetVarsPanel.add(getClientTypeComboBox(), c3);
			
			GridBagConstraints c4 = getConstraint(1, 1, 1);
			c4.fill = GridBagConstraints.VERTICAL;
			c4.weightx = 1.0;
			clinetVarsPanel.add(getClientModeComboBox(), c4);
		}
		return clinetVarsPanel;
	}
	
	/**
	 * This method initializes clientTypeComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getClientTypeComboBox() {
		if (clientTypeComboBox == null) {
			clientTypeComboBox = new JComboBox();
			clientTypeComboBox.setFont(getTextFont());
			clientTypeComboBox.setPreferredSize(getComboBoxSize());
			Object[] clientTypes = SimulatorConstants.ClientType.values();
			DefaultComboBoxModel aModel= new DefaultComboBoxModel(clientTypes);
			clientTypeComboBox.setModel(aModel);
		}
		return clientTypeComboBox;
	}

	/**
	 * This method initializes clientModeComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getClientModeComboBox() {
		if (clientModeComboBox == null) {
			clientModeComboBox = new JComboBox();
			clientModeComboBox.setFont(getTextFont());
			clientModeComboBox.setPreferredSize(getComboBoxSize());
			Object[] clientModes = SimulatorConstants.ClientMode.values();
			DefaultComboBoxModel aModel= new DefaultComboBoxModel(clientModes);
			clientModeComboBox.setModel(aModel);
		}
		return clientModeComboBox;
	}
	
	/**
	 * 
	 *
	 */
	public void actionOnOPCCfg() {
	    boolean b = getUseOPCCfgForAppSevClientCheckBox().isSelected();
		
	    getOpcInstanceNamesComboBox().setEnabled(b);
		getClientModeComboBox().setEnabled(!b);
		getClientTypeComboBox().setEnabled(!b);
		clientModeLabel.setEnabled(!b);
		clientTypeLabel.setEnabled(!b);
	}
	
	/**
	 * This method initializes opcInstanceNameTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JComboBox getOpcInstanceNamesComboBox() {
		if (opcInstanceNamesComboBox == null) {
			opcInstanceNamesComboBox = new JComboBox();
			opcInstanceNamesComboBox.setPreferredSize(new Dimension(120, 25));
			DefaultComboBoxModel aModel= new DefaultComboBoxModel(opcInstanceList.toArray());
			opcInstanceNamesComboBox.setModel(aModel);
		}
		return opcInstanceNamesComboBox;
	}


	/**
	 * This method initializes useOPCCfgForAppSevClientCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getUseOPCCfgForAppSevClientCheckBox() {
		if (useOPCCfgForAppSevClientCheckBox == null) {
			useOPCCfgForAppSevClientCheckBox = new JCheckBox();
			useOPCCfgForAppSevClientCheckBox.setFont(getTextFont());			
			useOPCCfgForAppSevClientCheckBox.setText("Use OPC Configuration Variables");
		}
		return useOPCCfgForAppSevClientCheckBox;
	}
	
    /** 
     * getter and setter
     */
	public void setClientType(int serverClientType) {
		getClientTypeComboBox().setSelectedIndex(serverClientType);
	}

	public void setClientMode(int serverClientMode) {
		getClientModeComboBox().setSelectedIndex(serverClientMode);
	}

	public void setUseOPCCfg(boolean b) {
		getUseOPCCfgForAppSevClientCheckBox().setSelected(b);
	}

	public void setOpcInstanceName(String opcInstanceName) {
		getOpcInstanceNamesComboBox().setSelectedItem(opcInstanceName);
	}
	
	public boolean isUseOPCCfg() {
		return getUseOPCCfgForAppSevClientCheckBox().isSelected();
	}
	
	public String getOpcInstanceName() {
		return (String)getOpcInstanceNamesComboBox().getSelectedItem();
	}
	
	public int getClientMode() {
		return getClientModeComboBox().getSelectedIndex();
	}
	
	public int getClientType() {
		return getClientTypeComboBox().getSelectedIndex();
	}

	protected void fromValueObj(ConfigValueObject vo) {
		setClientType(vo.getServerClientType());
		setClientMode(vo.getServerClientMode());
		setUseOPCCfg(vo.isUseOPCServerClientCfg());
		
		if(opcInstanceList.contains(vo.getOpcInstanceName()))
		{
			setOpcInstanceName(vo.getOpcInstanceName());
			setUseOPCCfg(true);
			
		}
		else
		{
			setOpcInstanceName(null);
			setUseOPCCfg(false);
		}
		
		actionOnOPCCfg(); 		
	}

	protected void saveToValueObj(ConfigValueObject vo) {
		vo.setServerClientMode(getClientMode());
		vo.setServerClientType(getClientType());
		vo.setUseOPCServerClientCfg(isUseOPCCfg());
		vo.setOpcInstanceName( isUseOPCCfg() ? getOpcInstanceName().trim() : null);
		
	}
}
