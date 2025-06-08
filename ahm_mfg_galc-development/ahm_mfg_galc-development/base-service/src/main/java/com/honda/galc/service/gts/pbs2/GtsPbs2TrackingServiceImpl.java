package com.honda.galc.service.gts.pbs2;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.message.Message;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.enumtype.GtsInspectionStatus;
import com.honda.galc.entity.enumtype.GtsDefectStatus;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.gts.GtsCarrierId;
import com.honda.galc.entity.gts.GtsIndicator;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsLaneId;
import com.honda.galc.entity.gts.GtsMove;
import com.honda.galc.entity.gts.GtsNode;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.service.gts.AbstractBodyTrackingService;
import com.honda.galc.service.gts.GtsPbs2TrackingService;
import com.honda.galc.service.property.PropertyService;

public class GtsPbs2TrackingServiceImpl extends AbstractBodyTrackingService implements GtsPbs2TrackingService{

	
	private static String CARRIER_MISS_READ = "FFFF";
	private static String PAINT_ON_PPID ="PAINT_ON_PPID";
	private static String EXIT_20S = "EXIT-20S";
	private static String EXIT_20D = "EXIT-20D";
	
	private static String TAG_INVALID_VIN = "INVALID_VIN";
	private static String TAG_PRE_PAINT_ON = "PRE_PAINT_ON";
	private static String TAG_ALREADY_TL3 = "ALREADY_TL3";
	private static String TAG_DOLLY_ASSIGNED = "DOLLY_ASSIGNED";
	private static String TAG_FINAL_RESULT = "FINAL_RESULT";
	private static String TAG_DOLLY_ID = "DOLLY_ID";
	
	private static String UNKNOWN_INSPECTION_STATUS_READERS = "UNKNOWN_INSPECTION_STATUS_READERS";
	
	private static String LANE_20A ="20A";
	private static String LANE_11X ="11X";

	@Autowired
	protected FrameDao frameDao;

	@Autowired
	protected ProductResultDao productResultDao;

	@Override
	public Product getProduct(String productId) {
		return frameDao.findByKey(productId);
	}

	@Override
	public String getAreaName() {
		return "P2PBS";
	}

	@Override
	public void setDecisionController() {
		this.decisionController = new GtsPbs2MoveDecisionController(this);
		
	}
	
	public DataContainer processCarrierProductAssociation() {
		
		String clientId = getDevice().getClientId();
		boolean isABS = clientId.contains("ABS");

		if(isABS) checkAbsDollyAssociation();

		DataContainer dc = checkCarrierProduct();
		super.processCarrierProductAssociation();
		
		if(isABS) notifyLaneCarrierChanged(LANE_20A);
		
		return dc;
	}
	
	private void checkAbsDollyAssociation() {
		
		String carrierId=(String) getDevice().getInputValue(TagNames.CARRIER_ID.name());
		String productId=(String) getDevice().getInputValue(TagNames.PRODUCT_ID.name());
		
        List<GtsLaneCarrier> laneCarriers = laneCarrierDao.findAll(getAreaName(), LANE_20A,getCarrierAreaName());
        if(laneCarriers.isEmpty()) return;
        
        GtsLaneCarrier lc = laneCarriers.get(0);
        if(lc.getProductId() != null && lc.getProductId().equalsIgnoreCase(productId)) {
         	
           	String oldCarrierId = lc.getLaneCarrier();
        	GtsCarrier carrier = carrierDao.findByKey(new GtsCarrierId(getCarrierAreaName(),oldCarrierId));
        	carrier.setProductId(null);
        	carrier.setProduct(null);
        	carrierDao.update(carrier);
        	
            getLogger().info("Paint Carrier : " + carrier.getCarrierNumber() + ", Product : " + productId + " is de-associated");
            
        	lc.setLaneCarrier(carrierId);
        	laneCarrierDao.save(lc);
            
        	getLogger().info("paint carrier " + oldCarrierId + " is updated by abs dolly " + carrierId);
        }
        
	}
	
	private DataContainer checkCarrierProduct() {
		
		int isVinInvalid = 0;
		int isVinAlreadyAssociated = 0;
		int isVinPrePaintOn = 0;
		int isDollyAssigned = 0;
		int overallStatus = 1;
		
		String carrierId=(String) getDevice().getInputValue(TagNames.CARRIER_ID.name());
		String productId=(String) getDevice().getInputValue(TagNames.PRODUCT_ID.name());
		
		GtsCarrier carrier= carrierDao.findByKey(new GtsCarrierId(getCarrierAreaName(),carrierId));
		
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
	
	/**
	 * update inspection status to UNKNOWN at PBS Entrance lanes
	 */
    public void receiveReader(GtsIndicator newIndicator){

    	super.receiveReader(newIndicator);
    	
    	 GtsCarrier carrier = findCarrier(newIndicator.getIndicatorValue());
    	 if(carrier == null || StringUtils.isEmpty(carrier.getProductId())) return;
    	 
    	
    	 List<String> readers = PropertyService.getPropertyList(getProcessPointId(), UNKNOWN_INSPECTION_STATUS_READERS);
    	
    	 if(readers == null || readers.isEmpty()) return;
    	
    	 for(String reader: readers) {
    		if(newIndicator.getIndicatorName().contains(reader)) {
    			updateProductInspectionStatus(carrier.getProductId(), GtsInspectionStatus.UNKNOWN.getId());
    		}
    	 }
    }

    @Override
	protected GtsLaneCarrier removeCarrier(String laneName,int position)throws SystemException{
    	
    	GtsLaneCarrier lc = super.removeCarrier(laneName,position);
    	if(lc == null || !LANE_20A.equalsIgnoreCase(laneName) || position != 1) return lc;
    	if (lc.getCarrierId().startsWith("A")) return lc;
    	
    	deassociateCarrierWithProduct(lc.getCarrier());
    	
    	return null;

    }  
    
    @Override
	protected void makeMoveDecision(GtsNode node, List<GtsMove> expiredMoves) {
		if (node.getNodeName().equalsIgnoreCase(EXIT_20S)) {
        	GtsNode decisionNode = nodeDao.findByNodeName(getAreaName(), EXIT_20D);  
        	super.makeMoveDecision(decisionNode, expiredMoves);
        } else 
        	super.makeMoveDecision(node, expiredMoves);
	}  
 
	public GtsDefectStatus getDefectStatus(Product product) {
    	return GtsDefectStatus.DIRECT_PASS;
    }

    
	@Override
	public String getTrackingServiceInterfaceName() {
		return "GtsPbs2TrackingService";
	}
	
	public void logErrorAndNotify(String message) {
		super.logErrorAndNotify(message);
	}	   

  	protected void logEmergencyAndNotify(String message) {
	   	Message msg = new Message(MessageType.EMERGENCY,message);
	   	getLogger().error(message);
	   	getNotificationService().message(msg);
	}
  	
    /*
     * move body from source to destination lane based on fromLanePosition and toLanePosition
     * indicator - MP signal has source lane and destination lane
     * fromLanePosition = (H,E) - H - remove the 1st position of the source lane
     *                            E - remove the last position of the source lane
     * toLanePositio = (H,E,"") - H - add to 1st position of the destination lane
     *                          - E - add to last position of the destination lane
     *                          - "" - don't add the destination lane                           
     */
    public void moveCarrier(GtsIndicator indicator, String fromLanePosition, String endLanePosition) {
    	String sourceLane = indicator.getSourceLaneName();
  		String destLane = indicator.getDestLaneName();
  		
  		GtsLane lane = laneDao.findByKey(new GtsLaneId(getAreaName(),sourceLane));
  		
  		if(lane == null) return;
  		
  		// -1 is the end position
  		
  		int fromPosition = "H".equals(fromLanePosition) ? 1 : Integer.MAX_VALUE;
      
  		GtsLaneCarrier laneCarrier  = this.removeCarrier(sourceLane, fromPosition);
      
  		
      // if source lane is empty, create the unknown carrier. The unknown carrier will be added to
      // end of the destination lane
      if(laneCarrier == null) {
          
          laneCarrier = new GtsLaneCarrier(getAreaName(),destLane);
          
      	 if ( sourceLane.equalsIgnoreCase(LANE_11X)) {
      		 laneCarrier.setLaneCarrier(getEmptyCarrier());
      
      	 }
      	 else {
           laneCarrier.setLaneCarrier(getUnknownCarrier());
          
          // log error - an unknown carrier was created and added to the destination lane
          
          logErrorAndNotify("Unknown carrier was created and added to the destination lane - Source lane : "+sourceLane+", Destination lane : " + destLane);
      	 }
      }

      GtsLane dest = laneDao.findByKey(new GtsLaneId(getAreaName(),destLane));
     
      if(dest != null) {
    	  int toPosition ;
    	  if("H".equals(endLanePosition)) toPosition = 0;
    	  else if("E".equals(endLanePosition)) toPosition = Integer.MAX_VALUE;
    	  else return;
    	  
          addCarrier(dest,toPosition, laneCarrier.getCarrierId());
      }
    }
    
    protected void appendCarrier(GtsLane lane,GtsLaneCarrier laneCarrier) {
        
        if(LANE_20A.equalsIgnoreCase(lane.getLaneName()) && !laneCarrier.isUnknownCarrier()) {

      	  // reassign VIN to a new virtual carrier
      	  
      	  String virtualCarrierId = findAvailableVirtualCarrier();
      	  
      	  GtsCarrier carrier = laneCarrier.getCarrier();
      	  
      	  getLogger().info("Switch carrier to virtual carrier "  + carrier + " with virtual carrier id " + virtualCarrierId);
      	  
      	  laneCarrier.setLaneCarrier(virtualCarrierId);
      	  carrier.getId().setCarrierNumber(virtualCarrierId);
      	  
      	  String productId = laneCarrier.getProductId();  
      	  
    	  changeAssociation(virtualCarrierId, productId);
      	  
        }
        
  	  basicAddCarrier(lane,Integer.MAX_VALUE,laneCarrier.getCarrierId(),laneCarrier.getDiscrepancyStatus(),true);
    }
    
    private String findAvailableVirtualCarrier() {
  	  List<GtsLaneCarrier> laneCarriers = laneCarrierDao.findAll(getAreaName(), LANE_20A,getCarrierAreaName());
  	  for(int i=1;i<10;i++) {
  		  String carrierId = "L03"+ i;
  		  if(contains(laneCarriers, carrierId)) continue;
  		  return carrierId;
  	  }
        
  	  return "L030"; // should not occur
    }
    
    private boolean contains(List<GtsLaneCarrier> laneCarriers, String carrierId) {
  	  for(GtsLaneCarrier lc : laneCarriers) {
  		  if(carrierId.equalsIgnoreCase(lc.getCarrierId())) return true;
  	  }
  	  
  	  return false;
    }
  
}
