package com.honda.galc.entity.conf;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.ProcessPointType;
import com.honda.galc.entity.product.Feature;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * ProcessPoint represents plant floor process point in the plant floor layout
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
@Table(name = "GAL214TBX")
public class ProcessPoint extends AuditEntry {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PROCESS_POINT_ID")
    private String processPointId;

    @Column(name = "PROCESS_POINT_NAME")
    private String processPointName;

    @Column(name = "PROCESS_POINT_DESCRIPTION")
    private String processPointDescription;

    @Column(name = "PROCESS_POINT_TYPE")
    private int processPointTypeId;

    @Column(name = "SITE_NAME")
    private String siteName;

    @Column(name = "PLANT_NAME")
    private String plantName;

    @Column(name = "DIVISION_ID")
    private String divisionId;

    @Column(name = "DIVISION_NAME")
    private String divisionName;

    @Column(name = "LINE_ID")
    private String lineId;

    @Column(name = "LINE_NAME")
    private String lineName;

    @Column(name = "BACK_FILL_PROCESS_POINT_ID")
    private String backFillProcessPointId;

    @Column(name = "SEQUENCE_NUMBER")
    private int sequenceNumber;

    @Column(name = "TRACKING_POINT_FLAG")
    private short trackingPointFlag;

    @Column(name = "RECOVERY_POINT_FLAG")
    private short recoveryPointFlag;

    @Column(name = "PASSING_COUNT_FLAG")
    private short passingCountFlag;

	@Column(name = "CURRENT_PROD_LOT")
    private String currentProductionLot;
    
    @Column(name = "FEATURE_TYPE")
    private String featureType;
    
    @Column(name = "FEATURE_ID")
    private String featureId;
    
    @Column(name = "CURRENT_KD_LOT")
    private String currentKdLot;
    
    @OneToOne(fetch = FetchType.EAGER)
	@JoinColumns({
		@JoinColumn(name="FEATURE_ID", referencedColumnName="FEATURE_ID", insertable=false, updatable=false ),
		@JoinColumn(name="FEATURE_TYPE", referencedColumnName="FEATURE_TYPE", insertable=false, updatable=false )
		})
	private Feature feature;
        
	private List<ApplicationTask> applicationTasks;

    public ProcessPoint() {
        super();
    }

    public String getProcessPointId() {
        return StringUtils.trim(this.processPointId);
    }
    
    public String getId() {
    	return getProcessPointId();
    }

    public void setProcessPointId(String processPointId) {
        this.processPointId = processPointId;
    }

    public String getProcessPointName() {
        return StringUtils.trim(processPointName);
    }

    public void setProcessPointName(String processPointName) {
        this.processPointName = processPointName;
    }

    public String getProcessPointDescription() {
        return StringUtils.trim(processPointDescription);
    }

    public void setProcessPointDescription(String processPointDescription) {
        this.processPointDescription = processPointDescription;
    }

    public int getProcessPointTypeId() {
        return this.processPointTypeId;
    }

    public void setProcessPointTypeId(int processPointTypeId) {
        this.processPointTypeId = processPointTypeId;
    }

    public ProcessPointType getProcessPointType() {
        return ProcessPointType.getType(processPointTypeId);
    }

    public void setProcessPointType(ProcessPointType type) {
        this.processPointTypeId = type.getId();
    }

    public String getSiteName() {
        return StringUtils.trim(siteName);
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getPlantName() {
        return StringUtils.trim(plantName);
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getDivisionId() {
        return StringUtils.trim(divisionId);
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivisionName() {
        return StringUtils.trim(divisionName);
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getLineId() {
        return StringUtils.trim(lineId);
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getLineName() {
        return StringUtils.trim(lineName);
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getBackFillProcessPointId() {
        return StringUtils.trim(backFillProcessPointId);
    }

    public void setBackFillProcessPointId(String backFillProcessPointId) {
        this.backFillProcessPointId = backFillProcessPointId;
    }

    public int getSequenceNumber() {
        return this.sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public short getTrackingPointFlag() {
        return this.trackingPointFlag;
    }
    
    public boolean isTrackingPoint() {
    	return trackingPointFlag >= 1;
    }

    public void setTrackingPointFlag(short trackingPointFlag) {
        this.trackingPointFlag = trackingPointFlag;
    }

    public short getRecoveryPointFlag() {
        return this.recoveryPointFlag;
    }

    public void setRecoveryPointFlag(short recoveryPointFlag) {
        this.recoveryPointFlag = recoveryPointFlag;
    }

    public short getPassingCountFlag() {
        return this.passingCountFlag;
    }
    
    public boolean isPassingCount() {
    	return passingCountFlag >= 1;
    }

    public void setPassingCountFlag(short passingCountFlag) {
        this.passingCountFlag = passingCountFlag;
    }

    public String getCurrentProductionLot() {
        return StringUtils.trim(currentProductionLot);
    }

    public void setCurrentProductionLot(String currentProdLot) {
        this.currentProductionLot = currentProdLot;
    }

    public String getFeatureType() {
		return featureType;
	}

	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}

	public String getFeatureId() {
		return featureId;
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
    
	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}
	
    public String getDisplayName() {
        if(getProcessPointName() == null) return getProcessPointId().trim();
    	return getProcessPointName().trim() + " (" + getProcessPointId().trim() + ")";
    }

    public List<ApplicationTask> getApplicationTasks() {
        if(applicationTasks == null) applicationTasks = new ArrayList<ApplicationTask>();
        return applicationTasks;
    }

    public void setApplicationTasks(List<ApplicationTask> applicationTasks) {
        this.applicationTasks = applicationTasks;
    }

	@Override
	public String toString() {
		return toString(getProcessPointId(),getProcessPointType(),getPlantName(),
				getDivisionId(),getLineId(),getFeatureType(),getFeatureId());
	}

	public String getCurrentKdLot() {
		return StringUtils.trimToEmpty(currentKdLot);
	}

	public void setCurrentKdLot(String currentKdLot) {
		this.currentKdLot = currentKdLot;
	}
}
