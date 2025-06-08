package com.honda.galc.client.ui.component;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.utils.UiFactory;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXOptionPane {

	public enum Response {
		NO, YES, CANCEL
	};
	
	
	public enum Type {
		QUESTION("resource/com/honda/galc/client/ui/component/dialog/dialog_question.png"),
		INFORMATION("resource/com/honda/galc/client/ui/component/dialog/dialog_information.png"),
		WARNING("resource/com/honda/galc/client/ui/component/dialog/dialog_warning.png"),
		ERROR("resource/com/honda/galc/client/ui/component/dialog/dialog_error.png"),
		CONFIRM("resource/images/common/confirm.png"),
		REJECT("resource/images/common/reject.png"),
		PLAIN(null);
			
		private final String iconPath;
			
		Type(String iconPath) {
		   this.iconPath = iconPath;
		}
			
		public String getIconPath() {
			return iconPath;
		}
	}

	private static final double NORMALIZED_IMAGE_SIZE = 40.0;

	private static Response buttonSelected = Response.CANCEL;
	private static LoggedTextArea commentArea;

	static class Message extends Text {
		public Message(String msg) {
			super(msg);
			setWrappingWidth(350);
			setId("fxoption-pane");
		}
	}

	
	private static Image getImage(Type type) {
		String path = type.getIconPath();
		if (path!=null) {
			return  new Image(path);
		} else {
			return null;
		}
	}
	
	private static ImageView getImageView(Type type) {
	   return normalizeImage(new ImageView(getImage(type)));
	}
	
	private static ImageView normalizeImage(ImageView imageView) {
		if (imageView.getImage() != null && ( imageView.getImage().getWidth() != NORMALIZED_IMAGE_SIZE || imageView.getImage().getWidth() != NORMALIZED_IMAGE_SIZE)) {
			imageView.setPreserveRatio(true);
			imageView.setFitHeight(NORMALIZED_IMAGE_SIZE);
			imageView.setFitWidth(NORMALIZED_IMAGE_SIZE);
		}
		return imageView;
	}
	

	public static Response showConfirmDialog(Stage owner, String message,String title) {
		return showConfirmDialog(owner,message,title, FXOptionPane.Type.PLAIN);
	}
	
	public static Response showConfirmDialog(Stage owner, String message,String title, Type messageType) {
		
		VBox vb = new VBox();

		final FxDialog dlg = new FxDialog(title, owner, vb);
		dlg.initStyle(StageStyle.DECORATED);
		vb.setPadding(Layout.PADDING);
		vb.setSpacing(Layout.SPACING);
		Button yesButton = UiFactory.createButton("Yes", getImageView(Type.CONFIRM));
		yesButton.setId("fxoption-pane");
		yesButton.defaultButtonProperty().bind(yesButton.focusedProperty());
		
		yesButton.setOnAction(new EventHandler<ActionEvent>() {
			// @Override
			public void handle(ActionEvent e) {
				dlg.close();
				buttonSelected = Response.YES;
			}
		});
		
		Button noButton = UiFactory.createButton("No", getImageView(Type.REJECT));
		noButton.setId("fxoption-pane");
		noButton.defaultButtonProperty().bind(noButton.focusedProperty());

		noButton.setOnAction(new EventHandler<ActionEvent>() {
			// @Override
			public void handle(ActionEvent e) {
				dlg.close();
				buttonSelected = Response.NO;
			}
		});
		
		BorderPane bp = new BorderPane();
		HBox buttons = new HBox();
		buttons.setAlignment(Pos.CENTER);
		buttons.setSpacing(Layout.SPACING);
		buttons.getChildren().addAll(yesButton, noButton);
		bp.setCenter(buttons);
		HBox msg = new HBox();
		msg.setSpacing( Layout.SPACING_SMALL );
		msg.getChildren().addAll(getImageView(messageType), new Message(message));
		vb.getChildren().addAll(msg, bp);
		dlg.showDialog();
		Response response = buttonSelected;
		buttonSelected = Response.CANCEL;
		return response;
	}
	
	public static Response showConfirmDialogWithComment(Stage owner, String message, String title, Type messageType,
			String commentName, final boolean isCommentMandatory, final String errorMessage) {

		VBox vb = new VBox();
		final LoggedLabel errorLabel = UiFactory.createLabel("errorLabel");
		errorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		errorLabel.setTextFill(Color.RED);
		errorLabel.setPadding(new Insets(0, 0, 0, 110));
		
		final FxDialog dlg = new FxDialog(title, owner, vb);
		dlg.initStyle(StageStyle.DECORATED);
		vb.setPadding(Layout.PADDING);
		vb.setSpacing(Layout.SPACING);
		Button yesButton = UiFactory.createButton("Yes", getImageView(Type.CONFIRM));
		yesButton.setId("fxoption-pane");
		yesButton.defaultButtonProperty().bind(yesButton.focusedProperty());

		yesButton.setOnAction(new EventHandler<ActionEvent>() {
			// @Override
			public void handle(ActionEvent e) {
				if (isCommentMandatory && StringUtils.isBlank(commentArea.getText())) {
					errorLabel.setText(errorMessage);
					return;
				}
				dlg.close();
				buttonSelected = Response.YES;
			}
		});

		Button noButton = UiFactory.createButton("No", getImageView(Type.REJECT));
		noButton.setId("fxoption-pane");
		noButton.defaultButtonProperty().bind(noButton.focusedProperty());

		noButton.setOnAction(new EventHandler<ActionEvent>() {
			// @Override
			public void handle(ActionEvent e) {
				dlg.close();
				buttonSelected = Response.NO;
			}
		});

		VBox buttonContainer = new VBox();
		buttonContainer.setSpacing(Layout.SPACING_SMALL);
		HBox buttons = new HBox();
		buttons.setPadding(new Insets(10, 0, 0, 110));
		buttons.setSpacing(Layout.SPACING);
		buttons.getChildren().addAll(yesButton, noButton);
		// creating comment box
		commentArea = UiFactory.createTextArea();
		commentArea.setWrapText(true);
		commentArea.setPrefRowCount(3);
		commentArea.setPrefColumnCount(16);
		commentArea.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (StringUtils.isNotBlank(newValue))
					errorLabel.setText(null);
			}
		});
	
		
		LoggedLabel commentLabel = UiFactory.createLabel("commentLabel", commentName, Fonts.SS_DIALOG_PLAIN(16), TextAlignment.CENTER);
		commentLabel.setPadding(new Insets(25, 0, 0, 0));
		HBox scrapResonPanel = new HBox(10);
		scrapResonPanel.getChildren().addAll(commentLabel, commentArea);
		
		buttonContainer.getChildren().addAll(scrapResonPanel, buttons);

		HBox msg = new HBox();
		msg.setSpacing(Layout.SPACING_SMALL);
		msg.getChildren().addAll(getImageView(messageType), new Message(message));
		vb.getChildren().addAll(msg, buttonContainer, errorLabel);
		dlg.showDialog();
		Response response = buttonSelected;
		buttonSelected = Response.CANCEL;
		return response;
	}
	
	public static Response showConfirmDialogWithNoSelected(Stage owner, String message,String title, Type messageType) {
		
		VBox vb = new VBox();

		final FxDialog dlg = new FxDialog(title, owner, vb);
		dlg.initStyle(StageStyle.DECORATED);
		vb.setPadding(Layout.PADDING);
		vb.setSpacing(Layout.SPACING);
		Button yesButton = UiFactory.createButton("Yes", getImageView(Type.CONFIRM));
		yesButton.setId("fxoption-pane");
		yesButton.defaultButtonProperty().bind(yesButton.focusedProperty());
		yesButton.setFocusTraversable(false);
		yesButton.setOnAction(new EventHandler<ActionEvent>() {
			// @Override
			public void handle(ActionEvent e) {
				dlg.close();
				buttonSelected = Response.YES;
			}
		});
		
		Button noButton = UiFactory.createButton("No", getImageView(Type.REJECT));
		noButton.setId("fxoption-pane");
		noButton.defaultButtonProperty().bind(noButton.focusedProperty());

		noButton.setOnAction(new EventHandler<ActionEvent>() {
			// @Override
			public void handle(ActionEvent e) {
				dlg.close();
				buttonSelected = Response.NO;
			}
		});
		noButton.setFocusTraversable(true);
		BorderPane bp = new BorderPane();
		HBox buttons = new HBox();
		buttons.setAlignment(Pos.CENTER);
		buttons.setSpacing(Layout.SPACING);
		buttons.getChildren().addAll(yesButton, noButton);
		bp.setCenter(buttons);
		HBox msg = new HBox();
		msg.setSpacing( Layout.SPACING_SMALL );
		msg.getChildren().addAll(getImageView(messageType), new Message(message));
		vb.getChildren().addAll(msg, bp);
		dlg.showDialog();
		Response response = buttonSelected;
		buttonSelected = Response.CANCEL;
		return response;
	}
	

	public static String getCommentText() {
		return commentArea != null ? commentArea.getText() : null;
	}

	
	public static void showMessageDialog(Stage owner, String message,String title) {
		showMessageDialog(owner, new Message(message), title, FXOptionPane.Type.PLAIN);
	}
	
	public static void showMessageDialog(Stage owner, String message,String title, Type messageType) {
		showMessageDialog(owner, new Message(message), title, messageType);
	}
	
	public static void showMessageDialog(Stage owner, Node message, String title, Type messageType) {
		VBox vb = new VBox();
		final FxDialog dlg = new FxDialog(title, owner, vb);
		dlg.initStyle(StageStyle.DECORATED);
		vb.setPadding(Layout.PADDING);
		vb.setSpacing(Layout.SPACING);
		Button okButton = UiFactory.createButton("OK");
		okButton.setAlignment(Pos.CENTER);
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			// @Override
			public void handle(ActionEvent e) {
				dlg.close();
			}
		});
		BorderPane bp = new BorderPane();
		bp.setCenter(okButton);
		HBox msg = new HBox();
		msg.setSpacing(Layout.SPACING_SMALL);
		msg.getChildren().addAll(getImageView(messageType), message);
		vb.getChildren().addAll(msg, bp);
		dlg.showDialog();
	}
}

class Layout {
	public static Insets PADDING = new Insets(20);
	public static double SPACING = 20;
	public static double SPACING_SMALL = 10;
}
