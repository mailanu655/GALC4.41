package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.LetLog;
import com.honda.galc.entity.product.LetLogId;
import com.honda.galc.entity.product.LetModifyHistory;
import com.honda.galc.entity.product.LetModifyHistoryId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
public interface LetModifyHistoryDao extends IDaoService<LetModifyHistory, LetModifyHistoryId> {
	
	public List<Object[]> findDistinctLetMdfyHstryPgmByProductId(String productId) ;
	public List<Object[]> findLetModifyResultHistoryByProductId(String productId) ;
	public List<Object[]> getInpsectionDetails(String productId,Integer programId) ;
	public Integer getMaxHistorySeq(String productId) ;
}