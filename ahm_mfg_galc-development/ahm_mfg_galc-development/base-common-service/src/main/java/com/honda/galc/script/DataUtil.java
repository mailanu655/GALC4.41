package com.honda.galc.script;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>DataUtil</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DataUtil description </p>
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
 * Apr 13, 2011
 *
 */

public class DataUtil {

	/**
	 * return a list of string split by delimiter
	 * @param str
	 * @param delim
	 * @param returnDelimiters
	 * @return
	 */

	public static List<String> parse(String str, String delim, boolean returnDelimiters) {
		List<String> exprs = new ArrayList<String> ();

		if(str == null)
			return null;
		
	    StringTokenizer parser = new StringTokenizer(
	      str,
	      delim,
	      returnDelimiters
	    );

	    String token = null;
	    while ( parser.hasMoreTokens() ) {
	      token = parser.nextToken(delim);
	      exprs.add(token);	      
	    }
	    return exprs;
	}
	
	/**
	 * Return a list of string with white space removed.
	 * @param str
	 * @param delim
	 * @param returnDelimiters
	 * @return
	 */
	public static List<String> parseAndRemoveWhitespace(String str, String delim, boolean returnDelimiters) {
		List<String> exprs = new ArrayList<String> ();

		if(str == null)
			return null;
		
	    StringTokenizer parser = new StringTokenizer(
	      str,
	      delim,
	      returnDelimiters
	    );

	    String token = null;
	    while ( parser.hasMoreTokens() ) {
	      token = StringUtils.strip(parser.nextToken(delim));
	      if(!StringUtils.isEmpty(token))
	    	  exprs.add(getCleanToken(token));	      
	    }
	    return exprs;
	}
	
	private static String getCleanToken(String token) {
		
		if(token.contains("("))
			token = token.substring(token.lastIndexOf("(") +1);
		
		if(token.contains("{"))
			token = token.substring(token.lastIndexOf("{") +1);
		
		return token;

			
	}
	
	public static <E extends Enum<E>> boolean isInEnum(String value, Class<E> enumClass) {
		 for (E e : enumClass.getEnumConstants()) {
			 if(e.name().equals(value)) { return true; }
				}
			 return false;
			}
}