package com.honda.galc.dto.qi;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.IDto;

/**
 * 
 * <h3>QiIqsValidationResultDto Class description</h3>
 * <p>
 * QiIqsValidationResultDto Description
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

public class QiIqsValidationResultDto implements IDto{

	private static final long serialVersionUID = 1L;
	private String iqsCategory;
	private String iqsVersion;
	private String iqsQuestionNo;
	private String iqsQuestion;
	private String comments;

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

	public String getIqsQuestionNo() {
		return StringUtils.trimToEmpty(this.iqsQuestionNo);
	}

	public void setIqsQuestionNo(String iqsQuestionNo) {
		this.iqsQuestionNo = iqsQuestionNo;
	}

	public String getIqsQuestion() {
		return StringUtils.trimToEmpty(iqsQuestion);
	}

	public void setIqsQuestion(String iqsQuestion) {
		this.iqsQuestion = iqsQuestion;
	}

	public String getComments() {
		return StringUtils.trimToEmpty(comments);
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result
				+ ((iqsCategory == null) ? 0 : iqsCategory.hashCode());
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
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QiIqsValidationResultDto other = (QiIqsValidationResultDto) obj;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (iqsCategory == null) {
			if (other.iqsCategory != null)
				return false;
		} else if (!iqsCategory.equals(other.iqsCategory))
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

	@Override
	public String toString() {
		return "QiIqsValidationResultDto [iqsCategory=" + iqsCategory
				+ ", iqsVersion=" + iqsVersion + ", iqsQuestionNo="
				+ iqsQuestionNo + ", iqsQuestion=" + iqsQuestion
				+ ", comments=" + comments + "]";
	}

}
