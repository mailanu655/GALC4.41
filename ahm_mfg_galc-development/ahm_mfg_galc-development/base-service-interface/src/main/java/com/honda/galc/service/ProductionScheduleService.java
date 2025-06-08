/**
 * 
 */
package com.honda.galc.service;

import java.util.List;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.product.PreProductionLot;

/**
 * 
 * <h3>ProductionScheduleService.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductionScheduleService.java description </p>
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
 * @author KMaharjan - vfc01499
 * Apr 10, 2014
 *
 */
public interface ProductionScheduleService extends IService{
	public DataContainer getNextProductionScheduleByLotNumber(DefaultDataContainer dc);
	public PreProductionLot getFirstAvailableProductionLot(String processLocation);
	public DataContainer getNextProductionSchedule(DefaultDataContainer dc);
	public DataContainer updatePreproductionLot(DefaultDataContainer dc);
	public DataContainer syncProductionLot(String processPointId, List<PreProductionLot> produtionLots);
}