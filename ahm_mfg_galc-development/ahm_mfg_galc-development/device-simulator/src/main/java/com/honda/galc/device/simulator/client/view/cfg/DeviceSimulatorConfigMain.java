package com.honda.galc.device.simulator.client.view.cfg;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

/**
 * <h3>DeviceSimulatorConfigMain</h3>
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

public class DeviceSimulatorConfigMain extends JFrame {
	private static final long serialVersionUID = 7844024014413659540L;
	private JSplitPane mainSplitPane = null;
	private JScrollPane treeView;
	private JPanel configPanel = null;
	private JPanel mainPane = null;
	private JPanel buttonPanel = null;
	private JButton okButton = null;
	private JButton cancelButton = null;
	private DeviceSimulatorConfigView cView = null;

	public DeviceSimulatorConfigMain() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		cView = new DeviceSimulatorConfigView();
		this.setSize(new Dimension(605, 646));
        this.setContentPane(getMainPane());
        this.setTitle("Options");
        
        addListeners();
	}

	private void addListeners(){
		getOkButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveConfiguration();
			}
		});
		getCancelButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelConfiguration();
			}
		});
		
	}
	
	private void saveConfiguration() {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		boolean flag = cView.saveConfiguration();
		setCursor(Cursor.getDefaultCursor());
		if(flag)	this.dispose();
		else JOptionPane.showMessageDialog(this, "The input Server Url is not allowed!");
	}

	private Container getMainPane() {
		if(mainPane == null)
		{
			mainPane = new JPanel();
			mainPane.setLayout(new BorderLayout());
			mainPane.add(getMainSplitPane(), BorderLayout.CENTER);
			mainPane.add(getButtonPanel(), BorderLayout.SOUTH);
		}
			
		return mainPane;
	}
	
	/**
	 * This method initializes actionPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new BorderLayout());
			JSeparator sp = new JSeparator(SwingConstants.HORIZONTAL);
            sp.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
            buttonPanel.add(sp, BorderLayout.NORTH);
            JPanel bPanel = new JPanel();
            bPanel.add(getOkButton(), null);
            bPanel.add(getCancelButton(), null);
			buttonPanel.add(bPanel, BorderLayout.CENTER);
            
		}
		return buttonPanel;
	}

	private JSplitPane getMainSplitPane() {
		if (mainSplitPane == null) {
			mainSplitPane = new JSplitPane();
			mainSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			mainSplitPane.setDividerSize(1);
			
			 ConfigVarListTreePanel vPane = new ConfigVarListTreePanel();			 
			 vPane.setDetailView(cView);
			 mainSplitPane.setLeftComponent(vPane.getTreeView());
             mainSplitPane.setRightComponent(cView.getConfigPanel());	
			
			Dimension minimumSize = new Dimension(100, 50);
			getTreePanel().setMinimumSize(minimumSize);
			getDetailsPanel().setMinimumSize(minimumSize);
			mainSplitPane.setDividerLocation(150); 
			mainSplitPane.setPreferredSize(new Dimension(500, 300));
		}
		return mainSplitPane;
	}
	
	private Component getDetailsPanel() {
		if (configPanel == null) {
			DeviceSimulatorConfigView cView = new DeviceSimulatorConfigView();
			configPanel = cView.getConfigPanel();
		}		
		return configPanel;
	}

	private Component getTreePanel() {
		if (treeView == null) {
		    ConfigVarListTreePanel vPane = new ConfigVarListTreePanel();
			treeView = vPane.getTreeView();
		}		
		return treeView;
	}

	private static void createAndShowGUI() {
        //Set up the content pane.
		DeviceSimulatorConfigMain dsc = new DeviceSimulatorConfigMain();

        //Display the window.
        dsc.pack();
        dsc.setVisible(true);
        dsc.setSize(new Dimension(611, 662));
    }

	/**
	 * This method initializes okButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setPreferredSize(new Dimension(73, 22));
			okButton.setFont(new Font("Dialog", Font.BOLD, 12));
			okButton.setText("OK");
		}
		return okButton;
	}

	/**
	 * This method initializes canncelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setPreferredSize(new Dimension(73, 22));
			cancelButton.setText("Cancel");
		}
		return cancelButton;
	}
	
	private void cancelConfiguration() {
		this.dispose();
	}

	public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
		
		if(args.length > 0) SimulatorConfig.setEnvArgument(args[0]);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
	

}  //  @jve:decl-index=0:visual-constraint="10,10"
