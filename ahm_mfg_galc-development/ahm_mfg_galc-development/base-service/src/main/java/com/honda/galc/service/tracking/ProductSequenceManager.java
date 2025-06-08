package com.honda.galc.service.tracking;

import java.sql.Timestamp;

import com.honda.galc.dao.product.ProductSequenceDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ProductSequence;
import com.honda.galc.service.ServiceFactory;
/**
 * 
 * <h3>ProductSequenceManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductSequenceManager description </p>
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
public class ProductSequenceManager <T extends BaseProduct> extends BaseProductSequenceManager<T> {

	private ProductSequenceDao productSequenceDao;
	
	public ProductSequenceManager() {}

	@Override
	public void productFactoryExit(T product, ProcessPoint processPoint) {
		getProductSequenceDao().remove(createProductSequence(product));
	}
	
	@Override
	public void addToLine(T product, ProcessPoint processPoint) {
		moveOnLine(product, processPoint);
	}
	
	@Override
	public void moveOnLine(T product, ProcessPoint processPoint) {
		ProductSequence productSequence = getProductSequenceDao().findByProductId(product.getProductId());
		if(productSequence == null){
			productSequence = createProductSequence(product);
		}
		
		productSequence.getId().setProcessPointId(processPoint.getProcessPointId());
		productSequence.setReferenceTimestamp(new Timestamp(System.currentTimeMillis()));
		//TODO update lineId and last passing process point id
		
		getProductSequenceDao().save(productSequence);
		
		//TODO log
	}

	private ProductSequence createProductSequence(T product) {
		ProductSequence productSequence = new ProductSequence();
		productSequence.getId().setProductId(product.getProductId());
		return productSequence;
	}

	public ProductSequenceDao getProductSequenceDao() {
		if(productSequenceDao == null)
			productSequenceDao = ServiceFactory.getDao(ProductSequenceDao.class);
		return productSequenceDao;
	}
	
    public boolean isProductIdAheadOfExpectedProductId(
			String expectedProductId, String productId) {
		ProductSequenceDao productSequenceDao = ServiceFactory.getDao(ProductSequenceDao.class);
		ProductSequence productSequence = productSequenceDao.findByProductId(productId);
		ProductSequence expectedProductSequence = productSequenceDao.findByProductId(expectedProductId);
		return (productSequence != null && expectedProductSequence != null && 
				productSequence.getId().getProcessPointId().equals(expectedProductSequence.getId().getProcessPointId()) &&
				expectedProductSequence.getReferenceTimestamp().after(productSequence.getReferenceTimestamp()));
    }
}
