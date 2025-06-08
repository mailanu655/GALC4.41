package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.dto.KickoutDto;
import com.honda.galc.entity.product.Kickout;
import com.honda.galc.service.IDaoService;

public interface KickoutDao extends IDaoService<Kickout, Long> {
	List<KickoutDto> findProductsWithKickout(List<String> productIdList);
	List<Kickout> findAllActiveByProductIdAndProcessPoint(String productId, String processPointId,int currentProcessSeq);
	List<Kickout> findAllActiveByProductId(String productId);
	List<KickoutDto> findActiveKickoutInfoByProductId(String productId);
	int releaseKickout(Kickout kickout);
	List<KickoutDto> findProductsWithKickoutAndTransactionId(long transactionId, boolean flag);
}
