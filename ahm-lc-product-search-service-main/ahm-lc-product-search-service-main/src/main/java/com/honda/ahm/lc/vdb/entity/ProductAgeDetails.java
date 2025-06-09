package com.honda.ahm.lc.vdb.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;
/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>ProductAgeDetails</code> is view class for Product Age.
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
 * @created Apr 22, 2021
 */

@Entity
@Immutable
@Subselect(value="select "
		+ "MIN(ACTUAL_TIMESTAMP) as actual_timestamp, "
		+ "PROCESS_POINT_ID as process_point_id, "
		+ "PRODUCT_ID as product_id "
		+ "from GAL215TBX "
		+ "group by PROCESS_POINT_ID, PRODUCT_ID")
public class ProductAgeDetails implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private ProductAgeDetailsId id;
	
	@Column(name="actual_timestamp", insertable = false, updatable = false)
	private Timestamp actualTimestamp;
	
	@Column(name="process_point_id", insertable = false, updatable = false)
	private String processPointId;
	
	@Column(name="product_id", insertable = false, updatable = false)
	private String productId;
	
	@Transient
	private String age;
	    
	public ProductAgeDetails() {
		super();
	}

	@Override
	public String toString() {
		
		String str = getClass().getSimpleName() + "{";
		str = str + "id: " + getId();
        str = str + ", actualTimestamp: " + getId().getActualTimestamp();
        str = str + ", processPointId: " + getId().getProcessPointId();
        str = str + "}";
        return str;
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actualTimestamp == null) ? 0 : actualTimestamp.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ProductAgeDetails other = (ProductAgeDetails) obj;
		if (actualTimestamp == null) {
			if (other.actualTimestamp != null)
				return false;
		} else if (!actualTimestamp.equals(other.actualTimestamp))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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

	public ProductAgeDetailsId getId() {
		return id;
	}

	public void setId(ProductAgeDetailsId id) {
		this.id = id;
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
	
	public void setAge(String age) {
		this.age = age;
	}
	
	public String getAge() {
		long frequency = (long)(TimeUnit.DAYS.convert(new Date().getTime() - this.actualTimestamp.getTime(), TimeUnit.MILLISECONDS));
		return String.valueOf(frequency);
	}
	
}