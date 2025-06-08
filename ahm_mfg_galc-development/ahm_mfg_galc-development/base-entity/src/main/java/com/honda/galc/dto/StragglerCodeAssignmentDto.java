package com.honda.galc.dto;

import java.sql.Timestamp;

import com.honda.galc.util.ToStringUtil;

public class StragglerCodeAssignmentDto implements IDto {

	@DtoTag()
	private Timestamp identifiedTimestamp;

	@DtoTag()
	private String productId;

	@DtoTag()
	private String ppDelayedAt;
	
	@DtoTag()
	private String stragglerType;

	@DtoTag()
	private String kdLotNumber;

	@DtoTag()
	private String model;

	@DtoTag()
	private String type;

	@DtoTag()
	private String option;

	@DtoTag()
	private String color;

	@DtoTag()
	private String interiorColor;

	@DtoTag()
	private String code;

	@DtoTag()
	private String comments;

	public Timestamp getIdentifiedTimestamp() {
		return identifiedTimestamp;
	}
	public void setIdentifiedTimestamp(Timestamp identifiedTimestamp) {
		this.identifiedTimestamp = identifiedTimestamp;
	}

	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getPpDelayedAt() {
		return ppDelayedAt;
	}
	public void setPpDelayedAt(String ppDelayedAt) {
		this.ppDelayedAt = ppDelayedAt;
	}

	public String getKdLotNumber() {
		return kdLotNumber;
	}
	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}

	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}

	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}

	public String getInteriorColor() {
		return interiorColor;
	}
	public void setIntColor(String interiorColor) {
		this.interiorColor = interiorColor;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public String getStragglerType() {
		return stragglerType;
	}
	
	public void setStragglerType(String stragglerType) {
		this.stragglerType = stragglerType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((identifiedTimestamp == null) ? 0 : identifiedTimestamp.hashCode());
		result = prime * result + ((interiorColor == null) ? 0 : interiorColor.hashCode());
		result = prime * result + ((kdLotNumber == null) ? 0 : kdLotNumber.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((option == null) ? 0 : option.hashCode());
		result = prime * result + ((ppDelayedAt == null) ? 0 : ppDelayedAt.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
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
		StragglerCodeAssignmentDto other = (StragglerCodeAssignmentDto) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (identifiedTimestamp == null) {
			if (other.identifiedTimestamp != null)
				return false;
		} else if (!identifiedTimestamp.equals(other.identifiedTimestamp))
			return false;
		if (interiorColor == null) {
			if (other.interiorColor != null)
				return false;
		} else if (!interiorColor.equals(other.interiorColor))
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
		if (ppDelayedAt == null) {
			if (other.ppDelayedAt != null)
				return false;
		} else if (!ppDelayedAt.equals(other.ppDelayedAt))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	public String toString() {
		return ToStringUtil.generateToString(this);
	}
}
