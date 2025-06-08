package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * The Class SmartEyeLabel.
 * SR# TASK0014476
 * Created Entity class for updating the VIN to blank in GAL228TBX
 * @author Sachin Kudikala
 * @since July 21, 2014
 */
@Entity
@Table(name = "GAL228TBX")
public class SmartEyeLabel extends AuditEntry {
	
	@Id
	@Column(name = "SMARTEYE_LABEL_NUMBER")
    private String id;
	
	@Column(name = "VIN")
    private String productId;


	public String getId() {
		return StringUtils.trim(id);
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductId() {
		return StringUtils.trim(productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	
	@Override
	public String toString() {
		return toString(getId(), getProductId());
	}
	
}
