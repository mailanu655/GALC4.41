package com.honda.galc.service.datacollection.task;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.SequenceDao;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.Sequence;
import com.honda.galc.property.ProductOnHlPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.service.on.OnErrorCode;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>AfOnTask</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Oct 28, 2013
 */
public class AfOnTask extends CollectorTask {

	SequenceDao sequenceDao;
	ProductOnHlPropertyBean onPropertyBean;
	
	public AfOnTask(HeadlessDataCollectionContext context, String processPointId) {
		super(context, processPointId);
	}

	@Override
	public void execute() {
		Frame product = (Frame) context.get("product");
		if (product.getAfOnSequenceNumber() != null) {
			getLogger().warn("AfOnSequence already exists, task (AfOnTask) will be skipped, product:", product.toString(), ", sequence: ", product.getAfOnSequenceNumber().toString());
			if(getOnPropertyBean().isProductAlreadyProcessedCheckEnabled())
				throw new TaskException("AfOnSequence already exists!");
		}
		
		if(!context.isProductStateCheckOk()){
			getLogger().warn("Failed Product State Check and task will be skipped, product:", product.toString());
			throw new TaskException("Product State Check Failed.");
		}
		
		if(getOnPropertyBean().isValidatePreviousProcessPointId() && !getOnPropertyBean().getPreviousProcessPointId().contains(product.getLastPassingProcessPointId())){
			context.put(TagNames.ERROR_CODE.name(), OnErrorCode.Processed_Ref.getCode());
			throw new TaskException("Exception: last passing process point id for " + product.getProductId() +  " is " + product.getLastPassingProcessPointId());
		}
		
		try {
			updateAfOnSequence(product);
		} catch (Exception e) {
			getLogger().error(e, "Failed to perform AfOnTask for product:", product.toString());
		}
		
	}

	protected void updateAfOnSequence(Frame product) {
		
		Sequence nextSequence = getSequenceDao().getNextSequence(context.getProperty().getSequenceName());
		if(product.getAfOnSequenceNumber() != null && (product.getAfOnSequenceNumber().equals(nextSequence.getCurrentSeq()))){
			//this should not happen the check condition should not allow this to here...just in case
			context.put(TagNames.ERROR_CODE.name(), OnErrorCode.Invalid_Product.getCode());
			throw new TaskException("Exception: product_id:" + product.getProductId() + " just processed.");
		}
		product.setAfOnSequenceNumber(nextSequence.getCurrentSeq());
		getProductDao().update(product);
		getSequenceDao().save(nextSequence);
		context.put(TagNames.AF_ON_SEQUENCE_NUMBER.name(), product.getAfOnSequenceNumber());
		context.put(TagNames.LINE_REF_NUMBER.name(), product.getLineRef(context.getProperty().getLineRefNumberOfDigits()));
		String msg = String.format("Updated afOnSequence for product : %s, afOnSequence: %s", product, product.getAfOnSequenceNumber());
		getLogger().info(msg);
	}

	// === get/set === //
	public FrameDao getProductDao() {
		return ServiceFactory.getDao(FrameDao.class);
	}
	
	public SequenceDao getSequenceDao() {
		if(sequenceDao == null)
			sequenceDao = ServiceFactory.getDao(SequenceDao.class);
		
		return sequenceDao;
	}

	 public ProductOnHlPropertyBean getOnPropertyBean() {
		if(onPropertyBean == null)
			onPropertyBean = PropertyService.getPropertyBean(ProductOnHlPropertyBean.class, context.getProcessPointId());
		
		return onPropertyBean;
	}
	
}
