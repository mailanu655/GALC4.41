package com.honda.galc.entity;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;

/**
 * <h3>ProcessPart</h3>
 * <h4>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Aug.19, 2009</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author Paul Chou
 */

public class FileFieldDef {
	public enum FieldType{
		K /** Key **/, 
		F /**Field**/, 
		A /**Build Attribute**/, 
		C /**Constant**/,
		M /**Method Invocation Spec**/,
		P /**Property**/, 
		X /**Expression**/}
	
	private String name;
	private String defStr;
	private FieldType type;
	private int start;
	private int length;
	private String expression;
	private EntityBuildMethodSpec methodSpec;
	
	public FileFieldDef(String name, String defStr) throws Exception{
		this.defStr = defStr;
		this.name = name;
		init();
	}

	private void init() throws Exception {
		String[] split = defStr.split("\\|");

		type = FieldType.valueOf(StringUtils.trimToEmpty(split[0]));
		if(type == FieldType.F || type == FieldType.K){
			if(split.length < 3) throw new Exception("Invalid File Field Defination!");
			start = Integer.parseInt(StringUtils.trimToEmpty(split[1]));
			length = Integer.parseInt(StringUtils.trimToEmpty(split[2]));
		} else {
			if(split.length < 2) throw new Exception("Invalid File Field Defination!");
			expression = split[1];
		}
		
	}
	
	public String getFieldValue(String str){

		return str.substring(start, getEnd());

	}

	// Getters && Setters
	public String getDefStr() {
		return defStr;
	}

	public String getName() {
		return name;
	}

	public FieldType getType() {
		return type;
	}

	public int getStart() {
		return start;
	}

	public int getLength() {
		return length;
	}

	public int getEnd() {
		return start + length;
	}

	public boolean isExpression() {
		
		return type == FieldType.X;
	}

	public String getExpression() {
		return expression;
	}

	@Override
	public String toString() {
		
		return this.type != FieldType.X ? 
				(getName() + " type:" + getType().name() + " start:" + getStart() + " end:" + getEnd()) :
			    (getName() + " type:" + getType().name() + " : " + getExpression());
	}

	public boolean isField() {
		return type == FieldType.F || type == FieldType.K;
	}

	public boolean isProperty() {
		return type == FieldType.P;
	}

	public boolean isBuildAttribute() {
		return type == FieldType.A;
	}

	public boolean isConstant() {
		
		return type == FieldType.C;
	}

	public boolean isKey() {
		
		return type == FieldType.K;
	}

	public boolean isMethodInvocation() {
		return type == FieldType.M;
	}
	
	public EntityBuildMethodSpec getMethodSpec() {
		if(isMethodInvocation() && methodSpec == null){
			try {
				methodSpec = new EntityBuildMethodSpec(expression);
			} catch (Exception e) {
				Logger.getLogger().error(e, "Exception to create method invocation spec for " + name);
			}
		}
		return methodSpec;
	}

	public boolean isProperty(String methodName) {
		if(StringUtils.isEmpty(expression)) return false;
		
		StringBuilder sb  = new StringBuilder("get");
		for(String s : expression.split(Delimiter.UNDERSCORE)){
			sb.append(StringUtils.capitalize(StringUtils.lowerCase(s)));
		}
		return methodName.equals(sb.toString()) || methodName.equals(sb.replace(0, 3, "is").toString());
	}
		
}

