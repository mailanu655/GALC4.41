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
 * <h3>QiLocalDefectCombination Class description</h3>
 * <p> QiLocalDefectCombination description </p>
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
 *        Oct 06, 2016
 * 
 */
@Entity
@Table(name = "QI_LOCAL_DEFECT_COMBINATION_TBX")
public class QiLocalDefectCombination extends CreateUserAuditEntry {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "LOCAL_DEFECT_COMBINATION_ID")
	private Integer localDefectCombinationId;
	
	@Column(name = "REGIONAL_DEFECT_COMBINATION_ID")
	@Auditable
	private Integer regionalDefectCombinationId;
	
	@Column(name = "ENTRY_SITE_NAME")
	@Auditable
	private String entrySiteName;
	
	@Column(name = "ENTRY_PLANT_NAME")
	@Auditable
	private String entryPlantName;
	
	@Column(name = "RESPONSIBLE_LEVEL_ID")
	@Auditable
	private Integer responsibleLevelId;
	
	@Column(name = "PDDA_RESPONSIBILITY_ID")
	@Auditable
	private Integer pddaResponsibilityId;
	
	@Column(name = "ENTRY_MODEL")
	private String entryModel;
	
	@Column(name = "ENTRY_SCREEN")
	@Auditable
	private String entryScreen;
	
	@Column(name = "TEXT_ENTRY_MENU")
	@Auditable
	private String textEntryMenu;
	
	@Column(name = "REPAIR_METHOD")
	@Auditable
	private String repairMethod;
	
	@Column(name = "REPAIR_METHOD_TIME")
	@Auditable
	private short repairMethodTime;
	
	@Column(name = "ESTIMATED_TIME_TO_FIX")
	@Auditable
	private Integer estimatedTimeToFix;
	
	@Column(name = "LOCAL_THEME")
	@Auditable
	private String localTheme;
	
	@Column(name = "ENGINE_FIRING_FLAG")
	@Auditable
	private short engineFiringFlag;
	
	@Column(name = "REPAIR_AREA_NAME")
	@Auditable
	private String repairAreaName;
	
	@Column(name = "DEFECT_CATEGORY_NAME")
	@Auditable
	private String defectCategoryName;
	
	@Column(name = "REPORTABLE")
	@Auditable
	private short reportable;
	
	@Column(name = "IS_USED")
	private short isUsed;
	
	public Object getId() {
		return localDefectCombinationId;
	}

	public Integer getLocalDefectCombinationId() {
		return localDefectCombinationId;
	}

	public void setLocalDefectCombinationId(Integer localDefectCombinationId) {
		this.localDefectCombinationId = localDefectCombinationId;
	}

	public Integer getRegionalDefectCombinationId() {
		return regionalDefectCombinationId;
	}

	public void setRegionalDefectCombinationId(Integer regionalDefectCombinationId) {
		this.regionalDefectCombinationId = regionalDefectCombinationId;
	}

	public String getEntrySiteName() {
		return StringUtils.trimToEmpty(this.entrySiteName);
	}

	public void setEntrySiteName(String entrySiteName) {
		this.entrySiteName = entrySiteName;
	}

	public String getEntryPlantName() {
		return StringUtils.trimToEmpty(this.entryPlantName);
	}

	public void setEntryPlantName(String entryPlantName) {
		this.entryPlantName = entryPlantName;
	}

	public String getEntryScreen() {
		return StringUtils.trimToEmpty(this.entryScreen);
	}

	public void setEntryScreen(String entryScreen) {
		this.entryScreen = entryScreen;
	}

	public String getTextEntryMenu() {
		return StringUtils.trimToEmpty(this.textEntryMenu);
	}

	public void setTextEntryMenu(String textEntryMenu) {
		this.textEntryMenu = textEntryMenu;
	}

	public String getRepairMethod() {
		return StringUtils.trimToEmpty(this.repairMethod);
	}

	public void setRepairMethod(String repairMethod) {
		this.repairMethod = repairMethod;
	}

	public short getRepairMethodTime() {
		return repairMethodTime;
	}

	public void setRepairMethodTime(short repairMethodTime) {
		this.repairMethodTime = repairMethodTime;
	}

	public Integer getEstimatedTimeToFix() {
		return estimatedTimeToFix == null ? 0 : estimatedTimeToFix;
	}

	public void setEstimatedTimeToFix(Integer estimatedTimeToFix) {
		this.estimatedTimeToFix = estimatedTimeToFix;
	}

	public String getLocalTheme() {
		return StringUtils.trimToEmpty(this.localTheme);
	}

	public void setLocalTheme(String localTheme) {
		this.localTheme = localTheme;
	}

	public short getEngineFiringFlag() {
		return engineFiringFlag;
	}

	public void setEngineFiringFlag(short engineFiringFlag) {
		this.engineFiringFlag = engineFiringFlag;
	}

	public String getRepairAreaName() {
		return StringUtils.trimToEmpty(this.repairAreaName);
	}

	public void setRepairAreaName(String repairAreaName) {
		this.repairAreaName = repairAreaName;
	}
	
	public Integer getResponsibleLevelId() {
		return responsibleLevelId;
	}

	public void setResponsibleLevelId(Integer responsibleLevelId) {
		this.responsibleLevelId = responsibleLevelId;
	}

	public String getDefectCategoryName() {
		return StringUtils.trimToEmpty(this.defectCategoryName);
	}

	public void setDefectCategoryName(String defectCategoryName) {
		this.defectCategoryName = defectCategoryName;
	}
	
	public Integer getPddaResponsibilityId() {
		return pddaResponsibilityId;
	}

	public void setPddaResponsibilityId(Integer pddaResponsibilityId) {
		this.pddaResponsibilityId = pddaResponsibilityId;
	}
	
	public short getReportable() {
		return reportable;
	}

	public void setReportable(short reportable) {
		this.reportable = reportable;
	}
	
	public short getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(short isUsed) {
		this.isUsed = isUsed;
	}
	
	public boolean isUsed() {
		return this.isUsed ==(short) 1;
	}
	
	

	public String getEntryModel() {
		return StringUtils.trimToEmpty(entryModel);
	}

	public void setEntryModel(String entryModel) {
		this.entryModel = entryModel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((defectCategoryName == null) ? 0 : defectCategoryName
						.hashCode());
		result = prime * result + engineFiringFlag;
		result = prime * result
				+ ((entryPlantName == null) ? 0 : entryPlantName.hashCode());
		result = prime * result + ((entryModel == null) ? 0 : entryModel.hashCode());
		result = prime * result
				+ ((entryScreen == null) ? 0 : entryScreen.hashCode());
		result = prime * result
				+ ((entrySiteName == null) ? 0 : entrySiteName.hashCode());
		result = prime
				* result
				+ ((estimatedTimeToFix == null) ? 0 : estimatedTimeToFix
						.hashCode());
		result = prime
				* result
				+ ((localDefectCombinationId == null) ? 0
						: localDefectCombinationId.hashCode());
		result = prime
				* result
				+ ((pddaResponsibilityId == null) ? 0 : pddaResponsibilityId
						.hashCode());
		result = prime
				* result
				+ ((regionalDefectCombinationId == null) ? 0
						: regionalDefectCombinationId.hashCode());
		result = prime * result
				+ ((repairAreaName == null) ? 0 : repairAreaName.hashCode());
		result = prime * result
				+ ((repairMethod == null) ? 0 : repairMethod.hashCode());
		result = prime * result + repairMethodTime;
		result = prime * result + reportable;
		result = prime
				* result
				+ ((responsibleLevelId == null) ? 0 : responsibleLevelId
						.hashCode());
		result = prime
				* result
				+ ((localTheme == null) ? 0 : localTheme
						.hashCode());
		result = prime * result
				+ ((textEntryMenu == null) ? 0 : textEntryMenu.hashCode());
		result = prime * result + isUsed;
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
		QiLocalDefectCombination other = (QiLocalDefectCombination) obj;
		if (defectCategoryName == null) {
			if (other.defectCategoryName != null)
				return false;
		} else if (!defectCategoryName.equals(other.defectCategoryName))
			return false;
		if (engineFiringFlag != other.engineFiringFlag)
			return false;
		if (entryPlantName == null) {
			if (other.entryPlantName != null)
				return false;
		} else if (!entryPlantName.trim().equals(other.entryPlantName.trim()))
			return false;
		if (entryModel == null) {
			if (other.entryModel != null)
				return false;
		} else if (!entryModel.equals(other.entryModel))
			return false;
		if (entryScreen == null) {
			if (other.entryScreen != null)
				return false;
		} else if (!entryScreen.equals(other.entryScreen))
			return false;
		if (entrySiteName == null) {
			if (other.entrySiteName != null)
				return false;
		} else if (!entrySiteName.trim().equals(other.entrySiteName.trim()))
			return false;
		if (estimatedTimeToFix == null) {
			if (other.estimatedTimeToFix != null)
				return false;
		} else if (!estimatedTimeToFix.equals(other.estimatedTimeToFix))
			return false;
		if (isUsed != other.isUsed)
			return false;
		if (localDefectCombinationId == null) {
			if (other.localDefectCombinationId != null)
				return false;
		} else if (!localDefectCombinationId
				.equals(other.localDefectCombinationId))
			return false;
		if (pddaResponsibilityId == null) {
			if (other.pddaResponsibilityId != null)
				return false;
		} else if (!pddaResponsibilityId.equals(other.pddaResponsibilityId))
			return false;
		if (regionalDefectCombinationId == null) {
			if (other.regionalDefectCombinationId != null)
				return false;
		} else if (!regionalDefectCombinationId
				.equals(other.regionalDefectCombinationId))
			return false;
		if (repairAreaName == null) {
			if (other.repairAreaName != null)
				return false;
		} else if (!repairAreaName.trim().equals(other.repairAreaName.trim()))
			return false;
		if (repairMethod == null) {
			if (other.repairMethod != null)
				return false;
		} else if (!repairMethod.equals(other.repairMethod))
			return false;
		if (repairMethodTime != other.repairMethodTime)
			return false;
		if (reportable != other.reportable)
			return false;
		if (responsibleLevelId == null) {
			if (other.responsibleLevelId != null)
				return false;
		} else if (!responsibleLevelId.equals(other.responsibleLevelId))
			return false;
		if (localTheme == null) {
			if (other.localTheme != null)
				return false;
		} else if (!localTheme.equals(other.localTheme))
			return false;
		if (textEntryMenu == null) {
			if (other.textEntryMenu != null)
				return false;
		} else if (!textEntryMenu.trim().equals(other.textEntryMenu.trim()))
			return false;
		return true;
	}
}
