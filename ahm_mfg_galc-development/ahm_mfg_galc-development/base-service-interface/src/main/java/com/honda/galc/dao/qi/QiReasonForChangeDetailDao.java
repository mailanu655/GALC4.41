package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiReasonForChangeDetail;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>QiReasonForChangeDetailDao Class description</h3>
 * <p>
 * QiReasonForChangeDetailDao description
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

public interface QiReasonForChangeDetailDao extends IDaoService<QiReasonForChangeDetail, Integer>{
	public List<QiReasonForChangeDetail> findAllByCategory(int categoryId);
	public List<QiReasonForChangeDetail> findAllByDetail(int categoryId, String detail);
	public List<String> findDeptBySitePlant(String site, String plant);
	public List<String> findCategoryBySitePlantDept(String site, String plant, String dept);
	public List<String> findDetailBySitePlantDeptCategory(String site, String plant, String dept, String category);
	public int findBySitePlantDeptCategoryDetail(String site, String plant, String dept, String category, String detail);
	public QiReasonForChangeDetail createDetail(int categoryId, String detail, String createUser);
	public void updateDetail(int detailId, String detail, String updateUser);
}
