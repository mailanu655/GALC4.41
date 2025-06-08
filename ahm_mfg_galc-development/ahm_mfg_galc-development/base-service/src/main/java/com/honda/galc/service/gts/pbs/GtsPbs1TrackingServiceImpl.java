package com.honda.galc.service.gts.pbs;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.enumtype.GtsCarrierType;
import com.honda.galc.entity.enumtype.GtsIndicatorType;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.gts.GtsCarrierId;
import com.honda.galc.entity.gts.GtsIndicator;
import com.honda.galc.entity.gts.GtsIndicatorId;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsNode;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.notification.GtsNotificationSender;
import com.honda.galc.service.gts.AbstractBodyTrackingService;
import com.honda.galc.service.gts.GtsPbs1TrackingService;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * 
 * <h3>GtsPaint1BodayTrackingServiceImpl Class description</h3>
 * <p> GtsPaint1BodayTrackingServiceImpl description </p>
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
 * @author is08925<br>
 * Nov 30, 2017
 *
 *
 */
public class GtsPbs1TrackingServiceImpl extends AbstractBodyTrackingService implements GtsPbs1TrackingService{
	
	private static String ENTRY_pT = "ENTRY-pT";
	private static String GS_ENTRY_pT = "GS-ENTRY-pT";
	private static String ENTRY_pR = "ENTRY-pR";
	private static String GS_ENTRY_pR = "GS-ENTRY-pR";
	private static String EXIT_pR = "EXIT-pR";
	private static String LF_pR = "LF-pR";
	
	private final String LOAD_VEHICLE = "LOAD-VEHICLE";
	
	private final String MP_pJ_pP = "MP-pJ-pP";
	
	private static String CHANGE_CONTROL_INTO_PR = "ChangeControlIntoPR";
	
	private static String LANE_pR = "pR";
	private static String LANE_pJ = "pJ";
	
	
	private static String CARRIER_MISS_READ = "FFFF";
	private static String PAINT_ON_PPID ="PAINT_ON_PPID";
	
	private static String TAG_INVALID_VIN = "INVALID_VIN";
	private static String TAG_PRE_PAINT_ON = "PRE_PAINT_ON";
	private static String TAG_ALREADY_TL3 = "ALREADY_TL3";
	private static String TAG_DOLLY_ASSIGNED = "DOLLY_ASSIGNED";
	private static String TAG_FINAL_RESULT = "FINAL_RESULT";
	
	private static String TAG_DOLLY_ID = "DOLLY_ID";
	
	private static String[] DEFAULT_CHECK_DEFECT_STATUS_LANES = {"qA","wD","wV"};
	
	private static String TBS_AREA_NAME = "P1TBS";
	
	private static String CARRIER_PRODUCT_ASSOCIATION_FLAG_TAG ="CARRIER_PRODUCT_ASSOCIATION_FLAG";
	
	@Autowired
	protected FrameDao frameDao;
	
	@Autowired
	protected ProductResultDao productResultDao;
	
	@Override
	public String getAreaName() {
		return "P1PBS";
	}

	@Override
	public void setDecisionController() {
		this.decisionController = new GtsPbs1MoveDecisionController(this);
	}

	@Override
	public String getTrackingServiceInterfaceName() {
		return "GtsPbs1TrackingService";
	}
	
	@Override
	public Product getProduct(String productId) {
		return frameDao.findByKey(productId);
	}

	@Override
    public void receiveSpecialRequest(GtsIndicator indicator) {
		
		super.receiveSpecialRequest(indicator);
        
        if(indicator.getIndicatorName().equalsIgnoreCase(LOAD_VEHICLE)) {
        	if(indicator.isStatusOn()) processVehicleAFOn();
        	GtsIndicator newIndicator = new GtsIndicator(getAreaName(),MP_pJ_pP,indicator.getIndicatorValue());
        	super.receiveMoveInProgress(newIndicator);
        }
        
	}
	
	public DataContainer processCarrierProductAssociation() {
		DataContainer dc = checkCarrierProduct();
		getData().put(CARRIER_PRODUCT_ASSOCIATION_FLAG_TAG, "1");
		super.processCarrierProductAssociation();
		getData().put(CARRIER_PRODUCT_ASSOCIATION_FLAG_TAG, "0");
		return dc;
	}
	
	@Override 
	public  void publishAssociationChanged(GtsCarrier carrier) {
		 super.publishAssociationChanged(carrier);
		 if(getData() != null && getData().containsKey(CARRIER_PRODUCT_ASSOCIATION_FLAG_TAG) && getData().getString(CARRIER_PRODUCT_ASSOCIATION_FLAG_TAG).equals("1")) {
			 GtsNotificationSender.getNotificationService(TBS_AREA_NAME).associationChanged(carrier);
		 }
	}
	
	/**
	 * allow at certain lanes to refresh defect status of last vin of the destination lane
	 */
    @Override
    public void receiveMoveInProgress(GtsIndicator newIndicator){
    	super.receiveMoveInProgress(newIndicator);
    	
    	if(!newIndicator.isStatusOn()) return;
    	
    	if(getCheckDefectStatusLanes().contains(newIndicator.getDestLaneName())) {
    		List<GtsLaneCarrier> laneCarriers = laneCarrierDao.findAll(getAreaName(), newIndicator.getDestLaneName(), getCarrierAreaName());
    		if(!laneCarriers.isEmpty()) {
    			GtsLaneCarrier laneCarrier = laneCarriers.get(laneCarriers.size() -1);
    			if(laneCarrier.getProduct() != null) {
    				refreshProductDefectStatus(laneCarrier.getProduct());
    			}
    		}
    	}
    	
    }
    
    private List<String> getCheckDefectStatusLanes() {
    	List<String> laneNames = PropertyService.getPropertyList(getProcessPointId(), "CHECK_DEFECT_STATUS_LANES");
    	return laneNames.isEmpty()? Arrays.asList(DEFAULT_CHECK_DEFECT_STATUS_LANES): laneNames;
    }
	
	private DataContainer checkCarrierProduct() {
		
		int isVinInvalid = 0;
		int isVinAlreadyAssociated = 0;
		int isVinPrePaintOn = 0;
		int isDollyAssigned = 0;
		int overallStatus = 1;
		
		
		String carrierId=(String) getDevice().getInputValue(TagNames.CARRIER_ID.name());
		String productId=(String) getDevice().getInputValue(TagNames.PRODUCT_ID.name());
		
		GtsCarrier carrier= null;
    	
		int carrierNumber = StringUtils.isNumeric(carrierId) ? Integer.parseInt(carrierId) : -1;
		
		if(carrierNumber != -1) {
			 carrier = carrierDao.findByCarrierId(getCarrierAreaName(), carrierNumber);
		}else {
	    	 carrier = carrierDao.findByKey(new GtsCarrierId(getCarrierAreaName(),carrierId));
	    }
		
		if(carrier == null) {
			// normal
		}else if(CARRIER_MISS_READ.equalsIgnoreCase(carrier.getCarrierNumber())) {
			overallStatus = 0;
		}
		
		if(StringUtils.isEmpty(productId) || getProduct(productId) == null) {
			isVinInvalid = 1;
		}else {
			List<GtsCarrier> carriers = carrierDao.findAllByProductId(getCarrierAreaName(), productId);
			
			if(!carriers.isEmpty()) {
				// already TL3
				isVinAlreadyAssociated = 1;
			}
			// pre-paint on
			List<ProductResult> prodResults = productResultDao.findAllByProductAndProcessPoint(productId, getProperty(PAINT_ON_PPID));
			if(prodResults.isEmpty()) isVinPrePaintOn = 1;
		}	
		
		if(carrier!= null && !StringUtils.isEmpty(carrier.getProductId())) {
			// dolly assigned
			isDollyAssigned = 1;
		}
		
		if(overallStatus == 1) {
			if(isVinInvalid == 1 || isVinAlreadyAssociated == 1 || isVinPrePaintOn == 1 || isDollyAssigned == 1) overallStatus = 0;
		}
		
		DataContainer dc = new DefaultDataContainer();
		dc.put(TAG_INVALID_VIN, isVinInvalid);
		dc.put(TAG_PRE_PAINT_ON, isVinPrePaintOn);
		dc.put(TAG_ALREADY_TL3, isVinAlreadyAssociated);
		dc.put(TAG_DOLLY_ASSIGNED, isDollyAssigned);
		dc.put(TAG_FINAL_RESULT, overallStatus);
		dc.put(TAG_DOLLY_ID, carrierId);
		
		getDevice().populateReply(dc);
		return getDevice().toReplyDataContainer(false);
	}
	
	private void processVehicleAFOn() {
		 
		 List<GtsLaneCarrier> laneCarriers = laneCarrierDao.findAll(getAreaName(),LANE_pJ,getCarrierAreaName());
		 if(laneCarriers.isEmpty()) {
			 logErrorAndNotify("No vehicle is at lane pJ");
			 return;
		 }else {
			 String carrierId = laneCarriers.get(0).getLaneCarrier();
			 
			 GtsCarrier carrier = carrierDao.findByKey(new GtsCarrierId(getCarrierAreaName(),carrierId));
			 if(carrier == null) {
				 logErrorAndNotify("Invalid carrier " + carrierId + " at head of lane pJ");
				 return;
			 }
			 
			 if(StringUtils.isEmpty(carrier.getProductId())) {
				 getLogger().info("carrier vin de-association is not processed at lane pJ due to carrier " + carrier.getCarrierNumber() + " is not associated with a VIN");
				 return;
			 }
			 
			 this.deassociateCarrierWithProduct(carrier);
			 
		 }
	       
	}
	
	public void receiveHeartbeat(GtsIndicator newIndicator)throws SystemException{
	    
		super.receiveHeartbeat(newIndicator);
	    
	    GtsNode node = nodeDao.findByNodeName(getAreaName(),EXIT_pR);
		if(node.isGateOpen()) return;
		
		GtsIndicatorId id = new GtsIndicatorId();
		id.setTrackingArea(getAreaName());
		id.setIndicatorName(LF_pR);
		GtsIndicator lf_pR = indicatorDao.findByKey(id);
		int isLogicallyFull = laneCarrierDao.isLaneLogicallyFull(getAreaName(), LANE_pR);
		
		if((lf_pR != null && lf_pR.isStatusOn()) || isLogicallyFull > 0) {
			logErrorAndNotify("Please open exit gate lane pR to \n" 
					+ "release carriers from lane pR - Lane pR is Full");
		}
	}
	
	@Override
	public void receiveGateStatus(GtsIndicator newIndicator){
		
		 processIndicatorUpdate(newIndicator);
		
		if(newIndicator.getIndicatorName().equals(GS_ENTRY_pT))  {
			GtsNode node = nodeDao.findByNodeName(getAreaName(),ENTRY_pT);
			if(newIndicator.isStatusOn() != node.isGateOpen()) {
				if(newIndicator.isStatusOn()&& !canOpenGate(ENTRY_pT,EXIT_pR)) return; 
			}
		}
		
		if(newIndicator.getIndicatorName().equals(GS_ENTRY_pR))  {
			GtsNode node = nodeDao.findByNodeName(getAreaName(),ENTRY_pR);
			if(newIndicator.isStatusOn() != node.isGateOpen()) return; 
		}
		
		super.receiveGateStatus(newIndicator);
	}    
	
	@Override
	public void toggleGateStatus(GtsNode node){

		if(node.getNodeName().equals(ENTRY_pT) && !node.isGateOpen() && !canOpenGate(ENTRY_pT,EXIT_pR)) 
			return;
		
		if(node.getNodeName().equals(EXIT_pR) && !node.isGateOpen() && !canOpenGate(EXIT_pR,ENTRY_pT))
			return;
		
		if(node.getNodeName().equals(ENTRY_pR)) sendIndicator(CHANGE_CONTROL_INTO_PR, "1");
		
		super.toggleGateStatus(node);
		
	}
	
	@Override
	public GtsCarrier findCarrier(String carrierId,String productId) {
		GtsCarrierType carrierType = GtsCarrierType.NORMAL;
		if(GtsCarrierType.SCRAP.getName().equalsIgnoreCase(productId)) carrierType = GtsCarrierType.SCRAP;
		
		GtsCarrier carrier= null;
    	
		int carrierNumber = StringUtils.isNumeric(carrierId) ? Integer.parseInt(carrierId) : -1;
		
		if(carrierNumber != -1) {
			 carrier = carrierDao.findByCarrierId(getCarrierAreaName(), carrierNumber);
		}else {
	    	 carrier = carrierDao.findByKey(new GtsCarrierId(getCarrierAreaName(),carrierId));
	    }
		 
		if(carrier == null) {
			if(CARRIER_MISS_READ.equalsIgnoreCase(carrierId)) return new GtsCarrier(getCarrierAreaName(),getUnknownCarrier());
			else {
				carrier =  carrierDao.save(new GtsCarrier(getCarrierAreaName(),carrierId));
			}
		}
		
		if(carrier.getCarrierType() != carrierType) {
			carrier.setCarrierType(carrierType);
			carrier = carrierDao.save(carrier);
			getNotificationService().associationChanged(carrier);
		}
		
		return carrier;
	}
	
	@Override
	public void makeMoveDecision(GtsIndicator indicator){
		
		 // no move decision for heart beat signal
		
	     if(indicator.getIndicatorType().equals(GtsIndicatorType.HEART_BEAT)) return;
	     super.makeMoveDecision(indicator);
	}
	
	private boolean canOpenGate(String gateName, String otherGateName) {
		GtsNode node = nodeDao.findByNodeName(getAreaName(),otherGateName);
		if(node.isGateOpen()) {
			logErrorAndNotify("Cannot open gate '" + gateName + "' when gate '" + otherGateName + "' is open -- \n " + 
		                      "Please close '" + otherGateName + "' before opening '" + gateName);
			return false;
		}
		
		return true;
		
	}

}
