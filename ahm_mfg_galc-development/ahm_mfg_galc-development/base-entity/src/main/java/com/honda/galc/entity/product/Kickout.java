package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name= "KICKOUT_TBX")
public class Kickout extends AuditEntry {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "KICKOUT_ID", nullable = false)
	private Long kickoutId;

	@Column(name ="PRODUCT_ID")
	private String productId;

	@Column(name = "PRODUCT_TYPE")
	private String productType;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "KICKOUT_STATUS")
	private int kickoutStatus;

	@Column(name = "COMMENT")
	private String comment;

	@Column(name = "RELEASE_COMMENT")
	private String releaseComment;
	
	@Column(name = "KICKOUT_USER")
	private String kickoutUser;
	
	@Column(name = "RELEASE_USER")
	private String releaseUser;

	@Column(name = "APPROVER_NAME")
	private String approverName;

	public Kickout() {
		super();
	}

	public Long getId() {
		return getKickoutId();
	}

	public void setId(Long id) {
		this.kickoutId = id;
	}

	public Long getKickoutId() {
		return this.kickoutId;
	}

	public void setKickoutId(Long kickoutId) {
		this.kickoutId = kickoutId;
	}

	public String getProductId() {
		return StringUtils.trim(this.productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductType() {
		return StringUtils.trim(this.productType);
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getDescription() {
		return StringUtils.trim(this.description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getKickoutStatus() {
		return this.kickoutStatus;
	}

	public void setKickoutStatus(int kickoutStatus) {
		this.kickoutStatus = kickoutStatus;
	}

	public String getComment() {
		return StringUtils.trim(this.comment);
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getReleaseComment() {
		return StringUtils.trim(this.releaseComment);
	}

	public void setReleaseComment(String releaseComment) {
		this.releaseComment = releaseComment;
	}
	
	public String getKickoutUser() {
		return StringUtils.trim(this.kickoutUser);
	}
	
	public void setKickoutUser(String kickoutUser) {
		this.kickoutUser = kickoutUser;
	}
	
	public String getReleaseUser() {
		return StringUtils.trim(releaseUser);
	}
	
	public void setReleaseUser(String releaseUser) {
		this.releaseUser = releaseUser;
	}

	public String getApproverName() {
		return StringUtils.trim(this.approverName);
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((approverName == null) ? 0 : approverName.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((kickoutId == null) ? 0 : kickoutId.hashCode());
		result = prime * result + kickoutStatus;
		result = prime * result + ((kickoutUser == null) ? 0 : kickoutUser.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((productType == null) ? 0 : productType.hashCode());
		result = prime * result + ((releaseComment == null) ? 0 : releaseComment.hashCode());
		result = prime * result + ((releaseUser == null) ? 0 : releaseUser.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Kickout other = (Kickout) obj;
		if (approverName == null) {
			if (other.approverName != null)
				return false;
		} else if (!approverName.equals(other.approverName))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (kickoutId == null) {
			if (other.kickoutId != null)
				return false;
		} else if (!kickoutId.equals(other.kickoutId))
			return false;
		if (kickoutStatus != other.kickoutStatus)
			return false;
		if (kickoutUser == null) {
			if (other.kickoutUser != null)
				return false;
		} else if (!kickoutUser.equals(other.kickoutUser))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (productType == null) {
			if (other.productType != null)
				return false;
		} else if (!productType.equals(other.productType))
			return false;
		if (releaseComment == null) {
			if (other.releaseComment != null)
				return false;
		} else if (!releaseComment.equals(other.releaseComment))
			return false;
		if (releaseUser == null) {
			if (other.releaseUser != null)
				return false;
		} else if (!releaseUser.equals(other.releaseUser))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Kickout [kickoutId=" + kickoutId + ", productId=" + productId + ", productType=" + productType
				+ ", description=" + description + ", kickoutStatus=" + kickoutStatus + ", comment=" + comment
				+ ", releaseComment=" + releaseComment + ", kickoutUser=" + kickoutUser + ", releaseUser=" + releaseUser
				+ ", approverName=" + approverName + "]";
	}
}
