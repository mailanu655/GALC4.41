package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiDefectCategory;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>QiDefectCategoryDao Class description</h3>
 * <p> QiDefectCategoryDao description </p>
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

public interface QiDefectCategoryDao extends IDaoService<QiDefectCategory, String> {
	public List<QiDefectCategory> findAllDefectCategory();
}
