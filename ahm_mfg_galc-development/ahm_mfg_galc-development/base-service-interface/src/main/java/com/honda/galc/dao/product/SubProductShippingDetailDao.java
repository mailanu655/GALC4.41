package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.SubProductShippingDetail;
import com.honda.galc.entity.product.SubProductShippingDetailId;
import com.honda.galc.service.IDaoService;


public interface SubProductShippingDetailDao extends IDaoService<SubProductShippingDetail, SubProductShippingDetailId> {

	public SubProductShippingDetail findByProductId(String productId); 
	
	public List<SubProductShippingDetail> findAllByProductId(String productId); 
	
	/*
	 * find all by kd lot number. result list is ordered by SUB_ID and PRODUCT_SEQ_NO (Ascending)
	 */
	public List<SubProductShippingDetail> findAllByKdLotNumber(String productType,String kdLotNumber);
	
	public List<SubProductShippingDetail> findAllByKdLotNumber(String productType,String kdLotNumber,String side);
	
	public List<SubProductShippingDetail> findAllNotShipped(String productType);

    public int deleteKdLots(String productType,List<String> kdLotsArray);

	public List<SubProductShippingDetail> findAllByShippingLot(String kdLotNumber,String productionLot, String side);
	
	public List<SubProductShippingDetail> findAllByShippingLot(String kdLotNumber,String productionLot);
	
	public List<SubProductShippingDetail> removeSubProduct(String productId);
	
	public List<SubProductShippingDetail> findAllNotShippedByContainer(String productType,String containerId);
	
	public int countNotShippedProducts(String kdLotNumber,String productionLot);
}
