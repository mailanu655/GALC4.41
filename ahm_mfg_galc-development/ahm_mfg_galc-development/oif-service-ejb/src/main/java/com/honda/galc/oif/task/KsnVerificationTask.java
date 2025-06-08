package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class KsnVerificationTask extends OifAbstractTask implements IEventTaskExecutable{
	
	
	public KsnVerificationTask(String name) {
		super(name);
	}
	
	public void execute(Object[] args) {
		
		try{	
			verify();
		}catch(Exception e) {
			logger.error(e,"Could not finish the task due to exception " + e.getMessage());
		}
		
	}
	
	private void verify() {
		
		logger.info("starting to check all pre-production lots...");
		
		// find all pre-production lots which has not passed on process (not splash shield )
		List<PreProductionLot> preProdLots = getDao(PreProductionLotDao.class).findAllWithIncorrectKsns();
		
		if(preProdLots.isEmpty())
			logger.info("ksns match the part number build attributes for all pre-production lots");
		else {
			for(PreProductionLot item : preProdLots) {

				logger.emergency("KSNs does not match the part number build attributes. Production lot: " + item.getProductionLot());
			}	
		}
		
		logger.info("finished checking all pre-production lots");
		
	}
	
}
