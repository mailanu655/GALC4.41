package com.honda.galc.client.dc.view.widget;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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
import com.honda.galc.dao.conf.MCOpEfficiencyHistoryDao;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class AccumulatedTimerPane  extends AbstractWidget {

	
	private static int widgetSize;
	private AccumulatedTimerWidget widget;
    

	static {
		widgetSize = PropertyService.getPropertyBean(PDDAPropertyBean.class).getAccumulatedTimerWidgetSize();
	}
	

	public AccumulatedTimerPane(ProductController productController) {
		super(ViewId.ACCUMULATED_TIMER_WIDGET, productController);
	}
	
	@Override
	protected void processProductCancelled(ProductModel productModel) {
		
	}
	
	@Override
	protected void processProductFinished(ProductModel productModel) {
		
	}
	
	@Override
	protected void processProductStarted(ProductModel productModel) {
	}

	@Override
	protected void initComponents() {
	}
	
	@Subscribe
	public void received(UnitNavigatorEvent event) {
		getLogger().debug("AccumulatedTimerWidget.handleEvent recvd : " + event.toString());
		if (event.getType().equals(UnitNavigatorEventType.SELECTED)) {
			ProductModel productModel = getProductController().getModel();
			Double actualTime = ServiceFactory.getDao(
					MCOpEfficiencyHistoryDao.class).getActualTimeInSeconds(
					productModel.getProductId(), productModel.getProcessPointId(),
					productModel.getApplicationContext().getTerminalId());
			actualTime = (actualTime != null)?actualTime : 0.0;
			init(widgetSize, (int) (event.getTotalTimeForCompletedUnits()-actualTime));
			widget.update();
		}
	}
	
	public void init (int size, int maxTime) {
		//initial time = maxTime allowed at start
		widget = new AccumulatedTimerWidget("Accumulated Timer", size, maxTime);
		
		HBox hbox = new HBox();
		hbox.getChildren().addAll(widget);
		
		setCenter(hbox);
	}
	
	
	
	static class AccumulatedTimerWidget extends BorderPane{
	   private String caption;               // title above dial
		private int size;                     // size of the dial
		protected float accumulatedTime;          // the maximum value the dial can show
   		
	   	public AccumulatedTimerWidget(String caption, int size, float accumulatedTime) {
	   		this.caption = caption;
	   		this.size = size;
	   		this.accumulatedTime = accumulatedTime;
	   	}
	   	
	   	protected ArcColor getArcColor() {
	   		if(accumulatedTime == 0 )	return ArcColor.ONTIME;
	   		else if(accumulatedTime < 0) return ArcColor.LATE;
	   		else return ArcColor.TIMER;	
	   	}
	   	
	   	protected String getLabelCaption() {
	   		return String.format("%d",(int)Math.ceil(accumulatedTime));
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
			root.getChildren().addAll(outline, circle, currentValueText);
			vbox.getChildren().addAll(root,title);
			setCenter(vbox);
		}
	}
}