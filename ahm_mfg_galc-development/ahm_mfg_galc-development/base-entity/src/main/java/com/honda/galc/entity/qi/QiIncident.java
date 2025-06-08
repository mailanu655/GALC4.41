package com.honda.galc.entity.qi;


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.AuditEntry;
/**
 * 
 * <h3>QiIncident Class description</h3>
 * <p>
 * QiIncident contains the getter and setter of the QiIncident properties and maps this class with database table and properties with the database its columns .
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
@Table(name = "QI_INCIDENT_TBX")
public class QiIncident extends AuditEntry{
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "INCIDENT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private int incidentId;
	@Column(name = "INCIDENT_TITLE")
	@Auditable(isPartOfPrimaryKey = true, sequence = 2)
	private String incidentTitle;
	@Column(name = "INCIDENT_DATE")
	@Auditable(isPartOfPrimaryKey = true, sequence = 3)
	private Date incidentDate;
	@Column(name = "INCIDENT_CAUSE")
	private String incidentCause;
	@Column(name = "INCIDENT_TYPE")
	@Auditable(isPartOfPrimaryKey = true, sequence = 4)
	private String incidentType;
	@Column(name = "DOCUMENT_CONTROL_TYPE")
	@Auditable(isPartOfPrimaryKey = true, sequence = 5)
	private String documentControlType;
	@Column(name = "DOCUMENT_CONTROL_ID")
	@Auditable(isPartOfPrimaryKey = true, sequence = 6)
	private String documentControlId;
	
	@Column(name = "CREATE_USER")
	@Auditable(isPartOfPrimaryKey = true, sequence = 7)
	private String createUser;
	
	@Column(name = "UPDATE_USER")
	@Auditable(isPartOfPrimaryKey = true, sequence = 8)
	private String updateUser;
	
	
	public QiIncident() {
		super();
	}
	
	public int getIncidentId() {
		return incidentId;
	}

	public void setIncidentId(int incidentId) {
		this.incidentId = incidentId;
	}

	public String getIncidentTitle() {
		return StringUtils.trimToEmpty(incidentTitle);
	}

	public void setIncidentTitle(String incidentTitle) {
		this.incidentTitle = incidentTitle;
	}

	public Date getIncidentDate() {
		return incidentDate;
	}

	public void setIncidentDate(Date incidentDate) {
		this.incidentDate = incidentDate;
	}

	public String getIncidentCause() {
		return StringUtils.trimToEmpty(incidentCause);
	}

	public void setIncidentCause(String incidentCause) {
		this.incidentCause = incidentCause;
	}

	public String getIncidentType() {
		return StringUtils.trimToEmpty(incidentType);
	}

	public void setIncidentType(String incidentType) {
		this.incidentType = incidentType;
	}

	public String getDocumentControlType() {
		return StringUtils.trimToEmpty(documentControlType);
	}

	public void setDocumentControlType(String documentControlType) {
		this.documentControlType = documentControlType;
	}

	public String getDocumentControlId() {
		return StringUtils.trimToEmpty(documentControlId);
	}

	public void setDocumentControlId(String documentControlId) {
		this.documentControlId = documentControlId;
	}

	public Object getId() {
		return getIncidentId();
	}

	public String getCreateUser() {
		return StringUtils.trimToEmpty(createUser);
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateUser() {
		return StringUtils.trimToEmpty(updateUser);
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createUser == null) ? 0 : createUser.hashCode());
		result = prime * result + ((documentControlId == null) ? 0 : documentControlId.hashCode());
		result = prime * result + ((documentControlType == null) ? 0 : documentControlType.hashCode());
		result = prime * result + ((incidentCause == null) ? 0 : incidentCause.hashCode());
		result = prime * result + ((incidentDate == null) ? 0 : incidentDate.hashCode());
		result = prime * result + incidentId;
		result = prime * result + ((incidentTitle == null) ? 0 : incidentTitle.hashCode());
		result = prime * result + ((incidentType == null) ? 0 : incidentType.hashCode());
		result = prime * result + ((updateUser == null) ? 0 : updateUser.hashCode());
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
		QiIncident other = (QiIncident) obj;
		if (createUser == null) {
			if (other.createUser != null)
				return false;
		} else if (!createUser.equals(other.createUser))
			return false;
		if (documentControlId == null) {
			if (other.documentControlId != null)
				return false;
		} else if (!documentControlId.equals(other.documentControlId))
			return false;
		if (documentControlType == null) {
			if (other.documentControlType != null)
				return false;
		} else if (!documentControlType.equals(other.documentControlType))
			return false;
		if (incidentCause == null) {
			if (other.incidentCause != null)
				return false;
		} else if (!incidentCause.equals(other.incidentCause))
			return false;
		if (incidentDate == null) {
			if (other.incidentDate != null)
				return false;
		} else if (!incidentDate.equals(other.incidentDate))
			return false;
		if (incidentId != other.incidentId)
			return false;
		if (incidentTitle == null) {
			if (other.incidentTitle != null)
				return false;
		} else if (!incidentTitle.equals(other.incidentTitle))
			return false;
		if (incidentType == null) {
			if (other.incidentType != null)
				return false;
		} else if (!incidentType.equals(other.incidentType))
			return false;
		if (updateUser == null) {
			if (other.updateUser != null)
				return false;
		} else if (!updateUser.equals(other.updateUser))
			return false;
		return true;
	}
	
	
}
