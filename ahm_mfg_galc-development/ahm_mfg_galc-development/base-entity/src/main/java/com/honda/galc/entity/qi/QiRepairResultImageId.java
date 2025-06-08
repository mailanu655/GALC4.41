package com.honda.galc.entity.qi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * <h3>Class description</h3>
 * 
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * </TABLE>
 * @version 1.0
 * @author Dylan Yang
 * @see
 *
 */
@Embeddable
public class QiRepairResultImageId implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "REPAIR_ID", nullable=false)
	private long repairId;
	
	@Column(name = "IMAGE_URL")
	private String imageUrl;

	public QiRepairResultImageId() {
		super();
	}
	
	public QiRepairResultImageId(long repairId, String imageUrl) {
		this.repairId = repairId;
		this.imageUrl = imageUrl;
	}

	public long getRepairId() {
		return repairId;
	}

	public void setRepairId(long repairId) {
		this.repairId = repairId;
	}

	public String getImageUrl() {
		return StringUtils.trimToEmpty(imageUrl);
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) repairId;
		result = prime * result + ((imageUrl == null) ? 0 : imageUrl.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		QiRepairResultImageId other = (QiRepairResultImageId) obj;
		return repairId == other.repairId && StringUtils.equals(imageUrl, other.getImageUrl());
	}

	@Override
	public String toString() {
		return "QiRepairResultImageId [repairId=" + repairId + ", imageUrl=" + imageUrl + "]";
	}
}
