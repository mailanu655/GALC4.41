package com.honda.galc.client.ui.component;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import com.honda.galc.client.ClientConstants;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.keypad.control.KeyBoardPopup;
import com.honda.galc.client.ui.keypad.control.KeyBoardPopupBuilder;
import com.honda.galc.client.ui.keypad.robot.RobotFactory;
import com.honda.galc.client.utils.CommonUtil;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.service.property.PropertyService;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.WindowEvent;

public abstract class AbstractManualEntryDialog extends FxDialog {
	@FXML
	private TextField inputTxtField;
	
	@FXML
	private Button searchBtn;
	
	@FXML
	private Button selectBtn;
	
	@FXML
	private GridPane searchGridPane;

	@FXML
	private ToggleGroup toggleGroup;

	private KeyBoardPopup popup;

	public AbstractManualEntryDialog(String title, String applicationId) {
		super(title, applicationId);
	}

	@Override
	public void loadFXML() {
		URL resource = getViewURL();

		assert resource != null;

		FXMLLoader loader = new FXMLLoader(resource);

		loader.setController(this);
		try {
			createKeyBoardPopup();
			AnchorPane pane = (AnchorPane) loader.load();
			final BorderPane borderPane = new BorderPane();
			borderPane.setCenter(pane);
			if(PropertyService.getPropertyBean(ProductPropertyBean.class, ClientMainFx.getInstance().getApplicationContext().getProcessPointId()).isKeyboardButtonVisible()) {
				LoggedButton keyBoardButton = new LoggedButton("Keyboard", "keyBoardButton");
				HBox keyBoardContainer = new HBox();
				keyBoardContainer.getChildren().add(keyBoardButton);
				keyBoardContainer.setAlignment(Pos.CENTER);
				borderPane.setBottom(keyBoardContainer);
				keyBoardButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						if(!popup.isVisible()) {
							CommonUtil.setPopupVisible(true, borderPane, popup);
							inputTxtField.requestFocus();
						}
						else {
							CommonUtil.setPopupVisible(false, null,popup);
						}
					}
				});
			}
			((BorderPane) this.getScene().getRoot()).setCenter(borderPane);
		} catch (LoadException e) {
			if (e.getMessage().compareTo("Root value already specified.") == 0
					|| e.getMessage().compareTo(
							"Controller value already specified.") == 0) {
				String message = String
						.format("Error [%s] encountered when loading the FXML file [%s].\n\n"
								+ "The scene definition must be defined as follows :\n"
								+ "   MUST be contained within a root node\n"
								+ "   MUST NOT define a controller attribute in fx:root.\n\n"
								+ "For Example :\n\n"
								+ "<fx:root type=\"javafx.scene.layout.BorderPane\" xmlns=\"http://javafx.com/javafx/8\" xmlns:fx=\"http://javafx.com/fxml/1\">\n"
								+ "  <center>\n"
								+ "   content .... \n"
								+ "  </center>\n"
								+ "</fx:root>\n\n"
								+ "Please refer to http://docs.oracle.com/javafx/2/fxml_get_started/custom_control.htm for further details\n",
								e.getMessage(), resource);
				MessageDialog.showScrollingInfo(null, message, 10, 50);
			} else {
				MessageDialog.showScrollingInfo(null, e.getMessage(), 10, 50);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected void initComponents() {
		
		// add ability to fire the Search button by pressing the Enter key
		inputTxtField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Logger.getLogger().check("Input Textfield Enter action performed for the search text " + inputTxtField.getText());
				searchBtn.fire(); 
			}
		});
		
		selectBtn.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.TAB) {
					inputTxtField.requestFocus();
					event.consume();
				}
			}
		});
		
		// add ability to fire the Search button by pressing the Enter key
		inputTxtField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Logger.getLogger().check("Input Textfield Enter action performed for the search text " + inputTxtField.getText());
				searchBtn.fire(); 
			}
		});
		
		// add a magnifying glass to the text boxes
		String magnifierStyle;
		StringBuilder magnifierStyleBuilder = new StringBuilder();
		magnifierStyleBuilder.append("-fx-background-image: url(\'");
		magnifierStyleBuilder.append(this.getClass().getResource("/resource/images/common/magnifier.png").toExternalForm());
		magnifierStyleBuilder.append("\');");
		magnifierStyleBuilder.append("-fx-background-repeat: no-repeat;");
		magnifierStyleBuilder.append("-fx-background-position: 3 center;");
		magnifierStyleBuilder.append("-fx-padding: 5 9 5 33;");
		magnifierStyle = magnifierStyleBuilder.toString();
		inputTxtField.setStyle(magnifierStyle);
		inputTxtField.requestFocus();

		// add a search button image to the search button
		String searchBtnStyle;
		StringBuilder searchBtnStyleBuilder = new StringBuilder();
		searchBtnStyleBuilder.append("-fx-background-image: url(\'");
		searchBtnStyleBuilder.append(this.getClass().getResource("/resource/images/common/search.png").toExternalForm());
		searchBtnStyleBuilder.append("\');");
		searchBtnStyleBuilder.append("-fx-background-repeat: no-repeat;");
		searchBtnStyleBuilder.append("-fx-background-position: center center;");
		searchBtnStyle = searchBtnStyleBuilder.toString();
		getSearchBtn().setStyle(searchBtnStyle);

	}
	
	protected void initConnections() {
		
		toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable,
					Toggle oldValue, Toggle newValue) {
				searchGridPane.getChildren().remove(getInputTxtField());
				searchGridPane.getChildren().remove(getSearchBtn());
				getInputTxtField().clear();
				searchGridPane.add(getInputTxtField(), (GridPane.getColumnIndex((Node) newValue) == null ? 0 : GridPane.getColumnIndex((Node) newValue)) + 1, GridPane.getRowIndex((Node) newValue));
				searchGridPane.add(getSearchBtn(), (GridPane.getColumnIndex((Node) newValue) == null ? 0 : GridPane.getColumnIndex((Node) newValue)) + 2, GridPane.getRowIndex((Node) newValue));

				GridPane.setValignment(getInputTxtField(), VPos.CENTER);
			}

		});

		this.setOnShown(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				getInputTxtField().requestFocus();
			}
		});
	}

	protected void createKeyBoardPopup() {
		this.getScene().getStylesheets().add(ClientMainFx.getInstance().getStylesheetPath());
		this.getScene().getStylesheets().add(this.getClass().getResource(ClientConstants.KEYBOARD_CSS_PATH).toExternalForm());
		String fontUrl = this.getClass().getResource(ClientConstants.KEYBOARD_FONT_URL).toExternalForm();
		Font.loadFont(fontUrl, -1);
		popup = KeyBoardPopupBuilder.create().initLayout("numblock").initScale(1.6).initLocale(Locale.ENGLISH).addIRobot(RobotFactory.createFXRobot()).build();
		popup.setX(Screen.getPrimary().getVisualBounds().getWidth()/2);
		popup.setY(Screen.getPrimary().getVisualBounds().getHeight());
		popup.getKeyBoard().setOnKeyboardCloseButton(new EventHandler<Event>() {
			public void handle(Event event) {
				CommonUtil.setPopupVisible(false, null,popup);
			}
		});
		popup.setOwner(this.getScene());
	}

	protected String getProcessPointId(){
		return ClientMainFx.getInstance().getApplicationContext().getProcessPointId();
	}

	protected abstract URL getViewURL();
	
	protected abstract String getResultTypeName();

	public TextField getInputTxtField() {
		return inputTxtField;
	}

	public void setInputTxtField(TextField inputTxtField) {
		this.inputTxtField = inputTxtField;
	}

	public Button getSearchBtn() {
		return searchBtn;
	}

	public Button getSelectBtn() {
		return selectBtn;
	}

	public GridPane getSearchGridPane() {
		return searchGridPane;
	}

	public ToggleGroup getToggleGroup() {
		return toggleGroup;
	}
}
