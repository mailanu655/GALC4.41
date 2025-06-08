/**
 * 
 */
package com.honda.galc.service.tracking;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InProcessProduct;

/**
 * @author Subu Kathiresan
 * @date May 09, 2013
 *
 */
public class ForceLineProductSequenceManager <T extends BaseProduct> extends BaseProductSequenceManager<T> {

	public ForceLineProductSequenceManager() {}

	@Override
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.REQUIRES_NEW)
	public void moveOnLine(T product, ProcessPoint processPoint) {
		StringBuilder msg = new StringBuilder();
		msg.append("In Process Product move on line, ");
		InProcessProduct inProcessProduct = getInProcessProductDao().findByKey(product.getProductId());
		
		if(inProcessProduct == null){
			inProcessProduct = createInProcessProduct(product);
			inProcessProduct.setNextProductId("MISSED FIRST ON");
			inProcessProduct.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		} else {
			if(!isEqual(inProcessProduct.getLineId(), processPoint.getLineId())){
				inProcessProduct.setNextProductId("MISSED ON");
				logger.warn("Tracking Product Id:", product.getProductId(), " Process Point Id:",
						processPoint.getProcessPointId(), " current Line Id does not match with 'TrackingStatus'",
						" - mission ON. TrackingStatus will be updated to current line.");
			}
		}
		
		if(!StringUtils.isEmpty(inProcessProduct.getLastPassingProcessPointId()))
			msg.append(" from processPoint=").append(inProcessProduct.getLastPassingProcessPointId()).append(". ");
			
		inProcessProduct.setLineId(processPoint.getLineId());
		inProcessProduct.setLastPassingProcessPointId(processPoint.getProcessPointId());
		getInProcessProductDao().save(inProcessProduct);
		
		logInfo(msg.toString(), product, processPoint);
	}
}
