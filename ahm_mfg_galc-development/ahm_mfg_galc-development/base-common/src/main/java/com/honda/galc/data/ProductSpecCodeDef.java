package com.honda.galc.data;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>ProductSpecCodeDef</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductSpecCodeDef description </p>
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
 * <TD>Apr 4, 2012</TD>
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
 * @since Apr 4, 2012
 */

 
public enum ProductSpecCodeDef {
	YEAR(1,1, 'Y',"([1-9A-Y&&[^IOQU]])|(\\~){0,1}+"),
	MODEL(2,3, 'M',"[0-9A-Z]{0,3}+"),
	TYPE(3,3,'T', "[0-9A-Z]{0,3}+"),
	OPTION(4,3,'O', "[0-9A-Z]{0,3}+"),
	EXT_COLOR(5,10,'C', "[0-9A-Z]{0,10}+"),
	INT_COLOR(6,3,'I', "[0-9A-Z]{0,2}+");
	
	public static final String MODEL_YEAR_WILDCARD = "~";
	public static final String NONE = "NONE";
	private int sequenceNumber;
	private int length;
	private char code;
	private String regex;
	
	private int startPosition = -1;
	ProductSpecCodeDef(int sequenceNumber, int length, char code, String regex){
		this.sequenceNumber = sequenceNumber;
		this.length = length;
		this.code = code;
		this.regex = regex;
	}
	
	public int getLength() {
		return length;
	}

	public int getStartPosition(){
		if (startPosition == -1) {
			startPosition = 0;
			for (ProductSpecCodeDef e : ProductSpecCodeDef.values()) {
				if (e.ordinal() < this.ordinal())
					startPosition += e.getLength();
			}
		}
		return startPosition;
	}
	
	public String getValue(String specCode){
		if(specCode.length() < getStartPosition()) return "";
		
		if((specCode.length() - getStartPosition()) > this.getLength())
			return specCode.substring(getStartPosition(), getStartPosition() + this.getLength()).trim();
		else
			return specCode.substring(getStartPosition()).trim();
	}
	
	public String format(String value){
		if(StringUtils.isEmpty(value)) value="";
		else{
			value = value.trim();
			value = value.length() > this.getLength() ? value.substring(0, getLength()) : value;
		}
		return String.format("%1$-" + getLength() + "s", value);
	}
	
	
	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public char getCode() {
		return code;
	}

	public boolean validate(String value) {
		switch(this){
		case YEAR:
		case MODEL:
			return value != null && (value.trim().length() == this.getLength() && value.matches(regex));
		default:
			return (StringUtils.isEmpty(value) )|| (value.trim().length() <= this.getLength() && value.matches(regex));
		}

	}
	
	//utility functions
	public static boolean validateProductSpecCode(String specCode){
		
		int previousSeq = 0;
		for(int i = 0; i < specCode.length(); i++){
			int currentSeq = valueOfCode(specCode.charAt(i)).getSequenceNumber();
			
			if(previousSeq >= currentSeq) return false;
			
			previousSeq = currentSeq;
		}
		
		return true;
	}

	public static ProductSpecCodeDef valueOfCode(char code) {
		for(ProductSpecCodeDef pscd : ProductSpecCodeDef.values())
			if(code == pscd.getCode()) return pscd;
		
		return null;
	}
	
	public static String getModelYearDescription(String yearCode){
		return MODEL_YEAR_WILDCARD.equals(yearCode) ? NONE : yearCode;
	}

	public static String fromModelYearDescription(String desc) {
		
		return NONE.equals(desc) ? MODEL_YEAR_WILDCARD : desc;
	}

}
