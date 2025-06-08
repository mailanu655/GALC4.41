package com.honda.galc.service.tracking;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>InProcessProductManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> InProcessProductManager description </p>
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
 * Sep 8, 2010
 *
 * @param <T>
 */

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class BaseProductSequenceManager<T extends BaseProduct> implements IProductSequenceManager<T> {
	
	protected InProcessProductDao inProcessProductDao;
	protected Logger logger;
	
	public BaseProductSequenceManager() {}
	
	@Transactional
	public void productFactoryExit(T product, ProcessPoint processPoint) {
		if (product == null) {
			logger.warn("Factory exit could not be processed for null product");
			return;
		}
		
		getInProcessProductDao().productFactoryExit(product, processPoint);
		logger.info("Completed factory exit for product " + product.getProductId()); 
	}
	
	public void moveOnLine(T product, ProcessPoint processPoint) {
		
		logInfo("Started In Process Product move on line, ", product, processPoint);
		
		try {
			getInProcessProductDao().updateLastPassingProcessPoint(product.getProductId(), processPoint.getProcessPointId());
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex, "Error updating last passing pp for InProcessProduct: productId = " + ((product == null) ? "null" : product.getProductId()));
		}
		
		logInfo("Finished In Process Product move on line, ", product, processPoint);
	}
	
	public void addToLine(T product, ProcessPoint processPoint) {
		InProcessProduct inProcessProduct = getInProcessProductDao().findByKey(product.getProductId());
		if (inProcessProduct != null) {
			if (inProcessProduct.getLineId().equals(processPoint.getLineId()) &&
					inProcessProduct.getNextProductId() == null) {
				logger.check("In Process Product " + product.getProductId() + " is already the tail of the target line " + processPoint.getLineId()); 
				return;		// already the tail in the correct line, so skip addToLine()
			}
		}
		
		try {
			getInProcessProductDao().addToLine(product, processPoint);
		} catch (Exception ex) {
			ex.printStackTrace();
			// If for some reason we encountered a deadlock exception the current solution is to
			// retry addToLine one more time.
			try {
				logInProcessProductException("Retrying add to line for product", product, processPoint, ex);
				getInProcessProductDao().addToLine(product, processPoint);
			} catch (Exception e) {
				e.printStackTrace();
				logInProcessProductException("Could not add product", product, processPoint, e);
			}
		}
	}

	public void logInProcessProductException(String msg, T product, ProcessPoint processPoint, Exception e) {
		logger.error(e, this.getClass().getSimpleName() + ": " 
			+ msg
			+ StringUtils.trimToEmpty(product.getProductId()) + " to line "
			+ StringUtils.trimToEmpty(processPoint.getLineId()) + " at process point "
			+ StringUtils.trimToEmpty(processPoint.getProcessPointId()));
	}

	protected boolean isEqual(String str1, String str2) {
		return str1 == null? str2 == null : str1.equals(str2);
	}
	
	protected InProcessProduct createInProcessProduct(T product) {
			return new InProcessProduct(product);
	}
	
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	protected void logInfo(String msg, T product, ProcessPoint processPoint) {
		logger.info(msg, "productId=", product == null? "null" : product.getProductId(),
				", processPoint=", processPoint.getProcessPointId());
	}
	
	protected void logWarn(String msg, T product, ProcessPoint processPoint) {
		logger.warn(msg, "productId=", product == null? "null" : product.getProductId(),
				", processPoint=", processPoint.getProcessPointId());
	}
	
	public InProcessProductDao getInProcessProductDao() {
		if(inProcessProductDao == null)
			inProcessProductDao = ServiceFactory.getDao(InProcessProductDao.class);
		return inProcessProductDao;
	}
}
