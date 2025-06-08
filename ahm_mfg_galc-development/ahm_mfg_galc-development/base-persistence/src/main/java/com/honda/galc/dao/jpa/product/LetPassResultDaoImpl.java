package com.honda.galc.dao.jpa.product;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.LetPassResultDao;
import com.honda.galc.entity.product.LetPassResult;
import com.honda.galc.entity.product.LetPassResultId;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date Mar 23, 2018
 */
public class LetPassResultDaoImpl extends BaseDaoImpl<LetPassResult, LetPassResultId> implements LetPassResultDao {

	public List<LetPassResult> findAllByProductId(String productId) {
		return findAll(Parameters.with("id.productId", productId));
	}
}
