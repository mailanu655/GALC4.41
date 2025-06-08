package com.honda.galc.dao.jpa.product;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.SkippedProductDao;
import com.honda.galc.entity.product.SkippedProduct;
import com.honda.galc.entity.product.SkippedProductId;
import com.honda.galc.service.Parameters;

public class SkippedProductDaoImpl extends BaseDaoImpl<SkippedProduct,SkippedProductId> implements SkippedProductDao
{
    
    private final String FIND_ALL_BY_PROCESS_POINT_AND_STATUS ="select product_id " +
    "from galadm.skipped_product_tbx where PROCESS_POINT_ID = ?1 and STATUS = ?2 order by create_timestamp";

	@Transactional
	public int deleteProdIds(List <String> prodIds) 
	{
        int count = 0;
		for( String prodId : prodIds )
		{
            count = count + delete(Parameters.with("id.productId", prodId));
		}
        return count;
	}
 
    public List<String> findProductByProcessPointAndStatus(String processPointId, int status) {
        Parameters params = Parameters.with("1", processPointId).put("2", status);
        return findAllByNativeQuery(FIND_ALL_BY_PROCESS_POINT_AND_STATUS, params, String.class);
    }

    public List<SkippedProduct> findAllByProcessPointId(String processPointId) {

        return findAll(Parameters.with("id.processPointId",processPointId));

    }

}
