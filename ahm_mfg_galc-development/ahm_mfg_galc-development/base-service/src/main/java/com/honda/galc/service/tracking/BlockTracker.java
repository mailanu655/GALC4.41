package com.honda.galc.service.tracking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.dao.product.BlockHistoryDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.BlockHistory;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>BlockTracker</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> BlockTracker description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Jul 13, 2011</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Jul 13, 2011
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class BlockTracker extends DieCastMachiningTracker<Block>{

	@Autowired
	BlockHistoryDao blockHistoryDao;
	@Autowired
	BlockDao blockDao;
	
	@Override
	boolean isProductHistoryExist(Block product, ProcessPoint processPoint) {
		return getBlockHistoryDao().hasProductHistory(product.getBlockId(), processPoint.getProcessPointId());
	}

	@Override
	void saveProductHistory(ProductHistory productHistory) {
		getBlockHistoryDao().save((BlockHistory)productHistory);
	}

	private BlockHistoryDao getBlockHistoryDao() {
		if(blockHistoryDao == null)
			blockHistoryDao = ServiceFactory.getDao(BlockHistoryDao.class);
		return blockHistoryDao;
	}

	@Override
	@Transactional
	void updateTrackingAttributes(Block product, DailyDepartmentSchedule schedule) {
		try {
			blockDao.updateTrackingAttributes(product);
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error(e, "Failed to update tracking attributes for Block, blockId=", product.getProductId());
		}
		
	}
	
	@Override
	protected boolean isProductShipped(Block product) {
		
		return super.isProductShipped(product) || isParentEngineShipped(product);
	}
	
	public BlockDao getBlockDao() {
		if(blockDao == null)
			ServiceFactory.getDao(BlockDao.class);
		
		return blockDao;
		
	}

	@Override
	@Transactional
	Block findProductById(String productId) {
		return getBlockDao().findByKey(productId);
	}

}
