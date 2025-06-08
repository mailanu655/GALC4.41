package com.honda.galc.util;

/**
* <h3>Class description</h3>
* <h4>Description</h4>
* Abstract implementation of the variable substitutor.<br/>
* Concrete implementation should provide with getValue() method
* <h4>Usage and Example</h4>
* This class should be subclassed by concrete subclass before it can be used.
* General usage will looke like:
* <pre>
* ... instantiate the class ...
* String result = varSubstitutor.substitue(templateString);
* </pre>
* <h4>Special Notes</h4>
* 
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
* <TD>R.Lasenko</TD>
* <TD>Dec 07, 2007</TD>
* <TD>@RL011</TD>
* <TD>&nbsp;</TD>
* <TD>Initial Creation</TD>
* </TR>
</TABLE>
*/
public abstract class AbstractVarSubstitutor {

	private static final String NULL_STRING = String.valueOf((String)null);
	private static final int INITIAL_CAPACITY = 512;
	protected static final String CLOSE_CURL_BRACKET = "}";
	protected static final String OPEN_CURL_BRACKET = "{";
	protected static final String DEFAULT_MARKER = "$";
	protected static final String START_VAR = DEFAULT_MARKER + OPEN_CURL_BRACKET;
	protected StringBuilder str = new StringBuilder(INITIAL_CAPACITY);
	protected static final String DEF_VALUE_SEP = ":";

	/**
	 * Replaces any occurrences within the string of the form "${key}" with
	 * the value from getValue(key) abstract method.
	 * <p>
	 * Returns the string after performing all substitutions.
	 * <p>
	 * If key contains semicolon (e.g. ${ENGINE_SN:J35A73073701}) then <br>
	 * token following semicolon is used as a default value, <br>
	 * So if key is not known by source, the default value will be used<br>
	 * instead of throwing IllegalArgumentException
	 * 
	 * @throws IllegalArgumentException
	 *             if the input param references a variable which is not known
	 *             to the specified source.
	 * @param param - template containg occurences of variable placeholders (e.g. ${ENGINE_SN})
	 * @return result with the variable placeholders replaced with corresponding values
	 * 
	 */
	public String substitute(String param) {
		param = expand(param);
	    return param;
	}

	/**
	 * Replaces any occurrences within the string of the form "${key}" with
	 * the value from getValue(key) abstract method.
	 * <p>
	 * Returns the string after performing all substitutions.
	 * <p>
	 * If key contains semicolon (e.g. ${ENGINE_SN:J35A73073701}) then <br>
	 * token following semicolon is used as a default value, <br>
	 * So if key is not known by source, the default value will be used<br>
	 * instead of throwing IllegalArgumentException
	 * 
	 * @throws IllegalArgumentException
	 *             if the input param references a variable which is not known
	 *             to the specified source.
	 * 
	 */
	protected String expand(String argStr) {
		if(argStr == null) {
			return null;
		}
		str.setLength(0);
		str.append(argStr);
		
	    String startMark = START_VAR;
	    int markLen = startMark.length();
	    
	    int index = 0;
	    for(;;)
	    {
	        index = str.indexOf(startMark, index);
	        if (index == -1)
	        {
	            return str.toString();
	        }
	        
	        int startIndex = index + markLen;
	        if (startIndex > str.length())
	        {
	            throw new IllegalArgumentException(
	                "var expression starts at end of string");
	        }
	        
	        int endIndex = str.indexOf(CLOSE_CURL_BRACKET, index + markLen);
	        if (endIndex == -1)
	        {
	            throw new IllegalArgumentException(
	                "var expression starts but does not end");
	        }
	        
	        String key = str.substring(index + markLen, endIndex);
	        
	        // Check for default value
	        int defValOffset = key.indexOf(DEF_VALUE_SEP);
	        String defValue = null;
	        
	        if(defValOffset > 0)
	        {
	            defValue = key.substring(defValOffset + 1);
	            key = key.substring(0, defValOffset);
	        }
	        
	        String varValue =  String.valueOf(this.getValue(key));
	        if (varValue.equals(NULL_STRING)) {
	            if (defValue != null)
	            {
	            	varValue = defValue;
	            }
	        }
	                   
	        str.replace(index, endIndex + 1, varValue);
	        index += varValue.length();
	    }
	}

	/**
	 * Provides with transaltion from varaible key to value<br/>
	 * It should be implemented by concreate subclass
	 * 
	 * @param key valriable key
	 * @return the value of the variable
	 */
	protected abstract Object getValue(String key);

}
