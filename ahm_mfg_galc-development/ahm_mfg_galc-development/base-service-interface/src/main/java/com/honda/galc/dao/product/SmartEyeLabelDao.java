package com.honda.galc.dao.product;

import com.honda.galc.entity.product.SmartEyeLabel;
import com.honda.galc.service.IDaoService;

/**
 * The Interface SmartEyeLabelDao.
 * SR# TASK0014476
 * Created Interface for updating the VIN to blank in GAL228TBX
 * @author Sachin Kudikala
 * @since July 21, 2014
 */
public interface SmartEyeLabelDao  extends IDaoService<SmartEyeLabel , String>{

	public int updateSmartEyeLabel(String productId);
}
