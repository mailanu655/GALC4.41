/**
 * 
 */
package com.honda.galc.client.qi.base;

import static com.honda.galc.service.ServiceFactory.getDao;

import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.MultiLineHelper;
import com.honda.galc.util.ProductCheckUtil;

/**
 * @author VCC44349
 *
 */
public abstract class AbstractQiDefectProcessModel extends QiProcessModel {

	/**
	 * 
	 */
	public AbstractQiDefectProcessModel() {
		super();
	}
	/**
	 * This method is used to get selected Product.
	 */
	public BaseProduct getProduct() {
		return getProductModel().getProduct();
	}
	
	//check if current tracking status is invalid previous line ID for this process point
	public boolean isPreviousLineInvalid() {
		boolean isLineIdCheckEnabled = PropertyService.getPropertyBean(ProductCheckPropertyBean.class, getCurrentWorkingProcessPoint().getProcessPointId()).isLineIdCheckEnabled();
		if (isLineIdCheckEnabled) {
			ProductCheckUtil productCheckUtil = new ProductCheckUtil();
			//get current tracking status
			BaseProduct product = ProductTypeUtil.getTypeUtil(getProduct().getProductType()).findProduct(getProduct().getProductId());
			productCheckUtil.setProduct(product); 
			//get current working process point
			MultiLineHelper multiLineHelper = MultiLineHelper.getInstance(getProcessPointId());
			productCheckUtil.setProcessPoint(multiLineHelper.getProcessPointToUse(product));
			return productCheckUtil.invalidPreviousLineCheck();
		} else {
			return false;
		}
	}

}