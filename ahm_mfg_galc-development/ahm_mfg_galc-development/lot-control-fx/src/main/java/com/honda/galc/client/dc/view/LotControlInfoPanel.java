package com.honda.galc.client.dc.view;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * @author Subu Kathiresan
 * @date Feb 2, 2015
 */
public class LotControlInfoPanel extends VBox {
	/** Animates a node on and off screen to the left. */

	/** @return a control button to hide and show the LotControlInfoPanel */
	public Button getControlButton() { return controlButton; }
	private final Button controlButton;

	/** creates a LotControlInfoPanel containing a vertical alignment of the given nodes */
	LotControlInfoPanel(final double expandedWidth, Node... nodes) {
		getStyleClass().add("SideBar");
		this.setPrefWidth(expandedWidth);

		// create a bar to hide and show.
		setAlignment(Pos.CENTER);
		getChildren().addAll(nodes);

		// create a button to hide and show the LotControlInfoPanel.
		controlButton = new Button("Collapse");
		controlButton.getStyleClass().add("hide-left");

		// apply the animations when the button is pressed.
		controlButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent actionEvent) {
				// create an animation to hide LotControlInfoPanel.
				final Animation hideLotControlInfoPanel = new Transition() {
					{ setCycleDuration(Duration.millis(250)); }
					protected void interpolate(double frac) {
						final double curWidth = expandedWidth * (1.0 - frac);
						setPrefWidth(curWidth);
						setTranslateX(-expandedWidth + curWidth);
					}
				};
				hideLotControlInfoPanel.onFinishedProperty().set(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent actionEvent) {
						setVisible(false);
						controlButton.setText("Show");
						controlButton.getStyleClass().remove("hide-left");
						controlButton.getStyleClass().add("show-right");
					}
				});

				// create an animation to show a LotControlInfoPanel.
				final Animation showLotControlInfoPanel = new Transition() {
					{ setCycleDuration(Duration.millis(250)); }
					protected void interpolate(double frac) {
						final double curWidth = expandedWidth * frac;
						setPrefWidth(curWidth);
						setTranslateX(-expandedWidth + curWidth);
					}
				};
				showLotControlInfoPanel.onFinishedProperty().set(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent actionEvent) {
						controlButton.setText("Collapse");
						controlButton.getStyleClass().add("hide-left");
						controlButton.getStyleClass().remove("show-right");
					}
				});

				if (showLotControlInfoPanel.statusProperty().get() == Animation.Status.STOPPED && hideLotControlInfoPanel.statusProperty().get() == Animation.Status.STOPPED) {
					if (isVisible()) {
						hideLotControlInfoPanel.play();
					} else {
						setVisible(true);
						showLotControlInfoPanel.play();
					}
				}
			}
		});
	}
}
