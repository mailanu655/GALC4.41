package com.honda.ahm.lc.vdb.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;
/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>TrackingStatusDetails</code> is view class for TrackingStatus.
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Hemant Rajput
 * @created Apr 22, 2021
 */

@Entity
@Immutable
@Subselect(value="select " + 
		"c.DIVISION_NAME as division_name, " + 
		"b.LINE_NAME as line_name, " + 
		"a.PROCESS_POINT_NAME as process_point_name, " +
		"c.SEQUENCE_NUMBER as div_seq_no, " +
		"b.LINE_SEQUENCE_NUMBER as line_seq_no, " +
		"a.PASSING_COUNT_FLAG as passing_count_flag, " +
		"p.PLANT_NAME as plant_name, " +
		"a.SEQUENCE_NUMBER as pp_seq_no, " + 
		"a.TRACKING_POINT_FLAG as tracking_point_flag " + 
		"from " + 
		"GAL214TBX a " + 
		"join GAL195TBX b on a.LINE_ID = b.LINE_ID " + 
		"join GAL128TBX c on b.DIVISION_ID = c.DIVISION_ID " + 
		"join GAL211TBX p on c.PLANT_NAME = p.PLANT_NAME " +
		"order by c.SEQUENCE_NUMBER, b.LINE_SEQUENCE_NUMBER, a.SEQUENCE_NUMBER")
public class TrackingStatusDetails implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private TrackingStatusDetailsId id;
	
	@Column(name = "division_name", insertable = false, updatable = false)
	private String divisionName;
	
	@Column(name = "line_name", insertable = false, updatable = false)
	private String lineName;
	
	@Column(name = "process_point_name", insertable = false, updatable = false)
	private String processPointName;
	
	@Column(name = "div_seq_no")
	private Integer divSeqNo;
	
	@Column(name = "line_seq_no")
	private Integer lineSeqNo;
	
	@Column(name = "passing_count_flag")
	private Short passingCountFlag;
	
	@Column(name = "plant_name")
	private String plantName;
	
	@Column(name = "pp_seq_no")
	private Integer ppSeqNo;
	
	@Column(name = "tracking_point_flag")
	private Short trackingPointFlag;
	
    public TrackingStatusDetails() {
		super();
	}

	@Override
	public String toString() {
		String str = getClass().getSimpleName() + "{";
		str = str + "id: " + getId();
		str = str + "divSeqNo: " + getDivSeqNo();
		str = str + "lineSeqNo: " + getLineSeqNo();
		str = str + "passingCountFlag: " + getPassingCountFlag();
		str = str + "plantName: " + getPlantName();
		str = str + "ppSeqNo: " + getPpSeqNo();
		str = str + "trackingPointFlag: " + getTrackingPointFlag();
        str = str + "}";
        return str;
	}

	public TrackingStatusDetailsId getId() {
		return id;
	}

	public void setId(TrackingStatusDetailsId id) {
		this.id = id;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getLineName() {
		return StringUtils.trimToEmpty(lineName);
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getProcessPointName() {
		return processPointName;
	}

	public void setProcessPointName(String processPointName) {
		this.processPointName = processPointName;
	}

	public Integer getDivSeqNo() {
		return divSeqNo;
	}

	public void setDivSeqNo(Integer divSeqNo) {
		this.divSeqNo = divSeqNo;
	}

	public Integer getLineSeqNo() {
		return lineSeqNo;
	}

	public void setLineSeqNo(Integer lineSeqNo) {
		this.lineSeqNo = lineSeqNo;
	}

	public Short getPassingCountFlag() {
		return passingCountFlag;
	}

	public void setPassingCountFlag(Short passingCountFlag) {
		this.passingCountFlag = passingCountFlag;
	}

	public Integer getPpSeqNo() {
		return ppSeqNo;
	}

	public void setPpSeqNo(Integer ppSeqNo) {
		this.ppSeqNo = ppSeqNo;
	}

	public Short getTrackingPointFlag() {
		return trackingPointFlag;
	}

	public void setTrackingPointFlag(Short trackingPointFlag) {
		this.trackingPointFlag = trackingPointFlag;
	}

	public String getPlantName() {
		return plantName;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((divSeqNo == null) ? 0 : divSeqNo.hashCode());
		result = prime * result + ((divisionName == null) ? 0 : divisionName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lineName == null) ? 0 : lineName.hashCode());
		result = prime * result + ((lineSeqNo == null) ? 0 : lineSeqNo.hashCode());
		result = prime * result + ((passingCountFlag == null) ? 0 : passingCountFlag.hashCode());
		result = prime * result + ((plantName == null) ? 0 : plantName.hashCode());
		result = prime * result + ((ppSeqNo == null) ? 0 : ppSeqNo.hashCode());
		result = prime * result + ((processPointName == null) ? 0 : processPointName.hashCode());
		result = prime * result + ((trackingPointFlag == null) ? 0 : trackingPointFlag.hashCode());
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
		TrackingStatusDetails other = (TrackingStatusDetails) obj;
		if (divSeqNo == null) {
			if (other.divSeqNo != null)
				return false;
		} else if (!divSeqNo.equals(other.divSeqNo))
			return false;
		if (divisionName == null) {
			if (other.divisionName != null)
				return false;
		} else if (!divisionName.equals(other.divisionName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lineName == null) {
			if (other.lineName != null)
				return false;
		} else if (!lineName.equals(other.lineName))
			return false;
		if (lineSeqNo == null) {
			if (other.lineSeqNo != null)
				return false;
		} else if (!lineSeqNo.equals(other.lineSeqNo))
			return false;
		if (passingCountFlag == null) {
			if (other.passingCountFlag != null)
				return false;
		} else if (!passingCountFlag.equals(other.passingCountFlag))
			return false;
		if (plantName == null) {
			if (other.plantName != null)
				return false;
		} else if (!plantName.equals(other.plantName))
			return false;
		if (ppSeqNo == null) {
			if (other.ppSeqNo != null)
				return false;
		} else if (!ppSeqNo.equals(other.ppSeqNo))
			return false;
		if (processPointName == null) {
			if (other.processPointName != null)
				return false;
		} else if (!processPointName.equals(other.processPointName))
			return false;
		if (trackingPointFlag == null) {
			if (other.trackingPointFlag != null)
				return false;
		} else if (!trackingPointFlag.equals(other.trackingPointFlag))
			return false;
		return true;
	}

}