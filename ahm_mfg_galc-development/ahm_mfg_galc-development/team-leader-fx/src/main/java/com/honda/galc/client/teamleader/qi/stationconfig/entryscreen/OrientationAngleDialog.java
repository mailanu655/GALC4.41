package com.honda.galc.client.teamleader.qi.stationconfig.entryscreen;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.QiEntryScreenDto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
/**
 * <h3>OrientationAngleDialog Class description</h3>
 * <p> OrientationAngleDialog description </p>
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
public class OrientationAngleDialog extends FxDialog{

	private LoggedLabel entryModelValueLabel;
	private LoggedLabel entryScreenValueLabel;
	private LoggedLabel imageNameValueLabel;
	private LabeledComboBox<Short> angleCombobox;
	private QiEntryScreenDto qiEntryScreenDto;
	private LoggedButton okBtn;
	private LoggedButton cancelBtn;	
	private String buttonClickedname;
	private MigPane pane;
	private LoggedLabel messageLabel;

	public OrientationAngleDialog(String title, String owner, QiEntryScreenDto qiEntryScreenDto) {
		super(title, owner);
		this.qiEntryScreenDto=qiEntryScreenDto;
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		initComponents();
		onLoad();
	}

	private void initComponents() {
		pane = new MigPane("insets 10 10 10 10", "[center,grow,fill]", "10[]10[]10");
		pane.add(createTitiledPane("Current Combination", createCurrentCombinationPanel()),"wrap");
		pane.add(createTitiledPane("Set Orientation Angle", angleDataPanel()),"wrap");
		addMessage(StringUtils.EMPTY,"error-message");
		pane.setPrefSize(Screen.getPrimary().getVisualBounds().getWidth()*0.4,Screen.getPrimary().getVisualBounds().getHeight()*0.45);
		((BorderPane) this.getScene().getRoot()).setCenter(pane);
	}

	private void onLoad(){
		ObservableList<Short> angleList = FXCollections.observableArrayList();
		angleList.addAll((short)0, (short)90, (short)180, (short)270);
		angleCombobox.getControl().setItems(angleList);
		angleCombobox.getControl().getSelectionModel().select(Short.valueOf(qiEntryScreenDto.getOrientationAngle()));
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

	private MigPane angleDataPanel(){

		MigPane pane = new MigPane("insets 10 10 10 10", "", "");      
		HBox buttonContainer = new HBox();
		angleCombobox =  createLabeledComboBox("siteComboBox", "Angle", true, true, false);
		setButtonContainer(buttonContainer);

		pane.add(angleCombobox,"wrap,right");
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
			titledPane.setPrefSize(500,180);
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

				buttonClickedname = btn.getText();
				if(getButtonClickedname().equalsIgnoreCase("Ok") && null == angleCombobox.getControl().getSelectionModel().getSelectedItem())
					messageLabel.setText("Please select Orientation Angle");

				else
				{
					Stage stage = (Stage) btn.getScene().getWindow();
					stage.close();
				}
			}  
		}
	};

	public boolean showOrientationAngleDialog()
	{
		return (getButtonClickedname().equalsIgnoreCase("Ok") && null != angleCombobox.getControl().getSelectionModel().getSelectedItem());
	}



	public String getButtonClickedname() {
		return buttonClickedname;
	}

	public void setButtonClickedname(String buttonClickedname) {
		this.buttonClickedname = buttonClickedname;
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
		messageBox.add(messageLabel);
		pane.add(messageBox);
	}




	/**
	 * This method is used to create LabeledComboBox
	 * @param id
	 * @return
	 */

	public LabeledComboBox<Short> createLabeledComboBox(String id, String labelName, boolean isHorizontal, boolean isMandatory, boolean isDisabled) {
		LabeledComboBox<Short> comboBox = new LabeledComboBox<Short>(labelName,isHorizontal,new Insets(0),true,isMandatory);
		comboBox.setId(id);
		comboBox.getControl().setMinHeight(25);
		comboBox.getControl().getStyleClass().add("combo-box-base");
		comboBox.getControl().setDisable(isDisabled);
		return comboBox;
	}

	public Short getSelectedAngle(){
		return angleCombobox.getControl().getSelectionModel().getSelectedItem();
	}

}
