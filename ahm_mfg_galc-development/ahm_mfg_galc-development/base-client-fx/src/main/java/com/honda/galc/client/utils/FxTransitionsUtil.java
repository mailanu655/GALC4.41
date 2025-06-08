package com.honda.galc.client.utils;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * @author Subu Kathiresan
 * @date Mar 2, 2015
 */
public class FxTransitionsUtil {

	public static InnerShadow innerGlow(Color color) {
		int depth = 70;  //Setting the uniform variable for the glow width and height

		InnerShadow innerGlow = new InnerShadow();
		innerGlow.setOffsetY(0f);
		innerGlow.setOffsetX(0f);
		innerGlow.setColor(color);
		innerGlow.setWidth(depth);
		innerGlow.setHeight(depth);

		return innerGlow;
	}

	public static DropShadow outerGlow(Color color) {
		int depth = 70;  //Setting the uniform variable for the glow width and height

		DropShadow borderGlow = new DropShadow();
		borderGlow.setOffsetY(0f);
		borderGlow.setOffsetX(0f);
		borderGlow.setColor(color);
		borderGlow.setWidth(depth);
		borderGlow.setHeight(depth);

		return borderGlow;
	}

	public static void fadeMessage(double duration, final Text text) {
		Scene scene = text.getScene();
		final Parent p = scene.getRoot();
		FadeTransition ft = new FadeTransition(Duration.millis(duration), text);
		ft.setFromValue(1.0);
		ft.setToValue(0.0);
		ft.play();
		ft.setOnFinished(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				if (p instanceof Group) {
					Group group = (Group) p;
					group.getChildren().remove(text);
				}
				if (p instanceof Pane) {
					Pane group = (Pane) p;
					group.getChildren().remove(text);
				}
			}
		});
		FadeTransition ft2 = new FadeTransition(Duration.millis(duration + (duration * .1)), text);
		ft2.setFromValue(1.0);
		ft2.setToValue(0.0);
		ft2.play();
	}

	public static void scaleTransition(double duration, final Text text) {
		Scene scene = text.getScene();
		final Parent p = scene.getRoot();

		ScaleTransition st = new ScaleTransition(Duration.millis(duration), text);
		st.setByX(1.2f);
		st.setByY(1.2f);
		st.setByZ(1.2f);
		st.setCycleCount((int) 4f);
		st.setAutoReverse(true);

		st.play();
		st.setOnFinished(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				if (p instanceof Group) {
					Group group = (Group) p;
					group.getChildren().remove(text);
				}
				if (p instanceof Pane) {
					Pane group = (Pane) p;
					group.getChildren().remove(text);
				}
			}
		});
	}

	public static void rotateTransition(int cycleCount, double duration, final Node node) {
		RotateTransition rt = new RotateTransition(Duration.millis(duration), node);
		rt.setByAngle(360);
		rt.setCycleCount(cycleCount);
		rt.play();
	}
}
