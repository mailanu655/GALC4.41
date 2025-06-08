package com.honda.galc.dao.jpa.product;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.PartShipmentDao;
import com.honda.galc.dao.product.PartShipmentProductDao;
//import com.honda.galc.dto.PartShipmentDto;
import com.honda.galc.entity.enumtype.BuildStatus;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.PartShipment;
import com.honda.galc.entity.product.PartShipmentProduct;
import com.honda.galc.entity.product.PartShipmentProductId;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.ServiceFactory;

public class PartShipmentProductDaoImpl extends BaseDaoImpl<PartShipmentProduct, PartShipmentProductId> implements PartShipmentProductDao {

	private static final String FIND_PART_SHIPMENT = "SELECT f.PRODUCT_ID, f.CURRENT_PRODUCT_SPEC_CODE, f.CURRENT_ORDER_NO FROM galadm.PART_SHIPMENT_PRODUCTS_TBX p inner join galadm.MBPN_PRODUCT_TBX f on p.product_Id = f.product_Id ";
	private static final String FIND_PARTS_BY_SHIPMENT = "SELECT * FROM galadm.PART_SHIPMENT_PRODUCTS_TBX p inner join galadm.MBPN_PRODUCT_TBX f on p.product_Id = f.product_Id where p.SHIPMENT_ID = ?1 AND ( p.build_Status IS NULL OR p.build_status <> ?2)";
	private static final String FIND_ALL_BY_SHIPMENT = "SELECT * FROM galadm.PART_SHIPMENT_PRODUCTS_TBX p inner join galadm.MBPN_PRODUCT_TBX f on p.product_Id = f.product_Id where p.SHIPMENT_ID = ?1";
	private static final String FIND_PARTS_BY_PRODUCT_SHIPMENT = "SELECT * FROM galadm.PART_SHIPMENT_PRODUCTS_TBX p  where p.PRODUCT_ID = ?1 AND p.SHIPMENT_ID = ?2 AND ( p.build_Status IS NULL OR p.build_status <> ?3)";
	private static final String FIND_PRODUCT_PART_OF_SHIPMENT = "SELECT * FROM galadm.PART_SHIPMENT_PRODUCTS_TBX p  where p.PRODUCT_ID = ?1 AND ( p.build_Status IS NULL OR p.build_status <> ?2)";
	
	public PartShipmentProduct findByProductId(String productId){
		String sql = FIND_PRODUCT_PART_OF_SHIPMENT ;
		Parameters params = Parameters.with("1", productId);
		params.put("2",BuildStatus.REMOVE.getId());
		return findFirstByNativeQuery(sql, params, PartShipmentProduct.class);
	}
	public PartShipmentProduct findByProductIdShipmentId(String productId, Integer shipmentId) {
		String sql = FIND_PARTS_BY_PRODUCT_SHIPMENT ;
		Parameters params = Parameters.with("1", productId);
		params.put("2",shipmentId);
		params.put("3",BuildStatus.REMOVE.getId());
		return findFirstByNativeQuery(sql, params, PartShipmentProduct.class);
	}
	
/*	public List<PartShipmentDto> findAllByPartShipments() {
		String sql = FIND_PART_SHIPMENT ;
		return findAllByNativeQuery(sql, null, PartShipmentDto.class);
	}*/

	public List<MbpnProduct> findByShipmentId(Integer shipmentId) {
		String sql = FIND_PARTS_BY_SHIPMENT ;
		Parameters params = Parameters.with("1", shipmentId);
		params.put("2",3);
		return findAllByNativeQuery(sql, params, MbpnProduct.class);
	}
	
	public List<MbpnProduct> findAllByShipmentId(Integer shipmentId) {
		String sql = FIND_ALL_BY_SHIPMENT ;
		Parameters params = Parameters.with("1", shipmentId);
		return findAllByNativeQuery(sql, params, MbpnProduct.class);
	}
	
	public void markShipmentUnsentByProductId(String productId){
		Parameters params = Parameters.with("id.productId", productId);
		List<PartShipmentProduct> partShipmentProducts = findAll(params);
	
		if(partShipmentProducts != null  && partShipmentProducts.size() > 0){
			for(PartShipmentProduct product : partShipmentProducts){
				if(product.getBuildStatus() == null || product.getBuildStatus()!= BuildStatus.REMOVE.getId()){
					PartShipmentDao partShipmentDao = ServiceFactory.getDao(PartShipmentDao.class);
					PartShipment partShipment = partShipmentDao.findByKey(product.getId().getShipmentId());
					partShipment.setSendStatus(0);
					partShipmentDao.save(partShipment);
				}
			
			}
				
		}
			
	}

}
