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
 * <h3>ReasonForChangeMaintDialog Class description</h3>
 * <p>
 * ReasonForChangeMaintDialog description
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
 *         November 3, 2020
 *
 */

public class ReasonForChangeMaintDialog extends FxDialog{
	
	private LoggedButton createBtn;
	private LoggedButton updateBtn;
	private LoggedButton cancelBtn;
	private LoggedTextField textField;
	private String message;
	private String clickedBtnName;
	private VBox mainBox = new VBox();
	private String label;
	private boolean isUpdate;
	
	public ReasonForChangeMaintDialog() {
		super("Reason for Change Maintenance", ClientMainFx.getInstance().getStage());
	}

	public ReasonForChangeMaintDialog(String applicationId) {
		super("Reason for Change Maintenance", ClientMainFx.getInstance().getStage(applicationId));
		this.initStyle(StageStyle.DECORATED);
	}
	
	public ReasonForChangeMaintDialog(Stage stage) {
		super("Reason for Change Maintenance", stage);
		this.initStyle(StageStyle.DECORATED);
	}
		
	public boolean showReasonForChangeMaintDialog(String message, String label, boolean isUpdate) {
		this.message = message;
		this.label = label;
		this.isUpdate = isUpdate;
		initComponents();
		showDialog();
		if (!getClickedBtnName().equalsIgnoreCase(QiConstant.CANCEL) && !StringUtils.isBlank(getText())) {
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
		textField = UiFactory.createTextField("textField", 128, TextFieldState.EDIT);
		textField.setPrefWidth(300);
		textField.addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(128));
		
		HBox labelBox = new HBox();
		LoggedLabel label = UiFactory.createLabel("label", this.label);
		label.getStyleClass().add("display-label");
		LoggedLabel asterisk = UiFactory.createLabel("asteriskReasonForChange", "*");
		asterisk.getStyleClass().add("display-label");
		asterisk.setStyle("-fx-text-fill: red");
		labelBox.setPadding(new Insets(15, 5, 5, 5));
		labelBox.getChildren().addAll(label, asterisk);

		textFieldHBox.setPadding(new Insets(10, 10, 10, 10));
		textFieldHBox.setSpacing(10);
		textFieldHBox.getChildren().addAll(labelBox, textField);
		textFieldHBox.setAlignment(Pos.CENTER_LEFT);
		textFieldHBox.setMinHeight(68);
		
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
		
		mainBox.getChildren().addAll(textFieldHBox, btnHBox);
		((BorderPane) this.getScene().getRoot()).setCenter(mainBox);
	}
	
	private LoggedButton createBtn(String text)	{
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.defaultButtonProperty().bind(btn.focusedProperty());
		btn.setOnAction(handler);
		btn.getStyleClass().add("popup-btn");
		return btn;
	}
	
	public String getText()	{
		return StringUtils.trim(textField.getText());
	}
	
	private EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {

		public void handle(ActionEvent event) {
			Object source = event.getSource();
			if (source instanceof LoggedButton) {
				LoggedButton btn = (LoggedButton) source;
				clickedBtnName = btn.getText();
				if(StringUtils.isBlank(getText()) 
						&& !getClickedBtnName().equals(QiConstant.CANCEL)) {
						textField.requestFocus();
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
