package com.honda.galc.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author Subu Kathiresan
 * @date Feb 19, 2015
 */
public class CustomToStringStyle extends ToStringStyle {
    private static final long serialVersionUID = 1L;
   
    protected void appendDetail(StringBuffer buffer, String fieldName, Object value) {
         if (value instanceof String) {
             value = StringUtils.trim((String)value);
         }
         // TODO add custom formatting for additional types
         
         buffer.append(value);
     }
}