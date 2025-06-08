package com.honda.galc.service.msip.dto.inbound;

import com.honda.galc.util.ToStringUtil;

/*
 * 
 * @author Anusha Gopalan
 * @date June, 2017
*/
public class RckaeDto implements IMsipInboundDto {
	private static final long serialVersionUID = 1L;
	private String ein;
	private String kdLotNumber;
	private int lineNumber;
	private String model;
	private String option;
	private String rackId;
	private String trailerId;
	private String timestamp;
	private String type;

	public String getEin() {
		return ein;
	}

	public void setEin(String ein) {
		this.ein = ein;
	}

	public String getKdLotNumber() {
		return kdLotNumber;
	}

	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public String getRackId() {
		return rackId;
	}

	public void setRackId(String rackId) {
		this.rackId = rackId;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTrailerId(String trailerId) {
		this.trailerId = trailerId;
	}

	public String getTrailerId() {
		return trailerId;
	}	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + lineNumber;
		result = prime * result + ((ein == null) ? 0 : ein.hashCode());
		result = prime * result + ((kdLotNumber == null) ? 0 : kdLotNumber.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((option == null) ? 0 : option.hashCode());
		result = prime * result + ((rackId == null) ? 0 : rackId.hashCode());
		result = prime * result + ((trailerId == null) ? 0 : trailerId.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		RckaeDto other = (RckaeDto) obj;
		if (lineNumber != other.lineNumber) 
			return false;
		if (ein == null) {
			if (other.ein != null)
				return false;
		} else if (!ein.equals(other.ein))
			return false;
		if (kdLotNumber == null) {
			if (other.kdLotNumber != null)
				return false;
		} else if (!kdLotNumber.equals(other.kdLotNumber))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (option == null) {
			if (other.option != null)
				return false;
		} else if (!option.equals(other.option))
			return false;
		if (rackId == null) {
			if (other.rackId != null)
				return false;
		} else if (!rackId.equals(other.rackId))
			return false;
		if (trailerId == null) {
			if (other.trailerId != null)
				return false;
		} else if (!trailerId.equals(other.trailerId))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}
