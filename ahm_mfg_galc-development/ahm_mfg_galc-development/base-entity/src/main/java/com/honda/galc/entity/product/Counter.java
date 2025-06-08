package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name="GAL240TBX")
public class Counter extends AuditEntry {
	@EmbeddedId
	private CounterId id;

	@Column(name="PASSING_COUNTER")
	private int passingCounter;

	@Column(name="PERIOD_LABEL")
	private String periodLabel;


	private static final long serialVersionUID = 1L;

	public Counter() {
		super();
	}
	
	public Counter(CounterId id) {
		super();
		this.id = id;
	}

	public CounterId getId() {
		return this.id;
	}

	public void setId(CounterId id) {
		this.id = id;
	}

	public int getPassingCounter() {
		return this.passingCounter;
	}

	public void setPassingCounter(int passingCounter) {
		this.passingCounter = passingCounter;
	}

	public String getPeriodLabel() {
		return StringUtils.trim(this.periodLabel);
	}

	public void setPeriodLabel(String periodLabel) {
		this.periodLabel = periodLabel;
	}

	@Override
	public String toString() {
		return toString(id.getProcessPointId(),id.getShift(),id.getPeriod(),getPassingCounter());
	}
}
