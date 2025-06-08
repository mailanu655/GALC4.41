package com.honda.galc.entity.product;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.constant.Delimiter;
import com.honda.galc.data.PrintAttribute;

/**
 * 
 * <h3>ProductSpec</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductSpec description </p>
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
 * @author Paul Chou
 * Mar 26, 2010
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@MappedSuperclass()
public abstract class ProductSpec extends BaseProductSpec{
	private static final long serialVersionUID = 1L;
	
	public static final String WILDCARD = "*";
	
	@Column(name = "MODEL_YEAR_CODE")
    private String modelYearCode;
	
	@Column(name = "MODEL_CODE")
	private String modelCode;

	@Column(name = "MODEL_TYPE_CODE")
	private String modelTypeCode;

	@Column(name = "MODEL_OPTION_CODE")
	private String modelOptionCode;

	@Column(name = "MODEL_YEAR_DESCRIPTION")
	private String modelYearDescription;
	
	public ProductSpec() {
		super();
	}

	public String getModelCode() {
		return StringUtils.stripEnd(this.modelCode, null);
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	@PrintAttribute
	public String getModelYearCode() {
		return StringUtils.stripEnd(this.modelYearCode, null);
	}

	public void setModelYearCode(String modelYearCode) {
		this.modelYearCode = modelYearCode;
	}

	@PrintAttribute
	public String getModelTypeCode() {
		return StringUtils.stripEnd(this.modelTypeCode, null);
	}

	public void setModelTypeCode(String modelTypeCode) {
		this.modelTypeCode = modelTypeCode;
	}

	public String getModelOptionCode() {
		return StringUtils.stripEnd(this.modelOptionCode, null);
	}

	public void setModelOptionCode(String modelOptionCode) {
		this.modelOptionCode = modelOptionCode;
	}

	@PrintAttribute
	public String getModelYearDescription() {
		return StringUtils.trim(this.modelYearDescription);
	}

	public void setModelYearDescription(String modelYearDescription) {
		this.modelYearDescription = modelYearDescription;
	}
	
	public abstract String getExtColorCode();
	public abstract String getIntColorCode();
	
	abstract public int getProductNoPrefixLength();
	
	public static String extractModelYearCode(String productSpecCode) {
		return StringUtils.substring(productSpecCode,0,1);
	}
	
	public static String extractModelCode(String productSpecCode) {
		return StringUtils.stripEnd(StringUtils.substring(productSpecCode,1,4)," ");
	}
	
	public static String extractModelTypeCode(String productSpecCode) {
		return StringUtils.stripEnd(StringUtils.substring(productSpecCode,4,7)," ");
	}
	
	public static String extractModelOptionCode(String productSpecCode) {
		return StringUtils.stripEnd(StringUtils.substring(productSpecCode,7,10)," ");
	}
	
	public static String extractExtColorCode(String productSpecCode) {
		return StringUtils.stripEnd(StringUtils.substring(productSpecCode,10,20)," ");
	}
	
	public static String extractIntColorCode(String productSpecCode) {
		return StringUtils.stripEnd(StringUtils.substring(productSpecCode,20,22)," ");
	}
	
	public static String excludeToModelYearCode(String productSpecCode) {
		return productSpecCode.substring(0, 1);
	}
	
	public static String excludeToModelCode(String productSpecCode) {
		return productSpecCode.substring(0, 4);
	}
	
	public static String excludeToModelTypeCode(String productSpecCode) {
		return productSpecCode.substring(0, 7);
	}
	
	public static String excludeToModelOptionCode(String productSpecCode) {
		return productSpecCode.substring(0, 10);
	}
	
	public static String excludeToExtColorCode(String productSpecCode) {
		return productSpecCode.substring(0, 20);
	}
	
	public static String padModelCode(String modelCode) {
		return StringUtils.rightPad(modelCode, 3, " ");
	}
	
	public static String padModelTypeCode(String modelTypeCode) {
		return StringUtils.rightPad(modelTypeCode, 3, " ");
	}
	
	public static String padModelOptionCode(String modelOptionCode) {
		return StringUtils.rightPad(modelOptionCode, 3, " ");
	}
	
	public static String padExtColorCode(String extColorCode) {
		return StringUtils.rightPad(extColorCode, 10, " ");
	}
	
	public static String padIntColorCode(String intColorCode) {
		return StringUtils.rightPad(intColorCode, 2, " ");
	}
	
	public static String trimColorCode(String productSpecCode) {
		return StringUtils.trim(StringUtils.substring(productSpecCode,0,10));
	}
	
	public static String trimWildcard(String productSpecCode) {
		String[] items = StringUtils.split(productSpecCode.substring(1), "*%");
		if(items == null || items.length == 0) return productSpecCode.substring(0, 1);
		return productSpecCode.substring(0, 1)+items[0];
	}
	
	public static List<String> trimWildcard(List<String> specCodes) {
		List<String> items = new ArrayList<String>();
		for(String specCode : specCodes)
			items.add(trimWildcard(specCode));
		
		return items;
	}
	
	public static boolean isWildCard(String code) {
		return WILDCARD.equals(StringUtils.trim(code));
	}
	
	public static <T extends ProductSpec> ProductSpec findProductSpec(String specCode, List<T> list){
		for(ProductSpec spec : list) {
			if(spec.getProductSpecCode().equals(specCode)) 
				return spec;
		}
		return null;
	}

	public String getBoundaryMarkRequired(){
		return Delimiter.SPACE;
	}
	

}
