package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiImageSectionDao;
import com.honda.galc.entity.qi.QiImageSection;
import com.honda.galc.entity.qi.QiImageSectionId;
import com.honda.galc.service.Parameters;

public class QiImageSectionDaoImpl extends BaseDaoImpl<QiImageSection, QiImageSectionId> implements QiImageSectionDao{

	final static String FIND_IMAGEFILTER_BY_FILTER = 	"select e from QiImageSection e where  e.imageName like :filterData";
	private final static String FIND_PART_LOCATION_ID  ="select e from QiImageSection e where e.id.partLocationId in (:partLocationIdList)";
	private final static String FIND_IMAGE_SECTION_BY_IMAGE_AND_PART  ="select e from QiImageSection e where e.imageName like :image and e.id.partLocationId in (:partLocationId)";
	private final static String UPDATE_IMAGE_NAME_FOR_IMAGE_SECTION = "UPDATE GALADM.QI_IMAGE_SECTION_TBX SET IMAGE_NAME= ?1, UPDATE_USER= ?2 WHERE IMAGE_NAME= ?3";
	private static final String FIND_ALL_BY_IMAGE_NAME = "select distinct IMAGE_SECTION_ID from  GALADM.QI_IMAGE_SECTION_TBX  where IMAGE_NAME = ?1";

	private static final String FIND_LOCAL_COUNT_BY_IMAGE_AND_PART1 = "SELECT SEL.ENTRY_SCREEN, SEL.IS_IMAGE, IMG.IMAGE_DATA, IMG.IMAGE_NAME from "
			+ "(SELECT DISTINCT QSES.ENTRY_SCREEN, QES.IS_IMAGE, QES.IMAGE_NAME "
			+ "from galadm.QI_STATION_ENTRY_SCREEN_TBX QSES "
			+ "join galadm.QI_ENTRY_SCREEN_TBX QES on QSES.ENTRY_SCREEN=QES.ENTRY_SCREEN AND QSES.ENTRY_MODEL=QES.ENTRY_MODEL AND QES.ACTIVE=1 "
			+ "AND QES.IS_IMAGE=1 " + "join galadm.QI_ENTRY_MODEL_GROUPING_TBX EMG on EMG.ENTRY_MODEL=QSES.ENTRY_MODEL "
			+ "join galadm.QI_ENTRY_MODEL_TBX EM on EMG.ENTRY_MODEL=EM.ENTRY_MODEL AND EM.ACTIVE=1 "
			+ "join galadm.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX ESDC on ESDC.ENTRY_SCREEN=QSES.ENTRY_SCREEN "
			+ "join galadm.QI_REGIONAL_DEFECT_COMBINATION_TBX RDC on ESDC.REGIONAL_DEFECT_COMBINATION_ID=RDC.REGIONAL_DEFECT_COMBINATION_ID AND "
			+ "RDC.ACTIVE=1 "
			+ "join galadm.QI_LOCAL_DEFECT_COMBINATION_TBX LDC on ESDC.REGIONAL_DEFECT_COMBINATION_ID=LDC.REGIONAL_DEFECT_COMBINATION_ID AND "
			+ "LDC.ENTRY_SCREEN=QSES.ENTRY_SCREEN "
			+ "join GALADM.QI_PART_LOCATION_COMBINATION_TBX PLC ON PLC.PART_LOCATION_ID=RDC.PART_LOCATION_ID "
			+ "where PLC.PART_LOCATION_ID in ( ";

	private static final String FIND_LOCAL_COUNT_BY_IMAGE_AND_PART2 = " ) ORDER BY QSES.ENTRY_SCREEN) SEL "
			+ "join galadm.QI_IMAGE_TBX IMG on SEL.IMAGE_NAME=IMG.IMAGE_NAME where IMG.IMAGE_NAME = ?1";

	private static final String FIND_LOCAL_COUNT_BY_IMAGE_AND_SECTION1 = "SELECT DISTINCT a.ENTRY_SCREEN FROM GALADM.QI_LOCAL_DEFECT_COMBINATION_TBX a "
			+ "JOIN GALADM.QI_REGIONAL_DEFECT_COMBINATION_TBX b on a.REGIONAL_DEFECT_COMBINATION_ID=b.REGIONAL_DEFECT_COMBINATION_ID "
			+ "JOIN GALADM.QI_IMAGE_SECTION_TBX c on c.PART_LOCATION_ID=b.PART_LOCATION_ID "
			+ "JOIN GALADM.QI_IMAGE_TBX d on c.IMAGE_NAME=d.IMAGE_NAME "
			+ "JOIN GALADM.QI_ENTRY_SCREEN_TBX e on e.ENTRY_SCREEN=a.ENTRY_SCREEN "
			+ "JOIN GALADM.QI_ENTRY_MODEL_GROUPING_TBX f on e.ENTRY_MODEL=f.ENTRY_MODEL "
			+ "JOIN GALADM.QI_STATION_ENTRY_SCREEN_TBX g on a.ENTRY_SCREEN=g.ENTRY_SCREEN "
			+ "JOIN GALADM.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX h on a.ENTRY_SCREEN=h.ENTRY_SCREEN "
			+ "WHERE c.IMAGE_NAME= ?1 and c.IMAGE_SECTION_ID = ?2 and c.PART_LOCATION_ID in ( ";

	private static final String FIND_LOCAL_COUNT_BY_IMAGE_AND_SECTION2 = " ) and b.ACTIVE=1 AND d.ACTIVE=1 "
			+ "AND a.REGIONAL_DEFECT_COMBINATION_ID=h.REGIONAL_DEFECT_COMBINATION_ID "
			+ "AND (b.IQS_ID IS NOT NULL AND  b.IQS_ID <> 0) AND  COALESCE(b.THEME_NAME,'') != '' ";
	
	
	private static final String FIND_ALL_IMAGE_NAME_BY_SECTION = "SELECT DISTINCT(img.IMAGE_NAME) from GALADM.QI_IMAGE_TBX img "
			+ "JOIN GALADM.QI_IMAGE_SECTION_TBX sec "
			+ "on img.IMAGE_NAME = sec.IMAGE_NAME order by img.IMAGE_NAME asc";



	private static final String FIND_ALL_PART_LOCATION_IDS_BY_IMAGE_NAME = "select e.REGIONAL_DEFECT_COMBINATION_ID "
			+ "from galadm.QI_REGIONAL_DEFECT_COMBINATION_TBX e join galadm.QI_IMAGE_SECTION_TBX i "
			+ "on e.PART_LOCATION_ID = i.PART_LOCATION_ID join galadm.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX a "
			+ "on a.REGIONAL_DEFECT_COMBINATION_ID = e.REGIONAL_DEFECT_COMBINATION_ID "
			+ "where a.ENTRY_SCREEN = ?1 and i.IMAGE_NAME = ?2";


	/**
	 * This method is used to filter the data based on the Image Name
	 */
	public List<QiImageSection> findImageByFilter(String filterData) {
		filterData=filterData.toUpperCase();
		Parameters params = Parameters.with("filterData", "%"+filterData+"%");
		return findAllByQuery(FIND_IMAGEFILTER_BY_FILTER,params);
	}

	/**
	 * This method is used to list of imageSection based on list of partLocationId associated with PLC
	 * @param partLocationIdList
	 * @return
	 */
	public List<QiImageSection> findPartLocationIdInImageSection(List<Integer> partLocationIdList) {
		Parameters params = Parameters.with("partLocationIdList", partLocationIdList);
		return findAllByQuery(FIND_PART_LOCATION_ID,params);
	}
	public List<QiImageSection> findAllImageSectionByImageAndPart(String image, int partLocationId) {
		Parameters params = Parameters.with("image", image);
		params.put("partLocationId", partLocationId);
		return findAllByQuery(FIND_IMAGE_SECTION_BY_IMAGE_AND_PART,params);
	}
	@Transactional
	public void updateImageSection(int partLocationId, String updateUser, int imageSectionId){
		update(Parameters.with("partLocationId", partLocationId).put("updateUser", updateUser),
				Parameters.with("imageSectionId", imageSectionId));
	}

	public int getMaxSectionId() {
		Integer maxId = max("id.imageSectionId",Integer.class); 
		if (maxId == null) {
			maxId = 0;
		}
		return maxId;
	}

	public int getImageSectionCount(int imageSectionId) {
		return (int) count(Parameters.with("id.imageSectionId", imageSectionId));
	}

	@Transactional
	public int deleteImageSectionById(QiImageSection imageSection) {
		Parameters params = Parameters.with("id.imageSectionId", imageSection.getId().getImageSectionId())			
				.put("id.partLocationId", imageSection.getId().getPartLocationId());
		return delete(params);
	}

	@Transactional
	public int deleteAllByImageSetionId(int imageSectionId) {
		return delete(Parameters.with("id.imageSectionId",imageSectionId));
	}

	public List<QiImageSection> findImageSectionByImageSectionId(int imageSectionId) {
		return findAll( Parameters.with("id.imageSectionId",imageSectionId));
	}


	@Transactional
	public void updateImageName(String newImageName, String updateUser, String oldImageName) {
		Parameters params = Parameters.with("1", newImageName).put("2", updateUser).put("3", oldImageName);
		executeNativeUpdate(UPDATE_IMAGE_NAME_FOR_IMAGE_SECTION, params);
	}


	/** This method will return list of unique image section ids having the provided imageName
	 * 
	 *  @param imageName 
	 */
	public List<String> findAllByImageName(String imageName) {
		return findResultListByNativeQuery(FIND_ALL_BY_IMAGE_NAME, Parameters.with("1", imageName));
	}

	public int getEntryScreenCountByImageAndPartLocation(String imageName, List<Integer> partLocationId) {
		Parameters params = Parameters.with("1", imageName);
		return findResultListByNativeQuery(FIND_LOCAL_COUNT_BY_IMAGE_AND_PART1 + StringUtils.join(partLocationId, ',') + FIND_LOCAL_COUNT_BY_IMAGE_AND_PART2, params).size();
	}

	public int getEntryScreenCountByImageAndSectionId(String imageName, int sectionId, List<Integer> partLocationId) {
		Parameters params = Parameters.with("1", imageName)
				.put("2", sectionId);
		return findResultListByNativeQuery(FIND_LOCAL_COUNT_BY_IMAGE_AND_SECTION1 +  StringUtils.join(partLocationId, ',') + FIND_LOCAL_COUNT_BY_IMAGE_AND_SECTION2, params).size();
	}

	public List<String> findAllImageNameBySection() {
		return findResultListByNativeQuery(FIND_ALL_IMAGE_NAME_BY_SECTION, null);
	}

	public List<Integer> findAllPartLocationIdsWithValidSectionByImageName(String entryScreen, String imageName) {
		Parameters params = Parameters.with("1", entryScreen).put("2", imageName);
		return findResultListByNativeQuery(FIND_ALL_PART_LOCATION_IDS_BY_IMAGE_NAME,params);
	}

}
