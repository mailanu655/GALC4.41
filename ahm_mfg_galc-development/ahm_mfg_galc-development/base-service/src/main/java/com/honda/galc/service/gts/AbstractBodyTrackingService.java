package com.honda.galc.service.gts;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.message.Message;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.ApplicationTaskDao;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.gts.GtsAreaDao;
import com.honda.galc.dao.gts.GtsCarrierDao;
import com.honda.galc.dao.gts.GtsDecisionPointConditionDao;
import com.honda.galc.dao.gts.GtsDecisionPointDao;
import com.honda.galc.dao.gts.GtsIndicatorDao;
import com.honda.galc.dao.gts.GtsLabelDao;
import com.honda.galc.dao.gts.GtsLaneCarrierDao;
import com.honda.galc.dao.gts.GtsLaneDao;
import com.honda.galc.dao.gts.GtsMoveConditionDao;
import com.honda.galc.dao.gts.GtsMoveDao;
import com.honda.galc.dao.gts.GtsNodeDao;
import com.honda.galc.dao.gts.GtsProductDao;
import com.honda.galc.dao.product.HoldResultDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.dao.qics.DefectResultDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.DeviceDataType;
import com.honda.galc.entity.enumtype.GtsCarrierType;
import com.honda.galc.entity.enumtype.GtsDefectStatus;
import com.honda.galc.entity.enumtype.GtsIndicatorType;
import com.honda.galc.entity.enumtype.GtsInspectionStatus;
import com.honda.galc.entity.enumtype.GtsLaneType;
import com.honda.galc.entity.enumtype.GtsMoveStatus;
import com.honda.galc.entity.enumtype.GtsProductStatus;
import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.gts.GtsCarrierId;
import com.honda.galc.entity.gts.GtsDecisionPoint;
import com.honda.galc.entity.gts.GtsIndicator;
import com.honda.galc.entity.gts.GtsLabel;
import com.honda.galc.entity.gts.GtsLabelId;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsLaneId;
import com.honda.galc.entity.gts.GtsMove;
import com.honda.galc.entity.gts.GtsNode;
import com.honda.galc.entity.gts.GtsProduct;
import com.honda.galc.entity.gts.GtsProductId;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.net.DataContainerSocketSender;
import com.honda.galc.net.JsonClient;
import com.honda.galc.notification.GtsNotificationSender;
import com.honda.galc.notification.service.IGtsNotificationService;
import com.honda.galc.notification.service.IProductHoldResultNotification;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.datacollection.IoServiceBase;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ProductSpecUtil;
import com.honda.galc.util.ReflectionUtils;

/**
 * 
 * 
 * <h3>AbstractBodyTrackingService Class description</h3>
 * <p> AbstractBodyTrackingService description </p>
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
 * May 21, 2015
 *
 *
 */
public abstract class AbstractBodyTrackingService extends IoServiceBase implements IBodyTrackingService, IProductHoldResultNotification{
	
	@Autowired
	protected DeviceDao deviceDao;
	
	@Autowired
	protected GtsAreaDao areaDao;
	
	@Autowired
	protected GtsIndicatorDao indicatorDao;
	
	@Autowired
	protected GtsMoveDao moveDao;
	
	@Autowired
	protected GtsMoveConditionDao moveConditionDao;
	
	@Autowired
	protected GtsLaneDao laneDao; 
	
	@Autowired
	protected GtsLaneCarrierDao laneCarrierDao;
	
	@Autowired
	protected GtsCarrierDao carrierDao;
	
	@Autowired
	protected GtsNodeDao nodeDao;
	
	@Autowired
	protected GtsProductDao productDao;
	
	@Autowired
	HoldResultDao holdResultDao;
	
	@Autowired
	protected GtsDecisionPointDao decisionPointDao;
	
	@Autowired
	protected GtsDecisionPointConditionDao decisionPointConditionDao;
	
	@Autowired 
	protected ApplicationTaskDao applicationTaskDao;
	
	@Autowired
	protected GtsLabelDao labelDao;

	protected AbstractDecisionController decisionController;
	
	protected String processPointId;
	
	public AbstractBodyTrackingService() {
		setDecisionController();
	}
	
	@Override
	public void populateData(Device device, DataContainer dc) {
		if(isUnsolicitedDevice(device)) {
			device.populateUnsolicitedData(dc);
		}else {
			super.populateData(device, dc);
		}
	}
	
	@Override
	public DataContainer processData() {
		GtsIndicator indicator = parseIndicator();
		if(indicator != null) {
			processIndicator(indicator);
			return getData();
		} else {
			DataContainer dc =  receiveOtherRequest();
			return dc != null ? dc : getData(); 
		}
	}
	
	public void processIndicator(GtsIndicator indicator) {
		getLogger().info("Received data - " + indicator);
	
		GtsIndicatorType type = indicator.getIndicatorType();
		if(type == null) {
			receiveSpecialRequest(indicator);
			
		}else {	
			ReflectionUtils.invoke(this, type.getMethodName(), indicator);
		}
	}
	
	private GtsIndicator parseIndicator() {
		
		Device device = getDevice();
		List<DeviceFormat> deviceFormats = device.getChangedDeviceDataFormats();
		DeviceFormat deviceFormat = null;
		
		if(isUnsolicitedDevice(device)) {
			if(deviceFormats.isEmpty() || deviceFormats.size() > 1) return null;
			deviceFormat = deviceFormats.get(0);
		}else {
			for(DeviceFormat item : deviceFormats) {
				if(device.getClientId().equalsIgnoreCase(getAreaName()+"."+item.getTag())) {
					//convention - device name = area name + indicator name
					deviceFormat = item;
					break;
				}	
			}
		}
		
		if(deviceFormat == null) return null;
		
		return new GtsIndicator(getAreaName(),deviceFormat.getTag(),ObjectUtils.toString(deviceFormat.getValue()));

	}
	
	public void receiveSpecialRequest(GtsIndicator indicator) {
		
	}
	
	public void receiveMoveControlCheckRequest(GtsIndicator indicator) {
		processIndicatorUpdate(indicator);
	}
	public DataContainer receiveOtherRequest() {
		return processCarrierProductAssociation();
	}
	
	public DataContainer processCarrierProductAssociation() {
		
		String carrierId=StringUtils.trim((String) getDevice().getInputValue(TagNames.CARRIER_ID.name()));
		String productId=StringUtils.trim((String) getDevice().getInputValue(TagNames.PRODUCT_ID.name()));		
		
		if(StringUtils.isEmpty(carrierId)) return null;
		
		 getLogger().info("Processing carrier - " + carrierId + " product - " + productId + " association");
		
		Message message = changeAssociation(carrierId, productId);
		
		if(message != null) logErrorAndNotify(message.getMessage());
		
		return null;
		
	}
	
	/**
     * receive PLC heart beat
     * if heartbeat is sent from unsolicited data list , the reply is sent to a reply device
     * otherwise the reply is sent as return dc
     * @param newIndicator
     * @throws SystemException
     */
    
    public void receiveHeartbeat(GtsIndicator newIndicator)throws SystemException{
        
        processIndicatorUpdate(newIndicator);
        
        String replyName = newIndicator.getNoTypeIndicatorName();
        
        // respond with GALC heart beat
        String replyDeviceId = "REPLY-" + replyName;
		
        if(getPropertyBean().getUnsolicitedDataList().length == 0) {
    		getDevice().setReplyValue(replyDeviceId,newIndicator.getIndicatorValue());
        	DataContainer dc = getDevice().toReplyDataContainer(false);
			this.dataCache.set(dc);
		}else {
			DataContainer dc = new DefaultDataContainer();
			dc.put(replyName, newIndicator.getIndicatorValue());
			sendToPLC(this.getAreaName() + "." + replyDeviceId , dc);
		}
        
        if(getPropertyBean().isCheckDefectRepaired())
        	checkDefectRepaired();

    }
	
	 /**
     * Process the MIP signal
     * @param value - MIP signal 1 or 0
     * @param source - source lane name
     * @param destination - destination lane name
     */
    
    public void receiveMoveInProgress(GtsIndicator newIndicator){
    	if(!updateMoveInProgress(newIndicator)) return;
    	if(newIndicator.isStatusOn()) {
    		String[] values = getDeadRailValues(newIndicator);
    		if(values == null) {
    			//normal MP signal
    			moveCarrier(newIndicator.getSourceLaneName(),newIndicator.getDestLaneName());
    		} else {
    		
	    		//dead rail
	    		values = checkDeadRail(values);
	    		
	    		if(values != null) {
	    			moveCarrier(newIndicator, values[0],values[1]);
	    		}else {
	    			// invalid dead rail configuration
	    			 getLogger().error("incorrect deadl rail configuration for " + newIndicator.getIndicatorName());
	    			 return;
	    		}
    		}
    	}
    	
    	getNotificationService().indicatorChanged(newIndicator);
    	
    	// execute move decision rules 
        makeMoveDecision(newIndicator);
    }
    
    public String[] checkDeadRail(String[] deadRailValues) {
        String[] values = new String[2];
        
        deadRailValues[0]= StringUtils.trim(deadRailValues[0]);
        if(deadRailValues.length > 1) deadRailValues[1]= StringUtils.trim(deadRailValues[1]);
        
        values[0] = "H";
        values[1] = "";
        
    	if(deadRailValues[0].equals("H") || deadRailValues[0].equals("E")) values[0] = deadRailValues[0];
     	else return null;
     	    
 	    if(deadRailValues.length >1) {
 	    	if(deadRailValues[1].equals("H") || deadRailValues[1].equals("E")|| deadRailValues[1].equals("")) values[1] = deadRailValues[1];
 	    	else return null;
     	}
    	return values;
    }
    
    public String[] getDeadRailValues(GtsIndicator newIndicator) {
    	Map<String, String[]> deadRailMap = getPropertyBean().getDeadRailMap(String[].class);
    	return deadRailMap == null ? null :deadRailMap.get(newIndicator.getIndicatorName());
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
      
		
      // if source lane is empty, create the unknown carrier. The unknow carrier will be added to
      // end of the destination lane
      if(laneCarrier == null) {
          
          laneCarrier = new GtsLaneCarrier(getAreaName(),destLane);
          laneCarrier.setLaneCarrier(getUnknownCarrier());
          
          // log error - an unknown carrier was created and added to the destination lane
          
          logErrorAndNotify("Unknown carrier was created and added to the destination lane - Source lane : "+sourceLane+", Destination lane : " + destLane);
          
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
    
    /**
     * process Carrier Present
     * @param newIndicator
     * @throws SystemException
     */
     
     public void receiveCarrierPresent(GtsIndicator newIndicator){
    	 processIndicatorUpdate(newIndicator);
     }
     
     /**
      * process CarrierPresent and LaneFull
      * @param newIndicator
      */
     public void receiveCarrierPresentLaneFull(GtsIndicator newIndicator){
    	 processIndicatorUpdate(newIndicator);
     }
     
     /**
      * process lane full
      * @param newIndicator
      */
     public void receiveLaneFull(GtsIndicator newIndicator){
    	 processIndicatorUpdate(newIndicator);
     }
     
     public List<GtsLaneCarrier> receiveBodyCountChange(GtsIndicator newIndicator) {
    	 checkAndSaveIndictorChange(newIndicator);
    	 return readBodyPositionsFromPLC(newIndicator.getLaneName());
     }
     
     public void receiveBodyPositionChange(GtsIndicator newIndicator) {
    	 checkAndSaveIndictorChange(newIndicator);
    	 readBodyPositionsFromPLC(newIndicator.getLaneName()); 
     }
     
     public void receiveMoveStatus(GtsIndicator newIndicator) {
    	 newIndicator = checkAndSaveIndictorChange(newIndicator);
    	 if(newIndicator == null) return;
    	 getNotificationService().indicatorChanged(newIndicator);
     }
     
     public void receiveInspectionStatus(GtsIndicator newIndicator) {
     	 newIndicator = checkAndSaveIndictorChange(newIndicator);
       	 if(newIndicator == null || !newIndicator.isStatusOn()) return;
       	 
         List<GtsLaneCarrier> laneCarriers = laneCarrierDao.findAll(getAreaName(), newIndicator.getLaneName(),getCarrierAreaName());
         if(laneCarriers.size() == 0) {
             getLogger().error("Received Inspection Status " + newIndicator.getIndicatorName() + " but no carrier at head of lane " + newIndicator.getLaneName());        	 
        	 return;
         }
         
         GtsProduct product = laneCarriers.get(0).getProduct();
         if(product == null) {
             getLogger().error("Received Inspection Status " + newIndicator.getIndicatorName() + " but no vehicle at head of lane " + newIndicator.getLaneName());
             return;
         }
         
         // use destination lane for status type like FAIL_S, FAIL_M, FAIL_L
         GtsInspectionStatus status = GtsInspectionStatus.getType(newIndicator.getDestLaneName());     
         
         if(status == null) {
             getLogger().error("Received Inspection Status " + newIndicator.getIndicatorName() + " but not valid status " + newIndicator.getDestLaneName());
             return;
         }
         
         product.setInspectionStatus(status.getId());
         
         productDao.save(product);
         
 		List<GtsProduct> products = new ArrayList<GtsProduct>();
 		products.add(product);

 		getNotificationService().productStatusChanged(products);
 		
 		getLogger().info("Product inspection status is updated - product id " + product.getProductId() + " inspection status " + product.getInspectionStatus());

       	 
     }
     
     public void receiveToggleLabel(GtsIndicator newIndicator) {
    	 newIndicator = checkAndSaveIndictorChange(newIndicator);
    	 getNotificationService().indicatorChanged(newIndicator);	
     }

     public void receiveActiveLane(GtsIndicator newIndicator) {
    	 GtsIndicator indicator = indicatorDao.findByKey(newIndicator.getId());
    	 if(indicator != null) {
    		 GtsIndicator mvIndicator = new GtsIndicator(getAreaName(),
     			GtsIndicatorType.MOVE_STATUS.getType()+ Delimiter.HYPHEN +  newIndicator.getLaneName() + Delimiter.UNDERSCORE + indicator.getIndicatorValue(), "0");
    		 this.receiveMoveStatus(mvIndicator);
    	 }
    	 
    	 String value = newIndicator.getIndicatorValue();
    	 if(StringUtils.isNumeric(value)) {
    		 GtsIndicator mvIndicator = new GtsIndicator(getAreaName(),
    				GtsIndicatorType.MOVE_STATUS.getType() + Delimiter.HYPHEN + newIndicator.getLaneName() + Delimiter.UNDERSCORE  + value, "1");
    		 this.receiveMoveStatus(mvIndicator);
    	 }
    	 
       	 indicatorDao.save(newIndicator);
       	 
     }
     
     protected List<GtsLaneCarrier> readBodyPositionsFromPLC(String laneName) {
    	
    	 List<GtsLaneCarrier> laneCarriers= new ArrayList<GtsLaneCarrier>();
      	
    	 String deviceId = getAreaName() + Delimiter.DOT + "BP-" + laneName;
    	 DataContainer dc = syncReadFromPLC(deviceId);
    	 if(dc == null) return laneCarriers;
    	 
     	 
     	 laneCarriers = createLaneCarriers(dc,laneName);
     	 
     	 laneCarrierDao.removeAll(getAreaName(), laneName);
     	 laneCarrierDao.saveAll(laneCarriers);
     	 
     	 notifyLaneCarrierChanged(laneName);
     	 
     	 return laneCarriers;
     }
     
     protected void writeBodyPositionsToPLC(String laneName) {
    	 
    	 DataContainer dc = new DefaultDataContainer();
    	 
    	 GtsLane lane = laneDao.findByKey(new GtsLaneId(getAreaName(),laneName));
    	 
    	 if(lane == null) {
    		 getLogger().error("invalid lane name : + " + lane);
    		 return;
    	 }
    	 
    	 String deviceId = getAreaName() + Delimiter.DOT + "BP-" + laneName;

    	 Device device = deviceDao.findByKey(deviceId);
      	
     	 if(device == null) {
 			getLogger().error("Invalid Configuration - no device found : " + deviceId);
 			return;
 		 }
   		 
    	 dc.setClientID(deviceId);
    	 
    	 
    	 List<GtsLaneCarrier> laneCarriers = laneCarrierDao.findAll(getAreaName(), laneName,getCarrierAreaName());
    	 
    	 for(int i = 0; i<lane.getLaneCapacity();i++) {
    		 String tag = "BP"+Delimiter.HYPHEN + laneName+ Delimiter.HYPHEN + (i + 1);
             String carrierId = "";      		 
    		 if(i < laneCarriers.size()) {
    			 carrierId = laneCarriers.get(i).getCarrierId();
    		 }
    		 
    		 if(StringUtils.isEmpty(carrierId)) {
    			 DeviceFormat deviceFormat = device.getDeviceFormat(tag);
        		 if(deviceFormat != null && deviceFormat.getDeviceDataType().equals(DeviceDataType.STRING)) carrierId = " ";
        		 else carrierId ="0";
    		 }
    		 dc.put(tag, carrierId);
     	 }
    	 
    	 dc.put("BODY-COUNT", laneCarriers.size());
    	 
    	 sendToPLC(deviceId, dc);
    	  
     }
     
     private List<GtsLaneCarrier> createLaneCarriers(DataContainer dc, String laneName) {
    	 List<GtsLaneCarrier> laneCarriers = new ArrayList<GtsLaneCarrier>();
    	 long currentTime = System.currentTimeMillis();
    	 
    	 List<String> tags = getDataPointNames(dc,"BP"+Delimiter.HYPHEN + laneName+ Delimiter.HYPHEN);
    	 
    	 for(String tag :tags) {
    		 String carrier = findCarrier(dc,tag);
    		 if(StringUtils.isEmpty(carrier)) continue;
    		 GtsLaneCarrier lc = new GtsLaneCarrier(getAreaName(),laneName);
    	     lc.getId().setLanePosition(new Timestamp(currentTime));
    	     lc.setLaneCarrier(carrier);
    	     laneCarriers.add(lc);
    	     currentTime += 1000;   
    	 }
    	 
    	 return laneCarriers;
    	 
     }
    	 
     private String findCarrier(DataContainer dc, String tag) {
    	 if(!dc.containsKey(tag)) return null;
    	 
		 GtsCarrier carrier = null;
		 int carrierId = DataContainerUtil.getInteger(dc, tag, -1);
		 if(carrierId == 0) return null;
		 if(carrierId != -1) {
			 carrier = carrierDao.findByCarrierId(getCarrierAreaName(), carrierId);
		 }else {
			 String carrierNumber = dc.getString(tag);
			 if("NULL".equalsIgnoreCase(carrierNumber) ||StringUtils.trim(carrierNumber).isEmpty()) return null;
	    	 carrier = carrierDao.findByKey(new GtsCarrierId(getCarrierAreaName(),carrierNumber));
	     }
		 
		 if(carrier == null) return getUnknownCarrier();
		 return carrier.getCarrierNumber();
     }
     
     protected List<String> getDataPointNames(DataContainer dc, String tagPrefix) {
    	 List<String> keys = new ArrayList<String>();
     	 for(Object obj : dc.keySet()) {
    		 String key = ObjectUtils.toString(obj);
    		 if(!StringUtils.isEmpty(key) && key.startsWith(tagPrefix)) keys.add(key);
    	 }
     	 
     	 Collections.sort(keys,GtsIndicator.DATA_POINT_COMPARATOR);
     	 return keys;
     }
     
     public void receiveConveyorStatus(GtsIndicator newIndicator){
    	 processIndicatorUpdate(newIndicator);
     }
     
     public void receiveControlBox(GtsIndicator newIndicator){
    	 processIndicatorUpdate(newIndicator);
     }
     
     /**
      * Gate Status from PLC
      * @param newIndicator
      */
     public void receiveGateStatus(GtsIndicator newIndicator){
    	
    	 String gateName = newIndicator.getGateName();
         if(gateName == null) return;
         GtsNode node = nodeDao.findByNodeName(getAreaName(),gateName);
         if(node == null) {
        	// log error - no gate is found
        	return;
         }
         
         if(node.isPLCGate()) {
        	 if(node.getStatus() == newIndicator.getStatus()) return;
         }else if(node.isNegativePLCGate()) {
        	 if(node.getStatus() != newIndicator.getStatus()) return;
         }
         
         basicToggleGateStatus(node);
         
     }
     
     /**
      * Receive photo eye
      * @param newIndicator
      * @throws SystemException
      */
     public void receivePhotoEyeStatus(GtsIndicator newIndicator){
         
         GtsLane lane = laneDao.findByKey(new GtsLaneId(getAreaName(),newIndicator.getLaneName())); 
         
         if(lane == null){
             // Emergency - reader or lane definition is incorrect
             getLogger().error("GTSX001","Reader Name = " + newIndicator.getIndicatorName());
             return;
         };
         
         
         
         // compare the reader value with the head of lane carrier number
         
         List<GtsLaneCarrier> laneCarriers = laneCarrierDao.findAll(getAreaName(),newIndicator.getLaneName(),getCarrierAreaName());
         
         if(laneCarriers.isEmpty()) {

             getLogger().info("GTSE008","Photoeye name = " + newIndicator.getIndicatorName());
         
         }else {
             
        	 GtsLaneCarrier lc = laneCarriers.get(0);
             
             if(!lc.isEmptyCarrier() ^ newIndicator.isStatusOn()){
              // there is photo eye and engine discrepancy  
                 lc.setPhotoEyeDiscrepancy(true);
                 laneCarrierDao.update(lc);
                 getNotificationService().laneCarrierChanged(lc.getId().getLaneId(), laneCarriers);
             }
             
         }
     }
     
     /**
      * process the reader information when get a reader value from PLC
      * when the reader is the one at a entry lane of the tracking area, we have to populate the data of the product which is associated with the carrier
      * also we have to check if the reader value matches the carrier at the head of lane
      * if they match, no message is logged, otherwise log the error message and publishs it to all clients 
      * @param newIndicator
      * @throws SystemException
      */
     
     public void receiveReader(GtsIndicator newIndicator){
         
         String carrierNumber = newIndicator.getIndicatorValue();
         
           GtsLane lane = laneDao.findByKey(new GtsLaneId(getAreaName(),newIndicator.getLaneName()));
         
         if(lane == null){
             // Emergency - reader or lane definition is incorrect
             logErrorAndNotify("Reader or lane definition is incorrect - Reader Name = " + newIndicator.getIndicatorName());
             return;
         };
         
         boolean isUpdateLaneCarriers = isUpdateLaneCarriers(newIndicator.getIndicatorName());

         GtsCarrier carrier = findCarrier(carrierNumber);
        	       
         if(carrier == null  || carrier.isUnknownCarrier()){
             //log error - carrier number does not exist in the GTS_Carrier table
        	 logErrorAndNotify("Carrier number does not exist - Lane = " + lane.getLaneName() + ", Reader number = " + newIndicator.getIndicatorValue());
             return;
         };
         
         carrierNumber = carrier.getCarrierNumber();
         
         if(lane.isEntryLane() && carrier.getProductId()!=null && carrier.getProductId().length() != 0){
             
             // entry lane - need to populate the Product with engine/vehicle data
             
             // create product object from the original production data (Engine or Vehicle or others)
             // the sub class of this handler which is area specific should implement the createProduct method
             // to populate the product object either with engine or vehicle data
             
             if(populateProduct(carrier.getProductId()) == null)
                 
                     // log no product found info
            	 logErrorAndNotify("No product found - " + "Product ID =  " + carrier.getProductId());
                     
         }
             
         GtsLaneCarrier lc = null;
         
         // compare the reader value with the head of lane carrier number
         
         List<GtsLaneCarrier> laneCarriers =  laneCarrierDao.findAll(getAreaName(),lane.getLaneName(),getCarrierAreaName());
        
         if(laneCarriers.isEmpty()){
             
             // lane is empty while reader gets a value
             int discrepancyStatus = 0;
             if(!lane.isEntryLane()){
                 
            	 discrepancyStatus = GtsLaneCarrier.READER_DISCREPANCY;
            	 // log error - no carrier at the current lane
            	 logErrorAndNotify("No carrier at the current lane - " + "Lane = " + lane.getLaneName() + ", Reader number = " + newIndicator.getIndicatorValue());
             }
             
             if(isUpdateLaneCarriers) {
             	// update the head of lane with the reader number
             	lc = basicAddCarrier(lane,0,carrierNumber,discrepancyStatus,true);
             }
              
         }else {
             
             // lane is not empty
             
        	 // just return if reader gets an unknown carrier while there is carrier at current lane
        	 if(carrier.isUnknownCarrier()) return;
        	 
             lc = laneCarriers.get(0);
             if(!carrierNumber.equals(lc.getCarrierId())){
             
                 // log error - carrier at the head of lane does not match the read value
            	 logErrorAndNotify("Carrier at head of lane does not match the reader value - " + 
                                 "Lane = " + lane.getLaneName() + 
                                 ", Carrier at head of lane = " + laneCarriers.get(0).getCarrierId() + 
                                 ", Reader number = " + newIndicator.getIndicatorValue());
                 if(isUpdateLaneCarriers) {
	            	 // update the head of with the reader number
	                 lc.setReaderDiscrepancy(true);
	                 lc.setLaneCarrier(carrierNumber);
	                 laneCarrierDao.update(lc);
	             
	                 // notificate the client the lane carriers are updated
	                 getNotificationService().laneCarrierChanged(lane.getLaneName(), laneCarrierDao.findAll(getAreaName(),lane.getLaneName(),getCarrierAreaName()));
            
                 }    
             } else {
                 
                 // reader value and the head of lane carrier does match
                 
                 if(lc.isReaderDiscrepancy()) {
                     
                     // there was a discrenpancy
                     // clear the discrepancy here
                     lc.setReaderDiscrepancy(false);
                     laneCarrierDao.update(lc);
                     
                     lc = null;
                     
                     // notificate the client the lane carriers are updated
                     getNotificationService().laneCarrierChanged(lane.getLaneName(), laneCarrierDao.findAll(getAreaName(),lane.getLaneName(),getCarrierAreaName()));
                
                 }
             }
        }
         
        if(lane.isEntryLane() && carrier.getProductId()!=null && carrier.getProductId().length() != 0){
             
                this.checkDuplicateProduct(carrierNumber, carrier.getProductId());
        
        } 
        
        if(lc != null) checkDuplicatedCarriers(lc);
         
     }
     
     protected boolean isUpdateLaneCarriers(String readerName) {
    	 String[] readers = getPropertyBean().getNotUpdateTrackingReaders();
    	 for(String reader : readers) {
    		 if(readerName.contains(reader)) return false;
    	 }
    	 return true;
     }
     
     protected GtsIndicator saveIndicatorChange(GtsIndicator newIndicator) {
    	 newIndicator = checkAndSaveIndictorChange(newIndicator);
    	 if(newIndicator == null) return null;
    	 
    	 getNotificationService().indicatorChanged(newIndicator);
    	 
    	 return newIndicator;
       
     }
     
     protected void processIndicatorUpdate(GtsIndicator newIndicator){
         
    	 newIndicator = saveIndicatorChange(newIndicator);
         if(newIndicator != null) {
        	 // execute move decision rules 
        	 makeMoveDecision(newIndicator);
         }
         
     }
     
     protected GtsIndicator checkAndSaveIndictorChange(GtsIndicator newIndicator){
    	 GtsIndicator indicator = indicatorDao.findByKey(newIndicator.getId());
         if(indicator != null && indicator.getIndicatorValue().equals(newIndicator.getIndicatorValue())){
         	// no indicator status chanage
         	return null;
         }
         
         return indicatorDao.save(newIndicator);
     }
     
     protected boolean updateMoveInProgress(GtsIndicator newIndicator){
        
        GtsIndicator indicator = indicatorDao.findByKey(newIndicator.getId());
        if(indicator != null && indicator.getIndicatorValue().equals(newIndicator.getIndicatorValue())){
        	// no indicator status chanage
        	return false;
        }
        
    	indicator = indicatorDao.save(newIndicator);
    	
        // update move information into move table
        GtsMove move = moveDao.findByKey(new GtsMove(newIndicator).getId());
        if(move != null) {
        	move.setMoveStatus(indicator.isStatusOn()? GtsMoveStatus.STARTED : GtsMoveStatus.FINISHED);
        	moveDao.update(move);
        }
        
        return true;
    }
    
    /**
     * move carrier from source lane to destination lane
     * @param source - source lane name
     * @param destination - destination lane name
     * @throws SystemException
     */
    
     /**
      * move carrier from source lane to destination lane
      * @param source - source lane name
      * @param destination - destination lane name
      * @throws SystemException
      */
     
     protected void moveCarrier(String source,String destination){
         
         GtsLane lane = laneDao.findByKey(new GtsLaneId(getAreaName(),source));
         if(lane == null) return;
         
         GtsLaneCarrier laneCarrier  = this.removeCarrier(source, 1);
         
         // if source lane is empty, create the unknown carrier. The unknow carrier will be added to
         // end of the destination lane
         if(laneCarrier == null) {
             
             laneCarrier = new GtsLaneCarrier(getAreaName(),destination);
             laneCarrier.setLaneCarrier(getUnknownCarrier());
             
             // log error - an unknown carrier was created and added to the destination lane
             
             logErrorAndNotify("Unknown carrier was created and added to the destination lane - Source lane : "+source+", Destination lane : " + destination);
             
         }

         GtsLane destLane = laneDao.findByKey(new GtsLaneId(getAreaName(),destination));
        
         if(destLane != null && destLane.getLaneCapacity() > 0) {
             this.appendCarrier(destLane,laneCarrier);
         }else  {
        	 if(destLane == null || destLane.getLaneType() == GtsLaneType.EXIT)
        		 processCarrierLeave(laneCarrier, lane,destination);
         }
     }
     

     /**
      * process carrier leaving the tracking area 
      * @param laneCarrier - leaving carrier
      * @param destination - destination lane name
      * @throws SystemException
      */
     
     protected void  processCarrierLeave(GtsLaneCarrier laneCarrier, GtsLane sourceLane, String destination) {
         
         if(laneCarrier.getProductId() == null) {
             
             // empty carrier or unknown carrier
             getLogger().info(" empty carrier or unknown carrier ", "Carrier : " + laneCarrier.getCarrierId());

             return;
         }
         
         // log carrier leaving the tracking area
         
         getLogger().info("Carrier leaving the tracking area - ", "Carrier : " + laneCarrier.getCarrierId() + ", Product : " + laneCarrier.getProductId());

         // carrier and production deassociation
  
         deassociateCarrierWithProduct(laneCarrier.getCarrier());
         
     }
     
     /**
      * remove a carrier at position of a lane 
      * @param laneName - lane name
      * @param position - position starting from 1
      * @return
      * @throws SystemException
      */
     
     protected GtsLaneCarrier removeCarrier(String laneName,int position)throws SystemException{
         
         List<GtsLaneCarrier> laneCarriers = laneCarrierDao.findAll(getAreaName(),laneName,getCarrierAreaName());
         GtsLaneCarrier laneCarrier = null;
         
         if(position > laneCarriers.size()) position = laneCarriers.size();
      
         if (position >0 && position <= laneCarriers.size()){
             laneCarrier = laneCarriers.get(position -1);
             laneCarrierDao.remove(laneCarrier);
             
             //log debug info - carrier is removed from lane
             getLogger().info("Carrier # : "+laneCarrier.getCarrierId()+" is removed from lane : " + laneName);
             
             getNotificationService().laneCarrierChanged(laneName, laneCarrierDao.findAll(getAreaName(),laneName,getCarrierAreaName()));
         }
   
         return laneCarrier;
     }
     
     protected String converFromShortProductId(String productId) {
    	 return productId;
     }
   
    
    /**
     * associate or deassociate the carrier with product
     * @param carrier - carrier number
     * @param productId - new product id, if productId = "" , deassoicate
     * @throws SystemException
     */
    
    @Transactional
    public Message changeAssociation(String carrierId,String productId){
    	
        GtsCarrier carrier = findCarrier(carrierId,productId); 
   
        if(carrier == null || carrier.getCarrierNumber().equals(getUnknownCarrier()) || isEmptyCarrier(carrier.getCarrierNumber())) {
            // carrier does not exist 
            return Message.logWarning("Carrier " + carrierId + " doest not exist!");
        }
        
        if(!StringUtils.isEmpty(carrier.getProductId()) && productId.equals(carrier.getProductId())) {
        	String message = "Carrier - " + carrierId + " Product - " + productId + " was associated";
        	getLogger().info(message);
        	return Message.logWarning(message); 
        }
        
        productId = converFromShortProductId(productId);
        
        carrierId = carrier.getCarrierNumber();
        
        String oldProductId = carrier.getProductId();
        
        if(productId == null || productId.length() == 0) {
            
            // deassoication
                
            this.deassociateCarrierWithProduct(carrier);
            
            return null;
            
        } else if(!getPropertyBean().isAllowDuplicatedAssociation()) {
        	this.deassociateCarrierWithProduct(productId);
        }
            
        // check if the product with productId exists 
        
        GtsProduct product = this.populateProduct(productId);
        
        if(product == null) 
            return Message.logWarning("Incorrect product ID: " + productId + ". Please check!");
        
        // assoicate carrier with product
        
        productId = product.getProductId();
        carrier.setProductId(productId);
        carrier.setCarrierType(GtsCarrierType.NORMAL);
        carrierDao.update(carrier);
        
        carrier.setProduct(product);
        
        this.resetDiscrepancy(carrierId);
        
        if(oldProductId != null && oldProductId.length() > 0 )
            this.checkDuplicateProduct(carrierId,oldProductId);
        
        this.checkDuplicateProduct(carrierId,productId);
        
        publishAssociationChanged(carrier);
        
        getLogger().info("Carrier: " + carrier.getCarrierNumber() + " product ID : " + productId + " old Product ID : " + oldProductId);

        return null;
         
    }
    
    protected void publishAssociationChanged(GtsCarrier carrier) {
        getNotificationService().associationChanged(carrier);
    }
    
    protected GtsCarrier findCarrier(String carrierId, String productId) {
    	return findCarrier(carrierId);
    }
    
    protected GtsCarrier findCarrier(String carrierId) {
    	
    	GtsCarrier carrier= null;
    	
		int carrierNumber = StringUtils.isNumeric(carrierId) ? Integer.parseInt(carrierId) : -1;
		
		if(carrierNumber != -1) {
			 carrier = carrierDao.findByCarrierId(getCarrierAreaName(), carrierNumber);
		}else {
	    	 carrier = carrierDao.findByKey(new GtsCarrierId(getCarrierAreaName(),carrierId));
	    }
		 
		if(carrier == null) return new GtsCarrier(getCarrierAreaName(),getUnknownCarrier());
		return carrier;
    }
    
    protected GtsProduct populateProduct(String productId) {
        
        GtsProduct product = createProduct(productId);
        
        if (product != null){
            // update or insert the new product
            GtsProduct oldProduct = productDao.findByKey(product.getId());
            if(oldProduct != null) { 
            	product.setProductSeq(oldProduct.getProductSeq());
            	product.setInspectionStatus(oldProduct.getInspectionStatusValue());
            }
            productDao.save(product);
     
            // log debug info for creating a new product
            getLogger().info("New Product is created - " +  product);
        }
        
        return product;

    }
    
    /**
     * create product from the engine or frame related tables, which contains the original product info
     * subclass of this should overide this to tell if engine/or vehicle 
     * @param productId
     * @return
     * @throws SystemException
     */
    
    public GtsProduct createProduct(String productId) {
    	Product product = getProduct(productId);
    	if(product == null) return null;
    	GtsProduct gtsProduct = new GtsProduct(getCarrierAreaName(),productId);
    	gtsProduct.setShortProdId(product.getShortProductId());
    	gtsProduct.setLotNumber(product.getProductionLot());
    	gtsProduct.setKdLotNumber(product.getKdLotNumber());
    	gtsProduct.setModelYear(ProductSpecUtil.extractModelYearCode(product.getProductSpecCode()));
    	gtsProduct.setModelCode(ProductSpecUtil.extractModelCode(product.getProductSpecCode()));
    	gtsProduct.setModelType(ProductSpecUtil.extractModelTypeCode(product.getProductSpecCode()));
    	gtsProduct.setModelOption(ProductSpecUtil.extractModelOptionCode(product.getProductSpecCode()));
    	gtsProduct.setExtColorCode(product.getModelExtColorCode());
    	gtsProduct.setIntColorCode(product.getModelIntColorCode());
    	gtsProduct.setProductStatus(getProductStatus(productId));
    	GtsDefectStatus defectStatus = getDefectStatus(product);
    	gtsProduct.setDefectStatus(defectStatus.getId());
    	return gtsProduct;
    }
    
    public abstract Product getProduct(String productId);
    
    public GtsDefectStatus getDefectStatus(Product product) {
    	return getDefectStatus(product.getProductId());
    }
    
    public GtsDefectStatus getDefectStatus(String productId) {
    	return getPropertyBean().isCheckNAQDefect() ?
    			getNAQDefectStatus(productId):
    			getQICSDefectStatus(productId);
    }
    
    public GtsDefectStatus getQICSDefectStatus(String productId) {
    	List<DefectResult> defectResults = getDao(DefectResultDao.class).findAllByProductId(productId);
    	
    	GtsDefectStatus currentStatus = GtsDefectStatus.DIRECT_PASS;
		for (DefectResult defect : defectResults) {
			switch(defect.getDefectStatus()) {
				case OUTSTANDING: 
				case NOT_FIXED:
				case NOT_REPAIRED:
				case NON_REPAIRABLE:
					currentStatus = GtsDefectStatus.OUTSTANDING;
					break;
				
				case DIRECT_PASS:
					break;
				
				case REPAIRED:
				case FIXED:
					if(currentStatus == GtsDefectStatus.DIRECT_PASS)
						currentStatus = GtsDefectStatus.REPAIRED;
				    break;
				case SCRAP:
				case PREHEAT_SCRAP: return GtsDefectStatus.SCRAP;
			}	
		}
		
		return currentStatus;
    }
    
    public GtsDefectStatus getNAQDefectStatus(String productId) {
    	List<QiDefectResult>  defectResults = getDao(QiDefectResultDao.class).findAllByProductId(productId, new ArrayList<Short>());
    	GtsDefectStatus defectStatus = GtsDefectStatus.DIRECT_PASS;
    	for(QiDefectResult item : defectResults) {
    		GtsDefectStatus currentStatus = convert(DefectStatus.getType(item.getCurrentDefectStatus()));
    		if(currentStatus == GtsDefectStatus.SCRAP || currentStatus == GtsDefectStatus.OUTSTANDING) return currentStatus;
    		else if(currentStatus == GtsDefectStatus.REPAIRED) defectStatus = currentStatus;
    	}
    	
    	return defectStatus;
    	
    }
    
    private GtsDefectStatus convert(DefectStatus defectStatus) {
    	switch(defectStatus) {
			case OUTSTANDING: 
			case NOT_FIXED:
			case NOT_REPAIRED:
			case NON_REPAIRABLE:
				return GtsDefectStatus.OUTSTANDING;
			
			
			case REPAIRED:
			case FIXED:
				return GtsDefectStatus.REPAIRED;

			case SCRAP:
			case PREHEAT_SCRAP: return GtsDefectStatus.SCRAP;
			
			default: return GtsDefectStatus.DIRECT_PASS;
    	}
    	
    }
    
    @Transactional
    public void refreshProductDefectStatus(String productId) {
    	
    	getLogger().info("Received refreshProductDefectStatus from user - Product Id " + productId);
    	GtsProduct product = productDao.findByKey(new GtsProductId(getCarrierAreaName(),productId));
    	if(product == null) {
    		getLogger().warn("Invalid product Id - " + productId);
    		return;
    	}
    	
    	refreshProductDefectStatus(product);
    	
    }
    
    protected void refreshProductDefectStatus(GtsProduct product) {
    	
    	GtsDefectStatus defectStatus = getDefectStatus(product.getProductId());
    	
    	if(product.getDefectStatus() != defectStatus) {
    		List<GtsProduct> products = new ArrayList<GtsProduct>();
    		product.setDefectStatus(defectStatus.getId());
    		productDao.save(product);
    		products.add(product);
 
    		getNotificationService().productStatusChanged(products);
    		
    		getLogger().info("Product defect status is updated - product id " + product.getProductId() + " defect status " + product.getDefectStatus());
    	}

    }
    
    public void updateProductInspectionStatus(String productId,int inspectionStatus) {
       	getLogger().info("Received updateProductInspectionStatus from user - Product Id " + productId + ", inspection status " + inspectionStatus);
    	GtsProduct product = productDao.findByKey(new GtsProductId(getCarrierAreaName(),productId));
    	if(product == null) {
    		getLogger().warn("Invalid product Id - " + productId);
    		return;
    	}

    	updateProductInspectionStatus(product, inspectionStatus);
    	
	}
    
    
    @Transactional
	public void updateProductInspectionStatus(GtsProduct product,int inspectionStatus) {
       	
		List<GtsProduct> products = new ArrayList<GtsProduct>();
		product.setInspectionStatus(inspectionStatus);
		productDao.save(product);
		products.add(product);
		getNotificationService().productStatusChanged(products);
		
		getLogger().info("Product inspection status is updated - product id " + product.getProductId() + " inspection status " + product.getInspectionStatus());

    }
	    
    
    private GtsProductStatus getProductStatus(String productId) {
    	List<HoldResult> holdResults = holdResultDao.findAllByProductId(productId);
    	
    	for (HoldResult holdResult :holdResults) {
    		GtsProductStatus productStatus= getProductStatus(holdResult);
    		if(!productStatus.equals(GtsProductStatus.RELEASE)) return productStatus;
    	}
    	return GtsProductStatus.RELEASE;
    }
    
    private GtsProductStatus getProductStatus(HoldResult holdResult) {
    	if(holdResult.getId().getHoldType() == 0 && holdResult.getReleaseFlag() == 0){
			return GtsProductStatus.HOLD;
		}
		else if(holdResult.getId().getHoldType() == 1 && holdResult.getReleaseFlag() == 0){
			return GtsProductStatus.SHIPPING_HOLD;
		}
    	
    	return GtsProductStatus.RELEASE;
    }
    
    /**
     * deassociate the carrier with the product
     * @param carrier
     * @throws SystemException
     */
    
    protected void deassociateCarrierWithProduct(GtsCarrier carrier){
    	
    	if(StringUtils.isEmpty(carrier.getProductId())) return;
    	
    	GtsProduct product = carrier.getProduct();
    	String productId = carrier.getProductId();
    	
    	if(product == null) {
    		product = productDao.findByKey(new GtsProductId(getCarrierAreaName(),productId));
    	}
    	
    	carrier.setProductId(null);
    	carrier.setProduct(null);
    	
    	carrierDao.update(carrier);
    	
    	if(!StringUtils.isEmpty(productId)){
            
             List<GtsCarrier> carriers = carrierDao.findAllByProductId(getCarrierAreaName(),productId); 
            
             if(carriers.size() <= 1) {    
                 // remove the product from the table
            
                 productDao.remove(product);
             
             }
             
             this.resetDiscrepancy(carrier.getCarrierNumber());
             
             // check if there are still duplicate products
             this.checkDuplicateProduct(carrier.getCarrierNumber(),productId);
             
        }
        
    	publishAssociationChanged(carrier);
        getLogger().info("Carrier : " + carrier.getCarrierNumber() + ", Product : " + productId + " is de-associated");
        
    }
    
    protected void deassociateCarrierWithProduct(String productId) {
    	
    	List<GtsCarrier> carriers = carrierDao.findAllByProductId(getCarrierAreaName(),productId); 
        
    	for(GtsCarrier carrier :carriers) {
    		carrier.setProductId(null);
        	carrier.setProduct(null);
        	carrierDao.update(carrier);
        	publishAssociationChanged(carrier);
            getLogger().info("Carrier : " + carrier.getCarrierNumber() + ", Product : " + productId + " is de-associated");
    	}
    	
    }
    
    private void checkDuplicateProduct(String carrierId,String productId){
        
        List<GtsLaneCarrier> laneCarriers = laneCarrierDao.findAllByProductId(getAreaName(), productId,getCarrierAreaName());
        
        if(laneCarriers.size() == 0) return;
        
        if(laneCarriers.size() == 1) {
            
            // only one engine in the tracking area and the engine is not in any shipping quorum yet 
            GtsLaneCarrier lc = laneCarriers.get(0);
            
            if(lc.isDuplicateDiscrepancy()) {
                lc.setDiscrepancyStatus(0);
                laneCarrierDao.update(lc);
                
                notifyLaneCarrierChanged(lc.getId().getLaneId());
            }
        
        }else {
            
            for(GtsLaneCarrier lc : laneCarriers) {
    
            	if(lc.isDuplicateDiscrepancy() && !(lc.getCarrierId().equals(carrierId) && lc.isPhotoEyeDiscrepancy())) continue;
                
                if(lc.getCarrierId().equals(carrierId) && lc.isPhotoEyeDiscrepancy()) lc.setPhotoEyeDiscrepancy(false);
                
                lc.setDuplicateDiscrepancy(true);
                laneCarrierDao.updateStatus(lc);
                notifyLaneCarrierChanged(lc.getId().getLaneId());
            }
        }
        
    }
    
    private void resetDiscrepancy(String carrierId) {
        
        
        for(GtsLaneCarrier lc : laneCarrierDao.findAllByCarrierId(getAreaName(), carrierId)){
            
            if(lc.getDiscrepancyStatus() == 0) continue;
                
            lc.setDiscrepancyStatus(0);
            
            laneCarrierDao.update(lc);
            
            notifyLaneCarrierChanged(lc.getId().getLaneId());
        
        }
        
    }
    
    protected void notifyLaneCarrierChanged(String laneId) {
    	List<GtsLaneCarrier> laneCarriers = laneCarrierDao.findAll(getAreaName(),laneId,getCarrierAreaName());
    	laneCarriers = new ArrayList<GtsLaneCarrier>(laneCarriers);
    	getNotificationService().laneCarrierChanged(laneId,laneCarriers);
    }
    
    /**
     * correct carrier label by the user
     * @param laneName - lane name
     * @param position - current position starting at 1
     * @param oldLabel - old label
     * @param newLabel - new label 
     * @return
     * @throws SystemException
     */
    
    @Transactional
    public Message correctCarrierByUser(GtsLaneCarrier laneCarrier, String newLabel) {
        
    	getLogger().info("Received correctCarrierByUser - carrier: " + laneCarrier + ", new label: " + newLabel);
    	
        // update carrier 
    	laneCarrier.setLaneCarrier(newLabel);
    	if(laneCarrier.getCarrier() != null)laneCarrier.getCarrier().getId().setCarrierNumber(newLabel);
    	laneCarrier.setDiscrepancyStatus(0);
        
        laneCarrierDao.update(laneCarrier);
        
        notifyLaneCarrierChanged(laneCarrier.getId().getLaneId());
        
        // check duplicated carrier
        checkDuplicatedCarriers(laneCarrier);
        
        return null;
    }
    
    /**
     * remove a carrier at "position' on a lane "laneNae"
     * @param laneName
     * @param position start from 1
     * @throws SystemException
     */
    
    @Transactional
    public Message removeCarrierByUser(GtsLaneCarrier laneCarrier,int position){
        
    	getLogger().info("Received removeCarrierByUser request - " + laneCarrier + " position: " + position);
    	
        String areaName = laneCarrier.getId().getTrackingArea();
        String laneName = laneCarrier.getId().getLaneId();
        String carrierId = laneCarrier.getCarrierId();
    	List<GtsLaneCarrier> laneCarriers = new ArrayList<GtsLaneCarrier>(laneCarrierDao.findAll(areaName,laneName,getCarrierAreaName()));
        if (position >0 && position <= laneCarriers.size()){
            GtsLaneCarrier lc = laneCarriers.get(position -1);
            if( carrierId.equals(lc.getCarrierId())){
            	laneCarrierDao.remove(lc);
            	notifyLaneCarrierChanged(laneName);
            	if(lc.getProductId() != null) {
            		checkDuplicateProduct(carrierId, lc.getProductId());
            	}
            	
            	getLogger().info("Carrier " + carrierId + " is removed from lane " + laneName);
            	
            	return null;
            } else {
                // log incorrect carrier to be removed
                return Message.logError("Carrier could not be deleted! Carrier " + carrierId + " not found at position" + position);
            }
        }else {
            // log the message about incorrect position
            return Message.logError("Carrier could not be deleted! Position " + position + " is out of range");
        }
        
    }
    
    /**
     * basic logic for add a carrier with specific discrepancy status at a specific position of a lane
     * if the lane is full and the "replaceFlag" is true, the carrier at the end of lane will be replaced
     * @param laneName - lane name
     * @param position - position of the lane a carrier will be inserted 
     * @param carrier  - carrier number to be inserted
     * @param discrepancyStatus - discrepancy Status
     * @param replaceFlag - replace flag
     * @return the flag to indicate if the lane is full or not
     * @throws SystemException
     */
    
    protected GtsLaneCarrier basicAddCarrier(GtsLane lane,int position,String carrier,int discrepancyStatus,boolean replaceFlag) {
        
    	GtsLaneCarrier lc = null;
    	
    	List<GtsLaneCarrier> laneCarriers = laneCarrierDao.findAll(getAreaName(),lane.getLaneName(),getCarrierAreaName());
        
    	 if (position >= lane.getLaneCapacity()) position = lane.getLaneCapacity();
         if(position <= 1) position = 1;
        
        if (laneCarriers.size() >= lane.getLaneCapacity()) {
            
            if(lane.getLaneCapacity() == 0) {

                // invalid configuration - lane capacity = 0
                getLogger().error("The capacity of the lane " + lane.getLaneName() + "is 0");
                return null;
                
            }
            // lane is full
            if(replaceFlag){
                
                // replace the carrier at the position
                lc = laneCarriers.get(position -1);

                // log error - carrier is replaced because lane full
                logErrorAndNotify("Lane = " + lane.getLaneName() + 
                                           ", Carrier " + lc.getCarrierId() +
                                           ", is replaced with carrier " + carrier);
                
                lc.setLaneCarrier(carrier);
                lc.setReaderDiscrepancy(true);
                laneCarrierDao.update(lc);
                notifyLaneCarrierChanged(lane.getId().getLaneId());
            }
            
            return lc;
        }
        
        long time;
        if(position <= laneCarriers.size()){
            long time2 = laneCarriers.get(position -1).getId().getLanePosition().getTime();
            if(position == 1){
                // if add at head of lane shift the time 60 seconds earlier
                time = time2 - 60000;
            }else {
               // if add in the middle, use the timestamp between 2 carriers 
               long time1 = laneCarriers.get(position -2).getId().getLanePosition().getTime();
               time = (time1 + time2)/2;
            }
        }else {
            // if add at the end of lane use the current system time
            time = System.currentTimeMillis();
        }
        lc = new GtsLaneCarrier(getAreaName(),lane.getLaneName());
        lc.getId().setLanePosition(new Timestamp(time));
        lc.setLaneCarrier(carrier);
        lc.setDiscrepancyStatus(discrepancyStatus);
        laneCarrierDao.update(lc);
        
        // log debug - carrier is added to a lane
        getLogger().info("Carrier # : " + carrier + " is added to Lane name : " + 
                        lane.getLaneName() + " Position : " + position);
        
        notifyLaneCarrierChanged(lane.getId().getLaneId());
        return lc;
    }
    
    /**
     * The user wants to add a carrier - the request from the GUI client
     * @param laneName - lane name to which the carrier will be added
     * @param position - the position of the lane the carrier to be added, starting from 1
     * @param carrier - the carrier number to be added
     * @return Message - based on the different condition, the error message will be passed to the GUI client side
     * @throws SystemException
     */
    
    public Message addCarrierByUser(String laneName,int position,String carrier){
    
    	getLogger().info("Received addCarrierByUser request - lane: " + laneName + " position: " + position + " carrier: " + carrier);
    	   
    	if(StringUtils.isEmpty(carrier))
            return Message.logError("null carrier number is not allowed!");
       
    	GtsLane lane = laneDao.findByKey(new GtsLaneId(getAreaName(),laneName));
    	if(lane == null) {
    		return Message.logError("Invalid lane name : " + laneName);
    	}
        GtsLaneCarrier lc = basicAddCarrier(lane,position,carrier,0,false);
        if(lc == null){
            return Message.logError("Lane is full. Can not add carrier!");
        }else {
        	
        	checkDuplicatedCarriers(lc);
        	
        	if(lc.getProductId()!= null) {
        		checkDuplicateProduct(lc.getCarrierId(), lc.getProductId());
        	}
        }
        getLogger().info("carrier " + carrier + " is added to lane " + laneName);
        return null;
    }
    
    public Message updateCarrierType(String carrierId, int carrierType) {
    	
    	getLogger().info("Received update carrier type request - carrier id " + carrierId + " carrier type " + carrierType);
    	
        GtsCarrierType type = GtsCarrierType.getType(carrierType);
        
        if(type == null) return Message.logError("Invalid carrier type : " + carrierType);

    	GtsCarrier carrier = carrierDao.findByKey(new GtsCarrierId(getCarrierAreaName(), carrierId));
    	if(carrier == null) Message.logError("Carrier : " + carrierId + " does not exist");
    	
    	if(!StringUtils.isEmpty(carrier.getProductId())) {
    		return Message.logError("cannot change carrier type due to carrier is associated with a product : " + carrier);
    	}
    	
    	carrier.setCarrierType(type);
    	
    	carrierDao.save(carrier);
    	
        List<GtsCarrier> carriers = new ArrayList<GtsCarrier>();
        carriers.add(carrier);
    	
    	getNotificationService().carrierUpdated(carriers);
    	
    	return null;
        
    }
    
    protected void checkDuplicatedCarriers(GtsLaneCarrier lc) {
    	
    	if(getPropertyBean().isAllowDuplicatedCarrier() ||
    			isEmptyCarrier(lc.getCarrierId()) ||
    			getUnknownCarrier().equals(lc.getCarrierId())) return;
    	
    	getLogger().info("check duplicated carriers " + lc.getCarrierId());
         
    	List<GtsLaneCarrier> updateLaneCarriers = new ArrayList<GtsLaneCarrier>();
    	
    	List<GtsLaneCarrier> allLaneCarriers = laneCarrierDao.findAllByCarrierId(getAreaName(), lc.getCarrierId());
    	
    	Set<String> updateLanes = new HashSet<String>();
        for(GtsLaneCarrier laneCarrier : allLaneCarriers) {
        	if(laneCarrier.getId().equals(lc.getId())) continue;
        	laneCarrier.setLaneCarrier(getUnknownCarrier());
        	updateLaneCarriers.add(laneCarrier);
        	updateLanes.add(laneCarrier.getId().getLaneId());
        }
        
        if(!updateLaneCarriers.isEmpty()) laneCarrierDao.saveAll(updateLaneCarriers);
        
        for(String laneName : updateLanes) {
        	notifyLaneCarrierChanged(laneName);
        }
        
    }
    
    /**
     * add a carrier at a position of a lane triggered by PLC 
     * @param laneName
     * @param position
     * @param carrier
     * @throws SystemException
     */
    
    protected GtsLaneCarrier addCarrier(GtsLane lane,int position,String carrier) {
        return basicAddCarrier(lane,position,carrier,0,true);
    }
    
    public void refreshPLCIndictors() {
    	
    }
    
    /**
     * append a carrier at end of lane, this is triggered by PLC - usually a MIP signal
     * @param laneName
     * @param laneCarrier
     * @throws SystemException
     */
    
    protected void appendCarrier(GtsLane lane,GtsLaneCarrier laneCarrier) {
        
        basicAddCarrier(lane,Integer.MAX_VALUE,laneCarrier.getCarrierId(),laneCarrier.getDiscrepancyStatus(),true);
    }
 
    protected void updateLabelText(int labelId, String value){
        
	   	 String area = getAreaName();
	   	 GtsLabelId gtsLabelId= new GtsLabelId(area, labelId);
	   	 
	   	 GtsLabel label =labelDao.findByKey(gtsLabelId);
	   	 if(label != null ) {
	   		 label.setLabelText(value);
     		 labelDao.update(label);
      	   	 getNotificationService().labelUpdated(label);
	   	 }    
	   	 
   }

    /**
     * toggle the open/close status of the gate with node id "noteId"
     * if node.status = 2 , close the gate
     * @param nodeId
     * @throws SystemException
     */
    
    public void toggleGateStatus(GtsNode node){
    	if(node == null) return;
    	if(node.isPLCGate()) {
    		// sending gate request 
    		String deviceId = "GR-" + node.getNodeName();
    		sendIndicator(deviceId,node.isGateOpen() ? "0" : "1");
    	}else if(node.isNegativePLCGate()) {
    		// sending negative gate request 
    		String deviceId = "NGR-" + node.getNodeName();
    		sendIndicator(deviceId,node.isGateOpen() ? "1" : "0");
    	}else {
    		basicToggleGateStatus(node);
    	}
    }
    
    protected void basicToggleGateStatus(GtsNode node) {
    	 if(node == null)return;
         if(node.isLaneClosed()) node.closeLane();
         else node.setGateStatus(!node.isGateOpen());
         nodeDao.updateNodeStatus(node.getId().getTrackingArea(), node.getNodeName(), node.getStatus());
         
         this.getNotificationService().gateStatusChanged(node.getId().getNodeId(),node.getStatus());
         
         getLogger().info("Gate " + node.getNodeName() + " is " +(node.isGateOpen() ? "opened" : "closed"));
         
         //      check expired moves
         List<GtsMove> expiredMoves = checkMoveExpired();
         
         // make move decision
         makeMoveDecision(node, expiredMoves);
    }
    
    protected void makeMoveDecision(GtsIndicator indicator){
        if(decisionController != null) {
        	 //      check expired moves
            List<GtsMove> expiredMoves = checkMoveExpired();
           
        	decisionController.executeRules(indicator,expiredMoves);
        }
    }
    
    protected void makeMoveDecision(GtsNode gate, List<GtsMove> expiredMoves){
        if(decisionController != null) {
        	decisionController.executeRules(gate, expiredMoves);
        }
    }
 
    
    /**
     * Check if any created moves are expired
     * @throws SystemException
     */
    
    public List<GtsMove> checkMoveExpired(){
        
    	List<GtsMove> expiredMoves = new ArrayList<GtsMove>(); 
    	
        List<GtsMove> moves = moveDao.findAll(getAreaName());
        
    	for(GtsMove move: moves) {
            
            if(move.isMoveExpired()) {

            	if(move.isCreated()) {
                
            		move.setMoveStatus(GtsMoveStatus.EXPIRED);
                
            		expiredMoves.add(move);
                
            		moveDao.update(move);
                
            		logWarnAndNotify("Move Request " + move + " expired");
                
            		// notify the GUI client the move request is created 
            		getNotificationService().moveStatusChanged(move);
                
            	}
            }
        }
    	
    	return expiredMoves;
        
    }
    
    /**
     * Check move possible - called from GUI client
     * @param move
     * @return
     * @throws SystemException
     */
    
    public Message checkMovePossible(GtsMove move) throws SystemException{
        
        //      check expired moves
  //      checkMoveExpired();
        
        
        if(decisionController != null) {
            return decisionController.checkMovePossible(move);
        }
        
        return null;
    }
    
    @Transactional
    public Message createMoveRequest(GtsMove move) throws SystemException{
        
        Message message = null; 
        
        //      check expired moves
        //    checkMoveExpired();
        
        if(decisionController != null) {
            
            message = decisionController.checkMovePossible(move);
            if(message != null) return message;
            
            move.setMoveStatus(GtsMoveStatus.CREATED);
            
            if(!this.sendMoveRequestToPLC(move)) 
                return Message.logError("Move could be created! There are possible communication problems" );
        }
        
        moveDao.update(move);
        
        return null;
        
    }
    
    public void checkDefectRepaired() {
    	List<GtsProduct> products = productDao.findAllWithDefects(getCarrierAreaName());
    	
    	List<GtsProduct> updatedProducts = new ArrayList<GtsProduct>();
    	
    	for(GtsProduct product : products) {
    		GtsDefectStatus defectStatus = getDefectStatus(product.getProductId());
    		
    		if(defectStatus == GtsDefectStatus.REPAIRED || defectStatus == GtsDefectStatus.DIRECT_PASS) {
    			product.setDefectStatus(defectStatus.getId());
    			updatedProducts.add(product);
    		}
    	}
    	
    	if(!updatedProducts.isEmpty()) {
    		productDao.saveAll(updatedProducts);
    		getNotificationService().productStatusChanged(products);
    	}
    }
    
    public void checkProductPopulated() {
    	List<GtsCarrier> carriers = carrierDao.findAllNoProduct(getAreaName(), getCarrierAreaName());
    	for(GtsCarrier carrier : carriers) {
    		if (!StringUtils.isEmpty(carrier.getProductId())) {
    			GtsProduct product = populateProduct(carrier.getProductId());
    			if(product != null) {
    				List<GtsLaneCarrier> laneCarriers = laneCarrierDao.findAllByCarrierId(getAreaName(), carrier.getCarrierNumber());
    				for(GtsLaneCarrier lc : laneCarriers) {
    					notifyLaneCarrierChanged(lc.getId().getLaneId());
    				}
    			}
    		}
    	}
    }
    
    public GtsArea fetchArea() {
    	GtsArea area = areaDao.findByKey(getAreaName());
    	
    	fetchLanes(area);
    	
    	area.setMoveConditions(moveConditionDao.findAll(getAreaName()));
    	area.setIndicators(indicatorDao.findAll(getAreaName()));
    	area.setNodes(nodeDao.findAll(getAreaName()));
    	area.setMoves(moveDao.findAll(getAreaName()));
    	area.setDecisionPoints(decisionPointDao.findAll(getAreaName()));
    	area.setDecisionPointConditions(decisionPointConditionDao.findAll(getAreaName()));
    	return area;
    }
    
    private List<GtsLane> fetchLanes(GtsArea area) {
    	Timestamp startTime= new Timestamp(System.currentTimeMillis());
    	
    	List<GtsLane> lanes = laneDao.findAll(getAreaName());
    	area.setLanes(lanes);
    	
    	List<GtsLaneCarrier> laneCarriers = ServiceFactory.getDao(GtsLaneCarrierDao.class).findAll(getAreaName(),getCarrierAreaName());
    	
    	for(GtsLaneCarrier laneCarrier : laneCarriers) {
    	
    		GtsLane lane=area.findLane(laneCarrier.getId().getLaneId());
    		lane.getLaneCarriers().add(laneCarrier);
    		laneCarrier.setLane(lane);
    	}

    	Timestamp endTime= new Timestamp(System.currentTimeMillis());
	    
    	getLogger().debug("start time " + startTime + ", end time " + endTime);
    	
    	return lanes;

    }

    public void updateLabel(GtsLabel label) {
    	
    }
    
    public void updateDecisionPoint(GtsDecisionPoint decisionPoint) {
    	decisionPointDao.update(decisionPoint);
    }
    
    public void updateMove(GtsMove move) {
    	moveDao.update(move);
    }
    
    /**
     * send move request to PLC
     * @param move - move request
     * @return if the move request is successfully sent
     */
    
    public boolean sendMoveRequestToPLC(GtsMove move){
        
        DataContainer dc = new DefaultDataContainer();
        dc.put(move.getIoPointName(), "TRUE");
        
        try{
            
            // send the move request to PLC
            sendToPLC(getAreaName()+ "." + move.getIoPointName(), dc);
            
            // notify the GUI client the move request is created 
            getNotificationService().moveStatusChanged(move);
            
            return true;
            
        }catch(SystemException e) {
        	logErrorAndNotify("Failed to Send Move Request to PLC! - Move Request = " + move.getIoPointName() + "Cause: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * write device - deviceId is "Area Name"."ioPointName" 
     * ioPointName is also the tag name in the device data format 
     * @param ioPointName
     * @param value
     * @return
     */
    public boolean sendIndicator(String ioPointName, Object value) {
    	 DataContainer dc = new DefaultDataContainer();
         dc.put(ioPointName, value);
         
         try{
             
             // send the move request to PLC
             sendToPLC(getAreaName()+ "." + ioPointName, dc);
             
             return true;
         }catch(SystemException e) {
         	logErrorAndNotify("Failed to Send Data to PLC! - " + ioPointName + "Cause: " + e.getMessage());
         }
         
         return false;
    }
    
    protected DataContainer syncSendToPLC(String deviceId, DataContainer dc) {
    	Device device = deviceDao.findByKey(deviceId);
    	DataContainer returnDC = null;
		if(device == null) {
			getLogger().error("Invalid Configuration - no device found : " + deviceId);
			return null;
		}
		device.populate(dc);
    	DataContainer dataContainer = device.toDataContainer();
    	try{
			if(device.isDeviceWiseType()) {
	    		JsonClient jsonClient = new JsonClient(device.getEifIpAddress(),5000,15000);
	    		returnDC = jsonClient.syncSend(dc, true);
			}else {
				DataContainerSocketSender socketSender
					= new DataContainerSocketSender(device.getEifIpAddress(),device.getEifPort());
				returnDC = socketSender.syncSend(dataContainer);
			}
    		
			getLogger().info("sent data : " + dataContainer.toString());
		}catch(Exception e) {
			getLogger().error(e,"Failed to sync send data " + dataContainer.toString() + " to device " + device );
		}
		return returnDC;
    }
    
    protected void sendToPLC(String deviceId, DataContainer dc) {
    	Device device = deviceDao.findByKey(deviceId);
    	
    	if(device == null) {
			getLogger().error("Invalid Configuration - no device found : " + deviceId);
			return;
		}
    	
     	device.populate(dc);
    	DataContainer dataContainer = device.toDataContainer();
    	dataContainer.setClientID(device.getClientId());
		
		try{
			
			if(device.isDeviceWiseType()) {
				JsonClient jsonClient = new JsonClient(device.getEifIpAddress(),5000,15000);
	    		jsonClient.syncSend(dataContainer, true);
			}else {
				DataContainerSocketSender socketSender
					= new DataContainerSocketSender(device.getEifIpAddress(),device.getEifPort());
				socketSender.send(dataContainer);
			}
    		
			getLogger().info("sent data : " + dataContainer.toString());
		}catch(Exception e) {
			getLogger().error(e,"Failed to sync send data " + dataContainer.toString() + " to device " + device );
		}
    	
    }
    
    protected DataContainer syncReadFromPLC(String deviceId) {
    	Device device = deviceDao.findByKey(deviceId);
    	return syncReadFromPLC(device);
    }
    
    protected DataContainer syncReadFromPLC(Device device) {
    	DataContainer dc = new DefaultDataContainer();
    	DataContainer returnDC = null;
		if(device == null) {
			getLogger().error("Invalid Configuration - no device found " );
			return null;
		}
		dc.setClientID(device.getClientId());
		DataContainerUtil.setEquipmentRead(dc);
    	try{
    		if(device.isDeviceWiseType()) {
    			JsonClient jsonClient = new JsonClient(device.getEifIpAddress(),5000,15000);
    			returnDC = jsonClient.syncSend(dc, true);
    		}else {
    			DataContainerSocketSender socketSender
					= new DataContainerSocketSender(device.getEifIpAddress(),device.getEifPort());
    			returnDC = socketSender.syncSend(dc);
    		}
			getLogger().info("sent data : " + dc.toString());
		}catch(Exception e) {
			getLogger().error(e,"Failed to sync send data " + dc.toString() + " to device " + device );
		}
    	if(returnDC != null) {
    		getLogger().info("return dc :" + returnDC.toString());
    	}
		return returnDC;
    }
    
    protected IGtsNotificationService getNotificationService() {
    	return GtsNotificationSender.getNotificationService(getAreaName());
    }
    
    protected void logErrorAndNotify(String message) {
    	Message msg = new Message(MessageType.ERROR,message);
    	getLogger().error(message);
    	getNotificationService().message(msg);
    }
    
    protected void logWarnAndNotify(String message) {
    	Message msg = new Message(MessageType.WARN,message);
    	getLogger().warn(message);
    	getNotificationService().message(msg);
    }
    
    @Override
    public String getProcessPointId() {
    	if(this.processPointId == null) {
    		this.processPointId = applicationTaskDao.findApplicationId(getTrackingServiceInterfaceName());
    	}
    	return this.processPointId;
    }
    
    public String getUnknownCarrier() {
    	return getPropertyBean().getUnknownCarrier();
    }
    
    public String getEmptyCarrier() {
    	return getPropertyBean().getEmptyCarrier();
    }
    
    public boolean isEmptyCarrier(String carrier) {
    	if(StringUtils.isEmpty(carrier)) return false;
    	String emptyCarrier = getPropertyBean().getEmptyCarrier();
    	if(StringUtils.isEmpty(emptyCarrier)) return false;
    	return emptyCarrier.equalsIgnoreCase(carrier);
    }
    
    public boolean isUnsolicitedDevice(Device device) {
    	return Arrays.asList(getPropertyBean().getUnsolicitedDataList()).contains(device.getClientId());
    }	
    
    public GtsPropertyBean getPropertyBean() {
    	return PropertyService.getPropertyBean(GtsPropertyBean.class, getProcessPointId());
    }
    
    public void holdResultChanged(List<HoldResult> holdResults) {
    	List<GtsProduct> products = new ArrayList<GtsProduct>();
    	
    	for(HoldResult holdResult : holdResults) {
    	
    		GtsProduct product = productDao.findByKey(new GtsProductId(getCarrierAreaName(), holdResult.getId().getProductId()));
    		if(product == null) continue;
    		
    		product.setProductStatus(getProductStatus(holdResult));
    		products.add(product);
    	}
    	
    	productDao.updateAll(products);
        
        getNotificationService().productStatusChanged(products);
    
    }
    
    public String getCarrierAreaName() {
    	String carrerAreaName = getPropertyBean().getCarrierArea();
    	return StringUtils.isEmpty(carrerAreaName) ? getAreaName() : carrerAreaName;
    }
    
    public abstract String getAreaName();
    
    // set the move decision controller
    // sub class of this has to implement this method
    abstract public void setDecisionController();
    
    // 
    public abstract String getTrackingServiceInterfaceName();
 
}
