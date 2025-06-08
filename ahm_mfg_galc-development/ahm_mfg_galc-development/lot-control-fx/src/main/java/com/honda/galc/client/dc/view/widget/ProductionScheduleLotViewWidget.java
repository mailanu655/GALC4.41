package com.honda.galc.client.dc.view.widget;

import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.client.dc.view.ViewControlUtil;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.product.pane.AbstractWidget;
import com.honda.galc.service.property.PropertyService;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class ProductionScheduleLotViewWidget extends AbstractWidget {

	private static final long serialVersionUID = 1L;

	public ProductionScheduleLotViewWidget(ProductController productController) {
		super(ViewId.PRODUCTION_SCHEDULE_LOTVIEWWIDGET, productController);
	}
	
	//@Override
	protected void init() {
			
		//get parameter from property
		PDDAPropertyBean property = PropertyService.getPropertyBean(PDDAPropertyBean.class);
		String width = property.getProductionScheduleLotWidth();
		String height = property.getProductionScheduleLotHeight();
		String fontsize = property.getProductionScheduleLotFontSize();
		String rows = property.getProductionScheduleLotRows();
			
		WebView view = new WebView();
		WebEngine engine = view.getEngine();
		String hosturl = getProductController().getModel().getApplicationContext().getArguments().getServerURL();
		String a = getProductController().getModel().getProductId();
		String b = getProductController().getProductTypeData().getProductType().toString();
		String url ="http://"+ViewControlUtil.getHostURL(hosturl)+"/BaseWeb/ProductionScheduleLotViewWidget.html?product-id="+getProductController().getModel().getProductId()+"&height="+height+"&width="+width+"&font-size="+fontsize+"&number-of-records="+rows;
		engine.load(url);
		this.setCenter(view);
	}
	
	@Override
	protected void processProductCancelled(ProductModel productModel) {
		//setExpectedProductId(productModel.getExpectedProductId());
	}
	
	@Override
	protected void processProductFinished(ProductModel productModel) {
	}
	
	@Override
	protected void processProductStarted(ProductModel productModel) {
		init();
	}

	@Override
	protected void initComponents() {
		// TODO Auto-generated method stub
		
	}

}
