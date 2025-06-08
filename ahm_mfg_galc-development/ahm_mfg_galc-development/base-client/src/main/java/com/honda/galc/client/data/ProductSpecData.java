package com.honda.galc.client.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductSpecUtil;
import com.honda.galc.util.SortedArrayList;

/**
 * 
 * <h3>ProductSpecData Class description</h3>
 * <p> ProductSpecData description </p>
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
 * Apr 16, 2012
 *
 *
 */
public class ProductSpecData implements SpecData {
    
    private List<? extends ProductSpec> productSpecs = new ArrayList<ProductSpec>();
    
    private String productType;
    
    public ProductSpecData(String productType){
    	this.productType = productType;
    	loadProductSpec();
    }
    
    @SuppressWarnings("unchecked")
	public void loadProductSpec() {
    	productSpecs = (List<? extends ProductSpec>)ProductTypeUtil.getProductSpecDao(productType).findAllProductSpecCodesOnly(productType.toString());
    }
    
    public String getProductType() {
    	return productType;
    }
    
    public boolean isProductType(String productType) {
    	return StringUtils.equalsIgnoreCase(this.productType, productType); 
    }
 
    public List<String> getModelYearCodes() {
    	Set<String> modelYears = new HashSet<String>();
    	modelYears.add("*");
    	if (productSpecs != null) {
			for (ProductSpec mto : productSpecs) {
				if (mto != null && mto.getModelYearCode() != null)
					modelYears.add(mto.getModelYearCode());
			}
		}
		return sort(modelYears);
    }
    
    public List<String> getModelCodes(String modelYearCode) {
        
        Set<String> modelCodes = new HashSet<String>();
        modelCodes.add("*");
        if (productSpecs != null) {
			for (ProductSpec mto : productSpecs) {
				if (mto.getModelYearCode() != null &&  (modelYearCode.equals("*") || modelYearCode.trim().equals(mto.getModelYearCode().trim()))
						&& mto.getModelCode() != null)
					modelCodes.add(mto.getModelCode());
			}
		}
		return sort(modelCodes);
        
    }
    
    public List<String> getModelTypeCodes(String modelYearCode, String modelCode) {
    	List<String> modelTypes = new ArrayList<String>();
    	modelTypes.add("*");
        Set<String> modelTypeCodes = new HashSet<String>();
        for(ProductSpec mto: productSpecs) {
        	if(mto.getModelYearCode()!= null && 
        			(modelYearCode.equals("*")|| modelYearCode.trim().equals(mto.getModelYearCode().trim())) &&
        			mto.getModelCode() != null &&   
        			(modelCode.equals("*") || modelCode.trim().equals(mto.getModelCode().trim()))&&
        			mto.getModelTypeCode() != null
        	) modelTypeCodes.add(mto.getModelTypeCode());
        }
        modelTypes.addAll(sort(modelTypeCodes));
        return modelTypes;
        
    }
    
    public ProductSpec newProductSpec() {
    	return new EngineSpec();
    }
    
    public List<String> getModelOptionCodes(String modelYearCode,String modelCode,Object[] modelTypeCodes) {
        Set<String> optionCodes = new HashSet<String>();
        List<Object> types = Arrays.asList(modelTypeCodes);
        optionCodes.add("*");
        for(ProductSpec mto: productSpecs) {
        	if(mto.getModelYearCode() != null && 
        			(modelYearCode.equals("*")|| modelYearCode.trim().equals(mto.getModelYearCode().trim()))  &&
        			mto.getModelCode() != null &&
        			(modelCode.equals("*") || modelCode.trim().equals(mto.getModelCode().trim())) &&
        			mto.getModelTypeCode() != null &&
        			(types.contains("*") || types.contains(mto.getModelTypeCode())) &&
        			mto.getModelOptionCode() != null)
        		optionCodes.add(mto.getModelOptionCode().trim());
            
         }
        
        return new SortedArrayList<String>(optionCodes);
 
    }
    
    public List<String> getModelExtColorCodes(String modelYearCode,String modelCode,Object[] modelTypeCodes,Object[] modelOptionCodes){
    	Set<String> exitColorCodes = new HashSet<String>();
    	List<Object> types = Arrays.asList(modelTypeCodes);
    	List<Object> options = Arrays.asList(modelOptionCodes);
    	exitColorCodes.add("*");
    	for(ProductSpec mto: productSpecs) {
            if(mto.getModelYearCode() != null && (modelYearCode.equals("*")|| modelYearCode.trim().equals(mto.getModelYearCode().trim())) &&
            		mto.getModelCode() != null && (modelCode.equals("*") || modelCode.trim().equals(mto.getModelCode().trim())) &&
            				mto.getModelTypeCode() != null && (types.contains("*") || types.contains(mto.getModelTypeCode())) &&
            						mto.getModelOptionCode() != null && (options.contains("*") || options.contains(mto.getModelOptionCode())))
            	exitColorCodes.add(mto.getExtColorCode());
         }
        
        return new SortedArrayList<String>(exitColorCodes);
    }
    
    public List<String> getModelIntColorCodes(String modelYearCode,String modelCode,Object[] modelTypeCodes,Object[] modelOptionCodes,Object[] modelExtColorCodes){
    	Set<String> intColorCodes = new HashSet<String>();
    	List<Object> types = Arrays.asList(modelTypeCodes);
    	List<Object> options = Arrays.asList(modelOptionCodes);
    	List<Object> extColorCodes = Arrays.asList(modelExtColorCodes);
    	intColorCodes.add("*");
    	for(ProductSpec mto: productSpecs) {
            if(mto.getModelYearCode() != null && (modelYearCode.equals("*")|| modelYearCode.trim().equals(mto.getModelYearCode().trim())) &&
            		mto.getModelCode() != null && (modelCode.equals("*") || modelCode.trim().equals(mto.getModelCode().trim()))&&
            				mto.getModelTypeCode() != null && (types.contains("*") || types.contains(mto.getModelTypeCode())) &&
            						mto.getModelOptionCode() != null && (options.contains("*") || options.contains(mto.getModelOptionCode())) &&
                            mto.getExtColorCode() != null &&(extColorCodes.contains("*") || extColorCodes.contains(mto.getExtColorCode())))
            	intColorCodes.add(mto.getIntColorCode());
         }
        
        return new SortedArrayList<String>(intColorCodes);
    }
    
    
    public List<String> getProductSpecData(String modelYearCode, String modelCode, Object[] modelTypeCodes, Object[] modelOptionCodes, Object[] extColorCodes, Object[] intColorCodes) {
    	
    	String baseSpecCode = "";
        List<String> specCodes = new ArrayList<String>();
        if(modelYearCode == null) return specCodes;
        if(modelCode == null){
        	specCodes.add(modelYearCode + "%");
        	return specCodes;
        }
        baseSpecCode = modelYearCode + ProductSpec.padModelCode(convertWildCard(modelCode));
         
        if(modelTypeCodes.length == 0) {
        	specCodes.add(baseSpecCode.toString() + "%");
        	return specCodes;
        }
        
        for(Object type : modelTypeCodes){
        	for(Object option : modelOptionCodes){
        		for(Object extColorCode : extColorCodes){
        			for(Object intColorCode : intColorCodes){
        				String specCode = baseSpecCode;
        				specCode += ProductSpec.padModelTypeCode(convertWildCard(type.toString()));
        				specCode += ProductSpec.padModelOptionCode(convertWildCard(option.toString()));
        				specCode += ProductSpec.padExtColorCode(convertWildCard(extColorCode.toString()));
        				specCode += ProductSpec.padIntColorCode(convertWildCard(intColorCode.toString()));
        				if(isValid(specCode)) {
        					System.out.println("SpecCode - "+ specCode);
        					specCodes.add(StringUtils.trim(specCode) + "%");
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
    
    public boolean isValid(String wildCardSpecCode) {
    	for(ProductSpec productSpec : productSpecs)  {
    		if (ProductSpecUtil.matchProductSpec(productSpec.getProductSpecCode(), wildCardSpecCode)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public List<EngineSpec> getProductSpecData(String modelYearCode,String modelCode,Object[] modelTypeCodes,Object[] modelOptionCodes) {
        List<EngineSpec> productSpecs = new ArrayList<EngineSpec>();
        List<Object> types = Arrays.asList(modelTypeCodes);
        List<Object> options = Arrays.asList(modelOptionCodes);
        for(ProductSpec mto: productSpecs) {
            if(modelYearCode.trim().equals(mto.getModelYearCode().trim()) &&
                            modelCode.trim().equals(mto.getModelCode().trim()) &&
                            types.contains(mto.getModelTypeCode()) &&
                            options.contains(mto.getModelOptionCode().trim()))
                productSpecs.add((EngineSpec)mto);
         }
        return productSpecs;
    }
    
    public List<String> sort(Set<String> items) {
 
        List<String> sorted = new ArrayList<String>(items);
        Collections.sort(sorted);
        return sorted;
        
    }


	public List<? extends ProductSpec> getProductSpecs() {
		return productSpecs;
	}

	/**
	 * @param selectedModelYearCode
	 * @param selectedModelCode
	 * @return
	 */
	public List<String> getProductSpecData(String modelYearCode,String modelCode) {
        List<String> specCodes = new ArrayList<String>();
        if(modelYearCode == null) return specCodes;
        if(modelCode == null){
        	specCodes.add(modelYearCode + "%");
        	return specCodes;
        }
        String specCode = modelYearCode + ProductSpec.padModelCode(modelCode);
        specCodes.add(specCode +"%");
    	return specCodes;
	}
    
    
}
