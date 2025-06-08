package com.honda.galc.entity.oif;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.device.Tag;
import com.honda.galc.entity.AuditEntry;
@Embeddable
public class InvoiceDetailId implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	@Column(name = "PRODUCT_ID")
    @Tag(name="PRODUCT_ID", alt="FRAME_NO")
	private String productId;

	public String getProductId()  {
		return productId;
	}

	public void setProductId(String newProductId)  {
		productId=newProductId;
	}
	
	@Column(name = "INVOICE_NO")
	private Long invoiceNo;

	public long getInvoiceNo()  {
		return invoiceNo;
	}

	public void setInvoiceNo(long factoryInvoiceNo)  {
		invoiceNo=factoryInvoiceNo;
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (invoiceNo ^ (invoiceNo >>> 32));
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InvoiceDetailId other = (InvoiceDetailId) obj;
		if (invoiceNo != other.invoiceNo)
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InvoiceDetailId [productId=" + productId + ", invoiceNo=" + invoiceNo + "]";
	}


}
