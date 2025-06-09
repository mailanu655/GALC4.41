package com.honda.ahm.lc.vdb.dao;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.honda.ahm.lc.vdb.entity.ProductHistoryDetails;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>ProductDetailsDao</code> is ... .
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

public interface ProductHistoryDetailsDao extends JpaRepository<ProductHistoryDetails, String> {

	public static final String FIND_HISTORY_BY_PRODUCT = "select e from ProductHistoryDetails e "
			+ "where e.productId = :productId " + "order by e.actualTimestamp desc";

	@Query(value = FIND_HISTORY_BY_PRODUCT)
	public List<ProductHistoryDetails> findHistoryByProduct(@Param("productId") String productId);

}
