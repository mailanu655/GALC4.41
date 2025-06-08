package com.honda.galc.entity.qi;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;
import com.honda.galc.util.ToStringUtil;

/**
 * <h3>Class Description</h3>
 * <p>
 * <code>QiReportingTarget</code>
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
 * <TD>L&T Infotech</TD>
 * <TD>15/11/2016</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
@Entity
@Table(name = "QI_REPORTING_TARGET_TBX")
public class QiReportingTarget extends CreateUserAuditEntry {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TARGET_ID", nullable = false)
	private int targetId;

	@Column(name = "SITE", nullable = false)
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private String site;

	@Column(name = "PLANT", nullable = false)
	@Auditable(isPartOfPrimaryKey = true, sequence = 2)
	private String plant;

	@Column(name = "PRODUCT_TYPE", nullable = false)
	@Auditable(isPartOfPrimaryKey = true, sequence = 3)
	private String productType;

	@Column(name = "MODEL_GROUP")
	@Auditable(isPartOfPrimaryKey = true, sequence = 4)
	private String modelGroup;

	@Column(name = "MODEL_YEAR_DESCRIPTION")
	@Auditable
	private String modelYearDescription;

	@Column(name = "DEMAND_TYPE")
	@Auditable
	private String demandType;

	@Column(name = "METRIC_NAME", nullable = false)
	@Auditable
	private String metricName;

	@Column(name = "METRIC_VALUE")
	@Auditable
	private double metricValue;

	@Column(name = "EFFECTIVE_DATE")
	@Auditable
	private Date effectiveDate;

	@Column(name = "TARGET")
	@Auditable
	private String target;
	
	@Column(name = "TARGET_ITEM")
	@Auditable
	private String targetItem;

	@Column(name = "DEPT")
	@Auditable
	private String department;
	
	@Column(name = "SYSTEM")
	@Auditable
	private String system;
	
	@Column(name = "LEVEL")
	@Auditable
	private String level;
	
	@Column(name = "CALCULATED_METRIC_VALUE")
	private double calculatedMetricValue;	
	
	public QiReportingTarget() {
		super();
	}
	
	//create an object by copying some data from lower level target
	public QiReportingTarget(QiReportingTarget lowerLevelTarget) {
		this.site = lowerLevelTarget.getSite();
		this.plant = lowerLevelTarget.getPlant();
		this.productType = lowerLevelTarget.getProductType();
		this.modelGroup = lowerLevelTarget.getModelGroup();
		this.modelYearDescription = lowerLevelTarget.getModelYearDescription();
		this.demandType = lowerLevelTarget.getDemandType();
		this.department = lowerLevelTarget.getDepartment();
		this.effectiveDate = lowerLevelTarget.getEffectiveDate();
		this.metricName = lowerLevelTarget.getMetricName();
		this.system = lowerLevelTarget.getSystem();
		this.setCreateUser(lowerLevelTarget.getCreateUser());
	}

	public Object getId() {
		return targetId;
	}

	public int getTargetId() {
		return targetId;
	}

	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}

	public String getSite() {
		return StringUtils.trimToEmpty(site);
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getPlant() {
		return StringUtils.trimToEmpty(plant);
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getProductType() {
		return StringUtils.trimToEmpty(productType);
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getModelGroup() {
		return StringUtils.trimToEmpty(modelGroup);
	}

	public void setModelGroup(String modelGroup) {
		this.modelGroup = modelGroup;
	}

	public String getModelYearDescription() {
		return StringUtils.trimToEmpty(modelYearDescription);
	}

	public void setModelYearDescription(String modelYearDescription) {
		this.modelYearDescription = modelYearDescription;
	}

	public String getDemandType() {
		return StringUtils.trimToEmpty(demandType);
	}

	public void setDemandType(String demandType) {
		this.demandType = demandType;
	}

	public String getMetricName() {
		return StringUtils.trimToEmpty(metricName);
	}

	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}

	public double getMetricValue() {
		return metricValue;
	}

	public void setMetricValue(double metricValue) {
		this.metricValue = metricValue;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getTarget() {
		return StringUtils.trimToEmpty(target);
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTargetItem() {
		return StringUtils.trimToEmpty(targetItem);
	}

	public void setTargetItem(String targetItem) {
		this.targetItem = targetItem;
	}

	public String getDepartment() {
		return StringUtils.trimToEmpty(department);
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getSystem() {
		return StringUtils.trimToEmpty(system);
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getLevel() {
		return StringUtils.trimToEmpty(level);
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
	public double getCalculatedMetricValue() {
		return calculatedMetricValue;
	}

	public void setCalculatedMetricValue(double calculatedMetricValue) {
		this.calculatedMetricValue = calculatedMetricValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((demandType == null) ? 0 : demandType.hashCode());
		result = prime * result
				+ ((department == null) ? 0 : department.hashCode());
		result = prime * result
				+ ((effectiveDate == null) ? 0 : effectiveDate.hashCode());
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result
				+ ((metricName == null) ? 0 : metricName.hashCode());
		result = prime * result
				+ ((modelGroup == null) ? 0 : modelGroup.hashCode());
		result = prime
				* result
				+ ((modelYearDescription == null) ? 0 : modelYearDescription
						.hashCode());
		result = prime * result + ((plant == null) ? 0 : plant.hashCode());
		result = prime * result
				+ ((productType == null) ? 0 : productType.hashCode());
		result = prime * result + ((site == null) ? 0 : site.hashCode());
		result = prime * result + ((system == null) ? 0 : system.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		result = prime * result
				+ ((targetItem == null) ? 0 : targetItem.hashCode());
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
		QiReportingTarget other = (QiReportingTarget) obj;
		if (demandType == null) {
			if (other.demandType != null)
				return false;
		} else if (!demandType.equals(other.demandType))
			return false;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (effectiveDate == null) {
			if (other.effectiveDate != null)
				return false;
		} else if (!effectiveDate.equals(other.effectiveDate))
			return false;
		if (level == null) {
			if (other.level != null)
				return false;
		} else if (!level.equals(other.level))
			return false;
		if (metricName == null) {
			if (other.metricName != null)
				return false;
		} else if (!metricName.equals(other.metricName))
			return false;
		if (modelGroup == null) {
			if (other.modelGroup != null)
				return false;
		} else if (!modelGroup.equals(other.modelGroup))
			return false;
		if (modelYearDescription == null) {
			if (other.modelYearDescription != null)
				return false;
		} else if (!modelYearDescription.equals(other.modelYearDescription))
			return false;
		if (plant == null) {
			if (other.plant != null)
				return false;
		} else if (!plant.equals(other.plant))
			return false;
		if (productType == null) {
			if (other.productType != null)
				return false;
		} else if (!productType.equals(other.productType))
			return false;
		if (site == null) {
			if (other.site != null)
				return false;
		} else if (!site.equals(other.site))
			return false;
		if (system == null) {
			if (other.system != null)
				return false;
		} else if (!system.equals(other.system))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		if (targetItem == null) {
			if (other.targetItem != null)
				return false;
		} else if (!targetItem.equals(other.targetItem))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}
}
