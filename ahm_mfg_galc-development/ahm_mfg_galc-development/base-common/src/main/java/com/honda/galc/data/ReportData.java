package com.honda.galc.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ReportData implements Serializable {
	
	private static final long serialVersionUID = 9071535121492875787L;
	
	private String destinationId;
	private String trayValue = "0";
	private boolean duplexCheck = false;
	private Map<String,String> attributesMap = new HashMap<String,String>();
	private String templateId;
	private String templateData;
	private String productId;
	private String partName;
	private String channelName;
	private String queueManagerName;
	private String hostName;
	private String printerName;
	
	public ReportData(){
		
	}
	
	public boolean isDuplexCheck(){
		return duplexCheck;
	}
	
	public void setDuplexCheck(boolean duplexCheck){
		this.duplexCheck = duplexCheck;
	}
	
	public String getTrayValue(){
		return trayValue;
	}

	public void setTrayValue(String trayValue) {
		this.trayValue = trayValue;
	}
	
	public String getHostName(){
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTemplateData() {
		return templateData;
	}

	public void setTemplateData(String templateData) {
		this.templateData = templateData;
	}
	
	public String getDestinationId() {
		return destinationId;
	}
	
	public void setDestinationId(String destinationId) {
		this.destinationId = destinationId;
	}
	
	public Map<String, String> getAttributesMap() {
		return attributesMap;
	}
	
	public void setAttributesMap(HashMap<String, String> attributesMap) {
		this.attributesMap = attributesMap;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}
	
	public String getChannelName() {
		return channelName;
	}
	
	public void setChannelName(String chName) {
		this.channelName = chName;
	}
	
	public String getQueueManagerName() {
		return queueManagerName;
	}
	
	public void setQueueManagerName(String qManagerName) {
		this.queueManagerName = qManagerName;
	}

	public String getPrinterName() {
		return printerName;
	}
	
	public void setPrinterName(String printer) {
		this.printerName = printer;
	}

}



