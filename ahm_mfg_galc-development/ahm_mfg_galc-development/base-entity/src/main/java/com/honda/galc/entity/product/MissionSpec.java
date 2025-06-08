package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

/**
 * <h3>Class description</h3>
 * Mission Spec Class.
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
 * <TD>Initial Release</TD>
 * </TR>
 */
@Entity
@Table(name = "MISSION_SPEC_CODE_TBX")
public class MissionSpec extends ProductSpec {
	private static final long serialVersionUID = 2254507440005421278L;

	@Id
	@Column(name = "PRODUCT_SPEC_CODE")
	private String productSpecCode;

    @Column(name = "PLANT_CODE")
    private String plantCode;

    @Column(name = "MISSION_NO_PREFIX")
    private String missionNoPrefix;
    
    @Column(name = "STAMP_SERIAL_DESC")
    private String stampSerialDesc;
    
    @Column(name = "BOUNDARY_MARK")
    private String boundaryMark;
    
    public MissionSpec() {
    	super();
    }
    
	@Override
    public String getProductSpecCode() {
    	return StringUtils.trim(this.productSpecCode);
    }
    
	@Override
	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

    
    public String getPlantCode() {
        return StringUtils.trim(this.plantCode);
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    public String getMissionNoPrefix() {
        return StringUtils.trim(this.missionNoPrefix);
    }

    public void setMissionNoPrefix(String missionNoPrefix) {
        this.missionNoPrefix = missionNoPrefix;
    }
    
	@Override
	public int getProductNoPrefixLength() {
		return missionNoPrefix == null ? 0 : getMissionNoPrefix().length();
	}

	public Object getId() {
		return getProductSpecCode();
	}

	@Override
	public String getExtColorCode() {
		return ProductSpec.WILDCARD;
	}

	@Override
	public String getIntColorCode() {
		return ProductSpec.WILDCARD;
	}

	public void setStampSerialDesc(String stampSerialDesc) {
		this.stampSerialDesc = stampSerialDesc;
	}

	public String getStampSerialDesc() {
		return StringUtils.trim(stampSerialDesc);
	}

	public void setBoundaryMark(String boundaryMark) {
		this.boundaryMark = boundaryMark;
	}

	public String getBoundaryMark() {
		return StringUtils.trim(boundaryMark);
	}

	@Override
	public String toString() {
		return toString(getProductSpecCode());
	}

}
