package com.honda.galc.client.product;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.event.SessionEvent;
import com.honda.galc.client.ui.event.SessionEventType;
import com.honda.galc.entity.product.BaseProduct;

/**
 * 
 * 
 * <h3>ProductIdProcessor Class description</h3>
 * <p> ProductIdProcessor description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Mar 11, 2014
 *
 *
 */
public class QiUPCProductIdProcessor extends AbstractProductIdProcessor{

	private String entryDept;
	private String inspectionPartName;
	
	
	public QiUPCProductIdProcessor(ProductController productIdController) {
		super(productIdController);
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public void processInputNumber(ProductEvent event) {
		Map<String, Object> mbpnProductMap = null;
		if(null!=event) {
			mbpnProductMap = (Map<String, Object>) event.getTargetObject();
			inspectionPartName = (String) mbpnProductMap.get("inspectionPartName");
			
		}
		
		BaseProduct product = getProductController().getModel().findProduct(getProductController().getView().getInputPane().getProductId());
		if(null!=product.getProductSpecCode()){
			product.setQuantity((String)mbpnProductMap.get("quantity"));
			getProductController().getModel().setMbpnProductMap(mbpnProductMap);
			getProductController().startProduct(product);
			EventBusUtil.publish(new ProductEvent(StringUtils.EMPTY, ProductEventType.PRODUCT_INPUT_OK));
			EventBusUtil.publish(new SessionEvent(SessionEventType.SESSION_START));
		}else{
			getProductController().getView().setErrorMessage("Product Spec code not found for "+inspectionPartName +" and "+entryDept);
		}
	}
	

	
}
