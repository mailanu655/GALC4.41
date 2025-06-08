package com.honda.galc.entity;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;

/**
 * 
 * <h3>EntityBuildMethodSpec</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> EntityBuildMethodSpec description </p>
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
 * <TD>Feb 23, 2015</TD>
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
 * @since Feb 23, 2015
 */
public class EntityBuildMethodSpec {
	enum Method_Keys{Class,Params};
	enum Method_Prefix{get, set, is};
	
	String expression;
	String methodName;
	
	HashMap<String, Object> args;
	public EntityBuildMethodSpec(String expression) {
		this.expression = expression;
		init();
	}
	
	private void init() {
		if(StringUtils.isEmpty(expression)) {
			throw new TaskException("Invalid Method Invocation:" + expression);
		} else {
			methodName = expression.contains("(") ? expression.substring(0, expression.indexOf("(")).trim() : expression;
			args = EntityBuilderHelper.parseArgs(expression);
		}
	}

	public String getMethodName() {
		return methodName;
	}

	public String getClassName() {
		return StringUtils.trimToEmpty((String)args.get(Method_Keys.Class.name()));
	}

	public String getParams() {
		return (String)args.get(Method_Keys.Params.name());
	}
	
	public String[] getParamsArray() {
		return ((String)args.get(Method_Keys.Params.name())).split(Delimiter.COMMA);
	}

	public Class<?> getClazz() {
		if(StringUtils.isEmpty(getClassName())) return null;
		
		try {
			return Class.forName(getClassName());
		} catch (Exception e) {
			Logger.getLogger().warn(e, " Exception to find class:", getClassName());
		}
		
		return null;
	}

	public String getNameToken() {
		
		for(Method_Prefix mp : Method_Prefix.values()){
			if(getMethodName().startsWith(mp.name()))
				return getMethodName().replaceFirst(mp.name(),""); 
		}
		
		
		return getMethodName();

	}
		
		
}
