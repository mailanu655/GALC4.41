package com.honda.galc.dao.product;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.honda.galc.data.ProductType;
import com.honda.galc.dto.ProductHistoryDto;
import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>ProductHistoryDao Class description</h3>
 * <p> ProductHistoryDao description </p>
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
 * Jul 20, 2011
 *
 *
 */
public interface ProductHistoryDao<E extends ProductHistory,K> extends IDaoService<E, K>{

    
	public List<E> findAllByProductId(String productId);
	
	
	/**
     * find all history result in the descending order of actual timestamp
     * @param productId
     * @param processPointId
     * @return
     */
    public List<E> findAllByProductAndProcessPoint(String productId,String processPointId);
    
    public List<E> findAllByProductIdAndSpecCodeAndProcessPoint(String productId,String specCode, String processPointId);
    
    public List<E> findAllByProcessPoint(String processPointId, Timestamp startTime, Timestamp endTime);
    
	public List<E> findAllByProductionDateAndProcessPoint(Date productionDate, String processPointId);
    
    public List<E> findAllByProcessPointAndModel(String processPointId, Timestamp startTime, Timestamp endTime , String modelCode, ProductType productType);
    
	public List<E> findAllByProcessPointAndTime(String processPointId, Timestamp startTime, Timestamp endTime);
	
	public List<E> findAllByProcessPointTimeAndDeviceId(String processPointId, String deviceId, Timestamp startTime, Timestamp endTime);
    
    public boolean hasProductHistory(String productId,String processPointId);
    
    /**
     * check if the product has been processed at processPointId after the time startTimestamp
     * @param productId
     * @param processPointId
     * @param startTimestamp
     * @return
     */
    public boolean isProductProcessed(String productId, String processPointId, String startTimestamp);
    
    public boolean isProductProcessedOnOrAfter(String productId, String processPointId, Timestamp timestamp);

    public int updateActualTimestamp(Date actualTimestamp, K id);
    
    public int updateActualTimestamp(Date newActualTimestamp, String productId, String processPointId, Timestamp oldActualTimestamp);
    
	public boolean isMostRecent(String productId, Date actualTimestamp);
	
	public String getLatestProductId(String ProcessPoint);

	public List<QiDefectResultDto> findAllDefectHistoryByStationAndStartTime(String processPointId, Timestamp startTimestamp, int maxRecords, boolean repairStation);
	
	public List<QiDefectResultDto> findAllDefectHistoryByMultiStationsAndStartTime(String processPointIds, Timestamp startTimestamp, int maxRecords, boolean repairStation);

	public List<ProductHistoryDto> findAllByProcessPoint(String processPointId, int rowNumber);
	
    public ProductHistory findMostRecentByProductAndProcessPointId(String productId, String processPointId);
    
    public List<E> findAllByProcessPointIdsAndProductIds(List<String> processPoints,List<String> productIds);
}
