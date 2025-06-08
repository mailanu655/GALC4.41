package com.honda.galc.client.ui.component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.product.entry.SearchByRepairAreaModel;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.entity.qi.QiRepairArea;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.service.property.PropertyService;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import javafx.stage.WindowEvent;
import javafx.util.Callback;

public class ManualRepairAreaEntryDialog extends AbstractManualEntryDialog implements
EventHandler<javafx.event.ActionEvent> {

	@FXML
	private TableColumn<QiRepairArea, String> repairAreaCol;
	
	@FXML
	private TableColumn<QiRepairArea, String> repairAreaDescriptionCol;
	
	@FXML
	private TableColumn<QiRepairArea, String> locationCol;

	@FXML
	private TextField inputTxtField;

	@FXML
	private GridPane searchGridPane;

	@FXML
	private Button selectBtn;

	@FXML
	private RadioButton repairAreaRdBtn;

	@FXML
	private RadioButton productIdRdBtn;

	@FXML
	private RadioButton locationRdBtn;
	
	@FXML
	private ComboBox<String> locations;

	@FXML
	private ToggleGroup toggleGroup;

	@FXML
	private Button searchBtn;

	@FXML
	private TableView<QiRepairArea> repairAreaTblView;

	@FXML
	private TableColumn<QiRepairArea, String> noCol;

	private ProductTypeData productTypeData;

	private String returnRepairArea = "";

	private int searchTextMinLength;

	private SearchByRepairAreaModel model;

	private static final String RESULT_TYPE_NAME = "RepairArea";
	
	private static final String LOCATION_INSIDE = "I - Inside";
	private static final String LOCATION_OUTSIDE = "O - Outside";
	private static final String LOCATION_INTRANSIT = "T - InTransit";

	public ManualRepairAreaEntryDialog(String title,
			ProductTypeData productTypeData, String applicationId) {
		super(title, applicationId);
		this.productTypeData = productTypeData;
		this.model = new SearchByRepairAreaModel(ClientMainFx.getInstance().getApplicationContext());
		loadFXML();
		selectBtn.setDisable(true);
		initcomponents();
		locations.setVisible(false);
		initConnections();
		searchTextMinLength = PropertyService.getPropertyBean(ProductPropertyBean.class, getProcessPointId()).getProductSearchMinLength();
	}

	private void initcomponents() {
		repairAreaTblView.setPlaceholder(new Label("No resultsFound"));
		repairAreaRdBtn.setUserData(repairAreaRdBtn.getText());
		productIdRdBtn.setUserData(productIdRdBtn.getText());
		// add ability to double-click a row to select it
		repairAreaTblView.setRowFactory(new Callback<TableView<QiRepairArea>,TableRow<QiRepairArea>>() {
			@Override
			public TableRow<QiRepairArea> call(TableView<QiRepairArea> tableView) {
				final TableRow<QiRepairArea> row = new TableRow<QiRepairArea>();
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

		repairAreaTblView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiRepairArea>() {
			public void changed(
					ObservableValue<? extends QiRepairArea> arg0,
					QiRepairArea oldValue,
					QiRepairArea newValue) {
				if(null != newValue)
				{
					selectBtn.setDisable(false);
					Logger.getLogger().check("Repair Area : "+ newValue.getRepairAreaName() + " selected from the list");
				}
				else
					selectBtn.setDisable(true);
			}
		});

		noCol.setCellValueFactory(new Callback<CellDataFeatures<QiRepairArea, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(
					CellDataFeatures<QiRepairArea, String> p) {
				return new ReadOnlyObjectWrapper<String>(repairAreaTblView
						.getItems().indexOf(p.getValue())+1 + "");
			}
		});

		repairAreaCol.setCellValueFactory(new PropertyValueFactory<QiRepairArea, String>("repairAreaName"));
		
		repairAreaDescriptionCol.setCellValueFactory(new PropertyValueFactory<QiRepairArea, String>("repairAreaDescription"));
		
	    locationCol.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<QiRepairArea, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(
							TableColumn.CellDataFeatures<QiRepairArea, String> repairArea) {
						char location = repairArea.getValue().getLocation();
						if(Character.valueOf(location).compareTo('I') == 0) {
							return new ReadOnlyObjectWrapper<String>("Inside");
						}else if(Character.valueOf(location).compareTo('O') == 0) {
							return new ReadOnlyObjectWrapper<String>("Outside");
						}else if(Character.valueOf(location).compareTo('T') == 0) {
							return new ReadOnlyObjectWrapper<String>("InTransit");
						}else return new ReadOnlyObjectWrapper<String>("");
					}
			});
	
		productIdRdBtn.setText(productTypeData.getProductIdLabel());
		productIdRdBtn.setFocusTraversable(false);
		repairAreaRdBtn.setFocusTraversable(false);
		locationRdBtn.setFocusTraversable(false);

		Node selectedNode = (Node) toggleGroup.getSelectedToggle();
		
		searchGridPane.getChildren().remove(searchBtn);
		searchGridPane.add(searchBtn, (GridPane.getColumnIndex(selectedNode) == null ? 0 :
			GridPane.getColumnIndex(selectedNode)) + 2, GridPane.getRowIndex(selectedNode));
		
		repairAreaRdBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) { inputTxtField.requestFocus(); }
		});

		productIdRdBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) { inputTxtField.requestFocus(); }
		});

		locationRdBtn.addEventHandler(MouseEvent.MOUSE_CLICKED,  new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) { locations.requestFocus();}
		});
		
		locations.getItems().addAll(getLocationList());

		locations.setStyle("-fx-font-size: 12px");


		locations.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observalble, String oldVal, String newVal) {

				List<? extends QiRepairArea> repairAreaList = null;
				String locationString = locations.getSelectionModel().getSelectedItem();
				char location = ' ';
				if(locationString.compareTo(LOCATION_INSIDE) == 0) location = 'I';
				else if(locationString.compareTo(LOCATION_OUTSIDE) == 0) location = 'O';
				else if(locationString.compareTo(LOCATION_INTRANSIT) == 0) location = 'T';
				repairAreaList = model.findRepairAreaByLocation(getProcessPointId(), location);
				repairAreaTblView.getItems().clear();
				if(repairAreaList !=null)
					repairAreaTblView.getItems().addAll(repairAreaList);

			}
		});

		super.initComponents();
	}
	
	private List<String> getLocationList(){
		List<String> locationList = new ArrayList<String>(3);
		locationList.add(LOCATION_INSIDE);
		locationList.add(LOCATION_OUTSIDE);
		locationList.add(LOCATION_INTRANSIT);
		
		return locationList;
	}
	

	@Override
	protected void initConnections() {
		searchBtn.setOnAction(this);
		toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable,
					Toggle oldValue, Toggle newValue) {
				searchGridPane.getChildren().remove(inputTxtField);
				searchGridPane.getChildren().remove(searchBtn);
				searchGridPane.getChildren().remove(locations);
				inputTxtField.clear();
				if(newValue.equals(repairAreaRdBtn) || newValue.equals(productIdRdBtn)) {
					searchGridPane.add(inputTxtField, (GridPane.getColumnIndex((Node) newValue) == null ? 0 : GridPane.getColumnIndex((Node) newValue)) + 1, GridPane.getRowIndex((Node) newValue));
					searchGridPane.add(searchBtn, (GridPane.getColumnIndex((Node) newValue) == null ? 0 : GridPane.getColumnIndex((Node) newValue)) + 2, GridPane.getRowIndex((Node) newValue));
				}
				
				if(newValue.equals(locationRdBtn)){
					searchGridPane.add(locations, (GridPane.getColumnIndex((Node) newValue) == null ? 0 : GridPane.getColumnIndex((Node) newValue)) + 1, GridPane.getRowIndex((Node) newValue));
					locations.setVisible(true);
					GridPane.setValignment(locations, VPos.CENTER);
				}
			}

		});
		selectBtn.setOnAction(this);
		this.setOnShown(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				inputTxtField.requestFocus();
			}
		});

	}

	@Override
	public void handle(ActionEvent event) {
		List<? extends QiRepairArea> repairAreaList = null;
		if (event.getSource() == searchBtn) {
			Toggle selectedToggle = toggleGroup.getSelectedToggle();
			if (validateInput(selectedToggle))
			{
				if(selectedToggle.equals(productIdRdBtn)){
					repairAreaList = model.findRepairAreaByProductId(getProcessPointId(),inputTxtField.getText().trim().toUpperCase());
				} else if(selectedToggle.equals(repairAreaRdBtn)){ 
					repairAreaList = model.findRepairAreaByRepairAreaName(getProcessPointId(),inputTxtField.getText().trim().toUpperCase());
				} else if(selectedToggle.equals(locationRdBtn)) {
					String locationString = locations.getSelectionModel().getSelectedItem();
					char location = ' ';
					if(locationString.compareTo(LOCATION_INSIDE) == 0) location = 'I';
					else if(locationString.compareTo(LOCATION_OUTSIDE) == 0) location = 'O';
					else if(locationString.compareTo(LOCATION_INTRANSIT) == 0) location = 'T';
					repairAreaList = model.findRepairAreaByLocation(getProcessPointId(), location);
				}

				Logger.getLogger().check("Action Performed on Search Button");
			}
		} else if (event.getSource() == selectBtn) {
			if(repairAreaTblView.getSelectionModel().getSelectedItem() == null) {
				MessageDialog.showError(this, "Please Select a Product");
				return;
			}
			this.returnRepairArea = repairAreaTblView.getSelectionModel().getSelectedItem().getRepairAreaName();
			this.close();
		}
		repairAreaTblView.getItems().clear();
		if(repairAreaList !=null)
			repairAreaTblView.getItems().addAll(repairAreaList);
	}

	private boolean validateInput(Toggle selectedToggle){
		if (selectedToggle.equals(productIdRdBtn)) {
			if (inputTxtField.getText().trim().length() < searchTextMinLength){
				String msgWarning = productTypeData.getProductIdLabel() + " search field must have at least " + searchTextMinLength + " characters.";
				MessageDialog.showError(this, msgWarning);
				return false;
			}
		} else if (selectedToggle.equals(repairAreaRdBtn)) {
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

	public String getResultRepairArea() {
		return this.returnRepairArea;
	}

	@Override
	protected String getResultTypeName() {
		return RESULT_TYPE_NAME;
	}
}
