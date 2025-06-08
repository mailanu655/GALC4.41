package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.PostLoad;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name="GAL118TBX")
public class CounterByModelGroup extends AuditEntry {
	@EmbeddedId
	private CounterByModelGroupId id;

	@Column(name="PASSING_COUNTER")
	private int passingCounter;

	@Column(name="PERIOD_LABEL")
	private String periodLabel;


	private static final long serialVersionUID = 1L;

	public CounterByModelGroup() {
		super();
	}

	public CounterByModelGroup(CounterByModelGroupId id) {
		super();
		this.id = id;
	}
	public CounterByModelGroupId getId() {
		return this.id;
	}

	public void setId(CounterByModelGroupId id) {
		this.id = id;
	}

	public int getPassingCounter() {
		return this.passingCounter;
	}

	public void setPassingCounter(int passingCounter) {
		this.passingCounter = passingCounter;
	}

	public String getPeriodLabel() {
		return this.periodLabel;
	}

	public void setPeriodLabel(String periodLabel) {
		this.periodLabel = periodLabel;
	}
	
	@PostLoad
    public void postLoad() {
		this.id.postLoad();
		this.periodLabel = StringUtils.trim(this.periodLabel);
		
	}

	@Override
	public String toString() {
		return toString(id.getModelCode(),id.getProcessPointId(),id.getShift(),id.getPeriod(),getPassingCounter());
	}
}
