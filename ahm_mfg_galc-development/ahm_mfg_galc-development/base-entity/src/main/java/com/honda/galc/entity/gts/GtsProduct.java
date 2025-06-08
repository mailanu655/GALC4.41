package com.honda.galc.entity.gts;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AbstractEntity;
import com.honda.galc.entity.enumtype.GtsDefectStatus;
import com.honda.galc.entity.enumtype.GtsInspectionStatus;
import com.honda.galc.entity.enumtype.GtsProductStatus;

@Entity
@Table(name="GTS_PRODUCT_TBX")
public class GtsProduct extends AbstractEntity {
	@EmbeddedId
	private GtsProductId id;

	@Column(name="SHORT_PROD_ID")
	private String shortProdId;

	@Column(name="LOT_NUMBER")
	private String lotNumber;

	@Column(name="KD_LOT_NUMBER")
	private String kdLotNumber;

	@Column(name="MODEL_YEAR")
	private String modelYear;

	@Column(name="MODEL_CODE")
	private String modelCode;

	@Column(name="MODEL_TYPE")
	private String modelType;

	@Column(name="MODEL_OPTION")
	private String modelOption;

	@Column(name="INT_COLOR_CODE")
	private String intColorCode;

	@Column(name="EXT_COLOR_CODE")
	private String extColorCode;

	@Column(name="HOLD_STATUS")
	private int holdStatus;

	@Column(name="DEFECT_STATUS")
	private Integer defectStatus;

	@Column(name="INSPECTION_STATUS")
	private int inspectionStatus;

	@Column(name="PRODUCT_SEQ")
	private int productSeq;

	private static final long serialVersionUID = 1L;

	public GtsProduct() {
		super();
	}
	
	public GtsProduct(String trackingArea, String productId) {
		setId(new GtsProductId());
		getId().setTrackingArea(trackingArea);
		getId().setProductId(productId);
	}

	public GtsProductId getId() {
		return this.id;
	}

	public void setId(GtsProductId id) {
		this.id = id;
	}
	
	public String getProductId() {
		return id.getProductId();
	}

	public String getShortProdId() {
		return StringUtils.trim(this.shortProdId);
	}

	public void setShortProdId(String shortProdId) {
		this.shortProdId = shortProdId;
	}

	public String getLotNumber() {
		return StringUtils.trim(this.lotNumber);
	}
	
	public String getShortLotNumber() {
		return StringUtils.right(getLotNumber(), 10);
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public String getKdLotNumber() {
		return StringUtils.trim(this.kdLotNumber);
	}

	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}

	public String getModelYear() {
		return StringUtils.trim(this.modelYear);
	}

	public void setModelYear(String modelYear) {
		this.modelYear = modelYear;
	}

	public String getModelCode() {
		return StringUtils.trim(this.modelCode);
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getModelType() {
		return StringUtils.stripEnd(this.modelType, null);
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public String getModelOption() {
		return StringUtils.trim(this.modelOption);
	}

	public void setModelOption(String modelOption) {
		this.modelOption = modelOption;
	}

	public String getIntColorCode() {
		return StringUtils.trim(this.intColorCode);
	}

	public void setIntColorCode(String intColorCode) {
		this.intColorCode = intColorCode;
	}

	public String getExtColorCode() {
		return StringUtils.trim(this.extColorCode);
	}

	public void setExtColorCode(String extColorCode) {
		this.extColorCode = extColorCode;
	}

	public int getHoldStatusValue() {
		return this.holdStatus;
	}
	
	public GtsProductStatus getProductStatus() {
		return GtsProductStatus.getType(this.holdStatus);
	}

	public void setHoldStatus(int holdStatus) {
		this.holdStatus = holdStatus;
	}
	
	public void setProductStatus(GtsProductStatus productStatus) {
		setHoldStatus(productStatus.getId());
	}
	
	public int getDefectStatusValue() {
		return this.defectStatus;
	}

	public GtsDefectStatus getDefectStatus() {
		return this.defectStatus == null ? null : GtsDefectStatus.getType(this.defectStatus);
	}

	public void setDefectStatus(int defectStatus) {
		this.defectStatus = defectStatus;
	}

	public int getInspectionStatusValue() {
		return this.inspectionStatus;
	}
	
	public GtsInspectionStatus getInspectionStatus() {
		return GtsInspectionStatus.getType(this.inspectionStatus);
	}

	public void setInspectionStatus(int inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
	}

	public int getProductSeq() {
		return this.productSeq;
	}

	public void setProductSeq(int productSeq) {
		this.productSeq = productSeq;
	}
	
	public String getProductSpec(){
        return getModelYear()+getModelCode()+getModelType()+getModelOption();
    }
	
	public boolean isReleased(){
	       return getProductStatus() == GtsProductStatus.RELEASE;
	   }
	   
	   public boolean isOutstanding(){
	       return getDefectStatus() == GtsDefectStatus.OUTSTANDING;
	   }
	   
	   public boolean isScrapped(){
	       return getDefectStatus() == GtsDefectStatus.SCRAP;
	   }
	   
	   public boolean isShippable(){
	       return isReleased() && !isOutstanding() && !isScrapped();
	   }

}
