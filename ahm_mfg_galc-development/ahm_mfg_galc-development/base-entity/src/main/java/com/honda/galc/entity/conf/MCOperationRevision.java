package com.honda.galc.entity.conf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.constant.OperationType;
import com.honda.galc.constant.PartType;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.util.ToStringUtil;

/**
 * @author Subu Kathiresan
 * Feb 10, 2014
 */
@Entity
@Table(name="MC_OP_REV_TBX")
public class MCOperationRevision extends AuditEntry implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MCOperationRevisionId id;

	@Column(name="REV_ID")
	private long revisionId;
	
	@Column(name="OP_DESC", length=255)
	private String description;

	@Column(name="OP_TYPE", length=32)
	@Enumerated(EnumType.STRING)
	private OperationType type;

	@Column(name="OP_VIEW", length=255)
	private String view;

	@Column(name="OP_PROCESSOR", length=255)
	private String processor;

	@Column(name="OP_CHECK")
	private int check;
	
	@Column(name="APPROVED")
	private Date approved;
	
	@Column(name="DEPRECATED_REV_ID")
	private long deprecatedRevisionId;

	@Column(name="DEPRECATED")
	private Date deprecated;
	
	@Column(name="COMMON_NAME", length=255)
	private String commonName;
	
	@Transient
	private List<MCOperationPartRevision> parts;
	
	@Transient 
	private MCOperationPartRevision selectedPart;
	
	@Transient 
	private int approvedUnitMaintenanceId;
	
	@Transient 
	private MCStructure structure;
	
	@Transient 
	private String unitNo;
	
    public MCOperationRevision() {}

	public MCOperationRevisionId getId() {
		return this.id;
	}

	public void setId(MCOperationRevisionId id) {
		this.id = id;
	}

	public long getRevisionId() {
		return revisionId;
	}

	public void setRevisionId(long revisionId) {
		this.revisionId = revisionId;
	}

	public String getDescription() {
		return StringUtils.trim(this.description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public OperationType getType() {
		return this.type;
	}

	public void setType(OperationType type) {
		this.type = type;
	}

	public String getView() {
		return StringUtils.trim(this.view);
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getProcessor() {
		return StringUtils.trim(this.processor);
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	public int getCheck() {
		return check;
	}

	public void setCheck(int check) {
		this.check = check;
	}

	public Date getApproved() {
		return approved;
	}

	public void setApproved(Date approved) {
		this.approved = approved;
	}

	public long getDeprecatedRevisionId() {
		return deprecatedRevisionId;
	}

	public void setDeprecatedRevisionId(long deprecatedRevisionId) {
		this.deprecatedRevisionId = deprecatedRevisionId;
	}

	public Date getDeprecated() {
		return deprecated;
	}

	public void setDeprecated(Date deprecated) {
		this.deprecated = deprecated;
	}
	
	public String getCommonName() {
		return StringUtils.trimToEmpty(commonName);
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public List<MCOperationPartRevision> getParts() {
		if (parts == null) {
			parts = new ArrayList<MCOperationPartRevision>();
		}			
		return parts;
	}

	public void setParts(List<MCOperationPartRevision> parts) {
		this.parts = parts;
	}
	
	
	public MCOperationPartRevision getSelectedPart() {
		List<MCOperationPartRevision> lst=getManufacturingMFGPartList();
		if(lst.size()==1 || this.selectedPart==null){
			return getManufacturingPart();
		}else {
			return this.selectedPart;
		}
		
	}

	public MCOperationPartRevision getManufacturingPart() {
		for (MCOperationPartRevision part : getParts()) {
			if (part.getPartType() == PartType.MFG) {
				return part;
			}
		}
		return null;
	}

	public List<MCOperationPartRevision> getManufacturingMFGPartList() {
		List<MCOperationPartRevision> lst = new ArrayList<MCOperationPartRevision>();
		for (MCOperationPartRevision part : getParts()) {
			if (part.getPartType() == PartType.MFG) {
				lst.add(part);
			}
		}
		return lst;
	}
	
	public void setSelectedPart(MCOperationPartRevision selectedPart) {
		this.selectedPart = selectedPart;
	}
	
	public int getApprovedUnitMaintenanceId() {
		return approvedUnitMaintenanceId;
	}

	public void setApprovedUnitMaintenanceId(int approvedUnitMaintenanceId) {
		this.approvedUnitMaintenanceId = approvedUnitMaintenanceId;
	}

	public String getUnitNo() {
		return unitNo;
	}

	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}
	
	public MCStructure getStructure() {
		return structure;
	}

	public void setStructure(MCStructure structure) {
		this.structure = structure;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((approved == null) ? 0 : approved.hashCode());
		result = prime * result + check;
		result = prime * result
				+ ((deprecated == null) ? 0 : deprecated.hashCode());
		result = prime * result
				+ (int) (deprecatedRevisionId ^ (deprecatedRevisionId >>> 32));
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((processor == null) ? 0 : processor.hashCode());
		result = prime * result + (int) (revisionId ^ (revisionId >>> 32));
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((view == null) ? 0 : view.hashCode());
		result = prime * result + ((commonName == null) ? 0 : commonName.hashCode());
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
		MCOperationRevision other = (MCOperationRevision) obj;
		if (approved == null) {
			if (other.approved != null)
				return false;
		} else if (!approved.equals(other.approved))
			return false;
		if (check != other.check)
			return false;
		if (deprecated == null) {
			if (other.deprecated != null)
				return false;
		} else if (!deprecated.equals(other.deprecated))
			return false;
		if (deprecatedRevisionId != other.deprecatedRevisionId)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (processor == null) {
			if (other.processor != null)
				return false;
		} else if (!processor.equals(other.processor))
			return false;
		if (revisionId != other.revisionId)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (view == null) {
			if (other.view != null)
				return false;
		} else if (!view.equals(other.view))
			return false;
		if (commonName == null) {
			if (other.commonName != null)
				return false;
		} else if (!commonName.equals(other.commonName))
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return ToStringUtil.generateToString(this, false);
	}
}
