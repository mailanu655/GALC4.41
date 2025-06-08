package com.honda.galc.dao.product;

import com.honda.galc.entity.product.LetProgramValueHistory;
import com.honda.galc.entity.product.LetProgramValueHistoryId;
import com.honda.galc.service.IDaoService;
import com.honda.galc.service.Parameters;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
public interface LetProgramValueHistoryDao extends IDaoService<LetProgramValueHistory, LetProgramValueHistoryId> {
	public Object[] getProgramValueData(String productId,Integer testSeq,Integer inspectionPgmId,Integer inspectionParamId,String inspectionParamType) ;
	public int updateProgramValueData(String productId ,Integer testSeq,Integer inspectionPgmId,Integer inspectionParamId,String inspectionParamValue,String inspectionParamType) ;
}