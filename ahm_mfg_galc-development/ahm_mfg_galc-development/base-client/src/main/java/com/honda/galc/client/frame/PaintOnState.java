package com.honda.galc.client.frame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.KeyValue;

/**
 * 
 * <h3>PaintOnState</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PaintOnState description </p>
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
 * <TD>Nov 2, 2018</TD>
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
 * @since Nov 2, 2018
 */
public class PaintOnState {
	private HashMap<String, KeyValue<String, String>> state = new HashMap<String, KeyValue<String, String>>();
	List<String> fields = new ArrayList<String>();
	String lastErrorField;
	
	public PaintOnState(List<String> fields) {
		super();
		this.fields = fields;
	}

	public String getValue(String key) {
		return state.get(key).getKey();
	}

	public void reset() {
		state.clear();
	}
	
	public boolean  change(String key, KeyValue<String, String> result) {
		state.put(key, result);
		if(!StringUtils.isEmpty(result.getValue())) {
			lastErrorField = key;
			return true;
		} else 
			return isLastErrorField(key);
	}
	
	public boolean isComplete() {
		for(String s : fields)
			if(!state.containsKey(s) || !StringUtils.isEmpty(state.get(s).getValue()))
					return false;
			
		return true;
	}
	
	public boolean isLastErrorField(String key) {
		return key.equals(lastErrorField);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(String key : fields) {
			if(sb.length() > 0) sb.append(" ");
			if((!state.containsKey(key)))
				 sb.append(key).append(":").append("null");
			else 
			     sb.append(key).append(":").append(state.get(key).getValue().equals(StringUtils.EMPTY) ? "OK" : (state.get(key).getValue()));
			
		}
		return sb.toString();
	}

	public boolean getStatus(String key) {
		return state.containsKey(key) && StringUtils.EMPTY.equals(state.get(key).getValue());
	}
	
	public boolean isAssociation() {
		return isComplete() && fields.size() > 1;
	}

}
