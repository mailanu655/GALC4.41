package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

@Embeddable
public class MeasurementId implements Serializable {
    @Column(name = "PRODUCT_ID")
    private String productId;

    @Column(name = "PART_NAME")
    private String partName;

    @Column(name = "MEASUREMENT_SEQUENCE_NUMBER")
    private int measurementSequenceNumber;

    private static final long serialVersionUID = 1L;

    public MeasurementId() {
        super();
    }

    public MeasurementId(String productId, String partName,
			int measurementSequenceNumber) {
		super();
		this.productId = productId;
		this.partName = partName;
		this.measurementSequenceNumber = measurementSequenceNumber;
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

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MeasurementId)) {
            return false;
        }
        MeasurementId other = (MeasurementId) o;
        return this.productId.equals(other.productId)
                && this.partName.equals(other.partName)
                && (this.measurementSequenceNumber == other.measurementSequenceNumber);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.productId.hashCode();
        hash = hash * prime + this.partName.hashCode();
        hash = hash * prime + this.measurementSequenceNumber;
        return hash;
    }

}
