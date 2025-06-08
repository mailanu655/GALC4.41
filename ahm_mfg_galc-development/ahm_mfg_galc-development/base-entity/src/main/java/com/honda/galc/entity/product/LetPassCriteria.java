package com.honda.galc.entity.product;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.entity.AuditEntry;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Nov 25, 2013
 */

@Entity
@Table(name="GAL717TBX")
public class LetPassCriteria extends AuditEntry {
		
	@EmbeddedId
	private LetPassCriteriaId id;

	@Column(name="INSPECTION_DEVICE_TYPE")
	private String inspectionDeviceType;

	@Column(name="CRITERIA_PGM_ATTRIBUTE")
	private String criteriaPgmAttr;

    private static final long serialVersionUID = 1L;
    	
	public LetPassCriteria() {
		super();
	}

	public String toString() {
		return toString(this.getId().getModelYearCode(),this.getId().getModelCode(),this.getId().getModelTypeCode(),this.getId().getModelOptionCode());
	}
	
	public LetPassCriteriaId getId() {
		return this.id;
	}

	public String getInspectionDeviceType() {
		return StringUtils.trim(inspectionDeviceType);
	}

	public void setInspectionDeviceType(String inspectionDeviceType) {
		this.inspectionDeviceType = inspectionDeviceType;
	}


	public String getCriteriaPgmAttr() {
		return StringUtils.trim(criteriaPgmAttr);
	}


	public void setCriteriaPgmAttr(String criteriaPgmAttr) {
		this.criteriaPgmAttr = criteriaPgmAttr;
	}


	public LetPassCriteria(LetPassCriteriaId id, String inspectionDeviceType,
			String criteriaPgmAttr) {
		super();
		this.id = id;
		this.inspectionDeviceType = inspectionDeviceType;
		this.criteriaPgmAttr = criteriaPgmAttr;
	}
	
	

}
