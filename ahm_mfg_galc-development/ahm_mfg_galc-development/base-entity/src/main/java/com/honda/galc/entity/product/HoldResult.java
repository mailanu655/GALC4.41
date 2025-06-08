package com.honda.galc.entity.product;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>HoldResult Class description</h3>
 * <p> HoldResult description </p>
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
 * Apr 6, 2011
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name="GAL147TBX")
public class HoldResult extends AuditEntry {

	@EmbeddedId
	private HoldResultId id;

	@Column(name="RELEASE_TIMESTAMP")
	private Timestamp releaseTimestamp;

	@Column(name="RELEASE_FLAG")
	private int releaseFlag;

	@Column(name="HOLD_ASSOCIATE_NO")
	private String holdAssociateNo;

	@Column(name="HOLD_ASSOCIATE_NAME")
	private String holdAssociateName;

	@Column(name="HOLD_ASSOCIATE_PHONE")
	private String holdAssociatePhone;

	@Column(name="HOLD_ASSOCIATE_PAGER")
	private String holdAssociatePager;

	@Column(name="HOLD_REASON")
	private String holdReason;

	@Column(name="RELEASE_ASSOCIATE_NO")
	private String releaseAssociateNo;

	@Column(name="RELEASE_ASSOCIATE_NAME")
	private String releaseAssociateName;

	@Column(name="RELEASE_ASSOCIATE_PHONE")
	private String releaseAssociatePhone;

	@Column(name="RELEASE_ASSOCIATE_PAGER")
	private String releaseAssociatePager;

	@Column(name="RELEASE_REASON")
	private String releaseReason;

	@Column(name="LOT_HOLD_STATUS")
	private int lotHoldStatus;

	@Column(name="PRODUCTION_LOT")
	private String productionLot;

	@Column(name="PRODUCT_SPEC_CODE")
	private String productSpecCode;

	@Column(name="PRODUCTION_DATE")
	private Date productionDate;

	@Column(name="QSR_ID")
	private int qsrId;

	@Column(name="HOLD_PROCESS_POINT")
	private String holdProcessPoint;

	@Column(name="EQUIPMENT_FLAG")
	private int equipmentFlg;

	@Column(name="RELEASE_PERMISSION")
	private int releasePermission;

	private static final long serialVersionUID = 1L;

	public HoldResult() {
		super();
	}
	
	public HoldResult(String productId, int holdType) {
		super();
		this.id = new HoldResultId(productId, holdType);
	}


	public HoldResultId getId() {
		return this.id;
	}

	public void setId(HoldResultId id) {
		this.id = id;
	}

	public Timestamp getReleaseTimestamp() {
		return this.releaseTimestamp;
	}

	public void setReleaseTimestamp(Timestamp releaseTimestamp) {
		this.releaseTimestamp = releaseTimestamp;
	}

	public int getReleaseFlag() {
		return this.releaseFlag;
	}

	public void setReleaseFlag(short releaseFlag) {
		this.releaseFlag = releaseFlag;
	}

	public String getHoldAssociateNo() {
		return StringUtils.trim(this.holdAssociateNo);
	}

	public void setHoldAssociateNo(String holdAssociateNo) {
		this.holdAssociateNo = holdAssociateNo;
	}

	public String getHoldAssociateName() {
		return StringUtils.trim(this.holdAssociateName);
	}

	public void setHoldAssociateName(String holdAssociateName) {
		this.holdAssociateName = holdAssociateName;
	}

	public String getHoldAssociatePhone() {
		return StringUtils.trim(this.holdAssociatePhone);
	}

	public void setHoldAssociatePhone(String holdAssociatePhone) {
		this.holdAssociatePhone = holdAssociatePhone;
	}

	public String getHoldAssociatePager() {
		return StringUtils.trim(this.holdAssociatePager);
	}

	public void setHoldAssociatePager(String holdAssociatePager) {
		this.holdAssociatePager = holdAssociatePager;
	}

	public String getHoldReason() {
		return StringUtils.trim(this.holdReason);
	}

	public void setHoldReason(String holdReason) {
		this.holdReason = holdReason;
	}

	public String getReleaseAssociateNo() {
		return StringUtils.trim(this.releaseAssociateNo);
	}

	public void setReleaseAssociateNo(String releaseAssociateNo) {
		this.releaseAssociateNo = releaseAssociateNo;
	}

	public String getReleaseAssociateName() {
		return StringUtils.trim(this.releaseAssociateName);
	}

	public void setReleaseAssociateName(String releaseAssociateName) {
		this.releaseAssociateName = releaseAssociateName;
	}

	public String getReleaseAssociatePhone() {
		return StringUtils.trim(this.releaseAssociatePhone);
	}

	public void setReleaseAssociatePhone(String releaseAssociatePhone) {
		this.releaseAssociatePhone = releaseAssociatePhone;
	}

	public String getReleaseAssociatePager() {
		return StringUtils.trim(this.releaseAssociatePager);
	}

	public void setReleaseAssociatePager(String releaseAssociatePager) {
		this.releaseAssociatePager = releaseAssociatePager;
	}

	public String getReleaseReason() {
		return StringUtils.trim(this.releaseReason);
	}

	public void setReleaseReason(String releaseReason) {
		this.releaseReason = releaseReason;
	}

	public int getLotHoldStatus() {
		return this.lotHoldStatus;
	}

	public void setLotHoldStatus(int lotHoldStatus) {
		this.lotHoldStatus = lotHoldStatus;
	}

	public String getProductionLot() {
		return StringUtils.trim(this.productionLot);
	}

	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}

	public String getProductSpecCode() {
		return StringUtils.trim(this.productSpecCode);
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public String getHoldProcessPoint() {
		return StringUtils.trim(this.holdProcessPoint);
	}

	public void setHoldProcessPoint(String holdProcessPoint) {
		this.holdProcessPoint = holdProcessPoint;
	}

	public int getEquipmentFlg() {
		return this.equipmentFlg;
	}

	public void setEquipmentFlg(int equipmentFlg) {
		this.equipmentFlg = equipmentFlg;
	}

	public int getReleasePermission() {
		return this.releasePermission;
	}

	public void setReleasePermission(int releasePermission) {
		this.releasePermission = releasePermission;
	}

	public Date getProductionDate() {
		return this.productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public int getQsrId() {
		return this.qsrId;
	}

	public void setQsrId(int qsrId) {
		this.qsrId = qsrId;
	}

	@Override
	public String toString() {
		String productId = id == null ? null : id.getProductId();
		Integer holdType = id == null ? null : id.getHoldType();
		return toString(productId, holdType, getHoldReason(), getHoldAssociateNo());
	}
}