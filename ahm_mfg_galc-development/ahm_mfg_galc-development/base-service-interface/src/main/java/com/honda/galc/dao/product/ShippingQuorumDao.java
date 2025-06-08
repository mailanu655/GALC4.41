package com.honda.galc.dao.product;

import java.sql.Date;
import java.util.List;

import com.honda.galc.entity.enumtype.ShippingQuorumStatus;
import com.honda.galc.entity.product.ShippingQuorum;
import com.honda.galc.entity.product.ShippingQuorumId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>ShippingQuorumDao Class description</h3>
 * <p> ShippingQuorumDao description </p>
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
public interface ShippingQuorumDao extends IDaoService<ShippingQuorum, ShippingQuorumId> {

	public int deleteAllByTrailerId(int trailerId);
	
	public List<ShippingQuorum> findAllActiveQuorums();
	
	public int updateStatus(Date quorumDate, int quorumId,ShippingQuorumStatus status);
	
	public int shiftQuorumSeq(Date quorumDate, int quorumId,int offset);

	public int changeQuorum(Date fromQuorumDate, int fromQuorumId, Date toQuorumDate,int toQuorumId);
	
	public ShippingQuorum findLastShippingQuorum();
	
	public ShippingQuorum findCurrentShippingQuorum();
	
	public void updateManualLoadEngines(ShippingQuorum quorum,List<String> eins);
	
	public void saveWithDetail(ShippingQuorum quorum);
	
	public List<ShippingQuorum> findAllByTrailerId(int trailerId);

}
