package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.LetPassResult;
import com.honda.galc.entity.product.LetPassResultId;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Mar 22, 2018
 */
public interface LetPassResultDao extends IDaoService<LetPassResult, LetPassResultId> {

	public List<LetPassResult> findAllByProductId(String productId);
}
