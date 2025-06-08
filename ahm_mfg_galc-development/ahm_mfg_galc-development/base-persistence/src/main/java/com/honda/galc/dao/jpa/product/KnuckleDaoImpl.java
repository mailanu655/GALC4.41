package com.honda.galc.dao.jpa.product;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.product.KnuckleDao;
import com.honda.galc.data.BuildAttributeTag;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.Knuckle;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.ProductionLot;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>KnuckleDaoImpl</code> is ... .
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Jul 14, 2015
 */
public class KnuckleDaoImpl extends BaseSubProductDaoImpl<Knuckle> implements KnuckleDao {

	@Override
	@Transactional
    public int createProducts(ProductionLot prodLot,String productType,String lineId,String ppId) {
		int count = 0;
		count = createKnuckles(prodLot, lineId, ppId);
	    if(count > 0) {
	    	saveProductionLot(prodLot);
	    }
		return count;
	}
	
	private int createKnuckles(ProductionLot prodLot,String lineId,String ppId ) {
		List<Knuckle> leftKnuckles = createKnuckles(prodLot,Product.SUB_ID_LEFT,lineId,ppId);
		List<Knuckle> rightKnuckles = createKnuckles(prodLot,Product.SUB_ID_RIGHT,lineId,ppId);
		
        if(leftKnuckles.isEmpty() || rightKnuckles.isEmpty()) return 0;
        
        List<Knuckle> allKnuckles = new ArrayList<Knuckle>();
        allKnuckles.addAll(leftKnuckles);
        allKnuckles.addAll(rightKnuckles);
        
        deleteAll(prodLot.getProductionLot(),Product.SUB_ID_LEFT);
        deleteAll(prodLot.getProductionLot(),Product.SUB_ID_RIGHT);
        saveAll(allKnuckles);
        
        String leftStartNumber = ProductNumberDef.KNU.getSequence(leftKnuckles.get(0).getProductId());
        String rightStartNumber = ProductNumberDef.KNU.getSequence(rightKnuckles.get(0).getProductId());
        //		set left and right start serial number
    	String startNumber = "L"+leftStartNumber+"*R"+rightStartNumber;
    	
     	prodLot.setStartProductId(startNumber);
     	
    	return allKnuckles.size();
 	}
	
	private String getBuildAttribute(String productSpecCode,String attribute) {
		String specCode = ProductSpec.excludeToModelCode(productSpecCode)+"%";
		List<BuildAttribute> buildAttributes = buildAttributeDao.findAllMatchId(specCode, attribute);
		for(BuildAttribute buildAttribute : buildAttributes) {
			if(productSpecCode.startsWith(buildAttribute.getProductSpecCode())) {
				return buildAttribute.getAttributeValue();
			}
		}
		return "";
	}
	
	private int findNextSerialNumber(String partNumberPrefix) {
		String productId = findMaxProductId(partNumberPrefix);
		if(productId == null) {
			return 1;
		}
		else {
			return Integer.parseInt(StringUtils.trim(productId.substring(partNumberPrefix.length()))) + 1;
		}
	}
	
	private Knuckle createKnuckle(ProductionLot prodLot, String productId,String subId,String lineId,String ppId) {
		Knuckle subProduct = new Knuckle();
		subProduct.setProductId(productId);
		subProduct.setProductionLot(prodLot.getProductionLot());
		subProduct.setKdLotNumber(prodLot.getKdLotNumber());
		subProduct.setSubId(subId);
		subProduct.setProductSpecCode(prodLot.getProductSpecCode());
		subProduct.setPlanOffDate(prodLot.getPlanOffDate());
		subProduct.setProductionDate(prodLot.getProductionDate());
		subProduct.setLastPassingProcessPointId(ppId);
		subProduct.setTrackingStatus(lineId);
		return subProduct;
	}
	
	private List<Knuckle> createKnuckles(ProductionLot prodLot,String subId,String lineId,String ppId) {
		List<Knuckle> knuckles = new ArrayList<Knuckle>();
		String attributeName = Product.SUB_ID_LEFT.equals(subId) ? BuildAttributeTag.KNUCKLE_LEFT_SIDE : BuildAttributeTag.KNUCKLE_RIGHT_SIDE;
		String partNumber = getBuildAttribute(prodLot.getProductSpecCode(), attributeName);
		if(StringUtils.isEmpty(partNumber)) return knuckles;
		
		String modelYear = ProductSpec.excludeToModelYearCode(prodLot.getProductSpecCode());
		int sn = findNextSerialNumber(partNumber+modelYear);
		for(int i = 0; i<prodLot.getLotSize();i++) {
			String productId = partNumber  + modelYear + StringUtils.leftPad(Integer.toString(sn + i), ProductNumberDef.KNU.getSequenceLength(), "0"); 
			Knuckle knuckle = createKnuckle(prodLot, productId, subId, lineId, ppId);
			knuckles.add(knuckle);
		}
		return knuckles;
	}
}
