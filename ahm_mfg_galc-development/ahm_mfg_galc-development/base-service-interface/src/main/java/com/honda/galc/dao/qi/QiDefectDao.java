package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiDefect;
import com.honda.galc.service.IDaoService;


/**
 * 
 * <h3>QiDefectDao Class description</h3>
 * <p> QiDefectDao description </p>
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
 * April 20, 2016
 *
 *
 */
public interface QiDefectDao extends IDaoService<QiDefect, String>  {

	public List<QiDefect> findDefectByFilter(String filterValue, String productKind, List<Short> statusList);
	public void updateDefectStatus(String name, short active, String user);
	public void updateDefect(QiDefect qiDefect, String oldDefectName);
	public List<QiDefect> findActivePrimaryDefectByFilter(String filterValue, String productKind);
	public List<QiDefect> findActiveSecondaryDefectByFilter(String filterValue, String productKind);
}


