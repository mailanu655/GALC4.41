package com.honda.galc.service.engine;

import com.honda.galc.dao.product.CrankshaftDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.Crankshaft;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>DcCrankshaftOnServiceImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DcCrankshaftOnServiceImpl description </p>
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
 * </TR>
 * <TR>
 * <TD>Kamlesh Maharjan</TD>
 * <TD>Oct 15, 2021</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 */
public class DcCrankshaftOnServiceImpl extends DiecastOn<Crankshaft> implements DcCrankshaftOnService{
	
	public DcCrankshaftOnServiceImpl() {
		context.setProductType(ProductType.CRANKSHAFT);
	}

	@Override
	public void saveDiecast(Crankshaft crankshaft) {
		crankshaft.setCrankshaftId(crankshaft.getDcSerialNumber().length()>MAX_ID_LENGTH ? generateId(crankshaft.getDcSerialNumber()): crankshaft.getDcSerialNumber());
		ServiceFactory.getDao(CrankshaftDao.class).save(crankshaft);		
	}
	
	protected String generateId(String dc) {
		int idLength = getProperty().getIdLength();
		return	dc.substring(0, idLength);		
	}
}
