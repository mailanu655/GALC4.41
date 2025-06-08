package com.honda.galc.client.teamleader.qi.stationconfig.entryscreen;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
/**
 * <h3>SetSequenceDialog Class description</h3>
 * <p> SetSequenceDialog description </p>
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
 * @author L&T Infotech<br>
 * June 8, 2017
 *
 */
public class SetSequenceDialog extends FxDialog{

	private LoggedLabel entryModelValueLabel;
	private LoggedLabel entryScreenValueLabel;
	private LoggedLabel imageNameValueLabel;
	private LabeledUpperCaseTextField setSequenceNoTextField;
	private QiEntryScreenDto qiEntryScreenDto;
	private LoggedButton okBtn;
	private LoggedButton cancelBtn;	
	private String buttonClickedname;
	private MigPane pane;
	private int maxSeqValue;
	private LoggedLabel messageLabel;

	public SetSequenceDialog(String title, String owner,QiEntryScreenDto qiEntryScreenDto ,int maxSize) {
		super(title, owner);
		this.qiEntryScreenDto=qiEntryScreenDto;
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.maxSeqValue=maxSize;
		initComponents();
	}

	private void initComponents() {
		pane = new MigPane("insets 10 10 10 10", "[center,grow,fill]", "10[]10[]10");
		pane.add(createTitiledPane("Current Combination", createCurrentCombinationPanel()),"wrap");
		pane.add(createTitiledPane("Set Sequence No", repairAreaDataPanel()),"wrap");
		addMessage(StringUtils.EMPTY,"error-message");
		pane.setPrefSize(Screen.getPrimary().getVisualBounds().getWidth()*0.4,Screen.getPrimary().getVisualBounds().getHeight()*0.55);
		((BorderPane) this.getScene().getRoot()).setCenter(pane);
	}

	private MigPane createCurrentCombinationPanel(){
		MigPane pane = new MigPane("", // Layout Constraints
				"47[]45[]", // Column constraints
				"10[]20[]");

		LoggedLabel entryModelLabel = UiFactory.createLabel("entryModel", "Entry Model");
		entryModelLabel.getStyleClass().add("display-label");
		entryModelValueLabel =new LoggedLabel();
		entryModelValueLabel.setText(qiEntryScreenDto.getEntryModel());

		LoggedLabel entryScreenLabel = UiFactory.createLabel("entryScreen", "Entry Screen");
		entryScreenLabel.getStyleClass().add("display-label");
		entryScreenValueLabel =new LoggedLabel();
		entryScreenValueLabel.setText(qiEntryScreenDto.getEntryScreen());

		LoggedLabel imageNameLabel = UiFactory.createLabel("imageName", "Image Name");
		imageNameLabel.getStyleClass().add("display-label");
		imageNameValueLabel =new LoggedLabel();
		imageNameValueLabel.setText(qiEntryScreenDto.getImageName());

		pane.add(entryModelLabel,"align left");
		pane.add(entryModelValueLabel,"wrap");
		pane.add(entryScreenLabel,"align left");
		pane.add(entryScreenValueLabel,"wrap");
		pane.add(imageNameLabel,"align left");
		pane.add(imageNameValueLabel,"wrap");
		return pane;
	}

	private MigPane repairAreaDataPanel(){

		MigPane pane = new MigPane("insets 10 10 10 10", "", "");      
		HBox buttonContainer = new HBox();

		setSequenceNoTextField = new LabeledUpperCaseTextField("Set Sequence #", "Sequence #", 10, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT, true,new Insets(10,10,10,10));
		setSequenceNoTextField.getLabel().setPadding(new Insets(10,20,10,40));
		setButtonContainer(buttonContainer);
		sequenceListener();
		setSequenceNoTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(64));
		pane.add(setSequenceNoTextField,"wrap");
		pane.add(buttonContainer,"span,wrap");
		return pane;
	}

	private TitledPane createTitiledPane(String title,Node content) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(content);
		if("Current Combination".equals(title)){
			titledPane.setPrefSize(500, 150);
		}
		else{
			titledPane.setPrefSize(500,250);
		}
		return titledPane;
	}

	private void setButtonContainer(HBox buttonContainer) {
		okBtn = createBtn("Ok");
		cancelBtn = createBtn(QiConstant.CANCEL);		
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setPadding(new Insets(10, 10, 10, 140));
		buttonContainer.setSpacing(20);
		buttonContainer.getChildren().addAll(okBtn, cancelBtn);
	}

	private LoggedButton createBtn(String text)
	{
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.defaultButtonProperty().bind(btn.focusedProperty());
		btn.setOnAction(handler);
		btn.getStyleClass().add("popup-btn");
		return btn;
	}

	private EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent event) {
			Object source = event.getSource();

			if (source instanceof LoggedButton) {
				LoggedButton btn = (LoggedButton) source;
				int seqValue =1 ;
				if(!StringUtils.isEmpty(getSetSequenceNoTextField().getText()) && StringUtils.isNumeric(getSetSequenceNoTextField().getText()))
					seqValue = Integer.valueOf(getSetSequenceNoTextField().getText());
				buttonClickedname = btn.getText();
				if(getButtonClickedname().equalsIgnoreCase("Ok"))
				{
					if(StringUtils.isBlank(getSetSequenceNoTextField().getText())){
						messageLabel.setText("Please enter Sequence #");
					}else if(seqValue > maxSeqValue){
						messageLabel.setText("Cannot enter value greater than "+ maxSeqValue);
					}else{
						Stage stage = (Stage) btn.getScene().getWindow();
						stage.close();	
					}
				}else
				{
					Stage stage = (Stage) btn.getScene().getWindow();
					stage.close();
				}
			}
		}
	};

	public boolean showSetSequenceDialog(String message)
	{
		return (getButtonClickedname().equalsIgnoreCase("Ok") && !StringUtils.isBlank(getSetSequenceNoTextField().getText()));
	}



	public String getButtonClickedname() {
		return buttonClickedname;
	}

	public void setButtonClickedname(String buttonClickedname) {
		this.buttonClickedname = buttonClickedname;
	}

	public LabeledUpperCaseTextField getSetSequenceNoTextField() {
		return setSequenceNoTextField;
	}

	private void addMessage(String message, String styleClass){
		MigPane messageBox = new MigPane("insets 10 10 10 10", "[center,grow,fill]", "");
		messageLabel= UiFactory.createLabel("messageLabel");
		messageLabel.setText(message);
		messageLabel.setWrapText(true);
		messageLabel.setPrefWidth(400);
		messageLabel.setAlignment(Pos.CENTER);
		Tooltip tooltip = new Tooltip(message);
		tooltip.getStyleClass().add("display-label");
		messageLabel.setTooltip(tooltip);
		messageLabel.getStyleClass().add(styleClass);
		messageBox.getChildren().add(messageLabel);
		pane.add(messageBox);
	}

	private void sequenceListener(){
		setSequenceNoTextField.getControl().textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				if(hasSpecialCharacters(setSequenceNoTextField.getControl())){
					setSequenceNoTextField.getControl().clear();
					setSequenceNoTextField.getControl().settext(oldValue);
				}
				else if(!StringUtils.isNumeric(setSequenceNoTextField.getText())){
					messageLabel.setText("Characters are not allowed ");
					setSequenceNoTextField.getControl().clear();
					setSequenceNoTextField.getControl().settext(oldValue);
				}
				else if(setSequenceNoTextField.getText().length() >5){
					messageLabel.setText("Value cannot be greater than 5");
					setSequenceNoTextField.getControl().settext(oldValue);
				}
				else if((Integer.parseInt(setSequenceNoTextField.getText())) == 0){
					messageLabel.setText("Sequence Cannot be zero");
					setSequenceNoTextField.getControl().settext(oldValue);
				}
			}
		});

	}

	/**
	 * This method is used to check the special character in  TextBox
	 * @param textField
	 * @return
	 */
	public boolean hasSpecialCharacters(final Object textField){
		if(QiCommonUtil.hasSpecialCharacters(textField)){
			messageLabel.setText("Special characters are not allowed ");
			return true;
		}
		return false;
	}

}
