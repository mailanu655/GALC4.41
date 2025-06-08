package com.honda.galc.client.dc.event;

import com.honda.galc.client.dc.enumtype.DataCollectionResultEventType;
import com.honda.galc.client.product.ProcessEvent;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * @date Jun 18, 2014
 */
public class DataCollectionResultEvent extends ProcessEvent {

	private DataCollectionResultEventType type;
	private InputData inputData;
	private String message;
	
	public DataCollectionResultEvent(DataCollectionResultEventType type, String message) {
		this.type = type;
		this.message = message;
	}
	
	public DataCollectionResultEvent(DataCollectionResultEventType type, MCOperationRevision operation) {
		this.type = type;
		this.operation = operation;
	}
	
	public DataCollectionResultEvent(DataCollectionResultEventType type, MCOperationRevision operation, InputData inputData) {
		this(type, operation);
		this.inputData = inputData;
	}
	
	public DataCollectionResultEventType getType() {
		return type;
	}
	
	public void setType(DataCollectionResultEventType type) {
		this.type = type;
	}
	
	public InputData getInputData() {
		return inputData;
	}
	
	public void setInputData(InputData inputData) {
		this.inputData = inputData;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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
		DataCollectionResultEvent other = (DataCollectionResultEvent) obj;
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

