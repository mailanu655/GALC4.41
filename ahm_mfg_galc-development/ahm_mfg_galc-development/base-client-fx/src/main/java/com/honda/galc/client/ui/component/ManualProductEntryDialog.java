package com.honda.galc.client.ui.component;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientConstants;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.keypad.control.KeyBoardPopup;
import com.honda.galc.client.ui.keypad.control.KeyBoardPopupBuilder;
import com.honda.galc.client.ui.keypad.robot.RobotFactory;
import com.honda.galc.client.utils.CommonUtil;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.PreviousLineDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.PreviousLine;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */
public class ManualProductEntryDialog extends FxDialog implements
EventHandler<javafx.event.ActionEvent> {

	private final ProductTypeData productTypeData;

	private final List<String> productTrackingStatusFilter;

	private static final String resourcePath = "/resource/%s.fxml";

	private String returnProductId = "";

	private ObservableList<BaseProduct> productList;

	private String[] shippingLines = null;


	@FXML
	private RadioButton prodLotRdBtn;

	@FXML
	private RadioButton trackStsRdBtn;

	@FXML
	private Button searchBtn;

	@FXML
	private TextField seqRangeTxtField;

	@FXML
	private RadioButton seqRangeRdBtn;

	@FXML
	private TableColumn<BaseProduct, String> prodSpecCodeCol;

	@FXML
	private TableColumn<BaseProduct, String> productIdCol;

	@FXML
	private TableColumn<BaseProduct, String> mcSnCol;

	@FXML
	private TableColumn<BaseProduct, String> dcSnCol;

	@FXML
	private TableColumn<BaseProduct, String> prodLotCol;

	@FXML
	private GridPane searchGridPane;

	@FXML
	private RadioButton seqNoRdBtn;

	@FXML
	private TableView<BaseProduct> productsTblView;

	@FXML
	private TextField inputTxtField;

	@FXML
	private TextField pageTxtField;

	@FXML
	private TableColumn<BaseProduct, Integer> afSeqCol;

	@FXML
	private Button selectBtn;

	@FXML
	private Button decrementPageBtn;

	@FXML
	private Button incrementPageBtn;

	@FXML
	private Label filterLbl;

	@FXML
	private CheckBox filterChkBx;

	@FXML
	private TableColumn<BaseProduct, String> prodDateCol;

	@FXML
	private ToggleGroup toggleGroup;

	@FXML
	private ToggleGroup shipToggleGroup;

	@FXML
	private RadioButton prodIdRdBtn;

	@FXML
	private RadioButton shortVinRdBtn;

	@FXML
	private ComboBox<Line> status;

	@FXML
	private TableColumn<BaseProduct, String> noCol;

	@FXML
	private RadioButton notShippedBtn;
	@FXML
	private RadioButton shippedBtn;
	@FXML
	private Label shippingOptionLabel;

	private KeyBoardPopup popup;

	private int searchTextMinLength;

	private String searchText;

	private String seqRangeSearchText;

	private Toggle searchToggle;

	private int pageNumber;

	private int maxPageNumber;

	private final int pageSize;

	public ManualProductEntryDialog(String title,
			ProductTypeData productTypeData, String applicationId,String searchText) {
		super(title, applicationId);
		this.productTypeData = productTypeData;
		this.productTrackingStatusFilter = findProductTrackingStatusFilter();
		this.searchText = searchText;
		SystemPropertyBean property = PropertyService.getPropertyBean(SystemPropertyBean.class,getProcessPointId());
		shippingLines = property.getProductSearchShippedLineIds();

		loadFXML();
		selectBtn.setDisable(true);
		filterLbl.setVisible(false);
		initcomponents();
		status.setVisible(false);
		initConnections();
		ProductPropertyBean productProperty = PropertyService.getPropertyBean(ProductPropertyBean.class, getProcessPointId());
		searchTextMinLength = productProperty.getProductSearchMinLength();
		pageSize = productProperty.getProductSearchPageSize();

	}

	public ManualProductEntryDialog(String title,
			ProductTypeData productTypeData, String applicationId) {
		this(title,productTypeData,applicationId,null);		
	}

	/**
	 * Returns the whitelist of TRACKING_STATUSes that will not be filtered out,
	 * or returns null if filtering is not enabled.
	 */
	private List<String> findProductTrackingStatusFilter() {
		if (PropertyService.getPropertyBean(ProductPropertyBean.class, getProcessPointId()).isProductSearchFilterEnabled()) { // check if filtering is enabled
			List<PreviousLine> previousLines = ServiceFactory.getDao(PreviousLineDao.class).findAllByProcessPointId(getProcessPointId());
			if (previousLines != null && !previousLines.isEmpty()) {
				List<String> previousLineIds = new ArrayList<String>();
				for (PreviousLine previousLine : previousLines) {
					previousLineIds.add(previousLine.getId().getPreviousLineId());
				}
				return previousLineIds;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private void initcomponents() {
		this.productList = FXCollections.observableArrayList(new Callback<BaseProduct,Observable[]>() {
			@Override
			public Observable[] call(BaseProduct product) {
				return new Observable[] { filterLbl.visibleProperty(), filterChkBx.selectedProperty(), shipToggleGroup.selectedToggleProperty() };
			}
		});
		FilteredList<BaseProduct> filteredList = new FilteredList<BaseProduct>(this.productList);
		filteredList.setPredicate(new Predicate<BaseProduct>() {
			public boolean test(BaseProduct product) {
				if (shippingLines != null && shippingLines.length > 0) {
					List<String> shippingLinesList = Arrays.asList(shippingLines);
					Toggle shipToggle = shipToggleGroup.getSelectedToggle();
					if (product.getTrackingStatus() != null && shippingLinesList.contains(product.getTrackingStatus().trim())) {
						if (!shipToggle.equals(shippedBtn)) return false; // if the product is shipped and the Shipped radio button is not selected, then don't show the product
					} else {
						if(shipToggle.equals(shippedBtn)) return false; // if the product is not shipped and the Shipped radio button is selected, then don't show the product
					}
				}
				if (filterLbl.isVisible() && !filterChkBx.isSelected()) { // check for filtered row if 'Show filtered results' is not checked
					if (productTrackingStatusFilter != null && !productTrackingStatusFilter.contains(product.getTrackingStatus())) {
						return false;
					}
				}
				return true;
			}
		});
		productsTblView.setItems(filteredList);
		productsTblView.setPlaceholder(new Label("No results found"));
		if (productTrackingStatusFilter == null) {
			// add ability to double-click a row to select it
			productsTblView.setRowFactory(new Callback<TableView<BaseProduct>,TableRow<BaseProduct>>() {
				@Override
				public TableRow<BaseProduct> call(TableView<BaseProduct> tableView) {
					final TableRow<BaseProduct> row = new TableRow<BaseProduct>();
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
		} else {
			// add ability to double-click a row to select it, and highlight filtered rows
			productsTblView.setRowFactory(new Callback<TableView<BaseProduct>,TableRow<BaseProduct>>() {
				@Override
				public TableRow<BaseProduct> call(TableView<BaseProduct> tableView) {
					final TableRow<BaseProduct> row = new TableRow<BaseProduct>() {
						@Override
						public void updateItem(BaseProduct item, boolean empty) {
							super.updateItem(item, empty);
							if (item == null) {
								setStyle(null);
							} else if (!productTrackingStatusFilter.contains(item.getTrackingStatus())) {
								java.awt.Color backgroundColor = PropertyService.getPropertyBean(ProductPropertyBean.class, getProcessPointId()).getProductSearchFilterBackgroundColor();
								if (getIndex() % 2 == 0) {
									setStyle(getBackground(backgroundColor));
								} else {
									setStyle(getAltBackground(backgroundColor));
								}
							} else {
								setStyle(null);
							}
						}
						private String getBackground(java.awt.Color backgroundColor) {
							return getBackgroundStyle(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue());
						}
						private String getAltBackground(java.awt.Color backgroundColor) {
							final double modifier = 0.975d;
							int newRed = Math.max(Math.min((int) Math.round(backgroundColor.getRed() * modifier), 255), 0);
							int newGreen = Math.max(Math.min((int) Math.round(backgroundColor.getGreen() * modifier), 255), 0);
							int newBlue = Math.max(Math.min((int) Math.round(backgroundColor.getBlue() * modifier), 255), 0);
							return getBackgroundStyle(newRed, newGreen, newBlue);
						}
						private String getBackgroundStyle(int red, int green, int blue) {
							return String.format("-fx-background-color: #%02X%02X%02X;", red, green, blue);
						}
					};
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
		}

		productsTblView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BaseProduct>() {
			public void changed(
					ObservableValue<? extends BaseProduct> arg0,
					BaseProduct oldValue,
					BaseProduct newValue) {
				if(null != newValue)
				{
					selectBtn.setDisable(false);
					Logger.getLogger().check("Product ID : "+ newValue.getProductId() + " selected from the list");
				}
				else
					selectBtn.setDisable(true);
			}
		});



		if (!productTypeData.getProductType().equals(ProductType.FRAME)) {
			seqRangeRdBtn.setVisible(false);
			seqNoRdBtn.setVisible(false);
			searchGridPane.getRowConstraints().get(GridPane.getRowIndex(seqRangeRdBtn)).setMaxHeight(0);
			searchGridPane.getRowConstraints().get(GridPane.getRowIndex(seqNoRdBtn)).setMaxHeight(0);
			productsTblView.getColumns().remove(afSeqCol);
		}

		noCol.setCellValueFactory(new Callback<CellDataFeatures<BaseProduct, String>, ObservableValue<String>>() {
			@SuppressWarnings("rawtypes")
			@Override
			public ObservableValue<String> call(
					CellDataFeatures<BaseProduct, String> p) {
				return new ReadOnlyObjectWrapper(productsTblView
						.getItems().indexOf(p.getValue())+1 + "");
			}
		});
		afSeqCol.setCellValueFactory(new PropertyValueFactory<BaseProduct, Integer>("afOnSequenceNumber"));
		productIdCol.setCellValueFactory(new PropertyValueFactory<BaseProduct, String>("productId"));
		if(ProductTypeUtil.isDieCast(productTypeData.getProductType())) {
			dcSnCol.setCellValueFactory(new PropertyValueFactory<BaseProduct, String>("dcSerialNumber"));
			mcSnCol.setCellValueFactory(new PropertyValueFactory<BaseProduct, String>("mcSerialNumber"));
		}else {
			productsTblView.getColumns().remove(dcSnCol);
			productsTblView.getColumns().remove(mcSnCol);
		}
		prodSpecCodeCol.setCellValueFactory(new PropertyValueFactory<BaseProduct, String>("productSpecCode"));

		prodDateCol.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<BaseProduct, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(
							TableColumn.CellDataFeatures<BaseProduct, String> product) {
						SimpleStringProperty property = new SimpleStringProperty("");
						DateFormat dateFormat = new SimpleDateFormat(
								"YYYY-MM-dd");
						if (product.getValue() instanceof Product) {
							if(((Product) product.getValue()).getProductionDate() != null){ 
								property.setValue(dateFormat
										.format(((Product) product.getValue())
												.getProductionDate()));
							}
						}
						return property;
					}
				});

		prodLotCol.setCellValueFactory(new PropertyValueFactory<BaseProduct, String>("productionLot"));

		productIdCol.setText(productTypeData.getProductIdLabel());
		prodIdRdBtn.setText(productTypeData.getProductIdLabel());

		// add ability to fire the Search button by pressing the Enter key
		inputTxtField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Logger.getLogger().check("Input Textfield Enter action performed for the search text " + inputTxtField.getText());
				searchBtn.fire(); 
			}
		});
		pageTxtField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				disablePageUi(true); // prevent the user from making multiple page search requests at once
				try {
					int uiPageNumber = Integer.parseInt(pageTxtField.getText());
					setPageNumber(uiPageNumber - 1);
				} catch (NumberFormatException nfe) {
					setPageNumber(0);
				}
			}
		});
		seqRangeTxtField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) { searchBtn.fire(); }
		});

		// limit pageTxtField to digits
		pageTxtField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					pageTxtField.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
		resetPageUi();
		disablePageUi(true);

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
		seqRangeTxtField.setStyle(magnifierStyle);

		// add a search button image to the search button
		String searchBtnStyle;
		StringBuilder searchBtnStyleBuilder = new StringBuilder();
		searchBtnStyleBuilder.append("-fx-background-image: url(\'");
		searchBtnStyleBuilder.append(this.getClass().getResource("/resource/images/common/search.png").toExternalForm());
		searchBtnStyleBuilder.append("\');");
		searchBtnStyleBuilder.append("-fx-background-repeat: no-repeat;");
		searchBtnStyleBuilder.append("-fx-background-position: center center;");
		searchBtnStyle = searchBtnStyleBuilder.toString();
		searchBtn.setStyle(searchBtnStyle);

		// add a search button image to the search button
		String decrementPageBtnStyle;
		StringBuilder decrementPageBtnStyleBuilder = new StringBuilder();
		decrementPageBtnStyleBuilder.append("-fx-background-image: url(\'");
		decrementPageBtnStyleBuilder.append(this.getClass().getResource("/resource/images/common/previous.png").toExternalForm());
		decrementPageBtnStyleBuilder.append("\');");
		decrementPageBtnStyleBuilder.append("-fx-background-repeat: no-repeat;");
		decrementPageBtnStyleBuilder.append("-fx-background-position: center center;");
		decrementPageBtnStyle = decrementPageBtnStyleBuilder.toString();
		decrementPageBtn.setStyle(decrementPageBtnStyle);

		// add a search button image to the search button
		String incrementPageBtnStyle;
		StringBuilder incrementPageBtnStyleBuilder = new StringBuilder();
		incrementPageBtnStyleBuilder.append("-fx-background-image: url(\'");
		incrementPageBtnStyleBuilder.append(this.getClass().getResource("/resource/images/common/next.png").toExternalForm());
		incrementPageBtnStyleBuilder.append("\');");
		incrementPageBtnStyleBuilder.append("-fx-background-repeat: no-repeat;");
		incrementPageBtnStyleBuilder.append("-fx-background-position: center center;");
		incrementPageBtnStyle = incrementPageBtnStyleBuilder.toString();
		incrementPageBtn.setStyle(incrementPageBtnStyle);

		// change the TAB order so that inputTxtField TABs to seqRangeTxtField rather than productsTblView when it is available
		inputTxtField.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (seqRangeRdBtn.isSelected() && event.getCode() == KeyCode.TAB) {
					seqRangeTxtField.requestFocus();
					event.consume();
				}
			}
		});
		seqRangeTxtField.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.TAB) {
					searchBtn.requestFocus();
					event.consume();
				}
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
		Node selectedNode = (Node) toggleGroup.getSelectedToggle();
		searchGridPane.getChildren().remove(searchBtn);

		if (!this.productTypeData.getProductType().equals(ProductType.FRAME)) {
			Integer row = null;
			for (Node node : searchGridPane.getChildren()) {
				if (!StringUtils.isEmpty(node.getId()) && node.getId().equals("shortVinRdBtn")) {
					row = GridPane.getRowIndex(node);
					searchGridPane.getChildren().remove(node);
					break;
				}
			}
			// Shift cells up
			for (Node node : searchGridPane.getChildren()) {
				if (row != null && GridPane.getRowIndex(node) != null &&
						GridPane.getRowIndex(node) > row.intValue())
					GridPane.setRowIndex(node, GridPane.getRowIndex(node)-1);
			}
		}

		searchGridPane.add(searchBtn, (GridPane.getColumnIndex(selectedNode) == null ? 0 : GridPane.getColumnIndex(selectedNode)) + 2, GridPane.getRowIndex(selectedNode));

		// disable focusing on radio buttons and auto-focus on text fields
		// focus-disabled radio buttons are still clickable, so the dialog is still responsive
		shortVinRdBtn.setFocusTraversable(false);
		shortVinRdBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) { inputTxtField.requestFocus(); }
		});
		prodLotRdBtn.setFocusTraversable(false);
		prodLotRdBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) { inputTxtField.requestFocus(); }
		});
		trackStsRdBtn.setFocusTraversable(false);
		trackStsRdBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) { status.requestFocus(); }
		});
		seqRangeRdBtn.setFocusTraversable(false);
		seqRangeRdBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) { inputTxtField.requestFocus(); }
		});
		seqNoRdBtn.setFocusTraversable(false);
		seqNoRdBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) { inputTxtField.requestFocus(); }
		});
		prodIdRdBtn.setFocusTraversable(false);
		prodIdRdBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) { inputTxtField.requestFocus(); }
		});
		//If no property is set in System level or Process point level, remove the shipping option entirely from the Dialog
		if(shippingLines == null || shippingLines.length <=0) {
			shippedBtn.setVisible(false);
			notShippedBtn.setVisible(false);
			shippingOptionLabel.setVisible(false);
		}
		notShippedBtn.setFocusTraversable(false);
		shippedBtn.setFocusTraversable(false);


		List<Line> trackingStatus=findAllTrackingStatus();
		status.getItems().addAll(trackingStatus);
		status.setConverter(new StringConverter<Line>() {
			/** Cache the Line */
			private Map<String, Line> mapLines = new HashMap<String, Line>();

			@Override
			public String toString(Line line) {
				String lineNameAndId = line.getLineName()+"("+line.getLineId()+")";
				mapLines.put(lineNameAndId, line);
				return lineNameAndId;
			}
			@Override
			public Line fromString(String lineNameAndId) {
				return mapLines.get(lineNameAndId);
			}
		});
		status.setStyle("-fx-font-size: 12px");

		if(null!=searchText && searchText.trim().length()>0) {
			List<? extends BaseProduct> productList = null;
			inputTxtField.setText(searchText);
			productList = findByProductId(this.searchText.trim().toUpperCase());
			sortProductList(productList);
			setTableItems(productList);
		}

		status.valueProperty().addListener(new ChangeListener<Line>() {

			@Override
			public void changed(ObservableValue<? extends Line> observalble, Line oldVal, Line newVal) {
				getScene().setCursor(Cursor.WAIT);
				resetPageUi();
				long totalNumber = ProductTypeUtil.getProductDao(productTypeData.getProductType()).countByTrackingStatus(status.getSelectionModel().getSelectedItem().getLineId().toString());
				updateMaxPageNumber(totalNumber);
				Task<Void> searchTask = new Task<Void>() {
					protected Void call() {
						List<? extends BaseProduct> productList = null;
						productList = findByTrackingStatus(status.getSelectionModel().getSelectedItem().getLineId().toString());
						searchText = status.getSelectionModel().getSelectedItem().getLineId().toString();
						searchToggle = trackStsRdBtn;
						sortProductList(productList);
						setTableItems(productList);
						return null;
					}
				};
				setOnTaskExit(searchTask, e -> {
					disablePageUi(false);
					getScene().setCursor(Cursor.DEFAULT);
				});
				new Thread(searchTask).start();
			}
		});
	}

	private void sortProductList(List<? extends BaseProduct> baseProductList) {
		if(baseProductList!=null && baseProductList.size()>0 && baseProductList.get(0) instanceof Product) {
			@SuppressWarnings("unchecked")
			List<Product> productList = (List<Product>) baseProductList;

			if(null!=productList && productList.size()>0) {
				java.util.Collections.sort(
						productList, 
						new Comparator<Product>()  {
							public int compare(Product p1, Product p2)  {
								try {
									if(p1 == null && p2 == null)  return 0;
									else if(p1 == null)  return 1;
									else if(p2 == null)  return -1;
									if(p1.getProductionDate() == null && p2.getProductionDate() == null) return 0;
									else if(p1.getProductionDate() == null) return 1;
									else if(p2.getProductionDate() == null) return -1;
									int myCompare = 0;
									DateFormat dateFormat = new SimpleDateFormat(
											"YYYY-MM-dd");
									myCompare = (dateFormat.format(p2.getProductionDate())).compareTo(dateFormat.format(p1.getProductionDate()));
									if(myCompare==0) {
										Integer seq1 = p1.getAfOnSequenceNumber();
										Integer seq2 = p2.getAfOnSequenceNumber();
										if(seq1 == null && seq2 == null)  myCompare = 0;
										else if(seq1 == null)  myCompare = 1;
										else if(seq2 == null)  myCompare =  (-1);
										else  {
											myCompare = seq1.compareTo(seq2);
										}
									}
									return myCompare;
								} catch (Exception e) {
									Logger.getLogger().error(e);
									return 0;
								}
							}			
						});
			}
		}
	}

	private void createKeyBoardPopup() {
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

	private void initConnections() {
		searchBtn.setOnAction(this);
		toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable,
					Toggle oldValue, Toggle newValue) {
				searchGridPane.getChildren().remove(inputTxtField);
				searchGridPane.getChildren().remove(searchBtn);
				searchGridPane.getChildren().remove(status);
				if (newValue.equals(searchToggle)) {
					disablePageUi(false);
					inputTxtField.setText(searchText);
					inputTxtField.selectAll();
					if(newValue.equals(seqRangeRdBtn)) {
						seqRangeTxtField.setText(seqRangeSearchText);
					}
				} else {
					disablePageUi(true);
					inputTxtField.clear();
					seqRangeTxtField.clear();
				}
				searchGridPane.add(inputTxtField, (GridPane.getColumnIndex((Node) newValue) == null ? 0 : GridPane.getColumnIndex((Node) newValue)) + 1, GridPane.getRowIndex((Node) newValue));
				searchGridPane.add(searchBtn, (GridPane.getColumnIndex((Node) newValue) == null ? 0 : GridPane.getColumnIndex((Node) newValue)) + 2, GridPane.getRowIndex((Node) newValue));
				if(newValue.equals(seqRangeRdBtn)) {
					seqRangeTxtField.setVisible(true); // FIXME: seqRangeTxtField is overlapped by inputTxtField
					GridPane.setValignment(inputTxtField, VPos.TOP);
				} else {
					seqRangeTxtField.setVisible(false);
					GridPane.setValignment(inputTxtField, VPos.CENTER);
				}
				if(newValue.equals(trackStsRdBtn)){
					searchGridPane.getChildren().remove(inputTxtField);
					searchGridPane.getChildren().remove(searchBtn);
					searchGridPane.add(status, (GridPane.getColumnIndex((Node) newValue) == null ? 0 : GridPane.getColumnIndex((Node) newValue)) + 1, GridPane.getRowIndex((Node) newValue));
					status.setVisible(true);
					GridPane.setValignment(status, VPos.CENTER);
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

		decrementPageBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				disablePageUi(true); // prevent the user from making multiple page search requests at once
				decrementPageNumber();
			}
		});

		incrementPageBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				disablePageUi(true); // prevent the user from making multiple page search requests at once
				incrementPageNumber();
			}
		});
	}

	private void resetPageUi() {
		this.pageNumber = 0;
		this.maxPageNumber = Integer.MAX_VALUE;
		pageTxtField.setText("1");
		productList.clear();
	}

	private void updateMaxPageNumber(long total) {
		if(pageSize == 0 || total == 0) {
			maxPageNumber = 0;
		} else if ( total % pageSize == 0) {
			maxPageNumber = (int) total / pageSize - 1; 
		} else {
			maxPageNumber = (int) total / pageSize;
		}
	}
	
	private void disablePageUi(boolean disable) {
		pageTxtField.setDisable(disable);
		decrementPageBtn.setDisable(disable || this.pageNumber <= 0);
		incrementPageBtn.setDisable(disable || this.pageNumber >= this.maxPageNumber);
	}

	private void decrementPageNumber() {
		if (this.pageNumber <= 0)
			throw new UnsupportedOperationException("Decrementing page number below 0 is not supported");
		else
			setPageNumber(this.pageNumber - 1);
	}

	private void incrementPageNumber() {
		if (this.pageNumber >= this.maxPageNumber)
			throw new UnsupportedOperationException("Incrementing page number above the current maximum page number (" + this.maxPageNumber + ") is not supported");
		else
			setPageNumber(this.pageNumber + 1);
	}

	private void setPageNumber(int pageNumber) {
		if (pageNumber < 0)
			this.pageNumber = 0;
		else if (pageNumber > this.maxPageNumber)
			this.pageNumber = this.maxPageNumber;
		else
			this.pageNumber = pageNumber;
		pageTxtField.setText(String.valueOf(this.pageNumber + 1)); // displayed page number is 1-indexed rather than 0-indexed
		searchBtn.fire();
	}
	
	public String getResultProductId() {
		return returnProductId;
	}

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

	public String getViewPath() {
		String fxmlFilepath = String.format(resourcePath, this.getClass()
				.getName().replace(".", "/"));

		return fxmlFilepath;
	}

	private URL getViewURL() {
		return getClass().getResource(getViewPath());
	}



	@SuppressWarnings("unchecked")
	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == searchBtn) {
			getScene().setCursor(Cursor.WAIT);
			final Toggle selectedToggle = toggleGroup.getSelectedToggle();
			final String searchedText = selectedToggle.equals(trackStsRdBtn) ? status.getSelectionModel().getSelectedItem().getLineId().toString() : inputTxtField.getText().trim();
			final String seqRangeSearchedText = selectedToggle.equals(seqRangeRdBtn) ? seqRangeTxtField.getText().trim() : null;
			if (!selectedToggle.equals(searchToggle) || !searchedText.equals(searchText) || (seqRangeSearchedText != null && !seqRangeSearchedText.equals(seqRangeSearchText)))
				resetPageUi();
			Task<Void> searchTask = new Task<Void>() {
				protected Void call() {
					List<? extends BaseProduct> productList = null;
					Logger.getLogger().info("Request received to Search by - "+selectedToggle.toString()+" for value - "+searchedText);
					if (validateInput(selectedToggle, searchedText))
					{
						if(selectedToggle.equals(prodIdRdBtn)){
							productList = findByProductId(searchedText.toUpperCase());
						} 
						else if(selectedToggle.equals(shortVinRdBtn)){ 
							productList = (List<Product>) findByShortVin(searchedText.toUpperCase());
						}
						else if(selectedToggle.equals(prodLotRdBtn)){ 
							productList = findByProductLot(searchedText.toUpperCase());
						}
						else if(selectedToggle.equals(trackStsRdBtn)){
							updateMaxPageNumber(ProductTypeUtil.getProductDao(productTypeData.getProductType()).countByTrackingStatus(searchedText));
							productList = findByTrackingStatus(searchedText);
						}
						else if(selectedToggle.equals(seqNoRdBtn)){
							productList = (List<Product>) findBySeq(searchedText.toUpperCase());

						}
						else if(selectedToggle.equals(seqRangeRdBtn)){
							productList = (List<Product>) findBySeqRange(searchedText.toUpperCase(), seqRangeSearchedText.toUpperCase());
						}
						Logger.getLogger().info("Action Performed on Search Button for - "+searchedText);
						searchToggle = selectedToggle;
						searchText = searchedText;
						seqRangeSearchText = seqRangeSearchedText;
					} else {
						searchToggle = null;
						searchText = null;
						seqRangeSearchText = null;
					}
					if (productList == null)
						maxPageNumber = 0;
					else if (productList.size() < pageSize)
						maxPageNumber = pageNumber;
					sortProductList(productList);
					setTableItems(productList);
					return null;
				}
			};
			setOnTaskExit(searchTask, e -> {
				disablePageUi(false);
				getScene().setCursor(Cursor.DEFAULT);
			});
			new Thread(searchTask).start();
		} else if (event.getSource() == selectBtn) {
			if(productsTblView.getSelectionModel().getSelectedItem() == null) {
				MessageDialog.showError(this, "Please Select a Product");
				return;
			}
			returnProductId = productsTblView.getSelectionModel().getSelectedItem().getProductId();
			this.close();
		}
	}

	private void setTableItems(List<? extends BaseProduct> productList) {
		this.productList.clear();
		if (productList != null) {
			this.productList.addAll(productList);
		}
		checkFilter(productList);
	}

	/**
	 * Shows the filtering check box if required.<br>
	 * Returns true iff filtering is required.
	 */
	private boolean checkFilter(List<? extends BaseProduct> productList) {
		if (productTrackingStatusFilter != null) {
			if (productList != null) {
				for (BaseProduct product : productList) {
					if (!productTrackingStatusFilter.contains(product.getTrackingStatus())) {
						filterLbl.setVisible(true);
						filterChkBx.setSelected(false);
						return true;
					}
				}
			}
			filterLbl.setVisible(false);
			filterChkBx.setSelected(false);
		}
		return false;
	}

	private boolean validateInput(Toggle selectedToggle, String searchedText){
		if (selectedToggle.equals(prodIdRdBtn)) {
			if (searchedText.length() < searchTextMinLength){
				String msgWarning = productTypeData.getProductIdLabel() + " search field must have at least " + searchTextMinLength + " characters.";
				MessageDialog.showError(this, msgWarning);
				return false;
			}
		}
		return true;
	}

	private List<? extends BaseProduct> findByProductId(String sn) {
		if(!StringUtils.isEmpty(productTypeData.getProductType().name()) && !StringUtils.isEmpty(sn)) {
			updateMaxPageNumber(ProductTypeUtil.getProductDao(productTypeData.getProductType()).countByMatchingSN(sn));
			return ProductTypeUtil.getProductDao(productTypeData.getProductType()).findPageBySN(sn, this.pageNumber, this.pageSize);
		} else {
			return new ArrayList<Product>();
		}
	}

	private List<? extends BaseProduct> findByShortVin(String shortVin) {
		if((productTypeData.getProductType() == ProductType.FRAME) && !StringUtils.isEmpty(shortVin)) {
			updateMaxPageNumber(ServiceFactory.getDao(FrameDao.class).countByShortVin(shortVin));
			return ServiceFactory.getDao(FrameDao.class).findPageByShortVin(shortVin, this.pageNumber, this.pageSize);
		} else {
			return new ArrayList<Product>();
		}
	}

	private List<? extends BaseProduct> findByProductLot(String productionLot) {
		if(!StringUtils.isEmpty(productTypeData.getProductType().name()) && !StringUtils.isEmpty(productionLot)) {
			updateMaxPageNumber(ProductTypeUtil.getProductDao(productTypeData.getProductType()).countByProductionLot(productionLot));
			return ProductTypeUtil.getProductDao(productTypeData.getProductType()).findPageByProductionLot(productionLot, this.pageNumber, this.pageSize);
		} else {
			return new ArrayList<Product>();
		}
	}

	private List<? extends BaseProduct> findByTrackingStatus(String trackingStatus) {
		if(!StringUtils.isEmpty(productTypeData.getProductType().name()) && !StringUtils.isEmpty(trackingStatus)) 
			return ProductTypeUtil.getProductDao(productTypeData.getProductType()).findPageByTrackingStatus(trackingStatus, this.pageNumber, this.pageSize); 
		else 
			return new ArrayList<Product>();
	}

	private List<? extends BaseProduct> findBySeq(String afOnSequenceNumber) {
		return findBySeqRange(afOnSequenceNumber, afOnSequenceNumber);
	}

	private List<? extends BaseProduct> findBySeqRange(String start, String end) {
		if(!StringUtils.isEmpty(productTypeData.getProductType().name()) && !StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)) {
			FrameDao dao = (FrameDao) ProductTypeUtil.getProductDao(productTypeData.getProductType());
			int startNumber = Integer.parseInt(start);
			int endNumber = Integer.parseInt(end);
			updateMaxPageNumber(dao.countByAfOnSequenceNumber(startNumber, endNumber));
			return dao.findPageByAfOnSequenceNumber(startNumber, endNumber, this.pageNumber, this.pageSize); 
		} else {
			return new ArrayList<Product>();
		}
	}

	private List<Line> findAllTrackingStatus() {
		List<Line> trackingStatusLineLst = new ArrayList<Line>();
		if(!StringUtils.isEmpty(productTypeData.getProductType().name())){
			List<String> trackingStatusLst= ProductTypeUtil.getProductDao(productTypeData.getProductType()).findAllTrackingStatus();
			for (String trackingStatus:trackingStatusLst) {
				if (!StringUtils.isBlank(trackingStatus)) {
					Line line = ServiceFactory.getDao(LineDao.class).findByKey(trackingStatus);
					if (line != null) trackingStatusLineLst.add(line);
				}
			}
		}
		return trackingStatusLineLst;
	}

	protected String getProcessPointId(){
		return ClientMainFx.getInstance().getApplicationContext().getProcessPointId();
	}

	private void setOnTaskExit(Task<?> task, EventHandler<WorkerStateEvent> e) {
		task.setOnSucceeded(e);
		task.setOnFailed(e);
		task.setOnCancelled(e);
	}
}