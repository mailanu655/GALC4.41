package com.honda.ahm.lc.vdb.dao;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.honda.ahm.lc.vdb.dto.ChartDto;
import com.honda.ahm.lc.vdb.dto.ProductIdDto;
import com.honda.ahm.lc.vdb.entity.ProductDetails;
import com.honda.ahm.lc.vdb.util.Constants;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>ProductDetailsDao</code> is ... .
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
public interface ProductDetailsDao extends JpaRepository<ProductDetails, String> {

	public static final String FIND_ALL_PRODUCT_ID = "select new com.honda.ahm.lc.vdb.dto.ProductIdDto(e.id, e.productSpecCode, e.engineSerialNo, e.missionSerialNo) from ProductDetails e where "
			+ "(:checkPlantNameList = 0 or e.plantName in (:plantNameList))";
	
	public static final String FIND_SPEC_ID = "select new com.honda.ahm.lc.vdb.dto.ProductIdDto(e.id, e.productSpecCode, e.engineSerialNo, e.missionSerialNo) from ProductDetails e where "
			+ "((:checkPlantNameList = 0 or e.plantName in (:plantNameList))and  e.productSpecCode LIKE '%' || :specCode || '%' )";
	
	public static final String FIND_SPEC_ID_VINRANGE = "select new com.honda.ahm.lc.vdb.dto.ProductIdDto(e.id, e.productSpecCode, e.engineSerialNo, e.missionSerialNo) from ProductDetails e where "
			+ "( e.productSpecCode LIKE '%' || :specCode || '%' )";

	public static final String FIND_PRODUCT_DETAILS_BY = "select e from ProductDetails e " + "where "
			+ "((:isShipped = " + Constants.ZERO + " and e.divisionName != '" + Constants.SHIPPED_DIVISION_NAME
			+ "') or " + "(:isShipped = " + Constants.ONE + " and e.divisionName = '" + Constants.SHIPPED_DIVISION_NAME
			+ "') or " + "(:isShipped = " + Constants.TWO + ")) and "
			+ "(:trackingStatusFlag = 0 or e.trackingStatus IN :trackingStatuss) and "
			+ "(:productIdFlag = 0 or e.id IN :productId) AND "
			+ "(:engineIdFlag = 0 or e.engineSerialNo IN :engineIds) and "
			+ "(:missionIdFlag = 0 or e.missionSerialNo IN :missionId) and "
			+ "(:seqNoFlag = 0 or e.afOnSequenceNumber IN :seqNo) AND "
			+ "(:productionFlag = 0 or e.productionLot IN :productionLots) and "
			+ "(:destinationFlag = 0 or e.destination IN :destination) and "
			+ "(:kdFlag = 0 or e.kdLotNumber IN :kdLotNumbers) and "
			+ "(:specCodeFlag = 0 or e.productSpecCode IN :specCodes) and "
			+ "(:processPoint is null or e.lastProcessPoint = :processPoint) and "
			+ "((:startAfOn = 0 and :endAfOn = 0) or (e.afOnSequenceNumber >= :startAfOn and  e.afOnSequenceNumber <= :endAfOn)) and "
			+ "(:checkPlantNameList = 0 or e.plantName in (:plantNameList)) and "
			+ "(:searchBy is null or e.productionLot LIKE '%' || :searchBy || '%' or e.id LIKE '%' || :searchBy || '%'  or e.trackingStatus LIKE '%' || :searchBy || '%'"
			+ "or e.destination LIKE '%' || :searchBy || '%' or e.kdLotNumber LIKE '%' || :searchBy || '%' or e.engineSerialNo LIKE '%' || :searchBy || '%' "
			+ "or e.productSpecCode LIKE '%' || :searchBy || '%'"
			+ " or e.lastProcessPoint LIKE '%' || :searchBy || '%' )";

	public static final String FIND_PRODUCT_DETAILS_COUNT = "select new com.honda.ahm.lc.vdb.dto.ChartDto(e.divisionName, e.sequenceNumber, count(e)) from ProductDetails e "
			+ "where " + "((:isShipped = " + Constants.ZERO + " and e.divisionName != '"
			+ Constants.SHIPPED_DIVISION_NAME + "') or " + "(:isShipped = " + Constants.ONE + " and e.divisionName = '"
			+ Constants.SHIPPED_DIVISION_NAME + "') or " + "(:isShipped = " + Constants.TWO + ")) and "
			+ "(:trackingStatus is null or e.trackingStatus = :trackingStatus) and "
			+ "(:productId is null or e.id = :productId) and "
			+ "(:productionFlag = 0 or e.productionLot IN :productionLots) and "
			+ "(:kdFlag = 0 or e.kdLotNumber IN :kdLotNumbers) and "
			+ "(:specCodeFlag = 0 or e.productSpecCode IN :specCodes) and "
			+ "(:destinationFlag = 0 or e.destination IN :destination) and "

			+ "(:processPoint is null or e.lastProcessPoint = :processPoint) and "
			+ "((:startAfOn = 0 and :endAfOn = 0) or (e.afOnSequenceNumber >= :startAfOn and  e.afOnSequenceNumber <= :endAfOn)) and "
			+ "(:checkPlantNameList = 0 or e.plantName in (:plantNameList)) "
			+ "group by e.divisionName, e.sequenceNumber " + "order by e.sequenceNumber";

	public static final String FIND_PRODUCT_DRILLDOWN_COUNT = "select new com.honda.ahm.lc.vdb.dto.ChartDto(e.divisionName, e.lineName, count(e)) from ProductDetails e "
			+ "where " + "((:isShipped = " + Constants.ZERO + " and e.divisionName != '"
			+ Constants.SHIPPED_DIVISION_NAME + "') or " + "(:isShipped = " + Constants.ONE + " and e.divisionName = '"
			+ Constants.SHIPPED_DIVISION_NAME + "') or " + "(:isShipped = " + Constants.TWO + ")) and "
			+ "(:trackingStatus is null or e.trackingStatus = :trackingStatus) and "
			+ "((:productId is null or e.id = :productId) and "
			+ "(:productionFlag = 0 or e.productionLot IN :productionLots) and "
			+ "(:kdFlag = 0 or e.kdLotNumber IN :kdLotNumbers) and "
			+ "(:specCodeFlag = 0 or e.productSpecCode IN :specCodes) and "
			+ "(:destinationFlag = 0 or e.destination IN :destination) and "

			+ "(:processPoint is null or e.lastProcessPoint = :processPoint) and "
			+ "((:startAfOn = 0 and :endAfOn = 0) or (e.afOnSequenceNumber >= :startAfOn and  e.afOnSequenceNumber <= :endAfOn)) and "
			+ "(:checkPlantNameList = 0 or e.plantName in (:plantNameList))) " + "group by e.divisionName, e.lineName";

	public static final String FIND_PRODUCT_DETAILS_BY_DEFECT_STATUS = "select e from ProductDetails e "
			+ "join QiDefectCountDetails q on e.id = q.id " + "where " + "((:isShipped = " + Constants.ZERO
			+ " and e.divisionName != '" + Constants.SHIPPED_DIVISION_NAME + "') or " + "(:isShipped = " + Constants.ONE
			+ " and e.divisionName = '" + Constants.SHIPPED_DIVISION_NAME + "') or " + "(:isShipped = " + Constants.TWO
			+ ")) and " + "((:qicsStatus = '" + Constants.NOT_FIXED + "' and q.notFixedCount > 0) or "
			+ "(:qicsStatus = '" + Constants.FIXED
			+ "' and q.fixedCount > 0 and q.notFixedCount = 0 and q.nonRepairableCount = 0) or " + "(:qicsStatus = '"
			+ Constants.NON_REPAIRABLE + "' and q.nonRepairableCount > 0)) and "

			+ "(:processPoint is null or e.lastProcessPoint = :processPoint) and "
			+ "(:productIdFlag = 0 or e.id IN :productId) and "
			+ "(:engineIdFlag = 0 or e.id IN :engineIds) and "
			+ "(:missionIdFlag = 0 or e.id IN :missionId) and "
			+ "(:seqNoFlag = 0 or e.afOnSequenceNumber IN :seqNo) and "
			+ "(:productionFlag = 0 or e.productionLot IN :productionLots) and "
			+ "(:kdFlag = 0 or e.kdLotNumber IN :kdLotNumbers) and "
			+ "(:specCodeFlag = 0 or e.productSpecCode IN :specCodes) and "
			+ "(:destinationFlag = 0 or e.destination IN :destination) and "
			+ "(:trackingStatusFlag = 0 or e.trackingStatus IN :trackingStatuss) and "
			+ "((:startAfOn = 0 and :endAfOn = 0) or (e.afOnSequenceNumber >= :startAfOn and e.afOnSequenceNumber <= :endAfOn)) and "
			+ "(:checkPlantNameList = 0 or e.plantName in (:plantNameList)) and"
			+ "(:searchBy is null or e.productionLot LIKE '%' || :searchBy || '%' or e.id LIKE '%' || :searchBy || '%'  or e.trackingStatus LIKE '%' || :searchBy || '%'"
			+ "or e.destination LIKE '%' || :searchBy || '%' or e.kdLotNumber LIKE '%' || :searchBy || '%' or e.engineSerialNo LIKE '%' || :searchBy || '%' or e.productSpecCode LIKE '%' || :searchBy || '%'"
			+ " or e.lastProcessPoint LIKE '%' || :searchBy || '%' )";

	public static final String FIND_PRODUCT_DETAILS_BY_DEFECT_STATUS_COUNT = "select new com.honda.ahm.lc.vdb.dto.ChartDto(e.divisionName, e.sequenceNumber, count(e)) from ProductDetails e "
			+ "join QiDefectCountDetails q on e.id = q.id " + "where " + "((:qicsStatus = '" + Constants.NOT_FIXED
			+ "' and q.notFixedCount > 0) or " + "(:qicsStatus = '" + Constants.FIXED
			+ "' and q.fixedCount > 0 and q.notFixedCount = 0 and q.nonRepairableCount = 0) or " + "(:qicsStatus = '"
			+ Constants.NON_REPAIRABLE + "' and q.nonRepairableCount > 0)) and " + "((:isShipped = " + Constants.ZERO
			+ " and e.divisionName != '" + Constants.SHIPPED_DIVISION_NAME + "') or " + "(:isShipped = " + Constants.ONE
			+ " and e.divisionName = '" + Constants.SHIPPED_DIVISION_NAME + "') or " + "(:isShipped = " + Constants.TWO
			+ ")) and " + "(:productId is null or e.id = :productId) and "
			+ "(:productionFlag = 0 or e.productionLot IN :productionLots) and "
			+ "(:kdFlag = 0 or e.kdLotNumber IN :kdLotNumbers) and "
			+ "(:specCodeFlag = 0 or e.productSpecCode IN :specCodes) and "
			+ "(:destinationFlag = 0 or e.destination IN :destination) and "
			+ "(:trackingStatus is null or e.trackingStatus = :trackingStatus) and "
		
			+ "(:processPoint is null or e.lastProcessPoint = :processPoint) and "
			+ "((:startAfOn = 0 and :endAfOn = 0) or (e.afOnSequenceNumber >= :startAfOn and e.afOnSequenceNumber <= :endAfOn)) and "
			+ "(:checkPlantNameList = 0 or e.plantName in (:plantNameList)) "
			+ "group by e.divisionName, e.sequenceNumber " + "order by e.sequenceNumber";

	public static final String FIND_PRODUCT_DRILLDOWN_BY_DEFECT_STATUS_COUNT = "select new com.honda.ahm.lc.vdb.dto.ChartDto(e.divisionName, e.lineName, count(e)) from ProductDetails e "
			+ "join QiDefectCountDetails q on e.id = q.id " + "where "
			+ "(:productId is null or e.id = :productId) and "
			+ "(:productionFlag = 0 or e.productionLot IN :productionLots) and "
			+ "(:kdFlag = 0 or e.kdLotNumber IN :kdLotNumbers) and "
			+ "(:specCodeFlag = 0 or e.productSpecCode IN :specCodes) and "
			+ "(:destinationFlag = 0 or e.destination IN :destination) and "
			+ "(:trackingStatus is null or e.trackingStatus = :trackingStatus) and " + "((:isShipped = "
			+ Constants.ZERO + " and e.divisionName != '" + Constants.SHIPPED_DIVISION_NAME + "') or "
			+ "(:isShipped = " + Constants.ONE + " and e.divisionName = '" + Constants.SHIPPED_DIVISION_NAME + "') or "
			+ "(:isShipped = " + Constants.TWO + ")) and "
			+ "(:processPoint is null or e.lastProcessPoint = :processPoint) and "
			+ "((:startAfOn = 0 and :endAfOn = 0) or (e.afOnSequenceNumber >= :startAfOn and e.afOnSequenceNumber <= :endAfOn)) and "
			+ "((:qicsStatus = '" + Constants.NOT_FIXED + "' and q.notFixedCount > 0) or " + "(:qicsStatus = '"
			+ Constants.FIXED + "' and q.fixedCount > 0 and q.notFixedCount = 0 and q.nonRepairableCount = 0) or "
			+ "(:qicsStatus = '" + Constants.NON_REPAIRABLE + "' and q.nonRepairableCount > 0)) and "
			+ "(:checkPlantNameList = 0 or e.plantName in (:plantNameList)) " + "group by e.divisionName, e.lineName";

	public static final String FIND_ALL_KD_LOT = "select distinct e.kdLotNumber from ProductDetails e where "
			+ "((:isShipped = " + Constants.ZERO + " and e.divisionName != '" + Constants.SHIPPED_DIVISION_NAME
			+ "') or " + "(:isShipped = " + Constants.ONE + " and e.divisionName = '" + Constants.SHIPPED_DIVISION_NAME
			+ "') or " + "(:isShipped = " + Constants.TWO + ")) and "
			+ "(:checkPlantNameList = 0 or e.plantName in (:plantNameList))";

	public static final String FIND_ALL_PRODUCTION_LOT = "select distinct e.productionLot from ProductDetails e "
			+ "where (:kdLotNumber is null or e.kdLotNumber = :kdLotNumber) and " + "((:isShipped = " + Constants.ZERO
			+ " and e.divisionName != '" + Constants.SHIPPED_DIVISION_NAME + "') or " + "(:isShipped = " + Constants.ONE
			+ " and e.divisionName = '" + Constants.SHIPPED_DIVISION_NAME + "') or " + "(:isShipped = " + Constants.TWO
			+ ")) and " + "(:checkPlantNameList = 0 or e.plantName in (:plantNameList))";

	public static final String FIND_ALL_MODEL = "select distinct e.modelDescription from ProductDetails e "
			+ "where (:kdLotNumber is null or e.kdLotNumber = :kdLotNumber) and "
			+ "(:productionLot is null or e.productionLot = :productionLot) and " + "((:isShipped = " + Constants.ZERO
			+ " and e.divisionName != '" + Constants.SHIPPED_DIVISION_NAME + "') or " + "(:isShipped = " + Constants.ONE
			+ " and e.divisionName = '" + Constants.SHIPPED_DIVISION_NAME + "') or " + "(:isShipped = " + Constants.TWO
			+ ")) and " + "(:checkPlantNameList = 0 or e.plantName in (:plantNameList))";

	public static final String FIND_ALL_TYPE = "select distinct e.modelTypeCode from ProductDetails e "
			+ "where (:kdLotNumber is null or e.kdLotNumber = :kdLotNumber) and "
			+ "(:productionLot is null or e.productionLot = :productionLot) and "
			+ "(:model is null or e.modelDescription = :model) and " + "((:isShipped = " + Constants.ZERO
			+ " and e.divisionName != '" + Constants.SHIPPED_DIVISION_NAME + "') or " + "(:isShipped = " + Constants.ONE
			+ " and e.divisionName = '" + Constants.SHIPPED_DIVISION_NAME + "') or " + "(:isShipped = " + Constants.TWO
			+ ")) and " + "(:checkPlantNameList = 0 or e.plantName in (:plantNameList))";

	public static final String FIND_ALL_OPTION = "select distinct e.modelOptionCode from ProductDetails e "
			+ "where (:kdLotNumber is null or e.kdLotNumber = :kdLotNumber) and "
			+ "(:productionLot is null or e.productionLot = :productionLot) and "
			+ "(:model is null or e.modelDescription = :model) and " + "(:type is null or e.modelTypeCode = :type) and "
			+ "((:isShipped = " + Constants.ZERO + " and e.divisionName != '" + Constants.SHIPPED_DIVISION_NAME
			+ "') or " + "(:isShipped = " + Constants.ONE + " and e.divisionName = '" + Constants.SHIPPED_DIVISION_NAME
			+ "') or " + "(:isShipped = " + Constants.TWO + ")) and "
			+ "(:checkPlantNameList = 0 or e.plantName in (:plantNameList))";

	public static final String FIND_ALL_EXTERIOR_COLOR = "select distinct e.exteriorColor from ProductDetails e "
			+ "where (:kdLotNumber is null or e.kdLotNumber = :kdLotNumber) and "
			+ "(:productionLot is null or e.productionLot = :productionLot) and "
			+ "(:model is null or e.modelDescription = :model) and " + "(:type is null or e.modelTypeCode = :type) and "
			+ "(:option is null or e.modelOptionCode = :option) and " + "((:isShipped = " + Constants.ZERO
			+ " and e.divisionName != '" + Constants.SHIPPED_DIVISION_NAME + "') or " + "(:isShipped = " + Constants.ONE
			+ " and e.divisionName = '" + Constants.SHIPPED_DIVISION_NAME + "') or " + "(:isShipped = " + Constants.TWO
			+ ")) and " + "(:checkPlantNameList = 0 or e.plantName in (:plantNameList))";

	public static final String FIND_ALL_INTERIOR_COLOR = "select distinct e.interiorColor from ProductDetails e "
			+ "where (:kdLotNumber is null or e.kdLotNumber = :kdLotNumber) and "
			+ "(:productionLot is null or e.productionLot = :productionLot) and "
			+ "(:model is null or e.modelDescription = :model) and " + "(:type is null or e.modelTypeCode = :type) and "
			+ "(:option is null or e.modelOptionCode = :option) and "
			+ "(:exteriorColor is null or e.exteriorColor = :exteriorColor) and " + "((:isShipped = " + Constants.ZERO
			+ " and e.divisionName != '" + Constants.SHIPPED_DIVISION_NAME + "') or " + "(:isShipped = " + Constants.ONE
			+ " and e.divisionName = '" + Constants.SHIPPED_DIVISION_NAME + "') or " + "(:isShipped = " + Constants.TWO
			+ ")) and " + "(:checkPlantNameList = 0 or e.plantName in (:plantNameList))";

	public static final String FIND_ALL_DESTINATION = "select distinct e.destination from ProductDetails e "
			+ "where (:kdLotNumber is null or e.kdLotNumber = :kdLotNumber) and "
			+ "(:productionLot is null or e.productionLot = :productionLot) and "
			+ "(:model is null or e.modelDescription = :model) and " + "(:type is null or e.modelTypeCode = :type) and "
			+ "(:option is null or e.modelOptionCode = :option) and "
			+ "(:exteriorColor is null or e.exteriorColor = :exteriorColor) and "
			+ "(:interiorColor is null or e.interiorColor = :interiorColor) and " + "((:isShipped = " + Constants.ZERO
			+ " and e.divisionName != '" + Constants.SHIPPED_DIVISION_NAME + "') or " + "(:isShipped = " + Constants.ONE
			+ " and e.divisionName = '" + Constants.SHIPPED_DIVISION_NAME + "') or " + "(:isShipped = " + Constants.TWO
			+ ")) and " + "(:checkPlantNameList = 0 or e.plantName in (:plantNameList))";

	public static final String FIND_ALL_TRACKING_STATUS = "select distinct e.trackingStatus from ProductDetails e where "
			+ "(:checkPlantNameList = 0 or e.plantName in (:plantNameList))";

	public static final String FIND_ALL_PROCESS_POINT = "select distinct e.lastProcessPoint from ProductDetails e "
			+ "where :lineName is null or e.lineName = :lineName and "
			+ "(:checkPlantNameList = 0 or e.plantName in (:plantNameList)) " + "order by e.lastProcessPoint";

	public static final String FIND_ALL_DIVISION_NAME = "select distinct e.divisionName from ProductDetails e where "
			+ "(:checkPlantNameList = 0 or e.plantName in (:plantNameList))";

	public static final String FIND_PRODUCT_DETAILS_BY_PRODUCT_ID_LIST = "select e from ProductDetails e " + "where "
			+ "e.id in :productIdList and " + "(:checkPlantNameList = 0 or e.plantName in (:plantNameList)) and "
			+ "(:searchBy is null or e.productionLot LIKE '%' || :searchBy || '%' or e.id LIKE '%' || :searchBy || '%'  or e.trackingStatus LIKE '%' || :searchBy || '%'"
			+ "or e.destination LIKE '%' || :searchBy || '%' or e.kdLotNumber LIKE '%' || :searchBy || '%' or e.engineSerialNo LIKE '%' || :searchBy || '%' or e.productSpecCode LIKE '%' || :searchBy || '%'"
			+ " or e.lastProcessPoint LIKE '%' || :searchBy || '%' )";

	public static final String FIND_PRODUCT_DETAILS_BY_TRACKING_STATUS_LIST = "select e from ProductDetails e "
			+ "where " + "e.trackingStatus in :trackingStatusList and "
			+ "(:checkPlantNameList = 0 or e.plantName in (:plantNameList)) and"
			+ "(:searchBy is null or e.productionLot LIKE '%' || :searchBy || '%' or e.id LIKE '%' || :searchBy || '%'  or e.trackingStatus LIKE '%' || :searchBy || '%'"
			+ "or e.destination LIKE '%' || :searchBy || '%' or e.kdLotNumber LIKE '%' || :searchBy || '%' or e.engineSerialNo LIKE '%' || :searchBy || '%' or e.productSpecCode LIKE '%' || :searchBy || '%'"
			+ " or e.lastProcessPoint LIKE '%' || :searchBy || '%' )";

	public static final String FIND_PRODUCT_DETAILS_BY_ENGINE_NO_LIST = "select e from ProductDetails e " + "where "
			+ "e.engineSerialNo in :engineNoList and "
			+ "(:checkPlantNameList = 0 or e.plantName in (:plantNameList)) and"
			+ "(:searchBy is null or e.productionLot LIKE '%' || :searchBy || '%' or e.id LIKE '%' || :searchBy || '%'  or e.trackingStatus LIKE '%' || :searchBy || '%'"
			+ "or e.destination LIKE '%' || :searchBy || '%' or e.kdLotNumber LIKE '%' || :searchBy || '%' or e.engineSerialNo LIKE '%' || :searchBy || '%' or e.productSpecCode LIKE '%' || :searchBy || '%'"
			+ " or e.lastProcessPoint LIKE '%' || :searchBy || '%' )";
	
	public static final String FIND_PRODUCT_DETAILS_BY_MISSION_NO_LIST = "select e from ProductDetails e " + "where "
			+ "e.missionSerialNo in :missionNoList and "
			+ "(:checkPlantNameList = 0 or e.plantName in (:plantNameList)) and"
			+ "(:searchBy is null or e.productionLot LIKE '%' || :searchBy || '%' or e.id LIKE '%' || :searchBy || '%'  or e.trackingStatus LIKE '%' || :searchBy || '%'"
			+ "or e.destination LIKE '%' || :searchBy || '%' or e.kdLotNumber LIKE '%' || :searchBy || '%' or e.engineSerialNo LIKE '%' || :searchBy || '%' or e.productSpecCode LIKE '%' || :searchBy || '%'"
			+ " or e.lastProcessPoint LIKE '%' || :searchBy || '%' )";

	@Query(value = FIND_ALL_PRODUCT_ID)
	@Cacheable(value = "findAllProductIds", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null || #result.isEmpty()")
	public List<ProductIdDto> findAllProductIds(@Param("plantNameList") List<String> plantNameList,
			@Param("checkPlantNameList") Integer checkPlantNameList);
	
	@Query(value = FIND_SPEC_ID)
	@Cacheable(value = "findAllProductIds", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null || #result.isEmpty()")
	public List<ProductIdDto> findSpecIds(@Param("plantNameList") List<String> plantNameList,
			@Param("checkPlantNameList") Integer checkPlantNameList,@Param("specCode") String specCode);
	
	@Query(value = FIND_SPEC_ID_VINRANGE)
	@Cacheable(value = "findAllProductIds", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null || #result.isEmpty()")
	public List<ProductIdDto> findSpecIds(@Param("specCode") String specCode);

	@Query(value = FIND_PRODUCT_DETAILS_BY)
	public Page<ProductDetails> findProductDetailsBy(@Param("productId") List<String> productId,
			@Param("seqNo") List<Integer> seqNo, @Param("productionLots") List<String> productionLots,
			@Param("kdLotNumbers") List<String> kdLotNumbers, @Param("specCodes") List<String> specCodes, @Param("destination") List<String> destination,
			@Param("trackingStatuss") List<String> trackingStatuss, @Param("isShipped") Integer isShipped,
			@Param("processPoint") String processPoint, @Param("startAfOn") Integer startAfOn,
			@Param("endAfOn") Integer endAfOn, @Param("plantNameList") List<String> plantNameList,
			@Param("checkPlantNameList") Integer checkPlantNameList, @Param("engineIds") List<String> engineIds,
			@Param("missionId") List<String> missionId, @Param("productIdFlag") Integer productIdFlag,

			@Param("seqNoFlag") Integer seqNoFlag, @Param("productionFlag") Integer productionFlag,
			@Param("kdFlag") Integer kdFlag, @Param("specCodeFlag") Integer specCodeFlag,@Param("destinationFlag") Integer destinationFlag,
			@Param("trackingStatusFlag") Integer trackingStatusFlag, @Param("engineIdFlag") Integer engineIdFlag,
			@Param("missionIdFlag") Integer missionIdFlag, @Param("searchBy") String searchBy, Pageable page);

	@Query(value = FIND_PRODUCT_DETAILS_COUNT)
	@Cacheable(value = "findProductDetailsCount", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null || #result.isEmpty()")
	public List<ChartDto> findProductDetailsCount(@Param("productId") String productId,
			@Param("productionLots") List<String> productionLots, @Param("kdLotNumbers") List<String> kdLotNumbers,
			@Param("specCodes") List<String> specCodes,@Param("destination") List<String> destination, @Param("trackingStatus") String trackingStatus,
			@Param("isShipped") Integer isShipped, @Param("processPoint") String processPoint,
			@Param("startAfOn") Integer startAfOn, @Param("endAfOn") Integer endAfOn,
			@Param("plantNameList") List<String> plantNameList, @Param("checkPlantNameList") Integer checkPlantNameList,
			@Param("productionFlag") Integer productionFlag, @Param("kdFlag") Integer kdFlag,
			@Param("specCodeFlag") Integer specCodeFlag,@Param("destinationFlag") Integer destinationFlag);

	@Query(value = FIND_PRODUCT_DRILLDOWN_COUNT)
	@Cacheable(value = "findProductDrilldownCount", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null || #result.isEmpty()")
	public List<ChartDto> findProductDrilldownCount(@Param("productId") String productId,
			@Param("productionLots") List<String> productionLots, @Param("kdLotNumbers") List<String> kdLotNumbers,
			@Param("specCodes") List<String> specCodes,@Param("destination") List<String>  destination, @Param("trackingStatus") String trackingStatus,
			@Param("isShipped") Integer isShipped, @Param("processPoint") String processPoint,
			@Param("startAfOn") Integer startAfOn, @Param("endAfOn") Integer endAfOn,
			@Param("plantNameList") List<String> plantNameList, @Param("checkPlantNameList") Integer checkPlantNameList,
			@Param("productionFlag") Integer productionFlag, @Param("kdFlag") Integer kdFlag,
			@Param("specCodeFlag") Integer specCodeFlag,@Param("destinationFlag") Integer destinationFlag);

	@Query(value = FIND_PRODUCT_DETAILS_BY_DEFECT_STATUS)
	public Page<ProductDetails> findProductDetailsByDefectStatus(@Param("productId") List<String> productId,
			@Param("seqNo") List<Integer> seqNo, @Param("productionLots") List<String> productionLots,
			@Param("kdLotNumbers") List<String> kdLotNumbers, @Param("specCodes") List<String> specCodes, @Param("destination") List<String> destination,
			@Param("trackingStatuss") List<String> trackingStatuss, @Param("isShipped") Integer isShipped,
			@Param("processPoint") String processPoint, @Param("startAfOn") Integer startAfOn,
			@Param("endAfOn") Integer endAfOn, @Param("qicsStatus") String qicsStatus,
			@Param("plantNameList") List<String> plantNameList, @Param("checkPlantNameList") Integer checkPlantNameList,
			@Param("engineIds") List<String> engineIds, @Param("missionId") List<String> missionId,
			@Param("productIdFlag") Integer productIdFlag, @Param("seqNoFlag") Integer seqNoFlag,
			@Param("productionFlag") Integer productionFlag, @Param("kdFlag") Integer kdFlag, 
			@Param("specCodeFlag") Integer specCodeFlag,@Param("destinationFlag") Integer destinationFlag, @Param("trackingStatusFlag") Integer trackingStatusFlag,
			@Param("engineIdFlag") Integer engineIdFlag, @Param("missionIdFlag") Integer missionIdFlag,
			@Param("searchBy") String searchBy, Pageable pageable);

	@Query(value = FIND_PRODUCT_DETAILS_BY_DEFECT_STATUS_COUNT)
	@Cacheable(value = "findProductDetailsByDefectStatusCount", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null || #result.isEmpty()")
	public List<ChartDto> findProductDetailsByDefectStatusCount(@Param("productId") String productId,
			@Param("productionLots") List<String> productionLots, @Param("kdLotNumbers") List<String> kdLotNumbers,
			@Param("specCodes") List<String>  specCodes, @Param("destination") List<String> destination,@Param("trackingStatus") String trackingStatus,
			@Param("isShipped") Integer isShipped, @Param("processPoint") String processPoint,
			@Param("startAfOn") Integer startAfOn, @Param("endAfOn") Integer endAfOn,
			@Param("qicsStatus") String qicsStatus, @Param("plantNameList") List<String> plantNameList,
			@Param("checkPlantNameList") Integer checkPlantNameList, @Param("productionFlag") Integer productionFlag,
			@Param("kdFlag") Integer kdFlag, @Param("specCodeFlag") Integer specCodeFlag,@Param("destinationFlag") Integer destinationFlag);

	@Query(value = FIND_PRODUCT_DRILLDOWN_BY_DEFECT_STATUS_COUNT)
	@Cacheable(value = "findProductDrilldownByDefectStatusCount", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null || #result.isEmpty()")
	public List<ChartDto> findProductDrilldownByDefectStatusCount(@Param("productId") String productId,
			@Param("productionLots") List<String> productionLots, @Param("kdLotNumbers") List<String> kdLotNumbers,
			@Param("specCodes") List<String> specCodes,@Param("destination") List<String> destination, @Param("trackingStatus") String trackingStatus,
			@Param("isShipped") Integer isShipped, @Param("processPoint") String processPoint,
			@Param("startAfOn") Integer startAfOn, @Param("endAfOn") Integer endAfOn,
			@Param("qicsStatus") String qicsStatus, @Param("plantNameList") List<String> plantNameList,
			@Param("checkPlantNameList") Integer checkPlantNameList, @Param("productionFlag") Integer productionFlag,
			@Param("kdFlag") Integer kdFlag, @Param("specCodeFlag") Integer specCodeFlag,@Param("destinationFlag") Integer destinationFlag);

	@Query(value = FIND_ALL_KD_LOT)
	@Cacheable(value = "findAllKdLot", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null || #result.isEmpty()")
	public List<String> findAllKdLot(@Param("isShipped") Integer isShipped,
			@Param("plantNameList") List<String> plantNameList,
			@Param("checkPlantNameList") Integer checkPlantNameList);

	@Query(value = FIND_ALL_PRODUCTION_LOT)
	@Cacheable(value = "findAllProductionLot", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null || #result.isEmpty()")
	public List<String> findAllProductionLot(@Param("kdLotNumber") String kdLotNumber,
			@Param("isShipped") Integer isShipped, @Param("plantNameList") List<String> plantNameList,
			@Param("checkPlantNameList") Integer checkPlantNameList);

	@Query(value = FIND_ALL_MODEL)
	@Cacheable(value = "findAllModel", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null || #result.isEmpty()")
	public List<String> findAllModel(@Param("kdLotNumber") String kdLotNumber,
			@Param("productionLot") String productionLot, @Param("isShipped") Integer isShipped,
			@Param("plantNameList") List<String> plantNameList,
			@Param("checkPlantNameList") Integer checkPlantNameList);

	@Query(value = FIND_ALL_TYPE)
	@Cacheable(value = "findAllType", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null || #result.isEmpty()")
	public List<String> findAllType(@Param("productionLot") String productionLot,
			@Param("kdLotNumber") String kdLotNumber, @Param("model") String model,
			@Param("isShipped") Integer isShipped, @Param("plantNameList") List<String> plantNameList,
			@Param("checkPlantNameList") Integer checkPlantNameList);

	@Query(value = FIND_ALL_OPTION)
	@Cacheable(value = "findAllOption", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null || #result.isEmpty()")
	public List<String> findAllOption(@Param("productionLot") String productionLot,
			@Param("kdLotNumber") String kdLotNumber, @Param("model") String model, @Param("type") String type,
			@Param("isShipped") Integer isShipped, @Param("plantNameList") List<String> plantNameList,
			@Param("checkPlantNameList") Integer checkPlantNameList);

	@Query(value = FIND_ALL_EXTERIOR_COLOR)
	@Cacheable(value = "findAllExteriorColor", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null || #result.isEmpty()")
	public List<String> findAllExteriorColor(@Param("productionLot") String productionLot,
			@Param("kdLotNumber") String kdLotNumber, @Param("model") String model, @Param("type") String type,
			@Param("option") String option, @Param("isShipped") Integer isShipped,
			@Param("plantNameList") List<String> plantNameList,
			@Param("checkPlantNameList") Integer checkPlantNameList);

	@Query(value = FIND_ALL_INTERIOR_COLOR)
	@Cacheable(value = "findAllInteriorColor", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null || #result.isEmpty()")
	public List<String> findAllInteriorColor(@Param("productionLot") String productionLot,
			@Param("kdLotNumber") String kdLotNumber, @Param("model") String model, @Param("type") String type,
			@Param("option") String option, @Param("exteriorColor") String exteriorColor,
			@Param("isShipped") Integer isShipped, @Param("plantNameList") List<String> plantNameList,
			@Param("checkPlantNameList") Integer checkPlantNameList);

	@Query(value = FIND_ALL_DESTINATION)
	@Cacheable(value = "findAllDestination", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null || #result.isEmpty()")
	public List<String> findAllDestination(@Param("productionLot") String productionLot,
			@Param("kdLotNumber") String kdLotNumber, @Param("model") String model, @Param("type") String type,
			@Param("option") String option, @Param("exteriorColor") String exteriorColor,
			@Param("interiorColor") String interiorColor, @Param("isShipped") Integer isShipped,
			@Param("plantNameList") List<String> plantNameList,
			@Param("checkPlantNameList") Integer checkPlantNameList);

	@Query(value = FIND_ALL_TRACKING_STATUS)
	@Cacheable(value = "findAllTrackingStatus", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null || #result.isEmpty()")
	public List<String> findAllTrackingStatus(@Param("plantNameList") List<String> plantNameList,
			@Param("checkPlantNameList") Integer checkPlantNameList);

	@Query(value = FIND_ALL_PROCESS_POINT)
	@Cacheable(value = "findAllProcessPoint", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null || #result.isEmpty()")
	public List<String> findAllProcessPoint(@Param("lineName") String lineName,
			@Param("plantNameList") List<String> plantNameList,
			@Param("checkPlantNameList") Integer checkPlantNameList);

	@Query(value = FIND_ALL_DIVISION_NAME)
	@Cacheable(value = "findAllDivisionName", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null || #result.isEmpty()")
	public List<String> findAllDivisionName(@Param("plantNameList") List<String> plantNameList,
			@Param("checkPlantNameList") Integer checkPlantNameList);

	@Query(value = FIND_PRODUCT_DETAILS_BY_PRODUCT_ID_LIST)
	public Page<ProductDetails> findProductDetailsByProductIdList(@Param("productIdList") List<String> productIdList,
			@Param("plantNameList") List<String> plantNameList, @Param("checkPlantNameList") Integer checkPlantNameList,
			@Param("searchBy") String searchBy, Pageable page);

	@Query(value = FIND_PRODUCT_DETAILS_BY_TRACKING_STATUS_LIST)
	public Page<ProductDetails> findProductDetailsByTrackingStatusList(
			@Param("trackingStatusList") List<String> trackingStatusList,
			@Param("plantNameList") List<String> plantNameList, @Param("checkPlantNameList") Integer checkPlantNameList,
			@Param("searchBy") String searchBy, Pageable page);

	@Query(value = FIND_PRODUCT_DETAILS_BY_ENGINE_NO_LIST)
	public Page<ProductDetails> findProductDetailsByEngineNoList(@Param("engineNoList") List<String> engineNoList,
			@Param("plantNameList") List<String> plantNameList, @Param("checkPlantNameList") Integer checkPlantNameList,
			@Param("searchBy") String searchBy, Pageable page);
	

	@Query(value = FIND_PRODUCT_DETAILS_BY_MISSION_NO_LIST)
	public Page<ProductDetails> findProductDetailsByMissionNoList(@Param("missionNoList") List<String> missionNoList,
			@Param("plantNameList") List<String> plantNameList, @Param("checkPlantNameList") Integer checkPlantNameList,
			@Param("searchBy") String searchBy, Pageable page);

	/*
	 * @Query(value = FIND_PRODUCT_DETAILS_BY) public Page<ProductDetails>
	 * findProductDetailsTestBy(@Param("productId") String productId,
	 * 
	 * @Param("productionLot") String productionLot, @Param("kdLotNumber") String
	 * kdLotNumber,
	 * 
	 * @Param("model") String model, @Param("type") String type, @Param("option")
	 * String option,
	 * 
	 * @Param("exteriorColor") String exteriorColor, @Param("interiorColor") String
	 * interiorColor,
	 * 
	 * @Param("trackingStatus") String trackingStatus, @Param("processPoint") String
	 * processPoint,
	 * 
	 * @Param("startAfOn") Integer startAfOn, @Param("endAfOn") Integer endAfOn,
	 * Pageable pageable);
	 */

	/*
	 * @Query(value = FIND_PRODUCT_DETAILS_BY_DEFECT_STATUS) public
	 * Page<ProductDetails> findProductDetailsByTestDefectStatus(@Param("productId")
	 * String productId,
	 * 
	 * @Param("productionLot") String productionLot, @Param("kdLotNumber") String
	 * kdLotNumber,
	 * 
	 * @Param("model") String model, @Param("type") String type, @Param("option")
	 * String option,
	 * 
	 * @Param("exteriorColor") String exteriorColor, @Param("interiorColor") String
	 * interiorColor,
	 * 
	 * @Param("trackingStatus") String trackingStatus, @Param("processPoint") String
	 * processPoint,
	 * 
	 * @Param("startAfOn") Integer startAfOn, @Param("endAfOn") Integer endAfOn,
	 * 
	 * @Param("qicsStatus") String qicsStatus, Pageable pageable);
	 */

}
