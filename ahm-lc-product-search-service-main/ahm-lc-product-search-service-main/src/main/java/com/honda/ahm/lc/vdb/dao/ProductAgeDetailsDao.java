package com.honda.ahm.lc.vdb.dao;

import java.util.List;
import java.util.Set;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.honda.ahm.lc.vdb.entity.ProductAgeDetails;
import com.honda.ahm.lc.vdb.entity.ProductAgeDetailsId;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>ProductAgeDetailsDao</code> is ... .
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Hemant Rajput
 * @created Apr 22, 2021
 */

public interface ProductAgeDetailsDao extends JpaRepository<ProductAgeDetails, ProductAgeDetailsId> {

	public static final String FIND_ALL_BY_PRODUCT_ID = "select e from ProductAgeDetails e where e.productId = :productId and e.processPointId = :processPointId";
	
	public static final String FIND_ALL_BY_PRODUCT_ID_PROCESS_POINT_ID = "select e from ProductAgeDetails e where e.productId IN :productId and e.processPointId IN :processPointId";

	@Query(value = FIND_ALL_BY_PRODUCT_ID)
	@Cacheable(value = "findAllBy", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null || #result.isEmpty()")
	public List<ProductAgeDetails> findAllBy(@Param("productId") String productId,
			@Param("processPointId") String processPointId);
	
	@Query(value = FIND_ALL_BY_PRODUCT_ID_PROCESS_POINT_ID)
	@Cacheable(value = "findAllBy", key = "T(com.honda.ahm.lc.vdb.util.CacheKeyUtil).generateCacheKey(#root.args)", unless = "#result == null || #result.isEmpty()")
	public List<ProductAgeDetails> findAllBy(@Param("productId") Set<String> productId,
			@Param("processPointId") List<String> processPointId);

}
