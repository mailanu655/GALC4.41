package com.honda.galc.entity.product;

import java.io.Serializable;
import java.lang.String;
import java.sql.Timestamp;
import javax.persistence.*;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Fredrick Yessaian
 * @since Oct 19 2012
 * @version 0.1
 */

@Entity
@Table(name = "GAL328TBX")
public class KnuckleBarMeasurement extends AuditEntry implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private KnuckleBarMeasurementId id;

	@Column(name = "MEASUREMENT_VALUE")
	private double measurementValue;

	@Column(name = "MEASUREMENT_ANGLE")
	private double measurementAngle;

	@Column(name = "MEASUREMENT_STATUS")
	private int measurementStatus;

	@Column(name = "PROCESS_POINT_ID")
	private String processPointId;


	public KnuckleBarMeasurement() {
		super();
	}

	public double getMeasurementValue() {
		return this.measurementValue;
	}

	public void setMeasurementValue(double MEASUREMENT_VALUE) {
		this.measurementValue = MEASUREMENT_VALUE;
	}

	public double getMeasurementAngle() {
		return this.measurementAngle;
	}

	public void setMeasurementAngle(double MEASUREMENT_ANGLE) {
		this.measurementAngle = MEASUREMENT_ANGLE;
	}

	public int getMeasurementStatus() {
		return this.measurementStatus;
	}

	public void setMeasurementStatus(int MEASUREMENT_STATUS) {
		this.measurementStatus = MEASUREMENT_STATUS;
	}

	public String getProcessPointId() {
		return StringUtils.trim(this.processPointId);
	}

	public void setProcessPointId(String PROCESS_POINT_ID) {
		this.processPointId = PROCESS_POINT_ID;
	}



	public KnuckleBarMeasurementId getId() {
		return this.id;
	}

	public void setId(KnuckleBarMeasurementId id) {
		this.id = id;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		long temp;
		temp = Double.doubleToLongBits(measurementAngle);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + measurementStatus;
		temp = Double.doubleToLongBits(measurementValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((processPointId == null) ? 0 : processPointId.hashCode());
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
		KnuckleBarMeasurement other = (KnuckleBarMeasurement) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (Double.doubleToLongBits(measurementAngle) != Double.doubleToLongBits(other.measurementAngle))
			return false;
		if (measurementStatus != other.measurementStatus)
			return false;
		if (Double.doubleToLongBits(measurementValue) != Double.doubleToLongBits(other.measurementValue))
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\"").append(getId().getPartName()).append("\"");
		sb.append("\"").append(getId().getPartId()).append("\"");
		sb.append("\"").append(getId().getPartSerialNumber()).append("\"");
		sb.append("\"").append(getId().getMeasurementSequenceNumber()).append("\"");
		sb.append("\"").append(getMeasurementValue()).append("\"");
		sb.append("\"").append(getMeasurementStatus()).append("\"");
		sb.append("\"").append(getMeasurementAngle()).append("\"");
		sb.append("\"").append(getProcessPointId()).append("\"");
		
		return sb.toString();
	}

	@Override
	public String toString(Object... objects){
		return super.toString(id,measurementValue, measurementAngle, measurementStatus,processPointId);
	}

}
