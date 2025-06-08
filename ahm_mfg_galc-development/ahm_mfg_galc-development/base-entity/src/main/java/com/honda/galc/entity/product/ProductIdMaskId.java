package com.honda.galc.entity.product;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>ProductIdMaskId</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> Id class for ProductIdMask </p>
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
 *
 * </TABLE>
 *   
 * @author Kamlesh Maharjan
 * March 05, 2016
 *
 */

@Embeddable
public class ProductIdMaskId implements Serializable {
	@Column(name="PROCESS_POINT_ID")
	private String processPointId;

	@Column(name="PRODUCT_ID_MASK")
	private String productIdMask;
	
	@Column(name="PRODUCT_TYPE")
	private String productType;


	private static final long serialVersionUID = 1L;

	public ProductIdMaskId() {
		super();
	}

	public String getProcessPointId() {
		return StringUtils.trim(this.processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getProductIdMask() {
		return StringUtils.trim(this.productIdMask);
	}

	public void setProductIdMask(String productIdMask) {
		this.productIdMask = productIdMask;
	}

	public String getProductType() {
		return StringUtils.trim(productType);
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof ProductIdMaskId)) {
			return false;
		}
		ProductIdMaskId other = (ProductIdMaskId) o;
		return this.getProcessPointId().equals(other.getProcessPointId())
			&& this.getProductIdMask().equals(other.getProductIdMask())
			&& this.getProductType().equals(other.getProductType());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.processPointId.hashCode();
		hash = hash * prime + this.productIdMask.hashCode();
		hash = hash * prime + this.productType.hashCode();
		return hash;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(processPointId).append(",");
		builder.append(productIdMask);
		builder.append(productType);
		return builder.toString();
	}
	
}
