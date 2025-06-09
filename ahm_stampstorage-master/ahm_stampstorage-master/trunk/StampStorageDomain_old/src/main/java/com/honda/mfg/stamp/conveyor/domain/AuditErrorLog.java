package com.honda.mfg.stamp.conveyor.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import javax.persistence.Column;
import java.sql.Timestamp;

@RooJavaBean
@RooToString
@RooEntity(identifierField = "id", identifierColumn = "LOG_ID", table = "AUDIT_ERROR_LOG_TBX")
public class AuditErrorLog {

    @NotNull
    @Column(name = "NODE_ID")
    private String nodeId;

    @NotNull
    @Column(name = "SOURCE")
    private String source;

    @NotNull
    @Column(name = "SEVERITY")
    private Integer severity;

    @Column(name = "MESSAGE_TEXT")
    private String messageText;

    @Column(name = "AUDIT_ERROR_LOG_TSTP")
    private Timestamp logTimestamp;
}
