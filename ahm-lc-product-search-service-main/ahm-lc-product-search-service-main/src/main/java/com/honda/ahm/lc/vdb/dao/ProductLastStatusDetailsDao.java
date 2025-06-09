package com.honda.ahm.lc.vdb.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.honda.ahm.lc.vdb.entity.ProductLastStatusDetails;

@Repository
public interface ProductLastStatusDetailsDao extends JpaRepository<ProductLastStatusDetails, String> {

	public static final String FIND_LAST_TRACKING_DATE = "select e from ProductLastStatusDetails e "
	        + " where e.productId = :productId and e.lineId = :lineId order by e.actualTimestamp";
	
	public static final String FIND_LAST_TRACKING_DATE_LIST = "select e from ProductLastStatusDetails e "
	        + " where e.productId IN :productId and e.lineId IN :lineId order by e.actualTimestamp";

	@Query(value = FIND_LAST_TRACKING_DATE)
	List<ProductLastStatusDetails> getLastTrackingDate(@Param("productId") String productId, @Param("lineId") String lineId);
    
	@Query(value = FIND_LAST_TRACKING_DATE_LIST)
	List<ProductLastStatusDetails> findAllBy(@Param("productId") Set<String> productId,@Param("lineId") List<String> lineId);

}
