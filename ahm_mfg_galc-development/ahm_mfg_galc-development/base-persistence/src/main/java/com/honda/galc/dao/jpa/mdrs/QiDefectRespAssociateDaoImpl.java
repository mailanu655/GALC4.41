package com.honda.galc.dao.jpa.mdrs;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.mdrs.QiDefectRespAssociateDao;
import com.honda.galc.entity.mdrs.QiDefectRespAssociate;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>QiDefectRespAssociateDaoImpl Class description</h3>
 * <p>
 * QiDefectRespAssociateDaoImpl description
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
public class QiDefectRespAssociateDaoImpl extends BaseDaoImpl<QiDefectRespAssociate, Integer> implements QiDefectRespAssociateDao {

	public List<QiDefectRespAssociate> findAllUnProcessedDefects(){
		return findAll(Parameters.with("status", (short)0));
	}
	
}
