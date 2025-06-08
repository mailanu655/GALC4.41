package com.honda.galc.util;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.constant.EngineManifestPlant;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.EngineFiringResultDao;
import com.honda.galc.data.ProductSpecCodeDef;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.EngineFiringResult;
import com.honda.galc.entity.product.EngineManifest;
import com.honda.galc.entity.product.ShippingQuorumDetail;
import com.honda.galc.property.EngineShippingPropertyBean;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
/**
 * 
 * 
 * <h3>EngineShippintHelper Class description</h3>
 * <p> EngineShippintHelper description </p>
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
 * @author Paul Chou<br>
 * Apr. 10, 2017
 *
 *
 */
public class EngineShippingHelper {
	
	private EngineFiringResultDao engineFiringResultDao;
	private EngineShippingPropertyBean propertyBean;
	private EngineDao engineDao;
	
	public EngineShippingHelper(EngineShippingPropertyBean propertyBean) {
		this.propertyBean = propertyBean;
	}


	public EngineManifest createEngineManifest(ShippingQuorumDetail detail, String engineSource) {
		Engine engine = getEngineDao().findByKey(detail.getEngineNumber());
		EngineManifest em = new EngineManifest();
		em.getId().setEngineNo(detail.getEngineNumber());
		em.getId().setPlant(getPlant(detail));
		em.setCompany(detail.getKdLot().substring(0, 3));
		em.setEngineKdLot(getEngineKdLot(detail));
		em.setModelYearCode(ProductSpecCodeDef.YEAR.getValue(engine.getProductSpecCode()));
		em.setEngineModel(ProductSpecCodeDef.MODEL.getValue(engine.getProductSpecCode()));
		em.setEngineType(ProductSpecCodeDef.TYPE.getValue(engine.getProductSpecCode()));
		em.setEngineOption(ProductSpecCodeDef.OPTION.getValue(engine.getProductSpecCode()));
		em.setMissionNo(engine.getMissionSerialNo());
		em.setEngineFiredInd(getEngineFiredIndicator(detail.getEngineNumber()));
		em.setEngineSource(engineSource);
		
		return em;
	}
	

	private EngineDao getEngineDao() {
		if(engineDao == null)
			engineDao = ServiceFactory.getDao(EngineDao.class);
		
		return engineDao;
	}


	private String getEngineFiredIndicator(String ein) {
		List<EngineFiringResult> firingResultList = getEngineFiringResultDao().findAllByProductId(ein);
		return (firingResultList != null && firingResultList.size() > 0 ) ? "F" : "N";
	}

	private String getPlant(ShippingQuorumDetail detail) {
		EngineManifestPlant emp = EngineManifestPlant.getById(Integer.parseInt(detail.getKdLot().substring(4,6)));
		
		return emp.getPlant();
	}

	private String getEngineKdLot(ShippingQuorumDetail detail) {
		StringBuffer sb = new StringBuffer();
		Map<String, String> shippingPlantsMap = propertyBean.getShippingPlants();
		String plantStr = detail.getKdLot().substring(0, 6);
		sb.append(shippingPlantsMap.keySet().contains(plantStr) ? shippingPlantsMap.get(plantStr) : "3");
		sb.append(detail.getKdLot().substring(8, 12));
		sb.append(detail.getKdLot().substring(13, 16));
		sb.append(detail.getKdLot().substring(17, 18));
		return sb.toString();
	}
	
	public EngineFiringResultDao getEngineFiringResultDao() {
		if(engineFiringResultDao == null)
			engineFiringResultDao = ServiceFactory.getDao(EngineFiringResultDao.class);
		
		return engineFiringResultDao;
	}

    public void invokeBroadcast(String engineNo,String kdLotNumber,String shippingProcessPointId) {
    	Integer broadcastSeq = getBroadcastSeq(engineNo,kdLotNumber);
    	if(broadcastSeq == null || broadcastSeq <= 0) return ;
    	BroadcastService broadcastService = ServiceFactory.getService(BroadcastService.class);
    	broadcastService.asynBroadcast(shippingProcessPointId, broadcastSeq, engineNo);
    }
    
    private Integer getBroadcastSeq(String engineNumber,String kdLotNumber) {
    	//first digit is plant number
    	String plant = StringUtils.substring(kdLotNumber,0, 1);
    	Map<String,Integer> broadcastSeqMap = propertyBean.getBroadcastSeq(Integer.class);
    	return broadcastSeqMap == null ? null : broadcastSeqMap.get(plant);
    }
    
  

}
