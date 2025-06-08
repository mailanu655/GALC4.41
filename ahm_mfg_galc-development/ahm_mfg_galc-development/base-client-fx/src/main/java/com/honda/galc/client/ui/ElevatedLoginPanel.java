package com.honda.galc.client.ui;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.AccessControlManager;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.component.LoggedText;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.device.SmartCardReaderUtil;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.service.property.PropertyService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class ElevatedLoginPanel extends BorderPane {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	private GridPane mainPanel;

	private ApplicationPropertyBean appBean;

	private Label assocaiteIdLabel = UiFactory.createLabel("assocaiteIdLabel", "Network User ID");
	private TextField associateTextField = UiFactory.createTextField("associateTextField");
	private Label passwordLabel = UiFactory.createLabel("passwordLabel", "Password");
	private PasswordField passwordField = UiFactory.createPasswordField("Password");
	private Label messageField = UiFactory.createLabel("messageField", "");
	private Button okButton = UiFactory.createButton("Okay", UiFactory.getButtonFont(), true);
	private Button cancelButton = UiFactory.createButton("Cancel", UiFactory.getButtonFont(), true);

	private String newAssociateId = null;
	private String reason = null;
	private boolean passwordRequired = false;
	private String securityGroup = null;

	public ElevatedLoginPanel() {
		mainPanel = initMainPanel();
		this.setCenter(mainPanel);
	}

	public ElevatedLoginPanel(String reason, boolean passwordRequired, String securityGroup) {
		this.reason = reason;
		this.passwordRequired = passwordRequired;
		this.securityGroup = securityGroup;

		mainPanel = initMainPanel();
		this.setCenter(mainPanel);
	}

	private GridPane initMainPanel() {
		GridPane panel = new GridPane();
		appBean = PropertyService.getPropertyBean(ApplicationPropertyBean.class,
				ClientMainFx.getInstance().getApplicationContext().getApplicationId());

		panel.setGridLinesVisible(false); // set true for debug
		panel.setVgap(10);
		panel.setHgap(10);
		panel.setPadding(new Insets(20, 30, 10, 30));

		LoggedText reasonField = UiFactory.createText(getReason());
		reasonField.setFont(new Font(13));
		reasonField.setWrappingWidth(300);
		reasonField.setTextAlignment(TextAlignment.JUSTIFY);
		reasonField.setText(getReason());

		int row = 0;
		panel.add(reasonField, 0, row++, 2, 1);

		associateTextField.setPrefWidth(250);
		panel.addRow(row++, assocaiteIdLabel, associateTextField);

		if (isPasswordRequired()) {
			panel.addRow(row++, passwordLabel, passwordField);
			this.messageField.setText("Enter your network ID and password");
		} else {
			this.messageField.setText("Enter your network ID");
		}
		this.messageField.setTextFill(Color.RED);

		Node buttonPane = createButtonPanel();
		panel.add(buttonPane, 0, row++, 2, 1);
		panel.add(messageField, 0, row++, 2, 1);
		return panel;
	}

	private HBox createButtonPanel() {
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER_RIGHT);
		hbox.setPadding(new Insets(5));
		hbox.setSpacing(10);
		hbox.getChildren().addAll(okButton);
		hbox.getChildren().addAll(cancelButton);
		return hbox;
	}

	public PasswordField getPasswordField() {
		return passwordField;
	}

	public Button getOkButton() {
		return okButton;
	}

	public Button getCancelButton() {
		return cancelButton;
	}

	public LoginStatus login() {
		String password = null;
		LoginStatus loginStatus = null;
		String userId = getUserId();

		if (StringUtils.isBlank(userId)) {
			loginStatus = LoginStatus.USER_NOT_EXIST;
		} else if (isPasswordRequired()) {
			password = passwordField.getText().trim();
			if (StringUtils.isBlank(password)) {
				loginStatus = LoginStatus.PASSWORD_REQUIRED;
			}
		}

		if (loginStatus == null) {
			loginStatus = AccessControlManager.getInstance().verifyLDAPUser(userId, password, getSecurityGroup());
		}

		if (loginStatus != LoginStatus.OK) {
			this.messageField.setText(loginStatus.getMessage());
			this.messageField.setTextFill(Color.RED);
		}

		return loginStatus;

	}

	public String getUserId() {
		newAssociateId = associateTextField.getText().trim();
		if (StringUtils.isBlank(newAssociateId)) {
			return StringUtils.EMPTY;
		}

		if (appBean.isMapCardNumber()) {
			Integer cardIdMaxLength = appBean.getCardIdMaxLength();
			String userId = SmartCardReaderUtil.getUserId(newAssociateId, appBean.getProximityCardReaderName(),
					cardIdMaxLength);
			if (userId != null) {
				userId = getAssociateIdWithPrefix(userId);
			}
			return (userId != null) ? userId : StringUtils.EMPTY;

		}
		return newAssociateId;
	}

	private String getAssociateIdWithPrefix(String associateId) {
		String[] prefixes = appBean.getAssociatePrefixes();
		if (prefixes != null && prefixes.length > 0) {
			LoginStatus loginStatus = null;
			String associateIdWithPrefix = "";
			for (String prefix : prefixes) {
				associateIdWithPrefix = prefix.trim() + associateId.trim();
				loginStatus = AccessControlManager.getInstance().verifyLDAPUser(associateIdWithPrefix, null, null);
				if (loginStatus == null) {
					ClientMainFx.getInstance().exitApplication(1);
				} else if (loginStatus == LoginStatus.USER_NOT_EXIST) {
					continue;
				} else if (loginStatus == LoginStatus.OK) {
					return associateIdWithPrefix;
				}
			}
		}
		return associateId;
	}

	public String getReason() {
		return reason;
	}

	public boolean isPasswordRequired() {
		return passwordRequired;
	}

	public String getSecurityGroup() {
		return securityGroup;
	}
}
