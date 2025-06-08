package com.honda.galc.client.teamleader.qi.stationconfig.clonestation;

import java.util.ArrayList;
import java.util.List;

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
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
/**
 * <h3>ReplaceDialog Class description</h3>
 * <p> ReplaceDialog description </p>
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
 * @author Vivek Bettada<br>
 * Jan 16, 2018
 *
 */
public class GenericDialog extends FxDialog  {

	private List<String> contents;
	private String subTitle = "";
	private LoggedButton okBtn;
	private LoggedButton cancelBtn;	
	private String buttonClickedname;
	private MigPane pane;
	private double width;
	private double height;

	public GenericDialog(String title, String newSubTitle, List<String> newContent) {
		super(title);
		if(StringUtils.isEmpty(newSubTitle))  {
			subTitle = newSubTitle;
		}
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		width = screenBounds.getWidth();
		height = screenBounds.getHeight();
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		contents = newContent;
		initComponents();
	}

	private void initComponents() {
		pane = new MigPane("insets 10 10 10 10", "[center,grow,fill]", "");
		VBox myVBox = new VBox();
		myVBox.getChildren().addAll(createMessageLabel());
		myVBox.getChildren().add(createButtons());
		pane.add(createTitledPane(subTitle, myVBox));
		pane.setPrefSize(width*0.30,height*0.30);
		((BorderPane) this.getScene().getRoot()).setCenter(pane);
	}

	private  VBox createMessageLabel()  {
		VBox messageLines = new VBox();
		if(contents != null && !contents.isEmpty())  {
			for(String contentLine : contents)  {
				LoggedLabel messageLine = UiFactory.createLabel("dialogMessage", contentLine, (int)(0.009 * width));
				messageLine.getStyleClass().add("display-label-warn");
				messageLine.setWrapText(true);
				messageLine.setAlignment(Pos.CENTER);
				messageLines.getChildren().add(messageLine);
			}
		}
		return messageLines;
	}

	private TitledPane createTitledPane(String title,Node content) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(content);
		titledPane.setPrefSize(0.25*width, 0.25*height);
		return titledPane;
	}

	private HBox createButtons() {
		HBox btnHBox = new HBox();
		okBtn = createBtn("Ok");
		cancelBtn = createBtn(QiConstant.CANCEL);		
		btnHBox.setAlignment(Pos.CENTER);
		btnHBox.setPadding(new Insets(100,0,0,0));
		btnHBox.setSpacing(20);
		btnHBox.getChildren().addAll(okBtn, cancelBtn);
		return btnHBox;
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
				Stage stage = (Stage) btn.getScene().getWindow();
				stage.close();
			}
		}
	};

	public boolean isConfirm()
	{
		return (getButtonClickedname().equalsIgnoreCase("Ok"));
	}



	public String getButtonClickedname() {
		return buttonClickedname;
	}

	public void setButtonClickedname(String buttonClickedname) {
		this.buttonClickedname = buttonClickedname;
	}

}
