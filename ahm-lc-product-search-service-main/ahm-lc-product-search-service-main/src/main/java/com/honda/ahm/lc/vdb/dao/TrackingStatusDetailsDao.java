package com.honda.ahm.lc.vdb.dao;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.honda.ahm.lc.vdb.entity.TrackingStatusDetails;
import com.honda.ahm.lc.vdb.entity.TrackingStatusDetailsId;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>TrackingStatusDetailsDao</code> is dao for TrackingStatusDetails .
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Hemant Rajput
 * @created Apr 22, 2021
 */

public interface TrackingStatusDetailsDao extends JpaRepository<TrackingStatusDetails, TrackingStatusDetailsId> {
	
	public static final String FIND_ALL_LINE = "select distinct e.id.lineName from TrackingStatusDetails e where (e.passingCountFlag = 1 or e.trackingPointFlag = 1) and "
			+ "(:checkDivNameList = 0 or e.id.divisionName in (:divNameList)) and "
			+ "(:checkPlantNameList = 0 or e.plantName in (:plantNameList)) "
			+ "order by e.id.lineName";
	
	public static final String FIND_ALL_PROCESS_POINT = "select distinct e.id.processPointName from TrackingStatusDetails e "
			+ "where (e.passingCountFlag = 1 or e.trackingPointFlag = 1) and "
			+ "(:lineName is null or e.id.lineName = :lineName) and "
			+ "(:checkDivNameList = 0 or e.id.divisionName in (:divNameList)) and "
			+ "(:checkPlantNameList = 0 or e.plantName in (:plantNameList)) "
			+ "order by e.id.processPointName";
	
	
	@Query(value = FIND_ALL_LINE)
	@Cacheable(value = "findAllLine", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null || #result.isEmpty()")
    public List<String> findAllLine(
    		@Param("divNameList") List<String> divNameList,
    		@Param("checkDivNameList") Integer checkDivNameList,
    		@Param("plantNameList") List<String> plantNameList,
    		@Param("checkPlantNameList") Integer checkPlantNameList
    	);
	
	@Query(value = FIND_ALL_PROCESS_POINT)
	@Cacheable(value = "findAllProcessPoint", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null || #result.isEmpty()")
    public List<String> findAllProcessPoint(
    		@Param("lineName") String lineName, 
    		@Param("divNameList") List<String> divNameList,
    		@Param("checkDivNameList") Integer checkDivNameList,
    		@Param("plantNameList") List<String> plantNameList,
    		@Param("checkPlantNameList") Integer checkPlantNameList
    	);
	
	
}
