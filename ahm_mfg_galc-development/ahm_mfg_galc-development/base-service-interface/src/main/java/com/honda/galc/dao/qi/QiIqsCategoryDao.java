package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiIqsCategory;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>QIIqsCategoryDao Class description</h3>
 * <p> QIIqsCategoryDao description </p>
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
 * July 5 2016
 *
 *
 */

public interface QiIqsCategoryDao extends IDaoService<QiIqsCategory, String> {
	
	public List<QiIqsCategory> findAllIqsCategory();
	public void updateIqsCategory(String currentIqsCategory,String userId, String previousIqsCategory);
}
