package com.honda.galc.client.product.widget;

import com.honda.galc.service.property.PropertyService;

import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class ProductionScheduleUnitViewWidget extends BorderPane {

	private static final long serialVersionUID = 1L;

	public ProductionScheduleUnitViewWidget() {
		initComponents();
	}

	public void initComponents() {
		//get parameter from property
		//PDDAPropertyBean property = PropertyService.getPropertyBean(PDDAPropertyBean.class);
		//String width = property.getOperationInfoWidth();
		//String height = property.getOperationInfoHeight();
		
		WebView view = new WebView();
		WebEngine engine = view.getEngine();
		String url ="http://localhost:8080/BaseWeb/ProductionScheduleUnitViewWidget.html?product-id=5FNRL5H9XEB088398&height=160&width=600&font-size=140&number-of-records=5";
		
		engine.load(url);
		this.setCenter(view);
	}

	protected void init() {
	}

}
