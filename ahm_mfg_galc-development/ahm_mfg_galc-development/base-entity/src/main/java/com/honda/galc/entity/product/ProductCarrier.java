package com.honda.galc.entity.product;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name="PRODUCT_CARRIER_TBX")
public class ProductCarrier extends AuditEntry {
	
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private ProductCarrierId id;

	@Column(name="PROCESS_POINT_ID")
	private String processPointId;

	@Column(name="OFF_TIMESTAMP")
	private Timestamp offTimestamp;


	public ProductCarrier() {
		super();
	}

	public ProductCarrierId getId() {
		return this.id;
	}

	public void setId(ProductCarrierId id) {
		this.id = id;
	}

	public String getProcessPointId() {
		return StringUtils.trim(this.processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	

	public Timestamp getOffTimestamp() {
		return this.offTimestamp;
	}

	public void setOffTimestamp(Timestamp offTimestamp) {
		this.offTimestamp = offTimestamp;
	}

	public String toString() {
        return getId().toString();
    }
	

}
