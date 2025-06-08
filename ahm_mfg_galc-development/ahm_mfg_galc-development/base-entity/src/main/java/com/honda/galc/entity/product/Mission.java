package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.PrintAttribute;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;

/**
 * <h3>Class description</h3>
 * Mission Class.
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Sep. 2, 2014</TD>
 * <TD>1.0</TD>
 * <TD>GY 20140902</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */
@Entity
@Table(name = "MISSION_TBX")
public class Mission extends Product {
	private static final long serialVersionUID = 2519866231221797892L;

    @Id
	@Column(name = "PRODUCT_ID")
	private String productId;

    @Column(name = "REPAIR_FLAG")
    private short repairFlag;

    @Column(name = "DEFECT_STATUS")
    private Integer defectStatus;
    
	
	
    public Mission() {
        super();
    }
    
	public Mission(String productId) {
		this.productId = productId;
	}

	@PrintAttribute
	@Override
	public String getProductId() {
		return StringUtils.trim(productId);
	}

	@Override
	public void setProductId(String productId) {
		this.productId = productId;
	}

    public short getRepairFlag() {
        return repairFlag;
    }

    public void setRepairFlag(short repairFlag) {
        this.repairFlag = repairFlag;
    }

    public Integer getDefectStatusValue() {
        return defectStatus;
    }

    public void setDefectStatusValue(Integer defectStatusValue) {
        this.defectStatus = defectStatusValue;
    }


	@Override
	public String getOwnerProductId() {
		return null;
	}

	@Override
	public ProductNumberDef getProductNumberDef() {
		return ProductNumberDef.MIN;
	}

	@Override
	public ProductType getProductType() {
		return ProductType.MISSION;
	}

}
