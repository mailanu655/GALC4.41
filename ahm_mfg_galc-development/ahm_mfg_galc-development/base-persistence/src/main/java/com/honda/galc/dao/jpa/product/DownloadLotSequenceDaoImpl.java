/**
 * 
 */
package com.honda.galc.dao.jpa.product;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.DownloadLotSequenceDao;
import com.honda.galc.entity.product.DownloadLotSequence;
import com.honda.galc.entity.product.DownloadLotSequenceId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>DownloadLotSequenceDaoImpl.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DownloadLotSequenceDaoImpl.java description </p>
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
public class DownloadLotSequenceDaoImpl extends BaseDaoImpl<DownloadLotSequence, DownloadLotSequenceId> implements DownloadLotSequenceDao{

	public DownloadLotSequence findAllByProcessPointAndProcessLocation(String processLocation, String processPoint){
		return findFirst(Parameters.with("id.processLocation",processLocation).put("id.processPointId", processPoint));
	}

	public List<DownloadLotSequence> findAllByProcessLocation(String processLocation) {
		
		return findAll(Parameters.with("id.processLocation",processLocation));
	}
}
