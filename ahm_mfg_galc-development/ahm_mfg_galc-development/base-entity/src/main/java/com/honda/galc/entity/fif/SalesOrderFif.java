package com.honda.galc.entity.fif;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>SalesOrderFif.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> SalesOrderFif.java description </p>
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

/**
 * The persistent class for the SALES_ORDER_FIF_TBX database table.
 * 
 */
@Entity
@Table(name = "SALES_ORDER_FIF_TBX")
public class SalesOrderFif extends AuditEntry {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SalesOrderFifId id;

	@Column(name = "LOC_PROD_MONTH")
	private String locProdMonth;

	@Column(name = "SALES_DIV")
	private String salesDiv;

	@Column(name = "PROD_DIV")
	private String prodDiv;

	@Column(name = "PROD_QTY")
	private int prodQty;

	@Column(name = "MODEL_YEAR")
	private String modelYear;

	@Column(name = "BUILD_BY_DT")
	private String buildByDt;

	@Column(name = "SHIPPER_ID")
	private String shipperId;

	@Column(name = "SALES_KD_LOT_NO")
	private String salesKdLotNo;

	@Column(name = "EFCT_END_DT")
	private Date efctEndDt;

	@Column(name = "FIF_CODES")
	private String fifCodes;

	public SalesOrderFif() {
		super();
	}

	public SalesOrderFif(SalesOrderFifId id) {
		super();
		this.id = id;
	}

	public SalesOrderFifId getId() {
		return id;
	}

	public void setId(SalesOrderFifId id) {
		this.id = id;
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
	public String toString() {
		return toString(id.getUniqueId(), id.getSalesModelCd(),
				id.getSalesVehDestCd(), id.getSalesOptionCd(),
				id.getSalesExtClrCd(), id.getSalesIntClrCd(),
				id.getOrderType(), id.getOrderSeqNo(), id.getFrmPlantCd(),
				id.getFrmModelYearCd(), id.getFrmModelCd(),
				id.getFrmDevSeqCd(), id.getFrmTypeCd(), id.getFrmOptionCd(),
				id.getFrmExtClrCd(), id.getFrmIntClrCd(), getLocProdMonth(),
				getSalesDiv(), getProdDiv(), getProdQty(), getModelYear(),
				getBuildByDt(), getShipperId(), getSalesKdLotNo(),
				getEfctEndDt(), getFifCodes());
	}
}