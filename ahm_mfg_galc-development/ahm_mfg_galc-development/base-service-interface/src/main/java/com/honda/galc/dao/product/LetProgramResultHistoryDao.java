package com.honda.galc.dao.product;

import com.honda.galc.entity.product.LetProgramResultHistory;
import com.honda.galc.entity.product.LetProgramResultHistoryId;
import com.honda.galc.service.IDaoService;


/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
public interface LetProgramResultHistoryDao extends IDaoService<LetProgramResultHistory, LetProgramResultHistoryId> {
	public Object[] getProgramResultData(String productId ,Integer testSeq,Integer inspectionPgmId);
}