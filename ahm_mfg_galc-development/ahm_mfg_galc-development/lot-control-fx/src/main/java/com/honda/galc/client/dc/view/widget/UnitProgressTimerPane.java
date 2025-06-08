package com.honda.galc.client.dc.view.widget;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.dc.enumtype.ArcColor;
import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.product.pane.AbstractWidget;
import com.honda.galc.client.ui.event.UnitNavigatorEvent;
import com.honda.galc.client.ui.event.UnitNavigatorEventType;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.service.property.PropertyService;

public class UnitProgressTimerPane extends AbstractWidget {

	
	private Timeline timer;
	private static int widgetSize;
	private UnitProgressWidget unitProgressTimer;
    private static final int currentTime = 0;
    

	static {
		widgetSize = PropertyService.getPropertyBean(PDDAPropertyBean.class).getUnitProgressTimerWidgetSize();
	}
	

	public UnitProgressTimerPane(ProductController productController) {
		super(ViewId.UNIT_PROGRESS_WIDGET, productController);
	}
	
	@Override
	protected void processProductCancelled(ProductModel productModel) {
		stopClock();
	}
	
	@Override
	protected void processProductFinished(ProductModel productModel) {
		stopClock();
	}
	
	@Override
	protected void processProductStarted(ProductModel productModel) {
	}

	@Override
	protected void initComponents() {
	}
	
	@Subscribe
	public void received(UnitNavigatorEvent event) {
		getLogger().debug("UnitProgressTimerPane.handleEvent recvd : " + event.toString());
		if (event.getType().equals(UnitNavigatorEventType.SELECTED)) {
			init(widgetSize, event.getOpTime(),currentTime);
			startClock();
		}
		
		if (event.getType().equals(UnitNavigatorEventType.MOVETO) || event.getType().equals(UnitNavigatorEventType.NEXT)
				|| event.getType().equals(UnitNavigatorEventType.PREVIOUS) || event.getType().equals(UnitNavigatorEventType.PREPARE_FOR_MOVE)) {
			stopClock();
		}
	}
	
	public void init (int size, int maxTime, int currentTime) {
		//initial time = maxTime allowed at start
		unitProgressTimer = new UnitProgressWidget("Unit Progress Timer", size, maxTime, maxTime);
		
		HBox hbox = new HBox();
		hbox.getChildren().addAll(unitProgressTimer);
		
		setCenter(hbox);
	}
	
	
	
	static class UnitProgressWidget extends BorderPane{
	   	private final int ANGLE_ORIGIN = 90;  // The 12 o'clock position
		private String caption;               // title above dial
		private int size;                     // size of the dial
		protected float maxTime;          // the maximum value the dial can show
   		private float remainingTime;  		// The amount of time remaining 
		protected float currentTime;
		
	   	public UnitProgressWidget(String caption, int size, float maxTime, int currentTime) {
	   		this.caption = caption;
	   		this.size = size;
	   		this.maxTime = maxTime;
	   		this.currentTime= currentTime;
	   	}
	   	
	   	public float getRemainingTime() {
	   		return remainingTime;
	   	}
	   	
	   	protected double getArcAngle() {
	   		double angle = Math.min ((360.0 / (maxTime )) * currentTime, 360);
	   		return angle;
	   	}
	   	
	   	protected ArcColor getArcColor() {
	   		if(currentTime == 0 )	return ArcColor.ONTIME;
	   		else if(currentTime < 0) return ArcColor.LATE;
	   		else return ArcColor.TIMER;	
	   	}
	   	
	   	protected String getLabelCaption() {
	   		currentTime -=1;
	   		return String.format("%d",(int)Math.ceil(currentTime));
	   	}
	
		protected void update() {
			int dialRadius = size / 2;
			float innerCircleRadius = dialRadius * 0.50f;
			int fontSize = size / 4;
			
			
			VBox vbox = new VBox();
			Text title = UiFactory.createText();
			title.setText(caption);
			title.setFont(new Font(size * 0.15));
			title.setWrappingWidth(size);
			title.setTextAlignment(TextAlignment.CENTER);
			title.setX((size - title.getLayoutBounds().getWidth() / 2));
			vbox.setAlignment(Pos.CENTER);

			Group root = new Group();

			Circle outline = new Circle();
			outline.setRadius(dialRadius);
			outline.setCenterX(dialRadius);
			outline.setCenterY(dialRadius);
			outline.setStroke(Color.DARKGRAY);
			outline.setFill(getArcColor().bg());

			Arc arc = new Arc();
			arc.setFill(getArcColor().fg());
			arc.setRadiusX(dialRadius);
			arc.setRadiusY(dialRadius);
			
			/* * JavaFx draws and arc in anti-clockwise direction so we have to draw
			 * the arc from the end position to the 12 o'clock position/.
*/			 
			double angle = getArcAngle();
			arc.setStartAngle(ANGLE_ORIGIN - angle);
			arc.setLength(angle);
			arc.setCenterX(dialRadius);
			arc.setCenterY(dialRadius);
			arc.setStroke(Color.DARKGRAY);
			arc.setType(ArcType.ROUND);

			Text currentValueText = UiFactory.createText();
			currentValueText.setFont(new Font(30));
			currentValueText.setWrappingWidth(200);
			currentValueText.setTextAlignment(TextAlignment.CENTER);
		
			currentValueText.setText(getLabelCaption());
			currentValueText.setFont(Font.font("Verdana", fontSize));
			double height = currentValueText.getLayoutBounds().getHeight();
			double width = currentValueText.getLayoutBounds().getWidth();
			currentValueText.setX((size - width) / 2);
			currentValueText.setY((size + (height * 0.75 )) / 2);

			Circle circle = new Circle();
			circle.setRadius(innerCircleRadius);
			circle.setCenterX(dialRadius);
			circle.setCenterY(dialRadius);
			circle.setFill(Color.LIGHTGRAY);
			circle.setStroke(Color.DARKGRAY);
			root.getChildren().addAll(outline, arc, circle, currentValueText);
			vbox.getChildren().addAll(root,title);
			setCenter(vbox);
		}
	}
	

	public void startClock() {
		if (timer!=null) {
			timer.stop();
		}
		
	    timer =  new Timeline(new KeyFrame(Duration.seconds(1),new EventHandler<javafx.event.ActionEvent>() {
		  public void handle(javafx.event.ActionEvent event) {
			  unitProgressTimer.update();
		  };
		}));
		timer.setCycleCount(Timeline.INDEFINITE);
		timer.play();
	}
	
	
	
	public void stopClock() {
		timer.stop();
	}
	
	

}