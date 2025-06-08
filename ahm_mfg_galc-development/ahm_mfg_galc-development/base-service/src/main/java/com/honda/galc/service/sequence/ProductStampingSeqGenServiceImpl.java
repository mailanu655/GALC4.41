package com.honda.galc.service.sequence;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.entity.product.ProductStampingSequenceId;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.oif.property.ReplicateScheduleProperty2;
import com.honda.galc.property.ProductStampingSeqGenPropertyBean;
import com.honda.galc.service.ProductStampingSeqGenService;
import com.honda.galc.service.property.PropertyService;

import edu.emory.mathcs.backport.java.util.Arrays;



/** * * 
 * @version 1
 * @author Gangadhararao Gadde 
 * @since Aug 08, 2017
 */
public class ProductStampingSeqGenServiceImpl implements ProductStampingSeqGenService {


	@Autowired
	MbpnDao mbpnDao;

	private ProductStampingSeqGenPropertyBean productStampingSeqGenPropertyBean=null;

	private Logger getLogger(){
		return Logger.getLogger(this.getClass().getSimpleName());
	}

	public void initialize(String componentId)
	{
		productStampingSeqGenPropertyBean=PropertyService.getPropertyBean(ProductStampingSeqGenPropertyBean.class, componentId);
	}

	public List<ProductStampingSequence> createStampingSequenceList(String targetSpecCode, String targetProcessLocation, PreProductionLot targetPreProductionLot,String componentId)  {

		initialize(componentId);
		String productType = productStampingSeqGenPropertyBean.getProductType().get(targetProcessLocation);

		
		Integer seqLen = Integer.parseInt(productStampingSeqGenPropertyBean.getSeqLengthMap().get(productType));
		Mbpn targetMbpn =mbpnDao.findByKey(targetSpecCode);
		
		if(targetMbpn != null) {
			String startProductIdPrefix = getProductIdPrefix(targetMbpn, targetProcessLocation, targetPreProductionLot);
			int lotSize = targetPreProductionLot.getLotSize();
			int lastSeq = findMbpnSequence(startProductIdPrefix, productType, seqLen);
			return createProducts(targetMbpn, targetPreProductionLot,startProductIdPrefix,lotSize,lastSeq,seqLen );
		}

		return new ArrayList<ProductStampingSequence>();
	}
	
	
	public List<ProductStampingSequence> createStampingSequenceList(Mbpn targetMbpn, String targetSpecCode, String targetProcessLocation, PreProductionLot targetPreProductionLot,ReplicateScheduleProperty2 bean, String subAssembleIdRule)  {

		String productType = bean.getProductType().get(targetProcessLocation);
		
		String partName = "";
		String subAssembleIdRules[] = subAssembleIdRule.split(",");

		if(subAssembleIdRules.length<2) {
			return null;
		}
		
		String prefix = subAssembleIdRules[0];
		
		if(prefix.startsWith("PREFIX_")) {
			partName = prefix.split("_")[1];
		} else {
			return null;
		}
		
		String seqLenMap = subAssembleIdRules[subAssembleIdRules.length-1];
		String seqLen = "";
		if(seqLenMap.startsWith("SEQ_")) {
			seqLen = seqLenMap.split("_")[1];
		} else {
			return null;
		}
		
		if(targetMbpn == null) {
			targetMbpn = mbpnDao.findByKey(targetSpecCode);
		}
		
		if(targetMbpn != null) {
			String startProductIdPrefix = getProductIdPrefix(partName, targetMbpn, targetProcessLocation, targetPreProductionLot, subAssembleIdRule);
			int lotSize = targetPreProductionLot.getLotSize();
			int lastSeq = findMbpnSequence(startProductIdPrefix, productType, Integer.parseInt(seqLen));
			return createProducts(targetMbpn, targetPreProductionLot,startProductIdPrefix,lotSize,lastSeq,Integer.parseInt(seqLen) );
		}

		return new ArrayList<ProductStampingSequence>();
	}
	
	private List<ProductStampingSequence> createProducts(Mbpn targetMbpn,PreProductionLot targetPreProductionLot,String startProductIdPrefix, int lotSize,int lastSeq, Integer seqLen ){
		List<ProductStampingSequence> pssList = new ArrayList<ProductStampingSequence>();
		
			String startProductId = "";
			for(int i=0; i<lotSize; i++) {
				String productId = startProductIdPrefix+StringUtils.leftPad(lastSeq+"", seqLen,"0");
				if (i == 0) startProductId = productId; 
				MbpnProduct mbpnProduct = new MbpnProduct();
				mbpnProduct.setProductId(productId);
				mbpnProduct.setCurrentProductSpecCode(targetMbpn.getProductSpecCode());
				mbpnProduct.setCurrentOrderNo(targetPreProductionLot.getProductionLot());
				getDao(MbpnProductDao.class).save(mbpnProduct);

				ProductStampingSequence pss = createProductStampingSequence(productId,targetPreProductionLot.getProductionLot(),i+1);
				pssList.add(pss);
				lastSeq++;
			}
			getDao(ProductionLotDao.class).updateStartProductId(targetPreProductionLot.getProductionLot(),startProductId);
		
		return pssList;
	}

	public PreProductionLot updateTargetPreProdLot(PreProductionLot targetPreProductionLot, String productId, String targetSpecCode, String targetProcessLocation){
		targetPreProductionLot.setProductSpecCode(targetSpecCode);
		targetPreProductionLot.setStartProductId(productId);
		targetPreProductionLot.setMbpn(targetPreProductionLot.deriveMbpn());
		targetPreProductionLot.setHesColor(targetPreProductionLot.deriveHesColor());
		getDao(PreProductionLotDao.class).save(targetPreProductionLot);
		return targetPreProductionLot;
	}


	
	private int findMbpnSequence(String productIdPrefix, String productType, Integer seqLen) { 
		int maxSequence = 1;
		String lastProductId = getDao(MbpnProductDao.class).findLastProductId(productIdPrefix+"%");
		if (!StringUtils.isBlank(lastProductId)) maxSequence =
				(int)Integer.parseInt(lastProductId.substring(lastProductId.trim().length()-seqLen, lastProductId.length()).trim())+1;		
		return maxSequence;
	}

	private ProductStampingSequence createProductStampingSequence(String Product_id,String productLot,int seq) {
		ProductStampingSequenceId productStampingSequeneId	=	new ProductStampingSequenceId ();
		productStampingSequeneId.setProductID(Product_id);
		productStampingSequeneId.setProductionLot(productLot);
		ProductStampingSequence	productStampingSequence = new ProductStampingSequence ();
		productStampingSequence.setId(productStampingSequeneId);
		productStampingSequence.setSendStatus(0);
		productStampingSequence.setStampingSequenceNumber(seq);
		return productStampingSequence;
	}

	private String getProductIdPrefix(Mbpn targetMbpn, String targetProcessLocation, PreProductionLot targetPreProductionLot) {
		Map<String, String> subAssembleIdRuleMap = new HashMap<String, String>();

			
		subAssembleIdRuleMap = productStampingSeqGenPropertyBean.getSubAssembleIdRule(String.class);

		ProductionLot prodLot = getDao(ProductionLotDao.class).findByKey( targetPreProductionLot.getProductionLot());
		String targetSpecCode = targetMbpn.getProductSpecCode();
		String partName = getMbpnPartName(targetSpecCode);
		String startProductIdPrefix = "";
		if (StringUtils.isEmpty(partName)) return "";
		
		
		if (subAssembleIdRuleMap.containsKey(partName)){  
			String[] subAssembleItRule = subAssembleIdRuleMap.get(partName).split(",");
			startProductIdPrefix=createProductPrefix(subAssembleItRule,partName,targetPreProductionLot,prodLot, targetMbpn,false);
			if(StringUtils.isEmpty(startProductIdPrefix)){
				String subAssembleIdRule =  subAssembleIdRuleMap.get(partName);
				if(!StringUtils.isEmpty(subAssembleIdRule)) startProductIdPrefix = subAssembleIdRule;
			}
		}
		
		return startProductIdPrefix;
	}
	
	private String getProductIdPrefix(String partName, Mbpn targetMbpn, String targetProcessLocation, PreProductionLot targetPreProductionLot, String subAssembleIdRules) {
		Map<String, String> subAssembleIdRuleMap = new HashMap<String, String>();

		String[] subAssembleItRule = subAssembleIdRules.split(",");

		String targetSpecCode = targetMbpn.getProductSpecCode();
		
		String hesColor = targetPreProductionLot.deriveHesColor();

		ProductionLot prodLot = getDao(ProductionLotDao.class).findByKey( targetPreProductionLot.getProductionLot());
		
		String startProductIdPrefix = "";
		 
			startProductIdPrefix=createProductPrefix(subAssembleItRule,partName,targetPreProductionLot,prodLot, targetMbpn, true);
			if(StringUtils.isEmpty(startProductIdPrefix)){
				String subAssembleIdRule =  subAssembleIdRuleMap.get(partName);
				if(!StringUtils.isEmpty(subAssembleIdRule)) startProductIdPrefix = subAssembleIdRule;
			}
		return startProductIdPrefix;
	}

	private String getMbpnSubstring(String startProductIdPrefix,
			String targetSpecCode, String ruleName) {
		if (StringUtils.isEmpty(targetSpecCode))
			return "";

		String[] ruleTokens = ruleName.split(":");
		if (ruleTokens.length == 2) {
			int pos = Integer.parseInt(ruleTokens[0].substring(ruleTokens[0]
					.length() - 1));
			int len = Integer.parseInt(ruleTokens[1]);
			if (StringUtils.isEmpty(targetSpecCode))

				return StringUtils.leftPad("", len, " "); 
			startProductIdPrefix = startProductIdPrefix
					+ targetSpecCode.substring(pos, len);
		} else {
			startProductIdPrefix = startProductIdPrefix + targetSpecCode;
		}
		return startProductIdPrefix;

	}

	public List<ProductStampingSequence> replicateStampingSequence(Map<String, List<ProductStampingSequence>> sourceProductStampingSequenceMap, String targetProductionLot, PreProductionLot sourcePreProductionLot, PreProductionLot targetPreProductionLot ){
		List<ProductStampingSequence> sourceProductStampingSequences = null;
		ProductStampingSequence sourceProductStampingSequence = null;
		ProductStampingSequence targetProductStampingSequence = null;
		sourceProductStampingSequences = sourceProductStampingSequenceMap.get(sourcePreProductionLot.getProductionLot());
		List<ProductStampingSequence> targetProductStampingSequences = new ArrayList<ProductStampingSequence>();
		for (int k = 0; k < sourceProductStampingSequences.size(); k++) {
			sourceProductStampingSequence = sourceProductStampingSequences.get(k);
			targetProductStampingSequence = new ProductStampingSequence();
			BeanUtils.copyProperties(sourceProductStampingSequence, targetProductStampingSequence);
			targetProductStampingSequence.setProductionLot(targetProductionLot);
			targetProductStampingSequences.add(targetProductStampingSequence);
			getLogger().info("In REPLICATE SOURCE_PLAN_CODE:" + sourcePreProductionLot + ", TARGET_PLAN_CODE: "+targetPreProductionLot+ 
					" and Starting ID:"+ sourceProductStampingSequences.get(0).getId().getProductID());    

		}
		return targetProductStampingSequences;
	}

	private String getProductionDate(String productiondate,boolean shortDate) {
		String strmonth = productiondate.substring(5, 6);
		if (shortDate) {
			if ( productiondate.substring(4, 6).equals("10")) strmonth = "A";
			if ( productiondate.substring(4, 6).equals("11")) strmonth = "B";
			if ( productiondate.substring(4, 6).equals("12")) strmonth = "C";
		} else return productiondate.substring(2,8);

		return productiondate.substring(2, 4)+strmonth+productiondate.substring(6, 8);
	}

	private String getMbpnPartName(String targetSpecCode) {
		String mbpnMainNo = targetSpecCode.substring(0, 5);
		String[] mbpnPartEnum = productStampingSeqGenPropertyBean.getMbpnPartEnum().split(",");
		Map<String, String> mainNoMap = new HashMap<String, String>();

		if (null == mbpnPartEnum || mbpnPartEnum.length == 0) return "";
		for (int i = 0;i<mbpnPartEnum.length;i++) {
			mainNoMap = productStampingSeqGenPropertyBean.getMbpnPartPrefix(String.class);
			if ( null == mainNoMap || mainNoMap.size() == 0) return "";
			if (mainNoMap.containsKey(mbpnPartEnum[i])) {
				String mbpnMainNoLst = mainNoMap.get(mbpnPartEnum[i]);
				if (mbpnMainNoLst.indexOf(mbpnMainNo) != -1 ) return  mbpnPartEnum[i];
			}
		}
		return "";
	}
	
	private String createProductPrefix(String[] subAssembleItRule,String partname, PreProductionLot targetPreProductionLot,ProductionLot prodLot,Mbpn targetMbpn, boolean ignoreFirstAndLast )
	{
		String hesColor = targetPreProductionLot.deriveHesColor();
		String targetSpecCode = targetMbpn.getProductSpecCode();
		String startProductIdPrefix = "";
		boolean matchFound= false;
		int startIndex=0; int endIndex = subAssembleItRule.length;
		if(ignoreFirstAndLast) {
			startIndex=1;endIndex= endIndex-1;
		}
		for(int i = startIndex;i < endIndex; i++) {
			matchFound= false;
			if (subAssembleItRule[i].equals("partName")){	startProductIdPrefix = startProductIdPrefix+partname;matchFound= true;}
			if (subAssembleItRule[i].equals("LineNum1")){	startProductIdPrefix = startProductIdPrefix+targetPreProductionLot.getLineNo().substring(1, 2);matchFound= true;}
			if (subAssembleItRule[i].equals("LineNum2")){	startProductIdPrefix = startProductIdPrefix+targetPreProductionLot.getLineNo();matchFound= true;}
			if (subAssembleItRule[i].contains("mbpnMain")){   startProductIdPrefix = getMbpnSubstring(startProductIdPrefix,targetSpecCode.substring(0, 5),subAssembleItRule[i]);matchFound= true;}
			if (subAssembleItRule[i].contains("mbpnClass")){   startProductIdPrefix = getMbpnSubstring(startProductIdPrefix,targetSpecCode.substring(5, 8),subAssembleItRule[i]);matchFound= true; }         
			if (subAssembleItRule[i].equals("mbpnPrototype1")){   startProductIdPrefix = startProductIdPrefix+targetSpecCode.substring(8, 9);matchFound= true; }        
			if (subAssembleItRule[i].contains("mbpnType")){  startProductIdPrefix = getMbpnSubstring(startProductIdPrefix,targetSpecCode.substring(9, 13),subAssembleItRule[i]);matchFound= true; }             
			if (subAssembleItRule[i].contains("mbpnSupplementary")){   startProductIdPrefix = getMbpnSubstring(startProductIdPrefix,targetSpecCode.substring(13, 15),subAssembleItRule[i]);matchFound= true; }        
			if (subAssembleItRule[i].contains("mbpnTarget")){   startProductIdPrefix = getMbpnSubstring(startProductIdPrefix,targetSpecCode.substring(15, 17),subAssembleItRule[i]);matchFound= true;   }     
			if (subAssembleItRule[i].contains("mbpnHesColor")){   startProductIdPrefix = getMbpnSubstring(startProductIdPrefix,hesColor,subAssembleItRule[i]);matchFound= true;  }
			if (subAssembleItRule[i].contains("mbpnMaskId")){   startProductIdPrefix = startProductIdPrefix+targetMbpn.getMaskId();matchFound= true; }
			if(prodLot != null){
				if (subAssembleItRule[i].equals("productionYear2")){	startProductIdPrefix = startProductIdPrefix+prodLot.getProductionDate().toString().substring(2, 4);matchFound= true;}
				if (subAssembleItRule[i].equals("productionYear4")){	startProductIdPrefix = startProductIdPrefix+prodLot.getProductionDate().toString().substring(0, 4);matchFound= true;}
				if (subAssembleItRule[i].equals("productionDate5")){	startProductIdPrefix = startProductIdPrefix+getProductionDate(prodLot.getProductionDate().toString().replaceAll("-", ""),true);matchFound= true;}
				if (subAssembleItRule[i].equals("productionDate6")){	startProductIdPrefix = startProductIdPrefix+getProductionDate(prodLot.getProductionDate().toString().replaceAll("-", ""),false);matchFound= true;}
			}else{
				if (subAssembleItRule[i].equals("productionYear2")){	startProductIdPrefix = startProductIdPrefix+targetPreProductionLot.getLotNumber().substring(2, 4);matchFound= true;}
				if (subAssembleItRule[i].equals("productionYear4")){	startProductIdPrefix = startProductIdPrefix+targetPreProductionLot.getLotNumber().substring(0, 4);matchFound= true;}
				if (subAssembleItRule[i].equals("productionDate5")){	startProductIdPrefix = startProductIdPrefix+getProductionDate(targetPreProductionLot.getLotNumber(),true);matchFound= true;}
				if (subAssembleItRule[i].equals("productionDate6")){	startProductIdPrefix = startProductIdPrefix+getProductionDate(targetPreProductionLot.getLotNumber(),false);matchFound= true;}
			}
			if (!matchFound){startProductIdPrefix = startProductIdPrefix+subAssembleItRule[i];}
		}
		
	
		return startProductIdPrefix; 
	}
}
