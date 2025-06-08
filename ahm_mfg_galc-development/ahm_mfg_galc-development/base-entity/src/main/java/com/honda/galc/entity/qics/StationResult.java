package com.honda.galc.entity.qics;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.DefectStatus;

/**
 * 
 * <h3>StationResult Class description</h3>
 * <p> StationResult description </p>
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
@Entity
@Table(name="GAL260TBX")
public class StationResult extends AuditEntry {
	@EmbeddedId
	private StationResultId id;

	@Column(name="FIRST_PRODUCT_ID")
	private String firstProductId;

	@Column(name="LAST_PRODUCT_ID")
	private String lastProductId;

	@Column(name="PRODUCT_ID_INSPECT")
	private int productIdInspect;

	@Column(name="PRODUCT_ID_DIRECT_PASSED")
	private int productIdDirectPassed;

	@Column(name="PRODUCT_ID_WITH_DEFECTS")
	private int productIdWithDefects;

	@Column(name="REPAIRED_PRODUCT_ID")
	private int repairedProductId;

	@Column(name="OUTSTANDING_PRODUCT_ID")
	private int outstandingProductId;

	@Column(name="SCRAP")
	private int scrap;

	@Column(name="AVERAGE_DEFECT_PER_PRODUCT_ID")
	private double averageDefectPerProductId;

	@Column(name="STRAIGHT_SHIP_PERCENTAGE")
	private double straightShipPercentage;

	@Column(name="ROYAL_STRAIGHT_SHIP_PERCENTAGE")
	private double royalStraightShipPercentage;

	@Column(name="TOP_COAT_PASS_RATE")
	private double topCoatPassRate;

	@Column(name="REPAIR_PASS_RATE")
	private double repairPassRate;

	@Column(name="OUTSTANDING_RPU")
	private double outstandingRpu;

	@Column(name="ACTUAL_TIMESTAMP")
	private Timestamp actualTimestamp;

	@Column(name="PRODUCT_ID_RSSR")
	private int productIdRssr;

	@Column(name="PREHEAT")
	private int preheat;
	
	@Column(name="TOTAL_REJECTION")
	private int totalRejections;
	
	@Column(name="SCAN_COUNT")
	private int scanCount;

	private static final long serialVersionUID = 1L;

	public StationResult() {
		super();
	}

	public StationResultId getId() {
		return this.id;
	}

	public void setId(StationResultId id) {
		this.id = id;
	}

	public String getFirstProductId() {
		return StringUtils.trim(this.firstProductId);
	}

	public void setFirstProductId(String firstProductId) {
		this.firstProductId = firstProductId;
	}

	public String getLastProductId() {
		return StringUtils.trim(this.lastProductId);
	}

	public void setLastProductId(String lastProductId) {
		this.lastProductId = lastProductId;
	}

	public int getProductIdInspect() {
		return this.productIdInspect;
	}

	public void setProductIdInspect(int productIdInspect) {
		this.productIdInspect = productIdInspect;
	}

	public int getProductIdDirectPassed() {
		return this.productIdDirectPassed;
	}

	public void setProductIdDirectPassed(int productIdDirectPassed) {
		this.productIdDirectPassed = productIdDirectPassed;
	}

	public int getProductIdWithDefects() {
		return this.productIdWithDefects;
	}

	public void setProductIdWithDefects(int productIdWithDefects) {
		this.productIdWithDefects = productIdWithDefects;
	}

	public int getRepairedProductId() {
		return this.repairedProductId;
	}

	public void setRepairedProductId(int repairedProductId) {
		this.repairedProductId = repairedProductId;
	}

	public int getOutstandingProductId() {
		return this.outstandingProductId;
	}

	public void setOutstandingProductId(int outstandingProductId) {
		this.outstandingProductId = outstandingProductId;
	}

	public int getScrap() {
		return this.scrap;
	}

	public void setScrap(int scrap) {
		this.scrap = scrap;
	}

	public double getAverageDefectPerProductId() {
		return this.averageDefectPerProductId;
	}

	public void setAverageDefectPerProductId(double averageDefectPerProductId) {
		this.averageDefectPerProductId = averageDefectPerProductId;
	}

	public double getStraightShipPercentage() {
		return this.straightShipPercentage;
	}

	public void setStraightShipPercentage(double straightShipPercentage) {
		this.straightShipPercentage = straightShipPercentage;
	}

	public double getRoyalStraightShipPercentage() {
		return this.royalStraightShipPercentage;
	}

	public void setRoyalStraightShipPercentage(double royalStraightShipPercentage) {
		this.royalStraightShipPercentage = royalStraightShipPercentage;
	}

	public double getTopCoatPassRate() {
		return this.topCoatPassRate;
	}

	public void setTopCoatPassRate(double topCoatPassRate) {
		this.topCoatPassRate = topCoatPassRate;
	}

	public double getRepairPassRate() {
		return this.repairPassRate;
	}

	public void setRepairPassRate(double repairPassRate) {
		this.repairPassRate = repairPassRate;
	}

	public double getOutstandingRpu() {
		return this.outstandingRpu;
	}

	public void setOutstandingRpu(double outstandingRpu) {
		this.outstandingRpu = outstandingRpu;
	}

	public Timestamp getActualTimestamp() {
		return this.actualTimestamp;
	}

	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	public int getProductIdRssr() {
		return this.productIdRssr;
	}

	public void setProductIdRssr(int productIdRssr) {
		this.productIdRssr = productIdRssr;
	}

	public int getPreheat() {
		return this.preheat;
	}

	public void setPreheat(int preheat) {
		this.preheat = preheat;
	}
	
	public int getTotalRejections() {
		return totalRejections;
	}

	public void setTotalRejections(int totalRejections) {
		this.totalRejections = totalRejections;
	}

	public void updateResult(DefectStatus defectStatus) {

		this.productIdInspect += 1;
		if(defectStatus.isScrap()) this.scrap += 1;
		else if(defectStatus.isPreheatScrap()) this.preheat += 1;
		else {
			if(defectStatus.isDirectPass()) 
				this.productIdDirectPassed += 1;
			else {
				this.productIdWithDefects += 1;
				if(defectStatus.isOutstanding()) 
					this.outstandingProductId += 1;
				else this.repairedProductId += 1;
			}
		}

	}

	public void updateStationResultCount(DefectStatus defectStatus, boolean isDifferentProductId, int count, boolean isScannedOnce ) {
		
		if(isScannedOnce){
			this.scanCount += count;
		}
		if(isDifferentProductId){
		this.productIdInspect += count;
		}
		
		switch(defectStatus){
		case REPAIRED:
			this.totalRejections += count;
			if(isDifferentProductId){
				this.productIdWithDefects += count;
				this.repairedProductId += count;
			}
			break;
		case NOT_REPAIRED: 
			this.totalRejections += count;
			if(isDifferentProductId){
				this.productIdWithDefects += count;
				this.outstandingProductId += count;
			}
			break;
		case DIRECT_PASS: 
			this.productIdDirectPassed += count;
			break;
		case NON_REPAIRABLE: 
			this.totalRejections += count;
			this.scrap += count; 
			if(isDifferentProductId){
				this.productIdWithDefects += count;
			}
			break;
		default:
			break;
		}
	}
	
public void updateQiRepairStationCount(boolean isDifferentProductId, int count, boolean isScannedOnce ) {
		
		if(isScannedOnce){
			this.scanCount += count;
		}
		if(isDifferentProductId){
			this.productIdInspect += count;
		}
	}

	@Override
	public String toString() {
		return toString(id.getApplicationId(),id.getShift());
	}

	public int getScanCount() {
		return scanCount;
	}

	public void setScanCount(int scanCount) {
		this.scanCount = scanCount;
	}

}
