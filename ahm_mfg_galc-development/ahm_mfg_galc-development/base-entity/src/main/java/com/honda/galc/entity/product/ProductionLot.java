package com.honda.galc.entity.product;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.PrintAttribute;
import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>ProductionLot Class description</h3>
 * <p> ProductionLot description </p>
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
@Entity
@Deprecated
@Table(name = "GAL217TBX")
public class ProductionLot extends AuditEntry{
    @Id
    @Column(name = "PRODUCTION_LOT")
    private String productionLot;

    @Column(name = "PLAN_CODE")
    private String planCode;

    @Column(name = "LINE_NO")
    private String lineNo;

    @Column(name = "PROCESS_LOCATION")
    private String processLocation;

    @Column(name = "WE_LINE_NO")
    private String weLineNo;

    @Column(name = "WE_PROCESS_LOCATION")
    private String weProcessLocation;

    @Column(name = "PA_LINE_NO")
    private String paLineNo;

    @Column(name = "PA_PROCESS_LOCATION")
    private String paProcessLocation;

    @Column(name = "PRODUCT_SPEC_CODE")
    private String productSpecCode;

    @Column(name = "KD_LOT_NUMBER")
    private String kdLotNumber;

    @Column(name = "START_PRODUCT_ID")
    private String startProductId;

    @Column(name = "LOT_SIZE")
    private int lotSize;

    @Column(name = "DEMAND_TYPE")
    private String demandType;

    @Column(name = "PLAN_OFF_DATE")
    private Date planOffDate;

    @Column(name = "PRODUCTION_DATE")
    private Date productionDate;

    @Column(name = "LOT_PASS_FLAG")
    private short lotPassFlag;

    @Column(name = "LOT_NUMBER")
    private String lotNumber;

    @Column(name = "PLANT_CODE")
    private String plantCode;

    @Column(name = "LOT_STATUS")
    private int lotStatus;

	@Column(name = "PROD_LOT_KD")
	private String prodLotKd;

    private static final long serialVersionUID = 1L;

    public ProductionLot() {
        super();
    }
    
    public ProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}

	@PrintAttribute
    public String getProductionLot() {
        return StringUtils.trim(this.productionLot);
    }
    
    public String getId() {
		return getProductionLot();
	}

    public void setProductionLot(String productionLot) {
        this.productionLot = StringUtils.trim(productionLot);
    }
    
    @PrintAttribute
    public String getPlanCode() {
        return this.planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = StringUtils.trim(planCode);
    }

    @PrintAttribute
    public String getLineNo() {
        return StringUtils.trim(this.lineNo);
    }

    public void setLineNo(String lineNo) {
        this.lineNo = StringUtils.trim(lineNo);
    }

    public String getProcessLocation() {
        return StringUtils.trim(this.processLocation);
    }

    public void setProcessLocation(String processLocation) {
        this.processLocation = StringUtils.trim(processLocation);
    }

    public String getWeLineNo() {
        return StringUtils.trim(this.weLineNo);
    }

    public void setWeLineNo(String weLineNo) {
        this.weLineNo = StringUtils.trim(weLineNo);
    }

    public String getWeProcessLocation() {
        return StringUtils.trim(this.weProcessLocation);
    }

    public void setWeProcessLocation(String weProcessLocation) {
        this.weProcessLocation = StringUtils.trim(weProcessLocation);
    }

    public String getPaLineNo() {
        return StringUtils.trim(this.paLineNo);
    }

    public void setPaLineNo(String paLineNo) {
        this.paLineNo = StringUtils.trim(paLineNo);
    }

    public String getPaProcessLocation() {
        return StringUtils.trim(this.paProcessLocation);
    }

    public void setPaProcessLocation(String paProcessLocation) {
        this.paProcessLocation = StringUtils.trim(paProcessLocation);
    }

    public String getProductSpecCode() {
        return StringUtils.trim(this.productSpecCode);
    }

    public void setProductSpecCode(String productSpecCode) {
        this.productSpecCode = StringUtils.trim(productSpecCode);
    }

    @PrintAttribute
    public String getKdLotNumber() {
        return StringUtils.trim(this.kdLotNumber);
    }

    public void setKdLotNumber(String kdLotNumber) {
        this.kdLotNumber = StringUtils.trim(kdLotNumber);
    }

    public String getStartProductId() {
        return StringUtils.trim(this.startProductId);
    }

    public void setStartProductId(String startProductId) {
        this.startProductId = StringUtils.trim(startProductId);
    }

    @PrintAttribute
    public int getLotSize() {
        return this.lotSize;
    }

    public void setLotSize(int lotSize) {
        this.lotSize = lotSize;
    }

    public String getDemandType() {
        return StringUtils.trim(this.demandType);
    }

    public void setDemandType(String demandType) {
        this.demandType = demandType;
    }

    public Date getPlanOffDate() {
        return this.planOffDate;
    }

    public void setPlanOffDate(Date planOffDate) {
        this.planOffDate = planOffDate;
    }

    public Date getProductionDate() {
        return this.productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public short getLotPassFlag() {
        return this.lotPassFlag;
    }

    public void setLotPassFlag(short lotPassFlag) {
        this.lotPassFlag = lotPassFlag;
    }

    @PrintAttribute
    public String getLotNumber() {
        return this.lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = StringUtils.trim(lotNumber);
    }

    @PrintAttribute
    public String getPlantCode() {
        return StringUtils.trim(this.plantCode);
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = StringUtils.trim(plantCode);
    }

    public int getLotStatus() {
        return this.lotStatus;
    }

    public void setLotStatus(int lotStatus) {
        this.lotStatus = lotStatus;
    }

    public boolean isInLot(String productId, int productNoPrefixLength) {
       return ProductionLotHelper.isInLot(productId, getStartProductId(), productNoPrefixLength, getLotSize());
       
    }

	@Override
	public String toString() {
		return toString(getProductionLot(),getProductSpecCode(),getProductionDate());
	}
    public String getProdLotKd() {
		return this.prodLotKd;
	}

    public void setProdLotKd(String prodLotKd) {
        this.prodLotKd = StringUtils.trim(prodLotKd);
    }
    
    public PreProductionLot derivePreProductionLot() {
    	PreProductionLot preProdLot = new PreProductionLot();
    	preProdLot.setProductionLot(getProductionLot());
    	preProdLot.setProcessLocation(getProcessLocation());
    	preProdLot.setPlantCode(getPlantCode());
    	preProdLot.setLineNo(getLineNo());
    	preProdLot.setLotNumber(getLotNumber());
    	preProdLot.setLotSize(getLotSize());
    	preProdLot.setProductSpecCode(getProductSpecCode());
    	preProdLot.setStartProductId(getStartProductId());
    	preProdLot.setHoldStatus(1);
    	preProdLot.setPlanCode(getPlanCode());
    	preProdLot.setKdLotNumber(getKdLotNumber());
    	return preProdLot;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((demandType == null) ? 0 : demandType.hashCode());
		result = prime * result + ((kdLotNumber == null) ? 0 : kdLotNumber.hashCode());
		result = prime * result + ((lineNo == null) ? 0 : lineNo.hashCode());
		result = prime * result + ((lotNumber == null) ? 0 : lotNumber.hashCode());
		result = prime * result + lotPassFlag;
		result = prime * result + lotSize;
		result = prime * result + lotStatus;
		result = prime * result + ((paLineNo == null) ? 0 : paLineNo.hashCode());
		result = prime * result + ((paProcessLocation == null) ? 0 : paProcessLocation.hashCode());
		result = prime * result + ((planCode == null) ? 0 : planCode.hashCode());
		result = prime * result + ((planOffDate == null) ? 0 : planOffDate.hashCode());
		result = prime * result + ((plantCode == null) ? 0 : plantCode.hashCode());
		result = prime * result + ((processLocation == null) ? 0 : processLocation.hashCode());
		result = prime * result + ((prodLotKd == null) ? 0 : prodLotKd.hashCode());
		result = prime * result + ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result + ((productionDate == null) ? 0 : productionDate.hashCode());
		result = prime * result + ((productionLot == null) ? 0 : productionLot.hashCode());
		result = prime * result + ((startProductId == null) ? 0 : startProductId.hashCode());
		result = prime * result + ((weLineNo == null) ? 0 : weLineNo.hashCode());
		result = prime * result + ((weProcessLocation == null) ? 0 : weProcessLocation.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof ProductionLot))
			return false;
		ProductionLot other = (ProductionLot) obj;
		if (productionLot == null) {
			if (other.productionLot != null)
				return false;
		} else if (!productionLot.equals(other.productionLot))
			return false;
		return true;
	}
		
}
