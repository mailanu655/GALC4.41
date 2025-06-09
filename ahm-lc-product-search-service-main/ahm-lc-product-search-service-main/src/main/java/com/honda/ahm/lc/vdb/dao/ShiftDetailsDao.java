package com.honda.ahm.lc.vdb.dao;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.honda.ahm.lc.vdb.entity.ShiftDetails;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>ShiftDetailsDao</code> is dao for ShiftDetails .
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

public interface ShiftDetailsDao extends JpaRepository<ShiftDetails, String> {
	
	public static final String FIND_ALL_SHIFT_DETAILS = "select distinct e.id from ShiftDetails e";
	public static final String FIND_ALL_SHIFT = "select distinct e.shift from ShiftDetails e";
	
	public static final String FIND_START_TIME = "select min(startTime) from ShiftDetails "
			+ "where ((:id is null and shift = :shift) or (:shift is null and id = :id)) "
			+ "and productionDate = :productionDate";
	public static final String FIND_END_TIME = "select max(endTime) from ShiftDetails "
			+ "where ((:id is null and shift = :shift) or (:shift is null and id = :id)) "
			+ "and productionDate = :productionDate";
	
	@Query(value = FIND_ALL_SHIFT_DETAILS)
	@Cacheable(value = "findAllShiftDetails", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)",unless = "#result == null")
	public List<String> findAllShiftDetails();

	@Query(value = FIND_ALL_SHIFT)
	@Cacheable(value = "findAllShift", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)",unless = "#result == null")
	public List<String> findAllShift();
	
	@Query(value = FIND_START_TIME)
	@Cacheable(value = "findStartTime", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)",unless = "#result == null")
    public Time findStartTime(
    		@Param("id") String id, 
    		@Param("shift") String shift,
    		@Param("productionDate") Date productionDate
    	);
	
	@Query(value = FIND_END_TIME)
	@Cacheable(value = "findEndTime", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null")
    public Time findEndTime(
    		@Param("id") String id, 
    		@Param("shift") String shift,
    		@Param("productionDate") Date productionDate
    	);
	
}
