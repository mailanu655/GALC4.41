package com.honda.galc.dto.qi;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;

public class QiLocalDefectCombinationDto implements IDto{

	private static final long serialVersionUID = 1L;

	@DtoTag(outputName = "REGIONAL_DEFECT_COMBINATION_ID")
	private Integer regionalDefectCombinationId;

	@DtoTag(outputName = "ENTRY_SITE_NAME")
	private String entrySiteName;
	
	@DtoTag(outputName = "ENTRY_PLANT_NAME")
	private String entryPlantName;
	
	@DtoTag(outputName = "RESPONSIBLE_LEVEL_ID")
	private Integer responsibleLevelId;
	
	@DtoTag(outputName = "PDDA_RESPONSIBILITY_ID")
	private Integer pddaResponsibilityId;
	
	@DtoTag(outputName = "ENTRY_SCREEN")
	private String entryScreen;
	
	@DtoTag(outputName = "TEXT_ENTRY_MENU")
	private String textEntryMenu;
	
	@DtoTag(outputName = "REPAIR_METHOD")
	private String repairMethod;
	
	@DtoTag(outputName = "REPAIR_METHOD_TIME")
	private short repairMethodTime;
	
	@DtoTag(outputName = "ESTIMATED_TIME_TO_FIX")
	private Integer estimatedTimeToFix;
	
	@DtoTag(outputName = "LOCAL_THEME")
	private String localTheme;
	
	@DtoTag(outputName = "ENGINE_FIRING_FLAG")
	private short engineFiringFlag;
	
	@DtoTag(outputName = "REPAIR_AREA_NAME")
	private String repairAreaName;
	
	@DtoTag(outputName = "DEFECT_CATEGORY_NAME")
	private String defectCategoryName;
	
	@DtoTag(outputName = "REPORTABLE")
	private short reportable;
	

	public QiLocalDefectCombinationDto() {
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

}
