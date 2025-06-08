package com.honda.galc.dao.jpa.product;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.PartLotDao;
import com.honda.galc.entity.enumtype.PartLotStatus;
import com.honda.galc.entity.product.PartLot;
import com.honda.galc.entity.product.PartLotId;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>PartLotDaoImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PartLotDaoImpl description </p>
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
 * @author Paul Chou
 * Nov 21, 2010
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class PartLotDaoImpl extends BaseDaoImpl<PartLot,PartLotId> implements PartLotDao{

    private final String FIND_ALL_BY_STATUS_AND_DATE =
        "select p " +
        "from PartLot p " +
        "where p.statusId = :statusId " +
        "and   p.updateTimestamp < :productionDate " +
        "order by p.updateTimestamp desc";
	
    private final String FIND_ALL_PART_LOTS_BY_STATUS ="select p " +
	"from PartLot p where p.id.partName = :partName and p.statusId = :statusId order by p.updateTimestamp desc";
    private static final String DELETE_BY_PRODUCTION_DATE = "DELETE FROM GALADM.PART_LOT_TBX WHERE STATUS = 2 AND DATE(UPDATE_TIMESTAMP) <= ?";

    public PartLot findCurrentPartLot(String partName, boolean remake) {
		Parameters params = Parameters.with("partName", partName);
		if(remake){
			params.put("statusId", (short)PartLotStatus.SAFTYSTOCK .getId());
		} else {
			params.put("statusId", (short)PartLotStatus.INPROGRESS.getId());
		}
		
		
		return findFirstByQuery(FIND_ALL_PART_LOTS_BY_STATUS, params);
	}
	
	public PartLot findInprogressPartLot(String partName) {
		Parameters params = Parameters.with("statusId", (short)PartLotStatus.INPROGRESS.getId());
		params.put("id.partName", partName);
		
		
		return findFirst(params);
	}

    public List<PartLot> findAllByStatusAndDate(String productionDate, short status) {
        
        Parameters params = Parameters.with("statusId", status);
        params.put("productionDate", Timestamp.valueOf(productionDate));        
        return findAllByQuery(FIND_ALL_BY_STATUS_AND_DATE, params);
        
    }

	public List<PartLot> findAllPartLotsByStatus(String partName, short status) {
		
		Parameters params = Parameters.with("statusId", status);
		params.put("partName", partName);
		return findAllByQuery(FIND_ALL_PART_LOTS_BY_STATUS, params);
		
	}
	
	@Transactional
	public void updateSaftyStockQuantity(PartLot partLot) {
		List<PartLot> all = findAll(Parameters.with("id.partSerialNumber", partLot.getId().getPartSerialNumber()));

		for(PartLot lot : all)
			lot.setCurrentQuantity(partLot.getCurrentQuantity());
		
		updateAll(all);
		
	}

    @Transactional
    public int delete(String partSerialNumber, String partName, String partNumber)
    {
        Parameters params = Parameters.with("id.partSerialNumber", partSerialNumber);
        params.put("id.partName"  , partName);
        params.put("id.partNumber", partNumber);
        return delete(params);
    }
    @Transactional
    public int deleteAllByProductionDate(Date productionDate) {
		return executeNativeUpdate(DELETE_BY_PRODUCTION_DATE,Parameters.with("1", productionDate));
	}

}
