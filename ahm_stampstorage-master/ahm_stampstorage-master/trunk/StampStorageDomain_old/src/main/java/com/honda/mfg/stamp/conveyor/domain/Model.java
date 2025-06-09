package com.honda.mfg.stamp.conveyor.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooEntity(identifierField = "id", identifierColumn = "MODEL_ID", table = "MODEL_TBX")
public class Model {

    @NotNull
    @Column(name = "NAME")
    private String name;

    @NotNull
    @Column(name = "DESCRIPTION")
    private String description;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    private Die leftDie;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    private Die rightDie;


}
