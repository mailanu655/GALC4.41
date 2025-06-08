package com.honda.galc.system.oif.svc.common;

import java.util.ArrayList;
import java.util.List;

public class OifRecordValues {
	private List<String> names = null;
	private List<List<Object>> values = new ArrayList<List<Object>>();
	
	/**
	 * @return
	 * @see java.util.List#isEmpty()
	 */
	public boolean isEmpty() {
		return values.isEmpty();
	}

	/**
	 */
	public OifRecordValues() {
	}

	/**
	 * @param names the names to set
	 */
	public void setNames(List<String> names) {
		this.names = names;
	}

	/**
	 * @return the names
	 */
	public List<String> getNames() {
		return names;
	}

	/**
	 * @return the values
	 */
	public List<List<Object>> getValues() {
		return values;
	}

	/**
	 * @param o
	 * @see java.util.List#add(java.lang.Object)
	 */
	public void addRecord(List<Object> o) {
		values.add(o);
	}
	
	/**
	 * @return
	 * @see java.util.List#size()
	 */
	public int size() {
		return values.size();
	}

	public boolean isNamesDefined() {
		return names != null && names.size() > 0;
	}
	
	public String recordToString(List<Object> rec) {
		if(isNamesDefined()) {
			StringBuilder sb = new StringBuilder("[");
			int index = 0;
			for (String name : names) {
				if(index >= rec.size()) {
					break;
				}
				sb.append(name).append(": ").append(rec.get(index++)).append(", ");
			}
			sb.setLength(sb.length() - 2);
			return sb.append("]").toString();
		} else {
			return rec.toString();
		}
	}
	
	public String recordToString(int recIndex) {
		return recordToString(values.get(recIndex));
	}
	
	public Object getRecordValueByName(List<Object> record, String name) {
		int index = names.indexOf(name);
		if(index >= 0) {
			return record.get(index);
		} else {
			return null;
		}
	}
	
	public void setRecordValueByName(List<Object> record, String name, Object value) {
		int index = names.indexOf(name);
		
		if(index < 0) {
			names.add(name);
			index = names.size() - 1;
		}
		
		if(index < record.size()) {
			record.set(index, value);				
		} else {
			// Assumes that only one element should be added
			record.add(value);
		}
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	public String getName(int index) {
		if(names.size() > index) {
			return names.get(index);
		} else {
			return "???";
		}
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	public List<Object> getValue(int index) {
		return values.get(index);
	}

	public Object getRecordValueByName(int ix, String name) {
		int index = names.indexOf(name);
		List<Object> record = this.values.get(ix);
		
		if(index >= 0) {
			return record.get(index);
		} else {
			return null;
		}
	}

	public void setRecordValueByName(int ix, String name, Object value) {
		int index = names.indexOf(name);
		
		if(index < 0) {
			names.add(name);
			index = names.size() - 1;
		}
		
		List<Object> record = this.values.get(ix);
		
		if(index < record.size()) {
			record.set(index, value);				
		} else {
			// Assumes that only one element should be added
			record.add(value);
		}
	}
	
}
