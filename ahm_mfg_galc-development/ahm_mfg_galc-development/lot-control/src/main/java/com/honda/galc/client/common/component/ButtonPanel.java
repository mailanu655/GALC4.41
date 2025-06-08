package com.honda.galc.client.common.component;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.honda.galc.client.datacollection.view.PartLotControlPanel;

 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class ButtonPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	//private JLabel messageLabel = new JLabel("",JLabel.LEFT);
	private JButton okButton;
	private JButton cancelButton;
	

	public ButtonPanel() {
		super();
		initialize();
	}
	

	private void initialize() {
		add(getOkButton());
		add(getCancelButton());
	}


	public JButton getOkButton() {
		if(okButton == null){
			okButton = new JButton("OK");
			okButton.setPreferredSize(new Dimension(150, 45));
			okButton.setFont(PartLotControlPanel.FONT);
		}
		return okButton;
	}

	public JButton getCancelButton() {
		if(cancelButton == null){
			cancelButton = new JButton("CANCEL");
			cancelButton.setPreferredSize(new Dimension(150, 45));
			cancelButton.setFont(PartLotControlPanel.FONT);
		}
		return cancelButton;
	}
	

}
