package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiReasonForChangeCategory;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>QiReasonForChangeCategoryDao Class description</h3>
 * <p>
 * QiReasonForChangeCategoryDao description
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
 * @author Justin Jiang<br>
 *         November 3, 2020
 *
 */

public interface QiReasonForChangeCategoryDao extends IDaoService<QiReasonForChangeCategory, Integer>{
	public List<QiReasonForChangeCategory> findAllByCategory(String site, String plant, String dept, String category);
	public QiReasonForChangeCategory createCategory(String site, String plant, String dept, String category, String createUser);
	public void updateCategory(int categoryId, String newCategory, String updateUser);
	public List<QiReasonForChangeCategory> findAllByDept(String site, String plant, String dept);
}
