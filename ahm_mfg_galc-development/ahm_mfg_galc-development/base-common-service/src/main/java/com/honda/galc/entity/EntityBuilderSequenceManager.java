package com.honda.galc.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * <h3>EntityBuilderSequenceManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> EntityBuilderSequenceManager description </p>
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
 * <TD>Feb 24, 2015</TD>
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
 * @since Feb 24, 2015
 */
public class EntityBuilderSequenceManager {
	private Map<String, Map<String, Double>> sequenceMapSpace = new HashMap<String, Map<String,Double>>();
	
	public Map<String, Double> getMap(String name){
		if(sequenceMapSpace.containsKey(name))
			return sequenceMapSpace.get(name);
		else {
			Map<String, Double> newMap = new HashMap<String, Double>();
			sequenceMapSpace.put(name, newMap);
			return newMap;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Double> getMap(Class clazz){
		return getMap(clazz.getSimpleName());
	}

}
