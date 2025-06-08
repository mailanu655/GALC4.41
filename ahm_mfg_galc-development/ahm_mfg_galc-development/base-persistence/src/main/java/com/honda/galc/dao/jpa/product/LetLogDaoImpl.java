package com.honda.galc.dao.jpa.product;

import org.springframework.transaction.annotation.Transactional;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.LetLogDao;
import com.honda.galc.entity.product.LetLog;
import com.honda.galc.entity.product.LetLogId;
import com.honda.galc.service.Parameters;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
public class LetLogDaoImpl extends BaseDaoImpl<LetLog, LetLogId> implements LetLogDao {
	
	public LetLog findLetLogByProductIdSeqNum(String productId,Integer testSeq) 
	{      
		Parameters params = Parameters.with("id.productId", productId).put("id.testSeq", testSeq);			
		return findFirst(params);
	}

	@Transactional
	public int deleteLetLogByProductIdSeqNum(String productId,Integer testSeq) 
	{
		Parameters params = Parameters.with("id.productId", productId).put("id.testSeq", testSeq);		
		return delete(params);
	}	

}
