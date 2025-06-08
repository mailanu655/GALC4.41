package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.enumtype.DefectStatus;


/**
 * 
 * <h3>Head Class description</h3>
 * <p> Head description </p>
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
 * Feb 28, 2011
 *
 *
 */

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name="HEAD_TBX")
public class Head extends DieCast {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "HEAD_ID")
    private String headId;
	
	@Column(name = "ENGINE_FIRING_FLAG")
    private int engineFiringFlagValue;

	
	public Head() {
		super();
	}

	public Head(String headId) {
		super();
		this.headId = headId;
		setDcSerialNumber(headId);
	}

	public String getHeadId() {
		return headId;
	}

	public void setHeadId(String headId) {
		this.headId = headId;
	}

	public int getEngineFiringFlagValue() {
		return engineFiringFlagValue;
	}

	public void setEngineFiringFlagValue(int engineFiringFlagValue) {
		this.engineFiringFlagValue = engineFiringFlagValue;
	}

	// === get/set === //
	public boolean getEngineFiringFlag() {
		return engineFiringFlagValue == 1;
	}

	public void setEngineFiringFlag(boolean engineFiringFlag) {
		this.engineFiringFlagValue = engineFiringFlag ? 1: 0;
	}

	@Override
	public String getProductId() {
		return getHeadId();
	}

	public Object getId() {
		return getHeadId();
	}

	@Override
	public ProductType getProductType() {
		
		return ProductType.HEAD;
		
	}

	@Override
	public ProductNumberDef getProductNumberDef() {
		return ProductNumberDef.DCH;
	}

	public boolean isDefect() {
		return getDefectStatus() != null && getDefectStatus() != DefectStatus.REPAIRED;
	}

}
