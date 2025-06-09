package com.honda.mfg.stamp.conveyor.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import javax.validation.constraints.NotNull;
import javax.persistence.ManyToOne;

@RooJavaBean
@RooToString
@RooEntity(identifierField = "id", identifierColumn = "ID", table = "VALID_DESTINATION_TBX")
/**
 * 
 * List of Destinations to validate data.
 *
 */
public class ValidDestination {

    @NotNull
    @ManyToOne
    private Stop stop;

    @NotNull
    @ManyToOne
    private Stop destination;
}
