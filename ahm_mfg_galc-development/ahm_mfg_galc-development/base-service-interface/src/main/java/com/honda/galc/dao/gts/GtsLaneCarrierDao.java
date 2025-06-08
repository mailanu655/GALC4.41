package com.honda.galc.dao.gts;

import java.util.List;

import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsLaneCarrierId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * 
 * <h3>GtsLaneCarrierDao Class description</h3>
 * <p> GtsLaneCarrierDao description </p>
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
public interface GtsLaneCarrierDao extends IDaoService<GtsLaneCarrier, GtsLaneCarrierId>{
	
	/**
	 * get list of lane carriers - sorted by lane position ascending.
	 * this query will fill the related GtsCarrier and GtsProduct objects
	 * @param trackingArea
	 * @param laneId
	 * @return
	 */
	public List<GtsLaneCarrier> findAll(String trackingArea, String laneId,String carrierArea);
	
	/**
	 * get list of lane carriers - sorted by lane name and lane position ascending.
	 * this query will fill the related GtsCarrier and GtsProduct objects
	 * @param trackingArea
	 * @param carrierArea
	 * @return
	 */
	public List<GtsLaneCarrier> findAll(String trackingArea,String carrierArea);
	
	/**
	 * This only returns LaneCarriers without GtsCarrier and GtsProduct objects
	 * @param trackingArea
	 * @param carrierId
	 * @return
	 */
	public List<GtsLaneCarrier> findAllByCarrierId(String trackingArea, String carrierId);
	
	/**
	 * This only returns LaneCarriers without GtsCarrier and GtsProduct objects
	 * @param trackingArea
	 * @param productId
	 * @param carrierArea
	 * @return
	 */
	public List<GtsLaneCarrier> findAllByProductId(String trackingArea, String productId,String carrierArea);
	
	/**
	 * replace target position with source lane carrier
	 * @param target
	 * @param source
	 * @return
	 */
	public void replaceCarrier(GtsLaneCarrier target, GtsLaneCarrier source);
	
	public int appendCarrier(String targetLane, GtsLaneCarrier source);
	
	public int updateStatus(GtsLaneCarrier laneCarrier);
	
	public int removeAll(String trackingArea, String laneId);
	
	public int isLaneLogicallyFull(String trackingArea, String laneId);
		
}
