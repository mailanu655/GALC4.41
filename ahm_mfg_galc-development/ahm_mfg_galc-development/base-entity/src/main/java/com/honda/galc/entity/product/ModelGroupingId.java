package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.ToStringUtil;

/**
 * 
 * <h3>ModelGroupingId Class description</h3>
 * <p>ModelGroupingId contains the getter and setter of the model group properties and
 * maps this class with database table and properties with the database its
 * columns .
 * </p>
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
 * 
 * </TABLE>
 * 
 * @author LnTInfotech<br>
 *         March 31, 2017
 * 
 */
@Embeddable
public class ModelGroupingId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "MODEL_GROUP", nullable=false)
	private String modelGroup;
	
	@Column(name = "SYSTEM", nullable=false)
	private String system;
	
	@Column(name = "MTC_MODEL", nullable=false)
	private String mtcModel;
	
	
	public ModelGroupingId() {
		super();
	}

	public ModelGroupingId(String modelGroup,String mtcModel, String system) {
		this.modelGroup = modelGroup;
		this.system = system;
		this.mtcModel = mtcModel;
	}

	public String getModelGroup() {
		return StringUtils.trimToEmpty(modelGroup);
	}

	public void setModelGroup(String modelGroup) {
		this.modelGroup = modelGroup;
	}

	public String getSystem() {
		return StringUtils.trimToEmpty(system);
	}
	public void setSystem(String system) {
		this.system = system;
	}

	public String getMtcModel() {
		return StringUtils.trimToEmpty(mtcModel);
	}

	public void setMtcModel(String mtcModel) {
		this.mtcModel = mtcModel;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((modelGroup == null) ? 0 : modelGroup.hashCode());
		result = prime * result + ((mtcModel == null) ? 0 : mtcModel.hashCode());
		result = prime * result + ((system == null) ? 0 : system.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelGroupingId other = (ModelGroupingId) obj;
		if (modelGroup == null) {
			if (other.modelGroup != null)
				return false;
		} else if (!modelGroup.equals(other.modelGroup))
			return false;
		if (mtcModel == null) {
			if (other.mtcModel != null)
				return false;
		} else if (!mtcModel.equals(other.mtcModel))
			return false;
		if (system == null) {
			if (other.system != null)
				return false;
		} else if (!system.equals(other.system))
			return false;
		return true;
	}
}
