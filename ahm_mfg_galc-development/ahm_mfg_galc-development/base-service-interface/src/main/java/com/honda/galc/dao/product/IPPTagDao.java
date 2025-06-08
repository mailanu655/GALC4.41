package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.dto.oif.IPPTagDTO;
import com.honda.galc.entity.product.IPPTag;
import com.honda.galc.entity.product.IPPTagId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>IPPTagDao Class description</h3>
 * <p> IPPTagDao description </p>
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
 * Jun 3, 2012
 *
 *
 */
public interface IPPTagDao extends IDaoService<IPPTag, IPPTagId> {

	List<IPPTag> findAllByProductId(String productId);

	public List<Object[]> getIPPInfo();
	
	/**
	 * Method to get the IPP tags info to send the CPCS system.
	 * IPP interface was for frame products, but new requirements is necessary send the I
	 * PP for other product types like transmissions and the specifics frame attribues will 
	 * send empty
	 * This is able to work for all the products type
	 * @param dateFilter
	 * @param processPoints
	 * @return
	 */
	public List<IPPTagDTO> getIPPInfo(  final String startDate, final String endDate );
	public List<IPPTagDTO> getIPPIPUInfo(  final String startDate, final String endDate );
	public List<IPPTagDTO> getFirstForEachLot( final String startDate, final String endDate, final String processPoints );
	
	public IPPTag update(IPPTag ippTag, String ippTagNo);
}
