package com.honda.galc.client.ui;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.honda.galc.client.utils.UiFactory;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;
import javafx.util.Duration;

/**
 * 
 * 
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 * 
 * @author Suriya Sena Jan 23 2014 JavaFx Migration
 */
public class StatusPane extends BorderPane {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	private static final Format dateFormat = new SimpleDateFormat("MM/dd/yy");
	private static final Format timeFormat = new SimpleDateFormat("hh:mma");

	// timer for updating current time
	private Timeline timer;

	private Label userLabel = UiFactory.createLabel("userLabel", "");
	private Label messageLabel = UiFactory.createLabel("messageLabel", "");
	private Label dateLabel = UiFactory.createLabel("dateLabel", "");
	private Label timeLabel = UiFactory.createLabel("timeLabel", "");

	protected BorderPane statusContentPanel;

	
	public StatusPane(boolean isMessageOnly) {
		super();

		userLabel.setAlignment(Pos.CENTER);
		messageLabel.setAlignment(Pos.CENTER);
		dateLabel.setAlignment(Pos.CENTER);
		timeLabel.setAlignment(Pos.CENTER);

		initComponents(isMessageOnly);

		if (!isMessageOnly) {
    		this.setDateTime();
    		
			timer = new Timeline(new KeyFrame(Duration.seconds(30), new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if (getScene() != null
						&& getScene().getWindow() != null
						&& getScene().getWindow().isShowing() == false) {
						timer.stop();
					} else
						if (event.getSource() instanceof KeyFrame) {
						setDateTime();
					}
				}
			}));

			timer.setCycleCount(Timeline.INDEFINITE);
			timer.play();
		}
	}
	
	
	protected void initComponents(boolean isMessageOnly) {
		this.setCenter(initMessageLabel());
	    if (!isMessageOnly)	 {
		this.setLeft(initUserLabel());
		this.setRight(getStatusContentPane());
	    }
	}

	protected BorderPane getStatusContentPane() {
		if (statusContentPanel == null) {
			statusContentPanel = new BorderPane();
			statusContentPanel.setCenter(initDateLabel());
			statusContentPanel.setRight(initTimeLabel());
		}
		return statusContentPanel;
	}

	private Label initUserLabel() {
		userLabel.setId("status-pane");
		return userLabel;
	}

	private Label initMessageLabel() {
		messageLabel.setId("status-pane");
		messageLabel.setMaxWidth(Double.MAX_VALUE);
		return messageLabel;
	}

	protected Label initDateLabel() {
		dateLabel.setId("status-pane");
		return dateLabel;
	}

	protected Label initTimeLabel() {
		timeLabel.setId("status-pane");
		return timeLabel;
	}

	private void setDateTime() {
		Date date = new Date(System.currentTimeMillis());
		timeLabel.setText(timeFormat.format(date));
		dateLabel.setText(dateFormat.format(date));
	}

	public void setStatusMessage(String message) {
		this.messageLabel.setText(message);
	}

	public void setUser(String user) {
		this.userLabel.setText(user);
	}
}
