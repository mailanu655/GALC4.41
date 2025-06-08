package com.honda.galc.entity.gts;

import java.util.StringTokenizer;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * 
 * <h3>GtsZone Class description</h3>
 * <p> GtsZone description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Nov 16, 2014
 *
 *
 */
@Entity
@Table(name="GTS_ZONE_TBX")
public class GtsZone extends AuditEntry {
	@EmbeddedId
	private GtsZoneId id;

	@Column(name="ZONE_DESCRIPTION")
	private String zoneDescription;

	private String lanes;

	private static final long serialVersionUID = 1L;

	public GtsZone() {
		super();
	}

	public GtsZoneId getId() {
		return this.id;
	}

	public void setId(GtsZoneId id) {
		this.id = id;
	}

	public String getZoneDescription() {
		return this.zoneDescription;
	}

	public void setZoneDescription(String zoneDescription) {
		this.zoneDescription = zoneDescription;
	}

	public String getLanes() {
		return this.lanes;
	}
	
	 public String[] getLaneNames(){
	        
	        if(lanes == null) return new String[0];
	        
	        StringTokenizer st = new StringTokenizer(lanes," ");
	        
	        String[] laneNames = new String[st.countTokens()];
	        for(int i=0; st.hasMoreTokens();i++){
	            laneNames[i] = st.nextToken();
	        }
	        return laneNames;
	        
	    }

	public void setLanes(String lanes) {
		this.lanes = lanes;
	}
	
	public String toString() {
		return toString(getId().getTrackingArea(),getId().getZone(),getLanes());
	}
	
}
