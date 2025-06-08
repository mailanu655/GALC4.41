package com.honda.galc.oif.dto;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.fif.SalesOrderFif;
import com.honda.galc.entity.fif.SalesOrderFifId;
import com.honda.galc.util.GPCSData;

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

public class SalesOrderFifDTO {

	@GPCSData("UNIQUE_ID")
	private String uniqueId;

	@GPCSData("SALES_MODEL_CD")
	private String salesModelCd;

	@GPCSData("SALES_VEH_DEST_CD")
	private String salesVehDestCd;

	@GPCSData("SALES_OPTION_CD")
	private String salesOptionCd;

	@GPCSData("SALES_EXT_CLR_CD")
	private String salesExtClrCd;

	@GPCSData("SALES_INT_CLR_CD")
	private String salesIntClrCd;

	@GPCSData("ORDER_TYPE")
	private String orderType;

	@GPCSData("ORDER_SEQ_NO")
	private int orderSeqNo;

	@GPCSData("FRM_PLANT_CD")
	private String frmPlantCd;

	@GPCSData("FRM_MODEL_YEAR_CD")
	private String frmModelYearCd;

	@GPCSData("FRM_MODEL_CD")
	private String frmModelCd;

	@GPCSData("FRM_DEV_SEQ_CD")
	private String frmDevSeqCd;

	@GPCSData("FRM_TYPE_CD")
	private String frmTypeCd;

	@GPCSData("FRM_OPTION_CD")
	private String frmOptionCd;

	@GPCSData("FRM_EXT_CLR_CD")
	private String frmExtClrCd;

	@GPCSData("FRM_INT_CLR_CD")
	private String frmIntClrCd;

	@GPCSData("LOC_PROD_MONTH")
	private String locProdMonth;

	@GPCSData("SALES_DIV")
	private String salesDiv;

	@GPCSData("PROD_DIV")
	private String prodDiv;

	@GPCSData("PROD_QTY")
	private int prodQty;

	@GPCSData("MODEL_YEAR")
	private String modelYear;

	@GPCSData("BUILD_BY_DT")
	private String buildByDt;

	@GPCSData("SHIPPER_ID")
	private String shipperId;

	@GPCSData("SALES_KD_LOT_NO")
	private String salesKdLotNo;

	@GPCSData("EFCT_END_DT")
	private Date efctEndDt;

	@GPCSData("FIF_CODES")
	private String fifCodes;

	public SalesOrderFifDTO() {
	}

	public SalesOrderFif deriveSalesOrderFif() {
		SalesOrderFif salesOrderFif = new SalesOrderFif();
		salesOrderFif.setId(deriveID());
		salesOrderFif.setBuildByDt(buildByDt);
		salesOrderFif.setEfctEndDt(efctEndDt);
		salesOrderFif.setFifCodes(fifCodes);
		salesOrderFif.setLocProdMonth(locProdMonth);
		salesOrderFif.setModelYear(modelYear);
		salesOrderFif.setProdDiv(prodDiv);
		salesOrderFif.setProdQty(prodQty);
		salesOrderFif.setSalesDiv(salesDiv);
		salesOrderFif.setSalesKdLotNo(salesKdLotNo);
		salesOrderFif.setShipperId(shipperId);
		return salesOrderFif;
	}

	private SalesOrderFifId deriveID() {
		SalesOrderFifId salesOrderFifId = new SalesOrderFifId();
		salesOrderFifId.setFrmDevSeqCd(frmDevSeqCd);
		salesOrderFifId.setFrmExtClrCd(frmExtClrCd);
		salesOrderFifId.setFrmIntClrCd(frmIntClrCd);
		salesOrderFifId.setFrmModelCd(frmModelCd);
		salesOrderFifId.setFrmModelYearCd(frmModelYearCd);
		salesOrderFifId.setFrmOptionCd(frmOptionCd);
		salesOrderFifId.setFrmPlantCd(frmPlantCd);
		salesOrderFifId.setFrmTypeCd(frmTypeCd);
		salesOrderFifId.setOrderSeqNo(orderSeqNo);
		salesOrderFifId.setOrderType(orderType);
		salesOrderFifId.setSalesExtClrCd(salesExtClrCd);
		salesOrderFifId.setSalesIntClrCd(salesIntClrCd);
		salesOrderFifId.setSalesModelCd(salesModelCd);
		salesOrderFifId.setSalesOptionCd(salesOptionCd);
		salesOrderFifId.setSalesVehDestCd(salesVehDestCd);
		salesOrderFifId.setUniqueId(uniqueId);
		return salesOrderFifId;
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
}
