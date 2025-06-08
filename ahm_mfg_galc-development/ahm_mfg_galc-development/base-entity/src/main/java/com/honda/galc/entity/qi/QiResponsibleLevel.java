package com.honda.galc.entity.qi;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;

/**
 * 
 * <h3>QiResponsibleLevel Class description</h3>
 * <p>
 * QiResponsibleLevel contains the getter and setter of the QiResponsibleLevel properties and maps this class with database table and properties with the database its columns .
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
 * 
 */
@Entity
@Table(name = "QI_RESPONSIBLE_LEVEL_TBX")
public class QiResponsibleLevel extends CreateUserAuditEntry {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RESPONSIBLE_LEVEL_ID")
	@Auditable
	private Integer responsibleLevelId;
	
	@Column(name = "SITE")
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private String site;
	
	@Column(name = "PLANT")
	@Auditable(isPartOfPrimaryKey = true, sequence = 2)
	private String plant;
	
	@Column(name = "DEPT")
	@Auditable(isPartOfPrimaryKey = true, sequence = 3)
	private String department;
	
	@Column(name = "RESPONSIBLE_LEVEL_NAME")
	@Auditable(isPartOfPrimaryKey = true, sequence = 4)
	private String responsibleLevelName;
	
	@Column(name = "RESPONSIBLE_LEVEL_DESCRIPTION")
	@Auditable
	private String responsibleLevelDescription;
	
	@Column(name = "LEVEL")
	@Auditable(isPartOfPrimaryKey = true, sequence = 5)
	private short level;
	
	@Column(name = "ROW_TYPE")
	@Auditable
	private short rowtype;
	
	@Column(name = "UPPER_RESPONSIBLE_LEVEL_ID")
	@Auditable
	private Integer upperResponsibleLevelId;
	
	@Column(name = "ACTIVE")
	@Auditable
	private int active;
	
	public Object getId() {
		return responsibleLevelId;
	}

	public Integer getResponsibleLevelId() {
		return responsibleLevelId;
	}

	public void setResponsibleLevelId(Integer responsibleLevelId) {
		this.responsibleLevelId = responsibleLevelId;
	}

	public String getSite() {
		return StringUtils.trimToEmpty(this.site);
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getPlant() {
		return StringUtils.trimToEmpty(this.plant);
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getDepartment() {
		return StringUtils.trimToEmpty(this.department);
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getResponsibleLevelName() {
		return StringUtils.trimToEmpty(this.responsibleLevelName);
	}

	public void setResponsibleLevelName(String responsibleLevelName) {
		this.responsibleLevelName = responsibleLevelName;
	}

	public String getResponsibleLevelDescription() {
		return StringUtils.trimToEmpty(this.responsibleLevelDescription);
	}

	public void setResponsibleLevelDescription(String responsibleLevelDescription) {
		this.responsibleLevelDescription = responsibleLevelDescription;
	}

	public short getLevel() {
		return level;
	}

	public void setLevel(short level) {
		this.level = level;
	}

	public short getRowtype() {
		return rowtype;
	}

	public void setRowtype(short rowtype) {
		this.rowtype = rowtype;
	}

	public Integer getUpperResponsibleLevelId() {
		return upperResponsibleLevelId;
	}

	public void setUpperResponsibleLevelId(Integer upperResponsibleLevelId) {
		this.upperResponsibleLevelId = upperResponsibleLevelId;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}
	
	public boolean isActive() {
		return this.active == (short) 1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime * result
				+ ((department == null) ? 0 : department.hashCode());
		result = prime * result + level;
		result = prime * result + ((plant == null) ? 0 : plant.hashCode());
		result = prime
				* result
				+ ((responsibleLevelDescription == null) ? 0
						: responsibleLevelDescription.hashCode());
		result = prime
				* result
				+ ((responsibleLevelId == null) ? 0 : responsibleLevelId
						.hashCode());
		result = prime
				* result
				+ ((responsibleLevelName == null) ? 0 : responsibleLevelName
						.hashCode());
		result = prime * result + rowtype;
		result = prime * result + ((site == null) ? 0 : site.hashCode());
		result = prime
				* result
				+ ((upperResponsibleLevelId == null) ? 0
						: upperResponsibleLevelId.hashCode());
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
		QiResponsibleLevel other = (QiResponsibleLevel) obj;
		if (active != other.active)
			return false;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (level != other.level)
			return false;
		if (plant == null) {
			if (other.plant != null)
				return false;
		} else if (!plant.equals(other.plant))
			return false;
		if (responsibleLevelDescription == null) {
			if (other.responsibleLevelDescription != null)
				return false;
		} else if (!responsibleLevelDescription
				.equals(other.responsibleLevelDescription))
			return false;
		if (responsibleLevelId == null) {
			if (other.responsibleLevelId != null)
				return false;
		} else if (!responsibleLevelId.equals(other.responsibleLevelId))
			return false;
		if (responsibleLevelName == null) {
			if (other.responsibleLevelName != null)
				return false;
		} else if (!responsibleLevelName.equals(other.responsibleLevelName))
			return false;
		if (rowtype != other.rowtype)
			return false;
		if (site == null) {
			if (other.site != null)
				return false;
		} else if (!site.equals(other.site))
			return false;
		if (upperResponsibleLevelId == null) {
			if (other.upperResponsibleLevelId != null)
				return false;
		} else if (!upperResponsibleLevelId
				.equals(other.upperResponsibleLevelId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QiResponsibleLevel [responsibleLevelId=" + responsibleLevelId + ", site=" + site + ", plant=" + plant
				+ ", department=" + department + ", responsibleLevelName=" + responsibleLevelName
				+ ", responsibleLevelDescription=" + responsibleLevelDescription + ", level=" + level + ", rowtype="
				+ rowtype + ", upperResponsibleLevelId=" + upperResponsibleLevelId + ", active=" + active + "]";
	}

	
}