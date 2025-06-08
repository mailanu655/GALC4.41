package com.honda.galc.client.qi.defectentry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.dto.qi.QiImageSectionDto;
import com.honda.galc.dto.qi.QiStationResponsibilityDto;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiImageSectionPoint;
import com.honda.galc.qi.cache.DefectEntryCache;
/**
 * <h3>DefectEntryCacheUtil description</h3> <h4>Description</h4>
 * <p>
 * <code>DefectEntryCacheUtil</code> is cache utility for Defect Entry Screen to maintain cache
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>L&T Infotech</TD>
 * <TD>17/3/2017</TD>
 * <TD>1.0.1</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.1
 * @author L&T Infotech
 */
public class DefectEntryCacheUtil {
	
	// === product cache, cleared for every product === //
	private DefectEntryCache imageSectionCache;
	private DefectEntryCache partDefectCombCache;
	
	// === client level cache, reused for multiple products === //
	private DefectEntryCache partDefectCombOriginalCache;
	private DefectEntryCache imageSectionClientCache;
	private DefectEntryCache part1Part2Cache;
	
	/**
	 * This method is used to initialize cache variables
	 */
	public void initializeCache(int maxElementsInMemory, int maxElementsOnDisk, int timeToLive, int timeToIdle) {
		imageSectionCache = new DefectEntryCache("imageSectionCache", maxElementsInMemory, maxElementsOnDisk, timeToLive, timeToIdle);
		partDefectCombCache = new DefectEntryCache("partDefectCombCache", maxElementsInMemory, maxElementsOnDisk, timeToLive, timeToIdle);
		partDefectCombOriginalCache = new DefectEntryCache("partDefectCombOriginalCache", maxElementsInMemory, maxElementsOnDisk, timeToLive, timeToIdle);
		imageSectionClientCache = new DefectEntryCache("imageSectionClientCache", maxElementsInMemory, maxElementsOnDisk, timeToLive, timeToIdle);
		part1Part2Cache = new DefectEntryCache("part1Part2Cache", maxElementsInMemory, maxElementsOnDisk, timeToLive, timeToIdle);
	}
	
	/**
	 * This method is used to clear product level cache 
	 */
	public void clearProductCache() {
		partDefectCombCache.clearCache();
		imageSectionCache.clearCache();
	}
	
	/**
	 *  This method is used to clear client level cache
	 */
	public void clearClientCache() {
		partDefectCombOriginalCache.clearCache();
		imageSectionClientCache.clearCache();
		part1Part2Cache.clearCache();
	}
	
	/**
	 *  This method is used to clear all cache
	 */
	public void clearCache() {
		clearProductCache();
		clearClientCache();
	}
	
	
	/**
	 * This method is used to iterate Part 1 from PDC cache
	 * @param entryScreen
	 * @param textEntryMenu
	 * @param loc
	 * @param part2
	 * @param defect
	 * @param part1Filter
	 * @return
	 */
	public List<String> getMatchingPart1List(String entryScreen, String textEntryMenu, String loc, String part2, String defect, String defect2,  String part1Filter) {
		List<String> part1List = new ArrayList<String>();
		List<QiDefectResultDto> filteredDtoList = getPdcListByTextFilter(entryScreen, textEntryMenu, StringUtils.EMPTY, loc, part2, defect, defect2, part1Filter);
		part1List = getPart1ListFromDtoList(filteredDtoList);
		Collections.sort(part1List);
		return part1List;
	}
	
	/**
	 * This method is used to iterate Part 1 from PDC cache
	 * @param entryScreen
	 * @param partFilter
	 * @return
	 */
	public List<String> getMatchingPart1ListByPartFilter(String entryScreen, String entryMenu, String partFilter) {
		List<String> newList = new ArrayList<String>();
		List<QiDefectResultDto> filteredDtoList = getTextPdcListByCombinedFilter(entryScreen, entryMenu, partFilter);
		newList = getPart1ListFromDtoList(filteredDtoList);
		Collections.sort(newList);
		return newList;
	}
	
	/**
	 * This method is used to iterate Part 1 from PDC cache
	 * @param entryScreen
	 * @param textEntryMenu
	 * @param part1
	 * @param part2
	 * @param defect
	 * @param part1Filter
	 * @return List<String>
	 */
	public List<String> getMatchingLocList(String entryScreen, String textEntryMenu, String part1, String part2, String defect, String defect2, String part1Filter) {
		List<String> locList = new ArrayList<String>();
		List<QiDefectResultDto> filteredDtoList = getPdcListByTextFilter(entryScreen, textEntryMenu, part1, StringUtils.EMPTY, part2, defect, defect2, part1Filter);
		locList = getLocListFromDtoList(filteredDtoList);
		Collections.sort(locList);
		return locList;
	}
	
	/**
	 * This method is used to iterate Part 1 from PDC cache
	 * @param entryScreen
	 * @param partFilter
	 * @return
	 */
	public List<String> getMatchingLocListByPartFilter(String entryScreen, String entryMenu, String partFilter) {
		List<String> newList = new ArrayList<String>();
		List<QiDefectResultDto> filteredDtoList = getTextPdcListByCombinedFilter(entryScreen, entryMenu, partFilter);
		newList = getLocListFromDtoList(filteredDtoList);
		Collections.sort(newList);
		return newList;
	}
	
	/**
	 * This method is used to iterate Part 2 from PDC Cache
	 * @param entryScreen
	 * @param textEntryMenu
	 * @param part1
	 * @param loc
	 * @param defect
	 * @param part1Filter
	 * @return
	 */
	public List<String> getMatchingPart2List(String entryScreen, String textEntryMenu, String part1, String loc, String defect, String defect2, String part2Name) {
		List<String> part2List = new ArrayList<String>();
		List<QiDefectResultDto> filteredDtoList = getPdcListByTextFilter(entryScreen, textEntryMenu, part1, loc, part2Name, defect, defect2, StringUtils.EMPTY);
		part2List = getPart2ListFromDtoList(filteredDtoList);
		if(part2List.contains(StringUtils.EMPTY))
			part2List.remove(StringUtils.EMPTY);
		Collections.sort(part2List);
		return part2List;
	}
	
	/**
	 * This method is used to iterate Part 1 from PDC cache
	 * @param entryScreen
	 * @param partFilter
	 * @return
	 */
	public List<String> getMatchingPart2ListByPartFilter(String entryScreen, String entryMenu, String partFilter) {
		List<String> newList = new ArrayList<String>();
		List<QiDefectResultDto> filteredDtoList = getTextPdcListByCombinedFilter(entryScreen, entryMenu, partFilter);
		newList = getPart2ListFromDtoList(filteredDtoList);
		Collections.sort(newList);
		return newList;
	}
	
	/**
	 * This method is used to iterate Defect from PDC Cache
	 * @param entryScreen
	 * @param textEntryMenu
	 * @param part1
	 * @param loc
	 * @param part2
	 * @param part1Filter
	 * @return
	 */
	public List<String> getMatchingDefectList(String entryScreen, String textEntryMenu, String part1, String loc, String part2, String part1Filter) {
		List<String> defectList = new ArrayList<String>();
		List<QiDefectResultDto> filteredDtoList = getPdcListByTextFilter(entryScreen, textEntryMenu, part1, loc, part2, StringUtils.EMPTY, StringUtils.EMPTY, part1Filter);
		defectList = getDefectListFromDtoList(filteredDtoList);
		Collections.sort(defectList);
		return defectList;
	}
	
	/**
	 * This method is used to iterate Defect 1 from PDC Cache based on Image Entry Screen Filters
	 * @param entryScreen
	 * @param imageSectionId
	 * @param defect2
	 * @param partLocationId
	 * @param part1Filter
	 * @return
	 */
	public List<String> getMatchingDefect1List(String entryScreen, int imageSectionId, String defect2, int partLocationId, String part1Filter) {
		List<QiDefectResultDto> filteredDtoList = getPdcListByImageFilter(entryScreen, imageSectionId, partLocationId, StringUtils.EMPTY, defect2, part1Filter);
		List<String> defectList = new ArrayList<String>();
		defectList = getDefectListFromDtoList(filteredDtoList);
		Collections.sort(defectList);
		return defectList;
	}
	
	/**
	 * This method is used to iterate Defect 2 from PDC Cache based on Image Entry Screen Filters
	 * @param entryScreen
	 * @param imageSectionId
	 * @param defect1
	 * @param partLocationId
	 * @return
	 */
	public List<String> getMatchingDefect2List(String entryScreen, int imageSectionId, String defect1, int partLocationId, String part1Filter) {
		List<QiDefectResultDto> filteredDtoList = getPdcListByImageFilter(entryScreen, imageSectionId, partLocationId, defect1, StringUtils.EMPTY, part1Filter);
		List<String> defectList = new ArrayList<String>();
		defectList = getDefect2ListFromDtoList(filteredDtoList);
		if(defectList.contains(StringUtils.EMPTY))
			defectList.remove(StringUtils.EMPTY);
		Collections.sort(defectList);
		return defectList;
	}
	
	/**
	 * This method is used to iterate Image Section points from PDC Cache based on Image Entry Screen Filters
	 * @param entryScreen
	 * @param defect1
	 * @param defect2
	 * @return
	 */
	public List<QiImageSectionPoint> getMatchingImageSectionList(String entryScreen, String defect1, String defect2, String part1Filter) {
		List<QiDefectResultDto> filteredDtoList = getPdcListByImageFilter(entryScreen, 0, 0, defect1, defect2, part1Filter);
		List<QiImageSectionPoint> imageSectionList = new ArrayList<QiImageSectionPoint>();
		imageSectionList = getImageSectionListFromDtoList(entryScreen, filteredDtoList);
		return imageSectionList;
	}
	/**
	 * This method is used to iterate Image Section points from PDC Cache based on Image Entry Screen Filters
	 * @param entryScreen
	 * @param defect1
	 * @param defect2
	 * @return
	 */
	public List<QiImageSectionPoint> getMatchingImageSectionListByPartFilter(String entryScreen, String partFilter) {
		List<QiDefectResultDto> filteredDtoList = getImagePdcListByCombinedFilter(entryScreen, partFilter);
		List<QiImageSectionPoint> imageSectionList = new ArrayList<QiImageSectionPoint>();
		imageSectionList = getImageSectionListFromDtoList(entryScreen, filteredDtoList);
		return imageSectionList;
	}
	/**
	 * This method is used to iterate PDC List on Image Entry Screen Filter
	 * @param entryScreen
	 * @param imageSectionId
	 * @param partLocationId
	 * @param defect1
	 * @param defect2
	 * @return
	 */
	public List<QiDefectResultDto> getPdcListByImageFilter(String entryScreen, int imageSectionId, int partLocationId, String defect1, String defect2, String part1Filter) {
		List<QiDefectResultDto> dtoList = partDefectCombCache.getList(entryScreen,QiDefectResultDto.class);
		List<QiDefectResultDto> filteredDtoList = new ArrayList<QiDefectResultDto>(); 
		if(dtoList != null) {
			for(QiDefectResultDto dto : dtoList) {
				 if(((dto.getImageSectionId() == imageSectionId && dto.getPartLocationId() == partLocationId) || (imageSectionId==0 && partLocationId==0)) &&
				    (StringUtils.isEmpty(defect1) ||  dto.getDefectTypeName().equalsIgnoreCase(defect1)) &&
					(StringUtils.isEmpty(defect2) ||  dto.getDefectTypeName2().equalsIgnoreCase(defect2)) &&
					(StringUtils.isEmpty(part1Filter) ||  dto.getInspectionPartName().equalsIgnoreCase(part1Filter))) 
					filteredDtoList.add(dto);
			}
		}
		return QiCommonUtil.getUniqueArrayList(filteredDtoList);
	}
	
	public List<QiDefectResultDto> getImagePdcListByCombinedFilter(String entryScreen, String filter) {
		List<QiDefectResultDto> dtoList = partDefectCombCache.getList(entryScreen,QiDefectResultDto.class);
		List<QiDefectResultDto> filteredDtoList = new ArrayList<QiDefectResultDto>(); 
		if(dtoList != null) {
			for(QiDefectResultDto dto : dtoList) {
			    String p1 = StringUtils.trimToEmpty(dto.getInspectionPartName());
			    String p1l1 = StringUtils.isEmpty(dto.getInspectionPartLocationName()) ? "" : dto.getInspectionPartLocationName().trim();
			    String p1l2 = StringUtils.isEmpty(dto.getInspectionPartLocation2Name()) ? "" : dto.getInspectionPartLocation2Name().trim();
			    String p2 = StringUtils.isEmpty(dto.getInspectionPart2Name()) ? "" : dto.getInspectionPart2Name().trim();
			    String p2l1 = StringUtils.isEmpty(dto.getInspectionPart2LocationName()) ? "" : dto.getInspectionPart2LocationName().trim();
			    String p2l2 = StringUtils.isEmpty(dto.getInspectionPart2Location2Name()) ? "" : dto.getInspectionPart2Location2Name().trim();
			    String p3 = StringUtils.trimToEmpty(dto.getInspectionPart3Name().trim());
			    StringBuilder sb = new StringBuilder();
			    sb.append(p1).append("_").append(p1l1).append("_").append(p1l2).append("_")
			    .append(p2).append("_").append(p2l1).append("_").append(p2l2).append("_").append(p3);
			    if(Pattern.matches(filter.toLowerCase(), sb.toString().replaceAll("_+", "_").replaceAll("_+$", "").toLowerCase()))
			    {
					filteredDtoList.add(dto);				    	
			    }
			}
		}
		Collections.sort(filteredDtoList, new QiDefectResultDto());
		return QiCommonUtil.getUniqueArrayList(filteredDtoList);
	}
	
	/**
	 * This method is used to retrieve PLC from PDC Cache based on Image Entry Screen and Image Section Id
	 * @param entryScreen
	 * @param imageSectionId
	 * @return 
	 */
	public QiImageSectionDto getMatchingPartLocCombList(String entryScreen, final Integer imageSectionId) {
		List<QiImageSectionDto> foundPartLocComb = getMatchingPartLocCombList(entryScreen, Arrays.asList(new Integer[]{ imageSectionId }), "", "", "");
		if(foundPartLocComb == null || foundPartLocComb.size() == 0){
			return null;
		}
		return foundPartLocComb.get(0);
	}
	
	/**
	 * This method is used to iterate PLC from PDC Cache based on Image Entry Screen Filters
	 * @param entryScreen
	 * @param imageSectionIdList
	 * @param defect1
	 * @param defect2
	 * @return
	 */
	public List<QiImageSectionDto> getMatchingPartLocCombList(String entryScreen, List<Integer> imageSectionIdList, String defect1, String defect2, String part1Filter) {
		List<QiDefectResultDto> dtoList = partDefectCombCache.getList(entryScreen,QiDefectResultDto.class);
		List<QiDefectResultDto> filteredDtoList = new ArrayList<QiDefectResultDto>(); 
		if(dtoList != null) {
			for(QiDefectResultDto dto : dtoList) {
				 if((imageSectionIdList.isEmpty() || (!imageSectionIdList.isEmpty()) && imageSectionIdList.contains(dto.getImageSectionId())) &&
					(StringUtils.isEmpty(defect1) ||  dto.getDefectTypeName().equalsIgnoreCase(defect1)) &&
					(StringUtils.isEmpty(defect2) ||  dto.getDefectTypeName2().equalsIgnoreCase(defect2)) &&
					(StringUtils.isEmpty(part1Filter) ||  dto.getInspectionPartName().equalsIgnoreCase(part1Filter))) 
					filteredDtoList.add(dto);
			}
		}
		return QiCommonUtil.getUniqueArrayList(getImageSectionDtoFromPdcList(filteredDtoList));
	}
	
	/**
	 * This method is used to get Part 1 from PDC Dto List
	 * @param dtoList
	 * @return
	 */
	public List<String> getPart1ListFromDtoList(List<QiDefectResultDto> dtoList) {
		List<String> part1List = new ArrayList<String>();
		for(QiDefectResultDto dto : dtoList) {
			part1List.add(dto.getInspectionPartName());
		}
		return QiCommonUtil.getUniqueArrayList(part1List);
	}	
	
	/**
	 * This method is used to get Part1 Loc from PDC Dto List
	 * @param dtoList
	 * @return
	 */
	public List<String> getLocListFromDtoList(List<QiDefectResultDto> dtoList) {
		List<String> locList = new ArrayList<String>();
		for(QiDefectResultDto dto : dtoList) {
			locList.add(dto.getInspectionPartLocationName());
		}
		return QiCommonUtil.getUniqueArrayList(locList);
	}
	
	/**
	 * This method is used to get Part 2 from PDC Dto List
	 * @param dtoList
	 * @return
	 */
	public List<String> getPart2ListFromDtoList(List<QiDefectResultDto> dtoList) {
		List<String> part2List = new ArrayList<String>();
		for(QiDefectResultDto dto : dtoList) {
			part2List.add(dto.getInspectionPart2Name());
		}
		return QiCommonUtil.getUniqueArrayList(part2List);
	}
	
	/**
	 * This method is used to get Defect 1 from PDC Dto List
	 * @param dtoList
	 * @return
	 */
	private List<String> getDefectListFromDtoList(List<QiDefectResultDto> dtoList) {
		List<String> defectList = new ArrayList<String>();
		for(QiDefectResultDto dto : dtoList) {
			defectList.add(dto.getCombinedDefectTypeName());
		}
		return QiCommonUtil.getUniqueArrayList(defectList);
	}
	
	/**
	 * This method is used to get Defect 2 from PDC Dto List
	 * @param dtoList
	 * @return
	 */
	private List<String> getDefect2ListFromDtoList(List<QiDefectResultDto> dtoList) {
		List<String> defectList = new ArrayList<String>();
		for(QiDefectResultDto dto : dtoList) {
			defectList.add(dto.getDefectTypeName2());
		}
		return QiCommonUtil.getUniqueArrayList(defectList);
	}
	
	/**
	 * This method is used to get Text Entry Menu List from PDC Dto List
	 * @param dtoList
	 * @return
	 */
	public List<String> getMenuListFromDtoList(List<QiDefectResultDto> dtoList, String part1Filter) {
		List<String> menuList = new ArrayList<String>();
		for(QiDefectResultDto dto : dtoList) {
			if(StringUtils.isEmpty(part1Filter) ||  dto.getInspectionPartName().equalsIgnoreCase(part1Filter)) {
				menuList.add(dto.getTextEntryMenu());
			}
		}
		menuList.removeAll(Arrays.asList(StringUtils.EMPTY));
		return QiCommonUtil.getUniqueArrayList(menuList);
	}
	
	/**
	 * This method is used to get Image Section Point List from PDC Dto List
	 * @param entryScreenName
	 * @param dtoList
	 * @return
	 */
	public List<QiImageSectionPoint> getImageSectionListFromDtoList(String entryScreenName, List<QiDefectResultDto> dtoList) {
		List<Integer> imageSectionIdList = new ArrayList<Integer>();
		List<QiImageSectionPoint> imageSectionPointList = new ArrayList<QiImageSectionPoint>();
		List<QiImageSectionPoint> filteredImageSectionPointList = new ArrayList<QiImageSectionPoint>();
		if(imageSectionCache.containsKey(entryScreenName)) {
			for(QiDefectResultDto dto : dtoList) {
				imageSectionIdList.add(dto.getImageSectionId());
			}
			imageSectionIdList = QiCommonUtil.getUniqueArrayList(imageSectionIdList);
			imageSectionPointList = imageSectionCache.getList(entryScreenName,QiImageSectionPoint.class);
			for(Integer id : imageSectionIdList) {
				for(QiImageSectionPoint point : imageSectionPointList) {
					if(point.getId().getImageSectionId() == id.intValue()) {
						filteredImageSectionPointList.add(point);
					}
				}
			}
		}
		return filteredImageSectionPointList;
	}
	
	/**
	 * This method is used to get Image Section Dto List from PDC Dto List
	 * @param dtoList
	 * @return
	 */
	public List<QiImageSectionDto> getImageSectionDtoFromPdcList(List<QiDefectResultDto> dtoList) {
		List<QiImageSectionDto> imageSectionDtoList = new ArrayList<QiImageSectionDto>();
		for(QiDefectResultDto dto : dtoList) {
			QiImageSectionDto imageSectionDto = new QiImageSectionDto();
			imageSectionDto.setImageSectionId(dto.getImageSectionId());
			imageSectionDto.setPartLocationId(dto.getPartLocationId());
			imageSectionDto.setInspectionPartName(dto.getInspectionPartName());
			imageSectionDto.setInspectionPartLocationName(dto.getInspectionPartLocationName());
			imageSectionDto.setInspectionPartLocation2Name(dto.getInspectionPartLocation2Name());
			imageSectionDto.setInspectionPart2Name(dto.getInspectionPart2Name());
			imageSectionDto.setInspectionPart2LocationName(dto.getInspectionPart2LocationName());
			imageSectionDto.setInspectionPart2Location2Name(dto.getInspectionPart2Location2Name());
			imageSectionDto.setInspectionPart3Name(dto.getInspectionPart3Name());
			
			imageSectionDtoList.add(imageSectionDto);
		}
		return imageSectionDtoList;
	}
	
	/**
	 * This method is used to get Filtered PDC from PDC Cache based on Text Entry Screen Filter
	 * @param entryScreen
	 * @param textEntryMenu
	 * @param inspectionPartName
	 * @param inspectionPart2Name
	 * @param defectTypeName
	 * @return
	 */
	public List<QiDefectResultDto> getPdcListByTextFilter(String entryScreen, String textEntryMenu, 
		String inspectionPartName, String location, String inspectionPart2Name, String defectTypeName, String defectTypeName2, String part1Filter) {
		List<QiDefectResultDto> dtoList = partDefectCombCache.getList(entryScreen,QiDefectResultDto.class);
		List<QiDefectResultDto> filteredDtoList = new ArrayList<QiDefectResultDto>(); 
		if(dtoList != null) {
			for(QiDefectResultDto dto : dtoList) {
				 if((StringUtils.isEmpty(textEntryMenu) || dto.getTextEntryMenu().equalsIgnoreCase(textEntryMenu)) &&
				    (StringUtils.isEmpty(inspectionPartName) ||  dto.getInspectionPartName().equalsIgnoreCase(inspectionPartName)) &&
				    (StringUtils.isEmpty(location) ||  dto.getInspectionPartLocationName().equalsIgnoreCase(location)) &&
				    (StringUtils.isEmpty(inspectionPart2Name) ||  dto.getInspectionPart2Name().equalsIgnoreCase(inspectionPart2Name)) &&
					(StringUtils.isEmpty(defectTypeName) ||  dto.getDefectTypeName().equalsIgnoreCase(defectTypeName)) &&
					(StringUtils.isEmpty(defectTypeName2) ||  dto.getDefectTypeName2().equalsIgnoreCase(defectTypeName2)) &&
					(StringUtils.isEmpty(part1Filter) ||  dto.getInspectionPartName().equalsIgnoreCase(part1Filter))) 
					filteredDtoList.add(dto);
			}
		}
		Collections.sort(filteredDtoList, new QiDefectResultDto());
		return QiCommonUtil.getUniqueArrayList(filteredDtoList);
	}
	
	public List<QiDefectResultDto> getTextPdcListByCombinedFilter(String entryScreen, String entryMenu,String filter) {
			List<QiDefectResultDto> dtoList = partDefectCombCache.getList(entryScreen,QiDefectResultDto.class);
			List<QiDefectResultDto> filteredDtoList = new ArrayList<QiDefectResultDto>(); 
			if(dtoList != null) {
				for(QiDefectResultDto dto : dtoList) {
					if(!(StringUtils.isEmpty(entryMenu) || dto.getTextEntryMenu().equalsIgnoreCase(entryMenu)))  {
						continue;
					}
				    String p1 = StringUtils.trimToEmpty(dto.getInspectionPartName());
				    String p1l1 = StringUtils.isEmpty(dto.getInspectionPartLocationName()) ? "" : dto.getInspectionPartLocationName().trim();
				    String p1l2 = StringUtils.isEmpty(dto.getInspectionPartLocation2Name()) ? "" : dto.getInspectionPartLocation2Name().trim();
				    String p2 = StringUtils.isEmpty(dto.getInspectionPart2Name()) ? "" : dto.getInspectionPart2Name().trim();
				    String p2l1 = StringUtils.isEmpty(dto.getInspectionPart2LocationName()) ? "" : dto.getInspectionPart2LocationName().trim();
				    String p2l2 = StringUtils.isEmpty(dto.getInspectionPart2Location2Name()) ? "" : dto.getInspectionPart2Location2Name().trim();
				    String p3 = StringUtils.trimToEmpty(dto.getInspectionPart3Name().trim());
				    StringBuilder sb = new StringBuilder();
				    sb.append(p1).append("_").append(p1l1).append("_").append(p1l2).append("_")
				    .append(p2).append("_").append(p2l1).append("_").append(p2l2).append("_").append(p3);
				    if(Pattern.matches(filter.toLowerCase(), sb.toString().replaceAll("_+", "_").replaceAll("_+$", "").toLowerCase()))
				    {
						filteredDtoList.add(dto);				    	
				    }
				}
			}
			Collections.sort(filteredDtoList, new QiDefectResultDto());
			return QiCommonUtil.getUniqueArrayList(filteredDtoList);
		}
		
	/**
	 * This method is used to get Filtered PDC from PDC Cache based on Image Entry Screen Filter
	 * @param entryScreen
	 * @param imageSectionId
	 * @param defectTypeName
	 * @param defectTypeName2
	 * @param partLocationId
	 * @return
	 */
	public List<QiDefectResultDto> findAllPartDefectCombByImageFilter(String entryScreen, int imageSectionId, String defectTypeName, String defectTypeName2, int partLocationId) {
		List<QiDefectResultDto> dtoList = partDefectCombCache.getList(entryScreen,QiDefectResultDto.class);
		List<QiDefectResultDto> cloneDtoList = new ArrayList<QiDefectResultDto>();
		List<QiDefectResultDto> filteredDtoList = new ArrayList<QiDefectResultDto>(); 
		if(dtoList != null) {
			cloneDtoList.addAll(dtoList);
			if(imageSectionId != 0 && partLocationId != 0 && !StringUtils.isEmpty(defectTypeName)) {
				for(QiDefectResultDto dto : cloneDtoList) {
					if((dto.getImageSectionId() == imageSectionId) && (dto.getPartLocationId() == partLocationId) 
							&& (dto.getDefectTypeName().equalsIgnoreCase(defectTypeName))
							&& (StringUtils.trimToEmpty(dto.getDefectTypeName2()).equalsIgnoreCase(defectTypeName2))) {
						filteredDtoList.add(dto);
					}
				}
			}
		}
		return QiCommonUtil.getUniqueArrayList(filteredDtoList);
	}
	
	/**
	 * This method is used to check if we need to add a blank value in defect 2 list
	 * @param entryScreen
	 * @param imageSectionId
	 * @param partLocationId
	 * @param defect1
	 * @return
	 */
	public boolean isDefect2Blank(String entryScreen, int imageSectionId, int partLocationId, String defect1) {
		boolean isValueBlank = false;
		boolean isValueNotBlank = false;
		List<QiDefectResultDto> dtoList = partDefectCombCache.getList(entryScreen,QiDefectResultDto.class);
		List<QiDefectResultDto> filteredDtoList = new ArrayList<QiDefectResultDto>(); 
		if(dtoList != null && ((imageSectionId!=0 && partLocationId!=0) || !StringUtils.isEmpty(defect1))) {
			for(QiDefectResultDto dto : dtoList) {
				 if(((dto.getImageSectionId() == imageSectionId && dto.getPartLocationId() == partLocationId) || (imageSectionId==0 && partLocationId==0)) &&
				    (StringUtils.isEmpty(defect1) ||  dto.getDefectTypeName().equalsIgnoreCase(defect1)))
					filteredDtoList.add(dto);
			}
			if(filteredDtoList.size() > 1) {
				for(QiDefectResultDto dto : filteredDtoList) {
					if(StringUtils.isEmpty(dto.getDefectTypeName2())) {
						isValueBlank = true;
					} else {
						isValueNotBlank = true;
					}
				}
			}
		}
		return isValueBlank && isValueNotBlank;
	}
	
	/**
	 * This method is used to check whether the selected PDC is present in current session or not.
	 */
	public boolean isPdcExistInCurrentSession(QiDefectResult defectResult) {
		List<QiDefectResultDto> dtoList = partDefectCombCache.getList(defectResult.getEntryScreen(),QiDefectResultDto.class);
		if(dtoList!=null) {
			for(QiDefectResultDto dto : dtoList) {
				if(dto.getInspectionPartName().equalsIgnoreCase(defectResult.getInspectionPartName())
						&& dto.getInspectionPartLocationName().equalsIgnoreCase(defectResult.getInspectionPartLocationName())
						&& dto.getInspectionPartLocation2Name().equalsIgnoreCase(defectResult.getInspectionPartLocation2Name())
						&& dto.getInspectionPart2Name().equalsIgnoreCase(defectResult.getInspectionPart2Name())
						&& dto.getInspectionPart2LocationName().equalsIgnoreCase(defectResult.getInspectionPart2LocationName())
						&& dto.getInspectionPart2Location2Name().equalsIgnoreCase(defectResult.getInspectionPart2Location2Name())
						&& dto.getInspectionPart3Name().equalsIgnoreCase(defectResult.getInspectionPart3Name())
						&& dto.getDefectTypeName().equalsIgnoreCase(defectResult.getDefectTypeName())
						&& dto.getDefectTypeName2().equalsIgnoreCase(defectResult.getDefectTypeName2())
						&& StringUtils.trimToEmpty(dto.getImageName()).equalsIgnoreCase(defectResult.getImageName())) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * This method is used to get plant list from responsibilities list
	 * @param responsibilities
	 * @param site
	 * @return
	 */
	public List<String> getPlantListFromResponsibilities(List<QiStationResponsibilityDto> responsibilities, String site) {
		List<String> plantList = new ArrayList<String>();
		for(QiStationResponsibilityDto responsibility : responsibilities) {
			if(responsibility.getSite().equalsIgnoreCase(site)) {
				plantList.add(responsibility.getPlant());
			}
		}
		return QiCommonUtil.getUniqueArrayList(plantList);
	}
	
	/**
	 * This method is used to get department list from responsibilities list
	 * @param responsibilities
	 * @param site
	 * @return
	 */
	public List<String> getDeptListFromResponsibilities(List<QiStationResponsibilityDto> responsibilities, String site, String plant) {
		List<String> deptList = new ArrayList<String>();
		for(QiStationResponsibilityDto responsibility : responsibilities) {
			if(responsibility.getSite().equalsIgnoreCase(site) && responsibility.getPlant().equalsIgnoreCase(plant)) {
				deptList.add(responsibility.getDept());
			}
		}
		return QiCommonUtil.getUniqueArrayList(deptList);
	}
	
	/**
	 * This method is used to get level 1 list from responsibilities list
	 * @param responsibilities
	 * @param site
	 * @return
	 */
	public List<String> getLevel1ListFromResponsibilities(List<QiStationResponsibilityDto> responsibilities, String site, String plant, String dept, boolean isUnique) {
		List<String> respLevel1List = new ArrayList<String>();
		for(QiStationResponsibilityDto responsibility : responsibilities) {
			if(responsibility.getSite().equalsIgnoreCase(site) 
					&& responsibility.getPlant().equalsIgnoreCase(plant)
					&& responsibility.getDept().equalsIgnoreCase(dept)) {
				respLevel1List.add(responsibility.getResponsibleLevelName());
			}
		}
		if(isUnique)  {
			return QiCommonUtil.getUniqueArrayList(respLevel1List);
		}
		else  {
			return respLevel1List;
		}
	}

	/**
	 * This method is used to get level 1 list from responsibilities list
	 * @param responsibilities
	 * @param site
	 * @return
	 */
	public List<String> getLevel1ListFromResponsibilities(List<QiStationResponsibilityDto> responsibilities, String site, String plant, String dept) {
		return getLevel1ListFromResponsibilities(responsibilities, site, plant, dept, true);
	}

	public DefectEntryCache getImageSectionCache() {
		return imageSectionCache;
	}

	public void setImageSectionCache(DefectEntryCache imageSectionCache) {
		this.imageSectionCache = imageSectionCache;
	}

	public DefectEntryCache getPartDefectCombOriginalCache() {
		return partDefectCombOriginalCache;
	}

	public void setPartDefectCombOriginalCache(DefectEntryCache partDefectCombOriginalCache) {
		this.partDefectCombOriginalCache = partDefectCombOriginalCache;
	}

	public DefectEntryCache getPartDefectCombCache() {
		return partDefectCombCache;
	}

	public void setPartDefectCombCache(DefectEntryCache partDefectCombCache) {
		this.partDefectCombCache = partDefectCombCache;
	}

	protected DefectEntryCache getImageSectionClientCache() {
		return imageSectionClientCache;
	}
	
	protected DefectEntryCache getPart1Part2Cache() {
		return part1Part2Cache;
	}
}
