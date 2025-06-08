
package com.honda.galc.entity.enumtype;

/**
 *
 * @author Wade Pei <br>
 * @date   Oct 22, 2013
 */
public enum OrderResponseCode {

	OK((short)1, "OK"), ALREADY_REQUESTED((short)2, "Already Requested Order Number"), EXPECTED_SKIPPED((short)3, "Expected Order Number Skipped"), UNKOWN((short)4, "Unknown Order Number");

	private short id;
	private String message;

	private OrderResponseCode(short id, String message) {
		this.id=id;
		this.message = message;
	}

	public short getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}
	
}
