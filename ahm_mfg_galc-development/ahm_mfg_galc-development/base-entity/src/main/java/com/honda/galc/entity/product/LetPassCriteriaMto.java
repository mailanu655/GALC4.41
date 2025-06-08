package com.honda.galc.entity.product;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


import com.honda.galc.entity.AuditEntry;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */

@Entity
@Table(name="GAL716TBX")
public class LetPassCriteriaMto extends AuditEntry {
	
	@EmbeddedId
	private LetPassCriteriaMtoId id;

	@Column(name="EXPIRATION_TIME")
	private Timestamp endTimestamp;

	@Column(name="CONDITION_WEIGHT")
	private Integer conditionalWeight;

	private static final long serialVersionUID = 1L;

	public LetPassCriteriaMto() {
		super();
	}

	public LetPassCriteriaMto(LetPassCriteriaMtoId id, Timestamp endTimestamp,
			Integer conditionalWeight) {
		super();
		this.id = id;
		this.endTimestamp = endTimestamp;
		this.conditionalWeight = conditionalWeight;
	}

	public void setId(LetPassCriteriaMtoId id) {
		this.id = id;
	}

	public LetPassCriteriaMtoId getId() {		
		return id;
	}

	public Timestamp getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(Timestamp endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	public Integer getConditionalWeight() {
		return conditionalWeight;
	}

	public void setConditionalWeight(Integer conditionalWeight) {
		this.conditionalWeight = conditionalWeight;
	}
	
	@Override
	public String toString() {
		return toString(getId().getModelYearCode(),getId().getModelCode(),getId().getModelTypeCode(),getId().getModelOptionCode(),getId().getEffectiveTime());
}

}
