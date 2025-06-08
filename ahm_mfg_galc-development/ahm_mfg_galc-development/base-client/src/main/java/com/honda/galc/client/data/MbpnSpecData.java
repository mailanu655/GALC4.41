package com.honda.galc.client.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.MbpnDef;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductSpecUtil;

/**
 * 
 * <h3>MbpnSpecData</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> MbpnSpecData description </p>
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
 * <TD>Jan 30, 2015</TD>
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
 * @since Jan 30, 2015
 */
public class MbpnSpecData implements SpecData {
	private List<Mbpn> mbpnSpecs = new ArrayList<Mbpn>();
	private String productType;

	public MbpnSpecData(String productType){
		this.productType = productType;
		loadProductSpec();
	}

	@SuppressWarnings("unchecked")
	private void loadProductSpec() {
		
		mbpnSpecs = (List<Mbpn>)ProductTypeUtil.getProductSpecDao(ProductTypeCatalog.getProductType(productType).toString()).findAllProductSpecCodesOnly(productType);
	}
	
	public List<String> getMainNos(){
		Set<String> mainNos = new HashSet<String>();
		if (mbpnSpecs != null) {
			for (Mbpn mbpn : mbpnSpecs) {
				if (mbpn != null && mbpn.getMainNo() != null && !mbpn.getMainNo().trim().equals(""))
					mainNos.add(mbpn.getMainNo());
			}
		}
		
		return sortAndAdd(mainNos, "*");
	}
	
	public List<String> getClassNos(String mainNo){
		Set<String> classNos = new HashSet<String>();
		if (mbpnSpecs != null) {
			for (Mbpn mbpn : mbpnSpecs) {
				if (mbpn != null && (mainNo.equals("*") || mainNo.trim().equals(mbpn.getMainNo().trim())) && 
						mbpn.getClassNo() != null && !mbpn.getClassNo().trim().equals("")) {
					classNos.add(mbpn.getClassNo());
				}
			}
		}
		
		return sortAndAdd(classNos, "*");
	}
	
	public List<String> getProtoTypeCodes(String mainNo, String classNo){
		Set<String> protoTypeCodes = new HashSet<String>();
		if (mbpnSpecs != null) {
			for (Mbpn mbpn : mbpnSpecs) {
				if (mbpn != null  && (mainNo.equals("*") || mainNo.trim().equals(mbpn.getMainNo().trim()))
						&& mbpn.getClassNo() !=null && (classNo.equals("*") || mbpn.getClassNo().trim().equals(classNo.trim()))
						&& mbpn.getPrototypeCode() != null && !mbpn.getPrototypeCode().trim().equals(""))
					protoTypeCodes.add(mbpn.getPrototypeCode());
			}
		}
		
		return sortAndAdd(protoTypeCodes, "*");
	}
	
	public List<String> getTypeNos(String mainNo, String classNo, Object[] protoTypeCodes){
		Set<String> typeNos = new HashSet<String>();
		List<Object> protoTypeList = Arrays.asList(protoTypeCodes);
		if (mbpnSpecs != null) {
			for (Mbpn mbpn : mbpnSpecs) {
				if (mbpn != null  && (mainNo.equals("*") || mainNo.trim().equals(mbpn.getMainNo().trim()))
						&& mbpn.getClassNo() !=null && (classNo.equals("*") || mbpn.getClassNo().trim().equals(classNo.trim()))
						&& mbpn.getPrototypeCode() != null && (protoTypeList.contains(mbpn.getPrototypeCode()) || protoTypeList.contains("*"))
						&& mbpn.getTypeNo() != null && !mbpn.getTypeNo().trim().equals(""))
					
					typeNos.add(mbpn.getTypeNo().trim());
			}
		}
		
		return sortAndAdd(typeNos, "*");
	}
	
	public List<String> getSupplementaryNos(String mainNo, String classNo, Object[] protoTypeCodes, Object[] typeNos){
		Set<String> supplementaryNos = new HashSet<String>();
		List<Object> protoTypeList = Arrays.asList(protoTypeCodes);
		List<Object> typeNoList = Arrays.asList(typeNos);
		if (mbpnSpecs != null) {
			for (Mbpn mbpn : mbpnSpecs) {
				if (mbpn != null  && (mainNo.equals("*") || mainNo.trim().equals(mbpn.getMainNo().trim()))
						&& mbpn.getClassNo() !=null && (classNo.equals("*") || mbpn.getClassNo().trim().equals(classNo.trim()))
						&& mbpn.getPrototypeCode() != null &&  (protoTypeList.contains(mbpn.getPrototypeCode()) || protoTypeList.contains("*"))
						&& mbpn.getTypeNo() != null && (typeNoList.contains(mbpn.getTypeNo())|| typeNoList.contains("*"))
						&& mbpn.getSupplementaryNo() != null && !mbpn.getSupplementaryNo().trim().equals(""))
					
					supplementaryNos.add(mbpn.getSupplementaryNo().trim());
			}
		}
		
		return sortAndAdd(supplementaryNos, "*");
	}
	
	public List<String> getTargetNos(String mainNo, String classNo, Object[] protoTypeCodes, Object[] typeNos, Object[] supplementaryNos){
		Set<String> targetNos = new HashSet<String>();
		List<Object> protoTypeList = Arrays.asList(protoTypeCodes);
		List<Object> typeNoList = Arrays.asList(typeNos);
		List<Object> supplementaryNoList = Arrays.asList(supplementaryNos);
		if (mbpnSpecs != null) {
			for (Mbpn mbpn : mbpnSpecs) {
				if (mbpn != null  && (mainNo.equals("*") || mainNo.trim().equals(mbpn.getMainNo().trim()))
						&& mbpn.getClassNo() !=null && (classNo.equals("*") || mbpn.getClassNo().trim().equals(classNo.trim()))
						&& mbpn.getPrototypeCode() != null &&  (protoTypeList.contains(mbpn.getPrototypeCode()) || protoTypeList.contains("*"))
						&& mbpn.getTypeNo() != null && (typeNoList.contains(mbpn.getTypeNo())|| typeNoList.contains("*"))
						&& mbpn.getSupplementaryNo() != null && (supplementaryNoList.contains(mbpn.getSupplementaryNo()) || supplementaryNoList.contains("*"))
						&& mbpn.getTargetNo() != null && !mbpn.getTargetNo().trim().equals(""))
					
					targetNos.add(mbpn.getTargetNo().trim());
			}
		}
		
		return sortAndAdd(targetNos, "*");
	}
	
	public List<String> getHesColors(String mainNo, String classNo, Object[] protoTypeCodes, Object[] typeNos, 
			Object[] supplementaryNos,	Object[] tagetNos){
		Set<String> hesColors = new HashSet<String>();
		List<Object> protoTypeList = Arrays.asList(protoTypeCodes);
		List<Object> typeNoList = Arrays.asList(typeNos);
		List<Object> supplementaryNoList = Arrays.asList(supplementaryNos);
		List<Object> tagetNoList = Arrays.asList(tagetNos);
		
		if (mbpnSpecs != null) {
			for (Mbpn mbpn : mbpnSpecs) {
				if (mbpn != null  && (mainNo.equals("*") || mainNo.trim().equals(mbpn.getMainNo().trim()))
						&& mbpn.getClassNo() !=null && (classNo.equals("*") || mbpn.getClassNo().trim().equals(classNo.trim()))
						&& mbpn.getPrototypeCode() != null &&  (protoTypeList.contains(mbpn.getPrototypeCode()) || protoTypeList.contains("*"))
						&& mbpn.getTypeNo() != null && (typeNoList.contains(mbpn.getTypeNo())|| typeNoList.contains("*"))
						&& mbpn.getSupplementaryNo() != null && (supplementaryNoList.contains(mbpn.getSupplementaryNo()) || supplementaryNoList.contains("*"))
						&& mbpn.getTargetNo() != null && (tagetNoList.contains(mbpn.getTargetNo()) || tagetNoList.contains("*"))
						&& mbpn.getHesColor() != null && !mbpn.getHesColor().trim().equals(""))
					
					hesColors.add(mbpn.getHesColor().trim());
			}
		}
		
		return sortAndAdd(hesColors, "*");
	}
	

	public List<String> sortAndAdd(Set<String> items, String first) {

		List<String> sorted = new ArrayList<String>(items);
		Collections.sort(sorted);
		if(!StringUtils.isEmpty(first)) sorted.add(0, first);
		return sorted;

	}
	
	public boolean isValid(String wildCardSpecCode) {
		for (Mbpn mbpn : getMbpnSpecs()) {
			if (ProductSpecUtil.matchMbpn(mbpn.getProductSpecCode(), wildCardSpecCode)) {
				return true;
			}
		}
		return false;
	}

	// -------- getters & setters -----------
	public List<Mbpn> getMbpnSpecs() {
		return mbpnSpecs;
	}

	public void setMbpnSpecs(List<Mbpn> mbpnSpecs) {
		this.mbpnSpecs = mbpnSpecs;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public List<String> getProductSpecData(String mainNo, String classNo, Object[] protoTypeCodes,
			Object[] typeNos, Object[] supplNos, Object[] targetNos, Object[] hesColors) {
		String baseSpecCode = "";
		List<String> specCodes = new ArrayList<String>();
		if(mainNo == null) return specCodes;
		if(classNo == null){
			specCodes.add(mainNo + "%");
			return specCodes;
		}
		baseSpecCode = MbpnDef.MAIN_NO.format(mainNo) + MbpnDef.CLASS_NO.format(convertWildCard(classNo));
		if(protoTypeCodes.length == 0) {
			specCodes.add(baseSpecCode + "%");
			return specCodes;
		}
		
		for(Object ptType : protoTypeCodes){
        	for(Object typeNo : typeNos){
        		for(Object suppl : supplNos){
        			for(Object targetNo : targetNos){
        				for(Object hesClr : hesColors){
	        				String specCode = baseSpecCode;
	        				specCode += MbpnDef.PROTOTYPE_CODE.format(convertWildCard(ptType.toString()));
	        				specCode += MbpnDef.TYPE_NO.format(convertWildCard(typeNo.toString()));
	        				specCode += MbpnDef.SUPPLEMENTARY_NO.format(convertWildCard(suppl.toString()));
	        				specCode += MbpnDef.TARGET_NO.format(convertWildCard(targetNo.toString()));
	        				specCode += MbpnDef.SPACE.format(" ") + MbpnDef.HES_COLOR.format(convertWildCard(hesClr.toString()));
	        				if(isValid(specCode)) {
	        					specCodes.add(StringUtils.trim(specCode) + "%");
	        				}
        				}
        			}
        		}
        	}
        }
		return specCodes;
	}
	
	private String convertWildCard(String code) {
    	return ProductSpec.isWildCard(code) ? "" : code;
    }
	
	public boolean isProductType(String productType) {
	   	return StringUtils.equalsIgnoreCase(this.productType, productType); 
	 }
	
	
}
