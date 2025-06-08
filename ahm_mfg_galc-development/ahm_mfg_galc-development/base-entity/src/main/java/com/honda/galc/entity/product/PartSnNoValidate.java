package com.honda.galc.entity.product;

import java.io.Serializable;

public class PartSnNoValidate implements Serializable {

	/**
	 * Serial version id
	 */
	private static final long serialVersionUID = 1L;
	
	private PartSpec partSpec;
    private String partSrNo;
    private String productId;
    private String processPoint;
    private boolean dateScanFlag;
    private String productType;
	private PartSnNoValidate(){
		super();
	}
	public PartSpec getPartSpec() {
		return partSpec;
	}
	public String getPartSrNo() {
		return partSrNo;
	}
	public String getProductId() {
		return productId;
	}
	public String getProcessPoint() {
		return processPoint;
	}
	public boolean isDateScanFlag() {
		return dateScanFlag;
	}
	public String getProductType() {
		return productType;
	}

}
