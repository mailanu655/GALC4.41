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
public class ProductSpecData {
    
    private List<? extends ProductSpec> productSpecs = new ArrayList<ProductSpec>();
    
    private String productType;
    
    public ProductSpecData(String productType){
    	this.productType = productType;
    	loadProductSpec(productType);
    }
    
    @SuppressWarnings("unchecked")
	public void loadProductSpec(String productType) {
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
    	
    	if (productSpecs != null) {
			for (ProductSpec mto : productSpecs) {
				if (mto != null && mto.getModelYearCode() != null)
					modelYears.add(mto.getModelYearCode());
			}
		}
		return sort(modelYears);
    }
    
    public List<String> getModelCodes() {
    	Set<String> modelCodes = new HashSet<String>();
    	
    	if (productSpecs != null) {
			for (ProductSpec mto : productSpecs) {
				if (mto != null && mto.getModelCode() != null)
					modelCodes.add(mto.getModelCode());
			}
		}
		return sort(modelCodes);
    }
    
    public List<String> getModelTypeCodes() {
    	Set<String> modelTypes = new HashSet<String>();
    	
    	if (productSpecs != null) {
			for (ProductSpec mto : productSpecs) {
				if (mto != null && mto.getModelTypeCode() != null)
					modelTypes.add(mto.getModelTypeCode());
			}
		}
		return sort(modelTypes);
    }
    
    public List<String> getModelOptionCodes() {
    	Set<String> modelOptionCodes = new HashSet<String>();
    	if (productSpecs != null) {
			for (ProductSpec mto : productSpecs) {
				if (mto != null && mto.getModelOptionCode() != null)
					modelOptionCodes.add(mto.getModelOptionCode());
			}
		}
		return sort(modelOptionCodes);
    }
    
    public List<String> getModelCodes(String modelYearCode) {
        
        Set<String> modelCodes = new HashSet<String>();
        if (productSpecs != null) {
			for (ProductSpec mto : productSpecs) {
				if (mto.getModelYearCode() != null && modelYearCode.trim().equals(mto.getModelYearCode().trim())
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
        			modelYearCode.trim().equals(mto.getModelYearCode().trim()) &&
        			mto.getModelCode() != null &&   
        			modelCode.trim().equals(mto.getModelCode().trim())&&
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
        			modelYearCode.trim().equals(mto.getModelYearCode().trim()) &&
        			mto.getModelCode() != null &&
        			modelCode.trim().equals(mto.getModelCode().trim()) &&
        			mto.getModelTypeCode() != null &&
        			types.contains(mto.getModelTypeCode()) &&
        			mto.getModelOptionCode() != null)
        		optionCodes.add(mto.getModelOptionCode().trim());
            
         }
        
        return new SortedArrayList<String>(optionCodes);
 
    }
    
    public List<String> getModelExtColorCodes(String modelYearCode,String modelCode,Object[] modelTypeCodes,Object[] modelOptionCodes){
    	Set<String> exitColorCodes = new HashSet<String>();
    	List<Object> types = Arrays.asList(modelTypeCodes);
    	List<Object> options = Arrays.asList(modelOptionCodes);
    	
    	for(ProductSpec mto: productSpecs) {
            if(modelYearCode.equals(mto.getModelYearCode()) &&
                            modelCode.equals(mto.getModelCode()) &&
                            types.contains(mto.getModelTypeCode()) &&
                            options.contains(mto.getModelOptionCode()))
            	exitColorCodes.add(mto.getExtColorCode());
            exitColorCodes.add("*");
         }
        
        return new SortedArrayList<String>(exitColorCodes);
    }
    
    public List<String> getModelIntColorCodes(String modelYearCode,String modelCode,Object[] modelTypeCodes,Object[] modelOptionCodes,Object[] modelExtColorCodes){
    	Set<String> intColorCodes = new HashSet<String>();
    	List<Object> types = Arrays.asList(modelTypeCodes);
    	List<Object> options = Arrays.asList(modelOptionCodes);
    	List<Object> extColorCodes = Arrays.asList(modelExtColorCodes);
    	
    	for(ProductSpec mto: productSpecs) {
            if(modelYearCode.equals(mto.getModelYearCode()) &&
                            modelCode.equals(mto.getModelCode()) &&
                            types.contains(mto.getModelTypeCode()) &&
                            options.contains(mto.getModelOptionCode()) &&
                            extColorCodes.contains(mto.getExtColorCode()))
            	intColorCodes.add(mto.getIntColorCode());
            intColorCodes.add("*");
         }
        
        return new SortedArrayList<String>(intColorCodes);
    }
    
    
    public List<String> getProductSpecData(String modelYearCode, String modelCode, Object[] modelTypeCodes, Object[] modelOptionCodes, Object[] extColorCodes, Object[] intColorCodes) {
    	
        List<String> specCodes = new ArrayList<String>();
        if(modelYearCode == null) return specCodes;
        if(modelCode == null){
        	specCodes.add(modelYearCode + "%");
        	return specCodes;
        }
        String specCode = modelYearCode + ProductSpec.padModelCode(modelCode);
        
        if(modelTypeCodes.length == 0) {
        	specCodes.add(specCode + "%");
        	return specCodes;
        }
        
        for(Object type : modelTypeCodes){
			if(ProductSpec.isWildCard(type.toString())) {
				specCodes.add(specCode + "%");
				continue;
			}

        	for(Object option : modelOptionCodes){ 
        		if(ProductSpec.isWildCard(option.toString())) {
    				String code = specCode 
    					+ ProductSpec.padModelTypeCode(type.toString());
    				
        			if(contains(code)) specCodes.add(code + "%");
        			continue;
    			}
        		for(Object extColorCode : extColorCodes){
        			if(ProductSpec.isWildCard(extColorCode.toString())) {
        				String code = specCode 
        					+ ProductSpec.padModelTypeCode(type.toString())
        					+ ProductSpec.padModelOptionCode(option.toString());
            			if(contains(code)) specCodes.add(code + "%");
            			continue;
        			}
        			for(Object intColorCode : intColorCodes) {
    					String code = specCode 
						+ ProductSpec.padModelTypeCode(type.toString())
						+ ProductSpec.padModelOptionCode(option.toString())
						+ ProductSpec.padExtColorCode(extColorCode.toString());
    					if(contains(code)){
    						if(!ProductSpec.isWildCard(extColorCode.toString())) 
    							code += ProductSpec.padIntColorCode(intColorCode.toString());
    						else code += "%";
    						specCodes.add(code);
    					}
        				
        			}
        		}
        	}
        }
        return specCodes;
    }
        	
    private boolean contains(String specCode) {
    	for(ProductSpec item : productSpecs) {
    		if(item.getProductSpecCode().indexOf(specCode.trim()) >=0) return true;
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
    
    
}
