package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiRepairAreaDao;
import com.honda.galc.entity.qi.QiRepairArea;
import com.honda.galc.service.Parameters;

public class QiRepairAreaDaoImpl extends BaseDaoImpl<QiRepairArea, String> implements QiRepairAreaDao {
	
	private static final String UPDATE_REPAIR_AREA = "update GALADM.QI_REPAIR_AREA_TBX  set REPAIR_AREA_NAME = ?1, SITE_NAME= ?2 , " +
			"PLANT_NAME = ?3, REPAIR_AREA_DESCRIPTION = ?4,LOCATION = ?5,PRIORITY = ?6,ROW_FILL_SEQ = ?7,  " +
			" UPDATE_USER = ?8,DIVISION_NAME = ?10 where REPAIR_AREA_NAME= ?9";

	private static final String FIND_ALL_BY_SITE_AND_PLANT = "SELECT TRIM(E.REPAIR_AREA_NAME) FROM GALADM.QI_REPAIR_AREA_TBX E WHERE E.SITE_NAME = ?1  AND E.PLANT_NAME = ?2 ";

	private static final String FIND_ALL_PLANTS_BY_SITE_AND_LOCATION ="select distinct trim(area.PLANT_NAME) from galadm.QI_REPAIR_AREA_TBX area "+
			"join galadm.QI_REPAIR_AREA_SPACE_TBX space on space.REPAIR_AREA_NAME = area.REPAIR_AREA_NAME where area.SITE_NAME=?1 and area.LOCATION=?2 "+
			"and space.PRODUCT_ID is null and space.ACTIVE = 1";
	
	private static final String FIND_ALL_BY_SITE_PLANT_DIVISION_PRODUCT_ID = "select r.* from GALADM.QI_REPAIR_AREA_TBX r INNER JOIN GALADM.QI_REPAIR_AREA_SPACE_TBX rs on r.REPAIR_AREA_NAME = rs.REPAIR_AREA_NAME "
			+ "where r.SITE_NAME = ?1 and r.PLANT_NAME = ?2 and r.DIVISION_NAME = ?3 and trim(rs.PRODUCT_ID) like ?4 order by r.REPAIR_AREA_NAME";
	
	private static final String FIND_ALL_BY_SITE_PLANT_DIVISION = "select r from QiRepairArea r where r.siteName = :site and r.plantName = :plant and r.divName = :div";
	
	private static final String FIND_ALL_AVAIL_BY_SITE_AND_LOCATION ="select area.* from galadm.QI_REPAIR_AREA_TBX area "+
			"join galadm.QI_REPAIR_AREA_SPACE_TBX space on space.REPAIR_AREA_NAME = area.REPAIR_AREA_NAME where area.SITE_NAME=?1 and area.LOCATION=?2 "+
			"and space.PRODUCT_ID is null and space.ACTIVE = 1";


	public List<QiRepairArea> findAllBySiteAndPlant(String siteName, String plantName) {
		return findAll(Parameters.with("siteName", siteName).put("plantName", plantName));
	}
	
	@Transactional
	public void updateRepairArea(QiRepairArea qiRepairArea, String oldRepairAreaName) {
		Parameters params = Parameters.with("1", qiRepairArea.getRepairAreaName())
				.put("2", qiRepairArea.getSiteName()).put("3",qiRepairArea.getPlantName()).put("4",qiRepairArea.getRepairAreaDescription())
				.put("5", qiRepairArea.getLocation()).put("6",qiRepairArea.getPriority()).put("7",qiRepairArea.getRowFillSeq()).put("8",qiRepairArea.getUpdateUser())
				.put("9",oldRepairAreaName).put("10", qiRepairArea.getDivName());
		executeNativeUpdate(UPDATE_REPAIR_AREA, params);
	}
	
	

	public List<String> findAllBySitePlantLocation(String siteName, String plantName, char location){
		Parameters params = Parameters.with("1", siteName).put("2", plantName);
		
		StringBuilder queryString = new StringBuilder(FIND_ALL_BY_SITE_AND_PLANT);
		if(location=='I') {
			queryString.append(" AND E.LOCATION = 'I' ");
		}
		
		else if(location=='O'){
			queryString.append(" AND E.LOCATION = 'O' ");
		}
		
		if(location != 'A'){
			queryString.append(" AND E.REPAIR_AREA_NAME IN (SELECT A.REPAIR_AREA_NAME FROM GALADM.QI_REPAIR_AREA_SPACE_TBX A "
					+ "WHERE A.PRODUCT_ID IS NULL  AND A.ACTIVE = 1  ) ORDER BY E.REPAIR_AREA_NAME");
		}
		else{
			queryString.append(" ORDER BY E.REPAIR_AREA_NAME");
		}
		
		return findAllByNativeQuery(queryString.toString(), params, String.class);
	}


	public QiRepairArea findBySiteAndPlant(String site, String plant) {
		return findFirst(Parameters.with("siteName", site).put("plantName", plant).put("location", 'T'));
	}
	
	public QiRepairArea isIntransitRepairAreaExist() {
		return findFirst(Parameters.with("location", 'T'));
	}
	
	
	public List<QiRepairArea> findAllRepairArea() {
		return findAll(null, new String[]{"repairAreaName"}, true);
	}

	public List<String> findAllPlantsBySiteAndLoc(String siteName, char location) {
		return findAllByNativeQuery(FIND_ALL_PLANTS_BY_SITE_AND_LOCATION, Parameters.with("1", siteName).put("2", location), String.class);
	}
	
	public List<QiRepairArea> findAllBySite(String siteName) {
		return findAll(Parameters.with("siteName", siteName));
	}
	
	public List<QiRepairArea> findRepairAreaBySitePlantDivisionProductId(String siteName, String plantName, String divisionName, String productId){
		String queryString = FIND_ALL_BY_SITE_PLANT_DIVISION_PRODUCT_ID;
		Parameters params = Parameters.with("1", siteName).put("2", plantName).put("3" ,divisionName).put("4", "%" + StringUtils.trim(productId));
		return findAllByNativeQuery(queryString, params);
	}
	
	public List<QiRepairArea> findRepairAreaBySitePlantDivisionLocation(String siteName, String plantName, String divisionName, char location){
		StringBuilder queryString = new StringBuilder(FIND_ALL_BY_SITE_PLANT_DIVISION);
		if(location=='I') {
			queryString.append(" AND r.location = 'I' ");
		}else if(location=='O'){
			queryString.append(" AND r.location = 'O' ");
		}else if(location=='T'){
			queryString.append(" AND r.location = 'T' ");
		}
		
		queryString.append(" order by r.repairAreaName");
		Parameters params = Parameters.with("site", siteName).put("plant", plantName).put("div" ,divisionName);
		return findAllByQuery(queryString.toString(), params);
	}
	
	public List<QiRepairArea> findRepairAreaBySitePlantDivisionPartialName(String siteName, String plantName, String divisionName, String repairAreaName){
		StringBuilder queryString = new StringBuilder(FIND_ALL_BY_SITE_PLANT_DIVISION);
		queryString.append(" and r.repairAreaName like '%" + repairAreaName + "%' ");
		
		queryString.append(" order by r.repairAreaName");
		Parameters params = Parameters.with("site", siteName).put("plant", plantName).put("div" ,divisionName);
		return findAllByQuery(queryString.toString(), params);
	}
	
	public List<QiRepairArea> findAllAvailRepairAreaBySiteLocation(String siteName, char location) {
		Parameters params = Parameters.with("1", siteName).put("2", location);
		return findAllByNativeQuery(FIND_ALL_AVAIL_BY_SITE_AND_LOCATION, params);
	}
}
