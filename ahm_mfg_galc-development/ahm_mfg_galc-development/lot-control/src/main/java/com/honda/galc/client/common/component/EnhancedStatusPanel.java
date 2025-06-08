package com.honda.galc.client.common.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.common.datacollection.data.StatusMessage;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.property.DefaultViewProperty;
import com.honda.galc.client.datacollection.view.info.LotControlSystemInfo;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.PlcDevice;
import com.honda.galc.client.ui.StatusPanel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.IDeviceDriver;
import com.honda.galc.net.ConnectionStatus;
import com.honda.galc.net.ConnectionStatusListener;
import com.honda.galc.property.DevicePropertyBean;
/**
 * 
 * <h3>EnhancedStatusPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> EnhancedStatusPanel description </p>
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
 * @author Paul Chou
 * Mar 19, 2010
 *
 */
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class EnhancedStatusPanel extends StatusPanel implements ConnectionStatusListener {
	private static final long serialVersionUID = 1L;
	private static final String INFO_ICON_PATH = "/resource/com/honda/galc/client/images/info2.gif";
	private PolygonButton comStatusButton;
	private JButton informationButton;
	private ComStatusDialog comStatusDialog;
	private DevicePropertyBean property;
	private Map<String,ConnectionStatus> statusList = new HashMap<String,ConnectionStatus>();
	private ClientContext context;
	private LotControlSystemInfo lotControlSysInfo;
	
	public EnhancedStatusPanel() {
		super();
		AnnotationProcessor.process(this);
		initConnections();
	}

	public EnhancedStatusPanel(DefaultViewProperty property) {
		this.property = property;
		
		AnnotationProcessor.process(this);
		initConnections();
		initDeviceStatus();
	}

	
	public EnhancedStatusPanel(ClientContext context) {
		super();
		this.context = context;
		AnnotationProcessor.process(this);
		
		if(context != null)
			setUser(context.getUserId());
		
		initConnections();
		
	}

	private void initConnections() {
		getComStatusButton().addActionListener(this);
		getInformationButton().addActionListener(this);
		
	}

	public void initDeviceStatus() {
		
		//always add Server status first
		statusList.put(StatusMessage.SERVER_ON_LINE, null); 
		

		Hashtable<String,IDevice> devices = DeviceManager.getInstance().getDevices();
		
		for(IDevice device : devices.values()) {
			if(device instanceof PlcDevice){
				for (IDeviceDriver driver : ((PlcDevice)device).getDriverList()){
					statusList.put(driver.getId(), new ConnectionStatus(driver.getId(), driver.isConnected()));
					driver.registerListener(this);
				}
			} else if(device.isEnabled()) {
				statusList.put(device.getId(),new ConnectionStatus(device.getId(),device.isConnected()));
				device.registerListener(this);
			}
			
		}
		
	}


	public JPanel getStatusContentPanel() {
		if(statusContentPanel == null) {
			statusContentPanel = new JPanel();
			statusContentPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
			statusContentPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,0,0));
			statusContentPanel.add(getInformationButton());
			statusContentPanel.add(getComStatusButton(), BorderLayout.WEST);
			statusContentPanel.add(initDateLabel(), BorderLayout.CENTER);
			statusContentPanel.add(initTimeLabel(), BorderLayout.EAST);
		}
		return statusContentPanel;
	}
	
	public JButton getInformationButton() {
		if(informationButton == null)
		{
			ImageIcon infoIcon = getInformationIcon();
				
			informationButton = infoIcon != null ? new JButton(infoIcon) : new JButton("Information");
			informationButton.setFont(new Font("dialog", 0, 14));
			informationButton.setBorder(new BevelBorder(BevelBorder.LOWERED));
			informationButton.setPreferredSize(new Dimension(40, 22));
			informationButton.setMnemonic(KeyEvent.VK_I);
			informationButton.setToolTipText("Client information");
		}
		return informationButton;
	}

	private ImageIcon getInformationIcon() {
		URL resource = getClass().getResource(INFO_ICON_PATH);
		if(resource != null) return new ImageIcon(resource);
		
		return null;
	}

	public JButton getComStatusButton(){
        if(comStatusButton == null){
            comStatusButton = new PolygonButton("COM");
            comStatusButton.setFont(new Font("dialog", 0, 12));
            comStatusButton.setPreferredSize(new Dimension(40,20));
            comStatusButton.setComStatus(true);
            comStatusButton.setEnabled(true);
            comStatusButton.setVisible(true);
            comStatusButton.setMnemonic(KeyEvent.VK_C);
            comStatusButton.setToolTipText("Communication status");
        }
        return comStatusButton;
    }
    
	@EventSubscriber(eventClass = StatusMessage.class)
    public void setComStatus(StatusMessage status){
		if(statusList.containsKey(status.getId()))
			statusList.put(status.getId(),new ConnectionStatus( status.getId( ), status.getDeviceStatus()));
		
		comStatusButton.setComStatus(getOverallComStatus());
        this.repaint();
    }

	private boolean getOverallComStatus() {
		Boolean result = true;
		for( ConnectionStatus status: statusList.values())
			if(status != null && !status.isConnected()) return false;
		
		return result;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if(e.getSource() ==  getComStatusButton()){
			displayComStatusDialog();
		} if(e.getSource() == getInformationButton()){
			displayInformationScreen();
		}
	}

	private void displayInformationScreen() {
		java.awt.Container parentScreen = this.getRootPane().getParent();
		LotControlSystemInfo cfg = getLotControlSystemInfo();
		cfg.refresh();
		cfg.setLocationRelativeTo(parentScreen);
		cfg.setVisible(true);
		
	}

	public LotControlSystemInfo getLotControlSystemInfo() {
		if(lotControlSysInfo == null){
			lotControlSysInfo = new LotControlSystemInfo(context);
		}
		return lotControlSysInfo;
	}

	private void displayComStatusDialog() {
		if(comStatusDialog == null) {
            
            comStatusDialog = new ComStatusDialog(getComStatusButton(), statusList);
            comStatusDialog.setVisible(true);
        } else if (!comStatusDialog.isVisible()){
        	comStatusDialog.refreshScreen();
        	comStatusDialog.setVisible(true);
        } else if(comStatusDialog.isVisible()){
            comStatusDialog.dispose();
            comStatusDialog = null;
        }
		
	}

	public DevicePropertyBean getProperty() {
		return property;
	}

	public void setProperty(DevicePropertyBean property) {
		this.property = property;
	}

	public ClientContext getContext() {
		return context;
	}

	public void setContext(ClientContext context) {
		this.context = context;
	}

	public void statusChanged(ConnectionStatus status) {
		Logger.getLogger().info("statusChanged:" + status.getConnectionId() + ":" + status.getStatus());
		
		if(statusList.containsKey(status.getConnectionId())){
			statusList.put(status.getConnectionId(),status);
			
			Message msg = new Message(status.isConnected()? StatusMessage.DEVICE_CONNECT : StatusMessage.DEVICE_DISCONNECT, 
					status.isConnected()? "connected" : "disconnected", MessageType.INFO);
			DataCollectionController.getInstance().getFsm().message(msg);
		}
		
		comStatusButton.setComStatus(getOverallComStatus());
		
		
	}

}
