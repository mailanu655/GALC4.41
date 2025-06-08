package com.honda.galc.dao.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.honda.galc.dto.InProcessProductMaintenanceDTO;
import com.honda.galc.dto.LineDetail;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.service.IDaoService;

/**
 * * *
 * 
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public interface InProcessProductDao extends IDaoService<InProcessProduct, String> {

	public InProcessProduct findLastForLine(String lineId);

	public InProcessProduct findLastForLine1(String lineId);

	public List<InProcessProduct> findByNextProductId(String productId);

	public boolean isProductIdAheadOfExpectedProductId(
			String expectedProductId, String productId);

	public List<InProcessProduct> findSequenceListByDivision(String divisionId);

	public int deleteProdIds(List<String> prodIds);

	public int deleteProdId(String prodId);

	public List<String> getVinSequence(String startingVin, int howMany);

	public String findFirstUnconfirmed(String lineId, String testPP, String confirmPP);

	public List<InProcessProduct> findItemsToResequence(String productId);

	public String getProductSpecCode(String productId);

	public ArrayList<LineDetail> getLineDetail(String lineId);

	public void addToLine(BaseProduct product, ProcessPoint processPoint);

	public List<Object[]> findFirstFiveProducts(String sequenceNumber,String processPointId, String plantName);

	public List<Map<String, String>> getVinSequenceVIOS(String startingVin,int howMany, String prodType);

	public List<Map<String, String>> getLotSequenceVIOS(String startingVin,String productType);
	
	public InProcessProduct findForUpdate(String productId);
	
	public void productFactoryExit(BaseProduct product, ProcessPoint processPoint);
	
	/**
	 * Gets the inventory for line.
	 *
	 * @param plantName the plant name
	 * @param divisionId the division id
	 * @param lineId the line id
	 * @return the inventory for line.
	 */
	public List<InProcessProduct> getInventoryForLine(String plantName, String divisionId, String lineId);
	
	public InProcessProduct findNextProductSeqByPlantName(String processPointId,String plantName);
	
	/**
	 * Returns a list of the product ids with<br>
	 * their associated ACTUAL_TIMESTAMPs (from GAL215TBX)<br>
	 * and PRODUCT_SPEC_CODEs (from GAL215TBX)<br>
	 * currently at the given process point ids.
	 */
	public List<Object[]> getInventoryForProcessPointIds(List<String> processPointIds);
	
	public List<Object[]> getCureTimerInventory(List<String> triggerLineIds, List<String> triggerOnParts, List<String> triggerOffProcessPointIds);
	
	//Return a linked sequence list of InprocessProcess associated with with a Line ID
	public List<InProcessProductMaintenanceDTO> findSeqListByDivIdLineId(String divisionId, String lineId,boolean isFrame);
	
	public int updateLastPassingProcessPoint(String productId, String lastPassingProcessPointId);

	/**
	 * Find the in coming product list from in-process product 
	 * @param productId
	 * @return
	 */
	public List<String> findIncomingExpectedProductIds(String productId);
	
	/*
	 * find head of product list for a line 
	 * if link list is broken , this will return multiple products
	 */
	public List<InProcessProduct> findHeadProducts(String lineId);
}
