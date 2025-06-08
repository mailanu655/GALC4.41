package com.honda.galc.service.gts.wbs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.message.Message;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.GtsCarrierStatus;
import com.honda.galc.entity.enumtype.GtsDefectStatus;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.gts.GtsCarrierId;
import com.honda.galc.entity.gts.GtsIndicator;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsNode;
import com.honda.galc.entity.gts.GtsProduct;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.Product;
import com.honda.galc.service.gts.AbstractBodyTrackingService;
import com.honda.galc.service.gts.GtsWbs1TrackingService;

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
public class GtsWbs1TrackingServiceImpl extends AbstractBodyTrackingService implements GtsWbs1TrackingService{
	
	
	private final String U_LS_PaintPallet_CARRIER = "U-LS-PaintPallet-CARRIER";
	private final String DEVICE_PALLET_CARRIER = "PALLET_CARRIER";
	private final String CARRIER_RETURN = "BC-CR";
	private final String LANE_CTR = "CTR";
	
	@Autowired
	protected FrameDao frameDao;
	
	
	@Override
	public String getAreaName() {
		return "P1WBS";
	}

	@Override
	public void setDecisionController() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTrackingServiceInterfaceName() {
		return "GtsWbs1TrackingService";
	}
	

	@Override
	public Product getProduct(String productId) {
		return frameDao.findByKey(productId);
	}
	
	@Override
	protected String converFromShortProductId(String productId) {
		if(StringUtils.length(productId) >0 && StringUtils.length(productId) <= 10) {
			List<Frame> frames = frameDao.findAllByShortVin(productId);
			return frames.size() == 1 ? frames.get(0).getProductId() : "";
		}	
   	 	return productId;
    }

	@Override
	public void refreshPLCIndictors() {
		
		String[] unsolicitedDataList = getPropertyBean().getUnsolicitedDataList();
		for(String deviceId :unsolicitedDataList) {
			Device device = deviceDao.findByKey(deviceId);
    	
			DataContainer dc = this.syncReadFromPLC(device);
		
			if(dc == null) return;
			
			device.populate(dc);
		
			for(DeviceFormat deviceFormat : device.getDeviceDataFormats()) {
			
				GtsIndicator indicator = new GtsIndicator(getAreaName(),deviceFormat.getTag(),ObjectUtils.toString(deviceFormat.getValue()));
				if(indicator.getIndicatorType() == null) continue;
			
				processIndicator(indicator);
			}
		}
		
	}
	
	@Transactional
    public Message correctCarrierByUser(GtsLaneCarrier laneCarrier, String newLabel) {
		Message message = super.correctCarrierByUser(laneCarrier, newLabel);
		writeBodyPositionsToPLC(laneCarrier.getLane().getLaneName());
	    
		return message;	
	}
	
	@Transactional
	public Message addCarrierByUser(String laneName,int position,String carrier){
		Message message = super.addCarrierByUser(laneName, position,carrier);
		writeBodyPositionsToPLC(laneName);
	
		return message;	
	}   
	
	 @Transactional
	 public Message removeCarrierByUser(GtsLaneCarrier laneCarrier,int position){
		Message message = super.removeCarrierByUser(laneCarrier, position);
		writeBodyPositionsToPLC(laneCarrier.getId().getLaneId());
		
		return message;	
	} 
	 
	public String getUnknownCarrier() {
		return "999";
	}
	
	@Override
    public void receiveSpecialRequest(GtsIndicator indicator) {
    	 super.receiveSpecialRequest(indicator);
    	 
    	 processPaintPallets(indicator);
  
	}
	
	@Override
    public void processIndicator(GtsIndicator indicator) {
		super.processIndicator(indicator);
        
		if(getPropertyBean().isCheckDefectRepaired())
        	checkDefectRepaired();
		
		checkProductPopulated();
		
		checkDefectStatusChanged();
	}
	
	@Override
    public void toggleGateStatus(GtsNode node){
		super.toggleGateStatus(node);
		
		if(getPropertyBean().isCheckDefectRepaired())
        	checkDefectRepaired();
		
		checkProductPopulated();
		
		checkDefectStatusChanged();

	}
	
	public void checkDefectStatusChanged() {
		List<GtsProduct> products = new ArrayList<GtsProduct>();
		List<GtsLaneCarrier> laneCarriers = laneCarrierDao.findAll(getAreaName(),LANE_CTR,getCarrierAreaName());
		for(GtsLaneCarrier lc : laneCarriers) {
			GtsProduct product = lc.getProduct();
			if(product != null) {
				GtsDefectStatus defectStatus= this.getDefectStatus(product.getProductId());
				if(!product.getDefectStatus().equals(defectStatus)) {
					product.setDefectStatus(defectStatus.getId());
					products.add(product);
				}
				
			}
		}
		
		if(!products.isEmpty()) {
			productDao.updateAll(products);
			getNotificationService().productStatusChanged(products);
		}
    	
	}
	
	@Override
    public List<GtsLaneCarrier> receiveBodyCountChange(GtsIndicator newIndicator) {
		List<GtsLaneCarrier> laneCarriers = super.receiveBodyCountChange(newIndicator);
		if(newIndicator.getIndicatorName().equalsIgnoreCase(CARRIER_RETURN)) {
			if(!laneCarriers.isEmpty()) {
				GtsLaneCarrier laneCarrier = laneCarriers.get(0);
				GtsCarrier carrier = carrierDao.findByKey(new GtsCarrierId(getCarrierAreaName(), laneCarrier.getCarrierId()));
				if(carrier != null && !StringUtils.isEmpty(carrier.getProductId())) 
					this.deassociateCarrierWithProduct(carrier);
			}
		}
		
		return laneCarriers;
	} 
	
	public void processPaintPallets(GtsIndicator indicator) {
		if(indicator.getIndicatorName().equalsIgnoreCase(U_LS_PaintPallet_CARRIER) && indicator.isStatusOn()) {
	   		 List<GtsCarrier> carriers = carrierDao.findAllByCarrierStatus(getAreaName(), GtsCarrierStatus.PALLET.getId());
	   		 
	   		 // this is to avoid JPA issue Result lists are read-only
	   		 carriers = new ArrayList<GtsCarrier>(carriers);
	   		 
	   		 DataContainer dc = syncReadFromPLC(getAreaName() + Delimiter.DOT + DEVICE_PALLET_CARRIER);
	   		 List<String> tags = getDataPointNames(dc,DEVICE_PALLET_CARRIER+ Delimiter.HYPHEN);
	   		 List<GtsCarrier> updateCarriers = new ArrayList<GtsCarrier>();
	   		 
	   		 for(String tag :tags) {
	   			 int carrierId = DataContainerUtil.getInteger(dc, tag, -1);
	   			 if(carrierId == 0 ) continue;
	   			 GtsCarrier thisCarrier = carrierDao.findByCarrierId(getAreaName(), carrierId);
	   			 if(thisCarrier != null) {
	   			   thisCarrier.setStatus(GtsCarrierStatus.PALLET);
	   			   updateCarriers.add(thisCarrier);
	   			   if(carriers.contains(thisCarrier)) {
	   				  carriers.remove(thisCarrier); 
	   			   }
	   			 }	 
	  		 }
	   		 
	   		 for(GtsCarrier carrier : carriers) {
	   			 carrier.setStatus(GtsCarrierStatus.NORMAL);
	   		 }
	            
	   		 updateCarriers.addAll(carriers);
	   		 
	   		 if(!updateCarriers.isEmpty()) {
	   			 carrierDao.saveAll(updateCarriers);
	   		 }
	   		 
	   		 getNotificationService().carrierUpdated(updateCarriers);
	   		 
  		 }
	}

}
