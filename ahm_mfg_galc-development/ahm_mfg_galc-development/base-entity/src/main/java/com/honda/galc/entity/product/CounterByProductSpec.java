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
@Table(name="GAL119TBX")
public class CounterByProductSpec extends AuditEntry {
	@EmbeddedId
	private CounterByProductSpecId id;

	@Column(name="PASSING_COUNTER")
	private int passingCounter;

	@Column(name="PERIOD_LABEL")
	private String periodLabel;


	private static final long serialVersionUID = 1L;

	public CounterByProductSpec() {
		super();
	}
	
	public CounterByProductSpec(CounterByProductSpecId id) {
		super();
		this.id = id;
	}

	public CounterByProductSpecId getId() {
		return this.id;
	}

	public void setId(CounterByProductSpecId id) {
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
		return toString(id.getProcessPointId(),id.getProductSpecCode(),id.getShift(),id.getPeriod(),getPassingCounter());
	}
}
