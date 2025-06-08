package com.honda.galc.client.datacollection.processor;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.ProductBean;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.ProductSequenceDao;
import com.honda.galc.dao.product.SequenceDao;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.ProductSequence;
import com.honda.galc.entity.product.Sequence;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.StragglerService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.StringUtil;

/**
 * @version 1.0
 * @author Gangadhararao Gadde 
 * @since May 19, 2014
 */
/**
 * @version 2.0
 * @author Subu Kathiresan
 * @date May 20, 2016
 */
public class AFOnFrameVinProcessor extends FrameVinProcessor {



	public AFOnFrameVinProcessor(ClientContext lotControlClientContext) {
		super(lotControlClientContext);
	}

	@Override
	public synchronized boolean execute(ProductId productId) {
		Logger.getLogger().debug("AFOnFrameVinProcessor : Enter execute method");

		try {	
			
				super.confirmProductId(productId);
				
				if(!validateProductSpecCode(productId)){
					return false;
				}
			
				if(!setSeqNum()) return false;
			
				if (isInvokeStragglerService()) identifyStragglers();
				getController().getFsm().productIdOk(product);
			}catch (Exception  e){
				logException(e, productId.getProductId());
				return false;
			}
		Logger.getLogger().debug("AFOnFrameVinProcessor : Exit execute method");
		return true;
	}
	
	
	protected boolean validateProductSpecCode(ProductId productId) {
		if(isSkipProductSpecCheck()) return true;
		product.setProductId(productId.getProductId());
		String productSpecCode = product.getProductSpec();
		Sequence nextSequence = ServiceFactory.getDao(SequenceDao.class).getNextSequence(getSequenceName());
		
		Logger.getLogger().info("Processing product- "+productId.getProductId() );
		
		List<ProductSequence> prodSequences = ServiceFactory.getDao(ProductSequenceDao.class).findAllByProcessPointIdAndSequenceNumber(getProductCheckPropertyBean().getLastPassingProcessPoint(),nextSequence.getCurrentSeq());
		String errMsg = " MTOCI mismatch: received - "+ productId.getProductId() +"( "+productSpecCode+" )" ; 
		if(prodSequences != null){
			ProductSequence prodSequence = prodSequences.get(prodSequences.size() -1) ;
			Frame prod = ServiceFactory.getDao(FrameDao.class).findByKey(prodSequence.getId().getProductId());
			String specCode = prod.getProductSpecCode();
			if(!specCode.equalsIgnoreCase(productSpecCode)){
					
					getController().getFsm().productIdNg(product, "MSG_PROD_SPEC_MISMATCH", errMsg +", expected : "+specCode);
					return false;
			}else{
				Logger.getLogger().info("MTOCI for product- "+productId.getProductId() + " matches expected SpecCode - "+ specCode);
			}
		}else{
				errMsg = "Sequence mismatch ";
				getController().getFsm().productIdNg(product, "MSG_PROD_SPEC_MISMATCH", errMsg );
				return false;
		}
			
		return true;
	}

	public ProcessPoint getProcessPoint() {
		return ServiceFactory.getDao(ProcessPointDao.class).findByKey(context.getProcessPointId().trim());	
	}
	
	public Frame getFrame() {
		return ServiceFactory.getDao(FrameDao.class).findByKey(product.getProductId());
	}

	public void identifyStragglers() {
		ProcessPoint processPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(context.getProcessPointId().trim());
		ServiceFactory.getService(StragglerService.class).identifyStragglers(processPoint, getFrame());
	}
	
	public boolean isInvokeStragglerService() {
		return PropertyService.getPropertyBean(ProductPropertyBean.class, context.getProcessPointId().trim()).isInvokeStragglerService();
	}

	public void logErrorMessage(String message) {
		Logger.getLogger().error(message);
	}
	
	public void logException(Exception ex, String productId) {
		ex.printStackTrace();
		ProductBean productBean = new ProductBean();
		productBean.setProductId(productId);
		String msg = StringUtils.trimToEmpty(ex.getMessage());
		getController().getFsm().productIdNg(productBean, MESSAGE_ID, msg);
		Logger.getLogger().error(msg);
	}
	
	public boolean isSkipProductSpecCheck() {
		return getController().getProperty().isSkipProductSpecCheck();
	}
	
	public boolean setSeqNum() {
		try {
				Sequence nextSeq = getNextSequence() ;
				Frame frame = getFrame();
				if(frame != null) {
					frame.setAfOnSequenceNumber(nextSeq.getCurrentSeq());
					Logger.getLogger().info("Assigning Sequence - "+getSequenceString(nextSeq) + " to Product - "+ frame.getId());
					ServiceFactory.getDao(FrameDao.class).save(frame);
				}
				
				Logger.getLogger().info("Saving Afon Sequence - "+getSequenceString(nextSeq) +" to Sequence Table");
				ServiceFactory.getDao(SequenceDao.class).save(nextSeq);
				
		} catch (Exception e) {
			logErrorMessage("An error occured while assigning AFON Sequence to ProductId");
			logException(e,product.getProductId());
			return false;
		}
		return true;
	}
	
	protected String getSequenceName(){
		return getController().getProperty().getSequenceName();
	}
	
	protected Sequence getNextSequence() {
		return ServiceFactory.getDao(SequenceDao.class).getNextSequence(getSequenceName());
	}
	
	protected String getSequenceString(Sequence nextSeq) {
		String text = nextSeq.getCurrentSeq()!= null?StringUtil.padLeft(nextSeq.getCurrentSeq().toString(),5,'0'):"N/A";
		
		return text;
	}
}
