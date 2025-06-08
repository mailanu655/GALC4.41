package com.honda.galc.oif.task;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.PartShipmentDao;
import com.honda.galc.dao.product.PartShipmentProductDao;
import com.honda.galc.entity.enumtype.BuildStatus;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.PartShipment;
import com.honda.galc.entity.product.PartShipmentProduct;
import com.honda.galc.entity.product.PartShipmentProductId;
import com.honda.galc.oif.dto.PartShipmentProductDTO;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.ToStringUtil;

public class PartShippingTask extends OifTask<Object> implements IEventTaskExecutable {

	
	private static String BUILD_SITE = "BUILD_SITE";
	private static String PARTIAL_BUILD = "PARTIAL_BUILD";
	private static String SHIPPING_DESTINATION = "SHIPPING_DEST";
	
	private String receivingSite = getProperty(SHIPPING_DESTINATION);
	private String buildSite = getProperty(BUILD_SITE);
	private Map<String, String> partialBuildMap= PropertyService.getPropertyMap(getComponentId(),PARTIAL_BUILD);
	
	public PartShippingTask(String name) {
		super(name);
	}

	@Override
	public void execute(Object[] args) {
		try{
		Timestamp startTs = getStartTime();
		String lastUpdate = startTs.toString();
		Timestamp endTs = getEndTime(startTs);
		
		logger.info("executing Part Shipping task for Build Site : "+buildSite +", Receiving Site : "+receivingSite);

		//The part shipping job will retrieve all of the unsent shipments in PART_SHIPMENT_TBX for receiving Site  
		//For each shipment the job will retrieve all of the scanned products, 
		//retrieve the MbpnProduct entity and append the entity values and build status to a manifest file to be sent to the receiving site.  
		//Once all of the unsent shipments for a site have been added to the manifest file the file will be sent via MQ to the receiving site.  
		//The manifest will only be a list of MBPN products to be sent and will not contain any part data.
		if(receivingSite.equalsIgnoreCase(buildSite)) return;
		
			List<PartShipment> unsentShipments = getPartShipmentDao().findAllUnSentShipmentsForReceivingSite(receivingSite);
			
			if(unsentShipments.isEmpty()){
				logger.info("No unsent Shipments for Receiving Site : "+receivingSite);
				return;
			}
			HashMap<String, PartShipmentProductDTO> resultMap = new LinkedHashMap<String, PartShipmentProductDTO>();
			for(PartShipment partShipment:unsentShipments){
				List<MbpnProduct> products = getPartShipmentProductDao().findAllByShipmentId(partShipment.getShipmentId());
				for(MbpnProduct product:products){
					PartShipmentProduct partShipmentProduct = getPartShipmentProduct(partShipment.getShipmentId(), product.getProductId());
					PartShipmentProductDTO dto = new PartShipmentProductDTO();
					dto.setReceivingSite(receivingSite);
					dto.setProductId(product.getProductId());
					dto.setProductSpecCode(product.getCurrentProductSpecCode());
					dto.setOrderNumber(product.getCurrentOrderNo());
					
					if(partShipmentProduct.getBuildStatus() == null){
						if(getBuildStatus(product.getProductId()).equalsIgnoreCase(BuildStatus.FINAL.getName())){
							logger.info("setting Build Status to FINAL for productId : "+partShipmentProduct.getId().getProductId());
							partShipmentProduct.setBuildStatus(BuildStatus.FINAL.getId());
						}else{
							partShipmentProduct.setBuildStatus(BuildStatus.PARTIAL.getId());
						}
						partShipmentProduct = getPartShipmentProductDao().save(partShipmentProduct);
					}
					if(product.isScrapStatus() && !partShipmentProduct.getBuildStatus().equals(BuildStatus.REMOVE.getId())){
						logger.info("setting Build Status to SCRAP for productId : "+partShipmentProduct.getId().getProductId());
						dto.setBuildStatus(BuildStatus.SCRAP.getName());
						//mark scrap
					}else{
						if(partShipmentProduct.getBuildStatus()!= null && partShipmentProduct.getBuildStatus().equals(BuildStatus.REMOVE.getId())){
							logger.info("setting Build Status to REMOVE for productId : "+partShipmentProduct.getId().getProductId());
							dto.setBuildStatus(BuildStatus.REMOVE.getName());
						}else{
							dto.setBuildStatus(getBuildStatus(product.getProductId()));
						}
					}
					logger.info("Adding data for product Id - "+product.getProductId()+" data ("+ ToStringUtil.generateToString(dto)+")");		
					resultMap.put(product.getProductId(), dto);
				}
					
			}
			
			if(errorsCollector.getErrorList().isEmpty()){	
				updateLastProcessTimestamp(endTs);
				//export data
				if (resultMap.size() > 0) {
					logger.info("Exporting PartShipment Data");
					exportDataByOutputFormatHelper(PartShipmentProductDTO.class, new ArrayList<PartShipmentProductDTO>(resultMap.values()));
					//update sendStatus to sent
					for(PartShipment partShipment:unsentShipments){
						partShipment.setSendStatus(2);
						partShipment.setSentTimestamp(new Date(System.currentTimeMillis()));
						partShipment = getPartShipmentDao().save(partShipment);
						logger.info("update Sent Status to 2 for shipment : "+partShipment.toString());
					}
				}
			}
		
		logger.info("Finished PartShipment Task");

		} catch (Exception e) {
			logger.info("Unexpected Exception Occurred  while running the PartShipment  Task :"
					+ e.getMessage());
				e.printStackTrace();
				errorsCollector.emergency(e, "Unexpected exception occured");
		} finally {
				errorsCollector.sendEmail();
		}
		
	}

	private PartShipmentProduct getPartShipmentProduct(Integer shipmentId, String productId) {
		PartShipmentProductId id = new PartShipmentProductId();
		id.setProductId(productId);
		id.setShipmentId(shipmentId);
		
		return getPartShipmentProductDao().findByKey(id);
	}

	private String getBuildStatus(String productId) {
		String buildStatus = BuildStatus.FINAL.getName();
		MbpnProduct product = getMbpnProductDao().findBySn(productId);
		if(product!= null){
			String lastPassingProcessPointId = product.getLastPassingProcessPointId();
			for(String key: partialBuildMap.keySet()){
				String processPointString = partialBuildMap.get(key);
				List<String> processPointList = Arrays.asList(processPointString.split(","));
				if(processPointList.contains(lastPassingProcessPointId)){
					buildStatus =  key;
					logger.info("setting Build Status to "+buildStatus +" for productId : "+productId);
					break;
					
				}
			}
		}
		return buildStatus;
	}

	private PartShipmentProductDao getPartShipmentProductDao(){
		return ServiceFactory.getDao(PartShipmentProductDao.class);
	}
	
	private PartShipmentDao getPartShipmentDao(){
		return ServiceFactory.getDao(PartShipmentDao.class);
	}
	
	private MbpnProductDao getMbpnProductDao(){
		return ServiceFactory.getDao(MbpnProductDao.class);
	}
	
	
}
