package com.honda.galc.client.ui.keypad.control;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.keypad.event.KeyButtonEvent;

public class KeyButton extends LoggedButton implements LongPressable {

	private final static long DEFAULT_DELAY = 400;

	private int keyCode;

	private final Timeline timer;

	private ObjectProperty<EventHandler<? super KeyButtonEvent>> onLongPressed;

	private ObjectProperty<EventHandler<? super KeyButtonEvent>> onShortPressed;

	public KeyButton() {
		this(null, null, DEFAULT_DELAY);
	}

	public KeyButton(String label) {
		this(label, null, DEFAULT_DELAY);
	}

	public KeyButton(Node graphic) {
		this(null, graphic, DEFAULT_DELAY);
	}

	public KeyButton(String label, Node graphic) {
		this(label, graphic, DEFAULT_DELAY);
	}

	public KeyButton(String label, long delay) {
		this(label, null, delay);
	}

	public KeyButton(String label, Node graphic, long delay) {
		super(label, graphic, "key-button");

		timer = new Timeline(new KeyFrame(new Duration(20), new KeyValue[0]));
		if (delay > 0) {
			timer.setDelay(new Duration(delay));
			initEventListener();
		}
	}

	protected void initEventListener() {
		timer.setOnFinished(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				fireLongPressed();
			}

		});

		setOnDragDetected(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				//getLogger().debug(keyCode+" drag detected");
				event.consume();
			}
		});

		setOnMouseClicked(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				//getLogger().debug(keyCode+" clicked: " + timer.getCurrentRate());

				if (event.getButton().equals(MouseButton.PRIMARY)) {
					if (timer.getStatus().equals(Status.RUNNING)) {
						timer.stop();
						fireShortPressed();

					}

				}
				setFocused(false);
				event.consume();
			}
		});

		setOnMousePressed(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				//getLogger().debug(keyCode+" pressed: "+timer.getCurrentRate());
				if (event.getButton().equals(MouseButton.PRIMARY)) {
					timer.playFromStart();
				}
				event.consume();
			}
		});

		setOnMouseDragged(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				//getLogger().debug(keyCode+" dragged: "+timer.getCurrentRate());

				if (event.getButton().equals(MouseButton.PRIMARY)) {
					if (timer.getStatus().equals(Status.RUNNING) && timer.getCurrentRate() > 1) {
						timer.stop();
						fireLongPressed();
					}
					event.consume();
				}

			}
		});

	}

	public Duration getDelay() {
		return timer.getDelay();
	}

	public void setDelay(Duration delay) {
		timer.setDelay(delay);
	}

	protected void fireLongPressed() {
		fireEvent(new KeyButtonEvent(this, KeyButtonEvent.LONG_PRESSED));
	}

	protected void fireShortPressed() {
		fireEvent(new KeyButtonEvent(this, KeyButtonEvent.SHORT_PRESSED));
	}

	public final void setOnLongPressed(EventHandler<? super KeyButtonEvent> eventhandler) {
		onLongPressedProperty().set(eventhandler);
	}

	public final EventHandler<? super KeyButtonEvent> getOnLongPressed() {
		return onLongPressedProperty().get();
	}

	public final ObjectProperty<EventHandler<? super KeyButtonEvent>> onLongPressedProperty() {
		if (onLongPressed == null) {
			onLongPressed = new SimpleObjectProperty<EventHandler<? super KeyButtonEvent>>() {

				@SuppressWarnings("unchecked")
				@Override
				protected void invalidated() {
					setEventHandler(KeyButtonEvent.LONG_PRESSED, (EventHandler<? super Event>) get());
				}
			};
		}
		return onLongPressed;
	}

	public final void setOnShortPressed(EventHandler<? super KeyButtonEvent> eventhandler) {
		onShortPressedProperty().set(eventhandler);
	}

	public final EventHandler<? super KeyButtonEvent> getOnShortPressed() {
		return onShortPressedProperty().get();
	}

	public final ObjectProperty<EventHandler<? super KeyButtonEvent>> onShortPressedProperty() {
		if (onShortPressed == null) {
			onShortPressed = new SimpleObjectProperty<EventHandler<? super KeyButtonEvent>>() {

				@SuppressWarnings("unchecked")
				@Override
				protected void invalidated() {
					setEventHandler(KeyButtonEvent.SHORT_PRESSED, (EventHandler<? super Event>) get());
				}
			};
		}
		return onShortPressed;

	}

	public int getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}

}
