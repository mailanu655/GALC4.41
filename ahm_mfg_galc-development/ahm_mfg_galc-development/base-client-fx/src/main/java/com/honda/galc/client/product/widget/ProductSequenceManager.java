package com.honda.galc.client.product.widget;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.product.AbstractExpectedProductManager;
import com.honda.galc.client.product.IExpectedProductManager;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.ProductSequenceDao;
import com.honda.galc.entity.product.ProductSequence;
import com.honda.galc.entity.product.ProductSequenceId;

 /**
  * 
  * 
  * <h3>ProductSequenceManager Class description</h3>
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
  * @author Jeffray Huang<br>
  * Mar 6, 2014
  *
  *
  */
public class ProductSequenceManager extends AbstractExpectedProductManager
implements IExpectedProductManager
{

	public ProductSequenceManager(ProductModel model, Logger logger) {
		super(model, logger);
	}

	@Override
	public String getNextExpectedProductId(String productId) {
		ProductSequence sequence = findCurrentProductSequence(productId); 
		if(sequence == null) return null;

		ProductSequence seq = getDao(ProductSequenceDao.class).findNextExpectedProductId(sequence);
		getLogger().debug("next expected productId:" + sequence.getId().getProductId());
		return seq == null? null : seq.getId().getProductId();
	}

	
	public List<String> getIncomingProducts() {
		if(StringUtils.isEmpty(model.getExpectedProductId())) return null;
		ProductSequence sequence = findCurrentProductSequence(model.getExpectedProductId());
		if(sequence == null) return null;
		
		return getDao(ProductSequenceDao.class).findIncomingExpectedProductIds(sequence);
	}
	
	private ProductSequence findCurrentProductSequence(String productId) {
		return getDao(ProductSequenceDao.class).findByKey(
				new ProductSequenceId(productId,model.getProperty().getInProductSequenceId()));
	}
	

	
	public void updateProductSequence() {
		if(StringUtils.isEmpty(StringUtils.trim(model.getProductId()))) return; 
		if(model.getProcessPointId().equals(model.getProperty().getInProductSequenceId())){
			addProductSequence();
		} else if(model.getProcessPointId().equals(model.getProperty().getOutProductSequenceId())){
			removeProductSequence();
		}
		
	}

	private void removeProductSequence() {
		ProductSequenceId id = new ProductSequenceId(model.getProductId(),model.getProperty().getInProductSequenceId());
		List<ProductSequence> removeList = getDao(ProductSequenceDao.class).removeProductSequence(new ProductSequence(id));
		getLogger().info("removed Product Sequence:", removeList.toString());
	}

	private void addProductSequence() {
		ProductSequence sequence = new ProductSequence();
		sequence.setId(new ProductSequenceId(model.getProductId(),model.getProcessPointId()));
		sequence.setAssociateNo(ApplicationContext.getInstance().getUserId());
		sequence.setReferenceTimestamp(new Timestamp(System.currentTimeMillis()));
		getDao(ProductSequenceDao.class).save(sequence);

	}

    public boolean isProductIdAheadOfExpectedProductId(String expectedProductId, String productId) {
		ProductSequenceDao productSequenceDao = getDao(ProductSequenceDao.class);
		ProductSequence productSequence = getDao(ProductSequenceDao.class).findByProductId(productId);
		ProductSequence expectedProductSequence = productSequenceDao.findByProductId(expectedProductId);
		return (productSequence != null && expectedProductSequence != null && 
				productSequence.getId().getProcessPointId().equals(expectedProductSequence.getId().getProcessPointId()) &&
				expectedProductSequence.getReferenceTimestamp().before(productSequence.getReferenceTimestamp()));
        }
}
