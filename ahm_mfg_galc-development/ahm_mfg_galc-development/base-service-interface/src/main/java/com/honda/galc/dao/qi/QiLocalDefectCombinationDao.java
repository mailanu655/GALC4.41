package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.dto.qi.QiLocalDefectCombinationDto;
import com.honda.galc.entity.qi.QiEntryScreenId;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>QiLocalDefectCombinationDao Class description</h3>
 * <p>
 * QiLocalDefectCombinationDao description
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
 * @author L&T Infotech<br>
 *         Sep 22 2016
 * 
 * 
 */
public interface QiLocalDefectCombinationDao extends IDaoService<QiLocalDefectCombination, Integer> {

	public QiLocalDefectCombination findByRegionalDefectCombinationId(Integer regionalDefectCombinationId, String entryScreen, String entryModel, short version);
	public QiLocalDefectCombination findByRegionalDefectCombinationIdAndEntryScreen(Integer regionalDefectCombinationId, String entryScreen);
	public List<QiLocalDefectCombination> findAllByEntryScreenAndModel(String entryScreen, String entryModel, short version);
	public void updateEntryScreenAndModel(QiEntryScreenId newEntryScreen, String oldEntryScreen, String oldEntryModel, short oldVersion, String userID);
	public List<QiLocalDefectCombination> findAllByRepairMethod(String repairMethodName);
	public void setAllLocalDefectCombinationsToUnassigned(String oldRepairMethodName, String userId);
	public void updateAllByRepairMethod(String repairMethodName,String oldRepairMethodName,String updateUser);
    public List<QiLocalDefectCombination> findAllLocalAttributesByPartDefectId(List<Integer> partDefectIdList);
	public List<QiLocalDefectCombination> findAllLocalAttributesByPartLocationId(List<Integer> partLocationIdList);
	public List<QiLocalDefectCombination> findAllByRegionalDefectCombinationId(Integer regionalDefectCombinationId);
	public boolean isRepairAreaUsed(String repairAreaName);
	public void updateAllByRepairArea(String repairMethodName,String oldRepairMethodName,String updateUser);
	public List<QiLocalDefectCombination> findAllByTextEntryMenu(String entryScreen, String textEntryMenu);
	public List<QiLocalDefectCombination> findAllByTextMenuAndEntryScreen(String entryScreen, String textEntryMenu, String entryModel);
	
	public List<QiLocalDefectCombination> findAllByPlantAndSite(String plant,String siteName);
	public List<QiLocalDefectCombination> findAllBySite(String siteName);
	public List<QiDefectResultDto> findAllPartDefectCombByDefectEntryTextFilter(String entryScreen, String entryModel, String inspectionPartName, String mainPartNo);
	public List<QiDefectResultDto> findAllPartDefectCombByDefectEntryImageFilter(String imageName, String entryScreen, String entryModel, String inspectionPartName, String mainPartNo);
	
	public boolean isLocalThemeInUseByLocalDefect(String localTheme);
	public void updateLocalThemeForLocalDefects(String newLocalTheme, String userId, String oldLocalTheme);
	
	public long findCountByRegionalAndScreenName(String entryScreen, String join);
	public List<QiLocalDefectCombination> findAllLocalDefectCombinationsByLocalDefectId(QiEntryScreenId fromEntryScreenId, QiEntryScreenId toEntryScreenId,
			List<Integer> regionalDefectCombList);
	public  Integer  findMappingOfMatchingLocalDefectsIds(QiEntryScreenId fromEntryScreenId, QiEntryScreenId toEntryScreenId,
			Integer regionalId);
	
	public List<QiLocalDefectCombinationDto> findAllModifiedByEntryScreens(QiEntryScreenId fromEntryScreenId, QiEntryScreenId toEntryScreenId);
	
	public List<QiLocalDefectCombination> findAllByEntryModelAndIsUsed(String entryModel, short version);
	public void updateVersionValue(String entryModel, short oldversion, short newVersion);
	public void updateEntryModelName(String newEntryModel, String userId, String oldEntryModel, short isUsed);
	public void removeByEntryModelAndVersion(String entryModel,  short version);
	public void removeByEntryScreenModelAndVersion(String entryScreen, String entryModel, short version);
	public List<QiLocalDefectCombination> findAllByLocalDefectCombination(QiLocalDefectCombination localCombination, short version);
	public List<QiLocalDefectCombination> findAllByPddaResponsibilityId(Integer pddaResponsibilityId);
	public List<QiLocalDefectCombination> findAllBySiteAndPlant(String site, String plantName);
	public List<QiLocalDefectCombination> findAllBySitePlantAndDepartment(String site, String plantName, String department);
	public List<QiLocalDefectCombination> findAllByResponsibleLevel(String site, 
			String plantName, String department, String responsibility, short level);
	long countByResponsibleLevelId(int responsibleLevelId);
	long countByResponsibleLevel3(int responsibleLevelId);
	long countByResponsibleLevel2(int responsibleLevelId);
	void deleteLDCByPlantEntryScreenAndModel(String plantName, String entryScreen, String entryModel);
	void deleteLDCByPlantEntryScreenMenuAndModel(String plantName, String entryScreen, String menu, String entryModel);
	void deleteListByPlantEntryScreenMenuAndModel(String plantName, String entryScreen, String menu, String entryModel, String regionalIdList);
	public List<QiLocalDefectCombination> findAllByPlantEntryScreenModelMenuAndRegionalId(Integer regionalDefectCombinationId, String plant, String entryScreen,
			String entryModel, String entryMenu, short version);
	public List<QiLocalDefectCombination> findFirstXByPlantAndModel(String entryModel, String plant, int lastHighId);
	long countByEntryModelAndPlant(String entryPlant, String entryModel);

}
