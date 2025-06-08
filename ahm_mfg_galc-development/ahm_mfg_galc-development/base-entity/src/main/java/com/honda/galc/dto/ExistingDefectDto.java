package com.honda.galc.dto;

import com.honda.galc.entity.qi.QiDefectResult;

public class ExistingDefectDto implements IDto {
	private static final long serialVersionUID = 1L;

	private QiDefectResult qiDefectResult;
	
	private String processPointId;

	public QiDefectResult getQiDefectResult() {
		return qiDefectResult;
	}

	public void setQiDefectResult(QiDefectResult qiDefectResult) {
		this.qiDefectResult = qiDefectResult;
	}

	public String getProcessPointId() {
		return processPointId;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result + ((qiDefectResult == null) ? 0 : qiDefectResult.hashCode());
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
		ExistingDefectDto other = (ExistingDefectDto) obj;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		if (qiDefectResult == null) {
			if (other.qiDefectResult != null)
				return false;
		} else if (!qiDefectResult.equals(other.qiDefectResult))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ExistingDefectDto [qiDefectResult=" + qiDefectResult + ", processPointId=" + processPointId + "]";
	}
	
	

}
