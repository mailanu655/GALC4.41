/**
 * 
 */
package com.honda.galc.client.dc.event;

import com.honda.galc.client.dc.enumtype.DataCollectionEventType;
import com.honda.galc.client.product.ProcessEvent;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * @date April 15, 2014
 */
public class DataCollectionEvent extends ProcessEvent {

	private DataCollectionEventType type;
	private InputData inputData;
	
	public DataCollectionEvent(DataCollectionEventType type, InputData inputData) {
		this.type = type;
		this.inputData = inputData;
	}
	
	public DataCollectionEvent(DataCollectionEventType type, MCOperationRevision operation, InputData inputData) {
		this.type = type;
		this.operation = operation;
		this.inputData = inputData;
	}
	
	public DataCollectionEventType getType() {
		return type;
	}
	
	public void setType(DataCollectionEventType type) {
		this.type = type;
	}
	
	public InputData getInputData() {
		return inputData;
	}
	
	public void setInputData(InputData inputData) {
		this.inputData = inputData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((inputData == null) ? 0 : inputData.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataCollectionEvent other = (DataCollectionEvent) obj;
		if (inputData == null) {
			if (other.inputData != null)
				return false;
		} else if (!inputData.equals(other.inputData))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override 
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getType(), getInputData());
	}
}
