package com.honda.galc.client.datacollection.observer;

import java.util.List;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>LotControlSubProductPersistenceManager Class description</h3>
 * <p> LotControlSubProductPersistenceManager description </p>
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
 * Nov 30, 2011
 *
 *
 */
public abstract class LotControlSubProductPersistenceManager extends LotControlPersistenceManager {
	
	public LotControlSubProductPersistenceManager(ClientContext context) {
		super(context);
	}

	@Override
	public synchronized Product confirmProductOnServer(String productId) {
		SubProductDao subProductDao = ServiceFactory.getDao(SubProductDao.class);
		SubProduct subProduct = subProductDao.findByKey(productId);
		return subProduct;
	}
	
	@Override
	public  List<SubProduct> findProductOnServer(String productId)
	{
		try	{
			SubProductDao subProductDao = ServiceFactory.getDao(SubProductDao.class);
			return (List<SubProduct>) subProductDao.findAllMatchSerialNumber(productId);
		}
		catch (Exception e) {
			Logger.getLogger().warn(e, "Failed searching for proudcts by serial number.");
			return null;
		}
	}
	
	@Override
	public Product createProduct(Product product) {
		try	{
			SubProductDao subProductDao = ServiceFactory.getDao(SubProductDao.class);
			return subProductDao.save((SubProduct)product);
		}
		catch (Exception e) {
			Logger.getLogger().warn(e, "Failed to create Sub product "  + product);
			return null;
		}
	}
	
	@Override
	public ProductionLot getProductionLot(String productId){
		SubProduct subProduct= null;
		ProductionLot productionLot = null;
		SubProductDao subProductDao = ServiceFactory.getDao(SubProductDao.class);
		subProduct = subProductDao.findByKey(productId);
		if(subProduct!= null  && subProduct.getProductionLot()!= null){
		ProductionLotDao productionLotDao = ServiceFactory.getDao(ProductionLotDao.class);
		productionLot = productionLotDao.findByKey(subProduct.getProductionLot().toString());
		}
		return productionLot;
	}
	
}
