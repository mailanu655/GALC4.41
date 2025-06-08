package com.honda.galc.data;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class InputData implements Serializable{

	private static final long serialVersionUID = 1L;
	private String productType;
	private String productId;
	private String processPointId;
	private String checkType;
	private String partSerialNumber;
    private String engine;
	private Boolean useAltEngineMto = false;
	private List<String> processPointIds;
	private List<String> partNames;
	private List<String> specialCheckerNames;
	
	public List<String> getSpecialCheckerNames() {
		return specialCheckerNames;
	}
	public void setSpecialCheckerNames(List<String> specialCheckerNames) {
		this.specialCheckerNames = specialCheckerNames;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProcessPointId() {
		return processPointId;
	}
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	public String getCheckType() {
		return StringUtils.trimToEmpty(checkType);
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public List<String> getPartNames() {
		return partNames;
	}
	public void setPartNames(List<String> partNames) {
		this.partNames = partNames;
	}
	public String getPartSerialNumber() {
		return partSerialNumber;
	}
	public void setPartSerialNumber(String partSerialNumber) {
		this.partSerialNumber = partSerialNumber;
	}
		
	public String getEngine() {
		return engine;
	}
	public void setEngine(String engine) {
		this.engine = engine;
	}
	public Boolean getUseAltEngineMto() {
		return useAltEngineMto;
	}
	public void setUseAltEngineMto(Boolean useAltEngineMto) {
		this.useAltEngineMto = useAltEngineMto;
	}
	
	public List<String> getProcessPointIds() {
		return processPointIds;
	}
	public void setProcessPointIds(List<String> processPointIds) {
		this.processPointIds = processPointIds;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("InputData [productId=").append(productId);
		sb.append(", productType=").append(productType);
		sb.append(", processPointId=").append(processPointId);
		if(StringUtils.isNotBlank(partSerialNumber)){
			sb.append(", partSerialNumber=").append(partSerialNumber);
		}
		if(partNames != null && !partNames.isEmpty()){
			sb.append(", partNames=").append(partNames);
		}
		if(engine != null && !engine.isEmpty()){
			sb.append(", engine=").append(engine);
		}
		if(useAltEngineMto != null) {
			sb.append(", useAltEngineMto=").append(useAltEngineMto);
		}
		if(processPointIds != null && !processPointIds.isEmpty()){
			sb.append(", processPointIds=").append(processPointIds);
		}

		sb.append("]");
	    return sb.toString();

	}
	public String getPartNamesAsString() {
		return String.join(",", getPartNames());
	}

}
