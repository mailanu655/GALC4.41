package com.honda.galc.client.ui.keypad.control;

import static com.honda.galc.common.logging.Logger.getLogger;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;


public class ShortPressKeyButton extends KeyButton {

	public ShortPressKeyButton(String key) {
		super(key);
	}

	protected void initEventListener() {
		setOnMousePressed(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				getLogger().debug(getKeyCode()+" pressed");
				if (event.getButton().equals(MouseButton.PRIMARY)) {
					fireShortPressed();
				}
				setFocused(false);
				event.consume();
			}
		});
	}

}
