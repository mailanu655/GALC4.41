package com.honda.galc.data;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.MbpnProductTypeDao;
import com.honda.galc.entity.product.MbpnProductType;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>ProductTypeCatelog</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductTypeCatelog description </p>
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
 * <TD>Feb 21, 2017</TD>
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
 * @since Feb 21, 2017
 */

public class ProductTypeCatalog {
	private List<MbpnProductType> mbpnProductTypes;
	private static final ProductTypeCatalog instance = new ProductTypeCatalog();
	
	private ProductTypeCatalog(){};
	
	
	public static ProductTypeCatalog getInstance(){
		return instance;
	}
	
	
	List<MbpnProductType> getMbpnProductTypes(){
		if(this.mbpnProductTypes != null){
			return mbpnProductTypes;
		} else
			return loadMbpnProductTypes();
			
	}

	private synchronized List<MbpnProductType> loadMbpnProductTypes() {
		this.mbpnProductTypes =  ServiceFactory.getDao(MbpnProductTypeDao.class).findAll();
		return mbpnProductTypes;
		
	}
	
	public static ProductType getProductType(String type){
		try {
			ProductType existType = ProductType.getType(type);
			return existType == null ? valueOfProductTypeByMbpnProductType(type) : existType;
		} catch (Exception e){
			Logger.getLogger().error(e, "Exception: Invalid product type:" + type);
		}
		
		return null;
	}

	private static ProductType valueOfProductTypeByMbpnProductType(String type) {
		for(MbpnProductType mbpnProdctType : getInstance().getMbpnProductTypes()){
			if(mbpnProdctType.getProductType().equals(type)){
				return ProductType.MBPN_PART;
			}
				
		}
		
		return null;
	}
	
	public static void update(){ //update when data updated by TL
		if(getInstance().mbpnProductTypes != null)
			getInstance().loadMbpnProductTypes();
	}
	
	
	public static MbpnProductType valueOfMbpnProductTypeByMbpn(String mbpn){
		for(MbpnProductType mbpnProdType : getInstance().getMbpnProductTypes())
			if(mbpnProdType.getId().getMainNo().equals(MbpnDef.MAIN_NO.getValue(mbpn)))
				return mbpnProdType;
		
		return null;
	};
	
	public static boolean isMbpnSubProduct(String type){
		return ProductType.MBPN_PART == valueOfProductTypeByMbpnProductType(type);
	}
	
	public static List<MbpnProductType> getMbpnProductTypes(String type){
		List<MbpnProductType> typeList = new ArrayList<MbpnProductType>();
		for(MbpnProductType mpt : getInstance().getMbpnProductTypes())
			if(type.equals(mpt.getProductType()))
				typeList.add(mpt);
		
		return typeList;
	}
	
	
}
