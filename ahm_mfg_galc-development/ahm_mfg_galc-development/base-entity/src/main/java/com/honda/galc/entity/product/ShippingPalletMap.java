package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AbstractEntity;

/**
 * 
 * 
 * <h3>ShippingPalletMap Class description</h3>
 * <p> ShippingPalletMap description </p>
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
 * Oct 3, 2014
 *
 *
 */
@Entity
@Table(name="PALLET_MAP_TBX")
public class ShippingPalletMap extends AbstractEntity {
	@Id
	@Column(name="MODEL_CODE")
	private String modelCode;

	@Column(name="PALLET_TYPE")
	private String palletType;

	private static final long serialVersionUID = 1L;

	public ShippingPalletMap() {
		super();
	}

	public String getId() {
		return getModelCode();
	}

	public String getModelCode() {
		return StringUtils.trim(modelCode);
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getPalletType() {
		return StringUtils.trim(palletType);
	}

	public void setPalletType(String palletType) {
		this.palletType = palletType;
	}

	public String toString() {
		return toString(getModelCode(),getPalletType());
	}
	

}
