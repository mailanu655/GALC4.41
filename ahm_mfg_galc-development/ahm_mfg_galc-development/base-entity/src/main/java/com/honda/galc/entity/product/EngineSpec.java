package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.PrintAttribute;

/**
 * 
 * <h3>EngineSpec Class description</h3>
 * <p> EngineSpec description </p>
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
@Table(name = "GAL133TBX")
public class EngineSpec extends ProductSpec {
    
	@Id
	@Column(name = "PRODUCT_SPEC_CODE")
	private String productSpecCode;
	
	@Column(name = "MISSION_PLANT_CODE")
    private String missionPlantCode;

    @Column(name = "MISSION_MODEL_CODE")
    private String missionModelCode;

    @Column(name = "MISSION_PROTOTYPE_CODE")
    private String missionPrototypeCode;

    @Column(name = "MISSION_MODEL_TYPE_CODE")
    private String missionModelTypeCode;

    @Column(name = "ENGINE_NO_PREFIX")
    private String engineNoPrefix;

    @Column(name = "TRANSMISSION")
    private String transmission;

    @Column(name = "TRANSMISSION_DESCRIPTION")
    private String transmissionDescription;

    @Column(name = "GEAR_SHIFT")
    private String gearShift;

    @Column(name = "GEAR_SHIFT_DESCRIPTION")
    private String gearShiftDescription;

    @Column(name = "DISPLACEMENT")
    private String displacement;

    @Column(name = "DISPLACEMENT_COMMENT")
    private String displacementComment;

    @Column(name = "ENGINE_PROTOTYPE_CODE")
    private String enginePrototypeCode;

    @Column(name = "PLANT_CODE")
    private String plantCode;

    @Column(name = "MISSION_MODEL_YEAR_CODE")
    private String missionModelYearCode;

    private static final long serialVersionUID = 1L;

    public EngineSpec() {
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
	
    public String getMissionPlantCode() {
        return StringUtils.trim(this.missionPlantCode);
    }

    public void setMissionPlantCode(String missionPlantCode) {
        this.missionPlantCode = missionPlantCode;
    }
    
    public String getId() {
		return getProductSpecCode();
	}

    @PrintAttribute
    public String getMissionModelCode() {
        return StringUtils.trim(this.missionModelCode);
    }

    public void setMissionModelCode(String missionModelCode) {
        this.missionModelCode = missionModelCode;
    }

    public String getMissionPrototypeCode() {
        return StringUtils.trim(this.missionPrototypeCode);
    }

    public void setMissionPrototypeCode(String missionPrototypeCode) {
        this.missionPrototypeCode = missionPrototypeCode;
    }
    
    @PrintAttribute
    public String getMissionModelTypeCode() {
        return StringUtils.trim(this.missionModelTypeCode);
    }

    public void setMissionModelTypeCode(String missionModelTypeCode) {
        this.missionModelTypeCode = missionModelTypeCode;
    }

    public String getEngineNoPrefix() {
        return StringUtils.trim(this.engineNoPrefix);
    }

    public void setEngineNoPrefix(String engineNoPrefix) {
        this.engineNoPrefix = engineNoPrefix;
    }

    @PrintAttribute
    public String getTransmission() {
        return StringUtils.trim(this.transmission);
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getTransmissionDescription() {
        return StringUtils.trim(this.transmissionDescription);
    }

    public void setTransmissionDescription(String transmissionDescription) {
        this.transmissionDescription = transmissionDescription;
    }

    public String getGearShift() {
        return StringUtils.trim(this.gearShift);
    }

    public void setGearShift(String gearShift) {
        this.gearShift = gearShift;
    }

    public String getGearShiftDescription() {
        return StringUtils.trim(this.gearShiftDescription);
    }

    public void setGearShiftDescription(String gearShiftDescription) {
        this.gearShiftDescription = gearShiftDescription;
    }

    public String getDisplacement() {
        return StringUtils.trim(this.displacement);
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    public String getDisplacementComment() {
        return StringUtils.trim(this.displacementComment);
    }

    public void setDisplacementComment(String displacementComment) {
        this.displacementComment = displacementComment;
    }

    public String getEnginePrototypeCode() {
        return StringUtils.trim(this.enginePrototypeCode);
    }

    public void setEnginePrototypeCode(String enginePrototypeCode) {
        this.enginePrototypeCode = enginePrototypeCode;
    }

    public String getPlantCode() {
        return StringUtils.trim(this.plantCode);
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    public String getMissionModelYearCode() {
        return StringUtils.trim(this.missionModelYearCode);
    }

    public void setMissionModelYearCode(String missionModelYearCode) {
        this.missionModelYearCode = missionModelYearCode;
    }
    
	public int getProductNoPrefixLength() {
		return engineNoPrefix == null ? 0 : engineNoPrefix.length();
	}

	@Override
	public String getExtColorCode() {
		return ProductSpec.WILDCARD;
	}

	@Override
	public String getIntColorCode() {
		return ProductSpec.WILDCARD;
	}

	@Override
	public String toString() {
		return toString(getProductSpecCode());
	}
	public String getActualMissionType(){
		//Actual Mission type will be concatenation of mission model year code, model code and model type code
		return getMissionModelYearCode().trim()+getMissionModelCode().trim()+getMissionModelTypeCode().trim();
	}


}
