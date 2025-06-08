package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiPddaStaging;
import com.honda.galc.service.IDaoService;



/**
 * 
 * <h3>QiPddaStagingDao Class description</h3>
 * <p>
 *<code>QiPddaStagingDao</code> is a DAO interface to implement database interaction for PALSQ1.
 * </p>
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
 * @author L&TInfotech<br>
 *      May 19, 2017
 * 
 */
public interface QiPddaStagingDao extends IDaoService<QiPddaStaging, Integer> {

	public List<QiPddaStaging> findAllByDataColByQics();
	
}
