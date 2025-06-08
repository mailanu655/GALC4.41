package com.honda.galc.dao.jpa.qi;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiProductKindDao;
import com.honda.galc.entity.qi.QiProductKind;

public class QiProductKindDaoImpl extends BaseDaoImpl<QiProductKind, String> implements QiProductKindDao {

	private static String FIND_ALL_PRODUCT_KIND = "select e.productKind from QiProductKind e order by e.productKind";

	public List<String> findAllList() {
		return findByQuery(FIND_ALL_PRODUCT_KIND, String.class);
	}

}
