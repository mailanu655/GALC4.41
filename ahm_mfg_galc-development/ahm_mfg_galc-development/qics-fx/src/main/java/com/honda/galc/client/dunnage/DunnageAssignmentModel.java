package com.honda.galc.client.dunnage;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.qi.base.QiProcessModel;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.property.DunnagePropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
/**
 * <h3>DunnageAssignmentModel description</h3> <h4>Description</h4>
 * <p>
 * <code>DunnageAssignmentModel</code> is model for Dunnage Assignment Screen
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>L&T Infotech</TD>
 * <TD>14/5/2017</TD>
 * <TD>1.0.1</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.1
 * @author L&T Infotech
 */
public class DunnageAssignmentModel extends QiProcessModel {
	
	/**
	 * This method is used to find all Dunnage products
	 * @param dunnageNumber
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BaseProduct> findAllDunnageProducts(String dunnageNumber) {
		List<BaseProduct> resultList = (List<BaseProduct>) ProductTypeUtil.getProductDao(ProductType.getType(getProductType())).findAllByDunnage(dunnageNumber) ;
		 return resultList == null? new ArrayList<BaseProduct>() : resultList;

	}
	/**
	 * This method is used to get DunnagePropertyBean
	 * @return
	 */
	public DunnagePropertyBean getDunnagePropertyBean(){
		return PropertyService.getPropertyBean(DunnagePropertyBean.class,getProcessPointId());
	}
	/**
	 * This method is used to update Dunnage.
	 * @param defectResult
	 * @return 
	 */
	public int updateDunnage(String productId, String dunnageNumber, int dunnageCapacity) {
		return ProductTypeUtil.getProductDao(getProductType()).updateDunnage(productId, dunnageNumber, dunnageCapacity);
	}
	
	public void removeDunnageProduct(BaseProduct product) {
		ProductTypeUtil.getProductDao(ProductType.getType(getProductType())).removeDunnage(product.getProductId());
	}

}

