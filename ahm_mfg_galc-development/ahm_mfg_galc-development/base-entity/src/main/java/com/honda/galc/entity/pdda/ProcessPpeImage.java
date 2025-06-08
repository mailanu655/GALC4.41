package com.honda.galc.entity.pdda;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * Apr 09, 2014
 */
@Entity
@Table(name="PVPPI1", schema="VIOS")
public class ProcessPpeImage extends AuditEntry implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ProcessPpeImageId id;

	@Column(name="DEPT_CODE", nullable=false, length=2)
	private String deptCode;

    @Lob()
	@Column(name="IMAGE", nullable=false)
	private byte[] image;

	@Column(name="PLANT_LOC_CODE", nullable=false, length=1)
	private String plantLocCode;

	@Column(name="POTENTIAL_HAZARD", nullable=false, length=300)
	private String potentialHazard;

	@Column(name="IMAGE_TIMESTAMP", nullable=false)
	private Timestamp imageTimestamp;

	@Column(name="PPE_REQUIRED", nullable=false, length=200)
	private String ppeRequired;

	@Column(name="PPE_USAGE", nullable=false, length=500)
	private String ppeUsage;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="MAINTENANCE_ID", nullable=false, insertable=false, updatable=false)
	private Process process;

    public ProcessPpeImage() {}

	public ProcessPpeImageId getId() {
		return this.id;
	}

	public void setId(ProcessPpeImageId id) {
		this.id = id;
	}
	
	public String getDeptCode() {
		return StringUtils.trim(this.deptCode);
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public byte[] getImage() {
		return this.image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getPlantLocCode() {
		return StringUtils.trim(this.plantLocCode);
	}

	public void setPlantLocCode(String plantLocCode) {
		this.plantLocCode = plantLocCode;
	}

	public String getPotentialHazard() {
		return StringUtils.trim(this.potentialHazard);
	}

	public void setPotentialHazard(String potentialHazard) {
		this.potentialHazard = potentialHazard;
	}

	public Timestamp getImageTimestamp() {
		return this.imageTimestamp;
	}

	public void setImageTimestamp(Timestamp imageTimestamp) {
		this.imageTimestamp = imageTimestamp;
	}
	
	public String getPpeRequired() {
		return StringUtils.trim(this.ppeRequired);
	}

	public void setPpeRequired(String ppeRequired) {
		this.ppeRequired = ppeRequired;
	}

	public String getPpeUsage() {
		return StringUtils.trim(this.ppeUsage);
	}

	public void setPpeUsage(String ppeUsage) {
		this.ppeUsage = ppeUsage;
	}

	public Process getProcess() {
		return this.process;
	}

	public void setProcess(Process Process) {
		this.process = Process;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((deptCode == null) ? 0 : deptCode.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + Arrays.hashCode(image);
		result = prime * result
				+ ((imageTimestamp == null) ? 0 : imageTimestamp.hashCode());
		result = prime * result
				+ ((plantLocCode == null) ? 0 : plantLocCode.hashCode());
		result = prime * result
				+ ((potentialHazard == null) ? 0 : potentialHazard.hashCode());
		result = prime * result
				+ ((ppeRequired == null) ? 0 : ppeRequired.hashCode());
		result = prime * result
				+ ((ppeUsage == null) ? 0 : ppeUsage.hashCode());
		result = prime * result + ((process == null) ? 0 : process.hashCode());
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
		ProcessPpeImage other = (ProcessPpeImage) obj;
		if (deptCode == null) {
			if (other.deptCode != null)
				return false;
		} else if (!deptCode.equals(other.deptCode))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (!Arrays.equals(image, other.image))
			return false;
		if (imageTimestamp == null) {
			if (other.imageTimestamp != null)
				return false;
		} else if (!imageTimestamp.equals(other.imageTimestamp))
			return false;
		if (plantLocCode == null) {
			if (other.plantLocCode != null)
				return false;
		} else if (!plantLocCode.equals(other.plantLocCode))
			return false;
		if (potentialHazard == null) {
			if (other.potentialHazard != null)
				return false;
		} else if (!potentialHazard.equals(other.potentialHazard))
			return false;
		if (ppeRequired == null) {
			if (other.ppeRequired != null)
				return false;
		} else if (!ppeRequired.equals(other.ppeRequired))
			return false;
		if (ppeUsage == null) {
			if (other.ppeUsage != null)
				return false;
		} else if (!ppeUsage.equals(other.ppeUsage))
			return false;
		if (process == null) {
			if (other.process != null)
				return false;
		} else if (!process.equals(other.process))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return toString(getId().getMaintenanceId(), getId().getImageSeqNo(), getId().getPpeId(), getDeptCode(), getImage(), 
				getPlantLocCode(), getPotentialHazard(), getImageTimestamp(), getPpeRequired(), getPpeUsage());
	}
}