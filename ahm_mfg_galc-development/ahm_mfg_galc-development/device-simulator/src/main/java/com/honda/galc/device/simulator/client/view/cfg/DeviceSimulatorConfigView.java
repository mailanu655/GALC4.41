package com.honda.galc.device.simulator.client.view.cfg;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.device.simulator.client.view.data.ConfigValueObject;
import com.honda.galc.device.simulator.data.SimulatorConstants.ServerType;

/**
 * <h3>DeviceSimulatorConfigView</h3>
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


public class DeviceSimulatorConfigView extends JPanel {

	private static final long serialVersionUID = 3516552793031784722L;
	private JPanel configPanel = null;
	private JPanel variablePanel = null;
	private JPanel topPanel = null;
	
	private JLabel cfgTitleLabel = null;
	List <String> items = new ArrayList<String>();
	private List <SimulatorVarPanel> panelist;
	private GeneralVarPanel generalPanel;
	private AppearenceVarPanel apprPanel;
	private AppServClientVarPanel srvClientPanel;
	private SimServerVarPanel simServerPanel;

	/**
	 * This method initializes 
	 * 
	 */
	public DeviceSimulatorConfigView() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setSize(new Dimension(479, 573));
        getConfigPanel();
        getPanels();
        initScreen();
	}
	
	/**
	 * Init panel list to add all panels
	 *
	 */
	private void getPanels() {
		panelist = new ArrayList<SimulatorVarPanel>();
		
		generalPanel = new GeneralVarPanel();
		apprPanel = new AppearenceVarPanel();
		srvClientPanel = new AppServClientVarPanel();
		simServerPanel = new SimServerVarPanel();
		
		panelist.add(generalPanel);
		panelist.add(simServerPanel);
		panelist.add(apprPanel);
		panelist.add(srvClientPanel);
	}
	
	/**
	 * Get a panel by panel name
	 * @param pname
	 * @return
	 */
	private JPanel getPanel(String pname)
	{
		for(JPanel p : panelist)
		{
			if(pname.equals(p.getName()))
				return p;
		}
		return null;
	}

	private void initScreen() {
        fromValueObj();		
	}

	private void fromValueObj() {
		
		ConfigValueObject cfgV = DeviceSimulatorConfigManager.getInstance().getConfig();
		
		for( SimulatorVarPanel p : panelist)
			p.fromValueObj(cfgV);
	}
	
	/**
	 * save configuration information
	 * @return
	 */
	public boolean saveConfiguration() {
		ConfigValueObject cfgV = DeviceSimulatorConfigManager.getInstance().getConfig();
		
		
		try{
			for( SimulatorVarPanel p : panelist)
				p.saveToValueObj(cfgV);
		}catch (Exception e){
			return false;
		}
		
		DeviceSimulatorConfigManager.getInstance().getWindow().setTitle(getWindowsTitle(cfgV));
		return true;
	}

	private String getWindowsTitle(ConfigValueObject cfgV) {
		String env = StringUtils.isEmpty(SimulatorConfig.hostName) ? SimulatorConfig.getServerName() + " - " : "";
		return env + ((cfgV.getServerType() == ServerType.Rest_Service) ? SimulatorConfig.getInstance().getRestServiceURL() : SimulatorConfig.getInstance().getDeviceURL());
	}

	/**
	 * This method initializes configPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	public JPanel getConfigPanel() {
		if (configPanel == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridy = 0;
			gridBagConstraints.gridx = 0;
			configPanel = new JPanel();
			configPanel.setLayout(new BorderLayout());
			configPanel.add(getTopPanel(), BorderLayout.NORTH);
			configPanel.add(getVariablePanel(), BorderLayout.CENTER);
		}
		return configPanel;
	}

	/**
	 * This method initializes variablePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getVariablePanel() {
		if (variablePanel == null) {
			variablePanel = new JPanel();
			variablePanel.setLayout(new BorderLayout());
			variablePanel.setPreferredSize(new Dimension(280, 182));
		}
		return variablePanel;
	}
	
	/**
	 * This method initializes variablePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	public void setVariablePanel(String pName) {
		JPanel vp = getVariablePanel();
		vp.removeAll();
		
		JPanel p = getPanel(pName);
		if(p != null)
		{
			vp.add(p, BorderLayout.NORTH);
		}
		vp.validate();
		vp.repaint();
		
		cfgTitleLabel.setText(pName);
			
	}

	/**
	 * This method initializes topPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTopPanel() {
		if (topPanel == null) {
			cfgTitleLabel = new JLabel();
			cfgTitleLabel.setFont(new Font("Dialog", Font.BOLD, 14));
			cfgTitleLabel.setText("Device Simulator Configuration");
			topPanel = new JPanel();
			topPanel.setLayout(new BorderLayout(10, 10));
			topPanel.add(cfgTitleLabel, BorderLayout.CENTER);
            JSeparator sp = new JSeparator(SwingConstants.HORIZONTAL);
            sp.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
            topPanel.add(sp, BorderLayout.SOUTH);
		}
		return topPanel;
	}

	

}  //  @jve:decl-index=0:visual-constraint="10,10"
