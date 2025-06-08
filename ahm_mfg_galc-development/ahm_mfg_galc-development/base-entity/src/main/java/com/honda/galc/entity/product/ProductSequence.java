package com.honda.galc.entity.product;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.honda.galc.entity.AuditEntry;

@JsonRootName(value = "ProductSequence")
@Entity
@Table(name="PRODUCT_SEQUENCE_TBX")
public class ProductSequence extends AuditEntry {
	@EmbeddedId
	private ProductSequenceId id;

	@Column(name="REFERENCE_TIMESTAMP")
	private Timestamp referenceTimestamp;

	@Column(name="ASSOCIATE_NO")
	private String associateNo;

	@Column(name="SEQUENCE_NUMBER")
	private Integer sequenceNumber;

	@Column(name="SOURCE_SYSTEM_ID")
	private String sourceSystemId;

	@Column(name="PRODUCT_TYPE")
	private String productType;

	private static final long serialVersionUID = 1L;

	public ProductSequence() {
		super();
	}

	public ProductSequence(ProductSequenceId productSequenceId) {
		this.id = productSequenceId;
	}


	public ProductSequenceId getId() {
		return this.id;
	}

	public void setId(ProductSequenceId id) {
		this.id = id;
	}

	public Timestamp getReferenceTimestamp() {
		return this.referenceTimestamp;
	}

	public void setReferenceTimestamp(Timestamp referenceTimestamp) {
		this.referenceTimestamp = referenceTimestamp;
	}

	public String getAssociateNo() {
		return StringUtils.trim(this.associateNo);
	}

	public void setAssociateNo(String associateNo) {
		this.associateNo = StringUtils.trim(associateNo);
	}

	public Integer getSequenceNumber() {
		return this.sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getSourceSystemId() {
		return StringUtils.trim(this.sourceSystemId);
	}

	public void setSourceSystemId(String sourceSystemId) {
		this.sourceSystemId = StringUtils.trim(sourceSystemId);
	}

	public String getProductType() {
		return StringUtils.trim(this.productType);
	}

	public void setProductType(String productType) {
		this.productType = StringUtils.trim(productType);
	}

	public String toString() {
		return getId().toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductSequence other = (ProductSequence) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
