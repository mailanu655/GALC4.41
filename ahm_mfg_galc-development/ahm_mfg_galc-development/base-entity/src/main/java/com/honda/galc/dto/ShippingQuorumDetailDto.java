package com.honda.galc.dto;

import org.apache.commons.lang.StringUtils;

import com.honda.global.galc.bl.common.data.shipping.ShippingTrailerInfo.TrailerInfoStatus;


/**
 * 
 * 
 * <h3>ShippingQuorumDetailDto Class description</h3>
 * <p> ShippingQuorumDetailDto description </p>
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
 * Apr 7, 2015
 *
 *
 */
public class ShippingQuorumDetailDto implements IDto{
       
	private static final long serialVersionUID = 1L;

	@DtoTag()
	private String trailerNumber;
	
	@DtoTag()
	private int trailerId;
	
	@DtoTag()
	private int trailerRow;
	
	@DtoTag()
	private int quorumSeq;
	
	@DtoTag()
	private String kdLot;

	@DtoTag()
	private String ymto;
	
	@DtoTag()
	private String engineNumber;
	
	@DtoTag()
	private int status;
	
	public String getTrailerNumber() {
		return trailerNumber;
	}

	public void setTrailerNumber(String trailerNumber) {
		this.trailerNumber = trailerNumber;
	}

	public int getTrailerId() {
		return trailerId;
	}

	public void setTrailerId(int trailerId) {
		this.trailerId = trailerId;
	}

	public int getTrailerRow() {
		return trailerRow;
	}

	public void setTrailerRow(int trailerRow) {
		this.trailerRow = trailerRow;
	}

	public int getQuorumSeq() {
		return quorumSeq;
	}

	public void setQuorumSeq(int quorumSeq) {
		this.quorumSeq = quorumSeq;
	}

	public String getKdLot() {
		return StringUtils.trim(kdLot);
	}

	public void setKdLot(String kdLot) {
		this.kdLot = kdLot;
	}

	public String getYmto() {
		return StringUtils.trim(ymto);
	}

	public void setYmto(String ymto) {
		this.ymto = ymto;
	}

	public String getEngineNumber() {
		return StringUtils.trim(engineNumber);
	}

	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public TrailerInfoStatus getTrailerInfoStatus() {
		return TrailerInfoStatus.convert(status);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((engineNumber == null) ? 0 : engineNumber.hashCode());
		result = prime * result + ((kdLot == null) ? 0 : kdLot.hashCode());
		result = prime * result + quorumSeq;
		result = prime * result + status;
		result = prime * result + trailerId;
		result = prime * result
				+ ((trailerNumber == null) ? 0 : trailerNumber.hashCode());
		result = prime * result + trailerRow;
		result = prime * result + ((ymto == null) ? 0 : ymto.hashCode());
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
		ShippingQuorumDetailDto other = (ShippingQuorumDetailDto) obj;
		if (engineNumber == null) {
			if (other.engineNumber != null)
				return false;
		} else if (!engineNumber.equals(other.engineNumber))
			return false;
		if (kdLot == null) {
			if (other.kdLot != null)
				return false;
		} else if (!kdLot.equals(other.kdLot))
			return false;
		if (quorumSeq != other.quorumSeq)
			return false;
		if (status != other.status)
			return false;
		if (trailerId != other.trailerId)
			return false;
		if (trailerNumber == null) {
			if (other.trailerNumber != null)
				return false;
		} else if (!trailerNumber.equals(other.trailerNumber))
			return false;
		if (trailerRow != other.trailerRow)
			return false;
		if (ymto == null) {
			if (other.ymto != null)
				return false;
		} else if (!ymto.equals(other.ymto))
			return false;
		return true;
	}

	
}
