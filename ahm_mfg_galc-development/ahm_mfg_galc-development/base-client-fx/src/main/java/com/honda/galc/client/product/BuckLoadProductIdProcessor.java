package com.honda.galc.client.product;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.checkers.CheckPointsRegistry;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.action.ProductReceivedAction;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.dataformat.BaseProductCheckerData;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.service.BuckLoadProductionScheduleService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class BuckLoadProductIdProcessor extends AbstractProductIdProcessor {

	private static final String PLAN_CODE = "PLAN_CODE";
	
	public BuckLoadProductIdProcessor(ProductController productController) {
		super(productController);
	}

	@Override
	public void processInputNumber(ProductEvent event) {
		BaseProduct product = validateAndCreateBaseProduct();
		
		if (product == null) {
			ProductTypeData productTypeData = getProductController().getView().getMainWindow().getApplicationContext().getProductTypeData();
			String productType = productTypeData == null ? "Product" : productTypeData.getProductTypeLabel();
			String msg = String.format("%s does not exist for: %s", productType, getProductController().getView().getInputPane().getProductIdField().getText());
			getProductController().getView().setErrorMessage(msg, getProductController().getView().getInputPane().getProductIdField());
			return ;
		}
		
		getProductController().getModel().setProduct(product);
		
		String inputNumber = getProductController().getView().getInputPane().getProductId();
		String productType = getProductController().getModel().getProductType();
		String processPoint = getProductController().getModel().getProcessPointId();
	
		ProductReceivedAction action = new ProductReceivedAction(getProductController().getView(), getProductController().getModel());
		if (CheckPointsRegistry.getInstance().isCheckPointConfigured(action)) {
			getProductController().getLogger().info("At CheckPoint: " + action.getCheckPointName());
			
			
			
			if (!action.executeCheckers(new BaseProductCheckerData(productType, inputNumber, processPoint))) {
				getProductController().getView().getInputPane().getProductIdField().requestFocus();
				getProductController().getView().getInputPane().getProductIdField().selectAll();
				return;
			}
			CheckPointsRegistry.getInstance().unregister(action);
		}
	
		if (!getProductController().getModel().isTrainingMode() && getProductController().getModel().getProperty().isAutoTracking())
			getProductController().startProduct(product);
	
		
		EventBusUtil.publish(new ProductEvent(StringUtils.EMPTY, ProductEventType.PRODUCT_INPUT_OK));
		
		//get the AFB information
		String planCode = PropertyService.getProperty(processPoint, PLAN_CODE,"") ;
		DefaultDataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.PROCESS_POINT_ID, processPoint);
		dc.put(DataContainerTag.PLAN_CODE, planCode);
		dc.put(DataContainerTag.VIN, inputNumber);
		BuckLoadProductionScheduleService buckLoadService = ServiceFactory.getService(BuckLoadProductionScheduleService.class);
		DataContainer afbOutput = buckLoadService.getProductSchedule(dc);
		
		if(afbOutput != null )
			getProductController().getModel().invokeBroadCast(processPoint, afbOutput, CheckPoints.AFTER_PRODUCT_PROCESSED);
		
		getProductController().cycleComplete();
		
	}

}
