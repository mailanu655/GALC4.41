package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>MeasurementSpec Class description</h3>
 * <p> MeasurementSpec description </p>
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
 * @author Jeffray Huang<br>
 * May 14, 2010
 *
 */

@Entity
@Table(name="MEASUREMENT_SPEC_TBX")
public class MeasurementSpec extends AuditEntry {
	@EmbeddedId
	@Auditable(isPartOfPrimaryKey= true,sequence=1)
	private MeasurementSpecId id;

	@Column(name="MINIMUM_LIMIT")
	@Auditable(isPartOfPrimaryKey= false,sequence=2)
	private double minimumLimit;

	@Column(name="MAXIMUM_LIMIT")
	@Auditable(isPartOfPrimaryKey= false,sequence=3)
	private double maximumLimit;

	@Column(name="MAX_ATTEMPTS")
	@Auditable(isPartOfPrimaryKey= false,sequence=4)
	private int maxAttempts;
	
	@Transient
	private String measurementName;

	private static final long serialVersionUID = 1L;

	public MeasurementSpec() {
		super();
	}

	public MeasurementSpecId getId() {
		return this.id;
	}

	public void setId(MeasurementSpecId id) {
		this.id = id;
	}

	public double getMinimumLimit() {
		return this.minimumLimit;
	}

	public void setMinimumLimit(double minimumLimit) {
		this.minimumLimit = minimumLimit;
	}

	public double getMaximumLimit() {
		return this.maximumLimit;
	}

	public void setMaximumLimit(double maximumLimit) {
		this.maximumLimit = maximumLimit;
	}
	
	public double getAverageLimit() {
		
		return (this.minimumLimit + this.maximumLimit)/2;
	}

	public int getMaxAttempts() {
		return this.maxAttempts;
	}

	public void setMaxAttempts(int maxAttempts) {
		this.maxAttempts = maxAttempts;
	}
	
	public String getMeasurementName() {
		return measurementName;
	}

	public void setMeasurementName(String measurementName) {
		this.measurementName = measurementName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getId().getMeasurementSeqNum()).append(",");
		builder.append(getMaxAttempts()).append(",");
		builder.append(getMinimumLimit()).append(",");
		builder.append(getMaximumLimit()).append(",");
		return builder.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		MeasurementSpec other = (MeasurementSpec) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}



}
