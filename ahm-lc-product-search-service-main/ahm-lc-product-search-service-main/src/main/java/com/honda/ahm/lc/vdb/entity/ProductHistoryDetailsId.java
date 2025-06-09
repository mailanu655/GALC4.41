package com.honda.ahm.lc.vdb.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;

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
public class ProductHistoryDetailsId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name="actual_timestamp")
    private LocalDateTime actualTimestamp;
    
    @Column(name="defect_id")
	private String defectId;
    
    @Column(name="process_point_id")
	private String processPointId;
    
    @Column(name="process_point_name")
	private String processPointName;
    
    @Column(name="product_id")
	private String productId;
	
	public ProductHistoryDetailsId() {
		super();
	}

	public LocalDateTime getActualTimestamp() {
		return actualTimestamp;
	}

	public void setActualTimestamp(LocalDateTime actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	public String getDefectId() {
		return defectId;
	}

	public void setDefectId(String defectId) {
		this.defectId = defectId;
	}

	public String getProcessPointId() {
		return processPointId;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getProcessPointName() {
		return processPointName;
	}

	public void setProcessPointName(String processPointName) {
		this.processPointName = processPointName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actualTimestamp == null) ? 0 : actualTimestamp.hashCode());
		result = prime * result + ((defectId == null) ? 0 : defectId.hashCode());
		result = prime * result + ((processPointName == null) ? 0 : processPointName.hashCode());
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
		ProductHistoryDetailsId other = (ProductHistoryDetailsId) obj;
		if (actualTimestamp == null) {
			if (other.actualTimestamp != null)
				return false;
		} else if (!actualTimestamp.equals(other.actualTimestamp))
			return false;
		if (defectId == null) {
			if (other.defectId != null)
				return false;
		} else if (!defectId.equals(other.defectId))
			return false;
		if (processPointName == null) {
			if (other.processPointName != null)
				return false;
		} else if (!processPointName.equals(other.processPointName))
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
		return "ProductHistoryDetailsId [actualTimestamp=" + actualTimestamp + ", defectId=" + defectId
				+ ", processPointId=" + processPointId + ", processPointName=" + processPointName
				+ ", productId=" + productId + "]";
	}
	
}
