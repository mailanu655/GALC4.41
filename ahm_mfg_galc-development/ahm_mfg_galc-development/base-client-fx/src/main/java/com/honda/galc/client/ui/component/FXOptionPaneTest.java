package com.honda.galc.client.ui.component;

import com.honda.galc.client.utils.UiFactory;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FXOptionPaneTest extends Application implements
		EventHandler<ActionEvent> {

	private Stage primaryStage = null;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage stage) {
		primaryStage = stage;

		String buttonNameList[] = { "Info Dialog", "Warning Dialog",
				"Error Dialog", "Question Dialog", "Plain Dialog" };

		VBox vbox = new VBox();

		for (String buttonName : buttonNameList) {
			Button button = UiFactory.createButton(buttonName);
			button.setOnAction(this);
			vbox.getChildren().add(button);
		}

		Scene scene = new Scene(vbox);
		primaryStage.setTitle("FXOptionPane Sample");
		primaryStage.setWidth(300);
		primaryStage.setHeight(190);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void handle(ActionEvent event) {
		System.out.println(event.getSource().toString());
		Button b = (Button) event.getSource();
		System.out.println(b.getText());

		if (b.getText().compareTo("Info Dialog") == 0) {
			FXOptionPane.showMessageDialog(primaryStage, "Info Dialog",
					"Info Dialog Title", FXOptionPane.Type.INFORMATION);
		} else if (b.getText().compareTo("Warning Dialog") == 0) {
			FXOptionPane.showMessageDialog(primaryStage, "Warning Dialog",
					"Warning Dialog Title", FXOptionPane.Type.WARNING);
		} else if (b.getText().compareTo("Error Dialog") == 0) {
			FXOptionPane.showMessageDialog(primaryStage, "Error Dialog",
					"Error Dialog Title", FXOptionPane.Type.ERROR);
		} else if (b.getText().compareTo("Question Dialog") == 0) {
			FXOptionPane.Response response = FXOptionPane.showConfirmDialog(
					primaryStage, "Question Dialog", "Question Dialog Title",
					FXOptionPane.Type.QUESTION);

			FXOptionPane.showMessageDialog(primaryStage,
					String.format("%s was choosen", response.toString()),
					"Question Dialog Result", FXOptionPane.Type.INFORMATION);
		} else if (b.getText().compareTo("Plain Dialog") == 0) {
			FXOptionPane.showMessageDialog(primaryStage, "Plain Dialog",
					"Plain Dialog Title");

		}

	}
}
