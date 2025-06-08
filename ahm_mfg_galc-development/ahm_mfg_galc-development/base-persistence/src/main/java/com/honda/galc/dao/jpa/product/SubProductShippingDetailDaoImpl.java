package com.honda.galc.dao.jpa.product;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.SubProductShippingDao;
import com.honda.galc.dao.product.SubProductShippingDetailDao;
import com.honda.galc.entity.enumtype.SubProductShippingDetailStatus;
import com.honda.galc.entity.product.SubProductShippingDetail;
import com.honda.galc.entity.product.SubProductShippingDetailId;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>SubProductShippingDetailDaoImpl Class description</h3>
 * <p> SubProductShippingDetailDaoImpl description </p>
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
 * Feb 8, 2013
 *
 *
 */
public class SubProductShippingDetailDaoImpl extends BaseDaoImpl<SubProductShippingDetail,SubProductShippingDetailId> implements SubProductShippingDetailDao {

    private static final long serialVersionUID = 1L;
    
    private static final String FIND_ALL_BY_CONTAINER =
    	"SELECT a.* FROM GALADM.SUB_PRODUCT_SHIPPING_DETAIL_TBX a,GALADM.SUB_PRODUCT_SHIPPING_TBX b " +
    	"WHERE a.CONTAINER_ID =?1 AND a.status < 8 AND b.KD_LOT_NUMBER = a.KD_LOT_NUMBER " + 
    	"AND b.PRODUCTION_LOT = a.PRODUCTION_LOT and b.PRODUCT_TYPE =?2 " + 
    	"ORDER BY SUB_ID ASC,PRODUCT_SEQ_NO ASC";
    
    private static final String COUNT_NOT_SHIPPED_PRODUCTS =
    	"SELECT COUNT(*) FROM GALADM.SUB_PRODUCT_SHIPPING_DETAIL_TBX " + 
    	"WHERE KD_LOT_NUMBER =?1 AND PRODUCTION_LOT = ?2 AND STATUS < 8";
    
    private static final String FIND_ALL_BY_KD_LOT = 
    	"SELECT a.* FROM galadm.SUB_PRODUCT_SHIPPING_DETAIL_TBX a,GALADM.SUB_PRODUCT_SHIPPING_TBX b " +  
    	" WHERE a.KD_LOT_NUMBER =?1 AND b.KD_LOT_NUMBER = a.KD_LOT_NUMBER AND b.PRODUCTION_LOT = a.PRODUCTION_LOT AND b.PRODUCT_TYPE = ?2" +  
    	" ORDER BY SUB_ID,PRODUCT_SEQ_NO ASC";
    
    private static final String FIND_ALL_BY_KD_LOT_AND_SUBID = 
    	"SELECT a.* FROM galadm.SUB_PRODUCT_SHIPPING_DETAIL_TBX a,GALADM.SUB_PRODUCT_SHIPPING_TBX b " +  
    	" WHERE a.KD_LOT_NUMBER =?1 AND a.SUB_ID = ?2 b.KD_LOT_NUMBER = a.KD_LOT_NUMBER AND b.PRODUCTION_LOT = a.PRODUCTION_LOT AND b.PRODUCT_TYPE = ?3" +  
    	" ORDER BY SUB_ID,PRODUCT_SEQ_NO ASC";
    
    private static final String FIND_ALL_NOT_SHIPPED = 
    	"SELECT a.* FROM galadm.SUB_PRODUCT_SHIPPING_DETAIL_TBX a,GALADM.SUB_PRODUCT_SHIPPING_TBX b " +  
    	" WHERE A.PRODUCT_ID IS NULL AND b.KD_LOT_NUMBER = a.KD_LOT_NUMBER AND b.PRODUCTION_LOT = a.PRODUCTION_LOT AND b.PRODUCT_TYPE = ?1" +  
    	" ORDER BY SUB_ID,PRODUCT_SEQ_NO ASC";
    
    private static final String DELETE_KD_LOT =
    	"SELECT a.* FROM galadm.SUB_PRODUCT_SHIPPING_DETAIL_TBX a " +
    	" WHERE a.KD_LOT_NUMBER = ?1 AND EXISTS( " + 
    	" SELECT * FROM GALADM.SUB_PRODUCT_SHIPPING_TBX " + 
    	" WHERE KD_LOT_NUMBER = a.KD_LOT_NUMBER AND PRODUCTION_LOT = a.PRODUCTION_LOT AND PRODUCT_TYPE = ?2) ";
    @Autowired 
    SubProductShippingDao subProductShippingDao;
    
	public SubProductShippingDetail findByProductId(String productId) {
		
		return findFirst(Parameters.with("productId",productId));
		
	}

	public List<SubProductShippingDetail> findAllByKdLotNumber(String productType,String kdLotNumber) {
		return findAllByNativeQuery(FIND_ALL_BY_KD_LOT, Parameters.with("1", kdLotNumber).put("2",productType));
	}
	
	public List<SubProductShippingDetail> findAllByKdLotNumber(String productType,String kdLotNumber,String subId) {
		return findAllByNativeQuery(FIND_ALL_BY_KD_LOT_AND_SUBID, 
				Parameters.with("1", kdLotNumber).put("2",subId).put("3",productType));
	}

	public List<SubProductShippingDetail> findAllNotShipped(String productType) {
		return findAllByNativeQuery(FIND_ALL_NOT_SHIPPED, Parameters.with("1",productType));
	}

    @Transactional 
    public int deleteKdLots(String productType,List <String> kdLots)  
    {
        int count = 0;
        for( String kdLot : kdLots )
        {
            count = count + deleteKdLot(kdLot,productType);
        }
        return count;
    }
    
    private int deleteKdLot(String kdLot, String productType) {
    	return executeNativeUpdate(DELETE_KD_LOT, Parameters.with("1", kdLot).put("2",productType));
    }

	public List<SubProductShippingDetail> findAllByShippingLot(String kdLotNumber, String productionLot, String subId) {
		return findAll(Parameters.with("id.kdLotNumber", kdLotNumber).put("id.productionLot", productionLot).put("id.subId",subId),
				new String[]{"id.subId","id.productSeqNo"},	true);
	}

	public List<SubProductShippingDetail> findAllByShippingLot(String kdLotNumber, String productionLot) {
		return findAll(Parameters.with("id.kdLotNumber", kdLotNumber).put("id.productionLot", productionLot),
				new String[]{"id.subId","id.productSeqNo"},	true);
	}

	@Transactional 
    public List<SubProductShippingDetail>  removeSubProduct(String productId) {
		List<SubProductShippingDetail> details = findAllByProductId(productId);
		for(SubProductShippingDetail detail :details) {
			detail.setProductId(null);
			detail.setStatus(SubProductShippingDetailStatus.WAITING);
			update(detail);
			subProductShippingDao.decrementActualQuantity(detail.getKdLot(), detail.getProductionLot());
		}
		return details;
	}

	public List<SubProductShippingDetail> findAllByProductId(String productId) {
		return findAll(Parameters.with("productId",productId));
	}

	public List<SubProductShippingDetail> findAllNotShippedByContainer(String productType, String containerId) {
		return findAllByNativeQuery(FIND_ALL_BY_CONTAINER, Parameters.with("1", containerId).put("2",productType));
	}

	public int countNotShippedProducts(String kdLotNumber, String productionLot) {
		return findFirstByNativeQuery(COUNT_NOT_SHIPPED_PRODUCTS, 
				Parameters.with("1", kdLotNumber).put("2",productionLot), Integer.class);
	}


}
