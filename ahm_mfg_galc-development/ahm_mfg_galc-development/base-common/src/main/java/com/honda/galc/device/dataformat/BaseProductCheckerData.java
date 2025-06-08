package com.honda.galc.device.dataformat;

import java.io.Serializable;
import org.apache.commons.lang.StringUtils;

/**
 * @author Brandon Kroeger
 * @date Jul 17, 2015
 */
public class BaseProductCheckerData extends BaseCheckerData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String productType;
	
	
	public BaseProductCheckerData() {
		super();
	}

	public BaseProductCheckerData(String productType, String productId, String currentProcessPoint) {
		super(productId, StringUtils.trim(currentProcessPoint));
		this.productType = StringUtils.trim(productType);
	}
	
	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = StringUtils.trim(productType);
	}

}
