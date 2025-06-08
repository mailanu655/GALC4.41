package com.honda.galc.client.dc.view;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import com.honda.galc.client.dc.processor.OperationProcessor;
import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.service.property.PropertyService;

public class OperationInfoWidget extends OperationView {

	private static final long serialVersionUID = 1L;

	public OperationInfoWidget(OperationProcessor processor) {
		super(processor);
	}

	public void initComponents() {
		//get parameter from property
		PDDAPropertyBean property = PropertyService.getPropertyBean(PDDAPropertyBean.class);
		String width = property.getOperationInfoWidth();
		String height = property.getOperationInfoHeight();
		String fontsize = property.getOperationInfoFontSize();
		
		WebView view = new WebView();
		view.setPrefSize(Double.parseDouble(width)+50, Double.parseDouble(height));

		//view.setPrefWidth(Double.parseDouble(width)+50);
		WebEngine engine = view.getEngine();
		
		String hosturl = getProcessor().getController().getModel().getProductModel().getApplicationContext().getArguments().getServerURL();
		String url ="http://"+ViewControlUtil.getHostURL(hosturl)+"/BaseWeb/OperationInfoWidget.html?width="+width+"&height="+height+"&fontsize="+fontsize+"&pddaMaintenanceId=54945485";
		
		engine.load(url);
		this.setCenter(view);
	}

	protected void init() {
	}

}