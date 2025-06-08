package com.honda.galc.client.teamlead.structure.delete;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.mvc.AbstractModel;
import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.MCOrderStructureDao;
import com.honda.galc.dao.conf.MCOrderStructureForProcessPointDao;
import com.honda.galc.dao.conf.MCPddaPlatformDao;
import com.honda.galc.dao.conf.MCProductStructureDao;
import com.honda.galc.dao.conf.MCProductStructureForProcessPointDao;
import com.honda.galc.dao.conf.MCStructureDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.dto.PddaDetailDto;
import com.honda.galc.dto.StructureDetailsDto;
import com.honda.galc.dto.StructureUnitDetailsDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.MCOrderStructure;
import com.honda.galc.entity.conf.MCOrderStructureForProcessPoint;
import com.honda.galc.entity.conf.MCOrderStructureForProcessPointId;
import com.honda.galc.entity.conf.MCOrderStructureId;
import com.honda.galc.entity.conf.MCProductStructure;
import com.honda.galc.entity.conf.MCProductStructureForProcessPoint;
import com.honda.galc.entity.conf.MCProductStructureForProcessPointId;
import com.honda.galc.entity.conf.MCProductStructureId;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.enumtype.StructureCreateMode;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

public class StructureDeleteModel extends AbstractModel {

	public StructureDeleteModel() {
		super();
	}
	
	public List<String> findAllProductTypes() {
		return getDao(ProductTypeDao.class).findAllProductTypes();
	}
	
	public List<Division> findAllDivision() {
		return getDao(DivisionDao.class).findAll();
	}
	
	public List<? extends BaseProduct> findByProductId(String sn, String productType) {
		ProductTypeData productTypeData = getDao(ProductTypeDao.class).findByKey(productType);
		if(!StringUtils.isEmpty(productTypeData.getProductType().name())) 
			return ProductTypeUtil.getProductDao(productTypeData.getProductType()).findAllBySN(sn); 
		else 
			return new ArrayList<Product>();
	}
	
	public BaseProduct findBaseProduct(String productId, String productType) {
		return ProductTypeUtil.getProductDao(productType).findByKey(productId);
	}
	
	public MCProductStructure findByProductIdAndDivisionId(String productId, String divisionId, String specCode) {
		return getDao(MCProductStructureDao.class).findByKey(productId, divisionId, specCode);
	}
	
	public List<StructureDetailsDto> loadStructureByOrderNumber(String orderNo, String divisionId, String processPointId) {
		if(getStructureCreateMode().equals(StructureCreateMode.PROCESS_POINT_MODE)) {
			return getDao(MCOrderStructureForProcessPointDao.class).findByOrderNumberDivisionAndProcessPoint(orderNo, divisionId, processPointId);
		} else {
			return getDao(MCOrderStructureDao.class).findByOrderNumberAndDivision(orderNo, divisionId);
		}
	}
	
	public MCProductStructure findById(MCProductStructureId id) {
		return getDao(MCProductStructureDao.class).findByKey(id);
	}
	
	public MCOrderStructure findById(MCOrderStructureId id) {
		return getDao(MCOrderStructureDao.class).findByKey(id);
	}
	
	public MCOrderStructureForProcessPoint findById(MCOrderStructureForProcessPointId id) {
		return getDao(MCOrderStructureForProcessPointDao.class).findByKey(id);
	}
	
	public MCProductStructureForProcessPoint findById(MCProductStructureForProcessPointId id) {
		return getDao(MCProductStructureForProcessPointDao.class).findByKey(id);
	}
	
	public List<MCProductStructure> findProductByStructureRevAndDivId(long structureRev, String divisionId) {
		return getDao(MCProductStructureDao.class).findProductByStructureRevAndDivId(structureRev, divisionId);
	}
	
	public List<MCOrderStructure> findOrderByStructureRevAndDivId(long structureRev, String divisionId) {
		return getDao(MCOrderStructureDao.class).findOrderByStructureRevAndDivId(structureRev, divisionId);
	}
	
	public void deleteProductStructure(MCProductStructure productStructure) {
		getDao(MCProductStructureDao.class).remove(productStructure);
	}
	
	public void deleteOrderStructure(MCOrderStructure orderStructure) {
		getDao(MCOrderStructureDao.class).remove(orderStructure);
	}
	
	public void deleteProductStructureByProcessPoint(MCProductStructureForProcessPoint id) {
		getDao(MCProductStructureForProcessPointDao.class).remove(id);
	}
	
	public void deleteOrderStructureByProcessPoint(MCOrderStructureForProcessPoint id) {
		getDao(MCOrderStructureForProcessPointDao.class).remove(id);
	}
	
	public void deleteStructure(String productSpecCode, String divId, long structureRevision) {
		getDao(MCStructureDao.class).deleteStructureBySpecCodeDivIdAndStructureRev(productSpecCode, divId, structureRevision);
	}

	public List<StructureUnitDetailsDto> loadStructureDetails(String productId, String divisionId, String productionLot, String processPoint, long structureRev) {
		if(getStructureCreateMode().equals(StructureCreateMode.DIVISION_MODE)) {
			if(!StringUtils.isEmpty(productId)) { 
				return getDao(MCProductStructureDao.class).findAllByProductAndDivision(productId, divisionId,structureRev);
			} else {
				return getDao(MCOrderStructureDao.class).findAllUnitDetailsByOrderNoAndDivision(productionLot, divisionId);
			} 
		} else {
			if(!StringUtils.isEmpty(productId)) {
				return getDao(MCProductStructureForProcessPointDao.class).findAllByProductIdDivisionAndProcessPoint(productId, divisionId, processPoint);
			} else {
				return getDao(MCOrderStructureForProcessPointDao.class).findAllByOrderNoDivisionAndProcessPoint(productionLot, divisionId, processPoint);
			} 
		}
	}
	
	public List<ProcessPoint> findAllByDivisionId(String divisionId) {
		return getDao(ProcessPointDao.class).findAllByDivisionId(divisionId);
	}

	public List<PddaDetailDto> findPlatformsByStructureRevision(long structureRev) {
		return getDao(MCPddaPlatformDao.class).findPlatformsByStructureRevision(structureRev);
	}
	
	public List<? extends BaseProduct> findAllProductsByProductionLot(String productionLot, String productType) {
		ProductTypeData productTypeData = getDao(ProductTypeDao.class).findByKey(productType);
		if(!StringUtils.isEmpty(productTypeData.getProductType().name())) 
			return ProductTypeUtil.getProductDao(productTypeData.getProductType()).findAllByProductionLot(productionLot); 
		else 
			return new ArrayList<Product>();
	}
	
	public List<MCProductStructure> findAllByProductIdDivisionAndSpecCode(List<String> products, String divisionId, String specCode) {
		return getDao(MCProductStructureDao.class).findAllByProductIdDivisionAndSpecCode(products, divisionId, specCode);
	}
	
	public List<MCProductStructureForProcessPoint> findAllByProductIdDivisionAndSpecCode(List<String> products, String divisionId, String specCode, String processPoint) {
		return getDao(MCProductStructureForProcessPointDao.class).findAllByProductIdDivisionAndSpecCode(products, divisionId, specCode, processPoint);
	}
	
	public void deleteAllProductStructure(List<MCProductStructure> id) {
		getDao(MCProductStructureDao.class).removeAll(id);
	}
	
	public void deleteAllProductStructureByProcessPoint(List<MCProductStructureForProcessPoint> id) {
		getDao(MCProductStructureForProcessPointDao.class).removeAll(id);
	}
	
	public List<InstalledPart> findAllByProductIdAndPartNames(String productId, List<String> partNames){
		return getDao(InstalledPartDao.class).findAllByProductIdAndPartNames(productId, partNames);
	}
	
	public void deleteInstalledParts(String productId, List<String> partNames){
		getDao(InstalledPartDao.class).deleteInstalledParts(productId, partNames);
	}
	
	private StructureCreateMode getStructureCreateMode() {
		String structureMode = PropertyService.getProperty(ApplicationConstants.DEFAULT_VIOS, ApplicationConstants.STRUCTURE_CREATE_MODE, StructureCreateMode.DIVISION_MODE.toString());
		return structureMode == null ? StructureCreateMode.DIVISION_MODE : StructureCreateMode.get(structureMode);
	}
	
	@Override
	public void reset() {
		
	}
}
