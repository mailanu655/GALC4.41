package com.honda.galc.entity.conf;

import com.honda.galc.entity.AuditEntry;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Process points by zones representing areas of responsibility
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
@Entity
@Table(name = "DC_ZONE_TBX")
public class DCZone extends AuditEntry {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private DCZoneId id;
    
    public DCZone(String zoneId, String processPointId, boolean repairable) {
    	this.id = new DCZoneId(zoneId,processPointId, repairable);
    }
    
    public DCZone() {
        super();
    }

    public DCZoneId getId() {
        return this.id;
    }

    public void setId(DCZoneId id) {
        this.id = id;
    }
    
    public String getZoneId() {
    	return getId().getZoneId();
    }
    
    public String getProcessPointId(){
    	return getId().getProcessPointId();
    }
    
    public boolean getRepairable() {
    	return getId().getRepairable();
    }
    
    public DCZone clone() {
    	DCZone clone = new DCZone(getId().getZoneId(),getId().getProcessPointId(), getId().getRepairable());
     	return clone;
    }
    
	@Override
	public String toString() {
		return toString(getZoneId(), getProcessPointId(), getRepairable());
	}

}
