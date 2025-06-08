package com.honda.galc.entity.qics;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.jdbc.ElementJoinColumn;
import org.apache.openjpa.persistence.jdbc.ElementJoinColumns;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>DefectDescription Class description</h3>
 * <p> DefectDescription description </p>
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
 * Mar 30, 2011
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name = "GAL322TBX")
public class DefectDescription extends AuditEntry {
    @EmbeddedId
    private DefectDescriptionId id;

    @Column(name = "IQS_CATEGORY_NAME")
    private String iqsCategoryName;

    @Column(name = "IQS_ITEM_NAME")
    private String iqsItemName;

    @Column(name = "REGRESSION_CODE")
    private String regressionCode;

    @Column(name = "LOCK_MODE")
    private short lockMode;

    @Column(name = "RESPONSIBLE_DEPT")
    private String responsibleDept;

    @Column(name = "RESPONSIBLE_ZONE")
    private String responsibleZone;

    @Column(name = "TWO_PART_DEFECT_FLAG")
    private short twoPartDefectFlag;

    @Column(name = "INVENTORY_REPAIR_TIME")
    private int inventoryRepairTime;

    @Column(name = "ONLINE_REPAIR_TIME")
    private int onlineRepairTime;

    @Column(name = "RESPONSIBLE_LINE")
    private String responsibleLine;

    @Column(name = "ENGINE_FIRING_FLAG")
    private short engineFiringFlag;

    private static final long serialVersionUID = 1L;

    @OneToMany(targetEntity = InspectionPartDescription.class,fetch = FetchType.EAGER,cascade={})
    @ElementJoinColumns({
    	@ElementJoinColumn(name="INSPECTION_PART_NAME", referencedColumnName="INSPECTION_PART_NAME",updatable = false,insertable=false),
    	@ElementJoinColumn(name="INSPECTION_PART_LOCATION_NAME", referencedColumnName="INSPECTION_PART_LOCATION_NAME",updatable = false,insertable=false),
    	@ElementJoinColumn(name="PART_GROUP_NAME", referencedColumnName="PART_GROUP_NAME",updatable = false,insertable=false)
    })
    @OrderBy(" descriptionId ASC")
    private List<InspectionPartDescription> inspectionPartDescriptions;
 
    @OneToMany(targetEntity = InspectionTwoPartDescription.class,fetch = FetchType.EAGER,cascade={})
    @ElementJoinColumns({
    	@ElementJoinColumn(name="INSPECTION_PART_NAME", referencedColumnName="INSPECTION_PART_NAME",updatable = false,insertable=false),
    	@ElementJoinColumn(name="INSPECTION_PART_LOCATION_NAME", referencedColumnName="INSPECTION_PART_LOCATION_NAME",updatable = false,insertable=false),
    	@ElementJoinColumn(name="PART_GROUP_NAME", referencedColumnName="PART_GROUP_NAME",updatable = false,insertable=false),
    	@ElementJoinColumn(name="TWO_PART_PAIR_PART", referencedColumnName="TWO_PART_PAIR_PART",updatable = false,insertable=false),
    	@ElementJoinColumn(name="TWO_PART_PAIR_LOCATION", referencedColumnName="TWO_PART_PAIR_LOCATION",updatable = false,insertable=false)
    })
     private List<InspectionTwoPartDescription> inspectionTwoPartDescriptions;
    
    public DefectDescription() {
        super();
        id = new DefectDescriptionId();
    }

    public DefectDescriptionId getId() {
        return this.id;
    }

    public void setId(DefectDescriptionId id) {
        this.id = id;
    }
    
    public String getDefectTypeName() {
    	return id == null ? null: id.getDefectTypeName();
    }
    
    public String getSecondaryPartName() {
       	return id == null ? null: id.getSecondaryPartName();
    }

    public String getIqsCategoryName() {
        return StringUtils.trim(this.iqsCategoryName);
    }

    public void setIqsCategoryName(String iqsCategoryName) {
        this.iqsCategoryName = iqsCategoryName;
    }

    public String getIqsItemName() {
        return StringUtils.trim(this.iqsItemName);
    }

    public void setIqsItemName(String iqsItemName) {
        this.iqsItemName = iqsItemName;
    }

    public String getRegressionCode() {
        return StringUtils.trim(this.regressionCode);
    }

    public void setRegressionCode(String regressionCode) {
        this.regressionCode = regressionCode;
    }

    public short getLockMode() {
        return this.lockMode;
    }

    public void setLockMode(short lockMode) {
        this.lockMode = lockMode;
    }

    public String getResponsibleDept() {
        return StringUtils.trim(this.responsibleDept);
    }

    public void setResponsibleDept(String responsibleDept) {
        this.responsibleDept = responsibleDept;
    }

    public String getResponsibleZone() {
        return StringUtils.trim(this.responsibleZone);
    }

    public void setResponsibleZone(String responsibleZone) {
        this.responsibleZone = responsibleZone;
    }

    public short getTwoPartDefectFlagValue() {
        return this.twoPartDefectFlag;
    }

    public void setTwoPartDefectFlagValue(short twoPartDefectFlag) {
        this.twoPartDefectFlag = twoPartDefectFlag;
    }
    
    public boolean getTwoPartDefectFlag() {
        return this.twoPartDefectFlag == 1;
    }

    public void setTwoPartDefectFlag(boolean twoPartDefectFlag) {
        this.twoPartDefectFlag = (short)(twoPartDefectFlag ? 1: 0);
    }
    public int getInventoryRepairTime() {
        return this.inventoryRepairTime;
    }

    public void setInventoryRepairTime(int inventoryRepairTime) {
        this.inventoryRepairTime = inventoryRepairTime;
    }

    public int getOnlineRepairTime() {
        return this.onlineRepairTime;
    }

    public void setOnlineRepairTime(int onlineRepairTime) {
        this.onlineRepairTime = onlineRepairTime;
    }

    public String getResponsibleLine() {
        return StringUtils.trim(this.responsibleLine);
    }

    public void setResponsibleLine(String responsibleLine) {
        this.responsibleLine = responsibleLine;
    }

    public short getEngineFiringFlagValue() {
        return this.engineFiringFlag;
    }

    public void setEngineFiringFlagValue(short engineFiringFlagValue) {
        this.engineFiringFlag = engineFiringFlagValue;
    }
    
    public boolean getEngineFiringFlag() {
        return this.engineFiringFlag == 1;
    }

    public void setEngineFiringFlag(boolean engineFiringFlag) {
        this.engineFiringFlag = (short)(engineFiringFlag ? 1 : 0);
    }
    
    
    public String getPartGroupName() {
    	
    	return id == null ? null : id.getPartGroupName();
    	
    }
    
    public String getInspectionPartName() {
    	return id == null ? null : id.getInspectionPartName();
    }
    
    public InspectionPartDescription getInspectionPartDescription() {
    	if(inspectionPartDescriptions == null || inspectionPartDescriptions.isEmpty() ) return null;
    	else return inspectionPartDescriptions.get(0);
    }
    
    public InspectionTwoPartDescription getInspectionTwoPartDescription() {
    	if(inspectionTwoPartDescriptions == null || inspectionTwoPartDescriptions.isEmpty()) return null;
    	else return inspectionTwoPartDescriptions.get(0);
    }

	@Override
	public String toString() {
		return toString(getInspectionPartName(), getDefectTypeName(),
				getPartGroupName());
	}
	
	public String getInspectionPartLocationName() {
		return id == null ? null : id.getInspectionPartLocationName();
    }
	
	 public String getTwoPartPairPart() {
		 return id == null ? null : id.getTwoPartPairPart();
	    }
	 
	 public String getTwoPartPairLocation() {
		 return id == null ? null : id.getTwoPartPairLocation();
	    }


}
