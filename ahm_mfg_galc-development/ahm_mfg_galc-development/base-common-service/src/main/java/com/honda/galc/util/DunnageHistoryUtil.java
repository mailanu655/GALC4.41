package com.honda.galc.util;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.List;

import com.honda.galc.dao.product.DunnageHistDao;
import com.honda.galc.entity.product.DunnageHist;
import com.honda.galc.entity.product.DunnageHistId;

/**
 * 
 * @author vf036360
 *
 */
public class DunnageHistoryUtil {
	
	public static void createDunnageHist(String productId, String dunnageId) {
		DunnageHistDao dao = getDao(DunnageHistDao.class);
		DunnageHist dunnageHist = getDunnageHist(productId,dunnageId);
		dao.save(dunnageHist);
	}

	public static DunnageHist getDunnageHist(String productId, String dunnageId) {
		DunnageHist dunnageHist = new DunnageHist();
		DunnageHistId dunnageHistId = new DunnageHistId(productId,dunnageId,new Timestamp(System.currentTimeMillis()));
		dunnageHist.setId(dunnageHistId);
		return dunnageHist;
	}
	
	public static void updateDunnageHist(String productId, String dunnageId) {
		DunnageHistDao dao = getDao(DunnageHistDao.class);
		List<DunnageHist> dunnageHistList=dao.findAll(productId, dunnageId);
		
		for(DunnageHist dunnageHist: dunnageHistList) {
			dunnageHist.setOffTimestamp(new Timestamp(System.currentTimeMillis()));
		}
		if(!dunnageHistList.isEmpty()) {
			dao.saveAll(dunnageHistList);
		}		
	}

	public static void createDunnageHist(List<String> productIds, String dunnageId) {
		DunnageHistDao dao = getDao(DunnageHistDao.class);
		for(String productId: productIds) {
			DunnageHist dunnageHist = getDunnageHist(productId,dunnageId);
			dao.save(dunnageHist);
		}		
	}
	
	public static void createDunnageHistForReassign(List<String> productIds, String dunnageId) {
		for(String productId: productIds) {
			createDunnageHist(productId,dunnageId);
		}		
	}

	public static void updateDunnageHist(List<String> productIds, String dunnageId) {
		for(String productId: productIds) {
			updateDunnageHist(productId,dunnageId);
		}
		
	}
}
