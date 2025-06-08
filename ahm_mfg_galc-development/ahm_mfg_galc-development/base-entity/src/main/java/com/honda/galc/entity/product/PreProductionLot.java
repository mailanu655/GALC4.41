package com.honda.galc.entity.product;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.enumtype.PreProductionLotStatus;
import com.honda.galc.util.CommonUtil;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Apr 26, 2016
 */
@Entity
@Table(name="GAL212TBX")
public class PreProductionLot extends AuditEntry {
	@Id
	@Column(name="PRODUCTION_LOT")
	private String productionLot;

	@Column(name="LOT_SIZE")
	private int lotSize;

	@Column(name="START_PRODUCT_ID")
	private String startProductId;

	@Column(name="SEND_STATUS")
	private int sendStatusId;

	@Column(name="STAMPED_COUNT")
	private int stampedCount;

	@Column(name="HOLD_STATUS")
	private int holdStatus;

	@Column(name="SENT_TIMESTAMP")
	private Timestamp sentTimestamp;

	@Column(name="NEXT_PRODUCTION_LOT")
	private String nextProductionLot;

	@Column(name="LOT_NUMBER")
	private String lotNumber;

	@Column(name="PLANT_CODE")
	private String plantCode;

	@Column(name="PROCESS_LOCATION")
	private String processLocation;

	@Column(name="LINE_NO")
	private String lineNo;

	@Column(name="PRODUCT_SPEC_CODE")
	private String productSpecCode;
	
	@Column(name="PLAN_CODE")
	private String planCode;

	@Column(name="KD_LOT_NUMBER")
	private String kdLotNumber;

	@Column(name="SEQUENCE")
	private double sequence;

	@Column(name="REMAKE_FLAG")
	private String remakeFlag;

	@Column(name="STAMPING_FLAG")
	private String stampingFlag;

	@Column(name="CARRY_IN_OUT_FLAG")
	private String carryInOutFlag;

	@Column(name="CARRY_IN_OUT_UNITS")
	private int carryInOutUnits;

	@Column(name="BUILD_SEQ_NOT_FIXED_FLAG")
	private Character buildSeqNotFixedFlag;
	
	@Column(name="BUILD_SEQUENCE_NUMBER")
	private String buildSequenceNumber;
	
	/** The Manufacturing basic part number*/
	@Column( name = "MBPN" )
	private String mbpn;
	/** The part color code or part control code*/
	@Column( name = "HES_COLOR" )
	private String hesColor;
	
	@Column(name = "DEMAND_TYPE")
    private String demandType;
	
	@Column(name = "NOTES")
    private String notes;
	
	@Column(name = "PLAN_OFF_DATE")
    private Date planOffDate;
	
    @Transient
    private String partNumber;
    
    @Transient
    private String partMark;
    
    @Transient
    private String productionDate;
    
    private static final long serialVersionUID = 1L;
    
    public static String PROCESS_LOCATION_KNUCKLE = "KN";
    public static String PROCESS_LOCATION_KNUCKLE_KR = "KR";

    public PreProductionLot() {
		super();
	}

	public PreProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}

	public String getProductionLot() {
		return StringUtils.trim(this.productionLot);
	}
	
	public String getId() {
		return getProductionLot();
	}

	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}

	public int getLotSize() {
		return this.lotSize;
	}

	public void setLotSize(int lotSize) {
		this.lotSize = lotSize;
	}

	public String getStartProductId() {
		boolean isJpVIN = ProductNumberDef.VIN_JPN.isNumberValid(this.startProductId);
		if(isJpVIN) {
			return this.startProductId;
		}
		return StringUtils.trim(this.startProductId);
	}

	public void setStartProductId(String startProductId) {
		this.startProductId = startProductId;
	}

	public int getSendStatusId() {
		return this.sendStatusId;
	}

	public void setSendStatusId(int sendStatusId) {
		this.sendStatusId = sendStatusId;
	}
		
	public PreProductionLotSendStatus getSendStatus() {
		return PreProductionLotSendStatus.getType(sendStatusId);
	}

	public void setSendStatus(PreProductionLotSendStatus status) {
		this.sendStatusId = status.getId();
	}

	public int getStampedCount() {
		return this.stampedCount;
	}

	public void setStampedCount(int stampedCount) {
		this.stampedCount = stampedCount;
	}

	public int getHoldStatus() {
		return this.holdStatus;
	}

	public void setHoldStatus(int holdStatus) {
		this.holdStatus = holdStatus;
	}
	
	public PreProductionLotStatus getLotStatus() {
		return PreProductionLotStatus.getType(this.holdStatus);
	}
	
	public void setLotStatus(PreProductionLotStatus lotStatus) {
		this.holdStatus = lotStatus.getId();
	}

	public Timestamp getSentTimestamp() {
		return this.sentTimestamp;
	}

	public void setSentTimestamp(Timestamp sentTimestamp) {
		this.sentTimestamp = sentTimestamp;
	}

	public String getNextProductionLot() {
		return StringUtils.trim(this.nextProductionLot);
	}

	public void setNextProductionLot(String nextProductionLot) {
		this.nextProductionLot = nextProductionLot;
	}

	public String getLotNumber() {
		return StringUtils.trim(this.lotNumber);
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public String getPlantCode() {
		return StringUtils.trim(this.plantCode);
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public String getProcessLocation() {
		return StringUtils.trim(this.processLocation);
	}

	public void setProcessLocation(String processLocation) {
		this.processLocation = processLocation;
	}

	public String getLineNo() {
		return StringUtils.trim(this.lineNo);
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getProductSpecCode() {
		return StringUtils.trim(this.productSpecCode);
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
	
	public String getKdLot() {
		return getKdLotNumber();
	}
	
	public String getKdLotSequence() {
		return getProductionLot() + getKdLot();
	}
	
	public String getLotPosition() {
		int units = 1;
		
		if(isKnuckleProduct()) units = 2;
		
		return "" + getStampedCount() + "/" + getLotSize() * units;
		
	}
	
	public boolean isKnuckleProduct() {
		return PROCESS_LOCATION_KNUCKLE.equalsIgnoreCase(getProcessLocation()) || 
			PROCESS_LOCATION_KNUCKLE_KR.equalsIgnoreCase(getProcessLocation());
	}
	
	public String getPartMark() {
		return partMark;
	}

	public void setPartMark(String partMark) {
		this.partMark = partMark;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public boolean isSameKdLot(PreProductionLot preProductionLot) {
		
		if(getKdLot() == null || preProductionLot == null || preProductionLot.getKdLot() == null) return false;
		return getKdLot().substring(0,getKdLot().length() - 1).equals(
				preProductionLot.getKdLot().substring(0,preProductionLot.getKdLot().length() -1));
		
	}
	
	public boolean isSameKdLot(String kdLot) {
		if(getKdLot() == null || kdLot == null) return false;
		return getKdLot().substring(0,getKdLot().length() - 1).equals(
				kdLot.substring(0,kdLot.length() -1));
		
	}

	public boolean isSamePlant(PreProductionLot preProductionLot) {
		
		if(preProductionLot == null) return false;
		return getProductionLot().substring(0, 6).equals(preProductionLot.getProductionLot().substring(0,6));
		
	}
	
	public boolean isSamePlant(String plantCode) {
		
		return getProductionLot().substring(0, 6).equals(plantCode);
		
	}
	
	public String deriveLineNumber() {
		return getProductionLot().substring(4, 6);
	}
	
	@SuppressWarnings("deprecation")
	public Date deriveProductionDate() {
		int year = Integer.parseInt(getLotNumber().substring(0, 4));
		int month = Integer.parseInt(getLotNumber().substring(4, 6));
		int day = Integer.parseInt(getLotNumber().substring(6, 8));
		return new Date(year,month,day);
	}
	
	public String deriveProductDateString() {
		return getProductionLot().substring(7, 16);
	}
	
	public boolean equals (Object o) {
		if(o == null || ! (o instanceof PreProductionLot)) 
			return false;
		
		PreProductionLot obj = (PreProductionLot) o;

		return 
			((this.getBuildSeqNotFixedFlag() == null) ? obj.getBuildSeqNotFixedFlag() == null : this.getBuildSeqNotFixedFlag().equals(obj.getBuildSeqNotFixedFlag())) &&
			((this.getBuildSequenceNumber() == null) ? obj.getBuildSequenceNumber() == null : this.getBuildSequenceNumber().equals(obj.getBuildSequenceNumber())) &&
			((this.getCarryInOutFlag() == null) ? obj.getCarryInOutFlag() == null : this.getCarryInOutFlag().equals(obj.getCarryInOutFlag())) &&
			this.getCarryInOutUnits() == obj.getCarryInOutUnits() &&
			((this.getHesColor() == null) ? obj.getHesColor() == null : this.getHesColor().equals(obj.getHesColor())) &&
			this.getHoldStatus() == obj.getHoldStatus() &&
			((this.getKdLotNumber() == null) ? obj.getKdLotNumber() == null : this.getKdLotNumber().equals(obj.getKdLotNumber())) &&
			((this.getLineNo() == null) ? obj.getLineNo() == null : this.getLineNo().equals(obj.getLineNo())) &&
			((this.getLotNumber() == null) ? obj.getLotNumber() == null : this.getLotNumber().equals(obj.getLotNumber())) &&
			this.getLotSize() == obj.getLotSize() && 
			((this.getMbpn() == null) ? obj.getMbpn() == null : this.getMbpn().equals(obj.getMbpn())) &&
			((this.getNextProductionLot() == null) ? obj.getNextProductionLot() == null : this.getNextProductionLot().equals(obj.getNextProductionLot())) &&
			((this.getPartMark() == null) ? obj.getPartMark() == null : this.getPartMark().equals(obj.getPartMark())) &&
			((this.getPartNumber() == null) ? obj.getPartNumber() == null : this.getPartNumber().equals(obj.getPartNumber())) &&
			((this.getPlanCode() == null) ? obj.getPlanCode() == null : this.getPlanCode().equals(obj.getPlanCode())) &&
			((this.getPlantCode() == null) ? obj.getPlantCode() == null : this.getPlantCode().equals(obj.getPlantCode())) &&
			((this.getProcessLocation() == null) ? obj.getProcessLocation() == null : this.getProcessLocation().equals(obj.getProcessLocation())) &&
			((this.getProductionLot() == null) ? obj.getProductionLot() == null : this.getProductionLot().equals(obj.getProductionLot())) &&
			((this.getProductSpecCode() == null) ? obj.getProductSpecCode() == null : this.getProductSpecCode().equals(obj.getProductSpecCode())) &&
			((this.getRemakeFlag() == null) ? obj.getRemakeFlag() == null : this.getRemakeFlag().equals(obj.getRemakeFlag())) &&
			this.getSendStatusId() == obj.getSendStatusId() &&
			this.getSequence() == obj.getSequence() &&
			this.getStampedCount() == obj.getStampedCount() &&
			((this.getStampingFlag() == null) ? obj.getStampingFlag() == null : this.getStampingFlag().equals(obj.getStampingFlag())) &&
			((this.getStartProductId() == null) ? obj.getStartProductId() == null : this.getStartProductId().equals(obj.getStartProductId()));
	}
	
	public String toString() {
		return toString(getProductionLot(),getNextProductionLot(),getKdLot(),getLotSize(),getProcessLocation(),getHoldStatus(),getSendStatus());
	}
	
	public void countOne(){
		stampedCount++;
	}

	public String getPlanCode() {
		return StringUtils.trim(this.planCode);
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public String getKdLotNumber() {
		return StringUtils.trim(this.kdLotNumber);
	}

	public String getPreProdLotKdLotNumber() { //get KD LOT from 212 even if it is null
		return StringUtils.trim(this.kdLotNumber);
	}
	
	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}

	public double getSequence() {
		return this.sequence;
	}

	public void setSequence(double sequence) {
		this.sequence = sequence;
	}

	public String getRemakeFlag() {
		return StringUtils.trim(this.remakeFlag);
	}

	public void setRemakeFlag(String remakeFlag) {
		this.remakeFlag = remakeFlag;
	}

	public String getStampingFlag() {
		return StringUtils.trim(this.stampingFlag);
	}

	public void setStampingFlag(String stampingFlag) {
		this.stampingFlag = stampingFlag;
	}

	public String getCarryInOutFlag() {
		return StringUtils.trim(this.carryInOutFlag);
	}

	public void setCarryInOutFlag(String carryInOutFlag) {
		this.carryInOutFlag = carryInOutFlag;
	}

	public int getCarryInOutUnits() {
		return this.carryInOutUnits;
	}

	public void setCarryInOutUnits(int carryInOutUnits) {
		this.carryInOutUnits = carryInOutUnits;
	}

	/**
	 * @return the buildSequenceNumber
	 */
	public String getBuildSequenceNumber() {
		return buildSequenceNumber;
	}

	/**
	 * @param buildSequenceNumber the buildSequenceNumber to set
	 */
	public void setBuildSequenceNumber(String buildSequenceNumber) {
		this.buildSequenceNumber = buildSequenceNumber;
	}

	/**
	 * @return the buildSeqNotFixedFlag
	 */
	public Character getBuildSeqNotFixedFlag() {
		return buildSeqNotFixedFlag;
	}

	/**
	 * @param buildSeqNotFixedFlag the buildSeqNotFixedFlag to set
	 */
	public void setBuildSeqNotFixedFlag(Character buildSeqNotFixedFlag) {
		this.buildSeqNotFixedFlag = buildSeqNotFixedFlag;
	}
	
	/**
	 * @return the mbpn
	 */
	public String getMbpn() {
		return StringUtils.trim( mbpn );
	}

	/**
	 * @param mbpn the mbpn to set
	 */
	public void setMbpn(String mbpn) {
		this.mbpn = mbpn;
	}

	/**
	 * @return the hesColor
	 */
	public String getHesColor() {
		return StringUtils.trim( hesColor );
	}

	/**
	 * @param hesColor the hesColor to set
	 */
	public void setHesColor(String hesColor) {
		this.hesColor = hesColor;
	}
	
	public String deriveMbpn() {
		if (getProductSpecCode().length() < 18) {
			return getProductSpecCode();
		} else {
			return getProductSpecCode().substring(0, 18);		
		}
	}

	public String deriveHesColor() {
		if (getProductSpecCode().length() >= 18) {
			return getProductSpecCode().substring(18);
		} else {
			return "";
		}
	}
	
	public String getDemandType() {
        return StringUtils.trim(this.demandType);
    }

    public void setDemandType(String demandType) {
        this.demandType = demandType;
    }

	public String getNotes() {
		return StringUtils.trim(notes);
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getLotPosition(String attributeValue) {
		int units = CommonUtil.toList(attributeValue).size();
		return units == 0 ? getLotPosition() : ("" + getStampedCount() + "/" + getLotSize() * units);
	}

	public String getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}

	public Date getPlanOffDate() {
		return planOffDate;
	}

	public void setPlanOffDate(Date planOffDate) {
		this.planOffDate = planOffDate;
	}


}