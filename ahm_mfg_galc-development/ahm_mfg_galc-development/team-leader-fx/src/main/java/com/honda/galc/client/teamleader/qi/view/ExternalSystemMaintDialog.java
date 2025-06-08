package com.honda.galc.client.teamleader.qi.view;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 
 * <h3>ExternalSystemMaintDialog Class description</h3>
 * <p>
 * ExternalSystemMaintDialog description
 * </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 * 
 * @author Justin Jiang<br>
 *         January 14, 2021
 *
 */

public class ExternalSystemMaintDialog extends FxDialog{
	
	private LoggedButton createBtn;
	private LoggedButton updateBtn;
	private LoggedButton cancelBtn;
	private LoggedTextField textFieldName;
	private LoggedTextField textFieldDesc;
	private String message;
	private String clickedBtnName;
	private VBox mainBox = new VBox();
	private String labelName = "External System Name";
	private String labelDesc = "External System Description";
	private boolean isUpdate;
	
	public ExternalSystemMaintDialog() {
		super("External System Maintenance", ClientMainFx.getInstance().getStage());
	}

	public ExternalSystemMaintDialog(String applicationId) {
		super("External System Maintenance", ClientMainFx.getInstance().getStage(applicationId));
		this.initStyle(StageStyle.DECORATED);
	}
	
	public ExternalSystemMaintDialog(Stage stage) {
		super("External System Maintenance", stage);
		this.initStyle(StageStyle.DECORATED);
	}
		
	public boolean showExternalSystemMaintDialog(String message, boolean isUpdate) {
		this.message = message;
		this.isUpdate = isUpdate;
		initComponents();
		showDialog();
		if (!getClickedBtnName().equalsIgnoreCase(QiConstant.CANCEL) && !StringUtils.isBlank(getName())) {
			return true;
		} else {
			return false;
		}
	}
	
	public void initComponents(){
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		if (message != null) {
			addMessage(message, "display-message");
		}
		
		HBox textFieldHBox = new HBox();
		textFieldName = UiFactory.createTextField("textField", 128, TextFieldState.EDIT);
		textFieldName.setPrefWidth(300);
		textFieldName.addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(255));
		
		HBox labelBox = new HBox();
		LoggedLabel label = UiFactory.createLabel("label", this.labelName);
		label.getStyleClass().add("display-label");
		LoggedLabel asterisk = UiFactory.createLabel("asteriskReasonForChange", "*");
		asterisk.getStyleClass().add("display-label");
		asterisk.setStyle("-fx-text-fill: red");
		labelBox.setPadding(new Insets(15, 5, 5, 5));
		labelBox.getChildren().addAll(label, asterisk);

		textFieldHBox.setPadding(new Insets(10, 10, 10, 10));
		textFieldHBox.setSpacing(10);
		textFieldHBox.getChildren().addAll(labelBox, textFieldName);
		textFieldHBox.setAlignment(Pos.CENTER_LEFT);
		textFieldHBox.setMinHeight(68);
		
		HBox textFieldHBox2 = new HBox();
		textFieldDesc = UiFactory.createTextField("textField2", 128, TextFieldState.EDIT);
		textFieldDesc.setPrefWidth(300);
		textFieldDesc.addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(255));
		
		HBox labelBox2 = new HBox();
		LoggedLabel label2 = UiFactory.createLabel("label", this.labelDesc);
		label2.getStyleClass().add("display-label");
		labelBox2.setPadding(new Insets(15, 5, 5, 5));
		labelBox2.getChildren().addAll(label2);

		textFieldHBox2.setPadding(new Insets(10, 10, 10, 10));
		textFieldHBox2.setSpacing(10);
		textFieldHBox2.getChildren().addAll(labelBox2, textFieldDesc);
		textFieldHBox2.setAlignment(Pos.CENTER_LEFT);
		textFieldHBox2.setMinHeight(68);
		
		HBox btnHBox = new HBox();
		createBtn = createBtn(QiConstant.CREATE);
		updateBtn = createBtn(QiConstant.UPDATE);
		cancelBtn = createBtn(QiConstant.CANCEL);
		
		if (isUpdate) {
			createBtn.setDisable(true);
		} else {
			updateBtn.setDisable(true);
		}
		btnHBox.getChildren().addAll(createBtn, updateBtn, cancelBtn);
		btnHBox.setAlignment(Pos.CENTER);
		btnHBox.setPadding(new Insets(0, 10, 10, 10));
		btnHBox.setSpacing(10);
		
		mainBox.getChildren().addAll(textFieldHBox, textFieldHBox2, btnHBox);
		((BorderPane) this.getScene().getRoot()).setCenter(mainBox);
	}
	
	private LoggedButton createBtn(String text)	{
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.defaultButtonProperty().bind(btn.focusedProperty());
		btn.setOnAction(handler);
		btn.getStyleClass().add("popup-btn");
		return btn;
	}
	
	public String getName()	{
		return StringUtils.trim(textFieldName.getText());
	}
	
	public String getDesc()	{
		return StringUtils.trim(textFieldDesc.getText());
	}
	
	private EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {

		public void handle(ActionEvent event) {
			Object source = event.getSource();
			if (source instanceof LoggedButton) {
				LoggedButton btn = (LoggedButton) source;
				clickedBtnName = btn.getText();
				if(StringUtils.isBlank(getName()) 
						&& !getClickedBtnName().equals(QiConstant.CANCEL)) {
						textFieldName.requestFocus();
				}
				else {
					Stage stage = (Stage) btn.getScene().getWindow();
					stage.close();
				}
			}
		}
	};
	
	private void addMessage(String message, String styleClass){
		HBox messageBox = new HBox();
		messageBox.setPadding(new Insets(10, 10, 10, 10));
		LoggedLabel messageLabel = UiFactory.createLabel("messageLabel");
		messageLabel.setText(message);
		messageLabel.setWrapText(true);
		messageLabel.setPrefWidth(400);
		messageLabel.setAlignment(Pos.CENTER);
		Tooltip tooltip = new Tooltip(message);
		tooltip.getStyleClass().add("display-label");
		messageLabel.setTooltip(tooltip);
		messageLabel.getStyleClass().add(styleClass);
		messageBox.getChildren().add(messageLabel);
		messageBox.setAlignment(Pos.CENTER);
		mainBox.getChildren().add(messageBox);
	}
	
	public String getClickedBtnName() {
		return StringUtils.trimToEmpty(clickedBtnName);
	}
}
