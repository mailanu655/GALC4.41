package com.honda.galc.entity.qi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.ToStringUtil;

/**
 * 
 * <h3>QiIqsQuestionId Class description</h3>
 * <p>
 * QiIqsQuestionId contains the getter and setter of the Iqs Question composite key properties and maps this class with database and these columns .
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
 *        Dec 11, 2016
 * 
 */

@Embeddable
public class QiIqsQuestionId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "IQS_QUESTION_NO")
	private Integer iqsQuestionNo;
	@Column(name = "IQS_QUESTION")
	private String iqsQuestion;

	public QiIqsQuestionId() {
		super();
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

	public Object getId() {
		return iqsQuestion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((iqsQuestion == null) ? 0 : iqsQuestion.hashCode());
		result = prime * result + iqsQuestionNo;
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
		QiIqsQuestionId other = (QiIqsQuestionId) obj;
		if (iqsQuestion == null) {
			if (other.iqsQuestion != null)
				return false;
		} else if (!iqsQuestion.equals(other.iqsQuestion))
			return false;
		if (iqsQuestionNo != other.iqsQuestionNo)
			return false;
		return true;
	}
	
	public void clear() {
		setIqsQuestion(StringUtils.EMPTY);
	}
	
	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}

}
