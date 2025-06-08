package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.dto.DefectMapDto;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiEntryScreenId;
import com.honda.galc.entity.qi.QiExternalSystemDefectMap;
import com.honda.galc.service.IDaoService;


/**
 * 
 * <h3>QiExternalSystemDefectMapDao Class description</h3>
 * <p>
 * QiExternalSystemDefectMapDao description
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
 * Feb 13, 2017
 * 
 */

public interface QiExternalSystemDefectMapDao  extends IDaoService<QiExternalSystemDefectMap, Integer>{
	
	List<DefectMapDto> findAllExternalSystemDefect(String externalSystemName, String entryScreen, String entryModel);

	 List<DefectMapDto> findAllPartDefectCombByEntryScreenModelMenu(String entryScreen,String entryModel,String textEntryMenu);
	
	 QiExternalSystemDefectMap findByPartAndDefectCodeExternalSystem(String partCode, String defectCode,String externalSystemName);

	 QiExternalSystemDefectMap findByLocalCombIdAndExternalSystemName(String externalSystemName, Integer localDefectCombinationId);
	
	 List<DefectMapDto> findAllByEntryScreenAndModel(String entryScreenName,String entryModel, boolean isImageEntryScreen);
	
	 QiExternalSystemDefectMap findByExternalSystem(String externalSystemName, String externalSystemPartCode, String externalSystemDefectCode);
	 
	 List<QiExternalSystemDefectMap> findAllByLocalCombinationId(String localCombIdListValue) ;
	 
	 List<QiExternalSystemDefectMap> findAllByRegionalCombinationId(String regionalCombIdListValue);
	 
	 long findCountByRegionalAndScreenName(String entryScreen, String regionalIds);
	
	 String findOldAppIdByAppId(String appId) ;

	 String findOldImgByImg(String imgName) ;
	 
	 String findOldRepairMethodByRepairMethod(String repairMethod) ;
	 
	 String findOldThemeByTheme(String theme) ;
	 
	 String findOldRepairAreaByRepairArea(String repairArea) ;
	 
	 DefectMapDto findOldIqsByIqs(QiDefectResult qiDefectResult) ;
	 
	 DefectMapDto findOldEntryDeptByEntryDept(QiDefectResult qiDefectResult) ;
	
	 String findOldWriteUpByWriteUpAndResponsibility(QiDefectResult qiDefectResult) ;
	 
	 DefectMapDto findOldResponsiblityByResponsibility(QiDefectResult qiDefectResult);
	 
	 void updateLocalDefectCombinationId(int oldLocalDefectCombinationId, int newLocalDefectCombinationId, String userId);
	 public List<DefectMapDto> findAllModifiedHeadlessData(QiEntryScreenId fromEntryScreen, QiEntryScreenId toEntryScreen);
	 List<QiExternalSystemDefectMap> findAllByLocalDefectCombIds(Integer localId);
	 List<QiExternalSystemDefectMap> findAllNewByEntryScreenAndLocalDefectComb(QiEntryScreenId fromEntryScreenId,QiEntryScreenId toEntryScreenId, List<Integer>  regionalIds);

	void deleteLDCByPlantEntryScreenAndModel(String plantName, String entryScreen, String entryModel);

	void deleteLDCByPlantEntryScreeMenuAndModel(String plantName, String entryScreen, String menu, String entryModel);

	void deleteListByPlantEntryScreenMenuAndModel(String plantName, String entryScreen, String menu, String entryModel,
			String regionalIdList);

	QiExternalSystemDefectMap findByPartAndDefectCodeExternalSystemAndEntryModel(String partCode, String defectCode, String externalSystemName, String entryModel);

	void saveHeadlessMappings(List<QiExternalSystemDefectMap>  insertList);

	void deleteHeadlessMappings(List<QiExternalSystemDefectMap>  deleteList);
	
	public boolean isExternalSystemNameUsed(String externalSystemName);
	
	/**
	 * This method is used to update ENTRY_MODEL from an old entry model to a new entry model
	 */
	public void updateEntryModelName(String newEntryModel, String oldEntryModel, String userId);

}
