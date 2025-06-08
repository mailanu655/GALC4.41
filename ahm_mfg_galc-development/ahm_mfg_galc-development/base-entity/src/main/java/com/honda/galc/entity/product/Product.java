package com.honda.galc.entity.product;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.honda.galc.data.PrintAttribute;
import com.honda.galc.data.SubId;

/**
 * <h3>Product</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> Product description </p>
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
 * @author Paul Chou
 * Mar 25, 2010
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@MappedSuperclass()
public abstract class Product extends BaseProduct {
	private static final long serialVersionUID = 1L;

	public static final String SUB_ID_LEFT = SubId.LEFT.getId();
	public static final String SUB_ID_RIGHT = SubId.RIGHT.getId();
	public static final String SUB_ID_TOP = SubId.TOP.getId();
	public static final String SUB_ID_BOTTOM = SubId.BOTTOM.getId();
	public static final String SUB_ID_FRONT = SubId.FRONT.getId();
	public static final String SUB_ID_REAR = SubId.REAR.getId();
	public static final String SUB_ID_FRONT_LEFT = SubId.FRONT_LEFT.getId();
	public static final String SUB_ID_FRONT_RIGHT = SubId.FRONT_RIGHT.getId();
	public static final String SUB_ID_REAR_LEFT = SubId.REAR_LEFT.getId();
	public static final String SUB_ID_REAR_RIGHT = SubId.REAR_RIGHT.getId();
	
	@Column(name = "PRODUCTION_LOT")
	private String productionLot;

	@Column(name = "PRODUCT_SPEC_CODE")
	private String productSpecCode;
	
	@Column(name = "PLAN_OFF_DATE")
    private Date planOffDate;
	
	@Column(name = "KD_LOT_NUMBER")
    private String kdLotNumber;
	
	@Column(name = "PRODUCTION_DATE")
    private Date productionDate;
	
	@Column(name = "PRODUCT_START_DATE")
    private Date productStartDate;
	
	@Column(name = "ACTUAL_OFF_DATE")
    private Date actualOffDate;

    @Column(name = "AUTO_HOLD_STATUS")
    private short autoHoldStatus;
    
    @Transient
    private boolean isProductScrappable = false;
    
	public Product() {
		super();
	}
	
	public String getId(){
		return getProductId();
	}
	public abstract void setProductId(String productId);

	public String getShortProductId() {
		return getProductId();
	}
	
	@PrintAttribute
	public String getProductSpecCode() {
		return StringUtils.trim(this.productSpecCode);
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
	

 
	@PrintAttribute
	public String getProductionLot() {
        return StringUtils.trim(this.productionLot);
    }

    public void setProductionLot(String productionLot) {
        this.productionLot = productionLot;
    }
    
    public Date getPlanOffDate() {
        return this.planOffDate;
    }

    public void setPlanOffDate(Date planOffDate) {
        this.planOffDate = planOffDate;
    }
    
    @PrintAttribute
	public String getKdLotNumber() {
        return StringUtils.trim(this.kdLotNumber);
    }

    public void setKdLotNumber(String kdLotNumber) {
        this.kdLotNumber = kdLotNumber;
    }
    
    public String getLineNumber() {
    	return kdLotNumber.substring(5, 6);
    }
    
    public String getPlantNumber() {
    	return "P"+getLineNumber();
    }
    
    public Date getProductionDate() {
        return this.productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public String getFormattedProductionDate() {
    	return new SimpleDateFormat("MM/dd/yyyy").format(this.productionDate);
    }
    
    public Date getProductStartDate() {
        return this.productStartDate;
    }

    public void setProductStartDate(Date productStartDate) {
        this.productStartDate = productStartDate;
    }
    
    public Date getActualOffDate() {
        return this.actualOffDate;
    }

    public void setActualOffDate(Date actualOffDate) {
        this.actualOffDate = actualOffDate;
    }

    public short getAutoHoldStatus() {
        return this.autoHoldStatus;
    }
    @Override
    public int getHoldStatus() {
    	return getAutoHoldStatus();
    }

    public void setAutoHoldStatus(short autoHoldStatus) {
        this.autoHoldStatus = autoHoldStatus;
    }

    @PrintAttribute
    public String getLastPassingProcessPointId() {
        return StringUtils.trim(this.lastPassingProcessPointId);
    }

    public void setLastPassingProcessPointId(String lastPassingProcessPointId) {
        this.lastPassingProcessPointId = lastPassingProcessPointId;
    }
    
    @PrintAttribute
    public String getTrackingStatus() {
        return StringUtils.trim(this.trackingStatus);
    }

    public void setTrackingStatus(String trackingStatus) {
        this.trackingStatus = trackingStatus;
    }
    
    
    @PrintAttribute
    public String getModelTypeCode() {
    	return ProductSpec.extractModelTypeCode(getProductSpecCode());
    }
    
    @PrintAttribute
    public String getModelCode() {
    	return ProductSpec.extractModelCode(getProductSpecCode());
    }
    
    @PrintAttribute
    public String getModelExtColorCode() {
    	return ProductSpec.extractExtColorCode(getProductSpecCode());
    }
    
    @PrintAttribute
    public String getModelIntColorCode() {
    	return ProductSpec.extractIntColorCode(getProductSpecCode());
    }
    
    @PrintAttribute
    public String getYMTO(){
    	return ProductSpec.excludeToModelOptionCode(getProductSpecCode());
    }
    
    @PrintAttribute
    public String getNextLineSeq(){
    	return StringUtils.trim(this.trackingStatus);
    }
    
    public ProductionLot getProdLot() {
		return prodLot;
	}

	public void setProdLot(ProductionLot prodLot) {
		this.prodLot = prodLot;
	}

    public Integer getAfOnSequenceNumber() {
		return null;
	}

	public void setAfOnSequenceNumber(Integer afOnSequenceNumber) {
		
	}
    
    public Integer getDefectStatusValue(){
    	return null;
    }
    
     public void setDefectStatusValue(Integer defectStatus) {
    	
    }
     
     @Override
     public boolean isProductScrappable() {
    	 return this.isProductScrappable;
     }
    
       /**
     * get serial number part of the product id
     * @return
     */
    public int  getSerialNumber() {
    	String sequence = getProductNumberDef().getSequence(getProductId());
    	return NumberUtils.toInt(sequence, -1);
    }
    
    /**
     * get start serial number of the current lot
     * @return
     */
    public int getStartSerialNumber() {
    	if(prodLot == null) return -1;
    	String sequence = getProductNumberDef().getSequence(prodLot.getStartProductId());
    	return NumberUtils.toInt(sequence, -1);
    }
    
    /**
     * get lot position of the current lot like 3, 15 (of 30) , prodLot has to be set
     * @return
     */
    @PrintAttribute
    public int getLotPosition() {
    	return getSerialNumber() - getStartSerialNumber() + 1;
    }
    
    @PrintAttribute
    public String getLastLineSeq(){
    	return StringUtils.trim(this.trackingStatus);
    }
    
    public static String[] getSubIds() {
    	return new String[] {
    			"",
    			SUB_ID_LEFT,
    			SUB_ID_RIGHT,
    			SUB_ID_TOP,
    			SUB_ID_BOTTOM,
    			SUB_ID_FRONT,
    			SUB_ID_BACK,
    			SUB_ID_FRONT_LEFT,
    			SUB_ID_FRONT_RIGHT,
    			SUB_ID_BACK_LEFT,
    			SUB_ID_BACK_RIGHT};
    			
    }
 
}
