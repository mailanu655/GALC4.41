package com.honda.galc.device.dataformat;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


/**
 * @author Subu Kathiresan
 * @date Nov 26, 2014
 */
public class PartSerialScanData extends InputData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String mask;
	private String serialNumber;
	private String productType;
	private String productId;
	private String partName;
	private String overrrideUser;
	private String productSpecCode;
	private String modelCode;
	private long strucRev;
	private Map<String, String> bearingPickOperation;
	
	
	public PartSerialScanData() {
		super();
	}

	public PartSerialScanData(String mask, String serialNumber) {
		super();
		this.mask = StringUtils.trim(mask);
		this.serialNumber = StringUtils.trim(serialNumber);
	}

	//bak - 20150702 - overloaded constructor to allow passing product type and product id
	public PartSerialScanData(String mask, String serialNumber, String productType, String productId) {
		super();
		this.mask = StringUtils.trim(mask);
		this.serialNumber = StringUtils.trim(serialNumber);
		this.productType = StringUtils.trim(productType);
		this.productId = StringUtils.trim(productId);
	}

	//sdw - 20150819 - overloaded constructor to allow passing product type, product id and part name
	public PartSerialScanData(String mask, String serialNumber, String productType, String productId, String partName) {
		super();
		this.mask = StringUtils.trim(mask);
		this.serialNumber = StringUtils.trim(serialNumber);
		this.productType = StringUtils.trim(productType);
		this.productId = StringUtils.trim(productId);
		this.partName = StringUtils.trim(partName);
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = StringUtils.trim(mask);
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = StringUtils.trim(serialNumber);
	}
	
	//bak - 20150702 - added get/set methods for product type
	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = StringUtils.trim(productType);
	}
	
	//bak - 20150702 - added get/set methods for product id
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = StringUtils.trim(productId);
	}

	//sdw - 20150819 - added get/set methods for part name
	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = StringUtils.trim(partName);
	}


	public String getOverrrideUser() {
		return overrrideUser;
	}

	public void setOverrrideUser(String overrrideUser) {
		this.overrrideUser = overrrideUser;
	}
	

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public Map<String, String> getBearingPickOperation() {
		return bearingPickOperation;
	}

	public void setBearingPickOperation(Map<String, String> bearingPickOperation) {
		this.bearingPickOperation = bearingPickOperation;
	}

	
	public long getStrucRev() {
		return strucRev;
	}

	public void setStrucRev(long strucRev) {
		this.strucRev = strucRev;
	}

	@Override
	public String toString() {
		return this.mask + " " + 
				this.serialNumber + " " +
				this.productType + " " + 
				this.productId + " " +
				this.partName+" "+
				this.overrrideUser;
	}
}