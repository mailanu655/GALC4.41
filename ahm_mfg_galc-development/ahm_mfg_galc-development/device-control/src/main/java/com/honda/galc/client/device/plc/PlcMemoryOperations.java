package com.honda.galc.client.device.plc;

public enum PlcMemoryOperations {
	READ(1, "read"),
	WRITE(2, "write");
	
	private String _operation;
	private int _value = 1;
	
	private PlcMemoryOperations(int value, String operation) {
		_value = value;
		_operation = operation;
	}
	
	public String getOperation() {
		return _operation;
	}
	
	public int getValue() {
		return _value;
	}
}
