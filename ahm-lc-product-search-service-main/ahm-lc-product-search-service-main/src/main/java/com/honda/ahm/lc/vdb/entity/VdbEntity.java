package com.honda.ahm.lc.vdb.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * s *
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>VdbEntity</code> is ... .
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Hemant Rajput
 * @created Apr 22, 2021
 */
@MappedSuperclass
public class VdbEntity<K extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private K id;

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
        return id;
    }

    public void setId(K id) {
        this.id = id;
    }
}
