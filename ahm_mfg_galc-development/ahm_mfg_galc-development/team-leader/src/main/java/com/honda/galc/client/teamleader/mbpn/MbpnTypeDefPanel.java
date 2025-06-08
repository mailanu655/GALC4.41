package com.honda.galc.client.teamleader.mbpn;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JSplitPane;

import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.TabbedPanel;

/**
 * 
 * <h3>MbpnTypeDefPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> MbpnTypeDefPanel description </p>
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
 * <TD>P.Chou</TD>
 * <TD>May 26, 2017</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since May 26, 2017
 */
public class MbpnTypeDefPanel extends TabbedPanel {
	private static final long serialVersionUID = 1L;
	private Dimension screenDimension;
	private JSplitPane mainSplitPane = null;
	private MbpnTypeManagementPanel mbpnTypePanel;
	
	public MbpnTypeDefPanel(MainWindow mainWindow) {
		super("Mbpn Type", KeyEvent.VK_M, mainWindow);
		screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
	}

	@Override
	public void onTabSelected() {
		if(isInitialized) 
		{	
			return;
		}
		initComponents();
		isInitialized = true;
	}	

	

	private void initComponents() {
		setLayout(new BorderLayout());
		add(getMainSplitPane(), BorderLayout.CENTER);
		
		
	}

	private Component getMainSplitPane() {
		if(mainSplitPane == null){

			mainSplitPane = new JSplitPane();
			mainSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			mainSplitPane.setDividerSize(1);
			
			MbpnTypeTreePanel  mbpnTypeTree = new MbpnTypeTreePanel();			 

			mainSplitPane.setLeftComponent(mbpnTypeTree);
			mainSplitPane.setRightComponent(getMbpnTypeManagementPanel(mbpnTypeTree));	
			
			new MbpnTypeDefController(window, mbpnTypeTree, getMbpnTypeManagementPanel(mbpnTypeTree));

			Dimension minimumSize = new Dimension((int)screenDimension.getWidth()/3, (int)screenDimension.getHeight());
			mbpnTypeTree.setMinimumSize(minimumSize);
			mainSplitPane.setDividerLocation((int)screenDimension.getWidth()/3); 
			mainSplitPane.setPreferredSize(screenDimension);
		
		}
		return mainSplitPane;
	}

	private MbpnTypeManagementPanel getMbpnTypeManagementPanel(MbpnTypeTreePanel mbpnTypeTree) {
		if(mbpnTypePanel == null)
			mbpnTypePanel = new MbpnTypeManagementPanel(window, mbpnTypeTree);
		
		return mbpnTypePanel;
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}
