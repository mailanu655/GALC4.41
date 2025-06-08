package com.honda.galc.client.teamleader.fx;

import java.awt.event.KeyEvent;

import javafx.scene.layout.HBox;

import com.honda.galc.entity.enumtype.MassMessageType;

public class SafetyLineMessageSenderPanel extends LineMessageSenderPanel {

	/** * * 
	* @author Fredrick Yessaian 
	* @since Sep 03, 2014
	*/
	private static final long serialVersionUID = 1L;
	
	public SafetyLineMessageSenderPanel() {
		super("Safety Mass Message Sender", KeyEvent.VK_N);
		setMassMessageType(MassMessageType.SAFETY);
	}

	@Override
	protected HBox populateFucntionSpecificLabel() {
		return createInfoLabel("You are not authorized to send Safety Line Level Mass Messages", true);
	}


}
