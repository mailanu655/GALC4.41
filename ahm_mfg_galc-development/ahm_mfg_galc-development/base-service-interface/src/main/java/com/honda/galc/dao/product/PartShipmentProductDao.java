package com.honda.galc.dao.product;


import java.util.List;

//import com.honda.galc.dto.PartShipmentDto;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.PartShipmentProduct;
import com.honda.galc.entity.product.PartShipmentProductId;
import com.honda.galc.service.IDaoService;

public interface PartShipmentProductDao extends IDaoService<PartShipmentProduct, PartShipmentProductId>{
	public PartShipmentProduct findByProductId(String productId);
	public PartShipmentProduct findByProductIdShipmentId(String productId, Integer shipmentId);
	//public List<PartShipmentDto> findAllByPartShipments();
	public List<MbpnProduct> findByShipmentId(Integer shipmentId);
	public void markShipmentUnsentByProductId(String productId);
	public List<MbpnProduct> findAllByShipmentId(Integer shipmentId);
}
