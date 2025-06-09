package com.honda.mfg.stamp.storage.service.utils;

/**
 * This class encapsulates request and request parameters in the message
 */

public class Command implements CommandInterface, java.io.Serializable {
	private int value = DEFAULT_VALUE;
	private Object[] arguments;
	private int code = 0; // chase

	public Command() {
	}

	public Command(int value) {
		this.value = value;
	}

	public Command(int value, Object[] arguments) {
		this.value = value;
		this.arguments = arguments;
	}

	// chase
	public void setCommandCode(int code) {
		this.code = code;
	}

	public int getCommandCode() {
		return code;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Value = ");
		buffer.append(value);
		buffer.append("\n");
		if (arguments.length > 0) {
			buffer.append("Arguments are...\n");
			for (int i = 0; i < arguments.length; i++) {
				buffer.append("\n");
				buffer.append(arguments[i].toString());
			}
		} else {
			buffer.append("There are no arguments.");
		}
		buffer.append("\n");

		return buffer.toString();
	}

	public String toJSON() {
		StringBuffer buffer = new StringBuffer();

		return buffer.toString();
	}
}
