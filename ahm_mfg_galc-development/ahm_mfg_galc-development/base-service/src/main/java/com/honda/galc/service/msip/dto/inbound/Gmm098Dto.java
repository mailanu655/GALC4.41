package com.honda.galc.service.msip.dto.inbound;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.ToStringUtil;

/**
 * 
 * <h3>SalesOrderFifDTO.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> SalesOrderFifDTO.java description </p>
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

public class Gmm098Dto  implements IMsipInboundDto {

	private static final long serialVersionUID = 1L;
	private String uniqueId;
	private String salesModelCd;
	private String salesVehDestCd;
	private String salesOptionCd;
	private String salesExtClrCd;
	private String salesIntClrCd;
	private String orderType;
	private int orderSeqNo;
	private String frmPlantCd;
	private String frmModelYearCd;
	private String frmModelCd;
	private String frmDevSeqCd;
	private String frmTypeCd;
	private String frmOptionCd;
	private String frmExtClrCd;
	private String frmIntClrCd;
	private String locProdMonth;
	private String salesDiv;
	private String prodDiv;
	private int prodQty;
	private String modelYear;
	private String buildByDt;
	private String shipperId;
	private String salesKdLotNo;
	private Date efctEndDt;
	private String fifCodes;

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

	public String getLocProdMonth() {
		return StringUtils.trim(locProdMonth);
	}

	public void setLocProdMonth(String locProdMonth) {
		this.locProdMonth = locProdMonth;
	}

	public String getSalesDiv() {
		return StringUtils.trim(salesDiv);
	}

	public void setSalesDiv(String salesDiv) {
		this.salesDiv = salesDiv;
	}

	public String getProdDiv() {
		return StringUtils.trim(prodDiv);
	}

	public void setProdDiv(String prodDiv) {
		this.prodDiv = prodDiv;
	}

	public int getProdQty() {
		return prodQty;
	}

	public void setProdQty(int prodQty) {
		this.prodQty = prodQty;
	}

	public String getModelYear() {
		return StringUtils.trim(modelYear);
	}

	public void setModelYear(String modelYear) {
		this.modelYear = modelYear;
	}

	public String getBuildByDt() {
		return StringUtils.trim(buildByDt);
	}

	public void setBuildByDt(String buildByDt) {
		this.buildByDt = buildByDt;
	}

	public String getShipperId() {
		return StringUtils.trim(shipperId);
	}

	public void setShipperId(String shipperId) {
		this.shipperId = shipperId;
	}

	public String getSalesKdLotNo() {
		return StringUtils.trim(salesKdLotNo);
	}

	public void setSalesKdLotNo(String salesKdLotNo) {
		this.salesKdLotNo = salesKdLotNo;
	}

	public Date getEfctEndDt() {
		return efctEndDt;
	}

	public void setEfctEndDt(Date efctEndDt) {
		this.efctEndDt = efctEndDt;
	}

	public String getFifCodes() {
		return StringUtils.trim(fifCodes);
	}

	public void setFifCodes(String fifCodes) {
		this.fifCodes = fifCodes;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uniqueId == null) ? 0 : uniqueId.hashCode());
		result = prime * result + ((salesModelCd == null) ? 0 : salesModelCd.hashCode());
		result = prime * result + ((salesVehDestCd == null) ? 0 : salesVehDestCd.hashCode());
		result = prime * result + ((salesOptionCd == null) ? 0 : salesOptionCd.hashCode());
		result = prime * result + ((salesExtClrCd == null) ? 0 : salesExtClrCd.hashCode());
		result = prime * result + ((salesIntClrCd == null) ? 0 : salesIntClrCd.hashCode());
		result = prime * result + ((orderType == null) ? 0 : orderType.hashCode());
		result = prime * result + orderSeqNo;
		result = prime * result + ((frmPlantCd == null) ? 0 : frmPlantCd.hashCode());
		result = prime * result + ((frmModelYearCd == null) ? 0 : frmModelYearCd.hashCode());
		result = prime * result + ((frmModelCd == null) ? 0 : frmModelCd.hashCode());
		result = prime * result + ((frmDevSeqCd == null) ? 0 : frmDevSeqCd.hashCode());
		result = prime * result + ((frmTypeCd == null) ? 0 : frmTypeCd.hashCode());
		result = prime * result + ((frmOptionCd == null) ? 0 : frmOptionCd.hashCode());
		result = prime * result + ((frmExtClrCd == null) ? 0 : frmExtClrCd.hashCode());
		result = prime * result + ((frmIntClrCd == null) ? 0 : frmIntClrCd.hashCode());
		result = prime * result + ((locProdMonth == null) ? 0 : locProdMonth.hashCode());
		result = prime * result + ((salesDiv == null) ? 0 : salesDiv.hashCode());
		result = prime * result + ((prodDiv == null) ? 0 : prodDiv.hashCode());
		result = prime * result + prodQty;
		result = prime * result + ((modelYear == null) ? 0 : modelYear.hashCode());
		result = prime * result + ((buildByDt == null) ? 0 : buildByDt.hashCode());
		result = prime * result + ((shipperId == null) ? 0 : shipperId.hashCode());
		result = prime * result + ((salesKdLotNo == null) ? 0 : salesKdLotNo.hashCode());
		result = prime * result + ((efctEndDt == null) ? 0 : efctEndDt.hashCode());
		result = prime * result + ((fifCodes == null) ? 0 : fifCodes.hashCode());
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
		Gmm098Dto other = (Gmm098Dto) obj;
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
		if (orderSeqNo != other.orderSeqNo)
			return false;
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
		if (locProdMonth == null) {
			if (other.locProdMonth != null)
				return false;
		} else if (!locProdMonth.equals(other.locProdMonth))
			return false;
		if (salesDiv == null) {
			if (other.salesDiv != null)
				return false;
		} else if (!salesDiv.equals(other.salesDiv))
			return false;
		if (prodDiv == null) {
			if (other.prodDiv != null)
				return false;
		} else if (!prodDiv.equals(other.prodDiv))
			return false;
		if (prodQty != other.prodQty) 
				return false;
		if (modelYear == null) {
			if (other.modelYear != null)
				return false;
		} else if (!modelYear.equals(other.modelYear))
			return false;
		if (buildByDt == null) {
			if (other.buildByDt != null)
				return false;
		} else if (!buildByDt.equals(other.buildByDt))
			return false;
		if (shipperId == null) {
			if (other.shipperId != null)
				return false;
		} else if (!shipperId.equals(other.shipperId))
			return false;
		if (salesKdLotNo == null) {
			if (other.salesKdLotNo != null)
				return false;
		} else if (!salesKdLotNo.equals(other.salesKdLotNo))
			return false;
		if (efctEndDt == null) {
			if (other.efctEndDt != null)
				return false;
		} else if (!efctEndDt.equals(other.efctEndDt))
			return false;
		if (fifCodes == null) {
			if (other.fifCodes != null)
				return false;
		} else if (!fifCodes.equals(other.fifCodes))
			return false;
		return true;
	}

	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}

}
