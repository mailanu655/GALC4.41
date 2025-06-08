package com.honda.galc.entity.conf;

import com.honda.galc.entity.AuditEntry;

import javax.persistence.*;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Division represents plant floor department<br>
 * It belongs to Plant and contains Lines
 * <p/>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>Aug 31, 2009</TD>
 * <TD>&nbsp;</TD>
 * <TD>Created</TD>
 * </TR>
 * </TABLE>
 */
@Entity
@Table(name = "GAL128TBX")
public class Division extends AuditEntry {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "DIVISION_ID")
    private String divisionId;

    @Column(name = "SITE_NAME")
    private String siteName;

    @Column(name = "PLANT_NAME")
    private String plantName;

    @Column(name = "DIVISION_NAME")
    private String divisionName;
    
    @Column(name = "DIVISION_DESCRIPTION")
    private String divisionDescription;

    @Column(name = "SEQUENCE_NUMBER")
    private int sequenceNumber;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "SITE_NAME", referencedColumnName = "SITE_NAME",
                    unique = true, nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "PLANT_NAME", referencedColumnName = "PLANT_NAME",
                    unique = true, nullable = false, insertable = false, updatable = false)
    })
    private Plant plant;
    
    private List<Terminal> terminals;
    
    private List<Device> devices;
    
    private List<Line> lines;
    
    private List<Zone> zones;
    
    @Transient
    private int deviceCount;
    
    @Transient
    private int terminalCount;
    
    public Division() {
        super();
    }

    public String getDivisionId() {
        return StringUtils.trim(this.divisionId);
    }
    
    public String getId() {
    	return getDivisionId();
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public String getSiteName() {
        return StringUtils.trim(this.siteName);
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getPlantName() {
        return StringUtils.trim(this.plantName);
    }

    public void setPlantName(String plantName) {
        this.plantName = StringUtils.trim(plantName);
    }

    public String getDivisionName() {
        return StringUtils.trim(this.divisionName);
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getDivisionDescription() {
        return StringUtils.trim(this.divisionDescription);
    }

    public void setDivisionDescription(String divisionDescription) {
        this.divisionDescription = divisionDescription;
    }

    public int getSequenceNumber() {
        return this.sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }
    
    public List<Terminal> getTerminals() {
        if (terminals == null) terminals = new ArrayList<Terminal>();
        return terminals;
    }

    public void setTerminals(List<Terminal> terminals) {
        this.terminals = terminals;
    }

    public List<Line> getLines() {
        if(lines == null) lines = new ArrayList<Line>();
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Zone> getZones() {
        if(zones == null) return new ArrayList<Zone>();
        return zones;
    }

    public void setZones(List<Zone> zones) {
        this.zones = zones;
    }
    
    public List<Device> getDevices() {
        if(devices == null) return new ArrayList<Device>();
        return devices;
    }
    public int getTerminalListCount() {
        return getTerminals().size();
    }
    
    public void setTerminalCount(int terminalCount) {
        this.terminalCount = terminalCount;
    }

    public int getTerminalCount() {
        return terminalCount;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public int getLineCount() {
        return getLines().size();
    }
    
    public int getZoneCount() {
        return getZones().size();
    }
    
    public int getDeviceListCount() {
        return getDevices().size();
    }
    
    public void setDeviceCount(int deviceCount) {
        this.deviceCount = deviceCount;
    }

    public int getDeviceCount() {
        return deviceCount;
    }

    public String toString(){
        return toString(getPlantName(),getDivisionId());
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((divisionId == null) ? 0 : divisionId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Division other = (Division) obj;
		if (divisionId == null) {
			if (other.divisionId != null)
				return false;
		} else if (!divisionId.equals(other.divisionId))
			return false;
		return true;
	}

}
