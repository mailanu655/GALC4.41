package com.honda.galc.entity.product;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.UserAuditEntry;
import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.enumtype.PartSerialNumberScanType;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name = "GAL246TBX")
public class LotControlRule extends UserAuditEntry {
    @EmbeddedId
    @Auditable(isPartOfPrimaryKey= true,sequence=1)
    private LotControlRuleId id;

    @Column(name = "SEQUENCE_NUMBER")
    @Auditable(isPartOfPrimaryKey= false,sequence=2)
    private int sequenceNumber;

    @Column(name = "EXPECTED_INSTALL_TIME")
    @Auditable(isPartOfPrimaryKey= false,sequence=3)
    private int expectedInstallTime;

    @Column(name = "VERIFICATION_FLAG")
    @Auditable(isPartOfPrimaryKey= false,sequence=4)
    private int verificationFlag;

    @Column(name = "SERIAL_NUMBER_SCAN_FLAG")
    @Auditable(isPartOfPrimaryKey= false,sequence=5)
    private int serialNumberScanFlag;
    
    @Column(name = "SUB_ID")
    @Auditable(isPartOfPrimaryKey= false,sequence=6)
    private String subId;

    @Column(name = "INSTRUCTION_CODE")
    @Auditable(isPartOfPrimaryKey= false,sequence=7)
    private String instructionCode;
    
	@Column(name="SERIAL_NUMBER_UNIQUE_FLAG")
	@Auditable(isPartOfPrimaryKey= false,sequence=8)
	private int serialNumberUniqueFlag;
	
	@Column(name="STRATEGY")
	@Auditable(isPartOfPrimaryKey= false,sequence=9)
	private String strategy;
	
	@Column(name="DEVICE_ID")
	@Auditable(isPartOfPrimaryKey= false,sequence=10)
	private String deviceId;
	
	@Column(name="GROUP_ID")
	@Auditable(isPartOfPrimaryKey= false,sequence=11)
	private String groupId;
	
	@Column(name="PART_CONFIRM_FLAG")
	@Auditable(isPartOfPrimaryKey= false,sequence=12)
	private int partConfirmFlag;
	
	@Column(name= "QI_DEFECT_FLAG")
	@Auditable(isPartOfPrimaryKey= false,sequence=13)
	private int qiDefectFlag;
	
	@OneToMany(targetEntity = PartByProductSpecCode.class,mappedBy = "lotControlRule",cascade = {CascadeType.MERGE,CascadeType.PERSIST},fetch = FetchType.EAGER)
    @OrderBy
    private List<PartByProductSpecCode> partByProductSpecs = new ArrayList<PartByProductSpecCode>();
	
	@OneToOne(targetEntity = PartName.class,fetch = FetchType.EAGER)
    @JoinColumn(name="PART_NAME",referencedColumnName="PART_NAME")
    private PartName partName;
	
	@Transient
	private List<PartSpec> parts;

    private static final long serialVersionUID = 1L;
    private static final String ASTERISK = "*";

    public LotControlRule() {
        super();
    }

    public LotControlRuleId getId() {
        return this.id;
    }

    public void setId(LotControlRuleId id) {
        this.id = id;
    }

    public int getSequenceNumber() {
        return this.sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getExpectedInstallTime() {
        return this.expectedInstallTime;
    }

    public void setExpectedInstallTime(int expectedInstallTime) {
        this.expectedInstallTime = expectedInstallTime;
    }

    public int getVerificationFlag() {
        return this.verificationFlag;
    }
    
 
    public void setVerificationFlag(int verificationFlag) {
        this.verificationFlag = verificationFlag;
    }
    
    public boolean isVerify() {
    	return getVerificationFlag() == 1;
    }
    
    public void setVerify(boolean verify) {
    	this.verificationFlag = verify ? 1 : 0; 
    }
    
    public int getSerialNumberScanFlag() {
        return this.serialNumberScanFlag;
    }

    public void setSerialNumberScanFlag(int serialNumberScanFlag) {
        this.serialNumberScanFlag = serialNumberScanFlag;
    }

    public String getSubId() {
		return StringUtils.trim(subId);
	}

	public void setSubId(String subId) {
		this.subId = subId;
	}

	public boolean isScan() {
    	return getSerialNumberScanFlag() == 1;
    }
	public PartSerialNumberScanType getSerialNumberScanType() {
		return PartSerialNumberScanType.getType(serialNumberScanFlag);
	}
    
    public void setScan(boolean scan) {
    	this.serialNumberScanFlag = scan ? 1 : 0; 
	}

    public String getInstructionCode() {
        return StringUtils.trim(this.instructionCode);
    }

    public void setInstructionCode(String instructionCode) {
        this.instructionCode = instructionCode;
    }

    public int getSerialNumberUniqueFlag() {
        return this.serialNumberUniqueFlag;
    }

    public void setSerialNumberUniqueFlag(int serialNumberUniqueFlag) {
        this.serialNumberUniqueFlag = serialNumberUniqueFlag;
    }

    public boolean isUnique() {
    	return getSerialNumberUniqueFlag() == 1;
    }
    
    public void setUnique(boolean scan) {
    	this.serialNumberUniqueFlag = scan ? 1 : 0; 
    }

	public String getDeviceId() {
		return StringUtils.trim(this.deviceId);
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

    public PartName getPartName() {
        return partName;
    }

    public void setPartName(PartName partName) {
        this.partName = partName;
    }
    
    public void setStrategy(String strategy) {
		this.strategy = strategy;
	}
    
    public String getStrategy() {
		return StringUtils.trim(strategy);
	}
    
	public List<PartByProductSpecCode> getPartByProductSpecs() {
		return partByProductSpecs;
	}

	public void setPartByProductSpecs(List<PartByProductSpecCode> partByProductSpecs) {
		this.partByProductSpecs = partByProductSpecs;
	}

    public String toString() {
    	StringBuilder builder = new StringBuilder();
    	builder.append("\"");
    	builder.append(id.toString()).append(",").append(instructionCode).append(",");
    	builder.append(deviceId).append(",");
    	builder.append(partName == null ? "null" : partName.toString()).append(",");
    	builder.append(verificationFlag).append(", ");
    	builder.append(serialNumberUniqueFlag).append(", ");
    	builder.append(getSerialNumberScanType()).append(", ");
    	builder.append(partConfirmFlag);
    	builder.append("\"");
    	return builder.toString();
    }
    
    public List<PartSpec> getParts() {
    	if(parts == null){
    		parts = new ArrayList<PartSpec>();
    		for(PartByProductSpecCode partCode :getPartByProductSpecs()){
    			if(partCode.getPartSpec() != null)
    				parts.add(partCode.getPartSpec());
    		}
    	}
		return parts;
	}

	public void setParts(List<PartSpec> parts) {
		this.parts = parts;
	}
	
	public String getPartNameString(){
		return getId().getPartName();
	}

	/**
     * detach - remove jpa state managers
     * this has to be called when send notifications
     * @return
     */
    public LotControlRule detach() {
    	LotControlRule newRule = new LotControlRule();
    	newRule.setId(id);
    	newRule.setDeviceId(deviceId);
    	newRule.setExpectedInstallTime(expectedInstallTime);
    	newRule.setInstructionCode(instructionCode);
    	newRule.setPartByProductSpecs(getDetachedPartByPproductSpecCodes());
    	newRule.setSequenceNumber(sequenceNumber);
    	newRule.setSerialNumberScanFlag(serialNumberScanFlag);
    	newRule.setSerialNumberUniqueFlag(serialNumberUniqueFlag);
    	newRule.setStrategy(strategy);
    	newRule.setVerificationFlag(verificationFlag);
    	newRule.setPartConfirmFlag(partConfirmFlag);
    	return newRule;
    }
    
    private List<PartByProductSpecCode> getDetachedPartByPproductSpecCodes() {
    	List<PartByProductSpecCode>  entities = new ArrayList<PartByProductSpecCode>();
    	for(PartByProductSpecCode entity : partByProductSpecs) {
    		entities.add(entity.detach());
    	}
    	return entities;
    }

	public void mapPartNameAndPartSpec() {
		List<PartSpec> selectedPartSpecs = new ArrayList<PartSpec>();
		if(partName == null || partName.getAllPartSpecs() == null) return;
		for(PartSpec partSpec : partName.getAllPartSpecs()){
			if(isSelectedPartSpec(partSpec))
				selectedPartSpecs.add(partSpec);
		}
	}

	private boolean isSelectedPartSpec(PartSpec partSpec) {
		for(PartByProductSpecCode partByProductSpecCode : partByProductSpecs)
			if(partSpec.getId().getPartName().equals(partByProductSpecCode.getId().getPartName()) &&
					partSpec.getId().getPartId().equals(partByProductSpecCode.getId().getPartId()))
				return true;
		
		return false;
	}
    
	public String getPartMasks() {
		StringBuilder sb = new StringBuilder();
		for(PartSpec spec : getParts()){
			if(sb.length() > 0) sb.append(",");
			sb.append(spec.getPartSerialNumberMask());
		}
		return sb.toString();
	}
	
	
	public boolean isNoScan() {
		return serialNumberScanFlag == PartSerialNumberScanType.NONE.ordinal();
	}
	
	public boolean isPartLotScan() {
		return serialNumberScanFlag == PartSerialNumberScanType.PART_LOT.ordinal();
	}

	public boolean isPartMaskScan() {
		return serialNumberScanFlag == PartSerialNumberScanType.PART_MASK.ordinal();
	}
	
	public boolean isProdLotScan() {
		return serialNumberScanFlag == PartSerialNumberScanType.PROD_LOT.ordinal();
	}

	public boolean isKdLotScan() {
		return serialNumberScanFlag == PartSerialNumberScanType.KD_LOT.ordinal();
	}

	public boolean isStatusOnly() {
		
		return PartSerialNumberScanType.STATUS_ONLY == getSerialNumberScanType();
	}

	
	public String getProductSpecCode(){
		return this.id.getProductSpecCode();
	}

	
	public boolean isDateScan() {
		return serialNumberScanFlag == PartSerialNumberScanType.DATE.ordinal();
	}



	public String getGroupId() {
		return StringUtils.trim(groupId);
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

/**
 * Check if 2 rules have same function and one MOTC is the parent of the other MTOC.
 * It is used to identify "duplicate" rules.	
 * @param rule
 * @return true if one rule is "duplicate" (not needed).
 */
	
	public boolean basicFunctionEquals(LotControlRule rule) {
		return getId().getProcessPointId().equals(rule.getId().getProcessPointId())
			&& getId().getPartName().equals(rule.getId().getPartName())
			&& (getExpectedInstallTime() == rule.getExpectedInstallTime())
			&& (getPartConfirmFlag() == rule.getPartConfirmFlag())
			&& (getVerificationFlag() == rule.getVerificationFlag())
			&& (getSerialNumberScanFlag() == rule.getSerialNumberScanFlag())
			&& StringUtils.equals(getSubId(), rule.getSubId())
			&& StringUtils.equals(getInstructionCode(), rule.getInstructionCode())
			&& (getSerialNumberUniqueFlag() == rule.getSerialNumberUniqueFlag())
			&& StringUtils.equals(getStrategy(), rule.getStrategy())
			&& StringUtils.equals(getDeviceId(), rule.getDeviceId());
	}
	
	public boolean functionEquals(LotControlRule rule) {
		return basicFunctionEquals(rule)
			&& hasSamePartByProductSpecs(rule)
			&& (this.isParentMTOC(rule) || rule.isParentMTOC(this));
	}
	
	public boolean functionEqualsForMBPN(LotControlRule rule) {
		return basicFunctionEquals(rule)
			&& hasSameProductSpecs(rule);
	}
	
	public boolean hasSameProductSpecs(LotControlRule rule) {
		return StringUtils.equals(getId().getProductSpecCode(), rule.getId().getProductSpecCode()) || StringUtils.equals(rule.getId().getProductSpecCode(), ASTERISK);
	}
		
	public boolean hasSamePartByProductSpecs(LotControlRule rule) {
		boolean found;
		if(this.getPartByProductSpecs().size() != rule.getPartByProductSpecs().size()) {
			return false;
		}
		for(PartByProductSpecCode part : getPartByProductSpecs()) {
			found = false;
			for(PartByProductSpecCode anotherPart : rule.getPartByProductSpecs()) {
				if(part.getId().getPartId().equals(anotherPart.getId().getPartId())) {
					found = true;
				}
			}
			if(!found) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isParentMTOC(LotControlRule rule) {
		return (getId().getModelYearCode().equals(rule.getId().getModelYearCode()) || rule.getId().getModelYearCode().equals(ASTERISK))
			&& (getId().getModelCode().equals(rule.getId().getModelCode()) || rule.getId().getModelCode().equals(ASTERISK))
			&& (getId().getModelTypeCode().equals(rule.getId().getModelTypeCode()) || rule.getId().getModelTypeCode().equals(ASTERISK))
			&& (getId().getModelOptionCode().equals(rule.getId().getModelOptionCode()) || rule.getId().getModelOptionCode().equals(ASTERISK))
			&& (getId().getExtColorCode().equals(rule.getId().getExtColorCode()) || rule.getId().getExtColorCode().equals(ASTERISK))
			&& (getId().getIntColorCode().equals(rule.getId().getIntColorCode()) || rule.getId().getIntColorCode().equals(ASTERISK));
	}

	public int getPartConfirmFlag() {
		return this.partConfirmFlag;
	}

	public void setPartConfirmFlag(int partConfirmFlag) {
		this.partConfirmFlag = partConfirmFlag;
	}
	
	public boolean isPartConfirm() {
	    return getPartConfirmFlag() == 1;
	}
	public void setPartConfirm(boolean confirm) {
    	this.partConfirmFlag = confirm ? 1 : 0; 
    }

	public int getQiDefectFlag() {
		return this.qiDefectFlag;
	}

	public void setQiDefectFlag(int qiDefectFlag) {
		this.qiDefectFlag = qiDefectFlag;
	}
	
	public boolean isQicsDefect() {
		return getQiDefectFlag() == 1;
	}
	
	public void setQicsDefect(boolean defect) {
		this.qiDefectFlag = defect ? 1 : 0;
	}
	
}

