package com.honda.galc.client.teamlead.vios;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.UiFactory;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
/**
 * <h3>AbstractViosPanel Class description</h3>
 * <p>
 * Abstract class for Vios Panel
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
 * @author Hemant Kumar<br>
 *        Aug 28, 2018
 */
public abstract class AbstractViosPanel extends BorderPane {
	
	protected IViosPanel iPanel;

	public AbstractViosPanel(IViosPanel iPanel) {
		super();
		this.getStylesheets().add(ViosConstants.CSS_PATH);
		this.iPanel = iPanel;
		this.setCenter(getPanelBody());
		loadData();
	}
	
	public void setErrorMessage(String msg) {
		iPanel.setErrorMessage(msg);
	}
	
	public void clearErrorMessage() {
		iPanel.clearErrorMessage();
	}
	
	public void setInfoMessage(String msg) {
		iPanel.setInfoMessage(msg);
	}
	
	public TitledPane createTitiledPane(String title, Node content) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setFont(Font.font(StringUtils.EMPTY, FontWeight.BOLD, 12));
		titledPane.setContent(content);
		titledPane.setCollapsible(false);
		titledPane.setContentDisplay(ContentDisplay.CENTER);
		return titledPane;
	}
	
	public LoggedButton createBtn(String text) {
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.defaultButtonProperty().bind(btn.focusedProperty());
		btn.setMinWidth(100);
		btn.getStyleClass().add("action-btn");
		return btn;
	}
	
	public LabeledComboBox<String> createLabeledComboBox(String id, String labelName, boolean isHorizontal, boolean isMandatory, boolean isDisabled) {
		LabeledComboBox<String> comboBox = new LabeledComboBox<String>(labelName,isHorizontal,new Insets(0),true,isMandatory);
		comboBox.setId(id);
		comboBox.getControl().setMinHeight(30);
		comboBox.getControl().setPrefWidth(200);
		comboBox.getControl().getStyleClass().add("combo-box-base");
		comboBox.getControl().setDisable(isDisabled);
		comboBox.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.009 * getScreenWidth())));
		comboBox.getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				clearErrorMessage();
			}
		});
		return comboBox;
	}
	
	public double getScreenWidth() {
		return Screen.getPrimary().getVisualBounds().getWidth();
	}
	
	public double getScreenHeight() {
		return Screen.getPrimary().getVisualBounds().getHeight();
	}
	
	public Stage getStage() {
		return iPanel.getStage();
	}
	
	public abstract Node getPanelBody();
	
	public abstract void loadData();
	
	public abstract void onPanelSelected();
	
	public String getUserId() {
		ApplicationContext applicationContext = ClientMainFx.getInstance().getApplicationContext();
		return applicationContext.getUserId();
	}
	
}
