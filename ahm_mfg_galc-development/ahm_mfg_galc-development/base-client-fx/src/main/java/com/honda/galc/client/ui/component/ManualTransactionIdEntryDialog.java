package com.honda.galc.client.ui.component;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.data.QiGroupDefectResult;
import com.honda.galc.client.product.entry.SearchByTransactionIdModel;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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

public class ManualTransactionIdEntryDialog extends AbstractManualEntryDialog implements
EventHandler<javafx.event.ActionEvent> {

	@FXML
	private RadioButton defectTypeBtn;

	@FXML
	private RadioButton userBtn;

	@FXML
	private RadioButton transactionIdBtn;
	
	@FXML
	private RadioButton productIdBtn;

	@FXML
	private TextField inputTxtField;

	@FXML
	private CheckBox filterCheckBox;

	@FXML
	private TableColumn<QiGroupDefectResult, String> defectTypeNameCol;

	@FXML
	private TableColumn<QiGroupDefectResult, String> createdUserCol;

	@FXML
	private TableColumn<QiGroupDefectResult, String> totalCol;
	
	@FXML
	private TableColumn<QiGroupDefectResult, String> npfCol;

	@FXML
	private TableColumn<QiGroupDefectResult, String> fixedCol;

	@FXML
	private TableColumn<QiGroupDefectResult, String> outstandingCol;

	@FXML
	private TableColumn<QiGroupDefectResult, String> scrappedCol;

	@FXML
	private TableColumn<QiGroupDefectResult, String> notFixedScrapCol;

	@FXML
	private TableColumn<QiGroupDefectResult, String> noCol;

	@FXML
	private TableColumn<QiGroupDefectResult, Long> transactionIdCol;

	@FXML
	private TableView<QiGroupDefectResult> transactionTblView;

	@FXML
	private ToggleGroup toggleGroup;

	@FXML
	private Button searchBtn;

	@FXML
	private Button selectBtn;

	private static final String RESULT_TYPE_NAME = "Transcation Id";

	private Long resultTransactionId = null;

	private int searchTextMinLength;

	private SearchByTransactionIdModel model;

	public ManualTransactionIdEntryDialog(String title,
			ProductTypeData productTypeData, String applicationId) {
		super(title, applicationId);
		this.model = new SearchByTransactionIdModel(ClientMainFx.getInstance().getApplicationContext());

		loadFXML();
		getSelectBtn().setDisable(true);
		initComponents();
		initConnections();
		searchTextMinLength = PropertyService.getPropertyBean(ProductPropertyBean.class, getProcessPointId()).getProductSearchMinLength();
	}

	@Override
	protected void initComponents() {
		userBtn.setUserData(userBtn.getText());
		defectTypeBtn.setUserData(defectTypeBtn.getText());
		transactionIdBtn.setUserData(transactionIdBtn.getText());
		transactionTblView.setPlaceholder(new Label("No results found"));
		transactionTblView.getSortOrder().add(transactionIdCol);
		productIdBtn.setUserData(productIdBtn.getText());

		transactionTblView.setRowFactory(new Callback<TableView<QiGroupDefectResult>,TableRow<QiGroupDefectResult>>() {
			@Override
			public TableRow<QiGroupDefectResult> call(TableView<QiGroupDefectResult> tableView) {
				final TableRow<QiGroupDefectResult> row = new TableRow<QiGroupDefectResult>();
				row.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (event.getClickCount() == 2 && (!row.isEmpty())) {
							getSelectBtn().fire();
						}
					}
				});
				return row;
			}
		});

		transactionTblView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiGroupDefectResult>() {
			public void changed(
					ObservableValue<? extends QiGroupDefectResult> arg0,
					QiGroupDefectResult oldValue,
					QiGroupDefectResult newValue) {
				if(null != newValue)
				{
					getSelectBtn().setDisable(false);
					Logger.getLogger().check(getResultTypeName() + " : "+ newValue.getDefectTransactionGroupId() + " selected from the list");
				}
				else
					getSelectBtn().setDisable(true);
			}
		});

		noCol.setCellValueFactory(new Callback<CellDataFeatures<QiGroupDefectResult, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(
					CellDataFeatures<QiGroupDefectResult, String> p) {
				return new ReadOnlyObjectWrapper<String>(transactionTblView
						.getItems().indexOf(p.getValue())+1 + "");
			}
		});
		
		if(ProductTypeUtil.isDieCast(ProductType.getType(model.getProductType()))) {
			defectTypeNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(
					cellData.getValue().getDefectTypeName().getDisplayNameShort()));
		} else { 
			defectTypeNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(
					cellData.getValue().getDefectTypeName().getDisplayNameLong()));
		}

		transactionIdCol.setComparator(transactionIdCol.getComparator().reversed());
		transactionIdCol.setCellValueFactory(new PropertyValueFactory<QiGroupDefectResult, Long> ("defectTransactionGroupId"));
		totalCol.setCellValueFactory(new PropertyValueFactory<QiGroupDefectResult, String> ("totalCount"));
		npfCol.setCellValueFactory(new PropertyValueFactory<QiGroupDefectResult, String>("npfCount"));
		fixedCol.setCellValueFactory(new PropertyValueFactory<QiGroupDefectResult, String> ("fixedCount"));
		outstandingCol.setCellValueFactory(new PropertyValueFactory<QiGroupDefectResult, String> ("outstandingCount"));
		scrappedCol.setCellValueFactory(new PropertyValueFactory<QiGroupDefectResult, String> ("scrapCount"));
		notFixedScrapCol.setCellValueFactory(new PropertyValueFactory<QiGroupDefectResult, String> ("notFixedScrapCount"));
		createdUserCol.setCellValueFactory(new PropertyValueFactory<QiGroupDefectResult, String> ("createUser"));

		Node selectedNode = (Node) getToggleGroup().getSelectedToggle();
		getSearchGridPane().getChildren().remove(getSearchBtn());

		getSearchGridPane().add(getSearchBtn(), (GridPane.getColumnIndex(selectedNode) == null ? 0 :
			GridPane.getColumnIndex(selectedNode)) + 2, GridPane.getRowIndex(selectedNode));

		transactionIdBtn.setFocusTraversable(false);

		transactionIdBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) { inputTxtField.requestFocus(); }
		});

		userBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) { inputTxtField.requestFocus(); }
		});

		defectTypeBtn.addEventHandler(MouseEvent.MOUSE_CLICKED,  new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) { inputTxtField.requestFocus();}
		});

		super.initComponents();
	}

	@Override
	protected void initConnections() {
		getSearchBtn().setOnAction(this);
		getSelectBtn().setOnAction(this);

		super.initConnections();
	}

	@Override
	public void handle(ActionEvent event) {
		Map<Long, QiGroupDefectResult> defectListMap = null; 
		if (event.getSource() == searchBtn) {
			Toggle selectedToggle = toggleGroup.getSelectedToggle();
			if (validateInput(selectedToggle)) {
				if(selectedToggle.equals(transactionIdBtn)) {
					defectListMap = createQiGroupDefectResult(model.findAllByGroupTransactionId(Long.parseLong(inputTxtField.getText())));
				} else if (selectedToggle.equals(userBtn)) {
					defectListMap = createQiGroupDefectResult(model.findAllByCreateUser(inputTxtField.getText().toUpperCase()));
				} else if (selectedToggle.equals(defectTypeBtn)) {
					defectListMap = createQiGroupDefectResult(model.findAllByDefectType(inputTxtField.getText().toUpperCase()));
				} else if (selectedToggle.equals(productIdBtn)) {
					defectListMap = createQiGroupDefectResult(getTransactionsByProductId());
				}
				Logger.getLogger().check("Action Performed on Search Button");
				
 				if(filterCheckBox.isSelected()) {
					defectListMap.entrySet().removeIf(e -> (e.getValue().getTotalCount() <= 1 || 
							e.getValue().getOutstandingCount() == 0) ||
							e.getValue().getTotalCount() - (e.getValue().getNpfCount() + e.getValue().getFixedCount() + e.getValue().getScrapCount() +
									e.getValue().getNotFixedScrapCount()) == 0);
					} else {
					defectListMap.entrySet().removeIf(e -> (e.getValue().getTotalCount() <= 1));
				}
			}
		} else {
			if(transactionTblView.getSelectionModel().getSelectedItem() == null) {
				MessageDialog.showError(this, "Please Select a Product");
				return;
			}
			resultTransactionId = transactionTblView.getSelectionModel().getSelectedItem().getDefectTransactionGroupId();
			this.close();
		}
		transactionTblView.getItems().clear();

		if(defectListMap != null && !defectListMap.isEmpty()) {
			for(QiGroupDefectResult defectResults : defectListMap.values()) {
				transactionTblView.getItems().add(defectResults);
			}
			transactionTblView.sort();
		}
		if(defectListMap != null && defectListMap.isEmpty()) {
			String msgWarning = "No results found.";
			MessageDialog.showError(this, msgWarning);
		}
	}

	private List<QiDefectResult> getTransactionsByProductId() {
		List<QiDefectResult> qiDefectResults = new ArrayList<QiDefectResult>();
		BaseProduct product = model.findProduct(inputTxtField.getText().toUpperCase());
		if(product != null) {
			List<QiDefectResult> defectsByProductId = model.findAllByProductId(product.getProductId());
			if(defectsByProductId != null) {
				for(QiDefectResult defectResult : defectsByProductId) {
					qiDefectResults.addAll(model.findAllByGroupTransactionId(defectResult.getDefectTransactionGroupId()));
				}
			}
		}
		return qiDefectResults;
	}

	private Map<Long, QiGroupDefectResult> createQiGroupDefectResult(List<QiDefectResult> qiDefectResultList) {
		Map<Long, QiGroupDefectResult> qiGroupDefectResultMap = new HashMap<Long , QiGroupDefectResult>();
		for(QiDefectResult qiDefectResult : qiDefectResultList) {
			Long id = qiDefectResult.getDefectTransactionGroupId();
			QiGroupDefectResult qiGroupDefectResult= new QiGroupDefectResult(qiDefectResult);

			qiGroupDefectResult = qiGroupDefectResultMap.getOrDefault(id, qiGroupDefectResult);

			qiGroupDefectResult.updateCounts(qiDefectResult);
			qiGroupDefectResultMap.putIfAbsent(id, qiGroupDefectResult);
		}

		return qiGroupDefectResultMap;
	}

	private boolean validateInput(Toggle selectedToggle) {
		if(selectedToggle.equals(transactionIdBtn)) {
			if(!inputTxtField.getText().matches("\\d+")) {
				String msgWarning = selectedToggle.getUserData().toString() + " search field can only contain numeric values";
				MessageDialog.showError(this, msgWarning);
				return false;
			}
		} else if(selectedToggle.equals(userBtn) || selectedToggle.equals(defectTypeBtn) || selectedToggle.equals(productIdBtn)) {
			if (inputTxtField.getText().trim().length() < searchTextMinLength){
				String msgWarning = "Search field is incomplete. Please use the full value for the search";
				MessageDialog.showError(this, msgWarning);
				return false;
			}
		}
		return true;
	}

	protected URL getViewURL() {
		return getClass().getResource(getViewPath());
	}

	public Long getResultTransactionID() {
		return resultTransactionId;
	}

	@Override
	public String getResultTypeName() {
		return RESULT_TYPE_NAME;
	}
}
