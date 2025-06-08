package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiIqsVersion;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>QIIqsVersionDao Class description</h3>
 * <p> QIIqsVersionDao description </p>
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
 * July 4 2016
 *
 *
 */

public interface QiIqsVersionDao extends IDaoService<QiIqsVersion, String> {
	
	public List<QiIqsVersion> findAllIqsVersion();
	public void updateIqsVersion(String currentIqsVersion,String userId, String previousIqsVersion);
}
