package com.honda.galc.client.dc.control;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.dc.property.DataCollectionPropertyBean;
import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.event.UnitNavigatorEventType;
import com.honda.galc.constant.OperationType;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.service.property.PropertyService;
import com.sun.javafx.scene.control.skin.ScrollBarSkin;

/**
 * 
 * @author Suriya Sena
 * @date Jan 23, 2015
 * 
 */

public abstract class AbstractUnitNavigatorControl extends BorderPane implements EventHandler<MouseEvent> {
	protected static String STYLESHEET = "resource/com/honda/galc/client/dc/view/UnitNavigatorWidget.css";
	@FXML
	protected ResourceBundle resources;

	@FXML
	protected URL location;

	@FXML
	protected VBox naviListVBox;

	@FXML
	protected ScrollBar scrollbar;

	@FXML
	protected Canvas fastTravelMap;

	protected ObservableList<MCOperationRevision> items;

	protected int MAX_VIEW = 18;
	protected int viewSize = MAX_VIEW;
	protected int centerView = MAX_VIEW / 2;
	protected final int SMALL_ICON_SIZE = 16;

	protected SimpleIntegerProperty cursorIndex = new SimpleIntegerProperty(0);
	protected boolean isScrolling = false;

	protected DataCollectionController controller;
	protected DataCollectionPropertyBean dataCollectionPropertyBean;
	protected PDDAPropertyBean pddaPropertyBean;

	protected void init() {
		cursorIndex.addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				if (oldValue != newValue && isScrolling == false) {
					moveTo(newValue.intValue());
				}
			}
		});

		scrollbar.valueProperty().bindBidirectional(cursorIndex);
		scrollbar.addEventFilter(MouseEvent.ANY,
				/*
				 * Prevent unnecessary loading and unloading of unit and images
				 * whilst the scroll bar button is held down and dragged.
				 */
				new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
							isScrolling = true;
						} else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
							isScrolling = false;
							scrollTo(clamp((int) scrollbar.getValue()));
						}
					}
				});

		initSearchPanel();
		scheduleDraw();
	}

	abstract protected void initSearchPanel();

	private void scheduleDraw() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						layoutChildren();
					}
				});
			}
		}, 10);
	}

	public void init(DataCollectionController controller, String processPointId) {
		this.controller = controller;
		dataCollectionPropertyBean = PropertyService.getPropertyBean(DataCollectionPropertyBean.class, processPointId);
		pddaPropertyBean = PropertyService.getPropertyBean(PDDAPropertyBean.class, processPointId);
		this.setMaxWidth(pddaPropertyBean.getUnitNavigatorWidth());
		MAX_VIEW = pddaPropertyBean.getMaxView();
		viewSize = MAX_VIEW;
		centerView = MAX_VIEW / 2;

		if (pddaPropertyBean.isUnitNavigatorSimpleMode()) {
			this.setTop(null);
			this.setRight(null);
		}
	}

	public void setItems(ObservableList<MCOperationRevision> list) {
		items = list;
		createList();
		requestLayout();
	}

	public void next() {
		cursorIndex.set(clamp(cursorIndex.get() + 1));
		scrollTo(cursorIndex.get());
	}

	public void previous() {
		cursorIndex.set(clamp(cursorIndex.get() - 1));
		scrollTo(cursorIndex.get());
	}

	public void moveTo(int index) {
		cursorIndex.set(clamp(index));
		scrollTo(cursorIndex.get());
	}

	protected void createList() {
		naviListVBox.getChildren().clear();

		if (!pddaPropertyBean.isUnitNavigatorSimpleMode()) {
			scrollbar.setMin(0);
			scrollbar.setMax(items.size());
		}

		centerView = Math.min(MAX_VIEW, items.size()) / 2;
		viewSize = Math.min(MAX_VIEW, items.size());

		for (int i = 0; i < viewSize; i++) {
			NavLabel label = new NavLabel("unitLabel" + Integer.toString(i), "", i);
			MCOperationRevision tmpOperataion= null;

			if (viewSize == items.size()) {
				label.setOperation(items.get(i));
				tmpOperataion = items.get(i);
			} else {
				if (i >= centerView) {
					label.setOperation(items.get(i));
					tmpOperataion = items.get(i);
				} else {
					label.setOperation(null);
				}
			}
			if (i == centerView) {
				label.setBackgroundColor(tmpOperataion, "nav-cell-selected");
			} else {
				label.setBackgroundColor(tmpOperataion, "nav-cell");
			}

			if (dataCollectionPropertyBean.isNavigatorClickable())
				label.setOnMouseClicked(this);
			naviListVBox.getChildren().add(label);
			
		}
		cursorIndex.set(0);
	}
	
	protected void scrollTo(int index) {
		int min = index - centerView;
		int max = min + viewSize;

		ObservableList<Node> labelList = naviListVBox.getChildren();

		if (viewSize == items.size()) {
			min = 0;
			max = viewSize;
		}

		for (int i = min, j = 0; i < max; i++, j++) {

			NavLabel l = (NavLabel) labelList.get(j);
			
			if (viewSize == items.size()) {
				if (l.getPosition() == index) {
					l.setId("nav-cell-selected");
				} else {
					l.setId("nav-cell");
				}
			}
			if (i < 0 || i >= items.size()) {
				l.setOperation(null);
			} else {
				l.setOperation(items.get(i));
				l.setBackgroundColor(items.get(i), l.getId());
			}
		}
		if(controller.getModel().getProcessTrainingMap().containsKey(items.get(index).getUnitNo())){
			controller.getAudioManager().playUserNotTrained();
		}
		
		setNeedsLayout(true);
		
		EventBusUtil.publish(controller.getModel().createUnitNavigatorUnit(index, UnitNavigatorEventType.SELECTED));

	}

	protected int clamp(int index) {
		if (index >= items.size()) {
			index = items.size() - 1;
		} else if (index < 0) {
			index = 0;
		}
		return index;
	}

	protected void layoutChildren() {
		super.layoutChildren();
		if (pddaPropertyBean != null) {
			redrawFastTravelMap();
		}
	}

	private void redrawFastTravelMap() {
		if (pddaPropertyBean.isUnitNavigatorSimpleMode()) {
			return;
		}

		positionFastTravelMap();

		for (int i = 0; i < items.size(); i++) {
			MCOperationRevision operation = items.get(i);
			if (controller.getModel().isComplete(operation) == false) {
				drawMapLocator(i);
			}
		}
	}

	/*
	 * The search map must be parallel to the scroll bar track i.e the bit
	 * between the scroll-bar up and down arrow buttons
	 */
	private void positionFastTravelMap() {
		double minY = 0, maxY = 0, canvasHeight = 0, canvasTopMargin = 0;

		ScrollBarSkin s = (ScrollBarSkin) scrollbar.getSkin();
		for (Node d : s.getChildren()) {

			ObservableList<String> styleClass = d.getStyleClass();
			if (styleClass.get(0).compareTo("track") == 0) {
				minY = d.getBoundsInParent().getMinY();
				maxY = d.getBoundsInParent().getMaxY();
				canvasHeight = maxY - minY;
				canvasTopMargin = minY;
			}
		}

		fastTravelMap.setHeight(canvasHeight);
		fastTravelMap.setLayoutY(canvasTopMargin);
		Insets insets = new Insets((int) canvasTopMargin, 0, 0, 0);
		HBox.setMargin((Node) fastTravelMap, insets);

		GraphicsContext gc = fastTravelMap.getGraphicsContext2D();
		gc.clearRect(0, 0, fastTravelMap.getWidth(), getHeight());
	}

	private void drawMapLocator(int pos) {
		GraphicsContext gc = fastTravelMap.getGraphicsContext2D();

		double y1 = (fastTravelMap.getHeight() / (double) items.size()) * pos;

		double y2 = y1;
		double x1 = 0;
		double x2 = fastTravelMap.getWidth();

		gc.setStroke(Color.ROYALBLUE);
		gc.setLineWidth(1);
		gc.strokeLine(x1, y1, x2, y2);
	}

	protected boolean isSkippedPart(MCOperationRevision operation) {
		return controller.getModel().isSkippedPart(operation.getId().getOperationName());
	}

	protected boolean hasSkippedMeasurements(MCOperationRevision operation) {
		String operationName = operation.getId().getOperationName();

		if (operation.getSelectedPart() != null && DataCollectionModel.hasMeasurements(operation)) {
			int goodCount = controller.getModel().getGoodMeasurementsCount(operationName);
			int expectedCount = operation.getSelectedPart().getMeasurementCount();
			return controller.getModel().getSkippedMeasurementsMap().get(operationName) != null
					&& goodCount < expectedCount;
		} else {
			return false;
		}
	}

	@FXML
	protected void onSearchKeyReleased(KeyEvent event) {
		onSearchKeyReleased();
	}

	abstract protected void onSearchKeyReleased();

	@FXML
	protected void mapClick(MouseEvent event) {
		event.getY();
		int index = (int) Math.ceil((items.size() / fastTravelMap.getHeight()) * event.getY());
		moveTo(index);
	}

	@FXML
	protected void onScroll(ScrollEvent event) {
		if (dataCollectionPropertyBean.isNavigatorClickable())
			moveTo(cursorIndex.get() + (int) event.getTextDeltaY());
	}

	public void handle(MouseEvent event) {
		controller.clearMessage();
		NavLabel l = (NavLabel) event.getSource();
		int newIndex = 0;

		if (viewSize == items.size()) {
			newIndex = l.getPosition();
		} else {
			if (l.getPosition() < centerView) {
				newIndex = cursorIndex.get() - Math.abs(centerView - l.getPosition());
			} else {
				newIndex = cursorIndex.get() + Math.abs(centerView - l.getPosition());
			}
		}

		moveTo(newIndex);
	}

	class NavLabel extends LoggedLabel {

		private int position;
		private final ImageView torqueImage;
		private final ImageView scanImage;
		private final ImageView greenCheckMarkImage;
		private final ImageView orangeCheckMarkImage;
		private final ImageView orangeCircleImage;
		private final ImageView noScanImage;
		private final ImageView noTorqueImage;
		private final ImageView noGreenCheckMarkImage;
		private final ImageView autoCompleteOpImage;
		private final ImageView orangeSquareImage;
		private final ImageView voltmeterImage;

		private static final int FIT_SIZE = 28;

		public NavLabel(String id, String text, Node graphic, int position) {
			super(id, text, graphic);

			this.position = position;

			Image scan = new Image("resource/com/honda/galc/client/dc/view/scan.png");
			Image torque = new Image("resource/com/honda/galc/client/dc/view/torque.png");
			Image greenCheckMark = new Image("resource/com/honda/galc/client/dc/view/green_checkmark.png");
			Image orangeCheckMark = new Image("resource/com/honda/galc/client/dc/view/orange_checkmark.png");
			Image orangeCircle = new Image("resource/com/honda/galc/client/dc/view/orange_circle.png");
			Image autoCompleteOp = new Image("resource/com/honda/galc/client/dc/view/autoCompleteOp.png");
			Image orangeSquare = new Image("resource/com/honda/galc/client/dc/view/orange_square.png");
			Image voltmeter = new Image("resource/com/honda/galc/client/dc/view/voltmeter.png");

			scanImage = new ImageView(scan);
			torqueImage = new ImageView(torque);
			greenCheckMarkImage = new ImageView(greenCheckMark);
			orangeCheckMarkImage = new ImageView(orangeCheckMark);
			orangeCircleImage = new ImageView(orangeCircle);
			autoCompleteOpImage = new ImageView(autoCompleteOp);
			orangeSquareImage = new ImageView(orangeSquare);
			voltmeterImage = new ImageView(voltmeter);
			voltmeterImage.setFitHeight(FIT_SIZE);
			voltmeterImage.setPreserveRatio(true);
			voltmeterImage.setSmooth(true);

			noScanImage = new ImageView();
			noScanImage.setFitHeight(FIT_SIZE);
			noScanImage.setFitWidth(FIT_SIZE);

			noTorqueImage = new ImageView();
			noTorqueImage.setFitHeight(FIT_SIZE);
			noTorqueImage.setFitWidth(FIT_SIZE);

			noGreenCheckMarkImage = new ImageView();
			noGreenCheckMarkImage.setFitHeight(FIT_SIZE);
			noGreenCheckMarkImage.setFitWidth(FIT_SIZE);

			this.setTextFill(Color.BLACK);
			this.setWrapText(true);
			this.setStyle("-fx-font-size:" + pddaPropertyBean.getUnitNavigatorFontSize() + ";");

			this.setMaxWidth(Double.MAX_VALUE);
		}

		public NavLabel(String id, String text, int position) {
			this(id, text, null, position);
		}

		public NavLabel(String id) {
			this(id, null, null, 0);
		}

		public void setPosition(int position) {
			this.position = position;
		}

		public int getPosition() {
			return position;
		}
		
		public void setBackgroundColor(MCOperationRevision operation, String labelId){
			if(StringUtils.isNotBlank(labelId) && operation != null)
			{
				if(controller.getModel().isSpecialOperation(operation) && !controller.getModel().isComplete(operation)){
					//Highlight the unit if it is not done by logged user in the given timeframe
					if("nav-cell-selected".equalsIgnoreCase(labelId) || 
					   "nav-cell-selected-and-highlighted".equalsIgnoreCase(labelId) ){
						this.setId("nav-cell-selected-and-highlighted");
					}else{
						this.setId("nav-cell-highlighted");
					}
				}else{
					//Check for the Selected index
					if(labelId.contains("nav-cell-selected"))this.setId("nav-cell-selected");
					else this.setId("nav-cell");
				}
			}else this.setId("nav-cell");
		}

		public void setOperation(MCOperationRevision operation) {
			if (operation == null) {
				this.setText("");
				this.setGraphic(null);
				this.setBackground(Background.EMPTY);
			} else {
				HBox hbox = new HBox();
				ImageView checked = noGreenCheckMarkImage;
				HBox.setMargin(greenCheckMarkImage, new Insets(3));
				
				if (operation.getType().equals(OperationType.GALC_AUTO_COMPLETE)) {
					if (controller.getModel().isComplete(operation)) {
						checked = greenCheckMarkImage;
					}
					HBox.setMargin(autoCompleteOpImage, new Insets(3));
					hbox.getChildren().addAll(autoCompleteOpImage, noScanImage, checked);
				} else {
					ImageView scan = DataCollectionModel.hasScanPart(operation) ? scanImage : noScanImage;
					ImageView torque;
					if (DataCollectionModel.hasMeasurements(operation) 
							&& operation.getSelectedPart() != null 
							&& operation.getSelectedPart().getMeasurementCount() > 0) {
						MCOperationPartRevision selectPart = operation.getSelectedPart();
						String deviceMsg = selectPart.getMeasurements().get(0).getDeviceMsg();
						if ( StringUtils.isNotBlank(deviceMsg)&& pddaPropertyBean.getVoltMeterCommandFormat().indexOf(deviceMsg)!=-1 ) torque = voltmeterImage;
						else torque = torqueImage;
					} else torque = noTorqueImage;
					if (hasSkippedMeasurements(operation) || isSkippedPart(operation)) {
						checked = orangeCircleImage;
					} 
					if(controller.getModel().getProcessTrainingMap().containsKey(operation.getUnitNo())){
						checked = orangeSquareImage;
					}
					if (controller.getModel().isPending(operation)) {
						checked = orangeCheckMarkImage;
					}
					if (controller.getModel().isComplete(operation)) {
						checked = greenCheckMarkImage;
					} 

					HBox.setMargin(scanImage, new Insets(3));
					HBox.setMargin(torqueImage, new Insets(3));

					hbox.getChildren().addAll(scan, torque, checked);

				}
				hbox.setAlignment(Pos.CENTER);
				
				if (dataCollectionPropertyBean.isShowOpNameInUnitNavigator()) {
					this.setText(operation.getId().getOperationName());
				} else {
					this.setText(operation.getUnitNo());
				}

				this.setGraphic(hbox);
				this.setContentDisplay(ContentDisplay.RIGHT);
							
			}
		}
	}
}