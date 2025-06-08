package com.honda.galc.client.datacollection.view.ubisense;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import com.honda.galc.client.datacollection.property.DefaultViewProperty;
import com.honda.galc.client.datacollection.view.DefaultDataCollectionPanel;

/**
 * Ubisense Panel with status button
 * 
 * @author Bernard Leong
 * @date Oct 23, 2017
 */
public class UbisensePanel extends DefaultDataCollectionPanel {
	private static final long serialVersionUID = 1L;
    protected JButton ubisenseButton;
    
	public UbisensePanel(DefaultViewProperty property, int winWidth,
			int winHeight) {
		super(property, winWidth, winHeight);
	}
	
	@Override
	protected void initPanel() {
		super.initPanel();
		add(getUbisenseButton(), null);
	}
	
	public javax.swing.JButton getUbisenseButton() {
		if (ubisenseButton == null) {
			ubisenseButton = new javax.swing.JButton("Ubisense");
			ubisenseButton.setBounds(40, 150, 140, 90);
			ubisenseButton.setName("JUbisense");
			ubisenseButton.setFont(new Font("dialog", java.awt.Font.BOLD, 16));
			ubisenseButton.setEnabled(true);
			ubisenseButton.setText("ERROR");
			ubisenseButton.setBackground(Color.RED);
		}
		return ubisenseButton;
	}
}
