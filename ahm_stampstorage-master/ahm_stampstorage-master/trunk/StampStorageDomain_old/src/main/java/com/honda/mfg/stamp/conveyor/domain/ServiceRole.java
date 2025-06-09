package com.honda.mfg.stamp.conveyor.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import javax.persistence.Column;
import javax.validation.constraints.Max;

@RooJavaBean
@RooToString
@RooEntity(identifierField = "id", identifierColumn = "SERVICE_ID", table = "SERVICE_ROLE_TBX", finders = { "findServiceRolesByServiceNameEquals", "findServiceRolesByCurrentActiveNot", "findServiceRolesByIpEqualsAndPortAndCurrentActiveNot", "findServiceRolesByServiceNameEqualsAndDesignatedPrimaryNot", "findServiceRolesByServiceNameEqualsAndCurrentActiveNot", "findServiceRolesByIpEqualsAndPortAndDesignatedPrimaryNot", "findServiceRolesByServiceNameEqualsAndIpEqualsAndPort", "findServiceRolesByHostNameEquals", "findServiceRolesByIpEqualsAndPort" })
/**Defines the Role of a node and failovers*/
public class ServiceRole {

    @NotNull
    @Column(name = "PORT")
    @Max(65535L)
    private int port;

    @NotNull
    @Column(name = "IP_ADDR")
    private String ip;

    @NotNull
    @Column(name = "SERVICE_NAME")
    private String serviceName;

    @NotNull
    @Column(name = "DESIGNATED_PRI")
    private Boolean designatedPrimary;

    @Column(name = "FAILOVER_ORDER")
    private int failoverOrder;

    @NotNull
    @Column(name = "CURRENT_ACTIVE")
    private Boolean currentActive;

    @Column(name = "HOSTNAME")
    private String hostName;
        
}
