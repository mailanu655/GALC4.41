package com.honda.ahm.lc.vdb.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class VinRangeVdbEntity<K extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private K product_id;

    // === overrides === //
    @Override
    public String toString() {
        String str = getClass().getSimpleName() + "{id:" + getId() + "}";
        return str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VdbEntity<?> other = (VdbEntity<?>) o;
        return Objects.equals(getId(), other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    // === get/set === //
    public K getId() {
        return product_id;
    }

    public void setId(K id) {
        this.product_id = id;
    }
}

