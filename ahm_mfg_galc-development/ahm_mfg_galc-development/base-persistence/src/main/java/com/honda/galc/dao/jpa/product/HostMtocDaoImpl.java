package com.honda.galc.dao.jpa.product;


import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.HostMtocDao;
import com.honda.galc.entity.conf.ShippingStatus;
import com.honda.galc.entity.product.HostMtoc;
import com.honda.galc.entity.product.HostMtocId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>HostMtocDaoImpl Class description</h3>
 * <p> HostMtocDaoImpl description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Dec 1, 2010
 *
 *
 */
public class HostMtocDaoImpl extends BaseDaoImpl<HostMtoc,HostMtocId> implements HostMtocDao {

    private static final long serialVersionUID = 1L;

	private static final String SELECT_BY_PRODSPEC = 
			"select distinct h "
		+	" from ShippingStatus s, Frame f, HostMtoc h, FrameSpec p " 
		+	" where s.vin = :vin "
		+	" and f.productId = s.vin " 
		+	" and f.productSpecCode = p.productSpecCode "
		+	" and p.modelYearCode = h.id.modelYearCode "
		+	" and p.modelCode = h.id.modelCode "
		+	" and p.modelTypeCode = h.id.modelTypeCode "
		+	" and p.modelOptionCode = h.id.modelOptionCode "
		+	" and p.extColorCode = h.id.extColorCode "
		+	" and p.intColorCode = h.id.intColorCode ";

 
	public List<HostMtoc> findBySpecCode(String vin) {
		Parameters p = Parameters.with("vin", vin);
		List<HostMtoc> thisList = findAllByQuery(SELECT_BY_PRODSPEC, p);
		return thisList;
	}

}
