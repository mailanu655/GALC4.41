package com.honda.galc.qics.mobile.client.widgets;

import com.smartgwt.mobile.client.widgets.Button;
import com.smartgwt.mobile.client.widgets.BaseButton.ButtonType;

public class ButtonFactory {

	private static String HEIGHT = "32px";
	private static int MARGIN = 5;

	public static Button  buildActionButton(String title) {
        Button button = new Button(title, Button.ButtonType.ACTION_DEFAULT );
        button.setHeight(HEIGHT);
        button.setMargin( MARGIN );
        button.setButtonType(ButtonType.BORDERED);
        return button;
	}
	
}
