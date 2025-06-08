package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ShippingVanningSchedule;
import com.honda.galc.entity.product.ShippingVanningScheduleId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>ShippingVanningScheduleDao Class description</h3>
 * <p> ShippingVanningScheduleDao description </p>
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
public interface ShippingVanningScheduleDao extends IDaoService<ShippingVanningSchedule, ShippingVanningScheduleId> {

	public int deleteAllByProductionLot(String productionLot);
	
	public List<ShippingVanningSchedule> findAllActiveVanningSchedules();
	
	public List<ShippingVanningSchedule> findAllCompleteVanningSchedules();
	
	public List<ShippingVanningSchedule> findAllPartialLoadSchedules();
	
	public int removeTrailerId(int trailerId);
	
	public List<ShippingVanningSchedule> findVanningSchedules(int trailerId, String kdLot);
	
	public ShippingVanningSchedule findIncompleteSchedule(int trailerId, String kdLot);
	
	public ShippingVanningSchedule findScheduleToRemove(int trailerId, String kdLot);
	
	public boolean loadEngineNumber(ShippingVanningSchedule schedule,String engineNumber);
	
	public void syncVanningSchedule();
	
	public ShippingVanningSchedule saveSchedule(PreProductionLot preProductionLot);
	
}
