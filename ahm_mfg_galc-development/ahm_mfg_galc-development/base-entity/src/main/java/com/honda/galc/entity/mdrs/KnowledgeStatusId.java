package com.honda.galc.entity.mdrs;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * Apr 09, 2014
 * 
 * @version 2 Fredrick Yessaian
 * Sep 10, 2014
 */
@Embeddable
public class KnowledgeStatusId implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="USER_LOGON_ID_NO", nullable=false, length=7)
	private String userLogonIdNo;

	@Column(name="PROCESS_ID", nullable=false)
	private int processId;

	@Column(name="MODEL_YEAR_DATE", nullable=false, precision=5, scale=1)
	private BigDecimal modelYearDate;

	@Column(name="MTC_MODEL", nullable=false, length=3)
	private String mtcModel;

	@Column(name="ENTERED_TSTP", nullable=false)
	private Date enteredTstp;

	@Column(name="EXTRACT_DATE", nullable=false)
	private Date extractDate;

	@Column(name="PLANT_LOC_CODE", nullable=false, length=1)
	private String plantLocCode;

	@Column(name="DEPT_CODE", nullable=false, length=2)
	private String deptCode;

    public KnowledgeStatusId() {
    }
	public String getUserLogonIdNo() {
		return StringUtils.trim(this.userLogonIdNo);
	}
	public void setUserLogonIdNo(String userLogonIdNo) {
		this.userLogonIdNo = userLogonIdNo;
	}
	public int getProcessId() {
		return this.processId;
	}
	public void setProcessId(int processId) {
		this.processId = processId;
	}
	public String getMtcModel() {
		return StringUtils.trim(this.mtcModel);
	}
	public void setMtcModel(String mtcModel) {
		this.mtcModel = mtcModel;
	}
	public Date getEnteredTstp() {
		return this.enteredTstp;
	}
	public void setEnteredTstp(Date enteredTstp) {
		this.enteredTstp = enteredTstp;
	}
	public Date getExtractDate() {
		return this.extractDate;
	}
	public void setExtractDate(Date extractDate) {
		this.extractDate = extractDate;
	}
	public String getPlantLocCode() {
		return StringUtils.trim(this.plantLocCode);
	}
	public void setPlantLocCode(String plantLocCode) {
		this.plantLocCode = plantLocCode;
	}
	public String getDeptCode() {
		return StringUtils.trim(this.deptCode);
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public void setModelYearDate(BigDecimal modelYearDate) {
		this.modelYearDate = modelYearDate;
	}
	public BigDecimal getModelYearDate() {
		return modelYearDate;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((deptCode == null) ? 0 : deptCode.hashCode());
		result = prime * result
				+ ((enteredTstp == null) ? 0 : enteredTstp.hashCode());
		result = prime * result
				+ ((extractDate == null) ? 0 : extractDate.hashCode());
		result = prime * result
				+ ((modelYearDate == null) ? 0 : modelYearDate.hashCode());
		result = prime * result
				+ ((mtcModel == null) ? 0 : mtcModel.hashCode());
		result = prime * result
				+ ((plantLocCode == null) ? 0 : plantLocCode.hashCode());
		result = prime * result + processId;
		result = prime * result
				+ ((userLogonIdNo == null) ? 0 : userLogonIdNo.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof KnowledgeStatusId)) {
			return false;
		}
		KnowledgeStatusId other = (KnowledgeStatusId) obj;
		if (deptCode == null) {
			if (other.deptCode != null) {
				return false;
			}
		} else if (!deptCode.equals(other.deptCode)) {
			return false;
		}
		if (enteredTstp == null) {
			if (other.enteredTstp != null) {
				return false;
			}
		} else if (!enteredTstp.equals(other.enteredTstp)) {
			return false;
		}
		if (extractDate == null) {
			if (other.extractDate != null) {
				return false;
			}
		} else if (!extractDate.equals(other.extractDate)) {
			return false;
		}
		if (modelYearDate == null) {
			if (other.modelYearDate != null) {
				return false;
			}
		} else if (!modelYearDate.equals(other.modelYearDate)) {
			return false;
		}
		if (mtcModel == null) {
			if (other.mtcModel != null) {
				return false;
			}
		} else if (!mtcModel.equals(other.mtcModel)) {
			return false;
		}
		if (plantLocCode == null) {
			if (other.plantLocCode != null) {
				return false;
			}
		} else if (!plantLocCode.equals(other.plantLocCode)) {
			return false;
		}
		if (processId != other.processId) {
			return false;
		}
		if (userLogonIdNo == null) {
			if (other.userLogonIdNo != null) {
				return false;
			}
		} else if (!userLogonIdNo.equals(other.userLogonIdNo)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getUserLogonIdNo(), getProcessId(), 
				getModelYearDate(), getMtcModel(), getEnteredTstp(), getPlantLocCode(), getDeptCode(), getExtractDate());
	}

}