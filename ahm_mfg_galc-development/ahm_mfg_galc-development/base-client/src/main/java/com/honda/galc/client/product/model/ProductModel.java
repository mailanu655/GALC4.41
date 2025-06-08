package com.honda.galc.client.product.model;

import static com.honda.galc.service.ServiceFactory.getService;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProductModel</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class ProductModel {

	private ApplicationContext applicationContext;
	private ProductPropertyBean property;
	private BaseProduct product;

	public ProductModel(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
		this.property = PropertyService.getPropertyBean(ProductPropertyBean.class, getApplicationContext().getProcessPointId());
	}

	public BaseProduct findProduct(String pin) {
		ProductTypeData productTypeData = getApplicationContext().getProductTypeData();
		BaseProduct product = getProductDao(productTypeData.getProductType()).findByKey(pin);
		return product;
	}

	public void invokeTracking() {
		ProcessPoint processPoint = getApplicationContext().getProcessPoint();
		if (processPoint != null && (processPoint.isTrackingPoint() || processPoint.isPassingCount())) {
			getService(TrackingService.class).track(getProduct(), processPoint.getProcessPointId());
		}
	}

	// === get/set === //
	public ProductDao<? extends BaseProduct> getProductDao(ProductType productType) {
		return ProductTypeUtil.getProductDao(productType);
	}

	protected ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public ProductPropertyBean getProperty() {
		return property;
	}

	public BaseProduct getProduct() {
		return product;
	}

	public void setProduct(BaseProduct product) {
		this.product = product;
	}
}
