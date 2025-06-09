package com.honda.ahm.lc.model;

import java.text.SimpleDateFormat;

public class ShippingStatus extends AuditEntry {
    private static final long serialVersionUID = 1L;

        
    private String vin;

    
    private Integer status;


    private String actualTimestamp;

   
    private String invoiced;
    
  
    private Integer onTimeShipping;
    
   
    private String dealerNo;
    

    public ShippingStatus() {
        super();
    }


	public String getVin() {
		return vin;
	}


	public void setVin(String vin) {
		this.vin = vin;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public String getActualTimestamp() {
		
		return actualTimestamp;
	}


	public void setActualTimestamp(String actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}


	public String getInvoiced() {
		return invoiced;
	}


	public void setInvoiced(String invoiced) {
		this.invoiced = invoiced;
	}


	public Integer getOnTimeShipping() {
		return onTimeShipping;
	}


	public void setOnTimeShipping(Integer onTimeShipping) {
		this.onTimeShipping = onTimeShipping;
	}


	public String getDealerNo() {
		return dealerNo;
	}


	public void setDealerNo(String dealerNo) {
		this.dealerNo = dealerNo;
	}


	@Override
	public String toString() {
		return "ShippingStatus [vin=" + vin + ", status=" + status + ", actualTimestamp=" + actualTimestamp
				+ ", invoiced=" + invoiced + ", onTimeShipping=" + onTimeShipping + ", dealerNo=" + dealerNo + "]";
	}
    
	public String toJsonString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss z");
		return "\"vin\":" + vin + ", \"status\":" + status + ", \"actualTimestamp\":" + sdf.format(actualTimestamp)
				+ ", \"invoiced\":" + invoiced + ", \"onTimeShipping\":" + onTimeShipping + ", \"dealerNo\":" + dealerNo + ",\"createTimestamp\":"+sdf.format(getCreateTimestamp())
				+",\"updateTimestamp\":"+sdf.format(getUpdateTimestamp());
	}
    
}