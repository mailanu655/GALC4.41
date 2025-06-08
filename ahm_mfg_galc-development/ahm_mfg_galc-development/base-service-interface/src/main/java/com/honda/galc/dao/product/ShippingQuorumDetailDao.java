package com.honda.galc.dao.product;

import java.sql.Date;
import java.util.List;

import com.honda.galc.dto.ShippingQuorumDetailDto;
import com.honda.galc.entity.product.ShippingQuorumDetail;
import com.honda.galc.entity.product.ShippingQuorumDetailId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>ShippingQuorumDetailDao Class description</h3>
 * <p> ShippingQuorumDetailDao description </p>
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
public interface ShippingQuorumDetailDao extends IDaoService<ShippingQuorumDetail, ShippingQuorumDetailId> {

	public int deleteAllByTrailerId(int trailerId);
	
	public int shiftQuorumSeq(Date quorumDate, int quorumId,int offset);
	
	public int changeQuorum(Date fromQuorumDate, int fromQuorumId, Date toQuorumDate,int toQuorumId);
	
	public List<ShippingQuorumDetail> findAllByEngineNumber(String ein);
	
	public List<ShippingQuorumDetail> checkEngineModels(int trailerId);
	
	public List<ShippingQuorumDetailDto> findAllDetails(int trailerId);
	
	public List<ShippingQuorumDetailDto> findAllDetailsByEngineNumber(String ein);
	
	public List<ShippingQuorumDetailDto> findAllDetailsByKdLot(String kdLot);
}
