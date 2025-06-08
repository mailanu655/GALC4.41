package com.honda.galc.client;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import com.honda.galc.client.mvc.KeyHandler;
import com.honda.galc.client.mvc.KeyHandler.VirtualKey;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.event.KeypadEvent;
import com.honda.galc.client.ui.event.KeypadEventType;

public final class ClickerControl {

	private boolean isNavPane = false;
	public static final String HIGHLIGHT_STYLE = "clicker-highlight-border";
	public static final String NORMAL_STYLE = "clicker-normal-border";

	private static ClickerControl instance;

	public static ClickerControl getInstance() {
		if (instance == null) {
			instance = new ClickerControl();
		}
		return instance;
	}

	public void addKeyHandler(Stage stage) {
		final StringBuffer buffer = new StringBuffer();
		stage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				KeyHandler.VirtualKey vk = KeyHandler.getVirtualKey(event);
				KeypadEvent keypadEvent = null;

				switch (vk) {
				case LEFT:
					keypadEvent = new KeypadEvent(KeypadEventType.KEY_LEFT, isNavPane);
					break;

				case RIGHT:
					keypadEvent = new KeypadEvent(KeypadEventType.KEY_RIGHT, isNavPane);
					break;

				case COMPLETE:
					keypadEvent = new KeypadEvent(KeypadEventType.KEY_COMPLETE, isNavPane);
					break;
					
				case REJECT:
					keypadEvent = new KeypadEvent(KeypadEventType.KEY_REJECT, isNavPane);
					break;

				case TOGGLEPANE:
					isNavPane = !isNavPane;
					keypadEvent = new KeypadEvent(KeypadEventType.KEY_TOGGLEPANE, isNavPane);
					break;

				case SKIPTASK:
					keypadEvent = new KeypadEvent(KeypadEventType.KEY_SKIPTASK, isNavPane);
					break;

				case SKIPOPERATION:
					keypadEvent = new KeypadEvent(KeypadEventType.KEY_SKIPOPERATION, isNavPane);
					break;

				case SKIPPRODUCT:
					keypadEvent = new KeypadEvent(KeypadEventType.KEY_SKIPPRODUCT, isNavPane);
					break;

				case PREVTASK:
					keypadEvent = new KeypadEvent(KeypadEventType.KEY_PREVTASK, isNavPane);
					break;

				case PREVOPERATION:
					keypadEvent = new KeypadEvent(KeypadEventType.KEY_PREVOPERATION, isNavPane);
					break;

				case UNDEFINED:
				default:
					break;
				}

				if (keypadEvent != null) {
					EventBusUtil.publish(keypadEvent);
					// System.out.printf("\n\n\nClickerControl: publish=%s\n" ,
					// keypadEvent.toString());
				}
			}
		});

		stage.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				Object obj = event.getTarget();
				boolean okToHandle = true;
				if (obj instanceof TextField) {
					TextField textField = (TextField) obj;
					okToHandle = !textField.isEditable();
				}
				if (okToHandle) {
					KeypadEvent keypadEvent = null;
					KeyHandler.VirtualKey vk = KeyHandler.getVirtualKey(event);
					if (vk.equals(VirtualKey.UNDEFINED)) {
						if (event.getCode().equals(KeyCode.ENTER)) {
							// System.out.printf(buffer.toString());
							keypadEvent = new KeypadEvent(KeypadEventType.KEY_ENTER, isNavPane);
							keypadEvent.setText(buffer.toString());
							buffer.delete(0, buffer.length());
						} else {
							buffer.append(event.getText());
						}
					}

					if (keypadEvent != null) {
						EventBusUtil.publish(keypadEvent);
						// System.out.printf("\n\n\nClickerControl:
						// publish=%s\n", keypadEvent.toString());
					}
				}
				else {
					buffer.delete(0, buffer.length());
				}
			}
		});

	}
}
