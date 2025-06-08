package com.honda.galc.client.product.pane;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.product.entry.SearchByProductFilterController;
import com.honda.galc.client.product.mvc.BulkProductController;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.event.ProductProcessEvent;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.ProductTypeData;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */
public class SearchByProductFilterPane extends AbstractProductSearchPane  {

	private static final String resourcePath = "/resource/%s.fxml";

	@FXML
	private RadioButton prodLotRdBtn;

	@FXML
	private RadioButton seqRangeRdBtn;

	@FXML
	private RadioButton productIdRangeRdBtn;

	@FXML
	private TextField productIdRangeTxt1;

	@FXML
	private TextField productIdRangeTxt2;

	@FXML
	private RadioButton trackStsRdBtn;

	@FXML
	private TextField seqRangeTxtField1;

	@FXML
	private TextField seqRangeTxtField2;

	@FXML
	private TextField prodLotTextField;

	@FXML
	private VBox vboxProductIdRange;
	
	@FXML
	private VBox vboxSeqRange;
	
	@FXML
	private Button searchBtn;


	@FXML
	private ToggleGroup searchGroup;

	@FXML
	private ComboBox<Line> trackingStatusComboBox;
	@FXML
	private ComboBox<ProcessPoint> processPointComboBox;
	private SearchByProductFilterController controller;
	
	AbstractProductInputPane myParent = null;
	
	public SearchByProductFilterPane(String title,
			ProductTypeData productTypeData, String applicationId,String searchText) {
		super();
		loadFXML();
		initcomponents();
		createController();
		initConnections();
	}

	public SearchByProductFilterPane(String title,
			ProductTypeData productTypeData, String applicationId) {
		this(title,productTypeData,applicationId,null);		
	}

	public SearchByProductFilterPane(BulkProductController productController, AbstractProductInputPane parentView) {
		super();
		myParent = parentView;
		this.productController = productController;
		parentPanelHeight = productController.getInputPaneHeight();
		loadFXML();
		initcomponents();
		createController();
		initConnections();
		EventBusUtil.register(this);
	}

	private void createController() {
		if(controller == null) {
			controller = new SearchByProductFilterController(this, getProductController().getView().getMainWindow().getApplicationContext(), productController);
		}
	}

	/**
	 * @return the controller
	 */
	public SearchByProductFilterController getSearchController() {
		return controller;
	}

	public void loadFXML() {

		URL resource = getViewURL();

		assert resource != null;

		FXMLLoader loader = new FXMLLoader(resource);

		loader.setController(this);
		try {
			AnchorPane pane = (AnchorPane) loader.load();
			final BorderPane borderPane = new BorderPane();
			borderPane.setCenter(pane);
			getChildren().add(borderPane);
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


	@Override
	public void associateSelected() {
		if(!searchBtn.isDisabled()) {
			searchBtn.fireEvent(new ActionEvent());
		}
		searchBtn.requestFocus();
	}
	
	@SuppressWarnings("unchecked")
	private void initcomponents() {


		processPointComboBox.setStyle(getLabelStyleSmall());
		processPointComboBox.setMaxWidth((int)(0.15 * getScreenWidth()));

		trackingStatusComboBox.setStyle(getLabelStyleSmall());
		trackingStatusComboBox.setMaxWidth((int)(0.15 * getScreenWidth()));

		Button btn = getSearchBtn();
		btn.getStyleClass().add("station-btn");
		btn.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.45 * btn.getWidth())));
		btn.setPrefWidth(getScreenWidth() * 0.09);
		
		// add ability to fire the Search button by pressing the Enter key
		seqRangeTxtField1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				seqRangeTxtField2.requestFocus(); 
			}
		});
		productIdRangeTxt1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				productIdRangeTxt2.requestFocus(); 
			}
		});

		// change the TAB order so that inputTxtField TABs to seqRangeTxtField rather than productsTblView when it is available
		seqRangeTxtField1.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (seqRangeRdBtn.isSelected() && event.getCode() == KeyCode.TAB) {
					seqRangeTxtField2.requestFocus();
					event.consume();
				}
			}
		});
		seqRangeTxtField1.focusedProperty().addListener((arg, ov, nv) -> {if (nv)  searchGroup.selectToggle(seqRangeRdBtn);});
		seqRangeTxtField2.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.TAB) {
					searchBtn.setDisable(false);
					searchBtn.requestFocus();
					event.consume();
				}
			}
		});
		seqRangeTxtField2.focusedProperty().addListener((arg, ov, nv) -> {if (nv)  searchGroup.selectToggle(seqRangeRdBtn);});

		// disable focusing on radio buttons and auto-focus on text fields
		// focus-disabled radio buttons are still clickable, so the dialog is still responsive
		prodLotRdBtn.setFocusTraversable(false);
		prodLotRdBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) { prodLotTextField.requestFocus(); }
		});
		trackStsRdBtn.setFocusTraversable(false);
		trackStsRdBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) { trackingStatusComboBox.requestFocus(); }
		});
		seqRangeRdBtn.setFocusTraversable(false);
		seqRangeRdBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) { seqRangeTxtField1.requestFocus(); }
		});
		productIdRangeRdBtn.setFocusTraversable(false);
		productIdRangeRdBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) { productIdRangeTxt1.requestFocus(); }
		});
		
		// change the TAB order so that productIdRangeTxt1 rather than productsTblView when it is available
		productIdRangeTxt1.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (productIdRangeRdBtn.isSelected() && event.getCode() == KeyCode.TAB) {
					productIdRangeTxt2.requestFocus();
					event.consume();
				}
			}
		});
		productIdRangeTxt1.focusedProperty().addListener((arg, ov, nv) -> {if (nv)  searchGroup.selectToggle(productIdRangeRdBtn);});
		productIdRangeTxt2.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.TAB) {
					searchBtn.setDisable(false);
					searchBtn.requestFocus();
					event.consume();
				}
			}
		});
		productIdRangeTxt2.focusedProperty().addListener((arg, ov, nv) -> {if (nv)  searchGroup.selectToggle(productIdRangeRdBtn);});
		prodLotTextField.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.TAB) {
					searchBtn.setDisable(false);
					searchBtn.requestFocus();
					event.consume();
				}
			}
		});
		prodLotTextField.focusedProperty().addListener((arg, ov, nv) -> {if (nv)  searchGroup.selectToggle(prodLotRdBtn);});

		trackingStatusComboBox.setConverter(new StringConverter<Line>() {
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


		trackingStatusComboBox.valueProperty().addListener((arg, ov, nv) -> System.out.println("Selected: " + arg.toString()));

		seqRangeTxtField1.requestFocus();

	}

	@Subscribe
	public void onProductProcessEvent(ProductProcessEvent event) {
		if (event == null) {
			return;
		} else {
			getSearchBtn().setDisable(true);
		} 
	
	}

	private void initConnections() {
		searchBtn.setOnAction(getSearchController());
		searchGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable,
					Toggle oldValue, Toggle newValue) {}
		});

	}


	protected String getProcessPointId(){
		return ClientMainFx.getInstance().getApplicationContext().getProcessPointId();
	}

	/**
	 * @return the searchBtn
	 */
	public Button getSearchBtn() {
		return searchBtn;
	}

	/**
	 * @param searchBtn the searchBtn to set
	 */
	public void setSearchBtn(Button searchBtn) {
		this.searchBtn = searchBtn;
	}

	public ComboBox<ProcessPoint> getProcessPointComboBox() {
		return processPointComboBox;
	}

	/**
	 * @return the trackingStatusComboBox
	 */
	public ComboBox<Line> getTrackingStatusComboBox() {
		return trackingStatusComboBox;
	}

	/**
	 * @return the searchGroup
	 */
	public ToggleGroup getSearchGroup() {
		return searchGroup;
	}

	/**
	 * @return the prodLotRdBtn
	 */
	public RadioButton getProdLotRdBtn() {
		return prodLotRdBtn;
	}

	/**
	 * @return the seqRangeRdBtn
	 */
	public RadioButton getSeqRangeRdBtn() {
		return seqRangeRdBtn;
	}

	/**
	 * @return the productIdRangeRdBtn
	 */
	public RadioButton getProductIdRangeRdBtn() {
		return productIdRangeRdBtn;
	}

	/**
	 * @return the productIdRangeTxt1
	 */
	public TextField getProductIdRangeTxt1() {
		return productIdRangeTxt1;
	}

	/**
	 * @return the productIdRangeTxt2
	 */
	public TextField getProductIdRangeTxt2() {
		return productIdRangeTxt2;
	}

	/**
	 * @return the trackStsRdBtn
	 */
	public RadioButton getTrackStsRdBtn() {
		return trackStsRdBtn;
	}

	/**
	 * @return the seqRangeTxtField1
	 */
	public TextField getSeqRangeTxtField1() {
		return seqRangeTxtField1;
	}

	/**
	 * @return the seqRangeTxtField2
	 */
	public TextField getSeqRangeTxtField2() {
		return seqRangeTxtField2;
	}

	/**
	 * @return the prodLotTextField
	 */
	public TextField getProdLotTextField() {
		return prodLotTextField;
	}

	/**
	 * @return the vboxProductIdRange
	 */
	public VBox getVboxProductIdRange() {
		return vboxProductIdRange;
	}

	/**
	 * @return the vboxSeqRange
	 */
	public VBox getVboxSeqRange() {
		return vboxSeqRange;
	}

}
