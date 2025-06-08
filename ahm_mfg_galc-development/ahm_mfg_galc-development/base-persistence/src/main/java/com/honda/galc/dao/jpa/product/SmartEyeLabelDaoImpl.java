package com.honda.galc.dao.jpa.product;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.SmartEyeLabelDao;
import com.honda.galc.entity.product.SmartEyeLabel;
import com.honda.galc.service.Parameters;

/**
 * The Class SmartEyeLabelDaoImpl.
 * SR# TASK0014476
 * Created the implementation class for updating the VIN to blank in GAL228TBX
 * @author Sachin Kudikala 
 * @since July 21, 2014
 */
public class SmartEyeLabelDaoImpl extends BaseDaoImpl<SmartEyeLabel , String> implements SmartEyeLabelDao{

    private static final long serialVersionUID = 1L;
    private static final String UPDATE_SMART_EYE_LABEL = "update SmartEyeLabel s set s.productId = :productId where s.productId = :oldProductId";		
	
	@Transactional
	public int updateSmartEyeLabel(String productId) {
		Parameters params = Parameters.with("productId", "");
		params.put("oldProductId", productId);
		return executeUpdate(UPDATE_SMART_EYE_LABEL, params);

	}
}
