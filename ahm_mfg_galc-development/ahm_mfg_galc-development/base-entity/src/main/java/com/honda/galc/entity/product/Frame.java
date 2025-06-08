package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.PrintAttribute;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.enumtype.VinDisplayValue;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name="GAL143TBX")
public class Frame extends Product {

	@Id
	@Column(name = "PRODUCT_ID")
	private String productId;
	
	@Column(name="ENGINE_SERIAL_NO")
	private String engineSerialNo;

	@Column(name="STRAIGHT_SHIP_PERCENTAGE")
	private Double straightShipPercentage;

	@Column(name="KEY_NO")
	private String keyNo;

	@Column(name="SHORT_VIN")
	private String shortVin;

	@Column(name="ENGINE_STATUS")
	private Integer engineStatus;

	@Column(name="AF_ON_SEQUENCE_NUMBER")
	private Integer afOnSequenceNumber;

    @Column(name = "MISSION_SERIAL_NO")
	private String missionSerialNo;
    
    @Column(name = "ACTUAL_MISSION_TYPE")
    private String actualMissionType;
    
    @Column(name = "PURCHASE_CONTRACT_NUMBER")
    private String purchaseContractNumber;

	private static final long serialVersionUID = 1L;

	public Frame() {
		super();
	}
	
	public Frame(String productId) {
		this.productId = productId;
	}

	@Override
	@PrintAttribute
	public String getProductId() {
		return StringUtils.trim(productId);
	}

	@Override
	public void setProductId(String productId) {
		this.productId = productId;
	}
	@PrintAttribute
	public String getEngineSerialNo() {
		return StringUtils.trim(this.engineSerialNo);
	}

	public void setEngineSerialNo(String engineSerialNo) {
		this.engineSerialNo = engineSerialNo;
	}

	public Double getStraightShipPercentage() {
		return this.straightShipPercentage;
	}

	public void setStraightShipPercentage(Double straightShipPercentage) {
		this.straightShipPercentage = straightShipPercentage;
	}

	public String getKeyNo() {
		return StringUtils.trim(this.keyNo);
	}

	public void setKeyNo(String keyNo) {
		this.keyNo = keyNo;
	}
	
	@PrintAttribute
	public String getShortVin() {
		return StringUtils.trim(this.shortVin);
	}
	
	@Override
	public String getShortProductId() {
		return getShortVin();
	}

	public void setShortVin(String shortVin) {
		this.shortVin = shortVin;
	}

	public Integer getEngineStatus() {
		return this.engineStatus;
	}

	public void setEngineStatus(int engineStatus) {
		this.engineStatus = engineStatus;
	}
	
	public void setEngineStatus(boolean engineStatus) {
		setEngineStatus(engineStatus ? 1 : 0);
	}	
	
	@PrintAttribute
	public Integer getAfOnSequenceNumber() {
		return this.afOnSequenceNumber;
	}
	
	public Integer getShortAfOnSequenceNumber(int numberOfDigits) {
		Integer sequence = getAfOnSequenceNumber();
		if (sequence == null) {
			return sequence;
		}
		if (numberOfDigits < 0) {
			return 0;
		}
		int divider = Double.valueOf(Math.pow(10, numberOfDigits)).intValue();
		int shortSequence = sequence % divider;
		return shortSequence;
	}
	
	public Integer getLineRef(int numberOfDigits){
		return getShortAfOnSequenceNumber(numberOfDigits);
	}

	public void setAfOnSequenceNumber(Integer afOnSequenceNumber) {
		this.afOnSequenceNumber = afOnSequenceNumber;
	}
	
	@PrintAttribute
    public String getMissionSerialNo() {
        return StringUtils.trim(this.missionSerialNo);
    }

    public void setMissionSerialNo(String missionSerialNo) {
        this.missionSerialNo = missionSerialNo;
    }

    public String getActualMissionType() {
        return StringUtils.trim(this.actualMissionType);
    }

    public void setActualMissionType(String actualMissionType) {
        this.actualMissionType = actualMissionType;
    }
    
    
    public String getPurchaseContractNumber() {
		return StringUtils.trim(this.purchaseContractNumber);
	}

	public void setPurchaseContractNumber(String purchaseContractNumber) {
		this.purchaseContractNumber = purchaseContractNumber;
	}

	public String getDiplayValue(VinDisplayValue vdv, Integer numberOfDigits) {
    	switch(vdv) {
    	case PRODUCT_ID:
    		return getProductId();
    	case SHORT_VIN:
    		return getShortVin();
    	case LINE_REF:
    		return getLineRef(numberOfDigits).toString();
    	default:
    		return getProductId();
    	}
    }

	@Override
	public ProductType getProductType() {
		
		return ProductType.FRAME;
		
	}
	
		@Override
	public ProductNumberDef getProductNumberDef() {
		return ProductNumberDef.VIN;
	}
 
	@Override
	public String getOwnerProductId() {
		return null;
	}

}
