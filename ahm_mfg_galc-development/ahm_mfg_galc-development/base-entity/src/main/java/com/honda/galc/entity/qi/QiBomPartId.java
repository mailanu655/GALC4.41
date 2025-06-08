package com.honda.galc.entity.qi;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>QiBomPartId Class description</h3>
 * <p>
 * QiBomPartId contains the getter and setter of the Bom Part composite key properties and maps this class with database and these columns .
 * </p>
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
 *        MAY 06, 2016
 * 
 */
@Embeddable
public class QiBomPartId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "PRODUCT_KIND", nullable=false, length=18)
	private String productKind;

	@Column(name = "DC_PART_NO", nullable=false, length=18)
	private String dcPartNo;
	
	@Column(name = "MODEL_CODE", nullable=false, length=4)
	private String modelCode;
	
	public QiBomPartId() {}
	
    public QiBomPartId(String productKind, String dcPartNo, String modelCode) {
      this.setProductKind(productKind);
      this.setDcPartNo(dcPartNo);
      this.setModelCode(modelCode);
    }
    
	public String getProductKind() {
		return StringUtils.trimToEmpty(productKind);
	}

	public void setProductKind(String productKind) {
		this.productKind = productKind;
	}
    
	public String getDcPartNo() {
		return StringUtils.trimToEmpty(dcPartNo);
	}

	public void setDcPartNo(String dcPartNo) {
		this.dcPartNo = dcPartNo;
	}

	public String getModelCode() {
		return StringUtils.trimToEmpty(modelCode);
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QiBomPartId other = (QiBomPartId) obj;
		if (productKind == null) {
			if (other.productKind != null)
				return false;
		} else if (!productKind.equals(other.productKind))
			return false;
		if (dcPartNo == null) {
			if (other.dcPartNo != null)
				return false;
		} else if (!dcPartNo.equals(other.dcPartNo))
			return false;
		if (modelCode == null) {
			if (other.modelCode != null)
				return false;
		} else if (!modelCode.equals(other.modelCode))
			return false;
		return true;
	}

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((productKind == null) ? 0 : productKind.hashCode());
		result = prime
				* result
				+ ((dcPartNo == null) ? 0 : dcPartNo.hashCode());
		result = prime * result
				+ ((modelCode == null) ? 0 : modelCode.hashCode());
		return result;
    }
}
