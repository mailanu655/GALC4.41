package com.honda.galc.dao.product;


import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.dao.oif.EntitySequenceInterface;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.dto.FrameSpecDto;
import com.honda.galc.dto.LotSequenceDto;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.service.IDaoService;

/**
 *
 * <h3>PreProductionLotDao Class description</h3>
 * <p> PreProductionLotDao description </p>
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
 * @author Jeffray Huang<br>
 * Nov 5, 2010
 *
 *
 */
/**
 *
 * @author Gangadhararao Gadde
 * @date apr 6, 2016
 */
public interface PreProductionLotDao extends IDaoService<PreProductionLot, String>, EntitySequenceInterface {


	/**
	 * find All pre-production lots in order (its send status = 0)
	 * in plant 1, there are either frame lots or knuckle lots, use "KN" for process location to find knuckle lots
	 * @param processLocation
	 * @return
	 */
	public List<PreProductionLot> findAllPreProductionLotsByProcessLocation(String processLocation);


	/**
	 * Find all pre-production lots by the given process location.
	 */
	public List<PreProductionLot> findAllByProcessLocation(String processLocation);

	public List<PreProductionLot> findAllSortedLotsByProcessLocation(String processLocation, boolean isAscending) ;

	/**
	 * Find all pre-production lots by the given process location sorted by the linked list & hold status.
	 */
	public List<PreProductionLot> findAllForProcessLocation(String processLocation);

	/**
	 * Find all pre-production lots by the given product spec code.
	 */
	public List<PreProductionLot> findAllPreProductionLotsByProductSpecCode(String productSpecCode);

	/**
	 * find first knuckle pre production lot whose send status is 2
	 * @return
	 */
	public PreProductionLot findFirstForKnuckleShipping();

	/*
	 * find next pre production lot of current lot with "productionLot as production lot
	 */
	public PreProductionLot findNext(String productionLot);

	/**
	 * find last pre production lot at a process location ( not body on yet)
	 * @param processLocation
	 * @return
	 */
	public PreProductionLot findLastPreProductionLotByProcessLocation(String processLocation);

	/**
	 * find last production lot of the schedule link at a process location
	 * @param processLocation
	 * @return
	 */
	public PreProductionLot findLastLot(String processLocation);


	/**
	 * find all pre-production lots in the same kd lots which have just started to be processed
	 * @param processLocation
	 * @return
	 */
	public List<PreProductionLot> findAllWithSameKdLotCurrentlyProcessed(String lastProductionLot);

	/**
	 * find all pre-production lots which has the same kd lot number ( ignoring last digit of kd lot number)
	 * @param kd lot
	 */
	public List<PreProductionLot> findAllWithSameKdLot(String kdLot);

	/**
	 * find all pre-production lots which has the same kd lot number ( ignoring last digit of kd lot number)
	 * The kd lot number might be reused, so the production lots in the same kd lot but not in the group of current
	 * production lot should be excluded from the list
	 * @param production lot
	 * @return sorted list based on the pre production lot sequence
	 */
	public List<PreProductionLot> findAllWithSameKdLotFromProductionLot(String productionLot);
	
	public List<PreProductionLot> findAllByProdDateAndLine(String processLocation, Date productionDate, String lineNo);

	public PreProductionLot findParent(String productionLot);

	public PreProductionLot findParentBySequence(String productionLot);

	/**
	 * update pre production lots' next production lot
	 * @param changedPreProductionLots
	 */
	public void updateAllNextProductionLots(List<PreProductionLot> changedPreProductionLots);

	/**
	 *
	 * @param productionLot
	 * @return
	 */
	public PreProductionLot findCurrentPreProductionLot(String processLocation);


	/**
	 * Find all preproduction lot for with small lots for the same KD lot
	 * @param nextProductionLot
	 * @return
	 */
	public List<PreProductionLot> getPreProductionLotsForSameKdLot(String productionLotId);


	public List<PreProductionLot> getUnSentLots(String processLocation);

	/**
	 * find all the pre-production lots which has not been processed for recipe and on process
	 * @param processLocation
	 * @param msProcessPointId
	 * @return
	 */
	public List<PreProductionLot> findAllNonProcessedLots(String processLocation,String msProcessPointId);

	/**
	 * updates stamped count to the value provided for the production lot
	 *
	 * @param productionLot
	 * @param stampedCount
	 */
	public void updateStampedCount(String productionLot, int stampedCount);

	/**
	 * updates stamped count based on the number of products that are
	 * in stamped status (send_status = 2)
	 *
	 * @param productionLot
	 */
	public void updateStampedCount(String productionLot);


	public void updateSendStatus(String productionLot, int status);

	public void updateSentTimestamp(String productionLot);
	/**
	 * updates StartProductId to the value provided for the production lot
	 *
	 * @param productionLot
	 * @param StartProductId
	 */
	public void updateStartProductId(String productionLot, String StartProductId);

	/**
	 * find all preproduction lots whose ksns do not match with part number build attributes
	 * @return
	 */
	public List<PreProductionLot> findAllWithIncorrectKsns();

	/**
	 * recreate ksns and update startProductId fields of PreProductLot and Production lot
	 * @param subProduct
	 * @param partNumber
	 */
	public void recreateKnuckleSerialNumbers(SubProduct subProduct, String partNumber);

	public int delete(String prodLotNumber);

	public String findNextWeldOnProductId(int sendStatus);

	/**
	 * Behaves as findNextWeldOnProductId(int sendStatus), but uses component<br>
	 * instead of VIN_STAMP_DASH to find the excluded models.<br>
	 * The component must not be null/empty.
	 */
	public String findNextWeldOnProductId(int sendStatus, String component);

	public Object[] findNextWeldOnProductId(String currentProductId, int sendStatus);

	/**
	 * Behaves as findNextWeldOnProductId(String currentProductId, int sendStatus),<br>
	 * but uses component instead of VIN_STAMP_DASH to find the excluded models.<br>
	 * The component must not be null/empty.
	 */
	public Object[] findNextWeldOnProductId(String currentProductId, int sendStatus, String component);

	public void appendPreProductionLot(PreProductionLot previousLot,PreProductionLot currentLot);

	/**
	 * get upcoming pre-production lot list in the production order
	 * @param count - number of production lots
	 * @return
	 */
	public List<PreProductionLot> findUpcomingPreProductionLots(int count);

	/**
	 * Move a list of Preproduction lots to new process location
	 * @param startProductionLot - start Pre Production Lot of the old list
	 * @param processLocation - New Process Location
	 */
	public void movePreProductionLots(String startProductionLot,String processLocation);

	public Date findLastUpdateTimestamp(String processLocation);

	public List<Object[]> findDistinctPlantCodes();

	public List<Object[]> findDistinctLines(String plantCode,String processLocation);

	public List<PreProductionLot> findLotsWithNullNextProdLot(String plantCode,String lineNo,String processLocation);

	public List<PreProductionLot> findLotByNextProductionLot(String nextProductionLot);

	public List<PreProductionLot> findLotsOnHold(String plantCode,String lineNo,String processLocation);

	public List<PreProductionLot> findAllSentLots(String processLocation);

	public String findLastStampedLot(String processLocation);

	public String findSplitLot(String productionLot);

	public void updateNextLotAndHoldStatus(String nextProductionLot, int holdStatus, String productionLot);

	public List<HashMap<String, Object>> getNextProductionSchedule(DefaultDataContainer dc);

	public PreProductionLot getCurrentPreProductionLotByLotNumber(String lotNumber, String processLocation);
	public PreProductionLot getCurrentPreProductionLotByStartProductId(String startProductId);

	public PreProductionLot getLastLot(String processLocation, String planCode6);

	public List<PreProductionLot> getTailsByPlantLineLocation(String plantCode, String lineNo, String processLocation);

	public int countByPlantLineLocation(String plantCode, String lineNo, String processLocation);

	public PreProductionLot findByStartProductId(String startProductId);

	public PreProductionLot findCurrentProductLotAtBlockLoad();

	public PreProductionLot findFirstAvailableLot(String processLocation);


	public Double findMaxSequence(String planCode);

	public Double findMaxSequenceWhereNotSendStatus(String planCode, int sendStatus);

	public List<PreProductionLot> findAllByPlanCode(String planCode);

	public Date findLastUpdateTimestampByPlanCode(String planCode);

	public PreProductionLot findCurrentPreProductionLotByPlanCode(String planCode);

	public List<String> getAllPlanCodes();

	public List<Object> getTmProductionReport(final String lastDate, final String processPointDcMc, final String processPointDcTc,
			final String processPointMcMc, final String processPointMcTc, final String processPointMcP, final String plantCode);

	public List<PreProductionLot> findReplicateSourceByFilters(String sourcePlanCode, Map<String, String> filters);
	public List<PreProductionLot> findReplicateSourceOnHoldByFilters(String sourcePlanCode, Map<String, String> filters);
	public List<PreProductionLot> findReplicateNotHoldSourceByFilters(String sourcePlanCode, Map<String, String> filters, String notHoldDemandTypes);
	public List<PreProductionLot> findReplicateHoldSourceByFilters(String sourcePlanCode, Map<String, String> filters, String notHoldDemandTypes, String lastProdLotValue);

	public PreProductionLot findLast(String planCode);

	public List<String> getUnmappedOrderIds();


	public List<ProductionLot> findAllNewEngineShippingLots();

	public List<PreProductionLot> findAllPreviousLots(String productionLot,int count);

	public List<PreProductionLot> findAllUpcomingLots(String processLocation);

	public List<PreProductionLot> findAllOnHoldLots(String processLocation);

	public PreProductionLot getNextPreProductionLot(String planCode, int orderStatusId);

	public PreProductionLot getNextByLastPreProductionLot(String productionLot, String planCode);

	public PreProductionLot findByPlanCodeAndProdSpecCode(String productionLot, String planCode, String prodSpecCode);

	public PreProductionLot findByProductionLotAndPlanCode(String productionLot, String planCode);


	public List<PreProductionLot> findAllByPlanCodeOrderBySeqNum(String planCode);


	public List<String> findDistinctSpecCodeByPlanCode(String planCode);
	public List<PreProductionLot> findAllBySendStatusAndPlanCode(int sendStatus, String planCode);
	public List<PreProductionLot> findAllWaitingByPlanCode(String planCode);

	public int changeToUnsent(String productionLot, String processLocation);
	
	public int changeToUnsentService(String processLocation, String planCode, String ppId);

	public PreProductionLot getNextByLastLotNumber(String lotNumber, String planCode);

	public PreProductionLot getByLotNumberAndPlanCode(String lotNumber, String planCode);

	public PreProductionLot findFirstUpcomingLotByPlanCode(String planCode);

	public List<PreProductionLot> findByKDLotAndPlanCode(String kdLotNumber, String planCode);

	public int deleteByPlanCodeSendStatus(String planCode, int sendStatus);
	public int deleteByPlanCodeSendStatusDemandType(String planCode, int sendStatus, String demandType);
	public void deleteByPlanCodeSendStatusNotHoldDemandType(String planCode, int sendStatus, String notHoldLotDemandTypes);

	public List<PreProductionLot> getTailsByPlanCode(String planCode);

	/**
	 * find last production lot of the schedule link for a Plan Code
	 * @param planCode
	 * @return
	 */
	public PreProductionLot findLastLotByPlanCode(String planCode);

	/**
	 * Returns true if there is at least one PreProductionLot which starts with the given lot prefix.
	 */
	public boolean isLotPrefixExist(String lotPrefix);

	/**
	 * Takes a production date in the format yyyyMMdd and<br>
	 * returns true iff it is the last production date.
	 */
	public boolean isLastProductionDate(String lotPrefix, String productionDate);

	public PreProductionLot findByProcessLocationKdLotNumberAndSpecCode(String processLocation, String kdLotNumber, String productSpecCode);

	public int countByPlanCode(String planCode);

	public int countByPlanCodeNextProductionLot(String planCode, String nextProductionLot);

	public String getNextLotNumberByProductionLot(String ProductionLot);


	/**
	 * find all the upcoming lot in with same plan code
	 * @param planCode
	 * @return
	 */
	public List<PreProductionLot> findAllUpcomingLotsByPlanCode(String planCode);

	/**
	 * find all upcoming lots with given product spec
	 * @param productSpecCode
	 * @return
	 */
	public List<PreProductionLot> findAllUpcomingLotsByProductSpecCode(String productSpecCode);


	/**
	 * find all processed lots in given plan code
	 * @param productionLot
	 * @param processedRowCount
	 * @return
	 */
	public List<PreProductionLot> findAllPreviousLotsByPlanCode(
			String planCode, int processedRowCount);


	/**
	 * find all lots on hold with the given plan code
	 * @param planCode
	 * @return
	 */
	public List<PreProductionLot> findAllOnHoldLotsByPlanCode(String planCode);

	public PreProductionLot findByNextProdLot(String nextProductionLot);

	/**
	 * find merged lots' last lotNumber,merged lotSize with the given spec code
	 * @param lotNumber,lotSize,SpecCode
	 * @return
	 */
	public PreProductionLot getMergedPreProductionLotBySpecCode(
			PreProductionLot productionLot,boolean isNextProductionLot);

	public PreProductionLot getMergedSubAssyPreProductionLotByBuildAttribute(PreProductionLot productionLot,String specCodeFromBuildAttr,boolean isNextProductionLot);

	public List<PreProductionLot> findCurrentDayLastServiceLots(String serviceLotPrefix, String processLocation);

	public List<String> findUpcomingLotsForProcessPointCsv(String productionLot,int count, String processPoint);

	public Date findLastUpdateTimestampByProcessLocation(String processLocation);

	public PreProductionLot findNextLotBySequence(PreProductionLot currentLot) ;

	public PreProductionLot findCurrentLotByPlanCodeOrderByLinkedList(String planCode);

	public Object[] getKdStampCountLotSize(String kdLot, String processLocation);

	/**
	 * find sorted list of lots for given plan code
	 * @param planCode
	 * @return
	 */
	public List<PreProductionLot> findAllByPlanCodeSort(String planCode);

	public List<PreProductionLot>findAllForProcessLocationAndPlanCode(String processLocation, String planCode);

	public List<PreProductionLot> findAllNonMassProductionLotsByPlanCodeAndCreateDate(String planCode, Timestamp createDate);
	
	public List<PreProductionLot> findAllMassProductionLotsByPlanCodeAndCreateDate(String planCode, Timestamp createDate);

	/**
	 * select distinct e.productSpecCode from PreProductionLot e where e.productSpecCode like :productSpecCode  and e.sendStatusId in (:sendStatus) 
	 * 
	 * @param specCode
	 * @param statusCodes
	 * @return
	 */
	public List<String> findDistinctProdSpecCodeBySpecCodeAndStatus(List<String> specCodeList, Integer sendStatusCode);
	public List<PreProductionLot> getProductionLots(String productId);
	public void updatePreProdLotDetails(String productCodeForUpdate,FrameSpecDto selectedFrameSpecDto, List<String> productionLots);
	
	/*
	 * Check if schedule exists for spec code
	 */
	public boolean findByProdSpecCode(String productSpecCode);

	public List<String> getNonShippedProductionLotsByPlanCode(String planeCode);
	
	public boolean isModelInSchedule(String modelPrefix);

	public boolean isLotStamped(String lotPrefix, String productionDate);
	
	public boolean isPassedProductionLot(String processLocation, String planCode, String startProdLot, String currentProdLot);
	
	public boolean isPassedProductionLot(String processPointId, String startProdLot);
	
	public Map<String, List<String>> getProductionProgress(Integer prodProgressType, List<String> processPointOn, List<String> processPointOff, String div, List<String> lines, Boolean allowDBUpdate, Boolean useSequenceForBuildSequence, Integer sequenceNumberScale, Boolean excludeListedPlanCodes, List<String> planCodesToExclude,List<String> exceptionProcessPoints,Timestamp runTimestamp);

    public List<PreProductionLot> findIncompleteChildLots(String planCode, String processLocation, String sendStatus, String planCodeSec, String processLocationSec);
	
    public List<PreProductionLot> findReplicatedLot(String planCode, String processLocation, Timestamp lookBackDate);
    
    public List<PreProductionLot> findBylotnumberAndProdSpecCodeAndDestProcLoc(String lotNumber, String prodSpecCode, String destProcLoc);
    
    public List<PreProductionLot> findByProcessLocationAndLotNumber(String processLocation, String lotNumber);
    
    public List<PreProductionLot> findParentLotOrder(String srcPlanCode, String srcProcLoc, String sourcePlanCode, String sourceProcLoc);
    
    public List<PreProductionLot> findDuplicateSchedule(String destPlanCode, String destProcLoc);
    
	public List<PreProductionLot> findBylotnumber(String lotNumber, String parentProdLot);
	
	public List<LotSequenceDto> getUpcomingProductLotSequence(String planCode, String processPointId);
	
	public List<LotSequenceDto> getUpcomingProductLotSequence(String planCode, String processPointId, String productType);

	public void removeReplicateLots(String processLocation, String productionLot);
	
	List<Object[]> fetchLotInfoByProductId(String productId);
}
