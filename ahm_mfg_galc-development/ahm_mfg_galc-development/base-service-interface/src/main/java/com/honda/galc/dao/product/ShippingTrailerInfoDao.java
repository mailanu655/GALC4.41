package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.ShippingQuorum;
import com.honda.galc.entity.product.ShippingTrailerInfo;
import com.honda.galc.entity.product.ShippingVanningSchedule;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>ShippingTrailerInfoDao Class description</h3>
 * <p> ShippingTrailerInfoDao description </p>
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
 * Jun 1, 2012
 *
 *
 */
public interface ShippingTrailerInfoDao extends IDaoService<ShippingTrailerInfo, Integer> {

	public List<ShippingTrailerInfo> findAllFinishedTrailers();
	
	public List<ShippingTrailerInfo> findAllShippingTrailers();
	
	public List<ShippingTrailerInfo> findAllCompleteTrailers();
	
	public ShippingTrailerInfo changeTrailerNumer(int trailerId, String trailerNumber);
	
	public ShippingTrailerInfo getLastShippingTrailerInfo();
	
	public void assignTrailer(String trailerNumber, List<ShippingVanningSchedule> schedules, 
			List<ShippingQuorum> shippingQuorums,ShippingVanningSchedule splitSchedule);
	
	public int deassignTrailer(int trailerId);
	
	public List<ShippingTrailerInfo> findLatestTrailers();
}
