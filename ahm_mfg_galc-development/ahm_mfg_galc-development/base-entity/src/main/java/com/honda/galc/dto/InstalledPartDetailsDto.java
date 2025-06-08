package com.honda.galc.dto;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.ToStringUtil;

/**
 * @author Subu Kathiresan
 * @date June 06, 2016
 */
public class InstalledPartDetailsDto implements IDto {

	private static final long serialVersionUID = 1L;
	
	public static final String STATUS_GOOD = "GOOD";
	public static final String INSTALLED = "INSTALLED";

	@DtoTag()
	private String partName = null;
		
	@DtoTag()
	private int partConfirmFlag = -1;	
		
	@DtoTag()
	private String installed = null;
	
	@DtoTag()
	private String status = null;
	
	@DtoTag()
	private String productType = null;
	
	@DtoTag()
	private String subProductType = null;
	
	@DtoTag()
	private String partSerialNumber = null;
	
	public InstalledPartDetailsDto() {}

	public String getPartName() {
		return StringUtils.trimToEmpty(partName);
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public int getPartConfirmFlag() {
		return partConfirmFlag;
	}

	public void setPartConfirmFlag(int partConfirmFlag) {
		this.partConfirmFlag = partConfirmFlag;
	}

	public boolean isInstalled() {
		return installed.equalsIgnoreCase(INSTALLED) ? true:false;
	}

	public void setInstalled(String installed) {
		this.installed = installed;
	}

	public String getStatus() {
		return StringUtils.trimToEmpty(status);
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getProductType() {
		return StringUtils.trimToEmpty(productType);
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getSubProductType() {
		return StringUtils.trimToEmpty(subProductType);
	}

	public void setSubProductType(String subProductType) {
		this.subProductType = subProductType;
	}

	public String getPartSerialNumber() {
		return StringUtils.trimToEmpty(partSerialNumber);
	}

	public void setPartSerialNumber(String partSerialNumber) {
		this.partSerialNumber = partSerialNumber;
	}
	
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}
