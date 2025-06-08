package com.honda.galc.client.dc.view.widget;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.product.pane.AbstractWidget;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.service.property.PropertyService;

public class AdvancedProcessCycleTimer extends AbstractWidget {

	private  Map<String,Integer>  lineSpeedMap;
	private final String DEFAULT_LINE = "DEFAULT";
	
	public AdvancedProcessCycleTimer(ProductController productController) {
		super(ViewId.ADVANCED_PROCESS_CYCLE_TIMER_WIDGET, productController);
	}

	@Override
	protected void processProductCancelled(ProductModel productModel) {
	}

	@Override
	protected void processProductFinished(ProductModel productModel) {
	}

	@Override
	protected void processProductStarted(ProductModel productModel) {
		this.setTop(getProductionMetrics());

	}

	@Override
	protected void initComponents() {
	}

	private Node getProductionMetrics() {

		VBox vbox = new VBox();
		vbox.setId("black-box");
		vbox.setAlignment(Pos.TOP_CENTER);
		vbox.setPrefWidth(400);

		Label title = UiFactory.createLabel("title", "Max Cycle Time");
		Label number = UiFactory.createLabel("number", getProcessTime() + "",Fonts.SS_DIALOG_BOLD(25));
		
		vbox.getChildren().addAll(title,number);

		return vbox;
	}

	private int getProcessTime() {
		ProcessPoint processPoint =  ClientMainFx.getInstance(). getApplicationContext().getProcessPoint();
		String lineId = processPoint.getLineId();
		String processPointId = processPoint.getProcessPointId();
		loadLineSpeedMap(processPointId);
		if (lineSpeedMap.containsKey(lineId)) {
		  return lineSpeedMap.get(lineId);
		} else if (lineSpeedMap.containsKey(DEFAULT_LINE)){
		  return lineSpeedMap.get(DEFAULT_LINE);
		} else {
		  return 0;
		}
	}
	
	private void loadLineSpeedMap(String processPointId) {
		lineSpeedMap = PropertyService.getPropertyBean(PDDAPropertyBean.class,processPointId).getLineSpeed(Integer.class);
		if (lineSpeedMap == null) {
			lineSpeedMap = new HashMap<String, Integer>();
		}
	}


}
