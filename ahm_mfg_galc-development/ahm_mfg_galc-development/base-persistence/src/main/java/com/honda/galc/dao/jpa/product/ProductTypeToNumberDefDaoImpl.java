package com.honda.galc.dao.jpa.product;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.ProductTypeToNumberDefDao;
import com.honda.galc.entity.product.ProductIdNumberDef;
import com.honda.galc.entity.product.ProductTypeToNumberDef;
import com.honda.galc.entity.product.ProductTypeToNumberDefId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>ProductTypeToNumberDefDaoImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductTypeToNumberDefDaoImpl description </p>
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
public class ProductTypeToNumberDefDaoImpl extends BaseDaoImpl<ProductTypeToNumberDef,ProductTypeToNumberDefId> implements ProductTypeToNumberDefDao {

	public static String FIND_ALL_BY_MAIN_NO = "select d from ProductIdNumberDef d, ProductTypeToNumberDef t where t.id.mainNo = :mainNo and t.id.productIdDef = d.productIdDef";
	
	@SuppressWarnings("unchecked")
	public List<ProductIdNumberDef> findByMainNo(String mainNo) {
		return findResultListByQuery(FIND_ALL_BY_MAIN_NO, Parameters.with("mainNo", mainNo));
	}

	@Transactional
	public void removeByMainNo(String mainNo) {
		 delete(Parameters.with("id.mainNo", mainNo));
	}
	
	@Transactional
	public void removeByProductIdDef(String productIdDef) {
		 delete(Parameters.with("id.productIdDef", productIdDef));
	}

}
