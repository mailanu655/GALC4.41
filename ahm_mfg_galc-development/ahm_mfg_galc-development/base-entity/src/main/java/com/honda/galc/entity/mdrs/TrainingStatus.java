package com.honda.galc.entity.mdrs;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;


/**
 * The persistent class for the TRAINING_STATUS database table.
 * 
 */
@Entity
@Table(name="TRAINING_STATUS", schema="VIOS")
public class TrainingStatus extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TrainingStatusId id;

	@Column(name="DAYS_FLAG", nullable=false, length=1)
	private String daysFlag;

	@Column(name="ELECTRONIC_SIGN_TSTP")
	private Timestamp electronicSignTstp;

	@Column(name="IS_AVAILABLE", nullable=false)
	private short isAvailable;

	@Column(name="LAST_UPDATED_BY", nullable=false, length=7)
	private String lastUpdatedBy;

	@Column(name="MODEL_YEAR", nullable=false, length=4)
	private String modelYear;

	@Column(name="MTC_MODEL", nullable=false, length=3)
	private String mtcModel;

	@Column(name="NOTE", length=250)
	private String note;

	@Column(name="PROCESS_ID", nullable=false)
	private long processId;

	@Column(name="PROD_RATE")
	private Integer prodRate;

	@Column(name="SCANNED_TRAINEE_ID", length=100)
	private String scannedTraineeId;

	@Column(name="SCANNED_TRAINEE_TSTP")
	private Timestamp scannedTraineeTstp;

	@Column(name="SCANNED_TRAINER_ID", length=100)
	private String scannedTrainerId;

	@Column(name="SCANNED_TRAINER_TSTP")
	private Timestamp scannedTrainerTstp;

	@Column(name="STATUS_ID", nullable=false)
	private short statusId;

	@Column(name="USER_ID", nullable=false, length=7)
	private String userId;

	public TrainingStatus() {
	}

	public TrainingStatusId getId() {
		return this.id;
	}

	public void setId(TrainingStatusId id) {
		this.id = id;
	}

	public String getDaysFlag() {
		return StringUtils.trim(this.daysFlag);
	}

	public void setDaysFlag(String daysFlag) {
		this.daysFlag = daysFlag;
	}

	public Timestamp getElectronicSignTstp() {
		return this.electronicSignTstp;
	}

	public void setElectronicSignTstp(Timestamp electronicSignTstp) {
		this.electronicSignTstp = electronicSignTstp;
	}

	public short getIsAvailable() {
		return this.isAvailable;
	}

	public void setIsAvailable(short isAvailable) {
		this.isAvailable = isAvailable;
	}

	public String getLastUpdatedBy() {
		return StringUtils.trim(this.lastUpdatedBy);
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getModelYear() {
		return StringUtils.trim(this.modelYear);
	}

	public void setModelYear(String modelYear) {
		this.modelYear = modelYear;
	}

	public String getMtcModel() {
		return StringUtils.trim(this.mtcModel);
	}

	public void setMtcModel(String mtcModel) {
		this.mtcModel = mtcModel;
	}

	public String getNote() {
		return StringUtils.trim(this.note);
	}

	public void setNote(String note) {
		this.note = note;
	}

	public long getProcessId() {
		return this.processId;
	}

	public void setProcessId(long processId) {
		this.processId = processId;
	}

	public Integer getProdRate() {
		return this.prodRate;
	}

	public void setProdRate(Integer prodRate) {
		this.prodRate = prodRate;
	}

	public String getScannedTraineeId() {
		return StringUtils.trim(this.scannedTraineeId);
	}

	public void setScannedTraineeId(String scannedTraineeId) {
		this.scannedTraineeId = scannedTraineeId;
	}

	public Timestamp getScannedTraineeTstp() {
		return this.scannedTraineeTstp;
	}

	public void setScannedTraineeTstp(Timestamp scannedTraineeTstp) {
		this.scannedTraineeTstp = scannedTraineeTstp;
	}

	public String getScannedTrainerId() {
		return StringUtils.trim(this.scannedTrainerId);
	}

	public void setScannedTrainerId(String scannedTrainerId) {
		this.scannedTrainerId = scannedTrainerId;
	}

	public Timestamp getScannedTrainerTstp() {
		return this.scannedTrainerTstp;
	}

	public void setScannedTrainerTstp(Timestamp scannedTrainerTstp) {
		this.scannedTrainerTstp = scannedTrainerTstp;
	}

	public short getStatusId() {
		return this.statusId;
	}

	public void setStatusId(short statusId) {
		this.statusId = statusId;
	}

	public String getUserId() {
		return StringUtils.trim(this.userId);
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((daysFlag == null) ? 0 : daysFlag.hashCode());
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((electronicSignTstp == null) ? 0 : electronicSignTstp.hashCode());
		result = prime * result + (int)isAvailable;
		result = prime * result
				+ ((lastUpdatedBy == null) ? 0 : lastUpdatedBy.hashCode());
		result = prime * result
				+ ((modelYear == null) ? 0 : modelYear.hashCode());
		result = prime * result
				+ ((mtcModel == null) ? 0 : mtcModel.hashCode());
		result = prime * result
				+ ((note == null) ? 0 : note.hashCode());
		result = prime * result
				+ ((int) (processId ^ (processId >>> 32)));
		result = prime * result
				+ ((prodRate == null) ? 0 : prodRate);
		result = prime * result
				+ ((scannedTraineeId == null) ? 0 : scannedTraineeId.hashCode());
		result = prime * result
				+ ((scannedTraineeTstp == null) ? 0 : scannedTraineeTstp.hashCode());
		result = prime * result
				+ ((scannedTrainerId == null) ? 0 : scannedTrainerId.hashCode());
		result = prime * result
				+ ((scannedTrainerTstp == null) ? 0 : scannedTrainerTstp.hashCode());
		result = prime * result + (int)statusId;
		result = prime * result
				+ ((userId == null) ? 0 : userId.hashCode());
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
		TrainingStatus other = (TrainingStatus) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (daysFlag == null) {
			if (other.daysFlag != null)
				return false;
		} else if (!daysFlag.equals(other.daysFlag))
			return false;
		if (electronicSignTstp == null) {
			if (other.electronicSignTstp != null)
				return false;
		} else if (!electronicSignTstp.equals(other.electronicSignTstp))
			return false;
		if (lastUpdatedBy == null) {
			if (other.lastUpdatedBy != null)
				return false;
		} else if (!lastUpdatedBy.equals(other.lastUpdatedBy))
			return false;
		if (modelYear == null) {
			if (other.modelYear != null)
				return false;
		} else if (!modelYear.equals(other.modelYear))
			return false;
		if (mtcModel == null) {
			if (other.mtcModel != null)
				return false;
		} else if (!mtcModel.equals(other.mtcModel))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (processId != other.processId)
			return false;
		if (prodRate == null) {
			if (other.prodRate != null)
				return false;
		} else if (!prodRate.equals(other.prodRate))
			return false;
		if (scannedTraineeId == null) {
			if (other.scannedTraineeId != null)
				return false;
		} else if (!scannedTraineeId.equals(other.scannedTraineeId))
			return false;
		if (scannedTraineeTstp == null) {
			if (other.scannedTraineeTstp != null)
				return false;
		} else if (!scannedTraineeTstp.equals(other.scannedTraineeTstp))
			return false;
		if (scannedTrainerId == null) {
			if (other.scannedTrainerId != null)
				return false;
		} else if (!scannedTrainerId.equals(other.scannedTrainerId))
			return false;
		if (scannedTrainerTstp == null) {
			if (other.scannedTrainerTstp != null)
				return false;
		} else if (!scannedTrainerTstp.equals(other.scannedTrainerTstp))
			return false;
		if (statusId != other.statusId)
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return toString(getId().getTrainingStatusId(), getId().getPlantLocCode(), getId().getDeptCode()
				, getId().getExtractDate(), getId().getLastUpdatedDate(), getDaysFlag()
				, getElectronicSignTstp(), getLastUpdatedBy(), getModelYear(), getMtcModel()
				, getNote(), getProcessId(), getProdRate(), getScannedTraineeId(), getScannedTraineeTstp()
				, getScannedTrainerId(), getScannedTrainerTstp(), getStatusId(), getUserId());
	}
}