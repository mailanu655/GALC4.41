package com.honda.galc.entity.fif;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>SalesOrderFifId.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> SalesOrderFifId.java description </p>
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
 * <TD>Justin Jiang</TD>
 * <TD>Feb 16, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 * </TABLE>
 */

@Embeddable
public class SalesOrderFifId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "UNIQUE_ID")
	private String uniqueId;

	@Column(name = "SALES_MODEL_CD")
	private String salesModelCd;

	@Column(name = "SALES_VEH_DEST_CD")
	private String salesVehDestCd;

	@Column(name = "SALES_OPTION_CD")
	private String salesOptionCd;

	@Column(name = "SALES_EXT_CLR_CD")
	private String salesExtClrCd;

	@Column(name = "SALES_INT_CLR_CD")
	private String salesIntClrCd;

	@Column(name = "ORDER_TYPE")
	private String orderType;

	@Column(name = "ORDER_SEQ_NO")
	private int orderSeqNo;

	@Column(name = "FRM_PLANT_CD")
	private String frmPlantCd;

	@Column(name = "FRM_MODEL_YEAR_CD")
	private String frmModelYearCd;

	@Column(name = "FRM_MODEL_CD")
	private String frmModelCd;

	@Column(name = "FRM_DEV_SEQ_CD")
	private String frmDevSeqCd;

	@Column(name = "FRM_TYPE_CD")
	private String frmTypeCd;

	@Column(name = "FRM_OPTION_CD")
	private String frmOptionCd;

	@Column(name = "FRM_EXT_CLR_CD")
	private String frmExtClrCd;

	@Column(name = "FRM_INT_CLR_CD")
	private String frmIntClrCd;

	public SalesOrderFifId() {
		super();
	}

	public SalesOrderFifId(String uniqueId, String salesModelCd,
			String salesVehDestCd, String salesOptionCd, String salesExtClrCd,
			String salesIntClrCd, String orderType, int orderSeqNo,
			String frmPlantCd, String frmModelYearCd, String frmModelCd,
			String frmDevSeqCd, String frmTypeCd, String frmOptionCd,
			String frmExtClrCd, String frmIntClrCd) {
		super();
		this.uniqueId = uniqueId;
		this.salesModelCd = salesModelCd;
		this.salesVehDestCd = salesVehDestCd;
		this.salesOptionCd = salesOptionCd;
		this.salesExtClrCd = salesExtClrCd;
		this.salesIntClrCd = salesIntClrCd;
		this.orderType = orderType;
		this.orderSeqNo = orderSeqNo;
		this.frmPlantCd = frmPlantCd;
		this.frmModelYearCd = frmModelYearCd;
		this.frmModelCd = frmModelCd;
		this.frmDevSeqCd = frmDevSeqCd;
		this.frmTypeCd = frmTypeCd;
		this.frmOptionCd = frmOptionCd;
		this.frmExtClrCd = frmExtClrCd;
		this.frmIntClrCd = frmIntClrCd;
	}

	public String getUniqueId() {
		return StringUtils.trim(uniqueId);
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getSalesModelCd() {
		return StringUtils.trim(salesModelCd);
	}

	public void setSalesModelCd(String salesModelCd) {
		this.salesModelCd = salesModelCd;
	}

	public String getSalesVehDestCd() {
		return StringUtils.trim(salesVehDestCd);
	}

	public void setSalesVehDestCd(String salesVehDestCd) {
		this.salesVehDestCd = salesVehDestCd;
	}

	public String getSalesOptionCd() {
		return StringUtils.trim(salesOptionCd);
	}

	public void setSalesOptionCd(String salesOptionCd) {
		this.salesOptionCd = salesOptionCd;
	}

	public String getSalesExtClrCd() {
		return StringUtils.trim(salesExtClrCd);
	}

	public void setSalesExtClrCd(String salesExtClrCd) {
		this.salesExtClrCd = salesExtClrCd;
	}

	public String getSalesIntClrCd() {
		return StringUtils.trim(salesIntClrCd);
	}

	public void setSalesIntClrCd(String salesIntClrCd) {
		this.salesIntClrCd = salesIntClrCd;
	}

	public String getOrderType() {
		return StringUtils.trim(orderType);
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public int getOrderSeqNo() {
		return orderSeqNo;
	}

	public void setOrderSeqNo(int orderSeqNo) {
		this.orderSeqNo = orderSeqNo;
	}

	public String getFrmPlantCd() {
		return StringUtils.trim(frmPlantCd);
	}

	public void setFrmPlantCd(String frmPlantCd) {
		this.frmPlantCd = frmPlantCd;
	}

	public String getFrmModelYearCd() {
		return StringUtils.trim(frmModelYearCd);
	}

	public void setFrmModelYearCd(String frmModelYearCd) {
		this.frmModelYearCd = frmModelYearCd;
	}

	public String getFrmModelCd() {
		return StringUtils.trim(frmModelCd);
	}

	public void setFrmModelCd(String frmModelCd) {
		this.frmModelCd = frmModelCd;
	}

	public String getFrmDevSeqCd() {
		return StringUtils.trim(frmDevSeqCd);
	}

	public void setFrmDevSeqCd(String frmDevSeqCd) {
		this.frmDevSeqCd = frmDevSeqCd;
	}

	public String getFrmTypeCd() {
		return StringUtils.trim(frmTypeCd);
	}

	public void setFrmTypeCd(String frmTypeCd) {
		this.frmTypeCd = frmTypeCd;
	}

	public String getFrmOptionCd() {
		return StringUtils.trim(frmOptionCd);
	}

	public void setFrmOptionCd(String frmOptionCd) {
		this.frmOptionCd = frmOptionCd;
	}

	public String getFrmExtClrCd() {
		return StringUtils.trim(frmExtClrCd);
	}

	public void setFrmExtClrCd(String frmExtClrCd) {
		this.frmExtClrCd = frmExtClrCd;
	}

	public String getFrmIntClrCd() {
		return StringUtils.trim(frmIntClrCd);
	}

	public void setFrmIntClrCd(String frmIntClrCd) {
		this.frmIntClrCd = frmIntClrCd;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((uniqueId == null) ? 0 : uniqueId.hashCode());
		result = prime * result
				+ ((salesModelCd == null) ? 0 : salesModelCd.hashCode());
		result = prime * result
				+ ((salesVehDestCd == null) ? 0 : salesVehDestCd.hashCode());
		result = prime * result
				+ ((salesOptionCd == null) ? 0 : salesOptionCd.hashCode());
		result = prime * result
				+ ((salesExtClrCd == null) ? 0 : salesExtClrCd.hashCode());
		result = prime * result
				+ ((salesIntClrCd == null) ? 0 : salesIntClrCd.hashCode());
		result = prime * result
				+ ((orderType == null) ? 0 : orderType.hashCode());
		result = prime * result + orderSeqNo;
		result = prime * result
				+ ((frmPlantCd == null) ? 0 : frmPlantCd.hashCode());
		result = prime * result
				+ ((frmModelYearCd == null) ? 0 : frmModelYearCd.hashCode());
		result = prime * result
				+ ((frmModelCd == null) ? 0 : frmModelCd.hashCode());
		result = prime * result
				+ ((frmDevSeqCd == null) ? 0 : frmDevSeqCd.hashCode());
		result = prime * result
				+ ((frmTypeCd == null) ? 0 : frmTypeCd.hashCode());
		result = prime * result
				+ ((frmOptionCd == null) ? 0 : frmOptionCd.hashCode());
		result = prime * result
				+ ((frmExtClrCd == null) ? 0 : frmExtClrCd.hashCode());
		result = prime * result
				+ ((frmIntClrCd == null) ? 0 : frmIntClrCd.hashCode());

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
		SalesOrderFifId other = (SalesOrderFifId) obj;
		if (uniqueId == null) {
			if (other.uniqueId != null)
				return false;
		} else if (!uniqueId.equals(other.uniqueId))
			return false;
		if (salesModelCd == null) {
			if (other.salesModelCd != null)
				return false;
		} else if (!salesModelCd.equals(other.salesModelCd))
			return false;
		if (salesVehDestCd == null) {
			if (other.salesVehDestCd != null)
				return false;
		} else if (!salesVehDestCd.equals(other.salesVehDestCd))
			return false;
		if (salesOptionCd == null) {
			if (other.salesOptionCd != null)
				return false;
		} else if (!salesOptionCd.equals(other.salesOptionCd))
			return false;
		if (salesExtClrCd == null) {
			if (other.salesExtClrCd != null)
				return false;
		} else if (!salesExtClrCd.equals(other.salesExtClrCd))
			return false;
		if (salesIntClrCd == null) {
			if (other.salesIntClrCd != null)
				return false;
		} else if (!salesIntClrCd.equals(other.salesIntClrCd))
			return false;
		if (orderType == null) {
			if (other.orderType != null)
				return false;
		} else if (!orderType.equals(other.orderType))
			return false;
		if (orderSeqNo != other.orderSeqNo) {
			return false;
		}
		if (frmPlantCd == null) {
			if (other.frmPlantCd != null)
				return false;
		} else if (!frmPlantCd.equals(other.frmPlantCd))
			return false;
		if (frmModelYearCd == null) {
			if (other.frmModelYearCd != null)
				return false;
		} else if (!frmModelYearCd.equals(other.frmModelYearCd))
			return false;
		if (frmModelCd == null) {
			if (other.frmModelCd != null)
				return false;
		} else if (!frmModelCd.equals(other.frmModelCd))
			return false;
		if (frmDevSeqCd == null) {
			if (other.frmDevSeqCd != null)
				return false;
		} else if (!frmDevSeqCd.equals(other.frmDevSeqCd))
			return false;
		if (frmTypeCd == null) {
			if (other.frmTypeCd != null)
				return false;
		} else if (!frmTypeCd.equals(other.frmTypeCd))
			return false;
		if (frmOptionCd == null) {
			if (other.frmOptionCd != null)
				return false;
		} else if (!frmOptionCd.equals(other.frmOptionCd))
			return false;
		if (frmExtClrCd == null) {
			if (other.frmExtClrCd != null)
				return false;
		} else if (!frmExtClrCd.equals(other.frmExtClrCd))
			return false;
		if (frmIntClrCd == null) {
			if (other.frmIntClrCd != null)
				return false;
		} else if (!frmIntClrCd.equals(other.frmIntClrCd))
			return false;
		return true;
	}
}
