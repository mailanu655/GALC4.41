package com.honda.galc.client.datacollection.observer;
import java.util.List;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.Product;
import com.honda.galc.service.ServiceFactory;
/**
 * 
 * <h3>LotControlFramePersistenceManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlFramePersistenceManager description </p>
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
 * <TD>Jun 13, 2011</TD>
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
 * @since Jun 13, 2011
 */
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class LotControlFramePersistenceManager extends LotControlPartLotPersistenceManager
{

	public LotControlFramePersistenceManager(ClientContext context) {
		super(context);
	}

	@Override
	public synchronized Product confirmProductOnServer(String productId) {
		if(!context.getProperty().getProductType().equals(ProductType.FRAME.toString()))
		{
			Logger.getLogger().error("ERROR: Product is not support:" + context.getProperty().getProductType());
			return null;
		}
		
		FrameDao frameDao = ServiceFactory.getDao(FrameDao.class);
		Frame frame = frameDao.findByKey(productId);
		return frame;
	}
	@Override
	public  List<Frame> findProductOnServer(String productId)
	{
		try
		{
			if(!context.getProperty().getProductType().equals(ProductType.FRAME.toString()))
			{
				Logger.getLogger().error("ERROR: Product is not support:" + context.getProperty().getProductType());
				return null;
			}
			
			FrameDao frameDao = ServiceFactory.getDao(FrameDao.class);
			return (List<Frame>) frameDao.findAllBySN(productId);
		}
		catch (Exception e) {
			Logger.getLogger().warn(e, "Failed searching for proudcts by serial number.");
			return null;
		}
	}
	
}
