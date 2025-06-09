package com.honda.ahm.lc.vdb.dao;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.honda.ahm.lc.vdb.dto.ChartDto;
import com.honda.ahm.lc.vdb.dto.ProductIdDto;
import com.honda.ahm.lc.vdb.entity.HistoryDetails;
import com.honda.ahm.lc.vdb.util.Constants;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>HistoryDetailsDao</code> is dao for HistoryDetails .
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
@Repository
public interface HistoryDetailsDao extends JpaRepository<HistoryDetails, String> {
	
	public static final String FIND_SPEC_ID = "select new com.honda.ahm.lc.vdb.dto.HistoryIdDto(e.id, e.productSpecCode, e.engineSerialNo) from HistoryDetails e where "
			+ "((:checkPlantNameList = 0 or e.plantName in (:plantNameList))and  e.productSpecCode LIKE '%' || :specCode || '%' )";

	public static final String COMMON_QUERY = "(e.actualTimestamp >= :startTime and  e.actualTimestamp <= :endTime) and "
			+ "(e.trackingStatus IN :trackingStatus) and " + " e.trackingPointFlag =1 and " + "((:isShipped = "
			+ Constants.ZERO + " and e.divisionName != '" + Constants.SHIPPED_DIVISION_NAME + "') or "
			+ "(:isShipped = " + Constants.ONE + " and e.divisionName = '" + Constants.SHIPPED_DIVISION_NAME + "') or "
			+ "(:isShipped = " + Constants.TWO + ")) and "
			+ "((:startAfOn = 0 and :endAfOn = 0) or (e.afOnSequenceNumber >= :startAfOn and  e.afOnSequenceNumber <= :endAfOn)) and "
			+ "(:productIdFlag =0 or e.id IN :productId) and "
			+ "(:productionFlag = 0 or e.productionLot IN :productionLots) and "
			+ "(:kdFlag = 0 or e.kdLotNumber IN :kdLotNumbers) and "
			+ "(:specCodeFlag = 0 or e.productSpecCode IN :specCodes) and "
			+ "(:destinationFlag = 0 or e.productSpecCode IN :destination) and "
			+ "(:checkPlantNameList = 0 or e.plantName in (:plantNameList)) ";

	public static final String FIND_PRODUCT_DETAILS_BY = "select e from HistoryDetails e " + "where " + COMMON_QUERY
			+ " and (:searchBy is null or e.productionLot LIKE '%' || :searchBy || '%' or e.id LIKE '%' || :searchBy || '%'  or e.trackingStatus LIKE '%' || :searchBy || '%'"
			+ " or e.kdLotNumber LIKE '%' || :searchBy || '%'"
			+ " or e.lastProcessPoint LIKE '%' || :searchBy || '%'  or e.engineSerialNo LIKE '%' || :searchBy || '%' "
			+ "or e.productSpecCode LIKE '%' || :searchBy || '%' or e.destination LIKE '%' || :searchBy || '%'  )";

	public static final String FIND_PRODUCT_DETAILS_COUNT = "select new com.honda.ahm.lc.vdb.dto.ChartDto(e.divisionName, e.sequenceNumber, count(e)) from HistoryDetails e "
			+ "where " + COMMON_QUERY + "group by e.divisionName, e.sequenceNumber " + "order by e.sequenceNumber";

	public static final String FIND_PRODUCT_DRILLDOWN_COUNT = "select new com.honda.ahm.lc.vdb.dto.ChartDto(e.divisionName, e.lineName, count(e)) from HistoryDetails e "
			+ "where " + COMMON_QUERY + "group by e.divisionName, e.lineName";
	
	@Query(value = FIND_SPEC_ID)
	@Cacheable(value = "findAllProductIds", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null || #result.isEmpty()")
	public List<ProductIdDto> findSpecIds(@Param("plantNameList") List<String> plantNameList,
			@Param("checkPlantNameList") Integer checkPlantNameList,@Param("specCode") String specCode);

	@Query(value = FIND_PRODUCT_DETAILS_BY)
	public Page<HistoryDetails> findProductDetailsBy(@Param("productId") List<String> productId,
			@Param("productionLots") List<String> productionLots, @Param("kdLotNumbers") List<String> kdLotNumbers,
			@Param("specCodes") List<String> specCodes,@Param("destination") List<String> destination, @Param("trackingStatus") List<String> trackingStatus,
			@Param("isShipped") Integer isShipped, @Param("startAfOn") Integer startAfOn,
			@Param("endAfOn") Integer endAfOn, @Param("startTime") Timestamp startTime,
			@Param("endTime") Timestamp endTime, @Param("plantNameList") List<String> plantNameList,
			@Param("checkPlantNameList") Integer checkPlantNameList, @Param("productIdFlag") Integer productIdFlag,
			@Param("productionFlag") Integer productionFlag, @Param("kdFlag") Integer kdFlag,
			@Param("specCodeFlag") Integer specCodeFlag,@Param("destinationFlag") Integer destinationFlag, @Param("searchBy") String searchBy, Pageable page);

	@Query(value = FIND_PRODUCT_DETAILS_COUNT)
	@Cacheable(value = "findProductDetailsCountHistory", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null || #result.isEmpty()")
	public List<ChartDto> findProductDetailsCount(@Param("productId") List<String> productId,
			@Param("productionLots") List<String> productionLots, @Param("kdLotNumbers") List<String> kdLotNumbers,
			@Param("specCodes") List<String> specCodes,@Param("destination") List<String> destination, @Param("trackingStatus") List<String> trackingStatus,
			@Param("isShipped") Integer isShipped, @Param("startAfOn") Integer startAfOn,
			@Param("endAfOn") Integer endAfOn, @Param("startTime") Timestamp startTime,
			@Param("endTime") Timestamp endTime, @Param("plantNameList") List<String> plantNameList,
			@Param("checkPlantNameList") Integer checkPlantNameList, @Param("productIdFlag") Integer productIdFlag,
			@Param("productionFlag") Integer productionFlag, @Param("kdFlag") Integer kdFlag,
			@Param("specCodeFlag") Integer specCodeFlag,@Param("destinationFlag") Integer destinationFlag);

	@Query(value = FIND_PRODUCT_DRILLDOWN_COUNT)
	@Cacheable(value = "findProductDrilldownCountHistory", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null || #result.isEmpty()")
	public List<ChartDto> findProductDrilldownCount(@Param("productId") List<String> productId,
			@Param("productionLots") List<String> productionLots, @Param("kdLotNumbers") List<String> kdLotNumbers,
			@Param("specCodes") List<String> specCodes,@Param("destination") List<String> destination, @Param("trackingStatus") List<String> trackingStatus,
			@Param("isShipped") Integer isShipped, @Param("startAfOn") Integer startAfOn,
			@Param("endAfOn") Integer endAfOn, @Param("startTime") Timestamp startTime,
			@Param("endTime") Timestamp endTime, @Param("plantNameList") List<String> plantNameList,
			@Param("checkPlantNameList") Integer checkPlantNameList, @Param("productIdFlag") Integer productIdFlag,
			@Param("productionFlag") Integer productionFlag, @Param("kdFlag") Integer kdFlag,
			@Param("specCodeFlag") Integer specCodeFlag,@Param("destinationFlag") Integer destinationFlag);

}
