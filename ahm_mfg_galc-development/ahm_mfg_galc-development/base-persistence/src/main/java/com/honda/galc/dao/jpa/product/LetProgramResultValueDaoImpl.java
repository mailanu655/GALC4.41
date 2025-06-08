package com.honda.galc.dao.jpa.product;

import java.sql.Timestamp;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.LetProgramResultValueDao;
import com.honda.galc.entity.product.LetProgramResultValue;
import com.honda.galc.entity.product.LetProgramResultValueId;
import com.honda.galc.service.Parameters;


public class LetProgramResultValueDaoImpl extends BaseDaoImpl<LetProgramResultValue, LetProgramResultValueId> implements LetProgramResultValueDao {

    public static final String SELECT_COUNT_BY_PRODUCT_AND_END_TS = "select count(*) from gal704tbxv where product_id = ?1 and end_timestamp = ?2";
    
    @Override
    public long count(String productId, Timestamp endTimestamp) {
        String sql = SELECT_COUNT_BY_PRODUCT_AND_END_TS;
        Parameters params = new Parameters();
        params.put("1", productId);
        params.put("2", endTimestamp);
        return countByNativeSql(sql, params);
    }
}
