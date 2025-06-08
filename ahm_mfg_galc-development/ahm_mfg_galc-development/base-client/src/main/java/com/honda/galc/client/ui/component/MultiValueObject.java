package com.honda.galc.client.ui.component;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * <h3>PreProductionLotData Class description</h3>
 * <p> PreProductionLotData description </p>
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
 * @author Jeffray Huang<br>
 * Feb 26, 2013
 *
 *
 */
public class MultiValueObject<T> implements TableRow{
	private T keyObject;
	private List<Object> values;
	
	
	public MultiValueObject(){
		
	}
	
	public MultiValueObject(List<?> values) {
		for(Object obj :values) {
			getValues().add(obj);
		}
	}
	
	public MultiValueObject(Object... values) {
		for(Object value: values) {
			getValues().add(value);
		}
	}
	
	public MultiValueObject(T keyObject,List<?> values) {
		this(values);
		this.keyObject = keyObject;
	}

	public Object getValue(int i){
		if(values == null || i < 0 || i >= values.size()) return null;
		return values.get(i);
	}
	
	public void setValue(int i,Object obj){
		if(values == null || i < 0 || i >= values.size()) return;
		values.set(i,obj);
	}

	public T getKeyObject() {
		return keyObject;
	}

	public void setKeyObject(T keyObject) {
		this.keyObject = keyObject;
	}

	public List<Object> getValues() {
		if(values == null) values = new ArrayList<Object>();
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}
	
	public void add(Object value) {
		getValues().add(value);
	}
	
	public int getSize() {
		return values == null ? 0: values.size();
	}
	
}
