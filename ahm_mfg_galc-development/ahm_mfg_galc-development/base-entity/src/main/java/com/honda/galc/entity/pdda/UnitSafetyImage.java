package com.honda.galc.entity.pdda;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * Apr 09, 2014
 */
@Entity
@Table(name="PVUSH1", schema="VIOS")
public class UnitSafetyImage extends AuditEntry implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UnitSafetyImageId id;

	@Column(name="DEPT_CODE", nullable=false, length=2)
	private String deptCode;

    @Lob()
	@Column(name="IMAGE", nullable=false)
	private byte[] image;

	@Column(name="IMAGE_TIMESTAMP", nullable=false)
	private Timestamp imageTimestamp;

	@Column(name="PLANT_LOC_CODE", nullable=false, length=1)
	private String plantLocCode;

    public UnitSafetyImage() {}

	public UnitSafetyImageId getId() {
		return this.id;
	}

	public void setId(UnitSafetyImageId id) {
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

	public Timestamp getImageTimestamp() {
		return this.imageTimestamp;
	}

	public void setImageTimestamp(Timestamp imageTimestamp) {
		this.imageTimestamp = imageTimestamp;
	}

	public String getPlantLocCode() {
		return StringUtils.trim(this.plantLocCode);
	}

	public void setPlantLocCode(String plantLocCode) {
		this.plantLocCode = plantLocCode;
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
		UnitSafetyImage other = (UnitSafetyImage) obj;
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
		return true;
	}
	
	@Override
	public String toString() {
		return toString(getId().getMaintenanceId(), getId().getImageSeqNo(), getId().getImageName(), 
				getDeptCode(), getImage(), getImageTimestamp(), getPlantLocCode());
	}
}