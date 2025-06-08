package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.CreateUserAuditEntry;
/**
 * 
 * <h3>QiProductKind Class description</h3>
 * <p> QiProductKind description </p>
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
 * @author LnTInfotech<br>
 *        Oct 06, 2016
 * 
 */
@Entity
@Table(name = "QI_PRODUCT_KIND_TBX")
public class QiProductKind extends CreateUserAuditEntry{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "PRODUCT_KIND")
	private String productKind;
	@Column(name = "PRODUCT_KIND_DESCRIPTION")
	private String productKindDescription;
	@Column(name = "ACTIVE")
	private boolean active;
	
	public QiProductKind() {
		super();
	}

	/**
	 * @return the productKind
	 */
	public String getProductKind() {
		return StringUtils.trimToEmpty(productKind);
	}

	/**
	 * @param productKind the productKind to set
	 */
	public void setProductKind(String productKind) {
		this.productKind = productKind;
	}

	/**
	 * @return the productKindDescription
	 */
	public String getProductKindDescription() {
		return StringUtils.trimToEmpty(productKindDescription);
	}

	/**
	 * @param productKindDescription the productKindDescription to set
	 */
	public void setProductKindDescription(String productKindDescription) {
		this.productKindDescription = productKindDescription;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	public Object getId() {
		return getProductKind();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + ((productKind == null) ? 0 : productKind.hashCode());
		result = prime * result + ((productKindDescription == null) ? 0 : productKindDescription.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		QiProductKind other = (QiProductKind) obj;
		if (active != other.active)
			return false;
		if (productKind == null) {
			if (other.productKind != null)
				return false;
		} else if (!productKind.equals(other.productKind))
			return false;
		if (productKindDescription == null) {
			if (other.productKindDescription != null)
				return false;
		} else if (!productKindDescription.equals(other.productKindDescription))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QiProductKind [productKind=" + productKind + ", productKindDescription=" + productKindDescription
				+ ", active=" + active + "]";
	}
	
}
