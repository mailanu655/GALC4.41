package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.BlockLoadStatus;

/**
 * 
 * <h3>BlockLoad</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> BlockLoad description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>May 26, 2011</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since May 26, 2011
 */

@Entity
@Table(name="BLOCK_LOAD_TBX")
public class BlockLoad extends AuditEntry {
	
	@Id
	@Column(name="MC_NUMBER")
	private String mcNumber;

	@Column(name="PRODUCTION_LOT")
	private String productionLot;

	@Column(name="LOT_SIZE")
	private int lotSize;

	@Column(name="PRODUCT_SPEC_CODE")
	private String productSpecCode;

	@Column(name="REFERENCE_NUMBER")
	private int referenceNumber;

	@Column(name="STATUS")
	private String statusId;

	@Transient
	private String kdLotNumber;

	private static final long serialVersionUID = 1L;

	public BlockLoad() {
		super();
	}

	public String getMcNumber() {
		return StringUtils.trim(this.mcNumber);
	}

	public void setMcNumber(String mcNumber) {
		this.mcNumber = mcNumber;
	}

	public String getProductionLot() {
		return StringUtils.trim(this.productionLot);
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

	public String getProductSpecCode() {
		return StringUtils.trim(this.productSpecCode);
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public int getReferenceNumber() {
		return this.referenceNumber;
	}

	public void setReferenceNumber(int referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	
	public String getLotSeq() {
		return "" + getReferenceNumber() + "/" + getLotSize();
	}

	public String getStatusId() {
		return this.statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public BlockLoadStatus getStatus() {
		return BlockLoadStatus.getType(Integer.valueOf(statusId));
	}

	public void setStatus(BlockLoadStatus status) {
		this.statusId = Integer.toString(status.getId());
	}

	public String getKdLotNumber() {
		return kdLotNumber;
	}

	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}

	public Object getId() {
		return getMcNumber();
	}
	
	public String toString() {
		return toString(getMcNumber(),getProductionLot(),getReferenceNumber(),getStatus(),getCreateTimestamp());
	}

}
