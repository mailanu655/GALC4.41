package com.honda.galc.entity.conf;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;


@Entity
@Table(name = "GAL263TBX")
public class ShippingStatus extends AuditEntry {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "VIN")
    private String vin;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "ACTUAL_TIMESTAMP")
    private Date actualTimestamp;

    @Column(name = "INVOICED")
    private String invoiced;
    
    @Column(name = "ON_TIME_SHIPPING")
    private Integer onTimeShipping;
    
    @Column(name = "DEALER_NO")
    private String dealerNo;
    

    public ShippingStatus() {
        super();
    }

    public String getId() {
    	return getVin();
    }


	public String getVin() {
		return StringUtils.trimToEmpty(vin);
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


	public Date getActualTimestamp() {
		return actualTimestamp;
	}


	public void setActualTimestamp(Date actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}


	public String getInvoiced() {
		return StringUtils.trimToEmpty(invoiced);
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
		return toString(getVin(),getInvoiced(),getStatus(),getActualTimestamp(),getOnTimeShipping());
	}
     
}
