package com.honda.galc.device.simulator.client.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.net.URL;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.ui.event.Event;
import com.honda.galc.client.ui.event.EventType;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.simulator.torque.VirtualTorqueDevice;
import com.honda.galc.openprotocol.OPMessageType;

/**
 * 
 * <h3>TorqueServerPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> TorqueServerPanel description </p>
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
 * <TD>May 25, 2012</TD>
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
 * @since May 25, 2012
 */
public class TorqueServerPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private JPanel controllerPanel;
	private JButton sendButton = null;
	private JLabel torqueStatusLabel;
	private JTextField instructionCodeField;
	
	private JPanel dataPanel;
	private JTextField torqueField;
	private JTextField torqueStatusField;
	private JTextField angleField;
	private JTextField angleStatusField;
	private JTextField tighteningStatusField;
	
	public TorqueServerPanel() {
		super();
		
		initialize();
	}

	private void initialize() {
		AnnotationProcessor.process(this);
		VirtualTorqueDevice.activate("TCSimulator",4545);
		
		TitledBorder border = new TitledBorder("Torque Controller");
		setBorder(border);	
		
		setLayout(new BorderLayout());
		
		add(getControllerPanel(), BorderLayout.NORTH);
		add(getDataPanel(), BorderLayout.CENTER);
		
		enableGun(true);
		
	}
	
	private JTextField getInstructionCodeField() {
		if(instructionCodeField == null){
			instructionCodeField = new JTextField();
			instructionCodeField.setPreferredSize(new Dimension(60, 20));
			instructionCodeField.setEditable(false);
		}
		return instructionCodeField;
	}

	@EventSubscriber(eventClass = Event.class)
	public void updateTorqueServerStatus(Event event) {
		
		if(event.getSource() instanceof OPMessageType){
			System.out.println("source:" + event.getSource() + "  :  " + event.getTarget());
			OPMessageType msgType = (OPMessageType)event.getSource();
			if(msgType == OPMessageType.paramSetSelected || msgType == OPMessageType.selectJob){
				getInstructionCodeField().setText(event.getTarget().toString().replace("0",""));
				enableGun(true);
			} else if(msgType == OPMessageType.abortJob || msgType == OPMessageType.toolDisable){
				getInstructionCodeField().setText("");
				enableGun(false);
			}
			
		} else {
			if (event.getEventType() == EventType.SUCCEEDED) {
				getTorqueStatusLabel().setIcon(new ImageIcon(getClass().getClassLoader().getResource("resource/running.GIF")));
			} else {
				getTorqueStatusLabel().setIcon(new ImageIcon(getClass().getClassLoader().getResource("resource/stopped.GIF")));
			}
		}
	}

	private void enableGun(boolean enabled) {
		getSendTorqueButton().setEnabled(enabled);
		for(Component c : getDataPanel().getComponents())
			c.setEnabled(enabled);
	}

	public JButton getSendTorqueButton() {
		if(sendButton == null){
			sendButton = new JButton("Torque");
			sendButton.setPreferredSize(new Dimension(100, 20));
		}
		return sendButton;
	}

	private JLabel getTorqueStatusLabel() {
		if(torqueStatusLabel == null){
			torqueStatusLabel = new JLabel();
			URL img = getClass().getClassLoader().getResource("resource/blank.GIF");
			ImageIcon icon = new ImageIcon(img, "");
			torqueStatusLabel.setIcon(icon);
		}
		return torqueStatusLabel;
	}

	public JPanel getControllerPanel() {
		if(controllerPanel == null){
			controllerPanel = new JPanel();
			controllerPanel.setLayout(new FlowLayout());
			
			controllerPanel.add(new JLabel("TC Server"));
			controllerPanel.add(getTorqueStatusLabel());
			controllerPanel.add(new JLabel("Instruction Code"));
			controllerPanel.add(getInstructionCodeField());
			controllerPanel.add(Box.createHorizontalStrut(15));
			controllerPanel.add(getSendTorqueButton());
			
		}
		return controllerPanel;
	}

	public JPanel getDataPanel() {
		if(dataPanel == null){
			dataPanel = new JPanel();
			TitledBorder border = new TitledBorder("Torque Data");
			dataPanel.setBorder(border);	
			dataPanel.setLayout(new GridLayout(2, 4, 5, 5));
			dataPanel.add(new JLabel("Torque"));
			dataPanel.add(getTorqueField());
			dataPanel.add(new JLabel("Torque Status"));
			dataPanel.add(getTorqueStatusField());
			dataPanel.add(new JLabel("AngleSatus"));
			dataPanel.add(getAngleStatusField());
			dataPanel.add(new JLabel("Tightening Status"));
			dataPanel.add(getTighteningStatusField());
		}
		return dataPanel;
	}

	public JTextField getTorqueField() {
		if(torqueField == null)
			torqueField = new JTextField();
		
		return torqueField;
	}

	public JTextField getTorqueStatusField() {
		if(torqueStatusField == null)
			torqueStatusField = new JTextField();
		return torqueStatusField;
	}

	public JTextField getAngleField() {
		if(angleField == null)
			angleField = new JTextField();
		return angleField;
	}

	public JTextField getAngleStatusField() {
		if(angleStatusField == null)
			angleStatusField = new JTextField();
		return angleStatusField;
	}

	public JTextField getTighteningStatusField() {
		if(tighteningStatusField == null)
			tighteningStatusField = new JTextField();
		return tighteningStatusField;
	}

	public DataContainer getTorque() {
		DataContainer dc = new DefaultDataContainer();
		dc.put("TORQUE", getTorqueField().getText());
		dc.put("TORQUE_STATUS", getTorqueStatusField().getText());
		dc.put("ANGLE_STATUS", getAngleStatusField().getText());
		dc.put("TIGHTENING_STATUS", getTighteningStatusField().getText());
		dc.put("ANGLE", "1.0");
		
		
		return dc;
	}
	
	

}
