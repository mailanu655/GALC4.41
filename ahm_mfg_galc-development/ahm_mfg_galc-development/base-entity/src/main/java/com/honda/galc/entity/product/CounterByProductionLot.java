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
@Table(name="GAL120TBX")
public class CounterByProductionLot extends AuditEntry {
	@EmbeddedId
	private CounterByProductionLotId id;

	@Column(name="PASSING_COUNTER")
	private int passingCounter;

	@Column(name="PERIOD_LABEL")
	private String periodLabel;


	private static final long serialVersionUID = 1L;

	public CounterByProductionLot() {
		super();
	}
	
	public CounterByProductionLot(CounterByProductionLotId id) {
		super();
		this.id = id;
	}
	
	public CounterByProductionLotId getId() {
		return this.id;
	}

	public void setId(CounterByProductionLotId id) {
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
	
//	@PostLoad
//    public void postLoad() {
//		this.id.postLoad();
//		this.periodLabel = StringUtils.trim(this.periodLabel);
	@Override
	public String toString() {
		return toString(id.getProcessPointId(),id.getProductionLot(),id.getShift(),id.getPeriod(),getPassingCounter());
	}
}
