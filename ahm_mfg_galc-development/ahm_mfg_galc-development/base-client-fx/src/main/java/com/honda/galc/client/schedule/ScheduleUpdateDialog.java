package com.honda.galc.client.schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.StringUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ScheduleUpdateDialog extends FxDialog{

	private ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane;
	private ScheduleMainPanel scheduleMainPanel;
	private LoggedButton updateButton;
	private LoggedButton cancelButton;
	
	private LabeledTextField productionLotTextField;
	private LabeledTextField stampCountTextField;
	private LabeledComboBox<String> sendStatusCombobox;
	private LabeledTextField lotSizeTextField;
	private LabeledTextField notesTextField;
	private PreProductionLot preProductionLot;
	private MainWindow mainWindow;
	private LoggedLabel errorMsg;
	
	public ScheduleUpdateDialog(MainWindow mainWindow, ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane, ScheduleMainPanel scheduleMainPanel) {
		super("Edit Production Lot Details");
		this.tablePane = tablePane;
		this.scheduleMainPanel = scheduleMainPanel;
		this.mainWindow = mainWindow;
		initComponents();
		((BorderPane) this.getScene().getRoot()).setBottom(getErrorMsgPane());
		loadData();
		mapActions();
	}

	
	private void mapActions() {
		updateButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent arg0) {
				clearErrorMessage();
				if(StringUtil.isNullOrEmpty(stampCountTextField.getText()))
					setErrorMessage("Stamp Count can not be blank");
				else if(StringUtil.isNullOrEmpty(lotSizeTextField.getText()))
					setErrorMessage("Lot Size can not be blank");
				
				preProductionLot.setStampedCount(Integer.parseInt(stampCountTextField.getText()));
				preProductionLot.setNotes(notesTextField.getText());
				preProductionLot.setLotSize(Integer.parseInt(lotSizeTextField.getText()));
				String sendStatusString = sendStatusCombobox.getControl().getSelectionModel().getSelectedItem();
				preProductionLot.setSendStatusId(Integer.parseInt(sendStatusString.substring(sendStatusString.indexOf("(")+1, sendStatusString.indexOf(")"))));
				ServiceFactory.getDao(PreProductionLotDao.class).update(preProductionLot);
				MessageDialog.showInfo(mainWindow.getStage(), "Data Updated Succesfully");	
				
				if(scheduleMainPanel.isProcessLocationSelected())
					scheduleMainPanel.setDropDownStringValue(preProductionLot.getProcessLocation());
				else
					scheduleMainPanel.setDropDownStringValue(preProductionLot.getPlanCode());
				
				Stage stage = (Stage) updateButton.getScene().getWindow();
				stage.close();
				scheduleMainPanel.getController().retrievePreProductionLots();
				
			}
		});
		
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Stage stage = (Stage) cancelButton.getScene().getWindow();
				stage.close();
			}
		});
		
	}



	private void loadData() {

		MultiValueObject<PreProductionLot> prevLot = tablePane.getSelectedItem();
		this.preProductionLot = prevLot.getKeyObject();
		productionLotTextField.setText(preProductionLot.getProductionLot());
		stampCountTextField.setText(String.valueOf(preProductionLot.getStampedCount()));
		lotSizeTextField.setText(String.valueOf(preProductionLot.getLotSize()));
		notesTextField.setText(preProductionLot.getNotes());
		sendStatusCombobox.getControl().getItems().clear();
		List<String> sendStatusList = new ArrayList<String>();
		for(int i = 0; i <= 3; i++) {
			sendStatusList.add(PreProductionLotSendStatus.getType(i).toString()+"("+i+")");
		}
		Collections.sort(sendStatusList);
		sendStatusCombobox.getControl().getItems().addAll(sendStatusList);
		
		sendStatusCombobox.getControl().getSelectionModel().select(preProductionLot.getSendStatus().toString()+"("+preProductionLot.getSendStatusId()+")");
	}


	private void initComponents() {
		((BorderPane) this.getScene().getRoot()).setCenter(createMainContainer());
	}


	private Node createMainContainer() {
		VBox mainBox = new VBox();
		mainBox.setAlignment(Pos.CENTER);
		mainBox.setSpacing(10);
		mainBox.setPadding(new Insets(10));
		
		mainBox.setMaxHeight(100);
		mainBox.setMaxWidth(500);
		
		HBox buttonBox = new HBox();
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(10);
		buttonBox.setPadding(new Insets(10));

		updateButton = createBtn("UPDATE");
		cancelButton = createBtn("CANCEL");
		buttonBox.getChildren().addAll( updateButton, cancelButton);
		
		VBox prodLotDetailsBox = new VBox();
		prodLotDetailsBox.setAlignment(Pos.CENTER);
		prodLotDetailsBox.setSpacing(10);
		
		productionLotTextField =  new LabeledTextField("Prodution Lot", true, new Insets(0), true, true, false);
		BorderPane.setMargin(productionLotTextField.getControl(), new Insets(0, 0, 0, 10));
		productionLotTextField.getControl().setMinHeight(25);
		productionLotTextField.getControl().setMaxWidth(150);
		BorderPane.setMargin(productionLotTextField, new Insets(10));
		productionLotTextField.setDisable(true);

	
		stampCountTextField = new LabeledTextField("Stamped Count", true, new Insets(0), true, true, false);
		BorderPane.setMargin(stampCountTextField.getControl(), new Insets(0, 0, 0, 10));
		stampCountTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(30));
		stampCountTextField.getControl().setMinHeight(25);
		stampCountTextField.getControl().setMaxWidth(150);
		stampCountTextField.getControl().textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		    	if (!newValue.matches("[-+]?\\d*(\\.\\d*)?")) {
		    		stampCountTextField.getControl().setText(oldValue);
		        }
		    }
		});
		
		lotSizeTextField = new LabeledTextField("Lot Size", true, new Insets(0), true, true, false);
		BorderPane.setMargin(lotSizeTextField.getControl(), new Insets(0, 0, 0, 10));
		lotSizeTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(10000));
		lotSizeTextField.getControl().setMinHeight(25);
		lotSizeTextField.getControl().setMaxWidth(150);
		lotSizeTextField.getControl().textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		    	if (!newValue.matches("[-+]?\\d*(\\.\\d*)?")) {
		    		lotSizeTextField.getControl().setText(oldValue);
		        }
		    }
		});
		
		notesTextField =  new LabeledTextField("Notes", true, new Insets(0), true, false, false);
		BorderPane.setMargin(notesTextField.getControl(), new Insets(0, 0, 0, 10));
		notesTextField.getControl().setMinHeight(25);
		notesTextField.getControl().setMaxWidth(150);
		BorderPane.setMargin(notesTextField, new Insets(10));
		notesTextField.setDisable(false);
		
		sendStatusCombobox = createLabeledComboBox("sendStatusCombobox", "Send Status", true, true, false);
		
		prodLotDetailsBox.getChildren().addAll(productionLotTextField, stampCountTextField,sendStatusCombobox,  lotSizeTextField,notesTextField);
		
		
		mainBox.getChildren().addAll(prodLotDetailsBox, buttonBox);
		

		return mainBox;
	}
	
	public LoggedButton createBtn(String text) {
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.defaultButtonProperty().bind(btn.focusedProperty());
		btn.setMinWidth(100);
		btn.getStyleClass().add("action-btn");
		return btn;
	}
	
	public void clearErrorMessage() {
		errorMsg.setText(StringUtils.EMPTY);
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
	
	public LabeledComboBox<String> createLabeledComboBox(String id, String labelName, boolean isHorizontal, boolean isMandatory, boolean isDisabled) {
		
		LabeledComboBox<String> comboBox = new LabeledComboBox<String>(labelName,isHorizontal,new Insets(0),true,isMandatory);
		comboBox.setId(id);
		comboBox.getControl().setMinHeight(25);
		comboBox.getControl().setMaxWidth(150);
		comboBox.getControl().getStyleClass().add("combo-box-base");
		comboBox.getControl().setDisable(isDisabled);
		comboBox.getControl().setStyle("-fx-font: 12 arial;");
		
		comboBox.getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				clearErrorMessage();
			}
		});
		
		return comboBox;
		
	}

	private HBox getErrorMsgPane() {
		HBox box = new HBox();
		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets(10));
		
		errorMsg = UiFactory.createLabel("errorMsg");
		
		box.getChildren().add(errorMsg);
		return box;
	}
	
	public void setErrorMessage(String msg) {
		errorMsg.setStyle("-fx-font-weight: bold;-fx-font: 15 arial;-fx-text-fill: red;");
		errorMsg.setText(msg);
	}
}
