package com.honda.galc.client.teamleader.schedule.backout;

import java.awt.event.KeyEvent;
import java.lang.reflect.Constructor;
import java.util.Comparator;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.service.property.PropertyService;

public class ProductionLotBackoutPanel extends TabbedPanel {

	private static final String CONTROLLER_CLASS = "CONTROLLER_CLASS";
	private static final Font FONT = new Font(null, 16);

	private ProductionLotBackoutController controller;
	private GridPane productionLotBackoutPane;
	private Label titleLabel;
	private Label lotPrefixLabel;
	private Label lotDateLabel;
	private StackPane lotPrefixTextFieldHolder;
	private StackPane lotDateTextFieldHolder;
	private TextField lotPrefixTextField;
	private TextField lotDateTextField;
	private Button deleteButton;
	private TableView<ProductionLotBackoutDTO> productionLotBackoutTable;
	private ObservableList<ProductionLotBackoutDTO> tableItems;

	public ProductionLotBackoutPanel(MainWindow mainWindow) {
		super("Production Lot Backout", KeyEvent.VK_B, mainWindow);
		initialize();
	}

	public void initialize() {
		controller = createClientController();
		initComponents();
	}

	public ProductionLotBackoutController getController() { return controller; }
	public void setController(ProductionLotBackoutController controller) { this.controller = controller; }

	public GridPane getPanel() {
		if (this.productionLotBackoutPane == null) {
			this.productionLotBackoutPane = new GridPane();
		}
		return this.productionLotBackoutPane;
	}
	public Label getTitleLabel() {
		if (this.titleLabel == null) {
			this.titleLabel = new Label(getMainWindow().getProductType().getProductName() + " Production Lot Backout");
			this.titleLabel.setFont(FONT);
		}
		return this.titleLabel;
	}
	public Label getLotPrefixLabel() {
		if (this.lotPrefixLabel == null) {
			this.lotPrefixLabel = new Label("Lot Prefix:");
			this.lotPrefixLabel.setFont(FONT);
			this.lotPrefixLabel.setLabelFor(getLotPrefixTextField());
		}
		return this.lotPrefixLabel;
	}
	public Label getLotDateLabel() {
		if (this.lotDateLabel == null) {
			this.lotDateLabel = new Label("Lot Date:");
			this.lotDateLabel.setFont(FONT);
			this.lotDateLabel.setLabelFor(getLotDateTextField());
		}
		return this.lotDateLabel;
	}
	public StackPane getLotPrefixTextFieldHolder() {
		if (this.lotPrefixTextFieldHolder == null) {
			this.lotPrefixTextFieldHolder = new StackPane(getLotPrefixTextField());
		}
		return this.lotPrefixTextFieldHolder;
	}
	public StackPane getLotDateTextFieldHolder() {
		if (this.lotDateTextFieldHolder == null) {
			this.lotDateTextFieldHolder = new StackPane(getLotDateTextField());
		}
		return this.lotDateTextFieldHolder;
	}
	public TextField getLotPrefixTextField() {
		if (this.lotPrefixTextField == null) {
			this.lotPrefixTextField = new TextField();
			this.lotPrefixTextField.setFont(FONT);
		}
		return this.lotPrefixTextField;
	}
	public TextField getLotDateTextField() {
		if (this.lotDateTextField == null) {
			this.lotDateTextField = new TextField();
			this.lotDateTextField.setFont(FONT);
		}
		return this.lotDateTextField;
	}
	public Button getDeleteButton() {
		if (this.deleteButton == null) {
			this.deleteButton = new Button("Delete");
			this.deleteButton.setFocusTraversable(false);
			this.deleteButton.setFont(FONT);
		}
		return this.deleteButton;
	}
	@SuppressWarnings("unchecked")
	public TableView<ProductionLotBackoutDTO> getTable() {
		if (this.productionLotBackoutTable == null) {
			// create the table
			this.productionLotBackoutTable = new TableView<ProductionLotBackoutDTO>();
			this.productionLotBackoutTable.getStylesheets().add(this.getClass().getResource("/resource/com/honda/galc/client/schedule/backout/ProductionLotBackoutStyle.css").toExternalForm());
			// create sorting capability for the table
			SortedList<ProductionLotBackoutDTO> sortedItems = new SortedList<ProductionLotBackoutDTO>(getTableItems());
			sortedItems.setComparator(new Comparator<ProductionLotBackoutDTO>() {
				public int compare(ProductionLotBackoutDTO dto1, ProductionLotBackoutDTO dto2) {
					return dto1.getTable().compareTo(dto2.getTable());
				}
			});
			sortedItems.comparatorProperty().bind(this.productionLotBackoutTable.comparatorProperty());
			this.productionLotBackoutTable.setItems(sortedItems);
			this.productionLotBackoutTable.setSelectionModel(new NullTableViewSelectionModel(this.productionLotBackoutTable));
			// create the columns for the table
			TableColumn<ProductionLotBackoutDTO,String> tableCol = createProductionLotBackoutStringColumn("Table", "table");
			tableCol.setMaxWidth(1f * Integer.MAX_VALUE * 20);
			TableColumn<ProductionLotBackoutDTO,Integer> rowsCol = createProductionLotBackoutIntegerColumn("Rows", "rows");
			rowsCol.setMaxWidth(1f * Integer.MAX_VALUE * 10);
			TableColumn<ProductionLotBackoutDTO,String> lotRangeCol = createProductionLotBackoutStringColumn("Info", "info");
			lotRangeCol.setMaxWidth(1f * Integer.MAX_VALUE * 70);
			this.productionLotBackoutTable.getColumns().setAll(tableCol, rowsCol, lotRangeCol);
			this.productionLotBackoutTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			this.productionLotBackoutTable.setFocusTraversable(false);
		}
		return this.productionLotBackoutTable;
	}
	public ObservableList<ProductionLotBackoutDTO> getTableItems() {
		if (this.tableItems == null) {
			Callback<ProductionLotBackoutDTO,Observable[]> callback = new Callback<ProductionLotBackoutDTO,Observable[]>(){
				public Observable[] call(ProductionLotBackoutDTO dto) {
					return new Observable[]{ dto.tableProperty() };
				}
			};
			this.tableItems = javafx.collections.FXCollections.observableArrayList(callback);
		}
		return this.tableItems;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ProductionLotBackoutController createClientController() {
		String controllerClz = PropertyService.getProperty(window.getApplicationContext().getProcessPointId(), CONTROLLER_CLASS);
		if (StringUtils.isEmpty(controllerClz)
				|| controllerClz.equals(ProductionLotBackoutController.class
						.getName()))
			return new ProductionLotBackoutController(this);
		else {
			try {
				Class<? extends ProductionLotBackoutController> forName = (Class<? extends ProductionLotBackoutController>) Class
						.forName(controllerClz);
				Class[] parameterTypes = { ProductionLotBackoutPanel.class };
				Object[] parameters = { this };
				Constructor<? extends ProductionLotBackoutController> constructor = forName
						.getConstructor(parameterTypes);
				return (ProductionLotBackoutController) constructor
						.newInstance(parameters);

			} catch (Exception e) {
				e.printStackTrace();
				getLogger().error(e);
			}
		}

		return null;

	}

	private void initComponents() {
		// Create components
		HBox lotPrefixHBox = new HBox(5);
		HBox lotDateHBox = new HBox(5);
		// Set up constraints
		lotPrefixHBox.getChildren().add(getLotPrefixLabel());
		lotPrefixHBox.getChildren().add(getLotPrefixTextFieldHolder());
		lotDateHBox.getChildren().add(getLotDateLabel());
		lotDateHBox.getChildren().add(getLotDateTextFieldHolder());
		GridPane.setConstraints(getTitleLabel(), 0, 0, 3, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER, new Insets(5,10,5,10));
		GridPane.setConstraints(lotPrefixHBox, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER, new Insets(5,10,5,10));
		GridPane.setConstraints(lotDateHBox, 1, 1, 1, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER, new Insets(5,10,5,10));
		GridPane.setConstraints(getDeleteButton(), 2, 1, 1, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER, new Insets(5,10,5,10));
		GridPane.setConstraints(getTable(), 0, 2, 3, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER, new Insets(5,10,5,10));
		// Set up the window
		getPanel().getChildren().addAll(getTitleLabel(), lotPrefixHBox, lotDateHBox, getDeleteButton(), getTable());
		getPanel().setAlignment(Pos.TOP_CENTER);
		getPanel().setMinWidth(this.getWidth());
		setCenter(getPanel());
	}

	private TableColumn<ProductionLotBackoutDTO,String> createProductionLotBackoutStringColumn(String header, String id) {
		TableColumn<ProductionLotBackoutDTO,String> productionLotBackoutColumn = new TableColumn<ProductionLotBackoutDTO,String>(header);
		productionLotBackoutColumn.setCellValueFactory(new PropertyValueFactory<ProductionLotBackoutDTO,String>(id));
		productionLotBackoutColumn.setCellFactory(createStringCellFactory());
		return productionLotBackoutColumn;
	}

	private TableColumn<ProductionLotBackoutDTO,Integer> createProductionLotBackoutIntegerColumn(String header, String id) {
		TableColumn<ProductionLotBackoutDTO,Integer> productionLotBackoutColumn = new TableColumn<ProductionLotBackoutDTO,Integer>(header);
		productionLotBackoutColumn.setCellValueFactory(new PropertyValueFactory<ProductionLotBackoutDTO,Integer>(id));
		productionLotBackoutColumn.setCellFactory(createIntegerCellFactory());
		return productionLotBackoutColumn;
	}

	private Callback<TableColumn<ProductionLotBackoutDTO,String>, TableCell<ProductionLotBackoutDTO,String>> createStringCellFactory() {
		return new Callback<TableColumn<ProductionLotBackoutDTO,String>, TableCell<ProductionLotBackoutDTO,String>>() {
			public TableCell<ProductionLotBackoutDTO,String> call(TableColumn<ProductionLotBackoutDTO,String> param) {
				return new TableCell<ProductionLotBackoutDTO,String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						this.setText(isEmpty() ? null : item);
					}
				};
			}
		};
	}

	private Callback<TableColumn<ProductionLotBackoutDTO,Integer>, TableCell<ProductionLotBackoutDTO,Integer>> createIntegerCellFactory() {
		return new Callback<TableColumn<ProductionLotBackoutDTO,Integer>, TableCell<ProductionLotBackoutDTO,Integer>>() {
			public TableCell<ProductionLotBackoutDTO,Integer> call(TableColumn<ProductionLotBackoutDTO,Integer> param) {
				return new TableCell<ProductionLotBackoutDTO,Integer>() {
					@Override
					public void updateItem(Integer item, boolean empty) {
						super.updateItem(item, empty);
						this.setText(isEmpty() ? null : String.valueOf(item));
					}
				};
			}
		};
	}

	public void clearTable() {
		getTable().setVisible(false);
		getTableItems().clear();
	}

	public void requestFocusOnLotPrefix() {
		if(getLotPrefixTextField() != null) {
			getLotPrefixTextField().requestFocus();
		}
	}

	public void requestFocusOnLotDate() {
		if(getLotDateTextField() != null) {
			getLotDateTextField().requestFocus();
		}
	}

	public void resetComponents() {
		getLotPrefixTextField().setText(null);
		getLotDateTextField().setText(null);
		getLotPrefixTextField().setDisable(false);
		getLotDateTextField().setDisable(true);
		getDeleteButton().setDisable(true);
		clearTable();
		requestFocusOnLotPrefix();
	}

	public void clearMessage() {
		getMainWindow().clearStatusMessage();
	}

	public void displayMessage(String message, MessageType type) {
		if (type.equals(MessageType.ERROR)){
			getMainWindow().setErrorMessage(message);
		} else {
			getMainWindow().setMessage(message, Color.YELLOW);
		}
	}

	@Override
	public void onTabSelected() {}

}
