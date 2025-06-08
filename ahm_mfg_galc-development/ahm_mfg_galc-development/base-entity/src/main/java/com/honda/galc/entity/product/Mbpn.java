package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;


/**
 * * *
 * 
 * @version 1
 * @author Gangadhararao Gadde,Subu Kathiresan
 * @since Nov 01, 2012
 * 
 * */

@Entity
@Table(name = "MBPN_TBX")
public class Mbpn extends BaseProductSpec {

	@Id
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	@Column(name = "PRODUCT_SPEC_CODE")
	private String productSpecCode;

	@Column(name = "MBPN")
	@Auditable(isPartOfPrimaryKey = false, sequence = 2)
	private String mbpn;

	@Column(name = "HES_COLOR")
	@Auditable(isPartOfPrimaryKey = false, sequence = 3)
	private String hesColor;

	@Column(name = "MAIN_NO")
	@Auditable(isPartOfPrimaryKey = false, sequence = 4)
	private String mainNo;

	@Column(name = "CLASS_NO")
	@Auditable(isPartOfPrimaryKey = false, sequence = 5)
	private String classNo;
	
	@Column(name = "PROTOTYPE_CODE")
	@Auditable(isPartOfPrimaryKey = false, sequence = 6)
	private String prototypeCode;

	@Column(name = "TYPE_NO")
	@Auditable(isPartOfPrimaryKey = false, sequence = 7)
	private String typeNo;

	@Column(name = "SUPPLEMENTARY_NO")
	@Auditable(isPartOfPrimaryKey = false, sequence = 8)
	private String supplementaryNo;

	@Column(name = "TARGET_NO")
	@Auditable(isPartOfPrimaryKey = false, sequence = 9)
	private String targetNo;
	
	@Column(name = "DESCRIPTION")
	@Auditable(isPartOfPrimaryKey = false, sequence = 10)
	private String description;

	@Column(name = "MASK_ID")
	@Auditable(isPartOfPrimaryKey = false, sequence = 11)
	private String maskId;

	@Column(name = "DISABLED")
	@Auditable(isPartOfPrimaryKey = false, sequence = 12)
	private int disabled;
	
	private static final long serialVersionUID = 1L;

	public Mbpn() {
		super();

	}

	public Mbpn(String productSpecCode, String mbpn, String hesColor,
			String mainNo, String classNo, String prototypeCode, String typeNo,
			String supplementaryNo, String targetNo, int disabled) {
		super();
		this.productSpecCode = productSpecCode;
		this.mbpn = mbpn;
		this.hesColor = hesColor;
		this.mainNo = mainNo;
		this.classNo = classNo;
		this.prototypeCode = prototypeCode;
		this.typeNo = typeNo;
		this.supplementaryNo = supplementaryNo;
		this.targetNo = targetNo;
		this.disabled = disabled;
	}
	
	
	@OneToOne(targetEntity = MbpnProductType.class,fetch = FetchType.EAGER)
    @JoinColumn(name="MAIN_NO",referencedColumnName="MAIN_NO",updatable=false,insertable=false)
    private MbpnProductType mbpnProductType;

	public MbpnProductType getMbpnProductType() {
		return mbpnProductType;
	}

	public void setMbpnProductType(MbpnProductType mbpnProductType) {
		this.mbpnProductType = mbpnProductType;
	}

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public String getMbpn() {
		return mbpn;
	}

	public void setMbpn(String mbpn) {
		this.mbpn = mbpn;
	}

	public String getHesColor() {
		return StringUtils.trimToEmpty(this.hesColor);
	}

	public void setHesColor(String hesColor) {
		this.hesColor = hesColor;
	}

	public String getMainNo() {
		return StringUtils.trimToEmpty(this.mainNo);
	}

	public void setMainNo(String mainNo) {
		this.mainNo = mainNo;
	}
	
	public String getClassNo() {
		return StringUtils.trimToEmpty(this.classNo);
	}

	public void setClassNo(String classNo) {
		this.classNo = classNo;
	}

	public String getPrototypeCode() {
		return StringUtils.trimToEmpty(this.prototypeCode);
	}

	public void setPrototypeCode(String prototypeCode) {
		this.prototypeCode = prototypeCode;
	}

	public String getTypeNo() {
		return StringUtils.trimToEmpty(this.typeNo);
	}

	public void setTypeNo(String typeNo) {
		this.typeNo = typeNo;
	}

	public String getSupplementaryNo() {
		return StringUtils.trimToEmpty(this.supplementaryNo);
	}

	public void setSupplementaryNo(String supplementaryNo) {
		this.supplementaryNo = supplementaryNo;
	}

	public String getTargetNo() {
		return StringUtils.trimToEmpty(this.targetNo);
	}

	public void setTargetNo(String targetNo) {
		this.targetNo = targetNo;
	}
	
	public String getMbpnFromSpecCode() {
		return this.productSpecCode.substring(0,17);
	}
	public String getHesColorFromSpecCode() {
		return this.productSpecCode.substring(17);
	}
	
	public String getDescription() {
		return StringUtils.trimToEmpty(this.description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMaskId() {
		return StringUtils.trimToEmpty(this.maskId);
	}

	public void setMaskId(String maskId) {
		this.maskId = maskId;
	}

	public int getDisabled() {
		return this.disabled;
	}

	public void setDisabled(int disabled) {
		this.disabled = disabled;
	}

	public boolean isDisabled() {
		return this.disabled > 0;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled ? 1 : 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classNo == null) ? 0 : classNo.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + disabled;
		result = prime * result + ((hesColor == null) ? 0 : hesColor.hashCode());
		result = prime * result + ((mainNo == null) ? 0 : mainNo.hashCode());
		result = prime * result + ((maskId == null) ? 0 : maskId.hashCode());
		result = prime * result + ((mbpn == null) ? 0 : mbpn.hashCode());
		result = prime * result + ((mbpnProductType == null) ? 0 : mbpnProductType.hashCode());
		result = prime * result + ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result + ((prototypeCode == null) ? 0 : prototypeCode.hashCode());
		result = prime * result + ((supplementaryNo == null) ? 0 : supplementaryNo.hashCode());
		result = prime * result + ((targetNo == null) ? 0 : targetNo.hashCode());
		result = prime * result + ((typeNo == null) ? 0 : typeNo.hashCode());
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
		Mbpn other = (Mbpn) obj;
		if (classNo == null) {
			if (other.classNo != null)
				return false;
		} else if (!classNo.equals(other.classNo))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (disabled != other.disabled)
			return false;
		if (hesColor == null) {
			if (other.hesColor != null)
				return false;
		} else if (!hesColor.equals(other.hesColor))
			return false;
		if (mainNo == null) {
			if (other.mainNo != null)
				return false;
		} else if (!mainNo.equals(other.mainNo))
			return false;
		if (maskId == null) {
			if (other.maskId != null)
				return false;
		} else if (!maskId.equals(other.maskId))
			return false;
		if (mbpn == null) {
			if (other.mbpn != null)
				return false;
		} else if (!mbpn.equals(other.mbpn))
			return false;
		if (mbpnProductType == null) {
			if (other.mbpnProductType != null)
				return false;
		} else if (!mbpnProductType.equals(other.mbpnProductType))
			return false;
		if (productSpecCode == null) {
			if (other.productSpecCode != null)
				return false;
		} else if (!productSpecCode.equals(other.productSpecCode))
			return false;
		if (prototypeCode == null) {
			if (other.prototypeCode != null)
				return false;
		} else if (!prototypeCode.equals(other.prototypeCode))
			return false;
		if (supplementaryNo == null) {
			if (other.supplementaryNo != null)
				return false;
		} else if (!supplementaryNo.equals(other.supplementaryNo))
			return false;
		if (targetNo == null) {
			if (other.targetNo != null)
				return false;
		} else if (!targetNo.equals(other.targetNo))
			return false;
		if (typeNo == null) {
			if (other.typeNo != null)
				return false;
		} else if (!typeNo.equals(other.typeNo))
			return false;
		return true;
	}

	public String getId() {
		return getProductSpecCode();
	}

}
