package com.honda.galc.dao.mdrs;

import java.util.List;

import com.honda.galc.entity.mdrs.QiDefectRespAssociate;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>QiDefectRespAssociateDao Class description</h3>
 * <p>
 * QiDefectRespAssociateDao description
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
 * @author L&T Infotech<br>
 *         May 15, 2017
 *
 *
 */
public interface QiDefectRespAssociateDao extends IDaoService<QiDefectRespAssociate, Integer> {
	
	public List<QiDefectRespAssociate> findAllUnProcessedDefects();
	
}
