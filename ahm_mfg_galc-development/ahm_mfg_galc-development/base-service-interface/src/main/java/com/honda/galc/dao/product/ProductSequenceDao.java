package com.honda.galc.dao.product;

import java.sql.Timestamp;
import java.util.List;

import com.honda.galc.dto.ProcessProductDto;
import com.honda.galc.entity.product.ProductSequence;
import com.honda.galc.entity.product.ProductSequenceId;
import com.honda.galc.service.IDaoService;

public interface ProductSequenceDao extends IDaoService<ProductSequence, ProductSequenceId>{

	public ProductSequence findNextExpectedProductId(ProductSequence productSequence);
	public List<String> findIncomingExpectedProductIds(ProductSequence productSequence);
	public ProductSequence findPrevProductId(ProductSequence productSequence);

	public ProductSequence findFirstExpectedProductId(String processPointId, String InSequenceProcessPointId);
	public ProductSequence findByProductId(String productId);

	public List<ProductSequence> findAll(String InSequenceProcessPointId);
	/** delete all measurement data matching array of product ids
	 * 
	 * @param prodIds
	 */
	public int deleteProdIds(List <String> prodIds);

	/**
	 * List all process point ids in the table
	 * @return
	 */
	public List<String> getAllProcessPointIds();

	/**
	 * Maintenance function to remove product sequence from database
	 * @param sequence
	 * @return
	 */
	public List<ProductSequence> removeProductSequence(ProductSequence sequence);

	/**
	 * Returns all records having the supplied process point id and sequence number.
	 */
	public List<ProductSequence> findAllByProcessPointIdAndSequenceNumber(String processPointId, Integer sequenceNumber);

	public List<ProcessProductDto> findAllUpcoming(String processPointId,Integer sequenceNumber, int rowLimit);
	public int deleteAllBeforeTimestamp(Timestamp refTs, String stationId);

	public List<ProcessProductDto> findAllAlreadyProcessed(String processPointId,int rowLimit);
	public List<ProductSequence> findAllForStationIdBeforeTimestamp(Timestamp refTs, String stationId);
}
