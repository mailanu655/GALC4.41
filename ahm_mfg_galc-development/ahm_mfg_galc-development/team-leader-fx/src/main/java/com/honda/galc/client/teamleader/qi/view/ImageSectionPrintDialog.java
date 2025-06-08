package com.honda.galc.client.teamleader.qi.view;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ImageSectionPrintDialog extends FxDialog {

	private LoggedButton okBtn;
	private LoggedButton cancelBtn;
	
	private LoggedRadioButton imgBtn;
	private LoggedRadioButton imgSectionBtn;
	private LoggedRadioButton imgSectionPartNameBtn;
	
	private String buttonClickedname;
	private VBox mainBox = new VBox();

	public ImageSectionPrintDialog(String applicationId) {
		super("Print Option Dialog", applicationId);
	}

	public boolean showPrintOptionDialog() {
		initComponents();
		showDialog();
		if (getButtonClickedname().equalsIgnoreCase("Ok")) {
			return true;
		} else {
			return false;
		}
	}

	public void initComponents() {
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);

		VBox radioBtnContainer = new VBox();

		ToggleGroup radioBtngroup = new ToggleGroup();
		imgSectionPartNameBtn = createRadioButton("Print Image Sections with Part Name", radioBtngroup, true);
		imgSectionBtn = createRadioButton("Print Image Sections without Part Name", radioBtngroup, false);
		imgBtn = createRadioButton("Print Image without Sections", radioBtngroup, false);

		radioBtnContainer.getChildren().addAll(imgSectionPartNameBtn, imgSectionBtn, imgBtn);
		radioBtnContainer.setAlignment(Pos.CENTER_LEFT);
		radioBtnContainer.setSpacing(10);
		radioBtnContainer.setPadding(new Insets(20, 15, 18, 20));

		HBox btnContainer = new HBox();
		okBtn = createBtn("Ok");
		cancelBtn = createBtn(QiConstant.CANCEL);
		btnContainer.getChildren().addAll(okBtn, cancelBtn);
		btnContainer.setAlignment(Pos.CENTER);
		btnContainer.setPadding(new Insets(0, 10, 10, 10));
		btnContainer.setSpacing(10);

		mainBox.getChildren().addAll(radioBtnContainer, btnContainer);
		((BorderPane) this.getScene().getRoot()).setCenter(mainBox);
	}

	/**
	 * This method is used to create Radio Button.
	 * 
	 * @param title
	 * @param group
	 * @param isSelected
	 * @return
	 */
	private LoggedRadioButton createRadioButton(String title, ToggleGroup group, boolean isSelected) {
		LoggedRadioButton radioButton = new LoggedRadioButton(title);
		radioButton.getStyleClass().add("radio-btn");
		radioButton.setToggleGroup(group);
		radioButton.setSelected(isSelected);
		radioButton.setAlignment(Pos.CENTER_LEFT);
		return radioButton;
	}

	private LoggedButton createBtn(String text) {
		LoggedButton btn = UiFactory.createButton(text, text);
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

	public LoggedRadioButton getImgBtn() {
		return imgBtn;
	}

	public LoggedRadioButton getImgSectionBtn() {
		return imgSectionBtn;
	}

	public LoggedRadioButton getImgSectionPartNameBtn() {
		return imgSectionPartNameBtn;
	}

	public String getButtonClickedname() {
		return StringUtils.trimToEmpty(buttonClickedname);
	}
}
