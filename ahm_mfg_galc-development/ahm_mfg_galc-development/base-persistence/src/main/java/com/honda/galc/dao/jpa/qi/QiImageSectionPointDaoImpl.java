package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiImageSectionPointDao;
import com.honda.galc.entity.qi.QiImageSectionPoint;
import com.honda.galc.entity.qi.QiImageSectionPointId;
import com.honda.galc.service.Parameters;
/** * *
* @version 0.1
* @author LnT Infotech
* @since May 19,2016
*/
public class QiImageSectionPointDaoImpl extends BaseDaoImpl<QiImageSectionPoint, QiImageSectionPointId> implements QiImageSectionPointDao{
	
	final static String FIND_POLYGON_POINTS = "SELECT QISP.* FROM GALADM.QI_IMAGE_SECTION_TBX QIS JOIN GALADM.QI_IMAGE_SECTION_POINT_TBX QISP ON"
			+" (QIS.IMAGE_SECTION_ID = QISP.IMAGE_SECTION_ID) WHERE QIS.PART_LOCATION_ID = ?1 AND QIS.IMAGE_NAME = ?2 ORDER BY QISP.IMAGE_SECTION_ID, QISP.POINT_SEQUENCE_NO";
	
	final static String SHOW_ALL_IMAGE_SECTION = "SELECT DISTINCT ISP.* FROM GALADM.QI_IMAGE_TBX I JOIN GALADM.QI_IMAGE_SECTION_TBX ISC"
			+" ON I.IMAGE_NAME = ISC.IMAGE_NAME JOIN GALADM.QI_IMAGE_SECTION_POINT_TBX ISP ON"
			+" ISC.IMAGE_SECTION_ID = ISP.IMAGE_SECTION_ID WHERE I.IMAGE_NAME = ?1 ORDER BY ISP.IMAGE_SECTION_ID, ISP.POINT_SEQUENCE_NO";
	
	final static String SHOW_ALL_IMAGE_SECTION_BY_DEFECT_FILTER = "SELECT DISTINCT qiImageSectionPoint.* FROM GALADM.QI_LOCAL_DEFECT_COMBINATION_TBX qiLocalDefectCombination " +
			"JOIN GALADM.QI_REGIONAL_DEFECT_COMBINATION_TBX qiRegionalDefectCombination ON qiLocalDefectCombination.REGIONAL_DEFECT_COMBINATION_ID=qiRegionalDefectCombination.REGIONAL_DEFECT_COMBINATION_ID " +
			"JOIN GALADM.QI_IMAGE_SECTION_TBX qiImageSection ON qiImageSection.PART_LOCATION_ID=qiRegionalDefectCombination.PART_LOCATION_ID " +
			"JOIN GALADM.QI_IMAGE_TBX qiImage ON qiImageSection.IMAGE_NAME=qiImage.IMAGE_NAME " +
			"JOIN GALADM.QI_ENTRY_SCREEN_TBX qiEntryScreen ON qiEntryScreen.ENTRY_SCREEN=qiLocalDefectCombination.ENTRY_SCREEN " +
			"JOIN GALADM.QI_ENTRY_MODEL_GROUPING_TBX qiEntryModelGrouping ON qiEntryScreen.ENTRY_MODEL=qiEntryModelGrouping.ENTRY_MODEL " +
			"JOIN GALADM.QI_ENTRY_MODEL_TBX EM ON qiEntryModelGrouping.ENTRY_MODEL=EM.ENTRY_MODEL AND EM.ACTIVE=1 AND EM.PRODUCT_TYPE=?1 " +
			"JOIN GALADM.QI_STATION_ENTRY_SCREEN_TBX qiStationEntryScreen ON qiLocalDefectCombination.ENTRY_SCREEN=qiStationEntryScreen.ENTRY_SCREEN " +
			"JOIN GALADM.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX qiEntryScreenDefectCombination ON qiLocalDefectCombination.ENTRY_SCREEN=qiEntryScreenDefectCombination.ENTRY_SCREEN " +
			"JOIN GALADM.QI_IMAGE_SECTION_POINT_TBX qiImageSectionPoint ON qiImageSection.IMAGE_SECTION_ID = qiImageSectionPoint.IMAGE_SECTION_ID " +
			"@MBPN_JOIN@" +
			"WHERE qiRegionalDefectCombination.PRODUCT_KIND=?2 " +
			"AND qiLocalDefectCombination.ENTRY_SCREEN=?3 " +
			"AND qiImageSection.IMAGE_NAME=?4 " +
			"AND qiRegionalDefectCombination.ACTIVE=1 " +
			"AND qiImage.ACTIVE=1 " +
			"AND qiEntryModelGrouping.MTC_MODEL=?5 " +
			"AND qiStationEntryScreen.PROCESS_POINT_ID=?6 " +
			"AND qiLocalDefectCombination.REGIONAL_DEFECT_COMBINATION_ID=qiEntryScreenDefectCombination.REGIONAL_DEFECT_COMBINATION_ID " +
			"AND (qiRegionalDefectCombination.IQS_ID IS NOT NULL AND  qiRegionalDefectCombination.IQS_ID <> 0) " +
			"AND  COALESCE(qiRegionalDefectCombination.THEME_NAME,'') != '' " +
			"@MBPN_WHERE@" +
			"ORDER BY qiImageSectionPoint.IMAGE_SECTION_ID, qiImageSectionPoint.POINT_SEQUENCE_NO";
	
	final static String SHOW_ALL_IMAGE_SECTION_BY_DEFECT_FILTER_MBPN_JOIN = "JOIN GALADM.QI_PART_LOCATION_COMBINATION_TBX qiPartLocationCombination ON qiPartLocationCombination.PART_LOCATION_ID = qiImageSection.PART_LOCATION_ID " +
			"JOIN GALADM.QI_BOM_QICS_PART_MAPPING_TBX qiBomQicsPartMapping ON qiBomQicsPartMapping.INSPECTION_PART_NAME = qiPartLocationCombination.INSPECTION_PART_NAME ";
	
	final static String SHOW_ALL_IMAGE_SECTION_BY_DEFECT_FILTER_MBPN_WHERE = "AND qiBomQicsPartMapping.MAIN_PART_NO=?7 ";
	
	public List<QiImageSectionPoint> findPolygonPoints(int partLocationId, String imageName){
		Parameters params = Parameters.with("1", partLocationId);
		params.put("2", imageName);
		return findAllByNativeQuery(FIND_POLYGON_POINTS,params);
	}
	@Transactional
	public int deleteAllByImageSetionId(int imageSectionId) {
		return delete(Parameters.with("id.imageSectionId",imageSectionId));
	}
	public List<QiImageSectionPoint> findPolygonPoints(int imageSectionId){
		Parameters params = Parameters.with("id.imageSectionId", imageSectionId);
		String[] orderBy = {"id.pointSequenceNo"};
		return findAll(params, orderBy);
	}
	public List<QiImageSectionPoint> findAllByImageName(String imageName) {
		Parameters params = Parameters.with("1", imageName);
		return findAllByNativeQuery(SHOW_ALL_IMAGE_SECTION,params);
	}
	/**
	 * This method is used to get All Image Section with respect to part defect description
	 */
	public List<QiImageSectionPoint> findAllByDefectFilter(String productKind, String entryScreen, String imageName, String mtcModel, String processPointId, String productType, String mainPartNo) {
		Parameters params = Parameters.with("1", productType)
				.put("2", productKind)
				.put("3", entryScreen)
				.put("4", imageName)
				.put("5", mtcModel)
				.put("6", processPointId);
		if (mainPartNo != null) {
			params = params.put("7", mainPartNo);
			return findAllByNativeQuery(SHOW_ALL_IMAGE_SECTION_BY_DEFECT_FILTER.replace("@MBPN_JOIN@", SHOW_ALL_IMAGE_SECTION_BY_DEFECT_FILTER_MBPN_JOIN).replace("@MBPN_WHERE@", SHOW_ALL_IMAGE_SECTION_BY_DEFECT_FILTER_MBPN_WHERE), params);
		} else {
			return findAllByNativeQuery(SHOW_ALL_IMAGE_SECTION_BY_DEFECT_FILTER.replace("@MBPN_JOIN@", "").replace("@MBPN_WHERE@", ""), params);
		}
	}
}
