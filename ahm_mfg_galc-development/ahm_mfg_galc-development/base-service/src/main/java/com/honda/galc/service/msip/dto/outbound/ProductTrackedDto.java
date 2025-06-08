package com.honda.galc.service.msip.dto.outbound;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.ProductType;
import com.honda.galc.entity.enumtype.ProcessPointType;
import com.honda.galc.util.ToStringUtil;

/**
 * @author Subu Kathiresan
 * @date Apr 26, 2017
 */
public class ProductTrackedDto extends BaseOutboundDto implements IMsipOutboundDto {

	private static final long serialVersionUID = 1L;
	
	private String planCode = "";
	private String lineNo = "";			// This is a numerical value 
	private String productSpecCode = "";
	private String productionLot = "";
	private String lotNumber = "";
	private String kdLotNumber = "";
	private String associateNo = "";
	private String approverNo = "";
	private String productId = "";
	private String processPointId = "";
	private String processPointName = "";
	private String lineName = "";
	private String lineId = "";
	private String divisionName = "";
	private String divisionId = "";
	private String plantName = "";
	private String siteName;
	private String buildSequence;
	private String engineSerialNo = "";
	private String missionSerialNo = "";
	private Character buildSequenceNotFixed;
	
	private int lotSize = -1;
	private int lotStatus = -1;
	private int afOnSequenceNumber = -1;
	
	private ProcessPointType processPointType = ProcessPointType.None;
	private ProductType productType = null;
	private Timestamp actualTimestamp = null;
	private Date productionDate = null;
	private String errorMsg;
	private Boolean isError;
	
	public String getVersion() {
		return version;
	}

	
	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Boolean getIsError() {
		return isError;
	}

	public void setIsError(Boolean isError) {
		this.isError = isError;
	}

	public ProductTrackedDto() {}
	
	public String getPlanCode() {
		return StringUtils.trimToEmpty(planCode);
	}
	
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public String getLineNo() {
		return StringUtils.trimToEmpty(lineNo);
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	
	public String getProductSpecCode() {
		return StringUtils.trimToEmpty(productSpecCode);
	}
	
	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
	
	public Date getProductionDate() {
		return productionDate;
	}
	
	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}
	
	public String getProductionLot() {
		return StringUtils.trimToEmpty(productionLot);
	}
	
	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}
	
	public String getLotNumber() {
		return StringUtils.trimToEmpty(lotNumber);
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public String getKdLotNumber() {
		return kdLotNumber;
	}

	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}

	public int getLotSize() {
		return lotSize;
	}

	public void setLotSize(int lotSize) {
		this.lotSize = lotSize;
	}

	public int getLotStatus() {
		return lotStatus;
	}

	public void setLotStatus(int lotStatus) {
		this.lotStatus = lotStatus;
	}

	public int getAfOnSequenceNumber() {
		return afOnSequenceNumber;
	}

	public void setAfOnSequenceNumber(int afOnSequenceNumber) {
		this.afOnSequenceNumber = afOnSequenceNumber;
	}
	
	public String getAssociateNo() {
		return StringUtils.trimToEmpty(associateNo);
	}
	
	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
	}
	
	public String getApproverNo() {
		return StringUtils.trimToEmpty(approverNo);
	}

	public void setApproverNo(String approverNo) {
		this.approverNo = approverNo;
	}
	
	public String getProductId() {
		return StringUtils.trimToEmpty(productId);
	}
	
	public void setProductId(String productId) {
		this.productId = productId;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	
	public String getProcessPointId() {
		return StringUtils.trimToEmpty(processPointId);
	}
	
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	
	public ProcessPointType getProcessPointType() {
		return processPointType;
	}
	
	public void setProcessPointType(ProcessPointType processPointType) {
		this.processPointType = processPointType;
	}
	
	public String getProcessPointName() {
		return StringUtils.trimToEmpty(processPointName);
	}
	
	public void setProcessPointName(String processPointName) {
		this.processPointName = processPointName;
	}
	
	public Timestamp getActualTimestamp() {
		return actualTimestamp;
	}
	
	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}
	
	public String getLineName() {
		return StringUtils.trimToEmpty(lineName);
	}
	
	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	
	public String getLineId() {
		return StringUtils.trimToEmpty(lineId);
	}
	
	public void setLineId(String lineId) {
		this.lineId = lineId;
	}
	
	public String getDivisionName() {
		return StringUtils.trimToEmpty(divisionName);
	}
	
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	
	public String getDivisionId() {
		return StringUtils.trimToEmpty(divisionId);
	}
	
	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}
	
	public String getPlantName() {
		return StringUtils.trimToEmpty(plantName);
	}
	
	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}
	
	public String getSiteName() {
		return StringUtils.trimToEmpty(siteName);
	}
	
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getBuildSequence() {
		return StringUtils.trimToEmpty(buildSequence);
	}

	public void setBuildSequence(String buildSequence) {
		this.buildSequence = buildSequence;
	}

	public Character getBuildSequenceNotFixed() {
		if (buildSequenceNotFixed == null) {
			return Character.MIN_VALUE;
		}
		return buildSequenceNotFixed;
	}

	public void setBuildSequenceNotFixed(Character buildSequenceNotFixed) {
		this.buildSequenceNotFixed = buildSequenceNotFixed;
	}

	public String getEngineSerialNo() {
		return engineSerialNo;
	}

	public void setEngineSerialNo(String engineSerialNo) {
		this.engineSerialNo = engineSerialNo;
	}

	public String getMissionSerialNo() {
		return missionSerialNo;
	}

	public void setMissionSerialNo(String missionSerialNo) {
		this.missionSerialNo = missionSerialNo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((planCode == null) ? 0 : planCode.hashCode());
		result = prime * result + ((lineNo == null) ? 0 : lineNo.hashCode());
		result = prime * result + ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result + ((productionLot == null) ? 0 : productionLot.hashCode());
		result = prime * result + ((lotNumber == null) ? 0 : lotNumber.hashCode());
		result = prime * result + ((kdLotNumber == null) ? 0 : kdLotNumber.hashCode());
		result = prime * result + ((associateNo == null) ? 0 : associateNo.hashCode());
		result = prime * result + ((approverNo == null) ? 0 : approverNo.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result + ((processPointName == null) ? 0 : processPointName.hashCode());
		result = prime * result + ((lineName == null) ? 0 : lineName.hashCode());
		result = prime * result + ((lineId == null) ? 0 : lineId.hashCode());
		result = prime * result + ((divisionName == null) ? 0 : divisionName.hashCode());
		result = prime * result + ((divisionId == null) ? 0 : divisionId.hashCode());
		result = prime * result + ((plantName == null) ? 0 : plantName.hashCode());
		result = prime * result + ((siteName == null) ? 0 : siteName.hashCode());
		result = prime * result + ((buildSequence == null) ? 0 : buildSequence.hashCode());		
		result = prime * result + ((engineSerialNo == null) ? 0 : engineSerialNo.hashCode());	
		result = prime * result + ((missionSerialNo == null) ? 0 : missionSerialNo.hashCode());	
		result = prime * result + ((buildSequenceNotFixed == null) ? 0 : buildSequenceNotFixed.hashCode());	
		result = prime * result + lotSize;	
		result = prime * result + lotStatus;	
		result = prime * result + afOnSequenceNumber;	
		result = prime * result + ((processPointType == null) ? 0 : processPointType.hashCode());	
		result = prime * result + ((productType == null) ? 0 : productType.hashCode());	
		result = prime * result + ((actualTimestamp == null) ? 0 : actualTimestamp.hashCode());	
		result = prime * result + ((productionDate == null) ? 0 : productionDate.hashCode());	
		result = prime * result + ((errorMsg == null) ? 0 : errorMsg.hashCode());
		result = prime * result + (isError ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductTrackedDto other = (ProductTrackedDto) obj;
		if (planCode == null) {
			if (other.planCode != null)
				return false;
		} else if (!planCode.equals(other.planCode))
			return false;
		if (productSpecCode  == null) {
			if (other.productSpecCode  != null)
				return false;
		} else if (!productSpecCode .equals(other.productSpecCode))
			return false;
		if (productionLot == null) {
			if (other.productionLot != null)
				return false;
		} else if (!productionLot.equals(other.productionLot))
			return false;
		if (lotNumber == null) {
			if (other.lotNumber != null)
				return false;
		} else if (!lotNumber.equals(other.lotNumber))
			return false;
		if (kdLotNumber == null) {
			if (other.kdLotNumber != null)
				return false;
		} else if (!kdLotNumber.equals(other.kdLotNumber))
			return false;
		if (associateNo == null) {
			if (other.associateNo != null)
				return false;
		} else if (!associateNo.equals(other.associateNo))
			return false;
		if (approverNo == null) {
			if (other.approverNo != null)
				return false;
		} else if (!approverNo.equals(other.approverNo))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		if (processPointName == null) {
			if (other.processPointName != null)
				return false;
		} else if (!processPointName.equals(other.processPointName))
			return false;
		if (lineName  == null) {
			if (other.lineName  != null)
				return false;
		} else if (!lineName .equals(other.lineName ))
			return false;
		if (lineNo == null) {
			if (other.lineNo != null)
				return false;
		} else if (!lineNo.equals(other.lineNo))
			return false;
		if (lineId  == null) {
			if (other.lineId  != null)
				return false;
		} else if (!lineId .equals(other.lineId))
			return false;
		if (divisionName == null) {
			if (other.divisionName != null)
				return false;
		} else if (!divisionName.equals(other.divisionName))
			return false;
		if (divisionId == null) {
			if (other.divisionId != null)
				return false;
		} else if (!divisionId.equals(other.divisionId))
			return false;
		if (plantName == null) {
			if (other.plantName != null)
				return false;
		} else if (!plantName.equals(other.plantName))
			return false;
		if (siteName == null) {
			if (other.siteName != null)
				return false;
		} else if (!siteName.equals(other.siteName))
			return false;
		if (buildSequence == null) {
			if (other.buildSequence != null)
				return false;
		} else if (!buildSequence.equals(other.buildSequence))
			return false;
		if (engineSerialNo == null) {
			if (other.engineSerialNo != null)
				return false;
		} else if (!engineSerialNo.equals(other.engineSerialNo))
			return false;
		if (missionSerialNo == null) {
			if (other.missionSerialNo != null)
				return false;
		} else if (!missionSerialNo.equals(other.missionSerialNo))
			return false;
		if (buildSequenceNotFixed == null) {
			if (other.buildSequenceNotFixed != null)
				return false;
		} else if (!buildSequenceNotFixed.equals(other.buildSequenceNotFixed))
			return false;
		if (other.lotSize != lotSize)
			return false;
		if (other.lotStatus != lotStatus)
			return false;
		if (other.afOnSequenceNumber != afOnSequenceNumber)
			return false;
		if (processPointType == null) {
			if (other.processPointType != null)
				return false;
		} else if (!processPointType.equals(other.processPointType))
			return false;
		if (productType == null) {
			if (other.productType != null)
				return false;
		} else if (!productType.equals(other.productType))
			return false;
		if (actualTimestamp == null) {
			if (other.actualTimestamp != null)
				return false;
		} else if (!actualTimestamp.equals(other.actualTimestamp))
			return false;
		if (productionDate == null) {
			if (other.productionDate != null)
				return false;
		} else if (!productionDate.equals(other.productionDate))
			return false;
		if (errorMsg == null) {
			if (other.errorMsg != null)
				return false;
		} else if (!errorMsg.equals(other.errorMsg))
			return false;
		if (!isError != other.isError)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}
