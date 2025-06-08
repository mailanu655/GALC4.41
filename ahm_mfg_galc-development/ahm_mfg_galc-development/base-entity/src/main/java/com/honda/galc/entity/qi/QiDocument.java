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
import com.honda.galc.util.ToStringUtil;

/**
 * 
 * <h3>QiDocument Class description</h3>
 * <p>
 * QiDocument description
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
 * @author Justin Jiang<br>
 *         February 20, 2020
 *
 */

@Entity
@Table(name = "QI_DOCUMENT_TBX")
public class QiDocument extends CreateUserAuditEntry {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "DOCUMENT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private int documentId;
	@Column(name = "DOCUMENT_NAME")
	@Auditable(isPartOfPrimaryKey = true, sequence = 2)
	private String documentName;
	@Column(name = "DOCUMENT_LINK")
	@Auditable(isPartOfPrimaryKey = true, sequence = 3)
	private String documentLink;
	@Column(name = "DESCRIPTION")
	private String description;

	public QiDocument() {
		super();
	}

	public Object getId() {
		return getDocumentId();
	}

	public int getDocumentId() {
		return documentId;
	}

	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}

	public String getDocumentName() {
		return StringUtils.trimToEmpty(documentName);
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocumentLink() {
		return StringUtils.trimToEmpty(documentLink);
	}

	public void setDocumentLink(String documentLink) {
		this.documentLink = documentLink;
	}

	public String getDescription() {
		return StringUtils.trimToEmpty(description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + documentId;
		result = prime * result + ((documentLink == null) ? 0 : documentLink.hashCode());
		result = prime * result + ((documentName == null) ? 0 : documentName.hashCode());
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
		QiDocument other = (QiDocument) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (documentId != other.documentId)
			return false;
		if (documentLink == null) {
			if (other.documentLink != null)
				return false;
		} else if (!documentLink.equals(other.documentLink))
			return false;
		if (documentName == null) {
			if (other.documentName != null)
				return false;
		} else if (!documentName.equals(other.documentName))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}
}
