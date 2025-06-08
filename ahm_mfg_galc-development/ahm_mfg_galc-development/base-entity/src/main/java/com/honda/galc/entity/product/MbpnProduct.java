package com.honda.galc.entity.product;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.PrintAttribute;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.enumtype.HoldStatus;

/**
 * * *
 * 
 * @version 1
 * @author Gangadhararao Gadde,Subu Kathiresan
 * @since Nov 01, 2012
 * 
 * */

@Entity
@Table(name = "MBPN_PRODUCT_TBX")
@AttributeOverrides({
		@AttributeOverride(name="lastPassingProcessPointId", column=@Column(name="LAST_PASSING_PROCESS_POINT"))       
	}			 
)
public class MbpnProduct extends BaseProduct {
	
	@Id
	@Column(name = "PRODUCT_ID")
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private String productId;

	@Column(name = "CURRENT_PRODUCT_SPEC_CODE")
	@Auditable(isPartOfPrimaryKey = false, sequence = 2)
	private String currentProductSpecCode;

	@Column(name = "CURRENT_ORDER_NO")
	@Auditable(isPartOfPrimaryKey = false, sequence = 3)
	private String currentOrderNo;

	@Column(name = "TRACKING_SEQ")
	@Auditable(isPartOfPrimaryKey = false, sequence = 4)
	private Double trackingSeq;

	@Column(name = "HOLD_STATUS_ID")
	@Auditable(isPartOfPrimaryKey = false, sequence = 5)
	private Integer holdStatusId;

	@Column(name = "PRODUCT_STATUS_ID")
	@Auditable(isPartOfPrimaryKey = false, sequence = 6)
	private Integer productStatusId;
	
	@Column(name = "CONTAINER_ID")
	@Auditable(isPartOfPrimaryKey = false, sequence = 7)
	private String containerId;
	
	@Column(name = "CONTAINER_POS")
	@Auditable(isPartOfPrimaryKey = false, sequence = 8)
	private Integer containerPos;
	
	@Column(name = "DEFECT_STATUS")
	@Auditable(isPartOfPrimaryKey = false, sequence = 9)
    private Integer defectStatus;
	
	@Column(name = "EXTERNAL_BUILD")
	@Auditable(isPartOfPrimaryKey = false, sequence = 10)
    private Integer externalBuild = 0;
	
	@OneToOne(targetEntity = Mbpn.class,fetch = FetchType.EAGER)
    @JoinColumn(name="CURRENT_PRODUCT_SPEC_CODE",referencedColumnName="PRODUCT_SPEC_CODE",updatable=false,insertable=false)
	private Mbpn mbpn;

	@Transient
	private String cellZone;
	@Transient
	private String product;
	@Transient
	private String sn;
	@Transient
	private Integer plan;
	@Transient
	private Integer actual;		// also as inventory in sub assembly inventory
	@Transient
	private boolean isProductScrappable = true;

	private static final long serialVersionUID = 1L;

	public MbpnProduct() {
		super();
	}
	
	public MbpnProduct(String productId) {
		this.productId = productId;
	}

	public String getProductId() {
		return StringUtils.trim(productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getCurrentProductSpecCode() {
		return StringUtils.trim(currentProductSpecCode);
	}

	public void setCurrentProductSpecCode(String currentProductSpecCode) {
		this.currentProductSpecCode = currentProductSpecCode;
	}

	public String getCurrentOrderNo() {
		return StringUtils.trim(currentOrderNo);
	}

	public void setCurrentOrderNo(String currentOrderNo) {
		this.currentOrderNo = currentOrderNo;
	}

	public Double getTrackingSeq() {
		return trackingSeq;
	}

	public void setTrackingSeq(Double trackingSeq) {
		this.trackingSeq = trackingSeq;
	}

	public Integer getHoldStatusId() {
		return holdStatusId;
	}

	public void setHoldStatusId(Integer holdStatusId) {
		this.holdStatusId = holdStatusId;
	}

	public Integer getProductStatusId() {
		return productStatusId;
	}

	public void setProductStatusId(Integer productStatusId) {
		this.productStatusId = productStatusId;
	}
	
	public String getContainerId() {
		return StringUtils.trim(containerId);
	}
	
	@Override
	public String getDunnage() {
		return getContainerId();
	}

	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

	public Integer getContainerPos() {
		return containerPos;
	}

	public void setContainerPos(Integer containerPos) {
		this.containerPos = containerPos;
	}
	
	public String getId() {
		return getProductId();
	}

	@Override
	public Integer getDefectStatusValue() {
		return defectStatus;
	}

	@Override
	public int getHoldStatus() {
		return getHoldStatusId() == null ? HoldStatus.NOT_ON_HOLD.getId() : getHoldStatusId();
	}

	@Override
	public String getOwnerProductId() {
		return null;
	}

	@Override
	public ProductNumberDef getProductNumberDef() {
		return ProductNumberDef.PLA;
	}

	@Override
	public String getProductSpecCode() {
		return currentProductSpecCode==null?"":getCurrentProductSpecCode();
	}

	@Override
	public ProductType getProductType() {
		return ProductType.MBPN;
	}

	@Override
	public String getProductionLot() {		
		return StringUtils.trim(currentOrderNo);
	}

	@Override
	public void setDefectStatusValue(Integer defectStatus) {
		this.defectStatus = defectStatus;
	}

	public String getCellZone() {
		return cellZone;
	}

	public void setCellZone(String cellZone) {
		this.cellZone = cellZone;
	}

	public String getProduct() {
		return StringUtils.trim(product);
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Integer getPlan() {
		return plan;
	}

	public void setPlan(Integer plan) {
		this.plan = plan;
	}

	public Integer getActual() {
		return actual;
	}

	public void setActual(Integer actual) {
		this.actual = actual;
	}
	
	public Mbpn getMbpn() {
		return mbpn;
	}

	public void setMbpn(Mbpn mbpn) {
		this.mbpn = mbpn;
	}
	
	@PrintAttribute
	public String getModelCode() {
		return StringUtils.trim(StringUtils.substring(getCurrentProductSpecCode(),5,8));
	}
	
	public void setProductionLot(String productionLot){
		this.currentOrderNo = productionLot;
	}

	public Integer getExternalBuild() {
		return externalBuild;
	}

	public void setExternalBuild(Integer externalBuild) {
		this.externalBuild = externalBuild;
	}
	
	@Override
	public boolean isProductScrappable() {
		return this.isProductScrappable;
	}
}
