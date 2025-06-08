package com.honda.galc.client.schedule;

import java.lang.reflect.Constructor;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ui.ApplicationMainPane;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.StyleChangingRowFactory;
import com.honda.galc.client.utils.ScheduleActivityListener;
import com.honda.galc.client.utils.SchedulingActivityEvent;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.StringUtil;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TitledPane;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * <h3>Class description</h3>
 * Schedule Main Panel Class Description
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Janak Bhalla & Alok Ghode</TD>
 * <TD>March 05, 2015</TD>
 * <TD>1.0</TD>
 * <TD>GY 20150305</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 */

public class ScheduleMainPanel extends ApplicationMainPane {

	private SplitPane scheduleClientPane;
	private ScrollPane onHoldLotScrollPane;
	private ScrollPane prcoessedLotScrollPane;
	private ScrollPane upcomingLotScrollPane;
	private ScrollPane currentLotScrollPane;
	private double sumOfPanelheight = 0.0;
	private double positionHeight = 0.0;
	private boolean exceptionOnProcessPointEnabled;
	private CurrentLotPanel currentLotPanel;

	private ObjectTablePane<MultiValueObject<BaseProduct>> processedProductTblPane;
	private ObjectTablePane<MultiValueObject<PreProductionLot>> processedLotTblPane;
	private ObjectTablePane<MultiValueObject<PreProductionLot>> upcomingLotTblPane;
	private ObjectTablePane<MultiValueObject<PreProductionLot>> onHoldLotTblPane;

	private static final String CONTROLLER_CLASS = "CONTROLLER_CLASS";
	private ScheduleClientController controller;
	int scheduleclientInactivityTimeoutMillis;
	private EventHandler<InputEvent>  activityEventHandler;
	private ScheduleActivityListener          activityListener;
	//To display time at Refresh
	private static final Format timeFormat = new SimpleDateFormat("hh:mma");
	private String dropDownStringValue;
	private boolean isProcessLocation;
	private String[] dropDownValue = null;
	private String label = "";
	
	private LabeledComboBox<String> planCodeComboBox;
	void initialize() {
		controller = createClientController();
		loadDropDown();
		//Start the Timeout Listener
		startScheduleActivityListener();
	}

	private void loadDropDown() {


		if(isProcessLocation)  {
			label = "Process Location";
			dropDownValue = controller.getProperties().getProcessLocation();
		} else {
			label = "Plan Code";
			dropDownValue = controller.getProperties().getPlanCode();
		}
		
		if(dropDownValue.length > 1) {
			HBox hbox = new HBox();
			planCodeComboBox = createLabeledComboBox("planCodeCombobox", label, true, true, false);
			planCodeComboBox.getControl().getItems().addAll(dropDownValue);
			hbox.getChildren().add(planCodeComboBox);
			hbox.setAlignment(Pos.BASELINE_CENTER);
			
			planCodeComboBox.getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					dropDownStringValue = newValue;
					controller.retrievePreProductionLots();
					init();
				}
			});
			setCenter(hbox);
		} else {
			if(!(dropDownValue.length == 0)){
				dropDownStringValue = StringUtil.isNullOrEmpty(dropDownValue[0])?"":dropDownValue[0];
				controller.retrievePreProductionLots();
				init();
			}

		}
	}
	
	public void setDropDownStringValue(String dropDownStringValue) {
		this.dropDownStringValue = dropDownStringValue;
	}
	

	public String getDropDownStringValue() {
		return dropDownStringValue;
	}

	public LabeledComboBox<String> createLabeledComboBox(String id, String labelName, boolean isHorizontal, boolean isMandatory, boolean isDisabled) {
		LabeledComboBox<String> comboBox = new LabeledComboBox<String>(labelName,isHorizontal,new Insets(0),true,isMandatory);
		comboBox.setId(id);
		comboBox.getControl().setMinHeight(25);
		comboBox.getControl().setMinWidth(200);
		comboBox.getControl().getStyleClass().add("combo-box-base");
		comboBox.getControl().setDisable(isDisabled);
		comboBox.getControl().setStyle("-fx-font: 12 arial;");
		return comboBox;
	}
	
	public ScheduleMainPanel(MainWindow window) {
		super(window);
		initialize();
		EventBusUtil.register(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ScheduleClientController createClientController() {
		String controllerClz = PropertyService.getProperty(window
				.getApplicationContext().getProcessPointId(), CONTROLLER_CLASS);
		if (StringUtils.isEmpty(controllerClz) 
				|| controllerClz.equals(ScheduleClientController.class
						.getName())) {
			isProcessLocation = true;
			return new ScheduleClientController(this);
		} else {
			try {
				Class<? extends ScheduleClientController> forName = (Class<? extends ScheduleClientController>) Class
						.forName(controllerClz);
				Class[] parameterTypes = { ScheduleMainPanel.class };
				Object[] parameters = { this };
				Constructor<? extends ScheduleClientController> constructor = forName
						.getConstructor(parameterTypes);
				isProcessLocation = false;
				return (ScheduleClientController) constructor
						.newInstance(parameters);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;

	}

	private void init() {
		//Determine whether the exception on process point is enabled

	
		exceptionOnProcessPointEnabled = (!StringUtils.isEmpty(controller.getExceptionOnProcessPoint()));
		//Determine which panels should be displayed
		final boolean processedPanelEnabled = (Boolean) controller.getProcessedPanelProperties().get(DefaultScheduleClientProperty.PANEL_ENABLED);
		final boolean currentPanelEnabled = (Boolean) controller.getCurrentPanelProperties().get(DefaultScheduleClientProperty.PANEL_ENABLED);
		final boolean upcomingPanelEnabled = (Boolean) controller.getUpcomingPanelProperties().get(DefaultScheduleClientProperty.PANEL_ENABLED);
		final boolean onHoldPanelEnabled = (Boolean) controller.getOnHoldPanelProperties().get(DefaultScheduleClientProperty.PANEL_ENABLED);
		//Calculating total panel height
		int processedPanelSize = processedPanelEnabled ? (Integer)controller.getProcessedPanelProperties().get(DefaultScheduleClientProperty.PANEL_SIZE) : 0;
		int currentPanelSize = currentPanelEnabled ? (Integer)controller.getCurrentPanelProperties().get(DefaultScheduleClientProperty.PANEL_SIZE) : 0;
		int upcomingPanelSize = upcomingPanelEnabled ? (Integer)controller.getUpcomingPanelProperties().get(DefaultScheduleClientProperty.PANEL_SIZE) : 0;
		int onHoldPanelSize = onHoldPanelEnabled ? (Integer)controller.getOnHoldPanelProperties().get(DefaultScheduleClientProperty.PANEL_SIZE) : 0;
		sumOfPanelheight = processedPanelSize + currentPanelSize + upcomingPanelSize + onHoldPanelSize;
		//Creating components
		scheduleClientPane = new SplitPane();
		scheduleClientPane.setOrientation(Orientation.VERTICAL);
		prcoessedLotScrollPane = processedPanelEnabled ? new ScrollPane() : null;
		currentLotScrollPane = currentPanelEnabled ? new ScrollPane() : null;
		upcomingLotScrollPane = upcomingPanelEnabled ? new ScrollPane() : null;
		onHoldLotScrollPane = onHoldPanelEnabled ? new ScrollPane() : null;		
		//Initialization in sequence to calculate divider position
		int index = 0;
		if (processedPanelEnabled) { initializeProcessedLotPane(index); index++; }
		if (currentPanelEnabled) { initializeCurrentLotPane(index); index++; }
		if (upcomingPanelEnabled) { initializeUpcomingLotPane(index); index++; }
		if (onHoldPanelEnabled) { initializeOnHoldLotPane(index); index++; }
		//Adding items in Split Pane
		if (processedPanelEnabled) scheduleClientPane.getItems().add(prcoessedLotScrollPane);
		if (currentPanelEnabled) scheduleClientPane.getItems().add(currentLotScrollPane);
		if (upcomingPanelEnabled) scheduleClientPane.getItems().add(upcomingLotScrollPane);
		if (onHoldPanelEnabled) scheduleClientPane.getItems().add(onHoldLotScrollPane);
		scheduleClientPane.setMinWidth(this.getWidth());
		scheduleClientPane.setMinHeight(this.getHeight());
		
        if(dropDownValue.length > 1) {
    		BorderPane borderPane = new BorderPane();
    		HBox hBox = new HBox();
    		hBox.setAlignment(Pos.CENTER_RIGHT);
    		Hyperlink hyperLink = new Hyperlink("<< Select "+label+">>");
    		hyperLink.setStyle("-fx-font-size: 8pt; -fx-font-weight: bold;");
    		hyperLink.setBorder(Border.EMPTY);
    		hyperLink.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                	loadDropDown();
                }
            });
    		//Adding the menubar from main window
    		Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            if(dropDownValue.length > 1){
            	Label label = new Label(dropDownStringValue);
            	label.setStyle("-fx-font: 12 arial;-fx-font-weight: bold;-fx-text-fill:blue;");
            	hBox.getChildren().addAll(label, hyperLink);
            }
            
            HBox hBox2 = new HBox();
    		hBox2.getChildren().add(scheduleClientPane);
    		VBox vbox = new VBox();
    		
        	vbox.getChildren().addAll(hBox, hBox2);
        	borderPane.setCenter(vbox);
        	setCenter(vbox);
        } else {
        	setCenter(scheduleClientPane);
        }
		
		
	}

	private void initializeTitledPane(Node content, boolean isExpanded, final int index, final ScrollPane scrollPane, Map<String, Object> properties) {
		TitledPane titlePane = new TitledPane();
		titlePane.setContent(content);
		titlePane.setExpanded(isExpanded);
		titlePane.setFont(Fonts.DIALOG_BOLD_16);
		//Initializing properties
		titlePane.setText((String) properties
					.get(DefaultScheduleClientProperty.PANEL_NAME));
		//Setting Titled Pane in Scroll Pane
		scrollPane.setContent(titlePane);
		//Setting preferred height
		double height = (double) ((Integer) properties.get(DefaultScheduleClientProperty.PANEL_SIZE)).intValue();
		final double position = calculatePosition(height);
		//Binding Height of Titled Pane with Scroll Pane
		titlePane.prefHeightProperty().bind(scrollPane.heightProperty());
		//Listener to set Split pane height
		titlePane.expandedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		       	boolean isExpanded = (observable.getValue()!=null)?observable.getValue():false;
		       	if(isExpanded) {
		       		scheduleClientPane.setDividerPosition(index, position);
		       	} else {
		       		scheduleClientPane.setDividerPosition(index, 0.0f);
		       	}
		    }
		});
		Platform.runLater(new Runnable() {
			public void run() {
				scheduleClientPane.setDividerPosition(index, position);
			}
		});
	}

	private void initializeProcessedLotPane(int dividerIndex) {
		Node content = null;
		Map<String, Object> processedPanelProperties = controller.getProcessedPanelProperties();
		if (controller.isProcessedProductOrLot()) {
			processedProductTblPane = createProductTableView(processedPanelProperties);
			setPaneWidth(processedProductTblPane.getTable());
			content = processedProductTblPane;
		} else {
			processedLotTblPane = createTableView(processedPanelProperties);
			setPaneWidth(processedLotTblPane.getTable());
			content = processedLotTblPane;

		}
		if(content!=null) {
			initializeTitledPane(content, true, dividerIndex, prcoessedLotScrollPane, processedPanelProperties);
		}
		
	}
	
	private double calculatePosition(double panelHeight) {
		positionHeight += panelHeight;
		return (sumOfPanelheight > 0.0)?(positionHeight/sumOfPanelheight):0.0;
	}
	
	private void setPaneWidth(Region pane) {
		double width = getMainWindow().getScreenWidth()-25;
		pane.setMinWidth(width);
		pane.setPrefWidth(width);
		pane.setMaxWidth(width);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initializeCurrentLotPane(int dividerIndex) {
		currentLotPanel = new CurrentLotPanel(controller);
		if (exceptionOnProcessPointEnabled) {
			currentLotPanel.getLotPanel().getTable().setRowFactory(new StyleChangingRowFactory(currentLotPanel.getLotPanel().getTable().getRowFactory()));
			currentLotPanel.getLotPanel().getTable().getSortOrder().addListener(new javafx.collections.ListChangeListener<javafx.scene.control.TableColumn<MultiValueObject<PreProductionLot>, ?>>() {
				@Override
				public void onChanged(
						javafx.collections.ListChangeListener.Change<? extends TableColumn<MultiValueObject<PreProductionLot>, ?>> arg0) {
					controller.updateColorCoding(currentLotPanel.getLotPanel().getTable());
				}
			});
		}
		Map<String, Object> currentPanelProperties = controller.getCurrentPanelProperties();
		setPaneWidth(currentLotPanel);
		initializeTitledPane(currentLotPanel, true, dividerIndex, currentLotScrollPane, currentPanelProperties);
	}

	private void initializeUpcomingLotPane(int dividerIndex) {
		Map<String, Object> upcomingPanelProperties = controller.getUpcomingPanelProperties();
		upcomingLotTblPane = createTableView(upcomingPanelProperties);
		setPaneWidth(upcomingLotTblPane.getTable());
		initializeTitledPane(upcomingLotTblPane, true, dividerIndex, upcomingLotScrollPane, upcomingPanelProperties);
	}

	private void initializeOnHoldLotPane(int dividerIndex) {
		Map<String, Object> onHoldPanelProperties = controller.getOnHoldPanelProperties();
		onHoldLotTblPane = createTableView(onHoldPanelProperties);
		setPaneWidth(onHoldLotTblPane.getTable());
		initializeTitledPane(onHoldLotTblPane, true, dividerIndex, onHoldLotScrollPane, onHoldPanelProperties);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ScheduleClientTable<BaseProduct> createProductTableView(
			Map<String, Object> properties) {
		final ScheduleClientTable<BaseProduct> pane = new ScheduleClientTable<BaseProduct>(
				controller, properties);
		if (exceptionOnProcessPointEnabled) {
			pane.getTable().setRowFactory(new StyleChangingRowFactory(pane.getTable().getRowFactory()));
			pane.getTable().getSortOrder().addListener(new javafx.collections.ListChangeListener<javafx.scene.control.TableColumn<MultiValueObject<BaseProduct>, ?>>() {
				@Override
				public void onChanged(
						javafx.collections.ListChangeListener.Change<? extends TableColumn<MultiValueObject<BaseProduct>, ?>> arg0) {
					controller.updateProductColorCoding(pane.getTable());
				}
			});
		}
		return pane;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ScheduleClientTable<PreProductionLot> createTableView(
			Map<String, Object> properties) {
		final ScheduleClientTable<PreProductionLot> pane = new ScheduleClientTablePreProductionLot(
				controller, properties);
		if (exceptionOnProcessPointEnabled) {
			pane.getTable().setRowFactory(new StyleChangingRowFactory(pane.getTable().getRowFactory()));
			pane.getTable().getSortOrder().addListener(new javafx.collections.ListChangeListener<javafx.scene.control.TableColumn<MultiValueObject<PreProductionLot>, ?>>() {
				@Override
				public void onChanged(
						javafx.collections.ListChangeListener.Change<? extends TableColumn<MultiValueObject<PreProductionLot>, ?>> arg0) {
					controller.updateColorCoding(pane.getTable());
				}
			});
		}
		pane.getTable().getSelectionModel()
				.setSelectionMode(SelectionMode.MULTIPLE);

		return pane;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Subscribe
	public void onEvent(SchedulingEvent event) {
		if (!(event.getTargetObject() instanceof List))
			return;

		if (event.getEventType() == SchedulingEventType.PROCESSED_PRODUCT_CHANGED) {
			List<MultiValueObject<BaseProduct>> products = (List<MultiValueObject<BaseProduct>>) event
					.getTargetObject();
			processedProductTblPane.setData(products);
			processedProductTblPane.getTable().scrollTo(Integer.MAX_VALUE);
			if (exceptionOnProcessPointEnabled) ((StyleChangingRowFactory) processedProductTblPane.getTable().getRowFactory()).getRowStyles().setAll(controller.getColorCodingForProducts(products));
			return;
		}

		List<MultiValueObject<PreProductionLot>> preProductionLots = (List<MultiValueObject<PreProductionLot>>) event
				.getTargetObject();

		switch (event.getEventType()) {
		case PROCESSED_ORDER_CHANGED:
			processedLotTblPane.setData(preProductionLots);
			processedLotTblPane.getTable().scrollTo(Integer.MAX_VALUE);
			if (exceptionOnProcessPointEnabled) ((StyleChangingRowFactory) processedLotTblPane.getTable().getRowFactory()).getRowStyles().setAll(controller.getColorCodingForPreProductionLots(preProductionLots));
			break;
		case UPCOMING_ORDER_CHANGED:
			upcomingLotTblPane.setData(preProductionLots);
			if (exceptionOnProcessPointEnabled) ((StyleChangingRowFactory) upcomingLotTblPane.getTable().getRowFactory()).getRowStyles().setAll(controller.getColorCodingForPreProductionLots(preProductionLots));
			break;
		case ON_HOLD_ORDER_CHANGED:
			onHoldLotTblPane.setData(preProductionLots);
			if (exceptionOnProcessPointEnabled) ((StyleChangingRowFactory) onHoldLotTblPane.getTable().getRowFactory()).getRowStyles().setAll(controller.getColorCodingForPreProductionLots(preProductionLots));
			break;
		case CURRENT_ORDER_CHANGED:
			currentLotPanel.lotInfoChanged(preProductionLots);
			if (exceptionOnProcessPointEnabled) ((StyleChangingRowFactory) currentLotPanel.getLotPanel().getTable().getRowFactory()).getRowStyles().setAll(controller.getColorCodingForPreProductionLots(preProductionLots));
			break;
		default:
		}
	}

	public ObjectTablePane<MultiValueObject<PreProductionLot>> getProcessedLotTblPane() {
		return processedLotTblPane;
	}

	public ObjectTablePane<MultiValueObject<PreProductionLot>> getUpcomingLotTblPane() {
		return upcomingLotTblPane;
	}

	public ObjectTablePane<MultiValueObject<PreProductionLot>> getOnHoldLotTblPane() {
		return onHoldLotTblPane;
	}

	public CurrentLotPanel getCurrentLotPanel() {
		return currentLotPanel;
	}

	public ScheduleClientController getController() {
		return controller;
	}

	public void setController(ScheduleClientController controller) {
		this.controller = controller;
	}
	
	public void requestFocusOnProductId() {
		if(getCurrentLotPanel() != null && getCurrentLotPanel().getProductPane() != null)
		     getCurrentLotPanel().getProductPane().getProductIdTextField().requestFocus();
	}
	
	@Subscribe
	public void displayMessage(DisplayMessageEvent displayEvent) {
		if (displayEvent.getMessageType().equals(MessageType.ERROR)){
			getMainWindow().setErrorMessage(displayEvent.getmessage());
		} else {
			getMainWindow().setMessage(displayEvent.getmessage(), Color.YELLOW);
		}
	}
	
	//Start the Timeout Listener
	public void startScheduleActivityListener() {
	    scheduleclientInactivityTimeoutMillis = controller.getProperties().getScheduleClientTimeout() * 1000;
		if (scheduleclientInactivityTimeoutMillis > 0) {
			getScheduleActivityListener().start();
			this.addEventFilter(InputEvent.ANY, getScheduleActivityEventHandler());
	   }
	}
	
	//Stop the Timeout Listener
	public void stopScheduleActivityListener() {
		getScheduleActivityListener().stop();
		this.removeEventFilter(InputEvent.ANY, getScheduleActivityEventHandler());
	}
	
	//Initialize the Schedule Client Listener
	private ScheduleActivityListener getScheduleActivityListener() {
		if (activityListener == null) {
		  	activityListener = new ScheduleActivityListener(scheduleclientInactivityTimeoutMillis);
		}
		return activityListener;
	}
	
	//Call to Refresh the Client
	private EventHandler<InputEvent> getScheduleActivityEventHandler() {
		if (activityEventHandler == null) {
			activityEventHandler = new EventHandler<InputEvent>() {
				@Override
				public void handle(InputEvent arg0) {
					EventBusUtil.publish(new SchedulingActivityEvent());
				}
			};
		}
		return activityEventHandler;
	}
	
	public String getSystemTime(){
		Date date = new Date(System.currentTimeMillis());
		return timeFormat.format(date);
	}

	public boolean isProcessLocationSelected() {
		return isProcessLocation;
	}
	
	

}

