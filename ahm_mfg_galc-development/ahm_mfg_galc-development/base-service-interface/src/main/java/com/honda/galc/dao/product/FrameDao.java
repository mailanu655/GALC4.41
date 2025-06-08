package com.honda.galc.dao.product;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.honda.galc.data.DataContainer;
import com.honda.galc.dto.FrameSpecDto;
import com.honda.galc.dto.ProcessProductDto;
import com.honda.galc.entity.product.Frame;


/** * *
* @version 0.2
* @author Gangadhararao Gadde
* @since Aug 09, 2012
*/
public interface FrameDao extends ProductDao<Frame>{
	public List<Frame> findAllByShortVin(String shortVin);

	/**
	 * Returns a page of products for the given short VIN.
	 * @param shortVin - short VIN to search for results
	 * @param pageNumber - page number, indexed at 0
	 * @param pageSize - number of results for the page
	 * @return
	 */
	public List<Frame> findPageByShortVin(String shortVin, int pageNumber, int pageSize);

	public List<Frame> findAllByProductSequence(String processPointId,String currentProductId,int processedSize,int upcomingSize);

	public List<Frame> findAllByInProcessProduct(String currentProductId,int processedSize,int upcomingSize);

	public void updateProductStartDate(String processPointId, String productId);

	public List<Frame> findAllByLastPassingProcessPointId(String lastPassingProcessPointId);

	public List<Frame> findByAfOnSequenceNumber(int afOn);

	/**
	 * Returns a page of products for the given AF ON sequence number.
	 * @param afOn - AF ON sequence number to search for results
	 * @param pageNumber - page number, indexed at 0
	 * @param pageSize - number of results for the page
	 * @return
	 */
	public List<Frame> findPageByAfOnSequenceNumber(int afOn, int pageNumber, int pageSize);

	public List<Frame> findByAfOnSequenceNumber(int afOn, String afOnProcessPointId);

	public List<Frame> findBySeqRange(String min, String max);

	public List<Frame> findBySeqRange(String min, String max, String afOnProcessPointId);

	public List<Frame> findByProductionLot(String productionLot);

	public List<Frame> findByTrackingStatus(String trackingStatus);

	public List<Frame> findByPartName(String lineId, int prePrintQty, int maxPrintCycle, String ppid, String partName);

	public List<Frame> findByModelAndSeqNumber(String model,String sequenceNumber);

	public List<Frame> findByEin(String ein);

	public Integer maxAfOnSequenceNumber();

	public List<Object[]> getProductsWithinAfOnSeqRange(Integer startAfOnSeq,Integer stopAfOnSeq);

	public List<String> getProductsByEngMTOC(String engMTOC,int daysToCheck, String[] notSellableTrackingStatus);

	public Integer findAfOnSequenceNumberByShortSequence(int numberOfDigits, int shortSequence);

	public long countByShortVin(String shortVin);
	
	public long countByAfOnSequenceNumber(int startSequence, int endSequence);
	
	public List<Frame> findAllByAfOnSequenceNumber(int startSequence, int endSequence);

	/**
	 * Returns a page of products for the given AF ON sequence number range.
	 * @param startSequence - start of AF ON sequence number range to search for results
	 * @param endSequence - end of AF ON sequence number range to search for results
	 * @param pageNumber - page number, indexed at 0
	 * @param pageSize - number of results for the page
	 * @return
	 */
	public List<Frame> findPageByAfOnSequenceNumber(int startSequence, int endSequence, int pageNumber, int pageSize);

	public List<Frame> findAllByMissionSn(String msn);

	public List<Object[]> getFrameLineMtoData(String productionLot,String scrapLineId,String exceptionalLineId);

	public Integer getScrapedExceptionalCount(String productionLot, String scrapLineID, String exceptionalLineID);

	public List<Frame> findByAfOnSequenceNumberOrderByPlanOffDate(int AfOn,boolean planOffDateOrderAsc);

	public List<Object[]> getVinInVQ(String trackingStatus);

	public List<Map<String, Object>> findAllSalesWarrantyData(Timestamp startTimestamp, String[] selectingPps, String[] notSellableTrackingStatus, Map<String, String> processPointIds,Map<String, String> processLocations, String[] plantCodesToExclude);

	public String getSalesModelTypeCode(String productId);

	public Frame findByKDLotNumber(String kdLotNumber);

	public List<Object[]> getVQShipVinXmRadio(List<String> xmPartNames, String vqOffPP, String afOffPP, String lastUpdate, String countryCode);

	public List<Object[]> getVQShipVinXmRadioFromLet(String partName, String productId);

	public List<Object[]> getExOutVinXmRadio(List<String> xmPartNames, String vqOffPP, String afOffPP, String lastUpdate, String countryCode, String trackingStatus);
	
	public List<Object[]> getVIOSVQShipVinXmRadio(List<String> xmPartNames, String vqOffPP, String afOffPP, String lastUpdate, String countryCode);

	public List<Object[]> getVIOSExOutVinXmRadio(List<String> xmPartNames, String vqOffPP, String afOffPP, String lastUpdate, String countryCode, String trackingStatus);


	public List<Frame> findAllByAfOnSeqRangeLineId(int startSequence, int endSequence,String lineId);

	public int getScheduleQuantity(String productId, String planCode);

	public int getPassQuantity(String productId, String processPointId);

	public int getCutlot(String productId, String processPointId, String sequencingPPId);

	public int getRebuild(String productId, String planCode);

	public List<Integer> findLatestAsmSeqNoByProcessPoint(String processPointId);

	public List<Object[]> getVQShipVinXmRadioWithSubProduct(List<String> xmPartNames, String vqOffPP, String afOffPP, String lastUpdate, String countryCode, List<String> subProduct);

	public List<Object[]> getExOutVinXmRadioWithSubProduct(List<String> xmPartNames, String vqOffPP, String afOffPP, String lastUpdate, String countryCode, String trackingStatus, List<String> subProduct);

	public List<Object[]> getVQShipVinXmRadioWithSubSubProduct(List<String> xmPartNames, String vqOffPP, String afOffPP, String lastUpdate, String countryCode, List<String> subProduct, List<String> subSubProduct);

	public List<Object[]> getExOutVinXmRadioWithSubSubProduct(List<String> xmPartNames, String vqOffPP, String afOffPP, String lastUpdate, String countryCode, String trackingStatus, List<String> subProduct, List<String> subSubProduct);

	public List<Object[]> getVinXmRadioWithExtRequired(String xmPartNames, String vqOffPP, String afOffPP, String lastUpdate, String countryCode, List<String> framePrefix);

    public List<Object[]> getExtOutVinXmRadioWithExtRequired(String xmPartNames, String vqOffPP, String afOffPP, String lastUpdate, String countryCode, String trackingStatus, List<String> framePrefix);


	/**
	 * Takes a DataContainer with any of the following keys (at least one key marked with # must be included):<br>
	 * LINE_NO, #AF_ON_SEQUENCE_NUMBER, #PRODUCT_ID, #TRACKING_STATUS, PRODUCT_SPEC_CODE,<br>
	 * MODEL_YEAR_CODE, MODEL_CODE, MODEL_TYPE_CODE, EXT_COLOR_CODE, INT_COLOR_CODE,<br>
	 * #ENGINE_SERIAL_NO, ENGINE_MTO, #KD_LOT_NUMBER, #PRODUCTION_LOT, #PROCESS_POINT_ID, ACTUAL_TIMESTAMP<br>
	 * The values must be a range or a single value, i.e. "fromValue, toValue", "fromValue" or "toValue".<br>
	 * Returns a DataContainer containing the following keys with their corresponding values:<br>
	 * LINE_NO, AF_ON_SEQUENCE_NUMBER, PRODUCT_ID, VIN_SERIAL, TRAKING_STATUS, PRODUCT_SPEC_CODE,<br>
	 * ENGINE_SERIAL_NO,ENGINE_MTO, KD_LOT_NUMBER, PARKING_LOCATION, PROCESS_POINT_ID, ACTUAL_TIMESTAMP
	 */
	public List<Map<String,Object>> findByFieldRanges(DataContainer input);



	/**
	 * Find the Product by line reference number
	 * @param lineRef
	 * @param onProcessPointId
	 * @param offProcessPointId
	 * @param refLength
	 * @return
	 */
	public Frame findFrameByLineRefNumber(int lineRef, String onProcessPointId, int refLength);

	public List<ProcessProductDto> findAllProcessedProductsForProcessPoint(String ppId, int rowLimit);



	public String findFirstByInstalledPart(String partName, String partId);

	public List<Frame> findAllByProductCarrier(String processPointId, String currentProductId,int processedSize, int upcomingSize);

	List<Frame> getProductsWithinSerialRange(String startSeq, String stopSeq);

	public List<Frame> findAllByProductionLotRangeAndPlanCode(String startLot, String endLot,String planCode);

	public List<Frame> findByRanges(String startAfSeq, String endAfSeq, String startProdLot, String endProdLot, String startVinSeq, String endVinSeq, List<String> modelYearCode, List<String> modelCode,List<String> modelTypeCode, List<String> destCode, String planCode,List<String> trackingStatuses);

	public List<Frame> findProductsBySpecCode(List<String> productIds,List<String> modelYearCode, List<String> modelCode, List<String> modelTypeCode);

	public long count(String startAfSeq, String endAfSeq, String startProdLot, String endProdLot, String startVinSeq, String endVinSeq, List<String> modelYearCode, List<String> modelCode,List<String> modelTypeCode, List<String> destCode, String planCode,List<String> trackingStatuses);
	
	public Integer findNextSequencePlateNumber(String uniqueSpeCode);
	
	public List<Frame> getProductsBySpecCode(String productSpecCode,List<String> productionLots) ;
	public void updateFrameDetails(String productCodeForUpdate, FrameSpecDto selectedFrameSpecDto, List<String> productionLots);
	
	public Frame findByKDLotNumber(String kdLotNumber, List<String> scrapLines);

	List<Frame> findAllByPlantAfOnSequenceNumber(String plantName, int startSequence, int endSequence, int count);

	List<Frame> findAllByAfOnSequenceNumber(int startSequence, int endSequence, int count);

	List<Frame> findAllByProductionLot(String productionLot, int count);

	public List<Object[]> findVinsInKdLotSortByAfOnTimestamp(String kdLotNumber, String afOnProcessPointId, String productId);
	
	public List<Frame> findAllByKDLotNumber(String kdLotNumber);
	
	List<Object[]> fetchTrackingDetailsByModelDeptAndLoc(String modelCode, String departments, String requestingLocation);
	
	List<Object[]> fetchTrackingDetailsByModelDeptAndVin(String modelCode, String departments, String requestingLocation, String productId);
}