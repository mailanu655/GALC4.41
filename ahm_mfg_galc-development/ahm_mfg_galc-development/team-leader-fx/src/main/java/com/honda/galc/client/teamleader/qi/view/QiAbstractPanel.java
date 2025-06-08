package com.honda.galc.client.teamleader.qi.view;

import com.honda.galc.client.ui.ApplicationMainPane;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public abstract class QiAbstractPanel extends ApplicationMainPane {
	
	private LoggedRadioButton activeRadioBtn;
	private LoggedRadioButton inactiveRadioBtn;
	private LoggedButton panelButton;
	private LoggedTextArea descriptionTextArea;
	
	public QiAbstractPanel(MainWindow window) {
		super(window);
	}

	/** This method is used initialize the panel view.
	 * 
	 * @param panelNode
	 * @param contentNode
	 */
	public void initPanel(Node panelNode, Node contentNode) {
		BorderPane parent=(BorderPane)panelNode;
		parent.setCenter(contentNode);
	}

	/**
	 * This method is used to create Radio Button.
	 * 
	 * @param title
	 * @param group
	 * @param isSelected
	 * @return
	 */
	public LoggedRadioButton createRadioButton(String title, ToggleGroup group, boolean isSelected, EventHandler<ActionEvent> handler) {
		LoggedRadioButton radioButton = new LoggedRadioButton(title);
		radioButton.getStyleClass().add("radio-btn");
		radioButton.setToggleGroup(group);
		radioButton.setSelected(isSelected);
		radioButton.setOnAction(handler);
		return radioButton;
	}
	
	/**
	 * This method is used to return gridPane template
	 * 
	 * @return
	 */
	public GridPane createGridPane() {
		GridPane fieldContainer = new GridPane();
		fieldContainer.setHgap(25);
		fieldContainer.setVgap(25);
		fieldContainer.setPadding(new Insets(100, 100, 100, 100));
		fieldContainer.setAlignment(Pos.CENTER);

		return fieldContainer;
	}

	/** The method which initialize Update and Cancel buttons container
	 *
	 */
	public void getRadioButtons(EventHandler<ActionEvent> event) {
		final ToggleGroup radioGroup = new ToggleGroup();

		activeRadioBtn = createRadioButton(QiConstant.ACTIVE, radioGroup, true, event);
		activeRadioBtn.setPadding(new Insets(5, 80, 0, 0));
		activeRadioBtn.setToggleGroup(radioGroup);

		inactiveRadioBtn = createRadioButton(QiConstant.INACTIVE, radioGroup, false, event);
		inactiveRadioBtn.setPadding(new Insets(5, 80, 0, 0));
		inactiveRadioBtn.setToggleGroup(radioGroup);
	}
	
	/** The method used to create button 
	 *
	 * @param text
	 * @param event
	 */
	public void createButton(String text, EventHandler<ActionEvent> event) {
		panelButton=UiFactory.createButton(text,text);
		panelButton.defaultButtonProperty().bind(panelButton.focusedProperty());
		panelButton.setOnAction(event);
	}
	
	/** The method used to create button 
	 *
	 * @param text
	 * @param event
	 */
	public void createButton(String text, EventHandler<ActionEvent> event, boolean setDisabled) {
		panelButton=UiFactory.createButton(text,text);
		panelButton.defaultButtonProperty().bind(panelButton.focusedProperty());
		panelButton.setOnAction(event);
		panelButton.setDisable(setDisabled);
	}

	/** The method used to initialize description TextArea
	 *
	 */
	public void initDescriptionTextArea(int rowCount ,int width) {
		descriptionTextArea = UiFactory.createTextArea();
		descriptionTextArea.setPrefRowCount(rowCount);
		descriptionTextArea.setWrapText(true);
		descriptionTextArea.setPrefWidth(width);
		descriptionTextArea.addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(256));
	}
	
	/** This method is used to return Container for label which is mandatory in panel
	 * 
	 * @param contextLabel
	 * @return
	 */
	public HBox createLabelContainer(LoggedLabel contextLabel) {
		LoggedLabel asteriskLabel = createLoggedLabel(contextLabel.getText()+"AsteriskLabel", "*", "display-label-14", "-fx-text-fill: red");
		return  (HBox) createBoxContainer(contextLabel, asteriskLabel);
	}	
	
	/** This method is used to return container which contains the provided n number of nodes.
	 * 
	 * @param nodes
	 * @return
	 */
	public Node createBoxContainer(Node... nodes) {
		HBox container = new HBox();
		for (Node currentNode : nodes) {
			container.getChildren().add(currentNode);
		}
		return container;
	}
	
	/**
	 * This method is used to create LoggedLabel.
	 * 
	 * @param id
	 * @param text
	 * @param cssClass
	 * @return LoggedLabel
	 */
	public LoggedLabel createLoggedLabel(String id, String text, String cssClass) {
		LoggedLabel label = UiFactory.createLabel(id, text);
		label.getStyleClass().add(cssClass);
		return label;
	}
	
	/**
	 * This method is used to create LoggedLabel.
	 * 
	 * @param id
	 * @param text
	 * @param cssClass
	 * @return LoggedLabel
	 */
	public LoggedLabel createLoggedLabel(String id, String text, String cssClass, boolean setDisabled) {
		LoggedLabel label = UiFactory.createLabel(id, text);
		label.getStyleClass().add(cssClass);
		label.setDisable(setDisabled);
		return label;
	}
	
	/** This method is used to create LoggedLabel.
	 * 
	 * @param id
	 * @param text
	 * @param cssClass
	 * @param cssStyle
	 * @return LoggedLabel
	 */
	public LoggedLabel createLoggedLabel(String id, String text, String cssClass, String cssStyle) {
		LoggedLabel label = UiFactory.createLabel(id, text);
		label.getStyleClass().add(cssClass);
		label.setStyle(cssStyle);
		return label;
	}

	/**
	 * This method is used to create ComboBox.
	 * @param <T>
	 *  
	 * @param className
	 * @param id
	 * @param width
	 * @param cssStyle
	 * @return comboBox
	 */
	@SuppressWarnings({})
	public <T> ComboBox<T> createComboBox(String id, int width, String cssStyle) {
		ComboBox<T> comboBox = new ComboBox<T>();
		comboBox.setId(id);
		comboBox.setMinWidth(width);
		comboBox.getStyleClass().add(cssStyle);
		return comboBox;
	}
	
	public void clearErrorMessage() {
		super.clearErrorMessage();
	}

	public void setErrorMessage(String errorMessage) {
		super.setErrorMessage(errorMessage);
	}
	
	public void setMessage(String message) {
		super.setMessage(message);
	}
	
	public void setMessage(String message,Color color) {
		super.setMessage(message, color);
	}
	
	public void setStatusMessage(String message) {
		super.setMessage(message);
	}
	
	public LoggedTextArea getDescriptionTextArea() {
		return descriptionTextArea;
	}

	public void setDescriptionTextArea(LoggedTextArea descriptionTextArea) {
		this.descriptionTextArea = descriptionTextArea;
	}
	
	public LoggedButton getPanelButton() {
		return panelButton;
	}

	public void setPanelButton(LoggedButton panelButton) {
		this.panelButton = panelButton;
	}
	
	public LoggedRadioButton getActiveRadioBtn() {
		return activeRadioBtn;
	}

	public void setActiveRadioBtn(LoggedRadioButton activeRadioBtn) {
		this.activeRadioBtn = activeRadioBtn;
	}

	public LoggedRadioButton getInactiveRadioBtn() {
		return inactiveRadioBtn;
	}

	public void setInactiveRadioBtn(LoggedRadioButton inactiveRadioBtn) {
		this.inactiveRadioBtn = inactiveRadioBtn;
	}
}
