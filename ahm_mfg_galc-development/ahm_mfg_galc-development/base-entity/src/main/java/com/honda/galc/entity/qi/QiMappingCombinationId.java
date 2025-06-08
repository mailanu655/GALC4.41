package com.honda.galc.entity.qi;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>QiMappingCombination Class description</h3>
 * <p>
 * QiMappingCombination
 * </p>
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
 * @author LnTInfotech<br>
 *        May 14, 2017
 * 
 */
@Embeddable
public class QiMappingCombinationId implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "REGIONAL_DEFECT_COMBINATION_ID", nullable=false)
	private Integer regionalDefectCombinationId;
	
	@Column(name = "OLD_COMBINATION", nullable=false)
	private String oldCombination;
	
	public QiMappingCombinationId() {
		super();
	}
	
	public QiMappingCombinationId(Integer regionalDefectCombinationId, String oldCombination) {
		super();
		this.regionalDefectCombinationId = regionalDefectCombinationId;
		this.oldCombination = oldCombination;
	}

	public Integer getRegionalDefectCombinationId() {
		return regionalDefectCombinationId;
	}

	public void setRegionalDefectCombinationId(Integer regionalDefectCombinationId) {
		this.regionalDefectCombinationId = regionalDefectCombinationId;
	}

	public String getOldCombination() {
		return StringUtils.trimToEmpty(oldCombination);
	}

	public void setOldCombination(String oldCombination) {
		this.oldCombination = oldCombination;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((oldCombination == null) ? 0 : oldCombination.hashCode());
		result = prime * result + ((regionalDefectCombinationId == null) ? 0 : regionalDefectCombinationId.hashCode());
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
		QiMappingCombinationId other = (QiMappingCombinationId) obj;
		if (oldCombination == null) {
			if (other.oldCombination != null)
				return false;
		} else if (!oldCombination.equals(other.oldCombination))
			return false;
		if (regionalDefectCombinationId == null) {
			if (other.regionalDefectCombinationId != null)
				return false;
		} else if (!regionalDefectCombinationId.equals(other.regionalDefectCombinationId))
			return false;
		return true;
	}
	
}
