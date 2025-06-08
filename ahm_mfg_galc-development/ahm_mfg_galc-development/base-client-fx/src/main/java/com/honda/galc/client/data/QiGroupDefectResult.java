package com.honda.galc.client.data;

import com.honda.galc.dto.QiDefectDescriptionDto;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.qi.QiDefectResult;

public class QiGroupDefectResult {
	private long  defectTransactionGroupId;
	
	private String createUser;
	
	private QiDefectDescriptionDto defectTypeName;
	
	private int scrapCount = 0;
	
	private int outstandingCount = 0;
	
	private int fixedCount = 0;
	
	private int npfCount = 0;
	
	private int totalCount = 0;
	
	private int notFixedScrapCount = 0;
	
	public QiGroupDefectResult(QiDefectResult qiDefectResult) {
		this.defectTransactionGroupId = qiDefectResult.getDefectTransactionGroupId();
		this.defectTypeName = createQiDefectDescription(qiDefectResult);
		this.createUser = qiDefectResult.getCreateUser();
	}

	private QiDefectDescriptionDto createQiDefectDescription(QiDefectResult qiDefectResult) {
		QiDefectDescriptionDto qiDefectDescription = new QiDefectDescriptionDto();
		qiDefectDescription.setResponsibleDept(qiDefectResult.getResponsibleDept());
		qiDefectDescription.setInspectionPartName(qiDefectResult.getInspectionPartName());
		qiDefectDescription.setInspectionPartLocationName(qiDefectResult.getInspectionPartLocationName());
		qiDefectDescription.setInspectionPartLocation2Name(qiDefectResult.getInspectionPartLocation2Name());
		qiDefectDescription.setInspectionPart2Name(qiDefectResult.getInspectionPart2Name());
		qiDefectDescription.setInspectionPart2LocationName(qiDefectResult.getInspectionPart2LocationName());
		qiDefectDescription.setInspectionPart2Location2Name(qiDefectResult.getInspectionPart2Location2Name());
		qiDefectDescription.setInspectionPart3Name(qiDefectResult.getInspectionPart3Name());
		qiDefectDescription.setDefectTypeName(qiDefectResult.getDefectTypeName());
		qiDefectDescription.setDefectTypeName2(qiDefectResult.getDefectTypeName2());
		qiDefectDescription.setResponsibleLevel1(qiDefectResult.getResponsibleLevel1());
		
		return qiDefectDescription;
	}
	
	public void updateCounts(QiDefectResult qiDefectResult) {
		int  defectStatusId = DefectStatus.getType(qiDefectResult.getCurrentDefectStatus()).getId();
		if(defectStatusId == DefectStatus.NON_REPAIRABLE.getId()) {
			scrapCount++;
		} else if (defectStatusId == DefectStatus.NOT_FIXED.getId() ||
				defectStatusId == DefectStatus.NOT_REPAIRED.getId()) {
			outstandingCount++;
		}else if(defectStatusId == DefectStatus.FIXED.getId() && qiDefectResult.getReportable() == 4) {
			npfCount++;
		} else if(defectStatusId == DefectStatus.NOT_FIXED_SCRAPPED.getId()) {
			notFixedScrapCount++;
		} else if(defectStatusId == DefectStatus.FIXED.getId()) {
			fixedCount++;
		} else return;
		totalCount++;
	}

	public long getDefectTransactionGroupId() {
		return this.defectTransactionGroupId;
	}

	public void setDefectTransactionGroupId(long defectTransactionGroupId) {
		this.defectTransactionGroupId = defectTransactionGroupId;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public QiDefectDescriptionDto getDefectTypeName() {
		return defectTypeName;
	}

	public void setDefectTypeName(QiDefectDescriptionDto defectTypeName) {
		this.defectTypeName = defectTypeName;
	}

	public int getScrapCount() {
		return this.scrapCount;
	}

	public void setScrapCount(int scrapCount) {
		this.scrapCount = scrapCount;
	}
	
	public int getNotFixedScrapCount() {
		return this.notFixedScrapCount;
	}

	public void setNotFixedScrapCount(int notFixedScrapCount) {
		this.notFixedScrapCount = notFixedScrapCount;
	}
	
	public int getOutstandingCount() {
		return this.outstandingCount;
	}

	public void setOutstandingCount(int outstandingCount) {
		this.outstandingCount = outstandingCount;
	}

	public int getFixedCount() {
		return this.fixedCount;
	}

	public void setFixedCount(int fixedCount) {
		this.fixedCount = fixedCount;
	}
	
	public int getNpfCount() {
		return this.npfCount;
	}
	
	public void setNpfCount(int npfCount) {
		this.npfCount = npfCount;
	}

	public int getTotalCount() {
		return this.totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
