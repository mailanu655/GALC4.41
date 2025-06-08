package com.honda.galc.entity.qi;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;
/**
 * 
 * <h3>QiRepairAreaSpaceHistoryId Class description</h3>
 * <p> QiRepairAreaSpaceHistoryId History data for repair area space </p>
 * 
 * <h4>Repair Area</h4>
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
 * @author LnTInfotech<br>
 *        Mar 1, 2017
 * 
 */
@Embeddable
public class QiRepairAreaSpaceHistoryId implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "REPAIR_AREA_NAME")
	private String repairAreaName;
	
	@Column(name = "REPAIR_AREA_ROW")
	private int repairAreaRow;
	
	@Column(name = "REPAIR_AREA_SPACE")
	private int repairAreaSpace;
	
	@Column(name = "PRODUCT_ID")
	private String productId;
	
	@Column(name = "ACTUAL_TIMESTAMP")
	private Date actualtimestamp;

	public QiRepairAreaSpaceHistoryId() {
		super();
	}
	
	public QiRepairAreaSpaceHistoryId(QiRepairAreaSpace qiRepairAreaSpace) {
		super();
		this.repairAreaName=qiRepairAreaSpace.getId().getRepairAreaName();
		this.repairAreaRow=qiRepairAreaSpace.getId().getRepairArearRow();
		this.repairAreaSpace=qiRepairAreaSpace.getId().getRepairArearSpace();
		this.productId=qiRepairAreaSpace.getProductId();
		this.actualtimestamp = new Date();
		
	}

	public String getRepairAreaName() {
		return StringUtils.trimToEmpty(repairAreaName);
	}

	public void setRepairAreaName(String repairAreaName) {
		this.repairAreaName = repairAreaName;
	}

	public int getRepairArearRow() {
		return repairAreaRow;
	}

	public void setRepairArearRow(int repairArearRow) {
		this.repairAreaRow = repairArearRow;
	}

	public int getRepairArearSpace() {
		return repairAreaSpace;
	}

	public void setRepairArearSpace(int repairArearSpace) {
		this.repairAreaSpace = repairArearSpace;
	}

	public String getProductId() {
		return StringUtils.trimToEmpty(productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Date getActualtimestamp() {
		return actualtimestamp;
	}

	public void setActualtimestamp(Date actualtimestamp) {
		this.actualtimestamp = actualtimestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actualtimestamp == null) ? 0 : actualtimestamp.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((repairAreaName == null) ? 0 : repairAreaName.hashCode());
		result = prime * result + repairAreaRow;
		result = prime * result + repairAreaSpace;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QiRepairAreaSpaceHistoryId other = (QiRepairAreaSpaceHistoryId) obj;
		if (actualtimestamp == null) {
			if (other.actualtimestamp != null)
				return false;
		} else if (!actualtimestamp.equals(other.actualtimestamp))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (repairAreaName == null) {
			if (other.repairAreaName != null)
				return false;
		} else if (!repairAreaName.equals(other.repairAreaName))
			return false;
		if (repairAreaRow != other.repairAreaRow)
			return false;
		if (repairAreaSpace != other.repairAreaSpace)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QiRepairAreaSpaceHistoryId [repairAreaName=" + repairAreaName + ", repairArearRow=" + repairAreaRow
				+ ", repairArearSpace=" + repairAreaSpace + ", productId=" + productId + ", actualtimestamp="
				+ actualtimestamp + "]";
	}

}
