package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.dto.ProcessProductDto;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.ProductCarrier;
import com.honda.galc.entity.product.ProductCarrierId;
import com.honda.galc.service.IDaoService;

public interface ProductCarrierDao extends IDaoService<ProductCarrier, ProductCarrierId>{	
	
	public List<ProductCarrier> findAll(String productId, String carrierId);
	public List findRackAssociatedEnginesInGivenStatus(String status, String[] validProcessPoints);
	public List<ProductCarrier> findEnginesInCarrier(String carrierId);
	public ProductCarrier findfirstProduct(String productId);
	public List<ProcessProductDto> findAllProcessedProductsForProcessPoint(String ppId, int rowLimit);
	public List<Frame> findByAfOnSeqNumber(String carrierId);
	public List<Frame> findByAfOnSeqRangeLineId(String startSeq, String endSeq, String lineID);
	public List<ProductCarrier> getProductCarriersByProcessPointAndDateRange(String ppId, String startTime, String endTime);
	/**
	 * Based on the AF On Sequence and Process Point in the Product Carrier TBX this method will check to see if the product ID contains EMPTY.  
	 * If if contains EMPTY this method will return a true value, otherwise it will return false
	 * @param productId
	 * @param seq
	 * @param processPointId
	 * @return
	 */
	public boolean checkForEmptyCarrierByAfOnSeqAndProcessPointId(String seq,String processPointId);
	
	/**
	 * This method will return a single Product Carrier TBX entity for the provided AF On Sequence and Process Point.  
	 * The query used by this method will order by the latest On_Timestamp
	 * @param seq
	 * @param processPointId
	 * @return
	 */
	public ProductCarrier findByAfOnSeqAndProcessPointId(String seq, String processPointId);
	/**
	 * This method will return a list of Product Carrier TBX entities for the provided AF On Sequence Range and Process Point.  
	 * The query used by this method will get the latest On_Timestamp for each sequence in the range. 
	 * @param startSeq
	 * @param endSeq
	 * @param processPointId
	 * @return
	 */
	public  List<ProductCarrier> findAllByAfOnSeqRangeAndProcessPointId(String startSeq,String endSeq,String processPointId);
	
	public ProductCarrier findByCarrierId(String string);
	
	public ProductCarrier findByProductId(String productId);
	
	public List<ProductCarrier> findAllByProductId(String productId);
	
	public ProductCarrier findProductCarrierByProductIdandProcessPointId(String productId, String processPointId);
	
	public void removeProductCarrierByProductIdandProcessPointId(String productId, String processPointId);
}
