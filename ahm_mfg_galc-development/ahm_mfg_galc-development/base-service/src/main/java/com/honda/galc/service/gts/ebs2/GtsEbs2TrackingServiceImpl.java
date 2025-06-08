package com.honda.galc.service.gts.ebs2;

import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.message.Message;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.gts.GtsIndicator;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsNode;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.Product;
import com.honda.galc.service.gts.AbstractBodyTrackingService;
import com.honda.galc.service.gts.GtsEbs2TrackingService;

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
public class GtsEbs2TrackingServiceImpl extends AbstractBodyTrackingService implements GtsEbs2TrackingService{

	private static String LANE_E9 ="E9";
	private static int LABEL_ID_DOLLY_COUNT_E9 = 44;

	@Autowired
	protected FrameDao frameDao;
	
	
	@Override
	public String getAreaName() {
		return "P2EBS";
	}

	@Override
	public void setDecisionController() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTrackingServiceInterfaceName() {
		return "GtsEbs2TrackingService";
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
    public void processIndicator(GtsIndicator indicator) {
		super.processIndicator(indicator);
        
		if(getPropertyBean().isCheckDefectRepaired())
        	checkDefectRepaired();
		
		checkProductPopulated();
		
	}
	
	@Override
    public void toggleGateStatus(GtsNode node){
		super.toggleGateStatus(node);
		
		if(getPropertyBean().isCheckDefectRepaired())
        	checkDefectRepaired();
		
		checkProductPopulated();
		
	}
	
	@Override
	protected List<GtsLaneCarrier> readBodyPositionsFromPLC(String laneName) {

		String count="";
		if(laneName.equalsIgnoreCase(LANE_E9)) {
			String countName = "DOLLY_COUNT-E9";
			String deviceId = getAreaName() + Delimiter.DOT + countName;
		    DataContainer dc = syncReadFromPLC(deviceId);		     
		    if(dc != null) count = dc.getString(countName);	    	 
		   
 		    updateLabelText(LABEL_ID_DOLLY_COUNT_E9, count);
 		    return null;
		}
	   
	    return super.readBodyPositionsFromPLC(laneName);
	}    

}
