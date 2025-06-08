package com.honda.galc.entity.conf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.LineType;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Line represents plant floor line section<br>
 * It belongs to Division and consists of ProcessPoints
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
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name = "GAL195TBX")
public class Line extends AuditEntry {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "LINE_ID")
    private String lineId;

    @Column(name = "SITE_NAME")
    private String siteName;

    @Column(name = "PLANT_NAME")
    private String plantName;

    @Column(name = "DIVISION_ID")
    private String divisionId;

    @Column(name = "DIVISION_NAME")
    private String divisionName;

    @Column(name = "LINE_NAME")
    private String lineName;

    @Column(name = "LINE_TYPE")
    private int lineTypeId;

    @Column(name = "LINE_DESCRIPTION")
    private String lineDescription;

    @Column(name = "ENTRY_PROCESS_POINT_ID")
    private String entryProcessPointId;

    @Column(name = "LINE_SEQUENCE_NUMBER")
    private int lineSequenceNumber;

    @Column(name = "STD_INVENTORY")
    private int standardInventory;

    @Column(name = "MAXIMUM_INVENTORY")
    private int maximumInventory;

    @Column(name = "MINIMUM_INVENTORY")
    private int minimumInventory;
	
    @Transient
    private int processPointListCount = 0;
	
    @Transient
    private List<ProcessPoint> processPoints;
	
    @Transient
    private int currentInventory = -1;
    
    @Transient
    private Date currentInventoryAsOfDate;
    
	@OneToMany(targetEntity = PreviousLine.class, mappedBy = "line", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<PreviousLine> previousLines;

	public Line() {
        super();
    }

    public String getLineId() {
        return StringUtils.trim(this.lineId);
    }
    
    public String getId() {
    	return getLineId();
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
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
        this.plantName = plantName;
    }

    public String getDivisionId() {
        return this.divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivisionName() {
        return StringUtils.trim(this.divisionName);
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getLineName() {
        return StringUtils.trim(this.lineName);
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public int getLineTypeId() {
        return this.lineTypeId;
    }

    public void setLineTypeId(int lineTypeId) {
        this.lineTypeId = lineTypeId;
    }

    public LineType getLineType() {
        return LineType.getType(lineTypeId);
    }

    public void setLineType(LineType type) {
        this.lineTypeId = type.getId();
    }

    public String getLineDescription() {
        return StringUtils.trim(this.lineDescription);
    }

    public void setLineDescription(String lineDescription) {
        this.lineDescription = lineDescription;
    }

    public String getEntryProcessPointId() {
        return StringUtils.trim(this.entryProcessPointId);
    }

    public void setEntryProcessPointId(String entryProcessPointId) {
        this.entryProcessPointId = entryProcessPointId;
    }

    public int getLineSequenceNumber() {
        return this.lineSequenceNumber;
    }

    public void setLineSequenceNumber(int lineSequenceNumber) {
        this.lineSequenceNumber = lineSequenceNumber;
    }

    public int getStandardInventory() {
        return this.standardInventory;
    }

    public void setStandardInventory(int stdInventory) {
        this.standardInventory = stdInventory;
    }

    public int getMaximumInventory() {
        return this.maximumInventory;
    }

    public void setMaximumInventory(int maximumInventory) {
        this.maximumInventory = maximumInventory;
    }

    public int getMinimumInventory() {
        return this.minimumInventory;
    }

    public void setMinimumInventory(int minimumInventory) {
        this.minimumInventory = minimumInventory;
    }
    
    public int getCurrentInventory() {
        return this.currentInventory;
    }

    public void setCurrentInventory(int currInventory) {
        this.currentInventory = currInventory;
    }
    
    public Date getCurrentInventoryAsOfDate() {
        return this.currentInventoryAsOfDate;
    }

    public void setCurrentInventoryAsOfDate(Date currInventoryAsOf) {
        this.currentInventoryAsOfDate = currInventoryAsOf;
    }

    public List<ProcessPoint> getProcessPoints() {
        return processPoints;
    }

    public void setProcessPoints(List<ProcessPoint> processPoints) {
        this.processPoints = processPoints;
    }
	
	public int getProcessPointListCount() {
	    return processPointListCount;
	}
	
	public void setProcessPointListCount(int count) {
		this.processPointListCount = count;
	}
	
	public List<PreviousLine> getPreviousLines() {
		return previousLines;
	}
	
	public String getPreviousLinesIdsAsString() {
		if(previousLines.isEmpty()) return "";
		boolean isFirst = true;
		StringBuffer buffer = new StringBuffer();
		for(PreviousLine previousLine : previousLines) {
			if(isFirst) isFirst = false;
			else buffer.append(",");
			buffer.append(previousLine.getId().getPreviousLineId());
		}
		return buffer.toString();
	}
	
	public void setPreviousLines(String previousLineString) {
		previousLines = new ArrayList<PreviousLine>();
		if(StringUtils.isEmpty(previousLineString)) return;
		
		for (String previousLine : previousLineString.split(",")) {
			if (StringUtils.isNotBlank(previousLine)) {
				previousLines.add(new PreviousLine(lineId, StringUtils.trim(previousLine)));
			}
		}
	}
	
	@Override
	public String toString() {
		return toString(getDivisionId(),getLineId());
	}
}
