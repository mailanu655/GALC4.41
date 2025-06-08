package com.honda.galc.dao.jpa.qi;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiDefectResultImageDao;
import com.honda.galc.dto.qi.QiDefectResultImageDto;
import com.honda.galc.entity.qi.QiDefectResultImage;
import com.honda.galc.entity.qi.QiDefectResultImageId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>Class description</h3>
 * 
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>October 02, 2020</TD>
 * <TD>1.0</TD>
 * <TD>DY 20201002</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 * 
 * </TABLE>
 * 
 * @author Dylan Yang<br>
 *         October 2, 2020
 */


public class QiDefectResultImageDaoImpl extends BaseDaoImpl<QiDefectResultImage, QiDefectResultImageId> implements QiDefectResultImageDao {

	private static final String FIND_ALL_BY_DEFECT_RESULT_ID = "SELECT * FROM GALADM.QI_DEFECT_RESULT_IMAGE_TBX WHERE DEFECTRESULTID = ?1";
	
	private static final String FIND_ALL_BY_PRODUCT_ID = 
			"SELECT b.PRODUCT_ID, a.APPLICATION_ID, a.DEFECTRESULTID AS DEFECT_RESULT_ID, 0 AS REPAIR_ID, a.IMAGE_URL, " + 
			"(REPLACE(REPLACE(REPLACE(b.INSPECTION_PART_NAME || ' ' || COALESCE(b.INSPECTION_PART_LOCATION_NAME, '') || ' ' || " + 
			"COALESCE(b.INSPECTION_PART_LOCATION2_NAME, '') || ' ' || COALESCE(b.INSPECTION_PART2_NAME,'')|| ' ' || " + 
			"COALESCE(b.INSPECTION_PART2_LOCATION_NAME, '') || ' ' || COALESCE(b.INSPECTION_PART2_LOCATION2_NAME, '') || ' ' || " + 
			"COALESCE(b.INSPECTION_PART3_NAME, '') || ' ' || COALESCE(b.DEFECT_TYPE_NAME, '') || ' ' || " + 
			"COALESCE(b.DEFECT_TYPE_NAME2, ''),' ','{}'),'}{',''),'{}',' ')) as PART_DEFECT_COMBINATION " + 
			"FROM GALADM.QI_DEFECT_RESULT_IMAGE_TBX a LEFT JOIN GALADM.QI_DEFECT_RESULT_TBX b on a.DEFECTRESULTID = b.DEFECTRESULTID " + 
			"WHERE b.PRODUCT_ID = ?1 " + 
			"UNION " + 
			"SELECT e.PRODUCT_ID, d.APPLICATION_ID, e.DEFECTRESULTID AS DEFECT_RESULT_ID, d.REPAIR_ID, d.IMAGE_URL, " + 
			"(REPLACE(REPLACE(REPLACE(e.INSPECTION_PART_NAME || ' ' || COALESCE(e.INSPECTION_PART_LOCATION_NAME, '') || ' ' || " + 
			"COALESCE(e.INSPECTION_PART_LOCATION2_NAME, '') || ' ' || COALESCE(e.INSPECTION_PART2_NAME,'')|| ' ' || " + 
			"COALESCE(e.INSPECTION_PART2_LOCATION_NAME, '') || ' ' || COALESCE(e.INSPECTION_PART2_LOCATION2_NAME, '') || ' ' || " + 
			"COALESCE(e.INSPECTION_PART3_NAME, '') || ' ' || COALESCE(e.DEFECT_TYPE_NAME, '') || ' ' || " + 
			"COALESCE(e.DEFECT_TYPE_NAME2, ''),' ','{}'),'}{',''),'{}',' ')) as PART_DEFECT_COMBINATION " + 
			"FROM GALADM.QI_REPAIR_RESULT_IMAGE_TBX d LEFT JOIN GALADM.QI_REPAIR_RESULT_TBX f on d.REPAIR_ID = f.REPAIR_ID " + 
			"LEFT JOIN GALADM.QI_DEFECT_RESULT_TBX e on f.DEFECTRESULTID = e.DEFECTRESULTID WHERE e.PRODUCT_ID = ?1";	
	
	private static final String FIND_ALL_BY_APPLICATION_ID =
			"SELECT b.PRODUCT_ID, a.APPLICATION_ID, a.DEFECTRESULTID AS DEFECT_RESULT_ID, 0 AS REPAIR_ID, a.IMAGE_URL, " + 
			"(REPLACE(REPLACE(REPLACE(b.INSPECTION_PART_NAME || ' ' || COALESCE(b.INSPECTION_PART_LOCATION_NAME, '') || ' ' || " + 
			"COALESCE(b.INSPECTION_PART_LOCATION2_NAME, '') || ' ' || COALESCE(b.INSPECTION_PART2_NAME,'')|| ' ' || " + 
			"COALESCE(b.INSPECTION_PART2_LOCATION_NAME, '') || ' ' || COALESCE(b.INSPECTION_PART2_LOCATION2_NAME, '') || ' ' || " + 
			"COALESCE(b.INSPECTION_PART3_NAME, '') || ' ' || COALESCE(b.DEFECT_TYPE_NAME, '') || ' ' || " + 
			"COALESCE(b.DEFECT_TYPE_NAME2, ''),' ','{}'),'}{',''),'{}',' ')) as PART_DEFECT_COMBINATION " + 
			"FROM GALADM.QI_DEFECT_RESULT_IMAGE_TBX a LEFT JOIN GALADM.QI_DEFECT_RESULT_TBX b on a.DEFECTRESULTID = b.DEFECTRESULTID " + 
			"WHERE a.APPLICATION_ID = ?1 " + 
			"UNION " + 
			"SELECT e.PRODUCT_ID, d.APPLICATION_ID, e.DEFECTRESULTID AS DEFECT_RESULT_ID, d.REPAIR_ID, d.IMAGE_URL, " + 
			"(REPLACE(REPLACE(REPLACE(e.INSPECTION_PART_NAME || ' ' || COALESCE(e.INSPECTION_PART_LOCATION_NAME, '') || ' ' || " + 
			"COALESCE(e.INSPECTION_PART_LOCATION2_NAME, '') || ' ' || COALESCE(e.INSPECTION_PART2_NAME,'')|| ' ' || " + 
			"COALESCE(e.INSPECTION_PART2_LOCATION_NAME, '') || ' ' || COALESCE(e.INSPECTION_PART2_LOCATION2_NAME, '') || ' ' || " + 
			"COALESCE(e.INSPECTION_PART3_NAME, '') || ' ' || COALESCE(e.DEFECT_TYPE_NAME, '') || ' ' || " + 
			"COALESCE(e.DEFECT_TYPE_NAME2, ''),' ','{}'),'}{',''),'{}',' ')) as PART_DEFECT_COMBINATION " + 
			"FROM GALADM.QI_REPAIR_RESULT_IMAGE_TBX d LEFT JOIN GALADM.QI_REPAIR_RESULT_TBX f on d.repair_id = f.repair_id " + 
			"LEFT JOIN GALADM.QI_DEFECT_RESULT_TBX e on f.DEFECTRESULTID = e.DEFECTRESULTID WHERE d.APPLICATION_ID = ?1";	
	
	private static final String FIND_ALL_BY_PART_DEFECT_COMBINATION = 
			"SELECT b.PRODUCT_ID, a.APPLICATION_ID, a.DEFECTRESULTID AS DEFECT_RESULT_ID, 0 AS REPAIR_ID, a.IMAGE_URL, " + 
			"(REPLACE(REPLACE(REPLACE(b.INSPECTION_PART_NAME || ' ' || COALESCE(b.INSPECTION_PART_LOCATION_NAME, '') || ' ' || " + 
			"COALESCE(b.INSPECTION_PART_LOCATION2_NAME, '') || ' ' || COALESCE(b.INSPECTION_PART2_NAME,'')|| ' ' || " + 
			"COALESCE(b.INSPECTION_PART2_LOCATION_NAME, '') || ' ' || COALESCE(b.INSPECTION_PART2_LOCATION2_NAME, '') || ' ' || " + 
			"COALESCE(b.INSPECTION_PART3_NAME, '') || ' ' || COALESCE(b.DEFECT_TYPE_NAME, '') || ' ' || " + 
			"COALESCE(b.DEFECT_TYPE_NAME2, ''),' ','{}'),'}{',''),'{}',' ')) as PART_DEFECT_COMBINATION " + 
			"FROM GALADM.QI_DEFECT_RESULT_IMAGE_TBX a LEFT JOIN GALADM.QI_DEFECT_RESULT_TBX b on a.DEFECTRESULTID = b.DEFECTRESULTID " + 
			"WHERE b.INSPECTION_PART_NAME LIKE ?1 OR b.INSPECTION_PART2_NAME LIKE ?1 OR b.INSPECTION_PART3_NAME LIKE ?1 OR " + 
			"b.INSPECTION_PART_LOCATION_NAME LIKE ?1 OR b.INSPECTION_PART_LOCATION2_NAME LIKE ?1 OR " + 
			"b.INSPECTION_PART2_LOCATION_NAME LIKE ?1 OR b.INSPECTION_PART2_LOCATION2_NAME LIKE ?1 OR " + 
			"b.DEFECT_TYPE_NAME LIKE ?1 OR b.DEFECT_TYPE_NAME2 LIKE ?1 " + 
			"UNION " + 
			"SELECT e.PRODUCT_ID, d.APPLICATION_ID, e.DEFECTRESULTID AS DEFECT_RESULT_ID, d.REPAIR_ID, d.IMAGE_URL, " + 
			"(REPLACE(REPLACE(REPLACE(e.INSPECTION_PART_NAME || ' ' || COALESCE(e.INSPECTION_PART_LOCATION_NAME, '') || ' ' || " + 
			"COALESCE(e.INSPECTION_PART_LOCATION2_NAME, '') || ' ' || COALESCE(e.INSPECTION_PART2_NAME,'')|| ' ' || " + 
			"COALESCE(e.INSPECTION_PART2_LOCATION_NAME, '') || ' ' || COALESCE(e.INSPECTION_PART2_LOCATION2_NAME, '') || ' ' || " + 
			"COALESCE(e.INSPECTION_PART3_NAME, '') || ' ' || COALESCE(e.DEFECT_TYPE_NAME, '') || ' ' || " + 
			"COALESCE(e.DEFECT_TYPE_NAME2, ''),' ','{}'),'}{',''),'{}',' ')) as PART_DEFECT_COMBINATION " + 
			"FROM GALADM.QI_REPAIR_RESULT_IMAGE_TBX d LEFT JOIN GALADM.QI_REPAIR_RESULT_TBX f on d.repair_id = f.repair_id " + 
			"LEFT JOIN GALADM.QI_DEFECT_RESULT_TBX e on f.DEFECTRESULTID = e.DEFECTRESULTID " + 
			"WHERE e.INSPECTION_PART_NAME LIKE ?1 OR e.INSPECTION_PART2_NAME LIKE ?1 OR e.INSPECTION_PART3_NAME LIKE ?1 OR " + 
			"e.INSPECTION_PART_LOCATION_NAME LIKE ?1 OR e.INSPECTION_PART_LOCATION2_NAME LIKE ?1 OR " + 
			"e.INSPECTION_PART2_LOCATION_NAME LIKE ?1 OR e.INSPECTION_PART2_LOCATION2_NAME LIKE ?1 OR " + 
			"e.DEFECT_TYPE_NAME LIKE ?1 OR e.DEFECT_TYPE_NAME2 LIKE ?1";
	
	@Override
	public List<QiDefectResultImage> findAllByDefectResultId(long defectResultId) {
		Parameters params = Parameters.with("1", defectResultId);
		return findAllByNativeQuery(FIND_ALL_BY_DEFECT_RESULT_ID, params);
	}

	@Override
	@Transactional
	public QiDefectResultImage save(QiDefectResultImage defectResultImage) {
		defectResultImage.setActualTimestamp(getDatabaseTimeStamp());
		return (QiDefectResultImage) super.save(defectResultImage);
	}

	@Override
	@Transactional
	public List<QiDefectResultImage> saveAll(List<QiDefectResultImage> entities) {
		Timestamp ts = getDatabaseTimeStamp();
		for(QiDefectResultImage entity : entities) {
			entity.setActualTimestamp(ts);
		}
		return super.saveAll(entities);
	}

	@Override
	@Transactional
	public QiDefectResultImage insert(QiDefectResultImage defectResultImage) {
		defectResultImage.setActualTimestamp(getDatabaseTimeStamp());
		return (QiDefectResultImage) super.insert(defectResultImage);
	}

	@Override
	@Transactional
	public void insertAll(List<QiDefectResultImage> entities) {
		Timestamp ts = getDatabaseTimeStamp();
		for(QiDefectResultImage entity : entities) {
			entity.setActualTimestamp(ts);
		}
		super.insertAll(entities);
	}
	
	public List<QiDefectResultImageDto> findAllByProductId(String productId) {
		Parameters param = Parameters.with("1", StringUtils.trim(productId));
		List<QiDefectResultImageDto> result = findAllByNativeQuery(FIND_ALL_BY_PRODUCT_ID, param, QiDefectResultImageDto.class);
		return result;
	}

	public List<QiDefectResultImageDto> findAllByApplicationId(String applicationId) {
		Parameters param = Parameters.with("1", StringUtils.trim(applicationId));
		List<QiDefectResultImageDto> result = findAllByNativeQuery(FIND_ALL_BY_APPLICATION_ID, param, QiDefectResultImageDto.class);
		return result;
	}

	public List<QiDefectResultImageDto> findAllByPartDefectCombination(String searchString) {
		Parameters param = Parameters.with("1", "%" + StringUtils.trim(searchString) + "%");
		List<QiDefectResultImageDto> result = findAllByNativeQuery(FIND_ALL_BY_PART_DEFECT_COMBINATION, param, QiDefectResultImageDto.class);
		return result;
	}
}
