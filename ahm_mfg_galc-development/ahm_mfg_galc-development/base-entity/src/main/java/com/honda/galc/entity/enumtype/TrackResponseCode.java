package com.honda.galc.entity.enumtype;
/**
* values of this class indicate the track result.
* @author Jiamei Li <br>
* @date   Dec 12, 2013
*/
public enum TrackResponseCode {
	
	OK((short)1, "OK"), ERROR((short)2, "ERROR") ;

	private short id;
	private String message;

	private TrackResponseCode(short id, String message) {
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
