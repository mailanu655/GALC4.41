package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * DCZoneId is ID of DCZone object identify property<br>
 * by its zone id and process point id
 * <p/>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>D.Kouznetsov</TD>
 * <TD>Jul 25, 2017</TD>
 * <TD>&nbsp;</TD>
 * <TD>Created</TD>
 * </TR>
 * </TABLE>
 */
@Embeddable
public class DCZoneId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "ZONE_ID")
    private String zoneId;

    @Column(name = "PROCESS_POINT_ID")
    private String processPointId;
    
    @Column(name = "REPAIRABLE")
    private boolean repairable;
    
    public DCZoneId(String zoneId, String processPointId, boolean repairable) {
    	this.zoneId = zoneId;
    	this.processPointId = processPointId;
    	this.repairable = repairable;
    }
    
    public DCZoneId() {
        super();
    }

    public String getZoneId() {
        return StringUtils.trim(zoneId);
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getProcessPointId() {
        return StringUtils.trim(processPointId);
    }

    public void setProcessPointId(String processPointId) {
        this.processPointId = processPointId;
    }
    
    public void setRepairable(boolean repairable) {
    	this.repairable = repairable;
    }
    
    public boolean getRepairable() {
    	return repairable;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DCZoneId)) {
            return false;
        }
        DCZoneId other = (DCZoneId) o;
        return this.getZoneId().equals(other.getZoneId())
                && this.getProcessPointId().equals(other.getProcessPointId())
                && this.repairable == other.getRepairable();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.zoneId.hashCode();
        hash = hash * prime + this.processPointId.hashCode();
        return hash;
    }
    
    public String toString() {
        return zoneId + "," + processPointId + "," + repairable;
    }

}
