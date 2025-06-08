package com.honda.galc.service.gts.pbs;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.gts.GtsCarrierId;
import com.honda.galc.entity.product.Product;
import com.honda.galc.service.gts.AbstractBodyTrackingService;
import com.honda.galc.service.gts.GtsTbs1TrackingService;

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
public class GtsTbs1TrackingServiceImpl extends AbstractBodyTrackingService implements GtsTbs1TrackingService{
	
	private static String CARRIER_MISS_READ = "FFFF";
	
		
	@Autowired
	protected FrameDao frameDao;
	
	@Override
	public String getAreaName() {
		return "P1TBS";
	}

	@Override
	public void setDecisionController() {
		this.decisionController = new GtsTbs1MoveDecisionController(this);
	}

	@Override
	public String getTrackingServiceInterfaceName() {
		return "GtsTbs1TrackingService";
	}
	
	@Override
	public Product getProduct(String productId) {
		return frameDao.findByKey(productId);
	}
	
	@Override
	public GtsCarrier findCarrier(String carrierId) {
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
				return carrierDao.save(new GtsCarrier(getCarrierAreaName(),carrierId));
			}
		}
		return carrier;
	}
	
}
