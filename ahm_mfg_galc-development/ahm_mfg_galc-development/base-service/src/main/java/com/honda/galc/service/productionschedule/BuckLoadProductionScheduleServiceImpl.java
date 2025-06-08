package com.honda.galc.service.productionschedule;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.oif.SalesOrderFifDao;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.service.BuckLoadProductionScheduleService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ServiceUtil;

/**
 * <h3>BuckOnProductionScheduleService</h3> <h3>The class is a service
 * implementation for Buck On Production Service</h3>
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
 * .
 * 
 * @see BuckLoadProductionScheduleService
 * @author Hale Xie August 20, 2014
 */
public class BuckLoadProductionScheduleServiceImpl implements
		BuckLoadProductionScheduleService {

	/** The expected product dao. */
	@Autowired
	protected ExpectedProductDao expectedProductDao;

	/** The pre production lot dao. */
	@Autowired
	protected PreProductionLotDao preProductionLotDao;

	/** The product stamping sequence dao. */
	@Autowired
	protected ProductStampingSequenceDao productStampingSequenceDao;
	
	final private String ALC_INFO_CODE = "ALC_INFO_CODE";

	/**
	 * Gets the first available production lot.
	 * 
	 * @param processLocation
	 *            the process location
	 * @return the first available production lot
	 */
	protected PreProductionLot getFirstAvailableProductionLot(
			String processLocation) {
		PreProductionLot preProdLot = null;
		PreProductionLot tempPreProdLot = null;
		PreProductionLot retPreProdLot = null;
		preProdLot = preProductionLotDao
				.findLastPreProductionLotByProcessLocation(processLocation);
		if (preProdLot != null) {
			retPreProdLot = preProdLot;
			int sendStatus = preProdLot.getSendStatusId();
			while (sendStatus < 2) {
				tempPreProdLot = preProductionLotDao.findParent(preProdLot
						.getProductionLot());
				retPreProdLot = preProdLot;
				if (tempPreProdLot == null)
					break;
				sendStatus = tempPreProdLot.getSendStatusId();
				preProdLot = tempPreProdLot;
			}
			return retPreProdLot;
		} else
			return null;
	}

	/**
	 * Gets the logger.
	 * 
	 * @return the logger
	 */
	protected Logger getLogger(String ppid) {
		String comp = StringUtils.isBlank(ppid) ? "BuckOnProductionScheduleService"
				: ppid.trim();
		return Logger.getLogger(comp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.honda.galc.service.BuckOnProductionScheduleService#getNextProductSchedule
	 * (com.honda.galc.data.DefaultDataContainer)
	 */
	@Override
	public DataContainer getNextProductSchedule(DefaultDataContainer data) {
		
		return getAFBData(data, true) ;
	}
	
	private DataContainer getAFBData(DefaultDataContainer data, boolean isGetNextProductionLot) {
		
		//assemble output
		DataContainer output = new DefaultDataContainer();
		
		BuckLoadInfoCode infoCode = BuckLoadInfoCode.OK;
		
		String ppid = StringUtils.trim(data.getString(TagNames.PROCESS_POINT_ID.name()));
		
		BuckLoadProductionScheduleServicePropertyBean properties = PropertyService
				.getPropertyBean(
						BuckLoadProductionScheduleServicePropertyBean.class,
						ppid);
		
		String planCode = StringUtils.trim(data.getString(properties.getPlanCodeTag()));
		
		
		String nextProductId = "";
		String kdLot = "";
		String productSpecCode = "";
		String fifCodes = "";
		String afb = "";
		String preProductionLot = "";
		
		setOutput(output, properties, planCode, kdLot, nextProductId, productSpecCode, fifCodes, afb, preProductionLot, isGetNextProductionLot);
		
		
		if (StringUtils.isBlank(ppid)) {
			infoCode = BuckLoadInfoCode.INVALID_REQUEST;
			output.put(ALC_INFO_CODE, infoCode.getInfoCode());
			return output;
		}

		try {

			String productId = null;
			if(isGetNextProductionLot)	productId= StringUtils.trim(data.getString(properties.getLastVINTag()));
			else productId= StringUtils.trim(data.getString(properties.getVINTag()));
			
			
			PreProductionLot nextPreProductionLot = null;
			ProductStampingSequence nextStampingSequence = null;
			
			if (StringUtils.isBlank(planCode)) {
				infoCode = BuckLoadInfoCode.INVALID_REQUEST;
				output.put(properties.getInfoCodeTag(), infoCode.getInfoCode());
				return output;
			}
			
			if (StringUtils.isBlank(productId)) {
				infoCode = BuckLoadInfoCode.INVALID_SN;
				output.put(properties.getInfoCodeTag(), infoCode.getInfoCode());
				return output;
			} else {
				// There is last product id in the request
				if (!validateProductId(properties, productId)) {
					infoCode = BuckLoadInfoCode.INVALID_SN;
					output.put(properties.getInfoCodeTag(), infoCode.getInfoCode());
					return output;
				} else {
					if (!isExpectedProductId(productId, ppid)) {
						infoCode = BuckLoadInfoCode.DUPLICATE_REQUEST;
					}
					if(isGetNextProductionLot)	nextStampingSequence = queryNextProductStampingSequence(productId, planCode);
					else nextStampingSequence = queryProductStampingSequence(productId, planCode);
					if (nextStampingSequence == null) {
						infoCode = BuckLoadInfoCode.NO_SCHEDULE;
						output.put(properties.getInfoCodeTag(), infoCode.getInfoCode());
						return output;
					} else {
						nextPreProductionLot = queryPreProductionLot(nextStampingSequence
								.getId().getProductionLot());
						if(isGetNextProductionLot)	nextProductId = nextStampingSequence.getId().getProductID();
						else	nextProductId = productId;
						productSpecCode = nextPreProductionLot.getProductSpecCode();
						fifCodes = queryFIFCodes(productSpecCode);
						afb = queryAFB(productSpecCode, properties.getAFBAttributeName());
						if(isGetNextProductionLot) updateExpectedProduct(nextProductId, productId, ppid);
						else updateExpectedProduct(nextProductId, null, ppid);
						planCode = nextPreProductionLot.getPlanCode();
						kdLot = nextPreProductionLot.getPreProdLotKdLotNumber(); //get from 212 even if it is null	
						preProductionLot = nextPreProductionLot.getProductionLot();
					}
				}
			}
		} catch (Exception e) {
			//Unknown exception happens.
			infoCode = BuckLoadInfoCode.UNKNOWN_ERROR;
			getLogger(ppid).error(e, "Unknown error.");
		}
		
		setOutput(output, properties, planCode, kdLot, nextProductId, productSpecCode, fifCodes, afb, preProductionLot, isGetNextProductionLot);
		output.put(properties.getInfoCodeTag(), infoCode.getInfoCode());
		return output;
	}
	
	@Override
	public DataContainer getProductSchedule(DefaultDataContainer data) {
		return getAFBData(data, false);
	}
	
	private void setOutput(DataContainer output, BuckLoadProductionScheduleServicePropertyBean properties, 
			String planCode, String kdLot, String nextProductId, String productSpecCode, String fifCodes, String afb, String productionLot,
			boolean isGetNextProductionLot) {
		planCode = planCode == null? "" : StringUtils.trim(planCode);
		kdLot = kdLot == null? "" : StringUtils.trim(kdLot);
		nextProductId = nextProductId == null? "" : StringUtils.trim(nextProductId);
		productSpecCode = productSpecCode == null? "" : StringUtils.trim(productSpecCode);
		fifCodes = fifCodes == null? "" : StringUtils.trim(fifCodes);
		afb = afb == null? "" : StringUtils.trim(afb);
		productionLot = productionLot == null? "" : StringUtils.trim(productionLot);
		output.put(properties.getPlanCodeTag(), planCode);
		output.put(properties.getKDLotTag(), kdLot);
		if(isGetNextProductionLot)  output.put(properties.getNextVINTag(), nextProductId);
		else output.put(properties.getVINTag(), nextProductId);
		output.put(properties.getProductSpecCodeTag(), productSpecCode);
		output.put(properties.getFIFCodesTag(), fifCodes);
		output.put(properties.getAFBTag(), afb);
		output.put(properties.getProductionLotTag(), productionLot);
	}

	/**
	 * Checks if the last product Id is expected product id.
	 * 
	 * @param productId
	 *            the product id
	 * @param ppid
	 *            the process point id
	 * @return true, if the last product Id is expected product id.
	 */
	protected boolean isExpectedProductId(String productId, String ppid) {
		ExpectedProduct ep = expectedProductDao.findForProcessPointAndProduct(
				ppid, productId);
		return ep != null;
	}

	/**
	 * Query afb from product build attributes
	 * 
	 * @param productSpecCode
	 *            the product spec code
	 * @param afbAttributeName
	 *            the afb attribute name
	 * @return the string
	 */
	protected String queryAFB(String productSpecCode, String afbAttributeName) {
		BuildAttributeCache cache = new BuildAttributeCache();
		return cache.findAttributeValue(productSpecCode, afbAttributeName);
	}

	/**
	 * Query fif codes.
	 * 
	 * @return the string
	 */
	protected String queryFIFCodes(String productSpecCode) {
		FrameSpecDao frameSpecDao = ServiceFactory.getDao(FrameSpecDao.class);
		SalesOrderFifDao fifDao = ServiceFactory.getDao(SalesOrderFifDao.class);
		//get the product spec entity
		FrameSpec frameSpec = frameSpecDao.findByKey(productSpecCode);
		//get the fif codes by product spec
		String fifCode = fifDao.getFIFCodeByProductSpec(frameSpec);
		return fifCode == null? "" : fifCode;
	}
	


	/**
	 * Query first product stamping sequence in production lot.
	 *
	 * @param productionLot the production lot
	 * @param index the index the stamping sequence
	 * @return the product stamping sequence
	 */
	protected ProductStampingSequence queryProductStampingSequenceInProductionLot(
			String productionLot, int index) {
		List<ProductStampingSequence> nextStampingSequences = productStampingSequenceDao
				.findAllNext(productionLot, index);
		if (nextStampingSequences == null || nextStampingSequences.isEmpty()) {
			// No product in the production lot
			return null;
		} else {
			return nextStampingSequences.get(0);
		}
	}

	/**
	 * Query next product stamping sequence. <br>
	 * 
	 * Query the stamping sequence(GAL216TBX) for the next VIN(using
	 * GAL216TBX.STAMPING_SEQUENCE_NO) in the same production lot. <br>
	 * <li>If the next VIN in the production lot exists, use the VIN as the next VIN in the
	 * output. 
	 * <li>If the next VIN in the production lot doesn't exist, the VIN is
	 * the last VIN in the production lot. So the service should look for the
	 * next production lot in GAL212TBX. If the next production lot doesn't
	 * exist, error code and message should be returned. If the next production
	 * lot exists, query the stamping sequence(GAL216TBX) for the first VIN in
	 * the next production lot.
	 * 
	 * @param lastProduct
	 *            the last product
	 * @return the product stamping sequence of the next product
	 */

	protected ProductStampingSequence queryNextProductStampingSequence(
			String lastProductId, String planCode) {
		if (lastProductId == null) {
			return null;
		}
		
		// Get PRODUCTION_LOT from GAL216TBX based on PRODUCT_ID. There may be multiple PRODUCTION_LOTs for PMC since PA has own schedule.
		List<ProductStampingSequence> lastStampingSequences = productStampingSequenceDao
				.findAllByProductId(lastProductId);
		if (lastStampingSequences == null || lastStampingSequences.isEmpty()) {
			return null;
		}
		
		// Get PRODUCTION_LOT from GAL212TBX based on PRODUCTION_LOT and PLAN_CODE. There should be only one PRODUCTION_LOT.
		PreProductionLot lastPreProductionLot = null;
		for (ProductStampingSequence lastStampingSequence : lastStampingSequences) {
			PreProductionLot preProductionLot = preProductionLotDao.
					findByKey(lastStampingSequence.getId().getProductionLot());
			if (preProductionLot != null && planCode.equals(preProductionLot.getPlanCode())) {
				lastPreProductionLot = preProductionLot;
				break;
			}
		}
		
		if (lastPreProductionLot == null || lastPreProductionLot.getId() == null) {
			return null;
		}

		// Get next stamping PRODUCT_ID from GAL216TBX based on PRODUCT_ID and PRODUCTION_LOT
		List<ProductStampingSequence> nextStampingSequences = productStampingSequenceDao.
				findAllNext(lastPreProductionLot.getId(), lastProductId);
		ProductStampingSequence nextStampingSequence = null;
		
		// For PMC, one VIN per PRODUCTION_LOT, nextStampingSequences is empty
		// Get next PRODUCTION_LOT from GAL212TBX based on current PRODUCTION_LOT using sequence
		if (nextStampingSequences == null || nextStampingSequences.isEmpty()) {
			// No more product in the production lot. the product id is the last
			// one in the production lot.
			// Look for the next production lot from GAL212TBX
			while (nextStampingSequence == null) {
				PreProductionLot nextPreProductionLot = preProductionLotDao.findNextLotBySequence(lastPreProductionLot);
				if (nextPreProductionLot == null) {
					// No more production lot
					break;
				}
				//query the first stamp sequence
				nextStampingSequence = queryProductStampingSequenceInProductionLot(nextPreProductionLot
						.getProductionLot(), 0);
				if (nextStampingSequence == null) {
					// No product in the production lot,throw an error as there is something wrong with Schedule
					return null;
				}
			}
		} else {
			// There is next product in the stamping sequence.
			nextStampingSequence = nextStampingSequences.get(0);
		}
		return nextStampingSequence;
	}
	
	protected ProductStampingSequence queryProductStampingSequence(String productId, String planCode) {
		
		ProductStampingSequence productStampingSequence = null;
		if (productId == null) {
			return null;
		}
		
		// Get PRODUCTION_LOT from GAL216TBX based on PRODUCT_ID. There may be multiple PRODUCTION_LOTs for PMC since PA has own schedule.
		List<ProductStampingSequence> stampingSequences = productStampingSequenceDao
				.findAllByProductId(productId);
		if (stampingSequences == null || stampingSequences.isEmpty()) {
			return null;
		}
		
		// Get PRODUCTION_LOT from GAL212TBX based on PRODUCTION_LOT and PLAN_CODE. There should be only one PRODUCTION_LOT.
		PreProductionLot ppLot = null;
		for (ProductStampingSequence temp : stampingSequences) {
			PreProductionLot preProductionLot = preProductionLotDao.
					findByKey(temp.getId().getProductionLot());
			if (preProductionLot != null && planCode.equals(preProductionLot.getPlanCode())) {
				productStampingSequence = temp;
				break;
			}
		}
		
		
		return productStampingSequence;
	}

	/**
	 * Query pre production lot.
	 * 
	 * @param productionLot
	 *            the production lot. If the argument of production lot is null,
	 *            the return value is null
	 * @return pre production lot entity
	 */
	protected PreProductionLot queryPreProductionLot(String productionLot) {
		if (StringUtils.isBlank(productionLot)) {
			return null;
		}
		return preProductionLotDao.findByKey(productionLot);
	}

	/**
	 * Update the expected product.
	 * 
	 * @param nextProductId
	 *            the next product id
	 * @param lastProductId
	 *            the last product id
	 * @param ppid
	 *            the ppid process point Id
	 */
	protected void updateExpectedProduct(String nextProductId,
			String lastProductId, String ppid) {
		ExpectedProduct ep = new ExpectedProduct();
		ep.setProcessPointId(ppid);
		ep.setProductId(nextProductId);
		if(StringUtils.isNotEmpty(lastProductId))	ep.setLastProcessedProduct(lastProductId);
		else {
			ExpectedProduct current =  expectedProductDao.findForProcessPoint(ppid);
			ep.setLastProcessedProduct(current.getProductId());
		}
		expectedProductDao.update(ep);
	}

	/**
	 * Validate if the product id is valid
	 * 
	 * @param properties
	 *            the properties
	 * @param productId
	 *            the product id // * @return true, if the product id is valid
	 */
	protected boolean validateProductId(
			BuckLoadProductionScheduleServicePropertyBean properties,
			String productId) {
		if (properties.isValidateProductId()) {
			return ServiceUtil.isProductIdValid(properties.getProductType(),
					productId);
		} else {
			// if we don't validate product id, any product id is regarded as
			// valid.
			return true;
		}
	}
}
