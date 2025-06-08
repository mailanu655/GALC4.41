/**
 * 
 */
package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.DownloadLotSequence;
import com.honda.galc.entity.product.DownloadLotSequenceId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>DownloadLotSequenceDao.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DownloadLotSequenceDao.java description </p>
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
 * Apr 23, 2014
 *
 */
public interface DownloadLotSequenceDao extends IDaoService<DownloadLotSequence, DownloadLotSequenceId>{
	public DownloadLotSequence findAllByProcessPointAndProcessLocation(String processLocation, String processPoint);
	
	public List<DownloadLotSequence> findAllByProcessLocation(String processLocation);
	
}
