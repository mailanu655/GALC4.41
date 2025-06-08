package com.honda.galc.util;

import java.util.HashMap;
import java.util.Map;

/**
* <h3>Class description</h3>
* <h4>Description</h4>
* Simple implementation of the variable substitutor.<br/>
* It hides variable source but provides with and ability to add variables<br/>
* to the source
* <h4>Usage and Example</h4>
* <pre>
* SimpleVarSubstitutor varSubstitutor = new SimpleVarSubstitutor();
* 
* varSubstitutor.add("ENGINE_SN", "R18A11400001");
* 
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
public class SimpleVarSubstitutor extends AbstractVarSubstitutor {

	/**
	 * Variable source - Map from variable names to variable values
	 */
	protected Map<String, Object> source = new HashMap<String, Object>();
	
	/**
	 * Consturtor
	 * 
	 */
	public SimpleVarSubstitutor() {
		super();
	}

	/**
	 * Adds variable to variable substitutor
	 * 
	 * @param key - variable name
	 * @param value - variable value
	 */
	public void add(String key, Object value) {
		source.put(key, value);
	}

	/** 
	 * Returns value from variable Map source
	 * 
	 * @see com.honda.global.galc.common.util.AbstractVarSubstitutor#getValue(java.lang.String)
	 */
	@Override
	protected Object getValue(String key) {
		return source.get(key);
	}

}
