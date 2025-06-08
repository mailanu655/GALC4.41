package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>IPPTagId Class description</h3>
 * <p> IPPTagId description </p>
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
 * @author Jeffray Huang<br>
 * Aug 22, 2011
 *
 *
 */
@Embeddable
public class IPPTagId implements Serializable {
	@Column(name="PRODUCT_ID")
	private String productId;

	@Column(name="IPP_TAG_NO")
	private String ippTagNo;

	@Column(name="DIVISION_ID")
	private String divisionId;

	private static final long serialVersionUID = 1L;

	public IPPTagId() {
		super();
	}
	
	

	public IPPTagId(String productId, String ippTagNo, String divisionId) {
		super();
		this.productId = productId;
		this.ippTagNo = ippTagNo;
		this.divisionId = divisionId;
	}



	public String getProductId() {
		return StringUtils.trim(this.productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getIppTagNo() {
		return StringUtils.trim(this.ippTagNo);
	}

	public void setIppTagNo(String ippTagNo) {
		this.ippTagNo = ippTagNo;
	}

	public String getDivisionId() {
		return StringUtils.trim(this.divisionId);
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof IPPTagId)) {
			return false;
		}
		IPPTagId other = (IPPTagId) o;
		return getProductId().equals(other.getProductId())
			&& getIppTagNo().equals(other.getIppTagNo())
			&& getDivisionId().equals(other.getDivisionId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.productId.hashCode();
		hash = hash * prime + this.ippTagNo.hashCode();
		hash = hash * prime + this.divisionId.hashCode();
		return hash;
	}

}
