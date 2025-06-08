package com.honda.galc.service.datacollection;


import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.ProcessPointGroupDao;
import com.honda.galc.dao.qi.QiEntryScreenDao;
import com.honda.galc.dao.qi.QiEntryScreenDefectCombinationDao;
import com.honda.galc.dao.qi.QiExternalSystemDefectMapDao;
import com.honda.galc.dao.qi.QiImageSectionDao;
import com.honda.galc.dao.qi.QiLocalDefectCombinationDao;
import com.honda.galc.dao.qi.QiStationRepairMethodDao;
import com.honda.galc.entity.conf.RegionalProcessPointGroup;
import com.honda.galc.entity.qi.QiEntryScreenDefectCombination;
import com.honda.galc.entity.qi.QiExternalSystemDefectMap;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiStationRepairMethod;
import com.honda.galc.service.QiDataValidationService;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>QiDataValidationServiceImpl Class description</h3>
 * <p>
 * QiDataValidationServiceImpl is used to validate regional data.
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
 * @author L&TInfotech<br>
 * 
 */
public class QiDataValidationServiceImpl implements QiDataValidationService{
	
	/**
	 * This method is used to validate data by Defect Id.
	 * @param partDefectCombList 
	 * @return impactedScreens 
	 */
	@Override
	public String validateByDefectId(List<Integer> partDefectCombList) {
		StringBuilder impactedScreens=new StringBuilder();
		List<QiEntryScreenDefectCombination> pdcEntryScreenList= ServiceFactory.getDao(QiEntryScreenDefectCombinationDao.class).findAllEntryScreensByPartDefectId(partDefectCombList);
		List<QiLocalDefectCombination> localAttributeList=ServiceFactory.getDao(QiLocalDefectCombinationDao.class).findAllLocalAttributesByPartDefectId(partDefectCombList);
		List<Integer> localCombinationIdList = new ArrayList<Integer>();
		for(QiLocalDefectCombination localDefectCombination:localAttributeList){
			localCombinationIdList.add(localDefectCombination.getLocalDefectCombinationId());
		}
		List<QiExternalSystemDefectMap> qiExternalSystemDefectMapList = new  ArrayList<QiExternalSystemDefectMap>();
		if(localCombinationIdList.size()>0){
			String localCombIdListValue=StringUtils.join(localCombinationIdList, ',');
			qiExternalSystemDefectMapList =	ServiceFactory.getDao(QiExternalSystemDefectMapDao.class).findAllByLocalCombinationId(localCombIdListValue);
		}
		if(pdcEntryScreenList.size()>0 ){
			impactedScreens.append("PDC to Entry Screen");
		}
		if(localAttributeList.size()>0){
			if(pdcEntryScreenList.size()>0)
				impactedScreens.append(",");
			impactedScreens.append("Local Attribute");
		}
		if(qiExternalSystemDefectMapList.size()>0){
			if(pdcEntryScreenList.size()>0 || localAttributeList.size()>0)
				impactedScreens.append(",");
			impactedScreens.append("Headless");
		}
		return impactedScreens.toString();
	}
	
	
	/**
	 * This method is used to validate data by part Location Id.
	 * @param partLocationIdList 
	 * @return impactedScreens 
	 */
	@Override
	public String validateByLocationId(List<Integer> partLocationIdList) {
		StringBuilder impactedScreens=new StringBuilder();
		List<QiEntryScreenDefectCombination> pdcEntryScreenList= ServiceFactory.getDao(QiEntryScreenDefectCombinationDao.class).findAllEntryScreensByPartLocationId(partLocationIdList);
		List<QiLocalDefectCombination> localAttributeList=ServiceFactory.getDao(QiLocalDefectCombinationDao.class).findAllLocalAttributesByPartLocationId(partLocationIdList);
		List<QiExternalSystemDefectMap> qiExternalSystemDefectMapList = new  ArrayList<QiExternalSystemDefectMap>();
		List<Integer> localCombinationIdList = new ArrayList<Integer>();
		for(QiLocalDefectCombination localDefectCombination:localAttributeList){
			localCombinationIdList.add(localDefectCombination.getLocalDefectCombinationId());
		}
		if(localCombinationIdList.size()>0){
			String localCombIdListValue=StringUtils.join(localCombinationIdList, ',');
			qiExternalSystemDefectMapList =	ServiceFactory.getDao(QiExternalSystemDefectMapDao.class).findAllByLocalCombinationId(localCombIdListValue);
		}
		if(pdcEntryScreenList.size()>0 ){
			impactedScreens.append("PDC to Entry Screen");
		}
		if(localAttributeList.size()>0){
			if(pdcEntryScreenList.size()>0)
				impactedScreens.append(",");
			impactedScreens.append("Local Attribute");
		}
		if(qiExternalSystemDefectMapList.size()>0){
			if(pdcEntryScreenList.size()>0 || localAttributeList.size()>0)
				impactedScreens.append(",");
			impactedScreens.append("Headless");
		}
		return impactedScreens.toString();
	}

	/**
	 * This method is used to validate data by repair Method Name.
	 * @param qirepairMethodList
	 * @return impactedScreens 
	 */
	@Override
	public String validateByRepairMethodName(List<String> qiRepairMethodList) {
		StringBuilder impactedScreens=new StringBuilder();
		List<QiStationRepairMethod> repairMethodStationCombList = new ArrayList<QiStationRepairMethod>();
		List<QiLocalDefectCombination> repairMethodLDCCombList = new ArrayList<QiLocalDefectCombination>();
		for (String repairMethodName : qiRepairMethodList) {
			repairMethodStationCombList.addAll((Collection<? extends QiStationRepairMethod>) getDao(QiStationRepairMethodDao.class).findAllByRepairMethod(repairMethodName));
			repairMethodLDCCombList.addAll((Collection<? extends QiLocalDefectCombination>) getDao(QiLocalDefectCombinationDao.class).findAllByRepairMethod(repairMethodName));
		}
		if(repairMethodLDCCombList.size()>0 ){
			impactedScreens.append("Local Attribute");
		}
		if(repairMethodStationCombList.size()>0){
			if(repairMethodLDCCombList.size()>0)
				impactedScreens.append(",");
			impactedScreens.append("Station Repair Method");
		}
		return impactedScreens.toString();
	}

	/**
	 * This method is used to validate data by imageName.
	 * @param imageNameList 
	 * @return impactedScreens 
	 */
	@Override
	public String validateByImageName(List<String> imageNameList) {
		StringBuilder impactedScreens=new StringBuilder();
		List<String> stationEntryList=new ArrayList<String>();
		Long entryScreenCount=(long) 0;
		List<String> imageToEntryScreenList=new ArrayList<String>();
		for(String imageName:imageNameList){
			stationEntryList.addAll(getDao(QiEntryScreenDao.class).findAllByStationEntry(imageName));
			entryScreenCount=entryScreenCount+getDao(QiEntryScreenDao.class).findCountByImageName(imageName);
			imageToEntryScreenList.addAll(getDao(QiEntryScreenDao.class).findAllByEntryScreenDefect(imageName));
		}
		if(entryScreenCount>0 ){
			impactedScreens.append("Image to Entry Screen");
		}
		if(stationEntryList.size()>0){
			if(entryScreenCount>0)
				impactedScreens.append(",");
			impactedScreens.append("Station Config");
		}
		if(imageToEntryScreenList.size()>0){
			if(entryScreenCount>0 || stationEntryList.size()>0)
				impactedScreens.append(",");
			impactedScreens.append("PDC to Entry Screen");
		}
		return impactedScreens.toString();
	}

	/**
	 * This method is used to validate data by plantName.
	 * @param plantNameList 
	 * @return impactedScreens 
	 */
	@Override
	public String validateByPlantName(List<String> plantNameList) {
		boolean isImpacted=false;
		for(String plantNameId:plantNameList){
			String[] plantNameArr=plantNameId.split(",");
			String plantName=plantNameArr[0]==null?"":plantNameArr[0].toString();
			String siteName=plantNameArr[1]==null?"":plantNameArr[1].toString();
			isImpacted=getDao(QiLocalDefectCombinationDao.class).findAllByPlantAndSite(plantName,siteName).size()>0;
			if(isImpacted){
				return "Local Attribute";
			}
		}
	return "";
	}

	/**
	 * This method is used to validate data by siteName.
	 * @param siteNameList 
	 * @return impactedScreens 
	 */
	@Override
	public String validateBySiteName(List<String> siteNameList) {
		boolean isImpacted=false;
		for(String siteName:siteNameList){
			isImpacted=getDao(QiLocalDefectCombinationDao.class).findAllBySite(siteName).size()>0;
			if(isImpacted){
				return "Local Attribute";
			}
		}
		return "";
	}

	/**
	 * This method is used to validate data by image SectionId.
	 * @param imageSectionList 
	 * @return impactedScreens 
	 */
	@Override
	public String validateByImageSection(List<String> imageSectionList) {
		List<Integer> partLocationIdList = new ArrayList<Integer>();
		for(String imageSection :imageSectionList) {
			String[] imageSectionId=imageSection.split(",");
			partLocationIdList.add(Integer.parseInt(imageSectionId[1].toString()));
		if (getDao(QiImageSectionDao.class).getEntryScreenCountByImageAndPartLocation(imageSectionId[2].toString(), partLocationIdList) > 0) {
			int count = getDao(QiImageSectionDao.class).getEntryScreenCountByImageAndSectionId(imageSectionId[2].toString(), Integer.parseInt(imageSectionId[0].toString()), partLocationIdList);
			if (count > 0){
				return "Station Config";
			}
		}
		}
		return "";
	}
	
	@Override
	public String validateByProcessPointGroup(List<RegionalProcessPointGroup> processPointGroupList) {
		if (processPointGroupList != null && !processPointGroupList.isEmpty()) {
			short categoryCode = processPointGroupList.get(0).getId().getCategoryCode();
			String site = processPointGroupList.get(0).getId().getSite();
			List<String> processPointGroupNameList = new ArrayList<String>();
			for (RegionalProcessPointGroup regionalProcessPointGroup : processPointGroupList) {
				processPointGroupNameList.add(regionalProcessPointGroup.getId().getProcessPointGroupName());
			}
			
			int count = getDao(ProcessPointGroupDao.class).getCount(categoryCode, site, processPointGroupNameList);
			if (count > 0) {
				return "Process Point Group";
			} else {
				return "";
			}
		} else {
			return "";
		}
	}
}
