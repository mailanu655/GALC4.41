package com.honda.mfg.stamp.conveyor.domain;

import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.Column;
import javax.persistence.Enumerated;

@RooJavaBean
@RooToString
@RooEntity(identifierField = "id", identifierColumn = "DIE_ID", table = "DIE_TBX")
public class Die {
    private static final Logger LOG = LoggerFactory.getLogger(Die.class);

//    @NotNull
//    @Column(name = "DIE_NUMBER")
//    private Integer dieNumber;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PART_PRODUCTION_VOLUME")
    @Enumerated
    private PartProductionVolume partProductionVolume;

    @Column(name = "BPM_PART_NUMBER")
    private String bpmPartNumber;

    @Column(name = "IMAGE_FILE_NAME")
    private String imageFileName;

    @Column(name = "ACTIVE")
    private Boolean active;

    @Column(name = "TEXT_COLOR")
    private String textColor;

    @Column(name = "BACKGROUND_COLOR")
    private String backgroundColor;

    public Die() {
    }

    public Die(Long dieNumber, PartProductionVolume volume) {

        if (dieNumber != null) {
            this.setId(dieNumber);
        } else {
            this.setId(0L);
        }
        // this.dieNumber = dieNumber;
        this.partProductionVolume = volume;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Die) {
            Die lhs = this;
            Die rhs = (Die) obj;
            if (lhs.getId() == null) {
                return false;
            }
            return lhs.getId().equals(rhs.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        //LOG.info("hashCode() called.  hashcode: " + super.hashCode());
        return super.hashCode();    //To change body of overridden methods use File | Settings | File Templates.
    }

}
