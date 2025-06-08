package com.honda.galc.entity.gts;

import java.awt.geom.Point2D;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.GtsCarrierDisplayType;
import com.honda.galc.entity.enumtype.GtsOrientation;
import com.honda.galc.entity.product.ProductSpec;
/**
 * 
 * 
 * <h3>GtsLaneQueue Class description</h3>
 * <p> GtsLaneQueue description </p>
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
 * Nov 16, 2014
 *
 *
 */
@Entity
@Table(name="GTS_LANE_CARRIER_TBX")
public class GtsLaneCarrier extends AuditEntry {
	
    //  discrepancy Status : true - there is potential discrpancy between physical and logical tracking
    // 0X0001 - Reader Discrepancy
    // 0X0002 - Duplicate Product IDs associated with same product id
    // 0X0004 - Photoeye Discrepancy - Empty Carrier or Carrier with Product
    
	public static int READER_DISCREPANCY = 0X0001;
    public static int DUPLICATE_DISCREPANCY = 0X0002;
    public static int PHOTOEYE_DISCREPANCY = 0X0004;
    
	@EmbeddedId
	private GtsLaneCarrierId id;

	@Column(name="LANE_CARRIER")
	private String laneCarrier;

	@Column(name="DISCREPANCY_STATUS")
	private int discrepancyStatus;
	
	@Transient
	private int position = 0;
    

	@Transient
	private GtsLane lane;
	
	@Transient
	private GtsProduct product;
	
	@Transient
	private GtsCarrier carrier;
	
	private static final long serialVersionUID = 1L;

	public GtsLaneCarrier() {
		super();
	}
	
	public GtsLaneCarrier(String areaName, String laneId) {
		id = new GtsLaneCarrierId();
		id.setTrackingArea(areaName);
		id.setLaneId(laneId);
	}
	
	public GtsLaneCarrier(GtsLaneCarrier lc) {
		this(lc.getId().getTrackingArea(),lc.getId().getLaneId());
		id.setLanePosition(lc.getId().getLanePosition());
		this.laneCarrier = lc.laneCarrier;
		this.discrepancyStatus = lc.discrepancyStatus;
		this.position = lc.position;
		this.lane = lc.lane;
	}

	public GtsLaneCarrierId getId() {
		return this.id;
	}

	public void setId(GtsLaneCarrierId id) {
		this.id = id;
	}

	public String getLaneCarrier() {
		return StringUtils.trim(this.laneCarrier);
	}

	public void setLaneCarrier(String laneCarrier) {
		this.laneCarrier = laneCarrier;
	}
	
	public String getCarrierId() {
		return getLaneCarrier();
	}
	
	public GtsCarrier getCarrier() {
		return carrier;
	}

	public void setCarrier(GtsCarrier carrier) {
		this.carrier = carrier;
	}

	public void setProduct(GtsProduct product) {
		this.product = product;
	}

	public int getDiscrepancyStatus() {
		return this.discrepancyStatus;
	}

	public void setDiscrepancyStatus(int discrepancyStatus) {
		this.discrepancyStatus = discrepancyStatus;
	}
	
	public String getDiscrepancyString() {
        
        String str = "";
        if(this.isDuplicateDiscrepancy()) str = "Duplicate Product Occured";
        if(this.isPhotoEyeDiscrepancy()){
            if (str.length() > 0) str += " + ";
            str += " Photoeye";
        }
        if(this.isReaderDiscrepancy()){
            if (str.length() > 0) str += " + ";
            str += "Reader Mismatch";
        }
        
        return str;

    }
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public GtsLane getLane() {
		return this.lane;
	}

	public void setLane(GtsLane lane) {
		this.lane = lane;
	}
	
	public Point2D.Double getPoint(){
	    return lane.getPoint(this);
	}
	
	public GtsOrientation getDirection(){
        return lane.getDirection(this);
    }
	
	public GtsOrientation getHorizontalDirection(){
        return lane.getHorizontalDirection(this);
    }
	
	public boolean isUnknownCarrier(){
		return GtsCarrier.UNKNOWN.equalsIgnoreCase(getCarrierId());
    }
	
    public boolean isEmptyCarrier(){
        return !isUnknownCarrier()&& getProductId() == null;
    }
    
    public String getProductId() {
    	return product == null ? null : product.getProductId();
    }
    
    public GtsProduct getProduct() {
    	return product;
    }
    
    public String getModelYear(){
    	return getProduct() == null ? "N/A" : getProduct().getModelYear();
    }
    
    public String getModelCode(){
    	return getProduct() == null ? "N/A" : getProduct().getModelCode();
    }
    
    public String getExtColorCode(){
    	return getProduct() == null ? "N/A" : getProduct().getExtColorCode();
    }
    
    public String getIntColorCode(){
    	return getProduct() == null ? "N/A" : getProduct().getIntColorCode();
    }
    
    public String getProductStatusString(){
    	return getProduct() == null ? "N/A" : getProduct().getProductStatus().name();
    }
 
    public String getDefectStatusString(){
    	GtsProduct product = getProduct();
        if(product == null) return "N/A";
        else if(product.getDefectStatus() == null) return ""; 
        else return product.getDefectStatus().name();
    }
    
    public String getInspectionStatusString(){
    	GtsProduct product = getProduct();
        if(product == null) return "N/A";
        else return product.getInspectionStatus().name();
    }
    
    public void setDuplicateDiscrepancy(boolean flag) {
        if(flag) this.discrepancyStatus |= DUPLICATE_DISCREPANCY;
        else this.discrepancyStatus ^= DUPLICATE_DISCREPANCY;
    }
    
    public boolean isDuplicateDiscrepancy() {
        return (this.discrepancyStatus & DUPLICATE_DISCREPANCY) == DUPLICATE_DISCREPANCY;
    }
    
    public void setPhotoEyeDiscrepancy(boolean flag) {
        if(flag) this.discrepancyStatus |= PHOTOEYE_DISCREPANCY;
        else this.discrepancyStatus ^= PHOTOEYE_DISCREPANCY;
    }
    
    public void setReaderDiscrepancy(boolean flag) {
        if(flag) this.discrepancyStatus |= READER_DISCREPANCY;
        else this.discrepancyStatus ^= READER_DISCREPANCY;
    }
    
    public boolean isPhotoEyeDiscrepancy() {
        return (this.discrepancyStatus & PHOTOEYE_DISCREPANCY) == PHOTOEYE_DISCREPANCY;
    }
    
    public boolean isReaderDiscrepancy() {
        return (this.discrepancyStatus & READER_DISCREPANCY) == READER_DISCREPANCY;
    }
    
    public String getDisplayText(GtsCarrierDisplayType type){
        
        switch(type){
            case CARRIER:
            	return getCarrierId();
            case PRODUCT_ID:
                return (product == null) ? "N/A" : product.getProductId();
            case SHORT_PRODUCT_ID:
                return getShortProdId();
            case PROD_LOT:
                return getProductionLot();
            case SHORT_PROD_LOT:
            	return getShortProductionLot();
            case YMTO:
                return getProductSpec();
            case YMTO_COLOR:
                return getColorCode();
        }
        
        return "";
    }
    
    public String getShortProdId(){
        
        if(getProduct() == null) return "N/A";
        else if(getProduct().getShortProdId() == null){
            return getProduct().getProductId(); 
        }else return getProduct().getShortProdId();
        
    }
    
    public String getProductionLot(){
    	return product == null ? "" : product.getLotNumber();
    }
    
    public String getShortProductionLot() {
    	return product == null ? "N/A" : product.getShortLotNumber();
     }
    
    public String getKdLot(){
    	return product == null ? "N/A" : product.getKdLotNumber();
    }
    
    public String getProductSpec(){
        GtsProduct product = getProduct();
        if(product != null) return product.getProductSpec();
        else return "N/A";
    }
    
    public String getColorCode() {
    	return ProductSpec.padExtColorCode(getExtColorCode()) + getIntColorCode();
    }
    
    public int getProductSeq() {
        GtsProduct product = getProduct();
        if(product == null) return 0;
        else return product.getProductSeq();
    }
    
    public void setProductSeq(int seq) {
    	GtsProduct product = getProduct();
        if(product != null) product.setProductSeq(seq);
    }
    
    public String getStatusText(){
        
        String str = carrier == null ? "" : carrier.getStatusText();
        GtsProduct product = getProduct();
        
        if(this.isDuplicateDiscrepancy() || this.isPhotoEyeDiscrepancy()) str += "?";

        if(product == null) return str;

        if(product.getProductSeq() != 0) str += product.getProductSeq();
        if(product.getDefectStatus() != null) str += product.getDefectStatus().getShortName();
        str += product.getProductStatus().getShortName();
        str += product.getInspectionStatus().getName();
        
        return str;
    }
    
    public boolean isVisible(){
        return lane.isCarrierVisible(this);
    }
    
    public GtsLaneCarrier copy() {
    	return new GtsLaneCarrier(this);
    }
	
	public String toString() {
		return toString(getId().getTrackingArea(),getId().getLaneId(),getId().getLanePosition(),getLaneCarrier(),getProductId(),getDiscrepancyStatus());
	}

}
