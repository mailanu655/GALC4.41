package com.honda.galc.client.teamleader.qi.stationconfig.clonestation;

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
public class ReplaceDialog extends FxDialog  {

	private LoggedLabel messageLabel;
	private String sectionName;
	private long numRows = 0;
	private LoggedButton okBtn;
	private LoggedButton skipBtn;	
	private String buttonClickedname;
	private MigPane pane;
	private double width;
	private double height;

	public ReplaceDialog(String title, String whichSection, long rowCount) {
		super(title);
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		width = screenBounds.getWidth();
		height = screenBounds.getHeight();
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		sectionName = whichSection;
		numRows = rowCount;
		initComponents();
	}

	private void initComponents() {
		pane = new MigPane("insets 10 10 10 10", "[center,grow,fill]", "");
		VBox myVBox = new VBox();
		myVBox.getChildren().add(createMessageLabel());
		myVBox.getChildren().add(createDetailsLabel());
		myVBox.getChildren().add(createButtons());
		pane.add(createTitledPane(QiConstant.CLONESTN_REPLACE_DIALOG_TITLE, myVBox));
		pane.setPrefSize(width*0.30,height*0.30);
		((BorderPane) this.getScene().getRoot()).setCenter(pane);
	}

	private  LoggedLabel createMessageLabel()  {
		StringBuffer sb = new StringBuffer();
		sb.append(sectionName).append(" already exists for this station.  Overwrite?");
		LoggedLabel messageLabel = UiFactory.createLabel("dialogMessage", sb.toString(), (int)(0.009 * width));
		messageLabel.getStyleClass().add("display-label-warn");
		messageLabel.setWrapText(true);
		messageLabel.setAlignment(Pos.CENTER);
		return messageLabel;
	}

	private  LoggedLabel createDetailsLabel()  {
		StringBuffer sb = new StringBuffer();
		sb.append(numRows).append(" rows exist and will be deleted");
		LoggedLabel messageLabel = UiFactory.createLabel("dialogMessage", sb.toString(), (int)(0.01 * width));
		messageLabel.getStyleClass().add("display-label-warn");
		messageLabel.setWrapText(true);
		messageLabel.setAlignment(Pos.CENTER);
		return messageLabel;
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
		skipBtn = createBtn(QiConstant.SKIP);		
		btnHBox.setAlignment(Pos.CENTER);
		btnHBox.setPadding(new Insets(100,0,0,0));
		btnHBox.setSpacing(20);
		btnHBox.getChildren().addAll(okBtn, skipBtn);
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
