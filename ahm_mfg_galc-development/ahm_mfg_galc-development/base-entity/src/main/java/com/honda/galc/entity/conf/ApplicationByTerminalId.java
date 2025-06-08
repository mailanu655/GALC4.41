package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class ApplicationByTerminalId implements Serializable {
    @Column(name = "APPLICATION_ID")
    private String applicationId;

    @Column(name = "HOST_NAME")
    private String hostName;

    private static final long serialVersionUID = 1L;

    public ApplicationByTerminalId() {
        super();
    }

    public ApplicationByTerminalId(String applicationId, String hostName) {
        this.applicationId = applicationId;
    	this.hostName = hostName;
    }

    public String getApplicationId() {
        return StringUtils.trim(applicationId);
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getHostName() {
        return StringUtils.trim(hostName);
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ApplicationByTerminalId)) {
            return false;
        }
        ApplicationByTerminalId other = (ApplicationByTerminalId) o;
        return this.applicationId.equals(other.applicationId)
                && this.hostName.equals(other.hostName);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.applicationId.hashCode();
        hash = hash * prime + this.hostName.hashCode();
        return hash;
    }

}
