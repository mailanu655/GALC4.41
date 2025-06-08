package com.honda.galc.service.gts.engine;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.ShippingQuorumDao;
import com.honda.galc.dao.product.ShippingQuorumDetailDao;
import com.honda.galc.dao.product.ShippingTrailerInfoDao;
import com.honda.galc.dao.product.ShippingVanningScheduleDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.enumtype.GtsDefectStatus;
import com.honda.galc.entity.enumtype.ShippingQuorumDetailStatus;
import com.honda.galc.entity.enumtype.ShippingTrailerInfoStatus;
import com.honda.galc.entity.gts.GtsIndicator;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsProduct;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ShippingQuorum;
import com.honda.galc.entity.product.ShippingQuorumDetail;
import com.honda.galc.entity.product.ShippingTrailerInfo;
import com.honda.galc.entity.product.ShippingVanningSchedule;
import com.honda.galc.notification.service.IEngineShippingNotification;
import com.honda.galc.property.EngineShippingPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.gts.AbstractBodyTrackingService;
import com.honda.galc.service.gts.GtsEngineBodyTrackingService;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * 
 * <h3>GtsEngineBodyTrackingServiceImpl Class description</h3>
 * <p> GtsEngineBodyTrackingServiceImpl description </p>
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
 * Jun 24, 2015
 *
 *
 */
public class GtsEngineBodyTrackingServiceImpl extends AbstractBodyTrackingService implements GtsEngineBodyTrackingService{

	private final String LOAD_ENGINE = "LOAD-ENGINE";
    private final String LANE_Z16 = "Z16";
    private final String RDR_Z16 = "RDR-Z16";
   
	@Autowired
	ShippingQuorumDao shippingQuorumDao;
	
	@Autowired
	ShippingQuorumDetailDao shippingQuorumDetailDao;
	
	@Autowired
	ShippingTrailerInfoDao shippingTrailerInfoDao;
	
	@Autowired
	ShippingVanningScheduleDao shippingVanningScheduleDao;
	
	@Autowired
	EngineDao engineDao;
	
	@Override
	public void setDecisionController() {
		this.decisionController = new EngineShippingDecisionController(this);
	}
	
	public String getShippingTrailerNumber(int trailerId) {
		if(trailerId <=0) return "";
		ShippingTrailerInfo trailerInfo = shippingTrailerInfoDao.findByKey(trailerId);
		return trailerInfo == null ? "" : trailerInfo.getTrailerNumber();
	}
	
	/**
     *  find the active shipping quorum
     * @return
     */
    
    public ShippingQuorum getActiveShippingQuorum(){
        return shippingQuorumDao.findCurrentShippingQuorum();
    }

    
    public void updateShippingQuorumStatus(ShippingQuorum quorum) {
    	shippingQuorumDao.save(quorum);
    	
    	ServiceFactory.getNotificationService(IEngineShippingNotification.class)
    		.shippingQuorumUpdated(quorum);
    }
    
    public void updateProductSequences(List<GtsProduct> products){

    	productDao.updateAll(products);
        
        getNotificationService().productStatusChanged(products);
        
    }
    
    public boolean isValidModelCode(String modelCode, String palletType) {
    	Map<String,String> palletTypes = getEngineShippingPropertyBean().getPalletMap();
    	if(palletTypes == null || palletTypes.isEmpty()) return true;
    	return palletTypes.containsKey(modelCode) && StringUtils.equalsIgnoreCase(palletTypes.get(modelCode),palletType);
    }
    
    public EngineShippingPropertyBean getEngineShippingPropertyBean() {
    	String ppid = getShippingProcessPointId();
		if(StringUtils.isEmpty(ppid)) throw new TaskException("Property SHIPPING_PPID is not configured");
		
    	return PropertyService.getPropertyBean(EngineShippingPropertyBean.class, ppid);
	}
    
    
    @Override
    public void receiveReader(GtsIndicator newIndicator) {
	    // TODO Auto-generated method stub
	    super.receiveReader(newIndicator);

	    if (newIndicator.getIndicatorName().equalsIgnoreCase(RDR_Z16)) {
	        sendPdsiSync();
	    }
    }
    
	private void sendPdsiSync() {
	    
        // send engine/quorum information to PDSI
        try {

        	ShippingQuorum currentQuorum = getActiveShippingQuorum();
        	if(currentQuorum == null) {
        		getLogger().error("Could not send quorum data to PDSI PLC - no active quorum available");
        		return;
        	}
        	
        	String trailerNumber = getShippingTrailerNumber(currentQuorum.getTrailerId()); 
        	
        	ShippingQuorumDetail currentQuorumDetail = currentQuorum.getNextQuorumDetail();
        	
        	if(currentQuorumDetail == null) {
        		logErrorAndNotify("Could not send quorum data to PDSI PLC - no quorum detail available");
        		return;
        	}
            
            DataContainer dc = new DefaultDataContainer();
            
    		dc.put("TRAILER_NUMBER", StringUtils.substring(trailerNumber, trailerNumber.length()> 6 ? trailerNumber.length()-6 : 0));
    		dc.put("ROW_NUMBER", Integer.toString(currentQuorum.getTrailerRow()));
    		dc.put("QUORUM_SIZE", Integer.toString(currentQuorum.getQuorumSize()));
    		dc.put("MODEL_TYPE", currentQuorum.getPalletType());
    		dc.put("QUORUM_TYPE", currentQuorum.getQuorumType());
    		dc.put("SEQUENCE_NUMBER", currentQuorumDetail.getQuorumSeq());
    
            // send quorum information to PDSI PLC
    		sendToPLC("ESTS.SEQUENCE-SYNC", dc);
    
    		getLogger().info("Sent quorum information to PDSI PLC - ShippingQuorum: " + currentQuorum.toString() + " - ShippingQuorumDetail: " + currentQuorumDetail.toString());
    		
        } catch (Exception e) {
        	getLogger().error(e,"Could not send data to PDSI PLC - ");
     	}
    
    }
	
	@Override
    public void receiveSpecialRequest(GtsIndicator indicator) {
    	 super.receiveSpecialRequest(indicator);
         
         if(indicator.getIndicatorName().equalsIgnoreCase(LOAD_ENGINE) && indicator.isStatusOn()) {
             
             List<GtsLaneCarrier> laneCarriers = laneCarrierDao.findAll(getAreaName(),LANE_Z16,getCarrierAreaName());
             
             if(laneCarriers.size() == 0) {
                 
                 // no engine at lane Z16
            	 logErrorAndNotify("No engine at lane Z16");
                 
                 
             }else {
                 
                 GtsLaneCarrier lc = laneCarriers.get(0);
                 
                 if(lc.getProductId() == null || lc.getProductId() == "") return;
                 
                 GtsProduct product = lc.getProduct();
                 
                 if(product.getProductSeq() == 0) {
                     
                     // unallocated engine comes to the drop lift --- emergency - something wrong happened
                	 logErrorAndNotify("Engine number " + lc.getProductId() + " is not an allocated engine!");
                     
                 }
                 
                 // send engine information to shipping bean
                 try {
                     
                     this.loadEngineProcess(lc.getProductId(),product.getProductSpec());
                 
                 } catch (Exception e) {
                     getLogger().error(e,"Engine number: " + lc.getProductId() + " Cause: " + e.getMessage());
                 }
                 
                 // deassociate carrier with product 
                 deassociateCarrierWithProduct(lc.getCarrier());
             }
         }
     
    }
    
    @Transactional
    public void loadEngineProcess(String engineNumber, String ymto) {
		
        ShippingQuorum quorum = getActiveShippingQuorum();
        
		
        ShippingQuorumDetail detail = quorum.loadEngine(engineNumber, ymto);
        
        if(detail == null) {
        	getLogger().error("Could not find shipping quorum detail for engine " + engineNumber);
        	return;
        }
        
        if(detail.getStatus() == ShippingQuorumDetailStatus.MISS_LOAD)
            getLogger().error("Misloading occurred, engine number:"+engineNumber + " actual YMTO:"+ymto);

        // update quorum detail
		shippingQuorumDetailDao.update(detail);
        
		getLogger().info("Engine Number:"+engineNumber + ", YMTO:"+ymto+" has been loaded into quorum id:"+quorum.getId().getQuorumId()+
					", Quorum Date:"+quorum.getId().getQuorumDate());
        
		if(quorum.getTrailerId() != ShippingTrailerInfo.TRAILER_ID_EXCEPT && quorum.getTrailerId() != ShippingTrailerInfo.TRAILER_ID_REPAIR){
			
           
			
			updateTrailerInfo(quorum.getTrailerId());
			
            //update vanning schedule actual qty based on the KD lot and trailer ID

            ShippingVanningSchedule vanningSchedule = getDao(ShippingVanningScheduleDao.class).findIncompleteSchedule(quorum.getTrailerId(), detail.getKdLot());
            
            if(vanningSchedule != null && vanningSchedule.getActQty() < vanningSchedule.getSchQty()) {
            	vanningSchedule.setActQty(vanningSchedule.getActQty() + 1);
            	shippingVanningScheduleDao.update(vanningSchedule);
            }
		}

		shippingQuorumDao.update(quorum);
		
		ServiceFactory.getNotificationService(IEngineShippingNotification.class).engineLoaded(quorum.getTrailerId(),detail);
			
    }
    
    private void updateTrailerInfo(int trailerId) {
    	ShippingTrailerInfo currentTrailerInfo = shippingTrailerInfoDao.findByKey(trailerId);
        
        
        // update trailerInfo actural quantity. also update staus to LOADING
    	currentTrailerInfo.loadEngine();
        
        //   update trailer info table for the status and actual qty.
        shippingTrailerInfoDao.update(currentTrailerInfo);

    	List<ShippingTrailerInfo> shippingTrailerInfoList = shippingTrailerInfoDao.findAllShippingTrailers();
		for(ShippingTrailerInfo trailerInfo : shippingTrailerInfoList) {
			if(trailerInfo.getStatus().equals(ShippingTrailerInfoStatus.LOADING) &&
			   trailerInfo.getTrailerId() != trailerId) {
				trailerInfo.setStatus(ShippingTrailerInfoStatus.HOLD);
				getDao(ShippingTrailerInfoDao.class).save(trailerInfo);
			}
		}
	}
    
   	@Override
	public String getAreaName() {
		return "ESTS";
	}

	@Override
	public String getTrackingServiceInterfaceName() {
		return "GtsEngineBodyTrackingService";
	}
	
	private String getShippingProcessPointId() {
		return getProperty("SHIPPING_PROCESS_POINT_ID");
	}
	
	private String getShipableProcessPointId() {
		return getProperty("SHIPABLE_PROCESS_POINT_ID");
	}

	@Override
	public Product getProduct(String productId) {
		return engineDao.findByKey(productId);
	}

	@Override
	public GtsDefectStatus getDefectStatus(Product product) {
		GtsDefectStatus defectStatus = 
	    		product.getLastPassingProcessPointId().equals(getShipableProcessPointId())? GtsDefectStatus.DIRECT_PASS :GtsDefectStatus.OUTSTANDING;
	    	return defectStatus;
	}
   
}
