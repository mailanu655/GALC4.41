/**
 * 
 */
package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * @author VF031824
 *
 */
@Entity
@Table(name = "LET_PART_CHECK_SPEC_TBX")
public class LetPartCheckSpec extends AuditEntry implements Serializable{

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private LetPartCheckSpecId id;

	@Column(name = "SEQUENCE_NUMBER")
	private int sequenceNumber;

	@Column(name = "PARAM_TYPE")
	private int paramType;


	public LetPartCheckSpec() {
		super();
	}

	public LetPartCheckSpecId getId() {
		return this.id;
	}

	public void setId(LetPartCheckSpecId id) {
		this.id = id;
	}

	public int getSequenceNumber() {
		return this.sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public int getParamType() {
		return this.paramType;
	}

	public void setParamType(int paramType) {
		this.paramType = paramType;
	}
}