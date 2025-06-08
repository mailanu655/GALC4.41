package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiReasonForChangeDetailDao;
import com.honda.galc.entity.qi.QiReasonForChangeDetail;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>QiReasonForChangeDetailDaoImpl Class description</h3>
 * <p>
 * QiReasonForChangeDetailDaoImpl description
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

public class QiReasonForChangeDetailDaoImpl extends BaseDaoImpl<QiReasonForChangeDetail, Integer> implements QiReasonForChangeDetailDao {
	
	private static final String FIND_ALL_BY_CATEGORY = "SELECT * FROM GALADM.QI_REASON_FOR_CHANGE_DETAIL_TBX WHERE "
			+ "CATEGORY_ID=?1";
	
	private static final String FIND_ALL_BY_DETAIL = "SELECT * FROM GALADM.QI_REASON_FOR_CHANGE_DETAIL_TBX WHERE "
			+ "CATEGORY_ID=?1 AND DETAIL=?2";
	
	private static final String FIND_DEPT_BY_SITE_PLANT = "SELECT DISTINCT DEPT FROM QI_REASON_FOR_CHANGE_DETAIL_TBX A, "
			+ "QI_REASON_FOR_CHANGE_CATEGORY_TBX B WHERE A.CATEGORY_ID = B.CATEGORY_ID AND B.SITE=?1 AND B.PLANT=?2";
	
	private static final String FIND_CATEGORY_BY_SITE_PLANT_DEPT = "SELECT DISTINCT CATEGORY FROM QI_REASON_FOR_CHANGE_DETAIL_TBX A, "
			+ "QI_REASON_FOR_CHANGE_CATEGORY_TBX B WHERE A.CATEGORY_ID = B.CATEGORY_ID AND B.SITE=?1 AND B.PLANT=?2 AND B.DEPT=?3";
	
	private static final String FIND_DETAIL_BY_SITE_PLANT_DEPT_CATEGORY = "SELECT DISTINCT DETAIL FROM QI_REASON_FOR_CHANGE_DETAIL_TBX A, "
			+ "QI_REASON_FOR_CHANGE_CATEGORY_TBX B WHERE A.CATEGORY_ID = B.CATEGORY_ID AND B.SITE=?1 AND B.PLANT=?2 AND B.DEPT=?3 AND B.CATEGORY=?4";
	
	
	private static final String FIND_BY_SITE_PLANT_DEPT_CATEGORY_DETAIL = "SELECT DETAIL_ID FROM QI_REASON_FOR_CHANGE_DETAIL_TBX A, "
			+ "QI_REASON_FOR_CHANGE_CATEGORY_TBX B WHERE A.CATEGORY_ID = B.CATEGORY_ID AND B.SITE=?1 AND B.PLANT=?2 AND B.DEPT=?3 AND B.CATEGORY=?4 "
			+ "AND A.DETAIL=?5";

	private static final String UPDATE_DETAIL = "UPDATE GALADM.QI_REASON_FOR_CHANGE_DETAIL_TBX "
			+ "SET DETAIL=?1, UPDATE_USER=?2 WHERE DETAIL_ID=?3";
	
	@Override
	public List<QiReasonForChangeDetail> findAllByCategory(int categoryId) {
		Parameters params = Parameters.with("1", categoryId);
		return findAllByNativeQuery(FIND_ALL_BY_CATEGORY, params, QiReasonForChangeDetail.class);
	}
	
	@Override
	public List<QiReasonForChangeDetail> findAllByDetail(int categoryId, String detail) {
		Parameters params = Parameters.with("1", categoryId).put("2", detail);
		return findAllByNativeQuery(FIND_ALL_BY_DETAIL, params, QiReasonForChangeDetail.class);
	}
	
	@Override
	public List<String> findDeptBySitePlant(String site, String plant) {
		Parameters params = Parameters.with("1", site).put("2", plant);
		return findAllByNativeQuery(FIND_DEPT_BY_SITE_PLANT, params, String.class);
	}
	
	@Override
	public List<String> findCategoryBySitePlantDept(String site, String plant, String dept) {
		Parameters params = Parameters.with("1", site).put("2", plant).put("3", dept);
		return findAllByNativeQuery(FIND_CATEGORY_BY_SITE_PLANT_DEPT, params, String.class);
	}
	
	@Override
	public List<String> findDetailBySitePlantDeptCategory(String site, String plant, String dept, String category) {
		Parameters params = Parameters.with("1", site).put("2", plant).put("3", dept).put("4", category);
		return findAllByNativeQuery(FIND_DETAIL_BY_SITE_PLANT_DEPT_CATEGORY, params, String.class);
	}
	
	@Override
	public int findBySitePlantDeptCategoryDetail(String site, String plant, String dept, String category, String detail) {
		Parameters params = Parameters.with("1", site).put("2", plant).put("3", dept).put("4", category).put("5", detail);
		List<Integer> detailIdList = findAllByNativeQuery(FIND_BY_SITE_PLANT_DEPT_CATEGORY_DETAIL, params, Integer.class);
		if (detailIdList != null && detailIdList.size() > 0) {
			return detailIdList.get(0).intValue();
		} else {
			return -1;
		}
	}

	@Override
	@Transactional
	public QiReasonForChangeDetail createDetail(int categoryId, String detail, String createUser) {
		QiReasonForChangeDetail qiReasonForChangeDetail = new QiReasonForChangeDetail();
		qiReasonForChangeDetail.setCategoryId(categoryId);
		qiReasonForChangeDetail.setDetail(detail);
		qiReasonForChangeDetail.setCreateUser(createUser);
		return super.insert(qiReasonForChangeDetail);	
	}

	@Override
	@Transactional
	public void updateDetail(int detailId, String detail, String updateUser) {
		Parameters params = Parameters.with("1", detail)
				.put("2", updateUser).put("3", detailId);
		executeNativeUpdate(UPDATE_DETAIL, params);
	}
}
