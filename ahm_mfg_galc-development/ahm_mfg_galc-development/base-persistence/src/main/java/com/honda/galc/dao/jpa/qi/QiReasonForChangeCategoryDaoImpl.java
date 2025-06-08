package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiReasonForChangeCategoryDao;
import com.honda.galc.entity.qi.QiReasonForChangeCategory;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>QiReasonForChangeCategoryDaoImpl Class description</h3>
 * <p>
 * QiReasonForChangeCategoryDaoImpl description
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

public class QiReasonForChangeCategoryDaoImpl extends BaseDaoImpl<QiReasonForChangeCategory, Integer> implements QiReasonForChangeCategoryDao {
	
	private static final String FIND_ALL_BY_CATEGORY = "SELECT * FROM GALADM.QI_REASON_FOR_CHANGE_CATEGORY_TBX WHERE "
			+ "SITE=?1 AND PLANT=?2 AND DEPT=?3 AND CATEGORY=?4";
	
	private static final String FIND_ALL_BY_DEPT = "SELECT * FROM GALADM.QI_REASON_FOR_CHANGE_CATEGORY_TBX WHERE "
			+ "SITE=?1 AND PLANT=?2 AND DEPT=?3";
	
	private static final String UPDATE_CATEGORY = "UPDATE GALADM.QI_REASON_FOR_CHANGE_CATEGORY_TBX "
			+ "SET CATEGORY=?1, UPDATE_USER=?2 WHERE CATEGORY_ID=?3";
	
	@Override
	public List<QiReasonForChangeCategory> findAllByCategory(String site, String plant, String dept, String category) {
		Parameters params = Parameters.with("1", site)
				.put("2", plant).put("3", dept).put("4", category);
		return findAllByNativeQuery(FIND_ALL_BY_CATEGORY, params, QiReasonForChangeCategory.class);
	}

	@Override
	@Transactional
	public QiReasonForChangeCategory createCategory(String site, String plant, String dept, String category, String createUser) {
		QiReasonForChangeCategory qiReasonForChangeCategory = new QiReasonForChangeCategory();
		qiReasonForChangeCategory.setSite(site);
		qiReasonForChangeCategory.setPlant(plant);
		qiReasonForChangeCategory.setDepartment(dept);
		qiReasonForChangeCategory.setCategory(category);
		qiReasonForChangeCategory.setCreateUser(createUser);
		return super.insert(qiReasonForChangeCategory);
	}

	@Override
	@Transactional
	public void updateCategory(int categoryId, String newCategory, String updateUser) {
		Parameters params = Parameters.with("1", newCategory)
				.put("2", updateUser).put("3", categoryId);
		executeNativeUpdate(UPDATE_CATEGORY, params);
	}

	@Override
	public List<QiReasonForChangeCategory> findAllByDept(String site, String plant, String dept) {
		Parameters params = Parameters.with("1", site)
				.put("2", plant).put("3", dept);
		return findAllByNativeQuery(FIND_ALL_BY_DEPT, params, QiReasonForChangeCategory.class);
	}
}
