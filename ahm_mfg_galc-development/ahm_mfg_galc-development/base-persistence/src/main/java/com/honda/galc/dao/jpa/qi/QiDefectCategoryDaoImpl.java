package com.honda.galc.dao.jpa.qi;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiDefectCategoryDao;
import com.honda.galc.entity.qi.QiDefectCategory;

public class QiDefectCategoryDaoImpl extends BaseDaoImpl<QiDefectCategory, String> implements QiDefectCategoryDao {
	String FIND_ALL_DEFECT_CATEGRY = "select e.defectCategoryName from QiDefectCategory e " ;
	/**
	 * To Populate Defect Category.
	 */
	public List<QiDefectCategory> findAllDefectCategory() {
		return findResultListByQuery(FIND_ALL_DEFECT_CATEGRY,null);
	}
}
