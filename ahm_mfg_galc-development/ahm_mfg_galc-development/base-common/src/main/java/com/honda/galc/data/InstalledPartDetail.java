package com.honda.galc.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Sriram Shanmugavel
 * @version 1.0 Create: Oct 3, 2008 2:00:12 PM
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class InstalledPartDetail implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String productId;
	
	private String rulePartName;
	
	private String partName;

	private String partSerialNumber;

	private String installedPartReason;

	private Integer installedPartStatus;
	
	private Integer measurementCount;

	private Integer measurementAttempt;

	private BigDecimal measurementValue;

	private Integer measurrementSequenceNumber;
	
	private String processPointId;
	
	private String processPointName;
	
	private String partId;
	
	private String partSerialMask;
	
	private String windowLabel;
	
	private String associate;
	
	private Date actualTimestamp;
	
	private Integer partConfirmCheck;
	
	private Integer measurementStatus;
	
	private Integer repairCheck;
	
	
	public InstalledPartDetail(){
		
	}
	public InstalledPartDetail(String productId, String partName,
			String partSerialNumber, String installedPartReason,
			Integer installedPartStatus, Integer measurementCount, Integer measurementAttempt,
			BigDecimal measurementValue) {
		super();
		this.productId = productId;
		this.partName = partName;
		this.partSerialNumber = partSerialNumber;
		this.installedPartReason = installedPartReason;
		this.installedPartStatus = installedPartStatus;
		this.measurementCount = measurementCount;
		this.measurementAttempt = measurementAttempt;
		this.measurementValue = measurementValue;
	}

	/**
	 * @return the installedPartReason
	 */
	public String getInstalledPartReason() {
		return installedPartReason;
	}

	/**
	 * @param installedPartReason
	 *            the installedPartReason to set
	 */
	public void setInstalledPartReason(String installedPartReason) {
		this.installedPartReason = installedPartReason;
	}

	/**
	 * @return the installedPartStatus
	 */
	public Integer getInstalledPartStatus() {
		return installedPartStatus;
	}

	/**
	 * @param installedPartStatus the installedPartStatus to set
	 */
	public void setInstalledPartStatus(Integer installedPartStatus) {
		this.installedPartStatus = installedPartStatus;
	}

	/**
	 * @return the measurementAttempt
	 */
	public Integer getMeasurementAttempt() {
		return measurementAttempt;
	}

	/**
	 * @param measurementAttempt
	 *            the measurementAttempt to set
	 */
	public void setMeasurementAttempt(Integer measurementAttempt) {
		this.measurementAttempt = measurementAttempt;
	}

	/**
	 * @return the measurementCount
	 */
	public Integer getMeasurementCount() {
		return measurementCount;
	}

	/**
	 * @param measurementCount
	 *            the measurementCount to set
	 */
	public void setMeasurementCount(Integer measurementCount) {
		this.measurementCount = measurementCount;
	}

	/**
	 * @return the measurementValue
	 */
	public BigDecimal getMeasurementValue() {
		return measurementValue;
	}

	/**
	 * @param measurementValue
	 *            the measurementValue to set
	 */
	public void setMeasurementValue(BigDecimal measurementValue) {
		this.measurementValue = measurementValue;
	}

	/**
	 * @return the actualTimestamp
	 */
	public Date getActualTimestamp() {
		return actualTimestamp;
	}
	/**
	 * @param actualTimestamp the actualTimestamp to set
	 */
	public void setActualTimestamp(Date actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}
	/**
	 * @return the associate
	 */
	public String getAssociate() {
		return associate;
	}
	/**
	 * @param associate the associate to set
	 */
	public void setAssociate(String associate) {
		this.associate = associate;
	}
	/**
	 * @return the measurrementSequenceNumber
	 */
	public Integer getMeasurrementSequenceNumber() {
		return measurrementSequenceNumber;
	}
	/**
	 * @param measurrementSequenceNumber the measurrementSequenceNumber to set
	 */
	public void setMeasurrementSequenceNumber(Integer measurrementSequenceNumber) {
		this.measurrementSequenceNumber = measurrementSequenceNumber;
	}
	/**
	 * @return the partId
	 */
	public String getPartId() {
		return partId;
	}
	/**
	 * @param partId the partId to set
	 */
	public void setPartId(String partId) {
		this.partId = partId;
	}
	/**
	 * @return the partSerialMask
	 */
	public String getPartSerialMask() {
		return partSerialMask;
	}
	/**
	 * @param partSerialMask the partSerialMask to set
	 */
	public void setPartSerialMask(String partSerialMask) {
		this.partSerialMask = partSerialMask;
	}
	/**
	 * @return the processPointId
	 */
	public String getProcessPointId() {
		return processPointId;
	}
	/**
	 * @param processPointId the processPointId to set
	 */
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	/**
	 * @return the processPointName
	 */
	public String getProcessPointName() {
		return processPointName;
	}
	/**
	 * @param processPointName the processPointName to set
	 */
	public void setProcessPointName(String processPointName) {
		this.processPointName = processPointName;
	}
	/**
	 * @return the windowLabel
	 */
	public String getWindowLabel() {
		return windowLabel;
	}
	/**
	 * @param windowLabel the windowLabel to set
	 */
	public void setWindowLabel(String windowLabel) {
		this.windowLabel = windowLabel;
	}
	/**
	 * @return the partName
	 */
	public String getPartName() {
		return partName;
	}

	/**
	 * @param partName
	 *            the partName to set
	 */
	public void setPartName(String partName) {
		this.partName = partName;
	}

	/**
	 * @return the partSerialNumber
	 */
	public String getPartSerialNumber() {
		return partSerialNumber;
	}

	/**
	 * @param partSerialNumber
	 *            the partSerialNumber to set
	 */
	public void setPartSerialNumber(String partSerialNumber) {
		this.partSerialNumber = partSerialNumber;
	}

	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId
	 *            the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}
	/**
	 * @return the rulePartName
	 */
	public String getRulePartName() {
		return rulePartName;
	}
	/**
	 * @param rulePartName the rulePartName to set
	 */
	public void setRulePartName(String rulePartName) {
		this.rulePartName = rulePartName;
	}
	/**
	 * @return the partConfirmCheck
	 */
	public Integer getPartConfirmCheck() {
		return partConfirmCheck;
	}
	/**
	 * @param partConfirmCheck the partConfirmCheck to set
	 */
	public void setPartConfirmCheck(Integer partConfirmCheck) {
		this.partConfirmCheck = partConfirmCheck;
	}
	/**
	 * @return the measurementStatus
	 */
	public Integer getMeasurementStatus() {
		return measurementStatus;
	}
	/**
	 * @param measurementStatus the measurementStatus to set
	 */
	public void setMeasurementStatus(Integer measurementStatus) {
		this.measurementStatus = measurementStatus;
	}
	
	public Integer getRepairCheck() {
		return repairCheck;
	}
	public void setRepairCheck(Integer repairCheck) {
		this.repairCheck = repairCheck;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((rulePartName == null) ? 0 : rulePartName.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final InstalledPartDetail other = (InstalledPartDetail) obj;
		if (rulePartName == null) {
			if (other.rulePartName != null)
				return false;
		} else if (!rulePartName.equals(other.rulePartName))
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("\nproductId 					: "  +  productId		   			+ "\t");
		strbuf.append("rulePartName 				: "  +  rulePartName		   		+ "\t");
		strbuf.append("partName 					: "  +  partName		   			+ "\t");
		strbuf.append("partSerialNumber	      		: "  +  partSerialNumber	   		+ "\t");
		strbuf.append("installedPartReason	    	: "  +  installedPartReason	   		+ "\t");
		strbuf.append("installedPartStatus	    	: "  +  installedPartStatus	   		+ "\t");
		strbuf.append("measurementCount	      		: "  +  measurementCount	   		+ "\t");
		strbuf.append("measurementAttempt	    	: "  +  measurementAttempt	   		+ "\t");
		strbuf.append("measurementValue	      		: "  +  measurementValue	   		+ "\t");
		strbuf.append("measurrementSequenceNumber   : "  +  measurrementSequenceNumber 	+ "\t");
		strbuf.append("processPointId				: "  +  processPointId	   	   		+ "\t");
		strbuf.append("processPointName	      		: "  +  processPointName	   		+ "\t");
		strbuf.append("partId						: "  +  partId		   	   			+ "\t");
		strbuf.append("partSerialMask				: "  +  partSerialMask	   	   		+ "\t");
		strbuf.append("windowLabel		      		: "  +  windowLabel		   			+ "\t");
		strbuf.append("associate		      		: "  +  associate		   			+ "\t");
		strbuf.append("actualTimestamp				: "  +  actualTimestamp		   		+ "\t");
		strbuf.append("partConfirmCheck	      		: "  +  partConfirmCheck	   		+ "\t");
		strbuf.append("measurementStatus	      	: "  +  measurementStatus	   		+ "\n");
		strbuf.append("repairCheck	      		: "  +  repairCheck	   		+ "\t");
		return strbuf.toString();
	}
}
