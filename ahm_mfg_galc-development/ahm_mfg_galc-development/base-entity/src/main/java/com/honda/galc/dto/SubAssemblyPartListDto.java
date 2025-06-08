/**
 * 
 */
package com.honda.galc.dto;

import org.apache.commons.lang.StringUtils;

/**
 * @author vf031824
 *
 */
public class SubAssemblyPartListDto implements IDto{
	
	private static final long serialVersionUID = 1L;
	
	@DtoTag()
	private String productId = null;
	
	@DtoTag()
	private String partName = null;
	
	@DtoTag()
	private String partSerialNumber = null;
	
	@DtoTag()
	private String partId = null;
	
	@DtoTag
	private String productType = null;
	
	public SubAssemblyPartListDto() {}
	
	public String getProductId() {
		return StringUtils.trim(this.productId);
	}
	
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getPartName() {
		return StringUtils.trim(this.partName);
	}
	
	public void setPartName(String partName) {
		this.partName = partName;
	}
	
	public String getPartSerialNumber() {
		return StringUtils.trim(this.partSerialNumber);
	}
	
	public void setPartSerialNumber(String partSerialNumber) {
		this.partSerialNumber = partSerialNumber;
	}
	
	public String getPartId() {
		return StringUtils.trim(this.partId);
	}
	
	public void setPartId(String part) {
		this.partId = part;
	}

	public String getProductType() {
		return StringUtils.trim(this.productType);
	}
	
	public void setProductType(String productType) {
		this.productType = productType;
	}
}