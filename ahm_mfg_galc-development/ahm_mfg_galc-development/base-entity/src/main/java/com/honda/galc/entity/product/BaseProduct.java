package com.honda.galc.entity.product;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.PrintAttribute;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.enumtype.DefectStatus;

/**
 * 
 * <h3>BaseProduct Class description</h3>
 * <p> BaseProduct description </p>
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
 * May 9, 2011
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@MappedSuperclass()
public abstract class BaseProduct extends AuditEntry {
	private static final long serialVersionUID = 1L;
	
	public static final String SUB_ID_LEFT = "L";
	public static final String SUB_ID_RIGHT = "R";
	public static final String SUB_ID_TOP = "T";
	public static final String SUB_ID_BOTTOM = "B";
	public static final String SUB_ID_FRONT = "F";
	public static final String SUB_ID_BACK = "B";
	public static final String SUB_ID_FRONT_LEFT = "FL";
	public static final String SUB_ID_FRONT_RIGHT = "FR";
	public static final String SUB_ID_BACK_LEFT = "BL";
	public static final String SUB_ID_BACK_RIGHT = "BR";
	
	@Column(name = "LAST_PASSING_PROCESS_POINT_ID")
	protected String lastPassingProcessPointId;

	@Column(name = "TRACKING_STATUS")
	protected String trackingStatus;
	
	@Transient
	protected ProductionLot prodLot;
	
	@Transient
	private List<MCOperationRevision> operations = new ArrayList<MCOperationRevision>();
	
	@Transient
	protected String quantity;

	/**
	 * Cannot have primary key product id here, otherwise openJPA will query both Frame and Engine table
	 * @return
	 */
	@PrintAttribute
	public abstract String getProductId();
	
	public abstract String getOwnerProductId();
	
	/**
	 * product id definitions has to be implemented by sub classes
	 * @return
	 */
	public abstract ProductNumberDef getProductNumberDef();
	
	/**
	 * get product spec code 
	 * @return
	 */
	public abstract String getProductSpecCode();
	
	public abstract boolean isProductScrappable();
	
	//Getters & Setters
	public String getLastPassingProcessPointId() {
		return StringUtils.trim(this.lastPassingProcessPointId);
	}

	public void setLastPassingProcessPointId(String lastPassingProcessPointId) {
		this.lastPassingProcessPointId = lastPassingProcessPointId;
	}

	public String getTrackingStatus() {
		return StringUtils.trim(this.trackingStatus);
	}

	public void setTrackingStatus(String trackingStatus) {
		this.trackingStatus = trackingStatus;
	}

    public ProductionLot getProdLot() {
		return prodLot;
	}

	public void setProdLot(ProductionLot prodLot) {
		this.prodLot = prodLot;
	}

	public abstract Integer getDefectStatusValue();
	public abstract void setDefectStatusValue(Integer defectStatus);
	
	public abstract ProductType getProductType();
	
	public DefectStatus getDefectStatus() {
		
		Integer status = getDefectStatusValue();
		return status == null ? null : DefectStatus.getType(status);
		
	}
	
	public boolean isOutstandingStatus() {
		
		return  DefectStatus.OUTSTANDING == getDefectStatus();
		
	}
	
	public boolean isScrapStatus() {
		
		return  DefectStatus.SCRAP == getDefectStatus();
		
	}
	
	public boolean isPreheatScrapStatus() {
		
		return  DefectStatus.PREHEAT_SCRAP == getDefectStatus();
	}
	
	
	public boolean isRepairedStatus() {

		return  DefectStatus.REPAIRED == getDefectStatus();

	}
	
	public boolean isDirectPassStatus() {
		
		return  getDefectStatus() == null  || DefectStatus.DIRECT_PASS == getDefectStatus();
		
	}
	
	public boolean isDefectStatusUpdatable() {
		
		return getDefectStatus() == null || isOutstandingStatus() || isRepairedStatus();
		
	}
	
	public void setDefectStatus(DefectStatus defectStatus) {
		
		setDefectStatusValue(defectStatus.getId());
		
	}
	
	public String toString() {
		return toString(getProductId());
	}
	
	
	public abstract String getProductionLot();
	public void setProductionLot(String productionLot){};
	
	public  String getSubId(){
    	return null;
    }
	
	@PrintAttribute
	public String getModelCode() {
		return ProductSpec.extractModelCode(getProductSpecCode());
	}
	
	public abstract int getHoldStatus();
    
    public void setSubId(String subId){
    	
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

	public String getProductIdWithBoundary(String boundary) {
			StringBuilder sb = new StringBuilder();
			sb.append(boundary).append(getProductId()).append(boundary);
			return sb.toString();
		}

	public String getDunnage() {
		return null;
	}
	
	public int removeDunnage(String productId) {
		return 0;
	}

	public List<MCOperationRevision> getOperations() {
		return operations;
	}

	public void setOperations(List<MCOperationRevision> operations) {
		this.operations = operations;
	}

	public String getQuantity() {
		return StringUtils.trimToEmpty(quantity);
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
}
