package com.honda.galc.dao.gts;

import java.util.List;

import com.honda.galc.entity.gts.GtsClientList;
import com.honda.galc.entity.gts.GtsClientListId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * 
 * <h3>GtsClientListDao Class description</h3>
 * <p> GtsClientListDao description </p>
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
 * Jun 9, 2015
 *
 *
 */
public interface GtsClientListDao extends IDaoService<GtsClientList, GtsClientListId>{
	
	public List<GtsClientList> findAll(String trackingArea);
	
}
