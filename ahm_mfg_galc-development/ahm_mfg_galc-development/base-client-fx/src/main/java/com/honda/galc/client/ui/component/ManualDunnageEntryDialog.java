package com.honda.galc.client.ui.component;

import java.net.URL;
import java.util.List;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.product.entry.SearchByDunnageModel;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.Dunnage;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.service.property.PropertyService;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class ManualDunnageEntryDialog extends AbstractManualEntryDialog implements
EventHandler<javafx.event.ActionEvent> {

	@FXML
	private TableColumn<Dunnage, Integer> quanityCol;

	@FXML
	private TableColumn<Dunnage, String> dunnageCol;

	@FXML
	private TableColumn<Dunnage, String> productSpecCodeCol;

	@FXML
	private TextField inputTxtField;

	@FXML
	private GridPane searchGridPane;

	@FXML
	private Button selectBtn;

	@FXML
	private RadioButton dunnageBtn;

	@FXML
	private RadioButton productIdBtn;

	@FXML
	private RadioButton mtocBtn;

	@FXML
	private ToggleGroup toggleGroup;

	@FXML
	private Button searchBtn;

	@FXML
	private TableView<Dunnage> dunnagesTblView;

	@FXML
	private TableColumn<Dunnage, String> noCol;

	private ProductTypeData productTypeData;

	private String resultDunnage = "";

	private int searchTextMinLength;

	private SearchByDunnageModel model;

	private static final String RESULT_TYPE_NAME = "Dunnage";

	public ManualDunnageEntryDialog(String title,
			ProductTypeData productTypeData, String applicationId) {
		super(title, applicationId);
		this.productTypeData = productTypeData;
		this.model = new SearchByDunnageModel(ClientMainFx.getInstance().getApplicationContext());
		loadFXML();
		selectBtn.setDisable(true);
		initcomponents();
		initConnections();
		searchTextMinLength = PropertyService.getPropertyBean(ProductPropertyBean.class, getProcessPointId()).getProductSearchMinLength();
	}

	private void initcomponents() {
		dunnagesTblView.setPlaceholder(new Label("No resultsFound"));
		dunnageBtn.setUserData(dunnageBtn.getText());
		productIdBtn.setUserData(productIdBtn.getText());
		mtocBtn.setUserData(mtocBtn.getText());

		// add ability to double-click a row to select it
		dunnagesTblView.setRowFactory(new Callback<TableView<Dunnage>,TableRow<Dunnage>>() {
			@Override
			public TableRow<Dunnage> call(TableView<Dunnage> tableView) {
				final TableRow<Dunnage> row = new TableRow<Dunnage>();
				row.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (event.getClickCount() == 2 && (!row.isEmpty())) {
							selectBtn.fire();
						}
					}
				});
				return row;
			}
		});

		dunnagesTblView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Dunnage>() {
			public void changed(
					ObservableValue<? extends Dunnage> arg0,
					Dunnage oldValue,
					Dunnage newValue) {
				if(null != newValue)
				{
					selectBtn.setDisable(false);
					Logger.getLogger().check("Dunnage : "+ newValue.getDunnageId() + " selected from the list");
				}
				else
					selectBtn.setDisable(true);
			}
		});

		noCol.setCellValueFactory(new Callback<CellDataFeatures<Dunnage, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(
					CellDataFeatures<Dunnage, String> p) {
				return new ReadOnlyObjectWrapper<String>(dunnagesTblView
						.getItems().indexOf(p.getValue())+1 + "");
			}
		});

		dunnageCol.setCellValueFactory(new PropertyValueFactory<Dunnage, String>("dunnageId"));
		productSpecCodeCol.setCellValueFactory(new PropertyValueFactory<Dunnage, String>("productSpecCode"));
		quanityCol.setCellValueFactory(new PropertyValueFactory<Dunnage, Integer>("expectedQty"));

		dunnageCol.setText("Dunnage");
		dunnageBtn.setFocusTraversable(false);

		productIdBtn.setText(productTypeData.getProductIdLabel());
		productIdBtn.setFocusTraversable(false);

		mtocBtn.setText(productTypeData.getProductSpecCodeLabel());
		mtocBtn.setFocusTraversable(false);

		Node selectedNode = (Node) toggleGroup.getSelectedToggle();
		searchGridPane.getChildren().remove(searchBtn);

		searchGridPane.add(searchBtn, (GridPane.getColumnIndex(selectedNode) == null ? 0 :
			GridPane.getColumnIndex(selectedNode)) + 2, GridPane.getRowIndex(selectedNode));


		dunnageBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) { inputTxtField.requestFocus(); }
		});

		productIdBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) { inputTxtField.requestFocus(); }
		});

		mtocBtn.addEventHandler(MouseEvent.MOUSE_CLICKED,  new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) { inputTxtField.requestFocus();}
		});

		super.initComponents();
	}

	@Override
	protected void initConnections() {
		searchBtn.setOnAction(this);
		selectBtn.setOnAction(this);

		super.initConnections();
	}

	@Override
	public void handle(ActionEvent event) {
		List<? extends Dunnage> dunnageList = null;
		if (event.getSource() == searchBtn) {
			Toggle selectedToggle = toggleGroup.getSelectedToggle();
			if (validateInput(selectedToggle))
			{
				if(selectedToggle.equals(productIdBtn)){
					dunnageList = model.findAllByProductId(inputTxtField.getText().trim().toUpperCase());
				} else if(selectedToggle.equals(dunnageBtn)){ 
					dunnageList = model.findByDunnage(inputTxtField.getText().trim().toUpperCase());
				} else if(selectedToggle.equals(mtocBtn)) {
					dunnageList = model.findByProductSpecCode(inputTxtField.getText().trim().toUpperCase());
				}

				Logger.getLogger().check("Action Performed on Search Button");
			}
		} else {
			if(dunnagesTblView.getSelectionModel().getSelectedItem() == null) {
				MessageDialog.showError(this, "Please Select a Product");
				return;
			}
			resultDunnage = dunnagesTblView.getSelectionModel().getSelectedItem().getDunnageId();
			this.close();
		}
		dunnagesTblView.getItems().clear();
		if(dunnageList !=null)
			dunnagesTblView.getItems().addAll(dunnageList);
	}

	private boolean validateInput(Toggle selectedToggle){
		if (selectedToggle.equals(productIdBtn)) {
			if (inputTxtField.getText().trim().length() < searchTextMinLength){
				String msgWarning = selectedToggle.getUserData().toString() + " search field must have at least " + searchTextMinLength + " characters.";
				MessageDialog.showError(this, msgWarning);
				return false;
			}
		} else if (selectedToggle.equals(mtocBtn) || selectedToggle.equals(dunnageBtn)) {
			if (inputTxtField.getText().trim().length() < 2) {
				String msgWarning = selectedToggle.getUserData().toString() + " search field must have at least 2 characters.";
				MessageDialog.showError(this, msgWarning);
				return false;
			}
		}
		return true;
	}

	@Override
	protected URL getViewURL() {
		return getClass().getResource(getViewPath());
	}

	public String getResultDunnage() {
		return resultDunnage;
	}

	@Override
	protected String getResultTypeName() {
		return RESULT_TYPE_NAME;
	}
}
