package com.honda.galc.qics.mobile.client.widgets;

import com.smartgwt.mobile.client.internal.util.AnimationUtil;
import com.smartgwt.mobile.client.widgets.Panel;

/**
 * The Class MessagePanel is used to display a message.  When no message, the panel is hidden.
 */
public class MessagePanel extends Panel {

	public MessagePanel() {
        setClassName("sc-rounded-panel");
        getElement().getStyle().setProperty("textAlign", "left");
        getElement().getStyle().setOpacity(0.0);
        clearMessage();
	}
	
	public void setMessage(String msg) {
		if ( msg == null || msg.isEmpty() ) {
			clearMessage();
		} else {
			setContents(msg);
			AnimationUtil.fadeTransition(this, true);
			this.setVisible(true);
		}
	}
	
	public void clearMessage() {
		setContents("");
		AnimationUtil.fadeTransition(this, false);
		this.setVisible(false);
	}
}
