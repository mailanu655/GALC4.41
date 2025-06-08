/**
 * 
 */
package com.honda.galc.dto;

import org.apache.commons.lang.StringUtils;

/**
 * @author vf031824
 *
 */
public class ExtRequiredPartDto  implements IDto {
	
	private static final long serialVersionUID = 1L;
	
	@DtoTag(outputName = "PART_NAME")
	private String partName;
	
	@DtoTag()
	private int extRequired;
	
	@DtoTag()
	private int LETRequired;
	
	public ExtRequiredPartDto() {}
	
	public String getPartName() {
		return StringUtils.trim(this.partName);
	}
	
	public void setPartName(String partName) {
		this.partName = partName;
	}	

	public int getExtRequired() {
		return this.extRequired;
	}
	
	public void setExtRequired(int extRequired) {
		this.extRequired = extRequired;
	}
	
	public int getLETRequired() {
		return this.LETRequired;
	}
	
	public void setLETRequired(int LETRequired) {
		this.LETRequired = LETRequired;
	}
}
