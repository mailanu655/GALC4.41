package com.honda.galc.client.teamleader;

/** * * 
* 
* @author Gangadhararao Gadde 
* @since May 16, 2016
*/

public class ProductPrintQueueItem {
	
	private String _productId = null;
	private String _seqNum = null;	
	private String _printerName = null;
	private String _trackingProcessPoint = null;
	private String _printData = null;
	private String _mtoc = null;
	private String status = null;
		
	
	public void setProductId(String productId) {
		_productId = productId;
	}
	
	
	public String getProductId() {
		return _productId;
	}
	
	public String getMtoc(){
		return _mtoc;
	}
	
	public void setMtoc(String mtoc){
		_mtoc = mtoc;
	}

	public void setSeqNum(String seqNum) {
		_seqNum = seqNum;
	}
	
	public String getSeqNum() {
		return _seqNum;
	}
	
	public void setPrinterName(String printerName) {
		_printerName = printerName;
	}
	
	public String getPrinterName() {
		return _printerName;
	}
	
	public void setTrackingProcessPoint(String trackingProcessPoint) {
		_trackingProcessPoint = trackingProcessPoint;
	}
	
	public String getTrackingProcessPoint() {
		return _trackingProcessPoint;
	}
	
	public void setPrintData(String printData) {
		_printData = printData;
	}
	
	public String getPrintData() {
		return _printData;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}

