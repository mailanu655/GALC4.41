package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

@Embeddable
public class MeasurementAttemptId implements Serializable {
    @Column(name = "PRODUCT_ID")
    private String productId;

    @Column(name = "PART_NAME")
    private String partName;

    @Column(name = "MEASUREMENT_SEQUENCE_NUMBER")
    private int measurementSequenceNumber;
    
	@Column(name = "MEASUREMENT_ATTEMPT")
	private int measurementAttempt = 0;

    private static final long serialVersionUID = 1L;

    public MeasurementAttemptId() {
        super();
    }

    public MeasurementAttemptId(String productId, String partName,
			int measurementSequenceNumber, int measurementAttempt) {
		super();
		this.productId = productId;
		this.partName = partName;
		this.measurementSequenceNumber = measurementSequenceNumber;
		this.measurementAttempt = measurementAttempt;
	}

	public String getProductId() {
        return StringUtils.trim(this.productId);
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPartName() {
        return StringUtils.trim(this.partName);
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public int getMeasurementSequenceNumber() {
        return this.measurementSequenceNumber;
    }

    public void setMeasurementSequenceNumber(int measurementSequenceNumber) {
        this.measurementSequenceNumber = measurementSequenceNumber;
    }

    public int getMeasurementAttempt() {
		return measurementAttempt;
	}

	public void setMeasurementAttempt(int measurementAttempt) {
		this.measurementAttempt = measurementAttempt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + measurementAttempt;
		result = prime * result + measurementSequenceNumber;
		result = prime * result
				+ ((partName == null) ? 0 : partName.hashCode());
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
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
		MeasurementAttemptId other = (MeasurementAttemptId) obj;
		if (measurementAttempt != other.measurementAttempt)
			return false;
		if (measurementSequenceNumber != other.measurementSequenceNumber)
			return false;
		if (partName == null) {
			if (other.partName != null)
				return false;
		} else if (!partName.equals(other.partName))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		return true;
	}
}
