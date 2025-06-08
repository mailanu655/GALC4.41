package com.honda.galc.entity.qics;

import com.honda.galc.entity.AuditEntry;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "GAL127TBX")
public class DefectTypeDescrition extends AuditEntry  {
    @EmbeddedId
    private DefectTypeDescritionId id;


    private static final long serialVersionUID = 1L;

    public DefectTypeDescrition() {
        super();
    }

    public DefectTypeDescritionId getId() {
        return this.id;
    }

    public void setPk(DefectTypeDescritionId id) {
        this.id = id;
    }

}
