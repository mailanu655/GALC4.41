package com.honda.galc.dao.product;

import java.sql.Date;
import java.util.List;

import com.honda.galc.dao.oif.EntitySequenceInterface;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.SubProductShipping;
import com.honda.galc.entity.product.SubProductShippingId;
import com.honda.galc.service.IDaoService;


public interface SubProductShippingDao extends IDaoService<SubProductShipping, SubProductShippingId>, EntitySequenceInterface {

	/**
	 * find last sub production shipping production lot
	 * @param processLocation
	 * @param lineNumber
	 * @return
	 */
	public SubProductShipping findLastKnuckle(String processLocation, String lineNumber);
	
	/**
	 * find last sub production shipping production lot
	 * @return
	 */
	public SubProductShipping findLastShippingLot();
	
	public List<SubProductShipping> findAllKnuckleShippingLots(String processLocation);
	
	/**
	 * find all to be shipped and order by production date and seq no
	 * @param lineNumber - plant 1 or plant 2
	 * @return 
	 */
	public List<SubProductShipping> findAllKnuckleShipping(String processLocation,String lineNumber);
	
	/**
	 * find all shipping and shipped and order by production date and seq no at a process location
	 * @return
	 */
	public List<SubProductShipping> findAllKuckleShippingAndShipped(String processLocation);

	// find all knuckle shipping lots and order by production date and seq no
	public List<SubProductShipping> findAllKuckleShippingAndShipped();
	
	// find all incomplete shipping lots of a ProductType and order by production date and seq no 
	public List<SubProductShipping> findAllShipping(String productType,String lineNo,List<String> subIds);
	
	/**
    * find all shipping lots with the same kd lot number
    * @param kdLotNumber
    * @return
    */
	public List<SubProductShipping> findAllWithSameKdLot(String kdLotNumber,String productionLot);
	
	/**
	 * create knuckle shipping lots with same kd lot and create knuckles for each lot
	 * @param productionLot
	 */
	public List<SubProductShipping> createKnuckleShippingLots(String productionLot);
	
	public SubProductShipping incrementActualQuantity(String kdLotNumber,String productionLot);
    
	public SubProductShipping decrementActualQuantity(String kdLotNumber,String productionLot);

	/**
     * find business date for pruning by using the days to keep parameter
     * @param days - number of business days to keep
     * @return
     */
    public String findMinPruningDate(int days);

    public int deleteKdLots(String productType,List<String> kdLotsArray);

	public SubProductShipping findLastShippingLot(ProductType productType, Date date);
	
	public SubProductShipping createSubProductShipping(PreProductionLot preproductionLot, ProductType productType, int seqIntval, String[] subIds);
}
