package com.honda.galc.service;

import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.data.DataContainer;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.product.BaseProduct;

/**
 * 
 * <h3>BroadcastService Class description</h3>
 * <p> BroadcastService description </p>
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
 * Aug 22, 2013
 *
 *
 */
public interface BroadcastService extends IService{
	
	/**
	 * 
	 * @param processPointId
	 * @param productId
	 * @return
	 */
	public void broadcast(String processPointId,String productId);
	
	public void broadcast(String processPointId,BaseProduct product);
	
	public void broadcast(String processPointId, DataContainer dc);
	
	public void broadcast(String processPointId, DataContainer dc, CheckPoints checkPoint);
	
	public DataContainer broadcast(String processPointId,int seqno,String productId);
	
	public DataContainer broadcast(String processPointId,int seqno,DataContainer dc);
	
	public void broadcast(String processPointId, BaseProduct product,DataContainer dc);
    
	public void asynBroadcast(String processPointId, int seqno, String productId); 

	public void asynBroadcast(String processPointId, int seqno, DataContainer dc); 

	public void broadcast(BroadcastDestination destination, String processPointId, DataContainer dc);
	
	/**
	 * This method does not verify condition, it will broadcast to all defined active destinations.
	 * @param processPointId
	 * @param dataContainer
	 */
	public void reBroadcast(String processPointId, DataContainer dataContainer);
}
