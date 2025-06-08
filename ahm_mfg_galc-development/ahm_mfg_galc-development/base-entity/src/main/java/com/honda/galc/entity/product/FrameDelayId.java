/**
 * 
 */
package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;
/**
 * 
 * @author Gangadhararao Gadde
 * @date May 22, 2014
 */


@Embeddable
public class FrameDelayId implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "CURRENT_VIN")
	private String currentVin;
	
	@Column(name = "PP_DELAYED_AT")
	private String ppDelayedAt;
	
	@Column(name = "PP_CURRENTLY_AT")
	private String ppCurrentlyAt;

	public String getCurrentVin() {
		return currentVin;
	}

	public void setCurrentVin(String currentVin) {
		this.currentVin = currentVin;
	}

	public String getPpDelayedAt() {
		return StringUtils.trimToEmpty(ppDelayedAt);
	}

	public void setPpDelayedAt(String ppDelayedAt) {
		this.ppDelayedAt = ppDelayedAt;
	}

	public String getPpCurrentlyAt() {
		return StringUtils.trimToEmpty(ppCurrentlyAt);
	}

	public void setPpCurrentlyAt(String ppCurrentlyAt) {
		this.ppCurrentlyAt = ppCurrentlyAt;
	}

	public FrameDelayId(String currentVin, String ppDelayedAt,
			String ppCurrentlyAt) {
		super();
		this.currentVin = currentVin;
		this.ppDelayedAt = ppDelayedAt;
		this.ppCurrentlyAt = ppCurrentlyAt;
	}

	public FrameDelayId() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((currentVin == null) ? 0 : currentVin.hashCode());
		result = prime * result
				+ ((ppCurrentlyAt == null) ? 0 : ppCurrentlyAt.hashCode());
		result = prime * result
				+ ((ppDelayedAt == null) ? 0 : ppDelayedAt.hashCode());
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
		FrameDelayId other = (FrameDelayId) obj;
		if (currentVin == null) {
			if (other.currentVin != null)
				return false;
		} else if (!currentVin.equals(other.currentVin))
			return false;
		if (ppCurrentlyAt == null) {
			if (other.ppCurrentlyAt != null)
				return false;
		} else if (!ppCurrentlyAt.equals(other.ppCurrentlyAt))
			return false;
		if (ppDelayedAt == null) {
			if (other.ppDelayedAt != null)
				return false;
		} else if (!ppDelayedAt.equals(other.ppDelayedAt))
			return false;
		return true;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getCurrentVin()).append(",");
		builder.append(getPpCurrentlyAt()).append(",");
		builder.append(getPpDelayedAt());
		return builder.toString();
	}
}

