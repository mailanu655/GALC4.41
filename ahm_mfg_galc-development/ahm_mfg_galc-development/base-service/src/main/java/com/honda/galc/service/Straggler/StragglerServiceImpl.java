package com.honda.galc.service.Straggler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.StragglerDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.conf.ComponentStatusId;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.StragglerStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.Straggler;
import com.honda.galc.entity.product.StragglerId;
import com.honda.galc.mail.MailContext;
import com.honda.galc.mail.MailSender;
import com.honda.galc.property.FrameLinePropertyBean;
import com.honda.galc.property.SmtpMailPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.StragglerService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

/*
* 
* @author Gangadhararao Gadde 
* @since May 19, 2014
*/
public class StragglerServiceImpl implements StragglerService {

	protected DataContainer retList = new DefaultDataContainer();
	
	private static final String KD_LOT_NUMBER = "KD_LOT_NUMBER";
	private static final String LOT_NUMBER_LIST = "LOT_NUMBER_LIST";
	private static final String CURRENT_SPEC_CODE = "CURRENT_SPEC_CODE";
	private static final String STRAGGLER_GROUPING = "STRAGGLER_GROUPING";
	private static final String REASON_CODE_AUTO_UPDATE_PROCESS_POINT = "REASON_CODE_AUTO_UPDATE_PROCESS_POINT";
	
	public DataContainer identifyStragglers(ProcessPoint processPoint, BaseProduct product)
	{
		DataContainer dataContainer = new DefaultDataContainer();
		dataContainer = identifyStragglersByLot(processPoint, product,"LOT");
		if(isStragglerGrouping(processPoint.getProcessPointId())){
			dataContainer =  identifyStragglersByProductSpec(processPoint, product,"MTOCI");
		}
		
		return dataContainer;
	}

	
	private DataContainer identifyStragglersByProductSpec(ProcessPoint processPoint, BaseProduct product,String stragglerType) {
		
		ComponentStatus componentStatusList = ServiceFactory.getDao(ComponentStatusDao.class).findByKey(processPoint.getProcessPointId(), LOT_NUMBER_LIST);
		ComponentStatus componentSpecCode = ServiceFactory.getDao(ComponentStatusDao.class).findByKey(processPoint.getProcessPointId(), CURRENT_SPEC_CODE);
		
		ComponentStatusId cpLotList = new ComponentStatusId(processPoint.getProcessPointId(), LOT_NUMBER_LIST);
		ComponentStatusId cpSpecCodeId = new ComponentStatusId(processPoint.getProcessPointId(), CURRENT_SPEC_CODE);
		
		String productProdLot=product.getProductionLot();
		if(StringUtils.equalsIgnoreCase( getFrameLinePropertyBean().getStragglerLotType(), KD_LOT_NUMBER)){
			Product prd = (Product)product;
			productProdLot=prd.getKdLotNumber();
		}
		
		if(componentStatusList != null && componentSpecCode != null){
			List<String> lotList = new ArrayList<String>(); 
			if(StringUtils.isNotBlank(componentStatusList.getStatusValue())) {
				String[] nameList = componentStatusList.getStatusValue().split(Delimiter.COMMA);
				for(String name : nameList) {
					lotList.add(name);
				}
			}
			if(!StringUtils.equals(componentSpecCode.getStatusValue(), product.getProductSpecCode())) {
				for(String lotNumber : lotList) {
					updateStragglers(processPoint, product, lotNumber,stragglerType);
				}
				updateComponentStatus(cpSpecCodeId, product.getProductSpecCode());
				updateComponentStatus(cpLotList, productProdLot);
			} else {
				if(!lotList.contains(productProdLot)) {
					lotList.add(productProdLot);
					updateComponentStatus(cpLotList, StringUtils.join(lotList, Delimiter.COMMA));
				}
			}
		} else {
			updateComponentStatus(cpSpecCodeId, product.getProductSpecCode());
			updateComponentStatus(cpLotList, productProdLot);
		}
		return retList;
	}
	
	private void updateComponentStatus(ComponentStatusId id, String statusValue){
		ComponentStatus cpStatus = new ComponentStatus();
		cpStatus.setId(id);
		cpStatus.setStatusValue(statusValue);
		ServiceFactory.getDao(ComponentStatusDao.class).save(cpStatus);
	}
	
	/**
	 * @param processPoint
	 * @param product
	 * @param ppCrntProdLot
	 * @param KD_LOT_NUMBER
	 * @param kdLotIndicator
	 */
	private void updateStragglers(ProcessPoint processPoint, BaseProduct product, String ppCrntProdLot,String stragglerType) {
		Division division = ServiceFactory.getDao(DivisionDao.class).findByKey(processPoint.getDivisionId());
		List<Object[]> prevUnprocessedLotList = new ArrayList<Object[]>();
		if(StringUtils.equalsIgnoreCase(getFrameLinePropertyBean().getStragglerLotType(), KD_LOT_NUMBER)){
			prevUnprocessedLotList=ServiceFactory.getDao(StragglerDao.class).findPrevUnProcessedLotsByKDLot(division.getPlantName(), processPoint.getProcessPointId(), ppCrntProdLot, stragglerType);
		} else {
			prevUnprocessedLotList=ServiceFactory.getDao(StragglerDao.class).findPrevUnProcessedLots(division.getPlantName(), processPoint.getProcessPointId(), ppCrntProdLot,stragglerType);					
		}
		String reasonCodeAutoUpdateProcessPoint = getReasonCodeAutoUpdateProcessPoint(processPoint.getProcessPointId());
		for (Object[] prevProductData:prevUnprocessedLotList) {
			StragglerId id=new StragglerId();
			id.setProductId((String)prevProductData[0]);
			id.setPpDelayedAt(processPoint.getProcessPointId());
			id.setStragglerType(stragglerType);
			Straggler stragglerProduct=new Straggler();
			stragglerProduct.setId(id);
			stragglerProduct.setLastPassingProcessPoint((String)prevProductData[2]);
			stragglerProduct.setUnitsBehind(Integer.valueOf((prevProductData[3]).toString()));
			stragglerProduct.setIdentifiedTimestamp((Timestamp)prevProductData[1]);
			
			if (StringUtils.isNotEmpty(reasonCodeAutoUpdateProcessPoint)) {
				Straggler reasonCodeAutoUpdateStraggler = ServiceFactory.getDao(StragglerDao.class).findByKey(new StragglerId(id.getProductId(), reasonCodeAutoUpdateProcessPoint,stragglerType));
				if (reasonCodeAutoUpdateStraggler != null) {
					stragglerProduct.setCode(reasonCodeAutoUpdateStraggler.getCode());
				}
			}
			ServiceFactory.getDao(StragglerDao.class).save(stragglerProduct);
			setStragglerStatus(StragglerStatus.CREATED);
			getLogger(processPoint.getProcessPointId()).info("Successfully inserted Straggler:"+stragglerProduct.toString());
		}
		
			if(!processStraggler(processPoint, product)){ // if the product is not a straggler for the process point, update the process point's current lot to match the product
				if(StringUtils.equalsIgnoreCase(getFrameLinePropertyBean().getStragglerLotType(),KD_LOT_NUMBER)){
					Product prd = (Product)product;
					processPoint.setCurrentKdLot(prd.getKdLotNumber());
				} else {
					processPoint.setCurrentProductionLot(product.getProductionLot());					
				}
				ServiceFactory.getDao(ProcessPointDao.class).save(processPoint);
				getLogger(processPoint.getProcessPointId()).info("Successfully updated the processpoint:"+processPoint.toString());
			}
			sendStragglerEmail(processPoint.getProcessPointId(), processPoint.getProcessPointName(), prevUnprocessedLotList,stragglerType);
				
	}

	/**
	 * Updates the product's Straggler records for the processPoint.<br>
	 * Returns true iff the product is a straggler for the processPoint.
	 */
	private boolean processStraggler(ProcessPoint processPoint, BaseProduct product) {
		List<Straggler> currentProductstragglerList=ServiceFactory.getDao(StragglerDao.class).findStragglerProductList(product.getProductId(), processPoint.getProcessPointId());	
		if (currentProductstragglerList.size() > 0) { // if the product is a straggler for the process point, mark it as processed
			java.util.Date currentDate= new java.util.Date();
			Timestamp currentTimestamp=new Timestamp(currentDate.getTime());
			for (Straggler stragglerProduct:currentProductstragglerList) {
				int processedCount=ServiceFactory.getDao(ProductResultDao.class).findAlreadyProcessedProdAtPP(processPoint.getProcessPointId(), stragglerProduct.getIdentifiedTimestamp().toString());
				stragglerProduct.setActualUnitsBehind(processedCount);
				stragglerProduct.setActualProcessTimestamp(currentTimestamp);
				ServiceFactory.getDao(StragglerDao.class).save(stragglerProduct);
				setStragglerStatus(StragglerStatus.RELEASE);
				getLogger(processPoint.getProcessPointId()).info("Successfully updated Straggler: "+stragglerProduct.toString());
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param processPoint
	 * @param product
	 * @return
	 */
	private DataContainer identifyStragglersByLot(ProcessPoint processPoint, BaseProduct product,String stragglerType) {
		try {
			retList.clear();
			setStragglerStatus(StragglerStatus.NO_ACTION);
			getLogger(processPoint.getProcessPointId()).info("Straggler service started for product:"+product.getProductId());
			String ppCrntProdLot=processPoint.getCurrentProductionLot();
			String productProdLot=product.getProductionLot();
			
			String kdLotIndicator = getFrameLinePropertyBean().getStragglerLotType();
			
			if(StringUtils.equalsIgnoreCase(kdLotIndicator,KD_LOT_NUMBER)){
				ppCrntProdLot=processPoint.getCurrentKdLot();
				Product prd = (Product)product;
				productProdLot=prd.getKdLotNumber();
			} 			
			
			if(StringUtils.isBlank(productProdLot)) {
				getLogger(processPoint.getProcessPointId()).error("Production Lot not found for product:"+product.getProductId());
				return retList;
			}			
			if(StringUtils.isBlank(ppCrntProdLot))	{
				if(StringUtils.equalsIgnoreCase(kdLotIndicator,KD_LOT_NUMBER)){
					Product prd = (Product)product;
					processPoint.setCurrentKdLot(prd.getKdLotNumber());
				} else {
					processPoint.setCurrentProductionLot(product.getProductionLot());					
				}
				ServiceFactory.getDao(ProcessPointDao.class).save(processPoint);
			}else{	
				if( !ppCrntProdLot.trim().equals(productProdLot.trim())){
					updateStragglers(processPoint, product, ppCrntProdLot,stragglerType);
				} else {
					processStraggler(processPoint, product); // update the product's Straggler record for the process point
				}
			}
			getLogger(processPoint.getProcessPointId()).info("Straggler service finished for product:"+product.getProductId());
		} catch (Exception e) {
			getLogger(processPoint.getProcessPointId()).error("Straggler service failed for product:"+product.getProductId()+e.getMessage());
			e.printStackTrace();
		}
		return retList;
	}

	public DataContainer identifyStragglers(String processPointID, String productId, String ProductType)
	{
		ProcessPoint processPoint = new ProcessPoint();
		processPoint = ServiceFactory.getDao(ProcessPointDao.class).findById(processPointID);
		
		BaseProduct product = ProductTypeUtil.getProductDao(ProductType).findByKey(productId);
		identifyStragglers(processPoint, product);
		return retList;
	}
	
	private StragglerStatus getStragglerStatus() {
		Integer status = (Integer) retList.get(TagNames.STRAGGLER_STATUS);
		return status == null ? null : StragglerStatus.getType(status);
	}
	
	private void setStragglerStatus(StragglerStatus newStragglerStatus) {
		int status;
		if ((newStragglerStatus == StragglerStatus.CREATED && getStragglerStatus() == StragglerStatus.RELEASE)
				|| (newStragglerStatus == StragglerStatus.RELEASE && getStragglerStatus() == StragglerStatus.CREATED)) {
			status = StragglerStatus.CREATED_AND_RELEASE.getId();
		} else {
			status = newStragglerStatus.getId();
		}
		retList.put(TagNames.STRAGGLER_STATUS, status);
	}
	
	private Logger getLogger(String processPointId) {
		return Logger.getLogger(PropertyService.getLoggerName(processPointId));
	}
	
	protected FrameLinePropertyBean getFrameLinePropertyBean() {
		return PropertyService.getPropertyBean(FrameLinePropertyBean.class);
	}
	
	protected boolean isStragglerGrouping(String componentId) {
		return PropertyService.getPropertyBoolean(componentId, STRAGGLER_GROUPING, false);
	}
	
	protected String getReasonCodeAutoUpdateProcessPoint(String componentId) {
		return PropertyService.getProperty(componentId, REASON_CODE_AUTO_UPDATE_PROCESS_POINT, "");
	}

	private void sendStragglerEmail(String processPointId, String processPointName, List<Object[]> prevUnprocessedLotList,String stragglerType) {
		SmtpMailPropertyBean mailPropertyBean = PropertyService.getPropertyBean(SmtpMailPropertyBean.class, processPointId);
		if (!mailPropertyBean.isStragglerMailEnabled() || prevUnprocessedLotList.size() <= 0) {
			return;
		}
		try {
			final MailContext mailContext = new MailContext();
			final StringBuilder messageBuilder = new StringBuilder(prevUnprocessedLotList.size() > 1 ? "The following stragglers were created at " : "The following straggler was created at ");
			messageBuilder.append(processPointName);
			messageBuilder.append(" (");
			messageBuilder.append(processPointId);
			messageBuilder.append("):");
			for (int i = 0; i < prevUnprocessedLotList.size(); i++) {
				String productId = (String) (prevUnprocessedLotList.get(i)[0]);
				String reasonCode = null; {
					Straggler straggler = ServiceFactory.getDao(StragglerDao.class).findByKey(new StragglerId(productId, processPointId, stragglerType));
					if (straggler != null) {
						reasonCode = straggler.getCode();
					}
				}
				messageBuilder.append("\n" + productId + (reasonCode == null ? "" : " (" + reasonCode + ")"));
			}
			mailContext.setMessage(messageBuilder.toString());
			mailContext.setSubject("Straggler creation at " + processPointName + " (" + processPointId + ")");
			mailContext.setHost(mailPropertyBean.getStragglerMailHost());
			mailContext.setSender(mailPropertyBean.getStragglerMailSender());
			mailContext.setRecipients(mailPropertyBean.getStragglerMailRecipients());
			mailContext.setTimeout(mailPropertyBean.getConnectionTimeout() * 1000);
			MailSender.sendAsync(mailContext); 
		} catch(Exception e) {
			getLogger(processPointId).error(e, "Unable to send straggler mail notification");
		}
	}

}
