package com.honda.galc.dao.gts;

import java.util.List;

import com.honda.galc.entity.gts.GtsProduct;
import com.honda.galc.entity.gts.GtsProductId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * 
 * <h3>GtsProductDao Class description</h3>
 * <p> GtsProductDao description </p>
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
 * Jun 23, 2015
 *
 *
 */
public interface GtsProductDao extends IDaoService<GtsProduct, GtsProductId>{
	
	List<GtsProduct> findAll(String trackingArea, String carrierArea);
	
	List<GtsProduct> findAllByLane(String trackingArea, String laneId,String carrierArea);
	
	List<GtsProduct> findAllWithDefects(String trackingArea);
}
