package com.honda.galc.dao.product;

import java.sql.Date;
import java.util.List;

import com.honda.galc.entity.product.PartLot;
import com.honda.galc.entity.product.PartLotId;
import com.honda.galc.service.IDaoService;


/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public interface PartLotDao extends IDaoService<PartLot, PartLotId>{
	
	/**
	 * Find the current Part Lot in processing or remake
	 * @param partName
	 * @param remake
	 * @return
	 */
	public PartLot findCurrentPartLot(String partName, boolean remake);
	
	/**
	 *Find the part lot is currently processing
	 * @param remake 
	 */
	public PartLot findInprogressPartLot(String partName);
	
    /**
     * Find all part lot with the specified status and older than date.
     * @param status
     * @param productionDate
     * @return
     */
    public List<PartLot> findAllByStatusAndDate(String productionDate, short status);

	/**
	 * Find all part lot with the specified status
	 * @param partName
	 * @param status
	 * @return
	 */
	public List<PartLot> findAllPartLotsByStatus(String partName, short status);

	/**
	 * Update quantity for part lots with same part serial number  
	 * @param currentPartLot
	 */
	public void updateSaftyStockQuantity(PartLot partLot);

    public int delete(String partSerialNumber, String partName, String partNumber);
    
    /**
     * delete all rows whose date is earlier than "production date"
     * @param productionDate
     * @return
     */
    public int deleteAllByProductionDate(Date productionDate);
}
