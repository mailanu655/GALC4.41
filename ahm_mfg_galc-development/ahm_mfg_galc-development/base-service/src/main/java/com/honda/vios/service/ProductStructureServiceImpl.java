package com.honda.vios.service;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.MCOrderStructureDao;
import com.honda.galc.dao.conf.MCOrderStructureForProcessPointDao;
import com.honda.galc.dao.conf.MCProductPddaPlatformDao;
import com.honda.galc.dao.conf.MCProductStructureDao;
import com.honda.galc.dao.conf.MCProductStructureForProcessPointDao;
import com.honda.galc.entity.conf.BaseMCProductStructure;
import com.honda.galc.entity.conf.MCOrderStructure;
import com.honda.galc.entity.conf.MCOrderStructureForProcessPoint;
import com.honda.galc.entity.conf.MCOrderStructureForProcessPointId;
import com.honda.galc.entity.conf.MCOrderStructureId;
import com.honda.galc.entity.conf.MCProductPddaPlatform;
import com.honda.galc.entity.conf.MCProductStructure;
import com.honda.galc.entity.conf.MCProductStructureForProcessPoint;
import com.honda.galc.entity.conf.MCProductStructureForProcessPointId;
import com.honda.galc.entity.conf.MCProductStructureId;
import com.honda.galc.entity.conf.MCStructure;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.enumtype.StructureCreateMode;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.service.vios.ProductStructureService;
import com.honda.galc.service.vios.StructureCreateService;
import com.honda.galc.vios.dto.PddaPlatformDto;


/**
 * @author Fredrick Yessaian
 * @date Mar 16 2015
 */
public class ProductStructureServiceImpl implements ProductStructureService {
	
	public BaseMCProductStructure findOrCreateProductStructure(BaseProduct productObj, ProcessPoint processPoint, String mode) throws Exception {

		BaseMCProductStructure prodStruc = null;
		String divisionId = processPoint.getDivisionId().trim();
		String productId = productObj.getProductId().trim();
		String productName = productObj.getProductType().getProductName().trim();
		String processPointId = processPoint.getProcessPointId().trim();
		String prodSpecCode = productObj.getProductSpecCode();
		String orderNo = productObj.getProductionLot();
		PddaPlatformDto pddaPlatform = new PddaPlatformDto();
		
		Logger.getLogger().info("ProductStructureServiceImpl.findOrCreateProductStructure method called for product type: "	+ productName + " product id : " + productId + " from process point : " + processPointId + " with mode : " + mode);
		if (StringUtils.equalsIgnoreCase(mode, StructureCreateMode.DIVISION_MODE.toString())) {
			prodStruc = ServiceFactory.getDao(MCProductStructureDao.class).findByKey(new MCProductStructureId(productId, divisionId, prodSpecCode));
			if (prodStruc == null) {
				//If product type is MBPN, use model year code to create structure
				if(ProductTypeUtil.isMbpnProduct(productObj.getProductType())) {
					MCProductPddaPlatform platform = ServiceFactory.getDao(MCProductPddaPlatformDao.class).findByKey(productId);
					if(platform != null) {
						pddaPlatform = platform.convertToPddaPlatformDto();
					}
				}

				// bak - 201508319 - Check for an order number, if no order #
				// need to create based on product spec code and division
				if (StringUtils.isBlank(orderNo)) {
					try {
						MCStructure struct = ServiceFactory.getService(StructureCreateService.class).findOrCreateStructure(prodSpecCode, divisionId, pddaPlatform);
						if (struct != null) {
							return insertProductStructure(productId, divisionId, struct.getId().getProductSpecCode(), (int) struct.getId().getRevision());
						}
					} catch (Exception e) {
						Logger.getLogger().error(e, "Exception while processing for product Id : " + productId + " at process point : " + processPoint);
						e.printStackTrace();
					}
				} else {
					MCOrderStructure ordStru = ServiceFactory.getDao(MCOrderStructureDao.class).findByKey(new MCOrderStructureId(orderNo, divisionId));
					if (ordStru == null) {
						try {
							MCOrderStructure newlyCreateOrder = ServiceFactory.getService(StructureCreateService.class).findOrCreateOrderStructure(orderNo,	divisionId, pddaPlatform);
							return insertProductStructure(productId, divisionId, newlyCreateOrder.getProductSpecCode(), (int) newlyCreateOrder.getStructureRevision());
						} catch (Exception e) {
							Logger.getLogger().error("Exception while processing for product Id : " + productId + " at process point : " + processPoint);
							Logger.getLogger().error("Exception Message : " + e.getMessage());
							e.printStackTrace();
						}
					} else {
						Logger.getLogger().info("ProductStructureServiceImpl.findOrCreateProductStructure method call finished for product type: " + productName + " product id : " + productId + " from process point : " + processPointId);
						return insertProductStructure(productId, divisionId, ordStru.getProductSpecCode(), (int) ordStru.getStructureRevision());
					}
				}
			} else {
				Logger.getLogger().info("ProductStructureServiceImpl.findOrCreateProductStructure method call finished for product type: " + productName + " product id : " + productId + " from process point : " + processPointId);
				return prodStruc;
			}
		} else if (StringUtils.equalsIgnoreCase(mode, StructureCreateMode.PROCESS_POINT_MODE.toString())) {
			prodStruc = ServiceFactory.getDao(MCProductStructureForProcessPointDao.class).findByKey(new MCProductStructureForProcessPointId(productId, processPointId, prodSpecCode));
			if (prodStruc == null) {
				// bak - 201508319 - Check for an order number, if no order #
				// need to create based on product spec code and division
				if (StringUtils.isBlank(orderNo)) {
					try {
						MCStructure struct = ServiceFactory.getService(StructureCreateService.class).findOrCreateStructure(prodSpecCode, divisionId, processPointId, mode, pddaPlatform);
						if (struct != null) {
							return insertProductStructureForProcessPoint(productId, processPointId, struct.getId().getProductSpecCode(), (int) struct.getId().getRevision(), divisionId);
						}
					} catch (Exception e) {
						Logger.getLogger().error(e, "Exception while processing for product Id : " + productId + " at process point : " + processPoint);
						e.printStackTrace();
					}
				} else {
					MCOrderStructureForProcessPoint ordStru = ServiceFactory.getDao(MCOrderStructureForProcessPointDao.class).findByKey(new MCOrderStructureForProcessPointId(orderNo, processPointId));
					if (ordStru == null) {
						try {
							MCOrderStructureForProcessPoint newlyCreateOrder = ServiceFactory.getService(StructureCreateService.class).findOrCreateOrderStructureForProcessPoint(orderNo, divisionId, processPointId, mode, pddaPlatform);
							return insertProductStructureForProcessPoint(productId, processPointId, newlyCreateOrder.getProductSpecCode(), (int) newlyCreateOrder.getStructureRevision(), divisionId);
						} catch (Exception e) {
							Logger.getLogger().error("Exception while processing for product Id : " + productId + " at process point : " + processPoint);
							Logger.getLogger().error("Exception Message : " + e.getMessage());
							e.printStackTrace();
						}
					} else {
						Logger.getLogger().info("ProductStructureServiceImpl.findOrCreateProductStructure method call finished for product type: " + productName + " product id : " + productId + " from process point : " + processPointId);
						return insertProductStructureForProcessPoint(productId, processPointId, ordStru.getProductSpecCode(), (int) ordStru.getStructureRevision(), divisionId);
					}
				}
			} else {
				Logger.getLogger().info("ProductStructureServiceImpl.findOrCreateProductStructure method call finished for product type: " + productName + " product id : " + productId + " from process point : " + processPointId);
				return prodStruc;
			}
		}
		Logger.getLogger().info("ProductStructureServiceImpl.findOrCreateProductStructure method call finished for product type: " + productName + " product id : " + productId + " from process point : " + processPointId);
		return prodStruc;
	}

	private MCProductStructure insertProductStructure(String productId, String divisionId, String productSpecCode, int structureRev) {
		
		try {
			MCProductStructure productStructure = new MCProductStructure();
			productStructure.setId(new MCProductStructureId(productId,divisionId,productSpecCode));
			productStructure.setStructureRevision(structureRev);
			return ServiceFactory.getDao(MCProductStructureDao.class).save(productStructure);
		} catch (Exception e) {
			Logger.getLogger().error("Exception while processing product id : "+ productId+" @ insertProductStructure() in ProductStructureServiceImpl");
			Logger.getLogger().error(e, "Exception @ insert ProductStructure method");
			return null;
		}
	}
	
	private BaseMCProductStructure insertProductStructureForProcessPoint(String productId, String processPointId, String productSpecCode, int structureRev, String divisionId) {
		
		try {
			MCProductStructureForProcessPoint productStructure = new MCProductStructureForProcessPoint();
			productStructure.setId(new MCProductStructureForProcessPointId(productId,processPointId,productSpecCode));
			productStructure.setStructureRevision(structureRev);
			productStructure.setDivisionId(divisionId);
			return ServiceFactory.getDao(MCProductStructureForProcessPointDao.class).save(productStructure);
		} catch (Exception e) {
			Logger.getLogger().error("Exception while processing product id : "+ productId+" @ insertProductStructure() in ProductStructureServiceImpl");
			Logger.getLogger().error(e, "Exception @ insert ProductStructure method");
			return null;
		}
	}
	
}
