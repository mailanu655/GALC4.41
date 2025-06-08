package com.honda.galc.dao.jpa.product;


import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.ExceptionalOutDao;
import com.honda.galc.entity.product.ExceptionalOut;
import com.honda.galc.entity.product.ExceptionalOutId;
import com.honda.galc.service.Parameters;

public class ExceptionalOutDaoImpl extends BaseDaoImpl<ExceptionalOut,ExceptionalOutId> implements ExceptionalOutDao {

	@Transactional
	public int deleteAllByProductId(String productId) {
		return delete(Parameters.with("id.productId", productId));
	}
	
	public List<ExceptionalOut> findAllByProductId(String productId) {
		return findAll(Parameters.with(("id.productId"),productId));
    }
	
	public ExceptionalOut findMostRecentByProductId(String productId) {
		return findFirst(Parameters.with(("id.productId"),productId), new String[]{"id.actualTimestamp"}, false);
    }
	
	@Transactional
	public ExceptionalOut createExceptionalOut(ExceptionalOut exceptionalOut) {
		if (exceptionalOut.getId().getActualTimestamp() == null) {
			exceptionalOut.getId().setActualTimestamp(super.getDatabaseTimeStamp());
		}
		return super.save(exceptionalOut);
	}
}
