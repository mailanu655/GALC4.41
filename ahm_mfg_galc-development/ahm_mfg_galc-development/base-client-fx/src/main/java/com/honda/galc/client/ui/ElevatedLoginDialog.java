package com.honda.galc.client.ui;

import java.util.Locale;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.keypad.control.KeyBoardPopup;
import com.honda.galc.client.ui.keypad.control.KeyBoardPopupBuilder;
import com.honda.galc.client.ui.keypad.robot.RobotFactory;
import com.honda.galc.client.utils.CommonUtil;
import com.honda.galc.enumtype.LoginStatus;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ElevatedLoginDialog extends FxDialog implements EventHandler<javafx.event.ActionEvent> {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	private ElevatedLoginPanel elevatedLoginPanel = new ElevatedLoginPanel();

	private KeyBoardPopup popup;

	private ElevatedLoginResult elevatedLoginResult = new ElevatedLoginResult(false, "", "");

	public ElevatedLoginDialog(Stage owner, String reason, boolean passwordRequired, String securityGroup) {
		super("", owner);
		setResizable(false);
		setTitle("Confirmation");
		initOwner(owner);
		initModality(Modality.WINDOW_MODAL);
		initComponent(reason, passwordRequired, securityGroup);
		centerOnScreen();
		sizeToScene();
		toFront();
		showAndWait();
	}

	private void initComponent(String reason, boolean passwordRequired, String securityGroup) {
		elevatedLoginPanel = new ElevatedLoginPanel(reason, passwordRequired, securityGroup);
		Scene primaryScene = new Scene(elevatedLoginPanel);

		primaryScene.getStylesheets().add(ClientMainFx.getInstance().getStylesheetPath());

		this.setScene(primaryScene);

		elevatedLoginPanel.getOkButton().setOnAction(this);
		elevatedLoginPanel.getCancelButton().setOnAction(this);
		elevatedLoginPanel.getOkButton().setDefaultButton(true);

		createKeyBoardPopup();
		primaryScene.getStylesheets().add(this.getClass()
				.getResource("/resource/com/honda/galc/client/ui/keypad/css/KeyboardButtonStyle.css").toExternalForm());

		// add keyboard scene listener to all text components
		primaryScene.focusOwnerProperty().addListener(new ChangeListener<Node>() {
			@Override
			public void changed(ObservableValue<? extends Node> value, Node n1, Node n2) {
				if (n2 != null) {
					if (n2 instanceof TextField) {
						CommonUtil.setPopupVisible(true, (TextField) n2, popup);
					} else if (n2 instanceof PasswordField) {
						CommonUtil.setPopupVisible(true, (PasswordField) n2, popup);
					}
				} else {
					CommonUtil.setPopupVisible(false, null, popup);
				}
			}
		});

		popup.show(this);
	}

	public static ElevatedLoginResult login(Stage owner, String reason, boolean passwordRequired,
			String securityGroup) {
		ElevatedLoginDialog elevatedLoginDialog = new ElevatedLoginDialog(owner, reason, passwordRequired,
				securityGroup);
		elevatedLoginDialog.centerOnScreen();
		return elevatedLoginDialog.getElevatedLoginResult();
	}

	@Override
	public void handle(ActionEvent e) {
		if (e.getSource().equals(elevatedLoginPanel.getOkButton())) {
			doLogin();
		} else if (e.getSource().equals(elevatedLoginPanel.getCancelButton())) {
			this.close();
		}
	}

	private void doLogin() {
		popup.hide();

		LoginStatus loginStatus = elevatedLoginPanel.login();
		elevatedLoginResult.setUserId(elevatedLoginPanel.getUserId());
		elevatedLoginResult.setMessage(loginStatus.getMessage());

		if (loginStatus == LoginStatus.OK) {
			elevatedLoginResult.setSuccessful(true);
			this.close();
		} else {
			elevatedLoginResult.setSuccessful(false);
			popup.show(popup.getOwnerWindow());
		}
	}

	private void createKeyBoardPopup() {
		String fontUrl = this.getClass()
				.getResource("/resource/com/honda/galc/client/ui/keypad/font/FontKeyboardFX.ttf").toExternalForm();
		Font.loadFont(fontUrl, -1);
		popup = KeyBoardPopupBuilder.create().initLayout("numblock").initScale(1.6).initLocale(Locale.ENGLISH)
				.addIRobot(RobotFactory.createFXRobot()).build();
		popup.getKeyBoard().setOnKeyboardCloseButton(new EventHandler<Event>() {
			public void handle(Event event) {
				CommonUtil.setPopupVisible(false, null, popup);
			}
		});
	}

	public ElevatedLoginResult getElevatedLoginResult() {
		return elevatedLoginResult;
	}
}
