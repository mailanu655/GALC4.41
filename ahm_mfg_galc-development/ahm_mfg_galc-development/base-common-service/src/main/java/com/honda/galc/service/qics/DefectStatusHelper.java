package com.honda.galc.service.qics;

import static com.honda.galc.service.ServiceFactory.getDao;

import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.service.utils.ProductTypeUtil;

public class DefectStatusHelper {

	public DefectStatusHelper() {
	}
	
	public static void updateDefectStatus(long defectResultId)  {
		QiDefectResultDao dao = getDao(QiDefectResultDao.class);
		QiDefectResult qiDefect = dao.findByKey(defectResultId);
		if(qiDefect == null)  return;
		long notFixed = dao.findNotFixedDefectCountByProductId(qiDefect.getProductId());
    	ProductDao<? extends BaseProduct> productDao = 
        		ProductTypeUtil.getProductDao(qiDefect.getProductType());
        	
		if(notFixed > 0)  {
	        productDao.updateDefectStatus(qiDefect.getProductId(), DefectStatus.OUTSTANDING);
			
		}
		else  {
	        productDao.updateDefectStatus(qiDefect.getProductId(), DefectStatus.REPAIRED);
		}
	}

}
