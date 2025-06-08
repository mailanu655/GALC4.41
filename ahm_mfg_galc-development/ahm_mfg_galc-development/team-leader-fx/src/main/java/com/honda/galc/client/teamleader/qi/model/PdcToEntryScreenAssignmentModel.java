package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.dao.qi.QiEntryModelDao;
import com.honda.galc.dao.qi.QiEntryScreenDao;
import com.honda.galc.dao.qi.QiEntryScreenDefectCombinationDao;
import com.honda.galc.dao.qi.QiExternalSystemDefectMapDao;
import com.honda.galc.dao.qi.QiImageSectionDao;
import com.honda.galc.dao.qi.QiLocalDefectCombinationDao;
import com.honda.galc.dao.qi.QiPartDefectCombinationDao;
import com.honda.galc.dao.qi.QiTextEntryMenuDao;
import com.honda.galc.dto.DefectMapDto;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.dto.qi.QiLocalDefectCombinationDto;
import com.honda.galc.dto.qi.QiRegionalPartDefectLocationDto;
import com.honda.galc.entity.qi.QiEntryScreenDefectCombination;
import com.honda.galc.entity.qi.QiEntryScreenDefectCombinationId;
import com.honda.galc.entity.qi.QiEntryScreenId;
import com.honda.galc.entity.qi.QiExternalSystemDefectMap;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiTextEntryMenu;
import com.honda.galc.entity.qi.QiTextEntryMenuId;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>QIMaintenanceController Class description</h3>
 * <p> QIMaintenanceController description </p>
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
 * April 20, 2016
 *
 *
 */
public class PdcToEntryScreenAssignmentModel extends QiModel {



	public PdcToEntryScreenAssignmentModel() {
		super();
	}


	public String getSiteName() {
		return PropertyService.getPropertyBean(SystemPropertyBean.class).getSiteName();
	}


	public List<String> getAllProductType() {
		return getDao(ProductTypeDao.class).findAllProductTypes();
	}


	public List<String> getEntryModelForProductType(String selectedProductType) {
		return getDao(QiEntryModelDao.class).findEntryModelsByProductType(selectedProductType);
	}


	public List<QiEntryScreenDto> getEntryScreenData(String entryModel,String plant) {
		return getDepartmentIdValues(getDao(QiEntryScreenDao.class).findEntryScreenData(entryModel,plant));
	}

	public List<QiEntryScreenDto> getEntryScreenDataByImage(String entryModel,String plant, boolean isImage) {
		return getDepartmentIdValues(getDao(QiEntryScreenDao.class).findEntryScreenData(entryModel,plant, isImage));
	}


	private List<QiEntryScreenDto> getDepartmentIdValues(List<QiEntryScreenDto> qiEntryScreenDtoList) {
		Map<String, QiEntryScreenDto> qiEntryScreenDtosTempMap = new HashMap <String, QiEntryScreenDto>();
		for (int i = 0; i < qiEntryScreenDtoList.size(); i++) {
			if (qiEntryScreenDtosTempMap.get(qiEntryScreenDtoList.get(i).getEntryScreen()+qiEntryScreenDtoList.get(i).getEntryModel()  + qiEntryScreenDtoList.get(i).getIsUsedVersion()) == null) {
				qiEntryScreenDtosTempMap.put(qiEntryScreenDtoList.get(i).getEntryScreen()+qiEntryScreenDtoList.get(i).getEntryModel()  + qiEntryScreenDtoList.get(i).getIsUsedVersion(), qiEntryScreenDtoList.get(i));
				continue;
			} else {
				QiEntryScreenDto dto = qiEntryScreenDtosTempMap.get(qiEntryScreenDtoList.get(i).getEntryScreen()+qiEntryScreenDtoList.get(i).getEntryModel()  + qiEntryScreenDtoList.get(i).getIsUsedVersion());
				dto.setDivisionId(dto.getDivisionId().trim() + "," + qiEntryScreenDtoList.get(i).getDivisionId().trim());
			}
		}
		ArrayList<QiEntryScreenDto> entryScreenList = new ArrayList<QiEntryScreenDto>(qiEntryScreenDtosTempMap.values());
		Collections.sort(entryScreenList);
		return entryScreenList;
	}


	public List<QiTextEntryMenu> getMenuDetailsByEntryScreen(QiEntryScreenDto qiEntryScreenDto) {
		if(null!= qiEntryScreenDto && QiConstant.TEXT.equalsIgnoreCase(qiEntryScreenDto.getImageValue())){
			return getDao(QiTextEntryMenuDao.class).findAllMenuDetailsByIsUsedAndEntryScreen(qiEntryScreenDto.getEntryScreen(), 
					qiEntryScreenDto.getEntryModel(), qiEntryScreenDto.getIsUsedVersion());
		}
		return new ArrayList<QiTextEntryMenu>();
	}



	public QiTextEntryMenu createTextEntryMenu(QiTextEntryMenu textEntryMenu) {
		return getDao(QiTextEntryMenuDao.class).insert(textEntryMenu);
	}


	public List<QiRegionalPartDefectLocationDto> getPartDefectDetails(String filter, String productkind) {
		return getDao(QiPartDefectCombinationDao.class).getPartDefectDetails(filter,productkind);
	}


	public List<QiEntryScreenDto>  getUsedEntryModelData(String entryModel,String plant) {
		return getDepartmentIdValues(getDao(QiEntryScreenDao.class).getUsedEntryModelData(entryModel,plant));
	}


	public List<QiEntryScreenDto>  getNotUsedEntryModelData(String entryModel,String plant) {
		return getDepartmentIdValues(getDao(QiEntryScreenDao.class).getNotUsedEntryModelData(entryModel,plant));
	}


	public void updateTextEntryMenu(QiTextEntryMenu qiTextEntryMenu, String oldTextEntryMenu) {
		getDao(QiTextEntryMenuDao.class).updateTextEntryMenu(qiTextEntryMenu,oldTextEntryMenu);
	}

	public void updateTextEntryMenu(QiTextEntryMenu qiTextEntryMenu) {
		getDao(QiTextEntryMenuDao.class).updateTextEntryMenu(qiTextEntryMenu);
	}

	public void deleteTextEntryMenu(QiTextEntryMenu selectedComb) {
		getDao(QiTextEntryMenuDao.class).removeSelectedTextEntry(selectedComb);
	}


	public List<QiEntryScreenDefectCombination> saveAssignedEntryScreenPdcList(
			List<QiEntryScreenDefectCombination> entryScreenDefectCombinationsList) {
		return getDao(QiEntryScreenDefectCombinationDao.class).saveAll(entryScreenDefectCombinationsList);
	}
	
	public void removeDeAssignedEntryScreenPdcList(
			List<QiEntryScreenDefectCombination> entryScreenDefectCombinationsList) {
		getDao(QiEntryScreenDefectCombinationDao.class).removeAll(entryScreenDefectCombinationsList);
	}

	public void deleteLDCList(
			List<QiLocalDefectCombination> ldcList) {
		getDao(QiLocalDefectCombinationDao.class).removeAll(ldcList);
	}

	public void deleteExtSysMapList(
			List<QiExternalSystemDefectMap> extList) {
		getDao(QiExternalSystemDefectMapDao.class).removeAll(extList);
	}

	public void moveHeadlessMappings(List<QiExternalSystemDefectMap> insertList, List<QiExternalSystemDefectMap> deleteList) {
		getDao(QiExternalSystemDefectMapDao.class).deleteHeadlessMappings(deleteList);
		getDao(QiExternalSystemDefectMapDao.class).saveHeadlessMappings(insertList);
	}

	public List<QiRegionalPartDefectLocationDto> getAssignedPartDefectDetails(String filterData,
			String productKind, String entryScreen, String selectedTextEntryMenu, String entryModel, short isUsedVersion) {

		if(!"".equals(selectedTextEntryMenu)){
			return getDao(QiPartDefectCombinationDao.class).getAssignedDefectDetails(filterData, productKind, entryScreen, selectedTextEntryMenu, entryModel, isUsedVersion);

		}else{
			return getDao(QiPartDefectCombinationDao.class).getAssignedDefectDetails(filterData, productKind, entryScreen, entryModel, isUsedVersion);
		}
	}


	public void deleteAssignedPartList(
			QiEntryScreenDto entryModelDto, QiTextEntryMenu textEntryMenu) {
		if(null!=textEntryMenu && null!=textEntryMenu.getId().getTextEntryMenu()){
			getDao(QiEntryScreenDefectCombinationDao.class).removeByTextEntryId(textEntryMenu.getId());
		}else{
			getDao(QiEntryScreenDefectCombinationDao.class).removeByEntryScreenModelAndVersion(entryModelDto.getEntryScreen(), 
					entryModelDto.getEntryModel(), entryModelDto.getIsUsedVersion());
		}
	}

	public boolean isTextEntryMenuExist(QiTextEntryMenuId qiTextEntryMenu) {
		boolean isMenuExist = false;
		QiTextEntryMenu textEntryMenu = getDao(QiTextEntryMenuDao.class).findByKey(qiTextEntryMenu);
		if(null!=textEntryMenu){
			isMenuExist = true;
		}
		return isMenuExist;
	}


	public List<QiRegionalPartDefectLocationDto> getPartDefectDetails(String filter, String productkind, QiEntryScreenDto qiEntryScreenDto) {
		if(null!=qiEntryScreenDto){
			if(null!=qiEntryScreenDto && QiConstant.IMAGE.equalsIgnoreCase(qiEntryScreenDto.getImageValue())){
				return getDao(QiPartDefectCombinationDao.class).getPartDefectDetailsByImage(filter,productkind,qiEntryScreenDto);
			}else{
				return getDao(QiPartDefectCombinationDao.class).getPartDefectDetailsByText(filter,productkind,qiEntryScreenDto);
			}
		}else{
			return getDao(QiPartDefectCombinationDao.class).getPartDefectDetails(filter,productkind);
		}
	}


	public List<String> getPlantForSite(String siteName) {
		return getDao(DivisionDao.class).findPlantBySite(siteName);
	}
	
	public void updateEntryScreenDefectCombination(QiTextEntryMenu qiTextEntryMenu,String oldTextEntryMenu) {
		getDao(QiEntryScreenDefectCombinationDao.class).updateTextEntryMenuName(qiTextEntryMenu.getId(), oldTextEntryMenu, getUserId());	
	}

	public void updateAllEntryScreenDefectCombination(List<QiEntryScreenDefectCombination> pdcList) {
		getDao(QiEntryScreenDefectCombinationDao.class).updateAll(pdcList);	
	}

	/**
	 * This method is used to derive combination of primary key values for audit
	 * purpose.
	 * 
	 * @param regionalDefectCombinationId
	 * @param entryScreenName
	 * @return auditPrimaryKeyValue
	 */
	public String getAuditPrimaryKeyValue(int regionalDefectCombinationId, String entryScreenName, String entryModel) {
		return getDao(QiEntryScreenDefectCombinationDao.class).fetchAuditPrimaryKeyValue(regionalDefectCombinationId, entryScreenName, entryModel);
	}
	
	public QiLocalDefectCombination getAssociatedAttributesByEntryScreenAndModel(Integer regionalCombId, QiEntryScreenDto entryScreenDto) {
		return getDao(QiLocalDefectCombinationDao.class).findByRegionalDefectCombinationId(regionalCombId, 
				entryScreenDto.getEntryScreen(), entryScreenDto.getEntryModel(), entryScreenDto.getIsUsedVersion());
	}


	public void deleteLocalDefectCombination(List<QiLocalDefectCombination> pdcCombList) {
		getDao(QiLocalDefectCombinationDao.class).removeAll(pdcCombList);

	}
	
	/**
	 * This method finds external mapping data based on local comb id
	 * @param localCombinationId
	 * @return List<QiExternalSystemDefectMap>ExternalSystemDefectMap
	 */
	public List<QiExternalSystemDefectMap> findAllByLocalCombinationId(List<Integer> localCombinationIdList) {
		String localCombIdListValue=StringUtils.join(localCombinationIdList, ',');
		return getDao(QiExternalSystemDefectMapDao.class).findAllByLocalCombinationId(localCombIdListValue);
	}
	
	/**
	 * This method finds all associations of text entry menu with local combination id
	 * @param entryScreen
	 * @param entryModel 
	 * @param textEntryMenu
	 * @return List<QiLocalDefectCombination>
	 */
	public List<QiLocalDefectCombination> findAllByTextMenuAndEntryScreen(String entryScreen, String textEntryMen, String entryModel) {
		return getDao(QiLocalDefectCombinationDao.class).findAllByTextMenuAndEntryScreen(entryScreen,textEntryMen,entryModel);
	}
	
	/**
	 * This method updates all associations of text entry menu in local combination table
	 * @return List<QiLocalDefectCombination>
	 * @param textEntryMenu
	 */
	public void updateAllLocalCombId(List<QiLocalDefectCombination> localDefectCombinationList ,String textEntryMenu) {
		for(QiLocalDefectCombination localDefectCombination : localDefectCombinationList)
	    	localDefectCombination.setTextEntryMenu(textEntryMenu); 
		getDao(QiLocalDefectCombinationDao.class).updateAll(localDefectCombinationList);
	}

	public void saveAllAssignedPDCs(List<QiEntryScreenDefectCombination> entryScreenDefectCombinationsList) {
		getDao(QiEntryScreenDefectCombinationDao.class).insertAll(entryScreenDefectCombinationsList);
	}


	public List<Integer> findAllPartLocationIdsWithValidSectionByImageName(String entryScreen, String imageName) {
		return getDao(QiImageSectionDao.class).findAllPartLocationIdsWithValidSectionByImageName(entryScreen, imageName);
	}
	

	

	public long findLocalAttributeCountByRegionalAndScreenName(String entryScreen, List<Integer> regionalIdList) {
		return getDao(QiLocalDefectCombinationDao.class).findCountByRegionalAndScreenName(entryScreen, StringUtils.join(regionalIdList, ','));
	}


	public long findHeadlessCountByRegionalAndScreenName(String entryScreen, List<Integer> regionalIdList) {
		return getDao(QiExternalSystemDefectMapDao.class).findCountByRegionalAndScreenName(entryScreen, StringUtils.join(regionalIdList, ','));
	}


	public QiLocalDefectCombination findLocalDefectCombIdByEntryScreenAndRegionalId(Integer regionalDefectCombinationId, QiEntryScreenDto entryScreenDto) {
		return getDao(QiLocalDefectCombinationDao.class).findByRegionalDefectCombinationId(regionalDefectCombinationId, 
				entryScreenDto.getEntryScreen(), entryScreenDto.getEntryModel(), entryScreenDto.getIsUsedVersion());
	}

	public List<QiLocalDefectCombination> findAllLocalDefectCombByPlantEntryScreenModelMenuAndRegionalId(Integer regionalDefectCombinationId, QiEntryScreenDto entryScreenDto, String entryMenu)  {
		return getDao(QiLocalDefectCombinationDao.class).findAllByPlantEntryScreenModelMenuAndRegionalId(regionalDefectCombinationId, 
				entryScreenDto.getPlantName(), entryScreenDto.getEntryScreen(), entryScreenDto.getEntryModel(), entryMenu, entryScreenDto.getIsUsedVersion());
		
	}
	
	public List<QiExternalSystemDefectMap> findAllExtSysMapByLocalDefectCombId(Integer localId)  {
		return getDao(QiExternalSystemDefectMapDao.class).findAllByLocalDefectCombIds(localId);		
	}
	
	public void updateAttributes(List<QiLocalDefectCombination> defectCombinations){
		getDao(QiLocalDefectCombinationDao.class).updateAll(defectCombinations);
	}
	
	public QiTextEntryMenu findTextEntryByKey(QiTextEntryMenuId qiTextEntryMenu) {
		return getDao(QiTextEntryMenuDao.class).findByKey(qiTextEntryMenu);
	}
	
	public List<QiEntryScreenDto> getEntryScreenData(String plant,String productType, boolean isImage) {
		List<QiEntryScreenDto> entryScreenDto = getDepartmentIdValues(getDao(QiEntryScreenDao.class).findAllByPlantAndProductType(plant, productType, isImage));
		return removeVersionRecords(entryScreenDto);
	}
	
	public List<QiLocalDefectCombinationDto> findModifiedLocalAttributesByEntryScreens(QiEntryScreenId fromEntryScreenId, QiEntryScreenId toEntryScreenId) {
		return getDao(QiLocalDefectCombinationDao.class).findAllModifiedByEntryScreens(fromEntryScreenId, toEntryScreenId);
	}
	
	public List<DefectMapDto> findAllModifiedHeadlessData(QiEntryScreenId fromEntryScreen, QiEntryScreenId toEntryScreen) {
		return getDao(QiExternalSystemDefectMapDao.class).findAllModifiedHeadlessData(fromEntryScreen, toEntryScreen);
	}

	public void copyHeadlessCodes(List<QiExternalSystemDefectMap> HeadlessCodsList){
		getDao(QiExternalSystemDefectMapDao.class).insertAll(HeadlessCodsList);
	}
	
	public List<QiLocalDefectCombination> findAllLocalDefectCombinationsByLocalDefectId(QiEntryScreenId fromEntryScreenId, QiEntryScreenId toEntryScreenId,
			List<Integer> regionalDefectCombList) {
		return getDao(QiLocalDefectCombinationDao.class).findAllLocalDefectCombinationsByLocalDefectId(fromEntryScreenId, toEntryScreenId, regionalDefectCombList);
	}

	public QiLocalDefectCombination copyLocalDefectCombination(QiLocalDefectCombination defectCombination){
		return getDao(QiLocalDefectCombinationDao.class).save(defectCombination);
	}

	public List<QiExternalSystemDefectMap> findExtSystemByLocalDefectCombId(Integer localId) {
		return getDao(QiExternalSystemDefectMapDao.class).findAllByLocalDefectCombIds(localId);
	}

	public List<QiExternalSystemDefectMap> findNewHeadlessByEntryScreenAndRegionalIds(QiEntryScreenId fromEntryScreenId, QiEntryScreenId toEntryScreenId, List<Integer> regionalIdsList) {
		return getDao(QiExternalSystemDefectMapDao.class).findAllNewByEntryScreenAndLocalDefectComb(fromEntryScreenId, toEntryScreenId, regionalIdsList);
	}
	
	public Integer findMappingOfMatchingLocalDefectsIds(QiEntryScreenId fromEntryScreenId, QiEntryScreenId toEntryScreenId,
			Integer regionalDefectCombList){
		return getDao(QiLocalDefectCombinationDao.class).findMappingOfMatchingLocalDefectsIds(fromEntryScreenId, toEntryScreenId, regionalDefectCombList);
	}
	
	/**
	 * This method is used to check version created or not.
	 */
	public boolean isVersionCreated(String entryModel) {
		return (getDao(QiEntryModelDao.class).findEntryModelCount(entryModel) > 1);
	}
	
	public List<QiEntryScreenDto> removeVersionRecords(List<QiEntryScreenDto> entryScreenDtoList) {
		
		Map<String, QiEntryScreenDto> qiEntryScreenDtosTempMap = new HashMap <String, QiEntryScreenDto>();
		List<QiEntryScreenDto> currentVersionList = new ArrayList<QiEntryScreenDto>();
		for(QiEntryScreenDto entryScreenDto : entryScreenDtoList) {
			if(entryScreenDto.getIsUsedVersion() == 0) {
				if(qiEntryScreenDtosTempMap.get(entryScreenDto.getEntryScreen() + entryScreenDto.getEntryModel() 
				+ entryScreenDto.getIsUsedVersion()) == null) {
					qiEntryScreenDtosTempMap.put(entryScreenDto.getEntryScreen() + entryScreenDto.getEntryModel() 
				+ entryScreenDto.getIsUsedVersion(), entryScreenDto);
				}
				
			} else {
				currentVersionList.add(entryScreenDto);
			}
		}
		
		for(QiEntryScreenDto entryScreenDto : currentVersionList) {
			if(qiEntryScreenDtosTempMap.get(entryScreenDto.getEntryScreen() + entryScreenDto.getEntryModel() 
			+ (short)0) == null) {
				qiEntryScreenDtosTempMap.put(entryScreenDto.getEntryScreen() + entryScreenDto.getEntryModel() 
				+ entryScreenDto.getIsUsedVersion(), entryScreenDto);
			}
		}
		
		ArrayList<QiEntryScreenDto> entryScreenList = new ArrayList<QiEntryScreenDto>(qiEntryScreenDtosTempMap.values());
		Collections.sort(entryScreenList);
		return entryScreenList;
	}
	
	public void deleteLDCByPlantEntryScreenAndModel(String plantName, String entryScreen, String entryModel)  {
		getDao(QiLocalDefectCombinationDao.class).deleteLDCByPlantEntryScreenAndModel(plantName, entryScreen, entryModel);		
	}

	public void deleteExternalSystemMapByPlantEntryScreenAndModel(String plantName, String entryScreen, String entryModel)  {
		getDao(QiExternalSystemDefectMapDao.class).deleteLDCByPlantEntryScreenAndModel(plantName, entryScreen, entryModel);		
	}
	
	public void deleteLDCByPlantEntryScreenMenuAndModel(String plantName, String entryScreen, String menu, String entryModel)  {
		getDao(QiLocalDefectCombinationDao.class).deleteLDCByPlantEntryScreenMenuAndModel(plantName, entryScreen, menu, entryModel);		
	}

	public void deleteExternalSystemMapByPlantEntryScreenMenuAndModel(String plantName, String entryScreen,  String menu, String entryModel)  {
		getDao(QiExternalSystemDefectMapDao.class).deleteLDCByPlantEntryScreeMenuAndModel(plantName, entryScreen, menu, entryModel);		
	}
	public void deleteLDCListByPlantEntryScreenMenuModelAndRegionalIdList(String plantName, QiTextEntryMenu entryMenu, List<QiRegionalPartDefectLocationDto> pdcList)  {
		String regionalIdList = buildRegionalIdList(pdcList);
		if(regionalIdList == null)  return;
		getDao(QiLocalDefectCombinationDao.class).deleteListByPlantEntryScreenMenuAndModel(
				plantName, entryMenu.getId().getEntryScreen(), entryMenu.getId().getTextEntryMenu(),
				entryMenu.getId().getEntryModel(),regionalIdList);		
	}

	public void deleteExternalSystemMapListByPlantEntryScreenMenuModelAndRegionalIdList(String plantName, QiTextEntryMenu entryMenu, List<QiRegionalPartDefectLocationDto> pdcList)  {
		String regionalIdList = buildRegionalIdList(pdcList);
		if(regionalIdList == null)  return;
		getDao(QiExternalSystemDefectMapDao.class).deleteListByPlantEntryScreenMenuAndModel(
				plantName, entryMenu.getId().getEntryScreen(), entryMenu.getId().getTextEntryMenu(),
				entryMenu.getId().getEntryModel(),regionalIdList);		
	}
	
	public QiEntryScreenDefectCombination findEntryScreenByKey(String entryScreen, String entryModel, int regionalId, short isUsed)  {
		QiEntryScreenDefectCombinationId id = new QiEntryScreenDefectCombinationId();
		id.setEntryScreen(entryScreen);
		id.setEntryModel(entryModel);
		id.setIsUsed(isUsed);
		id.setRegionalDefectCombinationId(regionalId);
		return getDao(QiEntryScreenDefectCombinationDao.class).findByKey(id);
	}
	
	public void deletePDCByPlantEntryScreenMenuAndModel(String plantName, String entryScreen,
			String menu, String entryModel, short isUserVersion)
	{
		getDao(QiEntryScreenDefectCombinationDao.class).deleteByEntryScreenModelMenuAndVersion(plantName, entryScreen, entryModel, menu, isUserVersion);
	}
	
	public void deletePDCByPlantEntryScreenAndModel(String plantName, String entryScreen, String entryModel, short isUserVersion)
	{
		getDao(QiEntryScreenDefectCombinationDao.class).deleteByEntryScreenModelAndVersion(plantName, entryScreen, entryModel, isUserVersion);
	}
	
	public String buildRegionalIdList(List<QiRegionalPartDefectLocationDto> pdcList)  {
		if(pdcList == null || pdcList.isEmpty())  return null;
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < pdcList.size(); i++)  {
			QiRegionalPartDefectLocationDto thisDto = pdcList.get(i);
			if(thisDto != null)  {
				sb.append(thisDto.getRegionalDefectCombinationId());
				if((i+1) < pdcList.size())  {
					sb.append(",");
				}
			}
		}
		return sb.toString();
	}
	
	public QiExternalSystemDefectMap findByPartAndDefectCodeExternalSystemAndEntryModel(String partCode,String defectCode, String externalSystemName, String entryModel) {
		QiExternalSystemDefectMap mapEntry = 
				getDao(QiExternalSystemDefectMapDao.class).findByPartAndDefectCodeExternalSystemAndEntryModel(partCode, defectCode, externalSystemName, entryModel);
		return mapEntry;
	}
}

