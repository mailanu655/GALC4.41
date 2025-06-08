package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Embeddable
public class ProductResultId implements Serializable {
    @Column(name = "PRODUCT_ID")
    private String productId;

    @Column(name = "PROCESS_POINT_ID")
    private String processPointId;

    @Column(name = "ACTUAL_TIMESTAMP")
    private Timestamp actualTimestamp;

    private static final long serialVersionUID = 1L;

    public ProductResultId() {
        super();
    }

    public String getProductId() {
        return StringUtils.trim(this.productId);
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProcessPointId() {
        return StringUtils.trim(this.processPointId);
    }

    public void setProcessPointId(String processPointId) {
        this.processPointId = processPointId;
    }

    public Timestamp getActualTimestamp() {
        return this.actualTimestamp;
    }

    public void setActualTimestamp(Timestamp actualTimestamp) {
        this.actualTimestamp = actualTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ProductResultId)) {
            return false;
        }
        ProductResultId other = (ProductResultId) o;
        return this.productId.equals(other.productId)
                && this.processPointId.equals(other.processPointId)
                && this.actualTimestamp.equals(other.actualTimestamp);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.productId.hashCode();
        hash = hash * prime + this.processPointId.hashCode();
        hash = hash * prime + this.actualTimestamp.hashCode();
        return hash;
    }

}
