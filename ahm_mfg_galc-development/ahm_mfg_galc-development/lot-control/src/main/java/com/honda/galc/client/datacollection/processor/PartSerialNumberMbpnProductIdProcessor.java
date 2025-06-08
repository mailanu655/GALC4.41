package com.honda.galc.client.datacollection.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.exception.ServiceInvocationException;
import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.LotControlPartUtil;

/**
 * Several different model types can be associated to a MBPN part. The product spec
 * variations can not be determined from the product ID mask - Jira NALC 110.
 * The product spec variations for the part will be based on the vehicle build 
 * sequence YMTOC lot control rule.
 * 
 * @author Bernard Leong
 * @since Jan 22, 2021
 */
public class PartSerialNumberMbpnProductIdProcessor extends PartSerialNumberProductIdProcessor {

	public PartSerialNumberMbpnProductIdProcessor(ClientContext context) {
		super(context);
	}

	protected void confirmProductId(ProductId productId)
			throws SystemException, TaskException, IOException
	{
		checkProductIdNotNull(productId);
		
		if (context.isRemoveIEnabled()) {
			if (getController().getProperty().getProductType().equalsIgnoreCase(ProductType.FRAME.toString())) 
				productId.setProductId(context.removeLeadingVinChars(productId.getProductId()));
		}

		product.setProductId(productId.getProductId());
//		checkProductIdLength();
		if (context.isPreviousLineCheckEnabled())
			performPreviousLineCheck();

		try {
			if (context.isOnLine())
				confirmProdIdOnServer();
			else
				confirmProductIdOnLocalCache();

			if (product.getKdLotNumber() != null) {
				Logger.getLogger().info("Product KD lot number:", product.getKdLotNumber());
			}

		} catch (TaskException te) {
			throw te;
		} catch (ServiceTimeoutException se) {
			handleServerOffLineException(se);
		} catch (ServiceInvocationException sie) {
			handleServerOffLineException(sie);
		} catch (Exception e) {
			throw new TaskException(e.getClass().toString(), this.getClass().getSimpleName());
		}

		if (context.isCheckExpectedProductId())
			checkExpectedProduct(productId);

		boolean checkResult = true;
		if (!getController().getClientContext().getProperty().isSkipProductChecks()) {
			checkResult = executeCheck(product.getBaseProduct(), context.getAppContext().getProcessPoint());
		}
		product.setValidProductId(checkResult);
		if (checkResult) {
			executeProductWarnChecks(product.getBaseProduct(), context.getAppContext().getProcessPoint());
		}
	}
	
	@Override
	protected BaseProduct getProductFromServer() {
		BaseProduct aproduct = context.getDbManager().confirmProductOnServer(product.getProductId());
		Frame frame = getExpectedFrame();
		String productSpecCode = getProductSpecCode(frame, product.getProductId());
		if (aproduct == null && ProductTypeUtil.isMbpnProduct(getSubAssyProductType())) {
				aproduct = getOrCreateMbpnProduct(product.getProductId(), productSpecCode,frame.getKdLotNumber());
				if(isUpdateMBPNProductSequence() && aproduct != null) {
					addMBPNProductToLine(aproduct, getController().getClientContext().getAppContext().getProcessPoint());
				}
		}
		product.setProductSpec(productSpecCode);

		return aproduct;
	}
	
	private Frame getExpectedFrame() {
		String expectedProductId = getExpectedProductId();
		if (expectedProductId == null) {
			handleException("Error: " + expectedProductId + " does not exist");
		}
		Frame frame = ServiceFactory.getDao(FrameDao.class).findByKey(expectedProductId);
		if (frame == null) 
			handleException("Frame " + expectedProductId + " not found.");
		return frame;
	}
	
	public String getProductSpecCode(Frame frame, String productId) {
		if (product.getProductId().equals(productId) && !StringUtils.isBlank(product.getProductSpec())) {
			return product.getProductSpec();
		}
		String productSpecCode = null;
		PartSpec partSpec = getPartSpecFromLotControlRule(frame);
		{
			String mask = partSpec.getPartSerialNumberMask();
			if (!CommonPartUtility.verification(productId, mask, PropertyService.getPartMaskWildcardFormat()))
				handleException("Invalid Part Serial Number: " + productId + ", not matching rule mask: " + mask);
			if (StringUtils.isEmpty(partSpec.getPartNumber()))
				handleException("Missing lot control rule: " + productId + ", no product spec code found.");
			else
				productSpecCode = partSpec.getPartNumber();
		}
		return productSpecCode;
	}
	
	private MbpnProduct getOrCreateMbpnProduct(String productId, String productSpecCode, String kdLotNumber) {
		MbpnProductDao mbpnProductDao = ServiceFactory.getDao(MbpnProductDao.class);
		MbpnProduct mbpnProduct = mbpnProductDao.findBySn(productId);
		if (mbpnProduct == null) {
			mbpnProduct = new MbpnProduct();
			mbpnProduct.setProductId(productId);
			mbpnProduct.setCurrentProductSpecCode(productSpecCode);
			mbpnProduct.setCurrentOrderNo(kdLotNumber);
			mbpnProduct.setLastPassingProcessPointId(context.getProcessPointId().trim());
			mbpnProductDao.save(mbpnProduct);
		} 
		else 
			handleException("Error: " + productId + " has a MBPN_PRODUCT_TBX record already");

		return mbpnProduct;
	}
	
	/**
	 * Get part spec from install/marriage lot control rule
	 */
	private PartSpec getPartSpecFromLotControlRule(Frame frame) {
		String prodSpecProcessPoint = property.getProdSpecProcessPointId();
		
		FrameSpec frameSpec = ServiceFactory.getDao(FrameSpecDao.class).findByKey(frame.getProductSpecCode());
		List<LotControlRule> rules = ServiceFactory.getDao(LotControlRuleDao.class)
				.findAllByProcessPointAndProductSpec(prodSpecProcessPoint, context.getProperty().getProductType(),
						frameSpec);
		// Get specific prod spec setup
		rules = LotControlPartUtil.getLotControlRuleByProductSpec(frameSpec, rules);
		
		List<LotControlRule> finalResult = new ArrayList<LotControlRule>();
		for (LotControlRule lotControlRule : rules) {
			List<PartSpec> partSpecs = lotControlRule.getParts(); 
			if (partSpecs.size() != 1) continue;

			String partNameString = partSpecs.get(0).getId().getPartName().trim();
			PartName specPartNameObject = ServiceFactory.getDao(PartNameDao.class).findByKey(partNameString);
			
			if (specPartNameObject.getSubProductType() == null) continue;
			

			if (specPartNameObject.getSubProductType().trim().equals(context.getProperty().getSubAssyProductType().trim()))
				finalResult.add(lotControlRule);
		}
		
		if (finalResult.size() != 1)
			handleException("Error: " + frame.getProductId() + " has no rules setup for prod spec "
					+ frame.getProductSpecCode() + " for process point " + prodSpecProcessPoint);
			
		return finalResult.get(0).getParts().get(0);
	}
	
	private boolean isUpdateMBPNProductSequence() {
		return PropertyService.getPropertyBoolean(getController().getClientContext().getProcessPointId(), "UPDATE_MBPN_SEQUENCE",false);
	}
	
	
	
	public void addMBPNProductToLine(BaseProduct product, ProcessPoint processPoint) {
		
		InProcessProductDao inProcessProductDao = ServiceFactory.getDao(InProcessProductDao.class);
		InProcessProduct inProcessProduct = inProcessProductDao.findByKey(product.getProductId());
		if (inProcessProduct != null) {
			if (inProcessProduct.getLineId().equals(processPoint.getLineId()) &&
					inProcessProduct.getNextProductId() == null) {
				Logger.getLogger().check("In Process Product " + product.getProductId() + " is already the tail of the target line " + processPoint.getLineId()); 
				return;		// already the tail in the correct line, so skip addToLine()
			}
		}
		
		try {
			inProcessProductDao.addToLine(product, processPoint);
		} catch (Exception ex) {
			ex.printStackTrace();
			// If for some reason we encountered a deadlock exception the current solution is to
			// retry addToLine one more time.
			try {
				Logger.getLogger().error(ex, "Retrying add to line for product" + product + processPoint);
				inProcessProductDao.addToLine(product, processPoint);
			} catch (Exception e) {
				e.printStackTrace();
				Logger.getLogger().error(ex, "Retrying add to line for product" + product + processPoint);
			}
		}
	}
	
}
