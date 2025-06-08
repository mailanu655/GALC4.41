package com.honda.galc.client.datacollection.observer;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.property.ProductSequencePropertyBean;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.ProductSequenceDao;
import com.honda.galc.entity.product.ProductSequence;
import com.honda.galc.entity.product.ProductSequenceId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class ProductSequenceManager extends AbstractExpectedProductManager
implements IExpectedProductManager
{
	ProductSequencePropertyBean prodSeqProperty;

	public ProductSequenceManager(ClientContext context) {
		super(context);
		init();
	}

	private void init() {
		prodSeqProperty = PropertyService.getPropertyBean(ProductSequencePropertyBean.class, context.getProcessPointId());
		
	}
	
	@Override
	public String getNextExpectedProductId(String productId) {
		ProductSequenceDao dao = ServiceFactory.getDao(ProductSequenceDao.class);
		ProductSequence sequence = getCurrentProductSequence(productId, dao);
		if(sequence == null) return null;

		ProductSequence seq = dao.findNextExpectedProductId(sequence);
		Logger.getLogger().debug("next expected productId:" + sequence.getId().getProductId());

		return seq == null? null : seq.getId().getProductId();
	}

	private ProductSequence getCurrentProductSequence(String productId,
			ProductSequenceDao dao) {
		ProductSequenceId id = new ProductSequenceId();
		id.setProcessPointId(prodSeqProperty.getInProductSequenceId());
		id.setProductId(productId);
		ProductSequence sequence = dao.findByKey(id);
		return sequence;
	}
	
	public List<String> getIncomingProducts(DataCollectionState state) {
		ProductSequenceDao dao = ServiceFactory.getDao(ProductSequenceDao.class);
		if(state.getExpectedProductId() == null) return null;
		ProductSequence sequence = getCurrentProductSequence(state.getExpectedProductId(), dao);
		if(sequence == null) return null;
		
		return dao.findIncomingExpectedProductIds(sequence);
	}
	
	@Override
	public void getExpectedProductIdFromServer(ProcessProduct state) {
		super.getExpectedProductIdFromServer(state);
		
		//In case the expected product id not exist, for example 1st time 
		// to set up this feature, then search data base to find one
		if(StringUtils.isEmpty(state.getExpectedProductId()) && expectedProdId == null){
			ProductSequenceDao productSequenceDao = ServiceFactory.getDao(ProductSequenceDao.class);
			ProductSequence productSequ = productSequenceDao.findFirstExpectedProductId(context.getProcessPointId(), prodSeqProperty.getInProductSequenceId());
			state.setExpectedProductId(productSequ == null ? null : productSequ.getId().getProductId());
		}
	}
	
	public void updateProductSequence(ProcessProduct state) {
		if(StringUtils.isEmpty(StringUtils.trim(state.getProductId()))) return; 
		if(context.getProcessPointId().equals(prodSeqProperty.getInProductSequenceId())){
			addProductSequence(state);

		} else if(context.getProcessPointId().equals(prodSeqProperty.getOutProductSequenceId())){
			removeProductSequence(state);
		}
		
	}

	private void removeProductSequence(ProcessProduct state) {
		ProductSequenceId id = new ProductSequenceId(state.getProductId(),prodSeqProperty.getInProductSequenceId());
		List<ProductSequence> removeList = ServiceFactory.getDao(ProductSequenceDao.class).removeProductSequence(new ProductSequence(id));
		Logger.getLogger().info("removed Product Sequence:", removeList.toString());

	}

	private void addProductSequence(ProcessProduct state) {
		ProductSequence sequence = new ProductSequence();
		sequence.setId(new ProductSequenceId(state.getProductId(),context.getProcessPointId()));
		sequence.setAssociateNo(ApplicationContext.getInstance().getUserId());
		sequence.setReferenceTimestamp(new Timestamp(System.currentTimeMillis()));
		ServiceFactory.getDao(ProductSequenceDao.class).save(sequence);

	}

    public boolean isProductIdAheadOfExpectedProductId(String expectedProductId, String productId) {
		ProductSequenceDao productSequenceDao = ServiceFactory.getDao(ProductSequenceDao.class);
		ProductSequence productSequence = productSequenceDao.findByProductId(productId);
		ProductSequence expectedProductSequence = productSequenceDao.findByProductId(expectedProductId);
		return (productSequence != null && expectedProductSequence != null && 
				productSequence.getId().getProcessPointId().equals(expectedProductSequence.getId().getProcessPointId()) &&
				expectedProductSequence.getReferenceTimestamp().before(productSequence.getReferenceTimestamp()));
        }

    public String findPreviousProductId(String productId) {
		String previousProductId = null;
		ProductSequence previousProductSequence = context.getDbManager().findPreviousProductSequenceByProductId(productId);
		if (previousProductSequence != null) {
			previousProductId = previousProductSequence.getId().getProductId();
		}
		return previousProductId;
	}

	public boolean isInSequenceProduct(String productId) {
		ProductSequence productSequence = context.getDbManager().findProductSequenceByProductId(productId);
		return (productSequence != null);
	}
}
