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
import com.honda.galc.entity.enumtype.QiActiveStatus;

/**
 * 
 * <h3>QiIqs Class description</h3>
 * <p>
 * QiIqs is used to map the data in IQS Association table 
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
 * @author LnT Infotech<br>
 *        Sept 06, 2016
 * 
 */

@Entity
@Table(name = "QI_IQS_TBX")
public class QiIqs extends CreateUserAuditEntry{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IQS_ID")
	private Integer iqsId;
	@Auditable(isPartOfPrimaryKey=true,sequence=2)
	@Column(name = "IQS_CATEGORY")
	private String iqsCategory;
	@Auditable(isPartOfPrimaryKey=true,sequence=1)
	@Column(name = "IQS_VERSION")
	private String iqsVersion;
	@Auditable
	@Column(name = "IQS_QUESTION_NO")
	private Integer iqsQuestionNo;
	@Column(name = "IQS_QUESTION")
	@Auditable(isPartOfPrimaryKey = true, sequence = 3)
	private String iqsQuestion;
	@Auditable
	@Column(name = "ACTIVE")
	private short active;

	public QiIqs() {
		super();
	}

	public Integer getIqsId() {
		return iqsId;
	}

	public String getIqsCategory() {
		return StringUtils.trimToEmpty(this.iqsCategory);
	}

	public void setIqsCategory(String iqsCategory) {
		this.iqsCategory = iqsCategory;
	}

	public String getIqsVersion() {
		return StringUtils.trimToEmpty(this.iqsVersion);
	}

	public void setIqsVersion(String iqsVersion) {
		this.iqsVersion = iqsVersion;
	}

	public Integer getIqsQuestionNo() {
		return iqsQuestionNo;
	}

	public void setIqsQuestionNo(Integer iqsQuestionNo) {
		this.iqsQuestionNo = iqsQuestionNo;
	}
	
	public String getIqsQuestion() {
		return StringUtils.trimToEmpty(iqsQuestion);
	}

	public void setIqsQuestion(String iqsQuestion) {
		this.iqsQuestion = iqsQuestion;
	}

	public short getActiveValue() {
		return active;
	}

	public void setActiveValue(short active) {
		this.active = active;
	}

	public boolean isActive() {
		return this.active ==(short) 1;
	}

	public void setActive(boolean active) {
		this.active =(short)( active ? 1 : 0);
	}

	public String getStatus() {
		return StringUtils.trimToEmpty(isActive()?QiActiveStatus.ACTIVE.getName():QiActiveStatus.INACTIVE.getName());
	}

	public Object getId() {
		return iqsId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime * result
				+ ((iqsCategory == null) ? 0 : iqsCategory.hashCode());
		result = prime * result + ((iqsId == null) ? 0 : iqsId.hashCode());
		result = prime * result
				+ ((iqsQuestion == null) ? 0 : iqsQuestion.hashCode());
		result = prime * result
				+ ((iqsQuestionNo == null) ? 0 : iqsQuestionNo.hashCode());
		result = prime * result
				+ ((iqsVersion == null) ? 0 : iqsVersion.hashCode());
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
		QiIqs other = (QiIqs) obj;
		if (active != other.active)
			return false;
		if (iqsCategory == null) {
			if (other.iqsCategory != null)
				return false;
		} else if (!iqsCategory.equals(other.iqsCategory))
			return false;
		if (iqsId == null) {
			if (other.iqsId != null)
				return false;
		} else if (!iqsId.equals(other.iqsId))
			return false;
		if (iqsQuestion == null) {
			if (other.iqsQuestion != null)
				return false;
		} else if (!iqsQuestion.equals(other.iqsQuestion))
			return false;
		if (iqsQuestionNo == null) {
			if (other.iqsQuestionNo != null)
				return false;
		} else if (!iqsQuestionNo.equals(other.iqsQuestionNo))
			return false;
		if (iqsVersion == null) {
			if (other.iqsVersion != null)
				return false;
		} else if (!iqsVersion.equals(other.iqsVersion))
			return false;
		return true;
	}

}
