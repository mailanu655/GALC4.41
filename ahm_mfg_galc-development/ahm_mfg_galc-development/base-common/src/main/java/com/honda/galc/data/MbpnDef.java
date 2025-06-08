package com.honda.galc.data;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>MbpnDef</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> MbpnDef description </p>
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
 * <TD>Jul 18, 2014</TD>
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
 * @since Jul 18, 2014
 */
public enum MbpnDef {
	MAIN_NO(1,5, 'M',"\\w{5}"),
	CLASS_NO(2,3, 'C',"[0-9A-Z]{3}+"),
	PROTOTYPE_CODE(3,1,'P',"[A-Z]{1}+"),
	TYPE_NO(4,4,'T', "\\w{4}"),
	SUPPLEMENTARY_NO (5,2,'S', "\\w{2}"),
	TARGET_NO        (6,2,'G', "\\w{2}"),
	SPACE            (7,1,'D',"\\s{1}"), /** always space unless change */
	HES_COLOR(8,11,'H', "\\w{1,11}+"),
	MBPN(-1, 18,'B',"\\w{1,18}");
	
	
	private int startPosition = -1;
	private int sequenceNumber;
	private int length;
	private char code;
	private String regex;
	
	MbpnDef(int sequenceNumber, int length, char code, String regex){
		this.sequenceNumber = sequenceNumber;
		this.length = length;
		this.code = code;
		this.regex = regex;
	}
	
	public int getStartPosition(){
		if (startPosition == -1) {
			startPosition = 0;
			for (MbpnDef e : MbpnDef.values()) {
				if (e.getSequenceNumber() < this.getSequenceNumber() && e.getSequenceNumber() > 0)
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
	
	public boolean validate(String value) {
		return value.trim().length() <= this.getLength() && 
		      (this == SPACE ? value.matches(regex) : StringUtils.trim(value).matches(regex));
	}
	
	public String format(String value){
		if(StringUtils.isEmpty(value)) value="";
		else{
			value = value.trim();
			value = value.length() > this.getLength() ? value.substring(0, getLength()) : value;
		}
		return String.format("%1$-" + getLength() + "s", value);
	}
	
	public static MbpnDef valueof(char code){
		for (MbpnDef e : MbpnDef.values()) {
			if(e.getCode() == code)
				return e;
		}
		
		return null;
	}
	
	// --------- getters & setters ----------
	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public int getLength() {
		return length;
	}

	public char getCode() {
		return code;
	}

	public String getRegex() {
		return regex;
	}

	public static int getStartPosition(String modelMappingCode) {
		return valueof(modelMappingCode.charAt(0)).getStartPosition();
		
	}

	public static int getEndPosition(String modelMappingCode) {
		MbpnDef lastToken = valueof(modelMappingCode.charAt(modelMappingCode.length() -1));
		return lastToken.getStartPosition() + lastToken.getLength();
	}
	
}
