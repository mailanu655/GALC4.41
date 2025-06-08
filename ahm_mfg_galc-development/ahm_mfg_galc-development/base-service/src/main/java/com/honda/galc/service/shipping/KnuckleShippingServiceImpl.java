package com.honda.galc.service.shipping;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.RequiredPartDao;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.dao.product.SubProductShippingDao;
import com.honda.galc.dao.product.SubProductShippingDetailDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductionLotHelper;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.entity.product.SubProductShipping;
import com.honda.galc.entity.product.SubProductShippingDetail;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.datacollection.IoServiceBase;
import com.honda.galc.service.printing.KnuckleShippingPrintingUtil;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>KnuckleShippingService Class description</h3>
 * <p> KnuckleShippingService description </p>
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
 * Jan 15, 2013
 *
 *
 */
public class KnuckleShippingServiceImpl extends IoServiceBase implements KnuckleShippingService{

	@Autowired
	public SubProductDao subProductDao;
	
	@Autowired
	public SubProductShippingDao subProductShippingDao;
	
	@Autowired
	public SubProductShippingDetailDao subProductShippingDetailDao;
	
	@Autowired
	public PreProductionLotDao preProductionLotDao;
	
	@Autowired
	public TrackingService trackingService;
	
	@Override
	public DataContainer processData() {
		
		String ksn = (String)getDevice().getInputValue("PRODUCT_ID");
		int status = (Short)getDevice().getInputValue("STATUS");
        
		SubProduct knuckle = subProductDao.findByKey(ksn);
		if(knuckle == null) {
			getLogger().error("Knuckle " + ksn + "does not exist");
			return dataCollectionComplete(false);
		}
		PreProductionLot preProductionLot = preProductionLotDao.findByKey(knuckle.getProductionLot());
		
		int lotPosition = getLotPosition(preProductionLot,knuckle);

		getLogger().info("Current Knuckle " + knuckle.getProductId() + " lot position " + lotPosition + " in " + preProductionLot);
		
		// find whole list of shipping kd lots. If not exists , create the whole shipping lots
		List<SubProductShipping> shippingLots = findShippingLots(preProductionLot, knuckle);
		
		// find current shipping lot
		SubProductShipping shippingLot = findShippingLot(shippingLots,preProductionLot);
		
		SubProductShippingDetail detail = subProductShippingDetailDao.findByProductId(ksn);
		if(detail != null) {
			getLogger().warn("Product " + ksn + " was loaded already " + detail);
			return dataCollectionComplete(true);
		}
		
		int lotSequence = getLotSequence(shippingLots, preProductionLot, lotPosition);
		// save shipping detail
		SubProductShippingDetail shippingDetail = new SubProductShippingDetail(shippingLot.getKdLotNumber(),shippingLot.getProductionLot(),knuckle.getSubId(),lotSequence);
		boolean isShippable = status == 1 && !isMissingRequiredParts(preProductionLot.getProductSpecCode(), knuckle.getProductId(), knuckle.getSubId());
		if(isShippable)shippingDetail.setProductId(ksn);
		subProductShippingDetailDao.save(shippingDetail);
		
		if(isShippable) shippingLot.incrementActQuantity();
		// increase the actual quantity
		subProductShippingDao.save(shippingLot);
		
		int kdLotSize = getKdLotSize(shippingLots);
		int kdLotSequence = getKdLotSequence(shippingLots, preProductionLot, lotPosition);
		
		getLogger().info("Current KD Lot Sequence = "+ kdLotSequence + "/" + kdLotSize); 
		
		// print the shipping cart
		// If the current kd lot sequence is the end of the kd lot or at the cart size, do printing
		if(kdLotSequence == kdLotSize || kdLotSequence % getCartSize() == 0){
			List<SubProductShippingDetail> details = 
				fetchShippingDetails(shippingLots,knuckle.getSubId(),kdLotSequence - getCartSize() + 1);

			DataContainer dc = new KnuckleShippingPrintingUtil().print(shippingLots, details,preProductionLot.getProcessLocation());
			getLogger().info("issued shipping cart printing " +dc);
		}
		 // tracking good ksn only
		if(isShippable) trackingService.track(knuckle, getProcessPointId());
		
		return dataCollectionComplete(isShippable);
	}
	
	private List<SubProductShippingDetail> fetchShippingDetails(List<SubProductShipping> shippingLots,String side,int start){
		List<SubProductShippingDetail> shippingDetails = fetchShippingDetails(shippingLots,side);
		List<SubProductShippingDetail> shippingCartDetails = new ArrayList<SubProductShippingDetail>();
		int i = 0;
		for(SubProductShippingDetail detail : shippingDetails) {
			if(contains(shippingLots,detail)){
				i++;
				if(i>= start && i < start + getCartSize())
					shippingCartDetails.add(detail);
			}
		}
		return shippingCartDetails;
	}
	
	private List<SubProductShippingDetail> fetchShippingDetails(List<SubProductShipping> shippingLots,String side){
		List<SubProductShippingDetail> shippingDetails = new ArrayList<SubProductShippingDetail>();
		for(SubProductShipping shippingLot : shippingLots) {
			shippingDetails.addAll(subProductShippingDetailDao.findAllByShippingLot(shippingLot.getKdLotNumber(),shippingLot.getProductionLot(),side));
		}
		return shippingDetails;
	}
	
	private boolean isMissingRequiredParts(String productSpecCode,String ksn, String side){
		 // check if the knuckle has missing required parts
	    List<String> missingRequiredParts = getDao(RequiredPartDao.class).findMissingRequiredParts(
	    		productSpecCode, getProcessPointId(), ProductType.KNUCKLE,ksn,side);
	    if(!missingRequiredParts.isEmpty()) 
	    	getLogger().error("Missing Required Parts or incomplete parts: " + ArrayUtils.toString(new ArrayList<String>(missingRequiredParts)));
	    return !missingRequiredParts.isEmpty();
	}
	
	private boolean contains(List<SubProductShipping> shippingLots,SubProductShippingDetail detail){
		for(SubProductShipping shippingLot : shippingLots) {
			if(shippingLot.getProductionLot().equalsIgnoreCase(detail.getProductionLot())) return true;
		}
		return false;
	}
	
	private int getLotPosition(PreProductionLot lot,SubProduct knuckle){
		int startSn = ProductionLotHelper.getStartProductSn(lot.getStartProductId(), knuckle.getSubId());
		int sn = knuckle.getSerialNumber();
		return sn - startSn + 1;
	}
	
	// find whole list of shipping kd lots. If not exists , create the whole shipping lots
	private List<SubProductShipping> findShippingLots(PreProductionLot lot,SubProduct knuckle) {
		List<SubProductShipping> shippingLots = subProductShippingDao.findAllWithSameKdLot(lot.getKdLot(),lot.getProductionLot());
		
		return !shippingLots.isEmpty() ? 
					filteredShippingLots(shippingLots,lot) : createShippingLots(lot);
	}
	
	private SubProductShipping findShippingLot(List<SubProductShipping> shippingLots,PreProductionLot lot){
		for(SubProductShipping shippingLot : shippingLots) {
			if(shippingLot.getProductionLot().equalsIgnoreCase(lot.getProductionLot())) return shippingLot;
		}
		return null;
	}
	
	private int getKdLotSequence(List<SubProductShipping> shippingLots,PreProductionLot lot,int currentLotPosition) {
		int kdLotSequence = 0;
		for(SubProductShipping shippingLot : shippingLots) {
			if(shippingLot.getProductionLot().equalsIgnoreCase(lot.getProductionLot())) 
				return kdLotSequence + currentLotPosition;
			else kdLotSequence += shippingLot.getSchQuantity()/2;
		}
		return kdLotSequence;
	}
	
	private int getLotSequence(List<SubProductShipping> shippingLots,PreProductionLot lot,int currentLotPosition){
		int lotSequence = 0;
		for(SubProductShipping item : shippingLots) {
			if(item.getProductionLot().equals(lot.getProductionLot())) break;
			if(item.getKdLotNumber().equals(lot.getKdLot())) lotSequence +=item.getSchQuantity() / 2;
		}
		return lotSequence + currentLotPosition;
	}
	
	private int getKdLotSize(List<SubProductShipping> shippingLots) {
		int kdLotSize = 0;
		for(SubProductShipping shippingLot : shippingLots)
			kdLotSize +=  shippingLot.getSchQuantity()/2;
		
		return kdLotSize;
		
	}
	
	/**
	 * This is to remove the lots with same kd lot but in a different batch
	 * note : same kd lot could be reused on diffrent days if some products have to be remade
	 */
	private List<SubProductShipping> filteredShippingLots(List<SubProductShipping> shippingLots, PreProductionLot lot) {
		int index = 0;
		int ix = 0;
		for(SubProductShipping shippingLot : shippingLots) {
			if(shippingLot.getProductionLot().equalsIgnoreCase(lot.getProductionLot())) {
				index = ix;
				break;
			}
			ix++;
		}
		int seq = shippingLots.get(index).getSeqNo();
		int start = 0,end = seq = shippingLots.size() - 1;
		for(int i = index -1 ; i>=0; i--){
			int seqno = shippingLots.get(i).getSeqNo();
			if(seq == seqno + 1000) start = i;
			seq -= 1000;
		}
		
		seq = shippingLots.get(index).getSeqNo();
		for (int j = index + 1; j < shippingLots.size();j++){
			int seqno = shippingLots.get(j).getSeqNo();
			if(seqno == seq + 1000) end = j;
			seq += 1000;
		}
		List<SubProductShipping> subList = new ArrayList<SubProductShipping>();
		subList.addAll(shippingLots);
		return subList.subList(start, end + 1);
		
	}
	
	private List<SubProductShipping> createShippingLots(PreProductionLot lot){
		List<SubProductShipping> shippingLots = subProductShippingDao.createKnuckleShippingLots(lot.getProductionLot());
		for(SubProductShipping shippingLot : shippingLots) {
			getLogger().info("Created new shipping lot - " + shippingLot);
		}
		return shippingLots;
	}
	
	private int getCartSize() {
		return PropertyService.getPropertyInt("KNUCKLE SHIPPING","CART_SIZE", 15);
	}

}
