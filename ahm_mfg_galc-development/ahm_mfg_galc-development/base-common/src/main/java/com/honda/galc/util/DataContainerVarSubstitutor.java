package com.honda.galc.util;

import com.honda.galc.data.DataContainer;



/**
* <h3>Class description</h3>
* <h4>Description</h4>
* DataContainer implementation of the variable substitutor.<br/>
* <h4>Usage and Example</h4>
* <pre>
* DataContainerVarSubstitutor varSubstitutor 
* 		= new DataContainerVarSubstitutor(dataContainer);
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
public class DataContainerVarSubstitutor extends AbstractVarSubstitutor {
	protected DataContainer source;
    
    /**
     * Constructs new variable substitutor with given DataContainer as source of varaibles
     * 
     * @param dc - DataContainer with variables for substitution
     */
    public DataContainerVarSubstitutor(DataContainer dc) {
    	super();
    	this.source = dc;
    }
    
    /**
     * Provides with concrete implementation of the method
     * @see com.honda.global.galc.common.util.AbstractVarSubstitutor#getValue(String)
	 * @param key - varibale placeholder key
	 * @return value of the variable
	 */
	@Override
	protected Object getValue(String key) {
		return this.source.get(key);
	}

}

