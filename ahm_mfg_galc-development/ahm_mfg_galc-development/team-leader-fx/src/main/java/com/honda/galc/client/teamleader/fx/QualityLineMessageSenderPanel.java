package com.honda.galc.client.teamleader.fx;

import java.awt.event.KeyEvent;

import javafx.scene.layout.HBox;

import com.honda.galc.entity.enumtype.MassMessageType;

/** * * 
* @author Fredrick Yessaian 
* @since Sep 03, 2014
*/

public class QualityLineMessageSenderPanel extends LineMessageSenderPanel {

	private static final long serialVersionUID = 1L;
	
	public QualityLineMessageSenderPanel() {
		super("Quality Mass Message Sender", KeyEvent.VK_N);
		setMassMessageType(MassMessageType.QUALITY);
	}

	@Override
	protected HBox populateFucntionSpecificLabel() {
		return createInfoLabel("You are not authorized to send Quality Line Level Mass Messages", true);
	}

}
