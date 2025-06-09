package com.honda.ahm.lc.vdb.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;

import org.apache.commons.lang3.StringUtils;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>HistoryId</code> is ... .
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Hemant Rajput
 * @created Oct 1, 2022
 */
public class ProductAgeDetailsId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name="actual_timestamp")
    private Timestamp actualTimestamp;
    
    @Column(name="process_point_id")
	private String processPointId;
    
    @Column(name="product_id")
	private String productId;
	
	public ProductAgeDetailsId() {
		super();
	}
	
	public Timestamp getActualTimestamp() {
		return actualTimestamp;
	}
	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}
	public String getProcessPointId() {
		return StringUtils.trimToEmpty(processPointId);
	}
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	public String getProductId() {
		return StringUtils.trimToEmpty(productId);
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actualTimestamp == null) ? 0 : actualTimestamp.hashCode());
		result = prime * result + ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductAgeDetailsId other = (ProductAgeDetailsId) obj;
		if (actualTimestamp == null) {
			if (other.actualTimestamp != null)
				return false;
		} else if (!actualTimestamp.equals(other.actualTimestamp))
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProductHistoryDetailsId [actualTimestamp=" + actualTimestamp + ", processPointId=" + processPointId + ", productId=" + productId + "]";
	}
    
 }
