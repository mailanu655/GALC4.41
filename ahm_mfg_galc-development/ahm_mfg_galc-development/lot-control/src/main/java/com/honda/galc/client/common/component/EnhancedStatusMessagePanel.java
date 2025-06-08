package com.honda.galc.client.common.component;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.ui.StatusMessagePanel;
import com.honda.galc.property.DevicePropertyBean;

 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class EnhancedStatusMessagePanel extends StatusMessagePanel {
	private static final long serialVersionUID = 1L;
	private static EnhancedStatusPanel enhancedStatusPanel = null;
	
	DevicePropertyBean property;
	private ClientContext context;
	public EnhancedStatusMessagePanel() {
		super();
	}

	public EnhancedStatusMessagePanel(ClientContext context) {
		this.context = context;
		this.property = context.getProperty();
		
		initConnections();
	}


	private void initConnections() {
		if(enhancedStatusPanel == null)
			enhancedStatusPanel = new EnhancedStatusPanel(context);
		

		if(context != null)
			enhancedStatusPanel.setUser(context.getUserId());
		
		enhancedStatusPanel.setContext(context);
		enhancedStatusPanel.setProperty(property);
		enhancedStatusPanel.initDeviceStatus();

		enhancedStatusPanel.getInformationButton().setEnabled(context != null);
		enhancedStatusPanel.getInformationButton().setVisible(context != null);
	}
	
	@Override
	public EnhancedStatusPanel createStatusPanel() {
		enhancedStatusPanel = new EnhancedStatusPanel(context);
		
		enhancedStatusPanel.setContext(context);
		enhancedStatusPanel.setProperty(property);
		enhancedStatusPanel.initDeviceStatus();

		return enhancedStatusPanel;
	}

	@Override
	public EnhancedStatusPanel getStatusPanel() {
		if(enhancedStatusPanel == null){
			enhancedStatusPanel = createStatusPanel();
		}
		
		return enhancedStatusPanel;
	}
	
	public void setProperty(DevicePropertyBean property){
		this.property = property;
		initConnections();
		
		enhancedStatusPanel.repaint();
		
	}
	
	public void setContext(ClientContext context){
		this.context = context;
		initConnections();
		enhancedStatusPanel.repaint();
		
	}
	
}
