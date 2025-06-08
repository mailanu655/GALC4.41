package com.honda.galc.data;

import java.util.Date;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;

/**
 * @author Brandon Kroeger
 * @version 1.0 Create: 7/22/2015
 */

public class MCInstalledPartDetailDto implements IDto {
	
	private static final long serialVersionUID = 1L;

	@DtoTag()
	private String productId;
	
	@DtoTag()
	private String processPointId;
	
	@DtoTag()
	private String processPointName;
	
	@DtoTag()
	private String partDesc;
	
	@DtoTag()
	private String opDesc;
	
	@DtoTag()
	private String operationName;
	
	@DtoTag()
	private String partName;

	@DtoTag()
	private String partSerialNumber;
	
	@DtoTag()
	private String partMask;

	@DtoTag()
	private String installedPartReason;

	@DtoTag()
	private Integer installedPartStatus;
	
	@DtoTag()
	private Integer measurementCount;
	
	@DtoTag()
	private String partMark;
	
	@DtoTag()
	private String productSpecCode;
	
	@DtoTag()
	private long structureRev;
	
	@DtoTag()
	private String divisionId;
	
	@DtoTag()
	private Integer opRev;
	
	@DtoTag()
	private String opType;
	
	@DtoTag()
	private Integer pddaPlatformId;
	
	@DtoTag()
	private double measurementValue;
	
	@DtoTag()
	private Integer opMeasSeqNum;

	@DtoTag()
	private Integer measurementStatus;

	@DtoTag()
	private String partId;
	
	@DtoTag()
	private Integer partRev;

	@DtoTag()
	private String partType;
	
	@DtoTag()
	private Integer opCheck;
	
	@DtoTag()
	private Integer partCheck;
	
	@DtoTag()
	private Integer measCheck;
	
	@DtoTag()
	private String associateNo;
	
	@DtoTag()
	private Date actualTimestamp;
	
	public MCInstalledPartDetailDto(){
		
	}
	public MCInstalledPartDetailDto(String productId, String partName,
			String partSerialNumber, String installedPartReason,
			Integer installedPartStatus, Integer measurementCount,
			double measurementValue) {
		super();
		this.productId = productId;
		this.partName = partName;
		this.partSerialNumber = partSerialNumber;
		this.installedPartReason = installedPartReason;
		this.installedPartStatus = installedPartStatus;
		this.measurementCount = measurementCount;		
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
	public double getMeasurementValue() {
		return measurementValue;
	}

	/**
	 * @param measurementValue
	 *            the measurementValue to set
	 */
	public void setMeasurementValue(double measurementValue) {
		this.measurementValue = measurementValue;
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
		return partMask;
	}
	/**
	 * @param partSerialMask the partSerialMask to set
	 */
	public void setPartSerialMask(String partSerialMask) {
		this.partMask = partSerialMask;
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
	
	
	public String getProcessPointName() {
		return processPointName;
	}
	public void setProcessPointName(String processPointName) {
		this.processPointName = processPointName;
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
	
	public String getPartDescription() {
		return partDesc;
	}
	public void setPartDescription(String partDescription) {
		this.partDesc = partDescription;
	}
	public String getOperationDescription() {
		return opDesc;
	}
	public void setOperationDescription(String operationDescription) {
		this.opDesc = operationDescription;
	}
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public String getPartMark() {
		return partMark;
	}
	public void setPartMark(String partMark) {
		this.partMark = partMark;
	}
	public String getProductSpecCode() {
		return productSpecCode;
	}
	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
	public long getStructureRevision() {
		return structureRev;
	}
	public void setStructureRevision(long structureRevision) {
		this.structureRev = structureRevision;
	}
	public String getDivisionId() {
		return divisionId;
	}
	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}
	public Integer getOperationRevision() {
		return opRev;
	}
	public void setOperationRevision(Integer operationRevision) {
		this.opRev = operationRevision;
	}
	public Integer getPddaPlatformId() {
		return pddaPlatformId;
	}
	public void setPddaPlatformId(Integer pddaPlatformId) {
		this.pddaPlatformId = pddaPlatformId;
	}
	public Integer getMeasurementSequenceNumber() {
		return opMeasSeqNum;
	}
	public void setMeasurementSequenceNumber(Integer measurementSequenceNumber) {
		this.opMeasSeqNum = measurementSequenceNumber;
	}
	public Integer getPartRevision() {
		return partRev;
	}
	public void setPartRevision(Integer partRevision) {
		this.partRev = partRevision;
	}
	public String getPartType() {
		return partType;
	}
	public void setPartType(String partType) {
		this.partType = partType;
	}
	public Integer getOperationCheck() {
		return opCheck;
	}
	public void setOperationCheck(Integer operationCheck) {
		this.opCheck = operationCheck;
	}
	public Integer getPartCheck() {
		return partCheck;
	}
	public void setPartCheck(Integer partCheck) {
		this.partCheck = partCheck;
	}
	public Integer getMeasurementCheck() {
		return measCheck;
	}
	public void setMeasurementCheck(Integer measurementCheck) {
		this.measCheck = measurementCheck;
	}
	
	public String getAssociate() {
		return associateNo;
	}
	public void setAssociate(String associate) {
		this.associateNo = associate;
	}
	public Date getActualTimestamp() {
		return actualTimestamp;
	}
	public void setActualTimestamp(Date actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}
	public String getOpType() {
		return opType;
	}
	public void setOpType(String opType) {
		this.opType = opType;
	}
	
	@Override
	public String toString() {
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("\nproductId 					: "  +  productId		   			+ "\t");
		strbuf.append("processPointId				: "  +  processPointId	   	   		+ "\t");
		strbuf.append("processPointName				: "  +  processPointName   	   		+ "\t");
		strbuf.append("partDescription 				: "  +  partDesc		   			+ "\t");
		strbuf.append("operationDescription 		: "  +  opDesc		   			+ "\t");
		strbuf.append("partSerialNumber	      		: "  +  partSerialNumber	   		+ "\t");
		strbuf.append("installedPartReason	    	: "  +  installedPartReason	   		+ "\t");
		strbuf.append("installedPartStatus	    	: "  +  installedPartStatus	   		+ "\t");
		strbuf.append("measurementCount	      		: "  +  measurementCount	   		+ "\t");
		strbuf.append("partMark			      		: "  +  partMark			   		+ "\t");
		strbuf.append("productSpecCode	      		: "  +  productSpecCode		   		+ "\t");
		strbuf.append("structureRevision      		: "  +  structureRev			   	+ "\t");
		strbuf.append("divisionId		      		: "  +  divisionId			   		+ "\t");
		strbuf.append("operationName	      		: "  +  operationName		   		+ "\t");
		strbuf.append("operationRevision      		: "  +  opRev	   					+ "\t");
		strbuf.append("operationType	      		: "  +  opType	   					+ "\t");
		strbuf.append("pddaPlatformId	      		: "  +  pddaPlatformId		   		+ "\t");		
		strbuf.append("partName 					: "  +  partName		   			+ "\t");
		strbuf.append("measurementValue	      		: "  +  measurementValue	   		+ "\t");
		strbuf.append("measurementSequenceNumber    : "  +  opMeasSeqNum 				+ "\t");
		strbuf.append("measurementStatus	      	: "  +  measurementStatus	   		+ "\n");
		strbuf.append("partId						: "  +  partId		   	   			+ "\t");
		strbuf.append("partRevision					: "  +  partRev		   	   			+ "\t");
		strbuf.append("partType						: "  +  partType	   	   			+ "\t");
		strbuf.append("operationCheck				: "  +  opCheck		 	   			+ "\t");
		strbuf.append("partCheck					: "  +  partCheck	 	   			+ "\t");
		strbuf.append("measurementCheck				: "  +  measCheck		 	   		+ "\t");		
		strbuf.append("partSerialMask				: "  +  partMask		   	   		+ "\t");		
		strbuf.append("associate		      		: "  +  associateNo		   			+ "\t");
		strbuf.append("actualTimestamp				: "  +  actualTimestamp		   		+ "\t");		
		return strbuf.toString();
	}
}
