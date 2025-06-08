package com.honda.galc.entity.product;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name="FRAME_ENGINE_MODEL_MAP_TBX")
public class FrameEngineModelMap extends AuditEntry {
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private FrameEngineModelMapId id;

	@Column(name="UPDT_USERID")
	private String updtUserId;
	
	public FrameEngineModelMap() {
		super();
	}
	
	public FrameEngineModelMap(
			String frmModelYearCode,
			String frmModelCode,
			String frmModelTypeCode,
			String frmModelOptionCode,
			String engModelYearCode,
			String engModelCode,
			String engModelTypeCode,
			String engModelOptionCode) {
		this(
			frmModelYearCode,
			frmModelCode,
			frmModelTypeCode,
			frmModelOptionCode,
			engModelYearCode,
			engModelCode,
			engModelTypeCode,
			engModelOptionCode,
			"");
	}
	
	public FrameEngineModelMap(
			String frmModelYearCode,
			String frmModelCode,
			String frmModelTypeCode,
			String frmModelOptionCode,
			String engModelYearCode,
			String engModelCode,
			String engModelTypeCode,
			String engModelOptionCode,
			String updtUserId) {
		this.id = new FrameEngineModelMapId();
		this.id.setFrmModelYearCode(frmModelYearCode.trim());
		this.id.setFrmModelCode(frmModelCode.trim());
		this.id.setFrmModelTypeCode(frmModelTypeCode.trim());
		this.id.setFrmModelOptionCode(frmModelOptionCode.trim());
		this.id.setEngModelYearCode(engModelYearCode.trim());
		this.id.setEngModelCode(engModelCode.trim());
		this.id.setEngModelTypeCode(engModelTypeCode.trim());
		this.id.setEngModelOptionCode(engModelOptionCode.trim());
		this.updtUserId = updtUserId.trim();
	}
	
	public FrameEngineModelMap(FrameEngineModelMapId id) {
		this(id,"");
	}
	
	public FrameEngineModelMap(FrameEngineModelMapId id, String updtUserId ) {
		this.id = id;
		this.updtUserId = updtUserId.trim();
	}

	public FrameEngineModelMapId getId() {
		return this.id;
	}

	public void setId(FrameEngineModelMapId id) {
		this.id = id;
	}
	
	public String getFrameYmto() {
		String frameYmto = 	this.id.getFrmModelYearCode().trim() +
							this.id.getFrmModelCode().trim() + 
							this.id.getFrmModelTypeCode().trim() +
							this.id.getFrmModelOptionCode().trim();
		return frameYmto;
	}
	
	public void setFrameYmto(String ModelYearCode, String ModelCode, String ModelTypeCode, String ModelOptionCode) {
		this.id.setFrmModelYearCode(ModelYearCode);
		this.id.setFrmModelCode(ModelCode);
		this.id.setFrmModelTypeCode(ModelTypeCode);
		this.id.setFrmModelOptionCode(ModelOptionCode);
	}
	
	public String getEngineYmto() {
		String engineYmto = this.id.getEngModelYearCode().trim() + 
							this.id.getEngModelCode().trim() +
							this.id.getEngModelTypeCode().trim() +
							this.id.getEngModelOptionCode().trim();
		return engineYmto;
	}
	
	public void setEngineYmto(String ModelYearCode, String ModelCode, String ModelTypeCode, String ModelOptionCode) {
		this.id.setEngModelYearCode(ModelYearCode);
		this.id.setEngModelCode(ModelCode);
		this.id.setEngModelTypeCode(ModelTypeCode);
		this.id.setEngModelOptionCode(ModelOptionCode);
	}
	
	public Boolean compareFrameYmto(String frameYmto) {
		return	this.getFrameYmto().equalsIgnoreCase(frameYmto); 
	}
	
	public Boolean compareFrameYmto(String year, String model, String type, String option) {
		return	this.id.getFrmModelYearCode().equalsIgnoreCase(year) &&
				this.id.getFrmModelCode().equalsIgnoreCase(model) &&
				this.id.getFrmModelTypeCode().equalsIgnoreCase(type) &&
				this.id.getFrmModelOptionCode().equalsIgnoreCase(option); 
	}
	
	public Boolean compareEngineYmto(String engineYmto) {
		return	this.getEngineYmto().equalsIgnoreCase(engineYmto); 
	}
	
	public Boolean compareEngineYmto(String year, String model, String type, String option) {
		return	this.id.getEngModelYearCode().equalsIgnoreCase(year) &&
				this.id.getEngModelCode().equalsIgnoreCase(model) &&
				this.id.getEngModelTypeCode().equalsIgnoreCase(type) &&
				this.id.getEngModelOptionCode().equalsIgnoreCase(option); 
	}
	
	public String getUpdtUserId() {
		return StringUtils.trim(this.updtUserId.trim());
	}

	public void setUpdtUserId(String updtUserId) {
		this.updtUserId = updtUserId.trim();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FrameEngineModelMap other = (FrameEngineModelMap) obj;
		if (this.updtUserId == null) {
			if (other.updtUserId != null)
				return false;
		} else if (other.updtUserId == null) {
			if (this.updtUserId != null)
				return false;
		} else if (!this.updtUserId.equals(other.updtUserId))
			return false;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (other.id == null) {
			if (this.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		return true;
	}

	public String toString() {
		return toString(
				getId().getFrmModelYearCode(),
				getId().getFrmModelCode(),
				getId().getFrmModelTypeCode(),
				getId().getFrmModelOptionCode(),
				getId().getEngModelYearCode(),
				getId().getEngModelCode(),
				getId().getEngModelTypeCode(),
				getId().getEngModelOptionCode());
	}
}
