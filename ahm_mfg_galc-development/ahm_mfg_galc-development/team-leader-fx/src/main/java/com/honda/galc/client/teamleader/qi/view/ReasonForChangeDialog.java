package com.honda.galc.client.teamleader.qi.view;

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

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ReasonForChangeDialog</code> is the class for Reason for Change Dialog.
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>L&T Infotech</TD>
 * <TD>14/07/2016</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */

public class ReasonForChangeDialog extends FxDialog{
	
	private LoggedButton okBtn;
	private LoggedButton cancelBtn;
	private LoggedTextArea reasonForChangeTextArea;
	private String message;
	private String buttonClickedname;
	private VBox mainBox = new VBox();
	
	public ReasonForChangeDialog() {
		super("Reason for Change", ClientMainFx.getInstance().getStage());
	}
	//Fix : Passing applicationId as parameter to fetch correct owner stage.
	public ReasonForChangeDialog(String applicationId) {
		super("Reason for Change", ClientMainFx.getInstance().getStage(applicationId));
		this.initStyle(StageStyle.DECORATED);
	}
	
	//Fix : Passing  owner stage. as parameter
	public ReasonForChangeDialog(Stage stage) {
		super("Reason for Change", stage);
		this.initStyle(StageStyle.DECORATED);
	}
		
		
	public boolean showReasonForChangeDialog(String message)
	{
		this.message = message;
		initComponents();
		showDialog();
		if(getButtonClickedname().equalsIgnoreCase("Ok") && !StringUtils.isBlank(getReasonForChangeText()))
			return true;
		else
			return false;
	}
	public void initComponents(){
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		if(message!=null)
		{
			addMessage(message, "display-message");
		}
		
		HBox reasonForChangeContainer = new HBox();
		reasonForChangeTextArea = UiFactory.createTextArea();
		reasonForChangeTextArea.setId("reasonForChangeTextArea");
		reasonForChangeTextArea.setPrefRowCount(4);
		reasonForChangeTextArea.setWrapText(true);
		reasonForChangeTextArea.setPrefWidth(300);
		reasonForChangeTextArea.addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(256));
		
		HBox labelBox = new HBox();
		LoggedLabel reasonForChangeLabel = UiFactory.createLabel("reasonForChangeLabel", "Reason for Change");
		reasonForChangeLabel.getStyleClass().add("display-label");
		LoggedLabel asterisk = UiFactory.createLabel("asteriskReasonForChange", "*");
		asterisk.getStyleClass().add("display-label");
		asterisk.setStyle("-fx-text-fill: red");
		labelBox.getChildren().addAll(reasonForChangeLabel,asterisk);

		reasonForChangeContainer.setPadding(new Insets(10, 10, 10, 10));
		reasonForChangeContainer.setSpacing(10);
		reasonForChangeContainer.getChildren().addAll(labelBox, reasonForChangeTextArea);
		reasonForChangeContainer.setAlignment(Pos.CENTER_LEFT);
		reasonForChangeContainer.setMinHeight(68);
		
		HBox btnContainer = new HBox();
		okBtn = createBtn("Ok");
		cancelBtn = createBtn(QiConstant.CANCEL);
		btnContainer.getChildren().addAll(okBtn, cancelBtn);
		btnContainer.setAlignment(Pos.CENTER);
		btnContainer.setPadding(new Insets(0, 10, 10, 10));
		btnContainer.setSpacing(10);
		
		mainBox.getChildren().addAll(reasonForChangeContainer, btnContainer);
		((BorderPane) this.getScene().getRoot()).setCenter(mainBox);
	}
	
	private LoggedButton createBtn(String text)
	{
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.defaultButtonProperty().bind(btn.focusedProperty());
		btn.setOnAction(handler);
		btn.getStyleClass().add("popup-btn");
		return btn;
	}
	
	public String getReasonForChangeText()
	{
		return StringUtils.trim(reasonForChangeTextArea.getText());
	}
	
	private EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {

		public void handle(ActionEvent event) {
			Object source = event.getSource();
			if (source instanceof LoggedButton) {
				LoggedButton btn = (LoggedButton) source;
				buttonClickedname = btn.getText();
				if(StringUtils.isBlank(getReasonForChangeText()) 
						&& getButtonClickedname().equalsIgnoreCase("Ok"))
				{
					if(mainBox.getChildren().size() < 4)
						addMessage("Please enter Reason for Change", "error-message");
				}
				else
				{
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
	
	public String getButtonClickedname() {
		return StringUtils.trimToEmpty(buttonClickedname);
	}
	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}
	public void setCancelBtn(LoggedButton cancelBtn) {
		this.cancelBtn = cancelBtn;
	}
	public LoggedTextArea getReasonForChangeTextArea() {
		return reasonForChangeTextArea;
	}
	public void setReasonForChangeTextArea(LoggedTextArea reasonForChangeTextArea) {
		this.reasonForChangeTextArea = reasonForChangeTextArea;
	}
	
}
