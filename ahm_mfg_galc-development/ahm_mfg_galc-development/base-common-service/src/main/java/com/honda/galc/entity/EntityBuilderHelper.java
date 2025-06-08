package com.honda.galc.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.enumtype.IdEnum;
import com.honda.galc.util.CommonUtil;

/**
 * 
 * <h3>EntityBuilderHelper</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> EntityBuilderHelper description </p>
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
public class EntityBuilderHelper {
	
	public static HashMap<String, Object> parseArgs(String property) {
		HashMap<String, Object> args = new LinkedHashMap<String, Object>();
		
		try {
			String parms = property.substring(property.indexOf("(") + 1, property.lastIndexOf(")"));
			String testStr = parms;
			String[] split = testStr.split(Delimiter.COMMA);
			for (String exp : split) {
				if (StringUtils.isEmpty(StringUtils.trimToEmpty(exp)))
					continue;
				String[] parmsArray = exp.split(Delimiter.EQUALS_SIGN, 2);
				args.put(StringUtils.trim(parmsArray[0]), StringUtils.trim(parmsArray[1]));
			}
		} catch (Exception e) {
			Logger.getLogger().error(e, " Exception to parse arguments!");
		}
		return args;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T convert(String str, Class<T> parmType) throws Exception {

    	if(parmType.isAssignableFrom(Integer.class) || parmType == int.class){
    		if(str!= null && str.contains(Delimiter.DOT)) str=str.substring(0, str.lastIndexOf(Delimiter.DOT));
    		return (T)(str != null ? Integer.valueOf(str) : Integer.valueOf(0));
    	} else if(parmType.isAssignableFrom(Float.class) || parmType == float.class){
    		return (T)(str != null ? Float.valueOf(str) : Float.valueOf(0));
    	} else if(parmType.isAssignableFrom(String.class)){
    		return (T)str;
    	} else if(parmType.isAssignableFrom(Short.class) || parmType == short.class){
    		if(str != null && str.contains(Delimiter.DOT)) str=str.substring(0, str.lastIndexOf(Delimiter.DOT));
    		return (T)(str != null ? Short.valueOf(str) : Short.valueOf("0")); 
    	} else if(parmType.isAssignableFrom(Double.class) || parmType == double.class){
    		return (T)(str != null ? Double.valueOf(str) : Double.valueOf(0));
    	}else if(parmType.isAssignableFrom(Boolean.class) || parmType == boolean.class){
    		return (T)(str != null ? Boolean.valueOf(str) : Boolean.FALSE);
    	}else if(Timestamp.class.isAssignableFrom(parmType)){
    		return (T)CommonUtil.convertTimeStamp(str);
    	}else if(Date.class.isAssignableFrom(parmType)){
    		return (T)CommonUtil.convertDate(str);
    	}else if(IdEnum.class.isAssignableFrom(parmType)){
    		return (T)(str != null ? Enum.valueOf((Class<? extends Enum>)parmType, str) : null);
    	}

    	Logger.getLogger().warn("WARN: data type is not support:" + parmType + " string:" + str);
		return (T)str;
	}
		
}
