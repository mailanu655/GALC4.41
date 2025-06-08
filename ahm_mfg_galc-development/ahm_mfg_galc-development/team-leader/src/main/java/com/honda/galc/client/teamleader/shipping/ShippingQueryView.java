package com.honda.galc.client.teamleader.shipping;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.DefaultWindow;
import com.honda.galc.client.ui.TabbedPanel;

/**
 * 
 * 
 * <h3>ShippingQueryView Class description</h3>
 * <p> ShippingQueryView description </p>
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
 * Mar 16, 2015
 *
 *
 */
public class ShippingQueryView extends ApplicationMainPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tabbedPane = new JTabbedPane();
	
	public ShippingQueryView(DefaultWindow window) {
		super(window);
		initComponents();
		window.pack();
	}
	
	private void initComponents(){
		setLayout(new BorderLayout());
		tabbedPane.addTab("Engine Info Search", new ShippingEngineQueryPanel(getMainWindow()));
		tabbedPane.addTab("KD Lot Info Search", new ShippingKdLotQueryPanel(getMainWindow()));
		tabbedPane.addTab("Trailer Info Search", new ShippingTrailerQueryPanel(getMainWindow(),false));
		tabbedPane.addTab("Trailer History Search", new ShippingTrailerQueryPanel(getMainWindow(),true));
		
		add(tabbedPane,BorderLayout.CENTER);
		
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				Component component = tabbedPane.getSelectedComponent();
				if (component instanceof TabbedPanel) {
					TabbedPanel selectedPanel = (TabbedPanel) component;
					selectedPanel.onTabSelected();
				}
			}
		});
		tabbedPane.setSelectedIndex(0);
	}
	
	public void actionPerformed(ActionEvent e) {
	
	}
	
}
