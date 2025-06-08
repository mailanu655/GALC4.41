package com.honda.galc.entity.oif;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name = "MS_PMX_TBX")
public class MaterialService extends AuditEntry {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private MaterialServiceId id;
	
	@Column(name = "PLAN_CODE")
	private String planCode;
	
	@Column(name = "LINE_NO")
	private String lineNo;
	
	@Column(name = "PRODUCTION_DATE")
	private Date productionDate;
	
	@Column(name = "PRODUCT_SPEC_CODE")
	private String productSpecCode;
	
	@Column(name = "LOT_SIZE")
	private Integer lotSize;
	
	@Column(name = "ON_SEQ_NO")
	private Integer onSeqNo;
	
	@Column(name = "PRODUCTION_LOT")
	private String productionLot;
	
	@Column(name = "KD_LOT_NUMBER")
	private String kdLotNumber;
	
	@Column(name = "PLAN_OFF_DATE")
	private Date planOffDate;
	
	@Column(name = "CURRENT_TIMESTAMP")
	private String currentTimestamp;
	
	@Column(name = "SENT_FLAG")
	private Character sentFlag;
	
	@Column(name = "ACTUAL_TIMESTAMP")
	private Timestamp actualTimestamp;
	
	/**
	 * @return the platCode
	 */
	public String getPlanCode() {
		return StringUtils.trim(planCode);
	}

	/**
	 * @param platCode the platCode to set
	 */
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	/**
	 * @return the lineNo
	 */
	public String getLineNo() {
		return StringUtils.trim(lineNo);
	}

	/**
	 * @param lineNo the lineNo to set
	 */
	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	/**
	 * @return the productionDate
	 */
	public Date getProductionDate() {
		return productionDate;
	}

	/**
	 * @param productionDate the productionDate to set
	 */
	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	/**
	 * @return the productSpecCode
	 */
	public String getProductSpecCode() {
		return StringUtils.trim(productSpecCode);
	}

	/**
	 * @param productSpecCode the productSpecCode to set
	 */
	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	/**
	 * @return the lotSize
	 */
	public Integer getLotSize() {
		return lotSize;
	}

	/**
	 * @param lotSize the lotSize to set
	 */
	public void setLotSize(Integer lotSize) {
		this.lotSize = lotSize;
	}

	/**
	 * @return the onSeqNo
	 */
	public Integer getOnSeqNo() {
		return onSeqNo;
	}

	/**
	 * @param onSeqNo the onSeqNo to set
	 */
	public void setOnSeqNo(Integer onSeqNo) {
		this.onSeqNo = onSeqNo;
	}

	/**
	 * @return the productionLot
	 */
	public String getProductionLot() {
		return StringUtils.trim(productionLot);
	}

	/**
	 * @param productionLot the productionLot to set
	 */
	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}

	/**
	 * @return the kdLotNumber
	 */
	public String getKdLotNumber() {
		return StringUtils.trim(kdLotNumber);
	}

	/**
	 * @param kdLotNumber the kdLotNumber to set
	 */
	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}

	/**
	 * @return the planOffDate
	 */
	public Date getPlanOffDate() {
		return planOffDate;
	}

	/**
	 * @param planOffDate the planOffDate to set
	 */
	public void setPlanOffDate(Date planOffDate) {
		this.planOffDate = planOffDate;
	}

	/**
	 * @return the currentTimestamp
	 */
	public String getCurrentTimestamp() {
		return StringUtils.trim(currentTimestamp);
	}

	/**
	 * @param currentTimestamp the currentTimestamp to set
	 */
	public void setCurrentTimestamp(String currentTimestamp) {
		this.currentTimestamp = currentTimestamp;
	}

	/**
	 * @return the sentFlag
	 */
	public Character getSentFlag() {
		return sentFlag;
	}

	/**
	 * @param sentFlag the sentFlag to set
	 */
	public void setSentFlag(Character sentFlag) {
		this.sentFlag = sentFlag;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(MaterialServiceId id) {
		this.id = id;
	}
	
	/**
	 * @return the id
	 */
	public MaterialServiceId getId() {		
		return this.id;
	}
	
	@Override
	public String toString() {
		return super.toString(id, productSpecCode, lotSize, productionLot, kdLotNumber, planOffDate);
	}

	/**
	 * @return the actualTimestamp
	 */
	public Timestamp getActualTimestamp() {
		return actualTimestamp;
	}

	/**
	 * @param actualTimestamp the actualTimestamp to set
	 */
	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

}
