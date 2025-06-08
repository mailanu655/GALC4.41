package com.honda.galc.dao.product;


import com.honda.galc.entity.product.LetLog;
import com.honda.galc.entity.product.LetLogId;
import com.honda.galc.entity.product.LetResult;
import com.honda.galc.service.IDaoService;
import com.honda.galc.service.Parameters;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
public interface LetLogDao extends IDaoService<LetLog, LetLogId> {
	
	public LetLog findLetLogByProductIdSeqNum(String productId,Integer testSeq);
	
	public int deleteLetLogByProductIdSeqNum(String productId,Integer testSeq) ;

}