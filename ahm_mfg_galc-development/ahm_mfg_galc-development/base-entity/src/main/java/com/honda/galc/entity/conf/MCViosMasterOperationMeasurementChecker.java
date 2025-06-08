package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.checkers.ReactionType;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dto.ExcelSheetColumn;
import com.honda.galc.entity.AuditEntry;

/**
 * <h3>MCViosMasterOperationMeasurementChecker Class description</h3>
 * <p>
 * Entity class for galadm.MC_VIOS_MASTER_OP_MEAS_CHECKER_TBX
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
 * @author Hemant Kumar<br>
 *        Nov 20, 2018
 */
@Entity
@Table(name="MC_VIOS_MASTER_OP_MEAS_CHECKER_TBX")
public class MCViosMasterOperationMeasurementChecker extends AuditEntry {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private MCViosMasterOperationMeasurementCheckerId id;
	
	@Column(name="CHECKER", nullable=false)
	@ExcelSheetColumn(name="Checker")
	private String checker;
	
	@Column(name="REACTION_TYPE")
	@ExcelSheetColumn(name="ReactionType")
    @Enumerated(EnumType.STRING)
	private ReactionType reactionType;
	
	
	@Column(name = "USER_ID")
    private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public MCViosMasterOperationMeasurementChecker() {
		super();
	}

	public MCViosMasterOperationMeasurementChecker(String viosPlatformId, String unitNo,
			 int measurementSeqNum, String checkPoint,
			int checkSeq, String checkName, String checker, ReactionType reactionType) {
		super();
		this.id = new MCViosMasterOperationMeasurementCheckerId(viosPlatformId, unitNo, measurementSeqNum, checkPoint, checkSeq, checkName);
		this.checker = checker;
		this.reactionType = reactionType;
	}

	public MCViosMasterOperationMeasurementCheckerId getId() {
		return id;
	}

	public void setId(MCViosMasterOperationMeasurementCheckerId id) {
		this.id = id;
	}

	public String getChecker() {
		return StringUtils.trimToEmpty(checker);
	}

	public void setChecker(String checker) {
		this.checker = checker;
	}

	public ReactionType getReactionType() {
		return reactionType;
	}
	
	public String getReactionTypeAsString() {
		return reactionType.name();
	}

	public void setReactionType(ReactionType reactionType) {
		this.reactionType = reactionType;
	}
	
	public String getOperationName() {
		return getId().getUnitNo()
				+Delimiter.UNDERSCORE
				+getId().getViosPlatformId();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((checker == null) ? 0 : checker.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((reactionType == null) ? 0 : reactionType.hashCode());
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
		MCViosMasterOperationMeasurementChecker other = (MCViosMasterOperationMeasurementChecker) obj;
		if (checker == null) {
			if (other.checker != null)
				return false;
		} else if (!checker.equals(other.checker))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (reactionType != other.reactionType)
			return false;
		return true;
	}
	
}
