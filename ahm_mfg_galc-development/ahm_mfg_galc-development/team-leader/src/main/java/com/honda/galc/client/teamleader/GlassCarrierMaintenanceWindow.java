package com.honda.galc.client.teamleader;

import java.awt.Component;
import java.util.List;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.entity.conf.Application;

public class GlassCarrierMaintenanceWindow extends MainWindow {

	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tabbedPane;
	
	private GlassCarrierMaintenancePanel carrierPanel;

	private GlassVinMaintenancePanel vinPanel;
	
	public GlassCarrierMaintenanceWindow(ApplicationContext appContext, Application application) {
		super(appContext, application, true);
		
		setSize(1024,768);
		
		initClientPanel();
	}

	private void initClientPanel() {
		
		this.setClientPanel(createTabbedContentPanel());
		
	}
	
 	protected Component createTabbedContentPanel() {
		this.tabbedPane = new JTabbedPane();

		this.carrierPanel = new GlassCarrierMaintenancePanel(this);
		
		this.vinPanel = new GlassVinMaintenancePanel(this);
		
		getTabbedPane().addTab("Carrier Maintenance", carrierPanel);
		getTabbedPane().addTab("Carrier VIN assignment", vinPanel);
		
		getTabbedPane().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				Component component = getTabbedPane().getSelectedComponent();
				if(component instanceof GlassCarrierMaintenancePanel) {
					carrierPanel.loadData();
				}else if(component instanceof GlassVinMaintenancePanel) {
					vinPanel.loadVinData();
				}
			}
		});
		
		return getTabbedPane();
	} 
 	
	protected JTabbedPane getTabbedPane() {
		return tabbedPane;
	}


}
