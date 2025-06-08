package com.honda.galc.client.datacollection.observer.knuckles;

import java.util.List;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.observer.LotControlPartLotPersistenceManager;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>LotControlKnucklesPersistenceManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlKnucklesPersistenceManager description </p>
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
 * <TD>Jan 31, 2012</TD>
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
 * @since Jan 31, 2012
 */
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class LotControlKnucklesPersistenceManager  extends LotControlPartLotPersistenceManager
{

	public LotControlKnucklesPersistenceManager(ClientContext context) {
		super(context);
	}

	@Override
	public synchronized Product confirmProductOnServer(String productId) {
		if(!context.getProperty().getProductType().equals(ProductType.KNUCKLE.toString()))
		{
			Logger.getLogger().error("ERROR: Product is not support:" + context.getProperty().getProductType());
			return null;
		}

		SubProductDao subProductDao = ServiceFactory.getDao(SubProductDao.class);
		SubProduct subProduct = subProductDao.findByKey(productId);
		return subProduct;
	}
	
	@Override
	public  List<SubProduct> findProductOnServer(String productId)
	{
		try
		{
			if(!context.getProperty().getProductType().equals(ProductType.KNUCKLE.toString()))
			{
				Logger.getLogger().error("ERROR: Product is not support:" + context.getProperty().getProductType());
				return null;
			}
			
			SubProductDao subProductDao = ServiceFactory.getDao(SubProductDao.class);
			return (List<SubProduct>) subProductDao.findAllMatchSerialNumber(productId);
		}
		catch (Exception e) {
			Logger.getLogger().warn(e, "Failed searching for proudcts by serial number.");
			return null;
		}
	}
	

}
