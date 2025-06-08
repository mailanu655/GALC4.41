package com.honda.galc.dao.jpa.product;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.MbpnProductTypeDao;
import com.honda.galc.entity.product.MbpnProductType;
import com.honda.galc.entity.product.MbpnProductTypeId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>MbpnProductTypeDaoImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> MbpnProductTypeDaoImpl description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Feb 22, 2017</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Feb 22, 2017
 */
public class MbpnProductTypeDaoImpl extends BaseDaoImpl<MbpnProductType,MbpnProductTypeId> implements MbpnProductTypeDao {

	private static final String FIND_ALL_BY_PRODUCT_TYPE = "SELECT p FROM MbpnProductType p WHERE p.productType = :productType";
	private static final String FIND_ALL_PRODUCT_TYPES = "select distinct p.productType  from MbpnProductType p";
	
	public List<MbpnProductType> findAllByProductType(String productType) {

		Parameters parameters = Parameters.with("productType", productType);
		return findAllByQuery(FIND_ALL_BY_PRODUCT_TYPE, parameters);
	}
	@SuppressWarnings("unchecked")
	public List<String> findAllProductTypes() {
		return findResultListByQuery(FIND_ALL_PRODUCT_TYPES, null);
	}

	
}
