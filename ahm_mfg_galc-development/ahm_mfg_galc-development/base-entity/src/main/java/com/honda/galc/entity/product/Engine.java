package com.honda.galc.entity.product;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.PrintAttribute;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductNumberDef;

/**
 * 
 * <h3>Engine Class description</h3>
 * <p> Engine description </p>
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
 * May 9, 2011
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name = "GAL131TBX")
public class Engine extends Product {

    private String vin;
    
    @Id
	@Column(name = "PRODUCT_ID")
	private String productId;
	
    @Column(name = "ENGINE_FIRING_FLAG")
    private short engineFiringFlag;

    @Column(name = "MISSION_STATUS")
    private int missionStatus;

    @Column(name = "FIRING_TYPE")
    private int firingType;

    @Column(name = "REPAIR_FLAG")
    private short repairFlag;

    @Column(name = "DEFECT_STATUS")
    private Integer defectStatus;
    
    @Column(name = "MISSION_SERIAL_NO")
	private String missionSerialNo;
    
    @Column(name = "ACTUAL_MISSION_TYPE")
    private String actualMissionType;
    
    @Column(name = "PLANT_CODE")
    private String plantCode;
    
    private static final long serialVersionUID = 1L;

    /**
     * the beginning cure time, it can be find out in table of installed part.
     */
    @Transient
	protected Timestamp cureTimeBegin;
    
    /**
     * cure wait timer, it is one of the properties of process point. 
     * the unit of timer is minutes.
     */
    @Transient
	protected String cureWaitTimer;
    
    /**
     * the end cure time, cureTimeEnd = cureTimeBegin + cureWaitTimer.
     */
    @Transient
	protected String cureTimeEnd;
    
    public Engine() {
        super();
    }
    
	public Engine(String productId) {
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
	
    public String getVin() {
    	return StringUtils.trim(this.vin);
    }
    
	public void setVin(String vin) {
        this.vin = vin;
    }

    public short getEngineFiringFlag() {
        return this.engineFiringFlag;
    }

    public void setEngineFiringFlag(short engineFiringFlag) {
        this.engineFiringFlag = engineFiringFlag;
    }

    public int getMissionStatus() {
        return this.missionStatus;
    }

    public void setMissionStatus(int missionStatus) {
		this.missionStatus = missionStatus;
	}

	public void setMissionStatus(boolean missionStatus) {
		setMissionStatus(missionStatus ? 1 : 0);
	}    

    public int getFiringType() {
        return this.firingType;
    }

    public void setFiringType(int firingType) {
        this.firingType = firingType;
    }

    public short getRepairFlag() {
        return this.repairFlag;
    }

    public void setRepairFlag(short repairFlag) {
        this.repairFlag = repairFlag;
    }

    public Integer getDefectStatusValue() {
        return this.defectStatus;
    }

    public void setDefectStatusValue(Integer defectStatus) {
        this.defectStatus = defectStatus;
    }
    
    public String getMissionSerialNo() {
        return StringUtils.trim(this.missionSerialNo);
    }

    public void setMissionSerialNo(String missionSerialNo) {
        this.missionSerialNo = missionSerialNo;
    }

    @PrintAttribute
    public String getActualMissionType() {
        return StringUtils.trim(this.actualMissionType);
    }

    public void setActualMissionType(String actualMissionType) {
        this.actualMissionType = actualMissionType;
    }
    
    @Override
	public ProductType getProductType() {
		
		return ProductType.ENGINE;
		
	}
    
 	@Override
	public ProductNumberDef getProductNumberDef() {
		return ProductNumberDef.EIN;
	}

	@Override
	public String getOwnerProductId() {
		return StringUtils.trim(this.vin);
	}

	public Timestamp getCureTimeBegin() {
		return cureTimeBegin;
	}

	public void setCureTimeBegin(Timestamp cureTimeBegin) {
		this.cureTimeBegin = cureTimeBegin;
	}

	public String getCureWaitTimer() {
		return cureWaitTimer;
	}

	public void setCureWaitTimer(String cureWaitTimer) {
		this.cureWaitTimer = cureWaitTimer;
	}

	public String getCureTimeEnd() {
		return cureTimeEnd;
	}

	public void setCureTimeEnd(String cureTimeEnd) {
		this.cureTimeEnd = cureTimeEnd;
	}

	public String getPlantCode() {
		return StringUtils.trim(this.plantCode);
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}
 
	
}
