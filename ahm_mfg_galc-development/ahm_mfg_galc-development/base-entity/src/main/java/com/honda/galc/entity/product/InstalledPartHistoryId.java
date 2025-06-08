package com.honda.galc.entity.product;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class InstalledPartHistoryId implements Serializable {
    @Column(name = "PRODUCT_ID")
    private String productId;

    @Column(name = "PART_NAME")
    private String partName;
    
	@Column(name = "ACTUAL_TIMESTAMP")
	private Timestamp actualTimestamp;

	private static final long serialVersionUID = 1L;

    public InstalledPartHistoryId() {
        super();
    }
    
    public InstalledPartHistoryId(String productId, String partName,
			Timestamp actualTimestamp) {
		super();
		this.productId = productId;
		this.partName = partName;
		this.actualTimestamp = actualTimestamp;
	}

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
    
    public Timestamp getActualTimestamp() {
		return actualTimestamp;
	}

	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actualTimestamp == null) ? 0 : actualTimestamp.hashCode());
		result = prime * result
				+ ((partName == null) ? 0 : partName.hashCode());
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
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
		InstalledPartHistoryId other = (InstalledPartHistoryId) obj;
		if (actualTimestamp == null) {
			if (other.actualTimestamp != null)
				return false;
		} else if (!actualTimestamp.equals(other.actualTimestamp))
			return false;
		if (partName == null) {
			if (other.partName != null)
				return false;
		} else if (!partName.equals(other.partName))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		return true;
	}
	
	

}
